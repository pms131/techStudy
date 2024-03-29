# 제목 없음

---

# Spring

## **Q2. Spring을 사용하면 어떤 이점이 있습니까?**

자카르타 EE 개발을 더 쉽게 만드는 Spring 목표. 사용의 장점은 다음과 같습니다.

- **경량 :**
    
    개발에서 프레임 워크를 사용하는 데 약간의 오버 헤드가 있습니다.
    
- **IoC (Inversion of Control) :**
    
    Spring 컨테이너는 종속 개체를 생성하거나 찾는 대신 다양한 개체의 연결 의존성을 처리합니다.
    
- **AOP (Aspect Oriented Programming) :**
    
    Spring은 시스템 서비스에서 비즈니스 로직을 분리하기 위해 AOP를 지원합니다.
    
- **IoC 컨테이너 :**
    
    Spring Bean 라이프 사이클 및 프로젝트 별 구성을 관리합니다.
    
- **MVC 프레임 워크 :**
    
    XML / JSON 응답을 반환 할 수있는 웹 애플리케이션 또는 RESTful 웹 서비스를 만드는 데 사용됩니다.
    
- **트랜잭션 관리 :**
    
    Java 어노테이션을 사용하거나 Spring Bean XML 구성 파일을 사용하여 JDBC 작업, 파일 업로드 등에서 상용구 코드의 양을 줄입니다.
    
- **예외 처리 :**
    
    Spring은 기술 별 예외를 확인되지 않은 예외로 변환하기위한 편리한 API를 제공합니다.
    

## **Q4. 의존성 주입이란?**

IoC (Inversion of Control)의 한 측면 인 의존성 주입은 개체를 수동으로 생성하지 않고 대신 생성 방법을 설명하는 일반적인 개념입니다. IoC 컨테이너는 필요한 경우 필요한 클래스를 인스턴스화합니다.

## **Q5. Spring에 Bean을 어떻게 주입 할 수 있습니까?**

몇 가지 다른 옵션이 있습니다.

- 세터 주입
- 생성자 주입
- Field 주입

구성은 XML 파일 또는 어노테이션을 사용하여 수행 할 수 있습니다.

## **Q7. Beanfactory와 Applicationcontext의 차이점은 무엇입니까?**

*BeanFactory* 는 Bean 인스턴스를 제공하고 관리하는 컨테이너를 나타내는 인터페이스입니다. 기본 구현 은 *getBean ()* 이 호출 될 때 빈을 느리게 인스턴스화 합니다.

*ApplicationContext* 는 *애플리케이션의* 모든 정보, 메타 데이터 및 Bean을 보유하는 컨테이너를 나타내는 인터페이스입니다. 또한 *BeanFactory* 인터페이스를 확장 하지만 기본 구현은 애플리케이션이 시작될 때 열심히 빈을 인스턴스화합니다. 이 동작은 개별 Bean에 대해 재정의 될 수 있습니다.

## Q**. 그럼 Path Variable과 Query Parameter를 각각 언제 사용해야 하는가?**

만약 어떤 **resource를 식별**하고 싶으면 **Path Variable**을 사용하고,**정렬이나 필터링**을 한다면 **Query Parameter**를 사용하는 것이 Best Practice이다.

## **Q25. Aspect-Oriented Programming이란 무엇입니까?**

*Aspect* 는 영향을받는 클래스를 수정하지 않고 기존 코드에 추가 동작을 추가하여 여러 유형 및 객체에 걸쳐있는 트랜잭션 관리와 같은 교차 절단 문제를 모듈화 할 수 있도록합니다.

