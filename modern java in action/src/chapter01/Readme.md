### 자바의 진화
- 자바 8에서는 고전적인 객체지향에서 벗어나 함수형 프로그래밍으로 다가섰다.

#### 자바 8의 주요 특징 3가지

1. 람다 표현식 : 익명 함수를 생성하여 코드가 간결해지며 코드 중복을 줄일 수 있다.
   - 문제 해결: 기존 자바에서는 함수의 인자로 값을 넘길 때 익명 클래스를 사용해서 코드를 작성했지만, 이로 인해 코드가 번잡했으며 가독성이 떨어졌습니다.
     람다 표현식으로 인해 간결한 코드 작성이 가능해졌다.
2. 스트림 API : 스트림 API를 도입하여 데이터 컬렉션을 유용히 처리할 수 있다. 스트림 API 를 사용하면 병렬 처리 가능한 코드를 간단하게 구현할 수 있다.
  - 문제 해결: 기존 자바에서는 직접 반복문 등을 사용하여 데이터를 처리했다. 이는 번거롭고, 병렬 처리를 위한 추가적인 구현이 필요했지만, 
  스트림 API로 인해 간편하게 데이터 처리와 병렬 처리를 구현할 수 있게 됐다.
3. 디폴트 메서드 및 Method 참조 : 인터페이스에 디폴트 메서드와 정적 메서드를 추가할 수 있게 됐다. 기존 코드 수정 없이 인터페이스를 개선하고, 새로운 기능을 추가할 수 있다.
   - 문제 해결: 기존 자바에서는 인터페이스를 변경하면 모든 구현 클래스를 수정해야 했다. 
   하지만 디폴트 메서드와 정적 메서드를 통해 인터페이스 변경에 따른 수정 사항을 최소화할 수 있게 됐다.
- `정리` : Java 8은 코드의 가독성과 작성 효율, 병렬 처리, 인터페이스의 유연성 등은 개선했다. 이로 인해 자바 개발자들에게 보다 사용하기 쉬운 개발 환경을 제공할 수 있었다.

- e.g) 함수형 프로그래밍을 예로 들면 우리가 수행하는 작업이 최우선시되며, 그 작업을 구체적으로 어떻게 수행할지는 별개의 문제로 취급한다.
``` java
// filtering 요건에 맞는 메서드를 매번 구현해야 한다.
 public List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals(GREEN)) {
                result.add(apple);
            }
        }
        return result;
    }

  public List<Apple> filterGoodQualityApples(List<Apple> inventory) {
      List<Apple> result = new ArrayList<>();
      for (Apple apple : inventory) {
          if (apple.getQuality().equals("High")) {
              result.add(apple);
          }
      }
      return result;
  }
```

- 자바 8 이전에는 작업이 구체적으로 어떻게 수행될지 직접 코드를 구현해야 했다면, 자바 8 부터는 단순히 함수를 호출하는 것으로 원하는 작업을 수행할 수 있다.
``` java
List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
    return inventory.stream()
        .filter(p)
        .collect(toList());
}
```


### 함수를 값처럼 취급할 때의 장점
- 자바의 다양한 구조체(클래스, 메서드)는 값의 구조를 표현하는데 도움이 될 수 있다.
  - 하지만 프로그램을 실행하는 동안 이러한 구조체는 자유롭게 전달할 수 없다.
  - 이렇게 전달할 수 없는 구조체를 이급 시민이라 한다.
  - 즉 메서드와 클래스는 그 자체로 값이 될 수 없다. 
  - 이게 왜 중요한가?
  - 만약 런타임에 메서드를 전달할 수 있다면, 즉 메서드를 일급 시민으로 만들수 있으면 프로그래밍에 유용하게 활용할 수 있다.

  - 예시) 컴파일 타임에 메서드를 넘긴 사례
``` java
public class CompileTimeMethod {

    public static void main(String[] args) {
        Integer[] numbers = {3, 1, 4, 2, 5};

        // Ascending order comparator (컴파일 타임에 메서드를 전달)
        Comparator<Integer> ascendingComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        };

        // 정렬 메서드에 정의된 comparator 전달
        Arrays.sort(numbers, ascendingComparator);
        System.out.println("Ascending order: " + Arrays.toString(numbers));

        // Descending order comparator (컴파일 타임에 메서드를 전달)
        Comparator<Integer> descendingComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        };

        // 정렬 메서드에 정의된 comparator 전달
        Arrays.sort(numbers, descendingComparator);
        System.out.println("Descending order: " + Arrays.toString(numbers));
    }
}
```

### 자바의 일급 객체
- 자바에서는 보통 람다와 익명 함수를 배울 때 일급 시민 (객체) 라는 표현을 사용. 
- 일급 객체는 아래 3가지 조건을 충족한 객체를 의미한다.
  1. 변수나 객체에 할당할 수 있다.
  2. 객체의 인자로 넘길 수 있다.
  3. 객체의 리턴값으로 사용할 수 있다.

- 자바의 `람다(익명 함수)`는 변수나 인자에 할당 가능하며, 리턴 값으로도 사용가능하기 때문에 일급 객체의 요건을 충족한다.

