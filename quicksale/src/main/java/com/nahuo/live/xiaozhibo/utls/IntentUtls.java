package com.nahuo.live.xiaozhibo.utls;

import android.content.Context;
import android.content.Intent;

import com.nahuo.live.xiaozhibo.common.utils.TCConstants;
import com.nahuo.live.xiaozhibo.permission.FloatingVideoService;

/**
 * Created by jame on 2019/6/3.
 */

public class IntentUtls {
    public static Intent floatingVideoService;
    public static Intent floatingVideoPlayService;

    public static Intent getLiveIntent(Context Vthis, String mPlayUrl) {
        if (floatingVideoService == null) {
            floatingVideoService = new Intent(Vthis, FloatingVideoService.class)
                    .putExtra(TCConstants.PLAY_URL, mPlayUrl)
                    .putExtra(TCConstants.PLAY_TYPE, true);
        } else {
            floatingVideoService.putExtra(TCConstants.PLAY_URL, mPlayUrl)
                    .putExtra(TCConstants.PLAY_TYPE, true);
        }
        return floatingVideoService;
    }

    public static Intent getLiveIntent() {
        return floatingVideoService;
    }

    public static Intent getPlayIntent() {
        return floatingVideoPlayService;
    }

    public static Intent getPlayIntent(Context Vthis, String mPlayUrl, int changId, int playProgress) {
        if (floatingVideoPlayService == null) {
            floatingVideoPlayService = new Intent(Vthis, FloatingVideoService.class)
                    .putExtra(TCConstants.PLAY_URL, mPlayUrl)
                    .putExtra(TCConstants.PLAY_TYPE, false)
                    .putExtra(TCConstants.PLAY_CHANG_ID, changId)
                    .putExtra(TCConstants.PLAY_PROGRESS, playProgress);
        } else {
            floatingVideoPlayService.putExtra(TCConstants.PLAY_URL, mPlayUrl)
                    .putExtra(TCConstants.PLAY_TYPE, false)
                    .putExtra(TCConstants.PLAY_CHANG_ID, changId)
                    .putExtra(TCConstants.PLAY_PROGRESS, playProgress);
        }
        return floatingVideoPlayService;
    }
}
