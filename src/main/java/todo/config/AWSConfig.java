package todo.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class AWSConfig {
	private static final String RESION_AMAZON_US_EAST = "us-east-1";

	@Value("${aws.api.key}")
	private String awsKey;
	@Value("${aws.api.secret}")
	private String awsSecret;

	@Bean
	@Lazy
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public AmazonSimpleEmailService ses() {
		AWSCredentials credentials = new BasicAWSCredentials(awsKey, awsSecret);
		return AmazonSimpleEmailServiceClient.builder()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(RESION_AMAZON_US_EAST)
				.build();
	}

	@Bean
	@Lazy
	@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
	public AmazonSQS sqs() {
		AWSCredentials credentials = new BasicAWSCredentials(awsKey, awsSecret);
		return AmazonSQSClient.builder()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(RESION_AMAZON_US_EAST)
				.build();
	}
}
