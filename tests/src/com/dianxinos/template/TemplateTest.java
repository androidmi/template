package com.dianxinos.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.test.AndroidTestCase;
import android.util.Log;

import com.dianxinos.template.parse.ParseUtils;
import com.dianxinos.template.parse.model.DataInfo;
import com.dianxinos.template.parse.model.TemplateInfo;
import com.dianxinos.template.parse.model.ViewItemInfo;

public class TemplateTest extends AndroidTestCase {

    public void testGetDataInfo() {
        String content = "您当前套餐使用情况：本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；";
        String regex = "本地通话时长累计(?<thzh>[0-9\\.]+)分钟，已使用(?<thyy>[0-9\\,]+)分钟";
        Map<String, String> map = new HashMap<String, String>();
        map.put("thsy", "20.0");
        map.put("thyy", "50.0");
        map.put("thzh", "70.0");
        DataInfo info = DataInfo.create(map);
        assertEquals(20, info.getSy());
        assertEquals(50, info.getYy());
        assertEquals(70, info.getZh());

        map.put("thsy", "20.0");
        map.put("thyy", "50.0");
        info = DataInfo.create(map);
        assertEquals(20, info.getSy());
        assertEquals(50, info.getYy());
        assertEquals(70, info.getZh());

        map.put("thyy", "50.0");
        map.put("thzh", "70.0");
        info = DataInfo.create(map);
        assertEquals(20, info.getSy());
        assertEquals(50, info.getYy());
        assertEquals(70, info.getZh());

        map.put("thsy", "20.0");
        map.put("thzh", "70.0");
        info = DataInfo.create(map);
        assertEquals(20, info.getSy());
        assertEquals(50, info.getYy());
        assertEquals(70, info.getZh());
    }

    public void testTemplateCreate() {
        String data = FileUtils.getAssetFileContent(mContext, "tc_data");
        String sms = FileUtils.getAssetFileContent(mContext, "tc_sms");
        try {
            JSONObject obj = new JSONObject(data);
            ArrayList<TemplateInfo> infoList = TemplateInfo.createArray(obj);
            assertEquals(2, infoList.size());
            TemplateInfo info = infoList.get(0);
            assertNull(info.mSubInfos);
            
            info = infoList.get(1);
            assertEquals(2, info.mSubInfos.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    

}
