package sb.xinxin.irc;

public class IRCUser
{
    public IRCUserLevel level;
    public String name;
    public String rank;
    
    public IRCUser(final IRCUserLevel level, final String name, final String rank) {
        this.level = level;
        this.name = name;
        this.rank = rank;
    }
}
