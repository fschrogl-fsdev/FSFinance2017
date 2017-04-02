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
