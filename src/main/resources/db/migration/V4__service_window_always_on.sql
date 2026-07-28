-- Make every service window cover the whole day so the app returns rutas at
-- any hour while the product is still being demoed and tested.
--
-- Background: V2 originally seeded realistic Mi Teleférico hours (06:30-22:30
-- weekdays, 07:00-21:00 Sundays). Those are correct for production but mean
-- /busquedas returns zero opciones outside them, which reads as a broken app
-- during off-hours testing — and /busquedas has no departure-time parameter,
-- so there is no way to preview another time from the client.
--
-- This is a TESTING convenience. Revert it (or gate it behind a Flyway
-- placeholder / profile) before launch, and restore the real timetable.
--
-- NOTE: the service hours flow DB -> GTFS export -> OTP graph. Applying this
-- migration alone changes nothing for routing: you must re-run
-- POST /internal/gtfs/export and rebuild the transit graph afterwards.

UPDATE service_window
   SET start_time = TIME '00:00:00',
       end_time   = TIME '23:59:00';
