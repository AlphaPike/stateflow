package com.stateflow.system.api.domain;

import java.util.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.stateflow.common.core.annotation.Excel;
import com.stateflow.common.core.annotation.Excel.ColumnType;
import com.stateflow.common.core.web.domain.BaseEntity;
import org.springframework.util.CollectionUtils;

/**
 * 角色表 sys_role
 * 
 * @author stateflow
 */
@Data
public class SysRole extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 角色ID */
    @TableId
    private Long roleId;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String roleName;

    /** 角色权限 */
    @Excel(name = "角色权限")
    private String roleKey;

    /** 角色排序 */
    @Excel(name = "角色排序")
    private Integer roleSort;

    @Size(message = "描述不能超过500字", min = 0, max = 500)
    private String roleDesc;

    /** 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限） */
    @Excel(name = "数据范围", readConverterExp = "1=所有数据权限,2=自定义数据权限,3=本部门数据权限,4=本部门及以下数据权限,5=仅本人数据权限")
    private String dataScope;

    // 优先级
    private Integer priority;

    /** 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示） */
    private boolean menuCheckStrictly;

    /** 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ） */
    private boolean deptCheckStrictly;

    /** 角色状态（0正常 1停用） */
    @Excel(name = "角色状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 用户是否存在此角色标识 默认不存在 */
    @TableField(exist = false)
    private boolean flag = false;

    /** 菜单组 */
    @TableField(exist = false)
    private Long[] menuIds;

    /** 部门组（数据权限） */
    @TableField(exist = false)
    private Long[] deptIds;

    /** 角色菜单权限 */
    @TableField(exist = false)
    private Set<String> permissions;

    /**
     * 是否是系统默认角色
     */
    private Integer sysDefault;

    @TableField(exist = false)
    private Long userId;

    /**
     * 上级ID，顶级用 * 表示
     */
    private String parentId;

    /** 作用域（查询条件，来源于 sys_role_type_rel） */
    @TableField(exist = false)
    private String scope;

    /** 业务类型（查询条件，来源于 sys_role_type_rel） */
    @TableField(exist = false)
    private String sector;

    /**
     * 角色是否可在当前场景下编辑（不落库）。
     * 租户组织成员等仅能在租户信息中维护的角色为 false。
     */
    @TableField(exist = false)
    private Boolean editable;

    /**
     * 绑定该角色时是否必须指定负责仓库（不落库）。
     */
    @TableField(exist = false)
    private Boolean requireWarehouses;

    /**
     * 新增时选填：从该 roleKey 对应角色复制菜单权限（仅自身配置，不含下级）。
     */
    @TableField(exist = false)
    private String copyRoleKey;


    public SysRole()
    {

    }

    public SysRole(Long roleId)
    {
        this.roleId = roleId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.roleId);
    }

    public static boolean isAdmin(Long roleId)
    {
        return roleId != null && 1L == roleId;
    }

    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字符")
    public String getRoleName()
    {
        return roleName;
    }


    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字符")
    public String getRoleKey()
    {
        return roleKey;
    }


    @NotNull(message = "显示顺序不能为空")
    public Integer getRoleSort()
    {
        return roleSort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("roleKey", getRoleKey())
            .append("roleSort", getRoleSort())
            .append("dataScope", getDataScope())
            .append("menuCheckStrictly", isMenuCheckStrictly())
            .append("deptCheckStrictly", isDeptCheckStrictly())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
