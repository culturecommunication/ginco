-- Copyright or © or Copr. Ministère Français chargé de la Culture et de la Communication (2013)
--
-- contact.gincoculture.gouv.fr
-- 
-- This software is a computer program whose purpose is to provide a thesaurus management solution. 
-- This software is governed by the CeCILL license under French law and abiding by the rules of distribution of free software.
-- You can use, modify and/ or redistribute the software under the terms of the CeCILL license as circulated by CEA, CNRS and INRIA at the following URL "http://www.cecill.info".
-- As a counterpart to the access to the source code and rights to copy, modify and redistribute granted by the license, users are provided only with a limited warranty and the software's author, the holder of the economic rights, and the successive licensors have only limited liability.
-- In this respect, the user's attention is drawn to the risks associated with loading, using, modifying and/or developing or reproducing the software by the user in light of its specific status of free software,that may mean that it is complicated to manipulate, and that also therefore means that it is reserved for developers and experienced professionals having in-depth computer knowledge. Users are therefore encouraged to load and test the software's suitability as regards their requirements in conditions enabling the security of their systemsand/or data to be ensured and, more generally, to use and operate it in the same conditions as regards security.
-- The fact that you are presently reading this means that you have hadknowledge of the CeCILL license and that you accept its terms.

--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.2
-- Dumped by pg_dump version 9.2.2

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 178 (class 3079 OID 12595)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2852 (class 0 OID 0)
-- Dependencies: 178
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 168 (class 1259 OID 16467)
-- Name: languages_iso639; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE languages_iso639 (
    id character(3) NOT NULL,
    part2b character(3),
    part2t character(3),
    part1 character(2),
    scope character(1) NOT NULL,
    type character(1) NOT NULL,
    ref_name character varying(150) NOT NULL,
    toplanguage boolean NOT NULL DEFAULT FALSE,
    comment character varying(150)
);


