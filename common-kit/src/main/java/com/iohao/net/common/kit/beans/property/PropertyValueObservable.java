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

/**
 * Observable property value that notifies registered listeners on value changes.
 *
 * @param <T> the type of the property value
 * @author 渔民小镇
 * @date 2024-04-17
 */
public interface PropertyValueObservable<T> {
    /**
     * Register a change listener to be notified when the value changes.
     *
     * @param listener the listener to register
     */
    void addListener(PropertyChangeListener<? super T> listener);

    /**
     * Remove a previously registered change listener.
     *
     * @param listener the listener to remove
     */
    void removeListener(PropertyChangeListener<? super T> listener);

    /**
     * Get the current property value.
     *
     * @return the current value
     */
    T getValue();

    /**
     * Set the property value, triggering listeners if the value changed.
     *
     * @param value the new value to set
     */
    void setValue(T value);
}
