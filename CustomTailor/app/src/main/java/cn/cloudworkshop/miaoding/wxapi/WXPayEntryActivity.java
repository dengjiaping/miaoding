package cn.cloudworkshop.miaoding.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import cn.cloudworkshop.miaoding.R;
import cn.cloudworkshop.miaoding.application.MyApplication;
import cn.cloudworkshop.miaoding.constant.Constant;
import cn.cloudworkshop.miaoding.ui.AppointmentActivity;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{

	private IWXAPI api;
	private String errCode;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(R.color.app_bg);
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode){
				case 0:
					errCode = "支付成功";
					break;
				case -1:
					errCode = "支付失败";
					break;
				case -2:
					errCode = "支付取消";
					break;
			}
			Toast.makeText(this, errCode, Toast.LENGTH_SHORT).show();
			if (resp.errCode == 0) {
				MobclickAgent.onEvent(this,"pay");
				Intent intent = new Intent(this,AppointmentActivity.class);
                intent.putExtra("type", "pay_success");
                intent.putExtra("order_id", MyApplication.orderId);
				finish();
				startActivity(intent);
			} else {
				Intent intent = new Intent(this,AppointmentActivity.class);
				intent.putExtra("type", "pay_fail");
				intent.putExtra("order_id", MyApplication.orderId);
				finish();
				startActivity(intent);
			}
		}
	}

}