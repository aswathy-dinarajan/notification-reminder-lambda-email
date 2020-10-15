package notificationreminder;

import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SNSEvent.SNSRecord;


/**
 * If the AWS account is in sandbox mode both the sender and receiver emails should be verified in SES service. Once it is moved to production mode mails can be sent to unverified recipients
 * In case of SMS depends on the countries the mobile number belongs the process of registering SenderIds varies.
 * @author Aswathy
 *
 */

public class SendEmailNotification implements RequestHandler<SNSEvent, Object> {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	AmazonSNS snsClient = AmazonSNSClient.builder().build();
    private String from = "achu.135@gmail.com";
	
    public Object handleRequest(SNSEvent input,Context context) {
		LambdaLogger logger = context.getLogger();
		try { 
			List<SNSRecord> records = input.getRecords();
			for(SNSRecord record : records){
				String data[] = record.getSNS().getMessage().split("#");
				sendEmail(data[0], data[1], data[2], logger);
				logger.log("Successfully read message\n"+record);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			logger.log("Caught exception: " + e.getMessage());
		}
		return null;
	}
	
	
	private void sendEmail(String message,String toEmail,String subject,LambdaLogger logger){
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_2).build();
		SendEmailRequest request = new SendEmailRequest()
				.withDestination(
						new Destination().withToAddresses(toEmail))
				.withMessage(new Message()
						.withSubject(new Content().withCharset("UTF-8").withData(subject))
						.withBody(new Body()
				                  .withHtml(new Content()
				                      .withCharset("UTF-8").withData(message))))
				.withSource(from);
		client.sendEmail(request);
		System.out.println("Email sent!");
	}
	
}
