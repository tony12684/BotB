--** = (Foreign key item)
CREATE TABLE `users` ( --minecraft UUID without dashes
  `uuid` char(32) PRIMARY KEY,
  `username` varchar(16) NOT NULL,
  `nickname` varchar(255)
);

CREATE TABLE `games` ( --each played game
  `game_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_start_date_time` datetime NOT NULL, --yyyy-MM-ddTHH:mm:ss
  `game_end_date_time` datetime, --yyyy-MM-ddTHH:mm:ss
  `team_id` int --**, the winning team
);

CREATE TABLE `user_games` ( --which users played in which games
  `user_games_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_id` int NOT NULL, --**
  `uuid` char(32) NOT NULL --**
);

CREATE TABLE `seats` ( --which seat each user had in each game
  `seat_id` int PRIMARY KEY AUTO_INCREMENT,
  `position` int NOT NULL,
  `uuid` char(32) NOT NULL, --**
  `game_id` int NOT NULL --**,
  `action_id` int --**, optional, the action that caused the seat change
); --in the case of seat changes, multiple entries per user per game

CREATE TABLE `user_game_roles` ( --which roles each user had in each game
  `user_game_roles_id` int PRIMARY KEY AUTO_INCREMENT,
  `role_id` int NOT NULL, --**
  `game_id` int NOT NULL, --**
  `uuid` char(32) NOT NULL, --**
  `action_id` int --**, optional, the action that caused the role change
); --in the case of role changes, multiple entries per user per game

CREATE TABLE `user_game_teams` ( --which teams each user was on in each game
  `user_game_teams_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_id` int NOT NULL, --**
  `uuid` char(32) NOT NULL, --**
  `team_id` int NOT NULL, --**
  `action_id` int --**, optional, the action that caused the team change
); --in the case of team changes, multiple entries per user per game


CREATE TABLE `actions` (--all documented actions taken by users in games
  `action_id` int PRIMARY KEY AUTO_INCREMENT,
  `game_id` int NOT NULL, --**
  `uuid` char(32) NOT NULL, --**
  `team_id` int NOT NULL, --**
  `role_id` int NOT NULL, --**
  `action_day` int NOT NULL,
  `action_type` varchar(255) NOT NULL,
  `action_contains_lie` bool NOT NULL,
  `action_has_targets` bool NOT NULL,
  `action_date_time` datetime NOT NULL,
  `action_notes` varchar(255)
);

CREATE TABLE `targets` ( --which users were targeted by a specific action
  `target_id` int PRIMARY KEY AUTO_INCREMENT,
  `action_id` int NOT NULL, --**
  `uuid` char(32) NOT NULL --**
); --in the case of multiple targets, multiple entries per action



CREATE TABLE `roles` ( --reference table for all roles
  `role_id` int PRIMARY KEY AUTO_INCREMENT,
  `role_name` varchar(255) UNIQUE NOT NULL
);

CREATE TABLE `teams` ( --reference table for all teams
  `team_id` int PRIMARY KEY AUTO_INCREMENT,
  `team_name` varchar(255) UNIQUE NOT NULL
);

--TODO add indexes for performance if needed
ALTER TABLE `games`
  ADD FOREIGN KEY (`team_id`) REFERENCES `teams`(`team_id`);

ALTER TABLE `user_game_roles`
  ADD FOREIGN KEY (`role_id`) REFERENCES `roles`(`role_id`),
  ADD FOREIGN KEY (`game_id`) REFERENCES `games`(`game_id`),
  ADD FOREIGN KEY (`uuid`) REFERENCES `users`(`uuid`),
  ADD FOREIGN KEY (`action_id`) REFERENCES `actions`(`action_id`);

ALTER TABLE `user_game_teams`
  ADD FOREIGN KEY (`game_id`) REFERENCES `games`(`game_id`),
  ADD FOREIGN KEY (`uuid`) REFERENCES `users`(`uuid`),
  ADD FOREIGN KEY (`team_id`) REFERENCES `teams`(`team_id`),
  ADD FOREIGN KEY (`action_id`) REFERENCES `actions`(`action_id`);

ALTER TABLE `seats`
  ADD FOREIGN KEY (`uuid`) REFERENCES `users`(`uuid`),
  ADD FOREIGN KEY (`game_id`) REFERENCES `games`(`game_id`),
  ADD FOREIGN KEY (`action_id`) REFERENCES `actions`(`action_id`);

ALTER TABLE `actions`
  ADD FOREIGN KEY (`game_id`) REFERENCES `games`(`game_id`),
  ADD FOREIGN KEY (`uuid`) REFERENCES `users`(`uuid`),
  ADD FOREIGN KEY (`team_id`) REFERENCES `teams`(`team_id`),
  ADD FOREIGN KEY (`role_id`) REFERENCES `roles`(`role_id`);

ALTER TABLE `user_games`
  ADD FOREIGN KEY (`game_id`) REFERENCES `games`(`game_id`),
  ADD FOREIGN KEY (`uuid`) REFERENCES `users`(`uuid`);

ALTER TABLE `targets`
  ADD FOREIGN KEY (`action_id`) REFERENCES `actions`(`action_id`),
  ADD FOREIGN KEY (`uuid`) REFERENCES `users`(`uuid`);