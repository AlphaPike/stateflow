package com.stateflow.common.core.enums;

import com.stateflow.common.core.constant.FileTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 表关联文件存储固定字段
 */
@Getter
@AllArgsConstructor
public enum TableFileType {
    /** 项目预览图 */
    PROJECT_REVIEW_IMG("review_img", "sys_project", "project_id", FileTypeConstants.IMG_TYPE),
    PROJECT_QR_IMG("qr_code_img", "sys_project", "project_id", FileTypeConstants.IMG_TYPE),
    PROJECT_ICON_IMG("icon_img", "sys_project", "project_id", FileTypeConstants.IMG_TYPE),
    PROJECT_QR_ZIP("qr_code_img", "sys_project", "project_id", FileTypeConstants.ZIP_TYPE),
    /** SKU预览 */
    SKU_IMG("sku_img", "sys_sku", "sku_id", FileTypeConstants.IMG_TYPE),
    PAY_IMG("pay_img", "sys_sku", "sku_id", FileTypeConstants.IMG_TYPE),
    /** 发票PDF */
    INVOICE_PDF("invoice_pdf", "sys_invoice", "invoice_id", FileTypeConstants.PDF_TYPE),
    /** 设备类型注册预览图（可多张） */
    DEVICE_CATEGORI_IMG("device_img", "sys_device_categori", "categori_id", FileTypeConstants.IMG_TYPE),

    /** 盘点明细图片（可多张） */
    INVENTORY_ITEM_IMG("inventory_img", "sys_device_inventory_task_item", "item_id", FileTypeConstants.IMG_TYPE),

    /** 设备远程日志包 */
    DEVICE_REMOTE_LOG("log_file", "sys_device_remote_log", "log_id", FileTypeConstants.ZIP_TYPE),

    /** 一体机外屏广告图片素材 */
    KIOSK_AD_IMAGE("material_file", "sys_kiosk_ad_plan_material", "material_id", FileTypeConstants.IMG_TYPE),

    /** 一体机外屏广告视频素材 */
    KIOSK_AD_VIDEO("material_file", "sys_kiosk_ad_plan_material", "material_id", FileTypeConstants.VIDEO_TYPE),

    /** 一体机外屏广告全局默认图片 */
    KIOSK_AD_DEFAULT_IMAGE("config_value", "sys_config", "config_key", FileTypeConstants.IMG_TYPE),

    APPROVE_ATTACHMENT("attachments", "sys_approval_task_history", "history_id", FileTypeConstants.ANY_TYPE),

    /** 维修明细无法维修设备图片（可多张） */
    REPAIR_ITEM_UNREPAIRABLE_IMG("unrepairable_img", "sys_device_repair_item", "repair_item_id", FileTypeConstants.IMG_TYPE);

    private final String columnName;
    private final String fromTable;
    private final String fromIdColumn;
    private final String fileType;
}
