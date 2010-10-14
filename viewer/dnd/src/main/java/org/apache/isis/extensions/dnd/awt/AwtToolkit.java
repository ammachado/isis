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


package org.apache.isis.extensions.dnd.awt;

import org.apache.isis.extensions.dnd.view.Toolkit;
import org.apache.isis.extensions.dnd.viewer.DefaultContentFactory;
import org.apache.isis.extensions.dnd.viewer.SkylarkViewFactory;
import org.apache.isis.extensions.dnd.viewer.basic.LogoBackground;


public class AwtToolkit extends Toolkit {
    @Override
    protected void init() {
        final XViewer v = new XViewer();
        final XFeedbackManager f = new XFeedbackManager(v);
        v.setFeedbackManager(f);
        feedbackManager = f;
        viewer = v;
        viewer.setBackground(new LogoBackground());
        contentFactory = new DefaultContentFactory();
        viewFactory = new SkylarkViewFactory();
        colorsAndFonts = new AwtColorsAndFonts();

        colorsAndFonts.init();
    }
}