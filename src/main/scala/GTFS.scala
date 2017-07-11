import scalikejdbc._

/**
  * Převádí JDF na GTFS
  *
  * @param path cesta do které se má rozbalit
  */
class GTFS(path: String)(implicit session: DBSession = AutoSession) {
  val tables = List(
  """agency(
     agency_id INT,
     agency_name VARCHAR(254),
     agency_url VARCHAR(48),
     agency_timezone VARCHAR(13),
     agency_lang VARCHAR(2),
     agency_phone VARCHAR(48),
     agency_email VARCHAR(48)
  )""",
  """calendar (
    id INT,
    transit_system VARCHAR(254),
    service_id VARCHAR(254),
    monday INT,
    tuesday INT,
    wednesday INT,
    thursday INT,
    friday INT,
    saturday INT,
    sunday INT,
    start_date VARCHAR(254),
    end_date VARCHAR(254)
  )""",
  """fare_attributes (
    id INT,
    transit_system VARCHAR(254),
    fare_id VARCHAR(254),
    price VARCHAR(254),
    currency_type VARCHAR(254),
    payment_method INT,
    transfers INT,
    transfer_duration VARCHAR(254),
    exception_type INT,
    agency_id INT
  )""",
  """fare_rules (
    id INT,
    transit_system VARCHAR(254),
    fare_id VARCHAR(254),
    route_id VARCHAR(254),
    origin_id VARCHAR(254),
    destination_id VARCHAR(254),
    contains_id VARCHAR(254)
  )""",
  """feed_info (
    id INT,
    transit_system VARCHAR(254),
    feed_publisher_name VARCHAR(254),
    feed_publisher_url VARCHAR(254),
    feed_lang VARCHAR(254),
    feed_start_date VARCHAR(254),
    feed_end_date VARCHAR(254),
    feed_version VARCHAR(254)
  )""",
  """frequencies (
    id INT,
    transit_system VARCHAR(254),
    trip_id VARCHAR(254),
    start_time VARCHAR(254),
    end_time VARCHAR(254),
    headway_secs VARCHAR(254),
    exact_times INT
  )""",
  """routes (
    id INT,
    transit_system VARCHAR(254),
    route_id VARCHAR(254),
    agency_id VARCHAR(254),
    route_short_name VARCHAR(254),
    route_long_name VARCHAR(254),
    route_type VARCHAR(254),
    route_text_color VARCHAR(254),
    route_color VARCHAR(254),
    route_url VARCHAR(254),
    route_desc VARCHAR(254)
  )""",
  """shapes (
    id INT,
    transit_system VARCHAR(254),
    shape_id VARCHAR(254),
    shape_pt_lat INT,
    shape_pt_lon INT,
    shape_pt_sequence INT,
    shape_dist_traveled VARCHAR(254)
  )""",
  """stop_times (
    id INT,
    transit_system VARCHAR(254),
    trip_id VARCHAR(254),
    arrival_time VARCHAR(254),
    arrival_time_seconds INT,
    departure_time VARCHAR(254),
    departure_time_seconds INT,
    stop_id VARCHAR(254),
    stop_sequence VARCHAR(254),
    stop_headsign VARCHAR(254),
    pickup_type VARCHAR(254),
    drop_off_type VARCHAR(254),
    shape_dist_traveled VARCHAR(254)
  )""",
  """stops (
    id INT AUTO_INCREMENT,
    transit_system VARCHAR(254),
    stop_id VARCHAR(254),
    stop_code VARCHAR(254),
    stop_name VARCHAR(254),
    stop_desc VARCHAR(254),
    stop_lat INT,
    stop_lon INT,
    zone_id VARCHAR(254),
    stop_url VARCHAR(254),
    location_type VARCHAR(254),
    parent_station VARCHAR(254),
    stop_timezone VARCHAR(254)
  )""",
  """transfers (
    id INT,
    transit_system VARCHAR(254),
    from_stop_id INT,
    to_stop_id VARCHAR(254),
    transfer_type INT,
    min_transfer_time VARCHAR(254)
  )""",
  """trips (
    id INT NOT NULL AUTO_INCREMENT,
    transit_system VARCHAR(254),
    route_id VARCHAR(254),
    service_id VARCHAR(254),
    trip_headsign VARCHAR(254),
    trip_short_name VARCHAR(254),
    direction_id INT,
    block_id VARCHAR(254),
    shape_id VARCHAR(254),
    wheelchair_accessible INT,
    bikes_allowed INT
  )""")

  tables.foreach(table => SQL(s"CREATE TABLE IF NOT EXISTS $table").execute().apply())
  SQL("INSERT INTO agency(agency_id, agency_name, agency_timezone, agency_lang, agency_phone, agency_url, agency_email) DIRECT SELECT IC, ObchodniJmeno, 'Europe/Prague', 'cs', TelefonSidla, WWW, EMail FROM Dopravci").execute().apply()
  SQL(s"call CSVWRITE('${path}/agency.csv', 'SELECT * FROM agency')").execute().apply()
  SQL("INSERT INTO stops(stop_name, stop_lon, stop_lat) DIRECT SELECT CONCAT(CONCAT(NazevObce, CastObce), BlizsiMisto), 0, 0 FROM Zastavky").execute().apply()
  SQL(s"call CSVWRITE('${path}/stops.csv', 'SELECT * FROM stops')").execute().apply()
  SQL("INSERT INTO routes(route_id, route_short_name, route_type) DIRECT SELECT CisloLinky, NazevLinky, 3 FROM Linky").execute().apply()
  SQL(s"call CSVWRITE('${path}/routes.csv', 'SELECT * FROM routes')").execute().apply()
  SQL("INSERT INTO trips(route_id, service_id) DIRECT SELECT Zasspoje.CisloLinky, NazevLinky FROM Zasspoje JOIN Linky ON Zasspoje.CisloLinky = Linky.CisloLinky").execute().apply()
  SQL(s"call CSVWRITE('${path}/trips.csv', 'SELECT * FROM trips')").execute().apply()
  SQL("INSERT INTO stop_times(stop_id) DIRECT SELECT stop_id FROM stops").execute().apply()
  SQL(s"call CSVWRITE('${path}/trips.csv', 'SELECT * FROM stop_times')").execute().apply()
  sql"DROP TABLE routes".execute().apply()
  sql"DROP TABLE stops".execute().apply()
  sql"DROP TABLE agency".execute().apply()
  sql"DROP TABLE trips".execute().apply()
}
