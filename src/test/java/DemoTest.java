import com.xdq.core.utils.PathUtils;
import org.junit.Test;

import java.util.List;

/**
 * <b><code>DemoTest</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/22 17:33.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
public class DemoTest {

    @Test
    public void test(){
        String path = "xdq";
        String time = "201902031000";

        System.out.println(PathUtils.getPath(path,time));

    }
}
