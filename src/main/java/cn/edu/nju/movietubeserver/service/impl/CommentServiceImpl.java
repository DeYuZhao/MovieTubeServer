package cn.edu.nju.movietubeserver.service.impl;

import cn.edu.nju.movietubeserver.constant.ESIndexFieldKey;
import cn.edu.nju.movietubeserver.constant.ESIndexFieldKey.Comment;
import cn.edu.nju.movietubeserver.constant.ESIndexFieldValue;
import cn.edu.nju.movietubeserver.dao.CommentDao;
import cn.edu.nju.movietubeserver.model.domain.SimpleMovieInfo;
import cn.edu.nju.movietubeserver.model.domain.SimpleUser;
import cn.edu.nju.movietubeserver.model.dto.CommentDto;
import cn.edu.nju.movietubeserver.model.dto.PostCommentDto;
import cn.edu.nju.movietubeserver.model.dto.ReceiveCommentDto;
import cn.edu.nju.movietubeserver.model.dto.ReplyCommentDto;
import cn.edu.nju.movietubeserver.model.dto.RootCommentDto;
import cn.edu.nju.movietubeserver.model.po.CommentPo;
import cn.edu.nju.movietubeserver.service.CommentService;
import cn.edu.nju.movietubeserver.service.MovieService;
import cn.edu.nju.movietubeserver.support.elasticsearch.dao.BaseElasticSearchDao;
import cn.edu.nju.movietubeserver.support.elasticsearch.service.impl.BaseElasticSearchServiceImpl;
import cn.edu.nju.movietubeserver.utils.ObjectUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.QueryBuilder;
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
 * @author leolu
 * @create 2019-12-26-19:36
 **/
