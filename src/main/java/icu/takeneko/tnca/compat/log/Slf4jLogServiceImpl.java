package icu.takeneko.tnca.compat.log;
//#if MC >= 11800

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;


public class Slf4jLogServiceImpl implements SimpleLogService {
    private final Logger logger = LogUtils.getLogger();

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