package com.nahuo.quicksale.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DebugUtil;
import com.nahuo.Dialog.PicMenu;
import com.nahuo.Dialog.ProblemTypeDialog;
import com.nahuo.bean.ApplyDetailBean;
import com.nahuo.bean.ApplyUpdateBean;
import com.nahuo.bean.ColorSizeBean;
import com.nahuo.bean.DetailForAddBean;
import com.nahuo.bean.ProblemListBean;
import com.nahuo.library.controls.LoadingDialog;
import com.nahuo.library.controls.NoScrollListView;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.PicGalleryActivity;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.ShipLogActivity;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.adapter.ColorSizeDetailAdapter;
import com.nahuo.quicksale.adapter.GridImageAdapter;
import com.nahuo.quicksale.adapter.SaleAfterHistoryAdapter;
import com.nahuo.quicksale.adapter.SimpleItemTouchHelperCallback;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.api.SaleAfterApi;
import com.nahuo.quicksale.base.BaseActivty;
import com.nahuo.quicksale.common.AfterStatus;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.util.ChatUtl;
import com.nahuo.quicksale.util.DateUtls;
import com.nahuo.quicksale.util.ImageUtls;
import com.nahuo.upyun.UpYunConst;
import com.nahuo.upyun.UpYunNewUtls;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.luck.picture.lib.PictureSelector.obtainMultipleResult;
import static com.nahuo.upyun.UpYunConst.OPERATER;
import static com.upyun.library.common.Params.SAVE_KEY;

/**
 * 售后
 *
 * @author James Chen
 * @create time in 2017/8/24 10:43
 */
public class CustomerServiceActivity extends BaseActivty implements View.OnClickListener, ProblemTypeDialog.PopDialogListener, HttpRequestListener, ColorSizeDetailAdapter.TotalListener {
    private static final String TAG = "CustomerServiceActivity";
    LoadingDialog mLoadingDialog;
    private static final String ERROR_PREFIX = "error:";
    //售后单ID
    public static final String EXTRA_APPLYID = "EXTRA_APPLYID";
    //清单ID
    public static final String EXTRA_ORDERID = "EXTRA_ORDERID";
    //包裹id
    public static final String EXTRA_SHIPPINGID = "EXTRA_SHIPPINGID";
    //传递类型
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    public static final int TYPE_AFTER_SALES_APPLY = 1;//填写售后申请表
    public static final int TYPE_AFTER_SALES_APPLY_DETAIL = 2;//申请中提交成功
    private int orderID, shippingID, type, ID;
    private Intent intent;
    private View layout_top, layout_reject, layout_refund, layout_customer_message, layout_adress_order,
            layout_show_order, layout_customer_return_order, layout_processing_log, layout_refund_type, layout_shop_problem, layout_shop_detail, layout_edit_order;
    //商品数据
    private EditText et_problem_description;
    private ImageView iv_shop_pic, iv_return_pic;
    private TextView tv_shop_title, tv_shop_price, tv_shop_num, tv_shop_total_price, tv_refund_type, tv_question_type;
    private NoScrollListView lv_shop_size_color, lv_processing_log;
    private RecyclerView recycler_pics;
    private GridImageAdapter adapter;
    private int picSelectMaxNum;
    private int maxSelectNum = 3;
    private List<LocalMedia> picAllList = new ArrayList<>();
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<LocalMedia> singleList = new ArrayList<>();
    public static final int REQUEST_SINGLE_PIC = 200;
    //头部
    private TextView tv_top_content, tv_top_time, tv_reject_reason, tv_customer_message, tv_refund_detail, tv_order_code, tv_copy_code;
    private TextView tv_show_reback_adress, tv_show_contact, tv_show_phone;
    private TextView tv_edit_company, tv_edit_express_num, tv_edit_mail_time;
    private TextView tv_return_company, tv_return_express_num, tv_return_fee, tv_return_mail_time;
    private ImageView iv_upload_coiorder_order;
    private View ll_del;
    private EditText et_courier_company, et_courier_num, et_freight_num;


    private Button btn_submit, btn_edit_submit, btn_contact_aftermarket, btn_look_logistics;
    private TextView tv_title, tv_right;
    private HttpRequestHelper httpRequestHelper = new HttpRequestHelper();
    private DetailForAddBean detailForAddBean;
    private ApplyDetailBean applyDetailBean;
    private List<ColorSizeBean> colorSizeBeanList = new ArrayList<>();
    private ColorSizeDetailAdapter colorSizeDetailAdapter;
    private List<ApplyDetailBean.HistoryInfoBean.ListBeanX> historyList = new ArrayList<>();
    private SaleAfterHistoryAdapter historyAdapter;

    private double price;
    private TextView tv_pic_voucher, tv_pic_voucher_des;
    private CustomerServiceActivity vThis;
    private String problemDetail = "", express_pic = "";
    private int applyID, statusID;//售后单ID
    private View sl_view;
    String ex_cover = "", top_code = "";
    private boolean is_Edit_Order = false;

