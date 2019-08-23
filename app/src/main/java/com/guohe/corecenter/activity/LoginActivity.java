package com.guohe.corecenter.activity;

import androidx.annotation.LayoutRes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guohe.corecenter.R;
import com.guohe.corecenter.utils.MobileUtil;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private TextView mSendCodeTV, mLoginTV;
    private EditText mTelephoneET, mCodeET;

    private EventHandler mEventHandler = new EventHandler() {
        public void afterEvent(int event, int result, Object data) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            new Handler(Looper.getMainLooper(), msg1 -> {
                int event1 = msg1.arg1;
                int result1 = msg1.arg2;
                Object data1 = msg1.obj;
                if (event1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    if (result1 == SMSSDK.RESULT_COMPLETE) {
                        Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
                    }
                } else if (event1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    if (result1 == SMSSDK.RESULT_COMPLETE) {
                        Toast.makeText(getApplicationContext(), "验证码正确", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                        ((Throwable) data1).printStackTrace();
                    }
                }
                // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                return false;
            }).sendMessage(msg);
        }
    };

    protected void parseNonNullBundle(Bundle bundle){

    }
    protected void initDataIgnoreUi() {
        SMSSDK.registerEventHandler(mEventHandler);
    }
    @LayoutRes
    protected int getLayoutResourceId() { return R.layout.activity_login;}
    protected void viewAffairs(){
        mSendCodeTV = fvb(R.id.tv_get_code);
        mTelephoneET = fvb(R.id.et_telephone);
        mCodeET = fvb(R.id.et_code);
        mLoginTV = fvb(R.id.tv_login);
    }
    protected void assembleViewClickAffairs(){
        mSendCodeTV.setOnClickListener(this::onClick);
        mLoginTV.setOnClickListener(this::onClick);
    }
    protected void initDataAfterUiAffairs(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code: {
                final String telephone = mTelephoneET.getText().toString().trim();
                if(MobileUtil.isMobileNO(telephone)) {
                    SMSSDK.getVerificationCode("86", telephone, "6977542", new OnSendMessageHandler() {
                        @Override
                        public boolean onSendMessage(String s, String s1) {
                            return false;
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "请输入正确手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.tv_login: {
                final String telephone = mTelephoneET.getText().toString().trim();
                final String code = mCodeET.getText().toString().trim();
                if(!TextUtils.isEmpty(telephone) && !TextUtils.isEmpty(code)) {
                    SMSSDK.submitVerificationCode("86", telephone, code);
                } else {
                    Toast.makeText(getApplicationContext(), "验证码为空", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(mEventHandler);
    }
}
