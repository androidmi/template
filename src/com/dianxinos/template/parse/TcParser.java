package com.dianxinos.template.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.dianxinos.template.parse.model.DataInfo;
import com.dianxinos.template.parse.model.TemplateInfo;
import com.dianxinos.template.parse.model.ViewItemInfo;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

/**
 * 解析套餐
 * 
 * @author dufan
 */
public class TcParser extends AbstractParsebase {

    private static final String TAG = "TcParser";
    private Map<String, String> mValueMap;
    private List<String> mGroupNameList;
    private String mContent;
    private static final boolean DEBUG = true;

    public TcParser(String content, String template, String regex) {
        super(content, template, regex);
        mContent = null;
    }

    public TcParser(String content, TemplateInfo info) {
        super(content, info.tmplate, info.regex);
        mContent = null;
    }

    @Override
    public void parseSms() {
        String[] regexArray = mRegex.split(";");
        for (int i = 0; i < regexArray.length; i++) {
            Pattern p = Pattern.compile(regexArray[i]);
            Matcher matcherParse = p.matcher(mSmsBody);
            mGroupNameList = p.groupNames();
            mValueMap = new HashMap<String, String>();
            boolean find = false;
            while (matcherParse.find()) {
                int size = mGroupNameList.size();
                for (int j = 0; j < size; j++) {
                    String key = mGroupNameList.get(j);
                    String value = null;
                    if ((value = matcherParse.group(key)) != null) {
                        mValueMap.put(key, value);
                        find = true;
                    }
                }
            }
            if (find) {
                Log.i(TAG, "find");
                break;
            }
        }

        Log.i(TAG, mGroupNameList.toString());
        Log.i(TAG, mValueMap.toString());
    }

    public ViewItemInfo getItemInfo() {
        return ViewItemInfo.create(mContent);
    }

    public void fillTemplate() {

        String[] templateArray = mTemplate.split(";");
        Pattern p = Pattern.compile(TextUtils.join("|", mGroupNameList));

        int matchCount = mGroupNameList.size();

        for (int i = 0; i < templateArray.length; i++) {
            Matcher matcherParse = p.matcher(templateArray[i]);
            StringBuffer sb = new StringBuffer();
            int j = 0;
            while (matcherParse.find()) {
                String groupName = mValueMap.get(mGroupNameList.get(j));
                matcherParse.appendReplacement(sb, groupName);
                j++;
            }
            matcherParse.appendTail(sb);
            if (j == matchCount) {
                mContent = sb.toString();
                Log.i(TAG, "match:" + mContent + ":" + matchCount);
            } else {
                Log.i(TAG, "continue:" + j);
            }
        }

    }

    /**
     * 整理数据
     * @return
     */
    public DataInfo getDataInfo() {
        DataInfo info = DataInfo.create(mValueMap);
        info.setContent(mContent);
        return info;
    }

    public Map<String, String> getValueMap() {
        return mValueMap;
    }

}
