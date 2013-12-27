package com.dianxinos.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import android.util.SparseArray;

import com.dianxinos.template.parse.NewTcParser;
import com.dianxinos.template.parse.model.DataInfo;
import com.dianxinos.template.parse.model.TemplateInfo;
import com.dianxinos.template.parse.model.TemplateInfo.ItemModel;
import com.dianxinos.template.parse.model.TemplateInfo.ItemModelCompare;
import com.dianxinos.template.parse.model.TemplateInfo.SuperItemModel;
import com.dianxinos.template.parse.model.TemplateInfo.SuperItemModelCompare;
import com.dianxinos.template.parse.model.TemplateInfo.TitleModel;

public class NewTemplateTest extends AndroidTestCase {
    public void testNewTemplateCreate() {
        String data = FileUtils.getAssetFileContent(mContext, "new_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));
            assertNotNull(info.mItemList);
            assertNotNull(info.mTitleMap);
            TitleModel model = info.mTitleMap.get(1);
            assertEquals("本地通话", model.title);

            ItemModel itemModel = info.mItemList.get(0);
            assertTrue(itemModel.regexList.contains("ctthzh"));
            assertEquals("长途通话时长", itemModel.title);
            assertEquals("分钟", itemModel.unit);
            assertEquals(1, itemModel.gid);
            assertEquals(3, itemModel.sort);
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json error");
        }
    }

    public void testParse() {
        String sms = "您当前套餐使用情况：本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；国内短信累计200条，已使用6条；"
                + "20元3G省内夜间流量包 省内忙时、省际移动数据流量0.00M，已使用0.00M，"
                + "20元3G省内夜间流量包 省内闲时移动数据流量3072.00M，已使用0.00M，"
                + "国内移动数据流量累计200.00M，已使用13.31M。"
                + "手机缴费通用户可直接回复17使用手机缴费通业务。";
        String regex = "语音剩余(?<thsy>[0-9\\.]+)分钟;短信剩余(?<dxsy>[0-9\\.]+)条;"
                + "本地主叫本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟，已使用(?<bdthyy>[0-9\\.]+)分钟;本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟"
                + "国内短信累计(?<dxzh>[0-9\\.]+)条，已使用(?<dxyy>[0-9\\.]+)条;"
                + "时长已使用(?<thyy>[0-9\\.]+)分钟，剩余(?<thsy>[0-9\\.]+)分钟";
        String data = FileUtils.getAssetFileContent(mContext, "new_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));
            ArrayList<ItemModel> itemList = info.mItemList;
            @SuppressWarnings("unused")
            HashMap<Integer, TitleModel> titleMap = info.mTitleMap;

            NewTcParser parser = new NewTcParser(sms, "", regex);
            parser.parseSms();
            Map<String, String> nameMap = parser.getNameMap();
            System.out.println(nameMap);
            boolean find = false;
            ArrayList<ItemModel> itemModelList = new ArrayList<TemplateInfo.ItemModel>();
            // TODO 正则匹配的数值和数据相同
            for (int i = 0; i < itemList.size(); i++) {
                ItemModel itemModel = itemList.get(i);
                ArrayList<String> regexList = itemModel.regexList;
                int regexSize = regexList.size();
                if (regexSize != nameMap.size()) {
                    continue;
                }
                for (int j = i; j < regexSize; j++) {
                    String regexItem = regexList.get(j);
                    String value = nameMap.get(regexItem);
                    if (TextUtils.isEmpty(value)) {
                        find = false;
                        break;
                    }
                    find = true;
                }
                if (find) {
                    itemModelList.add(itemModel);
                    break;
                }
            }
            // 解析出来的所有条目
            if (!itemModelList.isEmpty()) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json异常");
        }
    }

