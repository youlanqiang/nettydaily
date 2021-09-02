package top.youlanqiang.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 通过实时黑洞列表(real-time blackhole lists, RBL)来查询ip地址是否是垃圾邮件发送者
 * 如查询 207.87.34.17  的ip是不是垃圾邮件的发送者 只需要查询dns中是否存在 17.34.87.207.sbl.spamhaus.org
 * @author youlanqiang
 * created in 2021/8/28 19:42
 */
public class SpamCheck {

    public static final String BLACKHOLE = "sbl.spamhaus.org";

    public static void main(String[] args) {
        String[] lists = new String[]{"207.34.56.23"};

        for (String list : lists) {
            System.out.println(list + " is " + isSpammer(list));
        }
    }

    private static boolean isSpammer(String ip){
        try {
            InetAddress address = InetAddress.getByName(ip);
            byte[] quad = address.getAddress();
            StringBuilder query = new StringBuilder(BLACKHOLE);
            for(byte octet : quad){
                int unsignedByte = octet < 0 ? octet + 256 : octet;
                query.insert(0, unsignedByte + ".");
            }
            InetAddress.getByName(query.toString());
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }


}
