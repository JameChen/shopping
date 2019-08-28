package com.nahuo.quicksale.di.module;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nahuo.constant.Constant;
import com.nahuo.library.helper.FunctionHelper;
import com.nahuo.quicksale.BuildConfig;
import com.nahuo.quicksale.app.BWApplication;
import com.nahuo.quicksale.common.ListUtils;
import com.nahuo.quicksale.common.SpManager;
import com.nahuo.quicksale.model.http.api.PinHuoApi;
import com.nahuo.quicksale.oldermodel.PublicData;
import com.nahuo.quicksale.util.HttpsUtils;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by jame on 2018/4/26.
 */

public class HttpManager {
    private static Retrofit.Builder builder;
    private static OkHttpClient.Builder okNeedCacheBuilder;
    private static OkHttpClient.Builder okNoCacheBuilder;
    public static int CONNECTTIMEOUT = 60;
    public static int READTIMEOUT = 60;
    public static int WRITETIMEOUT =60;
    public static int CACHE_MAXSIZE = 1024 * 1024 * 50;
    public static int MAXSTALE = 60 * 60 * 24 * 28;

    public static Retrofit.Builder provideRetrofitBuilder() {
        if (builder == null) {
            builder = new Retrofit.Builder();
        }
        return builder;
    }

    public HttpManager() {

    }

    static HttpManager httpManager;

    public static HttpManager getInstance() {
        if (httpManager == null) {
            synchronized (HttpManager.class) {
                if (httpManager == null) {
                    httpManager = new HttpManager();
                }
            }
        }
        return httpManager;
    }

    public static OkHttpClient.Builder provideNeedCacheOkHttpBuilder() {
        if (okNeedCacheBuilder == null) {
            synchronized (HttpManager.class) {
                if (okNeedCacheBuilder == null)
                    okNeedCacheBuilder = new OkHttpClient.Builder();
            }

        }
        return okNeedCacheBuilder;
    }

    public static OkHttpClient.Builder provideNoCacheOkHttpBuilder() {
        if (okNoCacheBuilder == null) {
            synchronized (HttpManager.class) {
                if (okNoCacheBuilder == null)
                    okNoCacheBuilder = new OkHttpClient.Builder();
            }

        }
        return okNoCacheBuilder;
    }

    private static String Tag = "";

