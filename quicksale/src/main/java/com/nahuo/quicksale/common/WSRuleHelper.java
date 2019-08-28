package com.nahuo.quicksale.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nahuo.library.controls.LightAlertDialog;
import com.nahuo.quicksale.R;
import com.nahuo.quicksale.Topic.PostDetailActivity;
import com.nahuo.quicksale.Topic.TopicPageActivity;
import com.nahuo.quicksale.ViewHub;
import com.nahuo.quicksale.oldermodel.AgentGroup;
import com.nahuo.quicksale.oldermodel.ReturnData;

/**
 * Created by 诚 on 2015/9/21.
 */

public class WSRuleHelper {

    private static final String TAG = WSRuleHelper.class.getSimpleName();

    /**
     * @description 小组权限许可中心处理类
     * @author PJ
     */
    public static boolean doRule(final Context context, final AgentGroup.CanType resultData, final int gid, String join, View.OnClickListener l) {
        if (resultData == null) {
            DialogUtils.showSureCancelDialog(context, "非法权限", "知道了", null, null);
            return false;
        }
        if (resultData.isIsSuccess()) {
            return true;
        } else {
            if (resultData.getCode().equals("limit_member")) {// 没加入小组
                DialogUtils.showSureCancelDialog(context, resultData.getMessage(), join, "立即加入", l);
            } else if (resultData.getCode().equals("buyer_level_too_low") || // 买家等级不够
                    resultData.getCode().equals("seller_level_too_low") || // 卖家等级不够
                    resultData.getCode().equals("user_level_too_low") || // 用户社区等级不足
                    resultData.getCode().equals("action_over_max_times") || // 次数超过上限
                    resultData.getCode().equals("XXXX")) { // 小黑屋
                String urlText = "";
                if (resultData.getCode().equals("buyer_level_too_low")) // 买家等级不够
                {
                    urlText = "如何提高买家级别？>";
                } else if (resultData.getCode().equals("seller_level_too_low")) // 卖家等级不够
                {
                    urlText = "如何提高卖家级别？>";
                } else if (resultData.getCode().equals("user_level_too_low")
                        || resultData.getCode().equals("action_over_max_times")) // 用户社区等级不足 // 次数超过上限
                {
                    urlText = "如何提升社区级别？>";
                } else if (resultData.getCode().equals("XXX")) {
                    urlText = "补习社区行为准则？>";
                }
                showRuleGuidDialog(context, resultData.getMessage(), urlText, resultData.getData().getID() + "");

            }
//            else if (resultData.getCode().equals("limit_shop_identity")) {// 仅限实体认证用户
//                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
//            } else if (resultData.getCode().equals("limit_seller_identity")) {// 仅限卖家身份
//                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
//            }
            else //其他
            {
                DialogUtils.showSureCancelDialog(context, null, resultData.getMessage(), "知道了", null, null);
            }
            return false;
        }
    }

