CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE transport_stop
(
    id         UUID PRIMARY KEY               DEFAULT gen_random_uuid(),
    code       VARCHAR(80) UNIQUE,
    name       VARCHAR(200)          NOT NULL,
    stop_type  VARCHAR(30)           NOT NULL
        CHECK (
            stop_type IN (
                          'stop',
                          'station',
                          'informal_stop'
                )
            ),
    location   geometry(POINT, 4326) NOT NULL,
    active     BOOLEAN               NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ           NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ           NOT NULL DEFAULT now()
);

CREATE INDEX idx_transport_stop_location
    ON transport_stop
        USING GIST (location);


CREATE TABLE transport_route
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    code          VARCHAR(80)  NOT NULL UNIQUE,
    name          VARCHAR(200) NOT NULL,
    operator_name VARCHAR(200),
    mode          VARCHAR(30)  NOT NULL
        CHECK (
            mode IN (
                     'teleferico',
                     'pumakatari',
                     'minibus',
                     'trufi'
                )
            ),
    short_name    VARCHAR(80)  NOT NULL,
    color         VARCHAR(7)
        CHECK (
            color IS NULL
                OR color ~ '^#[0-9A-Fa-f]{6}$'
            ),
    fare_bob      NUMERIC(8, 2)
        CHECK (
            fare_bob >= 0
            ),
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);


CREATE TABLE route_pattern
(
    id         UUID PRIMARY KEY                    DEFAULT gen_random_uuid(),
    route_id   UUID                       NOT NULL
        REFERENCES transport_route (id)
            ON DELETE CASCADE,

    name       VARCHAR(200)               NOT NULL,
    direction  VARCHAR(30)                NOT NULL
        CHECK (
            direction IN (
                          'outbound',
                          'inbound',
                          'circular',
                          'variant'
                )
            ),
    shape      geometry(LINESTRING, 4326) NOT NULL,
    active     BOOLEAN                    NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ                NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ                NOT NULL DEFAULT now(),
    UNIQUE (route_id, name)
);

CREATE INDEX idx_route_pattern_shape
    ON route_pattern
        USING GIST (shape);

CREATE TABLE route_pattern_stop
(
    route_pattern_id                UUID    NOT NULL
        REFERENCES route_pattern (id)
            ON DELETE CASCADE,
    stop_id                         UUID    NOT NULL
        REFERENCES transport_stop (id),
    stop_sequence                   INTEGER NOT NULL
        CHECK (stop_sequence >= 0),
    estimated_seconds_from_previous INTEGER
        CHECK (
            estimated_seconds_from_previous IS NULL
                OR estimated_seconds_from_previous >= 0
            ),
    pick_up_allowed                 BOOLEAN NOT NULL DEFAULT TRUE,
    drop_off_allowed                BOOLEAN NOT NULL DEFAULT TRUE,
    PRIMARY KEY (
                 route_pattern_id,
                 stop_sequence
        )
);

CREATE INDEX idx_route_pattern_stop_stop
    ON route_pattern_stop (stop_id);

CREATE TABLE service_window
(
    id               UUID PRIMARY KEY  DEFAULT gen_random_uuid(),
    route_pattern_id UUID     NOT NULL
        REFERENCES route_pattern (id)
            ON DELETE CASCADE,
    day_of_week      SMALLINT NOT NULL
        CHECK (day_of_week BETWEEN 1 AND 7),
    start_time       TIME     NOT NULL,
    end_time         TIME     NOT NULL,
    headway_seconds  INTEGER
        CHECK (
            headway_seconds IS NULL
                OR headway_seconds > 0
            ),
    schedule_type    VARCHAR(20)
        CHECK (
            schedule_type IN (
                              'fixed',
                              'frequency_based',
                              'estimated_frequency'
                )
            ),
    active           BOOLEAN  NOT NULL DEFAULT TRUE,
    CHECK (end_time > start_time)
);

CREATE INDEX idx_service_window_pattern_day
    ON service_window (
                       route_pattern_id,
                       day_of_week
        );

CREATE TABLE anonymous_device
(
    device_id       VARCHAR(128) PRIMARY KEY,
    expo_push_token TEXT UNIQUE,
    language        VARCHAR(2)  NOT NULL DEFAULT 'es'
        CHECK (
            language IN (
                         'es',
                         'qu',
                         'ay',
                         'en'
                )
            ),

    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    last_seen_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);


