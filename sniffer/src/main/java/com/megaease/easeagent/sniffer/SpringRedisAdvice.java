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

package com.megaease.easeagent.sniffer;

import com.megaease.easeagent.core.Definition;
import com.megaease.easeagent.core.Transformation;
import com.megaease.easeagent.plugin.field.AgentDynamicFieldAccessor;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

//@Injection.Provider(Provider.class)
public abstract class SpringRedisAdvice implements Transformation {
    private static final String RedisStringCommands = "org.springframework.data.redis.connection.RedisStringCommands";
    private static final String RedisHashCommands = "org.springframework.data.redis.connection.RedisHashCommands";
    private static final String RedisGeoCommands = "org.springframework.data.redis.connection.RedisGeoCommands";
    private static final String RedisHyperLogLogCommands = "org.springframework.data.redis.connection.RedisHyperLogLogCommands";
    private static final String RedisKeyCommands = "org.springframework.data.redis.connection.RedisKeyCommands";
    private static final String RedisListCommands = "org.springframework.data.redis.connection.RedisListCommands";
    private static final String RedisSetCommands = "org.springframework.data.redis.connection.RedisSetCommands";
    private static final String RedisScriptingCommands = "org.springframework.data.redis.connection.RedisScriptingCommands";
    private static final String RedisServerCommands = "org.springframework.data.redis.connection.RedisServerCommands";
    private static final String RedisStreamCommands = "org.springframework.data.redis.connection.RedisStreamCommands";
    private static final String RedisZSetCommands = "org.springframework.data.redis.connection.RedisZSetCommands";
    private static final String[] clsNames = new String[]{
            RedisStringCommands, RedisHashCommands,
            RedisGeoCommands, RedisHyperLogLogCommands,
            RedisKeyCommands, RedisListCommands,
            RedisSetCommands, RedisScriptingCommands,
            RedisServerCommands, RedisStreamCommands,
            RedisZSetCommands
    };

    @Override
    public <T extends Definition> T define(Definition<T> def) {
        Definition.Fork<T> fork = (Definition.Fork<T>) def;
        for (String clsName : clsNames) {
            fork = fork.type(hasSuperType(named(clsName)))
                    .transform(
                            doCommand(
                                    any().and(isOverriddenFrom(named(clsName)))
                                    , AgentDynamicFieldAccessor.DYNAMIC_FIELD_NAME
                            )
                    );
        }
        return fork.end();

//        return def
//                .type(hasSuperType(named(RedisStringCommands))
//                )
//                .transform(doCommand(ElementMatchers.any().and(isOverriddenFrom(named(RedisStringCommands)))
//                ))
//                .type(hasSuperType(named(RedisHashCommands))
//                )
//                .transform(doCommand(ElementMatchers.any().and(isOverriddenFrom(named(RedisHashCommands)))
//                ))
//                .end();
    }

//    @AdviceTo(DoCommand.class)
    abstract Definition.Transformer doCommand(ElementMatcher<? super MethodDescription> matcher, String fieldName);

//    static class DoCommand extends AbstractAdvice {
//
//        @Injection.Autowire
//        DoCommand(@Injection.Qualifier("agentInterceptorChainBuilder4SpringRedis") AgentInterceptorChain.Builder builder,
//                  AgentInterceptorChainInvoker agentInterceptorChainInvoker) {
//            super(builder, agentInterceptorChainInvoker);
//        }
//
//        @Advice.OnMethodEnter
//        public ForwardLock.Release<Map<Object, Object>> enter(
//                @Advice.Origin Object invoker,
//                @Advice.Origin("#m") String method,
//                @Advice.AllArguments Object[] args
//        ) {
//            return doEnter(invoker, method, args);
//        }
//
//        @Advice.OnMethodExit(onThrowable = Throwable.class)
//        public void exit(
//                @Advice.Enter ForwardLock.Release<Map<Object, Object>> release,
//                @Advice.Origin Object invoker,
//                @Advice.Origin("#m") String method,
//                @Advice.AllArguments Object[] args,
//                @Advice.Thrown Exception exception
//        ) {
//            doExitNoRetValue(release, invoker, method, args, exception);
//        }
//    }
}
