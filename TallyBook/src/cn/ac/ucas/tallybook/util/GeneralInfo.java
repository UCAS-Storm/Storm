package cn.ac.ucas.tallybook.util;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

public class GeneralInfo {
	
	//收入
	final static int INCOME_MODE = 0;
	//支出
	final static int PAYOUT_MODE = 1;
	//修改(编辑)
	final static int EDIT_MODE = 2;

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
