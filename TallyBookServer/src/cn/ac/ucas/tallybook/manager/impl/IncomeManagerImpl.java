package cn.ac.ucas.tallybook.manager.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.ac.ucas.tallybook.manager.IncomeManager;
import cn.ac.ucas.tallybook.model.Income;
import cn.ac.ucas.tallybook.util.BaseFormat;
import cn.ac.ucas.tallybook.util.DB;

public class IncomeManagerImpl implements IncomeManager {
	
	/**
	 * 查找某段时间内的收入记录
	 */
	@Override
	public List findAllIncomes(int pageNo, int pageSize, String tenantID,
			String startTime, String endTime) {
		
		StringBuffer sb = new StringBuffer();
		
//		sql语句：查询当前用户在一段时间内的收入记录
//		SELECT IncomeID, CategoryID, Money, ExpenseTime, Note FROM Income 
//		WHERE TenantID = 'liubei' AND ExpenseTime >= '2014-12-21' AND ExpenseTime <= '2014-12-22'
//		ORDER BY IncomeID ASC LIMIT 0, 2;
		
		sb.append("SELECT IncomeID, TenantID, CategoryID, Money, ExpenseTime, Note FROM Income WHERE TenantID = ?")
			.append(" AND ExpenseTime >= ?")
			.append(" AND ExpenseTime <= ?")
			.append(" ORDER BY IncomeID ASC LIMIT ?,?");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List incomes = new ArrayList();
		
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
				Income income = new Income();
				income.setTenantID(rs.getNString("TenantID"));
				income.setIncomeID(rs.getInt("IncomeID"));
				income.setCategoryID(rs.getInt("CategoryID"));
				income.setCategoryName(findCategoryNameByID(rs.getInt("CategoryID")));
				income.setMoney(rs.getDouble("Money"));
				income.setExpenseTime(rs.getDate("ExpenseTime"));
				income.setNote(rs.getString("Note"));
				incomes.add(income);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return incomes;
	}

	/**
	 * 根据IncomeID查找特定的一条收入记录
	 */
	@Override
	public Income findIncomeByID(int incomeID) {

		String str = "SELECT TenantID, CategoryID, Money, ExpenseTime, Note FROM Income WHERE IncomeID = ? ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Income income = new Income();
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setInt(1, incomeID);
			rs = pstmt.executeQuery();
			rs.next();
			income.setIncomeID(incomeID);
			income.setTenantID(rs.getString("TenantID"));
			income.setCategoryID(rs.getInt("CategoryID"));
			income.setCategoryName(findCategoryNameByID(rs.getInt("CategoryID")));
			income.setMoney(rs.getDouble("Money"));
			income.setExpenseTime(rs.getDate("ExpenseTime"));
			income.setNote(rs.getString("Note"));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return income;
	}

	/**
	 * 记一笔
	 */
	@Override
	public void addIncome(Income income) {
		
		String str = "insert into Income"
				+ "(TenantID, CategoryID, Money, ExpenseTime, Note) "
				+ "values(?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, income.getTenantID());
			pstmt.setInt(2, income.getCategoryID());
			pstmt.setDouble(3, income.getMoney());
			pstmt.setDate(4, new Date(income.getExpenseTime().getTime()));
			pstmt.setString(5, income.getNote());
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
	public void upadteIncome(Income income) {
		
		String str = "update Income set TenantID=?, CategoryID=?, Money=?, ExpenseTime=?, Note=? where IncomeID = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, income.getTenantID());
			pstmt.setInt(2, income.getCategoryID());
			pstmt.setDouble(3, income.getMoney());
			pstmt.setDate(4, new Date(income.getExpenseTime().getTime()));
			pstmt.setString(5, income.getNote());
			pstmt.setInt(6, income.getIncomeID());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	}

	@Override
	public void deleteIncome(int incomeID) {
		
		String str = "delete from Income where Income = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
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
			rs.next();
			categoryName = rs.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return categoryName;
	}
	
	public static void main(String[] args) {
		
		IncomeManager incomeManager = new IncomeManagerImpl();
//		List incomes = incomeManager.findAllIncomes(1, 2, "liubei", "2014-12-21", "2014-12-22");
//		for (Iterator iterator = incomes.iterator(); iterator.hasNext();) {
//			Income income = (Income) iterator.next();
//			System.out.println(income.getIncomeID() + ", " + income.getCategoryID() + ", " + income.getTenantID() + ", " + income.getCategoryName() + ", " + income.getExpenseTime());
//		}
		
//		Income income = incomeManager.findIncomeByID(1);
//		System.out.println(income.getIncomeID() + ", " + income.getCategoryID() + ", " + income.getTenantID() + ", " + income.getCategoryName() + ", " + income.getExpenseTime());
		
//		Income income = new Income();
//		income.setTenantID("liubei");
//		income.setCategoryID(5);
//		income.setMoney(100);
//		income.setExpenseTime(BaseFormat.String2Date("2014-12-19"));
//		income.setNote("又花钱了");
//		incomeManager.addIncome(income);
		
		Income income = new Income();
		income.setTenantID("liubei");
		income.setCategoryID(5);
		income.setMoney(200);
		income.setExpenseTime(BaseFormat.String2Date("2014-12-18"));
		income.setNote("又花钱了");
		incomeManager.upadteIncome(income);
		
		
	}
}

