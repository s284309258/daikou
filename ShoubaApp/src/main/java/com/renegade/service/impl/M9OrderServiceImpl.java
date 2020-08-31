package com.renegade.service.impl;

import com.renegade.config.AjaxJson;
import com.renegade.config.PhoneSendCode;
import com.renegade.config.RedissionDSHOrder;
import com.renegade.dao.M9OrderMapper;
import com.renegade.domain.M9UserAndOrderDo;
import com.renegade.domain.ParamDO;
import com.renegade.service.M9OrderService;
import com.renegade.service.M9UserService;
import com.renegade.service.ParamService;
import com.renegade.service.RedissionDelayService;
import com.renegade.util.DateUtil;
import com.renegade.util.common.FileUpload;
import com.renegade.util.common.MapConvertUtil;
import com.renegade.util.common.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class M9OrderServiceImpl implements M9OrderService {

    private final static Logger logger= LoggerFactory.getLogger(M9OrderServiceImpl.class);

    @Autowired
    private M9OrderMapper orderMapper;

    @Autowired
    private M9UserService userService;

    @Autowired
    private ParamService paramService;

    @Autowired
    private RedissionDelayService delayService;

    @Override
    public List<M9UserAndOrderDo> getUserOrderListObj(Map<String, Object> params) {
        List<M9UserAndOrderDo> list = orderMapper.getUserOrderListObj(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> getUserOrderList(Map<String, Object> params) {
        List<Map<String,Object>> list = orderMapper.getUserOrderList(params);
        return list;
    }

    @Override
    public int updateOrder(Map<String, Object> params) {
        int cnt = orderMapper.updateOrder(params);
        return cnt;
    }

    @Override
    public List<Map<String, Object>> selectOrderAll(Map<String, Object> params) {
        List<Map<String, Object>> list = orderMapper.selectOrderAll(params);
        return list;
    }

    @Override
    public List<Map<String, Object>> selectOrder(Map<String, Object> params) {
        List<Map<String,Object>> list = orderMapper.selectOrder(params);
        return list;
    }

    @Override
    public int insertOrder(Map<String, Object> params) {
        orderMapper.insertOrder(params);
        return 0;
    }

    /***
     * 已上传打款凭证,进行交易确认放行
     * @param userId
     * @param map
     * @return
     */
    @Override
    @Transactional
    public AjaxJson transfer_confirm(String userId,Map<String, Object> map){
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");

        List<Map<String,Object>> user_List = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{"id:"+userId}));
        Map<String,Object> usermap = user_List.get(0);

        List<Map<String, Object>> order_list = orderMapper.selectOrder(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+map.get("id")
        }));
        Map<String,Object> ordermap = order_list.get(0);

        if("2".equals(usermap.get("user_state"))){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("账号已被封,请先解封账号");
            return ajaxJson;
        }
        //1.订单求购方(下单的那个人)
        String user_id = String.valueOf(ordermap.get("user_id"));
        List<Map<String, Object>> order_user_list = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+user_id
        }));
        Map<String, Object> order_user_map = order_user_list.get(0);
        //2.求购人的上级ID
        String senior = order_user_map.get("user_pid").toString();
        //3.买家人
        String buyer = ordermap.get("buyer").toString();

        ParamDO param4 = paramService.findValue("m9_lineup_coin_free4");
        ParamDO param40 = paramService.findValue("m9_lineup_coin_free40");
        String coin_free4 = param4.getParamValue();
        String coin_free40 = param40.getParamValue();
        int cnt = 0;
        if(Double.parseDouble(map.get("quantity").toString())==4){
            cnt = updateOrder(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+map.get("id"),
                    "state:3",
                    "state1:4"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
            //求购人上级得到赠送排单币
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+senior,
                    "user_lineup_coin_free:user_lineup_coin_free+"+coin_free4,
                    "order_type:3",
                    "upd_date:now()"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
            //求购人上级得到一个红灯
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+senior,
                    "light4_red:light4_red+1",
                    "order_id:"+order_user_map.get("telphone"),
                    "order_type:6661",
                    "source_id:4",
                    "user_lineup_coin_free:user_lineup_coin_free+"+coin_free4,
                    "upd_date:now()"
            }));

            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }

            //求购人得到一个蓝灯
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+user_id,
                    "light4_blue:light4_blue+1",
                    "order_id:"+order_user_map.get("telphone"),
                    "order_type:6662",
                    "source_id:4",
                    "upd_date:now()"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }

            //买家减少一个绿灯
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+buyer,
                    "light4_green:light4_green-1",
                    "order_id:"+order_user_map.get("telphone"),
                    "order_type:6663",
                    "source_id:4",
                    "upd_date:now()"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
        }else if(Double.parseDouble(map.get("quantity").toString())==40){
            cnt = updateOrder(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+map.get("id"),
                    "state:3",
                    "state1:4"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
            //求购人上级得到赠送排单币
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+senior,
                    "order_type:3",
                    "user_lineup_coin_free:user_lineup_coin_free+"+coin_free40,
                    "upd_date:now()"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
            //求购人上级得到一个红灯
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+senior,
                    "light40_red:light40_red+1",
                    "order_id:"+order_user_map.get("telphone"),
                    "order_type:6661",
                    "source_id:40",
                    "upd_date:now()"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }

            //求购人得到一个蓝灯
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+user_id,
                    "light40_blue:light40_blue+1",
                    "order_id:"+order_user_map.get("telphone"),
                    "order_type:6662",
                    "source_id:40",
                    "upd_date:now()"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
            //买家减少一个绿灯
            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+buyer,
                    "light40_green:light40_green-1",
                    "order_id:"+order_user_map.get("telphone"),
                    "order_type:6663",
                    "source_id:40",
                    "upd_date:now()"
            }));
            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setMsg("交易失败！");
                ajaxJson.setSuccess(false);
                return ajaxJson;
            }
        }
        if(cnt!=1){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            ajaxJson.setMsg("交易失败！");
            ajaxJson.setSuccess(false);
            return ajaxJson;
        }
        //更新用户为已报单用户
        userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+user_id,
                "user_limit:1"
        }));
        return ajaxJson;
    }

    /***
     * 交易撤回
     * @param userId
     * @param params 1.id=m9_order.id
     * @return
     */
    @Override
    public AjaxJson transfer_undo(String userId, Map<String, Object> params) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");

        List<Map<String, Object>> list = this.selectOrder(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+params.get("id")
        }));

        Map<String, Object> order_map = list.get(0);

        this.updateOrder(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+params.get("id"),
                "state:2",
                "state1:1",
                "upd_date:"+ DateUtil.get_format2_today()
        }));

        userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+userId,
                "user_lineup_coin:user_lineup_coin+"+order_map.get("use_lineup_coin"),
                "user_lineup_coin_free:user_lineup_coin_free+"+order_map.get("use_lineup_coin_free"),
                "order_type:36",
                "upd_date:now()"
        }));

        return ajaxJson;
    }

    /***
     * 卖给ta，交易大厅中卖给ta操作
     * @param userId
     * @param map
     * @return
     */
    @Override
    @Transactional
    public AjaxJson transfer(String userId,Map<String,Object> map){
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");

        Map<String,Object> user_Map = new HashMap<>();
        user_Map.put("id",userId);
        List<Map<String, Object>> userList = userService.selectUserList(user_Map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();


        Map<String,Object> check_map = new HashMap<>();
        check_map.put("id",userId);
        check_map.put("pay_pwd",map.get("pay_pwd"));
        check_map.put("telphone",usermap.get("telphone"));
        check_map.put("code",map.get("code"));
        check_map.put("bus_type",map.get("bus_type"));
        AjaxJson checkAjax = userService.checkSmsCodeAndPayPwd(check_map);
        if(!checkAjax.isSuccess()){
            return checkAjax;
        }

        if ("2".equals(usermap.get("user_state"))) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("账号已封号,请联系客服");
            return ajaxJson;
        }

        if(usermap.get("eos_address")==null || "".equals(usermap.get("eos_address"))){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("请先完善EOS地址信息");
            return ajaxJson;
        }

        Map<String,Object> select_map = new HashMap<>();
        select_map.put("id",map.get("id"));
        List<Map<String,Object>> orderList = this.selectOrder(select_map);
        Map<String,Object> checkOrderMap = orderList.get(0);
        String state = String.valueOf(checkOrderMap.get("state"));
        if(!"1".equals(state)){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("该订单已被其他玩家锁定");
            return ajaxJson;
        }

        String quantity = String.valueOf(map.get("quantity"));
        if("4".equals(quantity)){
            Integer green_light4 = Integer.parseInt(usermap.get("light4_green").toString());
            if(green_light4<1){
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("绿灯不足,无法卖出");
                return ajaxJson;
            }
        }else if("40".equals(quantity)){
            Integer green_light40 = Integer.parseInt(usermap.get("light40_green").toString());
            if(green_light40<1){
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("绿灯不足,无法卖出");
                return ajaxJson;
            }
        }else{
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("订单异常");
            return ajaxJson;
        }

        List<Map<String, Object>> list =userService.getUserOrderList(MapConvertUtil.StringArrayToMap(new String[]{
                "buyer:"+userId,"state:4"})
        );

        if(list.size()>0){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("有尚未处理的订单,请先处理该订单");
            return ajaxJson;
        }

        String pay_timeout = paramService.findValue("confirmSysem").getParamValue();
        if(pay_timeout==null || "".equals(pay_timeout)){
            pay_timeout = checkOrderMap.get("pay_timeout").toString();
        }

        Map<String,Object> order_map = new HashMap<>();
        order_map.put("id",map.get("id"));
        order_map.put("buyer",userId);
        order_map.put("state","5");
        order_map.put("eos_address",usermap.get("eos_address"));
        order_map.put("eos_address_label",usermap.get("eos_address_label"));
        order_map.put("pay_date","ADDDATE(NOW(), INTERVAL "+pay_timeout+" SECOND)");
        int cnt = this.updateOrder(order_map);
        if(cnt<1){
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("交易失败");
        }


        //添加倒计时,违规次数达到则封号begin
        ParamDO paramDO = paramService.findValue("cancelTime");

        Long timeout = Long.parseLong(paramDO.getParamValue());

        RedissionDSHOrder order=new RedissionDSHOrder("saletota-"+map.get("id"),timeout);
        delayService.add(order);
        //添加倒计时,违规次数达到则封号end



        //发送短信提醒对方上传凭证begin
        String user_id = checkOrderMap.get("user_id").toString();

        Map<String,Object> saleMap = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+user_id
        })).get(0);

        PhoneSendCode.sendCodeParams(saleMap.get("telphone").toString(),"123123","199240");
        //发送短信提醒对方上传凭证end
        return ajaxJson;
    }

    /***
     * 上传打款凭证
     * @param userId
     * @param map
     * @return
     */
    @Transactional
    public AjaxJson uploadFile(String userId, MultipartFile browerfile, Map<String,Object> map){
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("上传成功");

        if (browerfile.isEmpty()) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("图片不能为空");
            return ajaxJson;
        }

        AjaxJson data1 = FileUpload.uploadImg(browerfile);

        //上传图片成功
        if(data1.isSuccess()){
            map.put("eos_poto",data1.getData());
        }else{
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("上传图片失败");
            return ajaxJson;
        }
        List<Map<String,Object>> orderList = this.selectOrder(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+map.get("id")
        }));
        Map<String,Object> order_map = orderList.get(0);

        String confirm_timeout = paramService.findValue("confirmSysem").getParamValue();
        if(confirm_timeout==null || "".equals(confirm_timeout)){
            confirm_timeout = order_map.get("confirm_timeout").toString();
        }

        map.put("id",map.get("id"));
        map.put("state","4");
        map.put("state1","5");
        map.put("confirm_date","ADDDATE(NOW(), INTERVAL "+confirm_timeout+" SECOND)");
        int cnt = this.updateOrder(map);

        if(cnt<1){
            ajaxJson.setSuccess(true);
            ajaxJson.setMsg("上传图片失败");
            return ajaxJson;
        }

        //添加倒计时,违规次数达到则封号begin
        ParamDO paramDO = paramService.findValue("cancelTime");

        Long timeout = Long.parseLong(paramDO.getParamValue());

        RedissionDSHOrder order=new RedissionDSHOrder("confirm-"+map.get("id"),timeout);
        delayService.add(order);
        //添加倒计时,违规次数达到则封号end


        //发送短信提醒卖家放行begin
        List<Map<String, Object>> user_list =this.selectOrder(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+map.get("id")
        }));
        Map<String, Object> user_map = user_list.get(0);

        List<Map<String, Object>> buyer_list = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{
                "id:"+user_map.get("buyer")
        }));

        Map<String, Object> buyer_map = buyer_list.get(0);

        String telphone = String.valueOf(buyer_map.get("telphone"));

        PhoneSendCode.sendCodeParams(telphone,"123123","190426");
        //发送短信提醒卖家放行end
        return ajaxJson;
    }

    /***
     * 排单入场
     * @param map
     * @return
     */
    public AjaxJson transfer_buy(String userId,Map<String,Object> map) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");

        try {

            ParamDO param40 = paramService.findValue("m9_lineup_count40");
            ParamDO param4 = paramService.findValue("m9_lineup_count4");
            ParamDO percent_param = paramService.findValue("m9_lineup_coin_percent");

            ParamDO param_active = paramService.findValue("user_first_active");
            String active = param_active.getParamValue();

            String quantity = String.valueOf(map.get("quantity"));

            AjaxJson checkAjax = userService.checkPayPwd(MapConvertUtil.StringArrayToMap(new String[]{"id:"+userId,"pay_pwd:"+map.get("pay_pwd")}));

            if(!checkAjax.isSuccess()){
                return checkAjax;
            }

            List<Map<String,Object>> user_map = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{"id:"+userId}));
            Map<String,Object> usermap = user_map.get(0);

            if("2".equals(usermap.get("user_state"))){
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("账号已被封,请先解封账号");
                return ajaxJson;
            }

            double active_coin = Double.parseDouble(active);
            double coin = Double.parseDouble(usermap.get("user_lineup_coin").toString());
            double coin_free = Double.parseDouble(usermap.get("user_lineup_coin_free").toString());
            double percent = Double.parseDouble(percent_param.getParamValue());
            double use_coin_free = 0;
            double pay_coin = 0;
            if("0".equals(usermap.get("user_active").toString())){
                if(coin<active_coin){
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("余额不足,激活账户需要"+active_coin+"个排单币");
                    return ajaxJson;
                }

                int cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                        "id:"+userId,
                        "user_active:1",
                        "user_lineup_coin:user_lineup_coin-"+active_coin,
                        "order_type:3",
                        "upd_date:now()"
                }));

                if(cnt!=1){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("账户激活失败");
                    return ajaxJson;
                }
                user_map = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{"id:"+userId}));
                usermap = user_map.get(0);
                coin = Double.parseDouble(usermap.get("user_lineup_coin").toString());
                coin_free = Double.parseDouble(usermap.get("user_lineup_coin_free").toString());
                percent = Double.parseDouble(percent_param.getParamValue());

            }

            if("4".equals(map.get("quantity").toString())){
                double cnt4 = Double.parseDouble(param4.getParamValue());
                double actual_pay_free = cnt4*percent;
                if(actual_pay_free>coin_free){
                    use_coin_free=coin_free;
                }else{
                    use_coin_free=actual_pay_free;
                }
                pay_coin = cnt4 - use_coin_free;
                if(pay_coin>coin){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("排单币不足,请充值排单币");
                    return ajaxJson;
                }
            }

            if("40".equals(map.get("quantity").toString())){
                int cnt40 = Integer.parseInt(param40.getParamValue());
                double actual_pay_free = cnt40*percent;
                if(actual_pay_free>coin_free){
                    use_coin_free=coin_free;
                }else{
                    use_coin_free=actual_pay_free;
                }
                pay_coin = cnt40 - use_coin_free;
                if(pay_coin>coin){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("排单币不足,请充值排单币");
                    return ajaxJson;
                }
            }

            if(usermap.get("eos_address")==null || "".equals(usermap.get("eos_address"))){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("请先完善EOS地址信息");
                return ajaxJson;
            }

            if("4".equals(quantity)){
                Integer green_light4 = Integer.parseInt(usermap.get("light4_green").toString());
                if(green_light4>0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("有未使用完的绿灯,请先使用绿灯");
                    return ajaxJson;
                }
            }else if("40".equals(quantity)){
                Integer green_light40 = Integer.parseInt(usermap.get("light40_green").toString());
                if(green_light40>0){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("有未使用完的绿灯,请先使用绿灯");
                    return ajaxJson;
                }
            }else{
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("订单异常");
                return ajaxJson;
            }

            List<Map<String,Object>> state1_map = userService.getUserOrderList(MapConvertUtil.StringArrayToMap(new String[]{
                    "user_id:"+userId,"state:1"
            }));
            if(state1_map.size()>0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("有求购中的订单,请先处理该订单");
                return ajaxJson;
            }

            List<Map<String,Object>> state5_map = userService.getUserOrderList(MapConvertUtil.StringArrayToMap(new String[]{
                    "user_id:"+userId,"state:5"
            }));
            if(state5_map.size()>0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("有待上传凭证的订单,请先处理该订单");
                return ajaxJson;
            }


            Map<String,Object> insert_map = new HashMap<>();

            insert_map.put("order_id", StringUtil.getDateTimeAndRandomForID());
            insert_map.put("quantity",quantity);
            insert_map.put("state","1");
            insert_map.put("user_id",userId);
            if("4".equals(quantity)){
                insert_map.put("use_lineup_coin",pay_coin);
                insert_map.put("use_lineup_coin_free",use_coin_free);
            }else if("40".equals(quantity)){
                insert_map.put("use_lineup_coin",pay_coin);
                insert_map.put("use_lineup_coin_free",use_coin_free);
            }

            insert_map.put("remark","入场排单");

            int cnt = this.insertOrder(insert_map);

            if("4".equals(quantity)){
                cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                        "id:"+userId,
                        "user_lineup_coin:"+"user_lineup_coin-"+pay_coin,
                        "user_lineup_coin_free:user_lineup_coin_free-"+use_coin_free,
                        "order_type:1",
                        "upd_date:now()"
                }));
            }else if("40".equals(quantity)){
                cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                        "id:"+userId,
                        "user_lineup_coin:"+"user_lineup_coin-"+pay_coin,
                        "user_lineup_coin_free:user_lineup_coin_free-"+use_coin_free,
                        "order_type:1",
                        "upd_date:now()"
                }));
            }
            userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+userId,
                    "user_active:1"
            }));

            if(cnt!=1){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("处理异常");
                return ajaxJson;
            }
        }catch(Exception e){

        }

        return ajaxJson;
    }


    /***
     * 1.cancelTime
     * 2.warn_count_saletota
     * 3.warn_count_confirm
     * @return cancelTime,warn_count_saletota,warn_count_confirm
     */
    @Override
    public String[] getWarnAndEndTimeParam(){
        String[] args = new String[4];
        ParamDO param = paramService.findValue("cancelTime");
        String cancelTime = param.getParamValue();
        Integer cancel = Integer.parseInt(cancelTime)/3600;
        args[0]=String.valueOf(cancel);

//        model.addAttribute("cancelTime",cancel);

        ParamDO param1 = paramService.findValue("warn_count_saletota");
        String warn_count_saletota = param1.getParamValue();
        args[1] = String.valueOf(warn_count_saletota);

        ParamDO param2 = paramService.findValue("warn_count_confirm");
        String warn_count_confirm = param2.getParamValue();
        args[2] = String.valueOf(warn_count_confirm);

        ParamDO param3 = paramService.findValue("confirmSysem");
        String confirmSysem = param3.getParamValue();
        Integer confirm = Integer.parseInt(confirmSysem)/3600;
        args[3] = String.valueOf(confirm);

        return args;
    }


    /***
     *后台到期任务处理
     * @param param
     */
    @Transactional
    public void taskHandle(String param){
        try
        {
            logger.debug("===>>>>>>>>>>>>延时队列到期");
            Integer warn_count_saletota_sys = Integer.parseInt(paramService.findValue("warn_count_saletota").getParamValue());
            logger.debug("===>>>>>>>>>>>>延时队列到期{}",warn_count_saletota_sys);
//        String orderId = order.getOrderId();
            String pre = param.split("-")[0];
            String oid = param.split("-")[1];
            logger.debug("====================>>>>>>>>>>>>>>>>>>写下业务逻辑");
            List<Map<String, Object>> list = this.selectOrder(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+oid
            }));

            Map<String,Object> order_map = list.get(0);
            String user_id = order_map.get("user_id").toString();
            String buyer = order_map.get("buyer").toString();

            List<Map<String, Object>> userList = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+user_id
            }));
            Map<String,Object> user_map = userList.get(0);

            List<Map<String, Object>> buyerList = userService.selectUserList(MapConvertUtil.StringArrayToMap(new String[]{
                    "id:"+buyer
            }));
            Map<String,Object> buyer_map = buyerList.get(0);

            Integer warn_count_confirm_sys = Integer.parseInt(paramService.findValue("warn_count_confirm").getParamValue());
            if("saletota".equals(pre)){
                int cnt=0;
//                List<Map<String, Object>> obj_list =orderMapper.selectOrder(MapConvertUtil.StringArrayToMap(new String[]{
//                        "id:"+oid,
//                        "state:5"
//                }));
//                if(obj_list.size()>0){
                    //更新状态为求购中begin
                    cnt = this.updateOrder(MapConvertUtil.StringArrayToMap(new String[]{
                            "id:"+oid,
                            "state:1",
                            "state1:5",
                            "eos_address:0",
                            "buyer:0",
                            "remark:未上传打款凭证,系统自动挂单"
                    }));
                    //更新状态为求购中end
                    if(cnt==1){
                        //累计未上传打款凭证警告次数,达到则封号begin
                        Integer warn_count_saletota = Integer.parseInt(user_map.get("warn_count_saletota").toString());
                        if(warn_count_saletota>=warn_count_saletota_sys){
                            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                                    "id:"+user_id,
                                    "user_state:2"
                            }));
                        }else{
                            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                                    "id:"+user_id,
                                    "warn_count_saletota:warn_count_saletota+1"
                            }));
                        }
                        if(cnt!=1){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        //累计未上传打款凭证警告次数,达到则封号end
                    }
