-- Création des catégories globales
INSERT INTO ticket_category (id, name) VALUES (1, 'VIP') ON CONFLICT (id) DO NOTHING;
INSERT INTO ticket_category (id, name) VALUES (2, 'Standard') ON CONFLICT (id) DO NOTHING;

-- Création de l'Organisateur
INSERT INTO users (id, name, email) VALUES (1, 'Organisateur Test', 'orga@test.com') ON CONFLICT (id) DO NOTHING;
INSERT INTO organizer (id, organisation_name) VALUES (1, 'EventPro Corp') ON CONFLICT (id) DO NOTHING;

-- Création du Client
INSERT INTO users (id, name, email) VALUES (2, 'Client Test', 'client@test.com') ON CONFLICT (id) DO NOTHING;
INSERT INTO client (id, phone) VALUES (2, '0600000000') ON CONFLICT (id) DO NOTHING;