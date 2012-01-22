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

package org.apache.isis.viewer.scimpi.dispatcher.view.action;

import java.util.List;

import org.apache.isis.core.metamodel.adapter.ObjectAdapter;
import org.apache.isis.core.metamodel.spec.ActionType;
import org.apache.isis.core.metamodel.spec.ObjectActionSet;
import org.apache.isis.core.metamodel.spec.feature.ObjectAction;
import org.apache.isis.core.metamodel.spec.feature.ObjectActionContainer.Contributed;
import org.apache.isis.runtimes.dflt.runtime.system.context.IsisContext;
import org.apache.isis.viewer.scimpi.dispatcher.AbstractElementProcessor;
import org.apache.isis.viewer.scimpi.dispatcher.Dispatcher;
import org.apache.isis.viewer.scimpi.dispatcher.context.RequestContext.Scope;
import org.apache.isis.viewer.scimpi.dispatcher.processor.Request;
import org.apache.isis.viewer.scimpi.dispatcher.util.MethodsUtils;
import org.apache.isis.viewer.scimpi.dispatcher.view.field.InclusionList;

public class Methods extends AbstractElementProcessor {

    @Override
    public void process(final Request request) {
        String objectId = request.getOptionalProperty(OBJECT);
        final boolean showForms = request.isRequested(FORMS, false);
        final ObjectAdapter object = MethodsUtils.findObject(request.getContext(), objectId);
        if (objectId == null) {
            objectId = request.getContext().mapObject(object, null);
        }

        final InclusionList inclusionList = new InclusionList();
        request.setBlockContent(inclusionList);
        request.processUtilCloseTag();

        request.appendHtml("<div class=\"actions\">");
        if (inclusionList.includes("edit") && !object.getSpecification().isService()) {
            request.appendHtml("<div class=\"action\">");
            request.appendHtml("<a class=\"button\" href=\"_generic_edit." + Dispatcher.EXTENSION + "?_result=" + objectId + "\">Edit...</a>");
            request.appendHtml("</div>");
        }
        writeMethods(request, objectId, object, showForms, inclusionList, "_generic.shtml?_result=" + objectId);
        request.popBlockContent();
        request.appendHtml("</div>");
    }

    public static void writeMethods(final Request request, final String objectId, final ObjectAdapter adapter, final boolean showForms, final InclusionList inclusionList, final String cancelTo) {
        List<ObjectAction> actions = adapter.getSpecification().getObjectActions(ActionType.USER, Contributed.INCLUDED);
        writeMethods(request, adapter, actions, objectId, showForms, inclusionList, cancelTo);
        // TODO determine if system is set up to display exploration methods
        if (true) {
            actions = adapter.getSpecification().getObjectActions(ActionType.EXPLORATION, Contributed.INCLUDED);
            writeMethods(request, adapter, actions, objectId, showForms, inclusionList, cancelTo);
        }
        // TODO determine if system is set up to display debug methods
        if (true) {
            actions = adapter.getSpecification().getObjectActions(ActionType.DEBUG, Contributed.INCLUDED);
            writeMethods(request, adapter, actions, objectId, showForms, inclusionList, cancelTo);
        }
    }

    private static void writeMethods(final Request request, final ObjectAdapter adapter, List<ObjectAction> actions, final String objectId, final boolean showForms, final InclusionList inclusionList, final String cancelTo) {
        actions = inclusionList.includedActions(actions);
        for (int j = 0; j < actions.size(); j++) {
            final ObjectAction action = actions.get(j);
            if (action instanceof ObjectActionSet) {
                request.appendHtml("<div class=\"actions\">");
                writeMethods(request, adapter, action.getActions(), objectId, showForms, inclusionList, cancelTo);
                request.appendHtml("</div>");
            } else if (action.isContributed()) {
                if (action.getParameterCount() == 1 && adapter.getSpecification().isOfType(action.getParameters().get(0).getSpecification())) {
                    if (objectId != null) {
                        final ObjectAdapter target = request.getContext().getMappedObject(objectId);
                        final ObjectAdapter realTarget = action.realTarget(target);
                        final String realTargetId = request.getContext().mapObject(realTarget, Scope.INTERACTION);
                        writeMethod(request, adapter, new String[] { objectId }, action, realTargetId, showForms, cancelTo);
                    } else {
                        request.appendHtml("<div class=\"action\">");
                        request.appendAsHtmlEncoded(action.getName());
                        request.appendHtml("???</div>");
                    }
                } else if (!adapter.getSpecification().isService()) {
                    writeMethod(request, adapter, new String[0], action, objectId, showForms, cancelTo);
                }
            } else {
                writeMethod(request, adapter, new String[0], action, objectId, showForms, cancelTo);
            }
        }
    }

