/*
 * Copyright (c) 2017, MegaEase
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.megaease.easeagent.metrics.jdbc.interceptor;

import com.codahale.metrics.MetricRegistry;
import com.megaease.easeagent.common.config.SwitchUtil;
import com.megaease.easeagent.common.jdbc.JdbcUtils;
import com.megaease.easeagent.config.Config;
import com.megaease.easeagent.core.interceptor.AgentInterceptor;
import com.megaease.easeagent.core.interceptor.AgentInterceptorChain;
import com.megaease.easeagent.plugin.MethodInfo;
import com.megaease.easeagent.metrics.converter.Converter;
import com.megaease.easeagent.metrics.jdbc.AbstractJdbcMetric;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Supplier;

public class JdbcDataSourceMetricInterceptor extends AbstractJdbcMetric implements AgentInterceptor {

    public static final String ENABLE_KEY = "observability.metrics.jdbcConnection.enabled";

    private final Config config;

    public JdbcDataSourceMetricInterceptor(MetricRegistry metricRegistry, Config config) {
        super(metricRegistry);
        this.config = config;
    }

    @Override
    public Converter newConverter(Supplier<Map<String, Object>> attributes) {
        return new JDBCConverter("application", "jdbc-connection", "url", attributes);
    }

    @Override
    public Object after(MethodInfo methodInfo, Map<Object, Object> context, AgentInterceptorChain chain) {
        if (!SwitchUtil.enableMetric(config, ENABLE_KEY)) {
            return chain.doAfter(methodInfo, context);
        }
        Connection connection = (Connection) methodInfo.getRetValue();
        try {
            String key;
            boolean success = true;
            if (methodInfo.getRetValue() == null) {
                key = ERR_CON_METRIC_KEY;
                success = false;
            } else {
                key = getMetricKey(connection, methodInfo.getThrowable());
            }
            this.collectMetric(key, success, context);
        } catch (SQLException ignored) {
        }
        return chain.doAfter(methodInfo, context);
    }

    private static String getMetricKey(Connection con, Throwable throwable) throws SQLException {
        if (throwable != null) {
            return ERR_CON_METRIC_KEY;
        }
        return JdbcUtils.getUrl(con);
    }


}
