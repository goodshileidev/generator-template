package com.partner.be.backend.common.controller;

import com.partner.be.backend.common.dao.CheckExistDao;
import com.partner.be.backend.common.po.CheckExistPO;
import com.partner.be.backend.common.po.ColumnNameValue;
import com.partner.be.common.AbstractApiController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * Check Exist Controller
 *
 * Controller for checking if data exists in the database based on specified criteria.
 * Used for validation purposes such as checking for duplicate records or verifying
 * the existence of specific data before performing operations.
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@RestController
@RequestMapping()
public class CheckExistController extends AbstractApiController {
  @Autowired private CheckExistDao checkExistDao;

  /**
   * Converts camelCase string to underscore_case format
   * Used for converting Java field names to database column names
   *
   * @param value the camelCase string to convert
   * @return the underscore_case formatted string
   */
  String camelCaseToUnderline(String value) {
    return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(value), "_");
  }

  /**
   * Check if data exists in database
   *
   * Validates whether a record exists in the specified table based on the given criteria.
   * Converts field names from camelCase to underscore_case format for database compatibility
   * and checks if any records match the validation criteria.
   *
   * @param checkExistPO the check exist parameters object containing table name,
   *                     column names, and values to check
   * @return true if no matching records found (data does not exist),
   *         false if matching records found (data exists)
   */
  @RequestMapping(path = "/common/checkExist", method = RequestMethod.POST)
  // @RequiresPermissions("${moduleName}:${pathName}:list")
  public boolean check(@RequestBody CheckExistPO checkExistPO) {
    // If validation column value is empty, consider it as valid (no duplicate check needed)
    if (StringUtils.isEmpty(checkExistPO.getValidateColumnValue())) {
      return true;
    }

    // Convert foreign key column names from camelCase to underscore_case format
    if (checkExistPO.getForeignKeys() != null) {
      for (ColumnNameValue columnNameValue : checkExistPO.getForeignKeys()) {
        columnNameValue.setColumnName(camelCaseToUnderline(columnNameValue.getColumnName()));
      }
    }

    // Convert all column names from camelCase to underscore_case format for database compatibility
    checkExistPO.setKeyColumnName(camelCaseToUnderline(checkExistPO.getKeyColumnName()));
    checkExistPO.setTableName(camelCaseToUnderline(checkExistPO.getTableName()));
    checkExistPO.setValidateColumnName(camelCaseToUnderline(checkExistPO.getValidateColumnName()));

    // Check if any records exist matching the criteria
    if (checkExistDao.getExistCount(checkExistPO) > 0) {
      return false; // Data exists
    } else {
      return true; // Data does not exist
    }
  }
}
