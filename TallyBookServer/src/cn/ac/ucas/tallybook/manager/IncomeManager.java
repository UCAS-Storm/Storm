package cn.ac.ucas.tallybook.manager;

import java.util.List;

import cn.ac.ucas.tallybook.model.Income;

public interface IncomeManager {
	
	public List findAllIncomes(int pageNo, int pageSize,
			String tenantID, String startTime, String endTime); 
	
	public Income findIncomeByID(int incomeID);
	
	public void addIncome(Income income);
	
	public void upadteIncome(Income income);
	
	public void deleteIncome(int incomeID);
	
	public String findCategoryNameByID(int categoryID);
}
