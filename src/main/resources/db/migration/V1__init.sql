CREATE TABLE application_user (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  phone_number BIGINT NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  alerts_for_tasks BOOLEAN NOT NULL,
  alerts_for_deadlines BOOLEAN NOT NULL,
  profile_picture VARCHAR(255),
  auth0_id VARCHAR(255) NOT NULL
);

CREATE TABLE game (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  last_updated TIMESTAMP NOT NULL,
  status VARCHAR(255) NOT NULL,
  logo VARCHAR(255),
  is_family_friendly BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE question_function (
    id SERIAL PRIMARY KEY,
    timer INTEGER,
    answer VARCHAR(255)
);

CREATE TABLE question_function_challenges (
    question_function_id INTEGER,
    challenges TEXT,
    FOREIGN KEY (question_function_id) REFERENCES question_function(id)
);

CREATE TABLE question_function_questions (
     question_function_id INTEGER,
     questions TEXT,
     FOREIGN KEY (question_function_id) REFERENCES question_function(id)
);

CREATE TABLE question_function_options (
   question_function_id INTEGER,
   options TEXT,
   FOREIGN KEY (question_function_id) REFERENCES question_function(id)
);

CREATE TABLE question_type (
   id SERIAL PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   hex_color VARCHAR(255) NOT NULL,
   hint VARCHAR(2000) NOT NULL
);

CREATE TABLE question (
  id SERIAL PRIMARY KEY,
  active_weeks VARCHAR(255) NOT NULL DEFAULT 'default_value_here',
  question_description VARCHAR(255) NOT NULL,
  phase INTEGER NOT NULL,
  question_function_id INTEGER,
  punishment INTEGER,
  question_picture VARCHAR(255),
  game_id INTEGER,
  question_type_id INTEGER,
  group_size VARCHAR(255) NOT NULL DEFAULT 'All',
  FOREIGN KEY (question_function_id) REFERENCES question_function (id),
  FOREIGN KEY (game_id) REFERENCES game (id),
  FOREIGN KEY (question_type_id) REFERENCES question_type (id)
);

CREATE TYPE idea_category AS ENUM (
    'GAMES',
    'DEVELOPMENT',
    'DESIGN',
    'VARIOUS'
);

CREATE TABLE idea (
      id SERIAL PRIMARY KEY,
      idea_text VARCHAR(255) NOT NULL,
      category idea_category NOT NULL,
      application_user_id INTEGER,
      FOREIGN KEY (application_user_id) REFERENCES application_user (id)
);

CREATE TYPE task_status AS ENUM (
    'PENDING',
    'DOING',
    'DONE'
    );

CREATE TYPE task_category AS ENUM (
    'DESIGN',
    'DEVELOPMENT',
    'ECONOMY',
    'GAMES',
    'MARKETING'
    );

CREATE TYPE task_priority AS ENUM (
    'HIGH',
    'MEDIUM',
    'LOW'
    );

CREATE TABLE task (
      id SERIAL PRIMARY KEY,
      created_date_time TIMESTAMP NOT NULL,
      title VARCHAR(255) NOT NULL,
      description VARCHAR(255) NOT NULL,
      status task_status NOT NULL,
      deadline TIMESTAMP NOT NULL,
      category task_category NOT NULL,
      priority task_priority NOT NULL,
      application_user_id INTEGER,
      game_id INTEGER,
      last_updated TIMESTAMP NOT NULL,
      FOREIGN KEY (application_user_id) REFERENCES application_user (id),
      FOREIGN KEY (game_id) REFERENCES game (id)
);

CREATE TABLE comment (
     id SERIAL PRIMARY KEY,
     created_date TIMESTAMP NOT NULL,
     comment VARCHAR(255) NOT NULL,
     application_user_id INTEGER,
     task_id INTEGER,
     FOREIGN KEY (application_user_id) REFERENCES application_user (id),
     FOREIGN KEY (task_id) REFERENCES task (id)
);

CREATE TYPE contact_type AS ENUM (
    'Feedback',
    'Contact'
);

CREATE TABLE contact_form (
  id SERIAL PRIMARY KEY,
  type contact_type NOT NULL,
  message VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  accepted_terms BOOLEAN NOT NULL,
  created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