    public static OkHttpClient provideNeedCacheClient(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    synchronized (HttpLoggingInterceptor.class) {
                        if (TextUtils.isEmpty(message)) return;
                        String s = message.substring(0, 1);
                        //如果收到响应是
                        if ("{".equals(s) || "[".equals(s)) {
                            Logger.t(Tag).json(message);
                        } else {
                            Logger.t(Tag).d(message);
                        }
                    }
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.sslSocketFactory(HttpsUtils.getSslSocketFactory().sSLSocketFactory,HttpsUtils.getSslSocketFactory().trustManager);
//            builder.sslSocketFactory(HttpsUtils.getSslSocketFactory(new InputStream[]{
//                    BWApplication.getInstance().getResources().openRawResource(R.raw.yu)}).sSLSocketFactory,HttpsUtils.getSslSocketFactory(new InputStream[]{
//                    BWApplication.getInstance().getResources().openRawResource(R.raw.yu)}).trustManager);
            builder.addInterceptor(loggingInterceptor);
        }
        File cacheFile = new File(Constant.PATH_CACHE);
        Cache cache = new Cache(cacheFile, CACHE_MAXSIZE);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!FunctionHelper.isNetworkConnected(BWApplication.getInstance())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                if (FunctionHelper.isNetworkConnected(BWApplication.getInstance())) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = MAXSTALE;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        Interceptor apikey = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response;
                request = request.newBuilder()
                        .addHeader("Cookie", SpManager.getCookie(BWApplication.getInstance()))
                        .addHeader("User-Agent", BWApplication.getInstance().getUserAgent())
                        .build();
                response = chain.proceed(request);
                if (request.url() != null) {
                    String method = request.url().toString();
                    if (method.contains("user/user/register2") || method.contains("user/user/login")
                            || method.contains("user/connect/login")) {
                        // 获取cookie值
                        List<String> headers = response.headers("Set-Cookie");
                        if (!ListUtils.isEmpty(headers)) {
                            for (String header : headers) {
                                String nhStrPrefix = "NaHuo.UserLogin";
                                if (header.toString().contains(nhStrPrefix)) {
                                    // 保存cookie
                                    String cookieFulllStr = header.toString();
                                    // 保存cookie
                                    String cookie = cookieFulllStr.substring(cookieFulllStr.indexOf(nhStrPrefix),
                                            cookieFulllStr.length());
                                    PublicData.setCookieOnlyAtInit(cookie);
                                    if (method.contains("user/user/register2")) {
                                        SpManager.setCookie(BWApplication.getInstance(), cookie);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
//                if (response.code()==504)
//                    throw  new RuntimeException("504");
                 return getLogResponse(request, response);
            }
        };
        // 设置统一的请求头部参数
        builder.addInterceptor(apikey);
        //设置缓存
        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);
        //设置超时
        builder.connectTimeout(CONNECTTIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READTIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITETIMEOUT, TimeUnit.SECONDS);
        //builder.connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS));
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public static OkHttpClient provideNoCashClient(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    synchronized (HttpLoggingInterceptor.class) {
                        if (TextUtils.isEmpty(message)) return;
                        Logger.t(Tag).d(message);
//                        String s = message.substring(0, 1);
//                        //如果收到响应是
//                        if ("{".equals(s) || "[".equals(s)) {
//                            Logger.t(Tag).json(message);
//                        } else {
//                            Logger.t(Tag).d(message);
//                        }
                    }
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder.sslSocketFactory(HttpsUtils.getSslSocketFactory(new InputStream[]{
//                    BWApplication.getInstance().getResources().openRawResource(R.raw.yu)}).sSLSocketFactory,HttpsUtils.getSslSocketFactory(new InputStream[]{
//                    BWApplication.getInstance().getResources().openRawResource(R.raw.yu)}).trustManager);
            builder.sslSocketFactory(HttpsUtils.getSslSocketFactory().sSLSocketFactory,HttpsUtils.getSslSocketFactory().trustManager);
            builder.addInterceptor(loggingInterceptor);
        }
//        File cacheFile = new File(Constant.PATH_CACHE);
//        Cache cache = new Cache(cacheFile, CACHE_MAXSIZE);
//        Interceptor cacheInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Response response = chain.proceed(request);
//                int maxAge = 0;
//                //  不缓存, 最大保存时长为0
//                response.newBuilder()
//                        .header("Cache-Control", "public, max-age=" + maxAge)
//                        .removeHeader("Pragma")
//                        .build();
//                return response;
//            }
//        };
        Interceptor apikey = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                request = request.newBuilder()
                        .addHeader("Cookie", SpManager.getCookie(BWApplication.getInstance()))
                        .addHeader("User-Agent", BWApplication.getInstance().getUserAgent())
                        .build();
                Response response = chain.proceed(request);
                if (request.url() != null) {
                    String method = request.url().toString();
                    if (method.contains("user/user/register2") || method.contains("user/user/login")
                            || method.contains("user/connect/login")) {
                        // 获取cookie值
                        List<String> headers = response.headers("Set-Cookie");
                        if (!ListUtils.isEmpty(headers)) {
                            for (String header : headers) {
                                String nhStrPrefix = "NaHuo.UserLogin";
                                if (header.toString().contains(nhStrPrefix)) {
                                    // 保存cookie
                                    String cookieFulllStr = header.toString();
                                    // 保存cookie
                                    String cookie = cookieFulllStr.substring(cookieFulllStr.indexOf(nhStrPrefix),
                                            cookieFulllStr.length());
                                    PublicData.setCookieOnlyAtInit(cookie);
                                    if (method.contains("user/user/register2")) {
                                        SpManager.setCookie(BWApplication.getInstance(), cookie);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
                return getLogResponse(request, response);
            }
        };
        // 设置统一的请求头部参数
        builder.addInterceptor(apikey);
        //设置缓存
//        builder.addNetworkInterceptor(cacheInterceptor);
//        builder.addInterceptor(cacheInterceptor);
//        builder.cache(cache);
        //设置超时
        builder.connectTimeout(CONNECTTIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READTIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITETIMEOUT, TimeUnit.SECONDS);
        //builder.connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS));
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    @Nullable
    private static Response getLogResponse(Request request, Response response) throws IOException {
        if (BuildConfig.DEBUG) {
            String url = "", body = "",heads="";
            MediaType mediaType = null;
            if (request != null) {
                if (request.url() != null)
                    url = request.url().toString();
            }
            if (response != null) {
                if (response.body() != null) {
                    mediaType = response.body().contentType();
                    body = response.body().string();
                }
                if (response.headers()!=null)
                    heads=response.headers().toString();
            }
            if (mediaType == null) {
                return response;
            } else {
                Logger.t("INFO").i("发送请求=" +
                        url +"\n\n"+ heads +"\n"+ body);
                return response.newBuilder()
                        .body(ResponseBody.create(mediaType, body))
                        .build();
            }
        } else {
            return response;
        }
    }


    private Retrofit createRetrofit(Retrofit.Builder builder, OkHttpClient client, String url) {
        return builder
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


//    public Retrofit providePinHuoRetrofit(String TAG, boolean NeedNetCache) {
//
//        return createRetrofit(provideRetrofitBuilder(), provideNoCashClient(provideOkHttpBuilder()), PinHuoApi.HOST);
//    }

    private static Retrofit retrofit;
    private static Retrofit needCashRetrofit;

    public Retrofit getPinHuoNeedCacheRetrofit() {

        if (needCashRetrofit == null) {
            synchronized (HttpManager.class) {
                needCashRetrofit = new Retrofit.Builder().baseUrl(PinHuoApi.HOST)
                        .client(provideNeedCacheClient(provideNeedCacheOkHttpBuilder()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return needCashRetrofit;
    }

    public Retrofit getPinHuoNoCacheRetrofit() {

        if (retrofit == null) {
            synchronized (HttpManager.class) {
                retrofit = new Retrofit.Builder().baseUrl(PinHuoApi.HOST)
                        .client(provideNoCashClient(provideNoCacheOkHttpBuilder()))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        }
        return retrofit;
    }

    public PinHuoApi getPinHuoNetCacheApi(String TAG) {
        Tag = "DEBUG-"+TAG;
        return getPinHuoNeedCacheRetrofit().create(PinHuoApi.class);
    }

    public PinHuoApi getPinHuoNoCacheApi(String TAG) {
        Tag = "DEBUG-"+TAG;
        return getPinHuoNoCacheRetrofit().create(PinHuoApi.class);
    }

}
