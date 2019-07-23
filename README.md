# RestClient - RestTemplate utility

	* groupId: com.srhojo.utils
	* artifactId: restclient
	* version: 1.0.2-SNAPSHOT

## Technologies: <a name="Technologies">

| Nombre           | Version | URL                                    |
| ---------------  | ------- | -------------------------------------- |
| Java             | 8       |                                        |
| Spring Core      | 5.1     | https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#spring-core | 
| Spring Context   | 5.1     | https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html |
| Spring Web       | 5.1     | https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html |
| Spring Aspect    | 5.1     | https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html |
| Javax Validation | 2.0.1   | https://docs.oracle.com/javaee/7/api/javax/validation/package-summary.html |
| Jackson databind | 2.9.9.1 | https://github.com/FasterXML/jackson |
| Swagger2         | 2.9.2   |  |	
| spring-boot-starter-test            | 2.1.6    | https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html  |	
| Mockito-core     | 2.28.2  | https://site.mockito.org/ |	


## Use: <a name="howtouse">

### Instalation <a name="instalation">
* ####  Local <a name="local">
```java
    mvn clean install
```
* #### Maven <a name="maven">
```java
<dependency>
    <groupId>com.srhojo.utils</groupId>
    <artifactId>restclient</artifactId>
    <version>1.0.2-SNAPSHOT</version>
</dependency>
```


#### To Fix Jacoco
	λ mvn clean jacoco:prepare-agent install
