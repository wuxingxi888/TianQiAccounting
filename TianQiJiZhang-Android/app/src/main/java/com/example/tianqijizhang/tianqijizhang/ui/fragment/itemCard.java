package com.example.tianqijizhang.tianqijizhang.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.tianqijizhang.tianqijizhang.R;
import com.example.tianqijizhang.tianqijizhang.bean.ResponseLogin;
import com.example.tianqijizhang.tianqijizhang.config.Constant;
import com.example.tianqijizhang.tianqijizhang.config.config;
import com.example.tianqijizhang.tianqijizhang.ui.register.register;
import com.example.tianqijizhang.tianqijizhang.utils.JsonUtils;
import com.example.tianqijizhang.tianqijizhang.utils.PreferencesUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wuxinxi on 2017/7/3.
 */

public class itemCard extends Fragment implements View.OnClickListener{

    public View view;
    public TextView username;
    public EditText title;
    public ImageView image;
    public EditText content;
    public EditText money;
    public Button ok;
    public String user;
    int imageID;
    int pm;
    private ResponseLogin login;

    // 定义显示时间控件
    private Calendar calendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;

    // 传递的参数不会丢失
    public static itemCard getinstance(int data, int pm) {
        itemCard itemCard = new itemCard();
        Bundle bundle = new Bundle();
        //将需要传递的字符串以键值对的形式传入bundle
        bundle.putInt("data", data);
        bundle.putInt("pm", pm);
        itemCard.setArguments(bundle);
        return itemCard;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.item_card, null);
        initUI();
        initDta();
        initLisListener();
        return view;
    }

    public void initUI(){
    username = (TextView) view.findViewById(R.id.user_id);
    title = (EditText) view.findViewById(R.id.shijian_edit_card);
    image = (ImageView) view.findViewById(R.id.item_image);//备注  传递image
    content = (EditText) view.findViewById(R.id.beizhu_edit_card);
    money = (EditText) view.findViewById(R.id.jine_edit_card);
    ok = (Button) view.findViewById(R.id.quedin_button);
}

    public void initDta(){
    title.setInputType(InputType.TYPE_NULL); //点击editveiw时，不弹出输入键盘

    user = PreferencesUtils.getString(getContext(), "username");

    //获取水偏点击的图片编号
    imageID = getArguments().getInt("data");
    //获取pm值
    pm = getArguments().getInt("pm");

}

    public void initLisListener(){
    ok.setOnClickListener(this);
    title.setOnClickListener(this);
}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.quedin_button:

                        if ("".equals(money.getText().toString().trim())) {
                            Toast.makeText(getActivity(), "请输入金额", Toast.LENGTH_SHORT).show();
                        } else if ("".equals(content.getText().toString().trim())) {
                            Toast.makeText(getActivity(), "请输入消费详情", Toast.LENGTH_SHORT).show();
                        } else if ("".equals(title.getText().toString().trim())) {
                            Toast.makeText(getActivity(), "请输入消费日期", Toast.LENGTH_SHORT).show();
                        } else {
                            postData();
                        }
                 break;
            case R.id.shijian_edit_card:

                showDialog();
                break;
                default:
                    break;

        }

    }

    private void showDialog() {

        calendar = Calendar.getInstance();
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int month, int day) {
                // TODO Auto-generated method stub
                mYear = year;
                mMonth = month;
                mDay = day;
                // 更新EditText控件日期 小于10加0
                title.setText(new StringBuilder()
                        .append(mYear)
                        .append("-")
                        .append((mMonth + 1) < 10 ? "0"
                                + (mMonth + 1) : (mMonth + 1))
                        .append("-")
                        .append((mDay < 10) ? "0" + mDay : mDay));
            }
        }, calendar.get(Calendar.YEAR), calendar
                .get(Calendar.MONTH), calendar
                .get(Calendar.DAY_OF_MONTH)).show();

    }

    //提交数据到服务器的逻辑代码
    private void postData() {

        /*判断网络是否连接*/
        if(!NetworkUtils.isAvailableByPing()){
            ToastUtils.showShort("网络未连接...");
            return;
        }

        String str_time = title.getText().toString().trim();
        String str_content = content.getText().toString().trim();
        String str_money = money.getText().toString().trim();
        System.out.println("time"+str_time);
        System.out.println("content"+str_content);
        System.out.println("money"+str_money);
        System.out.println("imageid"+imageID);
        System.out.println("pm"+pm);

        OkHttpUtils
                .get()
                .url(config.ADDRECORD + "?username=" + user  + "&time=" + str_time
                        + "&imageId=" + imageID + "&content=" + str_content + "&money=" +str_money + "&pm="+pm)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        ToastUtils.showShort("系统错误："+e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        System.out.println("haha"+response);

                        login = new ResponseLogin();
                        login = JsonUtils.fromJson(response, ResponseLogin.class);
                        System.out.println("++++++++"+login.getStatus());

                        if (login.getStatus().equals(Constant.SUCCESS)) {
                            if (login.getMsg().equals(Constant.SUCCESS_ADDRECORD_OK)) {
                                ToastUtils.showShort("添加记录成功");
                                money.setText("");
                                content.setText("");
                                title.setText("");
                                return;
                            }
                        }
                    }
                });
    }
}
