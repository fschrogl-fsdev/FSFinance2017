# Getting started

The application's build process is configured to build an application that
will run in a JBoss EAP/Wildfly application server. Therefore various libraries
and frameworks are not included in the application's deployment artifact but
required at runtime.

## Required Libraries/Frameworks

  - JPA 2.1 (and a provider implementation)
  - JTA 1.2 (and a provider implementation)
  - SLF4J 1.7
  - JDBC driver for the database product being used

## Logging

The application defines a logging profile in its MANIFEST.MF file. On JBoss
EAP/Wildfly this can be used to configure handling of log output from the
application.

The name of the logging profile: **fsfinance-web**

## JNDI Resources

A JNDI resource reference is declared in the application's web.xml file. This
reference is optional and can be deleted if not needed/available in the
execution environment the application should deployed to.

## Spring Configuration

Spring is configured to obtain various resources  from JNDI. This includes:

  - Datasource object for the application
  - Reference to an EntityManagerFactory
  - Reference to a TransactionManager
  
This configuration can be changed in fsfinance-web/src/main/webapp/resources/WEB-INF/applicationContext.xml

For how to provide an EntityManagerFactory and EntityManager through JNDI on
JBoss EAP/Wildfly see: https://docs.jboss.org/author/display/WFLY10/JPA+Reference+Guide
