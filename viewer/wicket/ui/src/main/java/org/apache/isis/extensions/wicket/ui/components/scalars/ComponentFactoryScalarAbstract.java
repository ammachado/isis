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


package org.apache.isis.extensions.wicket.ui.components.scalars;

import org.apache.isis.extensions.wicket.model.models.ScalarModel;
import org.apache.isis.extensions.wicket.ui.ComponentFactoryAbstract;
import org.apache.isis.extensions.wicket.ui.ComponentType;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

public abstract class ComponentFactoryScalarAbstract extends ComponentFactoryAbstract {

	private static final long serialVersionUID = 1L;
	
	private final Class<?>[] scalarTypes;

	public ComponentFactoryScalarAbstract(final Class<?>... scalarTypes) {
		super(ComponentType.SCALAR_NAME_AND_VALUE);
		this.scalarTypes = scalarTypes;
	}

	@Override
	public ApplicationAdvice appliesTo(IModel<?> model) {
		if (!(model instanceof ScalarModel)) {
			return ApplicationAdvice.DOES_NOT_APPLY;
		}
		ScalarModel scalarModel = (ScalarModel) model;
		return appliesIf(scalarModel.isScalarTypeAnyOf(scalarTypes));
	}

	public final Component createComponent(String id, IModel<?> model) {
		ScalarModel scalarModel = (ScalarModel) model;
		return createComponent(id, scalarModel);
	}

	protected abstract Component createComponent(String id, ScalarModel scalarModel);

}
