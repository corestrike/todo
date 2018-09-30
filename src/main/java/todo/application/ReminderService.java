package todo.application;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import todo.application.task.ReminderTask;
import todo.domain.reminder.Reminder;
import todo.domain.reminder.ReminderRepository;
import todo.domain.task.Task;
import todo.domain.task.TaskRepository;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
public class ReminderService {

	@Autowired
	ReminderRepository reminderRepository;
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	ReminderTask reminderTask;
	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskScheduler;

	private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();

	public void createReminder(Integer taskId, String to, Date execDate) {
		if(StringUtils.isNotBlank(to) && execDate != null) {
			Reminder reminder = new Reminder();
			reminder.setEmail(to);
			reminder.setExecDate(execDate);
			reminder.setTaskId(taskId);
			reminderRepository.save(reminder);

			createScheduleTask(reminder.getId(), to, execDate);
		}
	}

	public void updateReminder(Reminder reminder) {
		if(reminder.getId() != null) {
			removeScheduleTask(reminder.getId());
			if(StringUtils.isNotBlank(reminder.getEmail()) && reminder.getExecDate() != null) {
				// update
				reminder.setSent(false);
				reminderRepository.save(reminder);
				createScheduleTask(reminder.getId(), reminder.getEmail(), reminder.getExecDate());
			} else {
				// delete
				reminderRepository.deleteById(reminder.getId());
			}
		} else {
			// create
			createReminder(reminder.getTaskId(), reminder.getEmail(), reminder.getExecDate());
		}
	}

	public void deleteReminder(Integer id) {
		Task task = taskRepository.findById(id);
		Reminder reminder = task.getReminder();
		if(reminder != null) {
			removeScheduleTask(reminder.getId());
		}
	}

	public void completeReminder(Integer reminderId) {
		if(reminderId != null) {
			Reminder reminder = reminderRepository.findById(reminderId);
			if(reminder != null) {
				reminder.setSent(true);
				reminderRepository.save(reminder);
				removeScheduleTask(reminderId);
			}
		}
	}

	public void createScheduleTask(Integer reminderId, String to, Date execDate) {
		String subject = "タスクの実施期限が近づいています。";
		String body = "タスクを実施してください。";
		reminderTask.setId(reminderId);
		reminderTask.setTo(to);
		reminderTask.setBody(body);
		reminderTask.setSubject(subject);
		ScheduledFuture sf = threadPoolTaskScheduler.schedule(reminderTask, execDate);
		scheduledTasks.put(reminderId, sf);
	}

	public void removeScheduleTask(Integer reminderId) {
		ScheduledFuture sf = scheduledTasks.get(reminderId);
		if(sf != null) {
			sf.cancel(true);
		}
		scheduledTasks.remove(reminderId);
	}
}
