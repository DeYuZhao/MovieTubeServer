import cn.edu.nju.movietubeserver.MovieTubeServerMain;
import cn.edu.nju.movietubeserver.constant.ESIndexFieldKey;
import cn.edu.nju.movietubeserver.constant.ESIndexFieldKey.Movie;
import cn.edu.nju.movietubeserver.model.dto.MovieDto;
import cn.edu.nju.movietubeserver.service.MovieService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author dc
 * @date 2019/12/23 15:37
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MovieTubeServerMain.class)
public class ElasticSearchTest
{

    @Autowired
    private MovieService movieService;

    @Test
    public void listAll()
    {
        Page<MovieDto> movieList = movieService.listByPage(0, 100);
        System.out.println(movieList.getTotalElements());
    }

    @Test
    public void listAllByPage()
    {
        Page<MovieDto> page = movieService.listByPage(1, 100);
        System.out.println(page.getTotalElements());
        System.out.println(page.getContent().size());
    }

    @Test
    public void getById()
    {
        Long movieId = 4917697L;
        movieService.getByPrimaryKey(movieId).ifPresent(System.out::println);
    }

    @Test
    public void getByTitle()
    {
        Page<MovieDto> moviePage = movieService.searchByKeyword(0, 10, "Happy", ESIndexFieldKey.Movie.TITLE);
        moviePage.getContent().stream().forEach(System.out::println);
    }

    @Test
    public void getByDirector()
    {
        Page<MovieDto> moviePage = movieService.searchByKeyword(0, 10, "霍曼·顾皮勒", Movie.DIRECTORS);
        System.out.println(moviePage.getContent().size());
        moviePage.getContent().stream().forEach(System.out::println);
    }
}
