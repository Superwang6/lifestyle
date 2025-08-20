package cn.fudges.server.utils;

import cn.fudges.server.common.result.ResultCodeEnum;
import cn.fudges.server.business.jot.entity.JotRecord;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;

/**
 * @author 王平远
 * @since 2025/7/21
 */
@Slf4j
public class MybatisPlusUtils {

    public static <T> UpdateWrapper<T> buildUpdateWrapper(T entity, String whereColumn) {
        UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();
        if(ObjectUtil.isNull(entity)) {
            return updateWrapper;
        }
        boolean isRight = false;
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            try {
                Object o = field.get(entity);
                if(ObjectUtil.isNotNull(o)) {
                    if(whereColumn.equals(field.getName())) {
                        updateWrapper.eq(StrUtil.toUnderlineCase(field.getName()), o);
                        isRight = true;
                    } else  {
                        updateWrapper.set(StrUtil.toUnderlineCase(field.getName()), o);
                    }
                }
            } catch (IllegalAccessException e) {
                log.error("error build updateWrapper", e);
            }
        }
        AssertUtils.isTrue(isRight, ResultCodeEnum.BUSINESS_EXCEPTION, "whereColumn is not exist");
        return updateWrapper;
    }

    public static void main(String[] args) {
        JotRecord record = new JotRecord();
        record.setId(1L);
        record.setTitle("title");
        record.setCreateTime(LocalDateTime.now());
        record.setStatus(1);
        UpdateWrapper<JotRecord> id = buildUpdateWrapper(record, "id");
        System.out.println(id);
    }
}
