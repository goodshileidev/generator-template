package com.partner.be.backend.system.service;

import com.partner.be.common.system.po.OperatorPO;
import com.partner.be.common.system.po.OperatorSearchPO;
import com.partner.be.common.system.vo.OperatorVO;
import com.partner.be.common.db.PageParam;
import java.util.List;

/**
 * Operator Service
 *
 * @author System
 */
public interface OperatorService {

  /**
   * Query Single Data Record
   *
   * @param operatorId
   * @return
   */
  OperatorVO getById(String operatorId);

  /**
   * Return Data Count
   *
   * @param operatorSearchPO
   * @return
   */
  Long count(OperatorSearchPO operatorSearchPO);

  /**
   * Return Pagination
   *
   * @param pageParam
   * @return
   */
  List<OperatorVO> search(PageParam<OperatorSearchPO, OperatorVO> pageParam);

  /**
   * Return Single Data Record
   *
   * @param operatorPO
   * @return
   */
  OperatorVO getByPO(OperatorPO operatorPO);

  /**
   * Return Data List
   *
   * @param operatorSearchPO
   * @return
   */
  List<OperatorVO> list(OperatorSearchPO operatorSearchPO);

  /**
   * Create
   *
   * @param operatorPO
   * @return
   */
  OperatorServiceResult create(OperatorPO operatorPO);

  /**
   * Save
   *
   * @param operatorPO
   * @return
   */
  OperatorServiceResult save(OperatorPO operatorPO);

  /**
   * Update
   *
   * @param operatorPO
   * @return
   */
  OperatorServiceResult update(OperatorPO operatorPO);

  /**
   * Enable
   *
   * @param operatorPO
   * @return
   */
  OperatorServiceResult enable(OperatorPO operatorPO);

  /**
   * Disable
   *
   * @param operatorPO
   * @return
   */
  OperatorServiceResult disable(OperatorPO operatorPO);

  /**
   * Delete Data
   *
   * @param operatorPO
   * @return
   */
  OperatorServiceResult delete(OperatorPO operatorPO);

  /**
   * Export Data
   *
   * @param operatorSearchPO
   * @return
   */
  List<OperatorVO> export(OperatorSearchPO operatorSearchPO);
}
