package com.nahuo.quicksale.yft;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.event.OnFragmentFinishListener;

public class AgreementFragment extends Fragment{

    private View mContentView;
	public static final String ARGS_LISTENER = "ARGS_LISTENER";
    private OnFragmentFinishListener mListener;
    private Button showAllContentBtn;
    private View showAllContent;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView = inflater.inflate(R.layout.frgm_agreement, container, false);
		mListener = getArguments().getParcelable(ARGS_LISTENER);
		handleConfirmBtn();
		return mContentView;
	}
	/**
	 * Description 处理同意开通相关
	 *2014-7-1下午4:42:52
	 */
	private void handleConfirmBtn() {
		View btn = mContentView.findViewById(R.id.yft_agreement_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    mListener.onFinish(AgreementFragment.class);
			}

		});

		 showAllContentBtn = (Button) mContentView.findViewById(R.id.yft_agreement_full_content_btn);
		 showAllContent = mContentView.findViewById(R.id.yft_agreement_full_content);
		showAllContentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			    v.setTag(showAllContent.getVisibility());
			    boolean isContentVisible = ((Integer)v.getTag()) == View.VISIBLE ? true : false;
                showAllContentBtn.setText(isContentVisible ?  "查看全部>" : "收起");
                showAllContent.setVisibility(isContentVisible ? View.GONE : View.VISIBLE);
			}

		});
	}
}
