package cn.cloudworkshop.miaoding.utils;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;
import cn.cloudworkshop.miaoding.constant.Constant;

/**
 * Author：Libin on 2016/8/23 09:40
 * Email：1993911441@qq.com
 * Describe：ConvenientBanner加载网络图片
 */
public class NetworkImageHolderView implements Holder<String> {
    private SimpleDraweeView imageView;

    @Override
    public View createView(Context context) {
        imageView = new SimpleDraweeView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        imageView.setImageURI(Uri.parse(Constant.HOST + data));
    }
}
