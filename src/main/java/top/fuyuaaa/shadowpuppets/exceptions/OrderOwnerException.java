package top.fuyuaaa.shadowpuppets.exceptions;

/**
 * @author: fuyuaaa
 * @creat: 2019-04-18 16:32
 */
public class OrderOwnerException extends RuntimeException {
    private static final long serialVersionUID = -5059194705206839449L;
    public OrderOwnerException(String message) {
        super(message);
    }
}
