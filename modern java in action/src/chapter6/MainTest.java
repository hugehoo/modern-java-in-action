package chapter6;

import static java.util.Arrays.*;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MainTest {

    static List<Dish> menu;

    @BeforeAll
    static void init() {
        menu = List.of(
            new Dish("FISH", "salmon", 100),
            new Dish("FISH", "prawns", 200),
            new Dish("MEAT", "pig", 300),
            new Dish("MEAT", "cow", 500),
            new Dish("MEAT", "chicken", 800),
            new Dish("MEAT", "beef", 900),
            new Dish("OTHER", "milk", 20),
            new Dish("OTHER", "orange", 30),
            new Dish("OTHER", "pizza", 800)
        );
    }

    @Test
    void sample() {
        Integer totalCalories = menu.stream().map(Dish::getCaloric).reduce(0, Integer::sum);
        int totalCalories3 = menu.stream()
            .mapToInt(Dish::getCaloric)
            .sum();
        assertEquals(totalCalories, totalCalories3);

    }

    public static enum CaloricLevel {
        DIET, NORMAL, FAT
    }

    @Test
    void grouping() {
        Map<String, List<Dish>> collect = menu.stream().collect(groupingBy(Dish::getType));

        for (String type : collect.keySet()) {
            System.out.println(type + " - " + collect.get(type));
        }

        Map<CaloricLevel, List<Dish>> collect1 = menu.stream()
            .collect(groupingBy(dish -> {
                if (dish.getCaloric() <= 400) return CaloricLevel.DIET;
                else if (dish.getCaloric() <= 700) return CaloricLevel.NORMAL;
                else return CaloricLevel.FAT;
            }));
        for (CaloricLevel type : collect1.keySet()) {
            System.out.println(type + " - " + collect1.get(type));
        }
    }

    @Test
    void groupingAdvanced() {

        Map<String, List<String>> dishTags = new HashMap<>();
        dishTags.put("pork", asList("greasy", "salty"));
        dishTags.put("beef", asList("salty", "roasted"));
        dishTags.put("chicken", asList("fried", "crisp"));
        dishTags.put("french fries", asList("light", "natural"));
        dishTags.put("season fruit", asList("fresh", "natural"));
        dishTags.put("prawns", asList("tasty", "roasted"));
        dishTags.put("salmon", asList("delicious", "fresh"));
        Map<String, Set<String>> dishNamesByType = menu.stream()
            .collect(groupingBy(Dish::getType,
                flatMapping(dish -> dishTags.getOrDefault(dish.getName(), Collections.emptyList()).stream(), toSet())
            ));
                // flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet()))); // NPE 발생 코드
        // flatMap 과 flatMapping 은 엄연히 다르다.
        System.out.println(dishNamesByType);

        Set<String> collect = dishTags.entrySet()
            .stream()
            .flatMap(tag -> dishTags.get(tag.getKey()).stream())
            .collect(toSet());
        System.out.println(collect);


        // public static <T, K> Collector<T, ?, Map<K, List<T>>>
        // groupingBy(Function<? super T, ? extends K> classifier) {
        //     return groupingBy(classifier, toList());
        // }
        // Map<String, List<Dish>> collect2 = menu.stream().collect(groupingBy(Dish::getType));
        // Map<String, List<Dish>> collect1 = menu.stream().collect(groupingBy(Dish::getType, toList()));

        Map<String, Optional<Dish>> mostCaloricByType = menu.stream()
            .collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCaloric))));
        System.out.println(mostCaloricByType);

        Map<String, Dish> mostCaloricByType2 = menu.stream()
            .collect(groupingBy(Dish::getType, collectingAndThen(getMaxCaloricDish(), Optional::get)));
        System.out.println(mostCaloricByType2);
    }

    private Collector<Dish, ?, Optional<Dish>> getMaxCaloricDish() {
        return maxBy(comparingInt(Dish::getCaloric));
    }

}