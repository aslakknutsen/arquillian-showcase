/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
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
package org.jboss.arquillian.showcase.universe;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.jboss.arquillian.showcase.universe.model.Conference;
import org.jboss.arquillian.showcase.universe.model.User;

/**
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class Models {


    public static Conference createConference() {
        Conference conference = createRandomConference();
        conference.setId("100");
        return conference;
    }

    public static Conference createRandomConference() {
        Conference conference = new Conference("DevNation 2014");
        conference.setLocation("Oslo, Norway");
        conference.setDescription("Passion for More");
        
        try {
            conference.setStart(parse("12.09.2012"));
            conference.setEnd(parse("13.09.2012"));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return conference;
    }
    
    public static User createUser() {
        return new User("aslak", "knutsen", "aslak@redhat.com");
    }
    
    private static Date parse(String date) throws ParseException {
        return DateFormat.getDateInstance(DateFormat.SHORT, new Locale("NO", "NO"))
                .parse(date);
    }
}