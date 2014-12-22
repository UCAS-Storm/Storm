package cn.ac.usac.tallybook.activity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.ac.ucas.tallybook.utility.GeneralInfo;

/**
 * 记一笔or编辑
 * @author Administrator
 *
 */
public class AddOrEditExpenseActivity extends Activity implements OnClickListener, OnCheckedChangeListener, OnItemSelectedListener {
	
	private Context context = AddOrEditExpenseActivity.this;
	

	private int type = GeneralInfo.getPayoutMode();
	
	private TextView title_tv = null;
	private RadioGroup trans_type_tab_rg = null;
	//支出单选按钮
	private RadioButton payout_tab_rb=null;
	//收入单选按钮
	private RadioButton income_tab_rb=null;
	//输入金额按钮
	private Button cost_btn = null;
	//money
	private String  value = "0";
	//类别选择
	private Spinner category_spn = null;
	private ArrayAdapter<String> adapter;
	private List<String> list = null;
	
	private Calendar calendar = Calendar.getInstance();
	//支出(或收入)时间
	private Button trade_time_btn = null;
	private DatePickerDialog datePicker = null;
	
	//备注按钮
	Button note_btn = null;
	private EditText editText = null;
	private AlertDialog alertDialog = null;
	
	//保存按钮
	private Button save_btn = null;
	//取消按钮
	private Button cancel_btn = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_or_edit_expense_activity);
		loadingFormation();
	}

	public void loadingFormation() {
		
		type = getIntent().getIntExtra("type", GeneralInfo.getPayoutMode());
		
		title_tv = (TextView) findViewById(R.id.title_tv);
		
		trans_type_tab_rg = (RadioGroup) findViewById(R.id.trans_type_tab_rg);
		payout_tab_rb = (RadioButton) findViewById(R.id.payout_tab_rb);
		income_tab_rb = (RadioButton) findViewById(R.id.income_tab_rb);
		switch (type) {
		case 0:
			income_tab_rb.setChecked(true);
			break;
		case 1:
			payout_tab_rb.setChecked(true);
			break;
		default:
			break;
		}
		
		payout_tab_rb.setOnCheckedChangeListener(this);
		income_tab_rb.setOnCheckedChangeListener(this);
		
		cost_btn = (Button)findViewById(R.id.cost_btn);
		cost_btn.setOnClickListener(this);
		
		//类别
		category_spn = (Spinner)findViewById(R.id.category_spn);
		//此处list的值应该从数据库读出
		list = Arrays.asList(new String[] { "餐饮", "娱乐", "购物", "交通", "工资", "其他"});
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category_spn.setAdapter(adapter);
		category_spn.setOnItemSelectedListener(this);
		
		trade_time_btn = (Button)findViewById(R.id.trade_time_btn);
		trade_time_btn.setOnClickListener(this);
		//编辑模式
		if(type == GeneralInfo.getEditMode()) {
			
		} else {
			trade_time_btn.setText(format(calendar.getTime()));
		}
		
		//备注按钮
		note_btn = (Button) findViewById(R.id.note_btn);
		note_btn.setOnClickListener(this);
		
		//保存按钮
		save_btn = (Button) findViewById(R.id.save_btn);
		save_btn.setOnClickListener(this);
		
		//取消按钮
		cancel_btn = (Button) findViewById(R.id.cancel_btn);
		cancel_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		
		//收入金额按钮监听
		if(view == cost_btn){
			Intent intent = new Intent(context, KeyPadActivity.class);
			intent.putExtra("value", value);
			startActivityForResult(intent, 0);
		}
		
		//选择支出或收入时间按钮监听
		if(view == trade_time_btn){
			datePicker = new DatePickerDialog(this, mDateSetListenerSatrt, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			datePicker.show();
		}
		
		//备注按钮事件监听
		if(view == note_btn){
			
			editText = new EditText(this);
			editText.setLines(5);
			editText.setText(note_btn.getText());
			alertDialog = new AlertDialog.Builder(this)
				.setTitle(getString(R.string.dialog_memo_title))
				.setView(editText)
				.setPositiveButton(getString(R.string.dialog_memo_ok), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						note_btn.setText(editText.getText());
					}
				}).setNegativeButton(getString(R.string.dialog_memo_cancle), new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
			}).show();
		}
		
		//保存事件监听
		if(view == save_btn){
			saveInfo();
		}
		
		//取消事件监听
		if(view == cancel_btn){
			exit();
		}
	}
	
	//回调函数
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 0) { 
			Bundle bundle = data.getExtras(); 
			value = bundle.getString("value"); 
			cost_btn.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(value)));
		} 

	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
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
	
	
	public void saveInfo() {
		if(value.equals("") || value == null || Double.parseDouble(value) <= 0){
			Toast.makeText(getApplicationContext(), getString(R.string.input_message),
			Toast.LENGTH_SHORT).show();
			return;
		}
		
		
		
		exit();
	}
	
	//获取日期
	private DatePickerDialog.OnDateSetListener mDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.YEAR, year);
			trade_time_btn.setText(format(calendar.getTime()));
		}
	};
	
	//格式化日期
	private String format(Date date){
		String str = "";
		SimpleDateFormat ymd = null;
		ymd = new SimpleDateFormat("yyyy-MM-dd");
		str = ymd.format(date); 
		return str;
	}
	
	
	private void exit(){
		if(type != GeneralInfo.getEditMode()){
			Intent intent = new Intent(context, MainTallyBookActivity.class);
			startActivity(intent);
			finish();
		}else{
//			this.setResult(RESULT_OK, getIntent());  
            this.finish();  
		}
	}
	
}
