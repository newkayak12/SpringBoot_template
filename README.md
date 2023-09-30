# Spring_BASE 

- spring 2.7.11
- spring config
- spring actuator
- hibernate
- querydsl
- modelMapper
- reflections
- spring-security
- jjwt 0.11.5 / jjwt-api 0.11.5 / jjwt-jackson 0.11.5
- [swagger3](http://localhost:8080/swagger-ui/index.html#/) (springfox-boot-starter 3.0.0 / springfox-swagger-ui 3.0.0)


### Jwts 관련 처리 토큰 

- security 적용

### Junit Test
```java
  class BaseTest{
//    아래의 Object는 request를 보내는 파라미터를 보내는 부분
    
      get(String url, Object object);
//      RestTemplate GetMapping
    
      post(String url, Object object);
//      RestTemplate PostMapping
    
      patch(String url, Object object);
//      RestTemplate PatchMapping
      
      delete(String url, Object object);
//      RestTemplate DeleteMapping
  }
    
```
