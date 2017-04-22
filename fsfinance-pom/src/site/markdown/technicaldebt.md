# Technical debt

## Open

### TD0001: Evalute Maven changelog plugin

### TD0002: Configure Maven Surefire Reports plugin

Configure the Maven Surefire Reports plugin to generate/include test reports into the
Maven Site build.

### TD0003: Use Hamcrest library in unit/integration test

<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>java-hamcrest</artifactId>
    <version>2.0.0.0</version>
</dependency>

### TD0004: Evaluate Flyway Database Migration

https://flywaydb.org/

### TD0006: Custom Annotations for Spring's @Component

Create custom annotations based on @Component for clearer meaning of what a bean's
purpose is. For instance: @BusinessManager, @Helper, etc.

## Closed

### <del>TD0005: Refactor/Move integration tests to separate Maven module</del>
