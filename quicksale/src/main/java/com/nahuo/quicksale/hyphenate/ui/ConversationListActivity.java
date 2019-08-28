package com.nahuo.quicksale.hyphenate.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nahuo.library.controls.CircleTextView;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.im.MyUnsetMsgCountBroadcastReceiver;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.CircleCarTxtUtl;
import com.nahuo.quicksale.util.LoadGoodsTask;

import de.greenrobot.event.EventBus;

public class ConversationListActivity extends BaseAppCompatActicity implements View.OnClickListener {
    private CircleTextView carCountTv;
    private boolean is_show_left_btn = false;
    private TextView tvTLeft, titleBar;
    private ConversationListFragment conversationListFragment;
    private MyUnsetMsgCountBroadcastReceiver unsetReceiver;
    private EventBus mEventBus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        unsetReceiver = new MyUnsetMsgCountBroadcastReceiver();
        registerReceiver(unsetReceiver, new IntentFilter(ChatUtl.UNSET));
        if (mEventBus != null) {
            if (!mEventBus.isRegistered(this))
                mEventBus.register(this);
        }
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (conversationListFragment != null) {
//            conversationListFragment.refresh();
//        }
        new LoadGoodsTask(this, carCountTv).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unsetReceiver != null)
            this.unregisterReceiver(unsetReceiver);
        if (mEventBus != null) {
            if (mEventBus.isRegistered(this))
                mEventBus.unregister(this);
        }
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.WEIXUN_NEW_MSG:
                if (conversationListFragment != null) {
                    conversationListFragment.refresh();
                }
                break;
        }
    }

    public void updateUnreadLabel() {
        if (this != null && !this.isFinishing()) {
            Intent xIntent = new Intent(ChatUtl.UNSET);
            this.sendBroadcast(xIntent);
        }
    }

    // 初始化
    private void initView() {
        findViewById(R.id.iv_shopping_cart).setOnClickListener(this);
        carCountTv = (CircleTextView) findViewById(R.id.circle_car_text);
        CircleCarTxtUtl.setColor(carCountTv);
        is_show_left_btn = getIntent().getBooleanExtra(ChatUtl.ETRA_LEFT_BTN_ISHOW, false);
        tvTLeft = (TextView) findViewById(R.id.tvTLeft);
        tvTLeft.setOnClickListener(this);
        if (is_show_left_btn) {
            tvTLeft.setVisibility(View.VISIBLE);
        } else {
            tvTLeft.setVisibility(View.GONE);
        }
        titleBar = (TextView) findViewById(R.id.chat_titlebar);
        titleBar.setText(getResources().getString(R.string.chat_title));
        conversationListFragment = new ConversationListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment, "f0")
                .commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shopping_cart:
                Utils.gotoShopcart(this);
                break;
            case R.id.tvTLeft:
                finish();
                break;
        }
    }
}
