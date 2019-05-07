package top.fuyuaaa.shadowpuppets.common.utils.init;

/**
 * 评论随机
 * 用于初始化数据
 *
 * @author: fuyuaaa
 * @creat: 2019-05-05 16:32
 */
public class CommentGenerateUtils {

    private static String[] NOUN = {"质量", "与卖家描述的", "发货速度", "包装", "我对这次购物"};
    private static String[] ADJ = {"好", "很好", "非常好"};
    private static String[] DESCRIBE_ADJ = {"较为一致", "一致", "完全一致"};
    private static String[] EXPRESS_ADJ = {"快", "很快", "非常快"};
    private static String[] PACKAGE_ADJ = {"仔细", "严实", "非常仔细、严实"};
    private static String[] SATISFACTION_ADJ = {"较为满意", "很满意", "非常满意"};
    private static String[][] ALL_ADJ = {ADJ, DESCRIBE_ADJ, EXPRESS_ADJ,PACKAGE_ADJ,SATISFACTION_ADJ};

    public static String generateComment() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NOUN.length; i++) {
            sb.append(NOUN[i]).append(ALL_ADJ[i][Random7788Utils.getNum(0, 2)]).append("，");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(generateComment());
        }
    }
}
