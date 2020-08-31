package com.renegade.controller;

import com.renegade.annotations.RenegadeAuth;
import com.renegade.config.AjaxJson;
import com.renegade.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/m9/order")
public class M9OrderController {

    @Autowired
    private M9OrderService orderService;

    @Autowired
    private M9UserService userService;

    @Autowired
    private ParamService paramService;

    private VerifyRecordService verifyRecordService;

    @Autowired
    private RedissionDelayService delayService;

    /***
     * 底部订单菜单
     *
     * @return
     */
    @GetMapping("/Order")
    @RenegadeAuth
    public String Order(HttpServletRequest requests, Model model) throws Exception{
        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();

        String zone = requests.getParameter("zone");
        if("40".equals(zone)){

            Map<String,Object> userMap4 = new HashMap<>();
            userMap4.put("user_id",userId);
            userMap4.put("quantity","40");
            userMap4.put("state","1,3,4,5");
            List<Map<String,Object>> saleList4 = orderService.getUserOrderList(userMap4);

            Map<String,Object> buyerMap40 = new HashMap<>();
            buyerMap40.put("buyer",userId);
            buyerMap40.put("quantity","40");
            buyerMap40.put("state","1,3,4,5");
            List<Map<String,Object>> buyerList4 = orderService.getUserOrderList(buyerMap40);

            model.addAttribute("userList",saleList4);
            model.addAttribute("buyerList",buyerList4);
            model.addAttribute("zone40",true);

        }else{
            Map<String,Object> userMap4 = new HashMap<>();
            userMap4.put("user_id",userId);
            userMap4.put("quantity","4");
            userMap4.put("state","1,3,4,5");
            List<Map<String,Object>> saleList4 = orderService.getUserOrderList(userMap4);

            Map<String,Object> buyerMap40 = new HashMap<>();
            buyerMap40.put("buyer",userId);
            buyerMap40.put("quantity","4");
            buyerMap40.put("state","1,3,4,5");
            List<Map<String,Object>> buyerList4 = orderService.getUserOrderList(buyerMap40);

            model.addAttribute("userList",saleList4);
            model.addAttribute("buyerList",buyerList4);
            model.addAttribute("zone4",true);
        }
        return "/Order";
    }

