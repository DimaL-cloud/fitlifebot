package ua.bibusukraine.fitlifebot.client.fatsecret.payload.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AccessTokenResponse {
  private final String accessToken;
  private final LocalDateTime expirationTime;

  @JsonCreator
  public AccessTokenResponse(@JsonProperty("access_token") String accessToken,
                             @JsonProperty("expires_in") Integer expiresIn) {
    this.accessToken = accessToken;
    this.expirationTime = LocalDateTime.now().plusMinutes(expiresIn);
  }

  public String getAccessToken() {
    return accessToken;
  }

  public LocalDateTime getExpirationTime() {
    return expirationTime;
  }
}
