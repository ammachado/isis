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


package org.apache.isis.extensions.wicket.ui.components.entity.tabbed;

import org.apache.isis.extensions.wicket.model.models.EntityModel;
import org.apache.isis.extensions.wicket.ui.ComponentType;
import org.apache.isis.extensions.wicket.ui.tabs.TabAbstract;

/**
 * Tab holding the entity properties.
 */
class EntityPropertiesTab extends TabAbstract<EntityModel> {
	private static final long serialVersionUID = 1L;

	public EntityPropertiesTab(String id, EntityModel entityModel)
    {
        super(id, entityModel);
		addOrReplace(ComponentType.ENTITY_PROPERTIES, entityModel);
    }
}