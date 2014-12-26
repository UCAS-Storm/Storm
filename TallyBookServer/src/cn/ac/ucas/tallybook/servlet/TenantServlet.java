package cn.ac.ucas.tallybook.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ac.ucas.tallybook.manager.TenantManager;
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
			if(flag) {
				req.getSession().setAttribute("tenantID", tenantID);
			}
			// 把验证的loginFlag封装成JSONObject
			try {
				JSONObject jsonObj = new JSONObject().put("flag" , flag);
				// 输出响应
				res.getWriter().println(jsonObj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
