package cn.cloudworkshop.miaoding.utils;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

import cn.cloudworkshop.miaoding.R;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Author：Libin on 2016/11/3 09:13
 * Email：1993911441@qq.com
 * Describe：分享
 */
public class ShareUtils {

    public static void showShare(Activity activity,String imgUrl, String title, String content, String url) {

        ShareSDK.initSDK(activity, "188b0b9b49186");
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        //分享图片
        oks.setImageUrl(imgUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(activity.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 启动分享GUI
        oks.show(activity);

        MobclickAgent.onEvent(activity,"share");
    }
}

