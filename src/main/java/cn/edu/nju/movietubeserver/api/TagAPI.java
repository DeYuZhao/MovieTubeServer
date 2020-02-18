package cn.edu.nju.movietubeserver.api;

import cn.edu.nju.movietubeserver.model.dto.TagDto;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;

/**
 * @author dc
 * @date 2020/2/8 15:18
 */
@Api(tags = {"tag-tag"})
public interface TagAPI
{

    @ApiOperation(value = "获取所有电影标签", notes = "获取所有电影标签", httpMethod = "GET")
    RestApiResponse<Map<String, List<TagDto>>> getTagsMap();
}
