package cn.fudges.server.utils;

import cn.fudges.server.common.exception.BusinessException;
import cn.fudges.server.common.result.IResultCodeEnum;
import cn.fudges.server.common.result.ResultResponse;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author 王平远
 * @since 2024/8/20
 */

public class AssertUtils {

    private AssertUtils() {
    }

    public static void isNotBlank(String str, IResultCodeEnum resultCodeEnum) {
        if (StrUtil.isBlank(str)) {
            throw new BusinessException(resultCodeEnum);
        }
    }

    public static void isAllNotBlank(String[] strs, IResultCodeEnum resultCodeEnum) {
        String[] var2 = strs;
        int var3 = strs.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            String str = var2[var4];
            isNotBlank(str, resultCodeEnum);
        }
    }

    public static void isNotBlank(String str, IResultCodeEnum resultCodeEnum, String msg) {
        if (StrUtil.isBlank(str)) {
            throw new BusinessException(resultCodeEnum, msg);
        }
    }

    public static void isTrue(boolean bool, IResultCodeEnum resultCodeEnum) {
        if (!bool) {
            throw new BusinessException(resultCodeEnum);
        }
    }

    public static void isTrue(boolean bool, IResultCodeEnum resultCodeEnum, String message) {
        if (!bool) {
            throw new BusinessException(resultCodeEnum, message);
        }
    }

    public static <T> void isSuccess(ResultResponse<T> response) {
        isTrue(response.whetherSuccess(), response.getCode(), response.getMessage());
    }

    public static <T> void isSuccess(ResultResponse<T> response, IResultCodeEnum resultCodeEnum) {
        isTrue(response.whetherSuccess(), resultCodeEnum.getCode(), resultCodeEnum.getMessage());
    }

    public static void isTrue(boolean bool, String code, String message) {
        if (!bool) {
            throw new BusinessException(code, message);
        }
    }

    public static void isNotNull(Object object, IResultCodeEnum resultCodeEnum) {
        if (ObjectUtil.isNull(object)) {
            throw new BusinessException(resultCodeEnum);
        }
    }

    public static void isNotNull(Object object, IResultCodeEnum resultCodeEnum, String message) {
        if (ObjectUtil.isNull(object)) {
            throw new BusinessException(resultCodeEnum, message);
        }
    }

    public static void isNull(Object object, IResultCodeEnum resultCodeEnum) {
        if (ObjectUtil.isNotNull(object)) {
            throw new BusinessException(resultCodeEnum);
        }
    }

    public static void isNull(Object object, IResultCodeEnum resultCodeEnum, String message) {
        if (ObjectUtil.isNotNull(object)) {
            throw new BusinessException(resultCodeEnum, message);
        }
    }
}
