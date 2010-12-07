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


package org.apache.isis.core.runtime.authentication.standard.singleuser;

import java.io.IOException;

import org.apache.isis.core.metamodel.authentication.AuthenticationSessionAbstract;
import org.apache.isis.core.metamodel.encoding.DataInputExtended;
import org.apache.isis.core.metamodel.encoding.DataOutputExtended;
import org.apache.isis.core.metamodel.encoding.Encodable;

public final class SingleUserSession extends AuthenticationSessionAbstract implements Encodable {
	
	private static final long serialVersionUID = 1L;
	
    private static final String DEFAULT_USER_NAME = "exploration";

    /**
     * Defaults validation code to <tt>""</tt>.
     */
    public SingleUserSession() {
    	this("");
    }

    public SingleUserSession(String code) {
        super(DEFAULT_USER_NAME, code);
        initialized();
    }

	public SingleUserSession(DataInputExtended input) throws IOException {
		super(input);
		initialized();
	}

	@Override
	public void encode(DataOutputExtended output)
			throws IOException {
		super.encode(output);
	}
	
	private void initialized() {
		// nothing to do
	}

}