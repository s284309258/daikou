package com.renegade.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.renegade.config.AjaxJson;
import com.renegade.config.MD5;
import com.renegade.config.PropertyUtil;
import com.renegade.config.RegexUtil;
import com.renegade.config.StringUtil;
import com.renegade.config.Utils;
import com.renegade.config.VerificationPhone;
import com.renegade.dao.FrontUserDao;
import com.renegade.dao.M9UserMapper;
import com.renegade.dao.UserAddressDao;
import com.renegade.domain.FrontUserDO;
import com.renegade.domain.UserAddressDO;
import com.renegade.filter.XssHttpServletRequestWrapper;
import com.renegade.service.FrontUserService;
import com.renegade.service.ParamService;
import com.renegade.service.TaskService;
import com.renegade.service.VerifyRecordService;

@Service
public class FrontUserServiceImpl implements FrontUserService {
	@Autowired
	private FrontUserDao frontUserDao;
	@Autowired
	private VerifyRecordService verifyRecordServiceImpl;
	@Autowired
	private UserAddressDao userAddressDao;
	@Autowired
	private TaskService taskServiceImpl;
	@Autowired
    private M9UserMapper m9UserMapper;

	@Override
	public FrontUserDO get(Integer userId) {
		return frontUserDao.get(userId);
	}

	@Override
	public List<FrontUserDO> list(Map<String, Object> map) {
		return frontUserDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return frontUserDao.count(map);
	}

	@Override
	public int save(FrontUserDO frontUser) {
		return frontUserDao.save(frontUser);
	}

	@Override
	public int remove(Integer userId) {
		return frontUserDao.remove(userId);
	}

	@Override
	public int batchRemove(Integer[] userIds) {
		return frontUserDao.batchRemove(userIds);
	}

