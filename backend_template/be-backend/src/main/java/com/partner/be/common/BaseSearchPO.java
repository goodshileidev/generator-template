package com.partner.be.common;

import com.partner.be.common.db.PageSizing;
import lombok.Getter;
import lombok.Setter;

/**
 * 検索パラメータを格納するベースクラス
 */
@Setter
@Getter
public class BaseSearchPO implements PageSizing {
    protected Integer pageNo = 1;// 页码，默认是第一页
    protected Integer pageSize = 20;// 每页显示的记录数，默认是20
    protected int isDeleted;
    protected String dataDate;
    protected String bizType;

}
