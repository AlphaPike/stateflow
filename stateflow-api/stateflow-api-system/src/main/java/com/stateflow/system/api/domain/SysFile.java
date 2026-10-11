package com.stateflow.system.api.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 文件信息
 * 
 * @author stateflow
 */
@Data
public class SysFile
{
    /**
     * 文件名称 (FileId)
     */
    private String name;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 相对地址
     */
    private String relativeUrl;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("name", getName())
            .append("url", getUrl())
            .toString();
    }
}
