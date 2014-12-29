package cn.ac.ucas.tallybook.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.ac.ucas.tallybook.manager.BuyServiceManager;
import cn.ac.ucas.tallybook.manager.TenantManager;
import cn.ac.ucas.tallybook.manager.impl.BuyServiceManagerImpl;
import cn.ac.ucas.tallybook.manager.impl.TenantManagerImpl;

public class TenantServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		String target = req.getParameter("target");
		
		/**
		 * 登录判断
		 */
		if("login".equals(target)) {
			String tenantID = req.getParameter("tenantID");
			String password = req.getParameter("password");
			TenantManager tenantManager =  TenantManagerImpl.getInstance();
			boolean flag = tenantManager.login(tenantID, password);
			List buyServices = null;
			if(flag) {
				req.getSession().setAttribute("tenantID", tenantID);
				BuyServiceManager buyServiceManager = BuyServiceManagerImpl.getInstance();
				buyServices = buyServiceManager.findAllBuyService(tenantID);
			}
			// 把验证的loginFlag封装成JSONObject
			try {
				// 输出响应
				List data = new ArrayList();
				data.add(0, flag);
				data.add(1, buyServices);

				JSONArray jsonArray = new JSONArray(data);
				res.getWriter().println(jsonArray);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
