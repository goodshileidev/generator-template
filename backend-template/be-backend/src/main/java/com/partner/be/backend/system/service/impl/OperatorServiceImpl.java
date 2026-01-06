package com.partner.be.backend.system.service.impl;

import com.partner.be.backend.common.security.PasswordHashService;
import com.partner.be.backend.system.service.OperatorService;
import com.partner.be.backend.system.service.OperatorServiceResult;
import com.partner.be.common.codelist.EnableDisableStatus;
import com.partner.be.common.codelist.UserRole;
import com.partner.be.common.codelist.YesNo;
import com.partner.be.common.db.PageParam;
import com.partner.be.common.result.ServiceResultType;
import com.partner.be.common.system.po.OperatorPO;
import com.partner.be.common.system.po.OperatorSearchPO;
import com.partner.be.common.system.vo.OperatorVO;
import com.partner.be.postgres.system.dao.OperatorDao;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Operator Service Implementation
 *
 * <p>This service provides business logic for operator (user) management operations including CRUD
 * operations, search, export, and operator status management with password encryption.
 *
 * @author System
 * @version 1.0
 */
@Service("operatorService")
@Slf4j
public class OperatorServiceImpl implements OperatorService {

  @Autowired private OperatorDao operatorDao;
  @Autowired private PasswordHashService passwordHashService;

  /**
   * Retrieves a single operator record by operator ID
   *
   * @param operatorId the unique identifier of the operator
   * @return OperatorVO containing operator details, or null if not found
   */
  public OperatorVO getById(String operatorId) {
    return operatorDao.getById(operatorId);
  }

  /**
   * Counts the number of operators matching the search criteria
   *
   * @param operatorSearchPO search parameters for filtering operators
   * @return the total count of matching operators
   */
  public Long count(OperatorSearchPO operatorSearchPO) {
    return operatorDao.count(operatorSearchPO);
  }

  /**
   * Searches for operators with pagination support
   *
   * @param pageParam pagination parameters including search criteria
   * @return list of operator records matching the search criteria
   */
  public List<OperatorVO> search(PageParam<OperatorSearchPO, OperatorVO> pageParam) {
    // Get total count for pagination
    Long count = operatorDao.count((OperatorSearchPO) pageParam.getParams());
    pageParam.setTotalRecord(Math.toIntExact(count));
    // Return paginated results
    return operatorDao.page(pageParam);
  }

  /**
   * Retrieves a single operator record by operator PO criteria
   *
   * @param operatorPO operator criteria object for lookup
   * @return OperatorVO containing operator details, or null if not found
   */
  public OperatorVO getByPO(OperatorPO operatorPO) {
    return operatorDao.getByPO(operatorPO);
  }

  /**
   * Retrieves a list of operators matching search criteria
   *
   * @param operatorSearchPO search parameters for filtering operators
   * @return list of operator records matching the criteria
   */
  public List<OperatorVO> list(OperatorSearchPO operatorSearchPO) {
    return operatorDao.list(operatorSearchPO);
  }

  /**
   * Creates a new operator record with password encryption
   *
   * <p><b>Password Format:</b> The newPassword in operatorPO should be SHA256-hashed by the
   * frontend. This method will apply BCrypt hashing to the SHA256 value for secure storage.
   *
   * @param operatorPO operator data to be created (with SHA256-hashed newPassword)
   * @return OperatorServiceResult indicating success or failure
   */
  public OperatorServiceResult create(OperatorPO operatorPO) {
    // Apply BCrypt to the SHA256-hashed password
    if (StringUtils.isNotEmpty(operatorPO.getNewPassword())) {
      operatorPO.setPassword(passwordHashService.hash(operatorPO.getNewPassword()));
    }
    long result = operatorDao.insert(operatorPO);
    if (result > 0) {
      return new OperatorServiceResult(ServiceResultType.SUCCESS, operatorPO);
    } else {
      return new OperatorServiceResult(ServiceResultType.FAILED);
    }
  }

  /**
   * Saves a new operator record (alias for create)
   *
   * @param operatorPO operator data to be saved
   * @return OperatorServiceResult indicating success or failure
   */
  public OperatorServiceResult save(OperatorPO operatorPO) {
    long result = operatorDao.insert(operatorPO);
    if (result > 0) {
      return new OperatorServiceResult(ServiceResultType.SUCCESS, operatorPO);
    } else {
      return new OperatorServiceResult(ServiceResultType.FAILED);
    }
  }

