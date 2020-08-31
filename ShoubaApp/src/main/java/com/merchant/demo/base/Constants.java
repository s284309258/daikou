package com.merchant.demo.base;

/**
 * SMS+API 2.0 常量
 * @author Payeco
 */
public class Constants {
  /**
   * 描述:商户根据对接的实际情况对下面数据进行修改；
   *     以下数据在测试通过后，部署到生产环境，需要替换为生产的数据。
   * 1.商户编号:由易联产生，邮件发送给商户；
   * 2.商户接收订单通知接口地址（异步通知）；
   * 3.商户RSA私钥，商户自己产生（可采用易联提供RSA工具产生）
   * 4.互联网金融测试商户RSA私钥（从证书文件导出）
   */

  //内部测试商户号
//  public static final String MERCHANT_ID = "302020000055";
  public static final String MERCHANT_ID = "502050005150";
  //互联网金融行业的商户号
//  public static final String MERCHANT_ID = "00000001";
  public static final String MERCHANT_NOTIFY_URL = "http://shouba.szfyhgs.com/yilian/api/notify";
  //生成商户私钥
  public static final String MERCHANT_RSA_PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAM7+CdFz6D/oZNoB8KcnUyqTf6hdzEA/GwMjYqjAqnZUU4SuD1bKLK/5VTVJ19HV5pAE6nRLrX9F37xYyjPXm1SN5UOMq/2NIsKw2NiUXujtDjSHjwKX3I83adnCieb1NmTAGiBQ/B80j/pAUgRdPpo+dR8NXqKXoasCOb54upqXAgMBAAECgYEAiz3M8P8dftAf959Seb80bPGn8GSYn6dTznqhRY/Fm7ACNsC4RNPf18jMDvrLmbZgUQVdijnWTkV8ykCYVON28uaBrpUHldpbkiZhG/Ah76kEfG89nXWDiSk1+ieXc9o6jNnzENMwjk0kjNZyH9m8IWmdP/SYQN+9n1ZtIbDU9YECQQDoMiIRUPCEWwEZS5L/8WClhuZ/o2DOSSxDoI6fc+z/OHFgFcfCikt5nifJiEldz+rM+ImSLZ5nBH2GlC6X/G33AkEA5DZzp/CGd5P3yxMJQE+R/pgIG02oqvHZIOwIndJCpxYfwnRyuHRxbxdAyb0juBEGrM4vG5A3dtm8lhab1xaQYQJAX4IHi1HcrMR2Dj6Mx6Q0mcGEXCoKrq9geqy3HZVkfZJs6BXtZfLWmr7YJMhfTSwxq3/rrmn/RuaM72dSRwf4/wJAC9ivxEX+FPL/Z/FDB62ZvIeT1CcB3G3VgZn/EII7p4TEfUmiMuUIhmXEnTROSF3J5OqHE303A9plqbzethmbwQJAJGv/p6F3YWaGdTE405DuVX/X5WfCTpYDA5Ssjkq8rq52e6/qcfHUsWx46PgQfl4hVZEJk12+eG4EQy4SRZhw8g==";
  //测试商户私钥
  //  public static final String MERCHANT_RSA_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOAqNu0SFh5Ksz8Mp/vzm1kxiMYoSREXNXGajCHkKJIVwTaxtPaPYq3JiASZbCALrjp9UM0jLsqayDzF51paUt5lbBDRgqabAClUos3X4c7v2uUt98ILDi8ABQV8BrMZE5RKaLcvvr3mhu/JhabXBz2SBflSCSG3K8HQRTDjjp7nAgMBAAECgYBg01suQ6WyJ+oMzdaxiaQMfszpavVEoJXBIFRvPzIXB7aRfWkBJyYkkuxhsDN4FBOJyB9ivFO1x+298m3gJSutfXfSRA9Kq9XrEIQDjJB4PBx8yTVmVckgCJlsWnhuySHf7gapLkfLHQ+GgiUpYUPW0MJczsu7juuMUZdKHJ6rIQJBAPVLJAxXQYI2e8WMfTPR1jqeZXSQ5r4XI0d8wKFMDa68gq3Y3B2CKmWO16faxafJ8oUWJtJJwRQT6YItBVA3DWUCQQDp8vymxQkLCVpyQ+SfG0Ab9mw2G7p2Y3pAYwms7SIOILoADUbJl2UxpyROj9N9Lq2ndZ0rNIWw4iJXigwRuaxbAkEAkiN7TZLqp25YXUCvEyFwFapq3YC6yAO29A9CIJbUDAepf3OU6Eu1gJ4So6F2YtmxEFM7O8vPKWwXkYPLB5hU9QJAHLjWR+s81vwI/KpVMSt5TXWNh38T/2DrK2h9UZuzaKSf8U2v+SP7KoNos7R4tI+8hiisaReDqlm4+aJbJPn0rQJBAK0EQLyG8iks7Ppclq9UBgEx2iKSsg9y60aSt1YwI73wEdW18paBtoUMjQ5GAqCyVmEb01IY6Kn1si43zqHct4o=";

