package com.example.tianqijizhang.tianqijizhang.ui.addDetails;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tianqijizhang.tianqijizhang.R;
import com.example.tianqijizhang.tianqijizhang.ui.base.ActivityCollector;
import com.example.tianqijizhang.tianqijizhang.ui.base.MyBaseActivity;
import com.example.tianqijizhang.tianqijizhang.ui.fragment.ShouruFragment;
import com.example.tianqijizhang.tianqijizhang.ui.fragment.ZhichuFragment;
import com.example.tianqijizhang.tianqijizhang.ui.fragment.blankFrangment;

import java.util.ArrayList;
import java.util.List;

public class addDetails extends MyBaseActivity implements View.OnClickListener {

    private TextView textView;
    private Toolbar toolbar;
    private Button itemDetailsFanhui;
    private Button zhichuButton;
    private Button shouruButton;

    private List<Button> btnList = new ArrayList<Button>();
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_add_details);
        ActivityCollector.addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.itemDetalils_toolbar);
        textView = (TextView) findViewById(R.id.itemDetails_title);
        itemDetailsFanhui = (Button) findViewById(R.id.itemDetailsFanhui_button);
        zhichuButton = (Button) findViewById(R.id.zhichu_button);
        shouruButton = (Button) findViewById(R.id.shouru_button);

    }

    @Override
    protected void initData() {
        toolbar.setTitle("");
        textView.setText("添加项目");
        setSupportActionBar(toolbar);
        btnList.add(zhichuButton);
        btnList.add(shouruButton);

        // 进入系统默认为支出界面
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        setBackgroundColorById(R.id.zhichu_button);
        ft.replace(R.id.item_fragment,new ZhichuFragment());
        ft.commit();
    }

    @Override
    protected void initListener() {
        itemDetailsFanhui.setOnClickListener(this);
        zhichuButton.setOnClickListener(this);
        shouruButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (v.getId()){
            case R.id.itemDetailsFanhui_button:
                onBack(v);
                break;
            case R.id.zhichu_button:
                setBackgroundColorById(R.id.zhichu_button);

                ft.replace(R.id.item_fragment,new ZhichuFragment());
                initItemCard();
                break;

            case R.id.shouru_button:
                setBackgroundColorById(R.id.shouru_button);

                ft.replace(R.id.item_fragment,new ShouruFragment());
                initItemCard();
                break;
            default:
                break;
        }
        ft.commit();

    }

    //初始化itemcard界面碎片
    private void initItemCard(){

        ft.replace(R.id.itemDetails_fragment,new blankFrangment());
    }

    private void setBackgroundColorById(int ButtonId) {
        for (Button btn : btnList) {
            if (btn.getId() == ButtonId){
                btn.setBackgroundResource(R.drawable.shouru2);
            }else {
                btn.setBackgroundResource(R.drawable.shouru1);
            }
        }
    }
}
