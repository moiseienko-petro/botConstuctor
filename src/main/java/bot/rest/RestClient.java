package bot.rest;

import bot.configs.BotConfig;
import bot.utils.UrlUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Component
public class RestClient {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private BotConfig botConfig;

    public JsonNode getForObject(String url, Map<String, String> values) throws Exception {
        ResponseEntity<String> response
                = restTemplate.getForEntity(botConfig.getPathToEnv() + UrlUtil.formatUrl(url, values), String.class);
        return getResponse(response);
    }

    public JsonNode postForObject(String url, Object request) throws Exception {
        ResponseEntity<String> response
                = restTemplate.postForEntity(botConfig.getPathToEnv() + url, request, String.class);
        return getResponse(response);
    }

    public JsonNode putForObject(String url, Object request) throws Exception {
        HttpEntity requestEntity = new HttpEntity(request);
        ResponseEntity<String> response
                = restTemplate.exchange(botConfig.getPathToEnv() + url, HttpMethod.PUT, requestEntity, String.class);
        return getResponse(response);

    }

    public JsonNode deleteForObject(String url, Object request) throws Exception {
        HttpEntity requestEntity = new HttpEntity(request);

        ResponseEntity<String> response
                = restTemplate.exchange(botConfig.getPathToEnv() + url, HttpMethod.DELETE, requestEntity, String.class);
        return getResponse(response);
    }

    public void executeForStatus(String url, String method) throws Exception {
        ResponseEntity result = null;
        String fullUrl = botConfig.getPathToEnv() + url;
        switch (method) {
            case "GET":
                result = restTemplate.getForEntity(fullUrl, String.class);
                break;
            case "POST":
                result = restTemplate.postForEntity(fullUrl, null, String.class);
                break;
            case "PUT":
                restTemplate.put(fullUrl, null);
                return;
            case "DELETE":
                restTemplate.delete(fullUrl);
                return;
        }
        if (result == null || !result.getStatusCode().is2xxSuccessful()) {
            throw new Exception();
        }
    }

    private JsonNode getResponse(ResponseEntity<String> responseEntity) throws Exception {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = null;
            try {
                root = mapper.readTree(responseEntity.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return root;
        } else {
            throw new Exception("Smth went wrong");
        }
    }
}
