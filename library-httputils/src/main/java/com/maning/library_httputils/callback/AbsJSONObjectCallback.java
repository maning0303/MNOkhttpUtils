package com.maning.library_httputils.callback;


import com.maning.library_httputils.constants.HttpConstants;

import org.json.JSONObject;

/**
 * @author : maning
 * @desc :   JSONObject 类型的回调
 */
public abstract class AbsJSONObjectCallback extends AbsStringCallback {

    public abstract void onSuccess(JSONObject responseObj);

    /**
     * 成功
     *
     * @param responseStr
     */
    @Override
    public void onSuccess(String responseStr) {
        JSONObject resultObj = null;
        try {
            resultObj = new JSONObject(responseStr);
        } catch (Exception e) {
            onFailure(HttpConstants.ERR_HTTPRESPONSE_DATA_ERROR_CODE, HttpConstants.ERR_HTTPRESPONSE_DATA_ERROR + e.getMessage());
        }
        onSuccess(resultObj);
    }
}
