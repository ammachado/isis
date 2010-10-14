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


package org.apache.isis.extensions.dnd.field;

import org.apache.isis.metamodel.consent.Allow;
import org.apache.isis.metamodel.consent.Consent;
import org.apache.isis.metamodel.consent.Veto;
import org.apache.isis.extensions.dnd.drawing.Location;
import org.apache.isis.extensions.dnd.view.View;
import org.apache.isis.extensions.dnd.view.Workspace;


public class CopyValueOption extends AbstractValueOption {

    public CopyValueOption(final AbstractField field) {
        super(field, "Copy");
    }

    @Override
    public Consent disabled(final View view) {
        if (isEmpty(view)) {
            // TODO: move logic into Facets
            return new Veto("Field is empty");
        }
        // TODO: move logic into Facets
        return Allow.DEFAULT;
        //return new Allow(String.format("Copy value '%s' to clipboard", field.getSelectedText()));
    }

    @Override
    public void execute(final Workspace frame, final View view, final Location at) {
        field.copyToClipboard();
    }

    @Override
    public String toString() {
        return "CopyValueOption";
    }
}