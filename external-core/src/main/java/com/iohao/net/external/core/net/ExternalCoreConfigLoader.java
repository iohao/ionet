package com.iohao.net.external.core.net;

import com.iohao.net.framework.protocol.EmptyExternalResponseMessage;
import com.iohao.net.framework.protocol.ExternalResponseMessage;
import com.iohao.net.external.core.message.ExternalMessage;
import com.iohao.net.external.core.net.codec.EmptyExternalResponseMessageSbe;
import com.iohao.net.external.core.net.codec.ExternalResponseMessageSbe;
import com.iohao.net.external.core.net.codec.CommunicationMessageSbe;
import com.iohao.net.framework.core.CoreConfigLoader;
import com.iohao.net.common.OnFragmentManager;
import com.iohao.net.common.SbeMessageManager;
import com.iohao.net.external.core.net.external.*;
import com.iohao.net.external.core.net.fragment.*;

public class ExternalCoreConfigLoader implements CoreConfigLoader {
    @Override
    public void config() {
        configSBE();
        configOnFragment();
        configOnExternal();
    }

    void configSBE() {
        SbeMessageManager.register(ExternalMessage.class, new CommunicationMessageSbe());
        SbeMessageManager.register(ExternalResponseMessage.class, new ExternalResponseMessageSbe());
        SbeMessageManager.register(EmptyExternalResponseMessage.class, new EmptyExternalResponseMessageSbe());
    }

    void configOnFragment() {
        OnFragmentManager.register(new BroadcastMulticastMessageOnFragment());
        OnFragmentManager.register(new BroadcastUserMessageOnFragment());
        OnFragmentManager.register(new BroadcastUserListMessageOnFragment());
        OnFragmentManager.register(new UserResponseMessageOnFragment());
        OnFragmentManager.register(new ExternalRequestMessageOnFragment());
    }

    void configOnExternal() {
        OnExternalManager.register(new SettingUserIdOnExternal());
        OnExternalManager.register(new AttachmentUpdateOnExternal());
        OnExternalManager.register(new ForcedOfflineOnExternal());
        OnExternalManager.register(new ExistUserOnExternal());
    }
}
