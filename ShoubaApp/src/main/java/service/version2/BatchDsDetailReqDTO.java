/**
 *@author: chenyoulong 
 *@Title:BatchDsReqDTO.java
 *@date 2020-6-1 上午10:55:42 
 *@Description:TODO
 */
package service.version2;

import service.util.JsontoBeanUtil;

import java.util.List;

/**
 *@ClassName:BatchDsReqDTO
 *@author: chenyoulong  Email: chen.youlong@payeco.com
 *@date :2020-6-1 上午10:55:42
 *@Description:TODO 批量代收
 */
public class BatchDsDetailReqDTO {
	private String merOrderNo;
	private String accNo;
	private String accName;
	private String accProvince;
	private String accCity;
	private String amount;
	private String bankName;
	//00银行卡，01存折
	private String accType="00"; 
	//0私人，1公司
	private String accProp="0";
	private String idCardType;
	private String idCardNo;
	private String mobileNo;
	private String transDesc;
	
	public static List<BatchDsDetailReqDTO> parseJson(String jsonStr){
		 return JsontoBeanUtil.getListFromJsonArrStr(jsonStr, BatchDsDetailReqDTO.class);
	}
	
	
	
	
	public String getMerOrderNo() {
		return merOrderNo;
	}
	public void setMerOrderNo(String merOrderNo) {
		this.merOrderNo = merOrderNo;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getAccProvince() {
		return accProvince;
	}

	public void setAccProvince(String accProvince) {
		this.accProvince = accProvince;
	}

	public String getAccCity() {
		return accCity;
	}
	public void setAccCity(String accCity) {
		this.accCity = accCity;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getAccProp() {
		return accProp;
	}
	public void setAccProp(String accProp) {
		this.accProp = accProp;
	}
	public String getIdCardType() {
		return idCardType;
	}
	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getTransDesc() {
		return transDesc;
	}
	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}
	
	
}
