package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.base.BaseActivity;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.utils.ShareUtils;
import cn.cloudworkshop.miaoding.utils.SharedPreferencesUtils;

/**
 * Author：Libin on 2016/8/31 11:37
 * Email：1993911441@qq.com
 * Describe：作品详情
 */
public class WorksInfoActivity extends BaseActivity {


    @BindView(R.id.img_works_back)
    ImageView imgWorksBack;

    @BindView(R.id.webview_designer_works)
    WebView webView;
    @BindView(R.id.img_works_share)
    ImageView imgWorksShare;

    private String id = "";
    private String imgUrl = "";
    private String title = "";
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_info);
        ButterKnife.bind(this);
        initView();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }

    /**
     * 加载webview
     */
    private void initView() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        imgUrl = intent.getStringExtra("img_url");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        imgWorksShare.setVisibility(View.VISIBLE);

        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(false);
        ws.setDomStorageEnabled(true);
        ws.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT >= 19) {
            ws.setLoadsImagesAutomatically(true);
        } else {
            ws.setLoadsImagesAutomatically(false);
        }
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.addJavascriptInterface(this, "nativeMethod");
        webView.loadUrl(Constant.DESIGNER_WORKS_INFO + "?type=2&" + "id=" + id + "&token=" +
                SharedPreferencesUtils.getString(this, "token"));

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!url.equals("myapp://tobuy")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }
        });
    }

    @JavascriptInterface
    public void toActivity(String activityName) {
        if (TextUtils.equals(activityName, "a")) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (TextUtils.equals(activityName, "b")) {
            Intent intent = new Intent(WorksInfoActivity.this, GoodsDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putString("img_url", imgUrl);
            intent.putExtras(bundle);
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

    @OnClick({R.id.img_works_back, R.id.img_works_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_works_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.img_works_share:
                ShareUtils.showShare(this, Constant.HOST + imgUrl, title, content,
                        Constant.DESIGNER_WORKS_SHARE + "?type=2&" + "id=" + id +
                                "&token=" + SharedPreferencesUtils.getString(this, "token"));
                break;
        }
    }
}
