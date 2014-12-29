package cn.ac.ucas.tallybook.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.ac.ucas.tallybook.util.BaseFormat;
import cn.ac.ucas.tallybook.util.DialogUtil;
import cn.ac.ucas.tallybook.util.ExpenseArrayAdapter;
import cn.ac.ucas.tallybook.util.HttpUtil;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NavFlowActivity extends Activity implements OnClickListener, OnScrollListener {
	
	private Context context = NavFlowActivity.this;
	
	//起始时间
	private Button start_time_btn = null;
	private DatePickerDialog sPicker = null;
	//结束时间
	private Button end_time_btn = null;
	private DatePickerDialog ePicker = null;
	
	//总收入、支出
	private TextView income_sum_tv = null;
	private TextView payout_sum_tv = null;
	private ListView expense_lv = null;
	
	//收入总和
	private double incomeSum = 0;
	//支出总和
	private double payoutSum = 0;
	
	//查询按钮
	private Button query_btn = null;
	
	private Calendar calendar = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_flow);
		loadingInformation();
	}

	public void loadingInformation() {
		//起始时间
		start_time_btn = (Button)findViewById(R.id.flow_start_time_btn);
		start_time_btn.setOnClickListener(this);
		start_time_btn.setText(format(calendar.getTime()));
		
		//结束时间
		end_time_btn = (Button)findViewById(R.id.flow_end_time_btn);
		end_time_btn.setOnClickListener(this);
		end_time_btn.setText(format(calendar.getTime()));
		
		//查询按钮
		query_btn = (Button) findViewById(R.id.flow_query_btn);
		query_btn.setOnClickListener(this);
		
		//总收入
		income_sum_tv = (TextView)findViewById(R.id.flow_income_sum_tv);
		//总支出
		payout_sum_tv = (TextView)findViewById(R.id.flow_payout_sum_tv);
		
		expense_lv = (ListView) findViewById(R.id.flow_expense_lv);
		
		String startTime = start_time_btn.getText().toString().trim();
		String endTime = end_time_btn.getText().toString().trim();
		loadFlowExpenses(startTime, endTime);
	}
	
	@Override
	public void onClick(View view) {

		//选择支出或收入时间按钮监听
		if(view == start_time_btn){
			sPicker = new DatePickerDialog(this, sDateSetListenerSatrt, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			sPicker.show();
		}
		
		//选择支出或收入时间按钮监听
		if(view == end_time_btn){
			ePicker = new DatePickerDialog(this, eDateSetListenerSatrt, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
			ePicker.show();
		}
		
		//查询事件监听
		if(view == query_btn){
			//清除先前数据
//			incomeData.clear();
//			payoutData.clear();
			
			//判断所设置时间是否有效
			String startTime = start_time_btn.getText().toString().trim();
			String endTime = end_time_btn.getText().toString().trim();
			if (BaseFormat.String2Date(startTime).after(BaseFormat.String2Date(endTime))) {
				DialogUtil.showDialog(context, "日期设置不正确，请重新输入！", false);
				return;
			}
			
			loadFlowExpenses(startTime, endTime);
		}
	}
	
	
	
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void loadFlowExpenses(String startTime, String endTime) {
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("target", "loadFlowInfo");
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "LoadFlowServlet";
		
		JSONArray jsonArray = null;
		// 发送请求
		 try {
			jsonArray = new JSONArray(HttpUtil.postRequest(url, map));
			if(jsonArray != null) {
				incomeSum = jsonArray.getInt(0);
				payoutSum = jsonArray.getInt(1);
				String isumAllStr = String.format("￥%.2f", incomeSum);
				income_sum_tv.setText(isumAllStr);
				String psumAllStr = String.format("￥%.2f", payoutSum);
				payout_sum_tv.setText(psumAllStr);
				//获取收入分类数据
				JSONArray expenseArray = jsonArray.getJSONArray(2);
				if(expenseArray != null) {
					expense_lv.setAdapter(new ExpenseArrayAdapter(expenseArray, context));
				} else {
					Toast toast = Toast.makeText(getApplicationContext(), "今天还没有记录呢，快来添加吧。", 
								Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
				    toast.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//获取日期
	private DatePickerDialog.OnDateSetListener sDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.YEAR, year);
			start_time_btn.setText(format(calendar.getTime()));
		}
	};

	//获取日期
	private DatePickerDialog.OnDateSetListener eDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			calendar.set(Calendar.MONTH, monthOfYear);
			calendar.set(Calendar.YEAR, year);
			end_time_btn.setText(format(calendar.getTime()));
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
}
