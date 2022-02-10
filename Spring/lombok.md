# [Lombok] Lombok 사용상 주의점

## **@AllArgsConstructor, @RequiredArgsConstructor 사용금지**

[@AllArgsConstructor, @RequiredArgsConstructor](https://projectlombok.org/features/constructor) 는 매우 편리하게 생성자를 만들어주지만 개발자의 별 생각없는 꼼꼼함이 치명적 버그가 되게 만들 수 있다.

```java
@AllArgsConstructor
public static class Order {
    private long cancelPrice;
    private long orderPrice;
}

// 취소금액 5,000원, 주문금액 10,000원
Order order = new Order(5000L, 10000L);
```

위 클래스에 대해 자동으로 `cancelPrice, orderPrice` 순서로 인자를 받는 생성자가 만들어진다. 그런데 개발자가 보기에 `Order` 클래스인데 `cancelPrice`가 `orderPrice`보다 위에 있는것이 마음에 안들어서 순서를 다음과 같이 바꾼다고 해보자.

```java
@AllArgsConstructor
public static class Order {
    private long orderPrice;
    private long cancelPrice;
}
```

이 경우, IDE가 제공해주는 **리팩토링은 전혀 작동하지 않고, lombok이 개발자도 인식하지 못하는 사이에 생성자의 파라미터 순서를 필드 선언 순서에 맞춰 `orderPrice,cancelPrice`로 바꿔버린다.** 게다가 이 두 필드는 **동일한 Type 이라서 기존 생성자호출 코드에서는 인자 순서를 변경하지 않았음에도 어떠한 오류도 발생하지 않는다.**

이에 의해, 위의 생성자를 호출하는 코드는 아무런 에러없이 잘 작동하는 듯 보이지만 실제로 입력된 값은 바뀌어 들어가게 된다.

```java
// 주문금액 5,000원, 취소금액 10,000원. 취소금액이 주문금액보다 많아짐!
Order order = new Order(5000L, 10000L);// 인자값의 순서 변경 없음
```

이 문제는 `@AllArgsConstructor`와 `@RequiredArgsConstructor`에 둘 다 존재하며, 이에 따라 이 두 lombok 애노테이션은 사용을 금지하는 것이 좋다.

대신, 생성자를 (IDE 자동생성등으로) 직접 만들고 필요할 경우에는 직접 만든 생성자에 [@Builder](https://projectlombok.org/features/Builder) 애노테이션을 붙이는 것을 권장한다. 파라미터 순서가 아닌 이름으로 값을 설정하기 때문에 리팩토링에 유연하게 대응할 수 있다.

```java
public static class Order {
    private long cancelPrice;
    private long orderPrice;

    @Builder
    private Order(long cancelPrice, long orderPrice) {
        this.cancelPrice = cancelPrice;
        this.orderPrice = orderPrice;
    }
}

// 필드 순서를 변경해도 문제 없음.
Order order = Order.builder().cancelPrice(5000L).orderPrice(10000L).build();
System.out.println(order);
```

### **필드에 붙은 애노테이션이 생성자 쪽으로 전달 안됨**

- 필드에 애노테이션이 있을 경우 자동으로 생성자로 전달이 안 되어 문제가 될 수 있다.
- 특히 `@Data`,`@AllArgs…` 등을 사용하면 이를 잊어버리는 경우가 부지기수 이다.
- `@XXXArgsConstructor(onConstructor=@__(@원하는애노테이션))` 를 사용할 경우 생성자의 각 인자에 애노테이션이 붙는게 아니라 생성자 자체에 애노테이션이 붙는다.
    - [onX](https://projectlombok.org/features/experimental/onX)

## **무분별한 @EqualsAndHashCode 사용 자제**

[@EqualsAndHashCode](https://projectlombok.org/features/EqualsAndHashCode)는 상당히 고품질의 `equals`, `hashCode` 메소드를 만들어준다. 잘 사용하면 좋지만 남발하면 심각한 문제가 생긴다.

특히 **문제가 되는 점**은 바로 **Mutable**(변경가능한) 객체에 아무런 파라미터 없이 그냥 사용하는 `@EqualsAndHashCode` 애노테이션이다.

```java
@EqualsAndHashCode
public static class Order {
    private Long orderId;
    private long orderPrice;
    private long cancelPrice;

    public Order(Long orderId, long orderPrice, long cancelPrice) {
        this.orderId = orderId;
        this.orderPrice = orderPrice;
        this.cancelPrice = cancelPrice;
    }
}

Order order = new Order(1000L, 19800L, 0L);

Set<Order> orders = new HashSet<>();
orders.add(order);// Set에 객체 추가

System.out.println("변경전 : " + orders.contains(order));// true

order.setCancelPrice(5000L);// cancelPrice 값 변경System.out.println("변경후 : " + orders.contains(order));// false
```

위와 같이 **동일한 객체임이도 `Set` 에 저장한 뒤에 필드 값을 변경하면 hashCode가 변경되면서 찾을 수 없게 되어버린다.** 이는 `@EqualsAndHashCode`의 문제라기 보다는 변경가능한 필드에 이를 남발함으로써 생기는 문제이다.

따라서,

- Immutable(불변) 클래스를 제외하고는 **아무 파라미터 없는 `@EqualsAndHashCode` 사용은 금지**한다.
- 일반적으로 비교에서 사용하지 않는 **Data 성 객체**는 `equals` & `hashCode`를 따로 구현하지 않는게 차라리 낫다.
- 항상 `@EqualsAndHashCode(of={“필드명시”})` 형태로 동등성 비교에 필요한 필드를 명시하는 형태로 사용한다.
- 실전에서는 누군가는 이에 대해 실수하기 마련인지라 차라리 사용을 완전히 금지시키고 IDE 자동생성으로 꼭 필요한 필드를 지정하는 것이 나을 수도 있다.
- [Java equals & hashCode](https://kwonnam.pe.kr/wiki/java/equals_hashcode) 좋은 `equals` 만드는 방법
- 막상 개발을 하다보면 온전히 Immutable 필드를 대상으로만 `equals & hashCode`를 만들기는 매우 어렵다. 최소한 꼭 필요하고 일반적으로 변하지 않는 필드에 대해서만 만들도록 노력해야 한다.
- [Equals Verifier](https://kwonnam.pe.kr/wiki/java/equals_verifier)를 통해 `equals, hashCode` 메소드 테스트를 자동으로 할 수 있다.

나는 위와 같은 이유로 [Apache Commons EqualsBuilder](https://commons.apache.org/proper/commons-lang/apidocs/index.html?org/apache/commons/lang3/builder/EqualsBuilder.html)의 `reflectionEquals`도 사용하지 말 것을 권한다.

## **@Data 사용금지**

[@Data](https://projectlombok.org/features/Data)는 파라미터 없는 `@EqualsAndHashCode`와 `@RequiredArgsConstructor` 등을 포함하는 Mutable한 데이터 클래스를 만들어주는 조합형 애노테이션이다.

바로 `@EqualsAndHashCode`와 `@RequiredArgsConstructor`를 포함하기 때문에 사용을 아예 금지하고, 차라리 다음과 같이 명시하는 것이 좋다.

```java
@Getter
@Setter
@ToString
public class Order {
...
// 생성자와 필요한 경우에만 equals, hashCode 직접 작성}
```

개발자에게 `@Data` 사용시 `equals`, `hashCode` 그리고 생성자를 직접 만들어주라고 “말해봐야” 아무 소용없다. 그냥 깔끔하게 금지시키는게 낫다.

## **@Value 사용금지**

[@Value](https://projectlombok.org/features/Value)는 Immutable 클래스를 만들어주는 조합 애노테이션이지만 이 또한 `@EqualsAndHashCode`, `@AllArgsConstructor` 를 포함한다. `@EqualsAndHashCode`는 불변 클래스라 큰 문제가 안되지만 `@AllArgsConstructor`가 문제가 된다.

아예 사용을 금지시키고 다음과 같이 만드는게 낫다.

```java
@Getter
@ToString
public class Order {
// private final 로 여러 필드 생성// 생성자와 필요한 경우에만 equals, hashCode 직접 작성}
```

## **@Builder 를 생성자나 static 객체 생성 메소드에**

[@Builder](https://projectlombok.org/features/Builder) 를 사용하면 객체 생성이 좀 더 명확하고 쉬워지는데, 이는 기본적으로 `@AllArgsConstructor`를 내포하고 있다. 이 자체는 평상시에는 큰 문제가 안된다. 생성자를 `package private`으로 만들기 때문에 외부에서 생성자를 호출하는 일은 쉽게 안생긴다. 하지만 그래도 해당 클래스의 다른 메소드에서 이렇게 자동으로 생성된 생성자를 사용하거나 할 때 문제 소지가 있다.

따라서 `@Builder` 애노테이션은 가급적 클래스 보다는 **직접 만든 생성자** 혹은 `static 객체 생성 메소드`에 붙이는 것을 권장한다. (생성자 부분에 예시 나옴)

## **@Log**

[@Log](https://projectlombok.org/features/log) 를 통해 각종 Logger 를 자동생성 할 수 있다. 이 때 기본적으로 `private static final`로 생성하는데, `static`이 아닌 필드로 만들고자 하거나 Logger 객체 이름을 변경하고자 한다면 `lombok.config`를 사용하면 된다.

```java
lombok.log.fieldName=logger # 로거 객체 이름을 logger로 변경. 원래는 log
lombok.log.fieldIsStatic=false # 로거를 static이 아닌 필드로 생성
```

또한, 가급적이면 `@Slf4j`만 사용하고 나머지는 사용할 수 없게 금지시키는 것도 가능하다. 자세한 것은 아래 `lombok.config` 항목에서 설명한다.

나는 (라이브러리성이 아닌) 일반 서비스에서는 Logger를 field 변수로 만들 필요 없이 static final로 하는 것을 선호한다. 그래야 별다른 처리 없이 static method에서도 호출 가능하기 때문이다.

## **@NonNull 사용 금지**

[@NonNull](https://projectlombok.org/features/NonNull) 사용금지는 사실 개인적 취향에 가까운데, 불필요하게 branch coverage를 증가시키기 때문이다(즉, 프로젝트 코드 커버리지를 유지하고 싶다면 null인 상황에서 오류발생과 그렇지 않은 상황에 대한 테스트를 모든 사용처에서 만들어야 한다). 그렇다고 이 코드를 일일이 테스트를 만들자니 이미 검증된 라이브러리의 기능을 사용하는 곳곳에서 모두 테스트를 만드는 것도 굉장히 소모적인 일이다.

나는 그래서 `@NonNull`을 사용하지 않고 [Guava](https://kwonnam.pe.kr/wiki/java/guava)의 [Preconditions](https://google.github.io/guava/releases/19.0/api/docs/com/google/common/base/Preconditions.html)로 null을 검증하고 오류 처리하는 것을 선호한다. `Preconditions.checkNotNull`은 branch를 만들지 않는다.

이 경우 에러 메시지를 명확하게 작성하는게 가능해지는 또 다른 장점도 생긴다.

```java
// null일 경우 "userName must not be null." 메시지로 예외 발생
Preconditions.checkNotNull(userName, "userName must not be null.")
```

## **@ToString, @EqualsAndHashCode 필드명 지정시 오타 문제**

**Lombok 1.18.0 부터 @ToString, @EqualsAndHashCode에 대해 필드, 혹은 메소드에 Include, Exclude 지정이 가능해졌다.** 따라서 아래 문제가 모두 해소 된다.

[@ToString](https://projectlombok.org/features/ToString) 과 [@EqualsAndHashCode](https://projectlombok.org/features/EqualsAndHashCode)에서는 파라미터로 특정 필드를 지정해서 처리 대상에 포함시키거(**of**)나 제외(**exclude**)시킬 수 있다.

헌데 문제는 이게 필드 이름을 String으로 지정한다는 점이다. 이로 인해 IDE 에서 필드명을 리팩토링할 때 올바로 반영이 안되거나, 아주 단순한 오타가 나도 눈치를 못 챌 수 있다. **보통 오타등으로 인해 잘못된 필드가 지정되면 Compile 시점에 warning이 출력된다. 하지만 warning일 뿐, error가 아닌지라 그마저도 모르고 넘어갈 가능성이 높다.**

현재 버전에서는 이를 error로 격상시킬 방법이 없다. 컴파일러 옵션 `-Werror`를 주면 warning시에도 오류를 내며 컴파일을 멈추는데, 또 다른 문제는 lombok과 관계없는 다른 너무 많은 경우에 대해서 에러를 내버린다.(java 8 현재 `-Xlint`를 통한 옵션 미세 조정이 제대로 작동을 안함. javac 버그로 보임)

이 문제는 [Jenkins Log Parser Plugin](https://wiki.jenkins.io/display/JENKINS/Log+Parser+Plugin) 으로 어느정도 해결 가능하다.

[Lombok Field 지정이 올바른지 검사](https://kwonnam.pe.kr/wiki/java/lombok/field_exist_verify) 에 관련 방법을 정리해두었다.

## **실무 프로젝트에서는 보수적으로 사용**

개인 Toy 프로젝트가 아닌 실무 프로젝트에서는 가급적 [@Getter, @Setter](https://projectlombok.org/features/GetterSetter), [@ToString](https://projectlombok.org/features/ToString) 만 사용하고 그 외의 것들 사용은 자제하거나 매뉴얼을 잘 읽어보고서 보수적으로 사용한다.

`@EqualsAndHashCode`는 Immutable 필드만 명시적으로 지정하면서 자제해서 사용하거나 가급적 IDE 코드 제너레이션등을 통해 코드를 직접 만든다.

특히 Experimental 기능은 사용하지 말고, IDE 지원이 확실치 못하고 문법 파괴적인 [val](https://projectlombok.org/features/val)도 사용하지 않는 것이 좋을 것 같다.

## **lombok.config를 통해 애노테이션 사용금지 및 각종 설정**

lombok 도 이렇게 일부 기능에 대해 사용금지가 필요하다고 느꼈는지, [Configuration System lombok.config](https://projectlombok.org/features/configuration)를 통해 다양한 설정이 가능하게 해두었다.

예를들어, 프로젝트 최상단 디렉토리에 `lombok.config` 파일을 만들고 다음과 같이 지정하면 `@Data`, `@Value`, `val`, `@NonNull`, `@AllArgsConstructor`, `@RequiredArgsConstructor` 등의 사용이 금지된다.

억지로 사용할 경우 컴파일 오류가 발생한다.

```java
config.stopBubbling = true
lombok.data.flagUsage=error
lombok.value.flagUsage=error
lombok.val.flagUsage=error
lombok.var.flagUsage=error
lombok.nonNull.flagUsage=error
lombok.allArgsConstructor.flagUsage=error
lombok.requiredArgsConstructor.flagUsage=error
lombok.cleanup.flagUsage=error
lombok.sneakyThrows.flagUsage=error
lombok.synchronized.flagUsage=error
# experimental 전체 금지
lombok.experimental.flagUsage=error

# 기타 각종 사용해서는 안되는 기능들을 모두 나열할 것.
```