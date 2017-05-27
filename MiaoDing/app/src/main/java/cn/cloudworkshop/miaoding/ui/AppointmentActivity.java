package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2016/11/24 12:18
 * Email：1993911441@qq.com
 * Describe：预约结果
 */
public class AppointmentActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.tv_appoint_result)
    TextView tvAppointResult;
    @BindView(R.id.tv_header_next)
    TextView tvHeaderNext;
    @BindView(R.id.tv_go_back)
    TextView tvGoBack;
    @BindView(R.id.tv_check_order)
    TextView tvCheckOrder;
    @BindView(R.id.img_pay_result)
    ImageView imgPayResult;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    /**
     * 加载视图
     */
    private void initView() {
        switch (type) {
            case "appoint":
                tvHeaderTitle.setText("预约详情");
                tvAppointResult.setTextSize(15);
                tvHeaderNext.setText("量体协议");
                OkHttpUtils.get()
                        .url(Constant.APPOINTMENT_STATUS)
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
                                    if (jsonObject1 != null) {
                                        int status = jsonObject1.getInt("status");
                                        int time = jsonObject1.getInt("sm_time");
                                        switch (status) {
                                            case 1:
                                            case 2:
                                                tvAppointResult.setText("当前状态：预约成功，等待客服受理");
                                                break;
                                            case 3:
                                            case 4:
                                                tvAppointResult.setText("当前状态：派单成功，等待上门量体\n"
                                                        + "上门时间：" + DateUtils.getDate("yyyy.MM.dd HH:mm", time));
                                                break;
                                            case -1:
                                                tvAppointResult.setText("当前状态：已取消");
                                                break;
                                            default:
                                                break;
                                        }

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                break;
            case "apply":
                tvHeaderTitle.setText("申请详情");
                tvAppointResult.setText("申请成功");
                imgPayResult.setImageResource(R.mipmap.icon_appoint_success);
                break;
            case "pay_success":
                tvHeaderTitle.setText("支付成功");
                tvAppointResult.setText("支付成功");
                imgPayResult.setImageResource(R.mipmap.icon_appoint_success);
                tvCheckOrder.setVisibility(View.VISIBLE);
                tvGoBack.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
                tvGoBack.setBackgroundResource(R.drawable.text_black_bg);
                break;
            case "pay_fail":
                tvHeaderTitle.setText("支付失败");
                tvAppointResult.setText("支付失败");
                tvCheckOrder.setVisibility(View.VISIBLE);
                imgPayResult.setImageResource(R.mipmap.icon_appoint_fail);
                tvGoBack.setTextColor(ContextCompat.getColor(this, R.color.dark_gray_22));
                tvGoBack.setBackgroundResource(R.drawable.text_black_bg);
                break;
            default:
                break;
        }
    }

    private void getData() {
        type = getIntent().getStringExtra("content");
    }

    @OnClick({R.id.img_header_back, R.id.tv_header_next, R.id.tv_go_back, R.id.tv_check_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.tv_header_next:
                Intent intent = new Intent(this, UserAgreementActivity.class);
                intent.putExtra("content", "measure");
                startActivity(intent);
                break;
            case R.id.tv_go_back:
                if (type.equals("pay")) {
                    Intent intent2 = new Intent(this, MainActivity.class);
                    finish();
                    startActivity(intent2);
                } else {
                    finish();
                }
                break;
            case R.id.tv_check_order:
                Intent intent1 = new Intent(this, OrderDetailsActivity.class);
                intent1.putExtra("id", getIntent().getStringExtra("order_id"));
                finish();
                startActivity(intent1);
                break;
        }
    }

}
