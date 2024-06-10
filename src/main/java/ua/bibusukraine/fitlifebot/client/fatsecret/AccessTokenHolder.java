package ua.bibusukraine.fitlifebot.client.fatsecret;

import ua.bibusukraine.fitlifebot.client.fatsecret.payload.response.AccessTokenResponse;

import java.time.LocalDateTime;

public class AccessTokenHolder {
  private static final ThreadLocal<AccessTokenResponse> accessToken = new ThreadLocal<>();

  public static String getAccessToken() {
    return accessToken.get().getAccessToken();
  }

  public static void setAccessToken(AccessTokenResponse token) {
    accessToken.set(token);
  }

  public static boolean isAccessTokenExpired() {
    return accessToken.get() == null ||
        accessToken.get()
            .getExpirationTime()
            .isBefore(LocalDateTime.now());
  }
}
