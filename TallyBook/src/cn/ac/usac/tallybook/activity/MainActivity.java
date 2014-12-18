package cn.ac.usac.tallybook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {
	
	private Context context = MainActivity.this;
	
	Button signin_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		signin_btn = (Button)findViewById(R.id.signin_btn);
		signin_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Intent in = null;
		if (view == signin_btn) {
			in = new Intent(context, MainTallyBookActivity.class);
			startActivity(in);
			finish();
		}
	}
}