    private static enum Step {
        STEP_APPLY, STEP_UPDATE, STEP_UPDATER_ETURN_EXPRESS
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service);
        vThis = this;
        initExtra();
        initView();
        initData();
    }


    private void initExtra() {
        intent = getIntent();
        if (intent != null) {
            orderID = intent.getIntExtra(EXTRA_ORDERID, 0);
            shippingID = intent.getIntExtra(EXTRA_SHIPPINGID, 0);
            type = intent.getIntExtra(EXTRA_TYPE, 1);
            applyID = intent.getIntExtra(EXTRA_APPLYID, 0);
        }
    }

    RequestOptions options;

    private void initView() {
        mLoadingDialog = new LoadingDialog(this);
        options = new RequestOptions()
                .placeholder(R.drawable.empty_photo)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(160, 160);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        btn_edit_submit = (Button) findViewById(R.id.btn_edit_submit);
        btn_edit_submit.setOnClickListener(this);
        tv_title.setText(R.string.sale_after_apply);
        tv_right.setText("售后流程");
        tv_right.setOnClickListener(this);
        findViewById(R.id.iv_left).setOnClickListener(this);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        layout_top = findViewById(R.id.layout_top);
        layout_reject = findViewById(R.id.layout_reject);
        layout_refund = findViewById(R.id.layout_refund);
        layout_shop_detail = findViewById(R.id.layout_shop_detail);
        layout_customer_message = findViewById(R.id.layout_customer_message);
        layout_adress_order = findViewById(R.id.layout_adress_order);
        layout_show_order = findViewById(R.id.layout_show_order);
        layout_customer_return_order = findViewById(R.id.layout_customer_return_order);
        layout_processing_log = findViewById(R.id.layout_processing_log);
        layout_refund_type = findViewById(R.id.layout_refund_type);
        ll_del = findViewById(R.id.ll_del);
        ll_del.setOnClickListener(this);
        sl_view = findViewById(R.id.sl_view);
        judeExprePic();
        tv_copy_code = (TextView) findViewById(R.id.tv_copy_code);
        tv_copy_code.setOnClickListener(this);
        tv_order_code = (TextView) findViewById(R.id.tv_order_code);
        tv_top_content = (TextView) findViewById(R.id.tv_top_content);
        tv_top_time = (TextView) findViewById(R.id.tv_top_time);
        tv_reject_reason = (TextView) findViewById(R.id.tv_reject_reason);
        tv_customer_message = (TextView) findViewById(R.id.tv_customer_message);
        tv_refund_detail = (TextView) findViewById(R.id.tv_refund_detail);
        tv_show_reback_adress = (TextView) findViewById(R.id.tv_show_reback_adress);
        tv_show_contact = (TextView) findViewById(R.id.tv_show_contact);
        tv_show_phone = (TextView) findViewById(R.id.tv_show_phone);
        tv_edit_company = (TextView) findViewById(R.id.tv_edit_company);
        tv_edit_express_num = (TextView) findViewById(R.id.tv_edit_express_num);
        tv_edit_mail_time = (TextView) findViewById(R.id.tv_edit_mail_time);

        tv_return_company = (TextView) findViewById(R.id.tv_return_company);
        tv_return_express_num = (TextView) findViewById(R.id.tv_return_express_num);
        tv_return_fee = (TextView) findViewById(R.id.tv_return_fee);
        tv_return_mail_time = (TextView) findViewById(R.id.tv_return_mail_time);
        btn_contact_aftermarket = (Button) findViewById(R.id.btn_contact_aftermarket);
        btn_look_logistics = (Button) findViewById(R.id.btn_look_logistics);
        btn_contact_aftermarket.setOnClickListener(this);
        btn_look_logistics.setOnClickListener(this);
        // layout_refund_type.setOnClickListener(this);
        layout_shop_problem = findViewById(R.id.layout_shop_problem);
        layout_edit_order = findViewById(R.id.layout_edit_order);
        layout_shop_problem.setOnClickListener(this);
        tv_pic_voucher = (TextView) findViewById(R.id.tv_pic_voucher);
        tv_pic_voucher_des = (TextView) findViewById(R.id.tv_pic_voucher_des);
        tv_question_type = (TextView) findViewById(R.id.tv_question_type);
        et_problem_description = (EditText) findViewById(R.id.et_problem_description);
        et_courier_company = (EditText) findViewById(R.id.et_courier_company);
        et_courier_num = (EditText) findViewById(R.id.et_courier_num);
        et_freight_num = (EditText) findViewById(R.id.et_freight_num);

        iv_shop_pic = (ImageView) findViewById(R.id.iv_shop_pic);
        iv_return_pic = (ImageView) findViewById(R.id.iv_return_pic);
        iv_return_pic.setOnClickListener(this);
        iv_upload_coiorder_order = (ImageView) findViewById(R.id.iv_upload_coiorder_order);
        iv_upload_coiorder_order.setOnClickListener(this);
        tv_shop_title = (TextView) findViewById(R.id.tv_shop_title);
        tv_shop_price = (TextView) findViewById(R.id.tv_shop_price);
        tv_shop_num = (TextView) findViewById(R.id.tv_shop_num);
        lv_shop_size_color = (NoScrollListView) findViewById(R.id.lv_shop_size_color);
        lv_processing_log = (NoScrollListView) findViewById(R.id.lv_processing_log);
        tv_shop_total_price = (TextView) findViewById(R.id.tv_shop_total_price);
        tv_refund_type = (TextView) findViewById(R.id.tv_refund_type);
        colorSizeDetailAdapter = new ColorSizeDetailAdapter(this);
        colorSizeDetailAdapter.setOnTotalListener(this);
        lv_shop_size_color.setAdapter(colorSizeDetailAdapter);
        historyAdapter = new SaleAfterHistoryAdapter(this);
        lv_processing_log.setAdapter(historyAdapter);

        initRecyView();
        judeType();
    }

    private void judeExprePic() {
        if (TextUtils.isEmpty(express_pic)) {
            ll_del.setVisibility(View.GONE);
        } else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.icon_pingzheng)
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
            Glide.with(vThis)
                    .load(express_pic)
                    .apply(options)
                    .into(iv_upload_coiorder_order);
            ll_del.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyView() {
        recycler_pics = (RecyclerView) findViewById(R.id.recycler_pics);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(vThis, 4, GridLayoutManager.VERTICAL, false);
        recycler_pics.setLayoutManager(manager);
        adapter = new GridImageAdapter(vThis, onAddPicClickListener);
        adapter.setList(picAllList);
        adapter.setSelectMax(maxSelectNum);
        recycler_pics.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);  //用Callback构造ItemtouchHelper
        touchHelper.attachToRecyclerView(recycler_pics);//调用ItemTouchHelper的attachToRecyclerView方法建立联系
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (picAllList.size() > 0) {
                    LocalMedia media = picAllList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(vThis).externalPicturePreview(position, picAllList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(vThis).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(vThis).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            // 进入相册 以下是例子：不需要的api可以不写
            int count = 0;
            List<LocalMedia> pList = new ArrayList<>();
            if (!ListUtils.isEmpty(picAllList)) {
                for (LocalMedia media : picAllList) {
                    if (!media.is_upload()) {
                        pList.add(media);
                    } else {
                        count++;
                    }
                }
            }
            picSelectMaxNum = maxSelectNum - count;
            PictureSelector.create(vThis)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(picSelectMaxNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(4)// 每行显示个数
                    .selectionMode(
                            PictureConfig.MULTIPLE)// 多选 or 单选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(false)// 是否可预览视频
                    .enablePreviewAudio(false) // 是否可播放音频
                    .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                    .isCamera(false)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                    .enableCrop(false)// 是否裁剪
                    .compress(false)// 是否压缩
                    .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                    .compressGrade(Luban.THIRD_GEAR)
                    //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(4, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(false)// 是否显示gif图片
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .circleDimmedLayer(false)// 是否圆形裁剪
                    .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                    .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                    .openClickSound(false)// 是否开启点击声音
                    .selectionMedia(pList)// 是否传入已选图片
                    //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                    .compressMaxKB(200)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                    .compressWH(1100, 1100) // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                    //.rotateEnabled() // 裁剪是否可旋转图片
                    //.scaleEnabled()// 裁剪是否可放大缩小图片
                    //.videoQuality()// 视频录制质量 0 or 1
                    //.videoSecond()//显示多少秒以内的视频or音频也可适用
                    //.recordVideoSecond()//录制视频秒数 默认60s
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

        }

    };

    /**
     * /storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1501476603502.jpg
     * /storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1501476589531.jpg
     * /storage/emulated/0/tencent/MicroMsg/WeiXin/wx_camera_1501461829261.jpg
     * <p>
     * 接收从其他界面返回的数据
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    List<LocalMedia> list = new ArrayList<>();
                    if (!ListUtils.isEmpty(picAllList)) {
                        for (LocalMedia xmedia : picAllList) {
                            if (xmedia.is_upload()) {
                                list.add(xmedia);
                            }
                        }
                    }
                    picAllList.clear();
                    if (list.size() > 0)
                        picAllList.addAll(list);
                    picAllList.addAll(selectList);
                    adapter.setList(picAllList);
                    adapter.notifyDataSetChanged();
                    DebugUtil.i(TAG, "onActivityResult:" + selectList.size());
                    break;
                case REQUEST_SINGLE_PIC:
                    singleList = PictureSelector.obtainMultipleResult(data);
                    if (!ListUtils.isEmpty(singleList)) {
                        express_pic = singleList.get(0).getPath();
                        judeExprePic();
                    }
                    break;
            }
        }
    }

    private void initData() {
        switch (type) {
            case TYPE_AFTER_SALES_APPLY:
                SaleAfterApi.getDetailForAdd(this, httpRequestHelper, this, orderID, shippingID);
                break;
            case TYPE_AFTER_SALES_APPLY_DETAIL:
                SaleAfterApi.getDetailForApplySucess(vThis, httpRequestHelper, this, applyID + "");
                break;
        }
    }

    private void judeType() {
        switch (type) {
            case TYPE_AFTER_SALES_APPLY:
                layout_top.setVisibility(View.GONE);
                layout_reject.setVisibility(View.GONE);
                layout_refund.setVisibility(View.GONE);
                layout_customer_message.setVisibility(View.GONE);
                layout_adress_order.setVisibility(View.GONE);
                layout_edit_order.setVisibility(View.GONE);
                layout_show_order.setVisibility(View.GONE);
                layout_customer_return_order.setVisibility(View.GONE);
                layout_processing_log.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
                layout_shop_problem.setEnabled(true);
                tv_pic_voucher.setText("上传凭证");
                tv_pic_voucher_des.setVisibility(View.VISIBLE);
                break;
            case TYPE_AFTER_SALES_APPLY_DETAIL:
//                layout_top.setVisibility(View.VISIBLE);
//                layout_reject.setVisibility(View.GONE);
//                layout_refund.setVisibility(View.GONE);
//                layout_customer_message.setVisibility(View.GONE);
//                layout_adress_order.setVisibility(View.GONE);
//                layout_show_order.setVisibility(View.GONE);
//                layout_customer_return_order.setVisibility(View.GONE);
//                layout_processing_log.setVisibility(View.VISIBLE);
//                .setVisibility(View.GONE);
//                layout_shop_problem.setEnabled(false);
//                tv_pic_voucher.setText("图片凭证");
                break;
        }
    }

    String exPressName = "", exPressCode = "";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_copy_code:
                Utils.addNewToClipboard(vThis, top_code);
                break;
            case R.id.tv_right:
                Intent xintent = new Intent(this, PostDetailActivity.class);
                xintent.putExtra(PostDetailActivity.EXTRA_TID, 104180);
                xintent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                this.startActivity(xintent);
//                String temp = "/xiaozu/topic/";
//                int topicID = 104180;
//                Intent xintent = new Intent(this, PostDetailActivity.class);
//                xintent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
//                xintent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
//                startActivity(xintent);
//                String temp = "/xiaozu/act/";
//                int actID = 104180;
//                Intent xintent = new Intent(this, PostDetailActivity.class);
//                xintent.putExtra(PostDetailActivity.EXTRA_TID, actID);
//                xintent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
//                startActivity(xintent);
                break;
            case R.id.iv_return_pic:
                Intent pintent = new Intent(vThis, PicGalleryActivity.class);
                pintent.putExtra(PicGalleryActivity.EXTRA_CUR_POS, 0);
                ArrayList<String> list = new ArrayList<>();
                list.add(ex_cover);
                pintent.putStringArrayListExtra(PicGalleryActivity.EXTRA_URLS, list);
                startActivity(pintent);
                break;
            case R.id.btn_contact_aftermarket:
                ChatUtl.goToChatActivity(this);
                break;
            case R.id.btn_look_logistics:
                Intent shipIntent = new Intent(vThis, ShipLogActivity.class);
                shipIntent.putExtra("name", exPressName);
                shipIntent.putExtra("code", exPressCode);
                vThis.startActivity(shipIntent);
                break;
            case R.id.iv_left:
                finish();
                break;
            case R.id.layout_shop_problem:
                //选择问题类型
                ProblemTypeDialog.getInstance(this).setID(problemId).setList(problemListBeanList).setPositive(vThis).showDialog();
                break;
            case R.id.btn_submit:
                if (is_Edit_Order) {
                    updateReturnExpress();
                } else {
                    submitData();
                }
                break;
            case R.id.btn_edit_submit:
                updateReturnExpress();
                break;
            case R.id.iv_upload_coiorder_order:
                if (TextUtils.isEmpty(express_pic)) {
                    PictureSelector.create(vThis)
                            .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                            .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                            .maxSelectNum(picSelectMaxNum)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .imageSpanCount(4)// 每行显示个数
                            .selectionMode(
                                    PictureConfig.SINGLE)// 多选 or 单选
                            .previewImage(true)// 是否可预览图片
                            .previewVideo(false)// 是否可预览视频
                            .enablePreviewAudio(false) // 是否可播放音频
                            .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                            .isCamera(false)// 是否显示拍照按钮
                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                            .enableCrop(false)// 是否裁剪
                            .compress(false)// 是否压缩
                            .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)//系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                            .compressGrade(Luban.THIRD_GEAR)
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .withAspectRatio(4, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                            .isGif(false)// 是否显示gif图片
                            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                            .circleDimmedLayer(false)// 是否圆形裁剪
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .openClickSound(false)// 是否开启点击声音
                            .selectionMedia(singleList)// 是否传入已选图片
                            //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                            //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                            .compressMaxKB(200)//压缩最大值kb compressGrade()为Luban.CUSTOM_GEAR有效
                            .compressWH(1100, 1100) // 压缩宽高比 compressGrade()为Luban.CUSTOM_GEAR有效
                            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                            //.rotateEnabled() // 裁剪是否可旋转图片
                            //.scaleEnabled()// 裁剪是否可放大缩小图片
                            //.videoQuality()// 视频录制质量 0 or 1
                            //.videoSecond()//显示多少秒以内的视频or音频也可适用
                            //.recordVideoSecond()//录制视频秒数 默认60s
                            .forResult(REQUEST_SINGLE_PIC);//结果回调onActivityResult code
                } else {
                    PictureSelector.create(vThis).externalPicturePreview(0, singleList);
                }
                break;
            case R.id.ll_del:
                express_pic = "";
                iv_upload_coiorder_order.setImageResource(R.drawable.icon_pingzheng);
                judeExprePic();
                break;
        }
    }

    String company_name = "", code = "", fee = "";

    private void updateReturnExpress() {
        company_name = et_courier_company.getText().toString();
        code = et_courier_num.getText().toString();
        fee = et_freight_num.getText().toString();
        if (TextUtils.isEmpty(fee)) {
            fee = "0";
        }
        if (TextUtils.isEmpty(express_pic)) {
            ViewHub.showShortToast(vThis, "请添加快递单图片");
        } else if (TextUtils.isEmpty(company_name)) {
            ViewHub.showShortToast(vThis, "请输入公司名字");
        } else if (TextUtils.isEmpty(code)) {
            ViewHub.showShortToast(vThis, "请输入快递单");
        }
//        else if (TextUtils.isEmpty(fee)){
//            ViewHub.showShortToast(vThis,"请输入垫付运费");
//        }
        else {
            try {
                menu = PicMenu.getInstance(vThis);
                menu.dShow("正在上传...", 0);
                String useid = SpManager.getUserId(vThis) + "";
                picName = System.currentTimeMillis() + ".jpg";
                picPath = "/" + useid + "/item/" + picName;
                date = DateUtls.rfc1123Format.format(new Date());
                expiration = System.currentTimeMillis() / 1000 + 1000 * 5 * 10;
                File temp = null;
                temp = ImageUtls.commPressNoWater(express_pic, picName);
                contentMd5 = UpYunUtils.md5Hex(temp);
                policy = UpYunNewUtls.makeNewPolicy(date, contentMd5, picPath, expiration, UpYunConst.PIC_BUCKET, null);
                signature = UpYunNewUtls.getSignature(UpYunConst.PIC_BUCKET, policy, date, contentMd5);
                Map<String, Object> paramsMap = new HashMap<>();
                //上传空间
                paramsMap.put(Params.BUCKET, UpYunConst.PIC_BUCKET);
                //保存路径，任选其中一个
                paramsMap.put(SAVE_KEY, picPath);
                paramsMap.put(Params.CONTENT_MD5, contentMd5);
                //可选参数（详情见api文档介绍）
                //  paramsMap.put(Params.RETURN_URL, "httpbin.org/post");
                // UploadEngine.getInstance().formUpload(temp, paramsMap, OPERATER, UpYunUtils.md5(UpYunConst.PASSWORD), completeListener, progressListener);
                UploadEngine.getInstance()
                        .formUpload(temp, policy, OPERATER, signature, sinCompleteListener, sinProgressListener);
            } catch (Exception e) {
                e.printStackTrace();
                if (menu != null) {
                    menu.dismiss();
                    menu = null;
                }
            }
        }
    }

    String picPath = "", picName = "", date = "", contentMd5 = "", policy = "", signature = "";
    long expiration;
    int index;
    int vIndex;

    public boolean judeColorSizeAppyNums(List<ColorSizeBean> list) {
        boolean flag = false;
        if (!ListUtils.isEmpty(list)) {
            for (ColorSizeBean bean : list) {
                if (bean.getApplyQty() > 0) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    //提交數據
    private void submitData() {
        if (!judeColorSizeAppyNums(colorSizeBeanList)) {
            ViewHub.showShortToast(this, "请选择退货商品数量");
        } else {
            if (hasPics(picAllList)) {
                //有需要上传图片
                menu = PicMenu.getInstance(vThis);
                menu.dShow("正在上传....", 0);
                index = getNoUploadIndex(picAllList);
                getRecurPic(index);
            } else {
                //提交
                applyData();


            }

        }
    }

    private void getRecurPic(int i) {
        try {
            index = i;
            String useid = SpManager.getUserId(vThis) + "";
            picName = System.currentTimeMillis() + ".jpg";
            picPath = "/" + useid + "/item/" + picName;
            date = DateUtls.rfc1123Format.format(new Date());
            expiration = System.currentTimeMillis() / 1000 + 1000 * 5 * 10;
            File temp = null;
            temp = ImageUtls.commPressNoWater(picAllList.get(i).getPath(), picName);
            contentMd5 = UpYunUtils.md5Hex(temp);
            policy = UpYunNewUtls.makeNewPolicy(date, contentMd5, picPath, expiration, UpYunConst.PIC_BUCKET, null);
            signature = UpYunNewUtls.getSignature(UpYunConst.PIC_BUCKET, policy, date, contentMd5);
            Map<String, Object> paramsMap = new HashMap<>();
            //上传空间
            paramsMap.put(Params.BUCKET, UpYunConst.PIC_BUCKET);
            //保存路径，任选其中一个
            paramsMap.put(SAVE_KEY, picPath);
            paramsMap.put(Params.CONTENT_MD5, contentMd5);
            //可选参数（详情见api文档介绍）
            //  paramsMap.put(Params.RETURN_URL, "httpbin.org/post");
            // UploadEngine.getInstance().formUpload(temp, paramsMap, OPERATER, UpYunUtils.md5(UpYunConst.PASSWORD), completeListener, progressListener);
            UploadEngine.getInstance()
                    .formUpload(temp, policy, OPERATER, signature, completeListener, progressListener);
        } catch (Exception e) {
            e.printStackTrace();
            if (menu != null) {
                menu.dismiss();
                menu = null;
            }
        }

    }

    //获取需要上传下标
    public int getNoUploadIndex(List<LocalMedia> list) {
        int index = 0;
        if (!ListUtils.isEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).is_upload()) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public boolean hasPics(List<LocalMedia> list) {
        boolean flag = false;
        if (ListUtils.isEmpty(list)) {
            flag = false;
        } else {
            for (LocalMedia bean : list) {
                if (!bean.is_upload()) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    PicMenu menu = null;
    //图片进度回调，可为空
    UpProgressListener progressListener = new UpProgressListener() {
        @Override
        public void onRequestProgress(final long bytesWrite, final long contentLength) {
            menu.dShow("正在上传" + (index + 1) + "/" + picAllList.size(), (int) (bytesWrite * 100 / contentLength));
        }
    };
    //图片结束回调，不可为空
    UpCompleteListener completeListener = new UpCompleteListener() {
        @Override
        public void onComplete(boolean isSuccess, String result) {
            if (isSuccess) {
                picAllList.get(index).setIs_upload(true);
                String serverPath = "upyun:" + UpYunConst.PIC_BUCKET + ":/" + picPath;
                picAllList.get(index).setPath(serverPath);
                if (adapter != null)
                    adapter.notifyDataSetChanged();
                //删除水印图片
                ImageUtls.delFile(picName);
                if (hasPics(picAllList)) {
                    //有需要上传图片
                    index = getNoUploadIndex(picAllList);
                    getRecurPic(index);
                } else {
                    //提交数据
                    if (menu != null) {
                        menu.dismiss();
                        menu = null;
                    }
                    applyData();
                }
            } else {
                ViewHub.showShortToast(vThis, "图片又拍云上传第" + (index + 1) + "张图片上传失败");
                if (menu != null) {
                    menu.dismiss();
                    menu = null;
                }
            }
        }
    };

    //图片进度回调，可为空
    UpProgressListener sinProgressListener = new UpProgressListener() {
        @Override
        public void onRequestProgress(final long bytesWrite, final long contentLength) {
            menu.dShow("正在上传" + (index + 1) + "/" + singleList.size(), (int) (bytesWrite * 100 / contentLength));
        }
    };
    String sinServerPath = "";
    //图片结束回调，不可为空
    UpCompleteListener sinCompleteListener = new UpCompleteListener() {
        @Override
        public void onComplete(boolean isSuccess, String result) {
            if (isSuccess) {
                sinServerPath = "upyun:" + UpYunConst.PIC_BUCKET + ":/" + picPath;
                //删除水印图片
                ImageUtls.delFile(picName);
                //提交数据
                if (menu != null) {
                    menu.dismiss();
                    menu = null;
                }
                new Task(Step.STEP_UPDATER_ETURN_EXPRESS).execute();

            } else {
                ViewHub.showShortToast(vThis, "图片又拍云上传第" + (index + 1) + "张图片上传失败");
                if (menu != null) {
                    menu.dismiss();
                    menu = null;
                }
            }
        }
    };

    //提交售后单
    private void applyData() {
        problemDetail = et_problem_description.getText().toString().trim();
        try {
            switch (type) {
                case TYPE_AFTER_SALES_APPLY:
                    new Task(Step.STEP_APPLY).execute();
                    break;
                case TYPE_AFTER_SALES_APPLY_DETAIL:
                    new Task(Step.STEP_UPDATE).execute();
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Task extends AsyncTask<Object, Void, Object> {
        private Step mStep;

        public Task(Step step) {
            mStep = step;
        }

        @Override
        protected void onPreExecute() {
            switch (mStep) {
                case STEP_APPLY:
                    mLoadingDialog.start("提交数据中...");
                    break;
                case STEP_UPDATE:
                    mLoadingDialog.start("修改数据中...");
                    break;
                case STEP_UPDATER_ETURN_EXPRESS:
                    mLoadingDialog.start("上传快递单中...");
                    break;

            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                switch (mStep) {

                    case STEP_APPLY:
                        int id = SaleAfterApi.applyDefective(vThis, orderID, shippingID, problemId, problemDetail, colorSizeBeanList, picAllList);

                        return id;
                    case STEP_UPDATE:
                        String bean = SaleAfterApi.updateDefective(vThis, ID, problemId, problemDetail, colorSizeBeanList, picAllList);
                        return bean;
                    case STEP_UPDATER_ETURN_EXPRESS:
                        String result = SaleAfterApi.updateReturnExpress(vThis, ID, company_name, code, fee, sinServerPath);
                        return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "error:" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.stop();
            }
            if (result instanceof String && ((String) result).startsWith(ERROR_PREFIX)) {
                String msg = ((String) result).replace(ERROR_PREFIX, "");
                ViewHub.showLongToast(vThis, msg);
                return;
            }
            switch (mStep) {
                case STEP_APPLY:
                    ViewHub.showShortToast(vThis, "提交成功");
                    SaleAfterApi.getDetailForApplySucess(vThis, httpRequestHelper, vThis, (int) result + "");
                    break;
                case STEP_UPDATE:
                    ViewHub.showShortToast(vThis, "修改成功");
                    // ApplyUpdateBean bean = (ApplyUpdateBean) result;
                    // changView(bean);
                    SaleAfterApi.getDetailForApplySucess(vThis, httpRequestHelper, vThis, ID + "");
                    break;
                case STEP_UPDATER_ETURN_EXPRESS:
                    ViewHub.showShortToast(vThis, "上传成功");
                    SaleAfterApi.getDetailForApplySucess(vThis, httpRequestHelper, vThis, ID + "");
                    break;

            }
        }
    }

    private void changView(ApplyUpdateBean bean) {
        if (bean != null) {
            et_problem_description.setText(bean.getProblemDetail() + "");

        }

    }


    @Override
    public void onRequestStart(String method) {
        switch (method) {
            case RequestMethod.SaleAfterMethod.GET_DETAIL_FOR_ADD:
                mLoadingDialog.start("获取商品信息...");
                break;
            case RequestMethod.SaleAfterMethod.GET_APPY_SUCCESSDETAIL:
                mLoadingDialog.start("获取商品信息...");
                break;
        }
    }

    String cover = "";
    String problemName = "", reMark = "";
    int problemId;
    List<ProblemListBean> problemListBeanList = new ArrayList<>();

    @Override
    public void onRequestSuccess(String method, Object object) {
        mLoadingDialog.stop();
        switch (method) {
            case RequestMethod.SaleAfterMethod.GET_DETAIL_FOR_ADD:
                //detailForAddBean=  GsonHelper.jsonToObject(object.toString(),DetailForAddBean.class);
                detailForAddBean = (DetailForAddBean) object;
                if (detailForAddBean != null) {
                    cover = detailForAddBean.getCover();
                    price = detailForAddBean.getPrice();
                    Glide.with(this).load(ImageUrlExtends.getImageUrl(cover, 11)).apply(options).into(iv_shop_pic);
                    if (!TextUtils.isEmpty(detailForAddBean.getName())) {
                        tv_shop_title.setText(detailForAddBean.getName());
                    }
                    tv_shop_price.setText("¥" + price);
                    tv_shop_num.setText("单号：" + detailForAddBean.getOrderCode());
                    if (!ListUtils.isEmpty(detailForAddBean.getProductList())) {
                        for (DetailForAddBean.ProductListBean productListBean : detailForAddBean.getProductList()) {
                            ColorSizeBean colorSizeBean = new ColorSizeBean();
                            colorSizeBean.setColor(productListBean.getColor());
                            colorSizeBean.setSize(productListBean.getSize());
                            colorSizeBean.setQty(productListBean.getQty());
                            colorSizeBean.setProductID(productListBean.getProductID());
                            if (productListBean.getQty() > 0) {
                                colorSizeBean.setApplyQty(0);
                            } else {
                                colorSizeBean.setApplyQty(0);
                            }
                            colorSizeBeanList.add(colorSizeBean);
                        }
                        colorSizeDetailAdapter.setData(colorSizeBeanList);
                        colorSizeDetailAdapter.notifyDataSetChanged();
                    }
                    if (!ListUtils.isEmpty(detailForAddBean.getProblemList())) {
                        problemListBeanList.clear();
                        problemListBeanList.addAll(detailForAddBean.getProblemList());
                        problemId = problemListBeanList.get(0).getID();
                        reMark = problemListBeanList.get(0).getRemark();
                        problemName = problemListBeanList.get(0).getName();
                        tv_question_type.setText(problemName);
                        tv_pic_voucher_des.setText(reMark);
                    }
                    sl_view.setVisibility(View.VISIBLE);
                }
                break;
            case RequestMethod.SaleAfterMethod.GET_APPY_SUCCESSDETAIL:
                applyDetailBean = (ApplyDetailBean) object;
                // applyDetailBean= GsonHelper.jsonToObject(object.toString(),ApplyDetailBean.class);
                judeStatus(applyDetailBean);
                Log.d(TAG, object.toString());
                break;
        }

    }

    private void judeStatus(ApplyDetailBean applyDetailBean) {
        if (applyDetailBean != null) {
            ApplyDetailBean.BaseInfoBean baseInfoBean = applyDetailBean.getBaseInfo();
            if (baseInfoBean != null) {
                statusID = baseInfoBean.getStatusID();
                ID = baseInfoBean.getID();
                switch (statusID) {
//                    case AfterStatus.STATUS_WAIT:
//                        //提交成功不可编辑
//                        btn_submit.setVisibility(View.GONE);
//                        tv_pic_voucher.setText("图片凭证");
//                        layout_shop_problem.setEnabled(false);
//                        adapter.setIs_Edit(false);
//                        colorSizeDetailAdapter.setHide(true);
//                        et_problem_description.setFocusable(false);
//                        tv_pic_voucher_des.setVisibility(View.GONE);
//                        break;
                    case AfterStatus.STATUS_REJECT_NOT_EDIT:
                        //驳回不可更改
                        btn_submit.setVisibility(View.GONE);
                        tv_pic_voucher.setText("图片凭证");
                        layout_shop_problem.setEnabled(false);
                        adapter.setIs_Edit(false);
                        colorSizeDetailAdapter.setHide(true);
                        et_problem_description.setFocusable(false);
                        tv_pic_voucher_des.setVisibility(View.GONE);
                        break;
                    case AfterStatus.STATUS_REJECT_EDIT:
                        //可以修改
                        btn_submit.setVisibility(View.VISIBLE);
                        tv_pic_voucher.setText("上传凭证");
                        layout_shop_problem.setEnabled(true);
                        adapter.setIs_Edit(true);
                        colorSizeDetailAdapter.setHide(false);
                        et_problem_description.setFocusable(true);
                        tv_pic_voucher_des.setVisibility(View.VISIBLE);
                        break;
                    default:
                        btn_submit.setVisibility(View.GONE);
                        tv_pic_voucher.setText("图片凭证");
                        layout_shop_problem.setEnabled(false);
                        adapter.setIs_Edit(false);
                        colorSizeDetailAdapter.setHide(true);
                        et_problem_description.setFocusable(false);
                        tv_pic_voucher_des.setVisibility(View.GONE);
                        break;
                }
                top_code = baseInfoBean.getOrderCode();
                if (baseInfoBean.isShow()) {
                    layout_top.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(baseInfoBean.getMsg())) {
                        tv_top_content.setText(baseInfoBean.getStatus());
                    } else {
                        tv_top_content.setText(baseInfoBean.getStatus() + "(" + baseInfoBean.getMsg() + ")");
                    }
                    tv_top_time.setText(baseInfoBean.getDate() + "");
                    if (TextUtils.isEmpty(baseInfoBean.getOrderCode())) {
                        tv_order_code.setVisibility(View.GONE);
                    } else {
                        tv_order_code.setVisibility(View.VISIBLE);
                        tv_order_code.setText(baseInfoBean.getOrderCode());
                    }
                } else {
                    layout_top.setVisibility(View.GONE);
                }
            }
            ApplyDetailBean.RejectInfoBean rejectInfoBean = applyDetailBean.getRejectInfo();
            if (rejectInfoBean != null) {
                if (rejectInfoBean.isShow()) {
                    layout_reject.setVisibility(View.VISIBLE);
                    tv_reject_reason.setText(rejectInfoBean.getRejectReason() + "");
                } else {
                    layout_reject.setVisibility(View.GONE);
                }

            }
            ApplyDetailBean.RefundInfoBean refundInfoBean = applyDetailBean.getRefundInfo();
            if (refundInfoBean != null) {
                if (refundInfoBean.isShow()) {
                    layout_refund.setVisibility(View.VISIBLE);
                    tv_refund_type.setText(refundInfoBean.getRefundType() + "");
                    tv_refund_detail.setText(refundInfoBean.getMoneyDetail() + "");
                    if (TextUtils.isEmpty(refundInfoBean.getCustomerMsg())) {
                        layout_customer_message.setVisibility(View.GONE);
                    } else {
                        layout_customer_message.setVisibility(View.VISIBLE);
                        tv_customer_message.setText(refundInfoBean.getCustomerMsg());
                    }
                } else {
                    layout_refund.setVisibility(View.GONE);
                }
            }
            ApplyDetailBean.ReturnCompanyInfoBean returnCompanyInfoBean = applyDetailBean.getReturnCompanyInfo();
            if (returnCompanyInfoBean != null) {
                if (returnCompanyInfoBean.isShow()) {
                    //添加快递单
                    is_Edit_Order = true;
                    btn_submit.setVisibility(View.VISIBLE);
                    layout_adress_order.setVisibility(View.VISIBLE);
                    layout_edit_order.setVisibility(View.VISIBLE);
                    tv_show_reback_adress.setText(returnCompanyInfoBean.getAddress() + "");
                    tv_show_contact.setText(returnCompanyInfoBean.getUserName() + "");
                    tv_show_phone.setText(returnCompanyInfoBean.getPhone() + "");
                } else {
                    is_Edit_Order = false;
                    layout_adress_order.setVisibility(View.GONE);
                    layout_edit_order.setVisibility(View.GONE);
                }
            }
            ApplyDetailBean.ExchangeExpressInfoBean exchangeExpressInfoBean = applyDetailBean.getExchangeExpressInfo();
            if (exchangeExpressInfoBean != null) {
                exPressName = exchangeExpressInfoBean.getCompany();
                exPressCode = exchangeExpressInfoBean.getCode();
                if (exchangeExpressInfoBean.isShow()) {
                    layout_show_order.setVisibility(View.VISIBLE);
                    tv_edit_company.setText(exchangeExpressInfoBean.getCompany() + "");
                    tv_edit_express_num.setText(exchangeExpressInfoBean.getCode() + "");
                    tv_edit_mail_time.setText(exchangeExpressInfoBean.getCreateDate() + "");
                } else {
                    layout_show_order.setVisibility(View.GONE);
                }
            }
            ApplyDetailBean.ProductInfoBean productInfoBean = applyDetailBean.getProductInfo();
            if (productInfoBean != null) {
                if (productInfoBean.isShow()) {
                    layout_shop_detail.setVisibility(View.VISIBLE);
                    cover = productInfoBean.getCover();
                    price = productInfoBean.getPrice();
                    Glide.with(this).load(ImageUrlExtends.getImageUrl(cover, 11)).apply(options).into(iv_shop_pic);
                    if (!TextUtils.isEmpty(productInfoBean.getName())) {
                        tv_shop_title.setText(productInfoBean.getName());
                    }
                    tv_shop_price.setText("¥" + price);
                    tv_shop_num.setText("单号：" + productInfoBean.getOrderCode());
                    if (!ListUtils.isEmpty(productInfoBean.getList())) {
                        colorSizeBeanList.clear();
                        int total_num = 0;
                        for (ApplyDetailBean.ProductInfoBean.ListBean productListBean : productInfoBean.getList()) {
                            ColorSizeBean colorSizeBean = new ColorSizeBean();
                            colorSizeBean.setColor(productListBean.getColor());
                            colorSizeBean.setSize(productListBean.getSize());
                            colorSizeBean.setQty(productListBean.getQty());
                            colorSizeBean.setApplyQty(productListBean.getApplyQty());
                            colorSizeBean.setProductID(productListBean.getProductID());
                            total_num = total_num + productListBean.getApplyQty();
                            colorSizeBeanList.add(colorSizeBean);
                        }
                        colorSizeDetailAdapter.setData(colorSizeBeanList);
                        colorSizeDetailAdapter.notifyDataSetChanged();
                        tv_shop_total_price.setText("共计：¥" + FunctionHelper.DoubleTwoFormat(price * total_num));
                    }
                } else {
                    layout_shop_detail.setVisibility(View.GONE);

                }
            }
            ApplyDetailBean.ProblemInfoBean problemInfoBean = applyDetailBean.getProblemInfo();
            if (problemInfoBean != null) {
                if (problemInfoBean.isShow()) {
                    layout_shop_problem.setVisibility(View.VISIBLE);
                } else {
                    layout_shop_problem.setVisibility(View.GONE);
                }
                if (!ListUtils.isEmpty(problemInfoBean.getProblemList())) {
                    problemListBeanList.clear();
                    problemListBeanList.addAll(problemInfoBean.getProblemList());
                    problemId = problemInfoBean.getSelectedID();
                    for (ProblemListBean bean : problemInfoBean.getProblemList()) {
                        if (problemId == bean.getID()) {
                            reMark = bean.getRemark();
                            problemName = bean.getName();
                            break;
                        }
                    }
                    tv_question_type.setText(problemName);
                    tv_pic_voucher_des.setText(reMark);
                }
                if (problemInfoBean != null) {
                    if (TextUtils.isEmpty(problemInfoBean.getProblemDetail())) {
                        if (!et_problem_description.isFocusable()) {
                            et_problem_description.setText("暂无描述");
                        }
                    } else {
                        et_problem_description.setText(problemInfoBean.getProblemDetail() + "");
                    }
                }
                if (!ListUtils.isEmpty(problemInfoBean.getImages())) {
                    picAllList.clear();
                    for (String pic : problemInfoBean.getImages()) {
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setIs_upload(true);
                        localMedia.setPath(pic);
                        picAllList.add(localMedia);
                    }
                    if (adapter != null)
                        adapter.notifyDataSetChanged();
                }
            }
            ApplyDetailBean.ReturnExpressInfoBean returnExpressInfoBean = applyDetailBean.getReturnExpressInfo();
            if (returnExpressInfoBean != null) {
                if (returnExpressInfoBean.isShow()) {
                    layout_customer_return_order.setVisibility(View.VISIBLE);
                    tv_return_company.setText(returnExpressInfoBean.getCompany() + "");
                    tv_return_express_num.setText(returnExpressInfoBean.getCode() + "");
                    tv_return_fee.setText("¥" + returnExpressInfoBean.getFee() + "");
                    tv_return_mail_time.setText(returnExpressInfoBean.getCreateDate() + "");

                    ex_cover = returnExpressInfoBean.getImage();
                    Glide.with(this).load(ImageUrlExtends.getImageUrl(ex_cover, 11)).apply(options).into(iv_return_pic);

                } else {
                    layout_customer_return_order.setVisibility(View.GONE);
                }
            }
            ApplyDetailBean.HistoryInfoBean historyInfoBean = applyDetailBean.getHistoryInfo();
            if (historyInfoBean != null) {
                if (historyInfoBean.isShow()) {
                    if (!ListUtils.isEmpty(historyInfoBean.getList())) {
                        historyList.clear();
                        historyList.addAll(historyInfoBean.getList());
                        historyAdapter.setData(historyList);
                        historyAdapter.notifyDataSetChanged();
                    }
                    layout_processing_log.setVisibility(View.VISIBLE);
                } else {
                    layout_processing_log.setVisibility(View.GONE);
                }


            }
            sl_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestFail(String method, int statusCode, String msg) {
        mLoadingDialog.stop();
    }

    @Override
    public void onRequestExp(String method, String msg, ResultData data) {
        mLoadingDialog.stop();
    }

    @Override
    public void onGetTotalClick(int applyqty) {
        if (tv_shop_total_price != null)
            tv_shop_total_price.setText("共计：¥" + FunctionHelper.DoubleTwoFormat(price * applyqty));
    }

    @Override
    public void onProblemCategoryDialogButtonClick(int parentID, String pName, String reMark) {
        //问题类型
        if (parentID > 0) {
            problemId = parentID;
            tv_question_type.setText(pName);
            tv_pic_voucher_des.setText(reMark);
        }
    }
}
