package cn.edu.nju.movietubeserver.constant;

/**
 * @author dc
 * @date 2020/4/5 14:03
 */
public class ESBoolQueryConstant
{
    // AND，文档需要完全匹配must选项下的查询条件
    public static final String MUST = "must";

    // MUST 取反，文档不能匹配must选项下的查询条件
    public static final String MUST_NOT = "mustNot";

    // OR， should下面会带一个以上的条件，文档至少匹配一个should选项下的条件
    public static final String SHOULD = "should";

    // 与must类似，但是filter不评分，只起过滤功能
    public static final String FILTER = "filter";
}
