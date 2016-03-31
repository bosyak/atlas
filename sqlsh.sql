CREATE KEYSPACE atlas WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
use atlas;

truncate param_val_poi;
truncate param_val;
truncate param;

DROP TABLE atlas.param_val_poi;
DROP TABLE atlas.param_val;
DROP TABLE atlas.param;

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

-- UPDATE atlas.param_val SET cnt = cnt + 1 WHERE key = 'key1' and param = 'param1' and value = 'val1';
-- SELECT * from param_val;

CREATE TABLE atlas.param ( 
	key text,
	param text,	 
	PRIMARY KEY (key, param)
);