//                }
            }

            //累计未确认放行警告次数,达到则封号 begin
            if("confirm".equals(pre)){
                int cnt = 0;

//                List<Map<String, Object>> obj_list =orderMapper.selectOrder(MapConvertUtil.StringArrayToMap(new String[]{
//                        "id:"+oid,
//                        "state:4"
//                }));

//                if(obj_list.size()>0){
                    //更新状态为已放行begin
                    AjaxJson ajaxJson = this.transfer_confirm(buyer,MapConvertUtil.StringArrayToMap(new String[]{
                            "id:"+oid,
                            "quantity:"+order_map.get("quantity"),
                            "remark:未确认放行,系统自动放行"
                    }));
                    //更新状态为已放行end

                    if(ajaxJson.isSuccess()){
                        //累计未确认放行警告次数,达到则封号begin
                        Integer warn_count_confirm = Integer.parseInt(buyer_map.get("warn_count_confirm").toString());
                        if(warn_count_confirm>=warn_count_confirm_sys){
                            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                                    "id:"+buyer,
                                    "user_state:2"
                            }));
                        }else{
                            cnt = userService.updateUser(MapConvertUtil.StringArrayToMap(new String[]{
                                    "id:"+buyer,
                                    "warn_count_confirm:warn_count_confirm+1"
                            }));
                        }

                        if(cnt!=1){
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                        //累计未上传打款凭证警告次数,达到则封号end
                    }
//                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
