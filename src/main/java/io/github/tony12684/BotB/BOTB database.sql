CREATE TABLE `users` (
  `uuid` char(32) PRIMARY KEY,
  `username` varchar(16) NOT NULL,
  `nickname` varchar(255)
);

CREATE TABLE `games` (
  `game_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_start_date_time` datetime NOT NULL, --yyyy-MM-ddTHH:mm:ss
  `game_end_date_time` datetime, --yyyy-MM-ddTHH:mm:ss
  `team_id` int -- the winning team
);

CREATE TABLE `teams` (
  `team_id` int PRIMARY KEY AUTO_INCREMENT,
  `team_name` varchar(255) UNIQUE NOT NULL
);

CREATE TABLE `user_game_roles` (
  `user_game_roles_id` int PRIMARY KEY AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `game_id` int NOT NULL,
  `uuid` char(32) NOT NULL,
  `action_id` int
);

CREATE TABLE `user_game_teams` (
  `user_game_teams_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_id` int NOT NULL,
  `uuid` char(32) NOT NULL,
  `team_id` int NOT NULL,
  `action_id` int
);

CREATE TABLE `seats` (
  `seat_id` int PRIMARY KEY AUTO_INCREMENT,
  `position` int NOT NULL,
  `uuid` char(32) NOT NULL,
  `game_id` int NOT NULL
);

CREATE TABLE `actions` (
  `action_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_id` int NOT NULL,
  `uuid` char(32) NOT NULL,
  `team_id` int NOT NULL,
  `role_id` int NOT NULL,
  `action_day` varchar(255) NOT NULL,
  `action_type` varchar(255) NOT NULL,
  `action_contains_lie` bool NOT NULL,
  `action_has_targets` bool NOT NULL,
  `action_date_time` datetime NOT NULL,
  `action_notes` varchar(255)
);

CREATE TABLE `user_games` (
  `user_games_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_id` int NOT NULL,
  `uuid` char(32) NOT NULL
);

CREATE TABLE `targets` (
  `target_id` int PRIMARY KEY AUTO_INCREMENT,
  `action_id` int NOT NULL,
  `uuid` char(32) NOT NULL
);

CREATE TABLE `roles` (
  `role_id` int PRIMARY KEY AUTO_INCREMENT,
  `role_name` varchar(255) UNIQUE NOT NULL
);


--TODO double check these rereferences
--TODO add indexes for performance if needed
ALTER TABLE `games` ADD FOREIGN KEY (`game_id`) REFERENCES `user_games` (`game_id`);
ALTER TABLE `games` ADD FOREIGN KEY (`game_id`) REFERENCES `actions` (`game_id`);
ALTER TABLE `games` ADD FOREIGN KEY (`game_id`) REFERENCES `user_game_roles` (`game_id`);
ALTER TABLE `games` ADD FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`);
ALTER TABLE `games` ADD FOREIGN KEY (`game_id`) REFERENCES `seats` (`game_id`);

ALTER TABLE `users` ADD FOREIGN KEY (`uuid`) REFERENCES `user_games` (`uuid`);
ALTER TABLE `users` ADD FOREIGN KEY (`uuid`) REFERENCES `actions` (`uuid`);
ALTER TABLE `users` ADD FOREIGN KEY (`uuid`) REFERENCES `user_game_roles` (`uuid`);
ALTER TABLE `users` ADD FOREIGN KEY (`uuid`) REFERENCES `seats` (`uuid`);
ALTER TABLE `users` ADD FOREIGN KEY (`uuid`) REFERENCES `targets` (`uuid`);

ALTER TABLE `roles` ADD FOREIGN KEY (`role_id`) REFERENCES `actions` (`role_id`);
ALTER TABLE `roles` ADD FOREIGN KEY (`role_id`) REFERENCES `user_game_roles` (`role_id`);

ALTER TABLE `teams` ADD FOREIGN KEY (`team_id`) REFERENCES `actions` (`team_id`);

ALTER TABLE `actions` ADD FOREIGN KEY (`action_id`) REFERENCES `targets` (`action_id`);


ALTER TABLE `user_game_teams` ADD FOREIGN KEY (`uuid`) REFERENCES `users` (`uuid`);
ALTER TABLE `user_game_teams` ADD FOREIGN KEY (`team_id`) REFERENCES `teams` (`team_id`);
ALTER TABLE `user_game_teams` ADD FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`);
