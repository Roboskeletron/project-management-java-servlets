-- init.sql for Task Tracker Application

-- Drop existing tables and types if they exist (for development convenience)
DROP TABLE IF EXISTS task_history CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS tasks CASCADE;
DROP TABLE IF EXISTS projects CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Table for Users
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(100) UNIQUE NOT NULL,
                       password_hash VARCHAR(255) NOT NULL, -- In a real app, use a strong hash (e.g., bcrypt)
                       email VARCHAR(255) UNIQUE NOT NULL,
                       full_name VARCHAR(255),
                       role VARCHAR(50) NOT NULL DEFAULT 'USER', -- e.g., 'ADMIN', 'PROJECT_MANAGER', 'USER'
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Table for Projects
CREATE TABLE projects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- User who owns/created the project
                          created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Table for Tasks
CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       status VARCHAR(50) NOT NULL DEFAULT 'TODO', -- e.g., 'TODO', 'IN_PROGRESS', 'REVIEW', 'DONE', 'CANCELLED'
                       priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM', -- e.g., 'LOW', 'MEDIUM', 'HIGH', 'URGENT'
                       assignee_id BIGINT REFERENCES users(id) ON DELETE SET NULL, -- User assigned to the task
                       reporter_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT, -- User who created the task
                       due_date DATE,
                       created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Table for Comments on Tasks
CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
                          user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- User who wrote the comment
                          content TEXT NOT NULL,
                          created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Table for Task History / Activity Log
CREATE TABLE task_history (
                              id BIGSERIAL PRIMARY KEY,
                              task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
                              user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE RESTRICT, -- User who made the change
                              field_changed VARCHAR(100) NOT NULL, -- e.g., 'status', 'assignee_id', 'description', 'title', 'priority'
                              old_value TEXT,
                              new_value TEXT,
                              changed_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better query performance
CREATE INDEX idx_projects_owner_id ON projects(owner_id);
CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_tasks_reporter_id ON tasks(reporter_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_priority ON tasks(priority);
CREATE INDEX idx_comments_task_id ON comments(task_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_task_history_task_id ON task_history(task_id);
CREATE INDEX idx_task_history_user_id ON task_history(user_id);

-- Sample Data (Passwords are plain text for simplicity, as in the example. HASH THEM IN PRODUCTION!)
INSERT INTO users (username, password_hash, email, full_name, role, is_active) VALUES
                                                                                   ('admin', 'adminpass', 'admin@tasktracker.dev', 'Администратор', 'ADMIN', TRUE),
                                                                                   ('alice', 'alicepass', 'alice@tasktracker.dev', 'Алиса Иванова', 'USER', TRUE),
                                                                                   ('bob', 'bobpass', 'bob@tasktracker.dev', 'Борис Петров', 'USER', TRUE);

INSERT INTO projects (name, description, owner_id, created_at, updated_at) VALUES
                                                                               ('РеДизайн Вебсайта', 'Полный редизайн корпоративного вебсайта.', (SELECT id from users WHERE username = 'admin'), NOW(), NOW()),
                                                                               ('Разработка Мобильного Приложения', 'Разработка нового мобильного приложения для iOS и Android.', (SELECT id from users WHERE username = 'admin'), NOW(), NOW());

INSERT INTO tasks (project_id, title, description, status, priority, reporter_id, assignee_id, due_date, created_at, updated_at) VALUES
                                                                                                                                     ((SELECT id from projects WHERE name = 'РеДизайн Вебсайта'), 'Макет главной страницы', 'Создать макеты для новой главной страницы.', 'TODO', 'HIGH', (SELECT id from users WHERE username = 'admin'), (SELECT id from users WHERE username = 'alice'), '2024-09-15', NOW(), NOW()),
                                                                                                                                     ((SELECT id from projects WHERE name = 'РеДизайн Вебсайта'), 'Реализация аутентификации', 'Настроить функционал входа и регистрации.', 'IN_PROGRESS', 'URGENT', (SELECT id from users WHERE username = 'admin'), (SELECT id from users WHERE username = 'bob'), '2024-09-20', NOW(), NOW()),
                                                                                                                                     ((SELECT id from projects WHERE name = 'Разработка Мобильного Приложения'), 'Определение ключевых функций', 'Провести мозговой штурм и финализировать ключевые функции.', 'DONE', 'HIGH', (SELECT id from users WHERE username = 'admin'), (SELECT id from users WHERE username = 'alice'), '2024-08-30', NOW(), NOW());

INSERT INTO comments (task_id, user_id, content, created_at) VALUES
                                                                 ((SELECT id from tasks WHERE title = 'Макет главной страницы'), (SELECT id from users WHERE username = 'admin'), 'Пожалуйста, убедитесь, что учтена адаптивность.', NOW()),
                                                                 ((SELECT id from tasks WHERE title = 'Реализация аутентификации'), (SELECT id from users WHERE username = 'bob'), 'Начинаю работу. Буду использовать JWT для сессий.', NOW());

INSERT INTO task_history (task_id, user_id, field_changed, old_value, new_value, changed_at) VALUES
    ((SELECT id from tasks WHERE title = 'Определение ключевых функций'), (SELECT id from users WHERE username = 'alice'), 'status', 'IN_PROGRESS', 'DONE', NOW());