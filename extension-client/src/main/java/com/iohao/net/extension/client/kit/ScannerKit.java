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
package com.iohao.net.extension.client.kit;

import com.iohao.net.common.kit.exception.ThrowKit;
import lombok.experimental.UtilityClass;

import java.util.Scanner;

/**
 * Console input helper utilities for simulated clients.
 *
 * @author 渔民小镇
 * @date 2023-07-04
 */
@UtilityClass
public class ScannerKit {

    final Scanner scanner = new Scanner(System.in);

    /**
     * Skips the runnable when console input is disabled.
     *
     * @param runnable runnable
     */
    public void log(Runnable runnable) {
        if (ClientUserConfigs.closeScanner) {
            return;
        }

        runnable.run();
    }

    /**
     * Reads a {@link String} from console input.
     * <pre>
     *     If console input is disabled, returns the default value.
     * </pre>
     *
     * @param defaultValue default value when console input is disabled
     * @return String value
     */
    public String nextLine(String defaultValue) {
        if (ClientUserConfigs.closeScanner) {
            return defaultValue;
        }

        return nextLine();
    }

    /**
     * Reads a {@link String} from console input.
     *
     * @return String value
     */
    public String nextLine() {
        if (ClientUserConfigs.closeScanner) {
            // Console input is disabled.
            ThrowKit.ofRuntimeException("No support for console input");
        }

        return scanner.nextLine();
    }

    /**
     * Reads a {@code long} from console input.
     * <pre>
     *     If console input is disabled, returns the default value.
     * </pre>
     *
     * @param defaultValue default value when console input is disabled
     * @return long value
     */
    public long nextLong(long defaultValue) {
        if (ClientUserConfigs.closeScanner) {
            return defaultValue;
        }

        return nextLong();
    }

    /**
     * Reads a {@code long} from console input.
     *
     * @return long value
     */
    public long nextLong() {
        if (ClientUserConfigs.closeScanner) {
            // Console input is disabled.
            ThrowKit.ofRuntimeException("No support for console input");
        }

        String s = ScannerKit.scanner.nextLine();
        return Long.parseLong(s);
    }

    /**
     * Reads an {@code int} from console input.
     * <pre>
     *     If console input is disabled, returns the default value.
     * </pre>
     *
     * @param defaultValue default value when console input is disabled
     * @return int value
     */
    public int nextInt(int defaultValue) {
        if (ClientUserConfigs.closeScanner) {
            return defaultValue;
        }

        return nextInt();
    }

    /**
     * Reads an {@code int} from console input.
     *
     * @return int value
     */
    public int nextInt() {
        if (ClientUserConfigs.closeScanner) {
            // Console input is disabled.
            ThrowKit.ofRuntimeException("No support for console input");
        }

        String s = ScannerKit.scanner.nextLine();
        return Integer.parseInt(s);
    }
}
