package com.w1sh.wave.core;

import com.w1sh.wave.example.service.impl.CircularDependantCalculatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationContextTest {

    private final ClassDefinitionFactory definitionFactory = new SimpleClassDefinitionFactory();
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        this.applicationContext = ApplicationContext.builder()
                .setEnvironment(ApplicationEnvironment.builder().build())
                .setScanner(new GenericComponentScanner())
                .setClassDefinitionFactory(definitionFactory)
                .build();
    }

    @Test
    void test(){
        final Definition definition = definitionFactory.create(CircularDependantCalculatorServiceImpl.class);
        applicationContext.register(definition);
    }
}