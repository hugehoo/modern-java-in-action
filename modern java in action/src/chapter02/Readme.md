### 동작 파라미터화
- 시시각각 변하는 요구상항을 충족시킬 수 있는 코드는 쉽게 구현할 수 있고 유지보수가 쉬워야 한다.
- `동작 파리미터화` 는 자주 바뀌는 요구사항에 효과적으로 대응할 수 있다.
  - `동작 파리미터화` : 어떻게 실행할지 아직 결정하지 않은 코드 블록
- 요구사항이 추가될 때 메서드에 파라미터를 추가하는 식으로 문제를 해결하면 모호한 코드가 만들어지고, 해당 메서드의 의도가 분명해지지 않는다.

``` java
@Test
void uglyParameters() {
    // 메서드 시그니처가 모호함 : true/false 가 무엇을 의미하는지 구현체를 까보지 않으면 추론할 수 없다.
    filterAppleByParmeters(사과박스, GREEN, 100, true);
    filterAppleByParmeters(사과박스, GREEN, 200, false);
}
```

<br/>

- 위와 달리 동작을 파라미터화 하면 메서드 시그니처 만으로 코드를 추론할 수 있다.
``` java
@Test
void customPredicate() {
    List<Apple> 초록사과 = filterAppleByPredicate(사과박스, new AppleGreenColorPredicate());
    List<Apple> 무거운사과 = filterAppleByPredicate(사과박스, new AppleHeavyPredicate());

    초록사과.forEach(i -> assertEquals(i.getColor(), GREEN));
    무거운사과.forEach(i -> assertTrue(i.getWeight() >= 100));
}

@Test
void byLambda() { // 람다를 사용하면 더 유연한 코드를 짤 수 있다.
    filterApplesByLambda(사과박스, (Apple apple) -> GREEN.equals(apple.getColor()));
    filterApplesByLambda(사과박스, (Apple apple) -> apple.getWeight() > 100);
}
``` 

### Comparator 정렬
- 동작 파라미터화는 동작을 (한 조각의 코드로) 캡슐화하여 메서드로 전달하는 패턴이다.
- Comparator 정렬 조건을 파라미터화 하여 다양한 정렬 조건을 충족 시킬 수도 있다.

``` java
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
```

``` java
// Comparator.thenComparing : 객체의 리턴값으로 함수(람다식)를 사용 ==> 일급 객체
default Comparator<T> thenComparing(Comparator<? super T> other) {
    Objects.requireNonNull(other);
    return (Comparator<T> & Serializable) (c1, c2) -> {
        int res = compare(c1, c2);
        return (res != 0) ? res : other.compare(c1, c2);
    };
}

```


- Runnable, Callable 등의 스레드 기법도 동작을 파라미터화 하여 메서드의 인수로 넘길 수 있다는걸 보여줬다. 


##### Reference
- https://m.blog.naver.com/tmondev/220945933678
- https://javabom.tistory.com/59