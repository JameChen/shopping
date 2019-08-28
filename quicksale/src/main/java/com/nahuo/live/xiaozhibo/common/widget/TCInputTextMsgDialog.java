package com.nahuo.live.xiaozhibo.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nahuo.live.deal.KeyBoardObserver;
import com.nahuo.live.deal.KeyBoardUtils;
import com.nahuo.live.deal.MeasureLinearLayout;
import com.nahuo.live.deal.SharePrefenceUtils;
import com.nahuo.live.xiaozhibo.adpater.TemplateMsgAdapter;
import com.nahuo.quicksale.R;

import java.util.List;


/**
 * 文本输入框
 */
public class TCInputTextMsgDialog extends Dialog implements KeyBoardObserver {


    public interface OnTextSendListener {

        void onTextSend(String msg, boolean tanmuOpen);
    }

    private TextView confirmBtn;
    private LinearLayout mBarrageArea;
    private EditText messageTextView;
    private static final String TAG = TCInputTextMsgDialog.class.getSimpleName();
    private Context mContext;
    private InputMethodManager imm;
    private View rlDlg;
    private int mLastDiff = 0;
    private LinearLayout mConfirmArea;
    private OnTextSendListener mOnTextSendListener;
    private boolean mDanmuOpen = false;
    //    private final String reg = "[`~@#$%^&*()-_+=|{}':;,/.<>￥…（）—【】‘；：”“’。，、]";
//    private Pattern pattern = Pattern.compile(reg);
    private ImageView iv_template_msg;
    private RecyclerView rv_template_msg;
    private TemplateMsgAdapter adapter;
    private List<String> msgList;
    private boolean isShowMsg;
    private MeasureLinearLayout rootLayout;
    private LinearLayout pannel;
    private boolean isClickViewDiss, isFirstShowKeyWord, isClickMsg;
    private int keyBoardVisibilecount;
    private LinearLayout rldlgview;

    public TCInputTextMsgDialog(Context context, int theme, List<String> msgList) {
        super(context, theme);
        Log.e("yu", "TCInputTextMsgDialog");
        mContext = context;
        this.msgList = msgList;
        setContentView(R.layout.dialog_input_text);
        getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context,R.color.bg_tran)));//背景颜色一定要有，看自己需求
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);//宽高最
        pannel = (LinearLayout) findViewById(R.id.pannel);
        rootLayout = (MeasureLinearLayout) findViewById(R.id.root_layout);
        messageTextView = (EditText) findViewById(R.id.et_input_message);
        messageTextView.setInputType(InputType.TYPE_CLASS_TEXT);
        //修改下划线颜色
        messageTextView.getBackground().setColorFilter(context.getResources().getColor(R.color.transparent), PorterDuff.Mode.CLEAR);

        iv_template_msg = (ImageView) findViewById(R.id.iv_template_msg);
        iv_template_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowMsg = !isShowMsg;
                isClickViewDiss = true;
                isClickMsg = true;
                keyBoardVisibilecount=0;
                showMsg(isShowMsg);
            }
        });

        //初始化高度，和软键盘一致，初值为手机高度一半
        pannel.getLayoutParams().height = SharePrefenceUtils.getKeyBoardHeight(mContext);
        rv_template_msg = (RecyclerView) findViewById(R.id.rv_template_msg);
        rv_template_msg.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new TemplateMsgAdapter(mContext);
        rv_template_msg.setAdapter(adapter);
        adapter.setNewData(msgList);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String msg = adapter.getData().get(position).toString();
                if (!TextUtils.isEmpty(msg)) {
                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    setMessFocus();
                    // imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    //messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(mContext, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                }
                // messageTextView.setText(null);
            }
        });
        rv_template_msg.setAdapter(adapter);

//        if (isShowMsg) {
//            rv_template_msg.setVisibility(View.VISIBLE);
//            iv_template_msg.setImageResource(R.drawable.keboad_icon);
//
//        } else {
//            iv_template_msg.setImageResource(R.drawable.mess_icon);
//            rv_template_msg.setVisibility(View.GONE);
//        }
        confirmBtn = (TextView) findViewById(R.id.confrim_btn);
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {
                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    setMessFocus();
//                    imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
//                    imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(mContext, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        final Button barrageBtn = (Button) findViewById(R.id.barrage_btn);
        barrageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_off);
                }
            }
        });

        mBarrageArea = (LinearLayout) findViewById(R.id.barrage_area);
        mBarrageArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDanmuOpen = !mDanmuOpen;
                if (mDanmuOpen) {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_on);
                } else {
                    barrageBtn.setBackgroundResource(R.drawable.barrage_slider_off);
                }
            }
        });
