package task1;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClient {

    public static final String API_URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

    }




//    public static void aVoid() {
//        CloseableHttpClient httpClient = HttpClientBuilder.create()
//                .setDefaultRequestConfig(RequestConfig.custom()
//                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
//                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
//                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
//                        .build())
//                .build();
//    }

}