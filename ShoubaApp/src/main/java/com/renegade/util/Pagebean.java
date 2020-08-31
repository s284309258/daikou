package com.renegade.util;


import java.util.List;

/**
 * Created by Administrator on 2018/6/25.
 */
public class Pagebean {

    private int totalCount;
    private int totalPage;
    private int sizePage=10;
    private int pageCode;
    private List rows;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        //return totalPage;
        return totalCount%sizePage==0?totalCount/sizePage:totalCount/sizePage+1;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getSizePage() {
        return sizePage;
    }

    public void setSizePage(int sizePage) {
        this.sizePage = sizePage;
    }

    public int getPageCode() {
        return pageCode;
    }

    public void setPageCode(int pageCode) {
        this.pageCode = pageCode;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
