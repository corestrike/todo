package todo.interfaces;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import todo.application.TaskService;
import todo.domain.task.Task;

@Controller
@RequestMapping("/")
public class TaskController {
    @Autowired
    TaskService taskService;

	@GetMapping
	public ModelAndView index(Task task) {
		List<Task> todoList = taskService.getTaskListByStatus(1);
		List<Task> doingList = taskService.getTaskListByStatus(2);
		List<Task> doneList = taskService.getTaskListByStatus(3);
		ModelAndView mav = new ModelAndView("index");
		mav.addObject("todoList", todoList);
		mav.addObject("doingList", doingList);
		mav.addObject("doneList", doneList);
		return mav;
	}

    /**
     * [作成]ボタンをクリックしたときの処理。
     * タスクを新規登録する。
     * @param task
     * @return
     */
    @PostMapping("/task")
    public ModelAndView create(Task task) {
        taskService.create(task);
        return new ModelAndView("redirect:/");
    }

    @PutMapping("/task/{id}")
    public ModelAndView update(@PathVariable("id") int id, Task task) {
        taskService.update(task);
        return new ModelAndView("redirect:/");
    }

    @DeleteMapping("/task/{id}")
    public ModelAndView delete(@PathVariable("id") int id) {
        taskService.delete(id);
        return new ModelAndView("redirect:/");
    }
}
