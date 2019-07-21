package com.example.tianqijizhang.tianqijizhang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tianqijizhang.tianqijizhang.R;

/**
 * Created by wuxinxi on 2017/7/14.
 */

public class blankFrangment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.blank_frangment,null);
        return view;
    }
}
