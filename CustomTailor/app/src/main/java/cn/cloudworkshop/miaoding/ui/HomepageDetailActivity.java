package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;

/**
 * Author：Libin on 2016/10/12 09:59
 * Email：1993911441@qq.com
 * Describe：主界面详情页
 */

public class HomepageDetailActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.img_header_share)
    ImageView imgHeaderShare;

    //webView url
    private String url = "";
    //商品图片
    private String imgUrl = "";
    //标题
    private String title = "";
    //内容
    private String content = "";
    //分享 url
    private String shareUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_detail);
        ButterKnife.bind(this);
        initData();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    /**
     * 商品参数
     */
    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        imgUrl = intent.getStringExtra("img_url");
        shareUrl = intent.getStringExtra("share_url");
        initView();

    }

    /**V
     * 加载webView
     */
    private void initView() {
        tvHeaderTitle.setText(title);
        imgHeaderShare.setVisibility(View.VISIBLE);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(false);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.addJavascriptInterface(this, "nativeMethod");

        webView.loadUrl(url + "&token=" + SharedPreferencesUtils.getString(this,"token"));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.equals("onlogin://tobuy")) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
    }

    @JavascriptInterface
    public void toActivity(String str) {
        if (TextUtils.equals(str, "a")) {
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            String[] split = str.split(",");
            Intent intent = new Intent(HomepageDetailActivity.this, NewGoodsDetailsActivity.class);
            intent.putExtra("id", split[0]);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @OnClick({R.id.img_header_back, R.id.img_header_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_header_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.img_header_share:
                ShareUtils.showShare(this, imgUrl, title, content, shareUrl + "&token=" +
                        SharedPreferencesUtils.getString(this,"token"));
                break;
        }
    }
}
