package com.renegade.domain;


import java.sql.Timestamp;

public class M9UserDO {
    private Integer id;
    private String nick_name;
    private String telphone;
    private String login_pwd;
    private String eos_address;
    private String email;
    private String sex;
    private Integer light4_red;
    private Integer light4_blue;
    private Integer light4_green;
    private Integer light40_red;
    private Integer light40_blue;
    private Integer light40_green;
    private Integer user_pid;
    private String user_ptel;
    private Timestamp cre_date;
    private Timestamp upd_date;
    private String user_state;
    private String remark;
    private String pay_password;

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUser_state() {
        return user_state;
    }

    public void setUser_state(String user_state) {
        this.user_state = user_state;
    }

    public String getUser_ptel() {
        return user_ptel;
    }

    public void setUser_ptel(String user_ptel) {
        this.user_ptel = user_ptel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getLogin_pwd() {
        return login_pwd;
    }

    public void setLogin_pwd(String login_pwd) {
        this.login_pwd = login_pwd;
    }

    public String getEos_address() {
        return eos_address;
    }

    public void setEos_address(String eos_address) {
        this.eos_address = eos_address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getLight4_red() {
        return light4_red;
    }

    public void setLight4_red(Integer light4_red) {
        this.light4_red = light4_red;
    }

    public Integer getLight4_blue() {
        return light4_blue;
    }

    public void setLight4_blue(Integer light4_blue) {
        this.light4_blue = light4_blue;
    }

    public Integer getLight4_green() {
        return light4_green;
    }

    public void setLight4_green(Integer light4_green) {
        this.light4_green = light4_green;
    }

    public Integer getLight40_red() {
        return light40_red;
    }

    public void setLight40_red(Integer light40_red) {
        this.light40_red = light40_red;
    }

    public Integer getLight40_blue() {
        return light40_blue;
    }

    public void setLight40_blue(Integer light40_blue) {
        this.light40_blue = light40_blue;
    }

    public Integer getLight40_green() {
        return light40_green;
    }

    public void setLight40_green(Integer light40_green) {
        this.light40_green = light40_green;
    }

    public Integer getUser_pid() {
        return user_pid;
    }

    public void setUser_pid(Integer user_pid) {
        this.user_pid = user_pid;
    }

    public Timestamp getCre_date() {
        return cre_date;
    }

    public void setCre_date(Timestamp cre_date) {
        this.cre_date = cre_date;
    }

    public Timestamp getUpd_date() {
        return upd_date;
    }

    public void setUpd_date(Timestamp upd_date) {
        this.upd_date = upd_date;
    }
}
