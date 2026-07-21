# subo — local infrastructure

Three containers:

| Service   | Port (host)      | Role |
|-----------|------------------|------|
| `postgis` | `127.0.0.1:55432`| Postgres 18 + PostGIS 3.6. Schema via Flyway (`V1` + `V2` seed). |
| `app`     | `0.0.0.0:8080`   | Spring Boot API. Exposed on all interfaces so a phone can reach it. |
| `otp`     |  `0.0.0.0:8082`   | OpenTripPlanner 2.9.0, serving a pre-built graph. |

All commands below run from this `infrastructure/` directory.

## Quick start

```bash
./bootstrap.sh      # one-time: builds the routing graph, then starts everything
```

Afterwards, the normal day-to-day command is:

```bash
docker compose --env-file .env up -d
```

## Why bootstrap.sh exists

`otp` runs with `--load`, which requires a `graph.obj` that **does not exist on a
clean checkout**. Producing it is a four-step chain that crosses the app
boundary — the transit half of the graph comes from a GTFS feed that the Spring
app generates on demand — so it cannot be expressed as compose `depends_on`.

```
  1. Geofabrik OSM extract          ->  bolivia-latest.osm.pbf   (~165 MB)
  2. otp --buildStreet              ->  streetGraph.obj          (~211 MB)
  3. POST /internal/gtfs/export     ->  subo-gtfs.zip
  4. otp --loadStreet --save        ->  graph.obj
```

Steps 2 and 4 are compose services under the `bootstrap` profile, so they are
never started by a plain `up`:

```bash
docker compose --env-file .env --profile bootstrap run --rm otp-build-street
docker compose --env-file .env --profile bootstrap run --rm otp-build-transit
```

`bootstrap.sh` is idempotent — it skips any artifact that already exists. Use
`./bootstrap.sh --force` to rebuild the whole chain.

> **If `otp` crash-loops**, it is almost always because `graph.obj` is missing.
> Run `./bootstrap.sh`. This is expected on a fresh clone, not a config bug.

All graph artifacts live in `otp/data/` and are gitignored (`*.pbf`, `*.obj`,
`*.zip`). Rebuild them; never commit them.

## Regenerating the graph after route data changes

Editing routes in the DB does not change the graph. Re-export and rebuild:

```bash
curl -X POST http://localhost:8080/internal/gtfs/export
docker compose --env-file .env --profile bootstrap run --rm otp-build-transit
docker compose --env-file .env restart otp
```

Step 2 (`--buildStreet`) only needs redoing when the OSM extract changes.

## Configuration

`application.yaml` targets a host-side workflow (`localhost:55432`,
`localhost:8081`). The compose `app` service overrides those with the
container-network equivalents via environment variables — no Spring profile and
no source changes involved:

| Variable | Value in compose |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgis:5432/subo` |
| `SUBO_OTP_BASE_URL` | `http://otp:8080` |
| `SUBO_GTFS_EXPORT_ZIP_PATH` | `/var/opentripplanner/subo-gtfs.zip` |
| `SPRING_DOCKER_COMPOSE_ENABLED` | `false` |

Tunables in `.env`: `APP_PORT`, `OTP_PORT` (default 8082, avoids Expo Metro on 8081), `OTP_MAX_HEAP`, `APP_UID`/`APP_GID`.

`app` runs as `${APP_UID:-1000}:${APP_GID:-1000}` so the GTFS export can write
into the bind-mounted `otp/data/`. If `id -u` is not 1000, set these in `.env`.

> `.env` is committed with dev-only credentials. Do not reuse them anywhere real.

## Running the app from the IDE instead

The host-side workflow still works and is unaffected. `application.yaml` keeps
`spring.docker.compose.enabled: true`, so starting `SuboApplication` brings up
the compose dependencies itself. Stop the containerised app first so port 8080
is free:

```bash
docker compose --env-file .env stop app
```

## Pointing the Expo frontend at this backend

`localhost` on a phone means the phone. Use the host's LAN address:

```bash
hostname -I | awk '{print $1}'      # e.g. 192.168.26.10
```

In `suBo_frontend`:

```
EXPO_PUBLIC_API_URL=http://192.168.26.10:8080
```

Both machines must be on the same network. If it is unreachable, check the host
firewall — the port is published on `0.0.0.0`, so Docker is not the blocker.

For `expo start --web` you will additionally need CORS on the backend
(`allowed-origins` for `http://localhost:8081`). Native builds do not need it,
and no CORS configuration exists in the app today.

## Windows / macOS notes

The stack is portable, with four caveats.

**1. `bootstrap.sh` needs a bash shell.** On Windows run it from **Git Bash** or
**WSL2**, not PowerShell. If you prefer PowerShell, run the four steps from
"Why bootstrap.sh exists" by hand — they are all plain `docker compose` calls.

**2. Give Docker Desktop enough memory.** Every OTP step requests a 4 GB heap
(`OTP_MAX_HEAP`), and on Windows/macOS containers are capped by the Docker
Desktop VM, not by host RAM. Allocate **at least 6 GB** in Docker Desktop →
Settings → Resources, otherwise `--buildStreet` dies with an opaque exit 137
(OOM-kill). Lowering `OTP_MAX_HEAP` below ~3 GB will not build this graph.

**3. Apple Silicon: switch the Postgres image.** `postgis/postgis` ships
`linux/amd64` only and runs under emulation on M-series Macs. Put a multi-arch
build in `.env`:

```
POSTGIS_IMAGE=imresamu/postgis:18-3.6
```

(`ghcr.io/baosystems/postgis:18-3.6` also works. Both provide amd64 + arm64.)
The OTP, Gradle and Temurin images are already multi-arch.

**4. `APP_UID`/`APP_GID` are a Linux concern.** Docker Desktop's file sharing
maps ownership for you, so the default `1000:1000` is harmless on Windows and
macOS even though the Mac's own uid is usually 501. Only change it on Linux, if
`id -u` is not 1000.

Line endings are pinned in `.gitattributes` (`*.sh`, `Dockerfile`, `*.yaml`
forced to LF), so a Windows checkout will not produce scripts the container
cannot parse.

## Health checks

```bash
curl http://localhost:8080/actuator/health     # {"status":"UP"}
curl http://localhost:8082/otp                 # OTP version/config JSON
docker compose --env-file .env ps
docker compose --env-file .env logs -f app
```
