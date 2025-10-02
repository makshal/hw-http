package task2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpClient {

    public static final String API_KEY_URL = "https://api.nasa.gov/planetary/apod?api_key=2ORgaSMWPK4GzzuNQWdLffFRVSFLtBHmxTH1daO0";

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        try {
            CloseableHttpClient httpClient = createHttpClient(); // создаем того, кто будет возвращать данные
            HttpGet request_one = new HttpGet(API_KEY_URL); // создаем запрос к серверу
            request_one.setHeader("Accept", "application/json"); // это для того, чтобы ответ вернулся именно в json-формате
            CloseableHttpResponse response_one = httpClient.execute(request_one); // сюда кладем ответ от сервера
            try {
                String jsonResponse = EntityUtils.toString(response_one.getEntity());

                System.out.println("Message body: " + jsonResponse);
                System.out.println();

                AnswerFromNASA answerFromNASA = mapper.readValue(jsonResponse, AnswerFromNASA.class);
                String url = answerFromNASA.getUrl();
                String fileName = url.substring(url.lastIndexOf('/') + 1);

                HttpGet request_two = new HttpGet(url);
                request_two.setHeader("Accept", "image/*");
                CloseableHttpResponse response_two = httpClient.execute(request_two);
                try {
                    HttpEntity imageResponseHttp = response_two.getEntity();
                    try (InputStream inputStream = imageResponseHttp.getContent();
                         FileOutputStream outputStream = new FileOutputStream(fileName)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                } finally {
                    response_two.close();
                }
            } finally {
                response_one.close();
                httpClient.close();
            }
        } catch (IOException | ParseException e) {
            System.err.println(e.getMessage());
        }

    }

    public static CloseableHttpClient createHttpClient() {

        return HttpClientBuilder.create()
                .setUserAgent("MyApp2/1.0")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectionRequestTimeout(Timeout.ofSeconds(30)) // 5 секунд
                        .setResponseTimeout(Timeout.ofSeconds(60))   // 30 секунд
                        .setRedirectsEnabled(false)
                        .build())
                .build();

    }
}