package com.stateflow.common.core.constant;

/**
 * 设备故障现象字典（sys_device_fault_type）相关常量。
 * <p>历史命名含 reason/故障原因，实际存的是故障现象。</p>
 */
public final class DeviceFaultTypeConstants {
    private DeviceFaultTypeConstants() {
    }

    /** 启用 */
    public static final String STATUS_ENABLED = "0";

    /** 停用 */
    public static final String STATUS_DISABLED = "1";

    /** 系统内置「其他」故障现象类型ID */
    public static final String OTHER_TYPE_ID = "FAULT_TYPE_OTHER";

    /** 系统内置「其他」故障现象名称 */
    public static final String OTHER_REASON_LABEL = "其他";

    /** 故障现象编号前缀 */
    public static final String CODE_PREFIX = "P";

    /** 编号数字位数 */
    public static final int CODE_SEQ_WIDTH = 4;
}
