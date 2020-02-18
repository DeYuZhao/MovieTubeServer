package cn.edu.nju.movietubeserver.api;

import cn.edu.nju.movietubeserver.model.dto.MovieDto;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;

/**
 * @author dc
 * @date 2020/2/8 15:23
 */
@Api(tags = {"movie-tag"})
public interface MovieAPI
{

    @ApiOperation(value = "获取该类别下电影的总数量", notes = "获取该类别下电影的总数量", httpMethod = "GET")
    RestApiResponse<Long> getCountOfMoviesByTag(String tag);

    @ApiOperation(value = "分页获取该类别下电影", notes = "分页获取该类别下电影", httpMethod = "GET")
    RestApiResponse<Page<MovieDto>> listByTag(String tag, Integer pageNo, Integer pageSize);

    @ApiOperation(value = "根据电影ID获取电影详情", notes = "根据电影ID获取电影详情", httpMethod = "GET")
    RestApiResponse<MovieDto> getByMovieId(Long movieId);

    @ApiOperation(value = "根据关键词搜索电影", notes = "根据关键词搜索电影", httpMethod = "GET")
    RestApiResponse<Page<MovieDto>> searchByKeyword(Integer pageNo, Integer pageSize, String keyword);
}
