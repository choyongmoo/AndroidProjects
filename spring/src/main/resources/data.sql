-- sample
INSERT INTO sample (message) VALUES ('Hello World'), ('Sample message 2');

-- users
INSERT INTO users (username, email, password_hash, display_name, role, created_at, updated_at)
VALUES 
  ('john_doe', 'john@example.com', 'hash1', 'John Doe', 'member', NOW(), NOW()),
  ('jane_smith', 'jane@example.com', 'hash2', 'Jane Smith', 'admin', NOW(), NOW()),
  ('alice_wang', 'alice@example.com', 'hash3', 'Alice Wang', 'member', NOW(), NOW());

-- places
INSERT INTO places (name, lat, lng, address)
VALUES 
  ('Central Park', 40.785091, -73.968285, 'New York, NY'),
  ('Eiffel Tower', 48.8584, 2.2945, 'Paris, France');

-- groups
INSERT INTO groups (group_name, created_by)
VALUES 
  ('Morning Joggers', 1),
  ('Travel Buddies', 2);

-- appointments
INSERT INTO appointments (title, penalty, group_id, place_id, "time", created_by)
VALUES 
  ('Jog at Central Park', 10, 1, 1, NOW() + INTERVAL '1 day', 1),
  ('Visit Eiffel Tower', 20, 2, 2, NOW() + INTERVAL '2 days', 2);

-- arrival_logs
INSERT INTO arrival_logs (appointment_id, user_id, arrival_time, is_late)
VALUES 
  (1, 1, NOW() + INTERVAL '1 day 00:05', TRUE),
  (1, 3, NOW() + INTERVAL '1 day', FALSE),
  (2, 2, NOW() + INTERVAL '2 days 00:10', TRUE);

-- friends
INSERT INTO friends (user_id, friend_id, created_at, status)
VALUES 
  (1, 2, NOW(), 'accepted'),
  (1, 3, NOW(), 'pending'),
  (2, 3, NOW(), 'accepted');

-- group_members
INSERT INTO group_members (group_id, user_id)
VALUES 
  (1, 1), (1, 3),
  (2, 2), (2, 3);

-- group_places
INSERT INTO group_places (group_id, place_id)
VALUES 
  (1, 1),
  (2, 2);

-- live_locations
INSERT INTO live_locations (user_id, lat, lng)
VALUES 
  (1, 40.784, -73.965),
  (2, 48.857, 2.295),
  (3, 40.7128, -74.0060);
