#Showcase for Arquillian Spring Container

All the examples has been tested with JBoss 7.1.1 in managed and remote configuration.

## Beans injection

An example of simple bean injection into the test case.

## JSR-330 support

Injecting Spring beans through JSR-330 annotations in the test case.

## Java-based config

Configuring the Spring context using Java-based config.

## JDBC tests

Simple JDBC tests.

## JPA

Example of running tests of JPA persistence.

## Hibernate

Example of testing hibernate persistence.

## JMS

Example of sending JMS messages using Spring's JmsTemplate

Note: in other to successfully run this example in remote JBoss AS 7.1.1 please start the server using fallowing command:

```
./standalone.sh --server-config=standalone-full.xml
```

## EJB - lookup and injection

Example of lookuping stateless session bean (SLSB) and autowiring it in test case.

## TestNG example

Example of running the tests using the TestNG.