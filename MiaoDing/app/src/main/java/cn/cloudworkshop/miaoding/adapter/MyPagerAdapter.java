package cn.cloudworkshop.miaoding.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.bean.GoodsListBean;
import cn.cloudworkshop.miaoding.bean.PopDesignerBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.jazzyviewpager.JazzyViewPager;
import cn.cloudworkshop.miaoding.ui.DesignerDetailActivity;
import cn.cloudworkshop.miaoding.utils.ToastUtils;

/**
 * Author：binge on 2017/9/25 16:38
 * Email：1993911441@qq.com
 * Describe：
 */
public class MyPagerAdapter extends PagerAdapter{
    private List<PopDesignerBean.DataBean> designerList;
    private Context context;

    public MyPagerAdapter(List<PopDesignerBean.DataBean> designerList, Context context) {
        this.designerList = designerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return designerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_item_designer, null);
        SimpleDraweeView imgDesigner = (SimpleDraweeView) view.findViewById(R.id.img_designer_card);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_designers_name);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_designers_info);

        tvTitle.setText(designerList.get(position).getName());
        tvContent.setText(designerList.get(position).getTag());

        imgDesigner.setImageURI(Uri.parse(Constant.HOST + designerList.get(position).getAvatar()));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
