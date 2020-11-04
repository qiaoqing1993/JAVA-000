package io.github.kimmking.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class SimpleHttpRequestFilter implements HttpRequestFilter {
    private String proxyServer;
    public SimpleHttpRequestFilter(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
       // fullRequest.headers().set("nio","jiayipan");
    }
}
