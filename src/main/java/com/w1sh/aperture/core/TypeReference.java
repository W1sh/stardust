package com.w1sh.aperture.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Class is based on <code>TypeReference</code> class Jackson library which
 * takes ideas from
 * <a href="http://gafter.blogspot.com/2006/12/super-type-tokens.html"
 * >http://gafter.blogspot.com/2006/12/super-type-tokens.html</a>,
 * Additional idea (from a suggestion made in comments of the article)
 * is to require bogus implementation of <code>Comparable</code>
 * (any such generic interface would do, as long as it forces a method
 * with generic type to be implemented).
 * to ensure that a Type argument is indeed given.
 *<p>
 * Usage is by sub-classing: here is one way to instantiate reference
 * to generic type <code>MetadataConditionFactory&lt;?&gt;</code>:
 *<pre>
 *  TypeReference ref = new TypeReference&lt;MetadataConditionFactory&lt;?&gt;&gt;() { };
 *</pre>**/
public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {

    private final Type type;

    protected TypeReference() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    /**
     * The only reason we define this method is to prevent constructing a
     * reference without type information.
     */
    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
