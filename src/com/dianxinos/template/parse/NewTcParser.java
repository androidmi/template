package com.dianxinos.template.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.SparseArray;

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
public class NewTcParser extends AbstractParsebase {

    private static final String TAG = "NewTcParser";
    private Map<String, String> mValueMap;
    private List<String> mGroupNameList;
    private static final boolean DEBUG = true;
    public ArrayList<Map<String, String>> mValueList;

    public NewTcParser(String smsBody, String template, String regex) {
        super(smsBody, template, regex);
    }

    @Override
    public void parseSms() {
        String[] regexArray = mRegex.split(";");
        mValueList = new ArrayList<Map<String,String>>();
        for (int i = 0; i < regexArray.length; i++) {
            Pattern p = Pattern.compile(regexArray[i]);
            Matcher matcherParse = p.matcher(mSmsBody);
            mGroupNameList = p.groupNames();
            mValueMap = matcherParse.namedGroups();
            boolean find = mValueMap.size() > 0;
            if (find) {
                System.out.println("find:" + i);
                mValueList.add(mValueMap);
            } else {
                System.out.println("not find:" + i);
            }
            System.out.println("list:" + mGroupNameList.toString());
            System.out.println("map:" + mValueMap.toString());
        }
    }

    private TemplateInfo mInfo;

    public void setTempalteInfo(TemplateInfo info) {
        mInfo = info;
    }
    
    public ArrayList<ItemModel> getItemList() {
        ArrayList<ItemModel> itemModelList = new ArrayList<TemplateInfo.ItemModel>();
        for (Map<String, String> valueItm : mValueList) {
            System.out.println("item:"+valueItm);
            ItemModel model = getTemplate(valueItm);
            if (model != null) {
                itemModelList.add(model);
            }
        }
        return itemModelList;
    }

    @SuppressWarnings("unused")
    public ItemModel getTemplate(Map<String, String> nameMap) {
        ArrayList<ItemModel> itemList = mInfo.mItemList;
        HashMap<Integer, TitleModel> titleMap = mInfo.mTitleMap;

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
            for (int j = 0; j < regexSize; j++) {
                String regexItem = regexList.get(j);
                String value = nameMap.get(regexItem);
                if (TextUtils.isEmpty(value)) {
                    find = false;
                    break;
                }
                find = true;
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
            System.out.println("array:" + itemModel);
        }

        ArrayList<SuperItemModel> superList = new ArrayList<TemplateInfo.SuperItemModel>();
        Iterator<Integer> iterator = map.keySet().iterator();
        System.out.println("array size:" + map.size() + ":" + itemModelList.size());
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

    /**
     * @deprecated 
     * @return
     */
    public Map<String, String> getNameMap() {
        return mValueMap;
    }

}
