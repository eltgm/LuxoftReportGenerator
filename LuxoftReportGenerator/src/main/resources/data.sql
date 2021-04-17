INSERT INTO users (username, password, missed_days, enabled)
VALUES ('eltgm', '{noop}1234', 0, true);

INSERT INTO authorities (username, authority)
VALUES ('eltgm', 'ROLE_ADMIN');