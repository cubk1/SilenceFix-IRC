package sb.xinxin.irc.netty;

import io.netty.handler.codec.*;
import java.security.*;
import io.netty.channel.*;
import java.util.*;
import javax.crypto.*;
import java.nio.*;
import io.netty.buffer.*;

public class AESDecoder extends MessageToMessageDecoder<ByteBuf>
{
    private final Key key;
    
    public AESDecoder(final Key key) {
        this.key = key;
    }
    
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(2, this.key);
        final int rawDataSize = in.readInt();
        final ByteBuffer decryptBuffer = ByteBuffer.allocate(cipher.getOutputSize(in.readableBytes()));
        cipher.doFinal(in.nioBuffer(), decryptBuffer);
        decryptBuffer.position(0);
        out.add(Unpooled.wrappedBuffer(decryptBuffer).slice(0, rawDataSize));
    }
}
