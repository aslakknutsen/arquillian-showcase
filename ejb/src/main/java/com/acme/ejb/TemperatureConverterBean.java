/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.acme.ejb;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(TemperatureConverter.class)
public class TemperatureConverterBean implements TemperatureConverter {
    @Resource
    private EJBContext ctx;

    public double convertToCelsius(double f) {
        return ((f - 32) * 5 / 9);
    }

    public double convertToFarenheit(double c) {
        return ((c * 9 / 5) + 32);
    }

    public boolean isTransactionActive() {
        if (ctx == null) {
            return false;
        }

        try {
            ctx.getRollbackOnly();
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }
}
