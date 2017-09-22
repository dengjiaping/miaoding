package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.GiftCardBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.utils.ToastUtils;
import okhttp3.Call;

/**
 * Author：binge on 2017-09-20 15:21
 * Email：1993911441@qq.com
 * Describe：礼品卡
 */
public class GiftCardActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_card_remain)
    TextView tvCardRemain;
    @BindView(R.id.img_null_card)
    ImageView imgNullCard;
    @BindView(R.id.tv_add_card)
    TextView tvAddCard;
    private GiftCardBean cardBean;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);
        ButterKnife.bind(this);
        tvHeaderTitle.setText("礼品卡");
        getData();
        initData();
    }

    private void getData() {
        type = getIntent().getStringExtra("type");
    }

    private void initData() {
        OkHttpUtils.get()
                .url(Constant.GIFT_CARD)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        cardBean = GsonUtils.jsonToBean(response, GiftCardBean.class);

                        if (cardBean.getInfo() != null) {
                            initView();
                        }
                    }
                });
    }

    private void initView() {
        tvCardRemain.setText("¥" + new DecimalFormat("0.00").format(Float.parseFloat(cardBean.getInfo().getGift_card())));
    }

    @OnClick({R.id.img_header_back, R.id.tv_add_card})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                if (type != null && type.equals("select")) {
                    Intent intent = new Intent();
                    intent.putExtra("card_money", Float.parseFloat(cardBean.getInfo().getGift_card()));
                    setResult(1, intent);
                }
                finish();
                break;
            case R.id.tv_add_card:
                addGiftCard();
                break;
        }
    }

    /**
     * 添加礼品卡
     */
    private void addGiftCard() {
        View popupView = getLayoutInflater().inflate(R.layout.ppw_add_card, null);
        final PopupWindow mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, -300);
        DisplayUtils.setBackgroundAlpha(this, true);

        final EditText etNumber = (EditText) popupView.findViewById(R.id.et_card_number);
        TextView tvConfirm = (TextView) popupView.findViewById(R.id.tv_confirm_card);
        TextView tvCancel = (TextView) popupView.findViewById(R.id.tv_cancel_card);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etNumber.getText().toString().trim())) {
                    ToastUtils.showToast(GiftCardActivity.this, "请输入序列号");
                } else {
                    exchangeCard(etNumber.getText().toString());
                    mPopupWindow.dismiss();
                }
            }

        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                DisplayUtils.setBackgroundAlpha(GiftCardActivity.this, false);
            }
        });

    }

    /**
     * @param number 兑换礼品卡金额
     */
    private void exchangeCard(String number) {
        OkHttpUtils.post()
                .url(Constant.EXCHANGE_CARD)
                .addParams("token", SharedPreferencesUtils.getStr(this, "token"))
                .addParams("exchange_code", number)
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
                            String msg = jsonObject.getString("msg");
                            ToastUtils.showToast(GiftCardActivity.this, msg);
                            if (code == 1) {
                                initData();
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
            if (type != null && type.equals("select")) {
                Intent intent = new Intent();
                intent.putExtra("card_money", Float.parseFloat(cardBean.getInfo().getGift_card()));
                setResult(1, intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}