CREATE TABLE community_alert
(
    id          UUID PRIMARY KEY               DEFAULT gen_random_uuid(),
    device_id   VARCHAR(128)          NOT NULL
        REFERENCES anonymous_device (device_id),
    alert_type  VARCHAR(30)           NOT NULL
        CHECK (
            alert_type IN (
                           'bloqueo',
                           'trafico',
                           'puma_lleno',
                           'teleferico_lleno'
                )
            ),
    location    geometry(POINT, 4326) NOT NULL,
    reported_at TIMESTAMPTZ           NOT NULL DEFAULT now(),
    expires_at  TIMESTAMPTZ           NOT NULL,

    CHECK (expires_at > reported_at)
);

CREATE INDEX idx_community_alert_location
    ON community_alert
        USING GIST (location);

CREATE INDEX idx_community_alert_expiration
    ON community_alert (expires_at);

CREATE INDEX idx_community_alert_device_time
    ON community_alert (
                        device_id,
                        reported_at DESC
        );

CREATE TABLE itinerary
(
    id                     UUID PRIMARY KEY       DEFAULT gen_random_uuid(),
    summary                TEXT[]        NOT NULL DEFAULT '{}',
    total_duration_minutes INTEGER       NOT NULL
        CHECK (
            total_duration_minutes >= 0
            ),
    total_fare_bob         NUMERIC(8, 2) NOT NULL
        CHECK (
            total_fare_bob >= 0
            ),
    transfers              INTEGER       NOT NULL
        CHECK (
            transfers >= 0
            ),
    tags                   TEXT[]        NOT NULL DEFAULT '{}',
    created_at             TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE shared_trip
(
    id                   UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    owner_device_id      VARCHAR(128) NOT NULL
        REFERENCES anonymous_device (device_id),
    itinerary_id         UUID         NOT NULL
        REFERENCES itinerary (id),
    share_token_hash     CHAR(64)     NOT NULL UNIQUE,
    write_token_hash     CHAR(64)     NOT NULL UNIQUE,
    expiration_mode      VARCHAR(20)  NOT NULL
        CHECK (
            expiration_mode IN (
                                'until_arrival',
                                'fixed_time'
                )
            ),
    status               VARCHAR(20)  NOT NULL DEFAULT 'en_curso'
        CHECK (
            status IN (
                       'en_curso',
                       'finalizado',
                       'cancelado'
                )
            ),
    current_step         INTEGER      NOT NULL DEFAULT 0
        CHECK (current_step >= 0),
    current_position     geometry(POINT, 4326),
    estimated_arrival_at TIMESTAMPTZ,
    started_at           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at           TIMESTAMPTZ  NOT NULL DEFAULT now(),
    expires_at           TIMESTAMPTZ
        CHECK (
            (
                expiration_mode = 'until_arrival'
                    AND expires_at IS NULL
                )
                OR
            (
                expiration_mode = 'fixed_time'
                    AND expires_at IS NOT NULL
                )
            ),
    ended_at             TIMESTAMPTZ,
    CHECK (expires_at > started_at),
    CHECK (
        ended_at IS NULL
            OR ended_at >= started_at
        )
);

CREATE INDEX idx_shared_trip_owner
    ON shared_trip (owner_device_id);

CREATE INDEX idx_shared_trip_status_expiration
    ON shared_trip (
                    status,
                    expires_at
        );

CREATE TABLE itinerary_step
(
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    itinerary_id     UUID                       NOT NULL
        REFERENCES itinerary (id)
            ON DELETE CASCADE,
    step_sequence    INTEGER                    NOT NULL
        CHECK (
            step_sequence >= 0
            ),
    mode             VARCHAR(30)                NOT NULL
        CHECK (
            mode IN (
                     'caminar',
                     'teleferico',
                     'pumakatari',
                     'minibus',
                     'trufi'
                )
            ),
    route_pattern_id UUID
        REFERENCES route_pattern (id),
    instruction      VARCHAR(300),
    from_label       VARCHAR(200)               NOT NULL,
    to_label         VARCHAR(200)               NOT NULL,
    duration_minutes INTEGER                    NOT NULL
        CHECK (
            duration_minutes >= 0
            ),
    distance_meters  INTEGER
        CHECK (
            distance_meters IS NULL
                OR distance_meters >= 0
            ),
    fare_bob         NUMERIC(8, 2)
        CHECK (
            fare_bob IS NULL
                OR fare_bob >= 0
            ),
    path             geometry(LINESTRING, 4326) NOT NULL,
    UNIQUE (
            itinerary_id,
            step_sequence
        )
);