--
-- TOC entry 2853 (class 0 OID 0)
-- Dependencies: 168
-- Name: TABLE languages_iso639; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE languages_iso639 IS 'This table stores all ISO639-3 languages code
(http://www.sil.org/iso639-3/iso-639-3_20130123.tab)';


--
-- TOC entry 169 (class 1259 OID 16470)
-- Name: thesaurus; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus (
    identifier text NOT NULL,
    contributor text,
    coverage text,
    date timestamp without time zone,
    description text,
    format integer,
    publisher text,
    relation text,
    rights text,
    source text,
    subject text,
    title text NOT NULL,
    type integer,
    creator integer,
    created timestamp without time zone DEFAULT now() NOT NULL
);


--
-- TOC entry 2854 (class 0 OID 0)
-- Dependencies: 169
-- Name: TABLE thesaurus; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus IS 'This table stores Thesaurus items.';


--
-- TOC entry 176 (class 1259 OID 16537)
-- Name: thesaurus_organization; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_organization (
    identifier integer NOT NULL,
    name text NOT NULL,
    homepage text
);


--
-- TOC entry 2855 (class 0 OID 0)
-- Dependencies: 176
-- Name: TABLE thesaurus_organization; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_organization IS 'This table stores data related to thesaurus item creators (a creator is an organization, with a name and a link to its homepage).';


--
-- TOC entry 175 (class 1259 OID 16535)
-- Name: thesaurus_creator_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE thesaurus_creator_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2856 (class 0 OID 0)
-- Dependencies: 175
-- Name: thesaurus_creator_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE thesaurus_creator_identifier_seq OWNED BY thesaurus_organization.identifier;


--
-- TOC entry 171 (class 1259 OID 16478)
-- Name: thesaurus_format; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_format (
    identifier integer NOT NULL,
    label text NOT NULL
);


--
-- TOC entry 2857 (class 0 OID 0)
-- Dependencies: 171
-- Name: TABLE thesaurus_format; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_format IS 'This table stores thesaurus file formats or medium (PDF 1.7, CSV, XML/SKOS, etc.).';


--
-- TOC entry 170 (class 1259 OID 16476)
-- Name: thesaurus_format_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE thesaurus_format_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2858 (class 0 OID 0)
-- Dependencies: 170
-- Name: thesaurus_format_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE thesaurus_format_identifier_seq OWNED BY thesaurus_format.identifier;


--
-- TOC entry 172 (class 1259 OID 16485)
-- Name: thesaurus_languages; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_languages (
    iso639_id character varying(3) NOT NULL,
    thesaurus_identifier text NOT NULL
);


--
-- TOC entry 2859 (class 0 OID 0)
-- Dependencies: 172
-- Name: TABLE thesaurus_languages; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_languages IS 'This table is a join table between thesaurus and languages_iso639';


--
-- TOC entry 174 (class 1259 OID 16493)
-- Name: thesaurus_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_type (
    identifier integer NOT NULL,
    label text NOT NULL
);


--
-- TOC entry 2860 (class 0 OID 0)
-- Dependencies: 174
-- Name: TABLE thesaurus_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_type IS 'This table stores the different types of thesaurus (structured lists, thesaurus, taxonomy, etc.).';


--
-- TOC entry 173 (class 1259 OID 16491)
-- Name: thesaurus_type_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE thesaurus_type_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2861 (class 0 OID 0)
-- Dependencies: 173
-- Name: thesaurus_type_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE thesaurus_type_identifier_seq OWNED BY thesaurus_type.identifier;


--
-- TOC entry 177 (class 1259 OID 16552)
-- Name: thesaurus_version_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_version_history (
    identifier text NOT NULL,
    date timestamp without time zone DEFAULT now() NOT NULL,
    versionnote text,
    currentversion boolean,
    thisversion boolean,
    thesaurus_identifier text NOT NULL
);


--
-- TOC entry 2862 (class 0 OID 0)
-- Dependencies: 177
-- Name: TABLE thesaurus_version_history; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_version_history IS 'This table stores versions history for thesaurus';


--
-- TOC entry 2817 (class 2604 OID 16481)
-- Name: identifier; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_format ALTER COLUMN identifier SET DEFAULT nextval('thesaurus_format_identifier_seq'::regclass);


--
-- TOC entry 2819 (class 2604 OID 16540)
-- Name: identifier; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_organization ALTER COLUMN identifier SET DEFAULT nextval('thesaurus_creator_identifier_seq'::regclass);


--
-- TOC entry 2818 (class 2604 OID 16496)
-- Name: identifier; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_type ALTER COLUMN identifier SET DEFAULT nextval('thesaurus_type_identifier_seq'::regclass);


--
-- TOC entry 2822 (class 2606 OID 16501)
-- Name: pk_languages_iso639; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY languages_iso639
    ADD CONSTRAINT pk_languages_iso639 PRIMARY KEY (id);


--
-- TOC entry 2829 (class 2606 OID 16503)
-- Name: pk_thesaurus_format_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_format
    ADD CONSTRAINT pk_thesaurus_format_identifier PRIMARY KEY (identifier);


--
-- TOC entry 2827 (class 2606 OID 16505)
-- Name: pk_thesaurus_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus
    ADD CONSTRAINT pk_thesaurus_identifier PRIMARY KEY (identifier);


--
-- TOC entry 2832 (class 2606 OID 16507)
-- Name: pk_thesaurus_languages; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages
    ADD CONSTRAINT pk_thesaurus_languages PRIMARY KEY (iso639_id, thesaurus_identifier);


--
-- TOC entry 2836 (class 2606 OID 16545)
-- Name: pk_thesaurus_organization; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_organization
    ADD CONSTRAINT pk_thesaurus_organization PRIMARY KEY (identifier);


--
-- TOC entry 2834 (class 2606 OID 16509)
-- Name: pk_thesaurus_type_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_type
    ADD CONSTRAINT pk_thesaurus_type_identifier PRIMARY KEY (identifier);


--
-- TOC entry 2839 (class 2606 OID 16559)
-- Name: pk_thesaurus_version_history; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_version_history
    ADD CONSTRAINT pk_thesaurus_version_history PRIMARY KEY (identifier);


--
-- TOC entry 2823 (class 1259 OID 16510)
-- Name: fki_thesaurus_format; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_format ON thesaurus USING btree (format);


--
-- TOC entry 2830 (class 1259 OID 16511)
-- Name: fki_thesaurus_language_identifier; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_language_identifier ON thesaurus_languages USING btree (thesaurus_identifier);


--
-- TOC entry 2837 (class 1259 OID 16565)
-- Name: fki_thesaurus_organization_thesaurus; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_organization_thesaurus ON thesaurus_version_history USING btree (thesaurus_identifier);


--
-- TOC entry 2824 (class 1259 OID 16551)
-- Name: fki_thesaurus_thesaurus_organization; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_thesaurus_organization ON thesaurus USING btree (creator);


--
-- TOC entry 2825 (class 1259 OID 16512)
-- Name: fki_thesaurus_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_type ON thesaurus USING btree (type);


--
-- TOC entry 2840 (class 2606 OID 16572)
-- Name: fk_thesaurus_format; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus
    ADD CONSTRAINT fk_thesaurus_format FOREIGN KEY (format) REFERENCES thesaurus_format(identifier);


--
-- TOC entry 2843 (class 2606 OID 16587)
-- Name: fk_thesaurus_languages_languages_iso639_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages
    ADD CONSTRAINT fk_thesaurus_languages_languages_iso639_id FOREIGN KEY (iso639_id) REFERENCES languages_iso639(id);


--
-- TOC entry 2844 (class 2606 OID 16592)
-- Name: fk_thesaurus_languages_thesaurus_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages
    ADD CONSTRAINT fk_thesaurus_languages_thesaurus_identifier FOREIGN KEY (thesaurus_identifier) REFERENCES thesaurus(identifier);


--
-- TOC entry 2845 (class 2606 OID 16567)
-- Name: fk_thesaurus_organization_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_version_history
    ADD CONSTRAINT fk_thesaurus_organization_thesaurus FOREIGN KEY (thesaurus_identifier) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 2841 (class 2606 OID 16577)
-- Name: fk_thesaurus_thesaurus_organization; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus
    ADD CONSTRAINT fk_thesaurus_thesaurus_organization FOREIGN KEY (creator) REFERENCES thesaurus_organization(identifier);


--
-- TOC entry 2842 (class 2606 OID 16582)
-- Name: fk_thesaurus_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus
    ADD CONSTRAINT fk_thesaurus_type FOREIGN KEY (type) REFERENCES thesaurus_type(identifier);


--
-- PostgreSQL database dump complete
--

