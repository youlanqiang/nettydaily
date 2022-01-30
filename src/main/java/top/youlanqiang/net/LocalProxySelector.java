package top.youlanqiang.net;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author youlanqiang
 * created in 2021/12/3 6:33 下午
 * 实现java的代理对象
 */
public class LocalProxySelector extends ProxySelector {

  public static void main(String[] args) {
      //每个jvm都只有一个ProxySelector
      //使用方法如下：
      ProxySelector proxySelector = new LocalProxySelector();
      ProxySelector.setDefault(proxySelector);

  }

    // 禁止代理集合
    List<URI> failed = new ArrayList<>();

  /**
   * @param uri 表示需要连接的uri
   * @return 代理对象集合
   */
  @Override
  public List<Proxy> select(URI uri) {
      List<Proxy> result = new ArrayList<>();
      if(failed.contains(uri) || !"http".equals(uri.getScheme())){
          //不代理集合中存在对应uri 或 不是http协议
          result.add(Proxy.NO_PROXY);
      }else{
          //代理服务器的 address
          SocketAddress socketAddress =
                  new InetSocketAddress("proxy.example.com", 8000);
          //代理对象
          Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
            result.add(proxy);
      }
        return result;
    }

    /**
     * 回调方法
     * 代理对象连接出错时会调用
     */
    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        failed.add(uri);
    }
}
