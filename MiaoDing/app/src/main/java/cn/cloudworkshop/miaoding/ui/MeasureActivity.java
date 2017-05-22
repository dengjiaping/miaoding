package cn.cloudworkshop.miaoding.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.AddressPickTask;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.DatePicker;
import okhttp3.Call;


/**
 * Author：Libin on 2016/8/24 16:11
 * Email：1993911441@qq.com
 * Describe：量体表
 */
public class MeasureActivity extends BaseActivity {

    @BindView(R.id.tv_current_address)
    TextView tvCurrentAddress;
    @BindView(R.id.img_location)
    ImageView imgLocation;
    @BindView(R.id.tv_measure_type)
    TextView mTvMeasureType;
    @BindView(R.id.tv_measure_time)
    TextView mTvMeasureTime;
    @BindView(R.id.tv_select_measurer)
    TextView mTvSelectMeasure;
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;
    @BindView(R.id.vp_select_measurer)
    ViewPager viewPager;
    @BindView(R.id.ll_measure_table)
    LinearLayout llMeasureTable;
    @BindView(R.id.et_measure_name)
    EditText etMeasureName;
    @BindView(R.id.et_measure_number)
    EditText etMeasureNumber;
    @BindView(R.id.et_measure_remarks)
    EditText etMeasureRemarks;
    @BindView(R.id.tv_submit_measure)
    TextView tvSubmitMeasure;
    @BindView(R.id.rl_select_measurer)
    RelativeLayout rlSelectMeasurer;
    @BindView(R.id.img_left_arrow)
    ImageView imgLeftArrow;
    @BindView(R.id.img_right_arrow)
    ImageView imgRightArrow;
    @BindView(R.id.rl_measure_type)
    RelativeLayout rlMeasureType;
    @BindView(R.id.rv_measure_type)
    RecyclerView rvMeasureType;
    @BindView(R.id.ll_measure_address)
    LinearLayout llMeasureAddress;
    @BindView(R.id.et_address_details)
    EditText etAddressDetails;

    //声明mLocationOption对象
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private Map<String, String> map = new HashMap<String, String>();

    private List<Integer> viewList;

    private List<String> typeList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_measure);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initData() {
        typeList.add("西服");
        typeList.add("裤装");
        typeList.add("衬衣");
        typeList.add("马甲");
        typeList.add("大衣");
    }

    private void initView() {
        tvHeaderTitle.setText("上门量体");
    }

    @OnClick({R.id.img_header_back, R.id.ll_measure_address, R.id.img_location, R.id.tv_measure_type,
            R.id.tv_measure_time, R.id.tv_select_measurer, R.id.ll_measure_table,
            R.id.et_measure_name, R.id.et_measure_number,  R.id.tv_submit_measure,
            R.id.rl_select_measurer, R.id.rl_measure_type, R.id.img_left_arrow, R.id.img_right_arrow})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                finish();
                break;
            case R.id.ll_measure_address:
                selectMeasureAddress();
                break;
            case R.id.img_location:
                startActivity(new Intent(this,MapViewActivity.class));
