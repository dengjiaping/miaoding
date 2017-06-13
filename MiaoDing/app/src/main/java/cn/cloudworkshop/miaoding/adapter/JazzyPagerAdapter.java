package cn.cloudworkshop.miaoding.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.bean.GoodsListBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.CustomGoodsActivity;
import cn.cloudworkshop.miaoding.view.JazzyViewPager;

/**
 * Author：binge on 2016/11/25 14:45
 * Email：1993911441@qq.com
 * Describe：
 */
public class JazzyPagerAdapter extends PagerAdapter {

    private List<GoodsListBean.DataBean.itemDataBean> dataList;
    private Context context;
    private JazzyViewPager jazzyViewPager;

    public JazzyPagerAdapter(List<GoodsListBean.DataBean.itemDataBean> dataList, Context context,
                             JazzyViewPager jazzyViewPager) {
        this.dataList = dataList;
        this.context = context;
        this.jazzyViewPager = jazzyViewPager;

    }

    @Override
    public int getCount() {
        return dataList.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_item_custom, null);
        ImageView imgWorks = (ImageView) view.findViewById(R.id.img_custom_goods);

        Glide.with(context)
                .load(Constant.HOST + dataList.get(position).getThumb())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgWorks);

        imgWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CustomGoodsActivity.class);
                intent.putExtra("id", String.valueOf(dataList.get(position).getId()));
                context.startActivity(intent);

            }
        });
        container.addView(view);
        jazzyViewPager.setObjectForPosition(view, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
