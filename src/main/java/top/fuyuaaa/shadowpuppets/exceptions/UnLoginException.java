package top.fuyuaaa.shadowpuppets.exceptions;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:32
 */
public class UnLoginException extends RuntimeException {

    private static final long serialVersionUID = -4676762148330373753L;

    public UnLoginException(String message) {
        super(message);
    }
}
