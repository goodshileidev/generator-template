# 文件迁移汇总清单

## 本次会话新增文件

### MyBatis Mapper XML (3个)
1. `be-backend/src/main/resources/mybatis/xml/LoginDao.xml`
2. `be-backend/src/main/resources/mybatis/xml/CheckExistDao.xml`
3. `be-dao-postgres/src/main/resources/mybatis/xml/OperatorDao.xml`

### DAO 层 (1个)
4. `be-dao-postgres/src/main/java/com/partner/be/postgres/system/dao/OperatorDao.java`

### Service 层 (3个)
5. `be-backend/src/main/java/com/partner/be/backend/system/service/OperatorService.java`
6. `be-backend/src/main/java/com/partner/be/backend/system/service/OperatorServiceResult.java`
7. `be-backend/src/main/java/com/partner/be/backend/system/service/impl/OperatorServiceImpl.java`

### 支持类 - Search & Pagination (7个)
8. `be-common-biz/src/main/java/com/partner/be/common/BaseSearchPO.java`
9. `be-common-biz/src/main/java/com/partner/be/common/HasCompanyIdCondition.java`
10. `be-common-biz/src/main/java/com/partner/be/common/system/po/OperatorSearchPO.java`
11. `be-common/src/main/java/com/partner/be/common/db/SearchParam.java`
12. `be-common/src/main/java/com/partner/be/common/db/PageParam.java`
13. `be-common/src/main/java/com/partner/be/common/db/PageSizing.java`
14. `be-common/src/main/java/com/partner/be/common/result/DataPage.java`

### CodeList 枚举 (3个)
15. `be-common-biz/src/main/java/com/partner/be/common/codelist/EnableDisableStatus.java`
16. `be-common-biz/src/main/java/com/partner/be/common/codelist/YesNo.java`
17. `be-common-biz/src/main/java/com/partner/be/common/codelist/UserRole.java`

### 配置类 (4个)
18. `be-backend/src/main/java/com/partner/be/config/WebSecurityPathConfig.java`
19. `be-backend/src/main/java/com/partner/be/config/WebFilterConfig.java`
20. `be-backend/src/main/java/com/partner/be/config/WebSecurityInterceptorConfig.java`
21. `be-backend/src/main/java/com/partner/be/config/CorsConfig.java`

### 数据库 DDL (1个)
22. `docs/ddl/t_operator.sql`

### MyBatis 配置 (1个)
23. `be-backend/src/main/resources/mybatis/spring-mybatis.xml`

### 配置文件更新 (1个)
24. `be-backend/src/main/resources/application.yml` (已更新，添加JWT和安全配置)

### 文档 (3个)
25. `MIGRATION_COMPLETE.md` - 迁移完成报告
26. `FILE_SUMMARY.md` - 本文件
27. `copy_operator_support.sh` - Operator支持类复制脚本
28. `copy_config_files.sh` - 配置文件复制脚本

---

## 前期会话已完成文件（参考）

### 模块 POM 文件
- `be-common-biz/pom.xml`
- `be-dao-postgres/pom.xml`
- `be-notification/pom.xml`
- `be-parent/pom.xml` (已更新)

### JWT 认证 (4个)
- `be-backend/src/main/java/com/partner/be/config/JwtProperties.java`
- `be-backend/src/main/java/com/partner/be/backend/common/security/JwtTokenProvider.java`
- `be-backend/src/main/java/com/partner/be/backend/common/security/JwtAuthenticationFilter.java`
- `be-backend/src/main/java/com/partner/be/backend/common/security/MutableHttpServletRequest.java`

### 安全认证 (2个)
- `be-backend/src/main/java/com/partner/be/backend/common/security/PasswordHashService.java`
- `be-backend/src/main/java/com/partner/be/backend/common/security/RateLimitService.java`

### 登录功能 (5个)
- `be-backend/src/main/java/com/partner/be/backend/common/controller/LoginController.java`
- `be-backend/src/main/java/com/partner/be/backend/common/service/LoginService.java`
- `be-backend/src/main/java/com/partner/be/backend/common/service/impl/LoginServiceImpl.java`
- `be-backend/src/main/java/com/partner/be/backend/common/service/LoginServiceResult.java`
- `be-backend/src/main/java/com/partner/be/backend/common/po/LoginPO.java`
- `be-backend/src/main/java/com/partner/be/backend/common/dao/LoginDao.java`

### 数据校验 (3个)
- `be-backend/src/main/java/com/partner/be/backend/common/controller/CheckExistController.java`
- `be-backend/src/main/java/com/partner/be/backend/common/dao/CheckExistDao.java`
- `be-backend/src/main/java/com/partner/be/backend/common/po/CheckExistPO.java`
- `be-backend/src/main/java/com/partner/be/backend/common/vo/CheckExistVO.java`
- `be-backend/src/main/java/com/partner/be/backend/common/po/ColumnNameValue.java`

### 基础类/接口 (7个)
- `be-common/src/main/java/com/partner/be/common/db/HasCreator.java`
- `be-common/src/main/java/com/partner/be/common/db/HasUpdater.java`
- `be-common/src/main/java/com/partner/be/common/db/HasCompanyId.java`
- `be-common/src/main/java/com/partner/be/common/db/ResultObject.java`
- `be-common-biz/src/main/java/com/partner/be/common/BaseDomain.java`
- `be-common/src/main/java/com/partner/be/common/result/ServiceResult.java`
- `be-common/src/main/java/com/partner/be/common/result/ServiceResultType.java`
- `be-common/src/main/java/com/partner/be/common/ApiLoginUser.java`

### Operator 实体 (3个)
- `be-common-biz/src/main/java/com/partner/be/common/system/domain/Operator.java`
- `be-common-biz/src/main/java/com/partner/be/common/system/vo/OperatorVO.java`
- `be-common-biz/src/main/java/com/partner/be/common/system/po/OperatorPO.java`

### 工具类 (4个)
- `be-common/src/main/java/com/partner/be/common/util/DateUtil.java`
- `be-common/src/main/java/com/partner/be/common/util/JacksonUtils.java`
- `be-common/src/main/java/com/partner/be/common/util/ReflectUtils.java`
- `be-common/src/main/java/com/partner/be/common/util/MessageUtils.java`

---

## 总计

**本次会话新增：** 24 个文件  
**前期会话完成：** 约 40 个文件  
**总计：** 约 64 个核心文件

**包名转换：** `com.napir.em` → `com.partner.be`
