package cn.ac.ucas.tallybook.manager.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import cn.ac.ucas.tallybook.manager.BuyServiceManager;
import cn.ac.ucas.tallybook.model.BuyService;
import cn.ac.ucas.tallybook.util.DB;

public class BuyServiceManagerImpl implements BuyServiceManager {
	
	/**
	 * 单例模式
	 */
	private static BuyServiceManager instance = new BuyServiceManagerImpl();
	
	private BuyServiceManagerImpl() {}
	
	public static BuyServiceManager getInstance() {
		return instance;
	}

	/**
	 * 查找当前租户购买了哪些服务并且没有到期
	 */
	@Override
	public List findAllBuyService(String tenantID) {
		
		String str = "SELECT ServiceID, Money, StartTime, EndTime FROM BuyService WHERE TenantID =? AND EndTime >=?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List buyServices = new ArrayList();
		
		conn = DB.getConnection();
		try {
			pstmt = conn.prepareStatement(str);
			pstmt.setString(1, tenantID);
			java.util.Date date = new java.util.Date();
			pstmt.setDate(2, new Date(date.getTime()));
			rs = pstmt.executeQuery();
			while (rs.next()) {
				BuyService buyService = new BuyService();
				buyService.setTenantID(tenantID);
				buyService.setBuyServiceID(rs.getInt("ServiceID"));
				buyService.setMoney(rs.getDouble("Money"));
				buyService.setStartTime(rs.getDate("StartTime"));
				buyService.setEndTime(rs.getDate("EndTime"));
				buyServices.add(buyService);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DB.close(rs);
			DB.close(pstmt);
			DB.close(conn);
		}
		return buyServices;
	}
	
//	public static void main(String[] args) {
//		BuyServiceManager buyServiceManager = BuyServiceManagerImpl.getInstance();
//		List buuServices = buyServiceManager.findAllBuyService("liubei");
//		for (Iterator iterator = buuServices.iterator(); iterator.hasNext();) {
//			BuyService buyService = (BuyService) iterator.next();
//			System.out.println(buyService.getTenantID() + ", " + buyService.getBuyServiceID());
//		}
//	}

}
