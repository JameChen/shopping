package com.nahuo.quicksale.hyphenate.presenter;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.nahuo.quicksale.hyphenate.widget.chatrow.EaseChatRow;
import com.nahuo.quicksale.hyphenate.widget.chatrow.EaseChatRowBigExpression;

/**
 * Created by zhangsong on 17-10-12.
 */

public class EaseChatBigExpressionPresenter extends EaseChatTextPresenter {
    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowBigExpression(cxt, message, position, adapter);
    }

    @Override
    protected void handleReceiveMessage(EMMessage message) {
    }
}
