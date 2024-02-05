package com.company;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Вася", "Инженер", 100),
                new Employee("Дима", "Инженер", 10),
                new Employee("Даша", "Инженер", 15),
                new Employee("Соня", "Инженер", 71),
                new Employee("Ваня", "Кассир", 50),
                new Employee("Оля", "Уборщик", 30),
                new Employee("Петя", "Уборщик", 20),
                new Employee("Катя", "Кассир", 4)
        );


        System.out.println(deleteDuplicates(List.of(1, 1, 2, 2, 2, 3, 4, 5)));
        System.out.println(thirdLargestInt(List.of(9, 9, 4, 1, 5, 5, 2, 3)));
        System.out.println(thirdLargestUniqInt(List.of(9, 9, 4, 1, 5, 2, 3)));
        System.out.println(threeOldestEngineer(employees));
        System.out.println(averageEngineerAge(employees));
        System.out.println(longestWord(List.of("1", "333", "55555", "4444", "22", "999999999", "7777777")));
        System.out.println(wordsCount("A A AAA A AA AA"));
        System.out.println(sortWords(List.of("C", "D", "B", "A", "ABBA", "BABA")));
        System.out.println(longestWord(new String[]{"C", "D", "B", "A", "ABBA", "BABA"}));
    }

    public static List<Object> deleteDuplicates(List<Object> objects) {
        return objects.stream().distinct().collect(Collectors.toList());
    }

    public static Integer thirdLargestInt(List<Integer> numbers) {
        return numbers.stream().sorted(Collections.reverseOrder()).skip(2).findFirst().get();
    }

    public static Integer thirdLargestUniqInt(List<Integer> numbers) {
        return numbers.stream().distinct().sorted(Collections.reverseOrder()).skip(2).findFirst().get();
    }

    public static List<String> threeOldestEngineer(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.spec.equals("Инженер"))
                .sorted((o1, o2) -> Integer.compare(o2.age, o1.age))
                .map(e -> e.name)
                .limit(3)
                .collect(Collectors.toList());
    }

    public static Integer averageEngineerAge(List<Employee> employees) {
        return employees.stream()
                .filter(e -> e.spec.equals("Инженер"))
                .map(e -> e.age)
                .reduce(0, Integer::sum)
                /
                (int) employees.stream()
                        .filter(e -> e.spec.equals("Инженер"))
                        .count();
    }

    public static String longestWord(List<String> words) {
        return words.stream()
                .sorted((o1, o2) -> Integer.compare(o2.length(), o1.length()))
                .findFirst().get();
    }

    public static Map<String, Long> wordsCount(String words) {
        return Arrays.stream(words.split(" "))
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));
    }

    public static List<String> sortWords(List<String> words) {
        return words.stream()
                .sorted()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
    }

    public static String longestWord(String[] words) {
        return Arrays.stream(words)
                .map(e -> e.split(" "))
                .flatMap(Arrays::stream)
                .sorted((o1, o2) -> Integer.compare(o2.length(), o1.length()))
                .findFirst().get();

    }

    static class Employee {
        String name;
        String spec;
        Integer age;

        public Employee(String name, String spec, Integer age) {
            this.name = name;
            this.spec = spec;
            this.age = age;
        }
    }
}
