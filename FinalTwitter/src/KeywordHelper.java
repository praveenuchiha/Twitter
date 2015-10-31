import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class KeywordHelper {
	 String regex = "[wW][iI][nN]|[gG][aA][mM][eE]|[sS][pP][oO][rR][tT]|[lL][oO][sS][eE][fF][bB][lL][dD]|moive";
	    Pattern p = Pattern.compile(regex);
	    Pattern p1 = Pattern.compile("[wW][iI][nN]");
	    Pattern p2 = Pattern.compile("[gG][aA][mM][eE]");
	    Pattern p3 = Pattern.compile("[sS][pP][oO][rR][tT]");
	    Pattern p4 = Pattern.compile("[lL][oO][sS][eE]");
	    Pattern p5 = Pattern.compile("[rR][iI][pP]");
	    Pattern p6 = Pattern.compile("[fF][oO][oO][tT][bB][aA][lL][lL]");
	    Pattern p7 = Pattern.compile("[fF][oO][oO][dD]");
	    
	    public String iskeyword(String text) {
	    	String ret = new String();
	    	Matcher m = p.matcher(text);
			if (m.find()) {
				if (p1.matcher(text).find()) {
					ret = "win";
					return ret;
				} else if (p2.matcher(text).find()) {
					ret = "game";
					return ret;
				} else if (p3.matcher(text).find()) {
					ret = "sport";
					return ret;
				} else if (p4.matcher(text).find()) {
					ret = "lose";
					return ret;
				}else if (p5.matcher(text).find()) {
					ret = "RIP";
					return ret;
				} else if (p5.matcher(text).find()) {
					ret = "football";
					return ret;
				} else if (p5.matcher(text).find()) {
					ret = "food";
					return ret;
				}  else {
					ret = "movie";
					return ret;
				}
			}
			ret = "none";
			return ret;
		}
	 

}
