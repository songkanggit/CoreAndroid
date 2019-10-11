package com.guohe.corecenter.bean;

import androidx.annotation.NonNull;

import com.guohe.corecenter.constant.PreferenceConst;
import com.guohe.corecenter.core.pereference.PreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestParam {
    public JSONObject param;

    public static RequestParam newInstance(PreferencesManager manager) {
        RequestParam requestParam = new RequestParam();
        try {
            requestParam.param = new JSONObject();
            final String accessToken = manager.get(PreferenceConst.ACCESS_TOKEN, "");
            requestParam.param.put("accessToken", accessToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestParam;
    }

    public void addAttribute(String key, Object value) {
        try {
            param.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return param.toString();
    }
}
