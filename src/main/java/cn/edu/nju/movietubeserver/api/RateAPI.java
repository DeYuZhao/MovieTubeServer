package cn.edu.nju.movietubeserver.api;

import cn.edu.nju.movietubeserver.model.dto.RateDetailDto;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import io.swagger.annotations.Api;

/**
 * @author dc
 * @date 2020/2/11 21:32
 */
@Api(tags = {"rate-tag"})
public interface RateAPI
{

    RestApiResponse<Integer> insertOrUpdateRate(RateDetailDto rateDetailDto);

}
