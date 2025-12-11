package com.partner.be.backend.common.vo;

import com.partner.be.common.db.ResultObject;
import lombok.Data;

/**
 * CheckExist Value Object
 * Used for returning duplicate data check results
 */
@Data
public class CheckExistVO implements ResultObject {
  private static final long serialVersionUID = 1L;

  /** Record ID */
  protected String id;

  /** Table name */
  protected String tableName;

  /** Column name */
  protected String columnName;

  /** Column value */
  protected String columnValue;

  /** Existence flag */
  protected Boolean exists;
}
