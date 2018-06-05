package log;

import com.jfinal.log.Log;
import org.slf4j.LoggerFactory;

/**
 * @author pfjia
 * @since 2018/6/4 20:02
 */


public class Slf4jLogger extends Log {

    private org.slf4j.Logger log;
//  private static final String callerFQCN = Slf4jLogger.class.getName();

    Slf4jLogger(Class<?> clazz) {
        log = LoggerFactory.getLogger(clazz);
    }

    Slf4jLogger(String name) {
        log = LoggerFactory.getLogger(name);
    }

    //(marker, this, level, msg,params, t);注意参数顺序
    @Override
    public void info(String message) {
        log.info(message);
    }

    @Override
    public void info(String message, Throwable t) {
        log.info(message, t);
    }

    @Override
    public void debug(String message) {
        log.debug(message);
    }

    @Override
    public void debug(String message, Throwable t) {
        log.debug(message, t);
    }

    @Override
    public void warn(String message) {
        log.warn(message);
    }

    @Override
    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    @Override
    public void error(String message) {
        log.error(message);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public void fatal(String message) {
        log.error(message);
    }

    @Override
    public void fatal(String message, Throwable t) {
        log.error(message, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled() {
        return log.isErrorEnabled();
    }
}

