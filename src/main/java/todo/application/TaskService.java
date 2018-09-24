package todo.application;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import todo.application.aws.S3Service;
import todo.domain.task.Task;
import todo.domain.task.TaskRepository;

@Service
public class TaskService {

	@Autowired
	S3Service s3service;

	@Autowired
	TaskRepository taskRepository;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	public void create(Task task, MultipartFile attachmentFile) {
		if(attachmentFile != null) {
			try {
				String key = attachmentFile.getOriginalFilename();
				s3service.uploadToPublicBucket(attachmentFile, bucketName, key);
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		task.setStatus(1);
		task.setUpdated(new Date());
		taskRepository.save(task);
	}

	public void update(Task task, MultipartFile attachmentFile) {
		if(attachmentFile != null) {
			try {
				String key = attachmentFile.getOriginalFilename();
				s3service.uploadToPublicBucket(attachmentFile, bucketName, key);
			} catch(Exception e) {
				System.out.println(e);
			}
		}
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
