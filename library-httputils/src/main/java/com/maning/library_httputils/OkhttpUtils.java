package com.maning.library_httputils;

import android.net.Uri;
import android.text.TextUtils;

import com.maning.library_httputils.callback.AbsBaseCallback;
import com.maning.library_httputils.callback.OkhttpCallback;
import com.maning.library_httputils.constants.HttpConstants;
import com.maning.library_httputils.model.OkhttpRequestModel;

import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author : maning
 * @desc : Okhttp 请求工具类
 */
public class OkhttpUtils {

    private static final String TAG = "OkhttpUtils";
    /**
     * MediaType
     */
    private static MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static MediaType MEDIA_TYPE_JPEG = MediaType.parse("image/jpeg;charset=utf-8");
    public static MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png;charset=utf-8");
    public static MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*;charset=utf-8");

    /**
     * 默认Callback
     */
    private Callback defaultCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

        }
    };

    /**
     * okHttpClient
     */
    private static OkHttpClient okHttpClient;
    private static OkHttpClient.Builder okhttpBuilder;
    /**
     * OkhttpUtils对象
     */
    private static OkhttpUtils okhttpUtils;
    /**
     * GET请求标记
     */
    public static final int GET = 0;
    /**
     * POST请求标记
     */
    private static final int POST_STRING = 1;
    private static final int POST_FORM = 2;
    /**
     * 请求相关参数
     */
    private OkhttpRequestModel okhttpRequestModel;

    private OkhttpUtils() {
        okhttpRequestModel = new OkhttpRequestModel();
    }

    /**
     * 回去当前实例
     *
     * @return
     */
    public static OkhttpUtils with() {
        okhttpUtils = new OkhttpUtils();
        return okhttpUtils;
    }

    /**
     * 设置请求Url
     *
     * @param url
     * @return
     */
    public OkhttpUtils url(String url) {
        okhttpRequestModel.setHttpUrl(url);
        return okhttpUtils;
    }

    /**
     * GET方法
     *
     * @return
     */
    public OkhttpUtils get() {
        okhttpRequestModel.setRequestMothed(GET);
        return okhttpUtils;
    }

    /**
     * POST方法-Json方式
     *
     * @return
     */
    public OkhttpUtils post() {
        okhttpRequestModel.setRequestMothed(POST_STRING);
        return okhttpUtils;
    }

    /**
     * POST方法-表单方式
     *
     * @return
     */
    public OkhttpUtils postForm() {
        okhttpRequestModel.setRequestMothed(POST_FORM);
        return okhttpUtils;
    }


    /**
     * 给请求打上TAG,默认当前Url
     *
     * @param tag
     * @return
     */
    public OkhttpUtils tag(Object tag) {
        okhttpRequestModel.setTag(tag);
        return okhttpUtils;
    }

    /**
     * 设置参数
     *
     * @param paramsMap
     * @return
     */
    public OkhttpUtils params(Map<String, String> paramsMap) {
        okhttpRequestModel.setParamsMap(paramsMap);
        return okhttpUtils;
    }

    /**
     * 设置单个参数
     *
     * @param paramsKey
     * @param paramsValue
     * @return
     */
    public OkhttpUtils addParams(String paramsKey, String paramsValue) {
        okhttpRequestModel.getParamsMap().put(paramsKey, paramsValue);
        return okhttpUtils;
    }

    /**
     * 设置参数
     *
     * @param paramsString
     * @return
     */
    public OkhttpUtils paramsString(String paramsString) {
        okhttpRequestModel.setParamsString(paramsString);
        return okhttpUtils;
    }

    /**
     * 设置请求头
     *
     * @param headersMap
     * @return
     */
    public OkhttpUtils headers(Map<String, String> headersMap) {
        okhttpRequestModel.setHeadersMap(headersMap);
        return okhttpUtils;
    }

    /**
     * 设置单个请求头
     *
     * @param headerKey
     * @param headerValue
     * @return
     */
    public OkhttpUtils addHeader(String headerKey, String headerValue) {
        okhttpRequestModel.getHeadersMap().put(headerKey, headerValue);
        return okhttpUtils;
    }

    /**
     * 设置MediaType
     *
     * @param mediaType
     * @return
     */
    public OkhttpUtils setMediaType(MediaType mediaType) {
        okhttpRequestModel.setMediaType(mediaType);
        return okhttpUtils;
    }

    /**
     * 开始执行：默认回调
     *
     * @param callback 回调监听
     */
    public void execute(Callback callback) {
        okhttpRequestModel.setCallback(callback);
        //开始请求
        sendRequest();
    }

    /**
     * 开始执行：
     *
     * @param callbackListener 自定义回调监听
     */
    public void execute(AbsBaseCallback callbackListener) {
        okhttpRequestModel.setCallbackListener(callbackListener);
        //开始请求
        sendRequest();
    }


    private void sendRequest() {
        if (okhttpRequestModel == null) {
            throw new NullPointerException("OkhttpRequestModel初始化失败");
        }
        //获取参数
        //请求地址
        String httpUrl = okhttpRequestModel.getHttpUrl();
        //请求Tag
        Object tag = okhttpRequestModel.getTag();
        //请求头
        Map<String, String> headersMap = okhttpRequestModel.getHeadersMap();
        //请求参数
        Map<String, String> paramsMap = okhttpRequestModel.getParamsMap();
        String paramsString = okhttpRequestModel.getParamsString();
        //回调监听
        AbsBaseCallback callbackListener = okhttpRequestModel.getCallbackListener();
        //Content-Type
        MediaType mediaType = okhttpRequestModel.getMediaType();
        //请求方法
        int requestMothed = okhttpRequestModel.getRequestMothed();

        //初始化请求
        final Request.Builder requestBuild = new Request.Builder();
        //添加请求地址
        requestBuild.url(httpUrl);
        //添加请求头
        if (headersMap != null && headersMap.size() > 0) {
            for (String key : headersMap.keySet()) {
                requestBuild.addHeader(key, headersMap.get(key));
            }
        }
        //添加Tag
        if (tag != null) {
            requestBuild.tag(okhttpRequestModel.getTag());
        } else {
            requestBuild.tag(httpUrl);
        }
        //GET---POST
        if (requestMothed == GET) {
            if (paramsMap != null && paramsMap.size() > 0) {
                //重新拼接参数
                httpUrl = appendParams(httpUrl, paramsMap);
                requestBuild.url(httpUrl);
            }
            requestBuild.get();
        } else if (requestMothed == POST_FORM) {
            //POST-表单形式
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                formBodyBuilder.add(key, paramsMap.get(key));
            }
            requestBuild.post(formBodyBuilder.build());
        } else if (requestMothed == POST_STRING) {
            //POST-Json字符串形式
            JSONObject jsonMap =new JSONObject(paramsMap);
            String requestContent = jsonMap.toString();
            if (TextUtils.isEmpty(requestContent)) {
                requestContent = "";
            }
            if (!TextUtils.isEmpty(paramsString)) {
                requestContent = paramsString;
            }
            //设置请求体
            if (mediaType == null) {
                mediaType = MEDIA_TYPE_JSON;
            }
            RequestBody requestBody = RequestBody.create(mediaType, requestContent);
            requestBuild.post(requestBody);
        }
        //设置回调
        Callback callback;
        if (callbackListener != null) {
            callback = new OkhttpCallback(callbackListener);
        } else {
            callback = okhttpRequestModel.getCallback();
        }
        //执行请求
        Call call = getOkhttpClient().newCall(requestBuild.build());
        if (callback != null) {
            call.enqueue(callback);
        } else {
            call.enqueue(defaultCallback);
        }
    }


    /**
     * 拼接Get请求Url
     *
     * @param url
     * @param params
     * @return
     */
    protected String appendParams(String url, Map<String, String> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    //-----------------------分割线-----------------------------

    /**
     * 动态设置okhttpBuilder
     *
     * @param okhttpBuilder
     */
    public static void setOkhttpBuilder(OkHttpClient.Builder okhttpBuilder) {
        OkhttpUtils.okHttpClient = okhttpBuilder.build();
        OkhttpUtils.okhttpBuilder = okhttpBuilder;
    }

    /**
     * 获取OkHttpClient
     *
     * @return
     */
    public static OkHttpClient getOkhttpClient() {
        if (okHttpClient == null) {
            okHttpClient = getOkhttpBuilder().build();
        }
        return okHttpClient;
    }

    /**
     * 获取默认OkHttpClient.Builder
     *
     * @return
     */
    public static OkHttpClient.Builder getOkhttpBuilder() {
        if (okhttpBuilder == null) {
            return getOkhttpDefaultBuilder();
        }
        return okhttpBuilder;
    }

    /**
     * 获取默认OkHttpClient.Builder
     *
     * @return
     */
    public static OkHttpClient.Builder getOkhttpDefaultBuilder() {
        //默认信任所有的证书
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[0];
                return x509Certificates;
            }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30000, TimeUnit.MILLISECONDS);
        builder.readTimeout(30000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(30000, TimeUnit.MILLISECONDS);
        builder.sslSocketFactory(sslSocketFactory, trustManager);
        builder.hostnameVerifier(DO_NOT_VERIFY);
        okhttpBuilder = builder;
        return okhttpBuilder;
    }


    //-----------------------分割线-----------------------------

    /**
     * 根据Tag取消请求
     */
    public void cancelTag(Object tag) {
        try {
            if (tag == null) {
                return;
            }
            for (Call call : getOkhttpClient().dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
            for (Call call : getOkhttpClient().dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) {
                    call.cancel();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        try {
            for (Call call : getOkhttpClient().dispatcher().queuedCalls()) {
                call.cancel();
            }
            for (Call call : getOkhttpClient().dispatcher().runningCalls()) {
                call.cancel();
            }
        } catch (Exception e) {

        }
    }

}
