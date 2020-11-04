package io.github.kimmking.gateway.outbound.httpclient;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class SimpleHttpOutBoundHandler {
    private CloseableHttpClient httpClient;
    private String backendUrl;
    private static Logger logger =  LoggerFactory.getLogger(SimpleHttpOutBoundHandler.class);

    public SimpleHttpOutBoundHandler(String backendUrl){
        this.backendUrl = backendUrl.endsWith("/")?backendUrl.substring(0,backendUrl.length()-1):backendUrl;
        httpClient = HttpClients.createDefault();
    }

    public void handle(final FullHttpRequest request, final ChannelHandlerContext ctx){
        FullHttpResponse fullHttpResponse = null;
        try {
            String url = backendUrl+request.uri();
            URI uri = new URIBuilder(url).build();
            HttpGet get = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(get);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
            fullHttpResponse = handleReponse(request,response,ctx);
        } catch (URISyntaxException | IOException e) {
            logger.error("处理接口出错", e);
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        }finally {
            if (request != null) {
                if (!HttpUtil.isKeepAlive(request)) {
                    ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    fullHttpResponse.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(fullHttpResponse);
                }
            }
        }


    }
    public FullHttpResponse handleReponse(FullHttpRequest request, HttpResponse response,ChannelHandlerContext ctx){
        FullHttpResponse fullHttpResponse = null;
        String value = null;
        try {
            value = EntityUtils.toString(response.getEntity(),"UTF-8");
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            fullHttpResponse.headers().set("Content-Type", "application/json");
            fullHttpResponse.headers().setInt("Content-Length", fullHttpResponse.content().readableBytes());
        } catch (IOException e) {
            logger.error("处理接口出错", e);
            fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        }finally {
            return fullHttpResponse;
        }
    }
}
