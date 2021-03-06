/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
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
package org.kie.remote.impl.producer;

import java.util.Properties;

import org.kie.remote.command.RemoteCommand;
import org.kie.remote.impl.ClientUtils;

public class Sender {

    private EventProducer producer;
    private Properties configuration;

    public Sender( Properties configuration ) {
        producer = new EventProducer();
        if ( configuration != null && !configuration.isEmpty() ) {
            this.configuration = configuration;
        }
    }

    public void start() {
        producer.start( configuration != null ? configuration : ClientUtils.getConfiguration( ClientUtils.PRODUCER_CONF ) );
    }

    public void stop() {
        producer.stop();
    }

    public long sendCommand(RemoteCommand command, String topicName ) {
        return producer.produceSync( topicName, command.getId(), command );
    }

    public void insertFireAndForget( RemoteCommand command, String topicName ) {
        producer.produceFireAndForget( topicName, command.getId(), command );
    }
}