@Service
public class CommentServiceImpl extends BaseElasticSearchServiceImpl<CommentDto, CommentPo, String>
    implements CommentService
{

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private MovieService movieService;

    private static final String KEYWORD_SUFFIX = ".keyword";

    @Override
    public BaseElasticSearchDao<CommentPo, String> getBaseElasticSearchDao()
    {
        return commentDao;
    }

    @Override
    public Page<RootCommentDto> listRootCommentByMovieId(Integer pageNo, Integer pageSize, Long movieId,
        Map<Integer, SimpleUser> simpleUserMap)
    {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery(Comment.MOVIE_ID, String.valueOf(movieId)))
            .must(QueryBuilders.matchQuery(Comment.PARENT_COMMENT_ID, ESIndexFieldValue.Comment.NO_PARENT_COMMENT_ID)))
            .withPageable(PageRequest.of(pageNo, pageSize))
            .withSort(SortBuilders.fieldSort(Comment.CREATE_TIME + KEYWORD_SUFFIX).order(SortOrder.ASC))
            .build();
        return search(searchQuery).map(dto -> convertToRootComment(dto, simpleUserMap));
    }

    @Override
    public Page<ReplyCommentDto> listReplyCommentOfRootComment(Integer pageNo, Integer pageSize, Long movieId,
        String rootCommentId, Map<Integer, SimpleUser> simpleUserMap)
    {
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery(Comment.MOVIE_ID, String.valueOf(movieId)))
            .must(QueryBuilders.matchQuery(Comment.ROOT_COMMENT_ID, rootCommentId)))
            .withPageable(PageRequest.of(pageNo, pageSize))
            .withSort(SortBuilders.fieldSort(Comment.CREATE_TIME + KEYWORD_SUFFIX).order(SortOrder.ASC))
            .build();
        return search(searchQuery).map(dto -> convertToReplyComment(dto, simpleUserMap));
    }

    @Override
    public Page<PostCommentDto> listUserPostComments(Integer pageNo, Integer pageSize, Integer userId,
        Map<Integer, SimpleUser> simpleUserMap)
    {
        return termSearchByKeyword(Comment.FROM_USER_ID,
            String.valueOf(userId),
            PageRequest.of(pageNo, pageSize),
            SortBuilders.fieldSort(Comment.CREATE_TIME + KEYWORD_SUFFIX)
                .order(SortOrder.DESC)).map(dto -> convertToPostComment(dto, simpleUserMap));
    }

    @Override
    public Page<ReceiveCommentDto> listUserReceiveComments(Integer pageNo, Integer pageSize, Integer userId,
        Map<Integer, SimpleUser> simpleUserMap)
    {
        return termSearchByKeyword(Comment.TO_USER_ID,
            String.valueOf(userId),
            PageRequest.of(pageNo, pageSize),
            SortBuilders.fieldSort(Comment.CREATE_TIME + KEYWORD_SUFFIX)
                .order(SortOrder.DESC)).map(dto -> convertToReceiveComment(dto, simpleUserMap));
    }

    @Override
    public void listAllChildrenComments(Long movieId, String commentId, List<CommentDto> resultList)
    {
        List<CommentDto> childList = listByParentId(movieId, commentId);
        if (CollectionUtils.isEmpty(childList))
        {
            return;
        }
        resultList.addAll(childList);
        for (CommentDto commentDto : childList)
        {
            listAllChildrenComments(movieId, commentDto.getId(), resultList);
        }
    }

    @Override
    public void insertComment(CommentPo commentPo)
    {
        commentDao.save(commentPo);
    }

    @Override
    public void deleteByCommentId(Long movieId, String commentId)
    {
        List<CommentDto> childCommentList = new ArrayList<>();
        listAllChildrenComments(movieId, commentId, childCommentList);
        commentDao.deleteById(commentId);
        // 删除子评论
        childCommentList.stream().forEach(childComment -> commentDao.deleteById(childComment.getId()));
    }

    @Override
    public void deleteByMovieId(Long movieId)
    {
        SearchQuery query =
            new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchQuery(ESIndexFieldKey.Comment.MOVIE_ID,
                String.valueOf(movieId))) // 搜索条件
                .build();
        List<CommentPo> commentInMovieList =
            search(query).getContent().stream().map(CommentPo::valueOf).collect(Collectors.toList());
        commentDao.deleteAll(commentInMovieList);
    }

    @Override
    public CommentDto convert(CommentPo commentPo)
    {
        Objects.requireNonNull(commentPo, "评论不能为空");
        return commentPo.toDto();
    }

    private RootCommentDto convertToRootComment(CommentDto commentDto, Map<Integer, SimpleUser> simpleUserMap)
    {
        RootCommentDto rootCommentDto = ObjectUtil.deepCloneByJson(commentDto, RootCommentDto.class);

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery(Comment.MOVIE_ID, String.valueOf(rootCommentDto.getMovieId())))
            .must(QueryBuilders.matchQuery(Comment.ROOT_COMMENT_ID, rootCommentDto.getId())))
            .withPageable(PageRequest.of(0, 2))
            .withSort(SortBuilders.fieldSort(Comment.CREATE_TIME + KEYWORD_SUFFIX).order(SortOrder.ASC)) //不加keyword的话会报错
            .build();
        Page<CommentDto> replyCommentPage = search(searchQuery);
        rootCommentDto.setFromUsername(simpleUserMap.get(commentDto.getFromUserId()).getUsername());
        rootCommentDto.setTotalReplyCommentCount(replyCommentPage.getTotalElements());
        rootCommentDto.setDefaultReplyCommentList(replyCommentPage.getContent()
            .stream()
            .map(dto -> convertToReplyComment(dto, simpleUserMap))
            .collect(Collectors.toList()));
        return rootCommentDto;
    }

    private ReplyCommentDto convertToReplyComment(CommentDto commentDto, Map<Integer, SimpleUser> simpleUserMap)
    {
        ReplyCommentDto replyCommentDto = ObjectUtil.deepCloneByJson(commentDto, ReplyCommentDto.class);
        replyCommentDto.setFromUsername(simpleUserMap.get(replyCommentDto.getFromUserId()).getUsername());
        replyCommentDto.setToUsername(simpleUserMap.getOrDefault(replyCommentDto.getToUserId(),
            SimpleUser.getDefaultUser()).getUsername());
        return replyCommentDto;
    }

    private ReceiveCommentDto convertToReceiveComment(CommentDto commentDto, Map<Integer, SimpleUser> simpleUserMap)
    {
        ReceiveCommentDto receiveCommentDto = ObjectUtil.deepCloneByJson(commentDto, ReceiveCommentDto.class);
        receiveCommentDto.setFromUsername(simpleUserMap.getOrDefault(receiveCommentDto.getFromUserId(),
            SimpleUser.getDefaultUser()).getUsername());

        SimpleMovieInfo simpleMovieInfo = movieService.getByMovieIdFromAllIndices(commentDto.getMovieId())
            .map(movie -> ObjectUtil.deepCloneByJson(movie, SimpleMovieInfo.class))
            .orElse(SimpleMovieInfo.getDefaultMovieInfo());
        receiveCommentDto.setSimpleMovieInfo(simpleMovieInfo);

        // TODO
        // receiveCommentDto.setCommentURL("FIX ME");
        return receiveCommentDto;
    }

    private PostCommentDto convertToPostComment(CommentDto commentDto, Map<Integer, SimpleUser> simpleUserMap)
    {
        PostCommentDto postCommentDto = ObjectUtil.deepCloneByJson(commentDto, PostCommentDto.class);
        postCommentDto.setToUsername(simpleUserMap.getOrDefault(postCommentDto.getToUserId(),
            SimpleUser.getDefaultUser()).getUsername());

        SimpleMovieInfo simpleMovieInfo = movieService.getByMovieIdFromAllIndices(commentDto.getMovieId())
            .map(movie -> ObjectUtil.deepCloneByJson(movie, SimpleMovieInfo.class))
            .orElse(SimpleMovieInfo.getDefaultMovieInfo());
        postCommentDto.setSimpleMovieInfo(simpleMovieInfo);

        // TODO
        // postCommentDto.setCommentURL("FIX ME");
        return postCommentDto;
    }

    private List<CommentDto> listByParentId(Long movieId, String commentId)
    {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery(Comment.MOVIE_ID, String.valueOf(movieId)))
            .must(QueryBuilders.matchQuery(Comment.PARENT_COMMENT_ID, commentId));
        return searchAll(queryBuilder);
    }

}
