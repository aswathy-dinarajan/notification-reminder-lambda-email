AWS Lambda function to read the event from SNS topic and send email using AWS SES service

This lambda function will be a subscriber to the SNS topic. When notification-reminder-lambda function pushes message(includes the email id, event message,mobile etc) to the SNS topic, based on the information an email will be sent to the recepient using SES service

Technologies used : AWS Lambda, aws sdk, Maven, Java 8,AWS SNS,AWS SES
