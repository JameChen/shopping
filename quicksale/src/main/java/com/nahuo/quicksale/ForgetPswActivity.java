package com.nahuo.quicksale;

import java.util.List;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.Const.PasswordExtra;
import com.nahuo.quicksale.common.Const.PasswordType;
import com.nahuo.quicksale.common.SafeQuestion;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.eventbus.BusEvent;
import com.nahuo.quicksale.eventbus.EventBusId;
import com.nahuo.quicksale.provider.UserInfoProvider;

import de.greenrobot.event.EventBus;

import static com.nahuo.quicksale.provider.UserInfoProvider.getNetSafeQuestions;

/**
 * Description:忘记密码页面
 * 2014-7-4下午2:23:19
 */
public class ForgetPswActivity extends BaseSlideBackActivity implements OnClickListener {

    private Context mContext = this;

    private LoadingDialog loadingDialog;
    private Button btnLeft, btnRight, btnGetVerifyCode, mBtnVerifyNext;
    private TextView tvTitle, mTvPhoneNo,tv_speech_verification;

    private View mGetVerifyView, safeQuestionView;
    private EditText mEtVerifyCode;

    private Step mCurrentStep;
    private PasswordType mPswType;

    private WaitTimer waitTimer;

    private EditText mEtAnswer;
    private TextView mQuestion;
    private int mCurQuestionPos = 0;
    private String[] mAnswers = new String[3];
    /**
     * 标志是否加载了安全问题，如果没有加载，不可回答问题
     */
    private List<SafeQuestion> mQNAs;

    private EventBus eventBus = EventBus.getDefault();
    private int smstype = 1;
    // 找回密码步骤
    private enum Step {

