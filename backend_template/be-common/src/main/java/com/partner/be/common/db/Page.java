package com.partner.be.common.db;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ページング結果を格納する
 */
@Data
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -5395997221963176643L;

    private List<T> list; // list result of this page
    private Integer totalPage; // total page
    private Integer totalRow; // total row
    private Integer pageSize; // page size
    private Integer pageNo; //current page num

    /**
     * Constructor.
     *
     * @param list      the list of paginate result
     * @param pageSize  the page size
     * @param totalPage the total page of paginate
     * @param totalRow  the total row of paginate
     */
    public Page(List<T> list, Integer pageSize, Integer totalPage, Integer totalRow) {
        this.list = list;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalRow = totalRow;
    }

}
