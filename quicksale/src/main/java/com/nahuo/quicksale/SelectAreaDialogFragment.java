package com.nahuo.quicksale;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nahuo.quicksale.db.AreaDao;
import com.nahuo.quicksale.oldermodel.Address;
import com.nahuo.quicksale.oldermodel.Area;

/**
 * @description 选择区域
 * @created 2014-8-28 下午2:59:50
 * @author ZZB
 */
public class SelectAreaDialogFragment extends DialogFragment implements OnItemSelectedListener, View.OnClickListener{

    public static final String TAG = SelectAreaDialogFragment.class.getSimpleName();
    private static final String ARG_ADDRESS = "ARGS_ADDRESS";
    private Context mContext;
    private AreaDao mDao;
    private Spinner mSpProvince, mSpCity, mSpArea;
    private View mBtnCancle, mBtnConfirm;
    private List<Area> mProvincies, mCities, mAreas;
    private Address mAddress;
    private int mOnSelectedCounter = 0;
    private DailogResultListener mListener;
    
    
    
    public static interface DailogResultListener{
        /** dialog选择后的结果 */
        void onResult(Address address);
    }
    

    public static SelectAreaDialogFragment newInstance(Address address) {
        SelectAreaDialogFragment f = new SelectAreaDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ADDRESS, address);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Dialog);
        mContext = getActivity();
        mDao = new AreaDao(mContext);
        mProvincies = mDao.getAreas(0);
        mAddress = (Address) getArguments().getSerializable(ARG_ADDRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dlg_select_area, container, false);
        initView(contentView);
        return contentView;
    }

    private void initView(View contentView) {
        mBtnCancle = contentView.findViewById(R.id.btn_cancle);
        mBtnCancle.setOnClickListener(this);
        mBtnConfirm = contentView.findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(this);
        
        mSpProvince = (Spinner) contentView.findViewById(R.id.sp_province);
        mSpCity = (Spinner) contentView.findViewById(R.id.sp_city);
        mSpArea = (Spinner) contentView.findViewById(R.id.sp_area);
        
        mSpProvince.setAdapter(newAdapter(mProvincies));
        
        mSpProvince.setOnItemSelectedListener(this);
        mSpCity.setOnItemSelectedListener(this);
        mSpArea.setOnItemSelectedListener(this);
        
        if(mAddress != null){
            int pos = getSelection(mProvincies, mAddress.getProvince().getId());
            mSpProvince.setSelection(pos);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mOnSelectedCounter ++;
        if(mOnSelectedCounter < 3 && mAddress != null){
            handleFirstLoad();
            return;
        }
        int parentId = -1;
        switch (parent.getId()) {
        case R.id.sp_province:
            parentId = mProvincies.get(position).getId();
            mCities = mDao.getAreas(parentId);
            mSpCity.setAdapter(newAdapter(mCities));
            break;
        case R.id.sp_city:
            parentId = mCities.get(position).getId();
            mAreas = mDao.getAreas(parentId);
            mSpArea.setAdapter(newAdapter(mAreas));
            break;
        case R.id.sp_area:
            break;
        }
    }

    /**
     * @description 处理第一次加载
     * @created 2014-8-29 上午9:58:00
     * @author ZZB
     */
    private void handleFirstLoad() {
        
        if(mOnSelectedCounter == 1){
            mCities = mDao.getAreas(mAddress.getProvince().getId());
            int cityPos = getSelection(mCities, mAddress.getCity().getId());
            mSpCity.setAdapter(newAdapter(mCities));
            mSpCity.setSelection(cityPos);
        }else if(mOnSelectedCounter == 2){
            mAreas = mDao.getAreas(mAddress.getCity().getId());
            int areaPos = getSelection(mAreas, mAddress.getArea().getId());
            mSpArea.setAdapter(newAdapter(mAreas));
            mSpArea.setSelection(areaPos);
        }
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }

    private ArrayAdapter<Area> newAdapter(List<Area> data){
        ArrayAdapter<Area> adapter = new ArrayAdapter<Area>(mContext, android.R.layout.simple_spinner_item, data);;
        adapter.setDropDownViewResource(R.layout.drop_down_item);
        return adapter;
    }
    
    private int getSelection(List<Area> area, int id){
        int counter = 0;
        for(Area a : area){
            if(a.getId() == id){
                return counter;
            }
            counter++;
        }
        return counter;
    }


    public void setListener(DailogResultListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
        case R.id.btn_cancle:
            dismiss();
            break;
        case R.id.btn_confirm:
            if(mListener != null){
                Area province = (Area) mSpProvince.getSelectedItem();
                Area city = (Area) mSpCity.getSelectedItem();
                Area area = (Area) mSpArea.getSelectedItem();
                Address add = new Address(province, city, area);
                mListener.onResult(add);
            }
            dismiss();
            break;
        }
    }

}
