package com.stateflow.common.core.constant;

/**
 * 设备故障原因相关常量
 */
public final class DeviceFaultCauseConstants {
    private DeviceFaultCauseConstants() {
    }

    /** 排查时「其他原因」快照使用的原因ID（不落故障原因字典） */
    public static final String OTHER_CAUSE_ID = "FAULT_CAUSE_OTHER";

    /** 「其他原因」展示名 */
    public static final String OTHER_CAUSE_LABEL = "其他";

    /** 故障原因编号前缀 */
    public static final String CODE_PREFIX = "C";

    /** 编号数字位数 */
    public static final int CODE_SEQ_WIDTH = 4;
}
