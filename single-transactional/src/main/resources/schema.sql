CREATE TABLE IF NOT EXISTS MESSAGE_BOARD (
  ID            UUID PRIMARY KEY,
  USERNAME      VARCHAR(10) NOT NULL,
  MESSAGE_BODY  VARCHAR(1000) NOT NULL,
  CREATED_TIMESTAMP TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS MESSAGE_BOARD_IX ON MESSAGE_BOARD (
  CREATED_TIMESTAMP, ID
)
