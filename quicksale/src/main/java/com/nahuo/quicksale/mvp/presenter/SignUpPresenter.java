package com.nahuo.quicksale.mvp.presenter;

import android.content.Context;

import com.nahuo.bean.LoginBean;
import com.nahuo.constant.Constant;
import com.nahuo.library.helper.MD5Utils;
import com.nahuo.quicksale.SignUpActivity;
import com.nahuo.quicksale.api.AccountAPI;
import com.nahuo.quicksale.api.HttpRequestHelper;
import com.nahuo.quicksale.api.HttpRequestListener;
import com.nahuo.quicksale.api.RequestMethod;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.base.BaseAppCompatActivity;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.common.Utils;
import com.nahuo.quicksale.di.module.HttpManager;
import com.nahuo.quicksale.model.http.CommonSubscriber;
import com.nahuo.quicksale.model.http.exception.ApiException;
import com.nahuo.quicksale.model.http.response.PinHuoResponse;
import com.nahuo.quicksale.mvp.MvpBasePresenter;
import com.nahuo.quicksale.mvp.RequestData;
import com.nahuo.quicksale.mvp.RequestError;
import com.nahuo.quicksale.mvp.view.SignUpView;
import com.nahuo.quicksale.oldermodel.ResultData;
import com.nahuo.quicksale.oldermodel.ShopInfoModel;
import com.nahuo.quicksale.util.RxUtil;
import com.umeng.analytics.AnalyticsConfig;

/**
 * Created by ZZB on 2015/8/10.
 */
public class SignUpPresenter extends MvpBasePresenter<SignUpView> {
    private HttpRequestHelper mRequestHelper = new HttpRequestHelper();

    public SignUpPresenter(Context context) {
        super(context);
    }

