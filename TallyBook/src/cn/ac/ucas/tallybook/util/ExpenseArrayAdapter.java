package cn.ac.ucas.tallybook.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.ac.ucas.tallybook.activity.R;

public class ExpenseArrayAdapter extends BaseAdapter {

	// 需要包装的JSONArray
	private JSONArray expenseArray;
	
	private Context context;
	
	//区分是哪个listview的listview_item
//	private String  itemType;
	
	public ExpenseArrayAdapter(JSONArray expenseArray, Context context)
	{
//		this.itemType = itemType;
		this.expenseArray = expenseArray;
		this.context = context;
	}
	@Override
	public int getCount()
	{
		// 返回ListView包含的列表项的数量
		return expenseArray.length();
	}

	@Override
	public Object getItem(int position)
	{
		// 获取指定列表项所包装的JSONObject
		return expenseArray.optJSONObject(position);
	}

	@Override
	public long getItemId(int position)
	{
		try
		{
			//获取JSONObject的ID
			return ((JSONObject) getItem(position)).getInt("expenseID");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		view = LayoutInflater.from(context).inflate(R.layout.main_tally_book_list_item, null);
		TextView category_name_tv = (TextView)view.findViewById(R.id.category_name_tv);
		TextView expense_tv = (TextView)view.findViewById(R.id.expense_tv);
		TextView expenseTime_tv = (TextView)view.findViewById(R.id.expenseTime_tv);
		JSONObject jsonObject = (JSONObject)getItem(position);
		try	{
			// 获取JSONArray数组元素的categoryName属性
			String categoryName = jsonObject.getString("categoryName");
			//获取JSONArray数组元素的money属性
			double money = jsonObject.getDouble("money");
			String moneyStr = String.format("￥%.2f", money);
			String expenseTimeStr = jsonObject.getString("expenseTimeStr");
			
			if (jsonObject.getInt("type") == 1)
				expense_tv.setTextColor(context.getResources().getColor(R.color.transaction_income_amount));
			else
				expense_tv.setTextColor(context.getResources().getColor(R.color.transaction_payout_amount));
			// 设置TextView所显示的内容
			category_name_tv.setText(categoryName);	
			expense_tv.setText(moneyStr);
			expenseTime_tv.setText(expenseTimeStr);
			view.setTag(jsonObject);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return view;
	}
	
	/**
	 * 添加列表项
	 * @param jsonArray
	 */
	public void loadMore(JSONArray jsonArray) {
		expenseArray.put(jsonArray);
	}
}
