package sb.xinxin.irc;

public enum IRCUserLevel
{
    FREE("Free"), 
    PAID("Paid"), 
    ADMINISTRATOR("Administrator");
    
    private final String name;
    
    private IRCUserLevel(final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static IRCUserLevel fromName(final String name) {
        for (final IRCUserLevel value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }
}
