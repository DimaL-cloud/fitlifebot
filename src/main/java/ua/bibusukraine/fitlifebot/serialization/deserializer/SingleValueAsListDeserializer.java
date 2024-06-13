package ua.bibusukraine.fitlifebot.serialization.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ua.bibusukraine.fitlifebot.client.fatsecret.payload.response.ProductResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleValueAsListDeserializer extends JsonDeserializer<List<ProductResponse>> {

  @Override
  public List<ProductResponse> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
    JsonNode node = p.getCodec().readTree(p);
    List<ProductResponse> list = new ArrayList<>();
    if (node instanceof ArrayNode) {
      for (JsonNode element : node) {
        ProductResponse product = p.getCodec().treeToValue(element, ProductResponse.class);
        list.add(product);
      }
    } else {
      ProductResponse product = p.getCodec().treeToValue(node, ProductResponse.class);
      list.add(product);
    }
    return list;
  }

}
