package com.maning.mnokhttputils.http;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.maning.library_httputils.callback.AbsJSONObjectCallback;
import com.maning.library_httputils.constants.HttpConstants;
import com.maning.mnokhttputils.model.HttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class HttpCommonCallback<T> extends AbsJSONObjectCallback {

    private static final String RESPONSE_RESULT = "results";
    private static final String RESPONSE_ERROR = "error";

    public abstract void onSuccess(T body);

    public void onSuccess(List<T> bodys) {

    }

    @Override
    public void onSuccess(JSONObject responseObj) {
        if (responseObj.has(RESPONSE_ERROR)) {
            Gson gson = new Gson();
            boolean error = responseObj.optBoolean(RESPONSE_ERROR);
            if (error) {
                handlerFailure(HttpConstants.ERR_NETEXCEPTION_ERROR_CODE, HttpConstants.ERR_NETEXCEPTION_ERROR);
            } else {
                T body = null;
                List<T> bodys = new ArrayList<>();
                if (responseObj.has(RESPONSE_RESULT)) {
                    //body获取---判断是集合还是Object
                    Object resultJson = null;
                    try {
                        resultJson = new JSONTokener(responseObj.getString(RESPONSE_RESULT)).nextValue();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (resultJson == null || "[]".equals(resultJson) || "{}".equals(resultJson) || "null".equals(resultJson)) {
                        handlerSuccess(body, bodys);
                        return;
                    }
                    try {
                        if (resultJson instanceof JSONObject) {
                            // JsonObject
                            JSONObject resultJsonObject = responseObj.optJSONObject(RESPONSE_RESULT);
                            if (resultJsonObject != null) {
                                body = (T) gson.fromJson(resultJsonObject.toString(), getClasses());
                            }
                        } else if (resultJson instanceof JSONArray) {
                            //JsonArray
                            bodys = (List<T>) fromJsonArray(responseObj.toString(), getClasses()).getResults();
                        } else {
                            String resultStr = responseObj.getString(RESPONSE_RESULT);
                            if (!TextUtils.isEmpty(resultStr) && !"null".equals(resultStr)) {
                                body = (T) resultStr;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //Json解析异常
                        handlerFailure(HttpConstants.ERR_HTTPRESPONSE_JSONPARSE_ERROR_CODE, HttpConstants.ERR_HTTPRESPONSE_JSONPARSE_ERROR);
                        return;
                    }
                    handlerSuccess(body, bodys);
                } else {
                    handlerSuccess(body, bodys);
                }
            }
        } else {
            //Json格式异常
            handlerFailure(HttpConstants.ERR_HTTPRESPONSE_JSONFORMAT_ERROR_CODE, HttpConstants.ERR_HTTPRESPONSE_JSONFORMAT_ERROR);
        }
    }


    private void handlerFailure(String errorCode, String errorMsg) {
        onFailure(errorCode, errorMsg);
    }

    private void handlerSuccess(T body, List<T> bodys) {
        onSuccess(body);
        onSuccess(bodys);
    }

    public static <T> HttpResponse<List<T>> fromJsonArray(String reader, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(HttpResponse.class, new Type[]{listType});
        return new Gson().fromJson(reader, type);
    }

    private Class getClasses() {
        Type[] params = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        Class<T> cls = (Class<T>) params[0];
        return cls;
    }
}