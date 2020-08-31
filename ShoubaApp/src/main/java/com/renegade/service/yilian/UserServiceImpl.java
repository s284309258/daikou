package com.renegade.service.yilian;


import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.renegade.dao.yilian.UserMapper;
import com.renegade.dao.yilian.UserRoleMapper;
import com.renegade.domain.yilian.dto.domain.User;
import com.renegade.domain.yilian.dto.domain.UserPost;
import com.renegade.domain.yilian.dto.domain.UserRole;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class UserServiceImpl
{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    /**
     * 新增保存用户信息
     * 
     * @param user 用户信息
     * @return 结果
     */
    @Transactional
    public int insertUser(User user)
    {
        user.randomSalt();//随机盐
        user.setPassword(encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));//密码加盐加密
        user.setCreateBy("admin");//创建人
        // （1）新增用户信息
        int rows = userMapper.insertUser(user);
        // （2）新增用户岗位关联
        insertUserPost(user);
        // （3）新增用户与角色管理
        insertUserRole(user);
        return user.getUserId().intValue();
    }

    /**
     * 密码用户名加盐加密
     * @param username
     * @param password
     * @param salt
     * @return
     */
    public String encryptPassword(String username, String password, String salt)
    {
        return new Md5Hash(username + password + salt).toHex().toString();
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(User user)
    {
        //岗位组
        Long[] posts = user.getPostIds();
        //岗位组非空
        if (posts!=null)
        {
            // 新增用户与岗位管理
            List<UserPost> list = new ArrayList<UserPost>();
            //循环遍历岗位组关系
            for (Long postId : user.getPostIds())
            {
                //用户和岗位关联
                UserPost up = new UserPost();
                up.setUserId(user.getUserId());//用户id
                up.setPostId(postId);//岗位id
                list.add(up);//加入该对象
            }
        }
    }


    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(User user)
    {
        //角色组
        Long[] roles = user.getRoleIds();
        //角色组非空
        if (roles!=null)
        {
            // 新增用户与角色管理
            List<UserRole> list = new ArrayList<UserRole>();
            //循环编辑角色组关系
            for (Long roleId : user.getRoleIds())
            {
                //用户和角色关联
                UserRole ur = new UserRole();
                ur.setUserId(user.getUserId());//用户id
                ur.setRoleId(roleId);//角色id
                list.add(ur);//加入该对象
            }
            //集合大于0，说明有对应的用户角色关联信息
            if (list.size() > 0)
            {
                //批量新增用户角色信息
                userRoleMapper.batchUserRole(list);
            }
        }
    }
}
