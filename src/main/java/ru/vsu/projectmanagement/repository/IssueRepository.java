package ru.vsu.projectmanagement.repository;

import ru.vsu.projectmanagement.domain.Issue;
import ru.vsu.projectmanagement.domain.IssuePriority;
import ru.vsu.projectmanagement.domain.IssueStatus;

import java.util.List;
import java.util.Optional;

public interface IssueRepository {
    Issue create(Issue issue);
    void update(Issue issue);
    Optional<Issue> findById(int id);
    List<Issue> findByParameters(String title, int assigneeId, int reportedId, IssueStatus status, IssuePriority priority);
}
