package icu.takeneko.tnca.compat.log;
//#if MC < 11800

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log4j2LogServiceImpl implements SimpleLogService {

    private Logger logger = LogManager.getLogger("TNCA");

    @Override
    public void info(String pattern, Object... args) {
        logger.info(pattern, args);
    }

    @Override
    public void warn(String pattern, Object... args) {
        logger.warn(pattern, args);
    }

    @Override
    public void error(String pattern, Object... args) {
        logger.error(pattern, args);
    }
}
//#endif