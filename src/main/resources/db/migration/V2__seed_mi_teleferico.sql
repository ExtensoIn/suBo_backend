-- Mi Teleférico seed for Subo
-- Generated for the current Subo schema discussed in the project.
--
-- Coordinate basis:
--   Public OpenStreetMap-derived station coordinates (queried via Mapcarta).
-- Route geometry:
--   Initial hackathon approximation: LINESTRING joins station coordinates in order.
--   This is sufficient for station-based boarding/alighting and can later be replaced
--   by detailed aerialway geometries without changing station IDs or pattern-stop data.
--
-- Service assumptions requested by the project:
--   Headway: ~40 seconds
--   Monday-Saturday: 06:30-22:30
--   Sunday: 07:00-21:00
--   Holidays are NOT represented separately by the current service_window schema.
--   Línea Dorada is intentionally excluded.
--   Línea Plateada segment times: 8 minutes, then 4 minutes.
--
-- Direction convention:
--   OUTBOUND follows the station order supplied in the detailed route screenshots.
--   INBOUND reverses the same route and assumes symmetric segment travel times.
--
-- IMPORTANT:
--   If this is used as a Flyway migration, rename it to your next migration version,
--   e.g. V2__seed_mi_teleferico.sql or V3__seed_mi_teleferico.sql.

BEGIN;

-- -----------------------------------------------------------------------------
-- Transport routes
-- -----------------------------------------------------------------------------
INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '0d844230-8bd2-51d4-bd1e-306d3231c6a5', 'TELEFERICO_ROJA', 'Roja',
    'Línea Roja', 'Mi Teleférico', 'teleferico',
    '#E31B23', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    'e5496b24-0eb3-505f-9169-25b2997f7425', 'TELEFERICO_AMARILLA', 'Amarilla',
    'Línea Amarilla', 'Mi Teleférico', 'teleferico',
    '#F2D000', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '20822904-a766-5b13-8344-70e7751de218', 'TELEFERICO_VERDE', 'Verde',
    'Línea Verde', 'Mi Teleférico', 'teleferico',
    '#2DAA45', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '89787c34-f57c-50e4-b871-f7e004bb22b3', 'TELEFERICO_AZUL', 'Azul',
    'Línea Azul', 'Mi Teleférico', 'teleferico',
    '#1554A3', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '80eba35f-443a-5403-a506-9b14ba8035fb', 'TELEFERICO_NARANJA', 'Naranja',
    'Línea Naranja', 'Mi Teleférico', 'teleferico',
    '#F58220', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '324bf7c6-0e69-51e4-8107-e41a637fa1c1', 'TELEFERICO_BLANCA', 'Blanca',
    'Línea Blanca', 'Mi Teleférico', 'teleferico',
    '#FFFFFF', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    'bf9a1da7-6bde-56e2-b634-a673e1df77f6', 'TELEFERICO_CELESTE', 'Celeste',
    'Línea Celeste', 'Mi Teleférico', 'teleferico',
    '#00AEEF', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '72aa0420-f9fb-52e4-bb8f-f475bd0534b3', 'TELEFERICO_MORADA', 'Morada',
    'Línea Morada', 'Mi Teleférico', 'teleferico',
    '#7A3DA5', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '76194fa2-53ec-5be9-8c28-8392e78e7df3', 'TELEFERICO_CAFE', 'Café',
    'Línea Café', 'Mi Teleférico', 'teleferico',
    '#795548', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

