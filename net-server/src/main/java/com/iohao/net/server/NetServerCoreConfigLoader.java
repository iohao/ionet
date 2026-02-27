package com.iohao.net.server;

import com.iohao.net.common.*;
import com.iohao.net.framework.communication.eventbus.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.protocol.*;
import com.iohao.net.server.codec.*;
import com.iohao.net.server.fragment.*;
import lombok.extern.slf4j.*;

/**
 * Registers net-server specific SBE encoders and fragment handlers into shared registries.
 */
@Slf4j
public class NetServerCoreConfigLoader implements CoreConfigLoader {
    @Override
    public void config() {
        configSBE();
        configOnFragment();
    }

    private void configSBE() {
        SbeMessageManager.register(ServerRequestMessage.class, new ConnectRequestMessageSbe());
        SbeMessageManager.register(EventBusMessage.class, new EventBusMessageSbe());
        SbeMessageManager.register(ServerOfflineMessage.class, new ServerOfflineMessageSbe());
    }

    private void configOnFragment() {
        OnFragmentManager.register(new ConnectResponseMessageOnFragment());
        OnFragmentManager.register(new EventBusMessageOnFragment());
        OnFragmentManager.register(new ServerOfflineMessageOnFragment());
    }
}
