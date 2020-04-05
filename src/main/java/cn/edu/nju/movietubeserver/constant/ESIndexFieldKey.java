package cn.edu.nju.movietubeserver.constant;

/**
 * @author dc
 * @date 2020/4/5 14:04
 *
 * 存放ElasticSearch索引的schema
 */
public final class ESIndexFieldKey
{

    public static final class Movie
    {
        public static final String ID = "id";

        public static final String TITLE = "title";

        public static final String RATE = "rate";

        public static final String STAR = "star";

        public static final String DIRECTORS = "directors";

        public static final String CASTS = "casts";
    }

    public static final class Comment
    {
        public static final String MOVIE_ID = "movieId";

        public static final String TO_USER_ID = "toUserId";

        public static final String FROM_USER_ID = "fromUserId";

        public static final String PARENT_COMMENT_ID = "parentCommentId";

        public static final String ROOT_COMMENT_ID = "rootCommentId";

        public static final String CREATE_TIME = "createTime";
    }
}
