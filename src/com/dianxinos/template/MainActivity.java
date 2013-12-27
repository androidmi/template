package com.dianxinos.template;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dianxinos.template.parse.TcParser;
import com.dianxinos.template.parse.model.DataInfo;
import com.dianxinos.template.parse.model.TemplateInfo;
import com.dianxinos.template.parse.model.ViewItemInfo;

public class MainActivity extends Activity implements OnClickListener {
    private LinearLayout mLayoyut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        mLayoyut = (LinearLayout) findViewById(R.id.relat_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        String data = FileUtils.getAssetFileContent(this, "tc_data");
        String sms = FileUtils.getAssetFileContent(this, "tc_sms");
        try {
            TemplateInfo info = TemplateInfo.createSingle(new JSONObject(data));
            ArrayList<TemplateInfo> infoList = TemplateInfo.createArray(new JSONObject(data));
            android.widget.LinearLayout.LayoutParams linearParams = new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            Log.i("hello", "size:" + infoList.size());
            for (int i = 0; i < infoList.size(); i++) {
                mLayoyut.addView(getItemView(sms, infoList.get(i)), i, linearParams);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private View getTCView(String sms, TemplateInfo info) {
        TcParser parser = new TcParser(sms, info.tmplate, info.regex);
        parser.parseSms();
        parser.fillTemplate();
        ViewItemInfo viewInfo = parser.getItemInfo();

        LinearLayout linearLayout = new LinearLayout(this);
        android.widget.LinearLayout.LayoutParams linearParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleView = new TextView(this);
        LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        titleView.setText(info.title);
        titleView.setBackgroundColor(Color.parseColor("#cdcdcd"));
        linearLayout.addView(titleView, titleParams);

        TextView textView = new TextView(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        Log.i("hello", viewInfo.content);
        textView.setText(Html.fromHtml(viewInfo.content));
        linearLayout.addView(textView, params);

        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        DataInfo dataInfo = parser.getDataInfo();
        Log.i("hello", "totle:" + dataInfo.getZh());
        Log.i("hello", "yy:" + dataInfo.getYy());
        progressBar.setMax(dataInfo.getZh());
        progressBar.setProgress(dataInfo.getYy());
        progressBar
                .setIndeterminateDrawable(getResources()
                        .getDrawable(
                                R.drawable.progress_bar_layerlist_level1));
        android.widget.LinearLayout.LayoutParams progressBarParams = new android.widget.LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        linearLayout.addView(progressBar, progressBarParams);
        return linearLayout;
    }

    /**
     * 获得条目view
     * 
     * @param sms
     * @param info
     * @return
     */
    private View getItemView(String sms, TemplateInfo info) {
        // 有子項目
        if (info.mSubInfos != null) {
            LinearLayout linearLayout = new LinearLayout(this);
            android.widget.LinearLayout.LayoutParams linearParams = new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(linearParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView titleView = new TextView(this);
            LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            titleView.setText(info.title);
            titleView.setBackgroundColor(Color.parseColor("#cdcdcd"));
            linearLayout.addView(titleView, titleParams);

            ArrayList<TemplateInfo> infoList = info.mSubInfos;
            for (TemplateInfo templateInfo : infoList) {
                TcParser temParser = new TcParser(sms, templateInfo.tmplate, templateInfo.regex);
                temParser.parseSms();
                temParser.fillTemplate();
                DataInfo dataInfo = temParser.getDataInfo();
                ViewItemInfo viewInfo = temParser.getItemInfo();
                int zh = dataInfo.getZh();
                int yy = dataInfo.getYy();
                int sy = dataInfo.getSy();
                // 如果总和是0,一定只有剩余或已用一个数据
                if (zh == 0) {
                    TextView textView = new TextView(this);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    if (yy != 0) {
                        textView.setText("已用：" + yy);
                    } else if (sy != 0) {
                        textView.setText("剩余：" + sy);
                    } else {
                        textView.setText("沒有该业务");
                    }
                    linearLayout.addView(textView, params);
                } else {
                    TextView textView = new TextView(this);
                    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT);
                    textView.setText(Html.fromHtml(viewInfo.content));
                    linearLayout.addView(textView, params);

                    ProgressBar progressBar = new ProgressBar(this, null,
                            android.R.attr.progressBarStyleHorizontal);
                    progressBar.setMax(dataInfo.getZh());
                    progressBar.setProgress(dataInfo.getYy());
                    progressBar.setIndeterminateDrawable(getResources().getDrawable(
                            R.drawable.progress_bar_layerlist_level1));
                    android.widget.LinearLayout.LayoutParams progressBarParams = new android.widget.LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                    linearLayout.addView(progressBar, progressBarParams);
                }
            }
            return linearLayout;
        } else {
            TcParser temParser = new TcParser(sms, info.tmplate, info.regex);
            temParser.parseSms();
            temParser.fillTemplate();
            DataInfo dataInfo = temParser.getDataInfo();
            ViewItemInfo viewInfo = temParser.getItemInfo();
            int zh = dataInfo.getZh();
            int yy = dataInfo.getYy();
            int sy = dataInfo.getSy();

            LinearLayout linearLayout = new LinearLayout(this);
            android.widget.LinearLayout.LayoutParams linearParams = new android.widget.LinearLayout.LayoutParams(
                    android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                    android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(linearParams);
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            TextView titleView = new TextView(this);
            LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            titleView.setText(info.title);
            titleView.setBackgroundColor(Color.parseColor("#cdcdcd"));
            linearLayout.addView(titleView, titleParams);

            if (zh == 0) {
                TextView textView = new TextView(this);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                if (yy != 0) {
                    textView.setText("已用：" + yy);
                } else if (sy != 0) {
                    textView.setText("剩余：" + sy);
                } else {
                    textView.setText("沒有该业务");
                }
                linearLayout.addView(textView, params);
            } else {
                TextView textView = new TextView(this);
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                textView.setText(Html.fromHtml(viewInfo.content));
                linearLayout.addView(textView, params);

                ProgressBar progressBar = new ProgressBar(this, null,
                        android.R.attr.progressBarStyleHorizontal);
                progressBar.setMax(dataInfo.getZh());
                progressBar.setProgress(dataInfo.getYy());
                progressBar.setIndeterminateDrawable(getResources().getDrawable(
                        R.drawable.progress_bar_layerlist_level1));
                android.widget.LinearLayout.LayoutParams progressBarParams = new android.widget.LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                linearLayout.addView(progressBar, progressBarParams);
            }
            return linearLayout;
        }
    }
    
    public View getSubView(DataInfo dataInfo) {
        int zh = dataInfo.getZh();
        int yy = dataInfo.getYy();
        int sy = dataInfo.getSy();
        LinearLayout linearLayout = new LinearLayout(this);
        android.widget.LinearLayout.LayoutParams linearParams = new android.widget.LinearLayout.LayoutParams(
                android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(linearParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        if (zh == 0) {
            TextView textView = new TextView(this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            if (yy != 0) {
                textView.setText("已用：" + yy);
            } else if (sy != 0) {
                textView.setText("剩余：" + sy);
            } else {
                textView.setText("沒有该业务");
            }
            linearLayout.addView(textView, params);
        } else {
            TextView textView = new TextView(this);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            textView.setText("总和："+zh);
            linearLayout.addView(textView, params);

            ProgressBar progressBar = new ProgressBar(this, null,
                    android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(dataInfo.getZh());
            progressBar.setProgress(dataInfo.getYy());
            progressBar.setIndeterminateDrawable(getResources().getDrawable(
                    R.drawable.progress_bar_layerlist_level1));
            android.widget.LinearLayout.LayoutParams progressBarParams = new android.widget.LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            linearLayout.addView(progressBar, progressBarParams);
        }
        return linearLayout;
    }
}
