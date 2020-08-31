package com.renegade.domain;

public class M9OrderDO {
    private Integer id;
    private String order_id;
    private Integer quantity;
    private String currency;
    private String state;
    private String eos_address;
    private String eos_qrcode;
    private String user_id;
    private String upd_date;
    private String remark;
    private Integer buyer;
    private String cre_date;

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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
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

    public String getCre_date() {
        return cre_date;
    }

    public void setCre_date(String cre_date) {
        this.cre_date = cre_date;
    }
}
