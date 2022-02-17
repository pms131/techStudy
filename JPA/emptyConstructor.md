# [JPA] 왜 JPA의 Entity는 기본 생성자를 가져야 하는가?

### **왜 JPA의 Entity는 기본 생성자를 가져야 하는가?**

정확히 이야기하면 Entity는 반드시 파라미터가 없는 생성자가 있어야 하고, 이것은 public 또는 protected 이어야 한다. 이러한 궁금증을 가지게 된 이유는 setter의 제한을 위해 정적 팩토리 메소드의 도입에서 시작되었다.

---

### **setter의 제한**

Entity의 모든 필드에 public setter 메소드를 생성하는 것은 객체의 값의 변경을 열어두기 때문에 일관성을 보장할 수 없다. 또한 단순히 setter 이기 때문에 그 의도를 쉽게 파악할 수 없다. setter 메소드를 제공하는 것 보단 확실한 비즈니스가 드러나는 메소드를 제공하는 것이 더욱 바람직하다.

간단한 예시를 위하여 학생을 나타내는 Student와 학생의 소속을 나타내는 Department Entity를 선언하였다.

![https://blog.kakaocdn.net/dn/oZaI2/btq1z3IqlHE/r7kFfdQoeBwe9uFkNKNAm1/img.png](https://blog.kakaocdn.net/dn/oZaI2/btq1z3IqlHE/r7kFfdQoeBwe9uFkNKNAm1/img.png)

Student Entity

![https://blog.kakaocdn.net/dn/VLEAu/btq1yE3HjB1/41wzn2kw8XZEZkVN9DZMXK/img.png](https://blog.kakaocdn.net/dn/VLEAu/btq1yE3HjB1/41wzn2kw8XZEZkVN9DZMXK/img.png)

Department Entity

여기서 department의 name을 변경하기 위한 메소드를 선언할 것이다.

![https://blog.kakaocdn.net/dn/bfDcKT/btq1C1Xpgc5/BZAgaldRpKtr4WfsKC1eL1/img.png](https://blog.kakaocdn.net/dn/bfDcKT/btq1C1Xpgc5/BZAgaldRpKtr4WfsKC1eL1/img.png)

단순히 setName을 하는 것 보다 의미가 있는 메소드 이름을 사용하면 좀 더 직관적으로 해당 메소드의 역할을 확인할 수 있게 된다.

또한 객체의 일관성 유지를 위해서 객체 생성 시점에 값들을 세팅함으로써 setter의 사용을 줄일 수 있다. 이러한 객체 생성을 위해서는 기본적인 생성자를 제외하고도 정적 팩토리 메소드와 빌더 등 다양한 방법들이 존재한다.

이펙티브 자바를 읽던 도중 정적 팩토리 메소드에 대하여 알게 되었고 이것을 적용해보기로 하였다.

---

### **정적 팩토리 메소드**

이펙티브 자바 읽던 중 이러한 기법을 소개하였다.

> 아이템 1. 생성자 대신 정적 팩토리 메소드를 고려하라
> 

여기서 말하는 팩토리 메소드는 Gof의 디자인 패턴에 나오는 팩토리 메소드 패턴과는 다른 패턴이고, 어떤 패턴과도 맞아 떨어지지 않기 때문에 혼동에 주의해야 한다.

정적 팩토리 메소드에는 크게 5 가지의 장점을 가지고 있다.

- 첫 번째, 이름을 가질 수 있다.

- 두 번째, 호출 될 때마다 인스턴스를 새로 생성하지는 않아도 된다.

- 세 번째, 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.

- 네 번째, 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.

- 다섯 번째, 정적 팩터리 메소드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.

정적 팩토리 메소드의 자세한 내용들은 이펙티브 자바 책과 [johngrib.github.io/wiki/static-factory-method-pattern/](https://johngrib.github.io/wiki/static-factory-method-pattern/) 를 참고하여 이해하였다.

위의 내용을 기반으로 Student와 Department의 정적 팩토리 메소드를 생성하였다.

![https://blog.kakaocdn.net/dn/l1JtX/btq1yZmePCo/Kw0zNVYIUZLkY2K0wbOoDK/img.png](https://blog.kakaocdn.net/dn/l1JtX/btq1yZmePCo/Kw0zNVYIUZLkY2K0wbOoDK/img.png)

![https://blog.kakaocdn.net/dn/bpEVbZ/btq1yZNg19B/Mlqd7jy89bN7IUqOe0pwY0/img.png](https://blog.kakaocdn.net/dn/bpEVbZ/btq1yZNg19B/Mlqd7jy89bN7IUqOe0pwY0/img.png)

private로 생성자를 만들면 외부에서 생성자를 통한 생성을 제한하기 때문에 오로지 의도한 대로 정적 팩토리 메소드를 통해서만 생성이 가능해진다.

하지만 여기서 문제가 생겼다! public 혹은 protecd의 default 생성자가 필요하다고 한다.

![https://blog.kakaocdn.net/dn/cwoZzn/btq1viz5vyR/nFx4ISLtRaQorFhksVN0K0/img.png](https://blog.kakaocdn.net/dn/cwoZzn/btq1viz5vyR/nFx4ISLtRaQorFhksVN0K0/img.png)

public 또는 protected 생성자를 만들면 해당 에러는 해결할 수 있다. 하지만 그렇게 되면 무분별한 생성을 제한하기 위해 만든 private 생성자가 무용지물이 되어 버린다. 그럼에도 불구하고 필요한 이유는 무엇인지 찾아보았다.

---

### **Java Reflection API**

> Java Reflection API란?구체적인 클래스 타입을 알지 못해도 그 클래스의 메소드, 타입, 변수들에 접근할 수 있도록 해주는 Java API
> 

이러한 Java Reflection을 활용하면 컴파일 시점이 아닌 런타임 시점에 동적으로 클래스를 객체화 하여 분석 및 추출 해낼 수 있는 프로그래밍 기법이다.

자세한 내용은 [woowacourse.github.io/javable/post/2020-07-16-reflection-api/](https://woowacourse.github.io/javable/post/2020-07-16-reflection-api/) 을 참고하여 이해하였다.

JPA는 DB 값을 객체 필드에 주입할 때 기본 생성자로 객체를 생성한 후 이러한 Reflection을 사용하여 값을 매핑하기 때문이다.

간단히 기본 생성자를 **주석** 처리하고 Student의 list를 조회하는 테스트를 작성하였다.

![https://blog.kakaocdn.net/dn/VzHyd/btq1zsIysxT/hLC3hvlKHmyLpud9DsPKPK/img.png](https://blog.kakaocdn.net/dn/VzHyd/btq1zsIysxT/hLC3hvlKHmyLpud9DsPKPK/img.png)

![https://blog.kakaocdn.net/dn/cpW24h/btq1xlpP4mZ/zVRKN70Ma6TlKILCSYoZv0/img.png](https://blog.kakaocdn.net/dn/cpW24h/btq1xlpP4mZ/zVRKN70Ma6TlKILCSYoZv0/img.png)

![https://blog.kakaocdn.net/dn/dmD2dY/btq1yY1VZ76/n3MEgYP1jCS48NafK1eQpk/img.png](https://blog.kakaocdn.net/dn/dmD2dY/btq1yY1VZ76/n3MEgYP1jCS48NafK1eQpk/img.png)

우선 department와 student1, student2를 생성하여 DB에 저장한다.

![https://blog.kakaocdn.net/dn/bzAub9/btq1C2B3W2p/YeBwIUwJgYUeYpFqth5Dk1/img.png](https://blog.kakaocdn.net/dn/bzAub9/btq1C2B3W2p/YeBwIUwJgYUeYpFqth5Dk1/img.png)

h2 database로 연동하여 저장된 데이터를 확인하였다. 그 다음 조회를 진행한다.

![https://blog.kakaocdn.net/dn/bwq0Sh/btq1xlKab4r/GZ9py4CE00BMYoxIR5I4W1/img.png](https://blog.kakaocdn.net/dn/bwq0Sh/btq1xlKab4r/GZ9py4CE00BMYoxIR5I4W1/img.png)

Student list를 조회하는 시점에 기본 생성자의 부재로 예외를 던지는 것을 확인할 수 있었다.

---

### **Proxy 객체**

JPA는 매핑한 Entity를 조회할 때 두 가지 전략을 사용한다. 조회 시점에 함께 가져오는 EAGER와 매핑한 Entity를 사용할 때 조회하는 LAZY가 있다.

이러한 LAZY, 지연로딩을 사용할 경우에는 student의 department에 department의 proxy 객체가 들어있다.

아주 간단한 예시를 위한 코드를 작성하였다.

![https://blog.kakaocdn.net/dn/5swJI/btq1AFgjy98/RLzrNdG03wpeLRg15mwkZk/img.png](https://blog.kakaocdn.net/dn/5swJI/btq1AFgjy98/RLzrNdG03wpeLRg15mwkZk/img.png)

각각의 테스트를 실행하기 전에 수행하는 beforeEach이다.

![https://blog.kakaocdn.net/dn/WYgPz/btq1C2hN3ce/DILEvkKRA219YyMhWqOJck/img.png](https://blog.kakaocdn.net/dn/WYgPz/btq1C2hN3ce/DILEvkKRA219YyMhWqOJck/img.png)

빠른 확인을 위하여 간단하게 entity manager는 flush 하고 clear하여 비워두었다. 그 다음 student의 모든 list를 가져오게 되면 영속성 컨텍스트에 저장될 것이다.

![https://blog.kakaocdn.net/dn/NMSoP/btq1vhBhYyL/DdkIokyc5oqcQDIkOEvzw1/img.png](https://blog.kakaocdn.net/dn/NMSoP/btq1vhBhYyL/DdkIokyc5oqcQDIkOEvzw1/img.png)

이제 findAll() 메소드를 활용하여 DB의 모든 student list를 가져온다. student Entity의 경우 class를 출력해보면 필자가 생성한 Student 객체임을 확인할 수 있었다. 하지만 Student class의 필드인 department의 경우 hibernate가 생성한 proxy 객체가 들어 있는 것을 확인 할 수 있다.

<aside>
💡 이것은 지연로딩으로 설정하였기 때문에 임시로 hibernate가 생성한 proxy 객체를 가리키는 것이다. 이러한 proxy 객체는 직접 만든 **Department class를 상속하기 때문에 public 혹은 protected 기본 생성자가 필요**하게 된다. 결국 public, protected 생성자가 없다면 이러한 proxy 객체를 사용할 수 없을 것이다.

</aside>

지연로딩과 즉시로딩에 대해서는 좀 더 공부를 통하여 정리할 예정이다.

---

<aside>
💡 정리하면 JPA의 구현체인 hibernate에서 제공하는 다양한 기능을 활용하기 위해서는 public이나 protected 기본 생성자가 필요하다. private로 생성자를 만들게 되면 이러한 기능들을 사용하는데 제약이 되기 때문이다. 
다만 안정성 측면에서 좀 더 작은 scope 가진 protected 기본 생성자를 주로 사용해야 겠다. 더 나아가 혼자가 아닌 다른 팀원들과 협업을 진행하게 된다면 객체 생성을 어떤 식으로 해결해야 할지 고민하고 관련 내용들을 공유하여 무분별한 생성을 막아야 겠다는 생각이 들었다.

</aside>

### (정리) JPA에서 기본 생성자(No-args)가 필요한 이유

<aside>
💡 다시 원론으로 돌아가면,Java Reflection API를 사용하면 해당 클래스의 매서드, 맴버 변수, 변수 타입 등을 알 수 있다.
하지만 **가져올 수 없는 정보 중 하나가 생성자의 인자 정보**들이다.

따라서 기본 생성자가 없다면 java Reflection은 해당 객체를 생성 할 수 없기 때문에 JPA의 Entity에는 기본 생성자가 필요하다.

</aside>

---

# 출처

[[JPA] 왜 JPA의 Entity는 기본 생성자를 가져야 하는가?](https://hyeonic.tistory.com/191)

[Spring, JPA에 기본 생성자가 필요한 이유](https://velog.io/@yebali/Spring-JPA%EC%97%90-%EA%B8%B0%EB%B3%B8-%EC%83%9D%EC%84%B1%EC%9E%90%EA%B0%80-%ED%95%84%EC%9A%94%ED%95%9C-%EC%9D%B4%EC%9C%A0)