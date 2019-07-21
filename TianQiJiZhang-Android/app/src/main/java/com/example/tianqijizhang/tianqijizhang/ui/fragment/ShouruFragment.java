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

public class ShouruFragment extends Fragment implements View.OnClickListener {

    Button gongzi, jianzhi, licai, lijin, qita;
    TextView title;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shouru_fragment, null);

        title = (TextView) getActivity().findViewById(R.id.itemDetails_title);

        gongzi = (Button) view.findViewById(R.id.gongzi_button);
        jianzhi = (Button) view.findViewById(R.id.jianzhi_button);
        licai = (Button) view.findViewById(R.id.licai_button);
        lijin = (Button) view.findViewById(R.id.lijin_button);
        qita = (Button) view.findViewById(R.id.qitashouru_button);

        gongzi.setOnClickListener(this);
        jianzhi.setOnClickListener(this);
        licai.setOnClickListener(this);
        lijin.setOnClickListener(this);
        qita.setOnClickListener(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        title.setText("工资");
                        break;
                    case 2:
                        title.setText("兼职");
                        break;
                    case 3:
                        title.setText("理财");
                        break;
                    case 4:
                        title.setText("礼金");
                        break;
                    case 5:
                        title.setText("其他");
                        break;
                }
            }
        };


        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gongzi_button:
                setWhat(1);
                initImage();
                gongzi.setBackgroundResource(R.drawable.gongzi);
                replaceFragment(13, 1);
                break;

            case R.id.jianzhi_button:
                setWhat(2);
                initImage();
                jianzhi.setBackgroundResource(R.drawable.jianzhi);
                replaceFragment(14, 1);
                break;

            case R.id.licai_button:
                setWhat(3);
                initImage();
                licai.setBackgroundResource(R.drawable.licai);
                replaceFragment(15, 1);
                break;

            case R.id.lijin_button:
                setWhat(4);
                initImage();
                lijin.setBackgroundResource(R.drawable.lijin);
                replaceFragment(16, 1);
                break;

            case R.id.qitashouru_button:
                setWhat(5);
                initImage();
                qita.setBackgroundResource(R.drawable.qitashouru);
                replaceFragment(17, 1);
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
        gongzi.setBackgroundResource(R.drawable.gongzi_unselict);
        jianzhi.setBackgroundResource(R.drawable.jianzhi_unselict);
        licai.setBackgroundResource(R.drawable.licai_unselict);
        lijin.setBackgroundResource(R.drawable.lijin_unselict);
        qita.setBackgroundResource(R.drawable.qitashouru_unselict);
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
