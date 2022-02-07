# [Spring] Bean LifeCycle

---

---

# **스프링 빈 라이프 사이클 요약**

- 객체 생성 -> 의존관계 주입 (생성자 주입 제외)
- **생성자 주입의 경우 객체 생성과 함께 의존관계 주입**

# **스프링 빈 이벤트 라이프 사이클**

- 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> **초기화 콜백 메서드 호출** -> 애플리케이션에서 빈 사용 -> **소멸 전 콜백 메서드 호출** -> 스프링 종료
- 스프링은 의존관계 주입이 완료될 경우 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려줌
- 또한, 스프링 컨테이너가 종료되기 직전에 소멸 전 콜백 메서드를 통해 소멸 시점을 알려줌
- 보다 자세한 내용은 [https://knpcode.com/spring/spring-bean-lifecycle-callback-methods/](https://knpcode.com/spring/spring-bean-lifecycle-callback-methods/) 참고!
- 스프링 빈 라이프 사이클을 압축시키기 위해 생성자 주입을 통해 빈 생성과 동시에 초기화를 모두 진행하면 되지 않을까?
    
    → 생성자는 필수 파라미터를 받고 메모리를 할당해서 객체를 생성하는 역할
    
    → 반면 초기화 과정은 이렇게 생성된 값을 활용해서 외부 커넥션을 연결하는 것처럼 무거운 동작을 수행 **(따라서, 분리하는 것이 좋음)**
    

# **빈 생명주기 콜백 종류 3가지**

1. 스프링에서 제공하는 인터페이스 (InitializingBean, DisposableBean)
2. 설정 정보에서 초기화 메서드, 종료 메서드 지정하는 방법
3. **@PostConstruct, @PreDestroy 어노테이션**

## ***1. 스프링에서 제공하는 인터페이스***

```java
public class ExampleBean implements InitializingBean, DisposableBean { 
		// 중략 

		@Override 
		public void afterPropertiesSet() throws Exception { 
				// 초기화 
		} 
		
		@Override public void destroy() throws Exception { 
				// 메모리 반납, 연결 종료와 같은 과정 
		} 
}

```

### **이 방식의 단점**

- 스프링 전용 인터페이스에 코드가 의존
- 메서드를 오버라이드하기 때문에 메서드명 변경 불가능
- 코드를 커스터마이징할 수 없는 외부 라이브러리에 적용 불가능

## ***2. 설정 정보에서 초기화 메서드, 종료 메서드 지정하는 방법***

```java
public class ExampleBean {
    // 중략
    public void initialize() throws Exception {
        // 초기화
    }

    public void close() throws Exception {
        // 메모리 반납, 연결 종료와 같은 과정
    }
}

@Configuration
static class LifeCycleConfig {
    @Bean(initMethod = "initialize", destroyMethod = "close")
    public ExampleBean exampleBean() {
        // 생략
    }
}
```

### **이 방식의 장점**

- 메서드 이름을 자유롭게 부여 가능
- 스프링 코드에 의존하지 않음
- 설정 정보를 사용하기 때문에 코드를 커스터마이징 할 수 없는 외부 라이브러리에서도 적용 가능

### **이 방식의 단점**

- Bean 지정 시 initMethod와 destroyMethod를 직접 지정해야 하는 번거로움

### **@Bean의 destroyMethod 속성의 특징**

- 라이브러리는 대부분 종료 메서드명이 close 혹은 shutdown
- @Bean의 destroyMethod는 기본값이 inferred (추론)으로 등록 즉, close, shutdown라는 이름의 메서드가 종료 메서드라고 추론하고 자동으로 호출해줌
- 즉, 종료 메서드를 따로 부여하지 않더라도 잘 작동
- inferred 기능을 사용하기 싫다면 명시적으로 destoroyMethod="" 와 같이 공백을 지정해줘야 함

## ***3. @PostConstruct, @PreDestroy 어노테이션***

```java
public class ExampleBean {
  
  // 중략
  
  @PostConstruct
  public void initialize() throws Exception {
    // 초기화 
  }
  
  @PreDestroy
  public void close() throws Exception {
    // 메모리 반납, 연결 종료와 같은 과정 
  }
}

@Configuration
static class LifeCycleConfig {

  @Bean
  public ExampleBean exampleBean() {
    // 생략
  }
}
```

### **이 방식의 장점**

- **최신 스프링에서 권장하는 방법!**
- 어노테이션만 붙이면 되기 때문에 편리함
- 스프링에 종속적인 기술이 아니라 자바 표준 코드이기 때문에 다른 컨테이너에서도 동작
- 컴포넌트 스캔과 잘 어울림

### **이 방식의 유일한 단점**

- 커스터마이징이 불가능한 외부 라이브러리에서 사용 불가능 (외부 라이브러리에서 초기화, 종료를 해야 할 경우 두 번째 방법 즉, @Bean의 initMethod와 destroyMethod 키워드를 사용)

---

# 출처

[](https://jaimemin.tistory.com/1787)

인프런 스프링 핵심 원리 - 기본편 (김영한 강사님)