    public void testParseVales() {
        String sms = "您当前套餐使用情况：本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；国内短信累计200条，已使用6条；"
                + "20元3G省内夜间流量包 省内忙时、省际移动数据流量0.00M，已使用0.00M，"
                + "20元3G省内夜间流量包 省内闲时移动数据流量3072.00M，已使用0.00M，"
                + "国内移动数据流量累计200.00M，已使用13.31M。"
                + "手机缴费通用户可直接回复17使用手机缴费通业务。";
        String regex = "语音剩余(?<thsy>[0-9\\.]+)分钟;短信剩余(?<dxsy>[0-9\\.]+)条;"
                + "本地主叫本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟，已使用(?<bdthyy>[0-9\\.]+)分钟;本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟;"
                + "国内短信累计(?<dxzh>[0-9\\.]+)条，已使用(?<dxyy>[0-9\\.]+)条;"
                + "时长已使用(?<thyy>[0-9\\.]+)分钟，剩余(?<thsy>[0-9\\.]+)分钟";
        String data = FileUtils.getAssetFileContent(mContext, "new_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));
            ArrayList<ItemModel> itemList = info.mItemList;
            @SuppressWarnings("unused")
            HashMap<Integer, TitleModel> titleMap = info.mTitleMap;

            NewTcParser parser = new NewTcParser(sms, "", regex);
            parser.setTempalteInfo(info);
            parser.parseSms();
            ArrayList<Map<String, String>> valueList = parser.mValueList;
            ArrayList<ItemModel> itemModelList = new ArrayList<TemplateInfo.ItemModel>();
            for (Map<String, String> valueItm : valueList) {
                System.out.println("item:" + valueItm);
                ItemModel model = parser.getTemplate(valueItm);
                if (model != null) {
                    System.out.println(model.dataInfo);
                    itemModelList.add(model);
                }
                System.out.println(model);
            }
            System.out.println(itemModelList.size());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json异常");
        }
    }

    public void testSort() {
        String sms = "您当前套餐使用情况："
                + "本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；"
                + "长途通话时长累计200.00分钟，已使用30.00分钟；"
                + "国内短信累计200条，已使用6条；"
                + "20元3G省内夜间流量包 省内忙时、省际移动数据流量0.00M，已使用0.00M，"
                + "20元3G省内夜间流量包 省内闲时移动数据流量3072.00M，已使用0.00M，"
                + "国内移动数据流量累计200.00M，已使用13.31M。"
                + "手机缴费通用户可直接回复17使用手机缴费通业务。";
        String regex = "语音剩余(?<thsy>[0-9\\.]+)分钟;短信剩余(?<dxsy>[0-9\\.]+)条;"
                + "本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟，已使用(?<bdthyy>[0-9\\.]+)分钟;"
                + "长途通话时长累计(?<ctthzh>[0-9\\.]+)分钟，已使用(?<ctthyy>[0-9\\.]+)分钟;"
                + "国内短信累计(?<dxzh>[0-9\\.]+)条，已使用(?<dxyy>[0-9\\.]+)条;"
                + "时长已使用(?<thyy>[0-9\\.]+)分钟，剩余(?<thsy>[0-9\\.]+)分钟";
        String data = FileUtils.getAssetFileContent(mContext, "new_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));
            ArrayList<ItemModel> itemList = info.mItemList;
            assertEquals("长途通话时长", itemList.get(0).title);
            assertEquals("市内通话时长", itemList.get(1).title);

        } catch (JSONException e) {
            e.printStackTrace();
            fail("json异常");
        }
    }

