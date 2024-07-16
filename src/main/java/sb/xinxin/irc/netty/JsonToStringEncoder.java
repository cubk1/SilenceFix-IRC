package sb.xinxin.irc.netty;

import io.netty.handler.codec.*;
import io.netty.channel.*;
import java.util.*;
import com.google.gson.*;

public class JsonToStringEncoder extends MessageToMessageEncoder<JsonElement>
{
    private static final Gson GSON;
    
    protected void encode(final ChannelHandlerContext ctx, final JsonElement msg, final List<Object> out) {
        out.add(JsonToStringEncoder.GSON.toJson(msg));
    }
    
    static {
        GSON = new GsonBuilder().create();
    }
}
