package edu.gsu.bbb.willdo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository repository;

    @GetMapping("/tasks") //get all tasks
    public List<Task> all() {
        return repository.findAll();
    }

    @GetMapping("/tasks/{groupId}") //get one specific task
    public Optional<Task> taskFromGroup(@PathVariable String groupId) {
        Optional<Task> findTasks = repository.findById(groupId); //Checks for groups with a like groupId
        Optional<Task> empty = Optional.empty(); //to see if it leaves loop

        if(!findTasks.isPresent()) {
            //some response annotation; invalid parameters?
        } else {
            return findTasks;
        }
        return empty; //should not return this ever
    }

    @PostMapping("/tasks/{groupId}") //saves new task as new doc in DB
    public Object newTaskToGroup(@RequestBody Task newTask, @PathVariable String groupId) {
        Optional<Task> empty = Optional.empty(); //to see if it leaves loop
        newTask.setGroupId(groupId);
        if(newTask.getSummary() == null){
            //some response annotation; null values
        } else {
            return repository.save(newTask);
        }
        return empty; //should not return this ever
    }

    @PutMapping("/tasks/{id}") //updates task already in DB
    public Task updateTask(@RequestBody Task newTask, @PathVariable String id) {
        if (repository.findById(id).isPresent()) {
            Optional<Task> oldTaskInfo = repository.findById(id)
                    .map(task -> {
                        if (newTask.getSummary() != null) {
                            task.setSummary(newTask.getSummary());
                        }
                        if (newTask.getDescription() != null) {
                            task.setDescription(newTask.getDescription());
                        }
                        if (newTask.getDate() != null) {
                            task.setDate(newTask.getDate());
                        }
                        if (newTask.isState() != task.isState()
                                && newTask.isState()) {
                            task.setState(newTask.isState());
                        }
//                        if (newTask.getGroup() != null) {
//                            task.setGroup(newTask.getGroup());
//                        }

                        return repository.save(task);
                    });
        }
        return newTask; //sends original request body so we can see what broke it
    }
}