    @GetMapping("/Order_detail2")
    @RenegadeAuth
    public String Order_detail2(HttpServletRequest requests,Model model) throws Exception{

        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();
        String id = requests.getParameter("id");

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        List<Map<String, Object>> list = orderService.getUserOrderList(map);
        Map<String, Object> ordermap = list.get(0);
        model.addAttribute("order",ordermap);

        String buyer = String.valueOf(ordermap.get("buyer"));

        Map<String,Object> user_map = new HashMap<>();
        user_map.put("id",buyer);
        List<Map<String, Object>> userList = userService.selectUserList(user_map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();
        model.addAttribute("buyer",usermap);

        Map<String,Object> my_map = new HashMap<>();
        my_map.put("id",userId);
        List<Map<String, Object>> myList = userService.selectUserList(my_map);
        Map<String,Object> mymap = myList.size()>0?myList.get(0):new HashMap<>();
        model.addAttribute("sale",mymap);

        String[] args = orderService.getWarnAndEndTimeParam();

        model.addAttribute("cancelTime",args[0]);
        model.addAttribute("warn_count_saletota",args[1]);

        return "Order_detail2";
    }

    /***
     * 别人发布的求购单，我卖给他,确认收款,卖方-待放行
     * @param requests
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/sale_order")
    @RenegadeAuth
    public String sale_order(HttpServletRequest requests,Model model) throws Exception{
        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();
        String id = requests.getParameter("id");

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        List<Map<String, Object>> list = orderService.getUserOrderList(map);
        Map<String, Object> ordermap = list.get(0);
        model.addAttribute("order",ordermap);

        /***
         * 得到求购方的用户信息,order.user_id
         * **/
        Map<String,Object> user_map = new HashMap<>();
        user_map.put("id",ordermap.get("user_id"));
        List<Map<String, Object>> userList = userService.selectUserList(user_map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();
        model.addAttribute("user",usermap);

        String[] args = orderService.getWarnAndEndTimeParam();
        model.addAttribute("confirmSysem",args[3]);
        model.addAttribute("warn_count_confirm",args[2]);

        return "sale_order";
    }


    /***
     * 别人发布的求购单，我卖给他,待确认上传凭证,卖方-待上传凭证
     * @param requests
     * @param model
     * @return
     */
    @GetMapping("/sale_order1")
    @RenegadeAuth
    public String sale_order1(HttpServletRequest requests,Model model) throws Exception{
        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();
        String id = requests.getParameter("id");

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        List<Map<String, Object>> list = orderService.getUserOrderList(map);
        Map<String, Object> ordermap = list.get(0);
        model.addAttribute("order",ordermap);

        /***
         * 得到求购方的用户信息,order.user_id
         * **/
        Map<String,Object> user_map = new HashMap<>();
        user_map.put("id",ordermap.get("user_id"));
        List<Map<String, Object>> userList = userService.selectUserList(user_map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();
        model.addAttribute("user",usermap);

        String[] args = orderService.getWarnAndEndTimeParam();

        model.addAttribute("cancelTime",args[0]);
        model.addAttribute("warn_count_saletota",args[1]);

        return "sale_order1";
    }


    /***
     *别人发布的求购单，我卖给他,完成的交易,卖方-已完成
     * @param requests
     * @param model
     * @return
     */
    @GetMapping("/sale_order2")
    @RenegadeAuth
    public String sale_order2(HttpServletRequest requests,Model model) throws Exception{
        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();
        String id = requests.getParameter("id");

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        List<Map<String, Object>> list = orderService.getUserOrderList(map);
        Map<String, Object> ordermap = list.get(0);
        model.addAttribute("order",ordermap);

        /***
         * 得到求购方的用户信息,order.user_id
         * **/
        Map<String,Object> user_map = new HashMap<>();
        user_map.put("id",ordermap.get("user_id"));
        List<Map<String, Object>> userList = userService.selectUserList(user_map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();
        model.addAttribute("user",usermap);
        return "sale_order2";
    }

    /***
     *别人发布的求购单，我卖给他,完成的交易,卖方-已完成
     * @param requests
     * @param model
     * @return
     */
    @GetMapping("/Purchase")
    @RenegadeAuth
    public String Purchase(HttpServletRequest requests,Model model) throws Exception{
        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();
        String id = requests.getParameter("id");

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        List<Map<String, Object>> list = orderService.getUserOrderList(map);
        Map<String, Object> ordermap = list.get(0);
        model.addAttribute("order",ordermap);

        /***
         * 得到求购方的用户信息,order.user_id
         * **/
        Map<String,Object> user_map = new HashMap<>();
        user_map.put("id",ordermap.get("user_id"));
        List<Map<String, Object>> userList = userService.selectUserList(user_map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();
        model.addAttribute("user",usermap);
        return "Purchase";
    }

    @PostMapping("/uploadFile")
    @RenegadeAuth
    @ResponseBody
    public AjaxJson uploadFile(HttpServletRequest requests,@RequestParam("browerfile") MultipartFile browerfile, @RequestParam Map<String,Object> map) throws Exception{
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("上传成功");

        HttpSession session = requests.getSession();
        if (session.getAttribute("userId") == null) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("登录超时,请重新登陆");
            return ajaxJson;
        }

        return orderService.uploadFile("",browerfile,map);
    }


    @GetMapping("/Order_details")
    @RenegadeAuth
    public String Order_details(HttpServletRequest requests,Model model) throws Exception{

        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();
        String id = requests.getParameter("id");

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("user_id",userId);
        List<Map<String, Object>> list = orderService.getUserOrderList(map);
        Map<String, Object> ordermap = list.get(0);
        model.addAttribute("order",ordermap);

        String buyer = String.valueOf(ordermap.get("buyer"));

        Map<String,Object> user_map = new HashMap<>();
        user_map.put("id",buyer);
        List<Map<String, Object>> userList = userService.selectUserList(user_map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();
        model.addAttribute("buyer",usermap);

        Map<String,Object> my_map = new HashMap<>();
        my_map.put("id",userId);
        List<Map<String, Object>> myList = userService.selectUserList(my_map);
        Map<String,Object> mymap = myList.size()>0?myList.get(0):new HashMap<>();
        model.addAttribute("sale",mymap);

        String[] args = orderService.getWarnAndEndTimeParam();

        model.addAttribute("warn_count_saletota",args[1]);

        model.addAttribute("cancelTime",args[0]);

        return "Order_details";
    }


    @GetMapping("/Order_details3")
    @RenegadeAuth
    public String Order_details3(HttpServletRequest requests,Model model) throws Exception{

        HttpSession session = requests.getSession();
        System.out.println("==="+session);
        if (session.getAttribute("userId") == null) {
            return "/login";
        }

        String userId = session.getAttribute("userId").toString();
        String id = requests.getParameter("id");

        Map<String,Object> map = new HashMap<>();
        map.put("id",id);
        map.put("user_id",userId);
        List<Map<String, Object>> list = orderService.getUserOrderList(map);
        Map<String, Object> ordermap = list.get(0);
        model.addAttribute("order",ordermap);

        String buyer = String.valueOf(ordermap.get("buyer"));

        Map<String,Object> user_map = new HashMap<>();
        user_map.put("id",buyer);
        List<Map<String, Object>> userList = userService.selectUserList(user_map);
        Map<String,Object> usermap = userList.size()>0?userList.get(0):new HashMap<>();
        model.addAttribute("buyer",usermap);

        Map<String,Object> my_map = new HashMap<>();
        my_map.put("id",userId);
        List<Map<String, Object>> myList = userService.selectUserList(my_map);
        Map<String,Object> mymap = myList.size()>0?myList.get(0):new HashMap<>();
        model.addAttribute("sale",mymap);

        return "Order_details3";
    }

    @PostMapping("/selectOrderAll")
    @RenegadeAuth
    @ResponseBody
    public AjaxJson selectOrderAll(HttpServletRequest requests,@RequestParam Map<String,Object> map) throws Exception{
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("获取交易数据成功");

        HttpSession session = requests.getSession();

        if (session.getAttribute("userId") == null) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("登录超时,请重新登陆");
            return ajaxJson;
        }

        String userId = session.getAttribute("userId").toString();

        map.put("user_id",userId);

        List<Map<String, Object>> list = orderService.selectOrderAll(map);

        ajaxJson.setData(list);

        return ajaxJson;
    }

    /**
     * 撤销订单
     * @param requests
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("/transfer_undo")
    @RenegadeAuth
    @ResponseBody
    public AjaxJson transfer_undo(HttpServletRequest requests,@RequestParam Map<String,Object> map) throws Exception{
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");
        HttpSession session = requests.getSession();

        if (session.getAttribute("userId") == null) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("登录超时,请重新登陆");
            return ajaxJson;
        }

        String userId = session.getAttribute("userId").toString();

        return orderService.transfer_undo(userId,map);
    }

    @PostMapping("/transfer_confirm")
    @RenegadeAuth
    @ResponseBody
    public AjaxJson transfer_confirm(HttpServletRequest requests,@RequestParam Map<String,Object> map) throws Exception{
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");

        HttpSession session = requests.getSession();

        if (session.getAttribute("userId") == null) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("登录超时,请重新登陆");
            return ajaxJson;
        }

        String userId = session.getAttribute("userId").toString();

        return orderService.transfer_confirm(userId,map);

    }

    /***
     *发布求购订单
     * @param requests
     * @param map
     * @return
     */
    @PostMapping("/transfer_buy")
    @RenegadeAuth
    @ResponseBody
    @Transactional
    public AjaxJson transfer_buy(HttpServletRequest requests,@RequestParam Map<String,Object> map) throws Exception{
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");

        HttpSession session = requests.getSession();

        if (session.getAttribute("userId") == null) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("登录超时,请重新登陆");
            return ajaxJson;
        }

        String userId = session.getAttribute("userId").toString();

        return orderService.transfer_buy(userId,map);

    }

    /***
     * 卖给ta，交易大厅中卖给ta操作
     * @param requests
     * @param map
     * @return
     * @throws Exception
     */
    @PostMapping("/transfer")
    @RenegadeAuth
    @ResponseBody
    public AjaxJson transfer(HttpServletRequest requests,@RequestParam Map<String,Object> map) throws Exception{
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setMsg("交易已处理");

        HttpSession session = requests.getSession();

        if (session.getAttribute("userId") == null) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("登录超时,请重新登陆");
            return ajaxJson;
        }
        String userId = session.getAttribute("userId").toString();

        return orderService.transfer(userId,map);
    }



}