다음은 [aspect 기반 실행 시간 로깅](https://recordsoflife.tistory.com/spring-aop-annotation) 의 예입니다 .

## **Q26. Aop에서 Aspect, Advice, Pointcut 및 Joinpoint는 무엇입니까?**

- ***Aspect***
    
    : 트랜잭션 관리와 같은 교차 문제를 구현하는 클래스
    
- ***Advice** JoinPoint*
    
    :애플리케이션에서 *Pointcut* 과 일치하는 특정
    
    에도달했을 때 실행되는 메소드
    
- ***Pointcut** JoinPoint*
    
    : *Advice* 를 실행해야하는지 여부를 결정하기 위해
    
    와 일치하는 정규식 세트
    
- ***JoinPoint***
    
    : 메소드 실행 또는 예외 처리와 같은 프로그램 실행 중 지점
    

## Q.  **디스패처 서블릿(Dispatcher Servlet)이란?**

디스패처 서블릿이란 톰캣과 같은 서블릿 컨테이너를 통해 들어오는 모든 요청을 제일 앞에서 받는 컨트롤러입니다. 디스패처 서블릿은 공통된 작업을 처리한 후에, 적절한 세부 컨트롤러로 작업을 위임해줍니다. 그리고 각각의 세부 컨트롤러는 처리할 부분을 처리하고 반환할 view를 Dispatcher Servlet에 넘기게 됩니다.

![https://blog.kakaocdn.net/dn/MRXX3/btqK7014LOZ/wMKgG9RVkdZR7Ag5nD4wh0/img.png](https://blog.kakaocdn.net/dn/MRXX3/btqK7014LOZ/wMKgG9RVkdZR7Ag5nD4wh0/img.png)

## Q. **트랜잭션의 ACID란?**

- 원자성(Atomicity): 트랜잭션에 포함된 작업은 전부 수행되거나 전부 수행되지 않아야 한다.
- 일관성(Consistency): 트랜잭션을 수행하기 전이나 후나 데이터베이스는 항상 일관된 상태를 유지해야 한다.
- 고립성(Isolation): 수행 중인 트랜잭션에 다른 트랜잭션이 끼어들어 변경중인 데이터 값을 훼손하지 않아야한다.
- 지속성(Durability): 수행을 성공적으로 완료한 트랜잭션은 변경한 데이터를 영구히 저장해야 한다.

## Q.  **Byte Ordering이란**

Byte Ordering이란 데이터가 저장되는 순서를 의미합니다. Byte Ordering의 방식에는 빅엔디안(Big Endian)과 리틀엔디안(Little Endian)이 있습니다.

### Big Endian

- MSB가 가장 낮은 주소에 위치하는 저장 방식
- 네트워크에서 데이터를 전송할 때 주로 사용됨
- 가장 낮은 주소에 MSB가 저장되므로, offset=0인 Byte를 보면 양수/음수를 바로 파악할 수 있다.

### Little Endian

- MSB가 가장 높은 주소에 위치하는 방식
- 마이크로프로세서에서 주로 사용된다.
- 가장 낮은 주소에 부호값이 아닌 데이터가 먼저 오기 때문에, 바로 연산을 할 수 있다.

## Q. Thread vs Process

- 프로세스
    - 정의: 메모리에 올라와 실행되고 있는 프로그램의 인스턴스
    - 특징
        - 운영체제로부터 독립된 메모리 영역을 할당받는다. (다른 프로세스의 자원에 접근 X)
        - 프로세스들은 독립적이기 때문에 통신하기 위해 IPC를 사용해야 한다.
        - 프로세스는 최소 1개의 쓰레드(메인 쓰레드)를 가지고 있다.
- 쓰레드
    - 정의: 프로세스 내에서 할당받은 자원을 이용해 동작하는 실행 단위
    - 특징
        - 쓰레드는 프로세스 내에서 Stack만 따로 할당 받고, Code, Data, Heap 영역은 공유한다.(Stack을 분리한 이유는 Stack에는 함수의 호출 정보가 저장되는데, Stack을 공유하면 LIFO 구조에 의해 실행 순서가 복잡해지기 때문에 실행 흐름을 원활하게 만들기 위함이다.)
        - 쓰레드는 프로세스의 자원을 공유하기 때문에 다른 쓰레드에 의한 결과를 즉시 확인할 수 있다.
        - 프로세스 내에 존재하며 프로세스가 할당받은 자원을 이용하여 실행된다.

## Q. **멀티 쓰레드 프로그래밍 작성 시 유의점**

멀티 쓰레드 프로그램을 개발한다면, 다수의 쓰레드가 공유 데이터에 동시에 접근하는 경우에 상호배제 또는 동기화 기법을 통해 동시성 문제 또는 교착 상태가 발생하지 않도록 주의해야 합니다.

## Q. **세마포어(Semaphore) vs 뮤텍스(Mutex) 차이**

뮤텍스는 Locking 메커니즘으로 락을 걸은 쓰레드만이 임계 영역을 나갈때 락을 해제할 수 있습니다. 하지만 세마포어는 Signaling 메커니즘으로 락을 걸지 않은 쓰레드도 signal을 사용해 락을 해제할 수 있습니다. 세마포어의 카운트를 1로 설정하면 뮤텍스처럼 활용할 수 있습니다.

## Q. **OOP란**

OOP는 현실 세계를 프로그래밍으로 옮겨와 현실 세계의 사물들을 객체로 보고, 그 객체로부터 개발하고자 하는 특징과 기능을 뽑아와 프로그래밍하는 기법입니다. OOP로 코드를 작성하면 재사용성과 변형가능성을 높일 수 있습니다.

## Q. **OOP의 5가지 설계 원칙**

- SRP(Single Responsibility Principle, 단일 책임 원칙): 클래스는 단 하나의 목적을 가져야 하며, 클래스를 변경하는 이유는 단 하나의 이유여야 한다.
- OCP(Open-Closed Principle, 개방 폐쇠 원칙): 클래스는 확장에는 열려 있고, 변경에는 닫혀 있어야 한다.
- LSP(Liskov Substitution Principle, 리스코프 치환 원칙): 상위 타입의 객체를 하위 타입으로 바꾸어도 프로그램은 일관되게 동작해야 한다.
- ISP(Interface Segregation Principle, 인터페이스 분리 원칙): 클라이언트는 이용하지 않는 메소드에 의존하지 않도록 인터페이스를 분리해야 한다.
- DIP(Dependency Inversion Principle, 의존 역전 법칙): 클라이언트는 추상화(인터페이스)에 의존해야 하며, 구체화(구현된 클래스)에 의존해선 안된다.

## Q. Overloading vs Overriding

### Overloading(오버로딩)

- ***같은 이름의 메소드***를 여러개 정의하는 것
- 매개변수의 타입이 다르거나 개수가 달라야 한다.
- return type과 접근 제어자는 영향을 주지 않음.

### Overriding(오버라이딩)

- 상속에서 나온 개념
- 상위 클래스(부모 클래스)의 메소드를 하위 클래스(자식 클래스)에서 ***재정의***

## Q. C**all By Value와 Call By Reference 차이**

- Call By Value
    - 인자로 받은 값을 복사하여 처리하는 방식
    - Call By Value에 의해 넘어온 값을 증가시켜도 원래의 값이 보존된다.
    - 값을 복사하여 넘기기 때문에 메모리 사용량이 늘어난다.
- Call By Reference
    - 인자로 받은 값의 주소를 참조하여 직접 값에 영향을 주는 방식
    - 값을 복사하지 않고 직접 참조하기 때문에 속도가 빠르다.
    - 원래의 값에 영향을 주는 리스크가 존재한다.

## Q. **프레임워크와 라이브러리 차이**

- 라이브러리: 사용자가 흐름에 대한 제어를 하며 필요한 상황에 가져다가 쓸 수 있다.
- 프레임워크: 전체적인 흐름을 자체적으로 제어한다.

프레임워크와 라이브러리는 실행 흐름에 대한 제어 권한이 어디 있는지에 따라 달라집니다. 프레임워크를 사용하면 사용자가 관리해야 하는 부분을 프레임워크에 넘김으로써 신경써야 할 것을 줄이는 제어의 역전(IoC, Inversion Of Control)이 적용됩니다.

## Q. Session과 Cookie

### Session과 Cookie 사용 이유

- 현재 우리가 인터넷에서 사용하고 있는 HTTP프로토콜은 연결 지향적인 성격을 버렸기 때문에 새로운 페이지를 요청할 때마다 새로운 접속이 이루어지며 이전 페이 지와 현재 페이지 간의 관계가 지속되지 않는다. 이에 따라 HTTP프로토콜을 이용하게 되는 웹사이트에서는 웹페이지에 특정 방문자가 머무르고 있는 동안에 그 방문 자의 상태를 지속시키기 위해 쿠키와 세션을 이용한다.

### Session

- 특정 웹사이트에서 사용자가 머무르는 기간 또는 한 명의 사용자의 한번의 방문을 의미한다.
- Session에 관련된 데이터는 **Server에 저장**된다.
- 웹 브라우저의 캐시에 저장되어 **브라우저가 닫히거나 서버에서 삭제시 사라진다.**
- Cookie에 비해 보안성이 좋다.

### Cookie

- 사용자 정보를 유지할 수 없다는 HTTP의 한계를 극복할 수 있는 방법
- 인터넷 웹 사이트의 방문 기록을 남겨 사용자와 웹 사이트 사이를 매개해 주는 정보이다.
- Cookie는 인터넷 사용자가 특정 웹서버에 접속할 때, 생성되는 개인 아이디와 비밀번호, 방문한 사이트의 정보를 담은 임시 파일로써, Server가 아닌 Client에 텍스트 파일로 저장되어 다음에 해당 웹서버를 찾을 경우 웹서버에서는 그가 누구인지 어떤 정보를 주로 찾았는지 등을 파악할 때 사용된다.
- Cookie는 Client PC에 저장되는 정보기 때문에, 다른 사용자에 의해서 임의로 변경이 가능하다.(정보 유출 가능, Session보다 보안성이 낮은 이유)

### 보안성이 낮은 Cookie 대신 Session을 사용하면 되는데 안하는 이유?

- 모든 정보를 Session에 저장하면 Server의 메모리를 과도하게 사용하게 되어 Server에 무리가 가기 때문이다.

## 접근제한자(public > protected > default > private)

- public - 접근 제한이 없다.(같은 프로젝트 내에 어디서든 사용가능)
- protected - 같은 패키지 내, 다른 패키지에서 상속받아 자손클래스에서 접근 가능
- default - 같은 패키지 내에서만 접근 가능
- private - 같은 클래스 내에서만 접근 가능

## 자바 컬렉션의 대표 인터페이스

- List : 순서가 있는 데이터의 집합으로 데이터의 중복을 허용하며 인덱스로 조회한다.
- Set : 순서를 유지하지 않는 데이터의 집합으로 데이터의 중복을 허용하지 않는다.
- Map : 키와 벨류값의 쌍으로 이루어진 데이터 집합으로 순서는 유지되지 않으며 키는 중복은 허용하지 않지만 벨류값은 중복을 허용한다.

## @Transactional

### Propagation

우리는 Spring이 트랜잭션을 어떻게 진행시킬지 결정하도록 전파 속성을 전달해야 하는데, 이를 통해 새로운 트랜잭션을 시작할지 또는 기존의 트랜잭션에 참여할지 등을 결정하게 된다.

Spring이 지원하는 전파 속성은 다음의 7가지가 있다.

- REQUIRED
- SUPPORTS
- MANDATORY
- REQUIRES_NEW
- NOT_SUPPORTED
- NEVER
- NESTED

### Isolation

트랜잭션 격리수준은 동시에 여러 트랜잭션이 진행될 때 트랜잭션의 작업 결과를 여타 트랜잭션에게 어떻게 노출할 것인지를 결정한다. 스프링은 다음의 5가지 격리수준 속성을 지원한다.

- DEFAULT
- READ_UNCOMMITTED
- READ_COMMITTED
- REPEATABLE_READ
- SERIALIZABLE

### ReadOnly

### Timeout

### Rollback/Commit 예외
