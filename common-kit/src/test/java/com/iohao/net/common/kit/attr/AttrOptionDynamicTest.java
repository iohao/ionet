package com.iohao.net.common.kit.attr;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author 渔民小镇
 * @date 2024-06-05
 * @since 25.1
 */
@Slf4j
class AttrOptionDynamicTest {

    final MyAttrOptions myAttrOptions = new MyAttrOptions();

    AttrOption<AttrCat> catAttrOption = AttrOption.valueOf("AttrCat");

    @Test
    public void ifNull() {
        Assertions.assertNull(myAttrOptions.option(catAttrOption));

        // 如果 catAttrOption 属性为 null，创建 AttrCat 对象，并赋值到属性中
        myAttrOptions.ifNull(catAttrOption, AttrCat::new);
        Assertions.assertNotNull(myAttrOptions.option(catAttrOption));

        myAttrOptions.option(catAttrOption, null);
        Assertions.assertNull(myAttrOptions.option(catAttrOption));

        AttrCat attrCat = new AttrCat();
        attrCat.name = "a";
        myAttrOptions.option(catAttrOption, attrCat);
        myAttrOptions.ifNull(catAttrOption, AttrCat::new);
        Assertions.assertEquals(myAttrOptions.option(catAttrOption).name, attrCat.name);
    }

    private static class AttrCat {
        String name;
    }

    @Getter
    private static class MyAttrOptions implements AttrOptionDynamic {
        final AttrOptions options = new AttrOptions();
    }
}