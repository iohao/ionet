package com.iohao.net.center;

import com.iohao.net.framework.protocol.ConnectResponseMessage;
import com.iohao.net.center.codec.ConnectResponseMessageSbe;
import com.iohao.net.center.fragment.ConnectRequestMessageOnFragment;
import com.iohao.net.framework.core.CoreConfigLoader;
import com.iohao.net.common.OnFragmentManager;
import com.iohao.net.common.SbeMessageManager;

/**
 * Registers center-server SBE encoders and fragment handlers into shared registries.
 */
public class CenterCoreConfigLoader implements CoreConfigLoader {
    @Override
    public void config() {
        configSBE();
        configOnFragment();
    }

    private void configSBE() {
        SbeMessageManager.register(ConnectResponseMessage.class, new ConnectResponseMessageSbe());
    }

    private void configOnFragment() {
        OnFragmentManager.register(new ConnectRequestMessageOnFragment());
    }
}
