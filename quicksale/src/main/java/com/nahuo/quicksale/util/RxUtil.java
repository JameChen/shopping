package com.nahuo.quicksale.util;

import android.text.TextUtils;

import com.google.gson.internal.LinkedTreeMap;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.model.http.exception.ApiException;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by codeest on 2016/8/3.
 */
public class RxUtil {
    public static String Result_OK = "Result_OK";
    public static String PASSWORD_ERROR = "password_error";
    public static String USER_NO_EXIST = "user_no_exist";
    public static String NOT_REGISTERED="not_registered";
    public static final int HttpErrorCode_401 = 401;
    public static final int HttpErrorCode_404 = 404;
    public static final int HttpErrorCode_405 = 405;
    public static final int HttpErrorCode_403 = 403;
    public static final int HttpErrorCode_500 = 500;
    public static final int HttpErrorCode_504 = 504;
    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<T, T> rxSchedulerHelper() {    //compose简化线程
        return new FlowableTransformer<T, T>() {
            @Override
            public Flowable<T> apply(Flowable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 生成Flowable
     *
     * @param <T>
     * @return
     */
    public static <T> Flowable<T> createData(final T t) {
        return Flowable.create(new FlowableOnSubscribe<T>() {
            @Override
            public void subscribe(FlowableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(t);
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        }, BackpressureStrategy.BUFFER);
    }

    /**
     * 统一返回结果处理
     *
     * @param <T>
     * @return
     */
    public static <T> FlowableTransformer<PinHuoResponse<T>, T> handleResult() {   //compose判断结果
        return new FlowableTransformer<PinHuoResponse<T>, T>() {
            @Override
            public Flowable<T> apply(Flowable<PinHuoResponse<T>> httpResponseFlowable) {
                return httpResponseFlowable.flatMap(new Function<PinHuoResponse<T>, Flowable<T>>() {
                    @Override
                    public Flowable<T> apply(PinHuoResponse<T> tGankHttpResponse) {
                        if (tGankHttpResponse.isResult()) {
                            if (tGankHttpResponse.getData() == null) {
                                if (!TextUtils.isEmpty(tGankHttpResponse.getMessage())) {
                                    return (Flowable<T>) createData(tGankHttpResponse.getMessage());
                                } else {
                                    return (Flowable<T>) createData(Result_OK);
                                }
                            }else if ( tGankHttpResponse.getData() instanceof LinkedTreeMap){
                                LinkedTreeMap data= (LinkedTreeMap) tGankHttpResponse.getData();
                                if (ListUtils.isEmpty(data)){
                                    if (!TextUtils.isEmpty(tGankHttpResponse.getMessage())) {
                                        return (Flowable<T>) createData(tGankHttpResponse.getMessage());
                                    } else {
                                        return (Flowable<T>) createData(Result_OK);
                                    }
                                }
                            }
                            return createData(tGankHttpResponse.getData());
                        } else {
                            return Flowable.error(new ApiException(tGankHttpResponse.getMessage(),tGankHttpResponse.getCode()));
                        }
                    }
                });
            }
        };
    }
}
