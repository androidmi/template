package com.dianxinos.template.parse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

import com.dianxinos.template.parse.model.ViewItemInfo;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

public class ParseUtils {

    /**
     * @param content
     *            匹配内容
     * @param template
     *            渲染模板
     * @param regex
     *            正则
     * @return 显示的内容
     */
    public static ViewItemInfo parseSms(String content, String template,
            String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcherParse = p.matcher(content);
        List<String> groupNameList = p.groupNames();

        HashMap<String, String> valueMap = new HashMap<String, String>();
        while (matcherParse.find()) {
            for (int i = 0; i < groupNameList.size(); i++) {
                String key = groupNameList.get(i);
                try {
                    String value = null;
                    if ((value = matcherParse.group(key)) != null) {
                        valueMap.put(key, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        p = Pattern.compile(TextUtils.join("|", groupNameList));
        matcherParse = p.matcher(template);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcherParse.find()) {
            String groupName = valueMap.get(groupNameList.get(i));
            if (groupName == null) {
                continue;
            }
            matcherParse.appendReplacement(sb, groupName);
            i++;
        }
        Log.i("hello", valueMap.toString());
        matcherParse.appendTail(sb);
        System.out.println(sb);
        System.out.println("----------------------------");
        return ViewItemInfo.create(sb.toString());
    }

    public static ViewItemInfo paseSmsContent(String content, String template,
            String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcherParse = p.matcher(content);
        List<String> groupNameList = p.groupNames();

        HashMap<String, String> valueMap = new HashMap<String, String>();
        while (matcherParse.find()) {
            for (int i = 0; i < groupNameList.size(); i++) {
                String key = groupNameList.get(i);
                try {
                    String value = null;
                    if ((value = matcherParse.group(key)) != null) {
                        valueMap.put(key, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return ViewItemInfo.create(fillTemplate(groupNameList, template,
                valueMap));
    }

    public static String fillTemplate(List<String> groupNameList,
            String template, Map<String, String> valueMap) {
        Pattern p = Pattern.compile(TextUtils.join("|", groupNameList));
        Matcher matcherParse = p.matcher(template);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcherParse.find()) {
            System.out.println("find");
            String groupName = valueMap.get(groupNameList.get(i));
            if (groupName == null) {
                return null;
            }
            matcherParse.appendReplacement(sb, groupName);
            i++;
        }
        matcherParse.appendTail(sb);
        return sb.toString();
    }
}
