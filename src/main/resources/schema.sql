CREATE TABLE IF NOT EXISTS user_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE,
    description VARCHAR(500),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

ALTER TABLE users ADD COLUMN IF NOT EXISTS user_type_id BIGINT;
ALTER TABLE users DROP COLUMN IF EXISTS role;

CREATE TABLE IF NOT EXISTS restaurants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    cuisine_type VARCHAR(255) NOT NULL,
    opening_hours VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL REFERENCES users(id)
);

ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS name VARCHAR(255);
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS address VARCHAR(255);
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS cuisine_type VARCHAR(255);
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS opening_hours VARCHAR(255);
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS owner_id BIGINT;
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS active BOOLEAN DEFAULT TRUE;
ALTER TABLE restaurants ALTER COLUMN active SET DEFAULT TRUE;
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE restaurants ALTER COLUMN created_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE restaurants ALTER COLUMN updated_at SET DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE restaurants ADD COLUMN IF NOT EXISTS phone VARCHAR(255) DEFAULT '';
ALTER TABLE restaurants ALTER COLUMN phone SET DEFAULT '';

CREATE INDEX IF NOT EXISTS idx_restaurants_owner_id ON restaurants(owner_id);

CREATE TABLE IF NOT EXISTS menu_items (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    price NUMERIC(12, 2) NOT NULL CHECK (price > 0),
    available_only_in_restaurant BOOLEAN NOT NULL DEFAULT FALSE,
    photo_path VARCHAR(500),
    restaurant_id BIGINT NOT NULL REFERENCES restaurants(id)
);

-- O catalogo possui ids de negocio estaveis: 1 = CLIENTE e
-- 2 = DONO_RESTAURANTE. A migracao tambem consolida tipos legados sem perder
-- os usuarios que ja apontavam para eles.
ALTER TABLE user_types DROP CONSTRAINT IF EXISTS chk_user_types_name;

INSERT INTO user_types (id, name, description, active, created_at, updated_at)
VALUES
    (1, '__CLIENTE_TARGET__', 'Cliente do restaurante', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, '__OWNER_TARGET__', 'Dono de restaurante', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

UPDATE users AS app_user
SET user_type_id = CASE
    WHEN UPPER(TRIM(legacy_type.name)) LIKE 'DONO%RESTAURANTE%' THEN 2
    ELSE 1
END
FROM user_types AS legacy_type
WHERE app_user.user_type_id = legacy_type.id;

UPDATE users
SET user_type_id = 1
WHERE user_type_id IS NULL;

DELETE FROM user_types
WHERE id NOT IN (1, 2);

UPDATE user_types
SET name = '__USER_TYPE_TARGET_' || id;

UPDATE user_types
SET name = 'CLIENTE',
    description = 'Cliente do restaurante',
    active = TRUE,
    updated_at = CURRENT_TIMESTAMP
WHERE id = 1;

UPDATE user_types
SET name = 'DONO_RESTAURANTE',
    description = 'Dono de restaurante',
    active = TRUE,
    updated_at = CURRENT_TIMESTAMP
WHERE id = 2;

SELECT setval(
    pg_get_serial_sequence('user_types', 'id'),
    (SELECT MAX(id) FROM user_types),
    TRUE
);

-- Corrige somente dados legados que impediriam a restauracao das entidades de
-- dominio. Novos cadastros continuam sujeitos as validacoes da API.
UPDATE users
SET name = 'Usuario ' || id
WHERE name IS NULL OR TRIM(name) = '';

UPDATE users
SET login = 'legacy-user-' || id
WHERE login IS NULL OR TRIM(login) = '';

UPDATE users
SET password = 'legacy-' || id
WHERE password IS NULL OR LENGTH(password) < 6;

UPDATE users
SET email = email || '.local'
WHERE email ~ '^[^[:space:]@]+@[^[:space:]@.]+$';

UPDATE users
SET email = 'legacy-user-' || id || '@invalid.local'
WHERE email IS NULL
   OR TRIM(email) = ''
   OR email !~ '^[^[:space:]@]+@[^[:space:]@]+\.[^[:space:]@]+$';

ALTER TABLE user_types DROP CONSTRAINT IF EXISTS chk_user_types_name;
ALTER TABLE user_types ADD CONSTRAINT chk_user_types_name
    CHECK (name IN ('CLIENTE', 'DONO_RESTAURANTE'));

-- Em uma base vazia, cria exatamente um usuario de exemplo de cada tipo.
-- Reinicializacoes normais nao duplicam nem apagam os cadastros existentes.
INSERT INTO users (
    id,
    name,
    email,
    login,
    password,
    last_modified_date,
    user_type_id,
    street,
    number,
    neighborhood,
    city,
    state,
    zip_code,
    complement
)
SELECT
    seed.id,
    seed.name,
    seed.email,
    seed.login,
    seed.password,
    CURRENT_TIMESTAMP,
    seed.user_type_id,
    seed.street,
    seed.number,
    seed.neighborhood,
    seed.city,
    seed.state,
    seed.zip_code,
    seed.complement
FROM (VALUES
    (1::BIGINT, 'Cliente Exemplo', 'cliente@exemplo.com', 'cliente', '123456', 1::BIGINT,
        'Rua do Cliente', '100', 'Centro', 'Sao Paulo', 'SP', '01001000', 'Casa'),
    (2::BIGINT, 'Dono de Restaurante Exemplo', 'dono@exemplo.com', 'dono', '123456', 2::BIGINT,
        'Avenida do Restaurante', '200', 'Centro', 'Sao Paulo', 'SP', '01002000', 'Sala 2')
) AS seed(
    id, name, email, login, password, user_type_id,
    street, number, neighborhood, city, state, zip_code, complement
)
WHERE NOT EXISTS (SELECT 1 FROM users);

SELECT setval(
    pg_get_serial_sequence('users', 'id'),
    (SELECT COALESCE(MAX(id), 1) FROM users),
    TRUE
);

CREATE INDEX IF NOT EXISTS idx_menu_items_restaurant_id ON menu_items(restaurant_id);
