
package ${package}.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 品牌车系表
 * </p>
 *
 * @author fangli
 * @since 2024-12-08 11:27:30
 */
@Getter
@Setter
@TableName("brand_series")
public class BrandSeriesDO {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车系id
     */
    @TableField("series_id")
    private Integer seriesId;

    /**
     * 车系名称
     */
    @TableField("series_name")
    private String seriesName;

    /**
     * 车系logo
     */
    @TableField("series_logo")
    private String seriesLogo;

    /**
     * 车系首字母
     */
    @TableField("series_first_letter")
    private String seriesFirstLetter;

    /**
     * 品牌id
     */
    @TableField("brand_id")
    private Integer brandId;

    /**
     * 品牌名称
     */
    @TableField("brand_name")
    private String brandName;

    /**
     * 厂商首字母
     */
    @TableField("fct_first_letter")
    private String fctFirstLetter;

    /**
     * 厂商id
     */
    @TableField("fct_id")
    private Integer fctId;

    /**
     * 厂商名称
     */
    @TableField("fct_name")
    private String fctName;

    /**
     * 车系级别id
     */
    @TableField("level_id")
    private Integer levelId;

    /**
     * 车系级别名称
     */
    @TableField("level_name")
    private String levelName;

    /**
     * 是否删除 0 正常 1 删除
     */
    @TableField("is_del")
    @TableLogic
    private Integer isDel;

    /**
     * 新增时间
     */
    @TableField("create_stime")
    private LocalDateTime createStime;

    /**
     * 变更时间
     */
    @TableField("modified_stime")
    private LocalDateTime modifiedStime;
}
