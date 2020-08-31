package com.renegade.util.pub;

import java.io.Serializable;

/**
 * 分页model类
 * @author Administrator
 *
 */
public class PageDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer currentPageNo;     // 当前页数 第几页

	private Integer pageSize = 10;    // 每页显示多少条

	public Integer startIndex;      // 开始条数 

	private Long totalPageNum;      // 总共页数
	
	private Long totalRecord;      // 总共条数

	private Boolean lastPage;      // 是否最后一页

	public void calculateTotalPageNum(long dbTotalRecord) {
		totalRecord = dbTotalRecord;
		totalPageNum = (dbTotalRecord + pageSize - 1) / pageSize;
		lastPage = (currentPageNo >= totalPageNum.intValue() ? true : false);
	}

	public Long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public Boolean getLastPage() {
		return lastPage;
	}

	public void setLastPage(Boolean lastPage) {
		this.lastPage = lastPage;
	}

	public Long getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(Long totalPageNum) {
		this.totalPageNum = totalPageNum;
	}

	public Integer getStartIndex() {
		return (currentPageNo - 1) * pageSize;
	}

	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getCurrentPageNo() {
		return currentPageNo;
	}

	public void setCurrentPageNo(Integer currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
