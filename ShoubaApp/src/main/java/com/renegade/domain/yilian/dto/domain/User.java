package com.renegade.domain.yilian.dto.domain;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 * 
 * @author ruoyi
 */
public class User extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 部门ID */
    private Long deptId;

    /** 部门父ID */
    private Long parentId;

    /** 角色ID */
    private Long roleId;

    /** 登录名称 */
    private String loginName;

    /** 用户名称 */
    private String userName;

    /** 用户邮箱 */
    private String email;

    /** 手机号码 */
    private String phonenumber;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;

    /** 密码 */
    private String password;

    /** 盐加密 */
    private String salt;

    /** 帐号状态（0正常 1停用） */
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 最后登陆IP */
    private String loginIp;

    /** 最后登陆时间 */
    private Date loginDate;

    /** 部门对象 */
    private Dept dept;

    private List<Role> roles;

    /** 角色组 */
    private Long[] roleIds;

    /** 岗位组 */
    private Long[] postIds;
    
    //是否身份认证
    private boolean isAuth;
    
    //实名认证是否需要审核（0:不需要，1：需要）
    private String authIsaudit;
    
    //银行卡是否需要审核（0:不需要，1：需要）
    private String cardIsaudit;
    
    //中付提现账号
    private String subAgentAccount;
    
    //app_id进件账号
    private String appId;

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public boolean isAdmin()
    {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId)
    {
        return userId != null && 1L == userId;
    }

    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getRoleId()
    {
        return roleId;
    }

    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    @NotBlank(message = "登录账号不能为空")
    @Size(min = 0, max = 30, message = "登录账号长度不能超过30个字符")
    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

//    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber)
    {
        this.phonenumber = phonenumber;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getAvatar()
    {
        return avatar;
    }

    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }

    /**
     * 生成随机盐
     */
    public void randomSalt()
    {
        // 一个Byte占两个字节，此处生成的3字节，字符串长度为6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        setSalt(hex);
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getDelFlag()
    {
        return delFlag;
    }

    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

    public String getLoginIp()
    {
        return loginIp;
    }

    public void setLoginIp(String loginIp)
    {
        this.loginIp = loginIp;
    }

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public Dept getDept()
    {
        if (dept == null)
        {
            dept = new Dept();
        }
        return dept;
    }

    public void setDept(Dept dept)
    {
        this.dept = dept;
    }

    public List<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(List<Role> roles)
    {
        this.roles = roles;
    }

    public Long[] getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds)
    {
        this.roleIds = roleIds;
    }

    public Long[] getPostIds()
    {
        return postIds;
    }

    public void setPostIds(Long[] postIds)
    {
        this.postIds = postIds;
    }
    
    public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	public String getAuthIsaudit() {
		return authIsaudit;
	}

	public void setAuthIsaudit(String authIsaudit) {
		this.authIsaudit = authIsaudit;
	}

	public String getCardIsaudit() {
		return cardIsaudit;
	}

	public void setCardIsaudit(String cardIsaudit) {
		this.cardIsaudit = cardIsaudit;
	}

	public String getSubAgentAccount() {
		return subAgentAccount;
	}

	public void setSubAgentAccount(String subAgentAccount) {
		this.subAgentAccount = subAgentAccount;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", deptId=" + deptId + ", parentId=" + parentId + ", roleId=" + roleId
				+ ", loginName=" + loginName + ", userName=" + userName + ", email=" + email + ", phonenumber="
				+ phonenumber + ", sex=" + sex + ", avatar=" + avatar + ", password=" + password + ", salt=" + salt
				+ ", status=" + status + ", delFlag=" + delFlag + ", loginIp=" + loginIp + ", loginDate=" + loginDate
				+ ", dept=" + dept + ", roles=" + roles + ", roleIds=" + Arrays.toString(roleIds) + ", postIds="
				+ Arrays.toString(postIds) + ", isAuth=" + isAuth + ", authIsaudit=" + authIsaudit + ", cardIsaudit="
				+ cardIsaudit + ", subAgentAccount=" + subAgentAccount + ", appId=" + appId + "]";
	}
	
}
