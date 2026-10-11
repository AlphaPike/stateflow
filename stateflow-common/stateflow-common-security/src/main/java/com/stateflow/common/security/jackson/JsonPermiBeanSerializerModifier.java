package com.stateflow.common.security.jackson;

import java.util.List;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.stateflow.common.security.annotation.JsonPermi;

/**
 * 将带 {@link JsonPermi} 的属性替换为按权限写出的 PropertyWriter。
 */
public class JsonPermiBeanSerializerModifier extends BeanSerializerModifier
{
    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
            List<BeanPropertyWriter> beanProperties)
    {
        for (int i = 0; i < beanProperties.size(); i++)
        {
            BeanPropertyWriter writer = beanProperties.get(i);
            JsonPermi jsonPermi = writer.getAnnotation(JsonPermi.class);
            if (jsonPermi == null)
            {
                jsonPermi = findOnFieldOrGetter(beanDesc, writer.getName());
            }
            if (jsonPermi != null)
            {
                beanProperties.set(i, new JsonPermiBeanPropertyWriter(writer, jsonPermi));
            }
        }
        return beanProperties;
    }

    private JsonPermi findOnFieldOrGetter(BeanDescription beanDesc, String name)
    {
        for (BeanPropertyDefinition definition : beanDesc.findProperties())
        {
            if (!name.equals(definition.getName()))
            {
                continue;
            }
            if (definition.getField() != null)
            {
                JsonPermi onField = definition.getField().getAnnotation(JsonPermi.class);
                if (onField != null)
                {
                    return onField;
                }
            }
            if (definition.getGetter() != null)
            {
                JsonPermi onGetter = definition.getGetter().getAnnotation(JsonPermi.class);
                if (onGetter != null)
                {
                    return onGetter;
                }
            }
        }
        return null;
    }
}
