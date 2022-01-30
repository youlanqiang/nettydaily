package top.youlanqiang.net;

import java.io.IOException;
import java.net.URL;

/**
 * @author youlanqiang
 * created in 2021/12/3 5:52 下午
 */
public class TestURL {

  public static void main(String[] args) throws IOException {
      URL url = new URL("https://www.baidu.com/hello#test");
      System.out.println(url.getProtocol()); //https
      System.out.println(url.getPort()); // -1
      System.out.println(url.getDefaultPort()); // 443
      System.out.println(url.getHost());  // www.baidu.com
      System.out.println(url.getPath());  //  /hello
      System.out.println(url.getRef());  // test
      System.out.println(url.getQuery()); // null

      URL url1 = new URL("tts://localhost:220");
      System.out.println(url1.getContent());


  }
}
