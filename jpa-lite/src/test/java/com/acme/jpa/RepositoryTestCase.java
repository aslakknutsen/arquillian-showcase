package com.acme.jpa;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.ejb.EJB;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class RepositoryTestCase {
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(Record.class, LineItem.class, PersistenceService.class,
                        PersistenceServiceBean.class, Repository.class, RepositoryBean.class)
                .addAsManifestResource("test-persistence.xml", "persistence.xml");
    }

    @EJB
    PersistenceService service;

    @EJB
    Repository repository;

    private static List<Record> seedRecords;

    private static long idOfFirstRecord;

    @Before
    public void seed_database() {
        // emulate @BeforeClass on instance
        // nice if this was supported in Arquillian
        // sucks that seedRecords has to be static
        if (seedRecords == null) {
            seedRecords = service.seed(true);
            idOfFirstRecord = seedRecords.get(0).getId();
        }
    }

    @Test
    public void entity_should_be_managed_outside_of_active_ejb() {
        Record record = repository.retrieveById(Record.class, idOfFirstRecord);
        repository.isManaging(record);
    }

    @Test
    public void lazy_load_should_succeed_outside_of_active_ejb() {
        Record record = repository.retrieveById(Record.class, idOfFirstRecord);
        int numLineItems = 1;
        try {
            numLineItems = record.getLineItems().size();
        } catch (Exception e) {
        }
        assertEquals(1, numLineItems);
    }

    @Test
    public void lazy_load_should_fail_outside_of_removed_ejb() {
        Record record = repository.retrieveById(Record.class, idOfFirstRecord);
        repository.close();
        // Some JPA providers (EclipseLink) allow LAZY relationships to be accessed after session is closed
        int expected = service.isLazyLoadingPermittedOnClosedSession() ? 1 : 0;
        int numLineItems = 0;
        try {
            numLineItems = record.getLineItems().size();
        } catch (Exception e) {
        }
        assertEquals(expected, numLineItems);
    }

    @Test
    public void dirty_change_should_be_flushed_by_transactional_method_on_managing_ejb() {
        Record record = repository.retrieveById(Record.class, idOfFirstRecord);
        String name = record.getName();
        record.setName(name + "-renamed");
        repository.update(record);
        List<Record> results = repository.retrieveByQuery(Record.class, "select r from Record r where r.name = ?1", name
                + "-renamed");
        assertEquals(1, results.size());
    }

    @Test
    public void dirty_change_should_be_flushed_by_transactional_method_on_another_ejb() {
        Record record = repository.retrieveById(Record.class, idOfFirstRecord);
        String name = record.getName();
        record.setName(name + "-renamed");
        service.transact();
        List<Record> results = repository.retrieveByQuery(Record.class, "select r from Record r where r.name = ?1", name
                + "-renamed");
        assertEquals(1, results.size());
    }
}
