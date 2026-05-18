
package ${package}.web;


import ${package}.Application;
import ${package}.dao.mapper.BrandSeriesMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class SeriesTest {
    @Autowired
    private BrandSeriesMapper brandSeriesMapper;
    @Test
    public void test() {
        brandSeriesMapper.selectById(3411);
    }

}