//
//        messageTextView.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_UP) && (keyCode == KeyEvent.KEYCODE_BACK)) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//        messageTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isShowMsg = false;
//                showMsg(isShowMsg);
//
//            }
//        });
        messageTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String msg = messageTextView.getText().toString().trim();
                switch (actionId) {
                    case KeyEvent.KEYCODE_ENDCALL:
                    case KeyEvent.KEYCODE_ENTER:
                        if (!TextUtils.isEmpty(msg)) {

                            mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                            //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                            messageTextView.setText("");
                            dismiss();
                        } else {
                            Toast.makeText(mContext, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                        }
                        messageTextView.setText(null);
//                        if (messageTextView.getText().length() > 0) {
////                            mOnTextSendListener.onTextSend("" + messageTextView.getText(), mDanmuOpen);
//                            //sendText("" + messageTextView.getText());
//                            //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
//                            imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
////                            messageTextView.setText("");
//                            dismiss();
//                        } else {
//                            Toast.makeText(mContext, "输入内容不能为空!", Toast.LENGTH_LONG).show();
//                        }
                        return true;
                    case KeyEvent.KEYCODE_BACK:
                        if (!TextUtils.isEmpty(msg)) {

                            mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                            setMessFocus();
                            //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                            // imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                            messageTextView.setText("");
                            dismiss();
                        } else {
                            Toast.makeText(mContext, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                        }
                        messageTextView.setText(null);
                        return false;
                    default:
                        return false;
                }
            }
        });

        mConfirmArea = (LinearLayout) findViewById(R.id.confirm_area);
        mConfirmArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messageTextView.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {

                    mOnTextSendListener.onTextSend(msg, mDanmuOpen);
                    setMessFocus();
                    //imm.showSoftInput(messageTextView, InputMethodManager.SHOW_FORCED);
                    // imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
                    messageTextView.setText("");
                    dismiss();
                } else {
                    Toast.makeText(mContext, "输入内容不能为空!", Toast.LENGTH_LONG).show();
                }
                messageTextView.setText(null);
            }
        });

        messageTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d("yu", "onKey " + keyEvent.getCharacters());
                return false;
            }
        });

        rlDlg = findViewById(R.id.rl_outside_view);
        rlDlg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (v.getId() != R.id.rl_inputdlg_view)
                dismiss();
            }
        });

