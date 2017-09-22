package cn.cloudworkshop.seller.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.seller.R;
import cn.cloudworkshop.seller.base.BaseActivity;
import cn.cloudworkshop.seller.constant.Constant;

import cn.cloudworkshop.seller.utils.SharedPreferencesUtils;
import cn.cloudworkshop.seller.utils.ToastUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016-10-20 11:12
 * Email：1993911441@qq.com
 * Describe：登录界面
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_password)
    EditText etUserPassword;
    @BindView(R.id.img_login)
    ImageView imgLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 加载视图
     */
    private void initView() {
        canLogIn();
        isEnable(etUserName);
        isEnable(etUserPassword);
    }

    /**
     * 监听账号密码输入
     */
    private void isEnable(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                canLogIn();
            }
        });

    }

    /**
     * 账号密码是否输入
     */
    private void canLogIn() {
        if (!TextUtils.isEmpty(etUserName.getText().toString().trim()) &&
                !TextUtils.isEmpty(etUserPassword.getText().toString().trim())) {
            imgLogin.setEnabled(true);
        }else {
            imgLogin.setEnabled(false);
        }
    }


    @OnClick(R.id.img_login)
    public void onViewClicked() {
        confirmLogin();
    }


    /**
     * 登录
     */
    private void confirmLogin() {

        OkHttpUtils.post()
                .url(Constant.LOGIN)
                .addParams("account", etUserName.getText().toString())
                .addParams("pwd", etUserPassword.getText().toString())
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
                            ToastUtils.showToast(LoginActivity.this, jsonObject.getString("msg"));
                            if (code == 1) {
                                String token = jsonObject.getString("token");
                                SharedPreferencesUtils.saveStr(LoginActivity.this, "token", token);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}

