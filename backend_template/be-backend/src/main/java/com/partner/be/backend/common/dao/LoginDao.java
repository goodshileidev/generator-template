package com.partner.be.backend.common.dao;

import com.partner.be.common.system.vo.OperatorVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * User Login DAO
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Mapper
public interface LoginDao {
  /**
   * Retrieve by Account
   *
   * @param account username or email
   * @return operator information
   */
  OperatorVO getByUserName(String account);
}
