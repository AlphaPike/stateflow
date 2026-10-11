package com.stateflow.common.security.config;

import java.util.TimeZone;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.stateflow.common.core.utils.poi.ExcelPermiChecker;
import com.stateflow.common.core.utils.poi.ExcelPermiUtils;
import com.stateflow.common.security.jackson.JsonPermiBeanSerializerModifier;
import com.stateflow.common.security.poi.AuthExcelPermiChecker;

/**
 * 系统配置
 *
 * @author stateflow
 */
public class ApplicationConfig
{
    /**
     * 时区配置
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
    {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }

    /**
     * 注册字段权限序列化（@JsonPermi）
     */
    @Bean
    public Module jsonPermiModule()
    {
        SimpleModule module = new SimpleModule("jsonPermiModule");
        module.setSerializerModifier(new JsonPermiBeanSerializerModifier());
        return module;
    }

    /**
     * 注册 Excel 导出字段权限（@ExcelPermi）
     */
    @Bean
    public ExcelPermiChecker excelPermiChecker()
    {
        AuthExcelPermiChecker checker = new AuthExcelPermiChecker();
        ExcelPermiUtils.register(checker);
        return checker;
    }
}
