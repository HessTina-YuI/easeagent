///*
// * Copyright (c) 2017, MegaEase
// * All rights reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *     http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.megaease.easeagent.sniffer.kafka.spring;
//
//import com.megaease.easeagent.core.Classes;
//import com.megaease.easeagent.core.Definition;
//import com.megaease.easeagent.core.QualifiedBean;
//import com.megaease.easeagent.core.interceptor.AgentInterceptorChainInvoker;
//import com.megaease.easeagent.sniffer.BaseSnifferTest;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.kafka.listener.MessageListener;
//import org.springframework.kafka.support.Acknowledgment;
//
//import java.util.List;
//
//import static org.mockito.Mockito.spy;
//
//@SuppressWarnings("unchecked")
//public class KafkaMessageListenerAdviceTest extends BaseSnifferTest {
//    static List<Class<?>> classList;
//    AgentInterceptorChainInvoker chainInvoker = spy(AgentInterceptorChainInvoker.getInstance());
//
//    @Before
//    public void before() {
//        if (classList != null) {
//            return;
//        }
//        Definition.Default def = new GenKafkaMessageListenerAdvice().define(Definition.Default.EMPTY);
//        ClassLoader loader = this.getClass().getClassLoader();
//        classList = Classes.transform(
//                this.getClass().getName() + "$MyListener"
//        )
//                .with(def, new QualifiedBean("", chainInvoker),
//                        new QualifiedBean("supplier4SpringKafkaMessageListenerOnMessage", this.mockSupplier())
//                )
//                .load(loader);
//    }
//
//
//    @Test
//    public void invoke() throws Exception {
//        MyListener<String, String> myListener = (MyListener<String, String>) classList.get(0).newInstance();
//        ConsumerRecord<String, String> consumerRecord = new ConsumerRecord<>("topic", 1, 0, "key", "value");
//        myListener.onMessage(consumerRecord);
//        this.verifyInvokeTimes(this.chainInvoker, 1);
//    }
//
//    static class MyListener<K, V> implements MessageListener<K, V> {
//
//        @Override
//        public void onMessage(ConsumerRecord<K, V> data) {
//
//        }
//
//        @Override
//        public void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment) {
//
//        }
//
//        @Override
//        public void onMessage(ConsumerRecord<K, V> data, Consumer<?, ?> consumer) {
//
//        }
//
//        @Override
//        public void onMessage(ConsumerRecord<K, V> data, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
//
//        }
//    }
//}
