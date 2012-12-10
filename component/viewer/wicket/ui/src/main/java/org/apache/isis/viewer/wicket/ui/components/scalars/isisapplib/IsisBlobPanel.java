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

package org.apache.isis.viewer.wicket.ui.components.scalars.isisapplib;

import java.util.List;

import org.apache.isis.applib.value.Blob;
import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.viewer.wicket.model.models.ScalarModel;
import org.apache.isis.viewer.wicket.ui.components.bookmarkedpages.BookmarkedPagesPanel;
import org.apache.isis.viewer.wicket.ui.components.scalars.ScalarPanelAbstract;
import org.apache.isis.viewer.wicket.ui.util.Components;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ByteArrayResource;

/**
 * Panel for rendering scalars of type {@link Blob Isis' applib.Blob}.
 */
public class IsisBlobPanel extends ScalarPanelAbstract {

    private static final long serialVersionUID = 1L;
    
    @SuppressWarnings("unused")
    private static final Logger LOG = Logger.getLogger(IsisBlobPanel.class);
    
    private static final String ID_SCALAR_IF_REGULAR = "scalarIfRegular";
    private static final String ID_SCALAR_IF_REGULAR_DOWNLOAD = "scalarIfRegularDownload";
    private static final String ID_SCALAR_IF_REGULAR_CLEAR = "scalarIfRegularClear";
    private static final String ID_SCALAR_NAME = "scalarName";
    private static final String ID_SCALAR_VALUE = "scalarValue";
    private static final String ID_FEEDBACK = "feedback";
    
    private static final String ID_SCALAR_IF_COMPACT = "scalarIfCompact";
    private static final String ID_SCALAR_IF_COMPACT_DOWNLOAD = "scalarIfCompactDownload";
    
    public IsisBlobPanel(final String id, final ScalarModel model) {
        super(id, model);
    }
    
    @Override
    protected FormComponentLabel addComponentForRegular() {
        final FileUploadField fileUploadField = createFileUploadField(ID_SCALAR_VALUE);
        fileUploadField.setLabel(Model.of(getModel().getName()));
        
        final FormComponentLabel scalarIfRegular = new FormComponentLabel(ID_SCALAR_IF_REGULAR, fileUploadField);
        scalarIfRegular.add(fileUploadField);
        
        final Label scalarName = new Label(ID_SCALAR_NAME, getModel().getName());
        scalarIfRegular.add(scalarName);
        
        updateDownloadLink(ID_SCALAR_IF_REGULAR_DOWNLOAD, scalarIfRegular);
        scalarIfRegular.addOrReplace(new ComponentFeedbackPanel(ID_FEEDBACK, fileUploadField));
        
        addOrReplace(scalarIfRegular);
        
        return scalarIfRegular;
    }

    @Override
    protected Component addComponentForCompact() {
        final MarkupContainer scalarIfCompact = new WebMarkupContainer(ID_SCALAR_IF_COMPACT);
        updateDownloadLink(ID_SCALAR_IF_COMPACT_DOWNLOAD, scalarIfCompact);
        addOrReplace(scalarIfCompact);
        return scalarIfCompact;
    }

    protected void onBeforeRenderWhenViewMode() {
        updateRegularFormComponents(InputFieldVisibility.NOT_VISIBLE);
    }

    protected void onBeforeRenderWhenDisabled(final String disableReason) {
        updateRegularFormComponents(InputFieldVisibility.NOT_VISIBLE);
    }

    protected void onBeforeRenderWhenEnabled() {
        updateRegularFormComponents(InputFieldVisibility.VISIBLE);
    }

    
    ///////////////////////////////////////////////
    // helpers
    ///////////////////////////////////////////////
    
    private FileUploadField createFileUploadField(String componentId) {
        final FileUploadField fileUploadField = new FileUploadField(componentId, new IModel<List<FileUpload>>() {

            private static final long serialVersionUID = 1L;

            @Override
            public void setObject(final List<FileUpload> fileUploads) {
                if (fileUploads == null || fileUploads.isEmpty()) {
                    return;
                }
                
                final FileUpload fileUpload = fileUploads.get(0);
                final String contentType = fileUpload.getContentType();
                final String clientFileName = fileUpload.getClientFileName();
                final byte[] bytes = fileUpload.getBytes();
                final Blob blob = new Blob(clientFileName, contentType, bytes);
                
                final ObjectAdapter adapter = getAdapterManager().adapterFor(blob);
                getModel().setObject(adapter);
            }

            @Override
            public void detach() {
            }

            @Override
            public List<FileUpload> getObject() {
                return null;
            }
            
        });
        return fileUploadField;
    }

    private Blob getBlob(final ScalarModel model) {
        ObjectAdapter adapter = model.getObject();
        return adapter != null? (Blob) adapter.getObject(): null;
    }

    private enum InputFieldVisibility {
        VISIBLE, NOT_VISIBLE;
    }
    
    private void updateRegularFormComponents(InputFieldVisibility visibility) {
        MarkupContainer formComponentLabel = (MarkupContainer) getComponentForRegular();
        formComponentLabel.get(ID_SCALAR_VALUE).setVisible(visibility == InputFieldVisibility.VISIBLE);
        updateClearLink(visibility);
        updateDownloadLink(ID_SCALAR_IF_REGULAR_DOWNLOAD, formComponentLabel);
    }

    private void updateClearLink(InputFieldVisibility visibility) {
        final MarkupContainer formComponent = (MarkupContainer) getComponentForRegular();
        formComponent.setOutputMarkupId(true); // enable ajax link

        final AjaxLink<Void> ajaxLink = new AjaxLink<Void>(ID_SCALAR_IF_REGULAR_CLEAR){
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                setEnabled(false);
                getModel().setObject(null);
                target.add(formComponent);
            }
        };
        ajaxLink.setOutputMarkupId(true);
        formComponent.addOrReplace(ajaxLink);

        final Blob blob = getBlob(getModel());
        formComponent.get(ID_SCALAR_IF_REGULAR_CLEAR).setVisible(blob != null && visibility == InputFieldVisibility.VISIBLE);
    }

    private ResourceLink<?> updateDownloadLink(String downloadId, MarkupContainer container) {
        final ResourceLink<?> resourceLink = createResourceLink(downloadId);
        if(resourceLink != null) {
            container.addOrReplace(resourceLink);
        } else {
            Components.permanentlyHide(container, downloadId);
        }
        return resourceLink;
    }

    private ResourceLink<?> createResourceLink(String id) {
        final Blob blob = getBlob(getModel());
        if(blob == null) {
            return null;
        }
        final ByteArrayResource bar = new ByteArrayResource(blob.getMimeType().getBaseType(), blob.getBytes(), blob.getName());
        return new ResourceLink<Object>(id, bar);
    }

}