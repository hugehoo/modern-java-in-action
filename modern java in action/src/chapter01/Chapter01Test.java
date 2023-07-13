package chapter01;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

public class Chapter01Test {

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals("Green")) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterHeavyApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() >= 100) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterGoodQualityApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getQuality().equals("High")) {
                result.add(apple);
            }
        }
        return result;
    }

    static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
        return inventory.stream()
            .filter(p)
            .collect(toList());
    }

    @Test
    public void test() {
        List<Apple> inventory = new ArrayList<>();
        // lambda 를 사용하나, collection api 를 사용하나 결과는 동일.
        filterApples(inventory, a -> a.getColor().equals("Green"));
        filterApples(inventory, a -> a.getQuality().equals("Good"));
        filterApples(inventory, a -> a.getWeight() >= 100);

        List<Apple> collect = inventory.parallelStream()
            .filter((Apple a) -> a.getWeight() > 100)
            .toList();
    }


}
