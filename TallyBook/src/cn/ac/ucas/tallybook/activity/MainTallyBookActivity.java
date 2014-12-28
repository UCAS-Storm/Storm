package cn.ac.ucas.tallybook.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.ac.ucas.tallybook.util.ExpenseArrayAdapter;
import cn.ac.ucas.tallybook.util.GeneralInfo;
import cn.ac.ucas.tallybook.util.HttpUtil;

public class MainTallyBookActivity extends Activity implements OnClickListener, OnScrollListener {

	private Context context = MainTallyBookActivity.this;
	
	//全部统计之收入总额
	private TextView income_amount_tv = null;
	
	//全部统计之支出总额
	private TextView payOut_amount_tv = null;
	
	//记一笔按钮
	private Button add_expense_quickly_btn = null;
	
	//当前日期(一天)内的所有收支记录
	private ListView expense_lv = null;
	
	//正在加载TextView
	TextView listview_loading_tv = null;
	
	//流水
	private TextView nav_flow_tv = null;
	//统计
	private TextView nav_statistics_tv = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_tallybook);
		
		//全部统计之收入总额
		income_amount_tv = (TextView) findViewById(R.id.income_amount_tv);
		
		//全部统计之支出总额
		payOut_amount_tv = (TextView) findViewById(R.id.payOut_amount_tv);
		
		//记一笔
		add_expense_quickly_btn = (Button)findViewById(R.id.add_expense_quickly_btn);
		add_expense_quickly_btn.setOnClickListener(this);
		
		//当前日期(一天)内的所有收支记录
		expense_lv = (ListView)findViewById(R.id.expense_lv);
		View load_more_view = LayoutInflater.from(context).inflate(R.layout.load_more, null);
		expense_lv.addFooterView(load_more_view);
		
		listview_loading_tv = (TextView) findViewById(R.id.listview_loading_tv);
		
		//流水
		nav_flow_tv = (TextView)findViewById(R.id.nav_flow);
		nav_flow_tv.setOnClickListener(this);
		
		//统计
		nav_statistics_tv = (TextView)findViewById(R.id.nav_statistics);
		nav_statistics_tv.setOnClickListener(this);
		
		loadMainInfo();
	}

	@Override
	public void onClick(View view) {
		
		Intent intent = null;
		
		//记一笔
		if (view == add_expense_quickly_btn) {
			intent = new Intent(context, AddOrEditExpenseActivity.class);
			intent.putExtra("type", GeneralInfo.getPayoutMode());
			startActivity(intent);
			finish();
		}
		
		//统计
		if (view == nav_statistics_tv) {
			intent = new Intent(context, NavStatisticsActivity.class);
			startActivity(intent);
		}
	}
	
	public void loadMainInfo() {
		
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("target", "loadMainInfo");
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "LoadInfoServlet";
		
		JSONArray jsonArray = null;
		// 发送请求
		 try {
			jsonArray = new JSONArray(HttpUtil.postRequest(url, map));
			if(jsonArray != null) {
				double isumAll = jsonArray.getInt(0);
				double psumAll = jsonArray.getInt(1);
				String isumAllStr = String.format("￥%.2f", isumAll);
				income_amount_tv.setText(isumAllStr);
				String psumAllStr = String.format("￥%.2f", psumAll);
				payOut_amount_tv.setText(psumAllStr);
				//当前日期的所有收入或支出记录
				JSONArray expenseArray = jsonArray.getJSONArray(2);
				if(expenseArray != null) {
					expense_lv.setAdapter(new ExpenseArrayAdapter(expenseArray, context));
				}
				listview_loading_tv.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 滑动时被调用
	 */
	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 滑动状态改变时被调用 
	 */
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
