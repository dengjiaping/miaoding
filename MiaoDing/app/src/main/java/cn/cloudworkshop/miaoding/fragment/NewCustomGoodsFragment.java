package cn.cloudworkshop.miaoding.fragment;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.indicators.BallSpinFadeLoaderIndicator;
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
import cn.cloudworkshop.miaoding.utils.GsonUtils;
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
    private Unbinder unbinder;


    private int page = 1;
    private GoodsListBean listBean;
    private GoodsTitleBean titleBean;
    private GoodsTitleBean.DataBean currentGoods;
    private int currentItem = 0;

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
                .addParams("token", SharedPreferencesUtils.getString(getActivity(),"token"))
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
                        listBean = GsonUtils.jsonToBean(response, GoodsListBean.class);
                        if (listBean.getData().getData() != null && listBean.getData().getData().size() > 0) {
                            initView();
                        }
                    }
                });

    }

    private void initView() {
        imgSelectType.setEnabled(true);
        tvCustomTitle.setText(currentGoods.getName());
        vpCustomGoods.setOffscreenPageLimit(listBean.getData().getData().size());
        vpCustomGoods.setTransitionEffect(JazzyViewPager.TransitionEffect.ZoomIn);
        JazzyPagerAdapter adapter = new JazzyPagerAdapter(listBean.getData().getData(),getActivity(),vpCustomGoods);
        vpCustomGoods.setAdapter(adapter);

    }


    @OnClick(R.id.img_select_type)
    public void onViewClicked() {
        if (titleBean != null) {
            showPopWindow();
        }
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



}
