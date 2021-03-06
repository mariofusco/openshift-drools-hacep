/*
 * Copyright 2019 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.hacep.consumer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.drools.core.common.InternalFactHandle;
import org.kie.api.event.rule.DefaultRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.remote.RemoteFactHandle;

public class FactHandlesManager implements Serializable {
    private BidirectionalMap<RemoteFactHandle, Integer> fhIdMap = new BidirectionalMap<>();

    private transient KieSession kieSession;

    private transient Map<RemoteFactHandle, InternalFactHandle> fhMap = new HashMap<>();

    //for serialization purpose
    public FactHandlesManager() { }

    public FactHandlesManager(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    public Set<RemoteFactHandle> getFhMapKeys() {
        return fhIdMap.keySet();
    }

    public void registerHandle(RemoteFactHandle remoteFH, FactHandle fh) {
        InternalFactHandle ifh = ( InternalFactHandle ) fh;
        fhMap.put(remoteFH, ifh);
        fhIdMap.put( remoteFH, ifh.getId() );
    }

    public FactHandlesManager initFromKieSession( KieSession kieSession ) {
        this.kieSession = kieSession;
        kieSession.addEventListener( new DefaultRuleRuntimeEventListener() {
            @Override
            public void objectDeleted( ObjectDeletedEvent objectDeletedEvent ) {
                fhMap.remove(
                    fhIdMap.removeValue( (( InternalFactHandle ) objectDeletedEvent.getFactHandle()).getId() ) );
            }
        } );
        fhMap.clear();
        return this;
    }

    public FactHandle mapRemoteFactHandle(RemoteFactHandle remoteFH) {
        return fhMap.computeIfAbsent( remoteFH, this::getFactHandleById );
    }

    private InternalFactHandle getFactHandleById(RemoteFactHandle remoteFH) {
        int id = fhIdMap.get(remoteFH);
        for (FactHandle fh : kieSession.getFactHandles()) {
            InternalFactHandle ifh = ( InternalFactHandle ) fh;
            if ( ifh.getId() == id) {
                return ifh;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return fhIdMap.keySet().toString();
    }
}
