package cn.cloudworkshop.miaoding.ui;

import android.content.Intent;
import android.os.Bundle;
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
 * Describe：设计师详情
 */
public class DesignerInfoActivity extends BaseActivity {


    @BindView(R.id.webview_designer_info)
    WebView webView;
    @BindView(R.id.img_designer_back)
    ImageView imgDesignerBack;
    @BindView(R.id.img_designer_share)
    ImageView imgDesignerShare;

    private String title = "";
    private String content = "";
    private String img = "";
    private String id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_info);
        ButterKnife.bind(this);

        getData();
        initView();
    }

    /**
     * 加载webview
     */
    private void initView() {
        imgDesignerShare.setVisibility(View.VISIBLE);
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        webView.addJavascriptInterface(this, "nativeMethod");
        ws.setSupportZoom(false);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(Constant.DESIGNER_INFO + "?id=" + id + "&token=" + SharedPreferencesUtils.getString(this, "token"));
    }

    @JavascriptInterface
    public void toActivity(String str) {
        String[] split = str.split(",");
        Intent intent = new Intent(DesignerInfoActivity.this, GoodsDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", split[0]);
        bundle.putString("img_url", split[1]);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getData() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        img = intent.getStringExtra("img_url");
        id = intent.getStringExtra("id");
    }

    @OnClick({R.id.img_designer_back, R.id.img_designer_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_designer_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.img_designer_share:
                ShareUtils.showShare(this, Constant.HOST + img, title, content,
                        Constant.DESIGNER_SHARE + "?id=" + id + "&token=" +
                                SharedPreferencesUtils.getString(this, "token"));
                break;
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

}