        /**
         * 获取验证码
         */
        GET_VERIFY_CODE,
        /**
         * 回答安全问题
         */
        SAFE_QUESTION,
        /**
         * 校验验证码
         */
        VALIDATE_VERIFY_CODE,
        LOAD_SAFE_QUESTIONS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);// 设置自定义标题栏
        setContentView(R.layout.activity_forget_psw);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);// 更换自定义标题栏布局
        mPswType = (PasswordType) getIntent().getSerializableExtra(PasswordExtra.EXTRA_PSW_TYPE);
        //注册eventbus刷新
        eventBus.registerSticky(this);
        initView();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    public void onEventMainThread(BusEvent event) {
        switch (event.id) {
            case EventBusId.FINISH_PAY_PWD:
                finish();
                break;
        }
    }

    /**
     * 初始化视图
     */
    private void initView() {
        loadingDialog = new LoadingDialog(mContext);
        // 标题栏
        initTitleBar();

        // 功能操作视图
        mGetVerifyView = findViewById(R.id.layout_get_verifycode);

        mBtnVerifyNext = (Button) mGetVerifyView.findViewById(R.id.btn_next);
        mBtnVerifyNext.setOnClickListener(this);

        safeQuestionView = findViewById(R.id.forgotpwd_safe_question);

        mTvPhoneNo = (TextView) mGetVerifyView.findViewById(R.id.tv_phone_num);
        mTvPhoneNo.setText(UserInfoProvider.getBindPhone(mContext, SpManager.getUserId(mContext)));

        mEtVerifyCode = (EditText) mGetVerifyView.findViewById(R.id.et_sms_key);

        btnGetVerifyCode = (Button) mGetVerifyView.findViewById(R.id.btn_sms_key);
        btnGetVerifyCode.setOnClickListener(this);

        // 获取窗体传值(手机号)
        String phoneNo = SpManager.getLoginAccount(mContext);
        // 如果不是手机的话
        if (FunctionHelper.isPhoneNo(phoneNo)) {
            mTvPhoneNo.setText(phoneNo);
        }

        btnLeft.setOnClickListener(this);
        btnGetVerifyCode.setOnClickListener(this);
        tv_speech_verification=(TextView)findViewById(R.id.tv_speech_verification);
        tv_speech_verification.setOnClickListener(this);
        mCurrentStep = Step.SAFE_QUESTION;
        changeView(mCurrentStep);

    }

    private void initTitleBar() {
        btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setOnClickListener(this);
        btnRight.setText("换一个问题");
        tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        btnLeft.setVisibility(View.VISIBLE);

        tvTitle.setText(mPswType != PasswordType.LOGIN ? "修改支付密码" : "修改登录密码");
    }

    /**
     * 改变界面视图
     */
    private void changeView(Step step) {
        switch (step) {
            case GET_VERIFY_CODE:
                new Task(Step.GET_VERIFY_CODE).execute();
            case SAFE_QUESTION:
            case VALIDATE_VERIFY_CODE:
            case LOAD_SAFE_QUESTIONS:
                btnLeft.setText(R.string.titlebar_btnBack);
                break;
        }

        changeContentView(step);
    }

    private void changeContentView(Step step) {
        if (step == Step.SAFE_QUESTION) {
            handleSafeQuestion();
        }
        // 输入手机号码获取验证码,填写验证码
        mGetVerifyView.setVisibility(step == Step.GET_VERIFY_CODE ? View.VISIBLE : View.GONE);
        // 安全问题
        safeQuestionView.setVisibility(step == Step.SAFE_QUESTION ? View.VISIBLE : View.GONE);
        btnRight.setVisibility(step == Step.SAFE_QUESTION ? View.GONE : View.GONE);
    }

    /**
     * Description:处理安全问题
     * 2014-7-3下午2:46:32
     */
    private void handleSafeQuestion() {
        // 换一个问题
        mQuestion = (TextView) safeQuestionView.findViewById(R.id.tv_question);
        mEtAnswer = (EditText) safeQuestionView.findViewById(R.id.et_answer);
        View submitQuestions = safeQuestionView.findViewById(R.id.btn_submit_safe_questions);

        submitQuestions.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSafeQ();
            }

        });
        new Task(Step.LOAD_SAFE_QUESTIONS).execute();

    }

    /**
     * @description 回答安全问题
     * @created 2014-9-18 下午7:55:22
     * @author ZZB
     */
    private void submitSafeQ() {
        if (TextUtils.isEmpty(mEtAnswer.getText().toString())) {
            ViewHub.setEditError(mEtAnswer, "请输入答案");
        } else {
            new Task(Step.SAFE_QUESTION).execute();
        }

    }

    /**
     * Description:变更问题
     * 2014-7-29 下午5:17:47
     *
     * @author ZZB
     */
    private void changeQuestionView() {
        String answerStr = mEtAnswer.getText().toString();
        mQuestion.setText("问题：" + mQNAs.get(0).getQuestion());
        mEtAnswer.setText("");
//        switch (mCurQuestionPos) {
//            case 0:
//                mQuestion.setText("问题 1/3 : " + mQNAs.get(0).getQuestion());
//                mEtAnswer.setText("");
//                mAnswers[2] = answerStr;
//                mCurQuestionPos++;
//                break;
//            case 1:
//                mQuestion.setText("问题 2/3 : " + mQNAs.get(1).getQuestion());
//                mEtAnswer.setText("");
//                mAnswers[0] = answerStr;
//                mCurQuestionPos++;
//                break;
//            case 2:
//                mQuestion.setText("问题 3/3 : " + mQNAs.get(2).getQuestion());
//                mEtAnswer.setText("");
//                mAnswers[1] = answerStr;
//                mCurQuestionPos++;
//                break;
//            case 3:
//                mCurQuestionPos = 0;
//                TextView tv = (TextView) safeQuestionView.findViewById(R.id.tv_forget_answer_tips);
//                tv.setVisibility(View.VISIBLE);
//                tv.setText(getString(R.string.security_forget_safeq_answer));
//                changeQuestionView();
//                break;
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_speech_verification:
                //语音验证
                smstype=2;
                new Task(Step.GET_VERIFY_CODE).execute();

                break;
            case R.id.titlebar_btnLeft:
                finish();
                break;
            case R.id.titlebar_btnRight://切换问题
                changeQuestionView();
                break;
            case R.id.btn_sms_key:// 获取验证码

                new Task(Step.GET_VERIFY_CODE).execute();

                break;
            case R.id.btn_next:// 校验验证码
                if (TextUtils.isEmpty(mEtVerifyCode.getText().toString())) {
                    ViewHub.setEditError(mEtVerifyCode, "请输入验证码");
                } else {
                    validateVerifyCode();
                }
                break;

        }
    }

    /**
     * @description 校验验证码
     * @created 2014-9-19 上午10:24:10
     * @author ZZB
     */
    private void validateVerifyCode() {
        if (validateVerifyCodeInput()) {
            new Task(Step.VALIDATE_VERIFY_CODE).execute();
        }
    }

    @Override
    public void finish() {
        super.finish();
        ViewHub.hideKeyboard(this);
    }

    private boolean validateVerifyCodeInput() {
        boolean validate = FunctionHelper.isVerifyCode(mEtVerifyCode.getText().toString());
        if (!validate) {
            ViewHub.setEditError(mEtVerifyCode, "验证码错误");
        }
        return validate;
    }

