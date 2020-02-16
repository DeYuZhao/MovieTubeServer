package cn.edu.nju.movietubeserver.service.impl;

import cn.edu.nju.movietubeserver.dao.RateDetailDao;
import cn.edu.nju.movietubeserver.dao.RateStatisticDao;
import cn.edu.nju.movietubeserver.model.po.RateDetailPo;
import cn.edu.nju.movietubeserver.model.po.RateStatisticPo;
import cn.edu.nju.movietubeserver.service.RateService;
import cn.edu.nju.movietubeserver.support.exception.ServiceException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dc
 * @date 2020/2/11 21:28
 */
@Service
public class RateServiceImpl implements RateService
{

    @Autowired
    private RateDetailDao rateDetailDao;

    @Autowired
    private RateStatisticDao rateStatisticDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertRate(RateDetailPo rateDetailPo)
    {
        if (rateDetailDao.insertRateDetail(rateDetailPo) <= 0)
        {
            throw new ServiceException("插入电影评分记录失败");
        }
        return rateStatisticDao.insertOrUpdateRateStatistic(rateDetailPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateRateById(RateDetailPo rateDetailPo)
    {
        if (rateDetailDao.updateRateDetailById(rateDetailPo) <= 0)
        {
            throw new ServiceException(String.format("电影评分记录不存在，id为[%d]", rateDetailPo.getId()));
        }
        return rateStatisticDao.insertOrUpdateRateStatistic(rateDetailPo);
    }

    @Override
    public Double getLocalRateByMovieId(Long movieId)
    {
        RateStatisticPo rateStatisticPo = rateStatisticDao.getByMovieId(movieId);
        if (rateStatisticPo == null || rateStatisticPo.getTotalCount() == 0)
        {
            return 0.0;
        }
        BigDecimal totalRate = new BigDecimal(String.valueOf(rateStatisticPo.getTotalRate()));
        BigDecimal totalCount = new BigDecimal(String.valueOf(rateStatisticPo.getTotalCount()));
        return totalRate.divide(totalCount, 1, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Double getMyRateByMovieId(Integer userId, Long movieId)
    {
        return Optional.ofNullable(rateDetailDao.getMyRateByMovieId(userId, movieId)).orElse(-1.0);
    }
}
