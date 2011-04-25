package com.acme.cdi.event;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

public class WordProcessor {
    // @Any allows us to select a qualified event to fire
    @Inject
    @Any
    private Event<Document> documentEvent;

    private Document document;

    public void create(int pages) {
        if (document != null) {
            throw new IllegalStateException("Document open. Please close it first!");
        }
        document = new Document(pages);
    }

    public void close() {
        document = null;
    }

    @SuppressWarnings("serial")
    public void printUnknownSize(Document document) {
        documentEvent.select(new AnnotationLiteral<Default>() {
        }).fire(document);
    }

    public void print() {
        documentEvent.select(new PrintJobLiteral(getJobSize())).fire(document);
    }

    public JobSize getJobSize() {
        int pp = document.getPages();
        if (pp < 5) {
            return JobSize.SMALL;
        }
        if (pp < 50) {
            return JobSize.MEDIUM;
        }
        return JobSize.LARGE;
    }
}
