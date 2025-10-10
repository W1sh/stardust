package com.w1sh.stardust;

import org.junit.jupiter.api.Test;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.*;

class JakartaPostConstructInterceptorTest {

    private final JakartaPostConstructInterceptor processor = new JakartaPostConstructInterceptor();

    @Test
    void should_returnInstance_whenProviderOfClassIsRegistered() {
        PostConstructClass spy = spy(new PostConstructClass());
        processor.intercept(spy);

        verify(spy, times(1)).init();
    }

    private static class PostConstructClass {

        public PostConstructClass(){/* Test constructor */}

        @PostConstruct
        public void init(){/* Test method */}
    }
}