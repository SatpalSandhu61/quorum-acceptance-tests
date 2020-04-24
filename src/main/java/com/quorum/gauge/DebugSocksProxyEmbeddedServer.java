/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.quorum.gauge;

import com.quorum.gauge.services.SocksProxyEmbeddedServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
@EnableConfigurationProperties
@Profile("DebugSocksProxyEmbeddedServer")
public class DebugSocksProxyEmbeddedServer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DebugSocksProxyEmbeddedServer.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder(DebugSocksProxyEmbeddedServer.class)
                .web(WebApplicationType.NONE)
                .lazyInitialization(true)
                .profiles("DebugSocksProxyEmbeddedServer")
                .run(args)
                .close();
    }

    @Autowired
    SocksProxyEmbeddedServer server;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Server started: {}", server.isStarted());
        Thread.sleep(Integer.MAX_VALUE);
    }
}