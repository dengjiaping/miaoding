package cn.cloudworkshop.miaoding.ui;

import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
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
import cn.cloudworkshop.miaoding.constant.Constant;

/**
 * Author：binge on 2016/12/12 11:44
 * Email：1993911441@qq.com
 * Describe：邀请好友,活动规则
 */
public class ShareRuleActivity extends BaseActivity {
    @BindView(R.id.img_header_back)
    ImageView imgHeaderBack;
    @BindView(R.id.tv_header_title)
    TextView tvHeaderTitle;
    @BindView(R.id.webview_share_rule)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_rule);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 加载webView
     */
    private void initView() {
        tvHeaderTitle.setText("活动规则");
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAppCacheEnabled(true);
        ws.setAppCachePath(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "CloudWorkshop/WebCache");
        ws.setAllowFileAccess(true);
        ws.setBuiltInZoomControls(false);
        ws.setSupportZoom(false);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(Constant.SHARE_RULE);
    }

    @OnClick(R.id.img_header_back)
    public void onClick() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
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
