# [Spring] IoC 컨테이너와 Bean

## IOC 컨테이너

- IOC(Inversion Of Control) : 의존 관계 주입(Dependency Injection)이라고도 하며, **어떤 객체가 사용하는 의존 객체를 직접 만들어 사용하는 것이 아닌, 주입을 받아서 사용하는 방법**

**스프링 IOC 컨테이너**Spring 애플리케이션에서 컴포넌트들의 중앙 저장소, Bean 설정 소스로 부터 Bean 정의를 읽어들이고, Bean을 구성하고 제공해주는 컨테이너. 또한 실제 IOC 컨테이너는 ApplicationContext 인터페이스를 구현한 클래스의 오브젝트이다.

**BeanFactory**스프링 프레임워크의 interface이며, 이 interface는 모든 유형의 개체를 관리할 수 있는 메커니즘을 제공한다. `ApplicationContext`는 이 `BeanFactory`의 하위 interface다.

**특징**

- Spring AOP 기능과 쉽게 통합
- MessageResource 처리 가능 (국제화)
- Event 발행
- 웹 애플리케이션에서 사용하기 위한 `WebApplicationContext` 같은 애플리케이션 계층 특정 컨텍스트 지원

## Bean

스프링 IOC 컨테이너가 관리하는 객체, Bean은 Spring IOC 컨테이너에 의해서 인스턴스화되고 조립 및 관리되는 객체이다.

**특징**

- 의존성 관리의 대상이 된다
- Scope
    - Singleton : 객체가 1번만 생성되는 것
    - ProtoType : 객체가 매번 새로 생성되는 것
- 라이프사이클 인터페이스 지원 가능

## Metadata 설정

스프링에서 IOC 컨테이너 구성하기 위한 메타 정보를 설정해주어야한다. 기본적으로 xml 형식을 통해 컨테이너 구성하기 위한 메타 데이터를 기술할 수 있으며, 이외에도 애노테이션 기반, java 파일 기반으로 컨테이너 구성이 가능하다.+) 추가적으로 xml형식, groovy형식, properties형식, yml형식도 가능하다

- 애노테이션 방식 : Spring 2.5 부터 주석 기반 메타 데이터 지원
- java 방식 : Spring 3.0 부터 java 파일을 통한 구성이 가능하며, 현재 스프링 부트가 이 방식을 채택하여 구성되어 있다.

## 예제

다음은 위 개념들인 기술을 사용하는 예시다.

