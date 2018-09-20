package todo.application;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import todo.domain.task.Task;
import todo.domain.task.TaskRepository;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepository;

	/**
	 * (サンプル実装)
	 * 受け取ったReportオブジェクトのcreatedに現在日時、userに"test_user"を設定してDBに保存する。
	 * @param report
	 * @return
	 */
	public void create(Task task) {
		task.setStatus(1);
		task.setUpdated(new Date());
		taskRepository.save(task);
	}

	public void update(Task task) {
		task.setUpdated(new Date());
		taskRepository.save(task);
	}

	public void delete(Integer id) {
		taskRepository.deleteById(id);
	}

	/**
	 * 登録済みのタスクの一覧を返す
	 * @return
	 */
	public List<Task> getTaskList() {
		return taskRepository.findAll();
	}

	/**
	 * ステータスごとの一覧を返す
	 * @return
	 */
	public List<Task> getTaskListByStatus(int status) {
		return taskRepository.findByStatusOrderByUpdated(status);
	}
}
