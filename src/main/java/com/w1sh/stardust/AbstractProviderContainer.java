package com.w1sh.stardust;

import com.w1sh.stardust.InvocationInterceptor.InvocationType;
import com.w1sh.stardust.annotation.Module;
import com.w1sh.stardust.annotation.Primary;
import com.w1sh.stardust.annotation.Provide;
import com.w1sh.stardust.exception.ProviderCandidatesException;
import com.w1sh.stardust.naming.DefaultNamingStrategy;
import com.w1sh.stardust.naming.NamingStrategy;
import com.w1sh.stardust.util.Constructors;
import com.w1sh.stardust.util.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

public abstract class AbstractProviderContainer implements ProviderContainer, InterceptorAware, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractProviderContainer.class);

    private final SetValueEnumMap<InvocationType, InvocationInterceptor> interceptors;
    private final ProviderStore providerStore;
    private final NamingStrategy namingStrategy;
    private final ParameterResolver resolver;

    protected AbstractProviderContainer(NamingStrategy namingStrategy) {
        this.namingStrategy = Objects.requireNonNullElseGet(namingStrategy, DefaultNamingStrategy::new);
        this.providerStore = new ProviderStoreImpl();
        this.resolver = new ParameterResolver(this);
        this.interceptors = new SetValueEnumMap<>(InvocationType.class);

        providerStore.register(namingStrategy.generate(this.getClass()), AbstractProviderContainer.class, new SingletonObjectProvider<>(this));
        providerStore.register(namingStrategy.generate(namingStrategy.getClass()), NamingStrategy.class, new SingletonObjectProvider<>(namingStrategy));
        providerStore.register(namingStrategy.generate(providerStore.getClass()), ProviderStore.class, new SingletonObjectProvider<>(providerStore));
        providerStore.register(namingStrategy.generate(resolver.getClass()), ParameterResolver.class, new SingletonObjectProvider<>(resolver));
        logger.trace("Container initialization complete. {} internal classes have been registered.", providerStore.count());
    }

    public static AbstractProviderContainer base() {
        return new DefaultProviderContainer(new DefaultNamingStrategy());
    }

    public void register(Class<?> clazz) {
        Objects.requireNonNull(clazz);
        final var constructor = new ResolvableConstructorImpl<>(Constructors.getInjectConstructor(clazz));
        register(constructor);

        if (clazz.isAnnotationPresent(Module.class)) {
            Object moduleInstance = instance(clazz);
            Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Provide.class))
                    .map(method -> new ResolvableMethodImpl<>(method, moduleInstance))
                    .sorted((Comparator.comparingInt(o -> o.getParameters().size())))
                    .forEach(this::register);
        }
    }

    private void register(ResolvableExecutable<?> executable) {
        Objects.requireNonNull(executable);
        String name = Objects.requireNonNullElse(executable.getName(), namingStrategy.generate(executable.getActualType()));
        logger.debug("Registering provider of class {} with name {}", executable.getActualType().getSimpleName(), name);
        providerStore.register(name, executable.getActualType(), asProvider(executable));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T instance(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        ObjectProvider<?> provider = get(clazz);
        return provider != null ? (T) provider.singletonInstance() : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T instance(String name) {
        Objects.requireNonNull(name);
        ObjectProvider<?> provider = providerStore.get(name);
        return provider != null ? (T) provider.singletonInstance() : null;
    }

    @Override
    public <T> T primaryInstance(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return primaryProvider(clazz).singletonInstance();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ObjectProvider<T> primaryProvider(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        List<? extends ObjectProvider<?>> primaries = providerStore.getAllClasses().stream()
                .filter(clazz::isAssignableFrom)
                .filter(key -> key.isAnnotationPresent(Primary.class))
                .map(providerStore::get)
                .flatMap(Collection::stream)
                .toList();

        if (primaries.isEmpty()) {
            logger.error("Expected 1 primary candidate but found none for class {}", clazz.getSimpleName());
            throw new ProviderCandidatesException(0, clazz);
        } else if (primaries.size() > 1) {
            logger.error("Expected 1 primary candidate but found {} for class {}", primaries.size(), clazz.getSimpleName());
            throw new ProviderCandidatesException(primaries.size(), clazz);
        }
        return (ObjectProvider<T>) primaries.get(0);
    }

    @Override
    public <T> List<T> instances(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return providerStore.get(clazz).stream()
                .map(ObjectProvider::singletonInstance)
                .toList();
    }

    @Override
    public <T> ObjectProvider<T> provider(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return get(clazz);
    }

    @Override
    public <T> ObjectProvider<T> provider(String name) {
        Objects.requireNonNull(name);
        return providerStore.get(name);
    }

    @Override
    public <T> List<ObjectProvider<T>> providers(Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return providerStore.get(clazz);
    }

    @Override
    public <T> boolean contains(Class<T> clazz) {
        return clazz != null && !providerStore.get(clazz).isEmpty();
    }

    @Override
    public boolean contains(String name) {
        return name != null && provider(name) != null;
    }

    public List<Class<?>> getAllAnnotatedWith(Class<? extends Annotation> annotationType) {
        Objects.requireNonNull(annotationType);
        return providerStore.getAllClasses().stream()
                .filter(clazz -> clazz.isAnnotationPresent(annotationType))
                .toList();
    }

    private <T> ObjectProvider<T> get(Class<T> clazz) {
        final var candidates = providerStore.get(clazz);

        if (candidates.size() > 1) {
            logger.error("Expected 1 candidate but found {} for class {}", candidates.size(), clazz.getSimpleName());
            throw new ProviderCandidatesException(candidates.size(), clazz);
        }
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    @SuppressWarnings("unchecked")
    private <T> T asProvider(ResolvableExecutable<?> executable) {
        if (Scope.SINGLETON.equals(executable.getScope())) {
            Object instance = createInstance(executable);
            return (T) new SingletonObjectProvider<>(instance);
        } else {
            return (T) new PrototypeObjectProvider<>(() -> createInstance(executable));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createInstance(ResolvableExecutable<?> executable) {
        Object[] objects = executable.getParameters().stream()
                .map(resolver::resolve)
                .toArray();
        T resolved = (T) executable.resolve(objects);
        interceptors.get(InvocationType.POST_CONSTRUCT).stream()
                .sorted(Comparator.comparing(o -> Types.getPriority(o.getClass())))
                .forEach(invocationInterceptor -> invocationInterceptor.intercept(resolved));
        return resolved;
    }

    @Override
    public void addInterceptor(InvocationInterceptor interceptor) {
        interceptors.put(interceptor.getInterceptorType(), interceptor);
    }

    @Override
    public void removeInterceptor(InvocationInterceptor interceptor) {
        interceptors.remove(interceptor.getInterceptorType(), interceptor);
    }

    @Override
    public void removeAllInterceptors() {
        interceptors.getUnderlyingEnumMap().clear();
    }

    @Override
    public List<InvocationInterceptor> getAllInterceptors() {
        return interceptors.getUnderlyingEnumMap().values()
                .stream()
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public List<InvocationInterceptor> getAllInterceptorsOfType(InvocationType type) {
        return List.copyOf(interceptors.get(type));
    }

    @Override
    public void close() throws Exception {
        logger.debug("Closing provider container");
        List<InvocationInterceptor> preDestroyInterceptors = interceptors.get(InvocationType.PRE_DESTROY).stream()
                .sorted(Comparator.comparing(o -> Types.getPriority(o.getClass())))
                .toList();
        logger.debug("Invoking pre-destroy interceptors on all required providers");
        List<ObjectProvider<?>> providers = providerStore.getAllOrdered();
        Collections.reverse(providers);
        for (ObjectProvider<?> objectProvider : providers) {
            for (Object instance : objectProvider.instances()) {
                for (InvocationInterceptor invocationInterceptor : preDestroyInterceptors) {
                    invocationInterceptor.intercept(instance);
                }
            }
        }
        providerStore.clear();
    }

    public static class DefaultProviderContainer extends AbstractProviderContainer {

        public DefaultProviderContainer(NamingStrategy namingStrategy) {
            super(namingStrategy);
        }
    }
}
