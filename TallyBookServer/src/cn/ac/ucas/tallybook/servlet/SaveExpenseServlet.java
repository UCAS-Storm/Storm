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
import cn.ac.ucas.tallybook.model.Expense;
import cn.ac.ucas.tallybook.util.BaseFormat;

public class SaveExpenseServlet extends HttpServlet{
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
		
		if(tenantID != null && !"".equals(tenantID)) {
			
			/**
			 * 响应主界面请求
			 */
			if("saveExpenseInfo".equals(target)) {
				//获取数据(TenantID, Type, CategoryID, Money, ExpenseTime, Note) 
				Expense expense = new Expense();
				expense.setTenantID(tenantID);
				expense.setType(Integer.valueOf(req.getParameter("Type")));
				expense.setCategoryID(Integer.valueOf(req.getParameter("CategoryID")));
				expense.setMoney(Double.valueOf(req.getParameter("Money")));
				expense.setExpenseTime(BaseFormat.String2Date(req.getParameter("ExpenseTime")));
				expense.setNote(req.getParameter("Note"));
				
				ExpenseManager expenseManager = ExpenseManagerImpl.getInstance();
				expenseManager.addExpense(expense);		
				
			}
		}
	}
}