INSERT INTO transport_route (
    id, code, short_name, name, operator_name, mode, color, fare_bob, active
) VALUES (
    '19b4f756-968f-5b81-ad27-addd5e041ddb', 'TELEFERICO_PLATEADA', 'Plateada',
    'Línea Plateada', 'Mi Teleférico', 'teleferico',
    '#A7A9AC', NULL, TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    short_name = EXCLUDED.short_name,
    name = EXCLUDED.name,
    operator_name = EXCLUDED.operator_name,
    mode = EXCLUDED.mode,
    color = EXCLUDED.color,
    fare_bob = EXCLUDED.fare_bob,
    active = EXCLUDED.active;

-- -----------------------------------------------------------------------------
-- Shared physical stations (inserted once even when multiple lines use them)
-- -----------------------------------------------------------------------------
INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '01997bf2-6251-54c1-ad0f-a7a8adce06bf', 'TELEFERICO_CENTRAL', 'Central', 'station',
    ST_SetSRID(ST_MakePoint(-68.14478, -16.49207), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'fc6c0a25-072b-515f-afc0-82a8479d9a50', 'TELEFERICO_CEMENTERIO', 'Cementerio', 'station',
    ST_SetSRID(ST_MakePoint(-68.15292, -16.49784), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '5a495d3b-31dc-57f4-8f0e-9969c29ded58', 'TELEFERICO_16_DE_JULIO', '16 de Julio', 'station',
    ST_SetSRID(ST_MakePoint(-68.16471, -16.49805), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '3f5ba859-b166-5f8c-8b3e-afb4f1e8b5d1', 'TELEFERICO_MIRADOR', 'Mirador', 'station',
    ST_SetSRID(ST_MakePoint(-68.14989, -16.51822), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '9d5e7d62-7e59-5e84-80d6-b70c4b203fc2', 'TELEFERICO_BUENOS_AIRES', 'Buenos Aires', 'station',
    ST_SetSRID(ST_MakePoint(-68.14410, -16.51547), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'd29e0631-2109-576e-bbe1-8963848e3449', 'TELEFERICO_SOPOCACHI', 'Sopocachi', 'station',
    ST_SetSRID(ST_MakePoint(-68.13048, -16.51454), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '83e278a8-9136-50f7-8902-958b306057e4', 'TELEFERICO_LIBERTADOR', 'Libertador', 'station',
    ST_SetSRID(ST_MakePoint(-68.11611, -16.51871), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '1ae58354-9abe-5414-be34-0a1d0852f596', 'TELEFERICO_ALTO_OBRAJES', 'Alto Obrajes', 'station',
    ST_SetSRID(ST_MakePoint(-68.11031, -16.52137), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'f45c6560-9cdc-5ece-bb74-efcaeb3c276e', 'TELEFERICO_OBRAJES', 'Obrajes', 'station',
    ST_SetSRID(ST_MakePoint(-68.10072, -16.52684), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'cfaa8ade-94bb-5648-8a34-f6df38f3e04a', 'TELEFERICO_IRPAVI', 'Irpavi', 'station',
    ST_SetSRID(ST_MakePoint(-68.08730, -16.53810), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '637dd64f-20de-5109-870d-3c510a931e31', 'TELEFERICO_RIO_SECO', 'Río Seco', 'station',
    ST_SetSRID(ST_MakePoint(-68.20957, -16.49003), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '2f8e3cda-d6be-577a-9484-527cf6876bf9', 'TELEFERICO_UPEA', 'UPEA', 'station',
    ST_SetSRID(ST_MakePoint(-68.19307, -16.48950), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '90776c35-7ffa-51c8-a353-cf6a134683d5', 'TELEFERICO_PLAZA_LA_PAZ', 'Plaza La Paz', 'station',
    ST_SetSRID(ST_MakePoint(-68.18308, -16.49227), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '2520d929-ad75-5632-9c64-916558d59ab8', 'TELEFERICO_PLAZA_LIBERTAD', 'Plaza Libertad', 'station',
    ST_SetSRID(ST_MakePoint(-68.17349, -16.49481), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '842519e1-57ba-563e-be76-24e655366c7c', 'TELEFERICO_ARMENTIA', 'Armentia', 'station',
    ST_SetSRID(ST_MakePoint(-68.13685, -16.49036), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '7705bd5c-bd1e-52c9-8069-b491e74d7453', 'TELEFERICO_PERIFERICA', 'Periférica', 'station',
    ST_SetSRID(ST_MakePoint(-68.13129, -16.48649), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'a836e440-8528-5532-9896-ba795187acdd', 'TELEFERICO_PLAZA_VILLARROEL', 'Plaza Villarroel', 'station',
    ST_SetSRID(ST_MakePoint(-68.12194, -16.48469), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'f93ca168-91a3-58ee-84ef-1fddbb21767b', 'TELEFERICO_MONUMENTO_BUSCH', 'Monumento Busch', 'station',
    ST_SetSRID(ST_MakePoint(-68.12107, -16.49373), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '0e939128-380c-50a5-bf97-0af0f7a02132', 'TELEFERICO_PLAZA_TRIANGULAR', 'Plaza Triangular', 'station',
    ST_SetSRID(ST_MakePoint(-68.12085, -16.50411), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '895ce3bd-ad86-5cd8-9f62-376f05c71685', 'TELEFERICO_AV_POETA', 'Av. Poeta', 'station',
    ST_SetSRID(ST_MakePoint(-68.12030, -16.51120), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '927f244e-9d72-5fc9-85bd-6ccfdd836d40', 'TELEFERICO_PRADO', 'Prado', 'station',
    ST_SetSRID(ST_MakePoint(-68.13272, -16.50042), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '9de11332-e504-59f5-befa-e759a309d4a7', 'TELEFERICO_TEATRO_AIRE_LIBRE', 'Teatro al Aire Libre', 'station',
    ST_SetSRID(ST_MakePoint(-68.12615, -16.50411), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'f9117fcf-12ec-5eca-a90f-09fd5448558d', 'TELEFERICO_6_DE_MARZO', '6 de Marzo', 'station',
    ST_SetSRID(ST_MakePoint(-68.16956, -16.52256), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'f26d6ba5-19dd-5dce-9523-14e524d71d53', 'TELEFERICO_FARO_MURILLO', 'Faro Murillo', 'station',
    ST_SetSRID(ST_MakePoint(-68.15366, -16.51225), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    '112336f2-f14d-5c66-8d7c-282331fadba8', 'TELEFERICO_SAN_JOSE', 'San José', 'station',
    ST_SetSRID(ST_MakePoint(-68.13515, -16.50002), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

INSERT INTO transport_stop (
    id, code, name, stop_type, location, active
) VALUES (
    'ec1c7729-ebf1-52de-9ace-3660d87b1754', 'TELEFERICO_LAS_VILLAS', 'Las Villas', 'station',
    ST_SetSRID(ST_MakePoint(-68.11510, -16.49761), 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    code = EXCLUDED.code,
    name = EXCLUDED.name,
    stop_type = EXCLUDED.stop_type,
    location = EXCLUDED.location,
    active = EXCLUDED.active;

-- -----------------------------------------------------------------------------
-- Route patterns (one per direction)
-- -----------------------------------------------------------------------------
INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    'aa402fbb-4453-542e-9b07-ccdae84c0745', '0d844230-8bd2-51d4-bd1e-306d3231c6a5', 'Línea Roja - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.14478 -16.49207, -68.15292 -16.49784, -68.16471 -16.49805)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '68fb3231-1c7c-5dfc-8d17-3ce42a8efd05', '0d844230-8bd2-51d4-bd1e-306d3231c6a5', 'Línea Roja - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.16471 -16.49805, -68.15292 -16.49784, -68.14478 -16.49207)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f', 'e5496b24-0eb3-505f-9169-25b2997f7425', 'Línea Amarilla - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.14989 -16.51822, -68.14410 -16.51547, -68.13048 -16.51454, -68.11611 -16.51871)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '3d54b87a-3ede-5283-9cc6-da40c47a4508', 'e5496b24-0eb3-505f-9169-25b2997f7425', 'Línea Amarilla - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.11611 -16.51871, -68.13048 -16.51454, -68.14410 -16.51547, -68.14989 -16.51822)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '3dbc3688-2ac2-5750-ac04-689d79288df9', '20822904-a766-5b13-8344-70e7751de218', 'Línea Verde - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.11611 -16.51871, -68.11031 -16.52137, -68.10072 -16.52684, -68.08730 -16.53810)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '337764d3-68ca-5e7c-9c5a-5790bd31de64', '20822904-a766-5b13-8344-70e7751de218', 'Línea Verde - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.08730 -16.53810, -68.10072 -16.52684, -68.11031 -16.52137, -68.11611 -16.51871)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '3d963aec-7d33-5da5-9db3-c59acca89aa0', '89787c34-f57c-50e4-b871-f7e004bb22b3', 'Línea Azul - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.20957 -16.49003, -68.19307 -16.48950, -68.18308 -16.49227, -68.17349 -16.49481, -68.16471 -16.49805)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '0d1de736-29d4-589e-a172-c58cb4aee364', '89787c34-f57c-50e4-b871-f7e004bb22b3', 'Línea Azul - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.16471 -16.49805, -68.17349 -16.49481, -68.18308 -16.49227, -68.19307 -16.48950, -68.20957 -16.49003)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    'a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a', '80eba35f-443a-5403-a506-9b14ba8035fb', 'Línea Naranja - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.14478 -16.49207, -68.13685 -16.49036, -68.13129 -16.48649, -68.12194 -16.48469)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '42d20d79-f51b-5bef-9259-e7a584ccd226', '80eba35f-443a-5403-a506-9b14ba8035fb', 'Línea Naranja - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.12194 -16.48469, -68.13129 -16.48649, -68.13685 -16.49036, -68.14478 -16.49207)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    'a9a7c8fb-17f2-52b7-8782-b8bb939cf05a', '324bf7c6-0e69-51e4-8107-e41a637fa1c1', 'Línea Blanca - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.12194 -16.48469, -68.12107 -16.49373, -68.12085 -16.50411, -68.12030 -16.51120)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '511e5b1b-e6a2-52f6-9c65-c8588299f583', '324bf7c6-0e69-51e4-8107-e41a637fa1c1', 'Línea Blanca - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.12030 -16.51120, -68.12085 -16.50411, -68.12107 -16.49373, -68.12194 -16.48469)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '655f2036-1f06-54d0-98b2-d0c9f5ac741c', 'bf9a1da7-6bde-56e2-b634-a673e1df77f6', 'Línea Celeste - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.13272 -16.50042, -68.12615 -16.50411, -68.12030 -16.51120, -68.11611 -16.51871)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '2ec8342e-c96f-5948-887f-a44d5c39f6d9', 'bf9a1da7-6bde-56e2-b634-a673e1df77f6', 'Línea Celeste - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.11611 -16.51871, -68.12030 -16.51120, -68.12615 -16.50411, -68.13272 -16.50042)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '421df8e1-b216-5717-a2da-5ae4d5ab9b22', '72aa0420-f9fb-52e4-bb8f-f475bd0534b3', 'Línea Morada - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.16956 -16.52256, -68.15366 -16.51225, -68.13515 -16.50002)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '8547c54e-f139-5107-a850-9f241c6902e6', '72aa0420-f9fb-52e4-bb8f-f475bd0534b3', 'Línea Morada - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.13515 -16.50002, -68.15366 -16.51225, -68.16956 -16.52256)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '84c89a53-1f66-5dd2-94d5-25c2e3eee202', '76194fa2-53ec-5be9-8c28-8392e78e7df3', 'Línea Café - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.12107 -16.49373, -68.11510 -16.49761)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '56dde7c5-2a33-54eb-88aa-ed9759d5c8a4', '76194fa2-53ec-5be9-8c28-8392e78e7df3', 'Línea Café - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.11510 -16.49761, -68.12107 -16.49373)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '3c0f2743-04b7-50ff-b7f5-57105af27511', '19b4f756-968f-5b81-ad27-addd5e041ddb', 'Línea Plateada - Ida', 'outbound',
    ST_GeomFromText('LINESTRING(-68.16471 -16.49805, -68.15366 -16.51225, -68.14989 -16.51822)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

INSERT INTO route_pattern (
    id, route_id, name, direction, shape, active
) VALUES (
    '3d9caec4-086f-54a4-8b8b-a942a196e8d3', '19b4f756-968f-5b81-ad27-addd5e041ddb', 'Línea Plateada - Vuelta', 'inbound',
    ST_GeomFromText('LINESTRING(-68.14989 -16.51822, -68.15366 -16.51225, -68.16471 -16.49805)', 4326),
    TRUE
)
ON CONFLICT (id) DO UPDATE SET
    route_id = EXCLUDED.route_id,
    name = EXCLUDED.name,
    direction = EXCLUDED.direction,
    shape = EXCLUDED.shape,
    active = EXCLUDED.active;

-- Rebuild ordered station occurrences and service windows for these seeded patterns.
DELETE FROM route_pattern_stop
WHERE route_pattern_id IN (
    'aa402fbb-4453-542e-9b07-ccdae84c0745'::uuid,
    '68fb3231-1c7c-5dfc-8d17-3ce42a8efd05'::uuid,
    '16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f'::uuid,
    '3d54b87a-3ede-5283-9cc6-da40c47a4508'::uuid,
    '3dbc3688-2ac2-5750-ac04-689d79288df9'::uuid,
    '337764d3-68ca-5e7c-9c5a-5790bd31de64'::uuid,
    '3d963aec-7d33-5da5-9db3-c59acca89aa0'::uuid,
    '0d1de736-29d4-589e-a172-c58cb4aee364'::uuid,
    'a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a'::uuid,
    '42d20d79-f51b-5bef-9259-e7a584ccd226'::uuid,
    'a9a7c8fb-17f2-52b7-8782-b8bb939cf05a'::uuid,
    '511e5b1b-e6a2-52f6-9c65-c8588299f583'::uuid,
    '655f2036-1f06-54d0-98b2-d0c9f5ac741c'::uuid,
    '2ec8342e-c96f-5948-887f-a44d5c39f6d9'::uuid,
    '421df8e1-b216-5717-a2da-5ae4d5ab9b22'::uuid,
    '8547c54e-f139-5107-a850-9f241c6902e6'::uuid,
    '84c89a53-1f66-5dd2-94d5-25c2e3eee202'::uuid,
    '56dde7c5-2a33-54eb-88aa-ed9759d5c8a4'::uuid,
    '3c0f2743-04b7-50ff-b7f5-57105af27511'::uuid,
    '3d9caec4-086f-54a4-8b8b-a942a196e8d3'::uuid
);

DELETE FROM service_window
WHERE route_pattern_id IN (
    'aa402fbb-4453-542e-9b07-ccdae84c0745'::uuid,
    '68fb3231-1c7c-5dfc-8d17-3ce42a8efd05'::uuid,
    '16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f'::uuid,
    '3d54b87a-3ede-5283-9cc6-da40c47a4508'::uuid,
    '3dbc3688-2ac2-5750-ac04-689d79288df9'::uuid,
    '337764d3-68ca-5e7c-9c5a-5790bd31de64'::uuid,
    '3d963aec-7d33-5da5-9db3-c59acca89aa0'::uuid,
    '0d1de736-29d4-589e-a172-c58cb4aee364'::uuid,
    'a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a'::uuid,
    '42d20d79-f51b-5bef-9259-e7a584ccd226'::uuid,
    'a9a7c8fb-17f2-52b7-8782-b8bb939cf05a'::uuid,
    '511e5b1b-e6a2-52f6-9c65-c8588299f583'::uuid,
    '655f2036-1f06-54d0-98b2-d0c9f5ac741c'::uuid,
    '2ec8342e-c96f-5948-887f-a44d5c39f6d9'::uuid,
    '421df8e1-b216-5717-a2da-5ae4d5ab9b22'::uuid,
    '8547c54e-f139-5107-a850-9f241c6902e6'::uuid,
    '84c89a53-1f66-5dd2-94d5-25c2e3eee202'::uuid,
    '56dde7c5-2a33-54eb-88aa-ed9759d5c8a4'::uuid,
    '3c0f2743-04b7-50ff-b7f5-57105af27511'::uuid,
    '3d9caec4-086f-54a4-8b8b-a942a196e8d3'::uuid
);

-- -----------------------------------------------------------------------------
-- Ordered stations and travel time from previous station
-- -----------------------------------------------------------------------------
INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'aa402fbb-4453-542e-9b07-ccdae84c0745', '01997bf2-6251-54c1-ad0f-a7a8adce06bf', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'aa402fbb-4453-542e-9b07-ccdae84c0745', 'fc6c0a25-072b-515f-afc0-82a8479d9a50', 2, 309
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'aa402fbb-4453-542e-9b07-ccdae84c0745', '5a495d3b-31dc-57f4-8f0e-9969c29ded58', 3, 338
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '68fb3231-1c7c-5dfc-8d17-3ce42a8efd05', '5a495d3b-31dc-57f4-8f0e-9969c29ded58', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '68fb3231-1c7c-5dfc-8d17-3ce42a8efd05', 'fc6c0a25-072b-515f-afc0-82a8479d9a50', 2, 338
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '68fb3231-1c7c-5dfc-8d17-3ce42a8efd05', '01997bf2-6251-54c1-ad0f-a7a8adce06bf', 3, 309
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f', '3f5ba859-b166-5f8c-8b3e-afb4f1e8b5d1', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f', '9d5e7d62-7e59-5e84-80d6-b70c4b203fc2', 2, 240
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f', 'd29e0631-2109-576e-bbe1-8963848e3449', 3, 375
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f', '83e278a8-9136-50f7-8902-958b306057e4', 4, 397
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d54b87a-3ede-5283-9cc6-da40c47a4508', '83e278a8-9136-50f7-8902-958b306057e4', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d54b87a-3ede-5283-9cc6-da40c47a4508', 'd29e0631-2109-576e-bbe1-8963848e3449', 2, 397
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d54b87a-3ede-5283-9cc6-da40c47a4508', '9d5e7d62-7e59-5e84-80d6-b70c4b203fc2', 3, 375
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d54b87a-3ede-5283-9cc6-da40c47a4508', '3f5ba859-b166-5f8c-8b3e-afb4f1e8b5d1', 4, 240
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3dbc3688-2ac2-5750-ac04-689d79288df9', '83e278a8-9136-50f7-8902-958b306057e4', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3dbc3688-2ac2-5750-ac04-689d79288df9', '1ae58354-9abe-5414-be34-0a1d0852f596', 2, 205
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3dbc3688-2ac2-5750-ac04-689d79288df9', 'f45c6560-9cdc-5ece-bb74-efcaeb3c276e', 3, 309
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3dbc3688-2ac2-5750-ac04-689d79288df9', 'cfaa8ade-94bb-5648-8a34-f6df38f3e04a', 4, 467
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '337764d3-68ca-5e7c-9c5a-5790bd31de64', 'cfaa8ade-94bb-5648-8a34-f6df38f3e04a', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '337764d3-68ca-5e7c-9c5a-5790bd31de64', 'f45c6560-9cdc-5ece-bb74-efcaeb3c276e', 2, 467
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '337764d3-68ca-5e7c-9c5a-5790bd31de64', '1ae58354-9abe-5414-be34-0a1d0852f596', 3, 309
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '337764d3-68ca-5e7c-9c5a-5790bd31de64', '83e278a8-9136-50f7-8902-958b306057e4', 4, 205
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d963aec-7d33-5da5-9db3-c59acca89aa0', '637dd64f-20de-5109-870d-3c510a931e31', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d963aec-7d33-5da5-9db3-c59acca89aa0', '2f8e3cda-d6be-577a-9484-527cf6876bf9', 2, 426
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d963aec-7d33-5da5-9db3-c59acca89aa0', '90776c35-7ffa-51c8-a353-cf6a134683d5', 3, 283
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d963aec-7d33-5da5-9db3-c59acca89aa0', '2520d929-ad75-5632-9c64-916558d59ab8', 4, 258
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d963aec-7d33-5da5-9db3-c59acca89aa0', '5a495d3b-31dc-57f4-8f0e-9969c29ded58', 5, 268
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '0d1de736-29d4-589e-a172-c58cb4aee364', '5a495d3b-31dc-57f4-8f0e-9969c29ded58', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '0d1de736-29d4-589e-a172-c58cb4aee364', '2520d929-ad75-5632-9c64-916558d59ab8', 2, 268
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '0d1de736-29d4-589e-a172-c58cb4aee364', '90776c35-7ffa-51c8-a353-cf6a134683d5', 3, 258
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '0d1de736-29d4-589e-a172-c58cb4aee364', '2f8e3cda-d6be-577a-9484-527cf6876bf9', 4, 283
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '0d1de736-29d4-589e-a172-c58cb4aee364', '637dd64f-20de-5109-870d-3c510a931e31', 5, 426
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a', '01997bf2-6251-54c1-ad0f-a7a8adce06bf', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a', '842519e1-57ba-563e-be76-24e655366c7c', 2, 239
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a', '7705bd5c-bd1e-52c9-8069-b491e74d7453', 3, 227
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a', 'a836e440-8528-5532-9896-ba795187acdd', 4, 274
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '42d20d79-f51b-5bef-9259-e7a584ccd226', 'a836e440-8528-5532-9896-ba795187acdd', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '42d20d79-f51b-5bef-9259-e7a584ccd226', '7705bd5c-bd1e-52c9-8069-b491e74d7453', 2, 274
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '42d20d79-f51b-5bef-9259-e7a584ccd226', '842519e1-57ba-563e-be76-24e655366c7c', 3, 227
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '42d20d79-f51b-5bef-9259-e7a584ccd226', '01997bf2-6251-54c1-ad0f-a7a8adce06bf', 4, 239
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a9a7c8fb-17f2-52b7-8782-b8bb939cf05a', 'a836e440-8528-5532-9896-ba795187acdd', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a9a7c8fb-17f2-52b7-8782-b8bb939cf05a', 'f93ca168-91a3-58ee-84ef-1fddbb21767b', 2, 237
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a9a7c8fb-17f2-52b7-8782-b8bb939cf05a', '0e939128-380c-50a5-bf97-0af0f7a02132', 3, 288
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    'a9a7c8fb-17f2-52b7-8782-b8bb939cf05a', '895ce3bd-ad86-5cd8-9f62-376f05c71685', 4, 196
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '511e5b1b-e6a2-52f6-9c65-c8588299f583', '895ce3bd-ad86-5cd8-9f62-376f05c71685', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '511e5b1b-e6a2-52f6-9c65-c8588299f583', '0e939128-380c-50a5-bf97-0af0f7a02132', 2, 196
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '511e5b1b-e6a2-52f6-9c65-c8588299f583', 'f93ca168-91a3-58ee-84ef-1fddbb21767b', 3, 288
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '511e5b1b-e6a2-52f6-9c65-c8588299f583', 'a836e440-8528-5532-9896-ba795187acdd', 4, 237
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '655f2036-1f06-54d0-98b2-d0c9f5ac741c', '927f244e-9d72-5fc9-85bd-6ccfdd836d40', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '655f2036-1f06-54d0-98b2-d0c9f5ac741c', '9de11332-e504-59f5-befa-e759a309d4a7', 2, 220
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '655f2036-1f06-54d0-98b2-d0c9f5ac741c', '895ce3bd-ad86-5cd8-9f62-376f05c71685', 3, 210
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '655f2036-1f06-54d0-98b2-d0c9f5ac741c', '83e278a8-9136-50f7-8902-958b306057e4', 4, 190
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '2ec8342e-c96f-5948-887f-a44d5c39f6d9', '83e278a8-9136-50f7-8902-958b306057e4', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '2ec8342e-c96f-5948-887f-a44d5c39f6d9', '895ce3bd-ad86-5cd8-9f62-376f05c71685', 2, 190
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '2ec8342e-c96f-5948-887f-a44d5c39f6d9', '9de11332-e504-59f5-befa-e759a309d4a7', 3, 210
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '2ec8342e-c96f-5948-887f-a44d5c39f6d9', '927f244e-9d72-5fc9-85bd-6ccfdd836d40', 4, 220
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '421df8e1-b216-5717-a2da-5ae4d5ab9b22', 'f9117fcf-12ec-5eca-a90f-09fd5448558d', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '421df8e1-b216-5717-a2da-5ae4d5ab9b22', 'f26d6ba5-19dd-5dce-9523-14e524d71d53', 2, 487
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '421df8e1-b216-5717-a2da-5ae4d5ab9b22', '112336f2-f14d-5c66-8d7c-282331fadba8', 3, 480
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '8547c54e-f139-5107-a850-9f241c6902e6', '112336f2-f14d-5c66-8d7c-282331fadba8', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '8547c54e-f139-5107-a850-9f241c6902e6', 'f26d6ba5-19dd-5dce-9523-14e524d71d53', 2, 480
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '8547c54e-f139-5107-a850-9f241c6902e6', 'f9117fcf-12ec-5eca-a90f-09fd5448558d', 3, 487
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '84c89a53-1f66-5dd2-94d5-25c2e3eee202', 'f93ca168-91a3-58ee-84ef-1fddbb21767b', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '84c89a53-1f66-5dd2-94d5-25c2e3eee202', 'ec1c7729-ebf1-52de-9ace-3660d87b1754', 2, 227
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '56dde7c5-2a33-54eb-88aa-ed9759d5c8a4', 'ec1c7729-ebf1-52de-9ace-3660d87b1754', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '56dde7c5-2a33-54eb-88aa-ed9759d5c8a4', 'f93ca168-91a3-58ee-84ef-1fddbb21767b', 2, 227
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3c0f2743-04b7-50ff-b7f5-57105af27511', '5a495d3b-31dc-57f4-8f0e-9969c29ded58', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3c0f2743-04b7-50ff-b7f5-57105af27511', 'f26d6ba5-19dd-5dce-9523-14e524d71d53', 2, 480
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3c0f2743-04b7-50ff-b7f5-57105af27511', '3f5ba859-b166-5f8c-8b3e-afb4f1e8b5d1', 3, 240
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d9caec4-086f-54a4-8b8b-a942a196e8d3', '3f5ba859-b166-5f8c-8b3e-afb4f1e8b5d1', 1, NULL
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d9caec4-086f-54a4-8b8b-a942a196e8d3', 'f26d6ba5-19dd-5dce-9523-14e524d71d53', 2, 240
);

INSERT INTO route_pattern_stop (
    route_pattern_id, stop_id, stop_sequence, estimated_seconds_from_previous
) VALUES (
    '3d9caec4-086f-54a4-8b8b-a942a196e8d3', '5a495d3b-31dc-57f4-8f0e-9969c29ded58', 3, 480
);

-- -----------------------------------------------------------------------------
-- Service windows
-- day_of_week: 1=Monday ... 7=Sunday
-- Teleférico is modeled as frequency-based service with ~40-second headway.
-- -----------------------------------------------------------------------------

-- Monday through Saturday
INSERT INTO service_window (
    id,
    route_pattern_id,
    day_of_week,
    start_time,
    end_time,
    headway_seconds,
    schedule_type,
    active
)
SELECT
    gen_random_uuid(),
    p.route_pattern_id,
    d.day_of_week,
    TIME '06:30:00',
    TIME '22:30:00',
    40,
    'frequency_based',
    TRUE
FROM (
    VALUES
    ('aa402fbb-4453-542e-9b07-ccdae84c0745'::uuid),
    ('68fb3231-1c7c-5dfc-8d17-3ce42a8efd05'::uuid),
    ('16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f'::uuid),
    ('3d54b87a-3ede-5283-9cc6-da40c47a4508'::uuid),
    ('3dbc3688-2ac2-5750-ac04-689d79288df9'::uuid),
    ('337764d3-68ca-5e7c-9c5a-5790bd31de64'::uuid),
    ('3d963aec-7d33-5da5-9db3-c59acca89aa0'::uuid),
    ('0d1de736-29d4-589e-a172-c58cb4aee364'::uuid),
    ('a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a'::uuid),
    ('42d20d79-f51b-5bef-9259-e7a584ccd226'::uuid),
    ('a9a7c8fb-17f2-52b7-8782-b8bb939cf05a'::uuid),
    ('511e5b1b-e6a2-52f6-9c65-c8588299f583'::uuid),
    ('655f2036-1f06-54d0-98b2-d0c9f5ac741c'::uuid),
    ('2ec8342e-c96f-5948-887f-a44d5c39f6d9'::uuid),
    ('421df8e1-b216-5717-a2da-5ae4d5ab9b22'::uuid),
    ('8547c54e-f139-5107-a850-9f241c6902e6'::uuid),
    ('84c89a53-1f66-5dd2-94d5-25c2e3eee202'::uuid),
    ('56dde7c5-2a33-54eb-88aa-ed9759d5c8a4'::uuid),
    ('3c0f2743-04b7-50ff-b7f5-57105af27511'::uuid),
    ('3d9caec4-086f-54a4-8b8b-a942a196e8d3'::uuid)
) AS p(route_pattern_id)
CROSS JOIN generate_series(1, 6) AS d(day_of_week);

-- Sunday
INSERT INTO service_window (
    id,
    route_pattern_id,
    day_of_week,
    start_time,
    end_time,
    headway_seconds,
    schedule_type,
    active
)
SELECT
    gen_random_uuid(),
    p.route_pattern_id,
    7,
    TIME '07:00:00',
    TIME '21:00:00',
    40,
    'frequency_based',
    TRUE
FROM (
    VALUES
    ('aa402fbb-4453-542e-9b07-ccdae84c0745'::uuid),
    ('68fb3231-1c7c-5dfc-8d17-3ce42a8efd05'::uuid),
    ('16bbc965-4db9-5f92-ba6e-90ca4d7e0c7f'::uuid),
    ('3d54b87a-3ede-5283-9cc6-da40c47a4508'::uuid),
    ('3dbc3688-2ac2-5750-ac04-689d79288df9'::uuid),
    ('337764d3-68ca-5e7c-9c5a-5790bd31de64'::uuid),
    ('3d963aec-7d33-5da5-9db3-c59acca89aa0'::uuid),
    ('0d1de736-29d4-589e-a172-c58cb4aee364'::uuid),
    ('a7b1f4a0-35b8-519f-8b04-bbc1ca1b9a2a'::uuid),
    ('42d20d79-f51b-5bef-9259-e7a584ccd226'::uuid),
    ('a9a7c8fb-17f2-52b7-8782-b8bb939cf05a'::uuid),
    ('511e5b1b-e6a2-52f6-9c65-c8588299f583'::uuid),
    ('655f2036-1f06-54d0-98b2-d0c9f5ac741c'::uuid),
    ('2ec8342e-c96f-5948-887f-a44d5c39f6d9'::uuid),
    ('421df8e1-b216-5717-a2da-5ae4d5ab9b22'::uuid),
    ('8547c54e-f139-5107-a850-9f241c6902e6'::uuid),
    ('84c89a53-1f66-5dd2-94d5-25c2e3eee202'::uuid),
    ('56dde7c5-2a33-54eb-88aa-ed9759d5c8a4'::uuid),
    ('3c0f2743-04b7-50ff-b7f5-57105af27511'::uuid),
    ('3d9caec4-086f-54a4-8b8b-a942a196e8d3'::uuid)
) AS p(route_pattern_id);

COMMIT;
