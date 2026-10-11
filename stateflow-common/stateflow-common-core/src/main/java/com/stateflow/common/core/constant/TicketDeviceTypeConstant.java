package com.stateflow.common.core.constant;

import java.util.Set;

/**
 * 设备调拨工单类型ID
 */
public class TicketDeviceTypeConstant {
    /** 跨归属方调拨工单 */
    public static final String TDT_CROSS_OWNER_TRANSFER = "TDT_CROSS_OWNER_TRANSFER";
    /** 设备跨主体借用工单 */
    public static final String TDT_CROSS_ENTITY_BORROW = "TDT_CROSS_ENTITY_BORROW";
    /** 设备跨主体归还工单 */
    public static final String TDT_CROSS_ENTITY_RETURN = "TDT_CROSS_ENTITY_RETURN";
    /** 样机设备退货工单 */
    public static final String TDT_DEMO_RETURN = "TDT_DEMO_RETURN";
    /** 设备导入工单 */
    public static final String TDT_IMPORT = "TDT_IMPORT";
    /** 新设备入库工单 */
    public static final String TDT_NEW_INBOUND = "TDT_NEW_INBOUND";
    /** 运营设备退货工单 */
    public static final String TDT_OPERATION_RETURN = "TDT_OPERATION_RETURN";
    /** 设备出货工单 */
    public static final String TDT_OUTBOUND = "TDT_OUTBOUND";
    /** 设备归属变更工单 */
    public static final String TDT_OWNER_CHANGE = "TDT_OWNER_CHANGE";
    /** 维修完成工单 */
    public static final String TDT_REPAIR_COMPLETE = "TDT_REPAIR_COMPLETE";
    /** 销售设备退货工单 */
    public static final String TDT_SALES_RETURN = "TDT_SALES_RETURN";
    /** 报废工单 */
    public static final String TDT_SCRAP = "TDT_SCRAP";
    /** 二手设备入库工单 */
    public static final String TDT_SECONDHAND_INBOUND = "TDT_SECONDHAND_INBOUND";
    /** 送修工单 */
    public static final String TDT_SEND_REPAIR = "TDT_SEND_REPAIR";
    /** 设备调拨工单 */
    public static final String TDT_TRANSFER = "TDT_TRANSFER";
    /** 设备报失工单 */
    public static final String TDT_DEVICE_LOSS = "TDT_DEVICE_LOSS";
    /** 设备寻回工单 */
    public static final String TDT_DEVICE_UNLOSS = "TDT_DEVICE_UNLOSS";
    /**
     * 报修工单（在库故障设备送修）。
     *
     * @deprecated 报修已拆为独立报修单模块（sys_device_repair_order）。逻辑暂不改动，待新报修单全部完成后再移除此工单类型及相关代码。
     */
    @Deprecated
    public static final String TDT_DEVICE_REPAIR = "TDT_DEVICE_REPAIR";
    /** 盘点异常调拨（异常 -> 在库） */
    public static final String TDT_INVENTORY_ABNORMAL_TRANSFER = "TDT_INVENTORY_ABNORMAL_TRANSFER";

    /** 允许已完成撤销的工单类型 */
    public static final Set<String> ROLLBACK_ALLOWED_TYPE_IDS = Set.of(
            TDT_OWNER_CHANGE,
            TDT_DEMO_RETURN,
            TDT_IMPORT,
            TDT_NEW_INBOUND,
            TDT_OPERATION_RETURN,
            TDT_OUTBOUND,
            TDT_SALES_RETURN,
            TDT_SECONDHAND_INBOUND,
            TDT_TRANSFER
    );

    public static boolean isRollbackAllowedType(String typeId) {
        return typeId != null && ROLLBACK_ALLOWED_TYPE_IDS.contains(typeId);
    }

    public static boolean isCrossEntityTicket(String typeId) {
        return TDT_CROSS_ENTITY_BORROW.equals(typeId) || TDT_CROSS_ENTITY_RETURN.equals(typeId);
    }
}
