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

package com.megaease.easeagent.config;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class ConfigUtilsTest {
    @Test
    public void test_bindProp() throws Exception {
        Configs configs = new Configs(Collections.singletonMap("hello", "world"));
        String[] rst = new String[]{null};
        ConfigUtils.bindProp("hello", configs, Config::getString, v -> rst[0] = v);
        Assert.assertEquals("world", rst[0]);
        configs.updateConfigs(Collections.singletonMap("hello", "test"));
        Assert.assertEquals("test", rst[0]);
        configs.updateConfigs(Collections.singletonMap("hello", "one"));
        Assert.assertEquals("one", rst[0]);
    }

    @Test
    public void test_json2KVMap() throws Exception {
        Map<String, String> map = ConfigUtils.json2KVMap("{\n" +
                "  \"output\": {\n" +
                "    \"servers\": \"127.0.0.1\",\n" +
                "    \"timeout\": 1000,\n" +
                "    \"enabled\": true,\n" +
                "    \"arr\": [\"x\", { \"test\": 0 }]\n" +
                "  },\n" +
                "  \"hello\":null,\n" +
                "  \"metrics\": {\n" +
                "    \"obj\": {\n" +
                "      \"a\": 1,\n" +
                "      \"b\": \"2\",\n" +
                "      \"c\": false\n" +
                "    },\n" +
                "    \"request\": {\n" +
                "      \"topic\": \"hello\",\n" +
                "      \"enabled\": false\n" +
                "    }\n" +
                "  }\n" +
                "}");
        Assert.assertEquals("127.0.0.1", map.get("output.servers"));
        Assert.assertEquals("1000", map.get("output.timeout"));
        Assert.assertEquals("true", map.get("output.enabled"));
        Assert.assertEquals("x", map.get("output.arr.0"));
        Assert.assertEquals("0", map.get("output.arr.1.test"));
        Assert.assertEquals("", map.get("hello"));
        Assert.assertEquals("1", map.get("metrics.obj.a"));
        Assert.assertEquals("2", map.get("metrics.obj.b"));
        Assert.assertEquals("false", map.get("metrics.obj.c"));
        Assert.assertEquals("hello", map.get("metrics.request.topic"));
        Assert.assertEquals("false", map.get("metrics.request.enabled"));
    }

    @Test
    public void test_json2KVMap_2() throws IOException {
        Map<String, String> map = ConfigUtils.json2KVMap("{\"serviceHeaders\":{\"mesh-app-backend\":[\"X-canary\"]}}");
        Assert.assertEquals("X-canary", map.get("serviceHeaders.mesh-app-backend.0"));
    }

    @Test
    public void isSelf() {
        Assert.assertTrue(ConfigUtils.isSelf("self"));
        Assert.assertFalse(ConfigUtils.isSelf("selddf"));
    }

    @Test
    public void isPluginConfig() {
        Assert.assertTrue(ConfigUtils.isPluginConfig("plugin."));
        Assert.assertFalse(ConfigUtils.isPluginConfig("plugin"));
        Assert.assertFalse(ConfigUtils.isPluginConfig("plugins."));
        Assert.assertTrue(ConfigUtils.isPluginConfig("plugin.observability.kafka.", "observability", "kafka"));
        Assert.assertFalse(ConfigUtils.isPluginConfig("plugin.observability.kafka.", "observabilitys", "kafka"));
        Assert.assertFalse(ConfigUtils.isPluginConfig("plugin.observability.kafka.", "observability", "kafkas"));
        Assert.assertFalse(ConfigUtils.isPluginConfig("plugin.observability.kafka.", "observabilitys", "kafkas"));
    }

    @Test
    public void pluginProperty() {
        PluginProperty pluginProperty = ConfigUtils.pluginProperty("plugin.observability.kafka.self.enabled");
        Assert.assertEquals(pluginProperty.getDomain(), "observability");
        Assert.assertEquals(pluginProperty.getNamespace(), "kafka");
        Assert.assertEquals(pluginProperty.getId(), "self");
        Assert.assertEquals(pluginProperty.getProperty(), "enabled");
        pluginProperty = ConfigUtils.pluginProperty("plugin.observability.kafka.self.tcp.enabled");
        Assert.assertEquals(pluginProperty.getDomain(), "observability");
        Assert.assertEquals(pluginProperty.getNamespace(), "kafka");
        Assert.assertEquals(pluginProperty.getId(), "self");
        Assert.assertEquals(pluginProperty.getProperty(), "tcp.enabled");
        try{
            ConfigUtils.pluginProperty("plugin.observability.kafka.self.tcp");
            throw new RuntimeException("must be error");
        }catch (Exception e ){
            Assert.assertNotNull(e);
        }


    }
}
