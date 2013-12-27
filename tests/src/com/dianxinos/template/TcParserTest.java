package com.dianxinos.template;

import android.test.AndroidTestCase;

import com.dianxinos.template.parse.TcParser;
import com.dianxinos.template.parse.model.DataInfo;
import com.dianxinos.template.parse.model.ViewItemInfo;

public class TcParserTest extends AndroidTestCase {

    public void testParse() {
        String content = "您当前套餐使用情况：本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；";
        String regex = "本地通话时长累计(?<thzh>[0-9\\.]+)分钟，已使用(?<thyy>[0-9\\.]+)分钟";
        String template = "剩余thzh";
        template = "累计thzh分钟，已使用thyy分钟";
        TcParser parser = new TcParser(content, template, regex);
        parser.parseSms();
        DataInfo info = parser.getDataInfo();
        assertEquals("80.00", info.getZh());
        
        parser.fillTemplate();
        ViewItemInfo itemInfo = parser.getItemInfo();
        assertEquals("累计80.00分钟，已使用44.00分钟", itemInfo.content);
    }
    
    public void testFillTemplate() {
        String template = "本地主叫本地通话时长累计thzh分钟，已使用thyy分钟;通话时长累计thzh分钟;已使用thyy分钟;剩余thsy分钟";
        String content = "您当前套餐使用情况：本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；";
        String regex = "本地通话时长累计(?<thzh>[0-9\\.]+)分钟，已使用(?<thyy>[0-9\\.]+)分钟;已使用(?<thyy>[0-9\\.]+)分钟;本地通话时长累计(?<thzh>[0-9\\.]+)分钟";
        TcParser parser = new TcParser(content, template, regex);
        parser.parseSms();
        parser.fillTemplate();
        ViewItemInfo itemInfo = parser.getItemInfo();
        assertEquals("本地主叫本地通话时长累计80.00分钟，已使用44.00分钟", itemInfo.content);
        
        content = "本地主叫本地通话时长累计80.00分钟;";
        parser = new TcParser(content, template, regex);
        parser.parseSms();
        parser.fillTemplate();
        itemInfo = parser.getItemInfo();
        assertEquals("通话时长累计80.00分钟", itemInfo.content);
        
        content = "tonghua已使用44.00分钟;";
        parser = new TcParser(content, template, regex);
        parser.parseSms();
        parser.fillTemplate();
        itemInfo = parser.getItemInfo();
        assertEquals("已使用44.00分钟", itemInfo.content);
    }
}
