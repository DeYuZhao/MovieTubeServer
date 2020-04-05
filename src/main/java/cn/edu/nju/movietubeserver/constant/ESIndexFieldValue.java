package cn.edu.nju.movietubeserver.constant;

/**
 * @author dc
 * @date 2020/4/5 14:06
 *
 * 存放ElasticSearch索引的schema对应的常量值
 */
public final class ESIndexFieldValue
{

    public static final class Comment
    {
        // parentId == -1 表示是根评论
        public static final Integer NO_PARENT_COMMENT_ID = -1;

        // toUserId == -1 也表示是根评论
        public static final Integer NO_TO_USER_ID = -1;
    }
}
