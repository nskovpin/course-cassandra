CREATE TABLE IF NOT EXISTS booking_keyspace.city(city_name text, hotel_name text, PRIMARY KEY (city_name, hotel_name));
CREATE TABLE IF NOT EXISTS booking_keyspace.hotel(hotel_name text, city_name text, rooms set<int>, address text, PRIMARY KEY (hotel_name, city_name));
CREATE TABLE IF NOT EXISTS booking_keyspace.room(room_number int, hotel text, city text, PRIMARY KEY ((room_number, hotel), city));
CREATE TABLE IF NOT EXISTS booking_keyspace.guest(guest_name text, id uuid, PRIMARY KEY (guest_name, id));
CREATE TABLE IF NOT EXISTS booking_keyspace.booking(id uuid, guest_id uuid, comment text, PRIMARY KEY (id));
CREATE TABLE IF NOT EXISTS booking_keyspace.booking_detail(guest_id uuid, day date, start timestamp, end timestamp, room_number int, hotel text, city text, booking_id uuid, PRIMARY KEY((guest_id), day)) WITH CLUSTERING ORDER BY (day DESC);
CREATE TABLE IF NOT EXISTS booking_keyspace.booking_hotel_detail(hotel text, city text, event_date timestamp, begin_end text, room_number int, booking_id uuid, PRIMARY KEY((hotel), city, event_date, begin_end, room_number)) WITH CLUSTERING ORDER BY (city ASC, event_date DESC, begin_end DESC);