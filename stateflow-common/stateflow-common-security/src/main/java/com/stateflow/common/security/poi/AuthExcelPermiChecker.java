package com.stateflow.common.security.poi;

import com.stateflow.common.core.utils.poi.ExcelPermiChecker;
import com.stateflow.common.security.auth.AuthUtil;
import com.stateflow.common.security.utils.SecurityUtils;

/**
 * 用当前登录用户权限判断 Excel 导出列是否可见。
 */
public class AuthExcelPermiChecker implements ExcelPermiChecker
{
    @Override
    public boolean isAdmin()
    {
        return SecurityUtils.isAdmin();
    }

    @Override
    public boolean hasPermi(String permission)
    {
        return AuthUtil.hasPermi(permission);
    }
}
