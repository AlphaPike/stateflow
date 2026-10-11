package com.stateflow.common.core.enums;

import com.stateflow.common.core.constant.ExtValConstants;
import com.stateflow.common.core.utils.StringUtils;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public enum WarehouseTypeRule {

    // 一级仓（顶级仓库）
    FACTORY_WAREHOUSE("1-FACTORY", "工厂仓", 1, "FT", null, false),
    PLATFORM_WAREHOUSE("1-PLATFORM", "平台总仓", 1, "PF", null, false),
    OPERATION_WAREHOUSE("1-OPERATION", "运营仓", 1, "OT", null, false),
    DEMO_WAREHOUSE("1-DEMO", "样机仓", 1, "DM", null, false),
    SALES_WAREHOUSE("1-SALES", "销售仓", 1, "SL", null, false),
    REPAIR_WAREHOUSE("1-REPAIR", "维修仓", 1, "RP", null, false),

    // 二级仓
    SUBJECT_TOTAL_WAREHOUSE("2-SUBJECTTOTAL", "主体总仓", 2, "PF", PLATFORM_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_OWNER_ID_EXT), // 创建主体后自动创建
    SUPPLY_CHAIN_TOTAL_WAREHOUSE("2-SUPPLYCHAINTOTAL", "供应链总仓", 2, "FT", FACTORY_WAREHOUSE, true),
    TENANT_TOTAL_WAREHOUSE("2-TENANTTOTAL", "运营租户总仓", 2, "OT", OPERATION_WAREHOUSE, false, ExtValConstants.WH_TENANT_ID_EXT),
    OWNER_TOTAL_WAREHOUSE("2-OWNERTOTAL", "归属方总仓", 2, "OW", PLATFORM_WAREHOUSE, false, ExtValConstants.WH_OWNER_ID_EXT),
    SUBJECT_DEMO_TOTAL_WAREHOUSE("2-SUBJECTDEMO", "主体样机总仓", 2, "DM", DEMO_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT),
    SALES_CHANNEL_TOTAL_WAREHOUSE("2-CHANNELTOTAL", "销售渠道商总仓", 2, "SL", SALES_WAREHOUSE, true),
    REPAIR_SERVICE_TOTAL_WAREHOUSE("2-REPAIRSERVICETOTAL", "维修服务商总仓", 2, "RP", REPAIR_WAREHOUSE, true),

    // 三级仓
    SUBJECT_NEW_WAREHOUSE("3-SUBJECTNEW", "主体新货仓", 3, "PF", SUBJECT_TOTAL_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_OWNER_ID_EXT),
    SUBJECT_SECONDHAND_WAREHOUSE("3-SUBJECTSECONDHAND", "主体二手仓", 3, "PF", SUBJECT_TOTAL_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_OWNER_ID_EXT),
    SUBJECT_OPERATION_RETURN_WAREHOUSE("3-SUBJECTOPERATIONRETURN", "主体运营退货仓", 3, "PF", SUBJECT_TOTAL_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_OWNER_ID_EXT),
    SUBJECT_DEMO_RETURN_WAREHOUSE("3-SUBJECTDEMORETURN", "主体样机退货仓", 3, "PF", SUBJECT_TOTAL_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_OWNER_ID_EXT),
    SUBJECT_SALES_RETURN_WAREHOUSE("3-SUBJECTSALERETURN", "主体销售退货仓", 3, "PF", SUBJECT_TOTAL_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_OWNER_ID_EXT),
    TENANT_TRANSFER_WAREHOUSE("3-TENANTTRANSFER", "运营租户中转仓", 3, "OT", TENANT_TOTAL_WAREHOUSE, false, ExtValConstants.WH_TENANT_ID_EXT),
    OWNER_INVENTORY_WAREHOUSE("3-OWNERINVENTORY", "归属方存货仓", 3, "OW", OWNER_TOTAL_WAREHOUSE, false, ExtValConstants.WH_OWNER_ID_EXT),
    TENANT_PROJECT_WAREHOUSE("3-TENANTPROJECT", "运营项目仓", 3, "OT", TENANT_TOTAL_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_PROJECT_ID_EXT),
    SUPPLY_CHAIN_BRANCH_WAREHOUSE("3-SUPPLYCHAINBRANCH", "供应链分仓", 3, "FT", SUPPLY_CHAIN_TOTAL_WAREHOUSE, true),
    PERSONAL_DEMO_WAREHOUSE("3-PERSONALDEMO", "个人样机仓", 3, "DM", SUBJECT_DEMO_TOTAL_WAREHOUSE, false, ExtValConstants.WH_ENTITY_ID_EXT, ExtValConstants.WH_USER_ID_EXT),
    CHANNEL_PICKUP_WAREHOUSE("3-CHANNELPICKUP", "渠道商提货仓", 3, "SL", SALES_CHANNEL_TOTAL_WAREHOUSE, true),
    END_CUSTOMER_WAREHOUSE("3-ENDCUSTOMER", "终端客户仓", 3, "SL", SALES_CHANNEL_TOTAL_WAREHOUSE, true), // 当前版本不存在
    REPAIR_SERVICE_POINT_WAREHOUSE("3-REPAIRPOINT", "维修服务点分仓", 3, "RP", REPAIR_SERVICE_TOTAL_WAREHOUSE, true);


    private final String typeKey;
    private final String typeName;
    private final int warehouseLevel;
    private final String idPrefix;
    private final WarehouseTypeRule parentType;
    private final boolean canEdit;

    /**
     * 扩展字段含义
     */
    private final List<String> extVarchar;

    WarehouseTypeRule(String typeKey, String typeName, int warehouseLevel, String idPrefix, WarehouseTypeRule parentType, boolean canEdit, String... extVarchar) {
        this.typeKey = typeKey;
        this.typeName = typeName;
        this.warehouseLevel = warehouseLevel;
        this.idPrefix = idPrefix;
        this.parentType = parentType;
        this.canEdit = canEdit;
        this.extVarchar = Arrays.asList(extVarchar);
    }

    public static WarehouseTypeRule fromInput(String input) {
        if (input == null) {
            return null;
        }
        for (WarehouseTypeRule state : values()) {
            if (state.typeKey.equals(input)) {
                return state;
            }
        }
        return null;
    }

    public static boolean equals(WarehouseTypeRule type, String input) {
        if (StringUtils.isBlank(input) || type == null) {
            return false;
        }
        WarehouseTypeRule warehouseTypeRule = fromInput(input);
        if (warehouseTypeRule == null) {
            return false;
        }
        return warehouseTypeRule.equals(type);
    }

    public static boolean equalsParent (WarehouseTypeRule type, String input) {
        if (StringUtils.isBlank(input) || type == null) {
            return false;
        }
        WarehouseTypeRule warehouseTypeRule = fromInput(input);
        if (warehouseTypeRule == null || warehouseTypeRule.getParentType() == null) {
            return false;
        }
        WarehouseTypeRule current = warehouseTypeRule;
        Set<WarehouseTypeRule> visited = new HashSet<>(); // 防止循环引用
        while (current != null) {
            if (current.equals(type)) {
                return true;
            }
            if (visited.contains(current)) {
                break; // 已访问过，防止死循环
            }
            visited.add(current);
            current = current.getParentType();
        }
        return false;
    }

    /**
     * 判断当前仓库类型下是否允许新增子类型仓库。
     * 当且仅当存在至少一种直接子类型且该子类型的 {@link #canEdit} 为 true 时返回 true。
     */
    public boolean canAdd() {
        for (WarehouseTypeRule rule : values()) {
            if (rule.getParentType() == this && rule.isCanEdit()) {
                return true;
            }
        }
        return false;
    }
}
