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
package org.apache.isis.objectstore.jdo.datanucleus.valuetypes;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.NucleusContext;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.mapped.DatastoreContainerObject;
import org.datanucleus.store.mapped.MappedStoreManager;
import org.datanucleus.store.mapped.mapping.SingleFieldMultiMapping;

import org.apache.isis.applib.value.Blob;

public class IsisBlobMapping extends SingleFieldMultiMapping {

    public IsisBlobMapping() {
    }
    
    @Override
    public Class<?> getJavaType() {
        return org.apache.isis.applib.value.Blob.class;
    }

    public void initialize(AbstractMemberMetaData mmd, DatastoreContainerObject container, ClassLoaderResolver clr)
    {
        super.initialize(mmd, container, clr);
        addDatastoreFields();
    }

    public void initialize(MappedStoreManager storeMgr, String type)
    {
        super.initialize(storeMgr, type);
        addDatastoreFields();
    }

    protected void addDatastoreFields()
    {
        addDatastoreField(ClassNameConstants.JAVA_LANG_STRING); // name
        addDatastoreField(ClassNameConstants.JAVA_LANG_STRING); // mime type
        addDatastoreField(ClassNameConstants.JAVA_LANG_BYTE_ARRAY); // bytes
    }

    public Object getValueForDatastoreMapping(NucleusContext nucleusCtx, int index, Object value)
    {
        Blob blob = ((Blob)value);
        switch (index) {
            case 0: return blob.getName();
            case 1: return blob.getMimeType().getBaseType();
            case 2: return blob.getBytes();
        }
        throw new IndexOutOfBoundsException();
    }

    public void setObject(ExecutionContext ec, Object preparedStmt, int[] exprIndex, Object value)
    {
        Blob blob = ((Blob)value);
        if (blob == null) {
            getDatastoreMapping(0).setObject(preparedStmt, exprIndex[0], null);
            getDatastoreMapping(1).setObject(preparedStmt, exprIndex[1], null);
            getDatastoreMapping(2).setObject(preparedStmt, exprIndex[2], null);
        } else {
            getDatastoreMapping(0).setString(preparedStmt, exprIndex[0], blob.getName());
            getDatastoreMapping(1).setString(preparedStmt, exprIndex[1], blob.getMimeType().getBaseType());
            getDatastoreMapping(2).setObject(preparedStmt, exprIndex[2], blob.getBytes());
        }
    }
    
    public Object getObject(ExecutionContext ec, Object resultSet, int[] exprIndex)
    {
        try
        {
            // Check for null entries
            if (getDatastoreMapping(0).getObject(resultSet, exprIndex[0]) == null)
            {
                return null;
            }
        }
        catch (Exception e)
        {
            // Do nothing
        }

        String name = getDatastoreMapping(0).getString(resultSet, exprIndex[0]); 
        String mimeTypeBase = getDatastoreMapping(1).getString(resultSet, exprIndex[1]); 
        byte[] bytes = (byte[]) getDatastoreMapping(2).getObject(resultSet,exprIndex[2]); 
        return new Blob(name, mimeTypeBase, bytes);
    }

}
