package todo.application.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import todo.application.aws.SESService;

public class ReminderTask implements Runnable {

	private String to;
	private String subject;
	private String body;

	@Autowired
	SESService SESService;

	@Value("${aws.ses.settings.from}")
	private String from;

	public ReminderTask(String to, String subject, String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public void run() {
		SESService.send(from, to, subject, body);
	}

}
