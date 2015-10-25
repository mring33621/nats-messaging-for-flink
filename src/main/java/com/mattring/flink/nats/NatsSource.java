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


import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.nats.Connection;
import org.nats.MsgHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A source function that reads strings from a NATS Topic. The incoming strings
 * will be split at the optional delimiter,
 * 
 * @author Matthew Ring
 */
public class NatsSource implements SourceFunction<String> {

    private static final long serialVersionUID = 2L;

    private static final Logger LOG = LoggerFactory.getLogger(NatsSource.class);

    private final NatsConfig natsConfig;
    private final String delimiter;

    private volatile boolean isRunning;

    public NatsSource(NatsConfig natsConfig, String optionalDelimiter) {        
        this.natsConfig = natsConfig;
        this.delimiter = Strings.emptyToNull(optionalDelimiter);
        this.isRunning = false;
    }

    @Override
    public void run(SourceFunction.SourceContext<String> ctx) throws Exception {
        
        isRunning = true;

        if (isRunning) {
            
            LOG.info("Running NatsSource with " + natsConfig);

            final Properties natsProps = natsConfig.getAsJava_NatsProperties();

            try (Connection natsConn = Connection.connect(natsProps)) {

                final LinkedBlockingQueue<String> inbox = new LinkedBlockingQueue<>();
                final MsgHandler msgHandler = new MsgHandler() {
                    @Override
                    public void execute(String msg, String reply, String subject) {
                        final boolean enqueued = inbox.offer(msg);
                        // TODO: if ! enqueued then do what?
                    }
                };
                final int natsSubId = natsConn.subscribe(natsConfig.getTopic(), msgHandler);

                while (isRunning) {
                    final String msg = inbox.poll();
                    if (msg != null) {
                        if (delimiter != null) {
                            final Iterable<String> msgPartIterable = 
                                    Splitter.on(delimiter)
                                            .trimResults()
                                            .omitEmptyStrings()
                                            .split(msg);
                            for (String msgPart : msgPartIterable) {
                                ctx.collect(msg);
                            }
                        } else {
                            ctx.collect(msg);
                        }
                    }
                }
                
                natsConn.unsubscribe(natsSubId);
            }
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