    @SuppressLint("UseSparseArrays")
    @SuppressWarnings("unused")
    public void testParseArray() {
        String sms = "您当前套餐使用情况："
                + "本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；"
                + "长途通话时长累计200.00分钟，已使用30.00分钟；"
                + "国内短信累计200条，已使用6条；"
                + "20元3G省内夜间流量包 省内忙时、省际移动数据流量0.00M，已使用0.00M，"
                + "20元3G省内夜间流量包 省内闲时移动数据流量3072.00M，已使用0.00M，"
                + "国内移动数据流量累计200.00M，已使用13.31M。"
                + "手机缴费通用户可直接回复17使用手机缴费通业务。";
        String regex = "语音剩余(?<thsy>[0-9\\.]+)分钟;短信剩余(?<dxsy>[0-9\\.]+)条;"
                + "本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟，已使用(?<bdthyy>[0-9\\.]+)分钟;"
                + "长途通话时长累计(?<ctthzh>[0-9\\.]+)分钟，已使用(?<ctthyy>[0-9\\.]+)分钟;"
                + "国内短信累计(?<dxzh>[0-9\\.]+)条，已使用(?<dxyy>[0-9\\.]+)条;"
                + "时长已使用(?<thyy>[0-9\\.]+)分钟，剩余(?<thsy>[0-9\\.]+)分钟";
        String data = FileUtils.getAssetFileContent(mContext, "new_tc_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));

            HashMap<Integer, TitleModel> titleMap = info.mTitleMap;

            NewTcParser parser = new NewTcParser(sms, "", regex);
            parser.setTempalteInfo(info);
            parser.parseSms();
            ArrayList<ItemModel> itemModelList = parser.getItemList();
            HashMap<Integer, ArrayList<ItemModel>> map = new HashMap<Integer, ArrayList<ItemModel>>();
            for (int i = 0; i < itemModelList.size(); i++) {
                ItemModel itemModel = itemModelList.get(i);
                int gid = itemModel.gid;
                if (map.containsKey(gid)) {
                    ArrayList<ItemModel> list = map.get(gid);
                    list.add(itemModel);
                    map.put(gid, list);
                } else {
                    ArrayList<ItemModel> list = new ArrayList<TemplateInfo.ItemModel>();
                    list.add(itemModel);
                    map.put(gid, list);
                }
                System.out.println("array:" + itemModel);
            }

            ArrayList<SuperItemModel> superList = new ArrayList<TemplateInfo.SuperItemModel>();
            Iterator<Integer> iterator = map.keySet().iterator();
            System.out.println("array size:" + map.size() + ":" + itemModelList.size());
            while (iterator.hasNext()) {
                Integer gid = (Integer) iterator.next();
                ArrayList<ItemModel> valueList = map.get(gid);
                TitleModel titleModel = titleMap.get(gid);
                SuperItemModel model = SuperItemModel.create(titleModel.title, titleModel.sort, valueList);
                superList.add(model);
            }
            // 排序
            Collections.sort(superList, new SuperItemModelCompare());
            System.out.println("super array:" + superList.size());
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json异常");
        }
    }

    public void testParseArrayNew() {
        String sms = "您当前套餐使用情况："
                + "本地主叫本地通话时长累计80.00分钟，已使用44.00分钟；"
                + "wlan累计80.00分钟;"
                + "长途通话时长累计200.00分钟，已使用30.00分钟；"
                + "国内短信累计200条，已使用6条；"
                + "20元3G省内夜间流量包 省内忙时、省际移动数据流量0.00M，已使用0.00M，"
                + "20元3G省内夜间流量包 省内闲时移动数据流量3072.00M，已使用0.00M，"
                + "国内移动数据流量累计200.00M，已使用13.31M。"
                + "手机缴费通用户可直接回复17使用手机缴费通业务。";
        String regex = "语音剩余(?<thsy>[0-9\\.]+)分钟;短信剩余(?<dxsy>[0-9\\.]+)条;"
                + "本地通话时长累计(?<bdthzh>[0-9\\.]+)分钟，已使用(?<bdthyy>[0-9\\.]+)分钟;"
                + "wlan累计(?<bdthzh>[0-9\\.]+)分钟;"
                + "长途通话时长累计(?<ctthzh>[0-9\\.]+)分钟，已使用(?<ctthyy>[0-9\\.]+)分钟;"
                + "国内短信累计(?<dxzh>[0-9\\.]+)条，已使用(?<dxyy>[0-9\\.]+)条;"
                + "时长已使用(?<thyy>[0-9\\.]+)分钟，剩余(?<thsy>[0-9\\.]+)分钟";
        String data = FileUtils.getAssetFileContent(mContext, "new_tc_data");
        try {
            TemplateInfo info = TemplateInfo.createFromJson(new JSONObject(data)
                    .getJSONObject("telFeeTemplate"));

            HashMap<Integer, TitleModel> titleMap = info.mTitleMap;

            NewTcParser parser = new NewTcParser(sms, "", regex);
            parser.setTempalteInfo(info);
            parser.parseSms();

            ArrayList<SuperItemModel> superItemModelList = parser.getSubItemModel();
            for (int i = 0; i < superItemModelList.size(); i++) {
                SuperItemModel model = superItemModelList.get(i);
                System.out.println("model:" + model);
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("hfzh", "10");
            System.out.println("hello:"+DataInfo.create(map));
        } catch (JSONException e) {
            e.printStackTrace();
            fail("json异常");
        }
    }
}
