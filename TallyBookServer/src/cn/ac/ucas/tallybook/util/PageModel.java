package cn.ac.ucas.tallybook.util;

import java.util.List;

public class PageModel {

	private List data;
	
	//总记录数
	private int total;
	
	//当前页数
	private int pageNo;
	
	//总页数
	private int pageSize;
	
	public int getTopPage() {
		return 1;
	}
	
	public int getPreviousPage() {
		if(pageNo - 1 <= 1) {
			return getTopPage();
		}
		return pageNo - 1;
	}
	
	public int getNextPage() {
		if(pageNo + 1 >= total) {
			return total;
		}
		return pageNo + 1;
	}
	
	public  int getBottomPage() {
		return total;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
