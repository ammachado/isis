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


package org.apache.isis.metamodel.facets.actions;

import java.lang.reflect.Method;

import org.apache.isis.metamodel.facets.Facet;
import org.apache.isis.metamodel.facets.FacetFactoryAbstract;
import org.apache.isis.metamodel.facets.FacetHolder;
import org.apache.isis.metamodel.facets.MethodFilteringFacetFactory;
import org.apache.isis.metamodel.facets.MethodRemover;
import org.apache.isis.metamodel.facets.MethodScope;
import org.apache.isis.metamodel.spec.feature.ObjectFeatureType;


/**
 * Designed to simply filter out {@link Iterable#iterator()} method if it exists.
 * 
 * <p>
 * Does not add any {@link Facet}s.
 */
public class IteratorFilteringFacetFactory extends FacetFactoryAbstract implements MethodFilteringFacetFactory {

    public IteratorFilteringFacetFactory() {
        super(ObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> type, final MethodRemover methodRemover, final FacetHolder holder) {
        methodRemover.removeMethod(MethodScope.OBJECT, "iterator", java.util.Iterator.class, new Class[] {});
        return false;
    }

    public boolean recognizes(final Method method) {
        if (method.toString().equals("public abstract java.util.Iterator java.lang.Iterable.iterator()")) {
            return true;
        }
        return false;
    }

}
