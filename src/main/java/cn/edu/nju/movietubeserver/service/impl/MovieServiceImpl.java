package cn.edu.nju.movietubeserver.service.impl;

import cn.edu.nju.movietubeserver.constant.ESIndexFieldKey;
import cn.edu.nju.movietubeserver.dao.MovieDao;
import cn.edu.nju.movietubeserver.model.dto.MovieDto;
import cn.edu.nju.movietubeserver.model.dto.TagDto;
import cn.edu.nju.movietubeserver.model.po.MoviePo;
import cn.edu.nju.movietubeserver.service.MovieService;
import cn.edu.nju.movietubeserver.service.TagService;
import cn.edu.nju.movietubeserver.support.elasticsearch.dao.BaseElasticSearchDao;
import cn.edu.nju.movietubeserver.support.elasticsearch.service.impl.BaseElasticSearchServiceImpl;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

/**
 * @author dc
 * @date 2020/2/7 0:38
 */
@Service
public class MovieServiceImpl extends BaseElasticSearchServiceImpl<MovieDto, MoviePo, Long> implements MovieService
{

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private TagService tagService;

    @Override
    public BaseElasticSearchDao<MoviePo, Long> getBaseElasticSearchDao()
    {
        return movieDao;
    }

    @Override
    public Page<MovieDto> searchByKeyword(Integer pageNo, Integer pageSize, String searchKeyword, String... fieldNames)
    {
        String[] tagIndexes = tagService.getTagsMap()
            .values()
            .stream()
            .flatMap(List::stream)
            .map(TagDto::getValue)
            .distinct()
            .toArray(String[]::new);

        // 去重
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(tagIndexes)
            .withQuery(QueryBuilders.multiMatchQuery(searchKeyword, fieldNames).type(Type.BEST_FIELDS).tieBreaker(0.1f))
            .withPageable(PageRequest.of(pageNo, pageSize))
            .withSort(SortBuilders.scoreSort().order(SortOrder.DESC))
            .build();
        return search(searchQuery);
    }

    @Override
    public Optional<MovieDto> getByMovieIdFromAllIndices(Long movieId)
    {
        String[] tagIndexes = tagService.getTagsMap()
            .values()
            .stream()
            .flatMap(List::stream)
            .map(TagDto::getValue)
            .distinct()
            .toArray(String[]::new);
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices(tagIndexes)
            .withQuery(QueryBuilders.termQuery(ESIndexFieldKey.Movie.ID, movieId))
            .withPageable(PageRequest.of(0, 1))
            .build();
        return Optional.ofNullable(search(searchQuery).getContent().get(0));
    }

    @Override
    public MovieDto convert(MoviePo moviePo)
    {
        return moviePo.toDto();
    }
}
