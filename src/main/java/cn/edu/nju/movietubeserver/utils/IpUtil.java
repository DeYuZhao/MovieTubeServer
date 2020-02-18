package cn.edu.nju.movietubeserver.utils;

import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author dc
 * @date 2019/12/21 21:51
 *
 * IP工具类
 */
@Slf4j
public class IpUtil
{
    private static final String UNKNOWN = "unknown";

    private static final String LOCALHOST_IPV4 = "127.0.0.1";

    private static final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    private IpUtil()
    {

    }

    /**
     * 获取登录用户的IP地址
     *
     * @param httpServletRequest httpServletRequest
     * @return IP地址
     */
    public static String getIpAddress(final HttpServletRequest httpServletRequest)
    {
        String ip = httpServletRequest.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip))
        {
            ip = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip))
        {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip))
        {
            ip = httpServletRequest.getRemoteAddr();
        }
        if (StringUtils.equals(LOCALHOST_IPV6, ip))
        {
            ip = LOCALHOST_IPV4;
        }
        if (ip.split(",").length > 1)
        {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}