  /**
   * Updates an existing operator record with email verification and password encryption
   *
   * <p><b>Password Format:</b> The newPassword in operatorPO should be SHA256-hashed by the
   * frontend. This method will apply BCrypt hashing to the SHA256 value for secure storage.
   *
   * @param operatorPO operator data to be updated (with SHA256-hashed newPassword if changing
   *     password)
   * @return OperatorServiceResult indicating success or failure
   */
  public OperatorServiceResult update(OperatorPO operatorPO) {
    // Reset email verification status if email is updated
    if (StringUtils.isNotEmpty(operatorPO.getEmail())
        && StringUtils.isBlank(operatorPO.getAssociatedEmailVerificationStatus())) {
      operatorPO.setAssociatedEmailVerificationStatus(YesNo.no.getCode());
    }
    // Apply BCrypt to the SHA256-hashed password
    if (StringUtils.isNotEmpty(operatorPO.getNewPassword())) {
      operatorPO.setPassword(passwordHashService.hash(operatorPO.getNewPassword()));
    }
    long result = operatorDao.update(operatorPO);
    if (result > 0) {
      return new OperatorServiceResult(ServiceResultType.SUCCESS, operatorPO);
    } else {
      return new OperatorServiceResult(ServiceResultType.FAILED);
    }
  }

  /**
   * Enables an operator (sets to active status)
   *
   * @param operatorPO operator to be enabled
   * @return OperatorServiceResult indicating success or failure
   */
  public OperatorServiceResult enable(OperatorPO operatorPO) {
    long result = operatorDao.enable(operatorPO);
    if (result > 0) {
      return new OperatorServiceResult(ServiceResultType.SUCCESS, operatorPO);
    } else {
      return new OperatorServiceResult(ServiceResultType.FAILED);
    }
  }

  /**
   * Disables an operator (sets to inactive status)
   *
   * @param operatorPO operator to be disabled
   * @return OperatorServiceResult indicating success or failure
   */
  public OperatorServiceResult disable(OperatorPO operatorPO) {
    long result = operatorDao.disable(operatorPO);
    if (result > 0) {
      return new OperatorServiceResult(ServiceResultType.SUCCESS, operatorPO);
    } else {
      return new OperatorServiceResult(ServiceResultType.FAILED);
    }
  }

  /**
   * Deletes an operator record
   *
   * @param operatorPO operator to be deleted
   * @return OperatorServiceResult indicating success or failure
   */
  public OperatorServiceResult delete(OperatorPO operatorPO) {
    long result = operatorDao.delete(operatorPO);
    if (result > 0) {
      return new OperatorServiceResult(ServiceResultType.SUCCESS, operatorPO);
    } else {
      return new OperatorServiceResult(ServiceResultType.FAILED);
    }
  }

  /**
   * Sets localized titles for code list fields in operator VO
   *
   * @param operatorVO operator value object to populate with localized titles
   * @param language language code for localization
   */
  private void setCodeListTitle(OperatorVO operatorVO, String language) {
    // Set localized titles for various operator configuration fields
    operatorVO.setUsageStatusTitle(
        EnableDisableStatus.getByCode(operatorVO.getUsageStatus()).getTitle(language));
    operatorVO.setAssociatedPhoneNumberVerificationStatusTitle(
        YesNo.getByCode(operatorVO.getAssociatedPhoneNumberVerificationStatus())
            .getTitle(language));
    operatorVO.setAssociatedEmailVerificationStatusTitle(
        YesNo.getByCode(operatorVO.getAssociatedEmailVerificationStatus()).getTitle(language));
    operatorVO.setUserRoleTitle(UserRole.getByCode(operatorVO.getUserRole()).getTitle(language));
    operatorVO.setLoginWelcomeMessageSettingStatusTitle(
        YesNo.getByCode(operatorVO.getLoginWelcomeMessageSettingStatus()).getTitle(language));
  }

  /**
   * Exports operator data with localized field titles
   *
   * @param searchPO search parameters for filtering operators to export
   * @return list of operator records with localized titles for export
   */
  public List<OperatorVO> export(OperatorSearchPO searchPO) {
    List<OperatorVO> list = operatorDao.export(searchPO);
    // Process each operator to set localized titles
    for (OperatorVO operatorVO : list) {
      setCodeListTitle(operatorVO, searchPO.getLanguage());
    }
    return list;
  }
}
