package com.example.tianqijizhang.tianqijizhang.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout.LayoutParams;

import com.example.tianqijizhang.tianqijizhang.R;
import com.example.tianqijizhang.tianqijizhang.application.MyApplication;

/**
 * 带动画的dialog
 */
public abstract class AnimDialog {
    //dialog出现动画
    public AnimationSet animIn;
    //dialog对象
    public Dialog dialog;
    //dialog布局
    public View dialogView;
    //上下文
    public Context context;
    //
    public Window window;
    //确定监听
    public DialogListener listener;

    public AnimDialog(Context context) {
        this.context = context;
        //初始化动画
        animIn = (AnimationSet) OptAnimationLoader.loadAnimation(context, R.anim.modal_in);
        //初始化dialog
        dialog = new Dialog(context, R.style.dialog_with_black_alpha_background);
        //初始化dialog布局
        initView();
    }

    //初始化布局
    public abstract void initView();

    /**
     * 消失对话框
     */
    public void dismiss() {
        //取消动画
        if (dialogView != null) {
            dialogView.clearAnimation();
        }
        //对话框消失
        if (dialog != null) {
            dialog.dismiss();
        }
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
                window.setLayout(MyApplication.screenWith / 4 * 3, LayoutParams.WRAP_CONTENT);
                window.setContentView(dialogView);
            }
            //播放动画
            dialogView.startAnimation(animIn);
        }

        return this;
    }

    /**
     * 定义事件监听接口
     *
     * @author Administrator
     */
    public interface DialogListener {
        //确定事件处理
        void yes();
    }

    /**
     * 设置事件监听
     */
    public AnimDialog addListener(DialogListener listener) {
        this.listener = listener;
        return AnimDialog.this;
    }

    public Dialog get() {
        return dialog;
    }

    public void setCancelable(boolean flag) {
        if (dialog == null) {
            return;
        }

        dialog.setCancelable(flag);
    }
}
