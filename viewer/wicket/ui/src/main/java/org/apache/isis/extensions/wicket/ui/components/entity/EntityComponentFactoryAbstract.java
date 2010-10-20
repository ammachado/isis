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


package org.apache.isis.extensions.wicket.ui.components.entity;

import org.apache.isis.extensions.wicket.model.models.EntityModel;
import org.apache.isis.extensions.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.extensions.wicket.ui.ComponentType;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.facets.object.value.ValueFacet;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * Convenience adapter for a number of {@link ComponentFactoryAbstract component factory}s that
 * where the created {@link Component} are backed by an {@link EntityModel}.
 */
public abstract class EntityComponentFactoryAbstract extends ComponentFactoryAbstract {

	private static final long serialVersionUID = 1L;

	public EntityComponentFactoryAbstract(ComponentType componentType) {
		super(componentType);
	}

	public EntityComponentFactoryAbstract(ComponentType componentType, String name) {
		super(componentType, name);
	}

	@Override
	protected ApplicationAdvice appliesTo(IModel<?> model) {
		if (!(model instanceof EntityModel)) {
			return ApplicationAdvice.DOES_NOT_APPLY;
		}
		EntityModel entityModel = (EntityModel) model;
		ObjectAdapter adapter = entityModel.getObject();
		if (adapter == null) {
			// is ok;
		}
		ObjectSpecification specification = entityModel.getTypeOfSpecification();
		final boolean isObject = specification.isNotCollection();
		final boolean isValue = specification.containsFacet(ValueFacet.class);
		return appliesIf(isObject && !isValue);
	}

}
