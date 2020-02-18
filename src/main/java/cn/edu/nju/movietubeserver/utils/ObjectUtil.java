package cn.edu.nju.movietubeserver.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.Objects;

/**
 * @author dc
 * @date 2019/12/21 22:15
 */
public class ObjectUtil
{

    private ObjectUtil()
    {

    }

    public static <S, T> T deepCloneByJson(S source, Class<T> targetClazz)
    {
        Objects.requireNonNull(source, "source object must not be null");
        Objects.requireNonNull(targetClazz, "target class must not be null");
        return JSON.parseObject(JSONObject.toJSONString(source), targetClazz);
    }
}
