package chapter03;

import static chapter01.Color.*;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chapter01.Apple;
import chapter01.Color;

public class Chapter03Test {

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
    public void PREDICATE_AND_TEST() {
        Predicate<Apple> filterHeavy = apple -> apple.getWeight() >= 100;
        List<Apple> highQualityApples = filterApplesWithHeavyWeight(사과박스, filterHeavy);
        highQualityApples.forEach(a ->
            assertTrue(a.getWeight() >= 100 && a.getQuality().equals("high"))
        );

        Predicate<Apple> filterRedColor = apple -> apple.getColor().equals(RED);
        List<Apple> redApples = filterApplesWithHighQuality(사과박스, filterRedColor);
        redApples.forEach(a ->
            assertTrue(a.getColor().equals(RED) && a.getQuality().equals("high"))
        );
    }

    @Test
    public void PREDICATE_OR_TEST() {
        Predicate<Apple> filterHeavy = apple -> apple.getWeight() >= 100;
        List<Apple> highQualityApples = filterApplesWithRedColor(사과박스, filterHeavy);
        highQualityApples.forEach(a ->
            assertTrue(a.getWeight() >= 100 || a.getColor().equals(RED))
        );
    }

    @Test
    public void Cons() {
        Consumer<Apple> printColor = apple -> System.out.println(apple.getColor());
        사과박스.forEach(apple ->
            printColor
                .andThen(apple1 -> System.out.println(apple1.getWeight()))
                .accept(apple)
        );
    }

    static List<Apple> filterApplesWithRedColor(List<Apple> inventory, Predicate<Apple> p) {
        Predicate<Apple> basicCondition = (apple) -> apple.getColor().equals(Color.RED);
        return inventory.stream()
            .filter(p.or(basicCondition))
            .collect(toList());
    }

    static List<Apple> filterApplesWithHeavyWeight(List<Apple> inventory, Predicate<Apple> p) {
        Predicate<Apple> basicCondition = (apple) -> apple.getWeight() > 100;
        return inventory.stream()
            .filter(p.and(basicCondition))
            .collect(toList());
    }

    static List<Apple> filterApplesWithHighQuality(List<Apple> inventory, Predicate<Apple> p) {
        Predicate<Apple> condition = apple -> apple.getQuality().equals("high");
        return inventory.stream()
            .filter(p.and(condition))
            .collect(toList());
    }

    @Test
    // 시간은 유의미한 차이를 내지 못한다. 그렇다면 메모리는
    void boxingTimeCompareTest() {
        int repetitions = 2_000_000_000; // Adjust the number of repetitions if needed.

        // Test non-auto boxing.
        long start = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            nonAutoBoxing(i);
        }
        long end = System.nanoTime();
        System.out.println("Non-auto boxing time: " + (end - start) + " nanoseconds");

        // Test auto boxing.
        long start2 = System.nanoTime();
        for (int i = 0; i < repetitions; i++) {
            autoBoxing(i);
        }
        long end2 = System.nanoTime();
        System.out.println("Auto boxing time: " + (end2 - start2) + " nanoseconds");
    }

    public static boolean nonAutoBoxing(int j) {
        IntPredicate evenNumbers = (int i) -> i % 2 == 0;
        return evenNumbers.test(j);
    }

    public static boolean autoBoxing(int j) {
        Predicate<Integer> oddNumbers = (Integer i) -> i % 2 != 0;
        return oddNumbers.test(j);
    }


    @Test
    void testMemoryUsage() {
        int repetitions = 1000; // Adjust the number of repetitions if needed.

        // Warm up the JVM by running the methods a few times before testing.
        for (int i = 0; i < 10; i++) {
            nonAutoBoxing(i);
            autoBoxing(i);
        }

        Object[] nonAutoBoxingResults = new Object[repetitions];
        Object[] autoBoxingResults = new Object[repetitions];

        // Test non-auto boxing.
        Runtime.getRuntime().gc(); // Run garbage collector to free unnecessary memory.
        long startMemory = getCurrentMemory();
        for (int i = 0; i < repetitions; i++) {
            nonAutoBoxingResults[i] = nonAutoBoxing(i);
        }
        long nonAutoBoxingMemory = getCurrentMemory() - startMemory;

        // Test auto boxing.
        Runtime.getRuntime().gc(); // Run garbage collector to free unnecessary memory.
        startMemory = getCurrentMemory();
        for (int i = 0; i < repetitions; i++) {
            autoBoxingResults[i] = autoBoxing(i);
        }
        long autoBoxingMemory = getCurrentMemory() - startMemory;

        System.out.println("Non-auto boxing memory: " + nonAutoBoxingMemory + " bytes");
        System.out.println("Auto boxing memory: " + autoBoxingMemory + " bytes");
    }

    private static long getCurrentMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }


}
