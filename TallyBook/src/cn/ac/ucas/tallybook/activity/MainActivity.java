package cn.ac.ucas.tallybook.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.ac.ucas.tallybook.util.DialogUtil;
import cn.ac.ucas.tallybook.util.GeneralInfo;
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
			int lgn = login(tenantID, password);
			
			//开发调试暂用*****
//			int lgn = 1;
			//************
			
			if(lgn == 1) {
				intent = new Intent(context, MainTallyBookActivity.class);
				startActivity(intent);
				finish();
			} else if(lgn == 2) {
				DialogUtil.showDialog(context, "账号或密码错误，请重新输入！", false);
			} else {
				DialogUtil.showDialog(context, "服务器响应异常，请稍后再试！", false);
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
		int flag = 3;
		// 使用Map封装请求参数
		Map<String, String> map = new HashMap<String, String>();
		map.put("target", "login");
		map.put("tenantID", tenantID);
		map.put("password", password);
		// 定义发送请求的URL
		String url = HttpUtil.BASE_URL + "TenantServlet";
		JSONObject jsonObject = null;
		// 发送请求
		 try {
			jsonObject = new JSONObject(HttpUtil.postRequest(url, map));
			if(jsonObject.getBoolean("flag")) {
				flag = 1;
			} else if(!jsonObject.getBoolean("flag")) {
				flag = 2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}