package com.dianxinos.template.parse.model;

import java.util.Iterator;
import java.util.Map;

import android.text.TextUtils;

/**
 * 解析后的内容
 * 
 * @author dufan
 */
public class DataInfo {

    /**
     * 总和
     */
    private String zh;
    /**
     * 剩余
     */
    private String sy;
    /**
     * 已用
     */
    private String yy;

    /**
     * 备用
     */
    private String bc;

    /**
     * 模板填充数据后显示的内容
     */
    private String content;

    private DataInfo() {
    }

    public static DataInfo create(Map<String, String> map) {
        DataInfo info = new DataInfo();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (key.endsWith("zh")) {
                info.zh = value;
            } else if (key.endsWith("yy")) {
                info.yy = value;
            } else if (key.endsWith("sy")) {
                info.sy = value;
            } else {
                info.bc = value;
            }
        }
        return info;
    }

    /**
     * 剩余
     * 
     * @return
     */
    public int getSy() {
        if (!TextUtils.isEmpty(sy)) {
            return (int) Float.parseFloat(sy);
        } else {
            if (!TextUtils.isEmpty(zh) && !TextUtils.isEmpty(yy)) {
                float zhF = Float.parseFloat(zh);
                float yyF = Float.parseFloat(yy);
                float syF = zhF - yyF;
                return (int) syF;
            } else {
                return 0;
            }
        }
    }

    /**
     * 已用
     * 
     * @return
     */
    public int getYy() {
        if (!TextUtils.isEmpty(yy)) {
            return (int) Float.parseFloat(yy);
        } else {
            if (!TextUtils.isEmpty(zh) && !TextUtils.isEmpty(sy)) {
                float zhF = Float.parseFloat(zh);
                float syF = Float.parseFloat(sy);
                float yyF = zhF - syF;
                return (int) yyF;
            } else {
                return 0;
            }
        }
    }

    /**
     * 总和
     * 
     * @return
     */
    public int getZh() {
        if (!TextUtils.isEmpty(zh)) {
            return (int) Float.parseFloat(zh);
        } else {
            if (!TextUtils.isEmpty(yy) && !TextUtils.isEmpty(sy)) {
                float syF = Float.parseFloat(sy);
                float yyF = Float.parseFloat(yy);
                float zhF = yyF + syF;
                return (int) zhF;
            } else {
                return 0;
            }
        }
    }

    public String getBc() {
        return bc;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format("总和%s,已用%s,剩余%s", getZh(), getYy(), getSy());
    }
}
