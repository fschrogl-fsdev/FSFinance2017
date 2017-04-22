## FSFinance

A webapp to track one's daily expenses. This application is written in Java (1.8+)
and can run on any Servlet 3.1+ container. The various libraries/frameworks
utilized by this project are:

  * Apache Wicket
  * Twitter's Bootstrap
  * jQuery
  * Spring Framework
  * JPA 2

### Getting Started

The application is currently build to run inside an JBoss EAP/Wildfly JavaEE application
server. Therefore various assumptions are made which libraries are already provided by
the application server at runtime and need not to be included in the application's WAR
file. The projects pom.xml files and Spring configuration must be modified and the
application rebuild if running inside a different environment.

For further information see fsfinance-pom/src/site/markdown/gettingstarted.md

### Project structure and build

The project is split up into several sub-modules. The parent project is fsfinance-pom,
from which a full build can be started using Maven:

	// Full build without execution of unit tests
	mvn

	// Full build with execution of unit tests
	mvn -DskipTests=false

	// Generate project's site documentation
	mvn site

#### Generate database schema DDL

There's a special build profile which can be utilized to generate a database schema DDL
script. The script will be placed in <i>fsfinance-persistence/target/ddl</i>.

	// Generate database schema ddl
	mvn -P database

####  Run integration tests

Integration tests are not run by default (not compiled even), because they are meant
to be executed during continuous integration where the environment is properly set up
for them. Nonetheless they can also be executed locale using another Maven profile:

	// Execute integration tests
	mvn -P continuous-integration
