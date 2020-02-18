package cn.edu.nju.movietubeserver.controller;

import cn.edu.nju.movietubeserver.api.TagAPI;
import cn.edu.nju.movietubeserver.model.dto.TagDto;
import cn.edu.nju.movietubeserver.service.TagService;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import cn.edu.nju.movietubeserver.support.response.RestApiResponseUtil;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dc
 * @date 2020/2/8 15:18
 */
@RestController
@RequestMapping(path = "/api/tag")
public class TagController implements TagAPI
{

    @Autowired
    private TagService tagService;

    @Override
    @PreAuthorize("hasAuthority('tag:list')")
    @GetMapping(path = "/getTagsMap")
    public RestApiResponse<Map<String, List<TagDto>>> getTagsMap()
    {
        return RestApiResponseUtil.createSuccessResponse(tagService.getTagsMap());
    }
}
