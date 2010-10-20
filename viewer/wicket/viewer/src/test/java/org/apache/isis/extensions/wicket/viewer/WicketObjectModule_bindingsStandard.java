/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.extensions.wicket.viewer;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.apache.isis.extensions.wicket.ui.app.registry.ComponentFactoryList;
import org.apache.isis.extensions.wicket.ui.app.registry.ComponentFactoryRegistry;
import org.apache.isis.extensions.wicket.ui.pages.PageClassList;
import org.apache.isis.extensions.wicket.ui.pages.PageClassRegistry;
import org.apache.isis.extensions.wicket.viewer.WicketObjectsModule;
import org.apache.isis.extensions.wicket.viewer.registries.components.ComponentFactoryListDefault;
import org.apache.isis.extensions.wicket.viewer.registries.components.ComponentFactoryRegistryDefault;
import org.apache.isis.extensions.wicket.viewer.registries.pages.PageClassListDefault;
import org.apache.isis.extensions.wicket.viewer.registries.pages.PageClassRegistryDefault;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.inject.Guice;
import com.google.inject.Injector;

@RunWith(Parameterized.class)
public class WicketObjectModule_bindingsStandard {

    private WicketObjectsModule wicketObjectsModule;
    private Injector injector;
    private Class<?> from;
    private Class<?> to;
    
    @Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {ComponentFactoryList.class, ComponentFactoryListDefault.class},
                {ComponentFactoryRegistry.class, ComponentFactoryRegistryDefault.class},
                {PageClassList.class, PageClassListDefault.class},
                {PageClassRegistry.class, PageClassRegistryDefault.class},
                });
    }
    
    public WicketObjectModule_bindingsStandard(Class<?> from, Class<?> to) {
        this.from = from;
        this.to = to;
    }

    @Before
    public void setUp() throws Exception {
        wicketObjectsModule = new WicketObjectsModule();
        injector = Guice.createInjector(wicketObjectsModule);
    }

	@Test
	public void binding() {
		final Object instance = injector.getInstance(from);
		assertThat(instance, is(instanceOf(to)));
	}


}
