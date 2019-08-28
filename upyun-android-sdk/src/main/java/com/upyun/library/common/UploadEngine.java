package com.upyun.library.common;

import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.AsyncRun;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadEngine {
    private static UploadEngine instance;
    private ExecutorService executor;
    private UploadClient upLoaderClient;

    private UploadEngine() {
        executor = Executors.newFixedThreadPool(UpConfig.CONCURRENCY);
        upLoaderClient = new UploadClient();
    }

    public static UploadEngine getInstance() {
        if (instance == null) {
            synchronized (UploadEngine.class) {
                if (instance == null) {
                    instance = new UploadEngine();
                }
            }
        }
        return instance;
    }

    public void formUpload(final File file, final Map<String, Object> params, String operator, String password, final UpCompleteListener completeListener, final UpProgressListener progressListener) {
        if (params.get(Params.BUCKET) == null) {
            params.put(Params.BUCKET, UpConfig.BUCKET);
        }

        if (params.get(Params.EXPIRATION) == null) {
            params.put(Params.EXPIRATION, System.currentTimeMillis() / 1000 + UpConfig.EXPIRATION);
        }

        UpProgressListener uiProgressListener = new UpProgressListener() {
            @Override
            public void onRequestProgress(final long bytesWrite, final long contentLength) {
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                        if (progressListener != null) {
                            progressListener.onRequestProgress(bytesWrite, contentLength);
                        }
                    }
                });
            }
        };

        UpCompleteListener uiCompleteListener = new UpCompleteListener() {
            @Override
            public void onComplete(final boolean isSuccess, final String result) {
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                        completeListener.onComplete(isSuccess, result);
                    }
                });
            }
        };

        Map<String, Object> localParams = new HashMap<>();
        localParams.putAll(params);
        Runnable uploadRunnable = new FormUploader2(upLoaderClient, file, localParams, operator, password, uiCompleteListener, uiProgressListener);
        executor.execute(uploadRunnable);
    }

    public void formUpload(final File file, final String policy, String operator, String signature, final UpCompleteListener completeListener, final UpProgressListener progressListener) {

        UpProgressListener uiProgressListener = new UpProgressListener() {
            @Override
            public void onRequestProgress(final long bytesWrite, final long contentLength) {
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                        if (progressListener != null) {
                            progressListener.onRequestProgress(bytesWrite, contentLength);
                        }
                    }
                });
            }
        };

        UpCompleteListener uiCompleteListener = new UpCompleteListener() {
            @Override
            public void onComplete(final boolean isSuccess, final String result) {
                AsyncRun.run(new Runnable() {
                    @Override
                    public void run() {
                        completeListener.onComplete(isSuccess, result);
                    }
                });
            }
        };
        Runnable uploadRunnable = new FormUploader2(upLoaderClient, file, policy, operator, signature, uiCompleteListener, uiProgressListener);
        executor.execute(uploadRunnable);
    }
}
