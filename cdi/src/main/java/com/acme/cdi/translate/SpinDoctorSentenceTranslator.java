package com.acme.cdi.translate;

public class SpinDoctorSentenceTranslator implements SentenceTranslator {
    @Override
    public String translate(String sentence) {
        return sentence.replace("escalation", "surge");
    }
}
