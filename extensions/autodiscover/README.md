AutoDiscover Dependencies / Runtime Enrichment Extension 
========================================================

Extension showing how you can append additional classes / resources to your @Deployment by scanning the TestClass for metadata, 
without defining them directly in the @Deployemnt method and do runtime enrichment.

Extension will add the library "org.mockito:mockito-all" to the @Deployment if a @Mock field is defined on the TestClass. 
When executed in-container the @Mock fields will be enriched by mocking them in Mockito and injected into the test instance.  

    @RunWith(Arquillian.class)
    public class MyTestCase {
    
        @Deployment
        public static WebArchive deploy() {
            return ShrinkWrap.create(WebArchive.class)
            .addClass(AccountService.class);
        }
    
        @Mock
        private AccountService service;
    
        @Test
        public void shouldBeAbleToAccessMockito() {
            service.withdraw(100);
        }
    }

Following SPI's are used:

Client side
------------

* org.jboss.arquillian.core.spi.LoadableExtension
* org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor
* org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender

Container side
---------------

* org.jboss.arquillian.container.test.spi.RemoteLoadableExtension
* org.jboss.arquillian.test.spi.TestEnricher
