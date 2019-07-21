package com.example.tianqijizhang.tianqijizhang.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.example.tianqijizhang.tianqijizhang.R;
import com.example.tianqijizhang.tianqijizhang.application.MyApplication;
import com.example.tianqijizhang.tianqijizhang.bean.ResponseLogin;
import com.example.tianqijizhang.tianqijizhang.config.Constant;
import com.example.tianqijizhang.tianqijizhang.config.config;
import com.example.tianqijizhang.tianqijizhang.ui.addDetails.addDetails;
import com.example.tianqijizhang.tianqijizhang.ui.base.ActivityCollector;
import com.example.tianqijizhang.tianqijizhang.utils.JsonUtils;
import com.example.tianqijizhang.tianqijizhang.utils.PreferencesUtils;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class homeActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView textView;
    private RelativeLayout ItemDetails;
    //支出和收入的textview
    private TextView ZhichuSum;
    private TextView shouruSum;
    private TextView userName;
    private Button menuB;
    public PopupWindow popup;
    public ListView mListView;
    private MyAdapter MyAdapter;
    private SwipeRefreshLayout swipeRefresh; //下拉刷新
    private Handler handler;
    //查询删除数据删除
    TextView IetmDataID;
    private ResponseLogin login;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private List<String> ID = new ArrayList<>();

    int income;//收入的金額
    int expenditure ;//消費的金額

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        initData();
        initListener();


        /**
         *  Handler运行在主线程中(UI线程中)，  它与子线程可以通过Message对象来传递数据
         */
        handler = new Handler() {
            @SuppressLint("ResourceType")
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    MyAdapter = new MyAdapter(homeActivity.this, list);
                    mListView.setAdapter(MyAdapter);

                    ZhichuSum.setText(expenditure+"");
                    shouruSum.setText(income+"");

                }
                if (1 == msg.getData().getInt("delete")) {
                    ToastUtils.showShort("删除成功");
                }


            }
        };

    }


    public void initUI() {

        //将此活动添加到活动管理
        ActivityCollector.addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        ItemDetails = (RelativeLayout) findViewById(R.id.tainjiaxiangqing_button);
        ZhichuSum = (TextView) findViewById(R.id.zhichujine_sum);
        shouruSum = (TextView) findViewById(R.id.shourujine_sum);
        menuB = (Button) findViewById(R.id.menu_button);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refesh);
        mListView = (ListView) findViewById(R.id.myList);
    }


    @SuppressLint("ResourceAsColor")
    public void initData() {
        textView.setText("天启记账");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        popupDialog();
        GetUserData();
    }


    public void initListener() {
        menuB.setOnClickListener(this);
        ItemDetails.setOnClickListener(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeMenuLayout viewCache = SwipeMenuLayout.getViewCache();
                    if (null != viewCache) {
                        viewCache.smoothClose();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button:
                try {
                    //在固定位置显示 设置相对坐标
                    popup.showAsDropDown(v, 0, 60);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tainjiaxiangqing_button:
                Intent intent = new Intent(homeActivity.this, addDetails.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.clear();
        GetUserData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long exitTime = 0;
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            // 判断是否在两秒之内连续点击返回键，是则退出，否则不退出
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                // 将系统当前的时间赋值给exitTime
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void popupDialog() {
        //加载popup对应的界面布局文件
        View root = this.getLayoutInflater().inflate(R.layout.popupwindow, null);
        userName = (TextView) root.findViewById(R.id.user_id);
        userName.setText(PreferencesUtils.getString(getApplication(), "username"));

        //创建popupWindow对象
        popup = new PopupWindow(root, 400, 500, true);
        //popup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        // popup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置背景不为空
        popup.setBackgroundDrawable(new ColorDrawable(0));
        //设置显示PopuWindow之后在外面点击是否有效
        popup.setOutsideTouchable(true);
        //设置获得焦点
        popup.setFocusable(true);

        //获取popupwindow里面的退出按钮
        root.findViewById(R.id.quit_button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.getInstance().removeUser(homeActivity.this);
                ActivityCollector.finishAll();

            }
        });
    }

    private void refreshData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);//刷新两秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        ZhichuSum.setText("");
                        shouruSum.setText("");
                        GetUserData();
                        MyAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void GetUserData() {

        OkHttpUtils
                .get()
                .url(config.GETUSERDATA + "?username=" + PreferencesUtils.getString(getApplication(), "username"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        ToastUtils.showShort("系统错误：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (response.length() == 40) {
                            login = new ResponseLogin();
                            login = JsonUtils.fromJson(response, ResponseLogin.class);

                            if (login.getStatus().equals(Constant.ERROR)) {
                                if (login.getMsg().equals(Constant.NO_RECORD)) {
                                    ToastUtils.showShort("请开始记账");
                                    return;
                                }
                            }
                        }

                        parseJSONWithJSONObject(response);
                    }
                });

    }

    private void parseJSONWithJSONObject(final String jsonData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Map<String, Object> map = new HashMap<String, Object>();

                        //获取到json数据中的jsonData数组里的内容
                        String title = jsonObject.getString("time");
                        String id = jsonObject.getString("id");
                        int image = jsonObject.getInt("imageId");
                        String content = jsonObject.getString("content");
                        double money = jsonObject.getDouble("money");
                        int pm = jsonObject.getInt("pm");

                        //存入map
                        map.put("map_title", title);
                        map.put("map_id", id);

                        switch (image) {
                            case 1:
                                map.put("map_image", R.drawable.canyin);
                                break;
                            case 2:
                                map.put("map_image", R.drawable.gouwu);
                                break;
                            case 3:
                                map.put("map_image", R.drawable.shenghuo);
                                break;
                            case 4:
                                map.put("map_image", R.drawable.jiaotong);
                                break;
                            case 5:
                                map.put("map_image", R.drawable.shucai);
                                break;
                            case 6:
                                map.put("map_image", R.drawable.shuiguo);
                                break;
                            case 7:
                                map.put("map_image", R.drawable.lingshi);
                                break;
                            case 8:
                                map.put("map_image", R.drawable.yundong);
                                break;
                            case 9:
                                map.put("map_image", R.drawable.yule);
                                break;
                            case 10:
                                map.put("map_image", R.drawable.yiliao);
                                break;
                            case 11:
                                map.put("map_image", R.drawable.yifu);
                                break;
                            case 12:
                                map.put("map_image", R.drawable.qita);
                                break;
                            case 13:
                                map.put("map_image", R.drawable.gongzi);
                                break;
                            case 14:
                                map.put("map_image", R.drawable.jianzhi);
                                break;
                            case 15:
                                map.put("map_image", R.drawable.licai);
                                break;
                            case 16:
                                map.put("map_image", R.drawable.lijin);
                                break;
                            case 17:
                                map.put("map_image", R.drawable.qitashouru);
                                break;
                            default:
                                break;
                        }
                        map.put("map_content", content);
                        map.put("map_money", money);
                        map.put("pm", pm);
                        //ArrayList集合
                        list.add(map);


                        //累計支出和收入的金額
                        if(ID.contains(id)){
                            continue;
                        }else {
                            ID.add(id);
                            if(pm == 1){
                                income += money;
                            }else {
                                expenditure += money;
                            }
                        }

                    }

                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void deleteData(CharSequence id) {

        OkHttpUtils
                .get()
                .url(config.DELETEDATA + "?id=" + id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        ToastUtils.showShort("系统错误：" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Message message = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putInt("delete", jsonObject.getInt("affectedRows"));
                            message.setData(bundle);
                            handler.sendMessage(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    class MyAdapter extends BaseAdapter {

        private final List<Map<String, Object>> datas;
        private Context mContext;

        public MyAdapter(Context mContext, List<Map<String, Object>> datas) {
            this.datas = datas;
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            //在此适配器中所代表的数据集中的条目数
            if (null != datas) {
                return datas.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // Get the data item associated with the specified position in the data set.
            if (null != datas && position < getCount()) {
                return datas.get(position);// 返回在list中指定位置的数据的内容
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            //Get the row id associated with the specified position in the list.
            //获取在列表中与指定索引对应的行id
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            //如果缓存convertView为空，则需要创建View
            if (convertView == null) {
                // 使用自定义的list_items作为Layout
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_details, null);
                // 减少findView的次数
                holder = new ViewHolder();
                // 初始化布局中的元素
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.id = (TextView) convertView.findViewById(R.id.item_id);
                holder.image = (ImageView) convertView.findViewById(R.id.item_image);
                holder.content = (TextView) convertView.findViewById(R.id.item_content);
                holder.money = (TextView) convertView.findViewById(R.id.item_money);
                holder.delete = (Button) convertView.findViewById(R.id.btnDelete);

                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
                final int pos = position;

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //在ListView里，点击侧滑菜单上的选项时，如果想让擦花菜单同时关闭，调用这句话
                        datas.remove(pos);
                        v = mListView.getChildAt(pos);
                        ((SwipeMenuLayout) v).quickClose();
                        IetmDataID = (TextView) v.findViewById(R.id.item_id);
                        deleteData(IetmDataID.getText());
                        notifyDataSetChanged();
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            // 从传入的数据中提取数据并绑定到指定的view中
            holder.title.setText((String) datas.get(position).get("map_title"));
            holder.id.setText((String) datas.get(position).get("map_id"));
            holder.image.setImageResource((Integer) datas.get(position).get("map_image"));
            holder.content.setText((String) datas.get(position).get("map_content"));
            if ((int) datas.get(position).get("pm") == -1) {
                holder.money.setText("一" + datas.get(position).get("map_money").toString());
            } else {
                holder.money.setText(datas.get(position).get("map_money").toString());
            }


            if (needTitle(position)) {
                holder.title.setText((String) datas.get(position).get("map_title"));
                holder.title.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
            }

            return convertView;
        }

        private boolean needTitle(int position) {

            if (position == 0) {
                return true;
            }


            if (position < 0) {
                return false;
            }


            String currentTitle = (String) datas.get(position).get("map_title");
            String previousTitle = (String) datas.get(position - 1).get("map_title");

            if (null == previousTitle || null == currentTitle) {
                return false;
            }


            if (currentTitle.equals(previousTitle)) {
                return false;
            }

            return true;
        }

        private class ViewHolder {
            TextView title;
            TextView id;
            TextView content;
            ImageView image;
            TextView money;
            Button delete;

        }
    }

}


