package com.rnkrsoft.gitserver.http.mgr;

import com.rnkrsoft.gitserver.http.nanohttpd.protocols.http.IHTTPSession;
import com.rnkrsoft.gitserver.http.nanohttpd.protocols.http.NanoHTTPD;
import com.rnkrsoft.gitserver.http.nanohttpd.protocols.http.response.Response;
import com.rnkrsoft.gitserver.http.nanohttpd.protocols.http.response.Status;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
 
/**
 * Created by XDA on 2019/4/2.
 */
public class FileServer extends NanoHTTPD {
    //根目录
    private static  final String REQUEST_ROOT = "/";

    public FileServer(int serverPort){
        super(serverPort);
    }
    //当接受到连接时会调用此方法
    public Response serve(IHTTPSession session){
        return responseFile(session);
    }
    //对于请求文件的，返回下载的文件
    public Response responseFile(IHTTPSession session){
        try {
            //uri：用于标示文件资源的字符串，这里即是文件路径
            String uri = session.getUri();
            //文件输入流
            FileInputStream fis = new FileInputStream(uri);
            // 返回OK，同时传送文件，为了安全这里应该再加一个处理，即判断这个文件是否是我们所分享的文件，避免客户端访问了其他个人文件
            return Response.newFixedLengthResponse(Status.OK,"application/octet-stream",fis,fis.available());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response404(session,null);
    }
    //页面不存在，或者文件不存在时
    public Response response404(IHTTPSession session,String url) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html>body>");
        builder.append("Sorry,Can't Found" + url + " !");
        builder.append("</body></html>\n");
        return Response.newFixedLengthResponse(builder.toString());
    }
}