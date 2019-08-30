package com.guohe.corecenter.bean;

import androidx.annotation.NonNull;

import com.guohe.corecenter.core.pereference.PreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestParam {
    public JSONObject param;

    public static RequestParam newInstance(PreferencesManager manager) {
        RequestParam requestParam = new RequestParam();
        try {
            requestParam.param.put("UserInfo", manager.get("UserInfo", null));
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
