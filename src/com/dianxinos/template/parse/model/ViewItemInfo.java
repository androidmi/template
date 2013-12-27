package com.dianxinos.template.parse.model;


/**
 * 条目内容实体类
 * @author dufan
 *
 */
public class ViewItemInfo {

    /**
     * 显示内容
     */
    public String content;
    
    public DataInfo dataInfo;
    
    public static ViewItemInfo create(String content) {
        ViewItemInfo info = new ViewItemInfo();
        info.content = content;
        return info;
    }
    
    public void setDataInfo(DataInfo dataInfo) {
        this.dataInfo = dataInfo;
    }
    
}
