package com.acme.cdi.event;

public class Document {
    public Document(int pages) {
        this.pages = pages;
    }

    private int pages;

    public int getPages() {
        return pages;
    }
}
