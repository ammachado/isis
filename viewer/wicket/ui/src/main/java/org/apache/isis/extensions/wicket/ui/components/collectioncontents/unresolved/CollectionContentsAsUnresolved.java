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


package org.apache.isis.extensions.wicket.ui.components.collectioncontents.unresolved;

import org.apache.isis.extensions.wicket.model.models.EntityCollectionModel;
import org.apache.isis.extensions.wicket.ui.panels.PanelAbstract;

/**
 * {@link PanelAbstract Panel} that represents a placeholder for a {@link EntityCollectionModel
 * collection of entity}s so that they can be only lazily resolved.
 */
public class CollectionContentsAsUnresolved extends
		PanelAbstract<EntityCollectionModel> {

	private static final long serialVersionUID = 1L;

	public CollectionContentsAsUnresolved(final String id,
			final EntityCollectionModel model) {
		super(id, model);
		buildGui();
	}

	private void buildGui() {
	    // nothing
	}

}
