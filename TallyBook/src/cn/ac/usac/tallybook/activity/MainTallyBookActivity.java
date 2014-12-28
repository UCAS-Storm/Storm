package cn.ac.usac.tallybook.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.ac.ucas.tallybook.util.GeneralInfo;
import cn.ac.ucas.tallybook.util.HttpUtil;
import cn.ac.ucas.tallybook.util.ExpenseArrayAdapter;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
//	private TextView listview_loading_tv = null;
	
	//加载更多view
	private View load_more_view = null;
	
	//最后的可视项索引  
	private int visibleLastIndex = 0;
	
	private static int pageNo = 1;
	
	private static final int pageSize = 12;
	
	// 当前窗口可见项总数 
    private int visibleItemCount;   
    
    private ExpenseArrayAdapter expenseArrayAdapter;
	
	//加载更多
	private Button load_more_btn = null;
	
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
		load_more_view = LayoutInflater.from(context).inflate(R.layout.load_more, null);
		expense_lv.addFooterView(load_more_view);
		expense_lv.setOnScrollListener(this);
		load_more_btn = (Button)load_more_view.findViewById(R.id.load_more_btn);
		load_more_btn.setOnClickListener(this);
		
//		listview_loading_tv = (TextView) findViewById(R.id.listview_loading_tv);
		
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
		}
		
		//统计
		if (view == nav_statistics_tv) {
			intent = new Intent(context, NavStatisticsActivity.class);
			startActivity(intent);
		}
		
		//加载更多
		if(view == load_more_btn) {
			pageNo = pageNo + 1;
			loadMore();
		}
	}
	
	/**
	 * 加载应用主界面
	 */
	public void loadMainInfo() {
		
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("target", "loadMainInfo");
		map.put("pageNo", "" + pageNo);
		map.put("pageSize", "" + pageSize);
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
					expenseArrayAdapter = new ExpenseArrayAdapter(expenseArray, context);
					expense_lv.setAdapter(expenseArrayAdapter);
				}
//				listview_loading_tv.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 滑动时被调用
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		this.visibleItemCount = visibleItemCount; 
//		// 计算最后可见条目的索引
//        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;  
//        // 所有的条目已经和最大条数相等，则移除底部的View
//        if (totalItemCount > maxVisibleNum) {
//        	expense_lv.removeFooterView(load_more_view);
//            Toast.makeText(this, "没有更多数据！", Toast.LENGTH_LONG).show();
//        }
	}

	/**
	 * 滑动状态改变时被调用 
	 */
	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {

//		/** 
//         * 当ListView滑动到最后一条记录时这时，我们会看到已经被添加到ListView的"加载项"布局， 这时应该加载剩余数据。 
//         */  
//        if (mLastItem == mListViewAdapter.count  
//                && mScrollState == OnScrollListener.SCROLL_STATE_IDLE) {  
//            if (mListViewAdapter.count <= mCount) {  
//                mHandler.postDelayed(new Runnable() {  
//                    @Override  
//                    public void run() {  
//                        mListViewAdapter.count += 10;  
//                        mListViewAdapter.notifyDataSetChanged();  
//                        mListView.setSelection(mLastItem);  
//                    }  
//                }, 1000);  
//            }  
//        }  
	}
	
	public void loadMore() {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {

					// 使用Map封装请求参数
					Map<String, String> map = new HashMap<String, String>();
					map.put("target", "loadMainInfo");
					map.put("pageNo", "" + pageNo);
					map.put("pageSize", "" + pageSize);
					// 定义发送请求的URL
					String url = HttpUtil.BASE_URL + "LoadInfoServlet";
//					Thread.sleep(3000);
					JSONArray jsonArray = null;
					
					// 发送请求
					 try {
						jsonArray = new JSONArray(HttpUtil.postRequest(url, map));
						Thread.sleep(3000);
						if(jsonArray != null) {
							JSONArray expenseArray = jsonArray.getJSONArray(2);
							expenseArrayAdapter.loadMore(expenseArray);
							expenseArrayAdapter.notifyDataSetChanged();    //数据集变化后,通知adapter
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}).start();
	}
	
}