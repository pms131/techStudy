# [Spring] Proxy형태로 동작하는 JPA @Transactional

예제 코드는 아래 github에 작성되어 있습니다.

**[https://github.com/cobiyu/transactional_proxy](https://github.com/cobiyu/transactional_proxy)**

## **[@Transactional (왜 이렇게 동작하지..?)](https://cobbybb.tistory.com/17#%--Transactional%---%EC%--%-C%--%EC%-D%B-%EB%A-%--%EA%B-%-C%--%EB%-F%--%EC%-E%--%ED%--%--%EC%A-%----%-F-)**

JPA를 사용하면서 가끔은 무의식적으로 @Transactional 을 사용하게 됩니다.

@Transactional 관련 개발 중 접하기 쉬운 제일 쉬운 오류는 아래 2가지 정도가 있습니다.

### **[1. private은 @Transactional이 적용되지 않는다.](https://cobbybb.tistory.com/17#--%--private%EC%-D%--%--%--Transactional%EC%-D%B-%--%EC%A-%--%EC%-A%A-%EB%--%--%EC%A-%--%--%EC%--%-A%EB%-A%--%EB%-B%A--)**

private method는 @Transactional을 적용할 경우 아래처럼 오류 메세지가 뜨게됩니다.

> Methods annotated with '@Transactional' must be overridable
> 

![https://blog.kakaocdn.net/dn/bN7z54/btqWNcEbjQ6/XZIAzFpEfJjNkpVKER8S8k/img.png](https://blog.kakaocdn.net/dn/bN7z54/btqWNcEbjQ6/XZIAzFpEfJjNkpVKER8S8k/img.png)

@Transactinonal 불가한 private method

### **[2. 같은 클래스 내의 여러 @Transactional method 호출](https://cobbybb.tistory.com/17#--%--%EA%B-%--%EC%-D%--%--%ED%--%B-%EB%-E%--%EC%-A%A-%--%EB%--%B-%EC%-D%--%--%EC%--%AC%EB%-F%AC%--%--Transactional%--method%--%ED%--%B-%EC%B-%-C)**

같은 클래스 내에 여러 @Transactional method를 호출할 경우 의도와는 다르게 Transaction이 동작하는 경우가 있습니다.

### [예제](https://cobbybb.tistory.com/17#%EC%--%--%EC%A-%-C)

(github 예제코드 **[UserService.java](https://github.com/cobiyu/transactional_proxy/blob/master/src/main/java/com/cobi/transactional/aop/UserService.java)** 참고)

UserService.java의 createUserListWithTrans()는 createUser를 이용해서 10명의 user를 생성하는 method입니다.

```java
// UserService.java

@Transactional
public void createUserListWithTrans(){
    for (int i = 0; i < 10; i++) {
        createUser(i);
    }
}

@Transactional
public User createUser(int index){
    User user = User.builder()
            .name("testname::"+index)
            .email("testemail::"+index)
            .build();

    userRepository.save(user);
    return user;
}
```

createUserListWithTrans()를 실행 후 다시 select해보면 정상적으로 User 10개가 생성된것을 알 수 있습니다.(아래 결과 참고)

![https://blog.kakaocdn.net/dn/tYavD/btqWUMqETeJ/y9czEeBCZXP0KbcqA2iD7K/img.png](https://blog.kakaocdn.net/dn/tYavD/btqWUMqETeJ/y9czEeBCZXP0KbcqA2iD7K/img.png)

정상적으로 User 10개 생성 성공

이 상태에서 createUserListWithTrans()의 반복문 이후에 RuntimeException을 발생시켜 보겠습니다.

@Transactional이 createUser에도 걸려있으니 별 문제 없이 10개의 User가 생성된다고 기대하지만 결과는 그렇지 않습니다.

```java
//UserService.java

@Transactional
public void createUserListWithTrans(){
    for (int i = 0; i < 10; i++) {
        createUser(i);
    }

    throw new RuntimeException(); // user 생성 완료 후 Exception 발생
}

@Transactional
public User createUser(int index){
    User user = User.builder()
            .name("testname::"+index)
            .email("testemail::"+index)
            .build();

    userRepository.save(user);
    return user;
}
```

![https://blog.kakaocdn.net/dn/bvmzHx/btqWTYE9SLv/Gva9XTsOHRCApjvwGvhu91/img.png](https://blog.kakaocdn.net/dn/bvmzHx/btqWTYE9SLv/Gva9XTsOHRCApjvwGvhu91/img.png)

10번의 createUser가 실행됐지만 최종적으로는 0개가 생성된 User

createUser에 @Transaction이 걸려있어 각각 commit이 될거라 예상했지만 실제 생성된 User는 0개이며 **transaction이 rollback된듯한 결과가 나왔습니다.**

**이는 JPA가 동작하는 방식을 이해한다면 충분히 예상할 수 있는 결과입니다.**

아래에서 @Transactional이 어떻게 동작하는지 살펴보겠습니다.

## **[@Transactional은 Proxy로 동작한다는 것을 기억해야 합니다.](https://cobbybb.tistory.com/17#%--Transactional%EC%-D%--%--Proxy%EB%A-%-C%--%EB%-F%--%EC%-E%--%ED%--%-C%EB%-B%A-%EB%-A%--%--%EA%B-%--%EC%-D%--%--%EA%B-%B-%EC%--%B-%ED%--%B-%EC%--%BC%--%ED%--%A-%EB%-B%--%EB%-B%A--)**

****@Transactional은 기본적으로 Proxy 형태로 동작합니다.

아래 AopApplication.java(**[링크](https://github.com/cobiyu/transactional_proxy/blob/master/src/main/java/com/cobi/transactional/aop/AopApplication.java)**)를 24번째 줄(userService.createUserListWithTrans)에 breakpoint를 걸어보면 바로 확인할 수 있습니다.

**UserService를 Injection받아 사용하는 cli method**

```java
@Slf4j
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }

    @Bean
    CommandLineRunner onStartUp(UserService userService){
        return args -> {
            try {
                userService.createUserListWithTrans(); // breakpoint
            } catch (Exception e){
                //..ignore
            }

            List<User> allUser = userService.findAllUser();
            log.info("created size : {}", allUser.size());
            log.info("created user : {}", allUser);
        };
    }
}
```

**@Transactional이 동작하는 UserService.java**

```java
// UserService.java
@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void createUserListWithTrans(){
        for (int i = 0; i < 10; i++) {
            createUser(i);
        }
    }

    public void createUserListWithoutTrans(){
        for (int i = 0; i < 10; i++) {
            createUser(i);
        }
    }

    @Transactional
    public User createUser(int index){
        User user = User.builder()
                .name("testname::"+index)
                .email("testemail::"+index)
                .build();

        userRepository.save(user);
        return user;
    }

    @Transactional
    public List<User> findAllUser(){
        return userRepository.findAll();
    }
}

```

![https://blog.kakaocdn.net/dn/cw1IuY/btqWKYNfSej/Ye8zB6xBtxl0zEfLsm8qfK/img.png](https://blog.kakaocdn.net/dn/cw1IuY/btqWKYNfSej/Ye8zB6xBtxl0zEfLsm8qfK/img.png)

UserService대신 이름모를 UserService@350caca0 이라는 객체가 Injection된 모습

UserService가 아닌 다른 객체가 injection 되어있는것을 확인할 수 있습니다.

분명 UserrService를 injection받아서 사용하도록했는데 실제로는 UserService@359caca0 라는 클래스의 객체가 injection되었습니다.

이 객체의 정체는 JPA가 만든 Proxy객체 입니다.

정확하지는 않지만 저 객체는 대략 아래 클래스처럼 구성되어있다고 예상할 수 있습니다.

```java
// Proxy로 생성된 UserService359caca0.java

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService359caca0 extends UserService {
    private final EntityManager em;

    public void createUserListWithTrans(){
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        super.createUserListWithTrans();

        tx.commit();
    }

    public void createUserListWithoutTrans(){
				EntityTransaction tx = em.getTransaction();
        tx.begin();

	      super.createUserListWithoutTrans();

        tx.commit();
    }

    public User createUser(int index){
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        User user = super.createUser(index);

        tx.commit();

        return user;
    }

    @Transactional
    public List<User> findAllUser(){
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        List<User> list = super.findAll();

        tx.commit();

        return list;
    }
}
```

정확한 Proxy패턴은 아닐지라도 추가로 EntityManager를 injection받아 실제 개발자가 작성한 클래스의 메소드를 사용하기 전,후에

transaction에 해당하는 코드를 추가하여서 transaction이 동작하도록 만들어져있습니다.

결국 JPA의 Transactional은 AOP를 사용하여 Proxy객체의 형태로 동작하고 있는것입니다.

## **[다시 2가지 오류](https://cobbybb.tistory.com/17#%EB%-B%A-%EC%-B%-C%---%EA%B-%--%EC%A-%--%--%EC%--%A-%EB%A-%--)**

위 Proxy 내용만 이해하고있다면 초기에 제시되었던 2가지 오류는 왜 오류인지 이해할 수 있습니다.

### **[1. private은 @Transactional이 적용되지 않는다.](https://cobbybb.tistory.com/17#--%--private%EC%-D%--%--%--Transactional%EC%-D%B-%--%EC%A-%--%EC%-A%A-%EB%--%--%EC%A-%--%--%EC%--%-A%EB%-A%--%EB%-B%A--)**

Intellij의 경우 private method에 @Transactional을 설정할 경우 error를 표시해줍니다.

![https://blog.kakaocdn.net/dn/be6rp5/btqWOhrM3OR/v792TD7bqt8DBKSE6dckQ0/img.png](https://blog.kakaocdn.net/dn/be6rp5/btqWOhrM3OR/v792TD7bqt8DBKSE6dckQ0/img.png)

@Transactinonal 불가한 private method

> Methods annotated with '@Transactional' must be overridable
> 

위 내용 그대로입니다.

@Transactional Proxy형태로 동작하기 때문에 외부에서 접근 가능한 메소드만 @Transactional 설정이 가능합니다.

```java
// UserService.java

private void createUserListWithTrans(){
    for (int i = 0; i < 10; i++) {
        createUser(i);
    }
}
```

위 코드는 JPA에 의해서 변환된다면 아래처럼 변경되겠지만 위 method는 private이기 때문에 동작할 수 없게 되는것입니다.

```java
private void createUserListWithTrans(){
    EntityTransaction tx = em.getTransaction();
    tx.begin();

    super.createUserListWithTrans();   // private이기 때문에 에러 발생

    tx.commit();
}
```

### **[2. 같은 클래스 내에서의 여러 @Transactional 호출](https://cobbybb.tistory.com/17#--%--%EA%B-%--%EC%-D%--%--%ED%--%B-%EB%-E%--%EC%-A%A-%--%EB%--%B-%EC%--%--%EC%--%-C%EC%-D%--%--%EC%--%AC%EB%-F%AC%--%--Transactional%--%ED%--%B-%EC%B-%-C)**

초반에 RuntimeException이 발생되면서 모두 rollback 됐던 예제는 결국 아래와 같은 형태로 동작하게 된것입니다.

```java
// UserService.java

public void createUserListWithTrans(){
    for (int i = 0; i < 10; i++) {
        this.createUser(i);
    }
}

public User createUser(int index){
    User user = User.builder()
            .name("testname::"+index)
            .email("testemail::"+index)
            .build();

    userRepository.save(user);
    return user;
}

// UserService359caca0.java (proxy객체)

public void createUserListWithTrans(){
    EntityTransaction tx = em.getTransaction();
    tx.begin();

    super.createUserListWithTrans();

    tx.commit();

    // Proxy객체에서 UserService의 createUserListWithTrans를 호출하고
    // createUserListWithTrans는 그 안에서 같은 클래스의 createUser()를 호출하기 때문에
    // createUserListWithTrans의 Transaction만 동작하게됨
}

```

1. Proxy 객체에서 Transaction을 설정
2. 이후 기존 UserService의 createUserListWithTrans 실행
3. UserService의 createUserListWithTrans안에서 같은 클래스 안의 createUser 실행
4. 모든 과정 완료 후 Transaction 종료

Proxy형태로 동작하게 되면 위 과정대로 동작하기 떄문에 최초 진입점인 createUserListWithTrans의 Transaction만 동작하게 되는것입니다.

### **[진입점에 Transaction이 없고 안에서 호출되는 method에만 Transaction이 있다면..?](https://cobbybb.tistory.com/17#%EC%A-%--%EC%-E%--%EC%A-%--%EC%--%--%--Transaction%EC%-D%B-%--%EC%--%--%EA%B-%A-%--%EC%--%--%EC%--%--%EC%--%-C%--%ED%--%B-%EC%B-%-C%EB%--%--%EB%-A%--%--method%EC%--%--%EB%A-%-C%--Transaction%EC%-D%B-%--%EC%-E%--%EB%-B%A-%EB%A-%B---%-F)**

```java
// UserService.java

// No Transaction
public void createUserListWithoutTrans(){
    for (int i = 0; i < 10; i++) {
        createUser(i);
    }
    throw new RuntimeException();
}

@Transactional
public User createUser(int index){
    User user = User.builder()
            .name("testname::"+index)
            .email("testemail::"+index)
            .build();

    userRepository.save(user);
    return user;
}

// AopApplication.java
userService.createUserListWithoutTrans();
```

위 코드는 아래처럼 동작할 것입니다.

```java
// UserService.java
public void createUserListWithoutTrans(){
    for (int i = 0; i < 10; i++) {
        this.createUser(i);
    }

    throw new RuntimeException();
}

public User createUser(int index){
    User user = User.builder()
            .name("testname::"+index)
            .email("testemail::"+index)
            .build();

    userRepository.save(user);
    return user;
}

// UserService359caca0.java (proxy객체)
public void createUserListWithoutTrans(){
    super.createUserListWithTrans();
    // 진입 시점에 @Transactional이 없기 때문에 트랜잭션없이 동작
}

```

실행 결과를 보면 user가 10개 생성된것을 확인 할 수 있는데

이는 오히려 Transaction이 없이 동작했기 때문에 나타난 결과로 볼 수 있습니다.

@Transactional이 없기때문에 createUser가 각각 insert하면서 DB의 설정대로 auto commit 까지 동작한 결과인것입니다.

![https://blog.kakaocdn.net/dn/cXB8FL/btqWUp3BcvJ/RZixtUtc8K6KkbiXYbwnfK/img.png](https://blog.kakaocdn.net/dn/cXB8FL/btqWUp3BcvJ/RZixtUtc8K6KkbiXYbwnfK/img.png)

createUser가 Transaction이 없이 동작했기 때문에 10개의 user 생성 성공

## **[요약](https://cobbybb.tistory.com/17#%EC%-A%--%EC%--%BD)**

- JPA의 @Transactional은 Proxy형태로 동작함
- Proxy 형태로 동작하기때문에 private method에 적용 불가
- Proxy 형태로 동작하기 때문에 같은 Class안의 method에서 다른 method를 호출했을때 중첩 Transaction이 동작하지 않음 —> 무조건 진입점의 Transaction기준으로 동작함

---

# 출처

[Proxy형태로 동작하는 JPA @Transactional](https://cobbybb.tistory.com/17#--%25--%EA%25B-%25--%EC%25-D%25--%25--%ED%25--%25B-%EB%25-E%25--%EC%25-A%25A-%25--%EB%25--%25B-%EC%25-D%25--%25--%EC%25--%AC%EB%25-F%AC%25--%25--Transactional%25--method%25--%ED%25--%25B-%EC%25B-%25-C)

[동일한 Bean(Class)에서 @Transactional 동작 방식 | Popit](https://www.popit.kr/%EB%8F%99%EC%9D%BC%ED%95%9C-beanclass%EC%97%90%EC%84%9C-transactional-%EB%8F%99%EC%9E%91-%EB%B0%A9%EC%8B%9D/)

[[Spring] @Transactional 사용시 주의해야할 점](https://mommoo.tistory.com/92)