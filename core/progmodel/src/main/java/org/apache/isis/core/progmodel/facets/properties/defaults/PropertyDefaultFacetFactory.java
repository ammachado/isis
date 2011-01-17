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


package org.apache.isis.core.progmodel.facets.properties.defaults;

import java.lang.reflect.Method;

import org.apache.isis.core.commons.lang.NameUtils;
import org.apache.isis.core.metamodel.adapter.map.AdapterMap;
import org.apache.isis.core.metamodel.adapter.map.AdapterMapAware;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.facetapi.FacetUtil;
import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.methodutils.MethodScope;
import org.apache.isis.core.progmodel.facets.MethodFinderUtils;
import org.apache.isis.core.progmodel.facets.MethodPrefixBasedFacetFactoryAbstract;
import org.apache.isis.core.progmodel.facets.MethodPrefixConstants;


public class PropertyDefaultFacetFactory extends MethodPrefixBasedFacetFactoryAbstract implements AdapterMapAware {

    private static final String[] PREFIXES = { MethodPrefixConstants.DEFAULT_PREFIX };

    private AdapterMap adapterMap;

    public PropertyDefaultFacetFactory() {
        super(FeatureType.PROPERTIES_ONLY, PREFIXES);
    }

    @Override
    public void process(ProcessMethodContext processMethodContext) {

        attachPropertyDefaultFacetIfDefaultMethodIsFound(processMethodContext);
    }

    private void attachPropertyDefaultFacetIfDefaultMethodIsFound(ProcessMethodContext processMethodContext) {
        
        final Method getMethod = processMethodContext.getMethod();
        final String capitalizedName = NameUtils.javaBaseName(getMethod.getName());
        
        Class<?> cls = processMethodContext.getCls();
        final Class<?> returnType = getMethod.getReturnType();
        final Method method = MethodFinderUtils.findMethod(cls, MethodScope.OBJECT, MethodPrefixConstants.DEFAULT_PREFIX + capitalizedName, returnType, NO_PARAMETERS_TYPES);
        if (method == null) {
            return;
        } 
        processMethodContext.removeMethod(method);
        
        final FacetHolder property = processMethodContext.getFacetHolder();
        FacetUtil.addFacet(new PropertyDefaultFacetViaMethod(method, property, getSpecificationLookup(), getAdapterMap()));
    }

    /////////////////////////////////////////////////////////
    // Dependencies (injected)
    /////////////////////////////////////////////////////////
    
    @Override
    public void setAdapterMap(AdapterMap adapterManager) {
        this.adapterMap = adapterManager;
    }

    protected AdapterMap getAdapterMap() {
        return adapterMap;
    }

}
