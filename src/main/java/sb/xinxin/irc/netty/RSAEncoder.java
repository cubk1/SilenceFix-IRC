package sb.xinxin.irc.netty;

import io.netty.handler.codec.*;
import io.netty.channel.*;
import java.util.*;
import javax.crypto.*;
import java.nio.*;
import io.netty.buffer.*;
import java.security.*;
import org.apache.commons.io.*;
import java.io.*;
import java.security.spec.*;

public class RSAEncoder extends MessageToMessageEncoder<ByteBuf>
{
    private static final Key RSA_PUBLIC_KEY;
    
    protected void encode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, RSAEncoder.RSA_PUBLIC_KEY);
        final ByteBuffer encryptBuffer = ByteBuffer.allocate(4 + cipher.getOutputSize(msg.readableBytes()));
        encryptBuffer.putInt(msg.readableBytes());
        cipher.doFinal(msg.nioBuffer(), encryptBuffer);
        encryptBuffer.position(0);
        out.add(Unpooled.wrappedBuffer(encryptBuffer));
    }
    
    static {
        try (final InputStream publicKeyIS = RSAEncoder.class.getResourceAsStream("/RSAKey")) {
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            final EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(IOUtils.toByteArray(publicKeyIS));
            RSA_PUBLIC_KEY = keyFactory.generatePublic(encodedKeySpec);
        }
        catch (Exception e) {
            throw new RuntimeException("Can't init key file", e);
        }
    }
}
