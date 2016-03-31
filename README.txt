1) Download Cassandra http://www-eu.apache.org/dist/cassandra/2.2.5/apache-cassandra-2.2.5-bin.tar.gz
2) Extract and start it: {folder}/bin/cassandra
3) Start CQLSH {folder}/bin/cqlsh
4) Execute next CQL code:
--------- CQL CODE ---------------------------
CREATE KEYSPACE atlas WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use atlas;

CREATE TABLE atlas.poi (
    id bigint,
    parameters map<text, text>,
    PRIMARY KEY (id)
);

CREATE TABLE atlas.param_val_poi (
	key text,
	param text,
	value text,
	poi_id bigint,
	PRIMARY KEY (key, param, value, poi_id)
);


CREATE TABLE atlas.param_val (
	key text,
	param text,
	value text,
	cnt counter,
	PRIMARY KEY (key, param, value)
);


CREATE TABLE atlas.param (
	key text,
	param text,
	PRIMARY KEY (key, param)
);
---------- CQL CODE --------------------------

5) CD to folder project and build it: mvn clean package
6) Start application: java -jar target/atlas-0.0.1-SNAPSHOT.jar
7) Make some query with **CORRECT PORT NUMBER** and **PASSWORD** admin \ NoPassword:

#QUERY for some POIs
http://localhost:8091/api/static/find?latitude=37.42242&longitude=-122.08585&building=Elektroniikkatie%208&floor=PARKING

********** NOTE ************************************************************************************
poi_id (Long), latitude (String), longitude (String) - have special meaning and have to be submitted for new POI
********** NOTE ************************************************************************************

#Submit new POI
curl -u admin:NoPassword --data "latitude=37.42242&longitude=-122.08585&building=Elektroniikkatie%208&floor=PARKING&poi_id=1459355500414" http://localhost:8091/api/static/post_poi

