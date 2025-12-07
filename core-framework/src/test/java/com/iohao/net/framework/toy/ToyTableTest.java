package com.iohao.net.framework.toy;

import com.iohao.net.framework.IonetVersion;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 渔民小镇
 * @date 2023-01-30
 */
public class ToyTableTest {
    @Test
    public void test() {

        ToyTable table = new ToyTable();
        ToyTableRegion tableRegion = table.getRegion("ionet");
        tableRegion.putLine("pid", "73033");
        tableRegion.putLine("version", IonetVersion.VERSION);
        tableRegion.putLine("document", "http://ionet.iohao.com");

        ToyTableRegion memoryRegion = table.getRegion("Memory");
        memoryRegion.putLine("used", "xx.xxMB");
        memoryRegion.putLine("freeMemory", "xxx.xxxMB");
        memoryRegion.putLine("totalMemory", "xxx.xMB");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        var startTime = new Date();
        var endTime = new Date();
        var consumeTime = (endTime.getTime() - startTime.getTime()) / 1000f;
        var consume = String.format("%.2f s", consumeTime);

        ToyTableRegion other = table.getRegion("Time");
        other.putLine("start", simpleDateFormat.format(startTime));
        other.putLine("end", simpleDateFormat.format(endTime));
        other.putLine("consume", consume);

        table.render();

        extractedLicense();
    }

    void extractedLicense() {
        String builder = "| LICENSE  | %s%n";
        System.out.printf(builder, "AGPL3.0");
        IonetBanner.printlnMsg("+----------+--------------------------------------------------------------------------------------");
    }

    @Test
    public void render() {
        IonetBanner.render();
    }
}