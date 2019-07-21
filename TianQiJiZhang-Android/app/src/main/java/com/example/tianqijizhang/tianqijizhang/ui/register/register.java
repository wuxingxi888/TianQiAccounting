package com.example.tianqijizhang.tianqijizhang.ui.register;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.tianqijizhang.tianqijizhang.R;
import com.example.tianqijizhang.tianqijizhang.bean.ResponseLogin;
import com.example.tianqijizhang.tianqijizhang.config.Constant;
import com.example.tianqijizhang.tianqijizhang.config.config;
import com.example.tianqijizhang.tianqijizhang.ui.base.ActivityCollector;
import com.example.tianqijizhang.tianqijizhang.ui.base.MyBaseActivity;
import com.example.tianqijizhang.tianqijizhang.ui.home.homeActivity;
import com.example.tianqijizhang.tianqijizhang.ui.login.login;
import com.example.tianqijizhang.tianqijizhang.utils.JsonUtils;
import com.example.tianqijizhang.tianqijizhang.widget.LoadingDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.BreakIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;

public class register extends MyBaseActivity implements View.OnClickListener {

    private EditText registerUserName;
    private EditText registerPassword;
    private EditText registerpassword1;
    private EditText registerEmail;
    private Toolbar toolbar;
    private Button backButton;
    private Button register;
    private String str_username;
    private String str_password;
    private String str_password1;
    private String str_Email;
    private ResponseLogin login;
    private LoadingDialog loadingDialog;

    @Override
    protected void initUI() {
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void initData() {
        toolbar = (Toolbar) findViewById(R.id.zhuce_toolbar);
        registerUserName = (EditText) findViewById(R.id.register_user_name);
        registerPassword = (EditText) findViewById(R.id.register_password);
        registerpassword1 = (EditText) findViewById(R.id.register_password1);
        registerEmail = (EditText) findViewById(R.id.register_email);
        backButton = (Button) findViewById(R.id.zhucefanhui_button);
        register = (Button) findViewById(R.id.register_btn);
    }

    @Override
    protected void initListener() {
        backButton.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhucefanhui_button:
                register.this.finish();
                break;
            case R.id.register_btn:
                checkUser();
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
        str_username = registerUserName.getText().toString();
        str_password = registerPassword.getText().toString();
        str_password1 = registerpassword1.getText().toString();
        str_Email = registerEmail.getText().toString();

        if (StringUtils.isSpace(str_username)) {
            ToastUtils.showShort("用户名不能为空");
            return;
        } else {
            String limitEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

            Pattern pattern = Pattern.compile(limitEx);
            Matcher m = pattern.matcher(str_username);

            if (m.find()) {
                ToastUtils.showShort("用户名不允许输入特殊符号！");
                return;
            }
        }

        if (StringUtils.isSpace(str_password)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }

        if (!str_password.equals(str_password1)) {
            ToastUtils.showShort("两次密码输入不一致");
            return;
        }
        if (StringUtils.isSpace(str_Email)) {
            ToastUtils.showShort("Email不能为空");
            return;
        } else {
            if (!RegexUtils.isEmail(str_Email)) {
                ToastUtils.showShort("请输入正确的E_mail");
                return;
            }
        }

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.show();

        OkHttpUtils
                .get()
                .url(config.REGISTER + "?username=" + str_username + "&" + "password=" + str_password
                        + "email=" + str_Email)
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

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (loadingDialog != null) {
                                        loadingDialog.dismiss();
                                    }
                                }
                            });
                            register.this.finish();
                            ToastUtils.showShort("注册成功，请登录");
                        } else {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (loadingDialog != null) {
                                        loadingDialog.dismiss();
                                    }
                                }
                            });
                            if (login.getMsg().equals(Constant.ERROR_USER_EXIST)) {
                                ToastUtils.showShort("用户名已被注册");
                                return;
                            }
                        }
                    }
                });
    }
}
