package com.w1sh.stardust;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.example.service.MerchantService;
import com.w1sh.stardust.example.service.impl.MerchantServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class SetterInjectionPostConstructInterceptorTest {

    private final ParameterResolver resolver = mock(ParameterResolver.class);

    private final SetterInjectionPostConstructInterceptor interceptor = new SetterInjectionPostConstructInterceptor(resolver);

    @Test
    void should_returnInstanceWithFieldSet_whenSetterIsAnnotatedAndProviderIsPresent() {
        final var service = new MerchantServiceImpl();
        final var spy = spy(new SetterInjectionPostConstructInterceptorTest.SetterPostConstructClass());
        when(resolver.resolve(any())).thenReturn(service);

        assertNull(spy.getMerchantService());
        interceptor.intercept(spy);

        assertNotNull(spy.getMerchantService());
        verify(spy, times(1)).setMerchantService(service);
        verify(resolver, times(1)).resolve(any());
    }

    private static class SetterPostConstructClass {

        private MerchantService merchantService;

        public SetterPostConstructClass() {}

        @Inject
        public void setMerchantService(MerchantService merchantService) {
            this.merchantService = merchantService;
        }

        public MerchantService getMerchantService() {
            return merchantService;
        }
    }

}