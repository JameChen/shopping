package com.nahuo.quicksale.yft;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.nahuo.bean.SafeBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.api.PaymentAPI;
import com.nahuo.quicksale.common.SafeQuestion;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.event.OnFragmentFinishListener;
import com.nahuo.quicksale.provider.SafeQuestionsProvider;
import com.nahuo.quicksale.provider.UserInfoProvider;

import java.util.List;

public class SetSafeQuestionsFragment extends Fragment implements View.OnClickListener {

    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    private OnFragmentFinishListener mListener;
    private Context mContext;
    private View mContentView;
    private LoadingDialog mDialog;
    private Spinner mSpinner;
    private ArrayAdapter<SafeQuestion> mAdapter;
    private TextView mTVTips;
    private TextView mQuestionKey;
    private EditText mETAnswer;

    private SparseArray<SafeQuestion[]> mSafeQs;
    private int mCurIndex = 0;
    // 当前的安全问题数组
    private SafeQuestion[] mCurQs;
    // 问题和答案
    private SparseArray<SafeQuestion> mQNAs = new SparseArray<SafeQuestion>();

    private static enum Step {
        SET_SAFEQ, CHECK_HAS_SAFEQ, ALL_SECURITY
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // initQuestions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.activity_safe_questions2, container, false);
        initView();
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        new Task(Step.ALL_SECURITY).execute();
        return mContentView;
    }


    private void initQuestions() {
        mSafeQs = SafeQuestionsProvider.getQuestions();
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

    private void initView() {
        mDialog = new LoadingDialog(mContext);
        mContentView.findViewById(R.id.safeq_next).setOnClickListener(this);
        boolean hasSetQuestion = hasSetQuestion();
        if (hasSetQuestion) {
            mContentView.findViewById(R.id.layout_questions).setVisibility(View.GONE);
            mContentView.findViewById(R.id.tv_has_set_question).setVisibility(View.VISIBLE);
            return;
        }
        mTVTips = (TextView) mContentView.findViewById(R.id.tv_bottom_info);
        mTVTips.setText("安全问题是为了您忘记支付密码或手机丢失等情况下，账户资金能得到最大限度的保障");
        // mTVTips.setText("问题多别嫌烦，手机掉了怎么办？安全问题可以最大限度保障您的资金安全哦。");
        mETAnswer = (EditText) mContentView.findViewById(R.id.et_answer);
        mQuestionKey = (TextView) mContentView.findViewById(R.id.question_key);
        //initSpinner();
    }


    private void initSpinner() {
        mSpinner = (Spinner) mContentView.findViewById(R.id.sp_question);
        mAdapter = new ArrayAdapter<SafeQuestion>(
                mContext, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(R.layout.drop_down_item);
        mSpinner.setAdapter(mAdapter);
        updateQuestion();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.safeq_next:// 下一步
                onNextClick();
                break;
        }
    }

    private void onNextClick() {
        String answer = mETAnswer.getText().toString();
        if (TextUtils.isEmpty(answer)) {
            ViewHub.setEditError(mETAnswer, "答案不能为空");
            return;
        }
        updateAnswers();
        new Task(Step.SET_SAFEQ).execute();
//        if (mCurIndex == 2) {
//            new Task(Step.SET_SAFEQ).execute();
//            return;
//        }
//        mCurIndex = (mCurIndex + 1) % mSafeQs.size();
//        updateQuestion();
    }

    /**
     * @description 保存答案
     * @created 2014-9-2 下午3:59:45
     * @author ZZB
     */
    private void updateAnswers() {
        SafeQuestion qa = (SafeQuestion) mSpinner.getSelectedItem();
        qa.setAnswer(mETAnswer.getText().toString());
        mQNAs.put(mCurIndex, qa);
       // mETAnswer.setText("");
    }

    /**
     * @description 更新sp问题
     * @created 2014-9-2 下午4:18:53
     * @author ZZB
     */
    private void updateQuestion() {
       // mQuestionKey.setText("问题" + (mCurIndex + 1) + "/3");
        mCurQs = mSafeQs.get(mCurIndex);
        mAdapter.clear();
        mAdapter.addAll(mCurQs);
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

    private class Task extends AsyncTask<Void, Void, Object> {

        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            switch (mStep) {
                case SET_SAFEQ:
                    if (mDialog != null)
                        mDialog.start("保存中...");
                    break;
                case CHECK_HAS_SAFEQ:
                    if (mDialog != null)
                        mDialog.start();
                    break;
                case ALL_SECURITY:
                    if (mDialog != null)
                        mDialog.start("加载安全问题数据");
                    break;
            }
        }

        @Override
        protected Object doInBackground(Void... params) {
            try {
                switch (mStep) {
                    case SET_SAFEQ:
                        PaymentAPI.setSafeQuestions(mContext, mQNAs);
                        break;
                    case ALL_SECURITY:
                        SafeBean bean = PaymentAPI.getAllSecurityQstList(mContext);
                        List<SafeBean.QuestionListBean> questionListBeen1 = bean.getQuestionList1();
                        List<SafeBean.QuestionListBean> questionListBeen2 = bean.getQuestionList2();
                        List<SafeBean.QuestionListBean> questionListBeen3 = bean.getQuestionList3();
                        int rad = (int) (Math.random() * 2);
                        switch (rad) {
                            case 0:
                                mSafeQs = SafeQuestionsProvider.getNewQuestions(questionListBeen1);

                                break;
                            case 1:
                                mSafeQs = SafeQuestionsProvider.getNewQuestions(questionListBeen2);

                                break;
                            case 2:
                                mSafeQs = SafeQuestionsProvider.getNewQuestions(questionListBeen3);
                                break;
                            default:
                                mSafeQs = SafeQuestionsProvider.getNewQuestions(questionListBeen1);
                                break;
                        }
                        break;
                    default:
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
            mDialog.stop();
            if (result instanceof String && ((String) result).startsWith("error:")) {
                ViewHub.showLongToast(mContext, ((String) result).replace("error:", ""));
            } else {
                switch (mStep) {
                    case SET_SAFEQ:
                        UserInfoProvider.setHasSafeQuestion(mContext, SpManager.getUserId(mContext));
                        if (mListener != null) {
                            mListener.onFinish(SetSafeQuestionsFragment.class);
                        }
                        break;
                    case ALL_SECURITY:
                        initSpinner();
                       // updateQuestion();
                        break;
                    default:
                        break;
                }
            }
        }
    }
//    @Override
//    public void finish() {
//        super.finish();
//        ViewHub.hideKeyboard(this);
//    }
}