  /**
   * 描述:易联信息: 以下信息区分为测试环境和生产环境，商户根据自己对接情况进行数据选择
   * 1.易联服务器地址_测试环境；
   * 2.易联服务器地址_生产环境；
   * 3.订单RSA公钥（易联提供）测试环境
   * 4.订单RSA公钥（易联提供）生产环境
   */
//  public static final String PAYECO_URL = "https://testmobile.payeco.com";
  public static final String PAYECO_URL = "https://tmobile.payeco.com";
//  public static final String PAYECO_RSA_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRxin1FRmBtwYfwK6XKVVXP0FIcF4HZptHgHu+UuON3Jh6WPXc9fNLdsw5Hcmz3F5mYWYq1/WSRxislOl0U59cEPaef86PqBUW9SWxwdmYKB1MlAn5O9M1vgczBl/YqHvuRzfkIaPqSRew11bJWTjnpkcD0H+22kCGqxtYKmv7kwIDAQAB";
  public static final String PAYECO_RSA_PUBLIC_KEY =   "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCoymAVb04bvtIrJxczCT/DYYltVlRjBXEBFDYQpjCgSorM/4vnvVXGRb7cIaWpI5SYR6YKrWjvKTJTzD5merQM8hlbKDucxm0DwEj4JbAJvkmDRTUs/MZuYjBrw8wP7Lnr6D6uThqybENRsaJO4G8tv0WMQZ9WLUOknNv0xOzqFQIDAQAB";
  
  /**
   * 描述：通用常量
   */
  public static final String PAYECO_CODE_SUCCESS_VAL = "0000";
  // 短信码检查接口交易码
  public static final String PAYECO_API_SMS_CODE_CHECK = "APISmsCodeCheckV2";
  // 重发短信码接口
  public static final String PAYECO_API_SMS_AGAIN = "APISmsAgain";
  // 支付接口交易码
  public static final String PAYECO_API_PAY = "APIPayV2";
  //测试环境的短信码固定000000
  public static final String PAYECO_TEST_SMSCODE = "000000";
  // 订单查询接口交易码
  public static final String PAYECO_QUERY_ORDER = "QueryOrder";
  // 订单冲正接口交易码
  public static final String PAYECO_QUASH_ORDER = "QuashOrder";
  // 商户订单退款申请接口交易码
  public static final String PAYECO_REFUND_ORDER = "RefundOrder";
  // 商户订单退款结果查询接口交易码
  public static final String PAYECO_QUERY_REFUND = "QueryRefund";
  // 互联网金融行业银行卡解除绑定接口
  public static final String PAYECO_UNBOUND_BANK_CARD = "UnboundBankCard";
  
}
