package cn.cloudworkshop.miaoding.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.bean.DesignWorksBean;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.DesignerInfoActivity;
import cn.cloudworkshop.miaoding.ui.WorksInfoActivity;
import cn.cloudworkshop.miaoding.view.JazzyViewPager;

/**
 * Author：binge on 2016/11/25 14:45
 * Email：1993911441@qq.com
 * Describe：
 */
public class JazzyPagerAdapter extends PagerAdapter {
    private List<DesignWorksBean.DataBean.ItemBean> designerList;
    private Context context;
    private JazzyViewPager jazzyViewPager;


    public JazzyPagerAdapter(Context context, JazzyViewPager jazzyViewPager, List<DesignWorksBean.DataBean.ItemBean> designerList) {
        this.context = context;
        this.jazzyViewPager = jazzyViewPager;
        this.designerList = designerList;
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
        ImageView imgWorks = (ImageView) view.findViewById(R.id.img_designer_works);
        ImageView imgBg = (ImageView) view.findViewById(R.id.img_designer_bg);
        TextView tvName = (TextView) view.findViewById(R.id.tv_designer_name);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_designer_info);
        View viewLine = view.findViewById(R.id.view_sign_line);
        TextView tvDesignerPage = (TextView) view.findViewById(R.id.tv_designer_page);
        final LinearLayout llDesigner = (LinearLayout) view.findViewById(R.id.ll_designer_info);

        tvDesignerPage.setText(designerList.get(position).getName());
        Glide.with(context)
                .load(Constant.HOST + designerList.get(position).getImg())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgWorks);
//                Glide.with(getActivity()).load(Constant.HOST + designerList.get(currentPos).getAvatar()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgIcon);

        if (position == designerList.size() - 1) {
            imgBg.setVisibility(View.GONE);
        }
        if (designerList.get(position).getRemark() != null) {
            tvInfo.setText(designerList.get(position).getRemark());
        }


        if (designerList.get(position).getUsername() != null) {
            tvName.setText(designerList.get(position).getUsername());
            viewLine.setVisibility(View.VISIBLE);
        }

        imgWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != designerList.size() - 1) {
                    Intent intent = new Intent(context, WorksInfoActivity.class);
                    intent.putExtra("id", String.valueOf(designerList.get(position).getGoods_id()));
                    intent.putExtra("img_url", designerList.get(position).getThumb());
                    intent.putExtra("title", designerList.get(position).getGoods_name());
                    intent.putExtra("content", designerList.get(position).getRemark());
                    context.startActivity(intent);
                }
            }
        });

        llDesigner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != designerList.size() - 1) {
                    Intent intent = new Intent(context, DesignerInfoActivity.class);
                    intent.putExtra("id", designerList.get(position).getUid() + "");
                    intent.putExtra("title", designerList.get(position).getUsername());
                    intent.putExtra("content", designerList.get(position).getRemark());
                    intent.putExtra("img_url", designerList.get(position).getAvatar());
                    context.startActivity(intent);
                }
            }
        });

        jazzyViewPager.setObjectForPosition(view, position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
