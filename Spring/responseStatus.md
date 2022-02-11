# [Spring MVC] Setting Response Status with Spring MVC

# **1. Overview**

In this tutorial, we'll explore different ways to set the response status using Spring MVC.

# **2. Default Status Codes**

**By default, Spring MVC returns *200 - OK* for the GET, POST, PUT, DELETE and PATCH requests in case of a successful result**.

```java
@ResjtController
public class ResponseStatusRestController {

    @GetMapping("/status")
    public String status() {
        return "Done";
    }

    @PostMapping("/statusPost")
    public String statusPost() {
        return "Done";
    }

    @PutMapping("/statusPut")
    public String statusPut() {
        return "Done";
    }

    @DeleteMapping("/statusDelete")
    public String statusDelete() {
        return "Done";
    }

    @PatchMapping("/statusPatch")
    public String statusPatch() {
        return "Done";
    }
}
```

# **3. Using *ResponseEntity***

***ResponseEntity* allows us to define the response status in a controller method**.

```java
@PostMapping("/statusWithResponseEntity")
public ResponseEntity<String> statusWithResponseEntity() {
    return ResponseEntity.status(HttpStatus.ACCEPTED).body("Done");
}
```

이 엔드포인트에서는 상태 코드를  *`HttpStatus.ACCEPTED`* 로 설정하고 있다*.*

일반적으로 *`ResponseEntity`* 클래스는 헤더, 상태 및 응답 본문을 설정할 수 있다는 점에서 응답에 대한 더 많은 제어권을 제공한다.

## **4. Using *HttpServletResponse***

또는 ***`HttpServletResponse`***를 **사용하여 응답 상태를 설정**할 수 **있다**. 이를 위해 우선 *`HttpServletResponse`*를 우리의 Controller Mehtod에 매개 변수로 추가해야 한다.

```java
@PostMapping("/statusWithResponse")
public String statusWithResponse(HttpServletResponse servletResponse) {
    servletResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
    return "Done";
}
```

Here, we're calling the *setStatus* method with a status code.

# **5. Using @ResponseStatus**

Lastly, Spring MVC provides the *@ResponseStatus* annotation which we can use in different places.

## **5.1. On Method**

**We'll first use *`@ResponseStatus`* on a controller method to set its status code**:

```java
@ResponseStatus(HttpStatus.ACCEPTED)
@PostMapping("/statusWithAnnotation")
public String statusWithAnnotation() {
    return "Done";
}
```

The response status will be *202 - Accepted* when the result is a success.

## **5.2. On Controller**

***`@ResponseStatus`***는 **컨트롤러 레벨에서도 사용**할 수 **있다**.이러한 방식으로 해당 컨트롤러의 모든 엔드포인트는 클래스 수준 응답 상태를 반환한다. 그러나 엔드포인트 메서드에 *`@ResponseStatus`* 주석도 포함된 경우 해당 값은 전자를 재정의한다.

```java
@ResponseStatus(HttpStatus.ACCEPTED)
@RestController
@RequestMapping("/controller")
public class ControllerLevelResponseRestController {

    @PostMapping("/status")
    public String status() {
        return "Done";
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/statusOverride")
    public String statusWithAnnotation() {
        return "Done";
    }
}
```

Here, *ControllerLevelResponseRestController* defines the response status as *202 - Accepted*. 

Thus the /*status* endpoint returns *202*. However, /*statusOverride* returns *201* since we're annotating it with *@ResponseStatus(HttpStatus.CREATED)*.

## **5.3. On @ExceptionHandler**

Another place we can use *`@ResponseStatus`* is *`@ExceptionHandler`* methods:

```java
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@ExceptionHandler(RuntimeException.class)
public void handleRuntimeException(RuntimeException e) {
    // Implementation details...
}
```

# **6. Summary**

In this tutorial, we've investigated how we can set the response status using Spring MVC.

As always the source code for all examples is available [on Github](https://github.com/isaolmez/javabyexamples/tree/master/spring-mvc-1/src/main/java/com/javabyexamples/spring/mvc1/responsestatus).

---

# 8. 출처

[Home | Java By Examples](http://www.javabyexamples.com/setting-response-status-with-spring-mvc)