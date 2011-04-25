package com.acme.cdi.translate;

import javax.inject.Inject;

public class TextTranslatorService {
    @Inject
    private SentenceTranslator translator;

    @Inject
    private SentenceParser parser;

    public String translate(String text) {
        StringBuilder sb = new StringBuilder();
        for (String phrase : parser.parse(text)) {
            sb.append(translator.translate(phrase)).append(". ");
        }
        return sb.toString().trim();
    }
}
