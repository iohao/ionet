package com.iohao.net.server;

import com.iohao.net.framework.communication.eventbus.EventBusMessage;
import com.iohao.net.framework.protocol.ServerOfflineMessage;
import com.iohao.net.framework.protocol.ServerRequestMessage;
import com.iohao.net.framework.core.CoreConfigLoader;
import com.iohao.net.common.OnFragmentManager;
import com.iohao.net.common.SbeMessageManager;
import com.iohao.net.server.codec.EventBusMessageSbe;
import com.iohao.net.server.codec.ServerOfflineMessageSbe;
import com.iohao.net.server.codec.ConnectRequestMessageSbe;
import com.iohao.net.server.fragment.EventBusMessageOnFragment;
import com.iohao.net.server.fragment.ServerOfflineMessageOnFragment;
import com.iohao.net.server.fragment.ConnectResponseMessageOnFragment;
import lombok.extern.slf4j.Slf4j;

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
