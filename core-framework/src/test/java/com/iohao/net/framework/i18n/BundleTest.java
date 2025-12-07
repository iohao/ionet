package com.iohao.net.framework.i18n;

import com.iohao.net.framework.toy.IonetBanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author 渔民小镇
 * @date 2024-10-02
 * @since 21.18
 */
public class BundleTest {

    @Test
    public void getMessage() {
        var b1 = ResourceBundle.getBundle(Bundle.baseName, Locale.getDefault());
        var b2 = ResourceBundle.getBundle(Bundle.baseName, Locale.getDefault());

        Assertions.assertEquals(b1, b2);

        Locale.setDefault(Locale.US);
        extracted();

        Locale.setDefault(Locale.CHINA);
        extracted();
    }

    private static void extracted() {
        System.out.println("------------------");

        Bundle.bundle = null;

        System.out.println(Locale.getDefault().toString());

        String value = Bundle.getMessage(MessageKey.printActionKitPrintClose);
        IonetBanner.printlnMsg(value + " BarSkeletonBuilder.setting.printRunners");
        Assertions.assertNotNull(value);
    }
}