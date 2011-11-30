 Arquillian Showcase
                                 
 Arquillian enables you to test your business logic in a remote or embedded container. Alternatively, it can deploy an archive to the container so the test can 
 interact as a remote client.
 
 All about arquillian: http://jboss.org/arquillian
 
GlassFish requires update to Weld 1.1.1.Final, see http://seamframework.org/Seam3/Compatibility

# Extensions

autodiscover - analyzes test case and generates mocks for fields annotated @Mock; demonstrates a custom enricher

declarative - analyzes test case and adds libraries to archive declared in @Library annotation; demonstrates use of an archive processor fed by declarative metadata

deploymentscenario - allows you to configure in arquillian.xml the @Deployment method per test class; demonstrates use of a custom deployment scenario generator

lifecycle - supports annotated methods for observing before/after deploy/undeploy life cycle events; demonstrates registration of a life cycle observer

systemproperties - adds system properties to the test archive that can be referenced from the test; demonstrates use of an archive processor/appender

weld-servlet - adds the weld-servlet library to the deployment archive for running tests that use cdi in a servlet environment; demonstrates the use of the archive processor/appender
