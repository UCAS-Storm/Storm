package cn.ac.usac.tallybook.activity;

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
	
	private Button add_expense_quickly_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tallybook);
		add_expense_quickly_btn = (Button)findViewById(R.id.add_expense_quickly_btn);
		add_expense_quickly_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		
		Intent in = null;
		if (view == add_expense_quickly_btn) {
			in = new Intent(context, AddOrEditExpenseActivity.class);
			startActivity(in);
//			finish();
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