    /**
     * 获取验证码
     *
     * @author ZZB
     * created at 2015/8/10 14:54
     */
    public void getVerifyCode(String phoneNo, String username, String cardid) {
        Context context = null;

        if (isViewAttached() && isContextNotNull()) {
            context = getContext();
        } else {
            return;
        }
        AccountAPI.getSignUpVerifyCode(context, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
                if (isViewAttached()) {
                    getView().showLoading(new RequestData(RequestMethod.UserMethod.GET_SIGN_UP_VERIFY_CODE));
                }
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeSuccess();
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, statusCode, msg, true));
                }
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, msg));
                }
            }
        }, phoneNo, username, cardid);
    }


    /**
     * 获取验证码(新接口用户名后台生成)
     *
     * @author ZZB
     * created at 2015/8/10 14:54
     */
    public void getVerifyCode2(String phoneNo, String cardid) {
        Context context = null;

        if (isViewAttached() && isContextNotNull()) {
            context = getContext();
        } else {
            return;
        }
        AccountAPI.getSignUpVerifyCode2(context, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
                if (isViewAttached()) {
                    getView().showLoading(new RequestData(RequestMethod.UserMethod.NEW_GET_SIGN_UP_VERIFY_CODE));
                }
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeSuccess();
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, statusCode, msg, true));
                }
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, msg));
                }
            }
        }, phoneNo);
    }

    /**
     * 获取验证码(新接口用户名后台生成)
     *
     * @author ZZB
     * created at 2015/8/10 14:54
     */
    public void getVerifyCode3(String phoneNo, String cardid, int smstype) {
        Context context = null;

        if (isViewAttached() && isContextNotNull()) {
            context = getContext();
        } else {
            return;
        }
        AccountAPI.getSignUpVerifyCode3(context, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
                if (isViewAttached()) {
                    getView().showLoading(new RequestData(RequestMethod.UserMethod.NEW_GET_SIGN_UP_VERIFY_CODE));
                }
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeSuccess();
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, statusCode, msg, true));
                }
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, msg));
                }
            }
        }, phoneNo, smstype);
    }


    /**
     * 获取验证码（新接口后台自动生成用户名）
     *
     * @author ZZB
     * created at 2015/8/10 14:54
     */
    public void getVerifyCode(String phoneNo, String cardid) {
        Context context = null;

        if (isViewAttached() && isContextNotNull()) {
            context = getContext();
        } else {
            return;
        }
        AccountAPI.getSignUpVerifyCode2(context, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
                if (isViewAttached()) {
                    getView().showLoading(new RequestData(RequestMethod.UserMethod.NEW_GET_SIGN_UP_VERIFY_CODE));
                }
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeSuccess();
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, statusCode, msg, true));
                }
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onGetVerifyCodeFailed(new RequestError(method, msg));
                }
            }
        }, phoneNo);
    }

    /**
     * 注册用户（新接口）
     *
     * @author ZZB
     * created at 2015/8/10 16:28
     */
    public void signUpUser(BaseAppCompatActivity baseAppCompatActivity, String phoneNo, String psw, String verifyCode, String cardId,String invitationCode) {
        Context context = null;

        if (isViewAttached() && isContextNotNull()) {
            context = getContext();
        } else {
            return;
        }
        if (baseAppCompatActivity == null)
            return;
        baseAppCompatActivity.addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(SignUpActivity.class.getSimpleName()
        ).getRegister2(phoneNo,  MD5Utils.encrypt32bit(psw), true, verifyCode, Constant.LoginRegisterFrom.LOGIN_REGISTER_FROM_ANDROID
                , Utils.GetAndroidImei(BWApplication.getInstance()), AnalyticsConfig.getChannel(BWApplication.getInstance()),invitationCode)
                .compose(RxUtil.<PinHuoResponse<LoginBean>>rxSchedulerHelper())
                .compose(RxUtil.<LoginBean>handleResult())
                .subscribeWith(new CommonSubscriber<LoginBean>(BWApplication.getInstance()) {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (isViewAttached()) {
                            getView().showLoading(new RequestData(RequestMethod.UserMethod.USER_REGISTER2));
                        }
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        super.onNext(loginBean);
                        if (loginBean != null) {
                            SpManager.setUserId(BWApplication.getInstance(), loginBean.getUserID());
                            SpManager.setUserName(BWApplication.getInstance(), loginBean.getUserName());
                        }
                        if (isViewAttached()) {
                            getView().hideLoading(null);
                            getView().onSignUpUserSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.getCode();
                            if (isViewAttached()) {
                                getView().hideLoading(null);
                                getView().onSignUpUserFailed(new RequestError(RequestMethod.UserMethod.USER_REGISTER2, apiException.getCode(), apiException.getMessage(), false));
                            }
                        } else {
                            if (isViewAttached()) {
                                getView().hideLoading(null);
                                String mess="";
                                if (e.getMessage()!=null)
                                    mess=e.getMessage();
                                getView().onSignUpUserFailed(new RequestError(RequestMethod.UserMethod.USER_REGISTER2, "", mess, true));
                            }
                        }

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
//        Params.BaseParams params = new Params.BaseParams(context, mRequestHelper, new HttpRequestListener() {
//            @Override
//            public void onRequestStart(String method) {
//                if (isViewAttached()) {
//                    getView().showLoading(new RequestData(RequestMethod.UserMethod.USER_REGISTER2));
//                }
//            }
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                if (isViewAttached()) {
//                    getView().hideLoading(null);
//                    getView().onSignUpUserSuccess();
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                if (isViewAttached()) {
//                    getView().hideLoading(null);
//                    getView().onSignUpUserFailed(new RequestError(method, statusCode, msg, true));
//                }
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                if (isViewAttached()) {
//                    getView().hideLoading(null);
//                    getView().onSignUpUserFailed(new RequestError(method, msg));
//                }
//            }
//        });
     // AccountAPI.signUpUser(params, phoneNo, psw, verifyCode, cardId);
    }

    /**
     * 注册店铺
     *
     * @author ZZB
     * created at 2015/8/10 16:28
     */
    public void signUpShop(BaseAppCompatActivity appCompatActivity, String phoneNo, String shopName) {
        Context context = null;

        if (isViewAttached() && isContextNotNull()) {
            context = getContext();
        } else {
            return;
        }
        if (appCompatActivity == null)
            return;
        appCompatActivity.addSubscribe(HttpManager.getInstance().getPinHuoNoCacheApi(SignUpActivity.class.getSimpleName()
        ).registerShop(phoneNo, shopName)
                .compose(RxUtil.<PinHuoResponse<ShopInfoModel>>rxSchedulerHelper())
                .compose(RxUtil.<ShopInfoModel>handleResult())
                .subscribeWith(new CommonSubscriber<ShopInfoModel>(BWApplication.getInstance()) {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        if (isViewAttached()) {
                            getView().showLoading(new RequestData(RequestMethod.ShopMethod.REGISTER_SHOP));
                        }
                    }

                    @Override
                    public void onNext(ShopInfoModel shopInfoModel) {
                        super.onNext(shopInfoModel);
                        if (isViewAttached()) {
                            getView().hideLoading(null);
                            getView().onSignUpShopSuccess();
                        }
                        if (shopInfoModel != null) {
                            SpManager.setShopInfo(BWApplication.getInstance(), shopInfoModel);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            apiException.getCode();
                            if (isViewAttached()) {
                                getView().hideLoading(null);
                                getView().onSignUpShopFailed(new RequestError(RequestMethod.ShopMethod.REGISTER_SHOP, apiException.getCode(), apiException.getMessage(), false));
                            }
                        } else {
                            if (isViewAttached()) {
                                getView().hideLoading(null);
                                String mess="";
                                if (e.getMessage()!=null)
                                    mess=e.getMessage();
                                getView().onSignUpShopFailed(new RequestError(RequestMethod.ShopMethod.REGISTER_SHOP,"", mess,true));
                            }
                        }

                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                    }
                }));
//        Params.BaseParams params = new Params.BaseParams(context, mRequestHelper, new HttpRequestListener() {
//            @Override
//            public void onRequestStart(String method) {
//                if (isViewAttached()) {
//                    getView().showLoading(new RequestData(RequestMethod.ShopMethod.REGISTER_SHOP));
//                }
//            }
//
//            @Override
//            public void onRequestSuccess(String method, Object object) {
//                if (isViewAttached()) {
//                    getView().hideLoading(null);
//                    getView().onSignUpShopSuccess();
//                }
//            }
//
//            @Override
//            public void onRequestFail(String method, int statusCode, String msg) {
//                if (isViewAttached()) {
//                    getView().hideLoading(null);
//                    getView().onSignUpShopFailed(new RequestError(method, statusCode, msg, true));
//                }
//            }
//
//            @Override
//            public void onRequestExp(String method, String msg, ResultData data) {
//                if (isViewAttached()) {
//                    getView().hideLoading(null);
//                    getView().onSignUpShopFailed(new RequestError(method, msg));
//                }
//            }
//        });
//        AccountAPI.signUpShop(params, phoneNo, shopName);
    }

    /**
     * 提交验证码，进行校验
     *
     * @author ZZB
     * created at 2015/8/10 15:08
     */
    public void submitVerifyCode(String phoneNo, String verifyCode) {
        Context context = null;

        if (isViewAttached() && isContextNotNull()) {
            context = getContext();
        } else {
            return;
        }
        AccountAPI.validateSignUpVerifyCode(context, mRequestHelper, new HttpRequestListener() {
            @Override
            public void onRequestStart(String method) {
                if (isViewAttached()) {
                    getView().showLoading(null);
                }
            }

            @Override
            public void onRequestSuccess(String method, Object object) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onValidateVerifyCodeSuccess();
                }
            }

            @Override
            public void onRequestFail(String method, int statusCode, String msg) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onValidateVerifyCodeFailed(new RequestError(method, statusCode, msg, true));
                }
            }

            @Override
            public void onRequestExp(String method, String msg, ResultData data) {
                if (isViewAttached()) {
                    getView().hideLoading(null);
                    getView().onValidateVerifyCodeFailed(new RequestError(method, msg));
                }
            }
        }, phoneNo, verifyCode);
    }
}
