import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

public class NameTest {

    public static void pattern7() {
        String content = "您当前套餐使用情况：本地主叫本地通话时长累计180.00分钟，已使用44.00分钟；国内短信累计200条，已使用6条；";
        String template = "当前余额thzh.可用余额thyy";
        String regex = "本地通话时长累计(?<thzh>[0-9\\.]+)分钟，已使用(?<thyy>[0-9\\.]+)分钟";
        parseSms(content, template, regex);

        String[] regexArray = regex.split(";");
        String[] templateArray = template.split(";");
// for (int i = 0; i < regexArray.length; i++) {
// parseSms(content, templateArray[i], regexArray[i]);
// }
    }

    public static void pattern9() {
        String content = "截至13年10月08日08时57分，您套餐内包含的本地通话时长已使用76分钟，剩余674分钟，剩余时长约占总时长90%，请您放心使用。以上内容供参考";
        String template = "语音剩余thsy;通话已用thyy，剩余thsy分钟;短信剩余dxsy条";
        String regex = "语音剩余(?<thsy>[0-9]+)分钟;您套餐内包含的本地通话时长已使用(?<thyy>[0-9]+)分钟，剩余(?<thsy>[0-9]+)分钟;短信剩余(?<dxsy>[0-9]+)条";
    }

    /**
     * @param content 匹配内容
     * @param template 渲染模板
     * @param regex 正则
     */
    private static void parseSms(String content, String template, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcherParse = p.matcher(content);
        Map<String, String> m = matcherParse.namedGroups();
        System.out.println("named:" + m);
        List<String> list = p.groupNames();
        System.out.println(list.toString());
        HashMap<String, String> valueMap = new HashMap<>();
        while (matcherParse.find()) {
            System.out.println("find");
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
                try {
                    String value = null;
                    if ((value = matcherParse.group(key)) != null) {
                        System.out.println("find value：" + value);
                        valueMap.put(key, value);
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        p = Pattern.compile(join("|", list));
        matcherParse = p.matcher(template);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcherParse.find()) {
            String key = valueMap.get(list.get(i));
            if (key == null) {
                continue;
            }
            matcherParse.appendReplacement(sb, key);
            i++;
        }
        matcherParse.appendTail(sb);
        System.out.println(sb);
        System.out.println(valueMap.toString());
        System.out.println("----------------------------");
    }

    public static String join(CharSequence delimiter, Iterable<String> tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        pattern7();

    }

    private void pattern8() {
        String tempale = "可用余额kyye";
        Pattern p = Pattern
                .compile("[当前余额|总账户]余额为(?<kyye>[0-9\\.]+)元");
// "[当前余额|总账户]余额为(?<kyye>[0-9\\.]+)元;您的账户余额为(?<kyye>[0-9\\.]+)元"
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("sms")));
            String content = null;
            while ((content = reader.readLine()) != null) {

                Matcher matcherParse = p.matcher(content);

                Map<String, Integer> mMap = null;
                Iterator<String> it = mMap.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    System.out.println(key);
                    System.out.println(mMap.get(key));
                }
                while (matcherParse.find()) {
                    for (int i = 1; i < matcherParse.groupCount() + 1; i++) {
                        System.out.println(matcherParse.group(i));
                        System.out.println("--------------");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static CharSequence getSubSequence(String text, int beginIndex, int endIndex) {
        return text.subSequence(beginIndex, endIndex);
    }

    /**
     * 可以满足需求了
     */
    private static void pattern6() {
        String t = "优惠总和：600.00分钟(时长);优惠剩余：400.00分钟(时长)";
        String template = "有yhzh分钟，剩余kyye分钟.";
        ArrayList<String> list = new ArrayList<>();
        list.add("yhzh");
        list.add("kyye");

        HashMap<String, String> valueMap = new HashMap<>();
        Pattern p = Pattern
                .compile("优惠总和：(?<yhzh>[0-9\\.]+)分钟[\\s\\S]时长[\\s\\S];优惠剩余：(?<kyye>[\\-0-9\\.]+)分钟[\\s\\S]时长[\\s\\S]");
        Matcher matcherParse = p.matcher(t);
        while (matcherParse.find()) {
            for (int i = 0; i < list.size(); i++) {
                String key = list.get(i);
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
        p = Pattern.compile("yhzh|kyye");
        matcherParse = p.matcher(template);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcherParse.find()) {
            matcherParse.appendReplacement(sb, valueMap.get(list.get(i)));
            i++;
        }
        matcherParse.appendTail(sb);
        System.out.println(sb);
        System.out.println(valueMap.toString());
        System.out.println("----------------------------");
    }

}
