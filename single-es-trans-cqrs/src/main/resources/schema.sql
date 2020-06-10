CREATE SEQUENCE IF NOT EXISTS EVENT_ORDER;

CREATE TABLE IF NOT EXISTS MESSAGE_BOARD_EVENT (
  ID            UUID PRIMARY KEY,
  USERNAME      VARCHAR(10) NOT NULL,
  MESSAGE_BODY  VARCHAR(1000) NOT NULL,
  CREATED_TIMESTAMP TIMESTAMP NOT NULL,
  SEQ           BIGINT
);

CREATE INDEX IF NOT EXISTS MESSAGE_BOARD_EVENT_IX ON MESSAGE_BOARD_EVENT (
  SEQ
);

CREATE TABLE IF NOT EXISTS MESSAGE_BOARD_QUERY (
  ID              UUID PRIMARY KEY,
  USERNAME1       VARCHAR(10) NULL,
  MESSAGE_BODY1   VARCHAR(1000) NULL,
  USERNAME2       VARCHAR(10) NULL,
  MESSAGE_BODY2   VARCHAR(1000) NULL,
  USERNAME3       VARCHAR(10) NULL,
  MESSAGE_BODY3   VARCHAR(1000) NULL,
  USERNAME4       VARCHAR(10) NULL,
  MESSAGE_BODY4   VARCHAR(1000) NULL,
  USERNAME5       VARCHAR(10) NULL,
  MESSAGE_BODY5   VARCHAR(1000) NULL,
  USERNAME6       VARCHAR(10) NULL,
  MESSAGE_BODY6   VARCHAR(1000) NULL,
  USERNAME7       VARCHAR(10) NULL,
  MESSAGE_BODY7   VARCHAR(1000) NULL,
  USERNAME8       VARCHAR(10) NULL,
  MESSAGE_BODY8   VARCHAR(1000) NULL,
  USERNAME9       VARCHAR(10) NULL,
  MESSAGE_BODY9   VARCHAR(1000) NULL,
  USERNAME10      VARCHAR(10) NULL,
  MESSAGE_BODY10  VARCHAR(1000) NULL
);

CREATE ALIAS IF NOT EXISTS QUERY_ID DETERMINISTIC
FOR "ed.carisu.messageboard.saescqrstx.db.MessageBoardQuery.queryId";

INSERT INTO MESSAGE_BOARD_QUERY
VALUES  (QUERY_ID(),
        NULL,NULL, --1
        NULL,NULL, --2
        NULL,NULL, --3
        NULL,NULL, --4
        NULL,NULL, --5
        NULL,NULL, --6
        NULL,NULL, --7
        NULL,NULL, --8
        NULL,NULL, --9
        NULL,NULL --10
);
