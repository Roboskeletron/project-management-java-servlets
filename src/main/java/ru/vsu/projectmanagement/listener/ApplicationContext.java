package ru.vsu.projectmanagement.listener;

import ru.vsu.projectmanagement.service.*;

public record ApplicationContext(
        UserService userService,
        ProjectService projectService,
        TaskService taskService,
        CommentService commentService,
        TaskHistoryService taskHistoryService
) {
}
