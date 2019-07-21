package com.example.tianqijizhang.tianqijizhang.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.tianqijizhang.tianqijizhang.R;


/**
 */
public class LoadingDialog extends AnimDialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    /**
     * 初始化dialog布局
     */
    @Override
    public void initView() {

        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_alert_loading, null);
        setCancelable(false);
    }

    /**
     * 显示对话框
     */
    public AnimDialog show() {

        if (dialog != null) {

            dialog.show();
            //设置动画
            if (window == null) {
                window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                window.setContentView(dialogView);
            }
            //播放动画
            dialogView.startAnimation(animIn);
        }

        return this;
    }

}
