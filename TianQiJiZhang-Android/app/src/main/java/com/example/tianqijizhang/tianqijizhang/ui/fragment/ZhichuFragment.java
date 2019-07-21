package com.example.tianqijizhang.tianqijizhang.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tianqijizhang.tianqijizhang.R;

/**
 * Created by wuxinxi on 2017/6/30.
 */

public class ZhichuFragment extends Fragment implements View.OnClickListener {

    Button canyin, gouwu, shenhuo, jiaotong, shucai,
            shuiguo, lingshi, yundong, yule, yiliao, yifu, qita;

    TextView Title;

    private Handler handler;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhichu_fragment, null);
        Title = (TextView) getActivity().findViewById(R.id.itemDetails_title);

        canyin = (Button) view.findViewById(R.id.canyin_button);
        gouwu = (Button) view.findViewById(R.id.gouwu_button);
        shenhuo = (Button) view.findViewById(R.id.shenghuo_button);
        jiaotong = (Button) view.findViewById(R.id.jiaotong_button);
        shucai = (Button) view.findViewById(R.id.shucai_button);
        shuiguo = (Button) view.findViewById(R.id.shuiguo_button);
        lingshi = (Button) view.findViewById(R.id.lingshi_button);
        yundong = (Button) view.findViewById(R.id.yundong_button);
        yule = (Button) view.findViewById(R.id.yule_button);
        yiliao = (Button) view.findViewById(R.id.yiliao_button);
        yifu = (Button) view.findViewById(R.id.yifu_button);
        qita = (Button) view.findViewById(R.id.qita_button);
        canyin.setOnClickListener(this);
        gouwu.setOnClickListener(this);
        shenhuo.setOnClickListener(this);
        jiaotong.setOnClickListener(this);
        shucai.setOnClickListener(this);
        shuiguo.setOnClickListener(this);
        lingshi.setOnClickListener(this);
        yundong.setOnClickListener(this);
        yule.setOnClickListener(this);
        yiliao.setOnClickListener(this);
        yifu.setOnClickListener(this);
        qita.setOnClickListener(this);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Title.setText("餐饮");
                        break;
                    case 2:
                        Title.setText("购物");
                        break;
                    case 3:
                        Title.setText("生活");
                        break;
                    case 4:
                        Title.setText("交通");
                        break;
                    case 5:
                        Title.setText("蔬菜");
                        break;
                    case 6:
                        Title.setText("水果");
                        break;
                    case 7:
                        Title.setText("零食");
                        break;
                    case 8:
                        Title.setText("运动");
                        break;
                    case 9:
                        Title.setText("娱乐");
                        break;
                    case 10:
                        Title.setText("医疗");
                        break;
                    case 11:
                        Title.setText("衣服");
                        break;
                    case 12:
                        Title.setText("其他");
                        break;


                    default:
                        break;
                }

            }
        };
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.canyin_button:
                setWhat(1);
                initImage();
                canyin.setBackgroundResource(R.drawable.canyin);
                replaceFragment(1, -1);
                break;

            case R.id.gouwu_button:
                setWhat(2);
                initImage();
                gouwu.setBackgroundResource(R.drawable.gouwu);
                replaceFragment(2, -1);
                break;

            case R.id.shenghuo_button:
                setWhat(3);
                initImage();
                shenhuo.setBackgroundResource(R.drawable.shenghuo);
                replaceFragment(3, -1);
                break;

            case R.id.jiaotong_button:
                setWhat(4);
                initImage();
                jiaotong.setBackgroundResource(R.drawable.jiaotong);
                replaceFragment(4, -1);
                break;

            case R.id.shucai_button:
                setWhat(5);
                initImage();
                shucai.setBackgroundResource(R.drawable.shucai);
                replaceFragment(5, -1);
                break;

            case R.id.shuiguo_button:
                setWhat(6);
                initImage();
                shuiguo.setBackgroundResource(R.drawable.shuiguo);
                replaceFragment(6, -1);
                break;

            case R.id.lingshi_button:
                setWhat(7);
                initImage();
                lingshi.setBackgroundResource(R.drawable.lingshi);
                replaceFragment(7, -1);
                break;

            case R.id.yundong_button:
                setWhat(8);
                initImage();
                yundong.setBackgroundResource(R.drawable.yundong);
                replaceFragment(8, -1);
                break;

            case R.id.yule_button:
                setWhat(9);
                initImage();
                yule.setBackgroundResource(R.drawable.yule);
                replaceFragment(9, -1);
                break;

            case R.id.yiliao_button:
                setWhat(10);
                initImage();
                yiliao.setBackgroundResource(R.drawable.yiliao);
                replaceFragment(10, -1);
                break;

            case R.id.yifu_button:
                setWhat(11);
                initImage();
                yifu.setBackgroundResource(R.drawable.yifu);
                replaceFragment(11, -1);
                break;

            case R.id.qita_button:
                setWhat(12);
                initImage();
                qita.setBackgroundResource(R.drawable.qita);
                replaceFragment(12, -1);
                break;
            default:
                break;

        }

    }

    private void replaceFragment(int data, int pm) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        itemCard itemCard = com.example.tianqijizhang.tianqijizhang.ui.fragment.itemCard.getinstance(data, pm);
        transaction.replace(R.id.itemDetails_fragment, itemCard);
        transaction.commit();
    }

    private void initImage() {
        canyin.setBackgroundResource(R.drawable.canyin_unselict);
        gouwu.setBackgroundResource(R.drawable.gouwu_unselict);
        shenhuo.setBackgroundResource(R.drawable.shenghuo_unselict);
        jiaotong.setBackgroundResource(R.drawable.jiaotong_unselict);
        shucai.setBackgroundResource(R.drawable.shucai_unselict);
        shuiguo.setBackgroundResource(R.drawable.shuiguo_unselict);
        lingshi.setBackgroundResource(R.drawable.lingshi_selict);
        yundong.setBackgroundResource(R.drawable.yundong_unselict);
        yule.setBackgroundResource(R.drawable.yule_unselict);
        yiliao.setBackgroundResource(R.drawable.yiliao_unselict);
        yifu.setBackgroundResource(R.drawable.yifu_unselict);
        qita.setBackgroundResource(R.drawable.qita_unselict);
    }

    private void setWhat(final int num) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = num;
                handler.sendMessage(message);

            }
        }).start();

    }

}
