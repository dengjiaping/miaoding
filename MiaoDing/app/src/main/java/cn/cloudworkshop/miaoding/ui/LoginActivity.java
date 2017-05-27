package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.cloudworkshop.miaoding.utils.LogUtils;
import cn.cloudworkshop.miaoding.utils.PhoneNumberUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016-10-20 11:12
 * Email：1993911441@qq.com
 * Describe：登录界面
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.img_cancel_login)
    ImageView imgCancelLogin;
    @BindView(R.id.img_app_icon)
    ImageView imgAppIcon;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.img_login_bg)
    ImageView imgLoginBg;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;
    @BindView(R.id.img_login)
    ImageView imgLogin;

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
                tvVerificationCode.setBackgroundColor(ContextCompat.getColor(LoginActivity.this
                        , R.color.light_gray));
            } else if (msg.what == -8) {
                tvVerificationCode.setText("获取验证码");
                tvVerificationCode.setClickable(true);
                tvVerificationCode.setBackgroundColor(ContextCompat.getColor(LoginActivity.this
                        , R.color.light_gray_3d));
                i = 30;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        msgToken = SharedPreferencesUtils.getString(this, "msgToken");
        ButterKnife.bind(this);
        loginLog();
        initView();
    }

    /**
     * 登录跟踪
     */
    private void loginLog() {
        String pageName = getIntent().getStringExtra("page_name");
        if (pageName != null){
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
        Glide.with(getApplicationContext())
                .load(Constant.HOST + MyApplication.loginBg)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgLoginBg);

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
                    tvVerificationCode.setBackgroundColor(ContextCompat.getColor(LoginActivity.this
                            , R.color.light_gray_3d));
                    isPhone = true;
                    if (isCode) {

                        imgLogin.setEnabled(true);
                    } else {
                        imgLogin.setEnabled(false);
                    }
                } else {
                    tvVerificationCode.setBackgroundColor(ContextCompat.getColor(LoginActivity.this
                            , R.color.light_gray));
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


    @OnClick({R.id.img_cancel_login, R.id.tv_verification_code, R.id.img_login, R.id.tv_user_agreement})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_cancel_login:
                finish();
                break;
            case R.id.tv_verification_code:
                getVerificationCode();
                break;
            case R.id.img_login:
                confirmLogin();
                break;
            case R.id.tv_user_agreement:
                Intent intent = new Intent(this, UserAgreementActivity.class);
                intent.putExtra("content", "register");
                startActivity(intent);
                break;
        }
    }

    /**
     * 登录
     */
    private void confirmLogin() {
        if (!PhoneNumberUtils.judgePhoneNumber(etUserName.getText().toString().trim()) ||
                TextUtils.isEmpty(etUserPassword.getText().toString().trim())) {
            Toast.makeText(this, "手机号或验证码有误，请重新输入", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.isEmpty(msgToken)) {
                Map<String ,String> map = new HashMap<>();
                map.put("phone", etUserName.getText().toString().trim());
                map.put("code", etUserPassword.getText().toString().trim());
                map.put("token", msgToken);
                map.put("device_id", SharedPreferencesUtils.getString(LoginActivity.this, "client_id"));
                if (logId != null){
                    map.put("id", logId);
                }

                OkHttpUtils.post()
                        .url(Constant.LOG_IN)
                        .params(map)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                JSONObject jsonObject;
                                try {
                                    jsonObject = new JSONObject(response);
                                    int code = jsonObject.getInt("code");
                                    if (code == 1) {
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                        loginToken = jsonObject1.getString("token");
                                        SharedPreferencesUtils.saveString(LoginActivity.this, "token",
                                                loginToken);
                                        getUserInfo();
                                        MobclickAgent.onEvent(LoginActivity.this, "log_in");
                                        finish();
                                    }
                                    Toast.makeText(LoginActivity.this, jsonObject.getString("msg"),
                                            Toast.LENGTH_SHORT).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        OkHttpUtils.get()
                .url(Constant.USER_INFO)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
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
                            SharedPreferencesUtils.saveString(LoginActivity.this, "icon",
                                    jsonObject1.getString("avatar"));
                            SharedPreferencesUtils.saveString(LoginActivity.this, "phone",
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
            new Thread(new Runnable() {
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
            }).start();
        } else {
            Toast.makeText(this, "手机号输入有误，请重新输入", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 获取验证码
     */
    private void sendPhoneNumber() {

        OkHttpUtils.post()
                .url(Constant.VERIFICATION_CODE)
                .addParams("phone", etUserName.getText().toString().trim())
                .addParams("content", "1")
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
                                Toast.makeText(LoginActivity.this, jsonObject.getString("msg"),
                                        Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                msgToken = jsonObject1.getString("token");
                                SharedPreferencesUtils.saveString(LoginActivity.this, "msgToken", msgToken);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