//    private boolean validatePhone() {
//        String phoneNo = mTvPhoneNo.getText().toString().trim();
//        if (TextUtils.isEmpty(phoneNo)) {
//            ViewHub.setEditError(mTvPhoneNo, "请输入手机号码");
//            mEtPhoneNo.requestFocus();
//            return false;
//        } else if (!FunctionHelper.isPhoneNo(phoneNo)) {
//            ViewHub.setEditError(mEtPhoneNo, "请输入正确的手机号码");
//            mEtPhoneNo.requestFocus();
//            return false;
//        }
//        return true;
//    }

    /**
     * 刷新倒计时的线程
     */
    private class WaitTimer extends CountDownTimer {

        public WaitTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnGetVerifyCode.setEnabled(false);
            tv_speech_verification.setEnabled(false);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.gray_yuyin));
            btnGetVerifyCode.setText("(" + millisUntilFinished / 1000 + ")重新获取");
        }

        @Override
        public void onFinish() {
            tv_speech_verification.setEnabled(true);
            tv_speech_verification.setTextColor(getResources().getColor(R.color.bule_overlay));
            btnGetVerifyCode.setEnabled(true);
            btnGetVerifyCode.setText(R.string.forgotpwd_btnGetSmsKey_text);
        }

    }

    /**
     * Description:检查是否设置了安全问题
     * 2014-7-30 上午10:48:51
     *
     * @author ZZB
     */
    private boolean hasSetQuestion() {
        boolean result = UserInfoProvider.hasSetSafeQuestion(mContext, SpManager.getUserId(mContext));
        return result;
    }

    /**
     * 找回密码：1.获取短信验证码 2.判断验证码有效性 3.重置密码
     */
    private class Task extends AsyncTask<Void, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            String msg = "";
            switch (mStep) {
                case GET_VERIFY_CODE:
                    if (smstype==2){
                        msg = getString(R.string.forgotpwd_getSmsKey_yu_yin_loading);
                    }else {
                        msg = getString(R.string.forgotpwd_getSmsKey_loading);
                    }

                    break;
                case SAFE_QUESTION:
                    msg = getString(R.string.safeq_checking_answers);
                    break;
                case VALIDATE_VERIFY_CODE:
                    msg = "校验验证码中...";
                    break;
                case LOAD_SAFE_QUESTIONS:
                    msg = "加载安全问题中...";
                    break;
            }
            loadingDialog.start(msg);
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                String result = "";
                String phoneNo = mTvPhoneNo.getText().toString();
                switch (mStep) {
                    case GET_VERIFY_CODE:

                      //  AccountAPI.getInstance().getMobileVerifyCode2(phoneNo, "", 1);
                        AccountAPI.getInstance().getSmsMobileVerifyCode(phoneNo,smstype);
                        result = "OK";
                        break;

                    case SAFE_QUESTION:
                        String answer = mEtAnswer.getText().toString();
                        SafeQuestion qna = new SafeQuestion();
                        qna.setAnswer(answer);
                        qna.setQuestionId(mQNAs.get(mCurQuestionPos).getQuestionId());
                        PaymentAPI.validateSafeQNA(mContext, qna);
                        result = "OK";
                        break;
                    case VALIDATE_VERIFY_CODE:
                        AccountAPI.getInstance().checkMobileVerifyCode(phoneNo,
                                mEtVerifyCode.getText().toString());
                        break;
                    case LOAD_SAFE_QUESTIONS:
                        mQNAs = UserInfoProvider.getNetSafeQuestions(mContext, SpManager.getUserId(mContext));
//                        if (hasSetQuestion()) {
//                            mQNAs = UserInfoProvider.getSafeQuestions(mContext, SpManager.getUserId(mContext));
//
//                        } else {
//                            mQNAs = UserInfoProvider.getNetSafeQuestions(mContext, SpManager.getUserId(mContext));
//                        }
                        break;

                }
                return result;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "error:" + ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(Object result) {

            if (loadingDialog.isShowing()) {
                loadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                String error = ((String) result).replace("error:", "");
                switch (mStep) {
                    case VALIDATE_VERIFY_CODE:
                        ViewHub.setEditError(mEtVerifyCode, error);
                        break;
                    default:
                        ViewHub.showLongToast(mContext, error);
                        break;
                }
            } else {
                switch (mStep) {
                    case GET_VERIFY_CODE:
                        // 启动定时器
                        if (smstype==2){
                            ViewHub.showOkDialog(mContext, "提示", getString(R.string.forgotpwd_getSmsKey_success_yuyin), "OK");
                        }else {
                            ViewHub.showOkDialog(mContext, "提示", "验证码已经发到"
                                    + mTvPhoneNo.getText().toString() + "的手机中", "OK");
                        }

                        waitTimer = new WaitTimer(60000, 1000);
                        waitTimer.start();
                        mCurrentStep = Step.VALIDATE_VERIFY_CODE;
                        break;
                    case SAFE_QUESTION:
                        mCurrentStep = Step.GET_VERIFY_CODE;
                        ViewHub.showShortToast(mContext, "回答正确");
                        changeView(mCurrentStep);
                        break;
                    case VALIDATE_VERIFY_CODE:
                        Intent intent = new Intent(mContext, SetPswActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);// 不加入回退栈
                        intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, mPswType);
                        intent.putExtra(SetPswActivity.EXTRA_VERIFYCODE, mEtVerifyCode.getText()
                                .toString());
                        startActivity(intent);
                        break;
                    case LOAD_SAFE_QUESTIONS:
                        changeQuestionView();
                        break;
                }
            }
        }
    }

    public static void toForgetPayPsw(final Context context) {
        boolean set = UserInfoProvider.hasSetSafeQuestion(context, SpManager.getUserId(context));
        if (set) {
            Intent intent = new Intent(context, ForgetPswActivity.class);
            intent.putExtra(PasswordExtra.EXTRA_PSW_TYPE, PasswordType.RESET_PAYMENT);
            context.startActivity(intent);
        } else {
            Builder builder = LightAlertDialog.Builder.create(context);
            builder.setTitle("提示").setMessage("您还没有设置密码问题").setNegativeButton("取消", null)
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, SetSafeQuestionsActivity.class);
                            context.startActivity(intent);
                        }
                    });
            builder.show();
        }
    }
}
