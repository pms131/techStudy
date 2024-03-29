# [JPA] Entity 생명주기

# **엔티티 생명주기(Entity LifeCycle)**

![https://blog.kakaocdn.net/dn/9TmXh/btq4cf7NDf1/8Ih7QbsUqZtGzK50KYIRTk/img.png](https://blog.kakaocdn.net/dn/9TmXh/btq4cf7NDf1/8Ih7QbsUqZtGzK50KYIRTk/img.png)

## **비영속(new/transient)**

순수한 객체 상태이며, **영속성 컨텍스트와 관련이 없는 상태**

```java
Member member = new Member();
```

## **영속(managed)**

EntityManager를 통해 엔티티를 영속성 컨텍스트에 저장되어 **영속성 컨텍스트가 관리**하는 상태

```java
em.persist(member);// 객체 저장
```

## **준영속(detached)**

영속성 컨텍스트에 저장되었다가 분리된(detached) 상태

```java
em.detach(member);// 특정 엔티티를 분리
em.close();// 영속성 컨텍스트 닫기em.clear();// 영속성 컨텍스트 초기화
```

[추가 설명](https://girawhale.tistory.com/122#%EC%A4%80%EC%98%81%EC%86%8D-%EC%83%81%ED%83%9C)

## **삭제(removed)**

엔티티를 영속성 컨텍스트와 데이터베이스에서 삭제한 상태

```java
em.remove(member);
```

# **준영속 상태**

영속 상태의 엔티티가 분리된 상태이기 때문에 Dirty Checking이나 변경 감지 등의 영속성 컨텍스트가 제공하는 기능을 사용할 수 없다

## **만드는 방법**

### detach()

```java
public void detach(Object entity);
```

특정 엔티티를 준영속 상태로 만든다

더이상 엔티티를 관리 하지 않고 1차 캐시, 쓰기지연 SQL 저장소에서 해당 엔티티를 관리하기 위한 모든 정보가 삭제

직접 사용하는 일이 거의 없다.

### clear()

영속성 컨텍스트를 초기화해 해당 영속성 컨텍스트에 존재하는 모든 엔티티를 준영속 상태로 만든다.

모든 것이 초기화되었기 때문에 새로 만든 영속성 컨텍스트 상태와 동일하다.

때문에 같은 Entity를 조회할 때 SELECT 쿼리가 발생하게 된다. 1차 캐시에 관계 없는 쿼리를 확인하고 싶을 때 유용하다

### close()

영속성 컨텍스트를 종료하고 관리하던 모든 엔티티가 준영속 상태가 된다.

### **특징**

1. 거의 비영속 상태와 동일
    - 영속성 컨텍스트가 관리하지 않기 때문에 제공하는 어떤 기능도 동작하지 않음
2. 식별값을 보유
    - 비영속 상태는 식별값이 없을 수 있지만 준영속 상태는 영속상태였기 때문에 반드시 식별자를 보유하고 있다
3. 지연 로딩 불가
    - 지연 로딩(LAZY LOADING)은 실제 객체 대신 프록시 객체를 로딩하고 해당 객체를 실제 사용시 영속성 컨텍스트를 통해 데이터를 불러옴
    - 하지만 더이상 영속성 컨텍스트가 관리하지 않기 때문에 지연 로딩시 문제가 발생

### **병합(merge())**

준영속 상태의 엔티티를 다시 영속상태로 변환한다. 준영속 상태의 엔티티를 받아 가지고 있는 정보로 새 영속 상태의 엔티티를 반환한다.

```java
Memeber mergeMember = em.merge(member);
```

병합은 비영속 상태의 엔티티도 영속 상태로 만들 수 있다. 파라미터로 넘어온 엔티티의 식별자 값으로 영속성 컨텍스트를 조회하고 찾는 엔티티가 없으면 데이터베이스에서 조회하고 영속 상태로 만든다. 만약 데이터베이스에서도 없으면 새로운 엔티티를 생성한다.

만약 불러온 엔티티와 파라미터로 넘긴 객체와 값이 다를 경우 값을 채우고 병합이 이루어진다.

따라서, 병합은 save or update를 수행한다.

---

# 출처

[[JPA] Entity 생명주기(Entity LifeCycle)](https://girawhale.tistory.com/122)