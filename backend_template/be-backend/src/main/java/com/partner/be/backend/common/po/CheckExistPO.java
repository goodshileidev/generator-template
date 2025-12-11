package com.partner.be.backend.common.po;

import lombok.Data;

@Data
public class CheckExistPO {
  /** Foreign key */
  ColumnNameValue[] foreignKeys;

  String foreignKeysStr;

  String tableName;

  String validateColumnName;

  String keyColumnName;

  String keyValue;

  String validateColumnValue;
}
