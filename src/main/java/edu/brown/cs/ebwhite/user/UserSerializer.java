package edu.brown.cs.ebwhite.user;

import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User> {
  public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.getUsername());
  }
}
