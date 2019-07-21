package com.example.tianqijizhang.tianqijizhang.ui.login;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.tianqijizhang.tianqijizhang.R;
import com.example.tianqijizhang.tianqijizhang.bean.ResponseLogin;
import com.example.tianqijizhang.tianqijizhang.config.Constant;
import com.example.tianqijizhang.tianqijizhang.config.config;
import com.example.tianqijizhang.tianqijizhang.ui.base.ActivityCollector;
import com.example.tianqijizhang.tianqijizhang.ui.base.MyBaseActivity;
import com.example.tianqijizhang.tianqijizhang.ui.home.homeActivity;
import com.example.tianqijizhang.tianqijizhang.ui.register.register;
import com.example.tianqijizhang.tianqijizhang.utils.JsonUtils;
import com.example.tianqijizhang.tianqijizhang.utils.PreferencesUtils;
import com.example.tianqijizhang.tianqijizhang.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class login extends MyBaseActivity implements View.OnClickListener {

    private AutoCompleteTextView login_username;
    private EditText login_password;
    private Button login_button;
    private TextView register_button;
    private String str_username;
    private String str_password;
    private ResponseLogin login;
    private LoadingDialog loadingDialog;
    private LinearLayout loginView;



    @Override
    protected void initUI() {
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void initData() {
        login_username = (AutoCompleteTextView) findViewById(R.id.user_name);
        login_password = (EditText) findViewById(R.id.password);
        login_button = (Button) findViewById(R.id.login_button);
        register_button = (TextView) findViewById(R.id.register_button);
        loginView = (LinearLayout) findViewById(R.id.login_view );

    }

    @Override
    protected void initListener() {
        login_button.setOnClickListener(this);
        register_button.setOnClickListener(this);
        autoScrollView(loginView, login_button);//弹出软键盘时滚动视图
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login_button:
                checkUser();

                break;
            case R.id.register_button:
                openActivity(register.class);
                break;

        }
    }

    //点击屏幕 关闭输入弹出框
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        return super.onTouchEvent(event);
    }

    /*用户名密码校验*/
    public void checkUser() {

        /*判断网络是否连接*/
        if(!NetworkUtils.isAvailableByPing()){
            ToastUtils.showShort("网络未连接...");
            return;
        }

        str_username = login_username.getText().toString();
        str_password = login_password.getText().toString();

        if (StringUtils.isSpace(str_username)) {
            Toast.makeText(login.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isSpace(str_password)) {
            Toast.makeText(login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();

        OkHttpUtils
                .get()
                .url(config.LOGIN + "?username=" + str_username + "&" + "password=" + str_password)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                            }
                        });
                        ToastUtils.showShort("系统错误："+e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {

                        login = new ResponseLogin();
                        login = JsonUtils.fromJson(response, ResponseLogin.class);

                        if (login.getStatus().equals(Constant.SUCCESS)) {

                            PreferencesUtils.putString(login.this,"username",str_username); //保存用户名

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (loadingDialog != null) {
                                        loadingDialog.dismiss();
                                    }
                                }
                            });

                            openActivity(homeActivity.class);
                            Toast.makeText(login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                            login.this.finish();
                        }else{

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (loadingDialog != null) {
                                        loadingDialog.dismiss();
                                    }
                                }
                            });

                            if(login.getMsg().equals(Constant.ERROR_SYSTEM)){
                                Toast.makeText(login.this,"系统错误",Toast.LENGTH_SHORT).show();
                                return;
                            }if(login.getMsg().equals(Constant.ERROR_USERNAME)){
                                Toast.makeText(login.this,"用户不存在",Toast.LENGTH_SHORT).show();
                                return;
                            }if(login.getMsg().equals(Constant.ERROR_PASSWORD)){
                                Toast.makeText(login.this,"密码错误",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                    }
                });
    }


}
