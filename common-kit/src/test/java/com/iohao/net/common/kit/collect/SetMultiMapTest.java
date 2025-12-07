package com.iohao.net.common.kit.collect;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 *
 * @author 渔民小镇
 * @date 2023-12-07
 * @since 25.1
 */
@Slf4j
class SetMultiMapTest {
    SetMultiMap<Integer, String> map = SetMultiMap.of();

    @Test
    @SuppressWarnings("all")
    public void test() {
        Assertions.assertTrue(map.isEmpty());

        map.put(1, "a");
        map.put(1, "a");
        map.put(1, "b");

        Assertions.assertEquals(1, map.size());
        Assertions.assertEquals(2, map.sizeValue());

        Set<String> set2 = map.get(2);
        Assertions.assertNull(set2);

        set2 = map.of(2);
        Assertions.assertNotNull(set2);
        Assertions.assertEquals(2, map.size());

        set2.add("2 - a");
        set2.add("2 - a");

        Assertions.assertEquals(3, map.sizeValue());
        Assertions.assertTrue(map.containsValue("a"));
        Assertions.assertTrue(map.containsValue("b"));

        var collection = map.clearAll(1);
        Assertions.assertTrue(collection.isEmpty());
        Assertions.assertEquals(2, map.size());

        Set<Integer> keySet = this.map.keySet();
        Assertions.assertNotNull(keySet);
        Assertions.assertEquals(2, keySet.size());
        Assertions.assertTrue(keySet.contains(1));
        Assertions.assertTrue(keySet.contains(2));
        Assertions.assertFalse(keySet.contains(3));

        this.map.clear();
        Assertions.assertTrue(this.map.isEmpty());
    }
}