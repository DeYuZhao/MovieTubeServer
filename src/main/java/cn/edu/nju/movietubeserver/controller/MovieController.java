package cn.edu.nju.movietubeserver.controller;

import cn.edu.nju.movietubeserver.api.MovieAPI;
import cn.edu.nju.movietubeserver.constant.ESIndexFieldKey.Movie;
import cn.edu.nju.movietubeserver.model.domain.MovieIndexBean;
import cn.edu.nju.movietubeserver.model.dto.MovieDto;
import cn.edu.nju.movietubeserver.service.MovieService;
import cn.edu.nju.movietubeserver.service.RateService;
import cn.edu.nju.movietubeserver.service.UserService;
import cn.edu.nju.movietubeserver.support.response.RestApiResponse;
import cn.edu.nju.movietubeserver.support.response.RestApiResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dc
 * @date 2020/2/7 0:03
 */
@RestController
@RequestMapping(path = "/api/movie")
public class MovieController implements MovieAPI
{

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieIndexBean movieIndexBean;

    @Autowired
    private RateService rateService;

    @Autowired
    private UserService userService;

    @Override
    @GetMapping(path = "/getCountOfMoviesByTag")
    public RestApiResponse<Long> getCountOfMoviesByTag(@RequestParam String tag)
    {
        //TODO 对tag进行校验
        movieIndexBean.setIndexName(tag);
        return RestApiResponseUtil.createSuccessResponse(movieService.getCount());
    }

    @Override
    @GetMapping(path = "/listByTag")
    public RestApiResponse<Page<MovieDto>> listByTag(@RequestParam String tag,
        @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize)
    {
        //TODO 对tag进行校验
        movieIndexBean.setIndexName(tag);
        return RestApiResponseUtil.createSuccessResponse(movieService.listByPage(pageNo, pageSize)
            .map(this::setLocalRate));
    }

    @Override
    @GetMapping(path = "/getByMovieId")
    public RestApiResponse<MovieDto> getByMovieId(@RequestParam Long movieId)
    {
        return movieService.getByMovieIdFromAllIndices(movieId)
            .map(this::setLocalRate)
            .map(this::setMyRate)
            .map(RestApiResponseUtil::createSuccessResponse)
            .orElse(RestApiResponseUtil.createErrorResponse(String.format("movie not found, id [%s]", movieId)));
    }

    @Override
    @GetMapping(path = "/searchByKeyword")
    public RestApiResponse<Page<MovieDto>> searchByKeyword(@RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam String keyword)
    {
        return RestApiResponseUtil.createSuccessResponse(movieService.searchByKeyword(pageNo,
            pageSize,
            keyword,
            Movie.TITLE,
            Movie.DIRECTORS,
            Movie.CASTS).map(this::setLocalRate));
    }

    private MovieDto setLocalRate(MovieDto movieDto)
    {
        movieDto.setLocalRate(rateService.getLocalRateByMovieId(movieDto.getId()));
        return movieDto;
    }

    private MovieDto setMyRate(MovieDto movieDto)
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userId = userService.getUserByUsername(username).getUserId();
        movieDto.setMyRate(rateService.getMyRateByMovieId(userId, movieDto.getId()));
        return movieDto;
    }
}
