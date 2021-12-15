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

package com.megaease.easeagent.zipkin;

import brave.handler.MutableSpan;
import brave.handler.SpanHandler;
import brave.propagation.TraceContext;

import java.util.function.Function;

public class RootSpanFinishHandler extends SpanHandler {
    private final Function rootSpanFinish;

    public RootSpanFinishHandler(Function rootSpanFinish) {
        this.rootSpanFinish = rootSpanFinish;
    }

    @Override
    public boolean end(TraceContext context, MutableSpan span, Cause cause) {
        if (context.isLocalRoot()) {
            rootSpanFinish.apply(context);
        }
        return true;
    }
}
