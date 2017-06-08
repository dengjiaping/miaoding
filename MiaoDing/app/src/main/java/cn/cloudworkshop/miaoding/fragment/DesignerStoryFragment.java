package cn.cloudworkshop.miaoding.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseFragment;
import cn.cloudworkshop.miaoding.constant.Constant;

/**
 * Author：binge on 2017-06-08 15:39
 * Email：1993911441@qq.com
 * Describe：
 */
public class DesignerStoryFragment extends BaseFragment {
    @BindView(R.id.img_designer_story)
    ImageView imgDesignerStory;
    private Unbinder unbinder;

    private String imgStory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_designer_story, container, false);
        unbinder = ButterKnife.bind(this, view);
        getData();
        initView();
        return view;
    }

    private void initView() {
        Glide.with(getActivity())
                .load(Constant.HOST + imgStory)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imgDesignerStory);

    }

    private void getData() {
        Bundle bundle = getArguments();
        imgStory = bundle.getString("designer_story");
    }


    public static DesignerStoryFragment newInstance(String imgUrl) {

        Bundle args = new Bundle();
        args.putString("designer_story", imgUrl);
        DesignerStoryFragment fragment = new DesignerStoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}