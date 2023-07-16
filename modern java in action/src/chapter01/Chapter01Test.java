package chapter01;

import static chapter01.Color.*;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Chapter01Test {

    List<Apple> 사과박스 = new ArrayList<>();

    @BeforeEach
    void init() {
        사과박스.add(new Apple(RED, 100, "high"));
        사과박스.add(new Apple(RED, 80, "middle"));
        사과박스.add(new Apple(RED, 70, "low"));
        사과박스.add(new Apple(RED, 120, "high"));
        사과박스.add(new Apple(RED, 50, "low"));
        사과박스.add(new Apple(GREEN, 110, "high"));
        사과박스.add(new Apple(RED, 110, "high"));
        사과박스.add(new Apple(GREEN, 80, "low"));
        사과박스.add(new Apple(GREEN, 130, "high"));
    }


    @Test
    public void 고전적_객체지향_코드() {

        List<Apple> 초록사과 = filterGreenApples(사과박스);
        List<Apple> 무거운사과 = filterHeavyApples(사과박스);
        List<Apple> 상급사과 = filterGoodQualityApples(사과박스);

        초록사과.forEach(i -> assertEquals(i.getColor(), GREEN));
        무거운사과.forEach(i -> assertTrue(i.getWeight() >= 100));
        상급사과.forEach(i -> assertEquals(i.getQuality(), "high"));
    }

    @Test
    public void 함수형_프로그래밍_코드() {
        List<Apple> 초록사과 = filterApples(사과박스, a -> a.getColor().equals(GREEN));
        List<Apple> 무거운사과 = filterApples(사과박스, a -> a.getWeight() >= 100);
        List<Apple> 상급사과 = filterApples(사과박스, a -> a.getQuality().equals("Good"));

        초록사과.forEach(i -> assertEquals(i.getColor(), GREEN));
        무거운사과.forEach(i -> assertTrue(i.getWeight() >= 100));
        상급사과.forEach(i -> assertEquals(i.getQuality(), "high"));
    }


    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals(GREEN)) {
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

}
