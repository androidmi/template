package com.dianxinos.template;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.text.TextUtils;

import com.dianxinos.template.parse.NTNewTcParser;
import com.dianxinos.template.parse.model.DataInfo;
import com.dianxinos.template.parse.model.TemplateInfo;
import com.dianxinos.template.parse.model.TemplateInfo.ItemModel;
import com.dianxinos.template.parse.model.TemplateInfo.SuperItemModel;

public class CTest extends AndroidTestCase {
    public void testParseArrayNew() {
        System.out.println("-----------------------------------");
        String sms = "您当前套餐使用情况："
                + "本地主叫本地通话时长累计80.00分钟；"
                + "长途通话时长累计200.00分钟，已使用30.00分钟；"
                + "国内短信累计200条，已使用6条；";

        String regex = "本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟，已使用(?<bdthyy>[0-9\\.]+)分钟;"
                + "本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟;";
        String data = FileUtils.getAssetFileContent(mContext, "ntnew_tc_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));

            NTNewTcParser parser = new NTNewTcParser(sms, "", regex);
            parser.setTempalteInfo(info);
            parser.parseSms();

            ArrayList<SuperItemModel> superItemModelList = parser.getSubItemModel();
            for (int i = 0; i < superItemModelList.size(); i++) {
                SuperItemModel model = superItemModelList.get(i);
                ItemModel itemModel = model.itemList.get(0);
                DataInfo dataInfo = itemModel.dataInfo;
                assertEquals(80, dataInfo.getZh());
                assertEquals(0, dataInfo.getYy());
                System.out.println("model:" + model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json异常");
        }
    }

    public void testParseArray() {
        System.out.println("--------------------a---------------------");
        String sms = "您当前套餐使用情况："
                + "本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；"
                + "长途通话时长累计200.00分钟，已使用30.00分钟；"
                + "国内短信累计200条；"
                + "20元3G省内夜间流量包 省内忙时、省际移动数据流量0.00M，已使用0.00M，"
                + "20元3G省内夜间流量包 省内闲时移动数据流量3072.00M，已使用0.00M，"
                + "国内移动数据流量累计200.00M，已使用13.31M。"
                + "手机缴费通用户可直接回复17使用手机缴费通业务。";
        String regex = "语音剩余(?<thsy>[0-9\\.]+)分钟;短信剩余(?<dxsy>[0-9\\.]+)条;"
                + "本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟，已使用(?<bdthyy>[0-9\\.]+)分钟;"
                + "长途通话时长累计(?<ctthzh>[0-9\\.]+)分钟，已使用(?<ctthyy>[0-9\\.]+)分钟;"
                + "国内短信累计(?<dxzh>[0-9\\.]+)条，已使用(?<dxyy>[0-9\\.]+)条;"
                + "国内短信累计(?<dxzh>[0-9\\.]+)条;"
                + "时长已使用(?<thyy>[0-9\\.]+)分钟，剩余(?<thsy>[0-9\\.]+)分钟";
        String data = FileUtils.getAssetFileContent(mContext, "ntnew_tc_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));

            NTNewTcParser parser = new NTNewTcParser(sms, "", regex);
            parser.setTempalteInfo(info);
            parser.parseSms();
            ArrayList<String> prefixList = parser.mPrefixList;
            System.out.println("prefixList:" + prefixList);
            ArrayList<SuperItemModel> superItemModelList = parser.getSubItemModel();
            for (int i = 0; i < superItemModelList.size(); i++) {
                SuperItemModel model = superItemModelList.get(i);
                ItemModel itemModel = model.itemList.get(0);
                DataInfo dataInfo = itemModel.dataInfo;
//                assertEquals(80, dataInfo.getZh());
//                assertEquals(60, dataInfo.getYy());
                System.out.println("model:" + model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json异常");
        }
    }
    
    public void testJson() {
        JSONArray array = new JSONArray();
        JSONObject o = new JSONObject();
        for (int i = 0; i < 10; i++) {
            try {
                o.put("hello", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(o);
        }
        System.out.println("hello:"+array);
    }
}
