package edu.brown.cs.ebwhite.user;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
/**
 * The User serializer specifies how the user is passed/translated with
 * GSON. Adds the username.
 * @author cchen5
 *
 */
public class UserSerializer implements JsonSerializer<User> {
  @Override
  public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
    return new JsonPrimitive(src.getUsername());
  }
}