    /**
     * @description 小组权限许可中心处理类
     * @author PJ
     */
    public static boolean doRule(final Context context, final AgentGroup.CanType resultData, final int gid) {
        if (resultData == null) {
            ViewHub.showOkDialog(context, "提示", "非法权限", "知道了");
            return false;
        }
        if (resultData.isIsSuccess()) {
            return true;
        } else {
            if (resultData.getCode().equals("limit_member")) {// 没加入小组
//                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "OK");
                ViewHub.showTextDialog(context, "提示", resultData.getMessage(), new ViewHub.EditDialogListener() {

                    @Override
                    public void onOkClick(DialogInterface dialog, EditText editText) {
                        Intent intent = new Intent(context, TopicPageActivity.class);
                        intent.putExtra("gid", gid);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onOkClick(EditText editText) {

                        Intent intent = new Intent(context, TopicPageActivity.class);
                        intent.putExtra("gid", gid);
                        context.startActivity(intent);


                    }

                    @Override
                    public void onNegativecClick() {
                    }
                });
            } else if (resultData.getCode().equals("buyer_level_too_low") || // 买家等级不够
                    resultData.getCode().equals("seller_level_too_low") || // 卖家等级不够
                    resultData.getCode().equals("user_level_too_low") || // 用户社区等级不足
                    resultData.getCode().equals("action_over_max_times") || // 次数超过上限
                    resultData.getCode().equals("XXXX")) { // 小黑屋
                String urlText = "";
                if (resultData.getCode().equals("buyer_level_too_low")) // 买家等级不够
                {
                    urlText = "如何提高买家级别？>";
                } else if (resultData.getCode().equals("seller_level_too_low")) // 卖家等级不够
                {
                    urlText = "如何提高卖家级别？>";
                } else if (resultData.getCode().equals("user_level_too_low")
                        || resultData.getCode().equals("action_over_max_times")) // 用户社区等级不足 // 次数超过上限
                {
                    urlText = "如何提升社区级别？>";
                } else if (resultData.getCode().equals("XXX")) {
                    urlText = "补习社区行为准则？>";
                }

                showRuleGuidDialog(context, resultData.getMessage(), urlText, resultData.getData().getID() + "");

            } else if (resultData.getCode().equals("limit_shop_identity")) {// 仅限实体认证用户
                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
            } else if (resultData.getCode().equals("limit_seller_identity")) {// 仅限卖家身份
                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
            } else //其他
            {
                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
            }
            return false;
        }
    }

    /**
     * @description 小组权限许可中心处理类
     * @author PJ
     */
    public static void doError(final Context context, final ReturnData resultData) {
        if (resultData == null) {
            ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
            return;
        }
        if (!resultData.isResult()) {
            if (resultData.getCode().equals("limit_member")) {// 没加入小组
                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "OK");

            } else if (resultData.getCode().equals("buyer_level_too_low") || // 买家等级不够
                    resultData.getCode().equals("seller_level_too_low") || // 卖家等级不够
                    resultData.getCode().equals("user_level_too_low") || // 用户社区等级不足
                    resultData.getCode().equals("action_over_max_times") || // 次数超过上限
                    resultData.getCode().equals("XXXX")) { // 小黑屋
                String urlText = "";
                if (resultData.getCode().equals("buyer_level_too_low")) // 买家等级不够
                {
                    urlText = "如何提高买家级别？>";
                } else if (resultData.getCode().equals("seller_level_too_low")) // 卖家等级不够
                {
                    urlText = "如何提高卖家级别？>";
                } else if (resultData.getCode().equals("user_level_too_low")
                        || resultData.getCode().equals("action_over_max_times")) // 用户社区等级不足 // 次数超过上限
                {
                    urlText = "如何提升社区级别？>";
                } else if (resultData.getCode().equals("XXX")) {
                    urlText = "补习社区行为准则？>";
                }

                showRuleGuidDialog(context, resultData.getMessage(), urlText, resultData.getData() + "");

            } else if (resultData.getCode().equals("limit_shop_identity")) {// 仅限实体认证用户
                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
            } else if (resultData.getCode().equals("limit_seller_identity")) {// 仅限卖家身份
                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
            } else //其他
            {
                ViewHub.showOkDialog(context, "提示", resultData.getMessage(), "知道了");
            }
            return;
        }
    }

    private static void showRuleGuidDialog(final Context context, String title, String urlText,
                                           String urlTopicID) {
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        int margin = context.getResources().getDimensionPixelSize(R.dimen.margin_10);
        param.setMargins(margin, margin, margin, margin);
        AlertDialog.Builder builder = LightAlertDialog.Builder.create(context);
        final LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        final TextView tv = new TextView(context);
        tv.setText(title);
        tv.setTextSize(16);
        tv.setLayoutParams(param);
//        tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        ll.addView(tv);
        final TextView tv1 = new TextView(context);
        tv1.setText(urlText);
//        tv1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
//        tv1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        tv1.setTextColor(context.getResources().getColor(R.color.blue_edittext_border));
        tv1.setLayoutParams(param);
        tv1.setTag(urlTopicID);
        tv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 跳转到指定的帖子
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra(PostDetailActivity.EXTRA_TID, Integer.valueOf(v.getTag().toString()));
                intent.putExtra(PostDetailActivity.EXTRA_LOGO_URL, "");
                intent.putExtra(PostDetailActivity.EXTRA_POST_TITLE, ((TextView) v).getText().toString());
                intent.putExtra(PostDetailActivity.EXTRA_POST_TYPE, Const.PostType.TOPIC);
                context.startActivity(intent);
            }
        });
        ll.addView(tv1);
        builder.setView(ll).setNegativeButton("知道了", null);
        builder.show();
    }

}
