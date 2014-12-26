package cn.ac.ucas.tallybook.manager.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.ac.ucas.tallybook.manager.TenantManager;
import cn.ac.ucas.tallybook.model.Tenant;
import cn.ac.ucas.tallybook.util.DB;

public class TenantManagerImpl implements TenantManager {
	
	/**
	 * 单例模式
	 */
	private static TenantManager instance = new TenantManagerImpl();
	
	private TenantManagerImpl() {}
	
	public static TenantManager getInstance() {
		return instance;
	}
	
	@Override
	public boolean login(String tenantID, String password) {
		
		boolean flag = true;
		Tenant tenant = findTenantByID(tenantID);
		if(tenant == null || !password.equals(tenant.getPassword())) {
			flag = false;
		} 
		return flag;
	}

	@Override
	public void addTenant(Tenant tenant) {
		String str = "insert into Tenant"
				+ "(TenantID, TenantName, Password, Tel, Email) "
				+ "values(?, ?, ?, ?, ?)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, tenant.getTenantID());
			pstmt.setString(2, tenant.getTenantName());
			pstmt.setString(3, tenant.getPassword());
			pstmt.setString(4, tenant.getTel());
			pstmt.setString(5, tenant.getEmail());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(pstmt);
			DB.close(conn);
		}
	}

	@Override
	public Tenant findTenantByID(String tenantID) {
		String str = "SELECT TenantID, Password FROM Tenant WHERE TenantID =? ";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Tenant tenant = null;
		try {
			tenant = new Tenant();
			conn = DB.getConnection();
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, tenantID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				tenant.setTenantID(rs.getString("TenantID"));
				tenant.setPassword(rs.getString("Password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return tenant;
	}
	
//	public static void main(String[] args) {
//		/**
//		 * 单例模式测试
//		 */
//		TenantManager tenantManager = TenantManagerImpl.getInstance();
//		boolean test = tenantManager.login("liubei", "liubeiw");
//		System.out.println(test);
//		
//		Tenant tenant = new Tenant();
//		tenant.setTenantID("zhouyu");
//		tenant.setPassword("zhouyu");
//		tenant.setTenantName("周瑜");
//		tenant.setTel("13121111012");
//		tenant.setEmail("zhouyuaaa@sina.com");
//		tenantManager.addTenant(tenant);
//	}
}
