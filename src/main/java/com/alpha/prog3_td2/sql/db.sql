create user mini_football_db_manager with password '123456';
GRANT ALL PRIVILEGES ON DATABASE mini_football_db TO mini_football_db_manager;
\c mini_football_db;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO mini_football_db_manager;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO mini_football_db_manager;