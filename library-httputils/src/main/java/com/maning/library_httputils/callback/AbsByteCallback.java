package com.maning.library_httputils.callback;


/**
 * author : maning
 * time   : 2018/07/03
 * desc   : 返回byte[] 类型的回调监听
 */
public abstract class AbsByteCallback extends AbsBaseCallback {

    /**
     * 主线程
     * @param responseByte
     */
    public abstract void onSuccess(byte[] responseByte);

}
