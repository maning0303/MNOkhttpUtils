package com.maning.library_httputils.callback;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.maning.library_httputils.constants.HttpConstants;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : maning
 * desc   : OkhttpCallback 回调
 */
public class OkhttpCallback implements Callback {

    private Handler mUITransHandler;
    private AbsBaseCallback mCallbackListener;

    private OkhttpCallback() {

    }

    public OkhttpCallback(AbsBaseCallback callbackListener) {
        this.mUITransHandler = new Handler(Looper.getMainLooper());
        this.mCallbackListener = callbackListener;
        if (this.mCallbackListener == null) {
            //实现空实现
            mCallbackListener = new AbsStringCallback() {
                @Override
                public void onSuccess(String responseStr) {

                }

                @Override
                public void onFailure(String errorCode, String errorMsg) {

                }
            };
        }
        //开始执行
        mUITransHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallbackListener.onStart();
            }
        });
    }

    @Override
    public void onFailure(final Call call, final IOException exception) {
        mUITransHandler.post(new Runnable() {
            @Override
            public void run() {
                //取消请求
                if (call.isCanceled()) {
                    mCallbackListener.onFinish();
                    return;
                }
                try {
                    //处理错误信息
                    String errorCode = HttpConstants.ERR_NETEXCEPTION_ERROR_CODE;
                    String errorMsg;
                    if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
                        //网络未连接
                        errorMsg = HttpConstants.ERR_UNKNOWNHOSTEXCEPTION_ERROR;
                    } else if (exception instanceof SocketTimeoutException) {
                        //连接超时
                        errorMsg = HttpConstants.ERR_SOCKETTIMEOUTEXCEPTION_ERROR;
                    } else {
                        //其他网络异常
                        errorMsg = HttpConstants.ERR_NETEXCEPTION_ERROR;
                    }
                    mCallbackListener.onFailure(errorCode, errorMsg);
                } catch (Exception e) {
                    e.printStackTrace();
                    mCallbackListener.onFailure(HttpConstants.ERR_HTTPRESPONSE_DATA_ERROR, e.getMessage());
                } finally {
                    mCallbackListener.onFinish();
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) {
        try {
            if (200 == response.code()) {
                if (mCallbackListener instanceof AbsByteCallback) {
                    //byte类型
                    final byte[] responseBytes = response.body().bytes();
                    mUITransHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((AbsByteCallback) mCallbackListener).onSuccess(responseBytes);
                        }
                    });
                } else if (mCallbackListener instanceof AbsStringCallback) {
                    //字符串类型
                    final String responseStr = response.body().string();
                    mUITransHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((AbsStringCallback) mCallbackListener).onSuccess(responseStr);
                        }
                    });
                }
            } else {
                mUITransHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //请求异常
                        mCallbackListener.onFailure(response.code() + "", HttpConstants.ERR_NETEXCEPTION_ERROR);
                    }
                });

            }
        } catch (final Exception e) {
            mUITransHandler.post(new Runnable() {
                @Override
                public void run() {
                    e.printStackTrace();
                    mCallbackListener.onFailure(HttpConstants.ERR_HTTPRESPONSE_DATA_ERROR, e.getMessage());
                }
            });
        } finally {
            mUITransHandler.post(new Runnable() {
                @Override
                public void run() {
                    //完成
                    mCallbackListener.onFinish();
                }
            });
        }
    }

}
