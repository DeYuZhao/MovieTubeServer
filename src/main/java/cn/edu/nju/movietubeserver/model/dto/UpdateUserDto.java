package cn.edu.nju.movietubeserver.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dc
 * @date 2020/2/12 19:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto
{

    @NotNull
    private Integer userId;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, message = "用户名长度不能少于3位")
    private String username;

    @Email
    @NotBlank(message = "邮箱不能为空")
    private String email;

    private String nickname;

    private String phoneNumber;

    private String address;

}
