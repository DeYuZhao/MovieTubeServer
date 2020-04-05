package cn.edu.nju.movietubeserver.model.dto;

import cn.edu.nju.movietubeserver.model.domain.SimpleMovieInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dc
 * @date 2020/1/2 13:07
 *
 * 用于展示在个人中心里，我收到的评论
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDto
{

    private String id;

    private SimpleMovieInfo simpleMovieInfo;

    private Integer toUserId;

    private String toUsername;

    private String content;

    // 点击评论，查看评论前后文，弹窗？新页面？
    private String commentURL;

    private String createTime;
}
