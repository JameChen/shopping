package com.nahuo.quicksale;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.SafeQuestion;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.provider.UserInfoProvider;

/**
 * @description 回答安全问题
 * @created 2014-8-21 下午12:01:22
 * @author ZZB
 */
public class AnswerSafeQuestionsActivity extends BaseSlideBackActivity implements OnClickListener {

    public static final String EXTRA_LISTENER = "EXTRA_LISTENER";
    private TextView mTvQuestion;
    private EditText mEtAnswer;
    private TextView mTvTips;
    private int mCurQuestionPos = 0;
    private String[] mAnswers = new String[3];
    private List<SafeQuestion> mQNAs;
    private Context mContext = this;
    public LoadingDialog mDialog;
    
    private static enum Step {
        LOAD_DATA, SUBMIT_DATA
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.layout_safe_question);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.layout_titlebar_default);
        initView();
    }


    private void initView() {
        mDialog = new LoadingDialog(mContext);
        initTitleBar();
        mEtAnswer = (EditText) findViewById(R.id.et_answer);
        mTvQuestion = (TextView) findViewById(R.id.tv_question);
        mTvTips = (TextView) findViewById(R.id.tv_forget_answer_tips);
        View nextQuestion = findViewById(R.id.switch_question);
        View submit = findViewById(R.id.btn_submit_safe_questions);

        nextQuestion.setOnClickListener(this);
        submit.setOnClickListener(this);
        loadQuestions();
    }

    private void initTitleBar() {
        TextView tvTitle = (TextView) findViewById(R.id.titlebar_tvTitle);
        tvTitle.setText("安全问题");

        Button btnLeft = (Button) findViewById(R.id.titlebar_btnLeft);
        btnLeft.setText(R.string.titlebar_btnBack);
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(this);
        
        Button btnRight = (Button) findViewById(R.id.titlebar_btnRight);
        btnRight.setText("换一个问题");
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);

    }

    /**
     * Description:变更问题
     * 2014-7-29 下午5:17:47
     * 
     * @author ZZB
     */
    private void changeQuestionView() {
        String answerStr = mEtAnswer.getText().toString();
        switch (mCurQuestionPos) {
        case 0:
            mTvQuestion.setText("问题 1/3 : " + mQNAs.get(0).getQuestion());
            mEtAnswer.setText("");
            mAnswers[2] = answerStr;
            mCurQuestionPos++;
            break;
        case 1:
            mTvQuestion.setText("问题 2/3 : " + mQNAs.get(1).getQuestion());
            mEtAnswer.setText("");
            mAnswers[0] = answerStr;
            mCurQuestionPos++;
            break;
        case 2:
            mTvQuestion.setText("问题 3/3 : " + mQNAs.get(2).getQuestion());
            mEtAnswer.setText("");
            mAnswers[1] = answerStr;
            mCurQuestionPos++;
            break;
        case 3:
            mCurQuestionPos = 0;
            mTvTips.setVisibility(View.VISIBLE);
            changeQuestionView();
            break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.titlebar_btnRight:
            changeQuestionView();
            break;
        case R.id.btn_submit_safe_questions:
            validateAnswer();
            break;
        case R.id.titlebar_btnLeft:
            finish();
            break;
        }
    }
    /**
     * @description 校验安全问题
     * @created 2014-9-18 下午5:22:29
     * @author ZZB
     */
    private void validateAnswer() {
        String answer = mEtAnswer.getText().toString();
        if(TextUtils.isEmpty(answer)){
            ViewHub.setEditError(mEtAnswer, "请输入答案");
            return;
        }
        new Task(Step.SUBMIT_DATA).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void loadQuestions() {
        new Task(Step.LOAD_DATA).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class Task extends AsyncTask<Void, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
            case LOAD_DATA:
                mDialog.start("加载数据中...");
                break;
            case SUBMIT_DATA:
                mDialog.start("提交中...");
                break;
            }

        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (mStep) {
                case LOAD_DATA:
                    mQNAs = UserInfoProvider.getSafeQuestions(mContext, SpManager.getUserId(mContext));
                    break;
                case SUBMIT_DATA:
                    SafeQuestion qna = mQNAs.get(mCurQuestionPos == 0 ? mCurQuestionPos : mCurQuestionPos - 1);
                    qna.setAnswer(mEtAnswer.getText().toString());
                    PaymentAPI.validateSafeQNA(mContext, qna);
                    break;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mDialog.isShowing()) {
                mDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                case LOAD_DATA:
                    changeQuestionView();
                    break;
                case SUBMIT_DATA:
//                    ViewHub.showOkDialog(mContext, "提示", "恭喜你，回答正确", "OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //解除
////                            UserInfoProvider.clearBindPhone(mContext, SpManager.getUserId(mContext));
//                            Intent intent = new Intent(mContext, BindPhoneActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); 
//                            intent.putExtra(BindPhoneActivity.EXTRA_CHANGE_BIND_PHONE, true);
//                            startActivity(intent);
//                        }
//                    });
                    ViewHub.showLongToast(mContext, "回答正确");
                    Intent intent = new Intent(mContext, BindPhoneActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); 
                    intent.putExtra(BindPhoneActivity.EXTRA_CHANGE_BIND_PHONE, true);
                    startActivity(intent);
                    break;
                }

            }
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
