package cn.ac.ucas.tallybook.manager;

import java.util.List;

import cn.ac.ucas.tallybook.model.Category;
import cn.ac.ucas.tallybook.model.Expense;

public interface ExpenseManager {
	
	public List<Expense> findAllExpenses(String tenantID, String startTime, String endTime); 
	
	public List<Expense> findExpenses(int pageNo, int pageSize, String tenantID);
	
	public Expense findExpenseByID(int expenseID);
	
	public void addExpense(Expense expense);
	
	public void upadteExpense(Expense expense);
	
	public void deleteExpense(int expenseID);
	
	public String findCategoryNameByID(int categoryID);
	
	public double sumAll(String tenantID, int type);
	
	public double sumAllPeriod(String tenantID, int type, String starTime, String endTime);
	
	public List<Category> sumAllPeriodByCategs(String tenantID, int type, String starTime, String endTime);
}
