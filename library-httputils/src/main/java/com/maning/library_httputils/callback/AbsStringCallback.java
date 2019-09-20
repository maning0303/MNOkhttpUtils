package com.maning.library_httputils.callback;


/**
 * @author : maning
 * @desc :   String 类型的回调
 */
public abstract class AbsStringCallback extends AbsBaseCallback {

    /**
     * 成功
     *
     * @param responseStr
     */
    public abstract void onSuccess(String responseStr);

}
