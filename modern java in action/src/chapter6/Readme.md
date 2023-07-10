
# 스트림으로 데이터 수집

## 6.1 컬렉터란 무엇인가?
- 함수형 프로그래밍에서는 `무엇`을 원하는지 직접 명시 가능
  - `어떻게`에 관해서는 신경쓰지 않아도 된다.
  
``` java
  // 명령형
  Map<Currency, List<Transaction>> transactionByCurrencies = new HashMap<>();
  List<Transaction> transactions = new ArrayList<>();
  for (Transaction transaction : transactions) {
      Currency currency = transaction.getCurrency();
      List<Transaction> transactionsForCurrency = transactionByCurrencies.get(currency);
      if (transactionsForCurrency == null) {
          transactionsForCurrency = new ArrayList<>();
          transactionByCurrencies.put(currency, transactionsForCurrency);
      }
      transactionsForCurrency.add(transaction);
  }
  
  // 함수형
  Map<Currency, List<Transaction>> transactionsByCurrencies = transactions.stream()
      .collect(groupingBy(Transaction::getCurrency));
```

### 6.1.1 고급 리듀싱 기능을 수행하는 컬렉터
- 함수형 API 의 또 다른 장점은 높은 수준의 조합성과 재사용성을 꼽을 수 있다. 

### 6.1.2 미리 정의된 컬렉터
- Collectors 에서 제공하는 메서드의 기능은 크게 3 가지로 구분 가능
  1. Stream 요소를 하나의 값으로 리듀스하고 요약
     - 스트렘 요소의 총합을 찾는 등 다양한 계산 수행할 때 사용 
  2. 요소 그룹화
     - 다수준으로 그룹화 혹은 각각의 결과 서브그룹에 추가로 리듀싱 연산을 적용
  3. 요소 분할
     - 한 개의 인수를 받아 불리언을 반환하는 함수, 즉 프레디케이트를 그룹화 함수로 사용

## 6.2 리듀싱과 요약
### 6.2.1 스트립 값에서 최댓값과 최솟값 검색

``` java
  Comparator<Dish> dishCaloriesComparator = comparingInt(Dish::getCaloric);
  Optional<Dish> mostCalorieDish = menu.stream().collect(maxBy(dishCaloriesComparator));

  >>> Optional<Dish> mostCalorieDish = menu.stream().max(comparingInt(Dish::getCaloric));
  >>> Optional<Dish> mostCalorieDish = menu.stream().min(comparingInt(Dish::getCaloric));
```

### 6.2.2 요약 연산
- `summingInt`, `summingLong`, `summingDouble` 팩토리 메서드 : 객체를 int 로 매핑하는 `함수`를 인수로 받는다.
### 6.2.3 문자열 연결
- String shortMenu = menu.stream().map(Dish::getName).collect(joining());
- joining() 메서드는 내부적으로 StringBuilder 를 이용해서 문자열을 연결
``` java
    public static Collector<CharSequence, ?, String> joining() {
        return new CollectorImpl<CharSequence, StringBuilder, String>(
                StringBuilder::new, 
                StringBuilder::append,
                (r1, r2) -> { r1.append(r2); return r1; },
                StringBuilder::toString, 
                CH_NOID);
    }
```

### 6.2.4 범용 리듀싱 요약 연산 
- 모든 컬렉터는 reducing 팩토리 메서드로도 정의 가능. 즉 범용 Collectors.reducing 으로도 구현할 수 있다.
- 그럼에도 이전 예제에서 범용 팩토리 메서드 대신 특화된 컬렉터를 사용한 이유는 프로그래밍적 편의성 때문(가독성 역시)
``` java
    // 자신의 상황에 맞는 최적의 해법을 선택할 수 있다.
    
    // 1
    Integer totalCalories = menu.stream().collect(reducing(0, Dish::getCaloric, (i, j) -> i + j));
    
    // 2
    int totalCalories3 = menu.stream()
                .mapToInt(Dish::getCaloric)
                .sum();
```
## 6.3 그룹화
- 데이터 집합을 하나 이상의 특성으로 분류하는 작업 -> 함수형을 이용하면 가독성 있는 코한 줄의 코드로 그룹화를 구현할 수 있다.
- 단순한 속성 접근자(ex, getter)로 분류할 땐 메서드 참조로 간단히 그룹화 가능
- 더 복잡한 분류 기준이 필요한 상황에서는 메서드 참조 대신 람다 표현식으로 필요한 로직 구현 가능
``` java
public static enum CaloricLevel {
    DIET, NORMAL, FAT
}

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

>>> OTHER - [milk, orange, pizza]
>>> FISH - [salmon, prawns]
>>> MEAT - [pig, cow, chicken, beef]

>>> NORMAL - [cow]
>>> DIET - [salmon, prawns, pig, milk, orange]
>>> FAT - [chicken, beef, pizza]
```
### 6.3.3 Collecting Data in subgroups
``` java
// groupingBy() with One arg.
public static <T, K> Collector<T, ?, Map<K, List<T>>>
groupingBy(Function<? super T, ? extends K> classifier) {
    return groupingBy(classifier, toList()); // 여기 groupingBy definition 이 바로 아래에 해당
}

// groupingBy() with two args.
public static <T, K, A, D>
Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier,
                                      Collector<? super T, A, D> downstream) {
    return groupingBy(classifier, HashMap::new, downstream);
}

Map<String, List<Dish>> collect1 = menu.stream()
                                        .collect(groupingBy(Dish::getType));

Map<String, List<Dish>> collect2 = menu.stream()
                                        .collect(groupingBy(Dish::getType, toList()));
```
- 


