package sb.xinxin.irc;

import javax.crypto.*;
import com.google.gson.*;

import java.util.Base64;

public class Messages
{

    public static JsonObject createHandshake(SecretKey secretKey) {
        JsonObject data = new JsonObject();
        data.addProperty("key", Base64.getEncoder().encodeToString(secretKey.getEncoded()));
        return pack(0, data);
    }

    public static JsonObject createRegister(String username, String password, String hardwareId, String qq, String code)
    {
        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        data.addProperty("password", password);
        data.addProperty("hardwareId", hardwareId);
        data.addProperty("qq", qq);
        data.addProperty("code", code);
        return pack(1, data);
    }

    public static JsonObject createLogin(String username, String password, String hardwareId){
        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        data.addProperty("password", password);
        data.addProperty("hardwareId", hardwareId);
        return pack(2, data);
    }
    public static JsonObject createChat(final String message) {
        final JsonObject data = new JsonObject();
        data.addProperty("message", message);
        return pack(3, data);
    }
    
    public static JsonObject createSetMinecraftProfile(final String mcUUID) {
        final JsonObject data = new JsonObject();
        data.addProperty("mcUUID", mcUUID);
        return pack(4, data);
    }
    
    public static JsonObject createQueryPlayer(final String mcUUID, final int type) {
        final JsonObject data = new JsonObject();
        data.addProperty("mcUUID", mcUUID);
        data.addProperty("type", (Number)type);
        return pack(5, data);
    }
    
    public static JsonObject createRequestEmailCode(final String qq) {
        final JsonObject data = new JsonObject();
        data.addProperty("qq", qq);
        return pack(6, data);
    }
    
    public static JsonObject createChatToOther(final String name, final String message) {
        final JsonObject data = new JsonObject();
        data.addProperty("name", name);
        data.addProperty("message", message);
        return pack(7, data);
    }
    
    public static JsonObject createQueryClients() {
        return pack(8, new JsonObject());
    }
    
    public static JsonObject createCommand(final String command) {
        final JsonObject data = new JsonObject();
        data.addProperty("command", command);
        return pack(9, data);
    }
    
    public static JsonObject createQueryVersion(final String clientName) {
        final JsonObject data = new JsonObject();
        data.addProperty("clientName", clientName);
        return pack(10, data);
    }
    
    private static JsonObject pack(final int id, final JsonObject data) {
        final JsonObject jo = new JsonObject();
        jo.addProperty("id", (Number)id);
        jo.add("data", (JsonElement)data);
        return jo;
    }
}
