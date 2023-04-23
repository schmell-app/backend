-- Increasing length of hint table in question_type
ALTER TABLE question_type ALTER COLUMN hint TYPE varchar(2000);