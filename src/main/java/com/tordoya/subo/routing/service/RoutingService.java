package com.tordoya.subo.routing.service;

import com.tordoya.subo.common.geo.PolylineUtils;
import com.tordoya.subo.otp.dto.request.OtpRoutingRequest;
import com.tordoya.subo.otp.dto.response.OtpPlanResponse;
import com.tordoya.subo.otp.model.OtpTransitMode;
import com.tordoya.subo.otp.service.OtpRoutingService;
import com.tordoya.subo.routing.dto.request.BusquedaRequest;
import com.tordoya.subo.routing.dto.response.*;
import com.tordoya.subo.routing.model.LineaTeleferico;
import com.tordoya.subo.routing.model.ModoTransporte;
import com.tordoya.subo.routing.model.PreferenciaRuta;
import com.tordoya.subo.transport.model.TransportRoute;
import com.tordoya.subo.transport.repository.TransportRouteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoutingService {
    private static final ZoneId LA_PAZ_ZONE = ZoneId.of("America/La_Paz");

    private final OtpRoutingService otpRoutingService;
    private final TransportRouteRepository transportRouteRepository;

    public RoutingService(
            OtpRoutingService otpRoutingService,
            TransportRouteRepository transportRouteRepository
    ) {
        this.otpRoutingService = otpRoutingService;
        this.transportRouteRepository = transportRouteRepository;
    }

    public BusquedaResponse search(BusquedaRequest request) {
        Set<OtpTransitMode> otpModes = toOtpModes(request.modos());
        boolean transitOnly = request.modos().stream()
                .anyMatch(mode -> mode != ModoTransporte.CAMINAR);

        OtpRoutingRequest otpRequest = new OtpRoutingRequest(
                request.origen().coordinate().latitude(),
                request.origen().coordinate().longitude(),
                request.destino().coordinate().latitude(),
                request.destino().coordinate().longitude(),
                OffsetDateTime.now(LA_PAZ_ZONE),
                otpModes,
                transitOnly,
                transitOnly ? 20.0 : 1.0,
                5
        );

        OtpPlanResponse.PlanConnection result = otpRoutingService.search(otpRequest);

        if (result.edges() == null || result.edges().isEmpty()) {
            return new BusquedaResponse(List.of(), List.of(), List.of());
        }

        List<OpcionRutaResponse> opciones = result.edges().stream()
                .map(edge -> mapOption(edge.node()))
                .filter(option -> usesOnlyEnabledModes(option, request.modos()))
                .collect(Collectors.toMap(
                        this::routeSignature,
                        option -> option,
                        (first, _) -> first,
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .sorted(optionComparator(request.preferencia()))
                .toList();

        return new BusquedaResponse(opciones, List.of(), List.of());
    }

    private String routeSignature(OpcionRutaResponse option) {
        return option.steps().stream()
                .map(step -> step.mode()
                        + ":" + step.telefericoLine()
                        + ":" + step.fromLabel()
                        + ":" + step.toLabel())
                .collect(Collectors.joining("|"));
    }

    private Set<OtpTransitMode> toOtpModes(Set<ModoTransporte> modes) {
        Set<OtpTransitMode> result = EnumSet.noneOf(OtpTransitMode.class);

        if (modes.contains(ModoTransporte.TELEFERICO)) {
            result.add(OtpTransitMode.GONDOLA);
        }

        if (modes.contains(ModoTransporte.PUMAKATARI)
                || modes.contains(ModoTransporte.MINIBUS)
                || modes.contains(ModoTransporte.TRUFI)) {
            result.add(OtpTransitMode.BUS);
        }

        return result;
    }

    private OpcionRutaResponse mapOption(OtpPlanResponse.Node node) {
        List<PasoRutaResponse> steps = node.legs().stream()
                .map(this::mapStep)
                .toList();

        BigDecimal totalFare = steps.stream()
                .map(PasoRutaResponse::fareBob)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalDuration = steps.stream()
                .mapToInt(PasoRutaResponse::durationMinutes)
                .sum();

        int transitLegs = (int) steps.stream()
                .filter(step -> step.mode() != ModoTransporte.CAMINAR)
                .count();

        List<String> summary = steps.stream()
                .map(this::buildSummary)
                .toList();

        return new OpcionRutaResponse(
                UUID.randomUUID().toString(),
                summary,
                totalDuration,
                totalFare,
                Math.max(0, transitLegs - 1),
                steps,
                List.of()
        );
    }

    private PasoRutaResponse mapStep(OtpPlanResponse.Leg leg) {
        TransportRoute route = resolveTransportRoute(leg);
        ModoTransporte mode = resolveMode(leg, route);
        BigDecimal fare = route != null ? route.getFareBob() : null;

        int durationMinutes = leg.duration() == null
                ? 0
                : (int) Math.ceil(leg.duration() / 60.0);

        Integer distanceMeters = leg.distance() == null
                ? null
                : (int) Math.round(leg.distance());

        List<LatLngResponse> path = decodePath(leg);

        return new PasoRutaResponse(
                UUID.randomUUID().toString(),
                mode,
                buildInstruction(leg, mode),
                resolveTelefericoLine(route),
                leg.from().name(),
                leg.to().name(),
                durationMinutes,
                distanceMeters,
                fare,
                path
        );
    }

    private TransportRoute resolveTransportRoute(OtpPlanResponse.Leg leg) {
        if (leg.route() == null || leg.route().gtfsId() == null) {
            return null;
        }

        String gtfsId = leg.route().gtfsId();
        String routeId = gtfsId.contains(":")
                ? gtfsId.substring(gtfsId.indexOf(':') + 1)
                : gtfsId;

        if (!routeId.startsWith("ROUTE_")) {
            return null;
        }

        String code = routeId.substring("ROUTE_".length());
        return transportRouteRepository.findByCode(code).orElse(null);
    }

    private ModoTransporte resolveMode(OtpPlanResponse.Leg leg, TransportRoute route) {
        if ("WALK".equals(leg.mode())) {
            return ModoTransporte.CAMINAR;
        }

        if (route == null) {
            if ("GONDOLA".equals(leg.mode())) {
                return ModoTransporte.TELEFERICO;
            }

            throw new IllegalStateException(
                    "Could not resolve Subo transport mode for OTP route "
                            + (leg.route() != null ? leg.route().gtfsId() : "unknown")
            );
        }

        return switch (route.getMode()) {
            case TELEFERICO -> ModoTransporte.TELEFERICO;
            case PUMAKATARI -> ModoTransporte.PUMAKATARI;
            case MINIBUS -> ModoTransporte.MINIBUS;
            case TRUFI -> ModoTransporte.TRUFI;
        };
    }

    private LineaTeleferico resolveTelefericoLine(TransportRoute route) {
        if (route == null || route.getMode() != com.tordoya.subo.transport.model.TransportMode.TELEFERICO) {
            return null;
        }

        String code = route.getCode().replace("TELEFERICO_", "");

        try {
            return LineaTeleferico.valueOf(code);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

    private List<LatLngResponse> decodePath(OtpPlanResponse.Leg leg) {
        List<LatLngResponse> path = PolylineUtils.decode(
                        leg.legGeometry() != null ? leg.legGeometry().points() : null
                )
                .stream()
                .map(point -> new LatLngResponse(point.latitude(), point.longitude()))
                .toList();

        if (path.size() >= 2) {
            return path;
        }

        return List.of(
                new LatLngResponse(leg.from().lat(), leg.from().lon()),
                new LatLngResponse(leg.to().lat(), leg.to().lon())
        );
    }

    private String buildInstruction(OtpPlanResponse.Leg leg, ModoTransporte mode) {
        return switch (mode) {
            case CAMINAR -> "Camina de " + leg.from().name() + " a " + leg.to().name();
            case TELEFERICO -> "Toma el Teleférico de " + leg.from().name() + " a " + leg.to().name();
            case PUMAKATARI -> "Toma el PumaKatari de " + leg.from().name() + " a " + leg.to().name();
            case MINIBUS -> "Toma el minibus de " + leg.from().name() + " a " + leg.to().name();
            case TRUFI -> "Toma el trufi de " + leg.from().name() + " a " + leg.to().name();
        };
    }

    private String buildSummary(PasoRutaResponse step) {
        if (step.mode() == ModoTransporte.CAMINAR) {
            return "A pie";
        }

        if (step.telefericoLine() != null) {
            String name = step.telefericoLine().name().toLowerCase();
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }

        return switch (step.mode()) {
            case PUMAKATARI -> "PumaKatari";
            case MINIBUS -> "Minibus";
            case TRUFI -> "Trufi";
            default -> "Transporte";
        };
    }

    private boolean usesOnlyEnabledModes(
            OpcionRutaResponse option,
            Set<ModoTransporte> enabledModes
    ) {
        return option.steps().stream()
                .filter(step -> step.mode() != ModoTransporte.CAMINAR)
                .allMatch(step -> enabledModes.contains(step.mode()));
    }

    private Comparator<OpcionRutaResponse> optionComparator(PreferenciaRuta preference) {
        if (preference == PreferenciaRuta.MAS_BARATA) {
            return Comparator.comparing(OpcionRutaResponse::precioTotalBob)
                    .thenComparingInt(OpcionRutaResponse::totalDurationMinutes);
        }

        return Comparator.comparingInt(OpcionRutaResponse::totalDurationMinutes);
    }
}