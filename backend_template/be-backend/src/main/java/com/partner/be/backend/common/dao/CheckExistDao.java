package com.partner.be.backend.common.dao;

import com.partner.be.backend.common.po.CheckExistPO;
import com.partner.be.backend.common.vo.CheckExistVO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * Check if data already exists
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Mapper
public interface CheckExistDao {

  /**
   * Returns the count of existing data
   *
   * @param checkExistParam check exist parameters
   * @return count of existing records
   */
  Integer getExistCount(CheckExistPO checkExistParam);

  /**
   * Query data that matches the conditions
   *
   * @param checkExistParam check exist parameters
   * @return list of matching records
   */
  List<CheckExistVO> list(CheckExistPO checkExistParam);
}
