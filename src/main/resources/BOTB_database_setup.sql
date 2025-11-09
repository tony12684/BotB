/* ** = (Foreign key item) */
CREATE TABLE IF NOT EXISTS users ( /* all users that have played at least one game */
  uuid char(36) PRIMARY KEY, /* minecraft UUID with dashes */
  username varchar(16) UNIQUE NOT NULL,
  nickname varchar(255)
);

CREATE TABLE IF NOT EXISTS roles ( /* reference table for all roles */
  role_id int PRIMARY KEY AUTO_INCREMENT,
  role_name varchar(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS teams ( /*reference table for all teams */
  team_id int PRIMARY KEY AUTO_INCREMENT,
  team_name varchar(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS games ( /* each played game */
  game_id int PRIMARY KEY AUTO_INCREMENT,
  game_start_date_time datetime NOT NULL, /* yyyy-MM-ddTHH:mm:ss */
  game_end_date_time datetime, /* yyyy-MM-ddTHH:mm:ss */
  won_team_id int, /* **, the winning team */
  FOREIGN KEY (team_id) REFERENCES teams(team_id)
);

CREATE TABLE IF NOT EXISTS user_games ( /* which users played in which games */
  user_games_id int PRIMARY KEY AUTO_INCREMENT,
  game_id int NOT NULL, /* ** */
  uuid char(36) NOT NULL, /* ** */
  FOREIGN KEY (game_id) REFERENCES games(game_id),
  FOREIGN KEY (uuid) REFERENCES users(uuid)
);

CREATE TABLE IF NOT EXISTS actions (/* all documented actions taken by users in games */
  action_id int PRIMARY KEY AUTO_INCREMENT,
  game_id int NOT NULL, /* ** */
  uuid char(36) NOT NULL, /* ** */
  team_id int NOT NULL, /* ** */
  role_id int NOT NULL, /* ** */
  action_day int NOT NULL,
  action_type varchar(255) NOT NULL,
  action_contains_lie bool NOT NULL,
  action_has_targets bool NOT NULL,
  action_date_time datetime NOT NULL,
  action_notes varchar(255),
  FOREIGN KEY (game_id) REFERENCES games(game_id),
  FOREIGN KEY (uuid) REFERENCES users(uuid),
  FOREIGN KEY (team_id) REFERENCES teams(team_id),
  FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

/*
INSERT INTO actions (game_id, uuid, team_id, role_id, action_day, action_type, action_contains_lie, action_has_targets, action_date_time, action_notes) VALUES (1, 123456789012345678901234567890123456, 1, (SELECT role_id FROM roles WHERE role_name = 'washerwoman'), 1, 'test', false, false, (NOW()), 'test')
*/

CREATE TABLE IF NOT EXISTS seats ( /* which seat each user had in each game */
  seat_id int PRIMARY KEY AUTO_INCREMENT,
  game_id int NOT NULL, /* ** */
  uuid char(36) NOT NULL, /* ** */
  position int NOT NULL,
  action_id int, /* **, optional, the action that caused the seat change */
  FOREIGN KEY (uuid) REFERENCES users(uuid),
  FOREIGN KEY (game_id) REFERENCES games(game_id),
  FOREIGN KEY (action_id) REFERENCES actions(action_id)
); /* in the case of seat changes, multiple entries per user per game */

CREATE TABLE IF NOT EXISTS user_game_roles ( /* which roles each user had in each game */
  user_game_roles_id int PRIMARY KEY AUTO_INCREMENT,
  game_id int NOT NULL, /* ** */
  uuid char(36) NOT NULL, /* ** */
  role_id int NOT NULL, /* ** */
  action_id int, /* **, optional, the action that caused the role change */
  FOREIGN KEY (role_id) REFERENCES roles(role_id),
  FOREIGN KEY (game_id) REFERENCES games(game_id),
  FOREIGN KEY (uuid) REFERENCES users(uuid),
  FOREIGN KEY (action_id) REFERENCES actions(action_id)
); /* in the case of role changes, multiple entries per user per game */

/*
INSERT INTO user_game_roles (game_id, uuid, role_id, action_id) VALUES (1, 123456789012345678901234567890123456, (SELECT role_id FROM roles WHERE role_name = 'washerwoman'), NULL)
*/

CREATE TABLE IF NOT EXISTS user_game_teams ( /* which teams each user was on in each game */
  user_game_teams_id int PRIMARY KEY AUTO_INCREMENT,
  game_id int NOT NULL, /* ** */
  uuid char(36) NOT NULL, /* ** */
  team_id int NOT NULL, /* ** */
  action_id int, /* **, optional, the action that caused the team change */
  FOREIGN KEY (game_id) REFERENCES games(game_id),
  FOREIGN KEY (uuid) REFERENCES users(uuid),
  FOREIGN KEY (team_id) REFERENCES teams(team_id),
  FOREIGN KEY (action_id) REFERENCES actions(action_id)
); /* in the case of team changes, multiple entries per user per game */

CREATE TABLE IF NOT EXISTS targets ( /* which users were targeted by a specific action */
  target_id int PRIMARY KEY AUTO_INCREMENT,
  action_id int NOT NULL, /* ** */
  uuid char(36) NOT NULL, /* ** */
  FOREIGN KEY (action_id) REFERENCES actions(action_id),
  FOREIGN KEY (uuid) REFERENCES users(uuid)
); /* in the case of multiple targets, multiple entries per action */

/* TODO add indexes for performance if needed */