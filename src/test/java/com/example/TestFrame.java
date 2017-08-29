package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestFrame {

    private static final Logger log = LoggerFactory.getLogger(TestFrame.class);

    public static void main(String... args) {
        log.info("Welcome to FakeLoad - TestFrame");

        SomeClass someClass = new SomeClass();

        someClass.someMethod();
//        someClass.someOtherMethod();
//        someClass.yetAnotherMethod();

    }



}
