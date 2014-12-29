package cn.ac.ucas.tallybook.model;

import java.util.Date;

public class Expense {

	private int expenseID;
	
	private String tenantID;
	
	/**
	 * type表示花费类别是收入还是支出
	 * type: 1=收入，2=支出
	 */
	private int type;
	
	private int categoryID;
	
	private String categoryName;
	
	private Date expenseTime;
	
	private String expenseTimeStr;
	
	private double money;
	
	private String note;

	public int getExpenseID() {
		return expenseID;
	}

	public void setExpenseID(int expenseID) {
		this.expenseID = expenseID;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Date getExpenseTime() {
		return expenseTime;
	}

	public void setExpenseTime(Date expenseTime) {
		this.expenseTime = expenseTime;
	}

	public String getExpenseTimeStr() {
		return expenseTimeStr;
	}

	public void setExpenseTimeStr(String expenseTimeStr) {
		this.expenseTimeStr = expenseTimeStr;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
