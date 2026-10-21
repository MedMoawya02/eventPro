-- 1. Création des catégories globales (TicketCategory)
-- On utilise ON CONFLICT pour éviter l'erreur si elles existent déjà
INSERT INTO ticket_category (id, name) VALUES (1, 'VIP') ON CONFLICT (id) DO NOTHING;
INSERT INTO ticket_category (id, name) VALUES (2, 'Standard') ON CONFLICT (id) DO NOTHING;

-- 2. Création de l'Organisateur (User + Organizer)
INSERT INTO users (id, name, email) VALUES (1, 'Organisateur Test', 'orga@test.com') ON CONFLICT (id) DO NOTHING;
INSERT INTO organizer (id, organisation_name) VALUES (1, 'EventPro Corp') ON CONFLICT (id) DO NOTHING;

-- 3. Création du Client (User + Client)
INSERT INTO users (id, name, email) VALUES (2, 'Client Test', 'client@test.com') ON CONFLICT (id) DO NOTHING;
INSERT INTO client (id, phone) VALUES (2, '0600000000') ON CONFLICT (id) DO NOTHING;

-- 4. Création de l'Événement (optionnel, pour tester)
INSERT INTO evenements (id, organisateur_id, titre, description, lieu, date_debut, date_fin, date_ouverture_vente, date_cloture_vente, statut)
VALUES (1, 1, 'Concert de Printemps', 'Un super concert', 'Casablanca', '2025-06-01 20:00:00', '2025-06-01 23:00:00', '2025-01-01 00:00:00', '2025-05-31 23:59:59', 'PUBLISHED') ON CONFLICT (id) DO NOTHING;

-- 5. Création des catégories pour l'événement (optionnel, pour tester)
INSERT INTO ticket_categories_per_event (id, event_id, category_id, unit_price, total_quantity, max_quota_per_client, reserved_quantity, sold_quantity)
VALUES (1, 1, 1, 500.00, 100, 4, 0, 0) ON CONFLICT (id) DO NOTHING;

INSERT INTO ticket_categories_per_event (id, event_id, category_id, unit_price, total_quantity, max_quota_per_client, reserved_quantity, sold_quantity)
VALUES (2, 1, 2, 200.00, 500, 10, 0, 0) ON CONFLICT (id) DO NOTHING;