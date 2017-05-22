package cn.cloudworkshop.miaoding.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.bean.GoodsListBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.NewGoodsDetailsActivity;
import cn.cloudworkshop.miaoding.utils.DisplayUtils;
import cn.cloudworkshop.miaoding.utils.GsonUtils;
import cn.cloudworkshop.miaoding.utils.HorVPTransformer;
import okhttp3.Call;

/**
 * Author：binge on 2017/3/28 09:13
 * Email：1993911441@qq.com
 * Describe：
 */
public class NewGoodsInfoFragment extends BaseFragment {
    @BindView(R.id.vp_goods)
    ViewPager vpGoods;
    private Unbinder unbinder;
    //当前页面
    private int page = 1;
    //商品id
    private int id;
    //是否最后一页
    private boolean isLastPage = false;
    //是否滑动状态
    private boolean isDragPage = false;
    //加载更多
    private boolean isLoadMore;

    private List<GoodsListBean.DataBean.itemDataBean> goodsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initData();
        return view;
    }

    private void initData() {
        OkHttpUtils
                .get()
                .url(Constant.GOODS_LIST)
                .addParams("type", String.valueOf(1))
                .addParams("classify_id", String.valueOf(id))
                .addParams("page", String.valueOf(page))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        GoodsListBean listEntity = GsonUtils.jsonToBean(response, GoodsListBean.class);
                        if (listEntity.getData().getData() != null && listEntity.getData().getData().size() > 0) {
                            goodsList.addAll(listEntity.getData().getData());
                            initView();
                        }
                    }
                });

    }

    private void initView() {
        vpGoods.setOffscreenPageLimit(goodsList.size());
        vpGoods.setPageMargin((int)DisplayUtils.dp2px(getParentFragment().getActivity(),(float)5));
        vpGoods.setPageTransformer(true, new HorVPTransformer());
        vpGoods.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return goodsList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = LayoutInflater.from(getParentFragment().getActivity())
                        .inflate(R.layout.viewpager_goods_item, null);
                ImageView img = (ImageView) view.findViewById(R.id.img_goods_item);
                Glide.with(getParentFragment().getActivity())
                        .load(Constant.HOST + goodsList.get(position).getThumb())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getParentFragment().getActivity(), NewGoodsDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id", String.valueOf(goodsList.get(position).getId()));
                        bundle.putString("img_url", goodsList.get(position).getThumb());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = (View) object;
                container.removeView(view);
            }
        });


        vpGoods.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                if (isLastPage && isDragPage && positionOffsetPixels == 0) {
                    if (isLoadMore) {

                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                isLastPage = position == goodsList.size() - 1;
            }

            /**
             * 在手指操作屏幕的时候发生变化
             * @param state   有三个值：0（END）,1(PRESS) , 2(UP) 。
             */
            @Override
            public void onPageScrollStateChanged(int state) {
                isDragPage = state == 1;
            }
        });


    }

    private void getData() {
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
    }

    public static NewGoodsInfoFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt("id", id);
        NewGoodsInfoFragment fragment = new NewGoodsInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
