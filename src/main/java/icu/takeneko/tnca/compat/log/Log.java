package icu.takeneko.tnca.compat.log;

public class Log {
    public static SimpleLogService getLogService(){
        //#if MC > 11800
        return new Slf4jLogServiceImpl();
        //#else
        //$$ return new Log4j2LogServiceImpl();
        //#endif
    }
}
