package com.nahuo.quicksale.hyphenate.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.nahuo.library.helper.ImageUrlExtends;
import com.nahuo.quicksale.common.Const;
import com.nahuo.quicksale.hyphenate.model.EaseDingMessageHelper;
import com.nahuo.quicksale.hyphenate.utils.EaseSmileUtils;
import com.nahuo.quicksale.oldermodel.ShopItemModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;
    View view;
    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
       view= inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        String mess = "";
        if (isItemMessage(txtBody)) {
            if (iv_shop != null) {
                iv_shop.setVisibility(VISIBLE);
                ShopItemModel itemModel = getItemMessage(txtBody);
                if (itemModel != null) {
                    mess = itemModel.getIntro();
                    if (!TextUtils.isEmpty(itemModel.getCover())) {
                        // 商品图片
                        Picasso.with(context).load(ImageUrlExtends.getImageUrl(itemModel.getCover(), Const.LIST_ITEM_SIZE)).into(iv_shop);
                    }else {
                        iv_shop.setImageResource(com.nahuo.quicksale.R.drawable.empty_photo);
                    }
                }
            }
        } else {
            if (iv_shop != null) {
                iv_shop.setVisibility(GONE);
            }
            mess = txtBody.getMessage();
        }
        Spannable span = EaseSmileUtils.getSmiledText(context, mess);
        // 设置内容
        contentView.setText(span, BufferType.SPANNABLE);
    }

    // 根据emmessage对象返回这个对象对应的商品对象，如果不是商品对象，则返回null
    private ShopItemModel getItemMessage(EMTextMessageBody txtBody) {
        if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
            String itemJson = txtBody.getMessage().substring(4, txtBody.getMessage().length() - 1);
            JSONObject itemJsonObject;
            try {
                itemJsonObject = new JSONObject(itemJson);

                ShopItemModel item = new ShopItemModel();
                item.ID = itemJsonObject.getInt("id");
                item.setCover(itemJsonObject.getString("cover"));
                item.setIntro(itemJsonObject.getString("intro"));

                return item;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 判断emmessage是不是商品消息
    private boolean isItemMessage(EMTextMessageBody txtBody) {
        if (txtBody.getMessage().startsWith("[商品:") && txtBody.getMessage().endsWith("]")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        switch (msg.status()) {
            case CREATE:
                onMessageCreate();
                break;
            case SUCCESS:
                onMessageSuccess();
                break;
            case FAIL:
                onMessageError();
                break;
            case INPROGRESS:
                onMessageInProgress();
                break;
        }
    }

    public void onAckUserUpdate(final int count) {
        if (ackedView != null) {
            ackedView.post(new Runnable() {
                @Override
                public void run() {
                    ackedView.setVisibility(VISIBLE);
                    ackedView.setText(String.format(getContext().getString(R.string.group_ack_read_count), count));
                }
            });
        }
    }

    private void onMessageCreate() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private void onMessageSuccess() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.GONE);

        // Show "1 Read" if this msg is a ding-type msg.
        if (EaseDingMessageHelper.get().isDingMessage(message) && ackedView != null) {
            ackedView.setVisibility(VISIBLE);
            List<String> userList = EaseDingMessageHelper.get().getAckUsers(message);
            int count = userList == null ? 0 : userList.size();
            ackedView.setText(String.format(getContext().getString(R.string.group_ack_read_count), count));
        }

        // Set ack-user list change listener.
        EaseDingMessageHelper.get().setUserUpdateListener(message, userUpdateListener);
    }

    private void onMessageError() {
        progressBar.setVisibility(View.GONE);
        statusView.setVisibility(View.VISIBLE);
    }

    private void onMessageInProgress() {
        progressBar.setVisibility(View.VISIBLE);
        statusView.setVisibility(View.GONE);
    }

    private EaseDingMessageHelper.IAckUserUpdateListener userUpdateListener =
            new EaseDingMessageHelper.IAckUserUpdateListener() {
                @Override
                public void onUpdate(List<String> list) {
                    onAckUserUpdate(list.size());
                }
            };
}
