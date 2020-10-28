package client;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MyHttpClient {
    public static void main (String[]args){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            URI uri = new URIBuilder("http://localhost:8808/test").build();
            HttpGet get = new HttpGet(uri);
            CloseableHttpResponse response = client.execute(get);
            System.out.println(response.toString());
            System.out.println(EntityUtils.toString(response.getEntity(),"UTF-8"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
