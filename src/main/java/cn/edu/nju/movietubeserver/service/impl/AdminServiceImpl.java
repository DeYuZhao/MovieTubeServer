package cn.edu.nju.movietubeserver.service.impl;

import cn.edu.nju.movietubeserver.constant.UserRole;
import cn.edu.nju.movietubeserver.dao.UserDao;
import cn.edu.nju.movietubeserver.service.AdminService;
import cn.edu.nju.movietubeserver.support.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author leolu
 * @create 2020-02-14-9:39
 **/
@Service
public class AdminServiceImpl implements AdminService
{

    @Autowired
    private UserDao userDao;

    @Override
    public boolean closureUserByUserId(@RequestParam Integer userId) {
        try {
            return userDao.updateRoleIdByUserId(userId, UserRole.RoleId.BLACKLIST);
        }catch (Throwable e) {
            throw new ServiceException("fail to closureRoleByUserId", e);
        }

    }

    @Override
    public boolean releaseUserByUserId(@RequestParam Integer userId) {
        try {
            return userDao.updateRoleIdByUserId(userId, UserRole.RoleId.USER);
        }catch (Throwable e) {
            throw new ServiceException("fail to releaseRoleByUserId", e);
        }
    }

}
