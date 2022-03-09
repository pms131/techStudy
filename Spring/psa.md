# [Spring] PSA

## PSA (Portable Service Abstraction)

우리는 Spring의 AOP가 Proxy 패턴을 발전시켜 만들어 졌다는것을 이전 포스팅들을 통해서 알게되었습니다. 그리고 FactoryBean을 통해 Proxy가 Bean이 생성될때 자동으로 생성 되는 것 또한 알게 되었습니다.

여기에 우리가 간과하고 있던 사실이 있습니다. `@Transactional` 어노테이션을 선언하는 것 만으로 별도의 코드 추가 없이 트랜잭션 서비스를 사용할 수 있다는 사실입니다. 그리고 내부적으로 트랜잭션 코드가 추상화되어 숨겨져 있는 것입니다. **이렇게 추상화 계층을 사용하여 어떤 기술을 내부에 숨기고 개발자에게 편의성을 제공해주는 것이 서비스 추상화(Service Abstraction)**입니다.

그리고 아시다시피 DB에 접근하는 방법은 여러가지가 있습니다. 

기본적으로 Jdbc를 통해 접근(DatasourceTransactionManager)할 수 있으며 ORM을 이용하고자한다면 JPA(JpaTransactionManager)를 통해서 접근할 수도 있습니다. 

신기하게도 어떠한 경우라도 `@Transactional` 어노테이션을 이용하면 트랜잭션을 유지하는 기능을 추가할 수 있습니다. 이렇게 **하나의 추상화로 여러 서비스를 묶어둔 것을 Spring에서 Portable Service Abstraction**이라고 합니다.

## PSA의 원리

그렇다면 어떻게 이런게 가능한걸까요? 

한번 그 원리에 대해서 알아보도록 하겠습니다. `@Transactional`로 이어서 설명하도록 하겠습니다. Java로 DB와 통신을 구현하기 위해서는 먼저 DB Connection을 맺어야합니다. 

그리고 트랜잭션을 시작합니다. 쿼리를 실행하고 결과에 따라 Commit 또는 Rollback을 하게됩니다. 마지막으로 DB Connection을 종료하며 마무리하게 됩니다. 이를 Java의 수도코드로 나타내면 아래와 같습니다.

```java
public void method_name() throw Exception {
// 1. DB Connection 생성// 2. 트랜잭션(Transaction) 시작try {
// 3. DB 쿼리 실행// 4. 트랜잭션 커밋
    } catch(Exception e) {
// 5. 트랜잭션 롤백throw e;
    } finally {
// 6. DB Connection 종료
    }
}
```

위 예제의 3번을 제외하고는 `@Transactional`에서 제어해주는 부분입니다. 3번은 우리가 직접 구현하는 비지니스 메서드가 될 것입니다. 3번을 제외한 부분은 AOP를 통해 구현되어진다는 사실을 우리는 알고 있습니다.

만약 JDBC로 `@Transactional`이 되어있다면 아래와 같은 코드가 될 것입니다. 이것은 JDBC에 특화되어있는 코드입니다. 이 코드로는 JPATransactionManager는 이용할 수가 없습니다. 왜냐하면 JPA는 Connection을 직접관리하지 않고 EntityManager로 간접으로 관리하기 때문입니다. Hibernate 라면 Session으로 관리하죠. 변경을 위해서는 코드의 수정이 불가피한 상황입니다. 

어떻게 해야 `@Transactional` 단일 어노테이션으로 JPATransactionManager도 사용할 수 있게 할 수 있을까요?

```java
public void method_name() throw Exception {
    TransactionalSynchronizationManager.initSunchronization();
    Connection c = DataSourceUtils.getConnection(dataSource);
    try {
// 3. DB 쿼리 실행c.commit();
    } catch(Exception e) {
        c.rollback();
        throw e;
    } finally {
        DatasourceUtils.releaseConnection(c, dataSource);
        TransactionSynchronizationManager.unbindResource(dataSource);
        TransactionSynchronizationManager.clearSynchronization();
    }
}
```

그 비밀은 바로 ***추상화***에 있습니다. 아래 사진은 Spring의 TransactionManager의 관계를 나타내고 있습니다. 이걸 보면 감이 오실 것 같습니다. 

<aside>
💡 즉, Spring의 `@Transactional`은 각 TransactionManager를 각각 구현하고 있는 것이 아니라 최상위 PlatformTransactionManager를 이용하고 필요한 TransactionManager를 DI로 주입받아 사용하는구나라는 사실을 알 수 있습니다.

</aside>

![https://blog.kakaocdn.net/dn/bOLvvy/btqN9MtzKKO/mkJukSC9T4xy74ZoOGJmu0/img.png](https://blog.kakaocdn.net/dn/bOLvvy/btqN9MtzKKO/mkJukSC9T4xy74ZoOGJmu0/img.png)

Spring의 TransactionManger 추상화 계층, 출처 : 토비의 스프링 3.1 5장

실제 그런지 `PlatformTransactionManager`의 소스코드와 구현 클래스로 어떤것들이 있는지 확인해보도록하겠습니다.

```java
public interface PlatformTransactionManager extends TransactionManager {

  TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException;

  void commit(TransactionStatus status) throws TransactionException;

  void rollback(TransactionStatus status) throws TransactionException;
}

```

![https://blog.kakaocdn.net/dn/bNlyj6/btqOdNSs6Ie/gk6eTWv3jcrDLzvVcJDmKK/img.png](https://blog.kakaocdn.net/dn/bNlyj6/btqOdNSs6Ie/gk6eTWv3jcrDLzvVcJDmKK/img.png)

PlatformTransactionManager의 구현 클래스

사진으로 살펴보고 예상한대로 PlatformTransactionManger의 하위로 다양한 TransactionManager가 구현되어있다는 사실을 알 수 있었습니다. 

그렇다면 우리가 지금까지 살펴본 예제를 아래와 같이 변경한다면 요구사항을 만족 시킬 수 있을 것입니다. 

**아래 코드는 Transaction을 제어함에 있어 하나의 기술에 국한되지 않고 트랜잭션관리의 모든 기술을 아우를 수 있는 Spring의 PSA(Portable Service Abstraction)에 맞는 코드라고 볼 수 있습니다.**

```java
private PlatformTransactionManger transactionManager;

public constructor(PlatformTransactionManger transactionManager) {// 생성자this.transactionManager = transactionManager;// 해당 주입 instance의 변경으로 JPA, hibernate, JDBC로 쉽게 변경 가능.
}

public void method_name() throw Exception {
  TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
  try {
// 3. DB 쿼리 실행
    transactionManager.commit(status);
  } catch(Exception e) {
    transactionManager.rollback(status);
    throw e;
  }
}
```

---

## 참조

토비의 스프링 3.1 Vol. 1, 5장. 서비스 추상화

[[Spring] Spring의 핵심기술 PSA - 개념과 원리](https://sabarada.tistory.com/127?category=803157)