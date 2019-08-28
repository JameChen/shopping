package com.nahuo.quicksale.common;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.nahuo.quicksale.BaseInfoActivity;
import com.nahuo.quicksale.activity.PinHuoDetailListActivity;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.activity.ItemPreview1Activity;
import com.nahuo.quicksale.oldermodel.PinHuoModel;
import com.nahuo.quicksale.wxapi.WXEntryActivity;

/***
 * @description View相关小工具
 * @created 2015年5月5日 下午3:42:33
 * @author JorsonWong
 */
public class ViewUtil {

    /***
     * @description 获取View的Bitmap
     * @created 2015年5月5日 下午3:42:29
     * @author JorsonWong
     */
    public static Bitmap shotView(View view) {

        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        if (bmp != null) {
            return bmp;
        }
        return null;
    }

    public static void gotoMarketChangci(final Context context, final PinHuoModel item) {
        if (!TextUtils.isEmpty(item.getUrl())) {
            judeUrl(context, item);
        } else {
            if (item.getVisitResult().isCanVisit()) {
                PinHuoDetailListActivity.launch(context, item, !item.IsStart);
            } else {
                judeMarketVisit(context, item);
            }
        }

    }

    private static void judeMarketVisit(final Context context, PinHuoModel item) {
        switch (item.getVisitResult().getResultType()) {
            case 1: {//需要实名认证
                int statuId = SpManager.getStatuId(context);
                switch (statuId) {
                    case 0:
                    case 3: {//未认证/认证失败
                        renZheng(context, "您需要进行实体认证才能进入本场次！");
                        break;
                    }
                    case 1:
                    case 4: {//审核中/冻结
                        ViewHub.showLongToast(context, "您提交的实体认证正在审核中/未通过，暂时不能进行本场次！");
                        break;
                    }
                }
                break;
            }
            case 2: {//积分不够
                String msg = item.getVisitResult().getMessage();
                if (msg.length() <= 0) {
                    msg = "抱歉,您的积分不足以进入本场次！";
                }
                ViewHub.showTextDialog(context, "提示", msg, "确定", "如何拿看货分", new ViewHub.EditDialogListener() {
                    @Override
                    public void onOkClick(DialogInterface dialog, EditText editText) {

                    }

                    @Override
                    public void onOkClick(EditText editText) {

                    }

                    @Override
                    public void onNegativecClick() {
                        int topicID = 102894;
                        Intent intent = new Intent(context, PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                        context.startActivity(intent);
                    }
                });
                break;
            }
            case 3: {//控货
                String msg = item.getVisitResult().getMessage();
                if (msg.length() <= 0) {
                    msg = "本场已进行区域控货，您店铺所在区域无看款权限！";
                }
                ViewHub.showOkDialog(context, "提示", msg, "知道了");
                break;
            }
            default: {
//                ViewHub.showOkDialog(context, "提示", item.getVisitResult().getMessage(), "知道了", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        context.startActivity(new Intent(context, WXEntryActivity.class));
//                    }
//                });
                ViewHub.showTextDialog(context, "提示", item.getVisitResult().getMessage(), "登录","算了", new ViewHub.EditDialogListener() {
                    @Override
                    public void onOkClick(DialogInterface dialog, EditText editText) {
                        context.startActivity(new Intent(context, WXEntryActivity.class));
                    }

                    @Override
                    public void onOkClick(EditText editText) {
                        context.startActivity(new Intent(context, WXEntryActivity.class));
                    }

                    @Override
                    public void onNegativecClick() {
                    }
                });
                break;
            }
        }
    }

    private static void judeUrl(Context context, PinHuoModel item) {
        if (item.Url.indexOf("/xiaozu/topic/") > 1) {
            String temp = "/xiaozu/topic/";
            int topicID = Integer.parseInt(item.Url.substring(item.Url.indexOf(temp) + temp.length()));

            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra(PostDetailActivity.EXTRA_TID, topicID);
            intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
            context.startActivity(intent);
        } else if (item.Url.indexOf("/xiaozu/act/") > 1) {
            String temp = "/xiaozu/act/";
            int actID = Integer.parseInt(item.Url.substring(item.Url.indexOf(temp) + temp.length()));

            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra(PostDetailActivity.EXTRA_TID, actID);
            intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.ACTIVITY);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, ItemPreview1Activity.class);
            if (item.getOpenStatu().getStatu().equals("预告")) {
                intent.putExtra("name", "拼货预告");
            } else if (item.getOpenStatu().getStatu().equals("开拼中")) {
                intent.putExtra("name", "开拼中");
            } else if (item.getOpenStatu().getStatu().equals("已结束")) {
                intent.putExtra("name", "已结束");
            }
            intent.putExtra("url", item.Url);
            context.startActivity(intent);

        }
    }

    public static void gotoChangci(final Context context, final PinHuoModel item) {

        if (item.getVisitResult().isCanVisit()) {
            PinHuoDetailListActivity.launch(context, item, !item.IsStart);
        } else {
            judeMarketVisit(context, item);
        }

    }

    private static void renZheng(final Context context, String content) {
        ViewHub.showTextDialog(context, "", content, "立即认证", "取消", new ViewHub.EditDialogListener() {
            @Override
            public void onOkClick(DialogInterface dialog, EditText editText) {

            }

            @Override
            public void onOkClick(EditText editText) {
                Intent baseInfo = new Intent(context, BaseInfoActivity.class);
                context.startActivity(baseInfo);
            }

            @Override
            public void onNegativecClick() {

            }
        });
    }
}
