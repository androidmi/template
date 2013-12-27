package com.dianxinos.template.parse.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 模板服务器端数据
 * 
 * @author dufan
 */
public class TemplateInfo {

    public String tmplate;
    public String regex;
    public String title;
    public boolean isSubData;

    public ArrayList<TemplateInfo> mSubInfos;
    public ArrayList<ItemModel> mItemList;
    public HashMap<Integer, TitleModel> mTitleMap;

    private TemplateInfo() {
    }

    public static TemplateInfo create(JSONObject obj) {
        TemplateInfo info = new TemplateInfo();
        try {
            JSONObject j = obj;
            // 两层结构
            if (j.has("sub")) {
                JSONArray subJ = j.getJSONArray("sub");
                ArrayList<TemplateInfo> subInfo = new ArrayList<TemplateInfo>();
                int subJsize = subJ.length();
                for (int i = 0; i < subJsize; i++) {
                    JSONObject subItem = subJ.getJSONObject(i);
                    TemplateInfo subTemInfo = new TemplateInfo();
                    subTemInfo.tmplate = subItem.getString("template");
                    subTemInfo.regex = subItem.getString("regexp");
                    subTemInfo.title = subItem.getString("title");
                    subInfo.add(subTemInfo);
                }
                info.mSubInfos = subInfo;
            } else {
                info.tmplate = j.getString("template");
                info.regex = j.getString("regexp");
            }
            info.title = j.getString("title");
            return info;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TemplateInfo createSingle(JSONObject objs) {
        TemplateInfo info = new TemplateInfo();
        try {
            objs = objs.getJSONObject("data");
            info.tmplate = objs.getString("template");
            info.regex = objs.getString("regexp");
            info.title = objs.getString("title");
            return info;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<TemplateInfo> createArray(JSONObject obj) {
        try {
            ArrayList<TemplateInfo> infoList = new ArrayList<TemplateInfo>();
            JSONArray array = obj.getJSONArray("array");
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);
                TemplateInfo info = TemplateInfo.create(j);
                infoList.add(info);
            }
            return infoList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class TitleModel {
        public String title;
        public int sort;
        public int id;

        public static TitleModel create(int id, String title, int sort) {
            TitleModel model = new TitleModel();
            model.title = title;
            model.id = id;
            model.sort = sort;
            return model;
        }
    }

    public static class SuperItemModel {
        public ArrayList<ItemModel> itemList;
        public String superTitle;
        public int sort;

        public static SuperItemModel create(String title, int sort, ArrayList<ItemModel> itemList) {
            SuperItemModel item = new SuperItemModel();
            item.superTitle = title;
            item.itemList = itemList;
            item.sort = sort;
            return item;
        }

        @Override
        public String toString() {
            return String.format("superTitle=%s,sort=%s,itemModel=%s", superTitle, sort, itemList);
        }
    }

    public static class SuperItemModelCompare implements Comparator<SuperItemModel> {

        @Override
        public int compare(SuperItemModel lhs, SuperItemModel rhs) {
            if (lhs.sort > rhs.sort) {
                return 1;
            } else {
                return lhs.sort == rhs.sort ? 0 : -1;
            }
        }
    }

    public static class ItemModel {
        public int gid;
        public String title;
        public String unit;
        public int sort;
        public ArrayList<String> regexList;
        public DataInfo dataInfo;

        public static ItemModel create(int gid, String title, String unit, int sort,
                ArrayList<String> regexList) {
            ItemModel model = new ItemModel();
            model.gid = gid;
            model.title = title;
            model.unit = unit;
            model.sort = sort;
            model.regexList = regexList;
            return model;
        }

        public void setDataInfo(DataInfo info) {
            this.dataInfo = info;
        }

        @Override
        public String toString() {
            return String.format("gid=%s,title=%s,unit=%s,sort=%s,regex=%s,datainof=%s", this.gid,
                    this.title, this.unit, this.sort, this.regexList, this.dataInfo);
        }
    }

    public static class ItemModelCompare implements Comparator<ItemModel> {

        @Override
        public int compare(ItemModel lhs, ItemModel rhs) {
            if (lhs.sort > rhs.sort) {
                return 1;
            } else {
                return lhs.sort == rhs.sort ? 0 : -1;
            }
        }
    }

    @SuppressWarnings("UseSparseArrays")
    public static TemplateInfo createFromJson(JSONObject obj) {
        TemplateInfo info = new TemplateInfo();
        try {
            JSONArray groupArra = obj.getJSONArray("groups");
            int arrayLength = groupArra.length();
            info.mTitleMap = new HashMap<Integer, TemplateInfo.TitleModel>(arrayLength);
            for (int i = 0; i < arrayLength; i++) {
                JSONObject o = groupArra.getJSONObject(i);
                int id = o.getInt("id");
                String title = o.getString("title");
                int sort = o.getInt("score");
                info.mTitleMap.put(id, TitleModel.create(id, title, sort));
            }

            JSONArray piecesArray = obj.getJSONArray("pieces");
            int piecesLength = piecesArray.length();
            ArrayList<ItemModel> itemList = new ArrayList<TemplateInfo.ItemModel>(piecesLength);
            for (int i = 0; i < piecesLength; i++) {
                JSONObject o = piecesArray.getJSONObject(i);
                int gid = o.getInt("gid");
                String title = o.getString("title");
                String unit = o.getString("unit");
                int sort = o.getInt("score");
                JSONArray regexArray = o.getJSONArray("regexvars");
                int regexArrayLength = regexArray.length();
                ArrayList<String> regexList = new ArrayList<String>(regexArrayLength);
                for (int j = 0; j < regexArrayLength; j++) {
                    String regex = regexArray.getString(j);
                    regexList.add(regex);
                }
                itemList.add(ItemModel.create(gid, title, unit, sort, regexList));
            }
            // sort by sort args
            SuperItemModelCompare a = new SuperItemModelCompare();
            Collections.sort(itemList, new ItemModelCompare());
            info.mItemList = itemList;
            return info;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
