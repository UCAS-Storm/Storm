package cn.ac.ucas.tallybook.util;


public class GeneralInfo {
	
	//收入
	final static int INCOME_MODE = 1;
	//支出
	final static int PAYOUT_MODE = 2;
	//修改(编辑)
	final static int EDIT_MODE = 3;

	public static int getIncomeMode() {
		return INCOME_MODE;
	}
	public static int getPayoutMode() {
		return PAYOUT_MODE;
	}
	public static int getEditMode() {
		return EDIT_MODE;
	}
	
	
}
