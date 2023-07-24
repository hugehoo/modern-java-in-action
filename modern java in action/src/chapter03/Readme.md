## 람다 표현식
- 람다 표현식이란, 메서드로 전달할 수 있는 익명 함수를 단순화 한 것. 

#### 람다의 특징
  - 익명: 보통 메서드와 달리 이름이 없다.
  - 함수: 람다는 일반 메서드처럼 클래스에 종속되지 않기 때문에 함수라고 부른다. 하지만 메서드처럼 파라미터 리스트, 바디, 리턴 타입, 가능한 예외 리스트를 포함할 수 있다.
  - 전달: 람다 표현식을 메서드 인수로 전달하거나 변수로 저장할 수 있다. 
  - 간결성: 익명 클래스와 달리 간결하게 코드를 나타낼 수 있다.

#### 람다 표현식의 구성
- 파라미터 리스트: 람다 바디로 전달할 인자로 구성된다.
- 화살표: JS 와 달리 -> 를 사용(JS : =>)
- 람다 바디: 람다 반환값에 해당하는 표현식

Q. 왜 람다는 함수형 인터페이스 문맥에서 사용할 수 있을까?

#### 함수형 인터페이스
- 하나의 추상 메서드를 가진 인터페이스 
- 함수형 인터페이스로 뭘 할 수 있을까? => 람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있다.

#### 함수 디스크립터
- 함수형 인터페이스의 `추상 메서드 시그니처`는 `람다 표현식의 시그니처`를 가리킨다.

Q. 왜 함수형 인터페이스를 인수로 받는 메서드에만 람다 표현식을 사용할 수 있을까?
- 자바에 함수형식을 추가하는 방법도 대안으로 생각했지만, 언어를 더 복잡하게 만들지 않는 현재의 방법을 선택했다.

### 람다 활용: 실행 어라운드 패턴
- 자원을 처리하는 코드를 설정과 정리 두 과정이 둘러싸는 형태 -> ( 초기화/준비 코드 {{ 작업 A }} 정리/마무리 코드 )
``` java
public String processFile() throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        return br.readLine();
    }
}
```

##### 1단계 : 동작 파라미터화
- 위 코드에서는 한번의 한 줄만 읽을 수 있다.
- 한번에 두 줄을 읽거나, 자주 사용되는 단어를 변환하려면 어떻게 해야할까?
``` java
String result = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```

##### 2단계 : 함수형 인터페이스를 이용해 동작 전달
- 함수형 인터페이스 자리에 람다를 사용할 수 있단 점을 기억하자. 
- 
``` java
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}

// 정의한 함수형 인터페이스를 메서드의 인자로 전달 받을 수 있다.
public String processFile(BufferedReaderProcessor p) throws IOException {
}
```

##### 3단계 : 동작 실행

``` java
public String processFile(BufferedReaderProcessor p) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
        // return br.readLine(); 기존
        return p.process(br); // 개선
    }
}
```

##### 4단계 : 람다 전달
- 람다를 활용하여 아래 예시 뿐만 아니라, 다양한 동작을 파라미터로 넣을 수 있다.
- 실행 어라운드 패턴에서 실행부를 감싸는 `초기화/준비`, `마무리/정리` 코드는 그대로 유지한채 람다를 활용하여 작업 코드만 유연하게 교체할 수 있다.

``` java
public void sample() throws IOException {
    String s1 = processFile(BufferedReader::readLine);
    String s2 = processFile((BufferedReader br) -> br.readLine() + br.readLine());
}
```

Q. 람다가 등장한 배경

### 함수형 인터페이스 사용
- 함수형 인터페이스의 추상메서드는 람다 표현식의 시그니처를 묘사한다.

#### Predicate
- T 형식의 객체를 사용하는 불리언 표현식이 필요한 상황에서 Predicate 인터페이스를 사용할 수 있다.

#### Consumer
- 제네릭 형식 T 객체를 받아서 void 를 반환하는 accept 추상 메서드를 정의한다.

#### Function
- 제네릭 형식 T를 인수로 받아서 제네릭 형식 R 객체를 반환하는 추상 메서드 apply 를 정의한다.
- 입력을 출력으로 매핑하는 람다를 정의할 때 Function Interface 를 활용할 수 있다.

Q. 코틀린에서는 functional interface 가 어떤 형식으로 정의돼 있을까?

### 기본형 특화
- 자바의 자료형은 기본형과 참조형으로 나뉘는데, 제너릭 파라미터는 참조형만 허용된다.
- 기본형을 참조형으로 변형하는 것을 Boxing, 참조형을 기본형으로 변환하는 것을 unboxing 이라 한다.
- 오토박싱은 박싱과 언박싱이 프로그램에 의해 자동으로 이루어지는 것을 의미한다.
``` java
List<Integer> list = new ArrayList<>();
for (int i = 300; i < 400; i++) {
    list.add(i);
}
```
- 위 코드는 기본형 int 가 자동으로 Integer 로 박싱된다. 하지만 이런 변환 과정은 비용이 소모된다.
  - Boxing 값은 primitive type 을 감싸는 Wrapper 이며, 힙에 저장된다.
  - 즉 Boxing 된 값은 더 많은 메모리를 소비하며, 기본형을 가져올 때도 메모리를 탐색하는 과정이 필요하다 (Why? 더 찾아보자)
- Java8 에서는 기본형을 입출력으로 사용하는 상황에서 오토박싱을 피할 수 있는 특별한 함수형 인터페이스를 제공한다.
``` java
public void boxing() {
    // 1000 을 박싱하지 않는다.
    IntPredicate evenNumbers = (int i) -> i % 2 == 0;
    evenNumbers.test(1000);
    
    // 1000 을 박싱한다 -> 고비용
    Predicate<Integer> oddNumbers = (Integer i) -> i % 2 != 0;
    oddNumbers.test(1000);
}
```
