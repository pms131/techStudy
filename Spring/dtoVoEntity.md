# [Spring] DTO VO Entity
## **DTO**

DTO(Data Transfer Object)로서 계층(Layer)간 데이터 교환을 위해 사용하는 객체이다.

데이터 교환만을 위해 사용하므로 로직을 갖지 않고, getter/setter 메소드만 갖는다.

DTO 예제 코드이다.

```java
class RGBColorDto {
   private int red;
   private int green;
   private int blue;

   public RGBColor(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
   }

   public int getRed() {
      return red;
   }

   public int setRed(int red) {
      this.red = red;
   }
   ...
}

```

로직은 없고, 데이터를 담고, 꺼내는 getter/setter 메소드만 담는다.

## **VO**

VO(Value Object)는 값 그 자체를 표현하는 객체이다.

로직을 포함할 수 있으며, 객체의 불변성(객체의 정보가 변경하지 않음)을 보장한다.

서로 다른 이름을 갖는 VO 인스턴스더라도 모든 속성 값이 같다면 두 인스턴스는 같은 객체라고 할 수 있다. 이를 위해 VO에는 Object 클래스의 `equals()`와 `hashCode()`를 오버라이딩해야 한다.

VO 예제 코드이다.

```java
class RGBColor {
   private final int red;
   private final int green;
   private final int blue;

   public RGBColor(int red, int green, int blue) {
      this.red = red;
      this.green = green;
      this.blue = blue;
   }

   public static RGBColor of(int red, int green, int blue){
      return new RGBColor(red, green, blue);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      RGBColor rgbColor = (RGBColor) o;
      return red == rgbColor.red && green == rgbColor.green && blue == rgbColor.blue;
   }
}

```
```java
@Override
public boolean equals(Object o) {
   if (this == o) return true;
   if (o == null || getClass() != o.getClass()) return false;
   RGBColor rgbColor = (RGBColor) o;
   return red == rgbColor.red && green == rgbColor.green && blue == rgbColor.blue;
}

```

`equals()` 를 한줄 한줄 살펴보면, VO를 설명할때 언급한 모든 속성값이 같다면 이름이 다른 인스턴스라도 같은 객체로 본다는 특징을 마지막 `return` 문에서 증명하고 있다.

`equals()`를 오버라이딩해서 정의하기 전과 정의한 후의 객체비교 테스트 코드이다.

![https://user-images.githubusercontent.com/33862991/116197225-6ef9b880-a76f-11eb-8e4f-fd5a68a2e48b.png](https://user-images.githubusercontent.com/33862991/116197225-6ef9b880-a76f-11eb-8e4f-fd5a68a2e48b.png)

테스트 결과 테스트에 실패하였다. 속성값이 갖더라도 두 객체를 `new` 키워드로 선언하여 생성한만큼 서로 다른 객체로 컴파일러가 인식했기 때문이다.

![https://user-images.githubusercontent.com/33862991/116197244-75883000-a76f-11eb-9ff2-ab975e11a093.png](https://user-images.githubusercontent.com/33862991/116197244-75883000-a76f-11eb-9ff2-ab975e11a093.png)

VO에서 `equals()`를 오버라이딩하고 나니까 테스트를 통과하는걸 확인할 수 있다.

`equals()`와 `hashCode()`에 대해서는 다음 포스팅에서 정리해볼 예정이다

## **Entity**

실제 DB의 테이블과 매핑되는 객체이다. `id`를 통해 각각의 Entity를 구분한다.

VO와 마찬가지로 로직을 가질 수 있다.

JPA를 사용한 Entity 예제 코드이다.

```java
import javax.persistence.*;

@Entity
public class User {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   privae Long id;

   @Column(nullable = false)
   private String name;

   @Column(nullable = false)
   private String email;

   @Builder
   public User(String name, String email) {
      this.name = name;
      this.email = email;
   }

   public User update(String name, String email) {
      this.name = name;
      this.email = email;
      return this;
   }
}

```

## **정리하면..**

- DTO는 계층(Layer)간 데이터 이동을 위해 사용되는 객체
- VO는 값을 갖는 순수한 도메인
- Entity는 이를 DB 테이블과 매핑하는 객체

![https://user-images.githubusercontent.com/33862991/116250387-a46ec800-a7a8-11eb-8368-8e2df28225fa.png](https://user-images.githubusercontent.com/33862991/116250387-a46ec800-a7a8-11eb-8368-8e2df28225fa.png)

---

## 출처

[DTO와 VO 그리고 Entity의 차이](https://youngjinmo.github.io/2021/04/dto-vo-entity/)