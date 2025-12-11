package com.partner.be.common.result;

import lombok.Data;

/**
 * <br>クラス名: PageInfo
 * <br>説　明: ページング用情報を格納する
 * <p/>
* <br>作成時間: 2018年1月12日
 * <br>版 本: 1.0
 * <br>
 * <br>履　歴: (版本) 作者 时间 注释
 */
@Data
public class PageInfo {
    Integer pageNo;
    Integer pageSize;
    Integer totalCount;
}
