package icu.takeneko.tnca.compat.log;

public interface SimpleLogService {

    void info(String pattern, Object... args);
    void warn(String pattern, Object... args);
    void error(String pattern, Object... args);
}
