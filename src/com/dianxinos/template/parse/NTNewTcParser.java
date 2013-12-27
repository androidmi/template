package com.dianxinos.template.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.dianxinos.template.parse.model.DataInfo;
import com.dianxinos.template.parse.model.TemplateInfo;
import com.dianxinos.template.parse.model.TemplateInfo.ItemModel;
import com.dianxinos.template.parse.model.TemplateInfo.SuperItemModel;
import com.dianxinos.template.parse.model.TemplateInfo.SuperItemModelCompare;
import com.dianxinos.template.parse.model.TemplateInfo.TitleModel;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

/**
 * 解析套餐
 * 
 * @author dufan
 */
public class NTNewTcParser extends AbstractParsebase {

    private static final String TAG = "NewTcParser";
    private static final boolean DEBUG = true;
    public ArrayList<Map<String, String>> mValueList;

    public NTNewTcParser(String smsBody, String template, String regex) {
        super(smsBody, template, regex);
    }

    /**
     * just for stat report
     * 需要显示的条目前缀
     */
    public ArrayList<String> mPrefixList = new ArrayList<String>();
    public ArrayList<String> parseSuccessList = new ArrayList<String>();

    @Override
    public void parseSms() {
        String[] regexArray = mRegex.split(";");
        mValueList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < regexArray.length; i++) {
            String regex = regexArray[i];
            Pattern p = Pattern.compile(regex);
            Matcher matcherParse = p.matcher(mSmsBody);
            List<String> groupNameList = p.groupNames();
            if (groupNameList == null || groupNameList.isEmpty()) {
                continue;
            }
            String name = groupNameList.get(0);
            String prefix = name.substring(0, name.length() - 2);
            if (!mPrefixList.contains(prefix)) {
                mPrefixList.add(prefix);
            }
            if (parseSuccessList.contains(prefix)) {
                continue;
            }
            Map<String, String> valueMap = matcherParse.namedGroups();
            if (!valueMap.isEmpty()) {
                System.out.println(regex + "+find:" + i);
                ItemModel model = getTemplate(valueMap);
                if (model != null) {
                    itemModelList.add(model);
                }
            } else {
                System.out.println(regex + ":not find:" + i);
            }
        }
    }

    private TemplateInfo mInfo;

    public void setTempalteInfo(TemplateInfo info) {
        mInfo = info;
    }

    public ArrayList<ItemModel> itemModelList = new ArrayList<TemplateInfo.ItemModel>();
    public ArrayList<String> itemTitleList = new ArrayList<String>();

    public ArrayList<ItemModel> getItemList() {
        return itemModelList;
    }

    public ItemModel getTemplate(Map<String, String> nameMap) {
        ArrayList<ItemModel> itemList = mInfo.mItemList;

        boolean find = false;
        // TODO 正则匹配的数值和数据相同
        for (int i = 0; i < itemList.size(); i++) {
            ItemModel itemModel = itemList.get(i);
            ArrayList<String> regexList = itemModel.regexList;
            int regexSize = regexList.size();
            for (int j = 0; j < regexSize; j++) {
                String regexItem = regexList.get(j);
                String value = nameMap.get(regexItem);
                if (!TextUtils.isEmpty(value)) {
                    find = true;
                    break;
                }
            }
            if (find) {
                itemModel.setDataInfo(DataInfo.create(nameMap));
                return itemModel;
            }
        }
        return null;
    }

    public ArrayList<SuperItemModel> getSubItemModel() {
        ArrayList<ItemModel> itemModelList = getItemList();
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
        }

        ArrayList<SuperItemModel> superList = new ArrayList<TemplateInfo.SuperItemModel>();
        Iterator<Integer> iterator = map.keySet().iterator();
        HashMap<Integer, TitleModel> titleMap = mInfo.mTitleMap;
        while (iterator.hasNext()) {
            Integer gid = (Integer) iterator.next();
            ArrayList<ItemModel> valueList = map.get(gid);
            TitleModel titleModel = titleMap.get(gid);
            SuperItemModel model = SuperItemModel.create(titleModel.title, titleModel.sort, valueList);
            superList.add(model);
        }
        // 排序
        Collections.sort(superList, new SuperItemModelCompare());
        return superList;
    }

}
