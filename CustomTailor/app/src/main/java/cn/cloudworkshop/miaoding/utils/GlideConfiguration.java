package cn.cloudworkshop.miaoding.utils;


import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;

import java.io.File;

/**
 * Author：binge on 2016/11/9 15:08
 * Email：1993911441@qq.com
 * Describe：Glide图片清晰度
 */

public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "CloudWorkshop/GlideCache");
                if (!file.exists()){
                    file.mkdirs();
                }
                return DiskLruCacheWrapper.get(file, 1024 * 1024 * 100);
            }
        });
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}