//                getLocation();
                break;
            case R.id.tv_measure_type:
                selectMeasureType();
                break;
            case R.id.tv_measure_time:
                selectMeasureTime();
                break;
            case R.id.tv_select_measurer:
                selectMeasurer();
                break;
            case R.id.rl_select_measurer:
                rlSelectMeasurer.setVisibility(View.GONE);
                break;
            case R.id.rl_measure_type:
                rlMeasureType.setVisibility(View.GONE);
                break;

            case R.id.img_left_arrow:
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
                break;
            case R.id.img_right_arrow:
                if (viewPager.getCurrentItem() < viewList.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                break;
            case R.id.tv_submit_measure:
                submitMeasureForm();
                break;
        }

    }

    private void submitMeasureForm() {
        if (TextUtils.isEmpty(etMeasureName.getText().toString().trim()) ||
                TextUtils.isEmpty(etMeasureNumber.getText().toString().trim()) ||
                TextUtils.isEmpty(mTvMeasureType.getText().toString().trim()) ||
                TextUtils.isEmpty(tvCurrentAddress.getText().toString().trim()) ||
                TextUtils.isEmpty(etAddressDetails.getText().toString().trim()) ||
                TextUtils.isEmpty(mTvMeasureTime.getText().toString().trim()) ||
                TextUtils.isEmpty(mTvSelectMeasure.getText().toString().trim())) {
            Toast.makeText(this, "请完善量体表", Toast.LENGTH_SHORT).show();
        } else {
            OkHttpUtils.post()
                    .url(Constant.MEASURE_FORM)
                    .addParams("token", SharedPreferencesUtils.getString(this,"token"))
                    .addParams("name",etMeasureName.getText().toString())
                    .addParams("phone",etMeasureNumber.getText().toString())
                    .addParams("type",mTvMeasureType.getText().toString())
                    .addParams("area_id",tvCurrentAddress.getText().toString())
                    .addParams("address",etAddressDetails.getText().toString())
                    .addParams("order_time",mTvMeasureTime.getText().toString())
                    .addParams("pattern_master_id",mTvSelectMeasure.getText().toString())
                    .addParams("remark",etMeasureRemarks.getText().toString())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Toast.makeText(MeasureActivity.this, "提交成功，请保持电话畅通，" +
                                    "工作人员将尽快与您联系", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * 选择量体师
     */
    private void selectMeasurer() {
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setCurrentItem(0);
        imgLeftArrow.setVisibility(View.GONE);
        imgRightArrow.setVisibility(View.VISIBLE);
        rlSelectMeasurer.setVisibility(View.VISIBLE);
        viewList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
//            viewList.add(R.mipmap.img_select_measurer);
        }
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;
                container.removeView(view);
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = LayoutInflater.from(MeasureActivity.this).inflate(R.layout.viewpager_item_measurer, null);
                ImageView img = (ImageView) view.findViewById(R.id.img_measurer_info);
                img.setImageResource(viewList.get(position));
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTvSelectMeasure.setText("詹妮斯");
                        viewPager.setVisibility(View.GONE);
                        rlSelectMeasurer.setVisibility(View.GONE);
                    }
                });
                TextView tv = (TextView) view.findViewById(R.id.tv_measurer_info);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MeasureActivity.this, DesignerInfoActivity.class);
                        startActivity(intent);
                    }
                });
                container.addView(view);
                return view;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    imgLeftArrow.setVisibility(View.GONE);
                } else if (position == viewList.size() - 1) {
                    imgRightArrow.setVisibility(View.GONE);
                } else {
                    imgLeftArrow.setVisibility(View.VISIBLE);
                    imgRightArrow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 量体品类
     */
    private void selectMeasureType() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        rlMeasureType.setVisibility(View.VISIBLE);
        rvMeasureType.setLayoutManager(new GridLayoutManager(MeasureActivity.this, 2));
        CommonAdapter<String> typeAdapter = new CommonAdapter<String>(MeasureActivity.this,
                R.layout.listitem_price_type, typeList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.tv_type_item, typeList.get(position));
            }
        };

        typeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                mTvMeasureType.setText(typeList.get(position));
                rlMeasureType.setVisibility(View.GONE);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        rvMeasureType.setAdapter(typeAdapter);
    }

    /**
     * 量体时间
     */
    private void selectMeasureTime() {
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
        Calendar calendar = Calendar.getInstance();
        picker.setRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
        picker.setCancelText("时间");
        picker.setTextSize(15);
        picker.setLineColor(Color.GRAY);
        picker.setAnimationStyle(R.style.Animation_CustomPopup);
        picker.setTextColor(getResources().getColor(R.color.dark_gray_22), Color.GRAY);
        picker.setOffset(2);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                mTvMeasureTime.setText(year + "年" + (month) + "月" + day + "日");
            }
        });
        picker.show();
    }

    /**
     * 定位
     */
    private void getLocation() {
        mLocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);

        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //启动定位
        mLocationClient.startLocation();

        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        map.put("province", aMapLocation.getProvince());
                        map.put("city", aMapLocation.getCity());
                        map.put("district", aMapLocation.getDistrict());

                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "ppw_wheel_location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        });
    }

    /**
     * 地址选择
     */
    private void selectMeasureAddress() {
        AddressPickTask task = new AddressPickTask(this);
        task.execute("北京市", "北京市", "朝阳区");
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressPicked(Province province, City city, County county) {
                tvCurrentAddress.setText(province.getAreaName() + city.getAreaName() + county.getAreaName());
            }

            @Override
            public void onAddressInitFailed() {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (rlSelectMeasurer.getVisibility() == View.VISIBLE) {
                rlSelectMeasurer.setVisibility(View.GONE);
                return false;
            }
            if (rlMeasureType.getVisibility() == View.VISIBLE) {
                rlMeasureType.setVisibility(View.GONE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
    }
}
