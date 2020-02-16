package cn.edu.nju.movietubeserver.controller;

import cn.edu.nju.movietubeserver.api.AdminAPI;
import cn.edu.nju.movietubeserver.service.AdminService;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import cn.edu.nju.movietubeserver.support.response.RestApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author leolu
 * @create 2020-02-13-22:54
 **/
@RestController
@RequestMapping(path = "/api/admin")
public class AdminController implements AdminAPI
{

    @Autowired
    private AdminService adminService;

    @Override
    @PostMapping(path = "/closureUserByUserId")
    public RestApiResponse<Boolean> closureUserByUserId(@RequestParam Integer userId) {
        return RestApiResponseUtil.createSuccessResponse(adminService.closureUserByUserId(userId));
    }

    @Override
    @PostMapping(path = "/releaseUserByUserId")
    public RestApiResponse<Boolean> releaseUserByUserId(@RequestParam Integer userId) {
        return RestApiResponseUtil.createSuccessResponse(adminService.releaseUserByUserId(userId));
    }

}
