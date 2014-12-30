package cn.ac.ucas.tallybook.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import cn.ac.ucas.tallybook.manager.ExpenseManager;
import cn.ac.ucas.tallybook.manager.impl.ExpenseManagerImpl;
import cn.ac.ucas.tallybook.model.Category;

public class LoadStatServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		String target = req.getParameter("target");
//		String tenantID = (String) req.getSession().getAttribute("tenantID");
		String tenantID = "liubei";
		
		String starTime = req.getParameter("startTime");
		String endTime = req.getParameter("endTime");
		
//		String starTime = "2008-08-08";
//		String endTime = "2014-12-31";
		if(tenantID != null && !"".equals(tenantID)) {
			
			/**
			 * 响应主界面请求
			 */
			if("loadStatInfo".equals(target)) {
							
				ExpenseManager expenseManager = ExpenseManagerImpl.getInstance();
				//收入
				double isumAll = expenseManager.sumAllPeriod(tenantID, 1, starTime, endTime);
				//支出
				double psumAll = expenseManager.sumAllPeriod(tenantID, 2, starTime, endTime);
				//按类别统计收入、支出
				List<Category> incomeData = expenseManager.sumAllPeriodByCategs(tenantID, 1, starTime, endTime);
				List<Category> payoutData = expenseManager.sumAllPeriodByCategs(tenantID, 2, starTime, endTime);
				
				List data = new ArrayList();
				data.add(0, isumAll);
				data.add(1, psumAll);
				data.add(2, incomeData);
				data.add(3, payoutData);

				JSONArray jsonArray = new JSONArray(data);
				res.getWriter().println(jsonArray);
			}
		}
	}
}
