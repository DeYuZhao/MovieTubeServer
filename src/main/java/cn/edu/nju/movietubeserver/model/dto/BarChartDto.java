package cn.edu.nju.movietubeserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhaodeyu
 * @classname BarChartDto
 * @description 图表DTO实体类
 * @date 2020-02-13 11:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarChartDto {
    private List<String> xAxisDataList;

    private List<List<Long>> yAxisDataLists;
}
