package cn.edu.nju.movietubeserver.constant;

/**
 * @author dc
 * @date 2020/4/5 14:01
 */
public class ErrorMessage
{

    public static final String DEFAULT_UNAUTHORIZED_MESSAGE = "Need authorized";

    public static final String DEFAULT_METHOD_NOT_ALLOWED_MESSAGE = "Request method incorrect";

    public static final String DATABASE_ERROR_MESSAGE = "Database error";


    // es
    public static final String EMPTY_QUERY_PARAM_MESSAGE = "query params cannot be null";

    public static final String EMPTY_PAGEABLE_PARAM_MESSAGE = "pageable params cannot be null";

    public static final String EMPTY_SORT_PARAM_MESSAGE = "sort params cannot be null";
}
