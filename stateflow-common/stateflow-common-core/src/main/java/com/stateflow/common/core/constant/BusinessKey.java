package com.stateflow.common.core.constant;

/**
 * 业务数据类型（sys_data_scope.business_type / business_id）。
 */
public class BusinessKey {
    /** 与 sys_data_scope.business_type 对齐：历史仓库/项目等简写（逐步迁移为表名） */
    public static final String TICKET_DEVICE = "TICKET_DEVICE";
    public static final String WAREHOUSE = "WAREHOUSE";
    public static final String WAREHOUSE_TYPE = "WAREHOUSE_TYPE";
    public static final String PROJECT = "PROJECT";
    public static final String TENANT = "TENANT";
    public static final String VENUE = "VENUE";
    public static final String ORDER = "ORDER";
    public static final String INVOICE = "INVOICE";
    public static final String TICKET_DEVICE_TYPE = "TICKET_DEVICE_TYPE";
    public static final String TICKET_DEVICE_TYPE_DIRECTION = "TICKET_DEVICE_TYPE_DIRECTION";
    /* 盘点仓库的发起 */
    public static final String INVENTORY_TASK_WAREHOUSE = "INVENTORY_TASK_WAREHOUSE";
    /** 售后客服负责的仓库（仅维修通知场景，不授予完整仓权） */
    public static final String REPAIR_NOTIFY_WAREHOUSE = "REPAIR_NOTIFY_WAREHOUSE";

    public static final String USER = "USER";
    public static final String LEGAL_ENTITY = "LEGAL_ENTITY";
    public static final String DEVICE_CATEGORY = "DEVICE_CATEGORY";
    public static final String DEVICE = "DEVICE";
    public static final String DEVICE_REPAIR_ITEM = "DEVICE_REPAIR_ITEM";
    public static final String DEVICE_REPAIR_ITEM_BATCH = "DEVICE_REPAIR_ITEM_BATCH";
    /** 设备报修单 */
    public static final String DEVICE_REPAIR_ORDER = "DEVICE_REPAIR_ORDER";
    public static final String DEVICE_INVENTORY = "DEVICE_INVENTORY";
    public static final String PERMISSION = "PERMISSION";

    public static String batchType(String businessType) {
        if (businessType == null || businessType.isEmpty()) {
            return businessType;
        }
        if (businessType.endsWith("_BATCH")) {
            return businessType;
        }
        return businessType + "_BATCH";
    }
}
