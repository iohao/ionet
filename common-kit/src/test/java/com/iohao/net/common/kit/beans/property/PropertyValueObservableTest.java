package com.iohao.net.common.kit.beans.property;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author 渔民小镇
 * @date 2024-04-17
 * @since 25.1
 */
@Slf4j
class PropertyValueObservableTest {
    AtomicInteger counter = new AtomicInteger(0);
    OnePropertyChangeListener listener = new OnePropertyChangeListener();

    @Test
    public void testInt() {
        counter.set(0);

        var property = new IntegerProperty();

        int value = property.get();
        property.increment();
        Assertions.assertEquals(property.get(), value + 1);
        property.decrement();
        Assertions.assertEquals(property.get(), value);

        property.addListener(listener);
        property.addListener((observable, oldValue, newValue) -> {
            counter.incrementAndGet();
            log.info("2 - int - oldValue:{}, newValue:{}", oldValue, newValue);
        });

        property.set(22);
        property.increment();
        Assertions.assertEquals(4, counter.get());

        System.out.println();
        property.removeListener(listener);
        property.decrement();
        Assertions.assertEquals(5, counter.get());
    }

    @Test
    public void testLong() {
        counter.set(0);

        var property = new LongProperty();

        long value = property.get();
        property.increment();
        Assertions.assertEquals(property.get(), value + 1);
        property.decrement();
        Assertions.assertEquals(property.get(), value);

        property.addListener(listener);
        property.addListener((observable, oldValue, newValue) -> {
            counter.incrementAndGet();
            log.info("2 - long - oldValue:{}, newValue:{}", oldValue, newValue);
        });

        property.set(22);
        property.increment();
        Assertions.assertEquals(4, counter.get());

        System.out.println();
        property.removeListener(listener);
        property.decrement();
        Assertions.assertEquals(5, counter.get());
    }

    @Test
    public void testString() {
        counter.set(0);

        var property = new StringProperty();

        property.addListener((observable, oldValue, newValue) -> {
            counter.incrementAndGet();
            log.info("String - oldValue:{}, newValue:{}, observable:{}", oldValue, newValue, observable);
        });

        property.set("aaa");
        property.set("bbb");
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    public void testBool() {
        counter.set(0);

        var property = new BooleanProperty();

        property.addListener((observable, oldValue, newValue) -> {
            counter.incrementAndGet();
            log.info("Boolean - oldValue:{}, newValue:{}, observable:{}", oldValue, newValue, observable);
        });

        property.set(true);
        property.set(false);
        Assertions.assertEquals(2, counter.get());
    }

    @Test
    public void testObject() {
        counter.set(0);

        YourUser user = new YourUser();
        user.age = 100;

        var property = new ObjectProperty<>(user);

        property.addListener((observable, oldValue, newValue) -> {
            counter.incrementAndGet();
            log.info("object - oldValue:{}, newValue:{}, observable:{}", oldValue, newValue, observable);
        });

        property.set(user);

        YourUser user2 = new YourUser();
        user2.age = 101;
        property.set(user2);

        Assertions.assertEquals(1, counter.get());
    }

    @ToString
    static class YourUser {
        int age;
    }

    @Test
    public void remove1() {
        IntegerProperty property = new IntegerProperty(10);

        property.addListener(new PropertyChangeListener<>() {
            @Override
            public void changed(PropertyValueObservable<? extends Number> observable, Number oldValue, Number newValue) {
                log.info("1 - newValue : {}", newValue);

                if (newValue.intValue() == 9) {
                    // 移除当前监听器
                    observable.removeListener(this);
                }
            }
        });

        property.decrement(); // value == 9，并触发监听器
        property.decrement(); // value == 8，由于监听器已经移除，所以不会触发任何事件。
        Assertions.assertEquals(8, property.get());
    }

    @Test
    public void remove2() {
        IntegerProperty property = new IntegerProperty(10);
        // 监听器移除的示例
        OnePropertyChangeListener onePropertyChangeListener = new OnePropertyChangeListener();
        property.addListener(onePropertyChangeListener);

        property.increment(); // value == 11，并触发监听器
        property.removeListener(onePropertyChangeListener); // 移除监听器
        property.increment(); // value == 12，，由于监听器已经移除，所以不会触发任何事件。

        Assertions.assertEquals(12, property.get());

    }

    class OnePropertyChangeListener implements PropertyChangeListener<Number> {
        @Override
        public void changed(PropertyValueObservable<? extends Number> observable, Number oldValue, Number newValue) {
            counter.incrementAndGet();
            log.info("1 - oldValue:{}, newValue:{}, observable:{}", oldValue, newValue, observable);
        }
    }
}