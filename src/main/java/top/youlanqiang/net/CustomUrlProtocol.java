package top.youlanqiang.net;

import sun.net.www.protocol.http.HttpURLConnection;

import java.io.IOException;
import java.net.*;

/**
 * @author youlanqiang
 * created in 2022/1/30 3:00 下午
 * 如果使用自定义的url protocol
 * 如 redis://localhost:6379
 * 系统会直接报错java.net.MalformedURLException: unknown protocol: redis
 * 所以要自定义一个URLStreamHandlerFactory
 */
public class CustomUrlProtocol {

    public static void main(String[] args) {

    }

    static {
        URL.setURLStreamHandlerFactory(new CustomURLStreamHandlerFactory());
    }



    public static class CustomURLStreamHandlerFactory implements URLStreamHandlerFactory{

        @Override
        public URLStreamHandler createURLStreamHandler(String protocol) {
            if(protocol.equals("local")){
                return new LocalStreamHandler();
            }
            return null;
        }
    }

    public static class LocalStreamHandler extends URLStreamHandler {

        @Override
        protected URLConnection openConnection(URL u) throws IOException {
            // ProxySelector.getDefault().select(u.toURI());
            return new HttpURLConnection(u, Proxy.NO_PROXY);
        }
    }
}


