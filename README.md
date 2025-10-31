# Java Starter (Maven + JUnit)

A minimal Java project using Maven with a sample JUnit 5 test.

## Prerequisites
- Java 17+ installed (`java -version`)
- Maven installed (`mvn -version`)

## Useful Commands
- Compile: `mvn -q -DskipTests package`
- Run tests: `mvn -q test`
- Run the app: `mvn -q -DskipTests package && java -cp target/test-project-0.1.0-SNAPSHOT.jar com.example.app.App`

## Project Structure
- `src/main/java/com/example/app/App.java` — simple app with `sum` method
- `src/test/java/com/example/app/AppTest.java` — JUnit test for `sum`

Adjust Java version in `pom.xml` if your JDK differs.
