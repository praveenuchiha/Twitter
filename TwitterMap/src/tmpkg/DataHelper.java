package tmpkg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

public class DataHelper {

	private AmazonDynamoDBClient dynamoDB;

	public DataHelper() {
		
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (C:\\Users\\Uchiha\\.aws\\credentials), and is in valid format.",
                    e);
        }
		dynamoDB = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2); 
	}

	public ArrayList<Twitter> getAllData() {
		String tableName = "twitterTable";
		HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
		/*
		 * Condition condition = new Condition()
		 * .withComparisonOperator(ComparisonOperator.GT.toString())
		 * .withAttributeValueList(new AttributeValue().withN("1985"));
		 * scanFilter.put("year", condition);
		 */
		ScanRequest scanRequest = new ScanRequest(tableName)
				.withScanFilter(scanFilter);
		ScanResult scanResult = dynamoDB.scan(scanRequest);
		ArrayList<Twitter> twitList = new ArrayList<Twitter>();
		List<Map<String, AttributeValue>> resultList = scanResult.getItems();
		for (Map<String, AttributeValue> res : resultList) {
			Twitter twit = new Twitter();
			twit.setId(res.get("id").getN());
			twit.setKeyword(res.get("keyword").getS());
			twit.setLatitude(res.get("latitude").getN());
			twit.setLongtitude(res.get("longtitude").getN());
			String text = res.get("text").getS();
			twit.setText(text);
			twit.setTimestamp(res.get("timestamp").getS());
			twit.setUrl(res.get("url").getS());
			String username =  res.get("username").getS();
			twit.setUsername(username);
			twitList.add(twit);
		}

		return twitList;
	}
}
