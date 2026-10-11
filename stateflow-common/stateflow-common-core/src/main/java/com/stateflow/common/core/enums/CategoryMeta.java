package com.stateflow.common.core.enums;

import com.stateflow.common.core.utils.StringUtils;
import lombok.Getter;

@Getter
public enum CategoryMeta {
    COMPLETE_PRODUCT("FP", "产品整机", "FP"),
    ACCESSORIES("PA", "产品配件", "PA"),
    OPERATING("OE", "运营设备", "OE");

    private final String code;
    private final String displayName;
    private final String prefix;

    CategoryMeta(String code, String displayName, String prefix) {
        this.code = code;
        this.displayName = displayName;
        this.prefix = prefix;
    }

    public static CategoryMeta fromInput(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        for (CategoryMeta v : values()) {
            if (v.code.equalsIgnoreCase(input)) {
                return v;
            }
        }
        return null;
    }
}
