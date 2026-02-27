package com.iohao.net.center;

import com.iohao.net.center.codec.*;
import com.iohao.net.center.fragment.*;
import com.iohao.net.common.*;
import com.iohao.net.framework.core.*;
import com.iohao.net.framework.protocol.*;

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
