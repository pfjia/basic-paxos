package log;

import com.jfinal.log.ILogFactory;
import com.jfinal.log.Log;

/**
 * @author pfjia
 * @since 2018/6/4 20:02
 */
public class Slf4jLogFactory implements ILogFactory {
    @Override
    public Log getLog(Class<?> clazz) {
        return new Slf4jLogger(clazz);
    }

    @Override
    public Log getLog(String name) {
        return new Slf4jLogger(name);
    }
}
