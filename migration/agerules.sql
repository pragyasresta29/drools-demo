CREATE TABLE age_rules(id INTEGER, min_age INTEGER, max_age INTEGER, status VARCHAR(256));
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(1, 0, 2,'Infant');
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(2, 2, 6,'Baby');
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(3, 6, 13,'Young Age');
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(4, 13,18,'Juvenile');
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(5, 18,41,'Youth');
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(6, 41,61,'Middle Aged');
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(7, 61,81,'Senior Citizen');
INSERT INTO age_rules(id, min_age, max_age, status) VALUES(8, 81, 100,'Old Aged');