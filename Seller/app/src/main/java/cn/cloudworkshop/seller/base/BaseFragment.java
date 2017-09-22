package cn.cloudworkshop.seller.base;

import android.app.Fragment;
import android.content.Context;

/**
 * Author：binge on 2017-09-14 16:12
 * Email：1993911441@qq.com
 * Describe：
 */
public class BaseFragment extends Fragment{
    private BaseActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (BaseActivity) context;
    }
}
