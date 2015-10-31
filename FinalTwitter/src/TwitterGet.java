import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.FilterQuery;

import java.io.IOException;

import twitter4j.GeoLocation;
//import twitter4j.Location;
// bY PRAVEEN (PKK236) AND REESHABH AGARWAL.
/**
 * <p>This is a code example of Twitter4J Streaming API - sample method support.<br>
 * Usage: java twitter4j.examples.PrintSampleStream<br>
 * </p>
 *
 * 
 */
public final class TwitterGet {
	protected static DynamoDB twitterDynamoDB = new DynamoDB();
	protected static KeywordHelper KeywordHelper = new KeywordHelper();
	private static long count = 0;
	
    /**
     * Main entry of this application.
     *
     * @param args
     */
	private static ConfigurationBuilder cb;
	private static TwitterStream twitterStream;
	
    public static void main(String[] args) throws TwitterException,IOException {
     
     
    	//just fill this
    	 cb = new ConfigurationBuilder();
         cb.setDebugEnabled(true)
           .setOAuthConsumerKey("")
           .setOAuthConsumerSecret("")
           .setOAuthAccessToken("")
           .setOAuthAccessTokenSecret("");
         

         count = twitterDynamoDB.findId("twitterTable") + 1;
         twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
         if(!twitterDynamoDB.testAndCheck("twitterTable")) {
         	System.out.println("There is no existing twitterTable,please create one first");
         	return;
         }
         stream();
    }
     public static void stream() throws TwitterException, IOException{   
       
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {
            	GeoLocation gl = status.getGeoLocation();
            	//System.out.println("I am inside");
            	 String userName = status.getUser().getName();
		    	   	String text = status.getText();
		    	   	String location = null;
		    	/*   	long ID = status.getId();
		    	   	GeoLocation geoLocation=null;
		    	   	double geoLocationLat = 361 ;
		    	   	double geoLocationLong =361 ;
		    	   	String geoLat = null;
		    	   	String geoLong = null;
		    	   	String place =status.getPlace().getFullName();
		            
		            if( &&status.getPlace().getFullName()!=null){
		            	 		            	
		       
		            }*/
		            if( status.getUser().getLocation()!=null){
		            	 location = status.getUser().getLocation();
		           
		            }
		           // GeoLocation gl = status.getGeoLocation();
		            if (gl!=null && status.getUser() != null){
		            	Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
		            	item.put("id", new AttributeValue().withN(Long.toString(count)));
		            	 item.put("username", new AttributeValue().withS(status.getUser().getName()));
		            	 item.put("text",new AttributeValue().withS(status.getText()));
		            	 item.put("timestamp", new AttributeValue().withS(status.getCreatedAt().toString()));
	                      item.put("latitude", new AttributeValue().withN(Double.toString(gl.getLatitude())));
	                      item.put("longtitude", new AttributeValue().withN(Double.toString(gl.getLongitude())));
		            	 item.put("Location",new AttributeValue().withS(location));
		            	 item.put("url", new AttributeValue().withS(status.getSource()));
		            	 item.put("keyword", new AttributeValue().withS(KeywordHelper.iskeyword(status.getText())));
		            	 item.put("TimeZone",new AttributeValue().withS(status.getUser().getTimeZone()));
		            	 count++;
		            	 twitterDynamoDB.addItem("twitterTable", item);
		            	
		            	 System.out.println(userName + " : " + text);
		            	 System.out.println("Location:  "+ location);
		            	 System.out.println("TimeZone:  "+ status.getUser().getTimeZone());
		            	
		            }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
               // System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
               System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:" + upToStatusId);
            }

            @Override
            public void onStallWarning(StallWarning warning) {
                System.out.println("Got stall warning:" + warning);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        FilterQuery q = new FilterQuery();
        double[][] locations = {{68.1,8.06}};
	    q.locations(locations);		
	    twitterStream.addListener(listener);
		twitterStream.filter(q);
        
        twitterStream.addListener(listener);
        twitterStream.sample();
        
    }
    
}