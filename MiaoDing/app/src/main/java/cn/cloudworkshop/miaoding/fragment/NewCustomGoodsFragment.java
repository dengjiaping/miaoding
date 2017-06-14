package cn.cloudworkshop.miaoding.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
import com.zbar.lib.CaptureActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.adapter.JazzyPagerAdapter;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.GoodsListBean;
import cn.cloudworkshop.miaoding.bean.GoodsTitleBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.ScanCodeActivity;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.PermissionUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;
import cn.cloudworkshop.miaoding.view.JazzyViewPager;
import okhttp3.Call;

/**
 * Author：binge on 2017-05-27 20:29
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewCustomGoodsFragment extends BaseFragment {
    @BindView(R.id.img_select_type)
    ImageView imgSelectType;
    @BindView(R.id.tv_custom_title)
    TextView tvCustomTitle;
    @BindView(R.id.vp_custom_goods)
    JazzyViewPager vpCustomGoods;
    @BindView(R.id.view_loading)
    AVLoadingIndicatorView loadingView;
    @BindView(R.id.img_code)
    ImageView imgCode;
    private Unbinder unbinder;


    private int page = 1;
    private GoodsListBean listBean;
    private GoodsTitleBean titleBean;
    private GoodsTitleBean.DataBean currentGoods;
    private int currentItem = 0;

    // 是否需要系统权限检测
    private boolean isRequireCheck = true;
    static final String[] permissionStr = new String[]{Manifest.permission.CAMERA};
    //是否授权
    private boolean isGrant;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_goods_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        initTitle();
        return view;
    }

    private void initTitle() {
        loadingView.setIndicator(new BallSpinFadeLoaderIndicator());
        loadingView.setIndicatorColor(Color.GRAY);
        OkHttpUtils
                .get()
                .url(Constant.GOODS_TITLE)
                .addParams("token", SharedPreferencesUtils.getString(getActivity(), "token"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        titleBean = GsonUtils.jsonToBean(response, GoodsTitleBean.class);
                        if (titleBean.getData() != null) {
                            currentGoods = titleBean.getData().get(0);
                            initGoods();
                        }
                    }
                });
    }


    /**
     * 加载商品
     */
    public void initGoods() {
        loadingView.smoothToShow();
        OkHttpUtils
                .get()
                .url(Constant.GOODS_LIST)
                .addParams("type", "1")
                .addParams("classify_id", currentGoods.getId() + "")
                .addParams("page", page + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        loadingView.smoothToHide();
                        imgSelectType.setEnabled(true);
                        listBean = GsonUtils.jsonToBean(response, GoodsListBean.class);
                        if (listBean.getData().getData() != null && listBean.getData().getData().size() > 0) {
                            initView();
                        }
                    }
                });

    }

    /**
     * 加载视图
     */
    private void initView() {

        tvCustomTitle.setText(currentGoods.getName());
        vpCustomGoods.setOffscreenPageLimit(listBean.getData().getData().size());
        vpCustomGoods.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        JazzyPagerAdapter adapter = new JazzyPagerAdapter(listBean.getData().getData(), getActivity(), vpCustomGoods);
        vpCustomGoods.setAdapter(adapter);

        vpCustomGoods.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == listBean.getData().getData().size() - 1) {
                    Toast.makeText(getActivity(), "已经是最后一页了", Toast.LENGTH_SHORT).show();
                }
                if (position == 0) {
                    Toast.makeText(getActivity(), "已经是第一页了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    /**
     * 选择种类
     */
    private void showPopWindow() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.ppw_goods_type, null);
        final PopupWindow mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopupWindow.showAsDropDown(imgSelectType);

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.rv_goods_type);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        CommonAdapter<GoodsTitleBean.DataBean> adapter = new CommonAdapter<GoodsTitleBean.DataBean>
                (getActivity(), R.layout.listitem_goods_type, titleBean.getData()) {
            @Override
            protected void convert(ViewHolder holder, GoodsTitleBean.DataBean dataBean, int position) {
                TextView tvGoods = holder.getView(R.id.tv_goods_type);
                tvGoods.setText(dataBean.getName());
                if (currentItem == position) {
                    tvGoods.setTypeface(Typeface.DEFAULT_BOLD);
                } else {
                    tvGoods.setTypeface(Typeface.DEFAULT);
                }
            }
        };
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                currentGoods = titleBean.getData().get(position);
                currentItem = position;
                imgSelectType.setEnabled(false);
                initGoods();
                mPopupWindow.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


    public static NewCustomGoodsFragment newInstance() {

        Bundle args = new Bundle();

        NewCustomGoodsFragment fragment = new NewCustomGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.img_select_type, R.id.img_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_select_type:
                if (titleBean != null) {
                    showPopWindow();
                }
                break;
            case R.id.img_code:
                judgePermission();
                if (isGrant) {
                    Intent intent = new Intent(getActivity(), ScanCodeActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 检测权限
     */
    private void judgePermission() {
        if (isRequireCheck) {
            //权限没有授权，进入授权界面
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                isGrant = false;
                if (Build.VERSION.SDK_INT >= 23) {
                    requestPermissions(permissionStr, 1);
                } else {
                    showPermissionDialog();
                }
            } else {
                isGrant = true;
            }
        } else {
            isRequireCheck = true;
        }
    }


    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isRequireCheck = false;
            isGrant = true;
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            startActivity(intent);

        } else {
            isRequireCheck = true;
            isGrant = false;
            showPermissionDialog();
        }
    }


    @Override
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return false;
    }

    /**
     * 提示对话框
     */
    public void showPermissionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        dialog.setTitle("帮助");
        dialog.setMessage("当前应用缺少相机权限，请点击\"设置\" - \"权限管理\"，打开所需权限。");
        //为“确定”按钮注册监听事件
        dialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 启动应用的设置
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                startActivity(intent);
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

}
