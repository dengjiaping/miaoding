package cn.cloudworkshop.miaoding.utils;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.NewWorksActivity;

/**
 * Author：Libin on 2016/8/23 09:40
 * Email：1993911441@qq.com
 * Describe：ConvenientBanner加载网络图片
 */
public class NetworkImageHolderView implements Holder<String> {
    private ImageView imageView;

    @Override
    public View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager_goods_details, null);
        imageView = (ImageView) view.findViewById(R.id.img_goods_picture);
        return view;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageURI(Uri.parse(Constant.HOST + data));
        Glide.with(context)
                .load(Constant.HOST + data)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }
}
