package com.partner.be.common.filter;


import com.google.common.collect.Maps;
import com.partner.be.common.ApiConstants;
import com.partner.be.common.ApiLoginUser;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Class: UserThreadHolder
 * Description: Thread-local storage for user context
 *
 * <p>
 * Created: January 13, 2018
 * Version: 1.0
 *
 * History: (Version) Author Time Comments
 */
public class UserThreadHolder {

    public static final String LOGIN_USER_ID = "loginUserId";

    public static final String LOGIN_USER_COMPANY_ID = "loginUserCompanyId";

    public static final String LOGIN_USER_POWER_STATION_IDS = "loginUserPowerStationIds";

    public static final String LOGIN_USER_ROLE = "loginUserRole";

    private static final ThreadLocal<Map<String, Object>> sessionValues = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, String>> loginUserId = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, String>> loginCompanyId = new ThreadLocal<>();

    private static final ThreadLocal<Map<String, List<String>>> powerStationIds = new ThreadLocal<>();

    private static final ConcurrentMap<String, CaptchaValue> CAPTCHA_STORE = new ConcurrentHashMap<>();

    /**
     * Sets key-value pair
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        Long id = Thread.currentThread().getId();
        if (sessionValues.get() == null) {
            sessionValues.set(Maps.<String, Object>newHashMap());
        }
        sessionValues.get().put(key, value);
    }

    public static void storeCaptchaValue(String captchaId, String value) {
        if (StringUtils.isBlank(captchaId) || StringUtils.isBlank(value)) {
            return;
        }
        CAPTCHA_STORE.put(captchaId, new CaptchaValue(value, Instant.now()));
    }

    public static boolean validateCaptchaValue(String captchaId, String providedValue, long ttlSeconds) {
        if (StringUtils.isBlank(captchaId) || StringUtils.isBlank(providedValue)) {
            return false;
        }
        CaptchaValue stored = CAPTCHA_STORE.remove(captchaId);
        if (stored == null) {
            return false;
        }
        if (ttlSeconds > 0 && Instant.now().isAfter(stored.createdAt.plusSeconds(ttlSeconds))) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(stored.value, providedValue);
    }

    /**
     *
     * バリューを返す
     *
     * @param key
     * @return tagPrice
     */
    public static Object get(String key) {
        Long id = Thread.currentThread().getId();
        if (sessionValues.get() != null) {
            return sessionValues.get().get(key);
        }
        return null;
    }

    /**
     * キーを削除
     *
     * @param key
     */
    public static void remove(String key) {
        if (sessionValues.get() != null) {
            sessionValues.get().remove(key);
        }

    }


    /**
     * Description: Gets operator ID
     * History: (Version) Author Time Comments
     *
     * @param
     * @return
     */
    public static ApiLoginUser getLoginUser() {
        ApiLoginUser loginUser = (ApiLoginUser) get(ApiConstants.Fields.USER_VALUE_OBJECT_KEY);
        if (loginUser == null) {
            loginUser = new ApiLoginUser();
            set(ApiConstants.Fields.USER_VALUE_OBJECT_KEY, loginUser);
            loginUser.setName("Test User");
            loginUser.setUserId("test_user");
            loginUser.setMenuRight(new HashSet<>());
        }
        return loginUser;
    }

    private static final class CaptchaValue {
        private final String value;
        private final Instant createdAt;

        private CaptchaValue(String value, Instant createdAt) {
            this.value = value;
            this.createdAt = createdAt;
        }
    }
}
