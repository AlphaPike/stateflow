package com.stateflow.system.controller;

import java.util.Collections;
import java.util.HashSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stateflow.common.core.domain.R;
import com.stateflow.common.security.annotation.InnerAuth;
import com.stateflow.system.api.domain.SysUser;
import com.stateflow.system.api.model.LoginUser;

/**
 * The only user surface auth needs: load by phone, then record the login.
 */
@RestController
@RequestMapping("/user")
public class InnerUserController
{
    private static final RowMapper<SysUser> USER_MAPPER = (rs, rowNum) -> {
        SysUser user = new SysUser();
        user.setUserId(rs.getLong("user_id"));
        user.setUserName(rs.getString("user_name"));
        user.setNickName(rs.getString("nick_name"));
        user.setPhonenumber(rs.getString("phonenumber"));
        user.setPassword(rs.getString("password"));
        user.setStatus(rs.getString("status"));
        user.setDelFlag(rs.getString("del_flag"));
        return user;
    };

    private final JdbcTemplate jdbcTemplate;

    public InnerUserController(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    @InnerAuth
    @GetMapping("/info-phone/{phone}")
    public R<LoginUser> infoByPhone(@PathVariable("phone") String phone)
    {
        SysUser user = jdbcTemplate.query(
                "select user_id, user_name, nick_name, phonenumber, password, status, del_flag "
                        + "from sys_user where phonenumber = ? and del_flag = '0' limit 1",
                USER_MAPPER, phone).stream().findFirst().orElse(null);
        if (user == null)
        {
            return R.fail("user not found");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUserid(user.getUserId());
        loginUser.setUsername(user.getUserName());
        loginUser.setSysUser(user);
        loginUser.setRoles(new HashSet<String>(Collections.singleton("admin")));
        loginUser.setPermissions(new HashSet<String>(Collections.singleton("*:*:*")));
        return R.ok(loginUser);
    }

    @InnerAuth
    @PutMapping("/recordlogin")
    public R<Boolean> recordLogin(@RequestBody SysUser sysUser)
    {
        jdbcTemplate.update(
                "update sys_user set login_ip = ?, login_date = ? where user_id = ?",
                sysUser.getLoginIp(), sysUser.getLoginDate(), sysUser.getUserId());
        return R.ok(Boolean.TRUE);
    }
}
