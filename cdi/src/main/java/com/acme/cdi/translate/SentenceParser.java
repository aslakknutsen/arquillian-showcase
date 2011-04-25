package com.acme.cdi.translate;

import java.util.Arrays;
import java.util.List;

public class SentenceParser {
    public List<String> parse(String text) {
        return Arrays.asList(text.split("[.?]"));
    }
}
