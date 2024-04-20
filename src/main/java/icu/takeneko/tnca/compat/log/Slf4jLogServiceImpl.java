package icu.takeneko.tnca.compat.log;
//#if MC >= 11800

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Slf4jLogServiceImpl implements SimpleLogService {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    private final Logger logger = LoggerFactory.getLogger(STACK_WALKER.getCallerClass());

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