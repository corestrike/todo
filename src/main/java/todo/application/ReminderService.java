package todo.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import todo.application.Task.ReminderTask;
import todo.domain.reminder.Reminder;
import todo.domain.reminder.ReminderRepository;

import java.util.Date;

@Service
public class ReminderService {

	@Autowired
	ReminderRepository reminderRepository;
	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskScheduler;

	public void createReminder(Integer taskId, String to, Date execDate) {
		Reminder reminder = new Reminder();
		reminder.setId(taskId);
		reminder.setTo(to);
		reminder.setExecDate(execDate);
		reminderRepository.save(reminder);

		String subject = "タスクの実施期限が近づいています。";
		String body = "タスクを実施してください。";
		threadPoolTaskScheduler.schedule(new ReminderTask(to, subject, body), execDate);
	}

}
