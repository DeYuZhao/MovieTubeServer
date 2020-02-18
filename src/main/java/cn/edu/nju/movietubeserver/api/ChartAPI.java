package cn.edu.nju.movietubeserver.api;

import cn.edu.nju.movietubeserver.model.dto.BarChartDto;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author zhaodeyu
 * @classname ChartAPI
 * @description TODO
 * @date 2020-02-13 11:12
 */
@Api(tags = {"chart-tag"})
public interface ChartAPI {

    @ApiOperation(value = "获取评论数量柱状图数据结构", notes = "返回结果为x，y轴的数据列表", httpMethod = "GET")
    RestApiResponse<BarChartDto> getCommentCountBarChart(
            @ApiParam(value = "用户Id", required = false) Integer userId
    );
}
