package top.fuyuaaa.shadowpuppets.exceptions;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:32
 */
@SuppressWarnings("all")
public class AlipayException extends RuntimeException {
    private static final long serialVersionUID = -5059194705206839449L;
    public AlipayException(String message) {
        super(message);
    }
}
