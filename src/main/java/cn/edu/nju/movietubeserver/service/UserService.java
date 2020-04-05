package cn.edu.nju.movietubeserver.service;

import cn.edu.nju.movietubeserver.model.domain.SimpleUser;
import cn.edu.nju.movietubeserver.model.dto.UserDto;
import cn.edu.nju.movietubeserver.model.po.UserPo;
import java.util.List;

/**
 * @author dc
 * @date 2019/12/21 22:11
 */
public interface UserService
{

    /**
     * 添加用户，用于用户注册
     * @param userPo
     * @return
     */
    int insertUser(UserPo userPo);

    /**
     * 根据用户邮箱从数据库中查询用户
     * @param email
     * @return
     */
    UserDto getUserByEmail(String email);

    /**
     * 根据用户名从数据库中查询用户
     * @param username
     * @return
     */
    UserDto getUserByUsername(String username);

    /**
     * 更新用户信息
     * @param userPo
     * @return
     */
    int updateUserInfoById(UserPo userPo);

    /**
     * 校验密码
     * @param rawPassword
     * @param encodedPassword
     * @return
     */
    boolean verifyPassword(String rawPassword, String encodedPassword);

    List<SimpleUser> listAllSimpleUsers();
}
