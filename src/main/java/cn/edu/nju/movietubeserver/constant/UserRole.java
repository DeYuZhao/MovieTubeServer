package cn.edu.nju.movietubeserver.constant;

/**
 * @author dc
 * @date 2020/4/5 14:08
 */
public final class UserRole
{

    public static final class RoleName
    {
        public static final String ADMIN = "ROLE_ADMIN";

        public static final String USER = "ROLE_USER";

        public static final String TEST = "ROLE_TEST";

        public static final String BLACKLIST = "ROLE_BLACKLIST";
    }

    public static final class RoleId
    {
        public static final Integer ADMIN = 1;

        public static final Integer USER = 2;

        public static final Integer TEST = 3;

        public static final Integer BLACKLIST = 4;
    }
}
