package cn.ac.ucas.tallybook.manager.impl;

import cn.ac.ucas.tallybook.manager.IncomeManager;
import cn.ac.ucas.tallybook.model.Income;
import cn.ac.ucas.tallybook.util.PageModel;

public class IncomeManagerImpl implements IncomeManager {
	
	private static IncomeManagerImpl instance = new IncomeManagerImpl();
	
	private IncomeManagerImpl() {}
	
	public static IncomeManagerImpl getInstance() {
		return instance;
	}

	@Override
	public PageModel findAllIncomes(int pageNo, int pageSize, String tenantID,
			String startTime, String endTime) {
		
		StringBuffer sb = new StringBuffer();
		
//		sql���
//		SELECT i.CategoryID, i.Money, i.ExpenseTime, i.Note FROM Income i 
//		WHERE TenantID = 'liubei' AND ExpenseTime >= '2014-12-21' AND ExpenseTime <= '2014-12-22'
//		ORDER BY IncomeID ASC LIMIT 0, 2;
		
//		sb.append(str)
		
		
		return null;
	}

	@Override
	public Income findIncomeByID(int incomeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addIncome(Income income) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upadteIncome(Income income) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteIncome(int incomeID) {
		// TODO Auto-generated method stub
		
	}

}
