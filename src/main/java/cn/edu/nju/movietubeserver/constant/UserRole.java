package cn.edu.nju.movietubeserver.constant;

/**
 * @author dc
 * @date 2020/1/2 17:48
 */
public interface UserRole
{

    interface RoleName
    {
        String ADMIN = "ROLE_ADMIN";

        String USER = "ROLE_USER";

        String TEST = "ROLE_TEST";

        String BLACKLIST = "ROLE_BLACKLIST";
    }

    interface RoleId
    {
        Integer ADMIN = 1;

        Integer USER = 2;

        Integer TEST = 3;

        Integer BLACKLIST = 4;
    }
}

