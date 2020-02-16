package cn.edu.nju.movietubeserver.api;

import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author leolu
 * @create 2020-02-12-11:56
 **/
@Api(value = "管理员的相关接口", description = "管理员的相关接口")
public interface AdminAPI
{

    @ApiOperation(value = "封禁账号", notes = "封禁账号", httpMethod = "POST")
    RestApiResponse<Boolean> closureUserByUserId(@ApiParam(value = "用户ID", required = true) Integer userId);

    @ApiOperation(value = "解封账号", notes = "解封账号", httpMethod = "POST")
    RestApiResponse<Boolean> releaseUserByUserId(@ApiParam(value = "用户ID", required = true) Integer userId);

}
