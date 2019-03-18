import com.xdq.core.dao.AreaDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * <b><code>OracleTest</code></b>
 * <p/>
 * 测试oracle数据的连接
 * <p/>
 * <b>Creation Time:</b> 2019/3/14 16:03.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class OracleTest {
    @Autowired
    private AreaDao areaDao;

    @Test
    public void test(){
        System.out.println(areaDao.showCount());
    }
}
