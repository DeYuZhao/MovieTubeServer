package cn.edu.nju.movietubeserver.controller;

import cn.edu.nju.movietubeserver.api.RateAPI;
import cn.edu.nju.movietubeserver.model.dto.RateDetailDto;
import cn.edu.nju.movietubeserver.model.po.RateDetailPo;
import cn.edu.nju.movietubeserver.service.RateService;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import cn.edu.nju.movietubeserver.support.response.RestApiResponseUtil;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dc
 * @date 2020/2/11 21:33
 */
@RestController
@RequestMapping(path = "/api/rate")
public class RateController implements RateAPI
{

    @Autowired
    private RateService rateService;

    @Override
    @PreAuthorize("hasAuthority('rate:insert')")
    @PostMapping(path = "/insertRate")
    public RestApiResponse<Integer> insertRate(@RequestBody RateDetailDto rateDetailDto)
    {
        rateDetailDto.setCreateTime(LocalDateTime.now());
        rateDetailDto.setUpdateTime(LocalDateTime.now());
        return RestApiResponseUtil.createSuccessResponse(rateService.insertRate(RateDetailPo.valueOf(rateDetailDto)));
    }

    @Override
    @PreAuthorize("hasAuthority('rate:update')")
    @PostMapping(path = "/updateRateById")
    public RestApiResponse<Integer> updateRateById(@RequestBody RateDetailDto rateDetailDto)
    {
        rateDetailDto.setUpdateTime(LocalDateTime.now());
        return RestApiResponseUtil.createSuccessResponse(rateService.updateRateById(RateDetailPo.valueOf(rateDetailDto)));
    }
}
