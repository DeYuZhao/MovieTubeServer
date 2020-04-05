package cn.edu.nju.movietubeserver.service.impl;

import cn.edu.nju.movietubeserver.constant.ESIndexFieldKey;
import cn.edu.nju.movietubeserver.dao.CommentDao;
import cn.edu.nju.movietubeserver.model.dto.BarChartDto;
import cn.edu.nju.movietubeserver.model.dto.CommentDto;
import cn.edu.nju.movietubeserver.model.po.CommentPo;
import cn.edu.nju.movietubeserver.service.ChartService;
import cn.edu.nju.movietubeserver.service.CommentService;
import cn.edu.nju.movietubeserver.support.elasticsearch.dao.BaseElasticSearchDao;
import cn.edu.nju.movietubeserver.support.elasticsearch.service.impl.BaseElasticSearchServiceImpl;
import cn.edu.nju.movietubeserver.utils.DateUtil;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * @author zhaodeyu
 * @classname ChartServiceImpl
 * @description 图表的相关接口实现
 * @date 2020-02-13 11:14
 */
@Service
public class ChartServiceImpl extends BaseElasticSearchServiceImpl<CommentDto, CommentPo, String>
    implements ChartService
{
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private CommentService commentService;

    @Override
    public BaseElasticSearchDao<CommentPo, String> getBaseElasticSearchDao()
    {
        return commentDao;
    }

    @Override
    public CommentDto convert(CommentPo commentPo)
    {
        Objects.requireNonNull(commentPo, "评论不能为空");
        return commentPo.toDto();
    }

    @Override
    public BarChartDto getCommentCounts(Integer userId)
    {
        List<String> xAxisList = new ArrayList<>();
        List<List<Long>> yAxisLists = new ArrayList<>();
        List<Long> yAxisList = new ArrayList<>();
        LocalDateTime nowTime = LocalDateTime.now();
        for (int i = 0; i < 12; ++i)
        {
            String localTime = DateUtil.formatDateToString(nowTime.minusMonths(i));
            xAxisList.add(0, localTime.substring(0, 7));
            long totalComments;
            if (userId != 0)
            {
                totalComments = search(new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery(ESIndexFieldKey.Comment.FROM_USER_ID, userId))
                    .must(QueryBuilders.wildcardQuery(ESIndexFieldKey.Comment.CREATE_TIME,
                        localTime.substring(0, 4) + "*"))
                    .must(QueryBuilders.wildcardQuery(ESIndexFieldKey.Comment.CREATE_TIME,
                        localTime.substring(5, 7) + "*"))).build()).getTotalElements();
            }
            else
            {
                totalComments = search(new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.wildcardQuery(ESIndexFieldKey.Comment.CREATE_TIME,
                        localTime.substring(0, 4) + "*"))
                    .must(QueryBuilders.wildcardQuery(ESIndexFieldKey.Comment.CREATE_TIME,
                        localTime.substring(5, 7) + "*"))).build()).getTotalElements();
            }
            yAxisList.add(0, totalComments);
        }
        yAxisLists.add(yAxisList);
        BarChartDto barChartDto = new BarChartDto();
        barChartDto.setXAxisDataList(xAxisList);
        barChartDto.setYAxisDataLists(yAxisLists);

        return barChartDto;
    }
}
