package com.stateflow.common.core.utils.poi;

/**
 * Excel 导出权限校验。由 security 模块注册实现，避免 core 依赖 security。
 */
public interface ExcelPermiChecker
{
    boolean isAdmin();

    boolean hasPermi(String permission);
}
