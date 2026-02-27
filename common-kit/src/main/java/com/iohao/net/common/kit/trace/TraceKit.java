/*
 * ionet
 * Copyright (C) 2021 - present  渔民小镇 （262610965@qq.com、luoyizhu@gmail.com） . All Rights Reserved.
 * # iohao.com . 渔民小镇
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.iohao.net.common.kit.trace;

import java.util.concurrent.atomic.*;
import java.util.function.*;
import lombok.*;
import lombok.experimental.*;
import org.slf4j.*;

/**
 * Distributed tracing utilities using SLF4J MDC for trace ID propagation.
 *
 * @author 渔民小镇
 * @date 2023-12-20
 */
@UtilityClass
public class TraceKit {
    @Setter
    TraceIdSupplier traceIdSupplier = new TraceIdSupplier() {
        final AtomicLong id = new AtomicLong(System.currentTimeMillis());

        @Override
        public String get() {
            return Long.toString(id.getAndIncrement());
        }
    };

    /** MDC key used for trace ID propagation. */
    public final String traceName = "ionetTraceId";

    /**
     * Generate a new trace ID using the configured {@link TraceIdSupplier}.
     *
     * @return a new trace ID string
     */
    public String newTraceId() {
        return traceIdSupplier.get();
    }

    /**
     * Get the current trace ID from the SLF4J MDC context.
     *
     * @return the current trace ID, or {@code null} if none is set
     */
    public String getTraceId() {
        return MDC.get(traceName);
    }

    /**
     * Put the given trace ID into the MDC context, returning a closeable handle
     * that removes it when closed.
     *
     * @param traceId the trace ID to set
     * @return a closeable that removes the trace ID from MDC on close
     */
    public MDC.MDCCloseable putCloseable(String traceId) {
        return MDC.putCloseable(TraceKit.traceName, traceId);
    }

    /**
     * Decorate a {@link Runnable} so that the given trace ID is set in MDC
     * during its execution.
     *
     * @param traceId the trace ID to propagate
     * @param command the runnable to decorate
     * @return a new runnable that sets and clears the trace ID around execution
     */
    public Runnable decorator(String traceId, Runnable command) {
        return () -> {
            try (var _ = TraceKit.putCloseable(traceId)) {
                command.run();
            }
        };
    }

    /**
     * Decorate a {@link Consumer} so that the given trace ID is set in MDC
     * during its execution.
     *
     * @param traceId the trace ID to propagate
     * @param action  the consumer to decorate
     * @param <T>     the type of the consumer input
     * @return a new consumer that sets and clears the trace ID around execution
     */
    public <T> Consumer<T> decorator(String traceId, Consumer<T> action) {
        return t -> {
            try (var _ = TraceKit.putCloseable(traceId)) {
                action.accept(t);
            }
        };
    }
}