![- BookRepository 클래스 -](https://media.vlpt.us/images/khsb2012/post/96d6d326-5797-41ff-9bb0-caec385151a4/bookRepository.PNG)

- BookRepository 클래스 -

![- BookService 클래스 -](https://media.vlpt.us/images/khsb2012/post/360fa1fd-f5e4-47d9-ba26-d543de5cf235/bookService.PNG)

- BookService 클래스 -

![ - application.xml(metadata config)](https://media.vlpt.us/images/khsb2012/post/7cec7066-5293-4236-a06e-bc5e3015673b/applicationConfig.PNG)

 - application.xml(metadata config)

위 application.xml 파일의 내용과 위 class 파일들을 비교하면서 보자

먼저 <bean> 태그들이 있으며 그 안에 id, class 속성이 있다.Bean 태그는 실제 IOC 컨테이너가 관리해줄 대상이 되는 Bean 객체들의 정보를 명시하는 태그다

- id : bead 의 id 정보, 이는 IOC컨테이너에 Bean으로 등록할 클래스명을 넣어주는 것이다. 표준은 camel 케이스 형식으로 id 값을 명시해주어야 한다.
- class : Bean 클래스가 위치하는 실제 정보를 명시해주어야한다.

<property> 태그는 다음과 같다 등록할 Bean 내부에 필드에 대한 참조를 명시하는 태그

- name : setter 메소드를 먼저 선언해주고 해당 필드명을 명시하는 것이다.
- ref : 해당 필드에 주입해줄 Bean 참조 정보를 명시, 예시에서는 BookRepository에 대한 의존성이 있으므로 BookRepository에 대한 Bean의 id 값을 명시한다.

application.xml 파일을 통해 IOC컨테이너를 구성하는 metadata 들을 기술해주었다. 비유를 하자면 application.xml은 일종의 설계도와 비슷한 역할을 해주는 것이다.

![테스트코드](https://media.vlpt.us/images/khsb2012/post/06267d3f-3f80-4d37-bee2-6a0bcb303cf3/%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%BD%94%EB%93%9C.PNG)

테스트코드

`ApplicationContext` 구현을 통해 IOC가 관리해주는 Bean 정보들을 꺼내오는 것이 가능하다.

`ClassPathXmlApplicationContext` 생성자에 metadata를 기술한 application.xml의 classpath 값을 인자로 넣어주면 해당 생성자가 인자로받은 xml 파일을 읽어 IOC 컨테이너를 구현화해서 ApplicationContext 타입 변수로 받을 수 있다.

이제 이 IOC 컨테이너를 활용해서 해당 컨테이너에 관리되고 있는 Bean 객체들의 정보를 읽고, 해당 객체를 주입받는 것이 가능해진다.

---

# 1. 스프링 IoC 컨테이너와 빈

### Inversion Of Control (제어의 역전)

- 객체에 대한 제어 흐름을 직접 제어하는 게 아니라, 외부에서 관리하는 것→ 스프링 IoC 컨테이너를 사용 가능 (꼭 Spring 에서만 사용되는 것이 아니라, 일반 POJO에서도 코드로 구현 가능하다.)

### Dependency Injection (의존 관계 주입)

- 어떤 객체가 사용하는 의존 객체를 개발자가 직접 코드로 구현하는 것이 아니라, 외부에서 주입받아 사용하는 방법을 말한다.

### Spring IoC 컨테이너

- [BeanFactory](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/beans/factory/BeanFactory.html) : 스프링 컨테이너 가장 최상단의 인터페이스 (reference docs 참고)
- Bean 설정 소스부터 Bean의 정의를 읽어들여 Bean을 구성하고 제공한다.

### Bean

- Spring IoC 컨테이너가 관리하는 객체
- Bean으로 등록이 되어 있어야 Spring IoC 컨테이너에 의해 의존성 주입이 가능하다.
- Spring IoC 컨테이너가 Bean의 LifeCircle 관리
- 기본적인 Scope은 싱글톤이다. (싱글톤 : 인스턴스 1개 / 프로토타입 : 매번 다른 인스턴스 생성)
    
    → 기본적으로 싱글톤으로 생성되기 때문에 메모리 관점, 런타임시 성능 최적화에도 효율적임
    

### [ApplicationContext](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/context/ApplicationContext.html)

- BeanFactory 인터페이스를 상속 받는 인터페이스
- 가장 많이 사용하는 인터페이스
- 메시지 소스 처리 기능 (i18n, internationalization, 메세지 다국화)
- 이벤트 발행 기능
- Resource 로딩 기능 등... reference docs 참고

### 🖊 참고

[BeanFactory (Spring Framework 5.0.8.RELEASE API)](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/beans/factory/BeanFactory.html)[ApplicationContext (Spring Framework 5.0.8.RELEASE API)](https://docs.spring.io/spring-framework/docs/5.0.8.RELEASE/javadoc-api/org/springframework/context/ApplicationContext.html)

---

# 2. ApplicationContext와 다양한 빈 설정 방법

### Spring IoC 컨테이너의 역할

- Bean Instance 생성
- 의존 관계 설정
- Bean을 제공

### ApplicationContext 설정 방법

- ClassPathXmlApplicationContext(XML) → 과거 방법
    
    ```java
    ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");       // xml 기반으로 bean 설정
    ```
    
- AnnotationConfigApplicationContext(Java) → Spring 2.5 버전부터 annotation 기반
- @Configuration 어노테이션을 통해, Config 파일 안에 있는 @Bean들을 빈을 등록
    
    ```java
    ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);   // java Config 파일을 기반으로 bean 설정
    ```
    

### @SpringBootApplication

- 해당 어노테이션이 붙어있는 클래스의 package를 기반으로 Bean 등록 → SpringBoot
    
    ```java
     @Target({ElementType.TYPE})
     @Retention(RetentionPolicy.RUNTIME)
     @Documented
     @Inherited
     @SpringBootConfiguration
     @EnableAutoConfiguration
     @ComponentScan
    ```
    

### Bean 설정

- Bean 에 대한 명세 및 정의를 담고 있다.이름, 클래스, Scope, constructor argument, property 등

### ComponentScan

- 설정 방법
    - XML 설정 (과거) : context:component-scan
    - 자바 설정 : @ComponentScan

---

# 3.@Autowired

@Autowired 애노테이션을 이용해서 필요한 의존 객체의 타입과 이름에 해당하는 빈을 찾아 주입한다.

required 옵션 : default가 true, false는 optional의 개념

사용할 수 있는 위치

### Constructor (Spring 4.3 부터는 생략 가능)

```java
@Autowired
   public BookService(BookRepository bookRepository){
       this.bookRepository = bookRepository;
   }
```

### Setter

```java
@Autowired(required = false)
   public void setBookRepository(BookRepository bookRepository) {
       this.bookRepository = bookRepository;
   }
```

### Field

```java
@Autowired(required = false)
    BookRepository bookRepository;
```

---

### 같은 타입의 Bean이 여러개 있다면 ?

`Consider marking one of the beans as @Primary, updating the consumer to accept multiple beans, or using @Qualifier to identify the bean that should be consumed`

- @Primary : Bean으로 만들고 싶은 클래스에 해당 애노테이션을 사용, (기본 값으로 사용하겠다는 의미)
- @Qualifier : Bean으로 주입받는 곳에 해당 애노테이션과 Bean이름 주입 (첫글자가 소문자인 카멜 케이스 이름, Spring Container가 최초에 빈 등록할 때의 이름 형식임) → TypeSafe 하지 않음
- 해당 타입의 Bean 이름을 모두 주입 받기
- 인스턴스 이름으로 구분 (정말 비추)
    
    ```
    // BookRepository 인터페이스를 상속받는 여러 클래스 중
    // MyBookResitory.class를 사용하겠다.
    @Autowired
    BookRepository myBookResitory;
    ```
    
    - Spring IoC의 최상단 인터페이스 BeanFactory의 LifeCycle을 보면 알 수 있다.
    - BookRepository가 myBookResitory를 사용할 것이라는 것은 Initialization 전에 해준다.( ref. `postProcessBeforeInitialization` methods of BeanPostProcessors)

---

# 4. Component와 컴포넌트 스캔

@ComponentScan은 Spring 3.1부터 적용이 되었다. @ComponentScan은 `basePackages(String Type)` 패키지 이름으로 컴포넌트 스캔을 하는 데, String값으로 패키지 이름을 받는 것은 TypeSafe하지 않다.

그래서 TypeSafe한 방법으로 `basePackageClasses` 라는 옵션을 사용한다.`basePackageClasses` 을 사용하면, 해당 옵션에 전달된 클래스가 있는 패키지를 기준으로 컴포넌트 스캔을 시작한다.→ SpringBoot에서 @SpringBootApplication 애노테이션은 @ComponentScan을 포함하고 있다.

따라서, @SpringBootApplication이 붙은 클래스의 Package와 다른 상속 Package에 있는 클래스들은 ComponetScan이 되지 않는다.

### 컴포넌트 스캔 주요 기능

- 스캔 위치를 설정한다.
- filter : 어떤 애노테이션을 스캔할 지 or 스캔하지 않을 지
    - excludeFilters → TypeExcludeFilter.class, AutoConfigurationExcludeFilter.class

### ComponentScan의 대상

- @Repository
- @Service
- @Controller
- @Configuration

### function을 이용한 Bean 등록

- Bean 등록 시, ComponentScan 에 해당되지 않는 빈들을 등록할 수 있다.
- 성능을 높일 수 있다. (하지만, 필요에 따라 사용해야 한다. 무조건 사용은 비추)

```java
// MyBean.class / ComPonentScan 외에 있는 bean을 function을 이용해서 등록이 가능하다.
public static void main(String[] args) {
	new SpringApplicationBuilder()
			.sources(Demospring51Application.class)
			.initializers((ApplicationContextInitializer<GenericApplicationContext>)
		applicationContext -> {
			applicationContext.registerBean(MyBean.class);
	})
	.run(args);
}
```

---

# 5. 빈의 스코프

### Scope

### singleton

- (Spring의 디폴트) 전체 application 내 해당 빈의 인스턴스가 오직 1개

### prototype

인스턴스를 여러개로 만들 수 있다.

- Request: request에 따라
- Session : 세선에 따라
- WebSocket : 웹 소켓에 따라

### prototype의 bean이 singleton을 참조하면 ?

참조하는 싱글톤은 늘 인스턴스가 1개이기 때문에 개발자의 의도대로 사용이 된다.

### Singleton의 bean이 prototype의 bean을 참조하면 ?

- 프로토타입의 빈이 변경이 안된다. → 싱글톤 빈이 최초 인스턴스화 될 때, 처음으로 만들어졌던 프로토타입의 인스턴스만을 참조하기 때문
- 업데이트 하려면 ?
    - `@Scope*(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)` : 프로토타입 클래스의 선언부에 다음과 같이 설정한다.
        - 해당 클래스를 Proxy로 감싸라. → 싱글톤 객체가 프로토타입 객체를 접근할 때 proxy를 거쳐서 참조하도록
        - 기존 JVM에 있는 proxy는 interface 기준으로만 proxy를 만들어 주는 데, 이 방법을 사용하면 CGLib을 사용하여 class도 proxy가 되도록 만들어준다.
    - `ObjectProvider`
    - `Provider`

---

# 출처

[Sptring IoC 컨테이너와 Bean](https://velog.io/@khsb2012/%EC%8A%A4%ED%94%84%EB%A7%81-IOC-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88%EC%99%80-Bean)

[Section 1. IoC 컨테이너와 빈 (1)](https://velog.io/@dev_jhjhj/Section-1.-IoC-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88%EC%99%80-%EB%B9%88-1)