CREATE SEQUENCE hibernate_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE application_user (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    phone_number BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    alerts_for_tasks BOOLEAN NOT NULL DEFAULT FALSE,
    alerts_for_deadlines BOOLEAN NOT NULL DEFAULT FALSE,
    profile_picture VARCHAR(255),
    auth0_id VARCHAR(255) NOT NULL
);

CREATE TABLE game (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    logo VARCHAR(255),
    is_family_friendly BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE question_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    hex_color VARCHAR(255) NOT NULL,
    hint VARCHAR(2000) NOT NULL
);

CREATE TABLE question_function (
    id SERIAL PRIMARY KEY,
    timer INT,
    answer VARCHAR(255)
);

CREATE TABLE question_function_questions (
    question_function_id INT NOT NULL,
    questions VARCHAR(255),
    FOREIGN KEY (question_function_id) REFERENCES question_function(id)
);

CREATE TABLE question_function_options (
    question_function_id INT NOT NULL,
    options VARCHAR(255),
    FOREIGN KEY (question_function_id) REFERENCES question_function(id)
);

CREATE TABLE question_function_challenges (
    question_function_id INT NOT NULL,
    challenges VARCHAR(255),
    FOREIGN KEY (question_function_id) REFERENCES question_function(id)
);

CREATE TABLE question (
    id SERIAL PRIMARY KEY,
    active_weeks VARCHAR(255) NOT NULL DEFAULT '1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52',
    question_description VARCHAR(255) NOT NULL,
    phase INT NOT NULL,
    question_function_id INT,
    punishment INT,
    question_picture VARCHAR(255),
    game_id INT NOT NULL,
    question_type_id INT NOT NULL,
    group_size VARCHAR(255) NOT NULL DEFAULT 'All',
    FOREIGN KEY (question_function_id) REFERENCES question_function(id),
    FOREIGN KEY (game_id) REFERENCES game(id),
    FOREIGN KEY (question_type_id) REFERENCES question_type(id)
);

CREATE TABLE game_session (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    game_id INT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES game(id)
);

CREATE TABLE idea (
    id SERIAL PRIMARY KEY,
    idea_text VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    application_user_id INT NOT NULL,
    FOREIGN KEY (application_user_id) REFERENCES application_user(id)
);

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    created_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    deadline TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    category VARCHAR(255) NOT NULL,
    priority VARCHAR(255) NOT NULL,
    application_user_id INT NOT NULL,
    game_id INT,
    last_updated TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    FOREIGN KEY (application_user_id) REFERENCES application_user(id),
    FOREIGN KEY (game_id) REFERENCES game(id)
);

CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    comment VARCHAR(255) NOT NULL,
    application_user_id INT NOT NULL,
    task_id INT NOT NULL,
    FOREIGN KEY (application_user_id) REFERENCES application_user(id),
    FOREIGN KEY (task_id) REFERENCES task(id)
);

CREATE TABLE contact_form (
    id SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    message VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    accepted_terms BOOLEAN NOT NULL DEFAULT FALSE,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);