    private static void writeMethod(final Request request, final ObjectAdapter adapter, final String[] parameters, final ObjectAction action, final String objectId, final boolean showForms, final String cancelTo) {
        // if (action.isVisible(IsisContext.getSession(), null) &&
        // action.isVisible(IsisContext.getSession(), adapter))
        // {
        if (action.isVisible(IsisContext.getAuthenticationSession(), adapter).isAllowed()) {
            request.appendHtml("<div class=\"action\">");
            if (IsisContext.getSession() == null) {
                request.appendHtml("<span class=\"disabled\" title=\"no user logged in\">");
                request.appendAsHtmlEncoded(action.getName());
                request.appendHtml("</span>");
                /*
                 * } else if (action.isUsable(IsisContext.getSession(),
                 * null).isVetoed()) {
                 * request.appendHtml("<span class=\"disabled\" title=\"" +
                 * action.isUsable(IsisContext.getSession(), null).getReason() +
                 * "\">"); request.appendHtml(action.getName());
                 * request.appendHtml("</span>");
                 */} else if (action.isUsable(IsisContext.getAuthenticationSession(), adapter).isVetoed()) {
                request.appendHtml("<span class=\"disabled\" title=\"" + action.isUsable(IsisContext.getAuthenticationSession(), adapter).getReason() + "\">");
                request.appendAsHtmlEncoded(action.getName());
                request.appendHtml("</span>");
            } else {
                final String version = request.getContext().mapVersion(adapter);
                if (action.getParameterCount() == 0 || (action.isContributed() && action.getParameterCount() == 1)) {
                    ActionButton.write(request, adapter, action, parameters, objectId, version, "_generic." + Dispatcher.EXTENSION, null, null, null, null, null, null, null, null, null);
                } else if (showForms) {
                    final CreateFormParameter params = new CreateFormParameter();
                    params.objectId = objectId;
                    params.methodName = action.getId();
                    params.forwardResultTo = "_generic." + Dispatcher.EXTENSION;
                    params.buttonTitle = "OK";
                    params.formTitle = action.getName();
                    // parameters.resultName =
                    // request.getOptionalProperty(RESULT_NAME);
                    // parameters.resultOverride =
                    // request.getOptionalProperty(RESULT_OVERRIDE);
                    // parameters.scope = request.getOptionalProperty(SCOPE);
                    // parameters.className = request.getOptionalProperty(CLASS,
                    // "action");
                    // parameters.id = request.getOptionalProperty(ID);
                    ActionForm.createForm(request, params, true);
                } else {
                    request.appendHtml("<a class=\"button\" href=\"_generic_action." + Dispatcher.EXTENSION + "?_result=" + objectId + "&amp;" + VERSION + "=" + version + "&_method=" + action.getId());
                    if (cancelTo != null) {
                        request.appendHtml("&_cancel-to=");
                        request.appendAsHtmlEncoded("cancel-to=\"" + cancelTo + "\"");
                    }
                    request.appendHtml("\" title=" + action.getDescription() + ">");
                    request.appendAsHtmlEncoded(action.getName() + "...");
                    request.appendHtml("</a>");
                }
            }
            request.appendHtml("</div>");
        }
    }

    @Override
    public String getName() {
        return "methods";
    }

}