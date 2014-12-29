package cn.ac.ucas.tallybook.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import cn.ac.ucas.tallybook.manager.ExpenseManager;
import cn.ac.ucas.tallybook.manager.impl.ExpenseManagerImpl;
import cn.ac.ucas.tallybook.model.Expense;
import cn.ac.ucas.tallybook.util.BaseFormat;

public class LoadInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		res.setContentType("text/html; charset=UTF-8");
		String target = req.getParameter("target");
//		String tenantID = (String) req.getSession().getAttribute("tenantID");
		/**
		 * 调试用
		 */
		String tenantID = "liubei";
		
		if(tenantID != null && !"".equals(tenantID)) {
			
			/**
			 * 响应主界面请求:只显示当天最近两条记录
			 */
			if("loadMainInfo".equals(target)) {
				
				int pageNo = 1;
				int pageSize = 2;
				
				ExpenseManager expenseManager = ExpenseManagerImpl.getInstance();
				//收入
				double isumAll = expenseManager.sumAll(tenantID, 1);
				 //支出
				double psumAll = expenseManager.sumAll(tenantID, 2);
				//花费记录
				List expenses = expenseManager.findExpenses(pageNo, pageSize, tenantID);
				for (Iterator iterator = expenses.iterator(); iterator.hasNext();) {
					Expense expense = (Expense) iterator.next();
					expense.setExpenseTimeStr(BaseFormat.Date2String(expense.getExpenseTime()));
				}
				List data = new ArrayList();
				data.add(0, isumAll);
				data.add(1, psumAll);
				data.add(2, expenses);

				JSONArray jsonArray = new JSONArray(data);
				res.getWriter().println(jsonArray);
			}
		}
	}
}
