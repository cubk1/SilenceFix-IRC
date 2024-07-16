package sb.xinxin.irc;

import javax.crypto.*;
import java.util.*;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.*;
import io.netty.channel.*;
import sb.xinxin.irc.netty.*;

public class IRCClient
{
    public static IRCClient Instance;
    public int reconnectTimes;
    public final Map<UUID, IRCUser> ircUserMap;
    final Map<String, Object> callbackMap;
    EventLoopGroup workerGroup;
    Channel channel;
    public SecretKey aesKey;
    
    public IRCClient() {
        this.ircUserMap = new HashMap<>();
        this.callbackMap = new HashMap<>();
    }

    public void connect() throws Exception{
        workerGroup = new NioEventLoopGroup();
        aesKey = genAESKey();
        Bootstrap b = new Bootstrap();
        b.group(workerGroup);
        b.channel(NioSocketChannel.class);
        b.option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline()
                        .addLast("frame_encoder",new FrameEncoder())
                        .addLast("rsa_encoder",new RSAEncoder())
                        .addLast("frame_decoder",new FrameDecoder())
                        .addLast("string_to_json",new StringEncoder())
                        .addLast("json_to_string",new StringDecoder())
                        .addLast(new JsonToStringEncoder())
                        .addLast(new StringToJsonDecoder());
                ch.pipeline().addLast(new MessageHandler(Instance));
            }
        });
        ChannelFuture f = b.connect("cn-wx.kuangmoge.xyz", 41201).sync();
        channel = f.channel();
    }
    
    public static void create(){
        Instance = new IRCClient();
    }
    
    public void shutdown() {
        if (this.channel != null && this.channel.isOpen()) {
            this.channel.close();
        }
        if (this.workerGroup != null && !this.workerGroup.isShutdown()) {
            this.workerGroup.shutdownGracefully();
        }
    }
    
    public void sendPacket(final Object data) {
        final Channel channel = this.channel;
        if (channel != null && channel.isOpen()) {
            if (channel.eventLoop().inEventLoop()) {
                channel.writeAndFlush(data).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
            else {
                channel.eventLoop().submit(() -> this.sendPacket(data));
            }
        }
    }

    private static SecretKey genAESKey() throws Exception  {
        return KeyGenerator.getInstance("AES").generateKey();
    }

    public static String generateHardwareId() throws Exception{
        return "sb";
    }
}
