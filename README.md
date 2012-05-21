# Arquillian Showcase

This repository contains a collection of tests that showcase Arquillian's capabilities. The test cases are organized into modules according to the technology that is being tested. For example, CDI tests are located in the cdi project, EJB tests in the ejb project and so on. The dependencies used in each of the projects are managed by the parent project.

## System Requirements 

To run the tests in these project, you need the following:

1. Java SDK 1.6, to compile the Java code and run Maven. You can choose from the following:
    * OpenJDK
    * Oracle Java SE
    * Oracle JRockit

2. Maven 3.0.0 or newer, to run the tests from the commandline
    * If you have not yet installed Maven, see the [Maven Getting Started Guide](http://maven.apache.org/guides/getting-started/index.html) for details.
    * If you have installed Maven, you can check the version by typing the following in a command line:

        mvn --version 

3. You can also use Eclipse with the [m2e plugin](http://www.eclipse.org/m2e/), NetBeans or IntelliJ IDEA to run the tests from the IDE.

## Preparing the projects

To prepare the projects, you first need to build the parent and container BOM projects and install them into your local Maven repository.

Begin by opening a terminal and switching to the root directory of the project. Then run the following Maven command:

    mvn install -Psetup

## Running your first tests

The simplest tests to run are the tests in the cdi project. By default, the tests will run on a Weld (EE) Embedded container. There is no extra configuration necessary.

    mvn test -f cdi/pom.xml

You can also run the tests on a managed JBoss AS 7 application server. Maven will handle downloading and extracting the server for you. All you need to do is activate the Maven profile setup for running tests in this environment:

    mvn test -f cdi/pom.xml -Parq-jbossas-managed-7

In the two examples above, you can drop the -f argument if you first change to the cdi directory:

    cd cdi
    mvn test
    mvn test -Parq-jbossas-managed-7

There are many other profiles available for running the tests on other CDI-compliant servers and embedded runtimes.

You can run all the tests in the showcase using the Maven test goal from the root directory of the showcase:

    mvn test

The tests are run on the default container specified for each project.

## Extension projects

The extensions directory contains several examples of Arquillian extensions. Here's a list of extension projects with a short description of each.

* **autodiscover** - analyzes test case and generates mocks for fields annotated @Mock; demonstrates a custom enricher
* **declarative** - analyzes test case and adds libraries to archive declared in @Library annotation; demonstrates use of an archive processor fed by declarative metadata
* **deploymentscenario** - allows you to configure in arquillian.xml the @Deployment method per test class; demonstrates use of a custom deployment scenario generator
* **lifecycle** - supports annotated methods for observing before/after deploy/undeploy life cycle events; demonstrates registration of a life cycle observer
* **systemproperties** - adds system properties to the test archive that can be referenced from the test; demonstrates use of an archive processor/appender
* **weld-servlet** - adds the weld-servlet library to the deployment archive for running tests that use cdi in a servlet environment; demonstrates the use of the archive processor/appender

