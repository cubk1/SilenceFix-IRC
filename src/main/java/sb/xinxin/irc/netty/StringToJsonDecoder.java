package sb.xinxin.irc.netty;

import io.netty.handler.codec.*;
import com.google.gson.*;
import io.netty.channel.*;
import java.util.*;

public class StringToJsonDecoder extends MessageToMessageDecoder<String>
{
    private final JsonParser parser;
    
    public StringToJsonDecoder() {
        this.parser = new JsonParser();
    }
    
    protected void decode(final ChannelHandlerContext ctx, final String msg, final List<Object> out) {
        out.add(this.parser.parse(msg));
    }
}
