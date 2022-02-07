# [Spring] @Bean vs @Component

Spring으로 개발을 하다보면 @Bean과 @Component를 언제 써야할지 헷갈릴때가 있다.

둘다 목적이 명확하지 않은 Bean을 생성할때 사용하는 어노테이션인데 왜 2개로 나누어져있나 궁금했었는데, 박재성님의 강의를 통해 둘의 차이를 알게 되었다.

![bean](image/2210D13B579B678822.jfif)

(@Bean)

![component](image/2210D13B579B678822.jfif)

(@Component)

<aside>
💡 위 코드를 보면 알수있는데 @Bean의 경우 개발자가 컨트롤이 불가능한 외부 라이브러리들을 Bean으로 등록하고 싶은 경우에 사용된다.

</aside>

(예를 들면 ObjectMapper의 경우 ObjectMapper Class에 @Component를 선언할수는 없으니, ObjectMapper의 인스턴스를 생성하는 메소드를 만들고 해당 메소드에 @Bean을 선언하여 Bean으로 등록한다.)

<aside>
💡 반대로 개발자가 직접 컨트롤이 가능한 Class들의 경우엔 @Component를 사용한다.

</aside>

그럼 개발자가 생성한 Class에 @Bean은 선언이 가능할까?

정답은 No 이다.

**@Bean과 @Component는 각자 선언할 수 있는 타입이 정해져있어**  해당 용도외에는 컴파일 에러를 발생시킨다.

![bean interface](image/2534C543579B685107.jfif)

(Bean : @Target이 METHOD로 지정되어 있지만, TYPE은 없다)

![component interface](image/2130E43D579B688C04.jfif)

(Component : @Target이 TYPE로 지정되어 Class위에서만 선언될수 있음을 알수 있다.)

---
## @Target Annotation

|ElementType 열거상수|적용대상|
|------|---|
|TYPE|클래스, 인터페이스, 열거타입|
|ANNOTATION_TYPE|어노테이션|
|FIELD|필드|
|CONSTRUCTOR|생성자|
|METHOD|메소드|
|LOCAL_VARIABLE|로컬 변수|
|PACKAGE|패키지|

---
출처 :

[@Bean vs @Component](https://jojoldu.tistory.com/27)