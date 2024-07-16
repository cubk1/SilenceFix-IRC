package sb.xinxin.irc.netty;

import io.netty.handler.codec.*;
import io.netty.channel.*;
import io.netty.buffer.*;
import java.util.*;

public class FrameDecoder extends ByteToMessageDecoder
{
    private int length;
    
    public FrameDecoder() {
        this.length = -1;
    }
    
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        if (this.length == -1) {
            if (in.readableBytes() >= 4) {
                this.length = in.readInt();
            }
        }
        else if (in.readableBytes() >= this.length) {
            out.add(in.readSlice(this.length).retain());
            this.length = -1;
        }
    }
}
