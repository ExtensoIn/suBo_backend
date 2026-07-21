# syntax=docker/dockerfile:1

# ---------------------------------------------------------------------------
# Build stage
# ---------------------------------------------------------------------------
# Uses the Gradle distribution baked into the image rather than ./gradlew.
# Tag is pinned to the exact version the wrapper declares in
# gradle/wrapper/gradle-wrapper.properties — keep the two in sync.
#
# NOTE: the committed ./gradlew is currently unusable (missing `;` before
# `then` on lines 132 and 182, so `sh -n gradlew` fails to parse). Building
# from the image sidesteps it; if the wrapper is repaired later this stage can
# go back to eclipse-temurin + ./gradlew.
FROM gradle:9.5.1-jdk25 AS build

WORKDIR /app

# Build scripts first: this layer only busts when the build definition
# changes, so dependency resolution stays cached across code edits.
COPY build.gradle settings.gradle ./

# Warm the dependency cache. Kept in a BuildKit cache mount so it also
# survives across image rebuilds, not just across layers.
RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle dependencies --no-daemon --quiet || true

COPY src ./src

RUN --mount=type=cache,target=/home/gradle/.gradle \
    gradle bootJar --no-daemon \
    && cp build/libs/*.jar /app/app.jar

# ---------------------------------------------------------------------------
# Runtime stage
# ---------------------------------------------------------------------------
FROM eclipse-temurin:25-jre

# curl is only here to back the container healthcheck against /actuator/health.
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Unprivileged by default. compose overrides this with the host uid/gid so the
# GTFS export can write into the bind-mounted OTP data directory.
RUN groupadd --system --gid 10001 subo \
    && useradd --system --uid 10001 --gid subo --create-home subo

WORKDIR /app

COPY --from=build /app/app.jar app.jar

# Respect container memory limits instead of the host's total RAM.
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

USER subo

EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=90s --retries=5 \
    CMD curl -fsS http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
