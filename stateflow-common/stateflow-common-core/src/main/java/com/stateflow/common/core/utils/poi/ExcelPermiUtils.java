package com.stateflow.common.core.utils.poi;

import com.stateflow.common.core.annotation.ExcelPermi;

/**
 * Excel 导出字段权限判断。
 */
public class ExcelPermiUtils
{
    private static volatile ExcelPermiChecker checker;

    public static void register(ExcelPermiChecker excelPermiChecker)
    {
        checker = excelPermiChecker;
    }

    /**
     * 无注解或具备权限时返回 true。未注册校验器时不过滤（无 security 的模块仍可导出）。
     */
    public static boolean allowed(ExcelPermi excelPermi)
    {
        if (excelPermi == null)
        {
            return true;
        }
        ExcelPermiChecker current = checker;
        if (current == null)
        {
            return true;
        }
        try
        {
            if (current.isAdmin())
            {
                return true;
            }
        }
        catch (Exception ignored)
        {
            // 无登录用户时按权限集合判断
        }
        String[] permissions = excelPermi.value();
        if (permissions == null || permissions.length == 0)
        {
            return false;
        }
        if (excelPermi.logical() == ExcelPermi.Logical.AND)
        {
            for (String permission : permissions)
            {
                if (!current.hasPermi(permission))
                {
                    return false;
                }
            }
            return true;
        }
        for (String permission : permissions)
        {
            if (current.hasPermi(permission))
            {
                return true;
            }
        }
        return false;
    }
}
