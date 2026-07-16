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
package io.aeron;

import io.aeron.logbuffer.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import org.agrona.*;

/**
 * Test publication that records close calls without requiring a real Aeron client.
 *
 * @author 渔民小镇
 * @date 2026-05-03
 * @since 25.4
 */
public final class RecordingPublication extends Publication {
    private static final sun.misc.Unsafe UNSAFE = lookupUnsafe();

    private AtomicInteger closeCount;
    private AtomicInteger offerCount;
    private AtomicInteger lastOfferLength;
    private Queue<Long> offerResults;
    private int maxMessageLength;

    @SuppressWarnings({"unused", "DataFlowIssue"})
    private RecordingPublication() {
        // Required only for compilation; instances are allocated without running this constructor.
        super(null, null, 0, 0, null, 0, null, 0, 0);
    }

    public static RecordingPublication create() {
        try {
            var publication = (RecordingPublication) UNSAFE.allocateInstance(RecordingPublication.class);
            publication.closeCount = new AtomicInteger();
            publication.offerCount = new AtomicInteger();
            publication.lastOfferLength = new AtomicInteger();
            publication.offerResults = new ConcurrentLinkedQueue<>();
            publication.maxMessageLength = Integer.MAX_VALUE;
            return publication;
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        }
    }

    public RecordingPublication setOfferResults(long... results) {
        Arrays.stream(results).forEach(this.offerResults::offer);
        return this;
    }

    public RecordingPublication setMaxMessageLength(int maxMessageLength) {
        this.maxMessageLength = maxMessageLength;
        return this;
    }

    public int closeCount() {
        return this.closeCount.get();
    }

    public int offerCount() {
        return this.offerCount.get();
    }

    public int lastOfferLength() {
        return this.lastOfferLength.get();
    }

    @Override
    public int maxMessageLength() {
        return this.maxMessageLength;
    }

    @Override
    public void close() {
        this.isClosed = true;
        this.closeCount.incrementAndGet();
    }

    @Override
    public long availableWindow() {
        return 0;
    }

    @Override
    public long offer(DirectBuffer buffer, int offset, int length, ReservedValueSupplier reservedValueSupplier) {
        this.recordOffer(length);
        return this.nextOfferResult();
    }

    @Override
    public long offer(
            DirectBuffer bufferOne,
            int offsetOne,
            int lengthOne,
            DirectBuffer bufferTwo,
            int offsetTwo,
            int lengthTwo,
            ReservedValueSupplier reservedValueSupplier) {
        this.recordOffer(lengthOne + lengthTwo);
        return this.nextOfferResult();
    }

    @Override
    public long offer(DirectBufferVector[] vectors, ReservedValueSupplier reservedValueSupplier) {
        this.recordOffer(0);
        return this.nextOfferResult();
    }

    @Override
    public long tryClaim(int length, BufferClaim bufferClaim) {
        return this.isClosed ? Publication.CLOSED : 1;
    }

    private void recordOffer(int length) {
        this.offerCount.incrementAndGet();
        this.lastOfferLength.set(length);
    }

    private long nextOfferResult() {
        if (this.isClosed) {
            return Publication.CLOSED;
        }

        var result = this.offerResults.poll();
        return result == null ? 1 : result;
    }

    private static sun.misc.Unsafe lookupUnsafe() {
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (sun.misc.Unsafe) field.get(null);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
