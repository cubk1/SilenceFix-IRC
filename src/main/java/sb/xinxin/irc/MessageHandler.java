package sb.xinxin.irc;

import java.util.function.*;
import com.google.gson.*;
import java.util.*;

import io.netty.channel.*;
import sb.xinxin.irc.netty.*;

public class MessageHandler extends SimpleChannelInboundHandler<JsonElement>
{
    private final Map<Integer, Consumer<JsonObject>> id2Func;
    private final IRCClient ircClient;
    
    public MessageHandler(final IRCClient ircClient) {
        this.id2Func = new HashMap<Integer, Consumer<JsonObject>>();
        this.ircClient = ircClient;
        this.id2Func.put(0, this::handleLog);
        this.id2Func.put(1, this::handleAuthResult);
        this.id2Func.put(2, this::handleChat);
        this.id2Func.put(4, this::handleUnauthenticated);
        this.id2Func.put(5, this::handleQueryPlayer);
        this.id2Func.put(6, this::handleMyUserInfo);
        this.id2Func.put(7, this::handleMuted);
        this.id2Func.put(8, this::handleChatFromOther);
        this.id2Func.put(9, this::handleChatFromServer);
        this.id2Func.put(10, this::handlePermissionDenied);
        this.id2Func.put(13, this::handleVersion);
    }


    protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final JsonElement msg) throws Exception {
        if (!msg.isJsonObject()) {
            return;
        }
        final JsonObject jo = msg.getAsJsonObject();
        final JsonElement idElement = jo.get("id");
        final JsonElement dataElement = jo.get("data");
        if (!idElement.isJsonPrimitive()) {
            return;
        }
        if (!dataElement.isJsonObject()) {
            return;
        }
        final JsonObject data = dataElement.getAsJsonObject();
        int id;
        try {
            id = idElement.getAsInt();
        }
        catch (NumberFormatException e) {
            return;
        }
        if (id == 3) {
            System.out.println("[IRC] handshake successfully.");
            channelHandlerContext.pipeline().addAfter("frame_decoder", "aes_decoder", (ChannelHandler)new AESDecoder(this.ircClient.aesKey));
            channelHandlerContext.pipeline().replace("rsa_encoder", "aes_encoder", (ChannelHandler)new AESEncoder(this.ircClient.aesKey));
            this.ircClient.sendPacket(Messages.createQueryVersion("LiquidBounce"));
            IRCClient.Instance.sendPacket(Messages.createRequestEmailCode("88888888"));

            // your spam etc here
        }
        else {
            this.id2Func.get(id).accept(data);
        }
    }
    
    public void handleLog(final JsonObject data) {
        final String message = getStringOrNull(data, "message");
        System.out.println("[server log] " + message);
    }
    
    public void handleAuthResult(final JsonObject data) {
        System.out.println("[auth result] " + data.toString());
    }
    
    public void handleChat(final JsonObject data) {
        final String message = getStringOrNull(data, "message");
        System.out.println("[chat message] " + message);
    }
    
    public void handleUnauthenticated(final JsonObject data) {
        System.out.println("[unauthenticated] " + data);
    }
    
    public void handleQueryPlayer(final JsonObject data) {
        System.out.println("[query player] " + data);
    }
    
    public void handleMyUserInfo(final JsonObject data) {
        System.out.println("[query user] " + data);
    }
    
    public void handleMuted(final JsonObject data) {
        System.out.println("[muted] " + data);
    }
    
    public void handleChatFromOther(final JsonObject data) {
        System.out.println("[pm] " + data);
    }
    
    public void handleChatFromServer(final JsonObject data) {
        System.out.println("[server chat] " + data);
    }
    
    public void handlePermissionDenied(final JsonObject data) {
        System.out.println("[permission denied] " + data);
    }
    
    public void handleClients(final JsonObject data) {
        System.out.println("[clients] " + data);
    }
    
    public void handleCommandOut(final JsonObject data) {
        final String message = getStringOrNull(data, "message");
        System.out.println("[command] " + data);
    }
    
    public void handleVersion(final JsonObject data) {
        final String version = getStringOrNull(data, "version");
        if (!"V8".equals(version)) {
            System.out.println("[outdated] " + data);
        }
    }
    
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        IRCClient.Instance.reconnectTimes = 0;
        System.out.println("[IRC] Connected");
    }
    
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        System.out.println("[IRC] Inactive");
    }
    
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {

        System.out.println("[IRC] Disconnected");
    }
    
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        cause.printStackTrace();
    }
    
    private static String getStringOrNull(final JsonObject data, final String name) {
        final JsonElement jsonElement = data.get(name);
        if (jsonElement == null || !jsonElement.isJsonPrimitive()) {
            return null;
        }
        return jsonElement.getAsString();
    }
}
