package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.TitleBarUtils;
import cn.cloudworkshop.miaoding.view.PhotoViewPager;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Author：binge on 2017/2/7 11:54
 * Email：1993911441@qq.com
 * Describe：图片预览
 */
public class ImagePreviewActivity extends BaseActivity {
    @BindView(R.id.vp_preview)
    PhotoViewPager vpPreview;
    //图片路径
    private ArrayList<String> imgList;
    //当期页面
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TitleBarUtils.setNoTitleBar(this);
        setContentView(R.layout.activity_image_preview);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    /**
     * 加载视图
     */
    private void initView() {
        vpPreview.setOffscreenPageLimit(imgList.size());
        vpPreview.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                View view = LayoutInflater.from(ImagePreviewActivity.this)
                        .inflate(R.layout.viewpager_item_preview, null);
                final PhotoView img = (PhotoView) view.findViewById(R.id.img_preview);
                Glide.with(ImagePreviewActivity.this)
                        .load(Constant.HOST + imgList.get(position))
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(img);
                PhotoViewAttacher mAttach = new PhotoViewAttacher(img);
                mAttach.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        finish();
                    }
                });

                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        vpPreview.setCurrentItem(currentItem);

    }

    /**
     * 获取数据
     */
    private void getData() {
        Intent intent = getIntent();
        currentItem = intent.getIntExtra("currentPos", 0);
        imgList = intent.getStringArrayListExtra("img_list");
    }

}
