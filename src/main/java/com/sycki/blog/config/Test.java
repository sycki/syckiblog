package com.sycki.blog.config;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by kxdmmr on 2017/8/25.
 */
public class Test {

    public String name;
    public int age;

    public void setName(String name, int age){
        this.name = name;
        this.age = age;
    }

    public String getName(){System.out.println("I am getName()");return this.name;}
    public int getAge(){System.out.println("I am getAge()");return this.age;}

    public static void main(String args[]) throws NoSuchFieldException {
        Enhancer en = new Enhancer();
        en.setSuperclass(Test.class);
        en.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                //System.out.println(o);
                System.out.println(method.getName());
                System.out.println(methodProxy.invokeSuper(o,objects));
                System.out.println(methodProxy.getSuperName());
                return null;
            }
        });
        Test obj = (Test)en.create();
        obj.getName();
        obj.getAge();


    }
}
