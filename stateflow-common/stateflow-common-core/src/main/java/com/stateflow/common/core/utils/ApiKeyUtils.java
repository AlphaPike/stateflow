package com.stateflow.common.core.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * API Key 工具类
 */
public final class ApiKeyUtils
{
    private ApiKeyUtils()
    {
    }

    public static String sha256Hex(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return "";
        }
        try
        {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(hash.length * 2);
            for (byte item : hash)
            {
                builder.append(String.format("%02x", item));
            }
            return builder.toString();
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new IllegalStateException("SHA-256 algorithm not available", ex);
        }
    }
}
