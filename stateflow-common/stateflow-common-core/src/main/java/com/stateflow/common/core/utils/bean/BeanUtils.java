package com.stateflow.common.core.utils.bean;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Bean 工具类
 * 
 * @author stateflow
 */
public class BeanUtils extends org.springframework.beans.BeanUtils
{
    /** Bean方法名中属性名开始的下标 */
    private static final int BEAN_METHOD_PROP_INDEX = 3;

    /** * 匹配getter方法的正则表达式 */
    private static final Pattern GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");

    /** * 匹配setter方法的正则表达式 */
    private static final Pattern SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");

    /**
     * Bean属性复制工具方法。
     * 
     * @param dest 目标对象
     * @param src 源对象
     */
    public static void copyBeanProp(Object dest, Object src)
    {
        try
        {
            copyProperties(src, dest);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取对象的setter方法。
     * 
     * @param obj 对象
     * @return 对象的setter方法列表
     */
    public static List<Method> getSetterMethods(Object obj)
    {
        // setter方法列表
        List<Method> setterMethods = new ArrayList<Method>();

        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();

        // 查找setter方法

        for (Method method : methods)
        {
            Matcher m = SET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 1))
            {
                setterMethods.add(method);
            }
        }
        // 返回setter方法列表
        return setterMethods;
    }

    /**
     * 获取对象的getter方法。
     * 
     * @param obj 对象
     * @return 对象的getter方法列表
     */

    public static List<Method> getGetterMethods(Object obj)
    {
        // getter方法列表
        List<Method> getterMethods = new ArrayList<Method>();
        // 获取所有方法
        Method[] methods = obj.getClass().getMethods();
        // 查找getter方法
        for (Method method : methods)
        {
            Matcher m = GET_PATTERN.matcher(method.getName());
            if (m.matches() && (method.getParameterTypes().length == 0))
            {
                getterMethods.add(method);
            }
        }
        // 返回getter方法列表
        return getterMethods;
    }

    /**
     * 检查Bean方法名中的属性名是否相等。<br>
     * 如getName()和setName()属性名一样，getName()和setAge()属性名不一样。
     * 
     * @param m1 方法名1
     * @param m2 方法名2
     * @return 属性名一样返回true，否则返回false
     */

    public static boolean isMethodPropEquals(String m1, String m2)
    {
        return m1.substring(BEAN_METHOD_PROP_INDEX).equals(m2.substring(BEAN_METHOD_PROP_INDEX));
    }

    public static <T, E> Map<Object, List<E>> getFieldList (
            BaseMapper<E> service,
            List<T> entities,
            Function<T, Object> idGetter,
            SFunction<E, Object> fieldIdGetter
    ) {
        List<Object> ids = entities.stream()
                .filter(Objects::nonNull)
                .map(idGetter)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return new HashMap<>();
        }
        // 批量查询文件记录
        LambdaQueryWrapper<E> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(fieldIdGetter, ids);
        List<E> list = service.selectList(queryWrapper);
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }

        // 根据子表主键分组
        return list.stream()
                .collect(Collectors.groupingBy(fieldIdGetter));
    }

    /**
     * mybatis plus 自动做分类，设置外键属性
     * @param service mybatis plus服务类
     * @param entities 实体类
     * @param idGetter 获取目标对象外键属性
     * @param fieldSetter 设置目标对象属性
     * @param fieldIdGetter 源对象ID获取
     * @param <T> 目标对象
     * @param <E> 源对象
     */
    public static <T, E> void setField (
            BaseMapper<E> service,
            List<T> entities,
            Function<T, Object> idGetter,
            BiConsumer<T, E> fieldSetter,
            SFunction<E, Object> fieldIdGetter
    ) {
        Map<Object, List<E>> fieldList = getFieldList(service, entities, idGetter, fieldIdGetter);
        for (T entity : entities) {
            Object id = idGetter.apply(entity);
            if (id == null) {
                continue;
            }
            List<E> o = fieldList.get(id);
            if (o == null || o.isEmpty()) {
                continue;
            }
            fieldSetter.accept(entity, o.get(0));
        }
    }

    /**
     * mybatis plus 自动做分类，设置外键属性
     * @param service mybatis plus服务类
     * @param entities 实体类
     * @param idGetter 获取目标对象外键属性
     * @param fieldSetter 设置目标对象属性
     * @param fieldIdGetter 源对象ID获取
     * @param <T> 目标对象
     * @param <E> 源对象
     */
    public static <T, E> void setFieldList (
            BaseMapper<E> service,
            List<T> entities,
            Function<T, Object> idGetter,
            BiConsumer<T, List<E>> fieldSetter,
            SFunction<E, Object> fieldIdGetter
    ) {
        Map<Object, List<E>> fieldList = getFieldList(service, entities, idGetter, fieldIdGetter);
        for (T entity : entities) {
            Object id = idGetter.apply(entity);
            if (id == null) {
                continue;
            }
            List<E> o = fieldList.get(id);
            if (o == null || o.isEmpty()) {
                o = Collections.emptyList();
            }
            fieldSetter.accept(entity, o);
        }
    }
}
