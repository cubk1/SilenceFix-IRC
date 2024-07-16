package sb.xinxin.irc.netty;

import io.netty.handler.codec.*;
import io.netty.buffer.*;
import io.netty.channel.*;

public class FrameEncoder extends MessageToByteEncoder<ByteBuf>
{
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final ByteBuf out) {
        out.writeInt(msg.readableBytes());
        out.writeBytes(msg);
    }
}
