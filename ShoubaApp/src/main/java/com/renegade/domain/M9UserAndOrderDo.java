package com.renegade.domain;

import java.sql.Timestamp;
import java.util.Date;

public class M9UserAndOrderDo {
    private Integer id;
    private String order_id;
    private Integer quantity;
    private String currency;
    private String state;
    public String eos_address;
    private String eos_qrcode;
    private Integer user_id;
    private String upd_date;
    private String remark;
    private Integer buyer;
    private String nick_name;
    private String telphone;
    private String login_pwd;
    private String email;
    private String sex;
    private Integer light4_red;
    private Integer light4_blue;
    private Integer light4_green;
    private Integer light40_red;
    private Integer light40_blue;
    private Integer light40_green;
    private Integer senior;

    private Timestamp cre_date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEos_address() {
        return eos_address;
    }

    public void setEos_address(String eos_address) {
        this.eos_address = eos_address;
    }

    public String getEos_qrcode() {
        return eos_qrcode;
    }

    public void setEos_qrcode(String eos_qrcode) {
        this.eos_qrcode = eos_qrcode;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUpd_date() {
        return upd_date;
    }

    public void setUpd_date(String upd_date) {
        this.upd_date = upd_date;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getBuyer() {
        return buyer;
    }

    public void setBuyer(Integer buyer) {
        this.buyer = buyer;
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

    public Integer getSenior() {
        return senior;
    }

    public void setSenior(Integer senior) {
        this.senior = senior;
    }

    public Date getCre_date() {
        return cre_date;
    }

    public void setCre_date(Timestamp cre_date) {
        this.cre_date = cre_date;
    }
}
