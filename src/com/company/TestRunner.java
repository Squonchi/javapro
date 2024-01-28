package com.company;

import com.company.annotations.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class TestRunner {

    public static void runTests(Class c) throws Exception {

        Object classInstance = c.getDeclaredConstructor().newInstance();

        List<Method> methods = List.of(c.getMethods());

        List<Method> beforeSuite = methods.stream()
                .filter(e -> e.isAnnotationPresent(BeforeSuite.class))
                .collect(Collectors.toList());
        List<Method> afterSuite = methods.stream()
                .filter(e -> e.isAnnotationPresent(AfterSuite.class))
                .collect(Collectors.toList());
        List<Method> beforeTest = methods.stream()
                .filter(e -> e.isAnnotationPresent(BeforeTest.class))
                .collect(Collectors.toList());
        List<Method> afterTest = methods.stream()
                .filter(e -> e.isAnnotationPresent(AfterTest.class))
                .collect(Collectors.toList());
        List<Method> test = methods.stream()
                .filter(e -> e.isAnnotationPresent(Test.class))
                .sorted((o1, o2) -> Integer.compare(
                        o2.getAnnotation(Test.class).priority(),
                        o1.getAnnotation(Test.class).priority()))
                .collect(Collectors.toList());

        if (beforeSuite.size() > 1)
            throw new Exception("В классе может быть только 1 метод с аннотацией @BeforeSuite");
        else if (beforeSuite.size() == 1 && !Modifier.isStatic(beforeSuite.get(0).getModifiers()))
            throw new Exception("Аннотация @BeforeSuite применяется только для статических методов");

        if (afterSuite.size() > 1)
            throw new Exception("В классе может быть только 1 метод с аннотацией @AfterSuite");
        else if (afterSuite.size() == 1 && !Modifier.isStatic(afterSuite.get(0).getModifiers()))
            throw new Exception("Аннотация @AfterSuite применяется только для статических методов");

        for (Method method : test) {
            int priority = method.getAnnotation(Test.class).priority();
            if (priority < 1 || priority > 10)
                throw new Exception("Значение поля priority должно быть больше 0 и меньше 11");
            if (Modifier.isStatic(method.getModifiers()))
                throw new Exception("Аннотация @Test применяется только для не статических методов");
        }

        if (beforeSuite.size() == 1)
            beforeSuite.get(0).invoke(null);


        for (Method testMethod : test) {
            for (Method beforeTestMethod : beforeTest)
                beforeTestMethod.invoke(null);

            List<Object> parameters = new ArrayList<>();
            if (testMethod.isAnnotationPresent(CsvSource.class)) {
                String csvSource = testMethod.getAnnotation(CsvSource.class).value();
                List<String> values = List.of(csvSource.split(", "));
                for (int i = 0; i < values.size(); i++) {
                    parameters.add(parseParameter(values.get(i), testMethod.getParameterTypes()[i]));
                }
            }
            testMethod.invoke(classInstance, parameters.toArray());


            for (Method afterTestMethod : afterTest)
                afterTestMethod.invoke(null);
        }


        if (afterSuite.size() == 1)
            afterSuite.get(0).invoke(null);

    }

    private static Object parseParameter(String value, Class type) throws Exception {
        if (String.class.equals(type))
            return value;
        if (boolean.class.equals(type))
            return Boolean.parseBoolean(value);
        if (int.class.equals(type))
            return Integer.parseInt(value);
        if (long.class.equals(type))
            return Long.parseLong(value);
        if (double.class.equals(type))
            return Double.parseDouble(value);
        if (float.class.equals(type))
            return Float.parseFloat(value);
        throw new Exception("Данный тип параметров не поддерживается" + type.getName());
    }
}
