package com.stateflow.common.core.constant;

/**
 * 角色权限常量
 *
 */
public class RoleKeyConstant {
    /**
     * 超级管理员角色权限
     */
    public static final String SUPER_ADMIN = "admin";

    /**
     * 现场运营
     */
    public static final String FIELD_OPS = "fieldOps";

    /**
     * 兼职（按项目绑定）
     */
    public static final String PART_TIME = "partTime";

    /**
     * 第一方供应链负责人角色。
     */
    public static final String INTERNAL_SUPPLY_CHAIN_MANAGER = "internalSupplyChainManager";

    /**
     * 第三方供应链负责人角色。
     */
    public static final String EXTERNAL_SUPPLY_CHAIN_MANAGER = "externalSupplyChainManager";

    /**
     * 第三设备方组织成员。
     */
    public static final String EXTERNAL_DEVICE_ORG_ADMIN = "externalDeviceOrgAdmin";

    /**
     * 平台运营角色。
     */
    public static final String PLATFORM_OPS = "platformOps";

    /**
     * 平台设备管理员。
     */
    public static final String PLATFORM_DEVICE_ADMIN = "platformDeviceAdmin";

    /**
     * 平台财务管理员（role_key 沿用库内拼写 platformFinaceAdmin）。
     */
    public static final String PLATFORM_FINANCE_ADMIN = "platformFinaceAdmin";

    /**
     * 平台运营管理员角色。
     */
    public static final String PLATFORM_OPS_ADMIN = "platformOpsAdmin";

    /**
     * 平台销售管理员角色。
     */
    public static final String PLATFORM_SALES_ADMIN = "platformSalesAdmin";

    /**
     * 平台管理员角色。
     */
    public static final String PLATFORM_ADMIN = "platformAdmin";

    /**
     * 平台售后客服。绑定后按三级仓写入维修通知数据权限（REPAIR_NOTIFY_WAREHOUSE）。
     */
    public static final String PLATFORM_AFTER_SALES_CS = "platformAfterSalesCs";

    /**
     * 第一方维修负责人角色。
     */
    public static final String INTERNAL_MAINTENANCE_MANAGER = "internalMaintenanceManager";

    /**
     * 维修工程师。
     */
    public static final String MAINTENANCE_ENGINEER = "maintenanceEngineer";

    /**
     * 样机持有人（个人样机仓数据权限，写入 sys_data_scope）
     */
    public static final String PROTOTYPE_HOLDER = "prototypeHolder";

    /**
     * 运营组织成员
     */
    public static final String OPS_ORG_ADMIN = "opsOrgAdmin";

    /**
     * 租户管理员。数据权限绑定 TENANT；仓库按所管租户类型动态计算；菜单在查询时并入类型关联角色。
     */
    public static final String TENANT_ADMIN = "tenantAdmin";
}
