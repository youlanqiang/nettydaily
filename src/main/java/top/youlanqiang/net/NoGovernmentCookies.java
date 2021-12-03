package top.youlanqiang.net;

import java.net.*;

/**
 * @author youlanqiang
 * created in 2021/12/3 7:58 下午
 * java net 对cookie的扩展
 */
public class NoGovernmentCookies implements CookiePolicy {

  public static void main(String[] args) {
    // 创建一个cookie
    HttpCookie cookie = new HttpCookie("key", "value");

    // 安装一个cookie manager 对于使用url类连接的http服务器，都会在后续中发送和使用这些cookie
      CookieManager manager = new CookieManager();
      manager.setCookiePolicy(new NoGovernmentCookies());
    CookieHandler.setDefault(manager);

    // 可以在本地上存放和获取cookie
    CookieStore cookieStore = manager.getCookieStore();

  }

    @Override
    public boolean shouldAccept(URI uri, HttpCookie cookie) {
      if(uri.getAuthority().toLowerCase().endsWith(".gov")){
          return false;
      }
        return true;
    }
}
