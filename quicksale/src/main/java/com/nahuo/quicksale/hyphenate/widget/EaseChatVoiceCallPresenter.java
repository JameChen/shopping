package com.nahuo.quicksale.hyphenate.widget;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.nahuo.quicksale.hyphenate.presenter.EaseChatRowPresenter;
import com.nahuo.quicksale.hyphenate.widget.chatrow.EaseChatRow;

/**
 * Created by zhangsong on 17-10-12.
 */

public class EaseChatVoiceCallPresenter extends EaseChatRowPresenter {
    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new ChatRowVoiceCall(cxt, message, position, adapter);
    }
}
