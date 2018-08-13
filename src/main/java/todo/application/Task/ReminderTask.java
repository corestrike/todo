package todo.application.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import todo.application.EmailService;

public class ReminderTask implements Runnable {

	private String to;
	private String subject;
	private String body;

	@Autowired
	EmailService emailService;

	@Value("${aws.ses.settings.from}")
	private String from;

	public ReminderTask(String to, String subject, String body) {
		this.to = to;
		this.subject = subject;
		this.body = body;
	}

	@Override
	public void run() {
		emailService.send(from, to, subject, body);
	}

}
