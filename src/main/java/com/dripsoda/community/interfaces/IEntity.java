package com.dripsoda.community.interfaces;
// T는 IEntity를 반드시 구현하거나 상속해야 한다. 라고 타입을 적어주는 것.
public interface IEntity<T extends IEntity<?>> {

    T clone();

    void copyValuesOf(T t);
}