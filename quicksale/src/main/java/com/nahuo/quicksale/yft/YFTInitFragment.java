package com.nahuo.quicksale.yft;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.nahuo.quicksale.R;
import com.nahuo.quicksale.event.OnFragmentFinishListener;

public class YFTInitFragment extends Fragment{

    public static final String ARGS_LISTENER = "ARGS_LISTENER";
    private OnFragmentFinishListener mListener;
    private View mContentView;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.frgm_yft_init, container,
                false);
        mListener = getArguments().getParcelable(ARGS_LISTENER);
        initView();
        return mContentView;
    }

    private void initView() {
        View open = mContentView.findViewById(R.id.yft_btn_open);
        open.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFinish(YFTInitFragment.class);
            }
        });
    }

    
    
}
