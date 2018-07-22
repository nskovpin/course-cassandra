CREATE TABLE IF NOT EXISTS booking_keyspace.city(city_name text, hotel_name text, PRIMARY KEY (city_name, hotel_name));
CREATE TABLE IF NOT EXISTS booking_keyspace.hotel(hotel_name text, city_name text, rooms set<int>, address text, PRIMARY KEY (hotel_name, city_name));
CREATE TABLE IF NOT EXISTS booking_keyspace.room(room_number int, hotel text, city text, PRIMARY KEY ((room_number, hotel), city));
CREATE TABLE IF NOT EXISTS booking_keyspace.guest(guest_name text, id uuid, PRIMARY KEY (guest_name, id));
CREATE TABLE IF NOT EXISTS booking_keyspace.booking(id timeuuid, guest_id uuid, comment text, PRIMARY KEY (id));
CREATE TABLE IF NOT EXISTS booking_keyspace.booking_detail(guest_id uuid, day date, start timestamp, end timestamp, room_number int, hotel text, city text, booking_id timeuuid, PRIMARY KEY((guest_id), day)) WITH CLUSTERING ORDER BY (day DESC);
CREATE TABLE IF NOT EXISTS booking_keyspace.booking_hotel_detail(hotel text, city text, event_date timestamp, begin_end text, room_number int, booking_id timeuuid, PRIMARY KEY((hotel), city, event_date, begin_end, room_number)) WITH CLUSTERING ORDER BY (city ASC, event_date DESC, begin_end DESC);


//test
CREATE TABLE IF NOT EXISTS booking_keyspace.test2(guest_id uuid, event_date1 timestamp, event_date2 timestamp, begin_end text, PRIMARY KEY((guest_id), event_date1, event_date2, begin_end)) WITH CLUSTERING ORDER BY (event_date1 DESC, event_date2 DESC);
insert into booking_keyspace.test2(guest_id, event_date1, event_date2, begin_end) values (e7c26e68-a8c5-4273-9227-412474ac5496, '2018-04-22 05:25:01', '2018-04-28 05:25:01', 'B');
insert into booking_keyspace.test2(guest_id, event_date1, event_date2, begin_end) values (e7c26e68-a8c5-4273-9227-412474ac5496, '2018-04-28 05:25:01', '2018-04-28 05:25:01', 'E');
select * from booking_keyspace.test2;
insert into booking_keyspace.test2(guest_id, event_date1, event_date2, begin_end) values (e7c26e68-a8c5-4273-9227-412474ac5496, '2018-04-13 05:25:01', '2018-04-16 05:25:01', 'B');
insert into booking_keyspace.test2(guest_id, event_date1, event_date2, begin_end) values (e7c26e68-a8c5-4273-9227-412474ac5496, '2018-04-16 05:25:01', '2018-04-16 05:25:01', 'E');


CREATE TABLE IF NOT EXISTS booking_keyspace.test3(guest_id uuid, days list<text>, start timestamp, end timestamp, begin_end text, PRIMARY KEY((guest_id)));
insert into booking_keyspace.test3(guest_id, days, start, end, begin_end) values (e7c26e68-a8c5-4273-9227-412474ac5496, ['2018-04-22','2018-04-23','2018-04-24'], '2018-04-22 05:25:01', '2018-04-28 05:25:01', 'B');


CREATE TABLE IF NOT EXISTS booking_keyspace.booking_detail(guest_id uuid, day text, start timestamp, end timestamp, begin_end text, PRIMARY KEY((guest_id), day));
insert into booking_keyspace.booking_detail(guest_id, day, start, end, room_number, hotel, city, booking_id) values (e7c26e68-a8c5-4273-9227-412474ac5496, '2018-04-22', '2018-04-22 05:25:01', '2018-04-28 05:25:01', 22, 'rest', 'vrn', now());
