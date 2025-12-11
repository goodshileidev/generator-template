package com.partner.be.backend.common.controller;

import com.partner.be.backend.common.po.LockPO;
import com.partner.be.backend.common.service.CommonService;
import com.partner.be.backend.common.service.CommonServiceResult;
import com.partner.be.common.AbstractApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 代码定义
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Slf4j
@Api("共通")
@RestController
@RequestMapping("/common")
public class CommonController extends AbstractApiController {

    @Autowired
    private CommonService commonService;


    /**
     * 代码定义
     */
    @RequestMapping(path = "lock", method = RequestMethod.POST)
    @ApiOperation(value = "代码定义")
    @ApiImplicitParam(name = "锁定数据", value = "", dataType = "LockPO", type = "body")
    //@RequiresPermissions("${module}::create")
    public CommonServiceResult lock(@RequestBody LockPO lockPO) {
        CommonServiceResult serviceResult = commonService.lock(lockPO);
        return serviceResult;
    }


    /**
     * 代码定义
     */
    @RequestMapping(path = "unlock", method = RequestMethod.POST)
    @ApiOperation(value = "解锁数据")
    @ApiImplicitParam(name = "lockPO", value = "", dataType = "LockPO", type = "body")
    //@RequiresPermissions("${module}::create")
    public CommonServiceResult unlock(@RequestBody LockPO lockPO) {
        CommonServiceResult serviceResult = commonService.unlock(lockPO);
        return serviceResult;
    }


}
