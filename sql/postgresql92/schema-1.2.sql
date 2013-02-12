CREATE TABLE log_journal (
    identifier integer NOT NULL,
    action text NOT NULL,
    author text NOT NULL,
    date timestamp without time zone,
    entityid text NOT NULL,
    entitytype text NOT NULL
);

ALTER TABLE ONLY log_journal
    ADD CONSTRAINT pk_log_journal_identifier PRIMARY KEY (identifier);
    
CREATE SEQUENCE log_journal_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE log_journal_identifier_seq OWNED BY log_journal.identifier;