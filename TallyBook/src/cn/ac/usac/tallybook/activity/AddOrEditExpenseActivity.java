package cn.ac.usac.tallybook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 记一笔or编辑
 * @author Administrator
 *
 */
public class AddOrEditExpenseActivity extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener {
	
	private Context context = AddOrEditExpenseActivity.this;
	
	private TextView title_tv = null;
	private RadioGroup trans_type_tab_rg = null;
	//支出单选按钮
	private RadioButton payout_tab_rb=null;
	//收入单选按钮
	private RadioButton income_tab_rb=null;
	//输入金额
	private Button cost_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_or_edit_expense_activity);
		loadingFormation();
	}

	public void loadingFormation() {
		
		title_tv = (TextView) findViewById(R.id.title_tv);
		
		trans_type_tab_rg = (RadioGroup) findViewById(R.id.trans_type_tab_rg);
		payout_tab_rb = (RadioButton) findViewById(R.id.payout_tab_rb);
		payout_tab_rb.setChecked(true);
		payout_tab_rb.setOnCheckedChangeListener(this);
		
		cost_btn = (Button)findViewById(R.id.cost_btn);
		cost_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		
		if(view == cost_btn){
			Intent in = new Intent(context, KeyPadActivity.class);
//			intent.putExtra("value", value);
//			startActivityForResult(intent, 0);
			startActivity(in);
		}
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
//		//支出
//		if(payout_tab_rb.isChecked())
//		{
//			type = PAYOUT_MODE;
//			corporation_fl.setVisibility(View.VISIBLE);
//			empty_fl.setVisibility(View.GONE);
//		}else //收入
//		{
//			type = INCOME_MODE;
//			corporation_fl.setVisibility(View.GONE);
//			empty_fl.setVisibility(View.VISIBLE);
//		}
//		updateInfo(-1);
	}
	
}
