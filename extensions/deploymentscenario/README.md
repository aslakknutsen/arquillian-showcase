Externalized Deployment Scenario Extension 
========================================================

Extension showing how you can replace the default behavior of defining the @Deployment method in the TestClass itself. 

We're implementing the DeploymentScenarioGenerator to read mapping between TestClass and Archive from a external source, arquillian.xml.  

```java
@RunWith(Arquillian.class)
public class MyTestCase {

  @Inject 
  private AccountService service;
  
  @Test
  public void shouldBeAbleTo() {
    service.withdraw(100);
  }
}
```


Following SPI's are used:

Client side
------------

* org.jboss.arquillian.core.spi.LoadableExtension
* org.jboss.arquillian.container.test.spi.client.deployment.DeploymentScenarioGenerator
