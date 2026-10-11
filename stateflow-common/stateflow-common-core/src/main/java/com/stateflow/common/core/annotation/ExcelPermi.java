package com.stateflow.common.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 导出字段权限：当前用户不具备指定权限时，该列不会写入导出文件。
 * <p>
 * 用法：{@code @ExcelPermi("system:device:detail:viewExtended")}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelPermi
{
    /**
     * 需要具备的权限字符串
     */
    String[] value();

    /**
     * 多个权限时的校验模式。列可见性默认 OR：具备其中任一权限即可导出该列。
     */
    Logical logical() default Logical.OR;

    enum Logical
    {
        AND, OR
    }
}
