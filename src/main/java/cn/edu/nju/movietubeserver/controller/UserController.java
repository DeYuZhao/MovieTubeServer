package cn.edu.nju.movietubeserver.controller;

import cn.edu.nju.movietubeserver.api.UserAPI;
import cn.edu.nju.movietubeserver.model.dto.LoginUserDto;
import cn.edu.nju.movietubeserver.model.dto.RegisterUserDto;
import cn.edu.nju.movietubeserver.model.dto.UpdateUserDto;
import cn.edu.nju.movietubeserver.model.dto.UserDto;
import cn.edu.nju.movietubeserver.model.po.UserPo;
import cn.edu.nju.movietubeserver.service.UserService;
import cn.edu.nju.movietubeserver.support.exception.ServiceException;
import cn.edu.nju.movietubeserver.support.jwt.JWTUtil;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import cn.edu.nju.movietubeserver.support.response.RestApiResponseUtil;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dc
 * @date 2019/12/21 21:26
 */
@RestController
@RequestMapping(path = "/api/user")
public class UserController implements UserAPI
{

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 可以以用户名登录或邮箱登录
     * @param loginUserDto
     * @param bindingResult
     * @return
     */
    @Override
    @PostMapping(path = "/login")
    public RestApiResponse<String> login(@Valid @RequestBody LoginUserDto loginUserDto,
        final BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            String errorMessage = Optional.ofNullable(bindingResult.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("表单信息校验不通过，请按要求填写登录信息");
            return RestApiResponseUtil.createErrorResponse(errorMessage);
        }
        if (StringUtils.isEmpty(loginUserDto.getUsername()) && StringUtils.isEmpty(loginUserDto.getEmail()))
        {
            return RestApiResponseUtil.createErrorResponse("用户名或邮箱不能为空");
        }

        UserDto dbUser = null;
        // 用户名登录
        if (StringUtils.isNotEmpty(loginUserDto.getUsername()))
        {
            dbUser = userService.getUserByUsername(loginUserDto.getUsername());
            if (dbUser == null)
            {
                return RestApiResponseUtil.createErrorResponse("用户" + loginUserDto.getUsername() + "不存在");
            }
        }

        if (StringUtils.isNotEmpty(loginUserDto.getEmail()))
        {
            dbUser = userService.getUserByEmail(loginUserDto.getEmail());
            if (dbUser == null)
            {
                return RestApiResponseUtil.createErrorResponse("用户" + loginUserDto.getEmail() + "不存在");
            }
            loginUserDto.setUsername(dbUser.getUsername());
        }

        String realEncodedPassword =
            Optional.ofNullable(dbUser).map(UserDto::getPassword).orElseThrow(() -> new ServiceException("用户不存在"));
        if (!userService.verifyPassword(loginUserDto.getPassword(), realEncodedPassword))
        {
            return RestApiResponseUtil.createErrorResponse("密码错误");
        }

        return RestApiResponseUtil.createSuccessResponse(createToken(loginUserDto.getUsername()));
    }

    @Override
    @PostMapping(path = "/register")
    public RestApiResponse<Integer> register(@Valid @RequestBody RegisterUserDto registerUserDto,
        final BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            String errorMessage = Optional.ofNullable(bindingResult.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("表单信息校验不通过，请按要求填写注册信息");
            return RestApiResponseUtil.createErrorResponse(errorMessage);
        }
        if (!StringUtils.equalsIgnoreCase(registerUserDto.getPassword(), registerUserDto.getConfirmPassword()))
        {
            return RestApiResponseUtil.createErrorResponse("两次密码输入不一致");
        }
        return RestApiResponseUtil.createSuccessResponse(userService.insertUser(UserPo.valueOf(registerUserDto)));
    }

    @Override
    @GetMapping(path = "/getUserInfo")
    public RestApiResponse<UserDto> getUserInfo(final Principal user)
    {
        UserDto userDB = userService.getUserByUsername(user.getName());
        return RestApiResponseUtil.createSuccessResponse(userDB);
    }

    @Override
    @PostMapping(path = "/updateUserInfoById")
    public RestApiResponse<Integer> updateUserInfoById(@Valid @RequestBody UpdateUserDto updateUserDto,
        final BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            String errorMessage = Optional.ofNullable(bindingResult.getFieldError())
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("表单信息校验不通过，请按要求填写更新信息");
            return RestApiResponseUtil.createErrorResponse(errorMessage);
        }
        return RestApiResponseUtil.createSuccessResponse(userService.updateUserInfoById(UserPo.valueOf(updateUserDto)));
    }

    @Override
    @GetMapping(path = "/logout")
    public RestApiResponse<Void> logout(final Principal user)
    {
        // 让token失效，可以在前端清除token
        return RestApiResponseUtil.createSuccessResponse();
    }

    private String createToken(final String username)
    {
        Objects.requireNonNull(username, "username cannot be null");
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return jwtUtil.sign(username, userDetails.getAuthorities());
    }
}
