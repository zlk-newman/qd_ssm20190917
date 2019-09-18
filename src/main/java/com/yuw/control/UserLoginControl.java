package com.yuw.control;

import com.yuw.bean.UserInfoBean;
import com.yuw.service.IUserInfoService;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class UserLoginControl {

    @Resource
    private IUserInfoService userInfoService;

    /**
     * 表单数据和Control层的方法参数之间具有一一映射关系；
     * 要求；前台页面的表单控件的name属性值和Control层方法参数的名字一致即可；
     */
    @RequestMapping("login")
    public String doLogin(String userName, String userPsw) {
        System.out.println("后台登录处理：" + userName + ":" + userPsw);
        // 返回视图路径
        return "logined";
    }

    /*
    如果方法参数名和前台页面的表单控件的name属性值不一样，则需要使用 @RequestParam 注解进行绑定
     */
    @RequestMapping("/login2")
    public String doLogin2(@RequestParam("userName") String userN, String userPsw) {
        System.out.println("后台登录处理：" + userN + ":" + userPsw);
        // 返回视图路径
        return "logined";
    }

    /*
    映射关系：要求实体类的属性名和前台页面的表单控件的name属性值一一对应
    课堂作业：
        如果使用 @RequestParam 对方法参数进行绑定，是否可行？
     */
    @RequestMapping("/login3")
    public String doLogin3(UserInfoBean userInfoB) {
        if (userInfoB != null) {
            System.out.println("后台登录处理：" + userInfoB.getUsername() + ":" + userInfoB.getUserpsw());
        } else {
            System.out.println("后台登录：null");
        }
        // 调用service的接口方法，进行登录的业务逻辑处理
        List<UserInfoBean> lstUserInfos = userInfoService.doLogin(userInfoB);

        // 登录判定
        if (lstUserInfos != null && lstUserInfos.size() > 0) {
            // 登录成功
            System.out.println("登录成功：" + lstUserInfos.get(0).toString());
            // /WEB-INF/pages/logined.jsp
            // 返回视图路径
            return "logined";
        } else {
            // 登录不成功
            System.out.println("登录失败");
            // 返回登录页面
            return "../../index";
        }
    }

    /*
    映射关系：要求实体类的属性名和前台页面的表单控件的name属性值一一对应
    课堂作业：
        如果使用 @RequestParam 对方法参数进行绑定，是否可行？
     */
    @RequestMapping("/login4")
    public ModelAndView doLogin4(UserInfoBean userInfoB) {
        // 返回的视图对象
        ModelAndView mav = new ModelAndView();
        // 设置modelandview要显示的页面名:默认进入登录成功页面
        mav.setViewName("logined");
        if (userInfoB != null) {
            System.out.println("后台登录处理：" + userInfoB.getUsername() + ":" + userInfoB.getUserpsw());
        } else {
            System.out.println("后台登录：null");
        }
        // 调用service的接口方法，进行登录的业务逻辑处理
        List<UserInfoBean> lstUserInfos = userInfoService.doLogin(userInfoB);

        // 登录判定
        if (lstUserInfos != null && lstUserInfos.size() > 0) {
            // 登录成功，缓存当前登录成功的数据
            // 课下作业：ModelAndView的缓存级别对应的是那个？  requestScope
            mav.addObject("loginedUserName", lstUserInfos.get(0).getUsername());
        } else {
            // 登录不成功
            System.out.println("登录失败");
            // 返回登录页面
            //mav.setViewName("../../login");  /// 注意：springmvc进行视图解析时，不会处理 ../ ,而是直接拼接字符串
            mav.setViewName("../../index");
            // 使用字符串方式作为响应函数的返回值
            // 扩展作业：是否还有其他的解决方案？
        }

        // 返回ModelAndView对象
        return mav;
    }


    @RequestMapping("/login5")
    public String doLogin5(UserInfoBean userInfoB, HttpServletRequest request) {
        if (userInfoB != null) {
            System.out.println("后台登录处理：" + userInfoB.getUsername() + ":" + userInfoB.getUserpsw());
        } else {
            System.out.println("后台登录：null");
        }
        // 调用service的接口方法，进行登录的业务逻辑处理
        List<UserInfoBean> lstUserInfos = userInfoService.doLogin(userInfoB);

        // 登录判定
        if (lstUserInfos != null && lstUserInfos.size() > 0) {
            // 登录成功
            System.out.println("登录成功：" + lstUserInfos.get(0).toString());

            // 缓存登录成功之后数据到session中
            request.getSession().setAttribute("loginedUserName", lstUserInfos.get(0).getUsername());
            // /WEB-INF/pages/logined.jsp
            // 返回视图路径
            return "logined";
        } else {
            // 登录不成功
            System.out.println("登录失败");
            // 返回登录页面
            return "../../index";
        }
    }
    /**
     * 保存和修改用于信息
     * @param userInfoBean 用户参数实体类
     * @return 结果视图
     */
    @RequestMapping("/saveOrUpdateUserinfo")
    public  String  saveOrUpdateUserInfo(UserInfoBean userInfoBean){

        // 调用Service层进行用户信息数据保存和修改操作
        int i = userInfoService.saveOrUpdateUserInfo(userInfoBean);
        // 根据操作结果进行视图转发处理
        return "userInfoManager/userInfoAddDetail";
    }


}
