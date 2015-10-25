/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mattring.flink.nats;

import static com.google.common.base.Preconditions.checkArgument;
import com.google.common.base.Strings;
import java.io.Serializable;
import java.util.Objects;
import java.util.Properties;

/**
 *
 * @author Matthew Ring
 */
public class NatsConfig implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String brokerUris;
    private final String topic;
    private final int maxConnectRetries;
    private final int reconnectWaitMillis;

    public NatsConfig(String brokerUris, String topic, int maxConnectRetries, int reconnectWaitMillis) {
        checkArgument(
                !Strings.isNullOrEmpty(brokerUris),
                "brokerUris must be populated");
        checkArgument(
                maxConnectRetries >= -1,
                "maxConnectRetries must be zero or larger (num retries), or -1 (infinite retries)");
        checkArgument(
                reconnectWaitMillis >= 0,
                "reconnectWaitMillis must be zero or positive");
        this.brokerUris = brokerUris;
        this.topic = topic;
        this.maxConnectRetries = maxConnectRetries;
        this.reconnectWaitMillis = reconnectWaitMillis;
    }

    public String getBrokerUris() {
        return brokerUris;
    }

    public String getTopic() {
        return topic;
    }

    public int getMaxConnectRetries() {
        return maxConnectRetries;
    }

    public int getReconnectWaitMillis() {
        return reconnectWaitMillis;
    }

    public Properties getAsJava_NatsProperties() {
        final Properties natsProps = new Properties();
        natsProps.put("uri", this.getBrokerUris());
        natsProps.put("max_reconnect_attempts", this.getMaxConnectRetries());
        natsProps.put("reconnect_time_wait", this.getReconnectWaitMillis());
        return natsProps;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.brokerUris);
        hash = 53 * hash + Objects.hashCode(this.topic);
        hash = 53 * hash + this.maxConnectRetries;
        hash = 53 * hash + this.reconnectWaitMillis;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NatsConfig other = (NatsConfig) obj;
        if (!Objects.equals(this.brokerUris, other.brokerUris)) {
            return false;
        }
        if (!Objects.equals(this.topic, other.topic)) {
            return false;
        }
        if (this.maxConnectRetries != other.maxConnectRetries) {
            return false;
        }
        if (this.reconnectWaitMillis != other.reconnectWaitMillis) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NatsConfig{" + "brokerUris=" + brokerUris + ", topic=" + topic + ", maxConnectRetries=" + maxConnectRetries + ", reconnectWaitMillis=" + reconnectWaitMillis + '}';
    }

}
