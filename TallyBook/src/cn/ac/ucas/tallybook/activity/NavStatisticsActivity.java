package cn.ac.ucas.tallybook.activity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.R.string;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.ac.ucas.tallybook.util.BaseFormat;
import cn.ac.ucas.tallybook.util.DialogUtil;
import cn.ac.ucas.tallybook.util.HttpUtil;

public class NavStatisticsActivity extends Activity implements OnClickListener{

	private Context context = NavStatisticsActivity.this;
	
	//起始时间
	private Button start_time_btn = null;
	private DatePickerDialog sPicker = null;
	//结束时间
	private Button end_time_btn = null;
	private DatePickerDialog ePicker = null;
	
	//总收入、支出
	private TextView income_sum_tv = null;
	private TextView payout_sum_tv = null;
	
	//查询按钮
	private Button query_btn = null;
	
	//饼图查看按钮
	private Button show_in_btn = null;
	private Button show_pay_btn = null;
	
	//收入总和
	private double incomeSum = 0;
	//支出总和
	private double payoutSum = 0;
	//收入分类数据
	private Map<String, Double> incomeData = new HashMap<String, Double>();
	//支出分类数据
	private Map<String, Double> payoutData = new HashMap<String, Double>();
	
	private Calendar calendar = Calendar.getInstance();
	
	////////AChartEngine
	private static int[] COLORS = new int[] { Color.RED, Color.GREEN,
		Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.DKGRAY };
	double data[] = new double[] { 20, 30, 40, 50, 60, 70, 80, 90, 100 };

	private CategorySeries mSeries = new CategorySeries("");// PieChart的DataSet
														// 其实就是一些键值对，跟Map使用方法差不多

	private DefaultRenderer mRenderer = new DefaultRenderer();// PieChart的主要描绘器

	private GraphicalView mChartView;// 用来显示PieChart 需要在配置文件Manifest中添加
									// <activity
									// android:name="org.achartengine.GraphicalActivity"
									// />

	private LinearLayout mLinear;	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_statistics_activity);
		loadingInformation();		
	}
	
	public void loadingInformation() {
		//起始时间
		start_time_btn = (Button)findViewById(R.id.start_time_btn);
		start_time_btn.setOnClickListener(this);
		start_time_btn.setText(format(calendar.getTime()));
		
		//结束时间
		end_time_btn = (Button)findViewById(R.id.end_time_btn);
		end_time_btn.setOnClickListener(this);
		end_time_btn.setText(format(calendar.getTime()));
		
		//查询按钮
		query_btn = (Button) findViewById(R.id.query_btn);
		query_btn.setOnClickListener(this);
		
		//总收入
		income_sum_tv = (TextView)findViewById(R.id.income_sum_tv);
		//总支出
		payout_sum_tv = (TextView)findViewById(R.id.payout_sum_tv);
		
		//查看收入饼图按钮
		show_in_btn = (Button) findViewById(R.id.show_in_btn);
		show_in_btn.setOnClickListener(this);
		//查看支出饼图按钮
		show_pay_btn = (Button) findViewById(R.id.show_pay_btn);
		show_pay_btn.setOnClickListener(this);
		
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
			incomeData.clear();
			payoutData.clear();
			
			
			//判断所设置时间是否有效
			String startTime = start_time_btn.getText().toString().trim();
			String endTime = end_time_btn.getText().toString().trim();
			if (BaseFormat.String2Date(startTime).after(BaseFormat.String2Date(endTime))) {
				DialogUtil.showDialog(context, "日期设置不正确，请重新输入！", false);
				return;
			}
			
			// 使用Map封装请求参数
			Map<String, String> map = new HashMap<String, String>();
			map.put("target", "loadStatInfo");
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			
			// 定义发送请求的URL
			String url = HttpUtil.BASE_URL + "LoadStatServlet";
			
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
					JSONArray incomeArray = jsonArray.getJSONArray(2);
					if(incomeArray != null) {
						for (int i = 0; i < incomeArray.length(); i++) {
							JSONObject jsonObject = incomeArray.optJSONObject(i);
							String name = jsonObject.getString("categoryName");
							double count = jsonObject.getDouble("count");
							incomeData.put(name, count);
						}
					}
					//获取支出分类数据
					JSONArray payourArray = jsonArray.getJSONArray(3);
					if(payourArray != null) {
						for (int i = 0; i < payourArray.length(); i++) {
							JSONObject jsonObject = payourArray.optJSONObject(i);
							String name = jsonObject.getString("categoryName");
							double count = jsonObject.getDouble("count");
							payoutData.put(name, count);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (view == show_in_btn) {
			//显示收入饼图
			showPieChart(1, incomeData);
		}
		
		if (view == show_pay_btn) {
			//显示支出饼图
			showPieChart(2, payoutData);
		}
	}
	
	//显示饼图
	private void showPieChart(int type, Map<String, Double> statData) {
		mRenderer.removeAllRenderers();
		mSeries.clear();
		
		mLinear = (LinearLayout) findViewById(R.id.chart);
		
		mRenderer.setStartAngle(180);// 设置为水平开始
		mRenderer.setDisplayValues(true);// 显示数据
		mRenderer.setFitLegend(true);// 设置是否显示图例
		mRenderer.setLegendTextSize(28);// 设置图例字体大小
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setLegendHeight(10);// 设置图例高度
		mRenderer.setChartTitle(type==1 ? "收入饼图" : "支出饼图");// 设置饼图标题
		
		Iterator<Map.Entry<String, Double>> statIterator = statData.entrySet().iterator();
		int cnt = 0;//color
		
		while (statIterator.hasNext()){
			Map.Entry<String, Double> entry = statIterator.next();
			mSeries.add(entry.getKey(), entry.getValue() / incomeSum);
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			if (cnt < COLORS.length) {
				renderer.setColor(COLORS[cnt++]);// 设置描绘器的颜色
			} else {
				renderer.setColor(getRandomColor());// 设置描绘器的颜色
			}
			renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// 设置百分比
			mRenderer.setChartTitleTextSize(32);// 设置饼图标题大小
			mRenderer.addSeriesRenderer(renderer);// 将最新的描绘器添加到DefaultRenderer中
		}
		if (mChartView == null) {// 为空需要从ChartFactory获取PieChartView
			mChartView = ChartFactory.getPieChartView(getApplicationContext(), mSeries, mRenderer);// 构建mChartView
			mRenderer.setClickEnabled(true);// 允许点击事件
			
			mChartView.setOnClickListener(new View.OnClickListener() {// 具体内容
				@Override
				public void onClick(View v) {
					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();// 获取当前的类别和指针
					if (seriesSelection != null) {
						for (int i = 0; i < mSeries.getItemCount(); i++) {
							mRenderer.getSeriesRendererAt(i).setHighlighted(i == seriesSelection.getPointIndex());
						}
						mChartView.repaint();
					}
				}
			});
			mLinear.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			mChartView.repaint();
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
	
	private int getRandomColor() {// 分别产生RBG数值
		Random random = new Random();
		int R = random.nextInt(255);
		int G = random.nextInt(255);
		int B = random.nextInt(255);
		return Color.rgb(R, G, B);
	}
}
