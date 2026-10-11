package com.stateflow.common.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSON 序列化字段权限：当前用户不具备指定权限时，该字段不会写入响应。
 * <p>
 * 用法：{@code @JsonPermi("system:user:viewPhone")}
 * 或多个权限：{@code @JsonPermi(value = {"a", "b"}, logical = Logical.OR)}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface JsonPermi
{
    /**
     * 需要具备的权限字符串
     */
    String[] value();

    /**
     * 多个权限时的校验模式。字段可见性默认 OR：具备其中任一权限即可看到该字段。
     */
    Logical logical() default Logical.OR;
}
