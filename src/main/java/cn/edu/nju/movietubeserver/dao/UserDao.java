package cn.edu.nju.movietubeserver.dao;

import cn.edu.nju.movietubeserver.model.domain.SimpleUser;
import cn.edu.nju.movietubeserver.model.po.UserPo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

/**
 * @author dc
 * @date 2019/12/22 13:38
 */
@Mapper
@Repository
public interface UserDao
{

    /**
     * 添加用户，用于用户注册
     * @param userPo
     * @return
     */
    int insertUser(@Param("userPo") UserPo userPo);

    /**
     * 根据用户名从数据库中查询用户
     * @param username
     * @return
     */
    UserPo getUserByUsername(@Param("username") String username);

    /**
     * 根据邮箱从数据库中查询用户
     * @param email
     * @return
     */
    UserPo getUserByEmail(@Param("email") String email);

    /**
     * 更新用户信息
     * @param userPo
     * @return
     */
    int updateUserInfoById(@Param("userPo") UserPo userPo);

    List<SimpleUser> listAllSimpleUsers();

    /**
     * 更新用户角色，用于封禁、解封用户
     * @param userId
     * @param roleId
     * @return
     */
    boolean updateRoleIdByUserId(@Param("userId") Integer userId, Integer roleId);

}
