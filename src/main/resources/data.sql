-- Création des catégories globales
INSERT INTO ticket_category (name) VALUES ('VIP');
INSERT INTO ticket_category (name) VALUES ('Standard');

-- Création de l'Organisateur
INSERT INTO users (name, email) VALUES ('Organisateur Test', 'orga@test.com');
INSERT INTO organizer (id, organisation_name)
SELECT id, 'EventPro Corp' FROM users WHERE email = 'orga@test.com';

-- Création du Client
INSERT INTO users (name, email) VALUES ('Client Test', 'client@test.com');
INSERT INTO client (id, phone)
SELECT id, '0600000000' FROM users WHERE email = 'client@test.com';