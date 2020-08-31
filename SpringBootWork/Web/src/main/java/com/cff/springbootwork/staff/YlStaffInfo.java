package com.cff.springbootwork.staff;


/**
 * 雇员信息表App用对象 yl_staff_info
 * 
 * @author ruoyi
 * @date 2020-07-14
 */
public class YlStaffInfo
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 员工姓名 */
    private String realName;

    /** 员工电话 */
    private String userTel;

    /** App登陆用户名 */
    private String loginName;

    /** App登陆密码 */
    private String loginPwd;

    /** 所属公司 */
    private Long orgNo;

    /** 所属公司名称 */
    private String orgName;

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }
    public void setUserTel(String userTel) 
    {
        this.userTel = userTel;
    }

    public String getUserTel() 
    {
        return userTel;
    }
    public void setLoginName(String loginName) 
    {
        this.loginName = loginName;
    }

    public String getLoginName() 
    {
        return loginName;
    }
    public void setLoginPwd(String loginPwd) 
    {
        this.loginPwd = loginPwd;
    }

    public String getLoginPwd() 
    {
        return loginPwd;
    }
    public void setOrgNo(Long orgNo) 
    {
        this.orgNo = orgNo;
    }

    public Long getOrgNo() 
    {
        return orgNo;
    }
    public void setOrgName(String orgName) 
    {
        this.orgName = orgName;
    }

    public String getOrgName() 
    {
        return orgName;
    }
}
