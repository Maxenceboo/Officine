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

## SonarCloud
- Create a project on SonarCloud and note:
  - Organization key (example: `my-org`)
  - Project key (example: `my-org_test-project`)
- Set repository secret `SONAR_TOKEN` with a SonarCloud user token.
- Update `pom.xml` properties to match your org and project:
  - `sonar.organization`
  - `sonar.projectKey`
- GitHub Actions workflow `.github/workflows/sonarcloud.yml` runs analysis on pushes and PRs:
  - Builds with Maven, runs tests and JaCoCo coverage
  - Sends results to `https://sonarcloud.io`
