package com.partner.be.postgres.system.dao;

import com.partner.be.common.db.PageParam;
import com.partner.be.common.system.domain.Operator;
import com.partner.be.common.system.po.OperatorPO;
import com.partner.be.common.system.po.OperatorSearchPO;
import com.partner.be.common.system.vo.OperatorVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * Operator (User) DAO
 *
 * @author System
 */
@Mapper
public interface OperatorDao {
  /** Delete operator */
  long delete(OperatorPO operatorPO);

  /** Insert operator data */
  long insert(Operator operator);

  /** Update operator data */
  long update(OperatorPO operatorPO);

  /** Enable operator status */
  long enable(OperatorPO operatorPO);

  /** Disable operator status */
  long disable(OperatorPO operatorPO);

  /** Query single operator data */
  OperatorVO getByPO(Operator operatorPO);

  /** Query single operator data by ID */
  OperatorVO getById(String operatorId);

  /** Returns data count */
  Long count(OperatorSearchPO operatorSearchPO);

  /** Returns paginated data */
  List<OperatorVO> page(PageParam<OperatorSearchPO, OperatorVO> pageParam);

  /** Returns data list */
  List<OperatorVO> list(OperatorSearchPO operatorSearchPO);

  /** Returns data list for export */
  List<OperatorVO> export(OperatorSearchPO operatorSearchPO);
}
