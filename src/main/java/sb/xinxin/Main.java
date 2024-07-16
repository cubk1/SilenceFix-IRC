package sb.xinxin;

import sb.xinxin.irc.IRCClient;
import sb.xinxin.irc.Messages;

public class Main {
    public static void main(String[] args) {
        IRCClient.create();
        try {
            IRCClient.Instance.connect();
            IRCClient.Instance.sendPacket(Messages.createHandshake(IRCClient.Instance.aesKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
