package regex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NameTest {

    static ArrayList<String> list = null;

    public static void pattern7() {
        String t = "当前余额余额为300.00元";
        String template = "当前余额kyye.";
        Pattern p = Pattern.compile("[当前余额|总账户]余额为(?<kyye>[0-9\\.]+)元");
        Matcher matcherParse = p.matcher(t);

        Map<String, Integer> mMap = p.namedGroups;
        Iterator<String> it = mMap.keySet().iterator();
        // 可以匹配的正则数量
        int contentSize = mMap.size();
        
        ArrayList<String> list = new ArrayList<>(contentSize);
        while (it.hasNext()) {
            String key = it.next();
            System.out.println(key);
            list.add(key);
        }

        HashMap<String, String> valueMap = new HashMap<>();
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
        p = Pattern.compile("kyye");
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

    private static void parseSms(String t, String template, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcherParse = p.matcher(t);

        Map<String, Integer> mMap = p.namedGroups;
        Iterator<String> it = mMap.keySet().iterator();
        // 可以匹配的正则数量
        int contentSize = mMap.size();
        
        ArrayList<String> list = new ArrayList<>(contentSize);
        while (it.hasNext()) {
            String key = it.next();
            System.out.println(key);
            list.add(key);
        }

        HashMap<String, String> valueMap = new HashMap<>();
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
        p = Pattern.compile("kyye");
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
    
    public static void main(String[] args) {
       pattern7();

    }
    
    private void pattern8() {
        String tempale = "可用余额kyye";
        Pattern p = Pattern
                .compile("[当前余额|总账户]余额为(?<kyye>[0-9\\.]+)元");
//        "[当前余额|总账户]余额为(?<kyye>[0-9\\.]+)元;您的账户余额为(?<kyye>[0-9\\.]+)元"
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File("sms")));
            String content = null;
            while ((content = reader.readLine()) != null) {
                
                Matcher matcherParse = p.matcher(content);
                
                Map<String, Integer> mMap = p.namedGroups;
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

    private static void pattern5() {
        String t = "优惠总和：600.00分钟(时长);优惠剩余：400.00分钟(时长)";
        String template = "有${yhzh}分钟，剩余${kyye}分钟.";
        HashMap<String, String> map = new HashMap<>(2);
        map.put("yhzh", "$1");
        map.put("kyye", "$2");
        ArrayList<String> list = new ArrayList<>();
        list.add("yhzh");
        list.add("kyye");

        HashMap<String, String> valueMap = new HashMap<>();
        Pattern p = Pattern
                .compile("优惠总和：(?<yhzh>[0-9\\.]+)分钟[\\s\\S]时长[\\s\\S];优惠剩余：(?<kyye>[\\-0-9\\.]+)分钟[\\s\\S]时长[\\s\\S]");
        Matcher matcherParse = p.matcher(t);
        while (matcherParse.find()) {
            System.out.println(matcherParse.group() + "0000000000");
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

    private static void pattern4() {
        String t = "优惠总和：600.00分钟(时长);优惠剩余：400.00分钟(时长)";
        String template = "有${yhzh}分钟，剩余${kyye}分钟.";
        HashMap<String, String> map = new HashMap<>(2);
        map.put("yhzh", "$1");
        map.put("kyye", "$2");
        HashMap<String, String> valueMap = new HashMap<>();
        Pattern p = Pattern
                .compile("优惠总和：(?<yhzh>[0-9\\.]+)分钟[\\s\\S]时长[\\s\\S];优惠剩余：(?<kyye>[\\-0-9\\.]+)分钟[\\s\\S]时长[\\s\\S]");
        Matcher matcherParse = p.matcher(t);
        while (matcherParse.find()) {
            System.out.println(matcherParse.group() + "0000000000");
            Iterator<String> keyIterator = map.keySet().iterator();
            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                try {
                    String value = null;
                    if ((value = matcherParse.group(key)) != null) {
                        System.out.println(value + ":");
                        valueMap.put(key, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Iterator<String> valueIterator = valueMap.keySet().iterator();
        String a = null;
        while (valueIterator.hasNext()) {
            String key = valueIterator.next();
            if (a != null) {
                a = a.replace(key, valueMap.get(key));
            } else {
                a = template.replace(key, valueMap.get(key));
            }
        }
        System.out.println(a);
        System.out.println(valueMap.toString());
        System.out.println("----------------------------");
    }

    private static void pattern3() {
        String template = "有${yhzh}分钟，剩余${kyye}分钟.";
        Pattern p = Pattern.compile("yhzh|kyye");
        Matcher m = p.matcher(template);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "dog");
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
    }

    private static void pattern2() {
        String t = "尊敬的联通用户，您好！您当前的优惠信息如下：1.短信累积;优惠总和：300条(短信);优惠剩余：0条(短信);2.省内主叫优惠;优惠总和：600.00分钟(时长);优惠剩余：600.00分钟(时长);3.省内流量;优惠总和：1048576.00K(gprs流量;优惠剩余：744150.00K(gprs流量;。\r\n 【老用户不换号0元购机上 10010.com】";
        Pattern p = Pattern.compile("");
        Matcher m = p.matcher(t);
        while (m.find()) {
            String one = m.group("two");
            String totle = m.group(1);
            String yiyong = m.group(2);
            String shenyu = m.group(3);
            System.out.println(totle + ":" + yiyong + ":" + shenyu);
            System.out.println(one + ":one");
        }
    }

    private static void pattern() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(
                    "pattern")));
            String content = null;
            while ((content = reader.readLine()) != null) {
                String pattern = reader.readLine();
                System.out.println(content);
                System.out.println(pattern);
                String[] patternArray = pattern.split(";");
                for (int i = 0; i < patternArray.length; i++) {
                    Pattern p = Pattern.compile(patternArray[i]);
                    Matcher m = p.matcher(content);
                    if (m.find()) {
                        int count = m.groupCount();
                        StringBuilder sb = new StringBuilder();
                        for (int j = 1; j <= count; j++) {
                            String part = m.group(j);
                            sb.append(part);
                            sb.append("-");
                        }
                        System.out.println(patternArray[i]
                                + "--- match value: " + sb);
                    } else {
                        System.out.println(patternArray[i] + " not match");
                    }
                }
                System.out
                        .println("---------------------分割线------------------------");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String[] TYPE = { "kyye",// 可用余额
            "hfye",// 话费余额
            "thyl",// 通话余量
            "dqhf",// 当前话费
            "tcyl",// 套餐余量
            "dxyl",// 短信余量
    };

    private static final String[] PATTERN_TYPE = {
    /* 话费余额 */
    "kyye", // 可用余额
            "qf",// 欠费
            /* 当月话费 */
            "dyhf",//
            /* 套餐余量 */
            "thzh",// 总通话时间
            "thsy",// 剩余通话时间
            "thyy",// 已使用通话时间
            "dxzh",// 短信总量
            "dxsy",// 剩余短信
            "dxyy",// 已使用短信
            /* 短信余量 */
            "dxzh",// 短信总量
            "dxsy",// 剩余短信
            "dxyy",// 已使用短信
            /* 通话余量 */
            "thzh",// 总通话时间
            "thsy",// 剩余通话时间
            "thyy"// 已使用通话时间
    };

    private static final String[] PATTERN_NAME = {
    /* 可用余额 */
    "可用余额", "欠费", "当月话费",
    /* 套餐余量 */
    "总通话时间", "剩余通话时间", "已使用通话时间", "短信总量", "剩余短信", "已使用短信",
    /* 短信余量 */
    "短信总量", "剩余短信", "已使用短信",
    /* 通话余量 */
    "总通话时间", "剩余通话时间", "已使用通话时间" };

    private static void parse(Matcher matcher) {
        for (int i = 0; i < PATTERN_TYPE.length; i++) {
            try {
                String r = matcher.group(PATTERN_TYPE[i]);
                if (r == null || r.equals("")) {
                    System.out.println("null +++++++++++++");
                } else {
                    System.out.println(PATTERN_NAME[i] + "--********");
                    // break;
                }
            } catch (Exception e) {
            }
        }
    }
}
