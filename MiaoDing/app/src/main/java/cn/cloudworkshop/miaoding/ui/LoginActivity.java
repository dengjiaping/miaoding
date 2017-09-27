package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.PhoneNumberUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016-10-20 11:12
 * Email：1993911441@qq.com
 * Describe：登录界面
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.img_cancel_login)
    ImageView imgCancelLogin;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;
    @BindView(R.id.img_login)
    ImageView imgLogin;
    @BindView(R.id.img_login_bg)
    ImageView bgLogin;

    //验证码token
    private String msgToken;
    //登录返回token
    private String loginToken;
    //重发验证码时间
    private int i = 30;
    //是否输入手机号
    private boolean isPhone;
    //是否输入验证码
    private boolean isCode;
    private String logId;

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                tvVerificationCode.setText("重发(" + i + ")");
                tvVerificationCode.setBackgroundResource(R.drawable.bound_c7_15dp);
            } else if (msg.what == -8) {
                tvVerificationCode.setText("获取验证码");
                tvVerificationCode.setClickable(true);
                tvVerificationCode.setBackgroundResource(R.drawable.bound_3d_15dp);
                i = 30;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        msgToken = SharedPreferencesUtils.getStr(this, "msg_token");
        ButterKnife.bind(this);
        loginLog();
        initView();
    }

    /**
     * 登录跟踪
     */
    private void loginLog() {
        String pageName = getIntent().getStringExtra("page_name");
        if (pageName != null) {
            OkHttpUtils.post()
                    .url(Constant.LOGIN_LOG)
                    .addParams("p_module_name", pageName)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                logId = jsonObject.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

    }

    /**
     * 加载视图
     */
    private void initView() {
        if (MyApplication.loginBg != null){
            Glide.with(this)
                    .load(Constant.HOST + MyApplication.loginBg)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(bgLogin);
        }

        imgLogin.setEnabled(false);
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (PhoneNumberUtils.judgePhoneNumber(etUserName.getText().toString().trim())) {
                    tvVerificationCode.setBackgroundResource(R.drawable.bound_3d_15dp);
                    isPhone = true;
                    if (isCode) {
                        imgLogin.setEnabled(true);
                    } else {
                        imgLogin.setEnabled(false);
                    }
                } else {
                    tvVerificationCode.setBackgroundResource(R.drawable.bound_c7_15dp);
                    isPhone = false;
                    imgLogin.setEnabled(false);
                }
            }
        });

        etUserPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(etUserPassword.getText().toString().trim())) {
                    isCode = false;
                    imgLogin.setEnabled(false);
                } else {
                    isCode = true;
                    if (isPhone) {
                        imgLogin.setEnabled(true);
                    } else {
                        imgLogin.setEnabled(false);
                    }
                }

            }
        });
    }


    @OnClick({R.id.img_cancel_login, R.id.tv_verification_code, R.id.tv_user_agreement, R.id.img_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_cancel_login:
                String cancel = getIntent().getStringExtra("log_in");
                if (!TextUtils.isEmpty(cancel) && cancel.equals("center")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("page", 0);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
                break;
            case R.id.tv_verification_code:
                getVerificationCode();
                break;
            case R.id.img_login:
                confirmLogin();
                break;
            case R.id.tv_user_agreement:
                if (!TextUtils.isEmpty(MyApplication.userAgreement)) {
                    Intent intent = new Intent(this, UserRuleActivity.class);
                    intent.putExtra("title", "用户协议");
                    intent.putExtra("img_url", MyApplication.userAgreement);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 登录
     */
    private void confirmLogin() {

        if (!TextUtils.isEmpty(msgToken)) {
            Map<String, String> map = new HashMap<>();
            map.put("phone", etUserName.getText().toString().trim());
            map.put("code", etUserPassword.getText().toString().trim());
            map.put("token", msgToken);
            map.put("device_id", SharedPreferencesUtils.getStr(LoginActivity.this, "client_id"));
            if (logId != null) {
                map.put("id", logId);
            }
            map.put("device_type", Build.MODEL);
            map.put("phone_type", "1");

            OkHttpUtils.post()
                    .url(Constant.LOGIN)
                    .params(map)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                int code = jsonObject.getInt("code");
                                if (code == 1) {
                                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                    loginToken = jsonObject1.getString("token");
                                    int uid = jsonObject1.getInt("uid");
                                    SharedPreferencesUtils.saveStr(LoginActivity.this, "uid", uid + "");
                                    SharedPreferencesUtils.saveStr(LoginActivity.this, "token", loginToken);
                                    getUserInfo();
                                    MobclickAgent.onEvent(LoginActivity.this, "log_in");
                                    finish();
                                }
                                ToastUtils.showToast(LoginActivity.this, jsonObject.getString("msg"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }

    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        OkHttpUtils.get()
                .url(Constant.USER_INFO)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            SharedPreferencesUtils.saveStr(LoginActivity.this, "avatar",
                                    jsonObject1.getString("avatar"));
                            SharedPreferencesUtils.saveStr(LoginActivity.this, "phone",
                                    jsonObject1.getString("phone"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 获取验证码
     */
    private void getVerificationCode() {

        if (PhoneNumberUtils.judgePhoneNumber(etUserName.getText().toString().trim())) {
            sendPhoneNumber();
            tvVerificationCode.setClickable(false);
            new Thread(myRunnable).start();
        } else {
            ToastUtils.showToast(this, "手机号输入有误，请重新输入");
        }
    }

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            for (; i > 0; i--) {
                handler.sendEmptyMessage(-9);
                if (i <= 0) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.sendEmptyMessage(-8);
        }
    };


    /**
     * 获取验证码
     */
    private void sendPhoneNumber() {

        OkHttpUtils.post()
                .url(Constant.VERIFICATION_CODE)
                .addParams("phone", etUserName.getText().toString().trim())
                .addParams("type", "1")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            if (code == 1) {
                                ToastUtils.showToast(LoginActivity.this, jsonObject.getString("msg"));
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                msgToken = jsonObject1.getString("token");
                                SharedPreferencesUtils.saveStr(LoginActivity.this, "msg_token", msgToken);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String loginTag = getIntent().getStringExtra("log_in");
            if (!TextUtils.isEmpty(loginTag) && loginTag.equals("center")) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("page", 0);
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(myRunnable);
    }

    @OnClick(R.id.img_login_bg)
    public void onViewClicked() {
    }
}