#### 일급 객체의 요건을 충족하며 생긴 이점
- 객체를 메서드의 인자로 넘길 수 있기 때문에 filter 메서드의 두번째 인자로 함수를 넣을 수 있다.
- 이로써 코드 재활용이 수월해진다. 람다가 없던 시절에는 filter() 의 두번째 인자로 함수를 넘길 수 없었기 때문에 매번 filter 메서드를 재구현 했으리라 예상해본다.
- 즉 일급객체가 중요한 이유 중 하나는 아래의 코드 재활용 예시처럼, 프로그래밍의 효율성과 유연성을 높일 수 있기 때문이다.

<br/>

- 일급 시민은 런타임에 메서드를 전달할 수 있다.
- 런타임에 메서드를 제공 : 프로그램이 실행되는 도중에 함수를 전달하거나 수정할 수 있는 능력을 의미. 즉, 컴파일 시점에서 이미 정의된 것이 아닌 실행 시점에 조건에 따라 다른 동작이나 함수를 전달할 수 있다.
``` java
public class Example {

    // Predicate<T> 함수형 인터페이스를 이용해 List<T>인자를 받은 메서드들을 만들어 봅시다.
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T element : list) {
            if (predicate.test(element)) {
                result.add(element);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 1. 2의 배수 찾기 : Predicate<T> 인터페이스의 test()메서드 내부 로직이 실행됩니다.
        List<Integer> evenNumbers = filter(numbers, x -> x % 2 == 0); // 해당 라인이 실행되기 전까지 filter() method 는 어떤 predicate 를 실행할지 알지 못한다.
        System.out.println("even numbers: " + evenNumbers);

        // 2. 홀수 찾기 : Predicate<T> 인터페이스의 test()메서드 내부 로직이 실행됩니다.
        List<Integer> oddNumbers = filter(numbers, x -> x % 2 != 0);  // 해당 라인이 실행되기 전까지 filter() method 는 어떤 predicate 를 실행할지 알지 못한다.
        System.out.println("odd numbers: " + oddNumbers);
    }
}
```

### 메서드 참조
메서드와 람다를 일급 시민으로(값으로 취급할 수 있도록)
- 메서드 참조 `::`( 이 메서드를 값으로 사용하라는 의미) 
- isHidden() 이라는 함수를 값(인수)처럼 넘길 수 있게 됐다.
- isHidden() 메서드를 File 의 객체 참조하는 것이 아닌, 메서드 참조를 만들어 전달할 수 있게 됨

``` java
// before Java 8
File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
    public boolean accept(File file){
        return file.isHidden();
    }
});

// since Java 8
File[] hiddenFiles = new File(".").listFiles(File::isHidden);
```

### 디폴트 메서드와 자바 모듈
- 인터페이스를 구현한 클래스가 여러가지 일 때, 인터페이스를 변경하는 것은 어려운 일이다.
- 하지만 디폴트 메서드가 등장하면서 인터페이스를 구현하는 클래스를 강제로 변경하지 않아도 된다.
- 예를 들어 자바8 이전에는 List<T> 가 stream 이나 parallelStream 메서드를 지원하지 않는다는 것이 문제였다. 그 때문에 아래의 코드는 컴파일 되지 않았다.
- 그럼 어떻게 기존 구현을 고치지 않고, 이미 공개된 인터페이스를 변경할 수 있을까?
- Default Method 의 등장이 이를 해결한다.
- 메서드 구현의 책임이 구체 클래스에 있지 않고, 인터페이스의 일부로 포함된다.
- 
``` java
List<Apple> collect = inventory.parallelStream()
.filter((Apple a) -> a.getWeight() > 100)
.toList();
```

``` java
// Collection Interface
// parallelStream() 디폴트 메서드로 구현돼 있는걸 확인 가능
default Stream<E> parallelStream() {
    return StreamSupport.stream(spliterator(), true);
}
```
- 자바8 이전에 `sort()` 메서드는 직접 구현해야 했지만, 
- 자바8 이후 List 인터페이스에 sort() 디폴트 메서드가 추가되어 List 의 인스턴스는 sort 메서드를 직접 호출할 수 있다.


### 마치며
- 자바8은 프로그램을 더 효과적이고 간결하게 구현할 수 있는 새로운 개념과 기능 제공
- 기존 자바는 멀티코어 프로세서를 온전히 활용하기 어렵다 -> 스트림의 등장으로 `멀티 코어`를 비교적 쉽게 다룰 수 있다.
- 함수는 일급값이다, 메서드를 어떻게 함수형값으로 넘기는지, 익명함수를 어떻게 구현하는지 기억하자. 

<hr/>

#### Reference
- 모던 자바 인 액션 chapter01
- [https://inpa.tistory.com](https://inpa.tistory.com/entry/CS-%F0%9F%91%A8%E2%80%8D%F0%9F%92%BB-%EC%9D%BC%EA%B8%89-%EA%B0%9D%EC%B2%B4first-class-object)
- [https://bcp0109.tistory.com](https://bcp0109.tistory.com/313)

- java5 부터 concurrent -> 멀티 코어 