//        rldlgview = (LinearLayout) findViewById(R.id.rl_inputdlg_view);
////
//        rldlgview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//            @Override
//            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight = getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//                Log.e("yu", heightDifference + ">>heightDifference>>>screenHeight=" + screenHeight + ">>" + ScreenUtils.getScreenHeight(mContext) + ">>r.bottom=" + r.bottom + "r.top=" + r.top+ ">>bottom=" + bottom + "top=" + top+ ">>olderbottom=" + oldBottom + "oldertop=" + oldTop);
////                boolean isActive;
////                if (Math.abs(heightDifference) > screenHeight / 5) {
////                    isActive = true; // 超过屏幕五分之一则表示弹出了输入法
////                } else {
////                    isActive = false;
////                }
////                Log.e("yu", isActive + "");
//                // Log.e("yu","bottom="+bottom+">>>>olderbottom="+oldBottom+">>>>bottom - oldBottom "+(bottom - oldBottom) );
//                if (heightDifference <= 0 && !myKeyBoardVisibile) {
//                    //imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
//                    dismiss();
//                }
//                mLastDiff = heightDifference;
//            }
//        });
//        rldlgview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                imm.hideSoftInputFromWindow(messageTextView.getWindowToken(), 0);
////                dismiss();
//            }
//        });
    }

    private void setMessFocus() {
        //点击列表部分就清除焦点并初始化状态
        if (pannel.getVisibility() == View.VISIBLE) {
            pannel.setVisibility(View.GONE);
            showMsg(false);
        } else {
            messageTextView.clearFocus();
            KeyBoardUtils.hideKeyboard(messageTextView);
        }
    }


    public void setmOnTextSendListener(OnTextSendListener onTextSendListener) {
        this.mOnTextSendListener = onTextSendListener;
    }

    boolean myKeyBoardVisibile;
    private void showMsg(boolean isShowMsg) {
        if (isShowMsg) {
            //想要显示面板
            iv_template_msg.setImageResource(R.drawable.keboad_icon);
            if (rootLayout.getKeyBoardObservable().isKeyBoardVisibile()) {
                //当前软键盘为 挂起状态
                //隐藏软键盘并显示面板
                messageTextView.clearFocus();
                KeyBoardUtils.hideKeyboard(messageTextView);
            } else {
                //显示面板
                pannel.setVisibility(View.VISIBLE);
            }
        } else {
            //想要关闭面板
            //挂起软键盘，并隐藏面板
            iv_template_msg.setImageResource(R.drawable.mess_icon);
            messageTextView.requestFocus();
            KeyBoardUtils.showKeyboard(messageTextView);
        }
//        if (isShowMsg) {
//            //想要显示面板
////            if (rootLayout.getKeyBoardObservable().isKeyBoardVisibile()) {
////                //当前软键盘为 挂起状态
////                //隐藏软键盘并显示面板
////                messageTextView.clearFocus();
////                KeyBoardUtils.hideKeyboard(messageTextView);
////            } else {
////                //显示面板
////                // pannel.setVisibility(View.VISIBLE);
////            }
//            iv_template_msg.setImageResource(R.drawable.keboad_icon);
//            pannel.setVisibility(View.VISIBLE);
//            messageTextView.clearFocus();
//            KeyBoardUtils.hideKeyboard(messageTextView);
////            if (pannel.getVisibility() == View.GONE) {
////                pannel.setVisibility(View.VISIBLE);
////            }
////            if (rootLayout.getKeyBoardObservable().isKeyBoardVisibile()) {
////                messageTextView.clearFocus();
////                KeyBoardUtils.hideKeyboard(messageTextView);
////            }
//
//        } else {
//            //想要关闭面板
//            //挂起软键盘，并隐藏面板
////            if (pannel.getVisibility() == View.VISIBLE) {
////                pannel.setVisibility(View.GONE);
////            }
//            pannel.setVisibility(View.GONE);
//            iv_template_msg.setImageResource(R.drawable.mess_icon);
//            messageTextView.requestFocus();
//            KeyBoardUtils.showKeyboard(messageTextView);
//
//        }

    }
    @Override
    public void update(boolean keyBoardVisibile, int keyBoardHeight) {
        myKeyBoardVisibile = keyBoardVisibile;
        Log.e("yu", "keyBoardVisibile=" + keyBoardVisibile + ">>>keyBoardHeight" + keyBoardHeight);
        if (keyBoardVisibile) {
            //软键盘挂起
            isFirstShowKeyWord = false;
            pannel.setVisibility(View.GONE);
            iv_template_msg.setImageResource(R.drawable.keboad_icon);
            isShowMsg=false;
           // showMsg(false);
//            if (isClickViewDiss&&!isFirstShowKeyWord){
//                isClickViewDiss=false;
//            }
//            if (keyBoardVisibilecount==1){
//                isClickViewDiss=true;
//            }
        } else {
            iv_template_msg.setImageResource(R.drawable.mess_icon);
            keyBoardVisibilecount+=1;
            Log.e("yu","!!!!!!!keyBoardVisibilecount="+keyBoardVisibilecount+">>>isClickViewDiss="+isClickViewDiss+"isFirstShowKeyWord="+isFirstShowKeyWord);
            //回复原样软键盘关闭
            if (!isClickViewDiss && !isFirstShowKeyWord) {
                dismiss();
                return;
            }

           // Log.e("yu","______keyBoardVisibilecount="+keyBoardVisibilecount+">>>isClickViewDiss="+isClickViewDiss+"isFirstShowKeyWord="+isFirstShowKeyWord);
                //showMsg(true);
            if (isShowMsg) {
                if (pannel.getLayoutParams().height != keyBoardHeight) {
                    pannel.getLayoutParams().height = keyBoardHeight;
                }
                pannel.setVisibility(View.VISIBLE);
            }

        }
        if (isClickViewDiss=true)
            isClickViewDiss=false;

    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.e("yu", "onCreate(savedInstanceState)");
//    }
//
//    @Override
//    public void create() {
//        super.create();
//        Log.e("yu", "onCreate");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.e("yu", "onStop");
//    }

    @Override
    public void dismiss() {
        super.dismiss();
        Log.e("yu", "dismiss");
        if (rootLayout != null)
            rootLayout.getKeyBoardObservable().unRegister(this);
        //dismiss之前重置mLastDiff值避免下次无法打开
        mLastDiff = 0;
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.e("yu", "onStart");
//    }

    @Override
    public void show() {
        super.show();
        Log.e("yu", "show");
        keyBoardVisibilecount=0;
        isFirstShowKeyWord = true;
        isClickViewDiss = false;
        isClickMsg = false;
        isShowMsg = false;
        showMsg(isShowMsg);
        if (rootLayout != null)
            rootLayout.getKeyBoardObservable().register(this);

    }
}
