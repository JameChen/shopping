package com.nahuo.quicksale;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.google.gson.reflect.TypeToken;
import com.nahuo.library.controls.AutoCompleteTextViewEx;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.GsonHelper;
import com.nahuo.quicksale.adapter.ShopSearchAdapter;
import com.nahuo.quicksale.api.ApiHelper;
import com.nahuo.quicksale.api.BuyOnlineAPI;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShopSearchActivity extends BaseActivity implements OnClickListener {

    /** 跳转过来的类名 */
    public static final String     EXTRA_FROM_CLASS = "EXTRA_FROM_CLASS";
    private String                 mFromClass;
    private boolean                showDefaultShop  = false;
    private ShopSearchActivity     vThis            = this;
    private AutoCompleteTextViewEx msearch;
    private LoadingDialog          mloadingDialog;
    private List<ShopInfoModel>    itemList         = null;
    private ShopSearchAdapter      adapter;
    private ListView               mlist;
    private SharedPreferences      sp;
    private View                   emptyView;
    private TextView               tvEmptyMessage;
    private final String           defaultShopJson  = "["
                                                            + "{\"UserID\":507293,\"ShopID\":56828,\"UserName\":\"高端定制女装\",\"ShopName\":\"高端定制女装\"},"
                                                            + "{\"UserID\":507434,\"ShopID\":56887,\"UserName\":\"大牌鞋类供货\",\"ShopName\":\"大牌鞋类供货\"},"
                                                            +
                                                            // "{\"UserID\":176298,\"ShopID\":18605,\"UserName\":\"布布精新\",\"ShopName\":\"布布精新店铺2\"},"+
                                                            // "{\"UserID\":117145,\"ShopID\":19057,\"UserName\":\"丸子小诗\",\"ShopName\":\"丸子小诗\"},"+
                                                            // "{\"UserID\":424608,\"ShopID\":33232,\"UserName\":\"名人名铺\",\"ShopName\":\"名人名铺\"},"+
                                                            // "{\"UserID\":452046,\"ShopID\":36163,\"UserName\":\"赵晓妞的店\",\"ShopName\":\"赵晓妞的店\"},"+
                                                            // "{\"UserID\":451540,\"ShopID\":39806,\"UserName\":\"白小微\",\"ShopName\":\"小微家的白富美\"},"+
                                                            // "{\"UserID\":452014,\"ShopID\":39903,\"UserName\":\"时间海的诗\",\"ShopName\":\"时尚大牌好质量女鞋\"},"+
                                                            // "{\"UserID\":452021,\"ShopID\":39904,\"UserName\":\"漂洋过海来看你\",\"ShopName\":\"大牌包包集中营\"},"+
                                                            // "{\"UserID\":466763,\"ShopID\":40317,\"UserName\":\"高端商务臻品\",\"ShopName\":\"高端商务臻品\"},"+
                                                            // "{\"UserID\":466877,\"ShopID\":40411,\"UserName\":\"费列罗鞋业\",\"ShopName\":\"费列罗鞋业\"},"+
                                                            // "{\"UserID\":454414,\"ShopID\":46653,\"UserName\":\"依晨阁\",\"ShopName\":\"美人制造\"}"+
                                                            "]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.layout_shop_search);
      //  getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        initExtras();
        initView();
        initItemAdapter();
        init();
        initTitle();
    }

    private void initTitle() {
        // 标题栏
        TextView tvTitle = (TextView)findViewById(R.id.titlebar_tvTitle);
        Button btnLeft = (Button)findViewById(R.id.titlebar_btnLeft);
        Button btnRight = (Button)findViewById(R.id.titlebar_btnRight);
        tvTitle.setText("搜索用户名");
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRight.setText("搜索");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                search(msearch.getText().toString());
            }
        });
    }

    private void initExtras() {
        Intent intent = getIntent();
        mFromClass = intent.getStringExtra(EXTRA_FROM_CLASS);
        showDefaultShop = intent.getBooleanExtra("showDefaultShop", false);
    }

    // 默认参数
    private void init() {

        sp = vThis.getSharedPreferences("Sp_SearchHis", MODE_APPEND);
        Map<String, ?> list = sp.getAll();

        if (showDefaultShop) {
            List<ShopInfoModel> defaultShops;
            try {
                defaultShops = GsonHelper.jsonToObject(defaultShopJson, new TypeToken<List<ShopInfoModel>>() {});
                Random random = new Random();
                int rd = 0;
                int rd1 = 1;
                // int rd = random.nextInt(defaultShops.size());
                // int rd1 = random.nextInt(defaultShops.size());
                itemList.add(defaultShops.get(rd));
                itemList.add(defaultShops.get(rd1));
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    // private void Write(String Value) {
    //
    // // 存入数据
    // Editor editor = sp.edit();
    // editor.putString("data", Value);
    // editor.commit();
    // }

    // 初始化数据
    private void initItemAdapter() {
        if (itemList == null)
            itemList = new ArrayList<ShopInfoModel>();

        adapter = new ShopSearchAdapter(itemList, vThis);
        adapter.setFromClass(mFromClass);
        mlist.setAdapter(adapter);
    }

    /**
     * 显示空数据视图
     * */
    private void showEmptyView(boolean show, String msg) {

        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(msg)) {
            tvEmptyMessage.setText(getString(R.string.layout_empty_message));
        } else {
            tvEmptyMessage.setText(msg);
        }
    }

    private void initView() {

        Intent intent = getIntent();
        /* 取出Intent中附加的数据 */
        // String first = intent.getStringExtra("flags");

        mloadingDialog = new LoadingDialog(vThis);
        msearch = (AutoCompleteTextViewEx)findViewById(R.id.shop_search_edtSearch);
        msearch.setOnSearchLogDeleteListener(new AutoCompleteTextViewEx.OnSearchLogDeleteListener() {
            @Override
            public void onSearchLogDeleted(String text) {
                String newChar = SpManager.deleteSearchShopHistories(getApplicationContext(), text);
                msearch.populateData(newChar, ",");
                msearch.getFilter().filter(msearch.getText());
            }
        });

        msearch.populateData(SpManager.getSearchShopHistories(getApplicationContext()), ",");

        InputMethodManager im = ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE));
        im.showSoftInput(msearch, 0);
        mlist = (ListView)findViewById(R.id.shop_search_listview);
        emptyView = findViewById(R.id.shop_search_layout_empty);
        tvEmptyMessage = (TextView)emptyView.findViewById(R.id.layout_empty_tvMessage);

        showEmptyView(false, "");

        msearch.setOnEditorActionListener(new OnEditorActionListener() {

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String key = v.getText().toString();
                    search(key);

                }

                return false;
            }

        });
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
    }

    private void search(String key) {
        if (key.trim().length() == 0) {
            Toast.makeText(vThis, "请输入要搜索的用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        SpManager.addSearchShopHistories(getApplicationContext(), key);
        msearch.populateData(SpManager.getSearchShopHistories(getApplicationContext()), ",");

        // Write(key);
        // ShopSearchAdapter adpter=new
        // AllItemSearchActivity(,vThis)

        LoadRearchDataTask task = new LoadRearchDataTask(key);
        task.execute((Void)null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                search(msearch.getText().toString());
                break;
        }

    }

    public class LoadRearchDataTask extends AsyncTask<Void, Void, String> {
        private String mkeywold;

        public LoadRearchDataTask(String keyword) {
            mkeywold = keyword;
        }

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                List<ShopInfoModel> result = BuyOnlineAPI.getInstance().getShopInfoList(0, 30,
                        PublicData.getCookie(vThis), mkeywold);
                itemList = result;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "OK";
        }

        @Override
        protected void onPostExecute(String result) {
            mloadingDialog.stop();
            if (itemList.size() == 0) {
                showEmptyView(true, "没有找到对应的店铺");
            }
            adapter.mDataList = itemList;
            adapter.notifyDataSetChanged();
            if (!result.equals("OK")) {
                // 验证result
                if (result.startsWith("401") || result.startsWith("not_registered")) {
                    Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
                    ApiHelper.checkResult(result, vThis);

                } else {
                    Toast.makeText(vThis, result, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onPreExecute() {
            showEmptyView(false, " ");
            mloadingDialog.start(getString(R.string.items_loadData_loading));
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
}
