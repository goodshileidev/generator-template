package com.partner.be.common.exception;

/**
 * <br>クラス名: InvalidDataException
 * <br>説　明: データが正しくない
* <br>作成時間： 2018年10月19日 上午11:40:15
 * <br>版 本： 1.0
 * <br>
 * <br>履　歴: (版本) 作者 时间 注释
 */
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }
}
