package chapter02;

import static chapter01.Color.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import chapter01.Apple;
import chapter01.Color;

public class Chapter02Test {

    List<Apple> inventory = new ArrayList<>();
    @BeforeEach
    void init() {
        inventory = List.of(
            new Apple(RED, 100, "high"),
            new Apple(RED, 80, "middle"),
            new Apple(RED, 70, "low"),
            new Apple(RED, 120, "high"),
            new Apple(RED, 50, "low"),
            new Apple(GREEN, 110, "high"),
            new Apple(GREEN, 80, "low"),
            new Apple(GREEN, 130, "high")
        );
    }

    @Test
    void uglyParameters() {
        // 메서드 시그니처가 모호함 ->
        filterAppleByParmeters(inventory, GREEN, 100, true);
        filterAppleByParmeters(inventory, GREEN, 200, false);
    }

    @Test
    void customPredicate() {
        filterAppleByPredicate(inventory, new AppleColorPredicate());
        filterAppleByPredicate(inventory, new AppleHeavyPredicate());
    }

    @Test
    void byLambda() {
        filterApplesByLambda(inventory, (Apple apple) -> GREEN.equals(apple.getColor()));
        filterApplesByLambda(inventory, (Apple apple) -> apple.getWeight() > 100);
    }

    @Test
    void byMultiLambda() {
        Predicate<Apple> appleColorFilter = (Apple apple) -> GREEN.equals(apple.getColor());
        Predicate<Apple> appleWeightFilter = (Apple apple) -> apple.getWeight() > 100;
        List<Predicate<Apple>> predicates = List.of(appleColorFilter, appleWeightFilter);

        filterApplesByMultiPredicates(inventory, predicates);
    }

    @Test
    void byMultiLambdaImproved() {
        List<Predicate<Apple>> predicates = List.of(
            (Apple apple) -> GREEN.equals(apple.getColor()),
            (Apple apple) -> apple.getWeight() > 100
        );

        filterApplesByMultiPredicatesImproved(inventory, predicates);
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

    public List<Apple> filterApplesByMultiPredicatesImproved(List<Apple> inventory, List<Predicate<Apple>> predicates) {
        return inventory.stream()
            .filter(apple -> predicates.stream().allMatch(predicate -> predicate.test(apple)))
            .collect(Collectors.toList());
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
            return apple.getWeight() > 50;
        }
    }

    public static class AppleColorPredicate implements ApplePredicate {
        @Override
        public boolean test(Apple apple) {
            return apple.getWeight() > 50;
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
}
