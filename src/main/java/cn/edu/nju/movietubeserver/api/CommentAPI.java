package cn.edu.nju.movietubeserver.api;

import cn.edu.nju.movietubeserver.model.dto.CommentDto;
import cn.edu.nju.movietubeserver.model.dto.PostCommentDto;
import cn.edu.nju.movietubeserver.model.dto.ReceiveCommentDto;
import cn.edu.nju.movietubeserver.model.dto.ReplyCommentDto;
import cn.edu.nju.movietubeserver.model.dto.RootCommentDto;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.data.domain.Page;

/**
 * @author leolu
 * @create 2019-12-25-20:17
 **/
@Api(tags = {"comment-tag"})
public interface CommentAPI
{

    @ApiOperation(value = "获取电影的主评论列表", notes = "获取电影的主评论列表，返回体包含所有评论的数量，默认返回2条子评论列表，返回分页结果", httpMethod = "GET")
    RestApiResponse<Page<RootCommentDto>> listRootCommentByMovieId(
        @ApiParam(value = "第几页，默认0", required = false) Integer pageNo,
        @ApiParam(value = "每页多少条数据，默认20", required = false) Integer pageSize,
        @ApiParam(value = "电影ID", required = true) Long movieId);

    @ApiOperation(value = "根据电影及父评论，查找子评论回复", notes = "根据电影及父评论，查找子评论回复，返回分页结果", httpMethod = "GET")
    RestApiResponse<Page<ReplyCommentDto>> listReplyCommentOfRootComment(
        @ApiParam(value = "第几页，默认0", required = false) Integer pageNo,
        @ApiParam(value = "每页多少条数据，默认20", required = false) Integer pageSize,
        @ApiParam(value = "电影ID", required = true) Long movieId,
        @ApiParam(value = "主评论ID", required = true) String rootCommentId);

    @ApiOperation(value = "获取用户发出的评论", notes = "获取用户发出的所有评论，返回分页结果", httpMethod = "GET")
    RestApiResponse<Page<PostCommentDto>> listUserPostComments(
        @ApiParam(value = "第几页，默认0", required = false) Integer pageNo,
        @ApiParam(value = "每页多少条数据，默认20", required = false) Integer pageSize,
        @ApiParam(value = "用户ID", required = true) Integer userId);

    @ApiOperation(value = "获取用户收到的评论", notes = "获取用户收到的所有评论，返回分页结果", httpMethod = "GET")
    RestApiResponse<Page<ReceiveCommentDto>> listUserReceiveComments(
        @ApiParam(value = "第几页，默认0", required = false) Integer pageNo,
        @ApiParam(value = "每页多少条数据，默认20", required = false) Integer pageSize,
        @ApiParam(value = "用户ID", required = true) Integer userId);

    @ApiOperation(value = "用户添加新的电影评论", notes = "用户添加新的电影评论", httpMethod = "POST")
    RestApiResponse<Boolean> insertComment(@ApiParam(value = "要添加的评论实体", required = true) CommentDto commentDto);

    @ApiOperation(value = "根据评论ID删除评论", notes = "根据评论ID删除评论，将删除该条评论下的所有子回复", httpMethod = "GET")
    RestApiResponse<Boolean> deleteByCommentId(@ApiParam(value = "评论所在的电影ID", required = true) Long movieId,
        @ApiParam(value = "要删除的评论ID", required = true) String commentId);

    @ApiOperation(value = "根据电影ID删除评论", notes = "根据电影ID删除评论，将删除该电影下的所有评论及回复", httpMethod = "GET")
    RestApiResponse<Boolean> deleteByMovieId(Long movieId);

}
