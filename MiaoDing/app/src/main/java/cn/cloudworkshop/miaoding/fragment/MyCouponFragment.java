package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.CouponBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.MainActivity;
import cn.cloudworkshop.miaoding.utils.DateUtils;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.MyLinearLayoutManager;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：binge on 2016/12/17 11:40
 * Email：1993911441@qq.com
 * Describe：优惠券子界面
 */
public class MyCouponFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.tv_exchange_coupon)
    TextView tvExchange;
    @BindView(R.id.ll_exchange_coupon)
    LinearLayout llExchange;
    @BindView(R.id.rv_coupon)
    RecyclerView rvCoupon;
    @BindView(R.id.img_null_coupon)
    ImageView imgNullCoupon;

    //兑换优惠券
    private int currentPos;
    private List<CouponBean.DataBean> couponList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_item_coupon, container, false);
        unbinder = ButterKnife.bind(this, view);

        getData();
        initData();
        return view;
    }

    /**
     * 获取数据
     */
    private void initData() {
        if (currentPos == 1) {
            llExchange.setVisibility(View.VISIBLE);
        } else {
            llExchange.setVisibility(View.GONE);
        }

        etInputCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etInputCode.getText().toString().trim().length() > 0) {
                    tvExchange.setEnabled(true);
                    tvExchange.setBackgroundResource(R.drawable.btn_black_bg);
                } else {
                    tvExchange.setEnabled(false);
                    tvExchange.setBackgroundResource(R.drawable.btn_gray_bg);
                }
            }
        });

        OkHttpUtils.get()
                .url(Constant.MY_COUPON)
                .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                .addParams("status", currentPos + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        couponList = new ArrayList<>();
                        CouponBean couponBean = GsonUtils.jsonToBean(response, CouponBean.class);
                        if (couponBean.getData() != null && couponBean.getData().size() > 0) {
                            imgNullCoupon.setVisibility(View.GONE);
                            couponList.addAll(couponBean.getData());
                            initView();
                        } else {
                            imgNullCoupon.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    /**
     * 加载视图
     */
    private void initView() {
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(getActivity());
        linearLayoutManager.setScrollEnabled(false);
        rvCoupon.setLayoutManager(linearLayoutManager);
        CommonAdapter<CouponBean.DataBean> adapter = new CommonAdapter<CouponBean.DataBean>
                (getActivity(), R.layout.listitem_coupon, couponList) {
            @Override
            protected void convert(ViewHolder holder, CouponBean.DataBean dataBean, int position) {
                switch (currentPos) {
                    case 1:
                        holder.getView(R.id.ll_coupon_bg).setBackgroundResource(R.mipmap.icon_coupon_available);
                        break;
                    case 2:
                        holder.getView(R.id.ll_coupon_bg).setBackgroundResource(R.mipmap.icon_coupon_used);
                        break;
                    case -1:
                        holder.getView(R.id.ll_coupon_bg).setBackgroundResource(R.mipmap.icon_coupon_overdue);
                        break;
                }


                TextView tvMoney = holder.getView(R.id.tv_coupon_money);
                tvMoney.setTypeface(DisplayUtils.setTextType(getActivity()));
                tvMoney.setText("¥" + (int) Float.parseFloat(dataBean.getMoney()));
                holder.setText(R.id.tv_coupon_range, dataBean.getTitle());
                holder.setText(R.id.tv_coupon_discount, dataBean.getSub_title());
                StringBuilder sb = new StringBuilder();
                sb.append("有效期：")
                        .append(DateUtils.getDate("yyyy-MM-dd", dataBean.getS_time()))
                        .append(" 至 ")
                        .append(DateUtils.getDate("yyyy-MM-dd", dataBean.getE_time()));
                holder.setText(R.id.tv_coupon_term, sb.toString());

            }
        };

        rvCoupon.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (currentPos == 1) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("page", 1);
                    getActivity().finish();
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });


    }

    /**
     * 优惠券状态
     */
    private void getData() {
        Bundle bundle = getArguments();
        currentPos = bundle.getInt("coupon");
    }

    public static MyCouponFragment newInstance(int currentPos) {
        Bundle args = new Bundle();
        args.putInt("coupon", currentPos);
        MyCouponFragment fragment = new MyCouponFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.tv_exchange_coupon)
    public void onClick() {
        exchangeCoupon();
    }

    /**
     * 兑换优惠券
     */
    private void exchangeCoupon() {

        OkHttpUtils.post()
                .url(Constant.EXCHANGE_COUPON)
                .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                .addParams("kouling", etInputCode.getText().toString().trim())
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
                            if (code == 1) {
                                initData();
                            }
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            etInputCode.setText("");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
