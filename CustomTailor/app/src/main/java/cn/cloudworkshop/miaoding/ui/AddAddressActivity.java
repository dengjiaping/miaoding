package cn.cloudworkshop.miaoding.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.ReceiveAddressBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.PhoneNumberUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/10/7 16:57
 * Email：1993911441@qq.com
 * Describe：新增地址
 */
public class AddAddressActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.et_add_name)
    EditText etAddName;
    @BindView(R.id.et_add_number)
    EditText etAddNumber;
    @BindView(R.id.et_detailed_address)
    EditText etDetailedAddress;
    @BindView(R.id.tv_confirm_address)
    TextView tvConfirmAddress;
    @BindView(R.id.tv_header_next)
    TextView tvHeaderNext;
    @BindView(R.id.ll_receive_address)
    LinearLayout llSelectAddress;
    @BindView(R.id.tv_receive_address)
    TextView tvSelectAddress;
    @BindView(R.id.switch_address)
    Switch switchAddress;

    //默认地址
    int index = 0;
    //省
    private String provinceAddress;
    //市
    private String cityAddress;
    //区
    private String countAddress;
    private ReceiveAddressBean.DataBean dataBean;

    //add:添加地址 alert:修改地址
    private String type;
    private AddressPicker addressPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        type = getIntent().getExtras().getString("type");
    }

    /**
     * 加载视图
     */
    private void initView() {

        switch (type) {
            case "add":
                tvHeaderTitle.setText("新增地址");
                tvHeaderNext.setVisibility(View.GONE);
                break;
            case "alert":
                tvHeaderTitle.setText("编辑地址");
                tvHeaderNext.setVisibility(View.VISIBLE);
                tvHeaderNext.setText("删除地址");
                dataBean = (ReceiveAddressBean.DataBean) getIntent().getExtras().getSerializable("alert");

                etAddName.setText(dataBean.getName());
                etAddNumber.setText(dataBean.getPhone());
                tvSelectAddress.setText(dataBean.getProvince() + dataBean.getCity() + dataBean.getArea());
                provinceAddress = dataBean.getProvince();
                cityAddress = dataBean.getCity();
                countAddress = dataBean.getArea();
                etDetailedAddress.setText(dataBean.getAddress());
                if (dataBean.getIs_default() == 1) {
                    switchAddress.setChecked(true);
                    switchAddress.setEnabled(false);
                    index = 1;
                } else {
                    switchAddress.setChecked(false);
                    switchAddress.setEnabled(true);
                }
                break;
        }
        switchAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    index = 1;
                } else {
                    index = 0;
                }
            }
        });

    }


    @OnClick({R.id.img_header_back, R.id.ll_receive_address, R.id.tv_confirm_address, R.id.tv_header_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.ll_receive_address:
                selectAddress();
                break;
            case R.id.tv_confirm_address:
                addAddress();
                break;
            case R.id.tv_header_next:
                deleteAddress();
                break;
        }
    }

    /**
     * 删除地址
     */
    private void deleteAddress() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setTitle("删除地址");
        dialog.setMessage("您确定要删除该地址吗？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpUtils.post()
                        .url(Constant.DELETE_ADDRESS)
                        .addParams("token", SharedPreferencesUtils.getString(AddAddressActivity.this, "token"))
                        .addParams("id", String.valueOf(dataBean.getId()))
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
                                        Toast.makeText(AddAddressActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
            }
        });
        //为“取消”按钮注册监听事件
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 根据实际情况编写相应代码。
                dialog.dismiss();
            }
        });
        dialog.create();
        dialog.show();

    }

    /**
     * 选择地址
     */
    private void selectAddress() {

        try {
            ArrayList<Province> data = new ArrayList<>();
            String json = ConvertUtils.toString(getAssets().open("city.json"));
            data.addAll(GsonUtils.jsonToArray(json, Province.class));
            if (addressPicker == null) {
                addressPicker = new AddressPicker(this, data);
            }
            addressPicker.setSelectedItem("北京市", "北京市", "朝阳区");
            addressPicker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    tvSelectAddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
                    provinceAddress = province.getAreaName();
                    cityAddress = city.getAreaName();
                    countAddress = county.getAreaName();
                }
            });
            if (addressPicker != null && !addressPicker.isShowing()) {
                addressPicker.show();
            }

        } catch (Exception e) {

        }

    }

    /**
     * 添加地址
     */
    private void addAddress() {
        if (TextUtils.isEmpty(etAddName.getText().toString().trim()) ||
                TextUtils.isEmpty(etAddNumber.getText().toString().trim()) ||
                TextUtils.isEmpty(tvSelectAddress.getText().toString().trim()) ||
                TextUtils.isEmpty(etDetailedAddress.getText().toString().trim())) {
            Toast.makeText(this, "请完善个人信息", Toast.LENGTH_SHORT).show();
        } else {
            if (PhoneNumberUtils.judgePhoneNumber(etAddNumber.getText().toString().trim())) {
                Map<String, String> map = new HashMap<>();
                map.put("token", SharedPreferencesUtils.getString(this, "token"));
                map.put("address", etDetailedAddress.getText().toString().trim());
                map.put("name", etAddName.getText().toString().trim());
                map.put("phone", etAddNumber.getText().toString().trim());
                map.put("is_default", index + "");
                if (type.equals("alert")) {
                    map.put("id", dataBean.getId() + "");
                }
                map.put("province", provinceAddress);
                map.put("city", cityAddress);
                map.put("area", countAddress);


                OkHttpUtils.post()
                        .url(Constant.ADD_ADDRESS)
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
                                    String addressId = jsonObject.getString("address_id");
                                    if (code == 1) {
                                        switch (type) {
                                            case "add":
                                                Toast.makeText(AddAddressActivity.this, "添加成功",
                                                        Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent();
                                                intent.putExtra("address_id", addressId);
                                                intent.putExtra("province", provinceAddress);
                                                intent.putExtra("city", cityAddress);
                                                intent.putExtra("area", countAddress);
                                                intent.putExtra("address", etDetailedAddress.getText().toString().trim());
                                                intent.putExtra("name", etAddName.getText().toString().trim());
                                                intent.putExtra("phone", etAddNumber.getText().toString().trim());
                                                intent.putExtra("is_default", 1);
                                                setResult(1, intent);
                                                finish();
                                                break;
                                            case "alert":
                                                Toast.makeText(AddAddressActivity.this, "修改成功",
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                                break;
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            } else {
                Toast.makeText(this, "请输入11位手机号码", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
