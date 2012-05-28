Declarative Dependency Extension 
================================

Extension showing how you can append additional classes / resources to your @Deployment, 
without defining them directly in the @Deployemnt method.

    @Library("org.mockito:mickito-all:1.8.3")
    @RunWith(Arquillian.class)
    public class MyTestCase {
    
        @Deployment
        public static WebArchive deploy() {
            return ShrinkWrap.create(WebArchive.class);
        }
      
        @Test
        public void shouldBeAbleToAccessMockito() {
            Class.forName("org.mockito.Mock");
        }
    }

Following SPI's are used:

* org.jboss.arquillian.core.spi.LoadableExtension
* org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor