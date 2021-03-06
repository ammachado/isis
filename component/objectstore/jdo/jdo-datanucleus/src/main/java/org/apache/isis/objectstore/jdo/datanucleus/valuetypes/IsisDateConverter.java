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

import org.datanucleus.store.types.converters.TypeConverter;

import org.apache.isis.applib.value.Date;

public class IsisDateConverter implements TypeConverter<Date, Long>{

    private static final long serialVersionUID = 1L;

    public IsisDateConverter() {
        
    }
    
//    @Override
//    public Long toLong(Date object) {
//        if(object == null) {
//            return null;
//        }
//
//        Date d = (Date)object;
//        return d.getMillisSinceEpoch();
//    }
//
//    @Override
//    public Date toObject(Long value) {
//        if(value == null) {
//            return null;
//        }
//        return new Date(value);
//    }

    @Override
    public Long toDatastoreType(Date memberValue) {
        if(memberValue == null) {
            return null;
        }

        Date d = (Date)memberValue;
        return d.getMillisSinceEpoch();
    }

    @Override
    public Date toMemberType(Long datastoreValue) {
        if(datastoreValue == null) {
            return null;
        }
        return new Date(datastoreValue);
    }

}
