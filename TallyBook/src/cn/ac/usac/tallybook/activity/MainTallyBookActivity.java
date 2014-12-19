package cn.ac.usac.tallybook.activity;

import cn.ac.ucas.tallybook.utility.GeneralInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainTallyBookActivity extends Activity implements OnClickListener {

	private Context context = MainTallyBookActivity.this;
	
	//记一笔按钮
	private Button add_expense_quickly_btn = null;
	
	//流水
	private TextView nav_flow_tv = null;
	//统计
	private TextView nav_statistics_tv = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tallybook);
		
		//记一笔
		add_expense_quickly_btn = (Button)findViewById(R.id.add_expense_quickly_btn);
		add_expense_quickly_btn.setOnClickListener(this);
		
		//流水
		nav_flow_tv = (TextView)findViewById(R.id.nav_flow);
		nav_flow_tv.setOnClickListener(this);
		
		//统计
		nav_statistics_tv = (TextView)findViewById(R.id.nav_statistics);
		nav_statistics_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		
		Intent intent = null;
		
		//记一笔
		if (view == add_expense_quickly_btn) {
			intent = new Intent(context, AddOrEditExpenseActivity.class);
			intent.putExtra("type", GeneralInfo.getPayoutMode());
			startActivity(intent);
//			finish();
		}
		
		//统计
		if (view == nav_statistics_tv) {
			intent = new Intent(context, NavStatisticsActivity.class);
			startActivity(intent);
		}
	}
	
	private void showNavExpenseActivity(long startTime, long endTime, String title, int mode)
	{
//		Intent intent = new Intent(this, NavExpenseActivity.class);
//		intent.putExtra(NavExpenseActivity.str_startTime, startTime);
//		intent.putExtra(NavExpenseActivity.str_endTime, endTime);
//		intent.putExtra(NavExpenseActivity.str_title, title);
//		intent.putExtra(NavExpenseActivity.str_mode, mode);
		
//		startActivity(intent);
	}
}