package com.w1sh.wave.core.condition;

public class SingletonNotPresentCondition extends Condition {

    private Class<?> targetClass;
    private String targetName;

    public SingletonNotPresentCondition(Class<?> target) {
        this.targetClass = target;
    }

    public SingletonNotPresentCondition(String targetName) {
        this.targetName = targetName;
    }
}
