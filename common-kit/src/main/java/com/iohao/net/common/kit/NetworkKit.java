package com.iohao.net.common.kit;

import java.net.*;
import java.util.*;
import lombok.experimental.*;

/**
 * Network utilities for local IP address detection.
 *
 * @author 渔民小镇
 * @date 2022-05-14
 */
@UtilityClass
public class NetworkKit {
    /** IP black list. 10.0.2.15 is the default IP for VirtualBox VMs. */
    final List<String> IP_BLACK_LIST = List.of("10.0.2.15");
    /** The detected local (non-loopback, non-blacklisted) IPv4 address. */
    public final String LOCAL_IP = getLocalIP();

    private String getLocalIP() {
        String ip = null;
        try {
            var e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = e.nextElement();
                Enumeration<?> ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    var inetAddress = (InetAddress) ee.nextElement();
                    String hostAddress = inetAddress.getHostAddress();
                    if (hostAddress.contains(".") && !IP_BLACK_LIST.contains(hostAddress) && !inetAddress.isLoopbackAddress()) {
                        ip = hostAddress;
                        break;
                    }
                }
            }

            if (ip == null) {
                ip = InetAddress.getLocalHost().getHostAddress();
            }
        } catch (Exception ignore) {
            return "127.0.0.1";
        }

        return ip;
    }
}