	@Override
	public AjaxJson login(String phone, String pass, HttpServletRequest req) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if (StringUtils.isEmpty(phone)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("手机号 不能为空");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(pass)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("密码不能为空");
				return ajaxJson;
			}
			// 利用手机号或者邮箱号查询用户信息
			FrontUserDO wFrontUser = frontUserDao.findUserByEmail(phone);
			if (wFrontUser == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入正确账号");
				return ajaxJson;
			}
			if (wFrontUser.getUserStatus() == 0) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("账号已冻结");
				return ajaxJson;
			}
			System.out.println("=========" + pass);
			String newPass = MD5.strToMd5(pass);
			System.out.println("=========" + newPass);
			System.out.println("=========" + wFrontUser.getLoginPassword());
			if (!wFrontUser.getLoginPassword().equals(newPass)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("密码错误");
				return ajaxJson;
			}
			HttpSession session = req.getSession();
			session.setAttribute("userId", wFrontUser.getUserId());
			ajaxJson.setMsg("登录成功");
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ajaxJson;
	}

	@Transactional
	public AjaxJson regiest(String phone, String pass1, String pass2, String code, String userName, String userNickname,
			String pid, String accType, String identityNo, HttpServletRequest req, String type) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if (StringUtils.isEmpty(phone)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入账号");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(identityNo)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入身份证号");
				return ajaxJson;
			}

			if (!RegexUtil.isValidIdCard(identityNo) && !RegexUtil.isHKValidIdCard(identityNo)
					&& !RegexUtil.isMCPhoneLegal(identityNo) && !RegexUtil.isTWValidIdCard(identityNo)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("身份证错误");
				return ajaxJson;
			}

			if (StringUtils.isEmpty(pass1)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入登录密码");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(pass2)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入登录密码");
				return ajaxJson;
			}
			if (!pass1.equals(pass2)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("两次输入的密码不一致");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(code)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("验证码为空");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(userName)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入真实姓名");
				return ajaxJson;
			}
			if (StringUtils.isEmpty(pid)) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入邀请码");
				return ajaxJson;
			}
			// 查询账户信息
			FrontUserDO wuser1 = frontUserDao.findUserByEmail(phone);
			if (wuser1 != null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("该账号已经被注册");
				return ajaxJson;
			}
			// 通过推荐码判断推荐人是否存在
			FrontUserDO wuser = frontUserDao.findUserByCode(pid);
			if (wuser == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入正确的邀请码");
				return ajaxJson;
			}
			// 判断登录密码与确认密码是否一致
			boolean flag = VerificationPhone.verificationPass(pass1);
			if (!flag) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("密码必须是6-20位数字与字母组合，请输入正确的密码");
				return ajaxJson;
			}
			// 判断验证码是否正确
			ajaxJson = verifyRecordServiceImpl.compare(null, "regiest", accType, phone, code, ajaxJson);
			if (!ajaxJson.isSuccess()) {
				ajaxJson.setMsg("验证码有异常");
				return ajaxJson;
			}
			// 生成一个邀请码
			String invitationCode = Utils.getInvitationCode();
			String newPass = MD5.strToMd5(pass1);
			FrontUserDO user = new FrontUserDO();
			// 代数
			user.setAlgebra(wuser.getAlgebra() + 1);// 自己的直推父级+1就是代数
			// 更新父级链以后逗号分隔
			String parentChain = "";
			if (StringUtils.isEmpty(wuser.getParentChain()) || StringUtils.isBlank(wuser.getParentChain())) {
				parentChain = wuser.getUserId().toString();
			} else {
				parentChain = wuser.getParentChain() + "," + wuser.getUserId().toString();
			}
			user.setParentChain(parentChain);
			user.setUserTel(phone);
			user.setPayPassword(newPass);
			user.setLoginPassword(newPass);
			user.setUserPid(wuser.getUserId());
			user.setAccountId(identityNo);// 身份证号码
			if (StringUtils.isBlank(userNickname)) {
				user.setUserName(userName);
			} else {
				user.setUserName(userNickname);// 用户昵称
			}
			user.setWxId(userName);// 真实姓名
			user.setInvitationCode(invitationCode);
			int rows = frontUserDao.insertUser(user);
			if (rows == 1) {
				// 更新父级信息直推个数和伞下所有人数！
				int result = frontUserDao.updateParentDirectNum(wuser.getUserId());
				if (result != 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("注册失败，请稍后再试");
					return ajaxJson;
				}
				// 插入钱包地址
				result = frontUserDao.updateWalletAddress(phone);
				if (result < 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("注册地址失败，请稍后再试");
					return ajaxJson;
				}
				result = frontUserDao.updateWalletAddressVfsUt(phone);
				if (result < 1) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					ajaxJson.setSuccess(false);
					ajaxJson.setMsg("注册地址失败，请稍后再试");
					return ajaxJson;
				}
				req.getSession().setAttribute("userId", user.getUserId());
				ajaxJson.setSuccess(true);
				ajaxJson.setMsg("注册成功");
				if (ajaxJson.isSuccess()) {// 异步插入钱包
					taskServiceImpl.insertWanlletManger(user.getUserId());
					// 插入钱包地址
				}
				return ajaxJson;
			} else {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("注册失败，请稍后再试");
				return ajaxJson;
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("注册失败，请稍后再试");
			return ajaxJson;
		}
	}

	@Override
	public AjaxJson frogetLoginPassWord(Map<String, Object> map) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		try {
			if (StringUtil.getMapValue(map, "code") == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("验证码为空");
				return ajaxJson;
			}
			if (StringUtil.getMapValue(map, "phone") == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("请输入账号");
				return ajaxJson;
			}
			// 验证码校验
			boolean flag = VerificationPhone.verificationPhone(map.get("phone").toString());
			// boolean flag1 = RegexUtil.isHKPhoneLegal(map.get("phone").toString());
			// boolean flag2 = RegexUtil.isMCPhoneLegal(map.get("phone").toString());
			// boolean flag3 = RegexUtil.isTWPhoneLegal(map.get("phone").toString());
			if (flag) {// 只要满足一个就走这个
				ajaxJson = verifyRecordServiceImpl.compare(null, "findPass", "1", map.get("phone").toString(),
						map.get("code").toString(), ajaxJson);
			} else if (RegexUtil.isValidEmail(map.get("phone").toString())) {// 邮箱正确
				ajaxJson = verifyRecordServiceImpl.compare(null, "findPass", "2", map.get("phone").toString(),
						map.get("code").toString(), ajaxJson);
			} else {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("账号格式不正确");
				return ajaxJson;
			}
			if (!ajaxJson.isSuccess()) {
				ajaxJson.setMsg("验证码有异常");
				return ajaxJson;
			}
			if (StringUtil.getMapValue(map, "newPass") == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("新密码不能为空");
				return ajaxJson;
			}
			if (StringUtil.getMapValue(map, "secondPass") == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("再次输入密码不能为空");
				return ajaxJson;
			}
			if (!map.get("secondPass").toString().equals(map.get("newPass").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("两次输入的密码不一致");
				return ajaxJson;
			}
			// 重置密码
			String newPass = MD5.strToMd5(map.get("newPass").toString());
			map.put("newPass", newPass);
			if (m9UserMapper.updateFrogetPass(map) != 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("重置登陆密码失败");
				return ajaxJson;
			}
			ajaxJson.setMsg("操作成功");
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setSuccess(false);
			ajaxJson.setMsg("系统异常");
			return ajaxJson;
		}

	}

	@Override
	public AjaxJson updatePayPass(Map<String, Object> map, Integer userId) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		// String type=map.get("type").toString();
		try {
			if (StringUtil.getMapValue(map, "code") == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("验证码不能为空");
				return ajaxJson;
			}
			FrontUserDO user = frontUserDao.get(userId);
			// 验证码校验
			boolean flag = VerificationPhone.verificationPhone(user.getUserTel());
			boolean flag1 = RegexUtil.isHKPhoneLegal(user.getUserTel());
			boolean flag2 = RegexUtil.isMCPhoneLegal(user.getUserTel());
			boolean flag3 = RegexUtil.isTWPhoneLegal(user.getUserTel());
			if (flag || flag1 || flag2 || flag3) {// 只要满足一个就走这个
				ajaxJson = verifyRecordServiceImpl.compare(null, "updatePayPass", "1", user.getUserTel(),
						map.get("code").toString(), ajaxJson);
			} else if (RegexUtil.isValidEmail(user.getUserTel())) {// 邮箱正确
				ajaxJson = verifyRecordServiceImpl.compare(null, "updatePayPass", "2", user.getUserTel(),
						map.get("code").toString(), ajaxJson);
			} else {
				ajaxJson.setMsg("账号格式不正确");
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			if (!ajaxJson.isSuccess()) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("验证码异常");
				return ajaxJson;
			}
			if (StringUtil.getMapValue(map, "newPass") == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("新密码不能为空");
				return ajaxJson;
			}
			if (StringUtil.getMapValue(map, "secondPass") == null) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("再次输入密码不能为空");
				return ajaxJson;
			}
			if (!map.get("secondPass").toString().equals(map.get("newPass").toString())) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("两次输入的密码不一致");
				return ajaxJson;
			}
			// 设置支付密码
			String newPass = MD5.strToMd5(map.get("newPass").toString());
			map.put("newPass", newPass);
			map.put("phone", user.getUserTel());
			if (frontUserDao.updatePayPass(map) != 1) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("修改支付密码失败");
				return ajaxJson;
			}
			ajaxJson.setMsg("修改支付密码成功");
			ajaxJson.setSuccess(true);
			return ajaxJson;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setMsg("系统异常");
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
	}

	@Override
	public AjaxJson addUserAddress(Map<String, Object> map, Integer userId) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		// String type = map.get("type").toString();
		try {
			FrontUserDO frontUserDO = frontUserDao.get(userId);
			if (frontUserDO.getUserStatus() == 0) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("对不起！该账号已经被冻结");
				// ajaxJson.setMsg(PropertyUtil.getProperty("frozen", type));
				return ajaxJson;
			}
			System.out.println(map.toString());
			String userPhone = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "userPhone"));
			String userName = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "userName"));
			String province = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "province"));
			String city = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "city"));
			String area = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "area"));
			String datalied = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "datalied"));
			String pCode = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "pCode"));
			String cCode = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "cCode"));
			String aCode = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "aCode"));
			// || StringUtils.isEmpty(pCode) || StringUtils.isEmpty(cCode) ||
			// StringUtils.isEmpty(aCode)
			if (StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(province)
					|| StringUtils.isEmpty(city) || StringUtils.isEmpty(area) || StringUtils.isEmpty(datalied)) {
				ajaxJson.setMsg("用户信息地址不能为空");
				// ajaxJson.setMsg(PropertyUtil.getProperty("addressNull", type));
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			boolean flag = VerificationPhone.verificationPhone(userPhone);
			if (!flag) {
				ajaxJson.setMsg("手机格式有误");
				// ajaxJson.setMsg(PropertyUtil.getProperty("phoneError", type));
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			UserAddressDO userAddress = new UserAddressDO();
			userAddress.setAddressPhone(userPhone);
			userAddress.setUserId(userId);
			userAddress.setAddressArea(area);
			userAddress.setAddressCity(city);
			userAddress.setAddressDetailed(datalied);
			userAddress.setAddressProvince(province);
			userAddress.setAddressState(0);
			userAddress.setAddressUserName(userName);
			userAddress.setProvinceCode(pCode);
			userAddress.setCityCode(cCode);
			userAddress.setAreaCode(aCode);
			userAddress.setAddressDorp(0);
			int rows = userAddressDao.save(userAddress);
			if (rows != 1) {
				ajaxJson.setMsg("添加失败");
				// ajaxJson.setMsg(PropertyUtil.getProperty("fail", type));
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setMsg("系统异常");
			// ajaxJson.setMsg(PropertyUtil.getProperty("systemInfo", type));
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	@Override
	public AjaxJson updateAddress(Map<String, Object> map, Integer userId) {
		// TODO Auto-generated method stub
		AjaxJson ajaxJson = new AjaxJson();
		// String type = map.get("type").toString();
		try {
			FrontUserDO frontUserDO = frontUserDao.get(userId);
			if (frontUserDO.getUserStatus() == 0) {
				ajaxJson.setSuccess(false);
				ajaxJson.setMsg("对不起！该账号已经被冻结");
				// ajaxJson.setMsg(PropertyUtil.getProperty("frozen", type));
				return ajaxJson;
			}
			System.out.println(map.toString());
			int assetsId = Integer.parseInt(map.get("userAddressId").toString());
			String userPhone = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "userPhone"));
			String userName = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "userName"));
			String province = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "province"));
			String city = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "city"));
			String area = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "area"));
			String datalied = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "datalied"));
			String pCode = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "pCode"));
			String cCode = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "cCode"));
			String aCode = XssHttpServletRequestWrapper.replaceXSS(StringUtil.getMapValue(map, "aCode"));
			if (StringUtils.isEmpty(userPhone) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(province)
					|| StringUtils.isEmpty(city) || StringUtils.isEmpty(area) || StringUtils.isEmpty(datalied)) {
				ajaxJson.setMsg("用户信息地址不能为空");
				// ajaxJson.setMsg(PropertyUtil.getProperty("addressNull", type));
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			boolean flag = VerificationPhone.verificationPhone(userPhone);
			if (!flag) {
				ajaxJson.setMsg("手机格式有误");
				// ajaxJson.setMsg(PropertyUtil.getProperty("phoneError", type));
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
			UserAddressDO userAddress = new UserAddressDO();
			userAddress.setAddressPhone(userPhone);
			userAddress.setUserId(userId);
			userAddress.setAddressArea(area);
			userAddress.setAddressCity(city);
			userAddress.setAddressDetailed(datalied);
			userAddress.setAddressProvince(province);
			userAddress.setAddressState(0);
			userAddress.setAddressUserName(userName);
			userAddress.setProvinceCode(pCode);
			userAddress.setCityCode(cCode);
			userAddress.setAreaCode(aCode);
			userAddress.setAddressDorp(0);
			userAddress.setAddressId(assetsId);
			int rows = userAddressDao.update(userAddress);
			if (rows != 1) {
				ajaxJson.setMsg("添加失败");
				// ajaxJson.setMsg(PropertyUtil.getProperty("fail", type));
				ajaxJson.setSuccess(false);
				return ajaxJson;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			ajaxJson.setMsg("系统异常");
			// ajaxJson.setMsg(PropertyUtil.getProperty("systemInfo", type));
			ajaxJson.setSuccess(false);
			return ajaxJson;
		}
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}

	public int underNumAll(Integer userId) {
		// TODO Auto-generated method stub
		return frontUserDao.underNumAll(userId);
	}

	@Override
	public List<FrontUserDO> findIsActive() {
		// TODO Auto-generated method stub
		return frontUserDao.findIsActive();
	}

	@Override
	public int getZhituiNum(Integer userId) {
		// TODO Auto-generated method stub
		return frontUserDao.getZhituiNum(userId);
	}

	@Override
	public void updateLevel(Map<String, Object> map) {
		frontUserDao.updateLevel(map);

	}

	public int findTeamLevel(Map<String, Object> mapLev) {
		// TODO Auto-generated method stub
		return frontUserDao.findTeamLevel(mapLev);
	}

	@Override
	public List<FrontUserDO> listProfit() {
		// TODO Auto-generated method stub
		return frontUserDao.listProfit();
	}
}
