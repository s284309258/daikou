/**
 *@author: chenyoulong 
 *@Title:BatchDsReqDTO.java
 *@date 2020-6-1 ????10:55:42 
 *@Description:TODO
 */
package service.version2;

import service.util.JsonUtil;
import service.util.JsontoBeanUtil;

import java.util.List;

/**
 *@ClassName:BatchDsReqDTO
 *@author: chenyoulong  Email: chen.youlong@payeco.com
 *@date :2020-6-1 ????10:55:42
 *@Description:TODO ????????
 */
public class BatchDsDetailResDTO {
	private String merOrderNo;
	/**
	 * ??????????
	 */
	private String payecoOrderNo;
	private String accNo;
	private String accName;
	private String amount;
	private String mobileNo;
	private String payState;
	private String resMsg;
	
	
	public static List<BatchDsDetailResDTO> parseJson(String jsonStr){
		 return JsontoBeanUtil.getListFromJsonArrStr(jsonStr, BatchDsDetailResDTO.class);
	}
	
	public String convertToJsonStr(){
		 return JsonUtil.toJson(this);
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public String getPayecoOrderNo() {
		return payecoOrderNo;
	}

	public void setPayecoOrderNo(String payecoOrderNo) {
		this.payecoOrderNo = payecoOrderNo;
	}
	
	
	
	
}
