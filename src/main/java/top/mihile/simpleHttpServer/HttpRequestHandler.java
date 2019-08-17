package top.mihile.simpleHttpServer;

import java.io.*;
import java.net.Socket;

public class HttpRequestHandler implements Runnable {
    private Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }


    public void run() {
        String line = null;
        BufferedReader br = null;
        BufferedReader reader = null;
        PrintWriter out = null;
        InputStream in = null;

        try{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String header = reader.readLine();
            // 由相对路径计算绝对路径
            String filePath = SimpleHttpServer.basePath+header.split(" ")[1];
            out = new PrintWriter(socket.getOutputStream());
            // 如果请求资源的后缀为jpg或者ico,则读取资源并输出
            if(filePath.endsWith("jpg") || filePath.endsWith("ico")){
                in = new FileInputStream(filePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i = 0;
                while ((i=in.read()) != -1){
                    baos.write(i);
                }
                byte[] array = baos.toByteArray();
                // 版本 空格 状态码 空格 原因短语 换行  一般\r\n
                out.println("HTTP/1.1 200 OK");
                // 头部域：头部域值 换行
                // 服务器名称为mihile server
                out.println("Server: MihileServer");
                // 媒体类型
                out.println("Content-Type: image/jpeg");
                // 指明发送给接收方的消息主体的大小
                out.println("Content-Length: "+array.length);
                // 空行
                out.println("");
                socket.getOutputStream().write(array,0,array.length);
            }else {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
                out = new PrintWriter(socket.getOutputStream());
                out.println("HTTP/1.1 200 OK");
                out.println("server: MihileServe");
                out.println("Content-Type: text/html; charset=UTF-8");
                out.println("");
                while ((line=br.readLine())!=null){
                    out.println(line);
                }
            }
            out.flush();
        }catch (Exception e){
            out.println("HTTP/1.1 500");
            out.println("");
            out.flush();

        }finally {
            SimpleHttpServer.close(br,reader,out,in,socket);
        }
    }
}
