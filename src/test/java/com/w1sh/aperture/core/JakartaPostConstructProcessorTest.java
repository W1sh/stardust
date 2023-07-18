package com.w1sh.aperture.core;

import org.junit.jupiter.api.Test;

import javax.annotation.PostConstruct;

import static org.mockito.Mockito.*;

class JakartaPostConstructProcessorTest {

    private final JakartaPostConstructProcessor processor = new JakartaPostConstructProcessor();

    @Test
    void should_returnInstance_whenProviderOfClassIsRegistered() {
        PostConstructClass spy = spy(new PostConstructClass());
        processor.process(spy);

        verify(spy, times(1)).init();
    }

    private static class PostConstructClass {

        public PostConstructClass(){}

        @PostConstruct
        public void init(){
            System.out.println("Ran post construct");
        }
    }
}