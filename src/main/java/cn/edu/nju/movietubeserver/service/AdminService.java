package cn.edu.nju.movietubeserver.service;

/**
 * @author leolu
 * @create 2020-02-14-9:29
 **/
public interface AdminService
{

    //封禁账户
    boolean closureUserByUserId(Integer userId);

    //解封账户
    boolean releaseUserByUserId(Integer userId);

}
