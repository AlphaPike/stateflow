package com.stateflow.common.security.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.stateflow.common.security.annotation.JsonPermi;
import com.stateflow.common.security.annotation.Logical;
import com.stateflow.common.security.auth.AuthUtil;
import com.stateflow.common.security.utils.SecurityUtils;

/**
 * 按 {@link JsonPermi} 决定是否写出字段。
 */
public class JsonPermiBeanPropertyWriter extends BeanPropertyWriter
{
    private final JsonPermi jsonPermi;

    public JsonPermiBeanPropertyWriter(BeanPropertyWriter base, JsonPermi jsonPermi)
    {
        super(base);
        this.jsonPermi = jsonPermi;
    }

    @Override
    public BeanPropertyWriter rename(NameTransformer transformer)
    {
        return new JsonPermiBeanPropertyWriter(super.rename(transformer), jsonPermi);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception
    {
        if (!allowed())
        {
            return;
        }
        super.serializeAsField(bean, gen, prov);
    }

    @Override
    public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception
    {
        if (!allowed())
        {
            return;
        }
        super.serializeAsElement(bean, gen, prov);
    }

    private boolean allowed()
    {
        try
        {
            if (SecurityUtils.isAdmin())
            {
                return true;
            }
        }
        catch (Exception ignored)
        {
            // 无登录用户时按权限集合判断，通常会隐藏
        }
        String[] permissions = jsonPermi.value();
        if (permissions == null || permissions.length == 0)
        {
            return false;
        }
        if (jsonPermi.logical() == Logical.AND)
        {
            for (String permission : permissions)
            {
                if (!AuthUtil.hasPermi(permission))
                {
                    return false;
                }
            }
            return true;
        }
        for (String permission : permissions)
        {
            if (AuthUtil.hasPermi(permission))
            {
                return true;
            }
        }
        return false;
    }
}
