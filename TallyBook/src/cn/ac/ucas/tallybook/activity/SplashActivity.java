package cn.ac.ucas.tallybook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
private Context context = SplashActivity.this;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		// 显示30秒后进入到系统主界面
		Handler handler = new Handler();
		handler.postDelayed(new StartMainThread(),300);
	}
	
	class StartMainThread implements Runnable {
		@Override
		public void run() {
			// 启动MainActivity
			Intent intent = new Intent();
			intent.setClass(context, MainActivity.class);
			startActivity(intent);
			// 将SplashActivity销毁
			finish();
		}
	}
}
