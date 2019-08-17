package top.mihile.simpleHttpServer;


import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SimpleHttpServer {
    /**
     * 根目录
     */
    static String basePath;
    /**
     * 服务套接字
     */
    static ServerSocket serverSocket;
    /**
     * 监听的端口
     */
    static int port;

    static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 设置服务器要监听的端口号
     * @param port 要监听的端口号，1~65535
     */
    public static void setPort(int port){
        if(port>0&&port<65536){
            SimpleHttpServer.port = port;
        }
    }

    /**
     * 设置服务器根目录
     * @param basePath 服务器根目录(必须存在且是文件夹)
     */
    public static void setBasePath(String basePath){
        if(basePath!=null && new File(basePath).exists() && new File(basePath).isDirectory()){
            SimpleHttpServer.basePath = basePath;
        }
    }

    /**
     * 启动SimpleHttpServer
     * @throws IOException
     */
    public static void start() throws IOException {
        // 启动服务端Socket
        serverSocket = new ServerSocket(port);
        Socket socket = null;
        // 每监听到一个客户端，往线程池中开启一个任务
        while ((socket = serverSocket.accept()) != null){
            // 往线程池中加入任务
            executorService.execute(new HttpRequestHandler(socket));
        }
        serverSocket.close();
    }


    /**
     * 通用的关闭方法
     * @param closeables
     */
    public static void close(Closeable...closeables){
        if(closeables != null){
            for (Closeable closeable : closeables) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
