package cn.edu.nju.movietubeserver.service;

import cn.edu.nju.movietubeserver.model.dto.BarChartDto;
import cn.edu.nju.movietubeserver.model.dto.CommentDto;
import cn.edu.nju.movietubeserver.model.po.CommentPo;
import cn.edu.nju.movietubeserver.support.elasticsearch.service.BaseElasticSearchService;

/**
 * @author zhaodeyu
 * @classname ChartService
 * @description
 * @date 2020-02-13 11:14
 */
public interface ChartService extends BaseElasticSearchService<CommentDto, CommentPo, String> {
    BarChartDto getCommentCounts(Integer userId);
}
