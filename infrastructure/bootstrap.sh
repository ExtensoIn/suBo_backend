#!/usr/bin/env bash
#
# One-time bootstrap for the OTP routing graph.
#
# OTP's serving mode (--load) needs a graph.obj that does not exist on a clean
# checkout. Building it is a four-step chain that crosses the app boundary
# (the transit half of the graph comes from a GTFS feed the app generates), so
# it cannot be expressed as compose depends_on. Hence this script.
#
#   1. OSM extract      -> bolivia-latest.osm.pbf
#   2. --buildStreet    -> streetGraph.obj
#   3. app GTFS export  -> subo-gtfs.zip
#   4. --loadStreet     -> graph.obj
#
# Idempotent: existing artifacts are reused. Pass --force to rebuild all.
#
set -euo pipefail

cd "$(dirname "$0")"

DATA_DIR="./otp/data"
PBF_URL="https://download.geofabrik.de/south-america/bolivia-latest.osm.pbf"
PBF_FILE="$DATA_DIR/bolivia-latest.osm.pbf"
STREET_GRAPH="$DATA_DIR/streetGraph.obj"
GTFS_ZIP="$DATA_DIR/subo-gtfs.zip"
GRAPH="$DATA_DIR/graph.obj"

FORCE=0
[[ "${1:-}" == "--force" ]] && FORCE=1

dc() { docker compose --env-file .env "$@"; }
step() { printf '\n\033[1;34m==> %s\033[0m\n' "$1"; }
skip() { printf '    \033[2m%s — skipping (use --force to rebuild)\033[0m\n' "$1"; }

APP_PORT="$(grep -E '^APP_PORT=' .env 2>/dev/null | cut -d= -f2 || true)"
APP_PORT="${APP_PORT:-8080}"

# --- 1. OSM extract --------------------------------------------------------
step "1/4  OSM extract"
if [[ -s "$PBF_FILE" && $FORCE -eq 0 ]]; then
  skip "$PBF_FILE exists"
else
  curl -L --fail --progress-bar -o "$PBF_FILE" "$PBF_URL"
fi

# --- 2. Street graph -------------------------------------------------------
step "2/4  Street graph (--buildStreet)"
if [[ -s "$STREET_GRAPH" && $FORCE -eq 0 ]]; then
  skip "$STREET_GRAPH exists"
else
  dc --profile bootstrap run --rm otp-build-street
fi

# --- 3. GTFS feed from the app --------------------------------------------
step "3/4  GTFS export"
if [[ -s "$GTFS_ZIP" && $FORCE -eq 0 ]]; then
  skip "$GTFS_ZIP exists"
else
  dc up -d --build postgis app

  printf '    waiting for app to become healthy'
  for _ in $(seq 60); do
    if [[ "$(docker inspect -f '{{.State.Health.Status}}' subo-app 2>/dev/null)" == "healthy" ]]; then
      printf ' ok\n'
      break
    fi
    printf '.'
    sleep 3
  done

  if [[ "$(docker inspect -f '{{.State.Health.Status}}' subo-app 2>/dev/null)" != "healthy" ]]; then
    printf '\n\033[1;31mapp never became healthy. Logs:\033[0m\n'
    dc logs --tail 60 app
    exit 1
  fi

  curl -fsS -X POST "http://localhost:${APP_PORT}/internal/gtfs/export"
  printf '\n'
fi

# --- 4. Transit graph ------------------------------------------------------
step "4/4  Transit graph (--loadStreet --save)"
if [[ -s "$GRAPH" && $FORCE -eq 0 ]]; then
  skip "$GRAPH exists"
else
  dc --profile bootstrap run --rm otp-build-transit
fi

step "Done — starting the full stack"
dc up -d

printf '\nStack is up:\n'
printf '  app   http://localhost:%s   (health: /actuator/health)\n' "$APP_PORT"
printf '  otp   http://localhost:8081\n'
LAN_IP="$(hostname -I | awk '{print $1}')"
printf '\nFor the Expo app on a phone, point it at:\n'
printf '  EXPO_PUBLIC_API_URL=http://%s:%s\n\n' "$LAN_IP" "$APP_PORT"
