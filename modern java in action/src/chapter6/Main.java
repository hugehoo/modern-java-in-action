package chapter6;

import static java.util.Arrays.*;
import static java.util.Comparator.*;
import static java.util.stream.Collectors.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) {

        List<Dish> menu = List.of(
            new Dish("FISH", "salmon", 100),
            new Dish("MEAT", "pig", 300),
            new Dish("MEAT", "cow", 500),
            new Dish("OTHER", "milk", 20),
            new Dish("OTHER", "oragne", 30)
        );
        Map<String, Optional<Dish>> collect = menu.stream()
            .collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCaloric))));
        System.out.println(collect);

        Map<String, Dish> collect1 = menu.stream()
            .collect(toMap(Dish::getType,
                Function.identity(),
                BinaryOperator.maxBy(comparingInt(Dish::getCaloric)))
            );
        // Map<String, Dish> collect1 = menu.stream()
        //     .collect(groupingBy(Dish::getType,
        //         collectingAndThen(maxBy(Comparator.comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println(collect1);

        /*
        6.2.1 스트립값에서 최대/최소값 검색
         */
        // Comparator<Dish> caloricComparator = comparingInt(Dish::getCaloric);
        // Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(caloricComparator));

        /*
        6.2.2 요약 연산
         */
        Integer collect2 = menu.stream().collect(summingInt(Dish::getCaloric));
        // Integer collect2 = menu.stream().mapToInt(Dish::getCaloric).sum();

        Comparator<Dish> dishCaloriesComparator = comparingInt(Dish::getCaloric);
        Optional<Dish> mostCalorieDish = menu.stream().collect(minBy(comparingInt(Dish::getCaloric)));
        Integer collect3 = menu.stream().collect(summingInt(Dish::getCaloric));
        String shortMenu = menu.stream().map(Dish::getName).collect(joining());

        // Integer totalCalories = menu.stream().collect(reducing(0, Dish::getCaloric, (i, j) -> i + j));
        // Optional<Dish> totalCalories2 = menu.stream().collect(reducing((d1, d2) -> d1.getCaloric() > d2.getCaloric() ? d1 : d2));

        Integer totalCalories = menu.stream().map(Dish::getCaloric).reduce(0, Integer::sum);
        Optional<Dish> totalCalories2 = menu.stream().reduce((d1, d2) -> d1.getCaloric() > d2.getCaloric() ? d1 : d2);
        int totalCalories3 = menu.stream()
            .mapToInt(Dish::getCaloric)
            .sum();

        Map<String, List<String>> dishTags = new HashMap<>();
        dishTags.put("pork", asList("greasy", "salty"));
        dishTags.put("beef", asList("salty", "roasted"));
        dishTags.put("chicken", asList("fried", "crisp"));
        dishTags.put("french fries", asList("light", "natural"));
        dishTags.put("season fruit", asList("fresh", "natural"));
        dishTags.put("prawns", asList("tasty", "roasted"));
        dishTags.put("salmon", asList("delicious", "fresh"));

        menu.stream()
            .collect(groupingBy(Dish::getType,
                flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));
    }
}
