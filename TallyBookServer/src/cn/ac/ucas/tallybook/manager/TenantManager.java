package cn.ac.ucas.tallybook.manager;

import cn.ac.ucas.tallybook.model.Tenant;

public interface TenantManager {

	public boolean login(String tenantID, String password);
	
	public void addTenant(Tenant tenant); 
	
	public Tenant findTenantByID(String tenantID);
}
