package cn.ac.ucas.tallybook.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdk.nashorn.api.scripting.JSObject;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.ac.ucas.tallybook.manager.ExpenseManager;
import cn.ac.ucas.tallybook.manager.impl.ExpenseManagerImpl;
import cn.ac.ucas.tallybook.model.Expense;

public class LoadFlowServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		String target = req.getParameter("target");
//		String tenantID = (String) req.getSession().getAttribute("tenantID");
		String starTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		/**
		 * 调试用
		 */
		String tenantID = "liubei";
//		String starTime = "2008-08-08";
//		String endTime = "2014-12-31";
		
		if(tenantID != null && !"".equals(tenantID)) {
			
			/**
			 * 响应主界面请求:只显示当天最近两条记录
			 */
			if("loadFlowInfo".equals(target)) {
				
				ExpenseManager expenseManager = ExpenseManagerImpl.getInstance();
				//收入
				double isumAll = expenseManager.sumAllPeriod(tenantID, 1, starTime, endTime);
				//支出
				double psumAll = expenseManager.sumAllPeriod(tenantID, 2, starTime, endTime);
				//按类别统计收入、支出
				List<Expense> expenses = expenseManager.findAllExpenses(tenantID, starTime, endTime);
				
				List data = new ArrayList();
				data.add(0, isumAll);
				data.add(1, psumAll);
				data.add(2, expenses);

				JSONArray jsonArray = new JSONArray(data);
				res.getWriter().println(jsonArray);
			}
			
			if("delFlowInfo".equals(target)) {
				ExpenseManager expenseManager = ExpenseManagerImpl.getInstance();
				int expenseID = Integer.parseInt(req.getParameter("expenseID"));
				boolean delFlag = expenseManager.deleteExpense(expenseID);
				List data = new ArrayList();
				data.add(0, delFlag);
//				JSONObject jsonObject = new JSONObject(delFlag);
				JSONArray jsonArray = new JSONArray(data);
				res.getWriter().println(jsonArray);
			}
		}
	}
}
