package ua.bibusukraine.fitlifebot.client.fatsecret;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import ua.bibusukraine.fitlifebot.client.fatsecret.payload.response.AccessTokenResponse;

import java.util.Base64;

@FeignClient(name = "fatsecretOauthClient",
    url = "https://oauth.fatsecret.com",
    configuration = {OauthClient.RequestInterceptorImpl.class})
public interface OauthClient {

  @PostMapping(path = "/connect/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  ResponseEntity<AccessTokenResponse> getAccessToken(@RequestPart("grant_type") String grantType,
                                                     @RequestPart("scope") String scope);

  @Component
  class RequestInterceptorImpl implements RequestInterceptor {
    private final String clientId;
    private final String clientSecret;

    public RequestInterceptorImpl(@Value("${fatsecret.clientId}") String clientId,
                                  @Value("${fatsecret.clientSecret}") String clientSecret) {
      this.clientId = clientId;
      this.clientSecret = clientSecret;
    }

    @Override
    public void apply(RequestTemplate template) {
      if (template.request().url().contains("/connect/token")) {
        String auth = "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
        template.header("Authorization", auth);
      }
    }
  }
}
