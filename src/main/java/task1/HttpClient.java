package task1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.IOException;
import java.util.List;

public class HttpClient {

    public static final String API_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        try {
            CloseableHttpClient httpClient = createHttpClient(); // создаем того, кто будет возвращать данные
            HttpGet request = new HttpGet(API_URL); // создаем запрос к API_URL
            request.setHeader("Accept", "application/json"); // это для того, чтобы запрос вернулся именно в json-формате
            CloseableHttpResponse response = httpClient.execute(request); // сюда кладем ответ от сервера
            try {
                System.out.println("Заголовки");
                for (var header : response.getHeaders()) {
                    System.out.println("    " + header.getName() + " : " + header.getValue());
                }

                String jsonResponse = EntityUtils.toString(response.getEntity());
                System.out.println("Message body: " + jsonResponse);
                System.out.println();

                System.out.println("Факты, при которых ");
                List<FactAboutCat> facts = mapper.readValue(jsonResponse, new TypeReference<List<FactAboutCat>>() {
                });
                facts.stream()
                        .filter(v -> v.getUpvotes() != null && v.getUpvotes() > 0)
                        .forEach(v -> {
                            System.out.println("id=" + v.getId());
                            System.out.println("text=" + v.getText());
                            System.out.println("type=" + v.getType());
                            System.out.println("user=" + v.getUser());
                            System.out.println("upvotes=" + v.getUpvotes());
                            System.out.println();
                        });
            } finally {
                response.close();
                httpClient.close();
            }
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
        }

    }

    public static CloseableHttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setUserAgent("MyApp/1.0")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofSeconds(5)) // 5 секунд
                        .setResponseTimeout(Timeout.ofSeconds(30))   // 30 секунд
                        .setRedirectsEnabled(false)
                        .build())
                .build();
    }

}