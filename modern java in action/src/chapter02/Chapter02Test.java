package chapter02;

import static chapter01.Color.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chapter01.Apple;
import chapter01.Color;

public class Chapter02Test {

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
    void uglyParameters() {
        // 메서드 시그니처가 모호함 ->
        filterAppleByParmeters(사과박스, GREEN, 100, true);
        filterAppleByParmeters(사과박스, GREEN, 200, false);
    }

    @Test
    void customPredicate() {
        List<Apple> 초록사과 = filterAppleByPredicate(사과박스, new AppleGreenColorPredicate());
        List<Apple> 무거운사과 = filterAppleByPredicate(사과박스, new AppleHeavyPredicate());

        초록사과.forEach(i -> assertEquals(i.getColor(), GREEN));
        무거운사과.forEach(i -> assertTrue(i.getWeight() >= 100));
    }

    @Test
    void byLambda() {
        filterApplesByLambda(사과박스, (Apple apple) -> GREEN.equals(apple.getColor()));
        filterApplesByLambda(사과박스, (Apple apple) -> apple.getWeight() > 100);
    }

    @Test
    void 다양한_조건_모두_적용() {
        Predicate<Apple> appleColorFilter = (Apple apple) -> GREEN.equals(apple.getColor());
        Predicate<Apple> appleWeightFilter = (Apple apple) -> apple.getWeight() > 100;
        List<Predicate<Apple>> predicates = List.of(appleColorFilter, appleWeightFilter);

        filterApplesByMultiPredicates(사과박스, predicates);
    }

    @Test
    void 다양한_조건_모두_적용_Improved() {
        List<Predicate<Apple>> predicates = List.of(
            (Apple apple) -> GREEN.equals(apple.getColor()),
            (Apple apple) -> apple.getWeight() > 100
        );

        filterApplesByMultiPredicatesImproved(사과박스, predicates);
    }

    @Test
    void 무게순_정렬_익명클래스() {
        사과박스.sort(new Comparator<Apple>() {
            @Override
            public int compare(Apple a1, Apple a2) {
                return a1.getWeight().compareTo(a2.getWeight());
            }
        });
        for (Apple apple : 사과박스) {
            System.out.println(apple);
        }
    }

    @Test
    void 무게순_정렬_람다함수() {
        사과박스.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));  // Comparator.comparing(Apple::getWeight)
        for (Apple apple : 사과박스) {
            System.out.println(apple);
        }
    }

    @Test
    void 다양한_기준으로_정렬() {
        Comparator<Apple> appleComparator = Comparator.comparing(Apple::getWeight)
            .thenComparing(Apple::getColor);
        사과박스.sort(appleComparator);

        for (Apple apple : 사과박스) {
            System.out.println(apple);
        }
    }

    @Test
    void 병렬_스트림_테스트() {
        List<Apple> 더미_데이터 = new ArrayList<>();
        int i = 1;
        while (i <= 30) {
            더미_데이터.add(new Apple(GREEN, 130, "high"));
            i += 1;
        }
        System.out.println("더미_데이터.size() = " + 더미_데이터.size());

        long 측정_시작 = System.currentTimeMillis();
        더미_데이터.parallelStream()
            .forEach(d -> {
                System.out.println("Starting " + Thread.currentThread().getName());
                dummyApiCall(d);
            });
        System.out.println(System.currentTimeMillis() - 측정_시작);
    }

    @Test
    void 일반_스트림_테스트() {
        List<Apple> 더미_데이터 = new ArrayList<>();
        int i = 1;
        while (i <= 50) {
            더미_데이터.add(new Apple(GREEN, 130, "high"));
            i += 1;
        }

        System.out.println("더미_데이터.size() = " + 더미_데이터.size());
        long 측정_시작 = System.currentTimeMillis();
        더미_데이터.stream()
            .forEach(this::dummyApiCall);
        System.out.println(System.currentTimeMillis() - 측정_시작);
    }

    public void dummyApiCall(Apple a) {
        try {
            // call External API using Apple a
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // handle exception
        }
    }

    public List<Apple> filterApplesByMultiPredicatesImproved(List<Apple> inventory, List<Predicate<Apple>> predicates) {
        return inventory.stream()
            .filter(apple -> predicates.stream()
                .allMatch(predicate -> predicate.test(apple))
            )
            .collect(Collectors.toList());
    }

    public List<Apple> filterApplesByMultiPredicates(List<Apple> inventory, List<Predicate<Apple>> predicates) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            for (Predicate<Apple> p : predicates) {
                if (!p.test(apple)) {
                    break;
                }
                result.add(apple);
            }
        }
        return result;
    }

    public List<Apple> filterApplesByLambda(List<Apple> inventory, Predicate<Apple> predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public List<Apple> filterAppleByParmeters(List<Apple> inventory, Color color, int weight, boolean flag) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ((flag && apple.getColor().equals(color)) || (!flag && apple.getWeight() > weight)) {
                result.add(apple);
            }
        }
        return result;
    }

    public interface ApplePredicate {
        boolean test(Apple apple);
    }

    public static class AppleHeavyPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() >= 100;
        }
    }

    public static class AppleGreenColorPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return apple.getColor().equals(GREEN);
        }
    }

    public List<Apple> filterAppleByPredicate(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public void runnable() {
        Thread threadForFirstHalf = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Call API with first half of collections");
            }
        });

        Thread threadForSecondHalf = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Call API with second half of collections");
            }
        });
        Thread thread = new Thread(() -> System.out.println("run() 의 구현부를 적용"));
    }
}
