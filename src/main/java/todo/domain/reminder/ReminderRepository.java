package todo.domain.reminder;

import org.springframework.stereotype.Repository;

@Repository
public interface ReminderRepository extends org.springframework.data.repository.Repository<Integer, Reminder> {
	Reminder findOne(Integer id);
	void save(Reminder reminder);
}
