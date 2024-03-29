# [Java] Lambda Expression

# 람다식이란?

람다식은 JAVA8에 도입된 문법으로 익명 함수(anonymous function)를 생성하기 위한 식이다. 객체 지향 언어보다는 함수 지향 언어에 가깝다.

람다식 > 매개 변수를 가진 코드 블록 > 익명 구현 객체

**람다식을 사용하지 않은 경우**

```java
Runnable runnable = new Runnable(){

	public void run(){ ... }

}// 익명 구현 객체
```

**람다식을 사용하는 경우**

```java
Runnable runnable = () -> { ... }// 람다식
```

람다식은 (매개변수) -> {실행코드} 형태로 작성된다. 함수 정의 형태를 띠고 있지만 런타임 시에 인터페이스의 익명 구현 객체로 생성된다. 위의 코드는 Runnable 변수에 대입되므로 람다식은 Runnable의 익명 구현 객체를 생성하게 된다.

# **람다식 사용 방법**

```java
// 매개 변수가 있는 경우

(타입 매개변수, ...) -> { 실행문; ... }

ex)

1. (int a) -> { System.out.println(a) }

2. (a) -> { System.out.println(a) }

3. a -> System.out.println(a)

// 매개 변수가 없는 경우

( ) -> { 실행문; ... }
```

1. 매개 변수 타입은 런타임 시에 대입되는 값에 따라 자동으로 인식될 수 있기 때문에 람다식에서는 매개 변수의 타입을 일반적으로 언급하지 않는다.
2. 하나의 매개 변수만 있으면 괄호 ( ) 를 생략할 수 있다.
3. 하나의 실행문만 있으면 중괄호 { } 도 생략 가능하다.

# **람다식의 장점과 단점**

## **장점**

- 효율적인 람다 함수의 사용으로 불필요한 루프문의 삭제가 가능하며, **함수의 재활용이 용이**.
- 필요한 정보만을 사용하는 방식을 통한 성능 향상됨.
- 일반적으로 다중 cpu를 활용하는 형태로 구현되어 **병렬 처리에 유리**함.
- 코드가 간결해져 개발자의 의도가 명확히 드러나므로 **가독성이 향상**

## **단점**

- 이론상 단순하게 모든 원소를 전부 순회하는 경우 람다식이 **조금 느릴 수 있음**.
- **디버깅 시 함수 콜 스택 추적이 다소 어려움**.
- 지나치게 남발하면 코드가 이해하기 어렵고 지저분해짐.
- 람다를 사용하여 만든 무명함수는 **재사용이 불가능**함.

---

# 출처:

[[JAVA] 람다식(Lambda Expressions)이란? 사용방법 & 장단점](https://junghn.tistory.com/entry/JAVA-%EB%9E%8C%EB%8B%A4%EC%8B%9DLambda-Expressions%EC%9D%B4%EB%9E%80-%EC%82%AC%EC%9A%A9%EB%B0%A9%EB%B2%95-%EC%9E%A5%EB%8B%A8%EC%A0%90)