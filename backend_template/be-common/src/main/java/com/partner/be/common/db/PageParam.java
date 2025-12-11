package com.partner.be.common.db;

import com.partner.be.common.result.DataPage;
import lombok.Data;

import java.util.List;

/**
 * @author ${author} <br>
 */
@Data
public class PageParam<P, R> {

    private Integer pageNo = 1;
    private Integer pageSize = 20;// Number of records per page, default is 20
    private Integer totalRecord;// Total number of records
    private Integer totalPage;// Total number of pages
    private List<R> results;// Current page records
    private SearchParam params = null;// Other parameters encapsulated as a Map object

    public void setPageNo(Integer pageNo) {
        if (pageNo > 0) { // Minimum page number is 1
            this.pageNo = pageNo;
        }
    }

    public void setTotalRecord(Integer totalRecord) {
        this.totalRecord = totalRecord;
        // Calculate total pages
        int totalPage = (int) (totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1);
        this.setTotalPage(totalPage);
    }

    public DataPage<R> convertDataPage(List<R> list) {
        DataPage dataPage = new DataPage<R>();
        dataPage.setList(list);
        dataPage.getPageInfo().setPageNo(this.pageNo);
        dataPage.getPageInfo().setPageSize(this.pageSize);
        dataPage.getPageInfo().setTotalCount(totalRecord);
        return dataPage;
    }

    public void setParams(SearchParam params) {
        if (params instanceof PageSizing) {
            this.setPageSize(((PageSizing) params).getPageSize());
            this.setPageNo(((PageSizing) params).getPageNo());
        }
        this.params = params;
    }

}
