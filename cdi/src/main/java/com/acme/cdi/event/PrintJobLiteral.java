package com.acme.cdi.event;

import javax.enterprise.util.AnnotationLiteral;

public class PrintJobLiteral extends AnnotationLiteral<PrintJob> implements PrintJob {
    private static final long serialVersionUID = 1L;

    private JobSize size;

    public PrintJobLiteral(JobSize size) {
        this.size = size;
    }

    public JobSize value() {
        return size;
    }
}
