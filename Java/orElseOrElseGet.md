# [Java] Optional – orElse() vs orElseGet() 차이점 알고 쓰자.

# **서론**

**자바 8**부터 지원하게 된 **optional**은 **NullPointerException** 에 대한 문제를 해결하기 위해 나왔습니다.

하지만 그런데도 null이 발생할 수 있고, null 체크를 해야만 하는 경우가 빈번합니다.

그러므로 null일 시 default 값을 넣어줄 수 있는데 그중에서 **orElse()와 orElseGet()** 이 존재합니다.

이를 그냥 이것저것 사용할 수도 있지만, 알고 쓰는 것과 모르고 쓰는 것은 천지 차이기 때문에

한 번 정리해보았습니다.

---

# **둘의 차이?**

```java
public T orElse(T other)

public T orElseGet(Supplier<? extends T> other)
```

```java
public T orElse(T other) {
    return value != null ? value : other;
}

public T orElseGet(Supplier<? extends T> other) {
    return value != null ? value : other.get();
}
```

**orElse** 는 **T의 모든 매개 변수**를 사용하고, **orElseGet**은 T 유형의 개체를 반환하는 **Supplier 유형의 인터페이스**를 허용합니다.

**이에 차이점은,**

- **orElse()** : T 클래스를 인수로 받습니다.
- **orElseGet()** : T 클래스를 상속받은 하위 클래스를 return해주는 Supplier 함수 인터페이스를 받습니다.

<aside>
💡 Supplier

 - **get을 호출하여 결과를 리턴하는 역할**

</aside>

---

# **예시**

### **getRandomName()**

```java
public String getRandomName() {
	LOG.info("getRandomName() method - start");

	Random random = new Random();
	int index = random.nextInt(5);

	LOG.info("getRandomName() method - end");
	return names.get(index);
}
```

## **orElse()**

```java
String name = Optional.of("baeldung")
										  .orElse(getRandomName());
```

이 경우 name의 값은 "baeldung"가 있어 null이 아니지만, 콘솔에는 아래와 같이 메세지가 출력됩니다.

```
getRandomName() method - start
getRandomName() method - end
```

여기서 **orElse()의 인수에 대한 해답**을 찾을 수 있습니다.

→ **orElse()는 메소드를 인수로 받지 않습니다. 바로, 값을 인수로 받습니다.**

- 결국, **orElse() 메소드 인수를 할당**하기 위해 **getRandomName() 메소드가 실행된 후** 해당 결과 값을 orElse() 메소드 인수로 할당하기 때문에 **Optional의 값과 상관 없이 메소드가 실행되게 되는 것** 입니다.

<aside>
💡 **따라서 null이 아닐 경우, orElse()는 의도치 않은 메소드 호출이 될 수 있다!**

</aside>

---

## **orElseGet()**

```java
String name = Optional.of("baeldung")
										  .orElseGet(() -> getRandomName());
```

위와 똑같이 **getRandomName()** 함수를 **orElseGet**에 넣었습니다.

하지만 결과는 **getRandomName()** 을 호출하지 않습니다.

그 이유는 **인수로 전달된 Supplier 메소드 경우 Optional의 값이 없을 때만 get()을 통해 실행**됩니다.

---

# **결론**

## **orElseGet()**

- null일 경우 메소드를 실행해야 할 때

## **orElse()**

- null일 때 값을 넘겨야 할 때

기존에는 orElse()가 무조건 실행된다는 것만 알고 있었기에 무조건 orElseGet()이 좋은 건 줄 알았습니다.

이후 다시 알게 되었을 땐 orElseGet(), orElse() 메소드 둘 다 실행은 되지만,

메소드가 인자로 넘어갈 경우에 따라 **인자 메소드 실행 타이밍**이 달라지는 걸 알게 되었습니다.

<aside>
💡 단순히 메소드가 아닌 값을 넘길 경우에는 Supplier 인터페이스를 감싸서 넘길 필요 없이 orElse()를 사용하면 될 것 같습니다.

</aside>

---

# 출처

[[Java] Optional - orElse() vs orElseGet() 차이점 알고 쓰자.](https://kdhyo98.tistory.com/40)

[Optional 클래스의 orElse와 orElseGet에 대한 정리](https://zgundam.tistory.com/174)