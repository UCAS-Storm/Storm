package cn.ac.ucas.tallybook.manager.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.ac.ucas.tallybook.manager.ExpenseManager;
import cn.ac.ucas.tallybook.model.Category;
import cn.ac.ucas.tallybook.model.Expense;
import cn.ac.ucas.tallybook.util.BaseFormat;
import cn.ac.ucas.tallybook.util.DB;

public class ExpenseManagerImpl implements ExpenseManager {
	
	/**
	 * 单例模式
	 */
	private static ExpenseManager instance = new ExpenseManagerImpl();
	
	private ExpenseManagerImpl() {}
	
	public static ExpenseManager getInstance() {
		return instance;
	}
	
	/**
	 * 查找某段时间内的支出记录
	 */
	@Override
	public List<Expense> findAllExpenses(int pageNo, int pageSize, String tenantID,
			String startTime, String endTime) {
		
		StringBuffer sb = new StringBuffer();
		
//		sql语句：查询当前用户在一段时间内的支出记录
//		SELECT ExpenseID, type, CategoryID, Money, ExpenseTime, Note FROM Expense 
//		WHERE TenantID = 'liubei' AND ExpenseTime >= '2014-12-21' AND ExpenseTime <= '2014-12-22'
//		ORDER BY ExpenseTime ASC LIMIT 0, 2;
		
		sb.append("SELECT ExpenseID, type, CategoryID, Money, ExpenseTime, Note FROM Expense WHERE TenantID = ?")
			.append(" AND ExpenseTime >= ?")
			.append(" AND ExpenseTime <= ?")
			.append(" ORDER BY ExpenseTime ASC LIMIT ?,?");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Expense> expenses = new ArrayList();
		
		conn = DB.getConnection();
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, tenantID);
			pstmt.setDate(2, new Date(BaseFormat.String2Date(startTime).getTime()));
			pstmt.setDate(3, new Date(BaseFormat.String2Date(endTime).getTime()));
			pstmt.setInt(4, (pageNo - 1) * pageSize);
			pstmt.setInt(5, pageSize);
			
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Expense expense = new Expense();
				expense.setTenantID(tenantID);
				expense.setExpenseID(rs.getInt("ExpenseID"));
				expense.setType(rs.getInt("Type"));
				expense.setCategoryID(rs.getInt("CategoryID"));
				expense.setCategoryName(findCategoryNameByID(rs.getInt("CategoryID")));
				expense.setMoney(rs.getDouble("Money"));
				expense.setExpenseTime(rs.getDate("ExpenseTime"));
				expense.setNote(rs.getString("Note") == null ? "" : rs.getString("Note"));
				expenses.add(expense);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return expenses;
	}
	
	/**
	 * 查询当前日期一天内的记录
	 */
	public List<Expense> findExpenses(String tenantID) {
		String str = "SELECT ExpenseID, Type, CategoryID, Money, ExpenseTime, Note FROM Expense WHERE TenantID = ? AND ExpenseTime = CURRENT_DATE() ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Expense> expenses = new ArrayList();
		
		conn = DB.getConnection();
		try {
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, tenantID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Expense expense = new Expense();
				expense.setTenantID(tenantID);
				expense.setExpenseID(rs.getInt("ExpenseID"));
				expense.setType(rs.getInt("Type"));
				expense.setCategoryID(rs.getInt("CategoryID"));
				expense.setCategoryName(findCategoryNameByID(rs.getInt("CategoryID")));
				expense.setMoney(rs.getDouble("Money"));
				expense.setExpenseTime(rs.getDate("ExpenseTime"));
				expense.setNote(rs.getString("Note") == null ? "" : rs.getString("Note"));
				expenses.add(expense);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return expenses;
	}

	/**
	 * 根据IncomeID查找特定的一条支出记录
	 */
	@Override
	public Expense findExpenseByID(int expenseID) {

		String str = "SELECT TenantID, Type, CategoryID, Money, ExpenseTime, Note FROM Expense WHERE ExpenseID = ? ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Expense expense = new Expense();
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setInt(1, expenseID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				expense.setExpenseID(expenseID);
				expense.setTenantID(rs.getString("TenantID"));
				expense.setType(rs.getInt("Type"));
				expense.setCategoryID(rs.getInt("CategoryID"));
				expense.setCategoryName(findCategoryNameByID(rs.getInt("CategoryID")));
				expense.setMoney(rs.getDouble("Money"));
				expense.setExpenseTime(rs.getDate("ExpenseTime"));
				expense.setNote(rs.getString("Note") == null ? "" : rs.getString("Note"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return expense;
	}

	/**
	 * 记一笔
	 */
	@Override
	public void addExpense(Expense expense) {
		
		String str = "insert into Expense"
				+ "(TenantID, Type, CategoryID, Money, ExpenseTime, Note) "
				+ "values(?, ?, ?, ?, ?,?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, expense.getTenantID());
			pstmt.setInt(2, expense.getType());
			pstmt.setInt(3, expense.getCategoryID());
			pstmt.setDouble(4, expense.getMoney());
			pstmt.setDate(5, new Date(expense.getExpenseTime().getTime()));
			pstmt.setString(6, expense.getNote());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	}

	/**
	 * 修改/编辑记录
	 */
	@Override
	public void upadteExpense(Expense expense) {
		
		String str = "update Expense set TenantID=?, Type=?, CategoryID=?, Money=?, ExpenseTime=?, Note=? where ExpenseID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, expense.getTenantID());
			pstmt.setInt(2, expense.getType());
			pstmt.setInt(3, expense.getCategoryID());
			pstmt.setDouble(4, expense.getMoney());
			pstmt.setDate(5, new Date(expense.getExpenseTime().getTime()));
			pstmt.setString(6, expense.getNote());
			pstmt.setInt(7, expense.getExpenseID());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	}

	@Override
	public void deleteExpense(int expenseID) {
		
		String str = "delete from Expense where ExpenseID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setInt(1, expenseID);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	}
	
	/**
	 * 根据花费类别ID查询花费类别描述
	 * @param categoryID
	 * @return
	 */
	public String findCategoryNameByID(int categoryID) {
		
		String str = "SELECT CategoryName FROM Category WHERE CategoryID =? ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String categoryName = "";
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setInt(1, categoryID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				categoryName = rs.getString(1);
			};
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return categoryName;
	}
	
	/**
	 * 统计到目前为止的所有花费总和(按收入和支出统计)
	 * type: 1=收入, 2=支出
	 */
	@Override
	public double sumAll(String tenantID, int type) {
		
		String str = "SELECT SUM(Money) FROM Expense WHERE TenantID = ? AND Type = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double sumAll = 0;
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, tenantID);
			pstmt.setInt(2, type);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				sumAll = rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return sumAll;
	}
	
	/**
	 * 统计某段时间内的所有花费总和(按收入和支出统计)
	 * type: 1=收入, 2=支出
	 */
	@Override
	public double sumAllPeriod(String tenantID, int type, String startTime, String endTime) {
		String str = "SELECT SUM(Money) FROM Expense WHERE TenantID = ? AND Type = ? AND ExpenseTime >= ? AND ExpenseTime <= ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		double sumAllPeriod = 0;
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, tenantID);
			pstmt.setInt(2, type);
			pstmt.setDate(3, new Date(BaseFormat.String2Date(startTime).getTime()));
			pstmt.setDate(4, new Date(BaseFormat.String2Date(endTime).getTime()));
			rs = pstmt.executeQuery();
			if(rs.next()) {
				sumAllPeriod = rs.getDouble(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return sumAllPeriod;
	}

	/**
	 * 按花费类别统计某段时间内的花费总和(按收入和支出统计)
	 * type: 1=收入, 2=支出
	 */
	@Override
	public List<Category> sumAllPeriodByCategs(String tenantID, int type, String startTime, String endTime) {
		String str = "SELECT CategoryID, SUM(Money) Count FROM Expense WHERE TenantID = ? "
				+ " AND Type = ?"
				+ " AND ExpenseTime >= ? AND ExpenseTime <= ? "
				+ " GROUP BY CategoryID ORDER BY CategoryID ASC";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Category> sumByCategs = new ArrayList();
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, tenantID);
			pstmt.setInt(2, type);
			pstmt.setDate(3, new Date(BaseFormat.String2Date(startTime).getTime()));
			pstmt.setDate(4, new Date(BaseFormat.String2Date(endTime).getTime()));
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Category category = new Category();
				category.setCategoryID(rs.getInt("CategoryID"));
				category.setCategoryName(findCategoryNameByID(rs.getInt("CategoryID")));
				category.setCount(rs.getDouble("Count"));
				sumByCategs.add(category);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return sumByCategs;
	}
	
	public static void main(String[] args) {
		ExpenseManager expenseManager = new ExpenseManagerImpl();
//		List<Category> sumByCategs = expenseManager.sumAllPeriodByCategs("liubei", 1, "2014-12-15", "2014-12-26");
//		for (Iterator<Category> iterator = sumByCategs.iterator(); iterator.hasNext();) {
//			Category category = (Category) iterator.next();
//			System.out.println(category.getCategoryID() + ", " + category.getCategoryName() + ", " + category.getCount());
//		}
//		
//		List<Expense> expenses = expenseManager.findExpenses("liubei");
//		for (Iterator<Expense> iterator = expenses.iterator(); iterator.hasNext();) {
//			Expense expense = (Expense) iterator.next();
//			System.out.println(expense.getExpenseID() + ", " + expense.getType() + ", " + expense.getCategoryID() + ", " + expense.getCategoryName()
//					+ ", " + expense.getTenantID() + ", " + expense.getMoney()
//					+ ", " + expense.getExpenseTime() + ", " + expense.getNote());
//		}
//		
//		Expense expense = expenseManager.findExpenseByID(1);
//		System.out.println(expense.getExpenseID() + ", " + expense.getType() + ", " + expense.getCategoryID() + ", " + expense.getCategoryName()
//				+ ", " + expense.getTenantID() + ", " + expense.getMoney()
//				+ ", " + expense.getExpenseTime() + ", " + expense.getNote());
//		Expense expense = new Expense();
//		expense.setTenantID("liubei");
//		expense.setType(1);
//		expense.setCategoryID(6);
//		expense.setExpenseTime(BaseFormat.String2Date("2014-12-26"));
//		expense.setMoney(1000);
//		expense.setNote("哈哈哈哈");
//		expenseManager.addExpense(expense);
//		
//		expenseManager.deleteExpense(1);
		
		System.out.println(expenseManager.sumAll("liubei", 1));
		System.out.println(expenseManager.sumAllPeriod("liubei", 1, "2014-12-22", "2014-12-24"));
		List sumByCategs = expenseManager.sumAllPeriodByCategs("liubei", 1, "2014-12-19", "2014-12-24");
		for (Iterator iterator = sumByCategs.iterator(); iterator.hasNext();) {
			Category category = (Category) iterator.next();
			System.out.println(category.getCategoryID() + ", " + category.getCategoryName() + ", " + category.getCount());
		}
	}
}

