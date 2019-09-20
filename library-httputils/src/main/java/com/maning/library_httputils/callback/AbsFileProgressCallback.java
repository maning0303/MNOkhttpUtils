package com.maning.library_httputils.callback;

/**
 * @author : maning
 * @desc :  文件下载监听
 */
public abstract class AbsFileProgressCallback {

    /**
     * 下载成功
     */
    public abstract void onSuccess(String result);

    /**
     */
    public abstract void onProgress(long bytesRead, long contentLength, boolean done);

    /**
     * 下载失败
     */
    public abstract void onFailed(String errorMsg);

    /**
     * 下载开始
     */
    public abstract void onStart();

}
