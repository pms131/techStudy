# 1주차

---

# ◇ JPA 소개

## ◆ 객체와 관계형 데이터베이스의 차이

1. 상속 ↔  Table 슈퍼타입 서브타입 관계
    - 각각의 테이블에 따른 조인 SQL 작성 필요
    - 각각의 객체 생성..
    - 따라서 DB 저장할 객체에는 상속 관계 안쓴다.
2. 연관관계
    - 객체는 참조를 사용 : member.getTeam()
    - 테이블은 FK를 사용
3. 데이터 타입
4. 데이터 식별 타입
- SQL 사용시, 계층형 아키틱처 진정한 의미의 계층 분할이 어렵다.
- 객체답게 모델링 할 수록 매핑 작업만 늘어난다..

## ◆ JPA 소개

- Java 진영 ORM 표준
- 애플리케이션과 JDBC 사이에서 동작
- 패러다임의 불일치 해결
    - 상속관계를 Join SQL을 짜지않고 가져올 수 있다.
    - 계층간 신뢰성을 갖게 해준다.
- JPA의 성능 최적화 기능
    - 1차 캐시와 동일성 보장
        - 같은 트랜잭션 안에서는 같은 엔티티를 반환 - 메모리에 캐싱같은 느낌?
        - DB Isolation Level이 Read Commit이어도 애플리케이션에서 Repeatable Read 보장(중요 X)
    - 트랜잭션을 지원하는 쓰기 지연
        - 트랜잭션을 커밋할 때 까지 Insert SQL을 모음 (옵션)
    - 지연 로딩(Lazy Loading)
        - 객체가 실제 사용될 때 로딩

---

# ◇ JPA 프로젝트 생성

- h2 database는 설치 버전과 maven 버전을 맞춰주는 것이 좋다

### ◆ JPA 설정 파일ㅣ

- /META-INF/persistence.xml 파일에 설정

```java
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
	xmlns="http://xmlns.jcp.org/xml/ns/persistence"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
	
	<persistence-unit name="hello">
		<properties> 
		<!-- 필수 속성 -->
			<property name="javax.persistence.jdbc.driver" value="org.h2.Driver" />
			<property name="javax.persistence.jdbc.user" value="sa" />
			<property name="javax.persistence.jdbc.password" value="" />
			<property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
			<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />
			
			<!-- 옵션 -->
			<property name="hibernate.show_sql" value="true" />
			<property name="hibernate.format_sql" value="true" />
			<property name="hibernate.use_sql_comments" value="true" /> 
			<!--<property name="hibernate.hbm2ddl.auto" value="create" /> -->
		</properties>
	</persistence-unit>
</persistence>
```

- JPA는 특정 DB에 종속 X
- dialect는 방언 표시라고 보면 된다. (MySQL5InnoDBDiaelct, Oracle10gDialect, H2Dialect...)
- javax.*는 표준, hiberante.*는 hibernate에서만 사용
- hibernate.show_sql : sql 쿼리 표시
- hibernate.format_sql : 이쁘게 포맷하여 출력
- hibernate.use_sql_comments : 어디서 사용되었는지 출력

## ◆ JPA 애플리케이션 개발

- Persistence.createEntityManagerFactory : persistence-unit name의 이름을 파라미터로 넘겨서 연동해준다.
- **모든 JPA 작업은 트랜잭션 안에서 해야 한다.**
- EntityManagerFactory : 애플리케이션 전체에 하나만 생성해서 공유
- EntityManager : 고객 요청마다 생성 후 버림, 쓰레드 간 공유 X
- 수정은 set만 해줘도 수정 가능
- 예제 코드

```java
public class JpaMain {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
		
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		
		try {
			/*
			 * 회원 생성
			Member member = new Member();
			
			member.setId(1L);
			member.setName("HelloA");
			
			em.persist(member);
			*/
			
			/*
			 * 회원 조회
			Member findMember = em.find(Member.class, 1L);
			
			 * 회원 삭제
			em.remove(findMember);
			*/

			// 회원 수정
			Member findMember = em.find(Member.class, 1L);
			
			findMember.setName("HelloJPA");
			
			tx.commit();
			
		} catch (Exception e) {
			tx.rollback();
		} finally {
			em.close();	
		}
		
		emf.close();
	}

}
```

## ◆ JPQL 소개

- 가장 단순한 조회 방법
- 객체를 대상으로 쿼리 작성
- 예제 : select m from Member as m

```sql
select m from Member as m
```

```sql
select
	member0_.id as id1_0_,
  member0_.name as name2_0_ 
from
  Member member0_member.name = HelloJPA
```

- persistence.xml 파일에서 dialect만 변경하면 해당 DB의 문법에 맞춰서 쿼리 실행