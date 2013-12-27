package com.dianxinos.template;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
    private static final String TAG = "SMSReceiver";
    public static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_SMS_RECEIVED.equals(intent.getAction())) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // 取pdus内容,转换为Object[]
                Object[] pdus = (Object[]) bundle.get("pdus");
                if (pdus == null) {
                    return;
                }

                // 解析短信
                SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++) {
                    byte[] pdu = (byte[]) pdus[i];
                    messages[i] = SmsMessage.createFromPdu(pdu);
                }
                StringBuilder smsContent = new StringBuilder();
                String number = "";
                // 解析完内容后分析具体参数
                for (SmsMessage msg : messages) {
                    // 获取短信内容
                    try {
                        number = msg.getDisplayOriginatingAddress();
                        smsContent.append(msg.getDisplayMessageBody());
                    } catch (NullPointerException e) {
                            Log.i(TAG, "getDisplayMessageBody nullPointerException");
                    }
                }
                    Log.i(TAG, "receive sms sucessful : " + smsContent.toString());
                abortBroadcast();
            }
        }
    }
}
