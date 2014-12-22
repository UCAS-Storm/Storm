package cn.ac.ucas.tallybook.manager;

import cn.ac.ucas.tallybook.model.Income;
import cn.ac.ucas.tallybook.util.PageModel;

public interface IncomeManager {
	
	public PageModel findAllIncomes(int pageNo, int pageSize,
			String tenantID, String queryStr, String target); 
	
	public Income findIncomeByID(int incomeID);
	
	public void addIncome(Income income);
	
	public void upadteIncome(Income income);
	
	public void deleteIncome(int incomeID);
}
