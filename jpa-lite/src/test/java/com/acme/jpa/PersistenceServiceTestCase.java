package com.acme.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PersistenceServiceTestCase
{
   @Deployment
   public static JavaArchive createDeployment()
   {
      return ShrinkWrap.create(JavaArchive.class)
         .addClasses(Record.class, LineItem.class,
               PersistenceService.class, PersistenceServiceBean.class,
               PersistenceManager.class, PersistenceManagerBean.class)
         .addAsManifestResource("test-persistence.xml", "persistence.xml");
   }

   @EJB
   PersistenceService service;
   
   @EJB
   PersistenceManager manager;

   private static List<Record> seedRecords;
   
   @Before
   public void seed_database()
   {
      // emulate @BeforeClass on instance
      // nice if this was supported in Arquillian
      // sucks that seedRecords has to be static
      if (seedRecords == null)
      {
         seedRecords = service.seed(true);
      }
   }
   
   @Test
   public void query_should_return_seed_record()
   {
      List<Record> results = service.selectAll();
      assertEquals("Should have found 1 record", 1, results.size());
   }
   
   @Test
   public void lazy_load_should_succeed_using_extended_persistence_context()
   {
      Record record = manager.select(seedRecords.get(0).getId(), false);
      assertTrue(manager.isManaged(record));
      // verify the line items can be fetched outside of EJB
      assertEquals(1, record.getLineItems().size());
   }
   
   @Test
   public void lazy_load_should_succeed_within_transaction()
   {
      Record record = service.select(seedRecords.get(0).getId(), true);
      assertEquals(1, record.getLineItems().size());
   }
   
   @Test
   public void lazy_load_should_fail_outside_transaction()
   {
      Record record = service.select(seedRecords.get(0).getId(), false);
      assertFalse(service.isManaged(record));
      // Some JPA providers (EclipseLink) allow LAZY relationships to be accessed after session is closed
      int expected = service.isLazyLoadingPermittedOnClosedSession() ? 1 : 0;
      int numLineItems = 0;
      try
      {
         numLineItems = record.getLineItems().size();
      }
      catch (Exception e)
      {
      }
      // verify the line items can't be fetched outside of EJB
      assertEquals(expected, numLineItems);
   }
}
