package cn.ac.ucas.tallybook.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.ac.ucas.tallybook.util.DialogUtil;
import cn.ac.ucas.tallybook.util.HttpUtil;

public class MainActivity extends Activity implements OnClickListener {
	
	private Context context = MainActivity.this;
	
	Button signin_btn = null;
	EditText tenantID_edit = null;
	EditText password_edit = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		signin_btn = (Button)findViewById(R.id.signin_btn);
		signin_btn.setOnClickListener(this);
		
		tenantID_edit = (EditText)findViewById(R.id.tenantID_edit);
		password_edit = (EditText)findViewById(R.id.password_edit);
		
	}

	@Override
	public void onClick(View view) {
		Intent intent = null;
		if (view == signin_btn) {
			String tenantID = tenantID_edit.getText().toString().trim();
			String password = password_edit.getText().toString().trim();
			if("".equals(tenantID)) {
				DialogUtil.showDialog(context, "账号不能为空，请重新输入！", false);
				return;
			} 
			
			if("".equals(password)) {
				DialogUtil.showDialog(context, "密码不能为空，请重新输入！", false);
				return;
			} 
			
			int lgn = login(tenantID, password);
						
			if(lgn == 4) {
				DialogUtil.showDialog(context, "没有购买服务或服务已到期！", false);
			} else if(lgn == 5) {
				DialogUtil.showDialog(context, "账号或密码错误，请重新输入！", false);
			} else if(lgn == 3) {
				DialogUtil.showDialog(context, "服务器响应异常，请稍后再试！", false);
			} else {
				intent = new Intent(context, MainTallyBookActivity.class);
				intent.putExtra("lgn", lgn);
				startActivity(intent);
				finish();
			} 
		}
	}
	
	/**
	 * 判断是否正确登录
	 * @param tenantID
	 * @param password
	 * @return
	 */
	public int login(String tenantID, String password) {
		//服务器或服务器连接错误
		int flag = 3;
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("target", "login");
		map.put("tenantID", tenantID);
		map.put("password", password);
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "TenantServlet";
		JSONArray jsonArray = null;
		JSONArray buyServiceArray = null;
		// 发送请求
		 try {
			 jsonArray = new JSONArray(HttpUtil.postRequest(url, map));
			 boolean login = jsonArray.getBoolean(0);
			 
			if(login) {
				buyServiceArray = jsonArray.getJSONArray(1);
				if(null == buyServiceArray || 0 == buyServiceArray.length()) {
					//没有购买服务或服务已到期
					flag = 4;
				} else {
					for (int i = 0; i < buyServiceArray.length(); i++) {
						int service = buyServiceArray.optJSONObject(i).getInt("serviceID");
						if(1 == service || 2 == service || 3 == service) {
							//记账基础功能
							flag = 1;
							break;
						} else if(4 == service || 5 == service || 6 == service) {
							//记账高级功能
							flag = 2;
							break;
						}
						
					}
				}
			} else if(!login) {
				//账号或密码错误
				flag = 5;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}