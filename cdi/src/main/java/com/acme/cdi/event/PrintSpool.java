package com.acme.cdi.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;

/**
 * A hypothetical print spool that demonstrates the selection and invocation of event observers.
 * 
 * <p>
 * This bean receives document events and records the document as printed. It classifies received documents by job size.
 * </p>
 * 
 * <p>
 * Observer resolution is defined in section 10.2 of the JSR-299 specification. Type-safe resolution rules, defined in section
 * 5.2 of the JSR-299 specification, are used when resolving observers for an event.
 * </p>
 * 
 * <p>
 * An observer method will be notified of an event if the event object is assignable to the observed event type, and if all the
 * qualifiers of the event observer are also qualifiers of the event.
 * </p>
 * 
 * <p>
 * Thus, an event must have all of the qualifiers specified on the observer (called the required qualifiers) for the observer to
 * match the event. The event may have additional qualifiers not specified on the observer. In other words, the qualifiers on
 * the observer must be a subset of the qualifiers on the event (or the qualifiers on the event must be a superset of the
 * qualifiers on the observer).
 * </p>
 * 
 * <p>
 * If the observer does not specify any qualifiers, it's assigned the &#064;Any qualifier, and hence matches an event with any
 * number of qualifiers. An observer with the qualifier &#064;Default matches an event with no qualifiers since events with no
 * qualifiers are assigned the &#064;Default qualifier.
 * </p>
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 */
@RequestScoped
public class PrintSpool {
    private Map<JobSize, List<Document>> documents;

    private int numDocumentsSent = 0;

    private int numFailedDocuments = 0;

    public void onPrint(@Observes @Any Document document) {
        numDocumentsSent++;
        System.out.println("A document is headed to the print spool!");
    }

    public void onPrintUnknownSize(@Observes @Default Document document) {
        System.out.println("Can't accept a document of unknown size");
        numFailedDocuments++;
        // not throwing exception because we want to verify observers that matched
        // an exception would terminate execution of the observers
        // throw new IllegalArgumentException("Cannot print unknown document size");
    }

    public void onLargePrint(@Observes @PrintJob(JobSize.LARGE) Document document) {
        System.out.println(document.getPages() + " pages printed (large job)");
        documents.get(JobSize.LARGE).add(document);
    }

    public void onMediumPrint(@Observes @PrintJob(JobSize.MEDIUM) Document document) {
        System.out.println(document.getPages() + " pages printed (medium job)");
        documents.get(JobSize.MEDIUM).add(document);
    }

    public void onSmallPrint(@Observes @PrintJob(JobSize.SMALL) Document document) {
        System.out.println(document.getPages() + " pages printed (small job)");
        documents.get(JobSize.SMALL).add(document);
    }

    public List<Document> getDocumentsProcessed(JobSize withSize) {
        return documents.get(withSize);
    }

    public int getNumDocumentsSent() {
        return numDocumentsSent;
    }

    public int getNumFailedDocuments() {
        return numFailedDocuments;
    }

    public List<Document> getDocumentsProcessed() {
        List<Document> all = new ArrayList<Document>();
        for (List<Document> ofSize : documents.values()) {
            all.addAll(ofSize);
        }
        return all;
    }

    @PostConstruct
    public void initialize() {
        System.out.println("Initializing print spool");
        documents = new HashMap<JobSize, List<Document>>();
        for (JobSize s : JobSize.values()) {
            documents.put(s, new ArrayList<Document>());
        }
    }
}
