package cn.cloudworkshop.miaoding.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.bean.ReceiveAddressBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import okhttp3.Call;

/**
 * Author：Libin on 2016/10/7 17:03
 * Email：1993911441@qq.com
 * Describe：收货地址
 */
public class DeliveryAddressActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.rv_delivery_address)
    LRecyclerView recyclerView;
    @BindView(R.id.ll_no_address)
    LinearLayout llNoAddress;
    @BindView(R.id.tv_add_address)
    TextView tvAddAddress;
    //1：编辑地址 2:选择地址
    private String type;
    //地址id
    private String addressId;
    //页面
    private int page = 1;
    //加载更多
    private boolean isLoadMore;
    private CommonAdapter<ReceiveAddressBean.DataBean> adapter;
    private List<ReceiveAddressBean.DataBean> dataList = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);
        ButterKnife.bind(this);
        getData();
        initData();
    }

    private void getData() {
        type = getIntent().getStringExtra("content");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dataList.clear();
        page = 1;
        initData();
    }

    /**
     * 加载数据
     */
    private void initData() {
        switch (type) {
            case "1":
                tvHeaderTitle.setText("收货地址");
                break;
            case "2":
                tvHeaderTitle.setText("选择地址");
                addressId = getIntent().getStringExtra("address_id");
                break;
        }

        OkHttpUtils.get()
                .url(Constant.MY_ADDRESS)
                .addParams("token", SharedPreferencesUtils.getString(this, "token"))
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        ReceiveAddressBean address = GsonUtils.jsonToBean(response, ReceiveAddressBean.class);
                        if (address.getData() != null && address.getData().size() > 0) {
                            if (isRefresh) {
                                dataList.clear();
                            }
                            dataList.addAll(address.getData());
                            if (isRefresh || isLoadMore) {
                                recyclerView.refreshComplete();
                                mLRecyclerViewAdapter.notifyDataSetChanged();

                            } else {
                                initView();
                            }
                            isRefresh = false;
                            isLoadMore = false;
                            llNoAddress.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            RecyclerViewStateUtils.setFooterViewState(DeliveryAddressActivity.this,
                                    recyclerView, 0, LoadingFooter.State.TheEnd, null);
                            if (page == 1) {
                                recyclerView.setVisibility(View.GONE);
                                llNoAddress.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
    }

    /**
     * 加载视图
     */
    private void initView() {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommonAdapter<ReceiveAddressBean.DataBean>(this, R.layout.listitem_address, dataList) {
            @Override
            protected void convert(ViewHolder holder, final ReceiveAddressBean.DataBean dataBean, final int position) {
                holder.setText(R.id.tv_user_name, dataBean.getName());
                holder.setText(R.id.tv_user_phone, dataBean.getPhone());
                holder.setText(R.id.tv_user_address, dataBean.getProvince() + dataBean.getCity()
                        + dataBean.getArea() + dataBean.getAddress());
                holder.setVisible(R.id.view_address_line, true);
                holder.setVisible(R.id.ll_edit_address, true);


                TextView tvDefault = holder.getView(R.id.tv_set_default);
                if (dataBean.getIs_default() == 1) {
                    Drawable leftDrawable = ContextCompat.getDrawable(DeliveryAddressActivity.this, R.mipmap.icon_default_address);
                    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                    tvDefault.setCompoundDrawables(leftDrawable, null, null, null);
                } else if (dataBean.getIs_default() == 0) {
                    Drawable leftDrawable = ContextCompat.getDrawable(DeliveryAddressActivity.this, R.mipmap.icon_not_default);
                    leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
                    tvDefault.setCompoundDrawables(leftDrawable, null, null, null);
                }

                tvDefault.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dataList.get(position - 1).getIs_default() == 0) {
                            setDefaultAddress(dataList.get(position - 1).getId());
                        }

                    }
                });

                TextView tvDelete = holder.getView(R.id.tv_delete_address);
                TextView tvEdit = holder.getView(R.id.tv_edit_address);

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteAddress(dataList.get(position - 1).getId());
                    }
                });

                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DeliveryAddressActivity.this, AddAddressActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("content", "alert");
                        bundle.putSerializable("alert", dataBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


//                holder.getView(R.id.ll_set_address).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(DeliveryAddressActivity.this, AddAddressActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("content", "alert");
//                        bundle.putSerializable("alert", dataBean);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                });
            }
        };


        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        recyclerView.setAdapter(mLRecyclerViewAdapter);
        recyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);

        //刷新
        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                page = 1;
                initData();
            }
        });

        //加载更多
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                RecyclerViewStateUtils.setFooterViewState(DeliveryAddressActivity.this, recyclerView,
                        0, LoadingFooter.State.Loading, null);
                isLoadMore = true;
                page++;
                initData();
            }
        });


        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (type) {
                    case "1":
//                        Intent intent = new Intent(DeliveryAddressActivity.this, AddAddressActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("content", "alert");
//                        bundle.putSerializable("alert", dataList.get(position));
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "2":
                        Intent intent1 = new Intent();
                        intent1.putExtra("address_id", dataList.get(position).getId() + "");
                        intent1.putExtra("province", dataList.get(position).getProvince());
                        intent1.putExtra("city", dataList.get(position).getCity());
                        intent1.putExtra("area", dataList.get(position).getArea());
                        intent1.putExtra("address", dataList.get(position).getAddress());
                        intent1.putExtra("name", dataList.get(position).getName());
                        intent1.putExtra("phone", dataList.get(position).getPhone());
                        intent1.putExtra("is_default", dataList.get(position).getIs_default());
                        setResult(1, intent1);
                        finish();
                        break;
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });


    }

    /**
     * 设置默认地址
     */
    private void setDefaultAddress(int id) {
        OkHttpUtils.post()
                .url(Constant.DEFAULT_ADDRESS)
                .addParams("token", SharedPreferencesUtils.getString(DeliveryAddressActivity.this, "token"))
                .addParams("id", id + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dataList.clear();
                        page = 1;
                        initData();
                    }
                });

    }

    /**
     * 删除地址
     */
    private void deleteAddress(final int id) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertDialog);
        dialog.setTitle("删除地址");
        dialog.setMessage("您确定要删除该地址吗？");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkHttpUtils.post()
                        .url(Constant.DELETE_ADDRESS)
                        .addParams("token", SharedPreferencesUtils.getString(DeliveryAddressActivity.this, "token"))
                        .addParams("id", String.valueOf(id))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                dataList.clear();
                                page = 1;
                                initData();
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

    @OnClick({R.id.img_header_back, R.id.tv_add_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                isSelectAddress();
                break;
            case R.id.tv_add_address:
                Intent intent1 = new Intent(DeliveryAddressActivity.this, AddAddressActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("content", "add");
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isSelectAddress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void isSelectAddress() {
        //地址被清空返回2,已选地址被删除返回3
        if (type.equals("2")) {
            if (dataList.size() > 0) {
                if (addressId == null) {
                    setResult(3);
                } else {
                    List<String> idList = new ArrayList<>();
                    for (int i = 0; i < dataList.size(); i++) {
                        idList.add(dataList.get(i).getId() + "");
                    }
                    if (!idList.contains(addressId)) {
                        setResult(3);
                    }
                }

            } else {
                setResult(2);
            }
        }
        finish();
    }

}
