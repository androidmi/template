package com.dianxinos.template.parse;

public abstract class AbstractParsebase implements Parser {
    protected String mSmsBody;
    protected String mTemplate;
    protected String mRegex;

    public AbstractParsebase(String smsBody, String template, String regex) {
        mSmsBody = smsBody;
        mTemplate = template;
        mRegex = regex;
    }

}
