package com.maning.library_httputils.model;



import com.maning.library_httputils.callback.AbsBaseCallback;
import com.maning.library_httputils.callback.AbsFileProgressCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;

/**
 * @author : maning
 * @desc : Okhttp请求参数相关
 */
public class OkhttpRequestModel {

    /**
     * 自定义回调
     */
    private AbsBaseCallback callbackListener;
    /**
     * 原始的回调
     */
    private Callback callback;
    /**
     * 请求方式标记:0:get,1:post
     */
    private int requestMothed;
    /**
     * 请求地址
     */
    private String httpUrl;
    /**
     * 请求参数
     */
    private Map<String, String> paramsMap;
    /**
     * 请求参数-String类型
     */
    private String paramsString;
    /**
     * 请求头
     */
    private Map<String, String> headersMap;
    /**
     * Content-Type
     */
    private MediaType mediaType;
    /**
     * 请求Tag
     */
    private Object tag;
    /**
     * 文件上传的
     */
    private HashMap<String, File> uploadFiles;
    /**
     * 下载文件保存的路径
     */
    private String downloadPath;

    private AbsFileProgressCallback fileProgressCallback;


    public AbsFileProgressCallback getFileProgressCallback() {
        return fileProgressCallback;
    }

    public void setFileProgressCallback(AbsFileProgressCallback fileProgressCallback) {
        this.fileProgressCallback = fileProgressCallback;
    }

    public String getParamsString() {
        return paramsString;
    }

    public void setParamsString(String paramsString) {
        this.paramsString = paramsString;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public HashMap<String, File> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(HashMap<String, File> uploadFilePaths) {
        this.uploadFiles = uploadFilePaths;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public AbsBaseCallback getCallbackListener() {
        return callbackListener;
    }

    public void setCallbackListener(AbsBaseCallback callbackListener) {
        this.callbackListener = callbackListener;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getRequestMothed() {
        return requestMothed;
    }

    public void setRequestMothed(int requestMothed) {
        this.requestMothed = requestMothed;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public Map<String, String> getParamsMap() {
        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public Map<String, String> getHeadersMap() {
        if (headersMap == null) {
            headersMap = new HashMap<>();
        }
        return headersMap;
    }

    public void setHeadersMap(Map<String, String> headersMap) {
        this.headersMap = headersMap;
    }
}
