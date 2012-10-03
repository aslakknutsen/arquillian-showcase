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
package org.jboss.arquillian.showcase.universe.core;

import java.util.concurrent.Callable;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.showcase.universe.Deployments;
import org.jboss.arquillian.showcase.universe.Models;
import org.jboss.arquillian.showcase.universe.model.Conference;
import org.jboss.arquillian.showcase.universe.repository.ConferenceRepository;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
@RunWith(Arquillian.class)
public class ConferenceRepositoryTestCase {

    @Deployment
    public static JavaArchive deploy() {
        return Deployments.Backend.conference();
    }

    @EJB
    private ConferenceRepository repository;
    
    @Inject
    private UserTransaction transaction;
    
    @Test
    public void shouldBeAbleToPersistConference() throws Exception {
        final Conference conference = Models.createRandomConference(); 
        conference.addSpeaker(Models.createUser());

        transactional(new StoreConference(repository, conference));
        Conference stored = transactional(new GetConference(repository, conference.getId()));

        Assert.assertNotNull(stored);

        Assert.assertEquals(conference.getName(), stored.getName());
        Assert.assertEquals(conference.getLocation(), stored.getLocation());
        Assert.assertEquals(conference.getDescription(), stored.getDescription());
        Assert.assertEquals(conference.getStart(), stored.getStart());
        Assert.assertEquals(conference.getEnd(), stored.getEnd());

        Assert.assertEquals(1, stored.getSpeakers().size());
    }
    
    private <T> T transactional(Callable<T> transactionalCallable) throws Exception {
        transaction.begin();
        T result = transactionalCallable.call();
        transaction.commit();
        return result;
    }

    private  static class StoreConference implements Callable<Void> {
        private ConferenceRepository repository;
        private Conference conference;

        public StoreConference(ConferenceRepository repository, Conference conference) {
            this.repository = repository;
            this.conference = conference;
        }

        @Override
        public Void call() throws Exception {
            repository.store(conference);
            return null;
        }
    }

    private class GetConference implements Callable<Conference> {
        private ConferenceRepository repository;
        private String id;

        public GetConference(ConferenceRepository repository, String id) {
            this.repository = repository;
            this.id = id;
        }
        
        @Override
        public Conference call() throws Exception {
            return repository.get(id);
        }
    }
}