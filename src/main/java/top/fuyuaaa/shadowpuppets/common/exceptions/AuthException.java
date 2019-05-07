package top.fuyuaaa.shadowpuppets.common.exceptions;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:32
 */
public class AuthException extends RuntimeException {
    private static final long serialVersionUID = 7928466198334087404L;
    public AuthException(String message) {
        super(message);
    }
}
