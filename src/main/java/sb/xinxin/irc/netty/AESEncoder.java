package sb.xinxin.irc.netty;

import io.netty.handler.codec.*;
import java.security.*;
import io.netty.channel.*;
import java.util.*;
import javax.crypto.*;
import java.nio.*;
import io.netty.buffer.*;

public class AESEncoder extends MessageToMessageEncoder<ByteBuf>
{
    private final Key key;
    
    public AESEncoder(final Key key) {
        this.key = key;
    }
    
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(1, this.key);
        final ByteBuffer encryptBuffer = ByteBuffer.allocate(4 + cipher.getOutputSize(msg.readableBytes()));
        encryptBuffer.putInt(msg.readableBytes());
        cipher.doFinal(msg.nioBuffer(), encryptBuffer);
        encryptBuffer.position(0);
        out.add(Unpooled.wrappedBuffer(encryptBuffer));
    }
}
