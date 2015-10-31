import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.util.Tables;

public class DynamoDB {
	protected static AmazonDynamoDBClient DynamoClient;
	public DynamoDB() {
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
		DynamoClient = new AmazonDynamoDBClient(credentials);
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        DynamoClient.setRegion(usWest2);
	}
		
   
	public boolean testAndCheck(String tableName) {
		if(Tables.doesTableExist(DynamoClient, tableName)) {
			return true;
		}
		return false;
	}
	public boolean addItem(String tableName,Map<String, AttributeValue> item) {
		PutItemRequest putItemRequest = new PutItemRequest(tableName,item);
		DynamoClient.putItem(putItemRequest);
		return true;
	}
	public long findId(String tableName) {
		long max = 0;
		HashMap<String, Condition> scanfilter = new HashMap<String,Condition>();
		ScanResult sResult = DynamoClient.scan(new ScanRequest(tableName).withScanFilter(scanfilter).withAttributesToGet("id"));
		List<Map<String,AttributeValue>> list = sResult.getItems();
		for (Map<String, AttributeValue> map : list) {
			long temp = Long.parseLong(map.get("id").getN());
			if (temp  > max) {
				max = temp;
			}
		}
		return max;
	}
}