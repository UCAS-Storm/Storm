package cn.ac.ucas.tallybook.model;

import java.util.Date;

public class Income {

	private int incomeID;
	
	private String tenantID;
	
	private int categoryID;
	
	private String categoryName;
	
	private Date expenseTime;
	
	private double money;
	
	private String note;

	public int getIncomeID() {
		return incomeID;
	}

	public void setIncomeID(int incomeID) {
		this.incomeID = incomeID;
	}

	public String getTenantID() {
		return tenantID;
	}

	public void setTenantID(String tenantID) {
		this.tenantID = tenantID;
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
