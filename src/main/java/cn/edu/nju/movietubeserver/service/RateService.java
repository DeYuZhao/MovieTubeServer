package cn.edu.nju.movietubeserver.service;

import cn.edu.nju.movietubeserver.model.po.RateDetailPo;

/**
 * @author dc
 * @date 2020/2/11 20:33
 */
public interface RateService
{

    int insertRate(RateDetailPo rateDetailPo);

    int updateRateById(RateDetailPo rateDetailPo);

    Double getLocalRateByMovieId(Long movieId);

    Double getMyRateByMovieId(Integer userId, Long movieId);

}
