package com.company.tests;

import com.company.annotations.*;

public class SimpleTestSuite {

    @BeforeSuite
    public static void beforeSuite(){
        System.out.println("beforeSuite");
    }

    @BeforeTest
    public static void beforeTest(){
        System.out.println("beforeTest");
    }

    @Test(priority = 10)
    public void test1(){
        System.out.println("test1");
    }

    @CsvSource("1, 2")
    @Test(priority = 9)
    public void test2(int a, int b){
        System.out.printf("test2: a = %d, b = %d%n", a, b);
    }

    @Test//(priority = 5)
    public void test3(){
        System.out.println("test3");
    }

    @Test(priority = 4)
    public void test4(){
        System.out.println("test4");
    }

    @Test(priority = 1)
    public void test5(){
        System.out.println("test5");
    }

    @AfterTest
    public static void afterTest(){
        System.out.println("afterTest");
    }

    @AfterSuite
    public static void afterSuite(){
        System.out.println("afterSuite");
    }
}
