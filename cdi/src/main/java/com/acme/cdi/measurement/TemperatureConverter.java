package com.acme.cdi.measurement;

public class TemperatureConverter {
    public double convertToCelsius(double f) {
        return ((f - 32) * 5 / 9);
    }

    public double convertToFarenheit(double c) {
        return ((c * 9 / 5) + 32);
    }
}
