package com.partner.be.common;

import com.partner.be.common.db.PageSizing;
import lombok.Getter;
import lombok.Setter;

/** Base class for storing search parameters */
@Setter
@Getter
public class BaseSearchPO implements PageSizing {
  protected Integer pageNo = 1; // Page number, default is first page
  protected Integer pageSize = 20; // Number of records displayed per page, default is 20
  protected int isDeleted;
  protected String dataDate;
  protected String bizType;
  protected String currentUserId;
  protected String language;
  protected String dateCondition;
  /**
   * Preferred export file type. Supported values: "csv" (default) or "excel" (xls template output).
   */
  protected String exportFileType = "csv";
}
