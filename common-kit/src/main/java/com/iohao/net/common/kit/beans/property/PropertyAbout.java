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
package com.iohao.net.common.kit.beans.property;

import java.util.*;
import java.util.concurrent.*;
import lombok.*;
import lombok.experimental.*;

/**
 * Internal helper that tracks property value changes and dispatches events to a single listener.
 *
 * @param <T> the type of the observed property value
 * @author 渔民小镇
 * @date 2024-04-17
 */
abstract class ChangeHelper<T> {
    final PropertyValueObservable<T> observable;

    protected abstract void fireValueChangedEvent();

    ChangeHelper(PropertyValueObservable<T> observable) {
        this.observable = observable;
    }

    static <T> ChangeHelper<T> create(PropertyValueObservable<T> observable, PropertyChangeListener<? super T> listener) {
        return new PropertySingleChange<>(observable, observable.getValue(), listener);
    }

    static <T> ChangeHelper<T> create(PropertyChangeListener<? super T> listener) {
        return new PropertySingleChange<>(null, null, listener);
    }

    private static class PropertySingleChange<T> extends ChangeHelper<T> {
        final PropertyChangeListener<? super T> listener;
        T currentValue;

        PropertySingleChange(PropertyValueObservable<T> observable, T currentValue, PropertyChangeListener<? super T> listener) {
            super(observable);
            this.currentValue = currentValue;
            this.listener = listener;
        }

        @Override
        protected void fireValueChangedEvent() {
            final T oldValue = currentValue;
            currentValue = observable.getValue();
            final boolean changed = !Objects.equals(currentValue, oldValue);
            if (changed) {
                try {
                    listener.changed(observable, oldValue, currentValue);
                } catch (Exception e) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                }
            }
        }

        @Override
        public final boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof PropertySingleChange<?> change)) {
                return false;
            }

            return Objects.equals(listener, change.listener);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(listener);
        }
    }
}

/**
 * Internal list of {@link ChangeHelper} instances that manages listener registration and event dispatching.
 *
 * @param <T> the type of the observed property value
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
final class ChangeHelperList<T> {
    List<ChangeHelper<? super T>> list;

    /**
     * Add a pre-built change helper to the internal list.
     *
     * @param helper the change helper to add
     */
    void addListener(ChangeHelper<? super T> helper) {
        if (this.list == null) {
            this.list = new CopyOnWriteArrayList<>();
        }

        this.list.add(helper);
    }

    /**
     * Remove the change helper associated with the given listener.
     *
     * @param listener the listener whose helper should be removed
     */
    void removeListener(PropertyChangeListener<? super T> listener) {
        if (this.list == null || listener == null) {
            return;
        }

        var helper = ChangeHelper.create(listener);
        this.list.remove(helper);
    }

    /**
     * Create and add a change helper for the given observable and listener.
     *
     * @param observable the property being observed
     * @param listener   the listener to notify on value changes
     */
    void addListener(PropertyValueObservable<T> observable, PropertyChangeListener<? super T> listener) {

        if (observable == null || listener == null) {
            throw new NullPointerException();
        }

        var helper = ChangeHelper.create(observable, listener);
        this.addListener(helper);
    }

    /**
     * Fire value change events to all registered listeners.
     */
    void fireValueChangedEvent() {
        if (this.list == null) {
            return;
        }

        this.list.forEach(ChangeHelper::fireValueChangedEvent);
    }
}
