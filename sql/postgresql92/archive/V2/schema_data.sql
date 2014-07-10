-- schema.sql ---------------------------- 
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

-- data.sql ---------------------------- 
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

SET search_path = public, pg_catalog;

--
-- TOC entry 2811 (class 0 OID 16467)
-- Dependencies: 168
-- Data for Name: languages_iso639; Type: TABLE DATA; Schema: public; Owner: hadocdb
--

INSERT INTO languages_iso639 VALUES ('aaa', '   ', '   ', '  ', 'I', 'L', 'Ghotuo', false, '');
INSERT INTO languages_iso639 VALUES ('aab', '   ', '   ', '  ', 'I', 'L', 'Alumu-Tesu', false, '');
INSERT INTO languages_iso639 VALUES ('aac', '   ', '   ', '  ', 'I', 'L', 'Ari', false, '');
INSERT INTO languages_iso639 VALUES ('aad', '   ', '   ', '  ', 'I', 'L', 'Amal', false, '');
INSERT INTO languages_iso639 VALUES ('aae', '   ', '   ', '  ', 'I', 'L', 'Arbëreshë Albanian', false, '');
INSERT INTO languages_iso639 VALUES ('aaf', '   ', '   ', '  ', 'I', 'L', 'Aranadan', false, '');
INSERT INTO languages_iso639 VALUES ('aag', '   ', '   ', '  ', 'I', 'L', 'Ambrak', false, '');
INSERT INTO languages_iso639 VALUES ('aah', '   ', '   ', '  ', 'I', 'L', 'Abu'' Arapesh', false, '');
INSERT INTO languages_iso639 VALUES ('aai', '   ', '   ', '  ', 'I', 'L', 'Arifama-Miniafia', false, '');
INSERT INTO languages_iso639 VALUES ('aak', '   ', '   ', '  ', 'I', 'L', 'Ankave', false, '');
INSERT INTO languages_iso639 VALUES ('aal', '   ', '   ', '  ', 'I', 'L', 'Afade', false, '');
INSERT INTO languages_iso639 VALUES ('aam', '   ', '   ', '  ', 'I', 'L', 'Aramanik', false, '');
INSERT INTO languages_iso639 VALUES ('aan', '   ', '   ', '  ', 'I', 'L', 'Anambé', false, '');
INSERT INTO languages_iso639 VALUES ('aao', '   ', '   ', '  ', 'I', 'L', 'Algerian Saharan Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('aap', '   ', '   ', '  ', 'I', 'L', 'Pará Arára', false, '');
INSERT INTO languages_iso639 VALUES ('aaq', '   ', '   ', '  ', 'I', 'E', 'Eastern Abnaki', false, '');
INSERT INTO languages_iso639 VALUES ('aar', 'aar', 'aar', 'aa', 'I', 'L', 'Afar', false, '');
INSERT INTO languages_iso639 VALUES ('aas', '   ', '   ', '  ', 'I', 'L', 'Aasáx', false, '');
INSERT INTO languages_iso639 VALUES ('aat', '   ', '   ', '  ', 'I', 'L', 'Arvanitika Albanian', false, '');
INSERT INTO languages_iso639 VALUES ('aau', '   ', '   ', '  ', 'I', 'L', 'Abau', false, '');
INSERT INTO languages_iso639 VALUES ('aaw', '   ', '   ', '  ', 'I', 'L', 'Solong', false, '');
INSERT INTO languages_iso639 VALUES ('aax', '   ', '   ', '  ', 'I', 'L', 'Mandobo Atas', false, '');
INSERT INTO languages_iso639 VALUES ('aaz', '   ', '   ', '  ', 'I', 'L', 'Amarasi', false, '');
INSERT INTO languages_iso639 VALUES ('aba', '   ', '   ', '  ', 'I', 'L', 'Abé', false, '');
INSERT INTO languages_iso639 VALUES ('abb', '   ', '   ', '  ', 'I', 'L', 'Bankon', false, '');
INSERT INTO languages_iso639 VALUES ('abc', '   ', '   ', '  ', 'I', 'L', 'Ambala Ayta', false, '');
INSERT INTO languages_iso639 VALUES ('abd', '   ', '   ', '  ', 'I', 'L', 'Manide', false, '');
INSERT INTO languages_iso639 VALUES ('abe', '   ', '   ', '  ', 'I', 'E', 'Western Abnaki', false, '');
INSERT INTO languages_iso639 VALUES ('abf', '   ', '   ', '  ', 'I', 'L', 'Abai Sungai', false, '');
INSERT INTO languages_iso639 VALUES ('abg', '   ', '   ', '  ', 'I', 'L', 'Abaga', false, '');
INSERT INTO languages_iso639 VALUES ('abh', '   ', '   ', '  ', 'I', 'L', 'Tajiki Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('abi', '   ', '   ', '  ', 'I', 'L', 'Abidji', false, '');
INSERT INTO languages_iso639 VALUES ('abj', '   ', '   ', '  ', 'I', 'E', 'Aka-Bea', false, '');
INSERT INTO languages_iso639 VALUES ('abk', 'abk', 'abk', 'ab', 'I', 'L', 'Abkhazian', false, '');
INSERT INTO languages_iso639 VALUES ('abl', '   ', '   ', '  ', 'I', 'L', 'Lampung Nyo', false, '');
INSERT INTO languages_iso639 VALUES ('abm', '   ', '   ', '  ', 'I', 'L', 'Abanyom', false, '');
INSERT INTO languages_iso639 VALUES ('abn', '   ', '   ', '  ', 'I', 'L', 'Abua', false, '');
INSERT INTO languages_iso639 VALUES ('abo', '   ', '   ', '  ', 'I', 'L', 'Abon', false, '');
INSERT INTO languages_iso639 VALUES ('abp', '   ', '   ', '  ', 'I', 'L', 'Abellen Ayta', false, '');
INSERT INTO languages_iso639 VALUES ('abq', '   ', '   ', '  ', 'I', 'L', 'Abaza', false, '');
INSERT INTO languages_iso639 VALUES ('abr', '   ', '   ', '  ', 'I', 'L', 'Abron', false, '');
INSERT INTO languages_iso639 VALUES ('abs', '   ', '   ', '  ', 'I', 'L', 'Ambonese Malay', false, '');
INSERT INTO languages_iso639 VALUES ('abt', '   ', '   ', '  ', 'I', 'L', 'Ambulas', false, '');
INSERT INTO languages_iso639 VALUES ('abu', '   ', '   ', '  ', 'I', 'L', 'Abure', false, '');
INSERT INTO languages_iso639 VALUES ('abv', '   ', '   ', '  ', 'I', 'L', 'Baharna Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('abw', '   ', '   ', '  ', 'I', 'L', 'Pal', false, '');
INSERT INTO languages_iso639 VALUES ('abx', '   ', '   ', '  ', 'I', 'L', 'Inabaknon', false, '');
INSERT INTO languages_iso639 VALUES ('aby', '   ', '   ', '  ', 'I', 'L', 'Aneme Wake', false, '');
INSERT INTO languages_iso639 VALUES ('abz', '   ', '   ', '  ', 'I', 'L', 'Abui', false, '');
INSERT INTO languages_iso639 VALUES ('aca', '   ', '   ', '  ', 'I', 'L', 'Achagua', false, '');
INSERT INTO languages_iso639 VALUES ('acb', '   ', '   ', '  ', 'I', 'L', 'Áncá', false, '');
INSERT INTO languages_iso639 VALUES ('acd', '   ', '   ', '  ', 'I', 'L', 'Gikyode', false, '');
INSERT INTO languages_iso639 VALUES ('ace', 'ace', 'ace', '  ', 'I', 'L', 'Achinese', false, '');
INSERT INTO languages_iso639 VALUES ('acf', '   ', '   ', '  ', 'I', 'L', 'Saint Lucian Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('ach', 'ach', 'ach', '  ', 'I', 'L', 'Acoli', false, '');
INSERT INTO languages_iso639 VALUES ('aci', '   ', '   ', '  ', 'I', 'E', 'Aka-Cari', false, '');
INSERT INTO languages_iso639 VALUES ('ack', '   ', '   ', '  ', 'I', 'E', 'Aka-Kora', false, '');
INSERT INTO languages_iso639 VALUES ('acl', '   ', '   ', '  ', 'I', 'E', 'Akar-Bale', false, '');
INSERT INTO languages_iso639 VALUES ('acm', '   ', '   ', '  ', 'I', 'L', 'Mesopotamian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('acn', '   ', '   ', '  ', 'I', 'L', 'Achang', false, '');
INSERT INTO languages_iso639 VALUES ('acp', '   ', '   ', '  ', 'I', 'L', 'Eastern Acipa', false, '');
INSERT INTO languages_iso639 VALUES ('acq', '   ', '   ', '  ', 'I', 'L', 'Ta''izzi-Adeni Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('acr', '   ', '   ', '  ', 'I', 'L', 'Achi', false, '');
INSERT INTO languages_iso639 VALUES ('acs', '   ', '   ', '  ', 'I', 'E', 'Acroá', false, '');
INSERT INTO languages_iso639 VALUES ('act', '   ', '   ', '  ', 'I', 'L', 'Achterhoeks', false, '');
INSERT INTO languages_iso639 VALUES ('acu', '   ', '   ', '  ', 'I', 'L', 'Achuar-Shiwiar', false, '');
INSERT INTO languages_iso639 VALUES ('acv', '   ', '   ', '  ', 'I', 'L', 'Achumawi', false, '');
INSERT INTO languages_iso639 VALUES ('acw', '   ', '   ', '  ', 'I', 'L', 'Hijazi Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('acx', '   ', '   ', '  ', 'I', 'L', 'Omani Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('acy', '   ', '   ', '  ', 'I', 'L', 'Cypriot Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('acz', '   ', '   ', '  ', 'I', 'L', 'Acheron', false, '');
INSERT INTO languages_iso639 VALUES ('ada', 'ada', 'ada', '  ', 'I', 'L', 'Adangme', false, '');
INSERT INTO languages_iso639 VALUES ('adb', '   ', '   ', '  ', 'I', 'L', 'Adabe', false, '');
INSERT INTO languages_iso639 VALUES ('add', '   ', '   ', '  ', 'I', 'L', 'Dzodinka', false, '');
INSERT INTO languages_iso639 VALUES ('ade', '   ', '   ', '  ', 'I', 'L', 'Adele', false, '');
INSERT INTO languages_iso639 VALUES ('adf', '   ', '   ', '  ', 'I', 'L', 'Dhofari Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('adg', '   ', '   ', '  ', 'I', 'L', 'Andegerebinha', false, '');
INSERT INTO languages_iso639 VALUES ('adh', '   ', '   ', '  ', 'I', 'L', 'Adhola', false, '');
INSERT INTO languages_iso639 VALUES ('adi', '   ', '   ', '  ', 'I', 'L', 'Adi', false, '');
INSERT INTO languages_iso639 VALUES ('adj', '   ', '   ', '  ', 'I', 'L', 'Adioukrou', false, '');
INSERT INTO languages_iso639 VALUES ('adl', '   ', '   ', '  ', 'I', 'L', 'Galo', false, '');
INSERT INTO languages_iso639 VALUES ('adn', '   ', '   ', '  ', 'I', 'L', 'Adang', false, '');
INSERT INTO languages_iso639 VALUES ('ado', '   ', '   ', '  ', 'I', 'L', 'Abu', false, '');
INSERT INTO languages_iso639 VALUES ('adp', '   ', '   ', '  ', 'I', 'L', 'Adap', false, '');
INSERT INTO languages_iso639 VALUES ('adq', '   ', '   ', '  ', 'I', 'L', 'Adangbe', false, '');
INSERT INTO languages_iso639 VALUES ('adr', '   ', '   ', '  ', 'I', 'L', 'Adonara', false, '');
INSERT INTO languages_iso639 VALUES ('ads', '   ', '   ', '  ', 'I', 'L', 'Adamorobe Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('adt', '   ', '   ', '  ', 'I', 'L', 'Adnyamathanha', false, '');
INSERT INTO languages_iso639 VALUES ('adu', '   ', '   ', '  ', 'I', 'L', 'Aduge', false, '');
INSERT INTO languages_iso639 VALUES ('adw', '   ', '   ', '  ', 'I', 'L', 'Amundava', false, '');
INSERT INTO languages_iso639 VALUES ('adx', '   ', '   ', '  ', 'I', 'L', 'Amdo Tibetan', false, '');
INSERT INTO languages_iso639 VALUES ('ady', 'ady', 'ady', '  ', 'I', 'L', 'Adyghe', false, '');
INSERT INTO languages_iso639 VALUES ('adz', '   ', '   ', '  ', 'I', 'L', 'Adzera', false, '');
INSERT INTO languages_iso639 VALUES ('aea', '   ', '   ', '  ', 'I', 'E', 'Areba', false, '');
INSERT INTO languages_iso639 VALUES ('aeb', '   ', '   ', '  ', 'I', 'L', 'Tunisian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('aec', '   ', '   ', '  ', 'I', 'L', 'Saidi Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('aed', '   ', '   ', '  ', 'I', 'L', 'Argentine Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('aee', '   ', '   ', '  ', 'I', 'L', 'Northeast Pashayi', false, '');
INSERT INTO languages_iso639 VALUES ('aek', '   ', '   ', '  ', 'I', 'L', 'Haeke', false, '');
INSERT INTO languages_iso639 VALUES ('ael', '   ', '   ', '  ', 'I', 'L', 'Ambele', false, '');
INSERT INTO languages_iso639 VALUES ('aem', '   ', '   ', '  ', 'I', 'L', 'Arem', false, '');
INSERT INTO languages_iso639 VALUES ('aen', '   ', '   ', '  ', 'I', 'L', 'Armenian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('aeq', '   ', '   ', '  ', 'I', 'L', 'Aer', false, '');
INSERT INTO languages_iso639 VALUES ('aer', '   ', '   ', '  ', 'I', 'L', 'Eastern Arrernte', false, '');
INSERT INTO languages_iso639 VALUES ('aes', '   ', '   ', '  ', 'I', 'E', 'Alsea', false, '');
INSERT INTO languages_iso639 VALUES ('aeu', '   ', '   ', '  ', 'I', 'L', 'Akeu', false, '');
INSERT INTO languages_iso639 VALUES ('aew', '   ', '   ', '  ', 'I', 'L', 'Ambakich', false, '');
INSERT INTO languages_iso639 VALUES ('aey', '   ', '   ', '  ', 'I', 'L', 'Amele', false, '');
INSERT INTO languages_iso639 VALUES ('aez', '   ', '   ', '  ', 'I', 'L', 'Aeka', false, '');
INSERT INTO languages_iso639 VALUES ('afb', '   ', '   ', '  ', 'I', 'L', 'Gulf Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('afd', '   ', '   ', '  ', 'I', 'L', 'Andai', false, '');
INSERT INTO languages_iso639 VALUES ('afe', '   ', '   ', '  ', 'I', 'L', 'Putukwam', false, '');
INSERT INTO languages_iso639 VALUES ('afg', '   ', '   ', '  ', 'I', 'L', 'Afghan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('afh', 'afh', 'afh', '  ', 'I', 'C', 'Afrihili', false, '');
INSERT INTO languages_iso639 VALUES ('afi', '   ', '   ', '  ', 'I', 'L', 'Akrukay', false, '');
INSERT INTO languages_iso639 VALUES ('afk', '   ', '   ', '  ', 'I', 'L', 'Nanubae', false, '');
INSERT INTO languages_iso639 VALUES ('afn', '   ', '   ', '  ', 'I', 'L', 'Defaka', false, '');
INSERT INTO languages_iso639 VALUES ('afo', '   ', '   ', '  ', 'I', 'L', 'Eloyi', false, '');
INSERT INTO languages_iso639 VALUES ('afp', '   ', '   ', '  ', 'I', 'L', 'Tapei', false, '');
INSERT INTO languages_iso639 VALUES ('afr', 'afr', 'afr', 'af', 'I', 'L', 'Afrikaans', false, '');
INSERT INTO languages_iso639 VALUES ('afs', '   ', '   ', '  ', 'I', 'L', 'Afro-Seminole Creole', false, '');
INSERT INTO languages_iso639 VALUES ('aft', '   ', '   ', '  ', 'I', 'L', 'Afitti', false, '');
INSERT INTO languages_iso639 VALUES ('afu', '   ', '   ', '  ', 'I', 'L', 'Awutu', false, '');
INSERT INTO languages_iso639 VALUES ('afz', '   ', '   ', '  ', 'I', 'L', 'Obokuitai', false, '');
INSERT INTO languages_iso639 VALUES ('aga', '   ', '   ', '  ', 'I', 'E', 'Aguano', false, '');
INSERT INTO languages_iso639 VALUES ('agb', '   ', '   ', '  ', 'I', 'L', 'Legbo', false, '');
INSERT INTO languages_iso639 VALUES ('agc', '   ', '   ', '  ', 'I', 'L', 'Agatu', false, '');
INSERT INTO languages_iso639 VALUES ('agd', '   ', '   ', '  ', 'I', 'L', 'Agarabi', false, '');
INSERT INTO languages_iso639 VALUES ('age', '   ', '   ', '  ', 'I', 'L', 'Angal', false, '');
INSERT INTO languages_iso639 VALUES ('agf', '   ', '   ', '  ', 'I', 'L', 'Arguni', false, '');
INSERT INTO languages_iso639 VALUES ('agg', '   ', '   ', '  ', 'I', 'L', 'Angor', false, '');
INSERT INTO languages_iso639 VALUES ('agh', '   ', '   ', '  ', 'I', 'L', 'Ngelima', false, '');
INSERT INTO languages_iso639 VALUES ('agi', '   ', '   ', '  ', 'I', 'L', 'Agariya', false, '');
INSERT INTO languages_iso639 VALUES ('agj', '   ', '   ', '  ', 'I', 'L', 'Argobba', false, '');
INSERT INTO languages_iso639 VALUES ('agk', '   ', '   ', '  ', 'I', 'L', 'Isarog Agta', false, '');
INSERT INTO languages_iso639 VALUES ('agl', '   ', '   ', '  ', 'I', 'L', 'Fembe', false, '');
INSERT INTO languages_iso639 VALUES ('agm', '   ', '   ', '  ', 'I', 'L', 'Angaataha', false, '');
INSERT INTO languages_iso639 VALUES ('agn', '   ', '   ', '  ', 'I', 'L', 'Agutaynen', false, '');
INSERT INTO languages_iso639 VALUES ('ago', '   ', '   ', '  ', 'I', 'L', 'Tainae', false, '');
INSERT INTO languages_iso639 VALUES ('agq', '   ', '   ', '  ', 'I', 'L', 'Aghem', false, '');
INSERT INTO languages_iso639 VALUES ('agr', '   ', '   ', '  ', 'I', 'L', 'Aguaruna', false, '');
INSERT INTO languages_iso639 VALUES ('ags', '   ', '   ', '  ', 'I', 'L', 'Esimbi', false, '');
INSERT INTO languages_iso639 VALUES ('agt', '   ', '   ', '  ', 'I', 'L', 'Central Cagayan Agta', false, '');
INSERT INTO languages_iso639 VALUES ('agu', '   ', '   ', '  ', 'I', 'L', 'Aguacateco', false, '');
INSERT INTO languages_iso639 VALUES ('agv', '   ', '   ', '  ', 'I', 'L', 'Remontado Dumagat', false, '');
INSERT INTO languages_iso639 VALUES ('agw', '   ', '   ', '  ', 'I', 'L', 'Kahua', false, '');
INSERT INTO languages_iso639 VALUES ('agx', '   ', '   ', '  ', 'I', 'L', 'Aghul', false, '');
INSERT INTO languages_iso639 VALUES ('agy', '   ', '   ', '  ', 'I', 'L', 'Southern Alta', false, '');
INSERT INTO languages_iso639 VALUES ('agz', '   ', '   ', '  ', 'I', 'L', 'Mt. Iriga Agta', false, '');
INSERT INTO languages_iso639 VALUES ('aha', '   ', '   ', '  ', 'I', 'L', 'Ahanta', false, '');
INSERT INTO languages_iso639 VALUES ('ahb', '   ', '   ', '  ', 'I', 'L', 'Axamb', false, '');
INSERT INTO languages_iso639 VALUES ('ahg', '   ', '   ', '  ', 'I', 'L', 'Qimant', false, '');
INSERT INTO languages_iso639 VALUES ('ahh', '   ', '   ', '  ', 'I', 'L', 'Aghu', false, '');
INSERT INTO languages_iso639 VALUES ('ahi', '   ', '   ', '  ', 'I', 'L', 'Tiagbamrin Aizi', false, '');
INSERT INTO languages_iso639 VALUES ('ahk', '   ', '   ', '  ', 'I', 'L', 'Akha', false, '');
INSERT INTO languages_iso639 VALUES ('ahl', '   ', '   ', '  ', 'I', 'L', 'Igo', false, '');
INSERT INTO languages_iso639 VALUES ('ahm', '   ', '   ', '  ', 'I', 'L', 'Mobumrin Aizi', false, '');
INSERT INTO languages_iso639 VALUES ('ahn', '   ', '   ', '  ', 'I', 'L', 'Àhàn', false, '');
INSERT INTO languages_iso639 VALUES ('aho', '   ', '   ', '  ', 'I', 'E', 'Ahom', false, '');
INSERT INTO languages_iso639 VALUES ('ahp', '   ', '   ', '  ', 'I', 'L', 'Aproumu Aizi', false, '');
INSERT INTO languages_iso639 VALUES ('ahr', '   ', '   ', '  ', 'I', 'L', 'Ahirani', false, '');
INSERT INTO languages_iso639 VALUES ('ahs', '   ', '   ', '  ', 'I', 'L', 'Ashe', false, '');
INSERT INTO languages_iso639 VALUES ('aht', '   ', '   ', '  ', 'I', 'L', 'Ahtena', false, '');
INSERT INTO languages_iso639 VALUES ('aia', '   ', '   ', '  ', 'I', 'L', 'Arosi', false, '');
INSERT INTO languages_iso639 VALUES ('aib', '   ', '   ', '  ', 'I', 'L', 'Ainu (China)', false, '');
INSERT INTO languages_iso639 VALUES ('aic', '   ', '   ', '  ', 'I', 'L', 'Ainbai', false, '');
INSERT INTO languages_iso639 VALUES ('aid', '   ', '   ', '  ', 'I', 'E', 'Alngith', false, '');
INSERT INTO languages_iso639 VALUES ('aie', '   ', '   ', '  ', 'I', 'L', 'Amara', false, '');
INSERT INTO languages_iso639 VALUES ('aif', '   ', '   ', '  ', 'I', 'L', 'Agi', false, '');
INSERT INTO languages_iso639 VALUES ('aig', '   ', '   ', '  ', 'I', 'L', 'Antigua and Barbuda Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('aih', '   ', '   ', '  ', 'I', 'L', 'Ai-Cham', false, '');
INSERT INTO languages_iso639 VALUES ('aii', '   ', '   ', '  ', 'I', 'L', 'Assyrian Neo-Aramaic', false, '');
INSERT INTO languages_iso639 VALUES ('aij', '   ', '   ', '  ', 'I', 'L', 'Lishanid Noshan', false, '');
INSERT INTO languages_iso639 VALUES ('aik', '   ', '   ', '  ', 'I', 'L', 'Ake', false, '');
INSERT INTO languages_iso639 VALUES ('ail', '   ', '   ', '  ', 'I', 'L', 'Aimele', false, '');
INSERT INTO languages_iso639 VALUES ('aim', '   ', '   ', '  ', 'I', 'L', 'Aimol', false, '');
INSERT INTO languages_iso639 VALUES ('ain', 'ain', 'ain', '  ', 'I', 'L', 'Ainu (Japan)', false, '');
INSERT INTO languages_iso639 VALUES ('aio', '   ', '   ', '  ', 'I', 'L', 'Aiton', false, '');
INSERT INTO languages_iso639 VALUES ('aip', '   ', '   ', '  ', 'I', 'L', 'Burumakok', false, '');
INSERT INTO languages_iso639 VALUES ('aiq', '   ', '   ', '  ', 'I', 'L', 'Aimaq', false, '');
INSERT INTO languages_iso639 VALUES ('air', '   ', '   ', '  ', 'I', 'L', 'Airoran', false, '');
INSERT INTO languages_iso639 VALUES ('ais', '   ', '   ', '  ', 'I', 'L', 'Nataoran Amis', false, '');
INSERT INTO languages_iso639 VALUES ('ait', '   ', '   ', '  ', 'I', 'E', 'Arikem', false, '');
INSERT INTO languages_iso639 VALUES ('aiw', '   ', '   ', '  ', 'I', 'L', 'Aari', false, '');
INSERT INTO languages_iso639 VALUES ('aix', '   ', '   ', '  ', 'I', 'L', 'Aighon', false, '');
INSERT INTO languages_iso639 VALUES ('aiy', '   ', '   ', '  ', 'I', 'L', 'Ali', false, '');
INSERT INTO languages_iso639 VALUES ('aja', '   ', '   ', '  ', 'I', 'L', 'Aja (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('ajg', '   ', '   ', '  ', 'I', 'L', 'Aja (Benin)', false, '');
INSERT INTO languages_iso639 VALUES ('aji', '   ', '   ', '  ', 'I', 'L', 'Ajië', false, '');
INSERT INTO languages_iso639 VALUES ('ajn', '   ', '   ', '  ', 'I', 'L', 'Andajin', false, '');
INSERT INTO languages_iso639 VALUES ('ajp', '   ', '   ', '  ', 'I', 'L', 'South Levantine Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ajt', '   ', '   ', '  ', 'I', 'L', 'Judeo-Tunisian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('aju', '   ', '   ', '  ', 'I', 'L', 'Judeo-Moroccan Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ajw', '   ', '   ', '  ', 'I', 'E', 'Ajawa', false, '');
INSERT INTO languages_iso639 VALUES ('ajz', '   ', '   ', '  ', 'I', 'L', 'Amri Karbi', false, '');
INSERT INTO languages_iso639 VALUES ('aka', 'aka', 'aka', 'ak', 'M', 'L', 'Akan', false, '');
INSERT INTO languages_iso639 VALUES ('akb', '   ', '   ', '  ', 'I', 'L', 'Batak Angkola', false, '');
INSERT INTO languages_iso639 VALUES ('akc', '   ', '   ', '  ', 'I', 'L', 'Mpur', false, '');
INSERT INTO languages_iso639 VALUES ('akd', '   ', '   ', '  ', 'I', 'L', 'Ukpet-Ehom', false, '');
INSERT INTO languages_iso639 VALUES ('ake', '   ', '   ', '  ', 'I', 'L', 'Akawaio', false, '');
INSERT INTO languages_iso639 VALUES ('akf', '   ', '   ', '  ', 'I', 'L', 'Akpa', false, '');
INSERT INTO languages_iso639 VALUES ('akg', '   ', '   ', '  ', 'I', 'L', 'Anakalangu', false, '');
INSERT INTO languages_iso639 VALUES ('akh', '   ', '   ', '  ', 'I', 'L', 'Angal Heneng', false, '');
INSERT INTO languages_iso639 VALUES ('aki', '   ', '   ', '  ', 'I', 'L', 'Aiome', false, '');
INSERT INTO languages_iso639 VALUES ('akj', '   ', '   ', '  ', 'I', 'E', 'Aka-Jeru', false, '');
INSERT INTO languages_iso639 VALUES ('akk', 'akk', 'akk', '  ', 'I', 'A', 'Akkadian', false, '');
INSERT INTO languages_iso639 VALUES ('akl', '   ', '   ', '  ', 'I', 'L', 'Aklanon', false, '');
INSERT INTO languages_iso639 VALUES ('akm', '   ', '   ', '  ', 'I', 'E', 'Aka-Bo', false, '');
INSERT INTO languages_iso639 VALUES ('ako', '   ', '   ', '  ', 'I', 'L', 'Akurio', false, '');
INSERT INTO languages_iso639 VALUES ('akp', '   ', '   ', '  ', 'I', 'L', 'Siwu', false, '');
INSERT INTO languages_iso639 VALUES ('akq', '   ', '   ', '  ', 'I', 'L', 'Ak', false, '');
INSERT INTO languages_iso639 VALUES ('akr', '   ', '   ', '  ', 'I', 'L', 'Araki', false, '');
INSERT INTO languages_iso639 VALUES ('aks', '   ', '   ', '  ', 'I', 'L', 'Akaselem', false, '');
INSERT INTO languages_iso639 VALUES ('akt', '   ', '   ', '  ', 'I', 'L', 'Akolet', false, '');
INSERT INTO languages_iso639 VALUES ('aku', '   ', '   ', '  ', 'I', 'L', 'Akum', false, '');
INSERT INTO languages_iso639 VALUES ('akv', '   ', '   ', '  ', 'I', 'L', 'Akhvakh', false, '');
INSERT INTO languages_iso639 VALUES ('akw', '   ', '   ', '  ', 'I', 'L', 'Akwa', false, '');
INSERT INTO languages_iso639 VALUES ('akx', '   ', '   ', '  ', 'I', 'E', 'Aka-Kede', false, '');
INSERT INTO languages_iso639 VALUES ('aky', '   ', '   ', '  ', 'I', 'E', 'Aka-Kol', false, '');
INSERT INTO languages_iso639 VALUES ('akz', '   ', '   ', '  ', 'I', 'L', 'Alabama', false, '');
INSERT INTO languages_iso639 VALUES ('ala', '   ', '   ', '  ', 'I', 'L', 'Alago', false, '');
INSERT INTO languages_iso639 VALUES ('alc', '   ', '   ', '  ', 'I', 'L', 'Qawasqar', false, '');
INSERT INTO languages_iso639 VALUES ('ald', '   ', '   ', '  ', 'I', 'L', 'Alladian', false, '');
INSERT INTO languages_iso639 VALUES ('ale', 'ale', 'ale', '  ', 'I', 'L', 'Aleut', false, '');
INSERT INTO languages_iso639 VALUES ('alf', '   ', '   ', '  ', 'I', 'L', 'Alege', false, '');
INSERT INTO languages_iso639 VALUES ('alh', '   ', '   ', '  ', 'I', 'L', 'Alawa', false, '');
INSERT INTO languages_iso639 VALUES ('ali', '   ', '   ', '  ', 'I', 'L', 'Amaimon', false, '');
INSERT INTO languages_iso639 VALUES ('alj', '   ', '   ', '  ', 'I', 'L', 'Alangan', false, '');
INSERT INTO languages_iso639 VALUES ('alk', '   ', '   ', '  ', 'I', 'L', 'Alak', false, '');
INSERT INTO languages_iso639 VALUES ('all', '   ', '   ', '  ', 'I', 'L', 'Allar', false, '');
INSERT INTO languages_iso639 VALUES ('alm', '   ', '   ', '  ', 'I', 'L', 'Amblong', false, '');
INSERT INTO languages_iso639 VALUES ('aln', '   ', '   ', '  ', 'I', 'L', 'Gheg Albanian', false, '');
INSERT INTO languages_iso639 VALUES ('alo', '   ', '   ', '  ', 'I', 'L', 'Larike-Wakasihu', false, '');
INSERT INTO languages_iso639 VALUES ('alp', '   ', '   ', '  ', 'I', 'L', 'Alune', false, '');
INSERT INTO languages_iso639 VALUES ('alq', '   ', '   ', '  ', 'I', 'L', 'Algonquin', false, '');
INSERT INTO languages_iso639 VALUES ('alr', '   ', '   ', '  ', 'I', 'L', 'Alutor', false, '');
INSERT INTO languages_iso639 VALUES ('als', '   ', '   ', '  ', 'I', 'L', 'Tosk Albanian', false, '');
INSERT INTO languages_iso639 VALUES ('alt', 'alt', 'alt', '  ', 'I', 'L', 'Southern Altai', false, '');
INSERT INTO languages_iso639 VALUES ('alu', '   ', '   ', '  ', 'I', 'L', '''Are''are', false, '');
INSERT INTO languages_iso639 VALUES ('alw', '   ', '   ', '  ', 'I', 'L', 'Alaba-K’abeena', false, '');
INSERT INTO languages_iso639 VALUES ('alx', '   ', '   ', '  ', 'I', 'L', 'Amol', false, '');
INSERT INTO languages_iso639 VALUES ('aly', '   ', '   ', '  ', 'I', 'L', 'Alyawarr', false, '');
INSERT INTO languages_iso639 VALUES ('alz', '   ', '   ', '  ', 'I', 'L', 'Alur', false, '');
INSERT INTO languages_iso639 VALUES ('ama', '   ', '   ', '  ', 'I', 'E', 'Amanayé', false, '');
INSERT INTO languages_iso639 VALUES ('amb', '   ', '   ', '  ', 'I', 'L', 'Ambo', false, '');
INSERT INTO languages_iso639 VALUES ('amc', '   ', '   ', '  ', 'I', 'L', 'Amahuaca', false, '');
INSERT INTO languages_iso639 VALUES ('ame', '   ', '   ', '  ', 'I', 'L', 'Yanesha''', false, '');
INSERT INTO languages_iso639 VALUES ('amf', '   ', '   ', '  ', 'I', 'L', 'Hamer-Banna', false, '');
INSERT INTO languages_iso639 VALUES ('amg', '   ', '   ', '  ', 'I', 'L', 'Amurdak', false, '');
INSERT INTO languages_iso639 VALUES ('amh', 'amh', 'amh', 'am', 'I', 'L', 'Amharic', false, '');
INSERT INTO languages_iso639 VALUES ('ami', '   ', '   ', '  ', 'I', 'L', 'Amis', false, '');
INSERT INTO languages_iso639 VALUES ('amj', '   ', '   ', '  ', 'I', 'L', 'Amdang', false, '');
INSERT INTO languages_iso639 VALUES ('amk', '   ', '   ', '  ', 'I', 'L', 'Ambai', false, '');
INSERT INTO languages_iso639 VALUES ('aml', '   ', '   ', '  ', 'I', 'L', 'War-Jaintia', false, '');
INSERT INTO languages_iso639 VALUES ('amm', '   ', '   ', '  ', 'I', 'L', 'Ama (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('amn', '   ', '   ', '  ', 'I', 'L', 'Amanab', false, '');
INSERT INTO languages_iso639 VALUES ('amo', '   ', '   ', '  ', 'I', 'L', 'Amo', false, '');
INSERT INTO languages_iso639 VALUES ('amp', '   ', '   ', '  ', 'I', 'L', 'Alamblak', false, '');
INSERT INTO languages_iso639 VALUES ('amq', '   ', '   ', '  ', 'I', 'L', 'Amahai', false, '');
INSERT INTO languages_iso639 VALUES ('amr', '   ', '   ', '  ', 'I', 'L', 'Amarakaeri', false, '');
INSERT INTO languages_iso639 VALUES ('ams', '   ', '   ', '  ', 'I', 'L', 'Southern Amami-Oshima', false, '');
INSERT INTO languages_iso639 VALUES ('amt', '   ', '   ', '  ', 'I', 'L', 'Amto', false, '');
INSERT INTO languages_iso639 VALUES ('amu', '   ', '   ', '  ', 'I', 'L', 'Guerrero Amuzgo', false, '');
INSERT INTO languages_iso639 VALUES ('amv', '   ', '   ', '  ', 'I', 'L', 'Ambelau', false, '');
INSERT INTO languages_iso639 VALUES ('amw', '   ', '   ', '  ', 'I', 'L', 'Western Neo-Aramaic', false, '');
INSERT INTO languages_iso639 VALUES ('amx', '   ', '   ', '  ', 'I', 'L', 'Anmatyerre', false, '');
INSERT INTO languages_iso639 VALUES ('amy', '   ', '   ', '  ', 'I', 'L', 'Ami', false, '');
INSERT INTO languages_iso639 VALUES ('amz', '   ', '   ', '  ', 'I', 'E', 'Atampaya', false, '');
INSERT INTO languages_iso639 VALUES ('ana', '   ', '   ', '  ', 'I', 'E', 'Andaqui', false, '');
INSERT INTO languages_iso639 VALUES ('anb', '   ', '   ', '  ', 'I', 'E', 'Andoa', false, '');
INSERT INTO languages_iso639 VALUES ('anc', '   ', '   ', '  ', 'I', 'L', 'Ngas', false, '');
INSERT INTO languages_iso639 VALUES ('and', '   ', '   ', '  ', 'I', 'L', 'Ansus', false, '');
INSERT INTO languages_iso639 VALUES ('ane', '   ', '   ', '  ', 'I', 'L', 'Xârâcùù', false, '');
INSERT INTO languages_iso639 VALUES ('anf', '   ', '   ', '  ', 'I', 'L', 'Animere', false, '');
INSERT INTO languages_iso639 VALUES ('ang', 'ang', 'ang', '  ', 'I', 'H', 'Old English (ca. 450-1100)', false, '');
INSERT INTO languages_iso639 VALUES ('anh', '   ', '   ', '  ', 'I', 'L', 'Nend', false, '');
INSERT INTO languages_iso639 VALUES ('ani', '   ', '   ', '  ', 'I', 'L', 'Andi', false, '');
INSERT INTO languages_iso639 VALUES ('anj', '   ', '   ', '  ', 'I', 'L', 'Anor', false, '');
INSERT INTO languages_iso639 VALUES ('ank', '   ', '   ', '  ', 'I', 'L', 'Goemai', false, '');
INSERT INTO languages_iso639 VALUES ('anl', '   ', '   ', '  ', 'I', 'L', 'Anu-Hkongso Chin', false, '');
INSERT INTO languages_iso639 VALUES ('anm', '   ', '   ', '  ', 'I', 'L', 'Anal', false, '');
INSERT INTO languages_iso639 VALUES ('ann', '   ', '   ', '  ', 'I', 'L', 'Obolo', false, '');
INSERT INTO languages_iso639 VALUES ('ano', '   ', '   ', '  ', 'I', 'L', 'Andoque', false, '');
INSERT INTO languages_iso639 VALUES ('anp', 'anp', 'anp', '  ', 'I', 'L', 'Angika', false, '');
INSERT INTO languages_iso639 VALUES ('anq', '   ', '   ', '  ', 'I', 'L', 'Jarawa (India)', false, '');
INSERT INTO languages_iso639 VALUES ('anr', '   ', '   ', '  ', 'I', 'L', 'Andh', false, '');
INSERT INTO languages_iso639 VALUES ('ans', '   ', '   ', '  ', 'I', 'E', 'Anserma', false, '');
INSERT INTO languages_iso639 VALUES ('ant', '   ', '   ', '  ', 'I', 'L', 'Antakarinya', false, '');
INSERT INTO languages_iso639 VALUES ('anu', '   ', '   ', '  ', 'I', 'L', 'Anuak', false, '');
INSERT INTO languages_iso639 VALUES ('anv', '   ', '   ', '  ', 'I', 'L', 'Denya', false, '');
INSERT INTO languages_iso639 VALUES ('anw', '   ', '   ', '  ', 'I', 'L', 'Anaang', false, '');
INSERT INTO languages_iso639 VALUES ('anx', '   ', '   ', '  ', 'I', 'L', 'Andra-Hus', false, '');
INSERT INTO languages_iso639 VALUES ('any', '   ', '   ', '  ', 'I', 'L', 'Anyin', false, '');
INSERT INTO languages_iso639 VALUES ('anz', '   ', '   ', '  ', 'I', 'L', 'Anem', false, '');
INSERT INTO languages_iso639 VALUES ('aoa', '   ', '   ', '  ', 'I', 'L', 'Angolar', false, '');
INSERT INTO languages_iso639 VALUES ('aob', '   ', '   ', '  ', 'I', 'L', 'Abom', false, '');
INSERT INTO languages_iso639 VALUES ('aoc', '   ', '   ', '  ', 'I', 'L', 'Pemon', false, '');
INSERT INTO languages_iso639 VALUES ('aod', '   ', '   ', '  ', 'I', 'L', 'Andarum', false, '');
INSERT INTO languages_iso639 VALUES ('aoe', '   ', '   ', '  ', 'I', 'L', 'Angal Enen', false, '');
INSERT INTO languages_iso639 VALUES ('aof', '   ', '   ', '  ', 'I', 'L', 'Bragat', false, '');
INSERT INTO languages_iso639 VALUES ('aog', '   ', '   ', '  ', 'I', 'L', 'Angoram', false, '');
INSERT INTO languages_iso639 VALUES ('aoh', '   ', '   ', '  ', 'I', 'E', 'Arma', false, '');
INSERT INTO languages_iso639 VALUES ('aoi', '   ', '   ', '  ', 'I', 'L', 'Anindilyakwa', false, '');
INSERT INTO languages_iso639 VALUES ('aoj', '   ', '   ', '  ', 'I', 'L', 'Mufian', false, '');
INSERT INTO languages_iso639 VALUES ('aok', '   ', '   ', '  ', 'I', 'L', 'Arhö', false, '');
INSERT INTO languages_iso639 VALUES ('aol', '   ', '   ', '  ', 'I', 'L', 'Alor', false, '');
INSERT INTO languages_iso639 VALUES ('aom', '   ', '   ', '  ', 'I', 'L', 'Ömie', false, '');
INSERT INTO languages_iso639 VALUES ('aon', '   ', '   ', '  ', 'I', 'L', 'Bumbita Arapesh', false, '');
INSERT INTO languages_iso639 VALUES ('aor', '   ', '   ', '  ', 'I', 'E', 'Aore', false, '');
INSERT INTO languages_iso639 VALUES ('aos', '   ', '   ', '  ', 'I', 'L', 'Taikat', false, '');
INSERT INTO languages_iso639 VALUES ('aot', '   ', '   ', '  ', 'I', 'L', 'A''tong', false, '');
INSERT INTO languages_iso639 VALUES ('aou', '   ', '   ', '  ', 'I', 'L', 'A''ou', false, '');
INSERT INTO languages_iso639 VALUES ('aox', '   ', '   ', '  ', 'I', 'L', 'Atorada', false, '');
INSERT INTO languages_iso639 VALUES ('aoz', '   ', '   ', '  ', 'I', 'L', 'Uab Meto', false, '');
INSERT INTO languages_iso639 VALUES ('apb', '   ', '   ', '  ', 'I', 'L', 'Sa''a', false, '');
INSERT INTO languages_iso639 VALUES ('apc', '   ', '   ', '  ', 'I', 'L', 'North Levantine Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('apd', '   ', '   ', '  ', 'I', 'L', 'Sudanese Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ape', '   ', '   ', '  ', 'I', 'L', 'Bukiyip', false, '');
INSERT INTO languages_iso639 VALUES ('apf', '   ', '   ', '  ', 'I', 'L', 'Pahanan Agta', false, '');
INSERT INTO languages_iso639 VALUES ('apg', '   ', '   ', '  ', 'I', 'L', 'Ampanang', false, '');
INSERT INTO languages_iso639 VALUES ('aph', '   ', '   ', '  ', 'I', 'L', 'Athpariya', false, '');
INSERT INTO languages_iso639 VALUES ('api', '   ', '   ', '  ', 'I', 'L', 'Apiaká', false, '');
INSERT INTO languages_iso639 VALUES ('apj', '   ', '   ', '  ', 'I', 'L', 'Jicarilla Apache', false, '');
INSERT INTO languages_iso639 VALUES ('apk', '   ', '   ', '  ', 'I', 'L', 'Kiowa Apache', false, '');
INSERT INTO languages_iso639 VALUES ('apl', '   ', '   ', '  ', 'I', 'L', 'Lipan Apache', false, '');
INSERT INTO languages_iso639 VALUES ('apm', '   ', '   ', '  ', 'I', 'L', 'Mescalero-Chiricahua Apache', false, '');
INSERT INTO languages_iso639 VALUES ('apn', '   ', '   ', '  ', 'I', 'L', 'Apinayé', false, '');
INSERT INTO languages_iso639 VALUES ('apo', '   ', '   ', '  ', 'I', 'L', 'Ambul', false, '');
INSERT INTO languages_iso639 VALUES ('app', '   ', '   ', '  ', 'I', 'L', 'Apma', false, '');
INSERT INTO languages_iso639 VALUES ('apq', '   ', '   ', '  ', 'I', 'L', 'A-Pucikwar', false, '');
INSERT INTO languages_iso639 VALUES ('apr', '   ', '   ', '  ', 'I', 'L', 'Arop-Lokep', false, '');
INSERT INTO languages_iso639 VALUES ('aps', '   ', '   ', '  ', 'I', 'L', 'Arop-Sissano', false, '');
INSERT INTO languages_iso639 VALUES ('apt', '   ', '   ', '  ', 'I', 'L', 'Apatani', false, '');
INSERT INTO languages_iso639 VALUES ('apu', '   ', '   ', '  ', 'I', 'L', 'Apurinã', false, '');
INSERT INTO languages_iso639 VALUES ('apv', '   ', '   ', '  ', 'I', 'E', 'Alapmunte', false, '');
INSERT INTO languages_iso639 VALUES ('apw', '   ', '   ', '  ', 'I', 'L', 'Western Apache', false, '');
INSERT INTO languages_iso639 VALUES ('apx', '   ', '   ', '  ', 'I', 'L', 'Aputai', false, '');
INSERT INTO languages_iso639 VALUES ('apy', '   ', '   ', '  ', 'I', 'L', 'Apalaí', false, '');
INSERT INTO languages_iso639 VALUES ('apz', '   ', '   ', '  ', 'I', 'L', 'Safeyoka', false, '');
INSERT INTO languages_iso639 VALUES ('aqc', '   ', '   ', '  ', 'I', 'L', 'Archi', false, '');
INSERT INTO languages_iso639 VALUES ('aqd', '   ', '   ', '  ', 'I', 'L', 'Ampari Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('aqg', '   ', '   ', '  ', 'I', 'L', 'Arigidi', false, '');
INSERT INTO languages_iso639 VALUES ('aqm', '   ', '   ', '  ', 'I', 'L', 'Atohwaim', false, '');
INSERT INTO languages_iso639 VALUES ('aqn', '   ', '   ', '  ', 'I', 'L', 'Northern Alta', false, '');
INSERT INTO languages_iso639 VALUES ('aqp', '   ', '   ', '  ', 'I', 'E', 'Atakapa', false, '');
INSERT INTO languages_iso639 VALUES ('aqr', '   ', '   ', '  ', 'I', 'L', 'Arhâ', false, '');
INSERT INTO languages_iso639 VALUES ('aqz', '   ', '   ', '  ', 'I', 'L', 'Akuntsu', false, '');
INSERT INTO languages_iso639 VALUES ('ara', 'ara', 'ara', 'ar', 'M', 'L', 'Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('arb', '   ', '   ', '  ', 'I', 'L', 'Standard Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('arc', 'arc', 'arc', '  ', 'I', 'A', 'Official Aramaic (700-300 BCE)', false, '');
INSERT INTO languages_iso639 VALUES ('ard', '   ', '   ', '  ', 'I', 'E', 'Arabana', false, '');
INSERT INTO languages_iso639 VALUES ('are', '   ', '   ', '  ', 'I', 'L', 'Western Arrarnta', false, '');
INSERT INTO languages_iso639 VALUES ('arg', 'arg', 'arg', 'an', 'I', 'L', 'Aragonese', false, '');
INSERT INTO languages_iso639 VALUES ('arh', '   ', '   ', '  ', 'I', 'L', 'Arhuaco', false, '');
INSERT INTO languages_iso639 VALUES ('ari', '   ', '   ', '  ', 'I', 'L', 'Arikara', false, '');
INSERT INTO languages_iso639 VALUES ('arj', '   ', '   ', '  ', 'I', 'E', 'Arapaso', false, '');
INSERT INTO languages_iso639 VALUES ('ark', '   ', '   ', '  ', 'I', 'L', 'Arikapú', false, '');
INSERT INTO languages_iso639 VALUES ('arl', '   ', '   ', '  ', 'I', 'L', 'Arabela', false, '');
INSERT INTO languages_iso639 VALUES ('arn', 'arn', 'arn', '  ', 'I', 'L', 'Mapudungun', false, '');
INSERT INTO languages_iso639 VALUES ('aro', '   ', '   ', '  ', 'I', 'L', 'Araona', false, '');
INSERT INTO languages_iso639 VALUES ('arp', 'arp', 'arp', '  ', 'I', 'L', 'Arapaho', false, '');
INSERT INTO languages_iso639 VALUES ('arq', '   ', '   ', '  ', 'I', 'L', 'Algerian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('arr', '   ', '   ', '  ', 'I', 'L', 'Karo (Brazil)', false, '');
INSERT INTO languages_iso639 VALUES ('ars', '   ', '   ', '  ', 'I', 'L', 'Najdi Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('aru', '   ', '   ', '  ', 'I', 'E', 'Aruá (Amazonas State)', false, '');
INSERT INTO languages_iso639 VALUES ('arv', '   ', '   ', '  ', 'I', 'L', 'Arbore', false, '');
INSERT INTO languages_iso639 VALUES ('arw', 'arw', 'arw', '  ', 'I', 'L', 'Arawak', false, '');
INSERT INTO languages_iso639 VALUES ('arx', '   ', '   ', '  ', 'I', 'L', 'Aruá (Rodonia State)', false, '');
INSERT INTO languages_iso639 VALUES ('ary', '   ', '   ', '  ', 'I', 'L', 'Moroccan Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('arz', '   ', '   ', '  ', 'I', 'L', 'Egyptian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('asa', '   ', '   ', '  ', 'I', 'L', 'Asu (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('asb', '   ', '   ', '  ', 'I', 'L', 'Assiniboine', false, '');
INSERT INTO languages_iso639 VALUES ('asc', '   ', '   ', '  ', 'I', 'L', 'Casuarina Coast Asmat', false, '');
INSERT INTO languages_iso639 VALUES ('asd', '   ', '   ', '  ', 'I', 'L', 'Asas', false, '');
INSERT INTO languages_iso639 VALUES ('ase', '   ', '   ', '  ', 'I', 'L', 'American Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('asf', '   ', '   ', '  ', 'I', 'L', 'Australian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('asg', '   ', '   ', '  ', 'I', 'L', 'Cishingini', false, '');
INSERT INTO languages_iso639 VALUES ('ash', '   ', '   ', '  ', 'I', 'E', 'Abishira', false, '');
INSERT INTO languages_iso639 VALUES ('asi', '   ', '   ', '  ', 'I', 'L', 'Buruwai', false, '');
INSERT INTO languages_iso639 VALUES ('asj', '   ', '   ', '  ', 'I', 'L', 'Sari', false, '');
INSERT INTO languages_iso639 VALUES ('ask', '   ', '   ', '  ', 'I', 'L', 'Ashkun', false, '');
INSERT INTO languages_iso639 VALUES ('asl', '   ', '   ', '  ', 'I', 'L', 'Asilulu', false, '');
INSERT INTO languages_iso639 VALUES ('asm', 'asm', 'asm', 'as', 'I', 'L', 'Assamese', false, '');
INSERT INTO languages_iso639 VALUES ('asn', '   ', '   ', '  ', 'I', 'L', 'Xingú Asuriní', false, '');
INSERT INTO languages_iso639 VALUES ('aso', '   ', '   ', '  ', 'I', 'L', 'Dano', false, '');
INSERT INTO languages_iso639 VALUES ('asp', '   ', '   ', '  ', 'I', 'L', 'Algerian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('asq', '   ', '   ', '  ', 'I', 'L', 'Austrian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('asr', '   ', '   ', '  ', 'I', 'L', 'Asuri', false, '');
INSERT INTO languages_iso639 VALUES ('ass', '   ', '   ', '  ', 'I', 'L', 'Ipulo', false, '');
INSERT INTO languages_iso639 VALUES ('ast', 'ast', 'ast', '  ', 'I', 'L', 'Asturian', false, '');
INSERT INTO languages_iso639 VALUES ('asu', '   ', '   ', '  ', 'I', 'L', 'Tocantins Asurini', false, '');
INSERT INTO languages_iso639 VALUES ('asv', '   ', '   ', '  ', 'I', 'L', 'Asoa', false, '');
INSERT INTO languages_iso639 VALUES ('asw', '   ', '   ', '  ', 'I', 'L', 'Australian Aborigines Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('asx', '   ', '   ', '  ', 'I', 'L', 'Muratayak', false, '');
INSERT INTO languages_iso639 VALUES ('asy', '   ', '   ', '  ', 'I', 'L', 'Yaosakor Asmat', false, '');
INSERT INTO languages_iso639 VALUES ('asz', '   ', '   ', '  ', 'I', 'L', 'As', false, '');
INSERT INTO languages_iso639 VALUES ('ata', '   ', '   ', '  ', 'I', 'L', 'Pele-Ata', false, '');
INSERT INTO languages_iso639 VALUES ('atb', '   ', '   ', '  ', 'I', 'L', 'Zaiwa', false, '');
INSERT INTO languages_iso639 VALUES ('atc', '   ', '   ', '  ', 'I', 'E', 'Atsahuaca', false, '');
INSERT INTO languages_iso639 VALUES ('atd', '   ', '   ', '  ', 'I', 'L', 'Ata Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('ate', '   ', '   ', '  ', 'I', 'L', 'Atemble', false, '');
INSERT INTO languages_iso639 VALUES ('atg', '   ', '   ', '  ', 'I', 'L', 'Ivbie North-Okpela-Arhe', false, '');
INSERT INTO languages_iso639 VALUES ('ati', '   ', '   ', '  ', 'I', 'L', 'Attié', false, '');
INSERT INTO languages_iso639 VALUES ('atj', '   ', '   ', '  ', 'I', 'L', 'Atikamekw', false, '');
INSERT INTO languages_iso639 VALUES ('atk', '   ', '   ', '  ', 'I', 'L', 'Ati', false, '');
INSERT INTO languages_iso639 VALUES ('atl', '   ', '   ', '  ', 'I', 'L', 'Mt. Iraya Agta', false, '');
INSERT INTO languages_iso639 VALUES ('atm', '   ', '   ', '  ', 'I', 'L', 'Ata', false, '');
INSERT INTO languages_iso639 VALUES ('atn', '   ', '   ', '  ', 'I', 'L', 'Ashtiani', false, '');
INSERT INTO languages_iso639 VALUES ('ato', '   ', '   ', '  ', 'I', 'L', 'Atong', false, '');
INSERT INTO languages_iso639 VALUES ('atp', '   ', '   ', '  ', 'I', 'L', 'Pudtol Atta', false, '');
INSERT INTO languages_iso639 VALUES ('atq', '   ', '   ', '  ', 'I', 'L', 'Aralle-Tabulahan', false, '');
INSERT INTO languages_iso639 VALUES ('atr', '   ', '   ', '  ', 'I', 'L', 'Waimiri-Atroari', false, '');
INSERT INTO languages_iso639 VALUES ('ats', '   ', '   ', '  ', 'I', 'L', 'Gros Ventre', false, '');
INSERT INTO languages_iso639 VALUES ('att', '   ', '   ', '  ', 'I', 'L', 'Pamplona Atta', false, '');
INSERT INTO languages_iso639 VALUES ('atu', '   ', '   ', '  ', 'I', 'L', 'Reel', false, '');
INSERT INTO languages_iso639 VALUES ('atv', '   ', '   ', '  ', 'I', 'L', 'Northern Altai', false, '');
INSERT INTO languages_iso639 VALUES ('atw', '   ', '   ', '  ', 'I', 'L', 'Atsugewi', false, '');
INSERT INTO languages_iso639 VALUES ('atx', '   ', '   ', '  ', 'I', 'L', 'Arutani', false, '');
INSERT INTO languages_iso639 VALUES ('aty', '   ', '   ', '  ', 'I', 'L', 'Aneityum', false, '');
INSERT INTO languages_iso639 VALUES ('atz', '   ', '   ', '  ', 'I', 'L', 'Arta', false, '');
INSERT INTO languages_iso639 VALUES ('aua', '   ', '   ', '  ', 'I', 'L', 'Asumboa', false, '');
INSERT INTO languages_iso639 VALUES ('aub', '   ', '   ', '  ', 'I', 'L', 'Alugu', false, '');
INSERT INTO languages_iso639 VALUES ('auc', '   ', '   ', '  ', 'I', 'L', 'Waorani', false, '');
INSERT INTO languages_iso639 VALUES ('aud', '   ', '   ', '  ', 'I', 'L', 'Anuta', false, '');
INSERT INTO languages_iso639 VALUES ('aue', '   ', '   ', '  ', 'I', 'L', '=/Kx''au//''ein', false, '');
INSERT INTO languages_iso639 VALUES ('aug', '   ', '   ', '  ', 'I', 'L', 'Aguna', false, '');
INSERT INTO languages_iso639 VALUES ('auh', '   ', '   ', '  ', 'I', 'L', 'Aushi', false, '');
INSERT INTO languages_iso639 VALUES ('aui', '   ', '   ', '  ', 'I', 'L', 'Anuki', false, '');
INSERT INTO languages_iso639 VALUES ('auj', '   ', '   ', '  ', 'I', 'L', 'Awjilah', false, '');
INSERT INTO languages_iso639 VALUES ('auk', '   ', '   ', '  ', 'I', 'L', 'Heyo', false, '');
INSERT INTO languages_iso639 VALUES ('aul', '   ', '   ', '  ', 'I', 'L', 'Aulua', false, '');
INSERT INTO languages_iso639 VALUES ('aum', '   ', '   ', '  ', 'I', 'L', 'Asu (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('aun', '   ', '   ', '  ', 'I', 'L', 'Molmo One', false, '');
INSERT INTO languages_iso639 VALUES ('auo', '   ', '   ', '  ', 'I', 'E', 'Auyokawa', false, '');
INSERT INTO languages_iso639 VALUES ('aup', '   ', '   ', '  ', 'I', 'L', 'Makayam', false, '');
INSERT INTO languages_iso639 VALUES ('auq', '   ', '   ', '  ', 'I', 'L', 'Anus', false, '');
INSERT INTO languages_iso639 VALUES ('aur', '   ', '   ', '  ', 'I', 'L', 'Aruek', false, '');
INSERT INTO languages_iso639 VALUES ('aut', '   ', '   ', '  ', 'I', 'L', 'Austral', false, '');
INSERT INTO languages_iso639 VALUES ('auu', '   ', '   ', '  ', 'I', 'L', 'Auye', false, '');
INSERT INTO languages_iso639 VALUES ('auw', '   ', '   ', '  ', 'I', 'L', 'Awyi', false, '');
INSERT INTO languages_iso639 VALUES ('aux', '   ', '   ', '  ', 'I', 'E', 'Aurá', false, '');
INSERT INTO languages_iso639 VALUES ('auy', '   ', '   ', '  ', 'I', 'L', 'Awiyaana', false, '');
INSERT INTO languages_iso639 VALUES ('auz', '   ', '   ', '  ', 'I', 'L', 'Uzbeki Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ava', 'ava', 'ava', 'av', 'I', 'L', 'Avaric', false, '');
INSERT INTO languages_iso639 VALUES ('avb', '   ', '   ', '  ', 'I', 'L', 'Avau', false, '');
INSERT INTO languages_iso639 VALUES ('avd', '   ', '   ', '  ', 'I', 'L', 'Alviri-Vidari', false, '');
INSERT INTO languages_iso639 VALUES ('ave', 'ave', 'ave', 'ae', 'I', 'A', 'Avestan', false, '');
INSERT INTO languages_iso639 VALUES ('avi', '   ', '   ', '  ', 'I', 'L', 'Avikam', false, '');
INSERT INTO languages_iso639 VALUES ('avk', '   ', '   ', '  ', 'I', 'C', 'Kotava', false, '');
INSERT INTO languages_iso639 VALUES ('avl', '   ', '   ', '  ', 'I', 'L', 'Eastern Egyptian Bedawi Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('avm', '   ', '   ', '  ', 'I', 'E', 'Angkamuthi', false, '');
INSERT INTO languages_iso639 VALUES ('avn', '   ', '   ', '  ', 'I', 'L', 'Avatime', false, '');
INSERT INTO languages_iso639 VALUES ('avo', '   ', '   ', '  ', 'I', 'E', 'Agavotaguerra', false, '');
INSERT INTO languages_iso639 VALUES ('avs', '   ', '   ', '  ', 'I', 'E', 'Aushiri', false, '');
INSERT INTO languages_iso639 VALUES ('avt', '   ', '   ', '  ', 'I', 'L', 'Au', false, '');
INSERT INTO languages_iso639 VALUES ('avu', '   ', '   ', '  ', 'I', 'L', 'Avokaya', false, '');
INSERT INTO languages_iso639 VALUES ('avv', '   ', '   ', '  ', 'I', 'L', 'Avá-Canoeiro', false, '');
INSERT INTO languages_iso639 VALUES ('awa', 'awa', 'awa', '  ', 'I', 'L', 'Awadhi', false, '');
INSERT INTO languages_iso639 VALUES ('awb', '   ', '   ', '  ', 'I', 'L', 'Awa (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('awc', '   ', '   ', '  ', 'I', 'L', 'Cicipu', false, '');
INSERT INTO languages_iso639 VALUES ('awe', '   ', '   ', '  ', 'I', 'L', 'Awetí', false, '');
INSERT INTO languages_iso639 VALUES ('awg', '   ', '   ', '  ', 'I', 'E', 'Anguthimri', false, '');
INSERT INTO languages_iso639 VALUES ('awh', '   ', '   ', '  ', 'I', 'L', 'Awbono', false, '');
INSERT INTO languages_iso639 VALUES ('awi', '   ', '   ', '  ', 'I', 'L', 'Aekyom', false, '');
INSERT INTO languages_iso639 VALUES ('awk', '   ', '   ', '  ', 'I', 'E', 'Awabakal', false, '');
INSERT INTO languages_iso639 VALUES ('awm', '   ', '   ', '  ', 'I', 'L', 'Arawum', false, '');
INSERT INTO languages_iso639 VALUES ('awn', '   ', '   ', '  ', 'I', 'L', 'Awngi', false, '');
INSERT INTO languages_iso639 VALUES ('awo', '   ', '   ', '  ', 'I', 'L', 'Awak', false, '');
INSERT INTO languages_iso639 VALUES ('awr', '   ', '   ', '  ', 'I', 'L', 'Awera', false, '');
INSERT INTO languages_iso639 VALUES ('aws', '   ', '   ', '  ', 'I', 'L', 'South Awyu', false, '');
INSERT INTO languages_iso639 VALUES ('awt', '   ', '   ', '  ', 'I', 'L', 'Araweté', false, '');
INSERT INTO languages_iso639 VALUES ('awu', '   ', '   ', '  ', 'I', 'L', 'Central Awyu', false, '');
INSERT INTO languages_iso639 VALUES ('awv', '   ', '   ', '  ', 'I', 'L', 'Jair Awyu', false, '');
INSERT INTO languages_iso639 VALUES ('aww', '   ', '   ', '  ', 'I', 'L', 'Awun', false, '');
INSERT INTO languages_iso639 VALUES ('awx', '   ', '   ', '  ', 'I', 'L', 'Awara', false, '');
INSERT INTO languages_iso639 VALUES ('awy', '   ', '   ', '  ', 'I', 'L', 'Edera Awyu', false, '');
INSERT INTO languages_iso639 VALUES ('axb', '   ', '   ', '  ', 'I', 'E', 'Abipon', false, '');
INSERT INTO languages_iso639 VALUES ('axe', '   ', '   ', '  ', 'I', 'E', 'Ayerrerenge', false, '');
INSERT INTO languages_iso639 VALUES ('axg', '   ', '   ', '  ', 'I', 'E', 'Mato Grosso Arára', false, '');
INSERT INTO languages_iso639 VALUES ('axk', '   ', '   ', '  ', 'I', 'L', 'Yaka (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('axl', '   ', '   ', '  ', 'I', 'E', 'Lower Southern Aranda', false, '');
INSERT INTO languages_iso639 VALUES ('axm', '   ', '   ', '  ', 'I', 'H', 'Middle Armenian', false, '');
INSERT INTO languages_iso639 VALUES ('axx', '   ', '   ', '  ', 'I', 'L', 'Xârâgurè', false, '');
INSERT INTO languages_iso639 VALUES ('aya', '   ', '   ', '  ', 'I', 'L', 'Awar', false, '');
INSERT INTO languages_iso639 VALUES ('ayb', '   ', '   ', '  ', 'I', 'L', 'Ayizo Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('ayc', '   ', '   ', '  ', 'I', 'L', 'Southern Aymara', false, '');
INSERT INTO languages_iso639 VALUES ('ayd', '   ', '   ', '  ', 'I', 'E', 'Ayabadhu', false, '');
INSERT INTO languages_iso639 VALUES ('aye', '   ', '   ', '  ', 'I', 'L', 'Ayere', false, '');
INSERT INTO languages_iso639 VALUES ('ayg', '   ', '   ', '  ', 'I', 'L', 'Ginyanga', false, '');
INSERT INTO languages_iso639 VALUES ('ayh', '   ', '   ', '  ', 'I', 'L', 'Hadrami Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ayi', '   ', '   ', '  ', 'I', 'L', 'Leyigha', false, '');
INSERT INTO languages_iso639 VALUES ('ayk', '   ', '   ', '  ', 'I', 'L', 'Akuku', false, '');
INSERT INTO languages_iso639 VALUES ('ayl', '   ', '   ', '  ', 'I', 'L', 'Libyan Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('aym', 'aym', 'aym', 'ay', 'M', 'L', 'Aymara', false, '');
INSERT INTO languages_iso639 VALUES ('ayn', '   ', '   ', '  ', 'I', 'L', 'Sanaani Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ayo', '   ', '   ', '  ', 'I', 'L', 'Ayoreo', false, '');
INSERT INTO languages_iso639 VALUES ('ayp', '   ', '   ', '  ', 'I', 'L', 'North Mesopotamian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ayq', '   ', '   ', '  ', 'I', 'L', 'Ayi (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('ayr', '   ', '   ', '  ', 'I', 'L', 'Central Aymara', false, '');
INSERT INTO languages_iso639 VALUES ('ays', '   ', '   ', '  ', 'I', 'L', 'Sorsogon Ayta', false, '');
INSERT INTO languages_iso639 VALUES ('ayt', '   ', '   ', '  ', 'I', 'L', 'Magbukun Ayta', false, '');
INSERT INTO languages_iso639 VALUES ('ayu', '   ', '   ', '  ', 'I', 'L', 'Ayu', false, '');
INSERT INTO languages_iso639 VALUES ('ayy', '   ', '   ', '  ', 'I', 'E', 'Tayabas Ayta', false, '');
INSERT INTO languages_iso639 VALUES ('ayz', '   ', '   ', '  ', 'I', 'L', 'Mai Brat', false, '');
INSERT INTO languages_iso639 VALUES ('aza', '   ', '   ', '  ', 'I', 'L', 'Azha', false, '');
INSERT INTO languages_iso639 VALUES ('azb', '   ', '   ', '  ', 'I', 'L', 'South Azerbaijani', false, '');
INSERT INTO languages_iso639 VALUES ('azd', '   ', '   ', '  ', 'I', 'L', 'Eastern Durango Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('aze', 'aze', 'aze', 'az', 'M', 'L', 'Azerbaijani', false, '');
INSERT INTO languages_iso639 VALUES ('azg', '   ', '   ', '  ', 'I', 'L', 'San Pedro Amuzgos Amuzgo', false, '');
INSERT INTO languages_iso639 VALUES ('azj', '   ', '   ', '  ', 'I', 'L', 'North Azerbaijani', false, '');
INSERT INTO languages_iso639 VALUES ('azm', '   ', '   ', '  ', 'I', 'L', 'Ipalapa Amuzgo', false, '');
INSERT INTO languages_iso639 VALUES ('azn', '   ', '   ', '  ', 'I', 'L', 'Western Durango Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('azo', '   ', '   ', '  ', 'I', 'L', 'Awing', false, '');
INSERT INTO languages_iso639 VALUES ('azt', '   ', '   ', '  ', 'I', 'L', 'Faire Atta', false, '');
INSERT INTO languages_iso639 VALUES ('azz', '   ', '   ', '  ', 'I', 'L', 'Highland Puebla Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('baa', '   ', '   ', '  ', 'I', 'L', 'Babatana', false, '');
INSERT INTO languages_iso639 VALUES ('bab', '   ', '   ', '  ', 'I', 'L', 'Bainouk-Gunyuño', false, '');
INSERT INTO languages_iso639 VALUES ('bac', '   ', '   ', '  ', 'I', 'L', 'Badui', false, '');
INSERT INTO languages_iso639 VALUES ('bae', '   ', '   ', '  ', 'I', 'E', 'Baré', false, '');
INSERT INTO languages_iso639 VALUES ('baf', '   ', '   ', '  ', 'I', 'L', 'Nubaca', false, '');
INSERT INTO languages_iso639 VALUES ('bag', '   ', '   ', '  ', 'I', 'L', 'Tuki', false, '');
INSERT INTO languages_iso639 VALUES ('bah', '   ', '   ', '  ', 'I', 'L', 'Bahamas Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('baj', '   ', '   ', '  ', 'I', 'L', 'Barakai', false, '');
INSERT INTO languages_iso639 VALUES ('bak', 'bak', 'bak', 'ba', 'I', 'L', 'Bashkir', false, '');
INSERT INTO languages_iso639 VALUES ('bal', 'bal', 'bal', '  ', 'M', 'L', 'Baluchi', false, '');
INSERT INTO languages_iso639 VALUES ('bam', 'bam', 'bam', 'bm', 'I', 'L', 'Bambara', false, '');
INSERT INTO languages_iso639 VALUES ('ban', 'ban', 'ban', '  ', 'I', 'L', 'Balinese', false, '');
INSERT INTO languages_iso639 VALUES ('bao', '   ', '   ', '  ', 'I', 'L', 'Waimaha', false, '');
INSERT INTO languages_iso639 VALUES ('bap', '   ', '   ', '  ', 'I', 'L', 'Bantawa', false, '');
INSERT INTO languages_iso639 VALUES ('bar', '   ', '   ', '  ', 'I', 'L', 'Bavarian', false, '');
INSERT INTO languages_iso639 VALUES ('bas', 'bas', 'bas', '  ', 'I', 'L', 'Basa (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('bau', '   ', '   ', '  ', 'I', 'L', 'Bada (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('bav', '   ', '   ', '  ', 'I', 'L', 'Vengo', false, '');
INSERT INTO languages_iso639 VALUES ('baw', '   ', '   ', '  ', 'I', 'L', 'Bambili-Bambui', false, '');
INSERT INTO languages_iso639 VALUES ('bax', '   ', '   ', '  ', 'I', 'L', 'Bamun', false, '');
INSERT INTO languages_iso639 VALUES ('bay', '   ', '   ', '  ', 'I', 'L', 'Batuley', false, '');
INSERT INTO languages_iso639 VALUES ('bba', '   ', '   ', '  ', 'I', 'L', 'Baatonum', false, '');
INSERT INTO languages_iso639 VALUES ('bbb', '   ', '   ', '  ', 'I', 'L', 'Barai', false, '');
INSERT INTO languages_iso639 VALUES ('bbc', '   ', '   ', '  ', 'I', 'L', 'Batak Toba', false, '');
INSERT INTO languages_iso639 VALUES ('bbd', '   ', '   ', '  ', 'I', 'L', 'Bau', false, '');
INSERT INTO languages_iso639 VALUES ('bbe', '   ', '   ', '  ', 'I', 'L', 'Bangba', false, '');
INSERT INTO languages_iso639 VALUES ('bbf', '   ', '   ', '  ', 'I', 'L', 'Baibai', false, '');
INSERT INTO languages_iso639 VALUES ('bbg', '   ', '   ', '  ', 'I', 'L', 'Barama', false, '');
INSERT INTO languages_iso639 VALUES ('bbh', '   ', '   ', '  ', 'I', 'L', 'Bugan', false, '');
INSERT INTO languages_iso639 VALUES ('bbi', '   ', '   ', '  ', 'I', 'L', 'Barombi', false, '');
INSERT INTO languages_iso639 VALUES ('bbj', '   ', '   ', '  ', 'I', 'L', 'Ghomálá''', false, '');
INSERT INTO languages_iso639 VALUES ('bbk', '   ', '   ', '  ', 'I', 'L', 'Babanki', false, '');
INSERT INTO languages_iso639 VALUES ('bbl', '   ', '   ', '  ', 'I', 'L', 'Bats', false, '');
INSERT INTO languages_iso639 VALUES ('bbm', '   ', '   ', '  ', 'I', 'L', 'Babango', false, '');
INSERT INTO languages_iso639 VALUES ('bbn', '   ', '   ', '  ', 'I', 'L', 'Uneapa', false, '');
INSERT INTO languages_iso639 VALUES ('bbo', '   ', '   ', '  ', 'I', 'L', 'Northern Bobo Madaré', false, '');
INSERT INTO languages_iso639 VALUES ('bbp', '   ', '   ', '  ', 'I', 'L', 'West Central Banda', false, '');
INSERT INTO languages_iso639 VALUES ('bbq', '   ', '   ', '  ', 'I', 'L', 'Bamali', false, '');
INSERT INTO languages_iso639 VALUES ('bbr', '   ', '   ', '  ', 'I', 'L', 'Girawa', false, '');
INSERT INTO languages_iso639 VALUES ('bbs', '   ', '   ', '  ', 'I', 'L', 'Bakpinka', false, '');
INSERT INTO languages_iso639 VALUES ('bbt', '   ', '   ', '  ', 'I', 'L', 'Mburku', false, '');
INSERT INTO languages_iso639 VALUES ('bbu', '   ', '   ', '  ', 'I', 'L', 'Kulung (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('bbv', '   ', '   ', '  ', 'I', 'L', 'Karnai', false, '');
INSERT INTO languages_iso639 VALUES ('bbw', '   ', '   ', '  ', 'I', 'L', 'Baba', false, '');
INSERT INTO languages_iso639 VALUES ('bbx', '   ', '   ', '  ', 'I', 'L', 'Bubia', false, '');
INSERT INTO languages_iso639 VALUES ('bby', '   ', '   ', '  ', 'I', 'L', 'Befang', false, '');
INSERT INTO languages_iso639 VALUES ('bbz', '   ', '   ', '  ', 'I', 'L', 'Babalia Creole Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('bca', '   ', '   ', '  ', 'I', 'L', 'Central Bai', false, '');
INSERT INTO languages_iso639 VALUES ('bcb', '   ', '   ', '  ', 'I', 'L', 'Bainouk-Samik', false, '');
INSERT INTO languages_iso639 VALUES ('bcc', '   ', '   ', '  ', 'I', 'L', 'Southern Balochi', false, '');
INSERT INTO languages_iso639 VALUES ('bcd', '   ', '   ', '  ', 'I', 'L', 'North Babar', false, '');
INSERT INTO languages_iso639 VALUES ('bce', '   ', '   ', '  ', 'I', 'L', 'Bamenyam', false, '');
INSERT INTO languages_iso639 VALUES ('bcf', '   ', '   ', '  ', 'I', 'L', 'Bamu', false, '');
INSERT INTO languages_iso639 VALUES ('bcg', '   ', '   ', '  ', 'I', 'L', 'Baga Binari', false, '');
INSERT INTO languages_iso639 VALUES ('bch', '   ', '   ', '  ', 'I', 'L', 'Bariai', false, '');
INSERT INTO languages_iso639 VALUES ('bci', '   ', '   ', '  ', 'I', 'L', 'Baoulé', false, '');
INSERT INTO languages_iso639 VALUES ('bcj', '   ', '   ', '  ', 'I', 'L', 'Bardi', false, '');
INSERT INTO languages_iso639 VALUES ('bck', '   ', '   ', '  ', 'I', 'L', 'Bunaba', false, '');
INSERT INTO languages_iso639 VALUES ('bcl', '   ', '   ', '  ', 'I', 'L', 'Central Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('bcm', '   ', '   ', '  ', 'I', 'L', 'Bannoni', false, '');
INSERT INTO languages_iso639 VALUES ('bcn', '   ', '   ', '  ', 'I', 'L', 'Bali (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('bco', '   ', '   ', '  ', 'I', 'L', 'Kaluli', false, '');
INSERT INTO languages_iso639 VALUES ('bcp', '   ', '   ', '  ', 'I', 'L', 'Bali (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('bcq', '   ', '   ', '  ', 'I', 'L', 'Bench', false, '');
INSERT INTO languages_iso639 VALUES ('bcr', '   ', '   ', '  ', 'I', 'L', 'Babine', false, '');
INSERT INTO languages_iso639 VALUES ('bcs', '   ', '   ', '  ', 'I', 'L', 'Kohumono', false, '');
INSERT INTO languages_iso639 VALUES ('bct', '   ', '   ', '  ', 'I', 'L', 'Bendi', false, '');
INSERT INTO languages_iso639 VALUES ('bcu', '   ', '   ', '  ', 'I', 'L', 'Awad Bing', false, '');
INSERT INTO languages_iso639 VALUES ('bcv', '   ', '   ', '  ', 'I', 'L', 'Shoo-Minda-Nye', false, '');
INSERT INTO languages_iso639 VALUES ('bcw', '   ', '   ', '  ', 'I', 'L', 'Bana', false, '');
INSERT INTO languages_iso639 VALUES ('bcy', '   ', '   ', '  ', 'I', 'L', 'Bacama', false, '');
INSERT INTO languages_iso639 VALUES ('bcz', '   ', '   ', '  ', 'I', 'L', 'Bainouk-Gunyaamolo', false, '');
INSERT INTO languages_iso639 VALUES ('bda', '   ', '   ', '  ', 'I', 'L', 'Bayot', false, '');
INSERT INTO languages_iso639 VALUES ('bdb', '   ', '   ', '  ', 'I', 'L', 'Basap', false, '');
INSERT INTO languages_iso639 VALUES ('bdc', '   ', '   ', '  ', 'I', 'L', 'Emberá-Baudó', false, '');
INSERT INTO languages_iso639 VALUES ('bdd', '   ', '   ', '  ', 'I', 'L', 'Bunama', false, '');
INSERT INTO languages_iso639 VALUES ('bde', '   ', '   ', '  ', 'I', 'L', 'Bade', false, '');
INSERT INTO languages_iso639 VALUES ('bdf', '   ', '   ', '  ', 'I', 'L', 'Biage', false, '');
INSERT INTO languages_iso639 VALUES ('bdg', '   ', '   ', '  ', 'I', 'L', 'Bonggi', false, '');
INSERT INTO languages_iso639 VALUES ('bdh', '   ', '   ', '  ', 'I', 'L', 'Baka (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('bdi', '   ', '   ', '  ', 'I', 'L', 'Burun', false, '');
INSERT INTO languages_iso639 VALUES ('bdj', '   ', '   ', '  ', 'I', 'L', 'Bai', false, '');
INSERT INTO languages_iso639 VALUES ('bdk', '   ', '   ', '  ', 'I', 'L', 'Budukh', false, '');
INSERT INTO languages_iso639 VALUES ('bdl', '   ', '   ', '  ', 'I', 'L', 'Indonesian Bajau', false, '');
INSERT INTO languages_iso639 VALUES ('bdm', '   ', '   ', '  ', 'I', 'L', 'Buduma', false, '');
INSERT INTO languages_iso639 VALUES ('bdn', '   ', '   ', '  ', 'I', 'L', 'Baldemu', false, '');
INSERT INTO languages_iso639 VALUES ('bdo', '   ', '   ', '  ', 'I', 'L', 'Morom', false, '');
INSERT INTO languages_iso639 VALUES ('bdp', '   ', '   ', '  ', 'I', 'L', 'Bende', false, '');
INSERT INTO languages_iso639 VALUES ('bdq', '   ', '   ', '  ', 'I', 'L', 'Bahnar', false, '');
INSERT INTO languages_iso639 VALUES ('bdr', '   ', '   ', '  ', 'I', 'L', 'West Coast Bajau', false, '');
INSERT INTO languages_iso639 VALUES ('bds', '   ', '   ', '  ', 'I', 'L', 'Burunge', false, '');
INSERT INTO languages_iso639 VALUES ('bdt', '   ', '   ', '  ', 'I', 'L', 'Bokoto', false, '');
INSERT INTO languages_iso639 VALUES ('bdu', '   ', '   ', '  ', 'I', 'L', 'Oroko', false, '');
INSERT INTO languages_iso639 VALUES ('bdv', '   ', '   ', '  ', 'I', 'L', 'Bodo Parja', false, '');
INSERT INTO languages_iso639 VALUES ('bdw', '   ', '   ', '  ', 'I', 'L', 'Baham', false, '');
INSERT INTO languages_iso639 VALUES ('bdx', '   ', '   ', '  ', 'I', 'L', 'Budong-Budong', false, '');
INSERT INTO languages_iso639 VALUES ('bdy', '   ', '   ', '  ', 'I', 'L', 'Bandjalang', false, '');
INSERT INTO languages_iso639 VALUES ('bdz', '   ', '   ', '  ', 'I', 'L', 'Badeshi', false, '');
INSERT INTO languages_iso639 VALUES ('bea', '   ', '   ', '  ', 'I', 'L', 'Beaver', false, '');
INSERT INTO languages_iso639 VALUES ('beb', '   ', '   ', '  ', 'I', 'L', 'Bebele', false, '');
INSERT INTO languages_iso639 VALUES ('bec', '   ', '   ', '  ', 'I', 'L', 'Iceve-Maci', false, '');
INSERT INTO languages_iso639 VALUES ('bed', '   ', '   ', '  ', 'I', 'L', 'Bedoanas', false, '');
INSERT INTO languages_iso639 VALUES ('bee', '   ', '   ', '  ', 'I', 'L', 'Byangsi', false, '');
INSERT INTO languages_iso639 VALUES ('bef', '   ', '   ', '  ', 'I', 'L', 'Benabena', false, '');
INSERT INTO languages_iso639 VALUES ('beg', '   ', '   ', '  ', 'I', 'L', 'Belait', false, '');
INSERT INTO languages_iso639 VALUES ('beh', '   ', '   ', '  ', 'I', 'L', 'Biali', false, '');
INSERT INTO languages_iso639 VALUES ('bei', '   ', '   ', '  ', 'I', 'L', 'Bekati''', false, '');
INSERT INTO languages_iso639 VALUES ('bej', 'bej', 'bej', '  ', 'I', 'L', 'Beja', false, '');
INSERT INTO languages_iso639 VALUES ('bek', '   ', '   ', '  ', 'I', 'L', 'Bebeli', false, '');
INSERT INTO languages_iso639 VALUES ('bel', 'bel', 'bel', 'be', 'I', 'L', 'Belarusian', false, '');
INSERT INTO languages_iso639 VALUES ('bem', 'bem', 'bem', '  ', 'I', 'L', 'Bemba (Zambia)', false, '');
INSERT INTO languages_iso639 VALUES ('ben', 'ben', 'ben', 'bn', 'I', 'L', 'Bengali', false, '');
INSERT INTO languages_iso639 VALUES ('beo', '   ', '   ', '  ', 'I', 'L', 'Beami', false, '');
INSERT INTO languages_iso639 VALUES ('bep', '   ', '   ', '  ', 'I', 'L', 'Besoa', false, '');
INSERT INTO languages_iso639 VALUES ('beq', '   ', '   ', '  ', 'I', 'L', 'Beembe', false, '');
INSERT INTO languages_iso639 VALUES ('bes', '   ', '   ', '  ', 'I', 'L', 'Besme', false, '');
INSERT INTO languages_iso639 VALUES ('bet', '   ', '   ', '  ', 'I', 'L', 'Guiberoua Béte', false, '');
INSERT INTO languages_iso639 VALUES ('beu', '   ', '   ', '  ', 'I', 'L', 'Blagar', false, '');
INSERT INTO languages_iso639 VALUES ('bev', '   ', '   ', '  ', 'I', 'L', 'Daloa Bété', false, '');
INSERT INTO languages_iso639 VALUES ('bew', '   ', '   ', '  ', 'I', 'L', 'Betawi', false, '');
INSERT INTO languages_iso639 VALUES ('bex', '   ', '   ', '  ', 'I', 'L', 'Jur Modo', false, '');
INSERT INTO languages_iso639 VALUES ('bey', '   ', '   ', '  ', 'I', 'L', 'Beli (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('bez', '   ', '   ', '  ', 'I', 'L', 'Bena (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('bfa', '   ', '   ', '  ', 'I', 'L', 'Bari', false, '');
INSERT INTO languages_iso639 VALUES ('bfb', '   ', '   ', '  ', 'I', 'L', 'Pauri Bareli', false, '');
INSERT INTO languages_iso639 VALUES ('bfc', '   ', '   ', '  ', 'I', 'L', 'Northern Bai', false, '');
INSERT INTO languages_iso639 VALUES ('bfd', '   ', '   ', '  ', 'I', 'L', 'Bafut', false, '');
INSERT INTO languages_iso639 VALUES ('bfe', '   ', '   ', '  ', 'I', 'L', 'Betaf', false, '');
INSERT INTO languages_iso639 VALUES ('bff', '   ', '   ', '  ', 'I', 'L', 'Bofi', false, '');
INSERT INTO languages_iso639 VALUES ('bfg', '   ', '   ', '  ', 'I', 'L', 'Busang Kayan', false, '');
INSERT INTO languages_iso639 VALUES ('bfh', '   ', '   ', '  ', 'I', 'L', 'Blafe', false, '');
INSERT INTO languages_iso639 VALUES ('bfi', '   ', '   ', '  ', 'I', 'L', 'British Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('bfj', '   ', '   ', '  ', 'I', 'L', 'Bafanji', false, '');
INSERT INTO languages_iso639 VALUES ('bfk', '   ', '   ', '  ', 'I', 'L', 'Ban Khor Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('bfl', '   ', '   ', '  ', 'I', 'L', 'Banda-Ndélé', false, '');
INSERT INTO languages_iso639 VALUES ('bfm', '   ', '   ', '  ', 'I', 'L', 'Mmen', false, '');
INSERT INTO languages_iso639 VALUES ('bfn', '   ', '   ', '  ', 'I', 'L', 'Bunak', false, '');
INSERT INTO languages_iso639 VALUES ('bfo', '   ', '   ', '  ', 'I', 'L', 'Malba Birifor', false, '');
INSERT INTO languages_iso639 VALUES ('bfp', '   ', '   ', '  ', 'I', 'L', 'Beba', false, '');
INSERT INTO languages_iso639 VALUES ('bfq', '   ', '   ', '  ', 'I', 'L', 'Badaga', false, '');
INSERT INTO languages_iso639 VALUES ('bfr', '   ', '   ', '  ', 'I', 'L', 'Bazigar', false, '');
INSERT INTO languages_iso639 VALUES ('bfs', '   ', '   ', '  ', 'I', 'L', 'Southern Bai', false, '');
INSERT INTO languages_iso639 VALUES ('bft', '   ', '   ', '  ', 'I', 'L', 'Balti', false, '');
INSERT INTO languages_iso639 VALUES ('bfu', '   ', '   ', '  ', 'I', 'L', 'Gahri', false, '');
INSERT INTO languages_iso639 VALUES ('bfw', '   ', '   ', '  ', 'I', 'L', 'Bondo', false, '');
INSERT INTO languages_iso639 VALUES ('bfx', '   ', '   ', '  ', 'I', 'L', 'Bantayanon', false, '');
INSERT INTO languages_iso639 VALUES ('bfy', '   ', '   ', '  ', 'I', 'L', 'Bagheli', false, '');
INSERT INTO languages_iso639 VALUES ('bfz', '   ', '   ', '  ', 'I', 'L', 'Mahasu Pahari', false, '');
INSERT INTO languages_iso639 VALUES ('bga', '   ', '   ', '  ', 'I', 'L', 'Gwamhi-Wuri', false, '');
INSERT INTO languages_iso639 VALUES ('bgb', '   ', '   ', '  ', 'I', 'L', 'Bobongko', false, '');
INSERT INTO languages_iso639 VALUES ('bgc', '   ', '   ', '  ', 'I', 'L', 'Haryanvi', false, '');
INSERT INTO languages_iso639 VALUES ('bgd', '   ', '   ', '  ', 'I', 'L', 'Rathwi Bareli', false, '');
INSERT INTO languages_iso639 VALUES ('bge', '   ', '   ', '  ', 'I', 'L', 'Bauria', false, '');
INSERT INTO languages_iso639 VALUES ('bgf', '   ', '   ', '  ', 'I', 'L', 'Bangandu', false, '');
INSERT INTO languages_iso639 VALUES ('bgg', '   ', '   ', '  ', 'I', 'L', 'Bugun', false, '');
INSERT INTO languages_iso639 VALUES ('bgi', '   ', '   ', '  ', 'I', 'L', 'Giangan', false, '');
INSERT INTO languages_iso639 VALUES ('bgj', '   ', '   ', '  ', 'I', 'L', 'Bangolan', false, '');
INSERT INTO languages_iso639 VALUES ('bgk', '   ', '   ', '  ', 'I', 'L', 'Bit', false, '');
INSERT INTO languages_iso639 VALUES ('bgl', '   ', '   ', '  ', 'I', 'L', 'Bo (Laos)', false, '');
INSERT INTO languages_iso639 VALUES ('bgm', '   ', '   ', '  ', 'I', 'L', 'Baga Mboteni', false, '');
INSERT INTO languages_iso639 VALUES ('bgn', '   ', '   ', '  ', 'I', 'L', 'Western Balochi', false, '');
INSERT INTO languages_iso639 VALUES ('bgo', '   ', '   ', '  ', 'I', 'L', 'Baga Koga', false, '');
INSERT INTO languages_iso639 VALUES ('bgp', '   ', '   ', '  ', 'I', 'L', 'Eastern Balochi', false, '');
INSERT INTO languages_iso639 VALUES ('bgq', '   ', '   ', '  ', 'I', 'L', 'Bagri', false, '');
INSERT INTO languages_iso639 VALUES ('bgr', '   ', '   ', '  ', 'I', 'L', 'Bawm Chin', false, '');
INSERT INTO languages_iso639 VALUES ('bgs', '   ', '   ', '  ', 'I', 'L', 'Tagabawa', false, '');
INSERT INTO languages_iso639 VALUES ('bgt', '   ', '   ', '  ', 'I', 'L', 'Bughotu', false, '');
INSERT INTO languages_iso639 VALUES ('bgu', '   ', '   ', '  ', 'I', 'L', 'Mbongno', false, '');
INSERT INTO languages_iso639 VALUES ('bgv', '   ', '   ', '  ', 'I', 'L', 'Warkay-Bipim', false, '');
INSERT INTO languages_iso639 VALUES ('bgw', '   ', '   ', '  ', 'I', 'L', 'Bhatri', false, '');
INSERT INTO languages_iso639 VALUES ('bgx', '   ', '   ', '  ', 'I', 'L', 'Balkan Gagauz Turkish', false, '');
INSERT INTO languages_iso639 VALUES ('bgy', '   ', '   ', '  ', 'I', 'L', 'Benggoi', false, '');
INSERT INTO languages_iso639 VALUES ('bgz', '   ', '   ', '  ', 'I', 'L', 'Banggai', false, '');
INSERT INTO languages_iso639 VALUES ('bha', '   ', '   ', '  ', 'I', 'L', 'Bharia', false, '');
INSERT INTO languages_iso639 VALUES ('bhb', '   ', '   ', '  ', 'I', 'L', 'Bhili', false, '');
INSERT INTO languages_iso639 VALUES ('bhc', '   ', '   ', '  ', 'I', 'L', 'Biga', false, '');
INSERT INTO languages_iso639 VALUES ('bhd', '   ', '   ', '  ', 'I', 'L', 'Bhadrawahi', false, '');
INSERT INTO languages_iso639 VALUES ('bhe', '   ', '   ', '  ', 'I', 'L', 'Bhaya', false, '');
INSERT INTO languages_iso639 VALUES ('bhf', '   ', '   ', '  ', 'I', 'L', 'Odiai', false, '');
INSERT INTO languages_iso639 VALUES ('bhg', '   ', '   ', '  ', 'I', 'L', 'Binandere', false, '');
INSERT INTO languages_iso639 VALUES ('bhh', '   ', '   ', '  ', 'I', 'L', 'Bukharic', false, '');
INSERT INTO languages_iso639 VALUES ('bhi', '   ', '   ', '  ', 'I', 'L', 'Bhilali', false, '');
INSERT INTO languages_iso639 VALUES ('bhj', '   ', '   ', '  ', 'I', 'L', 'Bahing', false, '');
INSERT INTO languages_iso639 VALUES ('bhl', '   ', '   ', '  ', 'I', 'L', 'Bimin', false, '');
INSERT INTO languages_iso639 VALUES ('bhm', '   ', '   ', '  ', 'I', 'L', 'Bathari', false, '');
INSERT INTO languages_iso639 VALUES ('bhn', '   ', '   ', '  ', 'I', 'L', 'Bohtan Neo-Aramaic', false, '');
INSERT INTO languages_iso639 VALUES ('bho', 'bho', 'bho', '  ', 'I', 'L', 'Bhojpuri', false, '');
INSERT INTO languages_iso639 VALUES ('bhp', '   ', '   ', '  ', 'I', 'L', 'Bima', false, '');
INSERT INTO languages_iso639 VALUES ('bhq', '   ', '   ', '  ', 'I', 'L', 'Tukang Besi South', false, '');
INSERT INTO languages_iso639 VALUES ('bhr', '   ', '   ', '  ', 'I', 'L', 'Bara Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('bhs', '   ', '   ', '  ', 'I', 'L', 'Buwal', false, '');
INSERT INTO languages_iso639 VALUES ('bht', '   ', '   ', '  ', 'I', 'L', 'Bhattiyali', false, '');
INSERT INTO languages_iso639 VALUES ('bhu', '   ', '   ', '  ', 'I', 'L', 'Bhunjia', false, '');
INSERT INTO languages_iso639 VALUES ('bhv', '   ', '   ', '  ', 'I', 'L', 'Bahau', false, '');
INSERT INTO languages_iso639 VALUES ('bhw', '   ', '   ', '  ', 'I', 'L', 'Biak', false, '');
INSERT INTO languages_iso639 VALUES ('bhx', '   ', '   ', '  ', 'I', 'L', 'Bhalay', false, '');
INSERT INTO languages_iso639 VALUES ('bhy', '   ', '   ', '  ', 'I', 'L', 'Bhele', false, '');
INSERT INTO languages_iso639 VALUES ('bhz', '   ', '   ', '  ', 'I', 'L', 'Bada (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('bia', '   ', '   ', '  ', 'I', 'L', 'Badimaya', false, '');
INSERT INTO languages_iso639 VALUES ('bib', '   ', '   ', '  ', 'I', 'L', 'Bissa', false, '');
INSERT INTO languages_iso639 VALUES ('bic', '   ', '   ', '  ', 'I', 'L', 'Bikaru', false, '');
INSERT INTO languages_iso639 VALUES ('bid', '   ', '   ', '  ', 'I', 'L', 'Bidiyo', false, '');
INSERT INTO languages_iso639 VALUES ('bie', '   ', '   ', '  ', 'I', 'L', 'Bepour', false, '');
INSERT INTO languages_iso639 VALUES ('bif', '   ', '   ', '  ', 'I', 'L', 'Biafada', false, '');
INSERT INTO languages_iso639 VALUES ('big', '   ', '   ', '  ', 'I', 'L', 'Biangai', false, '');
INSERT INTO languages_iso639 VALUES ('bij', '   ', '   ', '  ', 'I', 'L', 'Vaghat-Ya-Bijim-Legeri', false, '');
INSERT INTO languages_iso639 VALUES ('bik', 'bik', 'bik', '  ', 'M', 'L', 'Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('bil', '   ', '   ', '  ', 'I', 'L', 'Bile', false, '');
INSERT INTO languages_iso639 VALUES ('bim', '   ', '   ', '  ', 'I', 'L', 'Bimoba', false, '');
INSERT INTO languages_iso639 VALUES ('bin', 'bin', 'bin', '  ', 'I', 'L', 'Bini', false, '');
INSERT INTO languages_iso639 VALUES ('bio', '   ', '   ', '  ', 'I', 'L', 'Nai', false, '');
INSERT INTO languages_iso639 VALUES ('bip', '   ', '   ', '  ', 'I', 'L', 'Bila', false, '');
INSERT INTO languages_iso639 VALUES ('biq', '   ', '   ', '  ', 'I', 'L', 'Bipi', false, '');
INSERT INTO languages_iso639 VALUES ('bir', '   ', '   ', '  ', 'I', 'L', 'Bisorio', false, '');
INSERT INTO languages_iso639 VALUES ('bis', 'bis', 'bis', 'bi', 'I', 'L', 'Bislama', false, '');
INSERT INTO languages_iso639 VALUES ('bit', '   ', '   ', '  ', 'I', 'L', 'Berinomo', false, '');
INSERT INTO languages_iso639 VALUES ('biu', '   ', '   ', '  ', 'I', 'L', 'Biete', false, '');
INSERT INTO languages_iso639 VALUES ('biv', '   ', '   ', '  ', 'I', 'L', 'Southern Birifor', false, '');
INSERT INTO languages_iso639 VALUES ('biw', '   ', '   ', '  ', 'I', 'L', 'Kol (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('bix', '   ', '   ', '  ', 'I', 'L', 'Bijori', false, '');
INSERT INTO languages_iso639 VALUES ('biy', '   ', '   ', '  ', 'I', 'L', 'Birhor', false, '');
INSERT INTO languages_iso639 VALUES ('biz', '   ', '   ', '  ', 'I', 'L', 'Baloi', false, '');
INSERT INTO languages_iso639 VALUES ('bja', '   ', '   ', '  ', 'I', 'L', 'Budza', false, '');
INSERT INTO languages_iso639 VALUES ('bjb', '   ', '   ', '  ', 'I', 'E', 'Banggarla', false, '');
INSERT INTO languages_iso639 VALUES ('bjc', '   ', '   ', '  ', 'I', 'L', 'Bariji', false, '');
INSERT INTO languages_iso639 VALUES ('bje', '   ', '   ', '  ', 'I', 'L', 'Biao-Jiao Mien', false, '');
INSERT INTO languages_iso639 VALUES ('bjf', '   ', '   ', '  ', 'I', 'L', 'Barzani Jewish Neo-Aramaic', false, '');
INSERT INTO languages_iso639 VALUES ('bjg', '   ', '   ', '  ', 'I', 'L', 'Bidyogo', false, '');
INSERT INTO languages_iso639 VALUES ('bjh', '   ', '   ', '  ', 'I', 'L', 'Bahinemo', false, '');
INSERT INTO languages_iso639 VALUES ('bji', '   ', '   ', '  ', 'I', 'L', 'Burji', false, '');
INSERT INTO languages_iso639 VALUES ('bjj', '   ', '   ', '  ', 'I', 'L', 'Kanauji', false, '');
INSERT INTO languages_iso639 VALUES ('bjk', '   ', '   ', '  ', 'I', 'L', 'Barok', false, '');
INSERT INTO languages_iso639 VALUES ('bjl', '   ', '   ', '  ', 'I', 'L', 'Bulu (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('bjm', '   ', '   ', '  ', 'I', 'L', 'Bajelani', false, '');
INSERT INTO languages_iso639 VALUES ('bjn', '   ', '   ', '  ', 'I', 'L', 'Banjar', false, '');
INSERT INTO languages_iso639 VALUES ('bjo', '   ', '   ', '  ', 'I', 'L', 'Mid-Southern Banda', false, '');
INSERT INTO languages_iso639 VALUES ('bjp', '   ', '   ', '  ', 'I', 'L', 'Fanamaket', false, '');
INSERT INTO languages_iso639 VALUES ('bjr', '   ', '   ', '  ', 'I', 'L', 'Binumarien', false, '');
INSERT INTO languages_iso639 VALUES ('bjs', '   ', '   ', '  ', 'I', 'L', 'Bajan', false, '');
INSERT INTO languages_iso639 VALUES ('bjt', '   ', '   ', '  ', 'I', 'L', 'Balanta-Ganja', false, '');
INSERT INTO languages_iso639 VALUES ('bju', '   ', '   ', '  ', 'I', 'L', 'Busuu', false, '');
INSERT INTO languages_iso639 VALUES ('bjv', '   ', '   ', '  ', 'I', 'L', 'Bedjond', false, '');
INSERT INTO languages_iso639 VALUES ('bjw', '   ', '   ', '  ', 'I', 'L', 'Bakwé', false, '');
INSERT INTO languages_iso639 VALUES ('bjx', '   ', '   ', '  ', 'I', 'L', 'Banao Itneg', false, '');
INSERT INTO languages_iso639 VALUES ('bjy', '   ', '   ', '  ', 'I', 'E', 'Bayali', false, '');
INSERT INTO languages_iso639 VALUES ('bjz', '   ', '   ', '  ', 'I', 'L', 'Baruga', false, '');
INSERT INTO languages_iso639 VALUES ('bka', '   ', '   ', '  ', 'I', 'L', 'Kyak', false, '');
INSERT INTO languages_iso639 VALUES ('bkc', '   ', '   ', '  ', 'I', 'L', 'Baka (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('bkd', '   ', '   ', '  ', 'I', 'L', 'Binukid', false, '');
INSERT INTO languages_iso639 VALUES ('bkf', '   ', '   ', '  ', 'I', 'L', 'Beeke', false, '');
INSERT INTO languages_iso639 VALUES ('bkg', '   ', '   ', '  ', 'I', 'L', 'Buraka', false, '');
INSERT INTO languages_iso639 VALUES ('bkh', '   ', '   ', '  ', 'I', 'L', 'Bakoko', false, '');
INSERT INTO languages_iso639 VALUES ('bki', '   ', '   ', '  ', 'I', 'L', 'Baki', false, '');
INSERT INTO languages_iso639 VALUES ('bkj', '   ', '   ', '  ', 'I', 'L', 'Pande', false, '');
INSERT INTO languages_iso639 VALUES ('bkk', '   ', '   ', '  ', 'I', 'L', 'Brokskat', false, '');
INSERT INTO languages_iso639 VALUES ('bkl', '   ', '   ', '  ', 'I', 'L', 'Berik', false, '');
INSERT INTO languages_iso639 VALUES ('bkm', '   ', '   ', '  ', 'I', 'L', 'Kom (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('bkn', '   ', '   ', '  ', 'I', 'L', 'Bukitan', false, '');
INSERT INTO languages_iso639 VALUES ('bko', '   ', '   ', '  ', 'I', 'L', 'Kwa''', false, '');
INSERT INTO languages_iso639 VALUES ('bkp', '   ', '   ', '  ', 'I', 'L', 'Boko (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('bkq', '   ', '   ', '  ', 'I', 'L', 'Bakairí', false, '');
INSERT INTO languages_iso639 VALUES ('bkr', '   ', '   ', '  ', 'I', 'L', 'Bakumpai', false, '');
INSERT INTO languages_iso639 VALUES ('bks', '   ', '   ', '  ', 'I', 'L', 'Northern Sorsoganon', false, '');
INSERT INTO languages_iso639 VALUES ('bkt', '   ', '   ', '  ', 'I', 'L', 'Boloki', false, '');
INSERT INTO languages_iso639 VALUES ('bku', '   ', '   ', '  ', 'I', 'L', 'Buhid', false, '');
INSERT INTO languages_iso639 VALUES ('bkv', '   ', '   ', '  ', 'I', 'L', 'Bekwarra', false, '');
INSERT INTO languages_iso639 VALUES ('bkw', '   ', '   ', '  ', 'I', 'L', 'Bekwel', false, '');
INSERT INTO languages_iso639 VALUES ('bkx', '   ', '   ', '  ', 'I', 'L', 'Baikeno', false, '');
INSERT INTO languages_iso639 VALUES ('bky', '   ', '   ', '  ', 'I', 'L', 'Bokyi', false, '');
INSERT INTO languages_iso639 VALUES ('bkz', '   ', '   ', '  ', 'I', 'L', 'Bungku', false, '');
INSERT INTO languages_iso639 VALUES ('bla', 'bla', 'bla', '  ', 'I', 'L', 'Siksika', false, '');
INSERT INTO languages_iso639 VALUES ('blb', '   ', '   ', '  ', 'I', 'L', 'Bilua', false, '');
INSERT INTO languages_iso639 VALUES ('blc', '   ', '   ', '  ', 'I', 'L', 'Bella Coola', false, '');
INSERT INTO languages_iso639 VALUES ('bld', '   ', '   ', '  ', 'I', 'L', 'Bolango', false, '');
INSERT INTO languages_iso639 VALUES ('ble', '   ', '   ', '  ', 'I', 'L', 'Balanta-Kentohe', false, '');
INSERT INTO languages_iso639 VALUES ('blf', '   ', '   ', '  ', 'I', 'L', 'Buol', false, '');
INSERT INTO languages_iso639 VALUES ('blg', '   ', '   ', '  ', 'I', 'L', 'Balau', false, '');
INSERT INTO languages_iso639 VALUES ('blh', '   ', '   ', '  ', 'I', 'L', 'Kuwaa', false, '');
INSERT INTO languages_iso639 VALUES ('bli', '   ', '   ', '  ', 'I', 'L', 'Bolia', false, '');
INSERT INTO languages_iso639 VALUES ('blj', '   ', '   ', '  ', 'I', 'L', 'Bolongan', false, '');
INSERT INTO languages_iso639 VALUES ('blk', '   ', '   ', '  ', 'I', 'L', 'Pa''o Karen', false, '');
INSERT INTO languages_iso639 VALUES ('bll', '   ', '   ', '  ', 'I', 'E', 'Biloxi', false, '');
INSERT INTO languages_iso639 VALUES ('blm', '   ', '   ', '  ', 'I', 'L', 'Beli (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('bln', '   ', '   ', '  ', 'I', 'L', 'Southern Catanduanes Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('blo', '   ', '   ', '  ', 'I', 'L', 'Anii', false, '');
INSERT INTO languages_iso639 VALUES ('blp', '   ', '   ', '  ', 'I', 'L', 'Blablanga', false, '');
INSERT INTO languages_iso639 VALUES ('blq', '   ', '   ', '  ', 'I', 'L', 'Baluan-Pam', false, '');
INSERT INTO languages_iso639 VALUES ('blr', '   ', '   ', '  ', 'I', 'L', 'Blang', false, '');
INSERT INTO languages_iso639 VALUES ('bls', '   ', '   ', '  ', 'I', 'L', 'Balaesang', false, '');
INSERT INTO languages_iso639 VALUES ('blt', '   ', '   ', '  ', 'I', 'L', 'Tai Dam', false, '');
INSERT INTO languages_iso639 VALUES ('blv', '   ', '   ', '  ', 'I', 'L', 'Bolo', false, '');
INSERT INTO languages_iso639 VALUES ('blw', '   ', '   ', '  ', 'I', 'L', 'Balangao', false, '');
INSERT INTO languages_iso639 VALUES ('blx', '   ', '   ', '  ', 'I', 'L', 'Mag-Indi Ayta', false, '');
INSERT INTO languages_iso639 VALUES ('bly', '   ', '   ', '  ', 'I', 'L', 'Notre', false, '');
INSERT INTO languages_iso639 VALUES ('blz', '   ', '   ', '  ', 'I', 'L', 'Balantak', false, '');
INSERT INTO languages_iso639 VALUES ('bma', '   ', '   ', '  ', 'I', 'L', 'Lame', false, '');
INSERT INTO languages_iso639 VALUES ('bmb', '   ', '   ', '  ', 'I', 'L', 'Bembe', false, '');
INSERT INTO languages_iso639 VALUES ('bmc', '   ', '   ', '  ', 'I', 'L', 'Biem', false, '');
INSERT INTO languages_iso639 VALUES ('bmd', '   ', '   ', '  ', 'I', 'L', 'Baga Manduri', false, '');
INSERT INTO languages_iso639 VALUES ('bme', '   ', '   ', '  ', 'I', 'L', 'Limassa', false, '');
INSERT INTO languages_iso639 VALUES ('bmf', '   ', '   ', '  ', 'I', 'L', 'Bom', false, '');
INSERT INTO languages_iso639 VALUES ('bmg', '   ', '   ', '  ', 'I', 'L', 'Bamwe', false, '');
INSERT INTO languages_iso639 VALUES ('bmh', '   ', '   ', '  ', 'I', 'L', 'Kein', false, '');
INSERT INTO languages_iso639 VALUES ('bmi', '   ', '   ', '  ', 'I', 'L', 'Bagirmi', false, '');
INSERT INTO languages_iso639 VALUES ('bmj', '   ', '   ', '  ', 'I', 'L', 'Bote-Majhi', false, '');
INSERT INTO languages_iso639 VALUES ('bmk', '   ', '   ', '  ', 'I', 'L', 'Ghayavi', false, '');
INSERT INTO languages_iso639 VALUES ('bml', '   ', '   ', '  ', 'I', 'L', 'Bomboli', false, '');
INSERT INTO languages_iso639 VALUES ('bmm', '   ', '   ', '  ', 'I', 'L', 'Northern Betsimisaraka Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('bmn', '   ', '   ', '  ', 'I', 'E', 'Bina (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('bmo', '   ', '   ', '  ', 'I', 'L', 'Bambalang', false, '');
INSERT INTO languages_iso639 VALUES ('bmp', '   ', '   ', '  ', 'I', 'L', 'Bulgebi', false, '');
INSERT INTO languages_iso639 VALUES ('bmq', '   ', '   ', '  ', 'I', 'L', 'Bomu', false, '');
INSERT INTO languages_iso639 VALUES ('bmr', '   ', '   ', '  ', 'I', 'L', 'Muinane', false, '');
INSERT INTO languages_iso639 VALUES ('bms', '   ', '   ', '  ', 'I', 'L', 'Bilma Kanuri', false, '');
INSERT INTO languages_iso639 VALUES ('bmt', '   ', '   ', '  ', 'I', 'L', 'Biao Mon', false, '');
INSERT INTO languages_iso639 VALUES ('bmu', '   ', '   ', '  ', 'I', 'L', 'Somba-Siawari', false, '');
INSERT INTO languages_iso639 VALUES ('bmv', '   ', '   ', '  ', 'I', 'L', 'Bum', false, '');
INSERT INTO languages_iso639 VALUES ('bmw', '   ', '   ', '  ', 'I', 'L', 'Bomwali', false, '');
INSERT INTO languages_iso639 VALUES ('bmx', '   ', '   ', '  ', 'I', 'L', 'Baimak', false, '');
INSERT INTO languages_iso639 VALUES ('bmy', '   ', '   ', '  ', 'I', 'L', 'Bemba (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('bmz', '   ', '   ', '  ', 'I', 'L', 'Baramu', false, '');
INSERT INTO languages_iso639 VALUES ('bna', '   ', '   ', '  ', 'I', 'L', 'Bonerate', false, '');
INSERT INTO languages_iso639 VALUES ('bnb', '   ', '   ', '  ', 'I', 'L', 'Bookan', false, '');
INSERT INTO languages_iso639 VALUES ('bnc', '   ', '   ', '  ', 'M', 'L', 'Bontok', false, '');
INSERT INTO languages_iso639 VALUES ('bnd', '   ', '   ', '  ', 'I', 'L', 'Banda (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('bne', '   ', '   ', '  ', 'I', 'L', 'Bintauna', false, '');
INSERT INTO languages_iso639 VALUES ('bnf', '   ', '   ', '  ', 'I', 'L', 'Masiwang', false, '');
INSERT INTO languages_iso639 VALUES ('bng', '   ', '   ', '  ', 'I', 'L', 'Benga', false, '');
INSERT INTO languages_iso639 VALUES ('bni', '   ', '   ', '  ', 'I', 'L', 'Bangi', false, '');
INSERT INTO languages_iso639 VALUES ('bnj', '   ', '   ', '  ', 'I', 'L', 'Eastern Tawbuid', false, '');
INSERT INTO languages_iso639 VALUES ('bnk', '   ', '   ', '  ', 'I', 'L', 'Bierebo', false, '');
INSERT INTO languages_iso639 VALUES ('bnl', '   ', '   ', '  ', 'I', 'L', 'Boon', false, '');
INSERT INTO languages_iso639 VALUES ('bnm', '   ', '   ', '  ', 'I', 'L', 'Batanga', false, '');
INSERT INTO languages_iso639 VALUES ('bnn', '   ', '   ', '  ', 'I', 'L', 'Bunun', false, '');
INSERT INTO languages_iso639 VALUES ('bno', '   ', '   ', '  ', 'I', 'L', 'Bantoanon', false, '');
INSERT INTO languages_iso639 VALUES ('bnp', '   ', '   ', '  ', 'I', 'L', 'Bola', false, '');
INSERT INTO languages_iso639 VALUES ('bnq', '   ', '   ', '  ', 'I', 'L', 'Bantik', false, '');
INSERT INTO languages_iso639 VALUES ('bnr', '   ', '   ', '  ', 'I', 'L', 'Butmas-Tur', false, '');
INSERT INTO languages_iso639 VALUES ('bns', '   ', '   ', '  ', 'I', 'L', 'Bundeli', false, '');
INSERT INTO languages_iso639 VALUES ('bnu', '   ', '   ', '  ', 'I', 'L', 'Bentong', false, '');
INSERT INTO languages_iso639 VALUES ('bnv', '   ', '   ', '  ', 'I', 'L', 'Bonerif', false, '');
INSERT INTO languages_iso639 VALUES ('bnw', '   ', '   ', '  ', 'I', 'L', 'Bisis', false, '');
INSERT INTO languages_iso639 VALUES ('bnx', '   ', '   ', '  ', 'I', 'L', 'Bangubangu', false, '');
INSERT INTO languages_iso639 VALUES ('bny', '   ', '   ', '  ', 'I', 'L', 'Bintulu', false, '');
INSERT INTO languages_iso639 VALUES ('bnz', '   ', '   ', '  ', 'I', 'L', 'Beezen', false, '');
INSERT INTO languages_iso639 VALUES ('boa', '   ', '   ', '  ', 'I', 'L', 'Bora', false, '');
INSERT INTO languages_iso639 VALUES ('bob', '   ', '   ', '  ', 'I', 'L', 'Aweer', false, '');
INSERT INTO languages_iso639 VALUES ('bod', 'tib', 'bod', 'bo', 'I', 'L', 'Tibetan', false, '');
INSERT INTO languages_iso639 VALUES ('boe', '   ', '   ', '  ', 'I', 'L', 'Mundabli', false, '');
INSERT INTO languages_iso639 VALUES ('bof', '   ', '   ', '  ', 'I', 'L', 'Bolon', false, '');
INSERT INTO languages_iso639 VALUES ('bog', '   ', '   ', '  ', 'I', 'L', 'Bamako Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('boh', '   ', '   ', '  ', 'I', 'L', 'Boma', false, '');
INSERT INTO languages_iso639 VALUES ('boi', '   ', '   ', '  ', 'I', 'E', 'Barbareño', false, '');
INSERT INTO languages_iso639 VALUES ('boj', '   ', '   ', '  ', 'I', 'L', 'Anjam', false, '');
INSERT INTO languages_iso639 VALUES ('bok', '   ', '   ', '  ', 'I', 'L', 'Bonjo', false, '');
INSERT INTO languages_iso639 VALUES ('bol', '   ', '   ', '  ', 'I', 'L', 'Bole', false, '');
INSERT INTO languages_iso639 VALUES ('bom', '   ', '   ', '  ', 'I', 'L', 'Berom', false, '');
INSERT INTO languages_iso639 VALUES ('bon', '   ', '   ', '  ', 'I', 'L', 'Bine', false, '');
INSERT INTO languages_iso639 VALUES ('boo', '   ', '   ', '  ', 'I', 'L', 'Tiemacèwè Bozo', false, '');
INSERT INTO languages_iso639 VALUES ('bop', '   ', '   ', '  ', 'I', 'L', 'Bonkiman', false, '');
INSERT INTO languages_iso639 VALUES ('boq', '   ', '   ', '  ', 'I', 'L', 'Bogaya', false, '');
INSERT INTO languages_iso639 VALUES ('bor', '   ', '   ', '  ', 'I', 'L', 'Borôro', false, '');
INSERT INTO languages_iso639 VALUES ('bos', 'bos', 'bos', 'bs', 'I', 'L', 'Bosnian', false, '');
INSERT INTO languages_iso639 VALUES ('bot', '   ', '   ', '  ', 'I', 'L', 'Bongo', false, '');
INSERT INTO languages_iso639 VALUES ('bou', '   ', '   ', '  ', 'I', 'L', 'Bondei', false, '');
INSERT INTO languages_iso639 VALUES ('bov', '   ', '   ', '  ', 'I', 'L', 'Tuwuli', false, '');
INSERT INTO languages_iso639 VALUES ('bow', '   ', '   ', '  ', 'I', 'E', 'Rema', false, '');
INSERT INTO languages_iso639 VALUES ('box', '   ', '   ', '  ', 'I', 'L', 'Buamu', false, '');
INSERT INTO languages_iso639 VALUES ('boy', '   ', '   ', '  ', 'I', 'L', 'Bodo (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('boz', '   ', '   ', '  ', 'I', 'L', 'Tiéyaxo Bozo', false, '');
INSERT INTO languages_iso639 VALUES ('bpa', '   ', '   ', '  ', 'I', 'L', 'Daakaka', false, '');
INSERT INTO languages_iso639 VALUES ('bpb', '   ', '   ', '  ', 'I', 'E', 'Barbacoas', false, '');
INSERT INTO languages_iso639 VALUES ('bpd', '   ', '   ', '  ', 'I', 'L', 'Banda-Banda', false, '');
INSERT INTO languages_iso639 VALUES ('bpg', '   ', '   ', '  ', 'I', 'L', 'Bonggo', false, '');
INSERT INTO languages_iso639 VALUES ('bph', '   ', '   ', '  ', 'I', 'L', 'Botlikh', false, '');
INSERT INTO languages_iso639 VALUES ('bpi', '   ', '   ', '  ', 'I', 'L', 'Bagupi', false, '');
INSERT INTO languages_iso639 VALUES ('bpj', '   ', '   ', '  ', 'I', 'L', 'Binji', false, '');
INSERT INTO languages_iso639 VALUES ('bpk', '   ', '   ', '  ', 'I', 'L', 'Orowe', false, '');
INSERT INTO languages_iso639 VALUES ('bpl', '   ', '   ', '  ', 'I', 'L', 'Broome Pearling Lugger Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('bpm', '   ', '   ', '  ', 'I', 'L', 'Biyom', false, '');
INSERT INTO languages_iso639 VALUES ('bpn', '   ', '   ', '  ', 'I', 'L', 'Dzao Min', false, '');
INSERT INTO languages_iso639 VALUES ('bpo', '   ', '   ', '  ', 'I', 'L', 'Anasi', false, '');
INSERT INTO languages_iso639 VALUES ('bpp', '   ', '   ', '  ', 'I', 'L', 'Kaure', false, '');
INSERT INTO languages_iso639 VALUES ('bpq', '   ', '   ', '  ', 'I', 'L', 'Banda Malay', false, '');
INSERT INTO languages_iso639 VALUES ('bpr', '   ', '   ', '  ', 'I', 'L', 'Koronadal Blaan', false, '');
INSERT INTO languages_iso639 VALUES ('bps', '   ', '   ', '  ', 'I', 'L', 'Sarangani Blaan', false, '');
INSERT INTO languages_iso639 VALUES ('bpt', '   ', '   ', '  ', 'I', 'E', 'Barrow Point', false, '');
INSERT INTO languages_iso639 VALUES ('bpu', '   ', '   ', '  ', 'I', 'L', 'Bongu', false, '');
INSERT INTO languages_iso639 VALUES ('bpv', '   ', '   ', '  ', 'I', 'L', 'Bian Marind', false, '');
INSERT INTO languages_iso639 VALUES ('bpw', '   ', '   ', '  ', 'I', 'L', 'Bo (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('bpx', '   ', '   ', '  ', 'I', 'L', 'Palya Bareli', false, '');
INSERT INTO languages_iso639 VALUES ('bpy', '   ', '   ', '  ', 'I', 'L', 'Bishnupriya', false, '');
INSERT INTO languages_iso639 VALUES ('bpz', '   ', '   ', '  ', 'I', 'L', 'Bilba', false, '');
INSERT INTO languages_iso639 VALUES ('bqa', '   ', '   ', '  ', 'I', 'L', 'Tchumbuli', false, '');
INSERT INTO languages_iso639 VALUES ('bqb', '   ', '   ', '  ', 'I', 'L', 'Bagusa', false, '');
INSERT INTO languages_iso639 VALUES ('bqc', '   ', '   ', '  ', 'I', 'L', 'Boko (Benin)', false, '');
INSERT INTO languages_iso639 VALUES ('bqd', '   ', '   ', '  ', 'I', 'L', 'Bung', false, '');
INSERT INTO languages_iso639 VALUES ('bqf', '   ', '   ', '  ', 'I', 'E', 'Baga Kaloum', false, '');
INSERT INTO languages_iso639 VALUES ('bqg', '   ', '   ', '  ', 'I', 'L', 'Bago-Kusuntu', false, '');
INSERT INTO languages_iso639 VALUES ('bqh', '   ', '   ', '  ', 'I', 'L', 'Baima', false, '');
INSERT INTO languages_iso639 VALUES ('bqi', '   ', '   ', '  ', 'I', 'L', 'Bakhtiari', false, '');
INSERT INTO languages_iso639 VALUES ('bqj', '   ', '   ', '  ', 'I', 'L', 'Bandial', false, '');
INSERT INTO languages_iso639 VALUES ('bqk', '   ', '   ', '  ', 'I', 'L', 'Banda-Mbrès', false, '');
INSERT INTO languages_iso639 VALUES ('bql', '   ', '   ', '  ', 'I', 'L', 'Bilakura', false, '');
INSERT INTO languages_iso639 VALUES ('bqm', '   ', '   ', '  ', 'I', 'L', 'Wumboko', false, '');
INSERT INTO languages_iso639 VALUES ('bqn', '   ', '   ', '  ', 'I', 'L', 'Bulgarian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('bqo', '   ', '   ', '  ', 'I', 'L', 'Balo', false, '');
INSERT INTO languages_iso639 VALUES ('bqp', '   ', '   ', '  ', 'I', 'L', 'Busa', false, '');
INSERT INTO languages_iso639 VALUES ('bqq', '   ', '   ', '  ', 'I', 'L', 'Biritai', false, '');
INSERT INTO languages_iso639 VALUES ('bqr', '   ', '   ', '  ', 'I', 'L', 'Burusu', false, '');
INSERT INTO languages_iso639 VALUES ('bqs', '   ', '   ', '  ', 'I', 'L', 'Bosngun', false, '');
INSERT INTO languages_iso639 VALUES ('bqt', '   ', '   ', '  ', 'I', 'L', 'Bamukumbit', false, '');
INSERT INTO languages_iso639 VALUES ('bqu', '   ', '   ', '  ', 'I', 'L', 'Boguru', false, '');
INSERT INTO languages_iso639 VALUES ('bqv', '   ', '   ', '  ', 'I', 'L', 'Koro Wachi', false, '');
INSERT INTO languages_iso639 VALUES ('bqw', '   ', '   ', '  ', 'I', 'L', 'Buru (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('bqx', '   ', '   ', '  ', 'I', 'L', 'Baangi', false, '');
INSERT INTO languages_iso639 VALUES ('bqy', '   ', '   ', '  ', 'I', 'L', 'Bengkala Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('bqz', '   ', '   ', '  ', 'I', 'L', 'Bakaka', false, '');
INSERT INTO languages_iso639 VALUES ('bra', 'bra', 'bra', '  ', 'I', 'L', 'Braj', false, '');
INSERT INTO languages_iso639 VALUES ('brb', '   ', '   ', '  ', 'I', 'L', 'Lave', false, '');
INSERT INTO languages_iso639 VALUES ('brc', '   ', '   ', '  ', 'I', 'E', 'Berbice Creole Dutch', false, '');
INSERT INTO languages_iso639 VALUES ('brd', '   ', '   ', '  ', 'I', 'L', 'Baraamu', false, '');
INSERT INTO languages_iso639 VALUES ('bre', 'bre', 'bre', 'br', 'I', 'L', 'Breton', false, '');
INSERT INTO languages_iso639 VALUES ('brf', '   ', '   ', '  ', 'I', 'L', 'Bera', false, '');
INSERT INTO languages_iso639 VALUES ('brg', '   ', '   ', '  ', 'I', 'L', 'Baure', false, '');
INSERT INTO languages_iso639 VALUES ('brh', '   ', '   ', '  ', 'I', 'L', 'Brahui', false, '');
INSERT INTO languages_iso639 VALUES ('bri', '   ', '   ', '  ', 'I', 'L', 'Mokpwe', false, '');
INSERT INTO languages_iso639 VALUES ('brj', '   ', '   ', '  ', 'I', 'L', 'Bieria', false, '');
INSERT INTO languages_iso639 VALUES ('brk', '   ', '   ', '  ', 'I', 'E', 'Birked', false, '');
INSERT INTO languages_iso639 VALUES ('brl', '   ', '   ', '  ', 'I', 'L', 'Birwa', false, '');
INSERT INTO languages_iso639 VALUES ('brm', '   ', '   ', '  ', 'I', 'L', 'Barambu', false, '');
INSERT INTO languages_iso639 VALUES ('brn', '   ', '   ', '  ', 'I', 'L', 'Boruca', false, '');
INSERT INTO languages_iso639 VALUES ('bro', '   ', '   ', '  ', 'I', 'L', 'Brokkat', false, '');
INSERT INTO languages_iso639 VALUES ('brp', '   ', '   ', '  ', 'I', 'L', 'Barapasi', false, '');
INSERT INTO languages_iso639 VALUES ('brq', '   ', '   ', '  ', 'I', 'L', 'Breri', false, '');
INSERT INTO languages_iso639 VALUES ('brr', '   ', '   ', '  ', 'I', 'L', 'Birao', false, '');
INSERT INTO languages_iso639 VALUES ('brs', '   ', '   ', '  ', 'I', 'L', 'Baras', false, '');
INSERT INTO languages_iso639 VALUES ('brt', '   ', '   ', '  ', 'I', 'L', 'Bitare', false, '');
INSERT INTO languages_iso639 VALUES ('bru', '   ', '   ', '  ', 'I', 'L', 'Eastern Bru', false, '');
INSERT INTO languages_iso639 VALUES ('brv', '   ', '   ', '  ', 'I', 'L', 'Western Bru', false, '');
INSERT INTO languages_iso639 VALUES ('brw', '   ', '   ', '  ', 'I', 'L', 'Bellari', false, '');
INSERT INTO languages_iso639 VALUES ('brx', '   ', '   ', '  ', 'I', 'L', 'Bodo (India)', false, '');
INSERT INTO languages_iso639 VALUES ('bry', '   ', '   ', '  ', 'I', 'L', 'Burui', false, '');
INSERT INTO languages_iso639 VALUES ('brz', '   ', '   ', '  ', 'I', 'L', 'Bilbil', false, '');
INSERT INTO languages_iso639 VALUES ('bsa', '   ', '   ', '  ', 'I', 'L', 'Abinomn', false, '');
INSERT INTO languages_iso639 VALUES ('bsb', '   ', '   ', '  ', 'I', 'L', 'Brunei Bisaya', false, '');
INSERT INTO languages_iso639 VALUES ('bsc', '   ', '   ', '  ', 'I', 'L', 'Bassari', false, '');
INSERT INTO languages_iso639 VALUES ('bse', '   ', '   ', '  ', 'I', 'L', 'Wushi', false, '');
INSERT INTO languages_iso639 VALUES ('bsf', '   ', '   ', '  ', 'I', 'L', 'Bauchi', false, '');
INSERT INTO languages_iso639 VALUES ('bsg', '   ', '   ', '  ', 'I', 'L', 'Bashkardi', false, '');
INSERT INTO languages_iso639 VALUES ('bsh', '   ', '   ', '  ', 'I', 'L', 'Kati', false, '');
INSERT INTO languages_iso639 VALUES ('bsi', '   ', '   ', '  ', 'I', 'L', 'Bassossi', false, '');
INSERT INTO languages_iso639 VALUES ('bsj', '   ', '   ', '  ', 'I', 'L', 'Bangwinji', false, '');
INSERT INTO languages_iso639 VALUES ('bsk', '   ', '   ', '  ', 'I', 'L', 'Burushaski', false, '');
INSERT INTO languages_iso639 VALUES ('bsl', '   ', '   ', '  ', 'I', 'E', 'Basa-Gumna', false, '');
INSERT INTO languages_iso639 VALUES ('bsm', '   ', '   ', '  ', 'I', 'L', 'Busami', false, '');
INSERT INTO languages_iso639 VALUES ('bsn', '   ', '   ', '  ', 'I', 'L', 'Barasana-Eduria', false, '');
INSERT INTO languages_iso639 VALUES ('bso', '   ', '   ', '  ', 'I', 'L', 'Buso', false, '');
INSERT INTO languages_iso639 VALUES ('bsp', '   ', '   ', '  ', 'I', 'L', 'Baga Sitemu', false, '');
INSERT INTO languages_iso639 VALUES ('bsq', '   ', '   ', '  ', 'I', 'L', 'Bassa', false, '');
INSERT INTO languages_iso639 VALUES ('bsr', '   ', '   ', '  ', 'I', 'L', 'Bassa-Kontagora', false, '');
INSERT INTO languages_iso639 VALUES ('bss', '   ', '   ', '  ', 'I', 'L', 'Akoose', false, '');
INSERT INTO languages_iso639 VALUES ('bst', '   ', '   ', '  ', 'I', 'L', 'Basketo', false, '');
INSERT INTO languages_iso639 VALUES ('bsu', '   ', '   ', '  ', 'I', 'L', 'Bahonsuai', false, '');
INSERT INTO languages_iso639 VALUES ('bsv', '   ', '   ', '  ', 'I', 'E', 'Baga Sobané', false, '');
INSERT INTO languages_iso639 VALUES ('bsw', '   ', '   ', '  ', 'I', 'L', 'Baiso', false, '');
INSERT INTO languages_iso639 VALUES ('bsx', '   ', '   ', '  ', 'I', 'L', 'Yangkam', false, '');
INSERT INTO languages_iso639 VALUES ('bsy', '   ', '   ', '  ', 'I', 'L', 'Sabah Bisaya', false, '');
INSERT INTO languages_iso639 VALUES ('bta', '   ', '   ', '  ', 'I', 'L', 'Bata', false, '');
INSERT INTO languages_iso639 VALUES ('btc', '   ', '   ', '  ', 'I', 'L', 'Bati (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('btd', '   ', '   ', '  ', 'I', 'L', 'Batak Dairi', false, '');
INSERT INTO languages_iso639 VALUES ('bte', '   ', '   ', '  ', 'I', 'E', 'Gamo-Ningi', false, '');
INSERT INTO languages_iso639 VALUES ('btf', '   ', '   ', '  ', 'I', 'L', 'Birgit', false, '');
INSERT INTO languages_iso639 VALUES ('btg', '   ', '   ', '  ', 'I', 'L', 'Gagnoa Bété', false, '');
INSERT INTO languages_iso639 VALUES ('bth', '   ', '   ', '  ', 'I', 'L', 'Biatah Bidayuh', false, '');
INSERT INTO languages_iso639 VALUES ('bti', '   ', '   ', '  ', 'I', 'L', 'Burate', false, '');
INSERT INTO languages_iso639 VALUES ('btj', '   ', '   ', '  ', 'I', 'L', 'Bacanese Malay', false, '');
INSERT INTO languages_iso639 VALUES ('btl', '   ', '   ', '  ', 'I', 'L', 'Bhatola', false, '');
INSERT INTO languages_iso639 VALUES ('btm', '   ', '   ', '  ', 'I', 'L', 'Batak Mandailing', false, '');
INSERT INTO languages_iso639 VALUES ('btn', '   ', '   ', '  ', 'I', 'L', 'Ratagnon', false, '');
INSERT INTO languages_iso639 VALUES ('bto', '   ', '   ', '  ', 'I', 'L', 'Rinconada Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('btp', '   ', '   ', '  ', 'I', 'L', 'Budibud', false, '');
INSERT INTO languages_iso639 VALUES ('btq', '   ', '   ', '  ', 'I', 'L', 'Batek', false, '');
INSERT INTO languages_iso639 VALUES ('btr', '   ', '   ', '  ', 'I', 'L', 'Baetora', false, '');
INSERT INTO languages_iso639 VALUES ('bts', '   ', '   ', '  ', 'I', 'L', 'Batak Simalungun', false, '');
INSERT INTO languages_iso639 VALUES ('btt', '   ', '   ', '  ', 'I', 'L', 'Bete-Bendi', false, '');
INSERT INTO languages_iso639 VALUES ('btu', '   ', '   ', '  ', 'I', 'L', 'Batu', false, '');
INSERT INTO languages_iso639 VALUES ('btv', '   ', '   ', '  ', 'I', 'L', 'Bateri', false, '');
INSERT INTO languages_iso639 VALUES ('btw', '   ', '   ', '  ', 'I', 'L', 'Butuanon', false, '');
INSERT INTO languages_iso639 VALUES ('btx', '   ', '   ', '  ', 'I', 'L', 'Batak Karo', false, '');
INSERT INTO languages_iso639 VALUES ('bty', '   ', '   ', '  ', 'I', 'L', 'Bobot', false, '');
INSERT INTO languages_iso639 VALUES ('btz', '   ', '   ', '  ', 'I', 'L', 'Batak Alas-Kluet', false, '');
INSERT INTO languages_iso639 VALUES ('bua', 'bua', 'bua', '  ', 'M', 'L', 'Buriat', false, '');
INSERT INTO languages_iso639 VALUES ('bub', '   ', '   ', '  ', 'I', 'L', 'Bua', false, '');
INSERT INTO languages_iso639 VALUES ('buc', '   ', '   ', '  ', 'I', 'L', 'Bushi', false, '');
INSERT INTO languages_iso639 VALUES ('bud', '   ', '   ', '  ', 'I', 'L', 'Ntcham', false, '');
INSERT INTO languages_iso639 VALUES ('bue', '   ', '   ', '  ', 'I', 'E', 'Beothuk', false, '');
INSERT INTO languages_iso639 VALUES ('buf', '   ', '   ', '  ', 'I', 'L', 'Bushoong', false, '');
INSERT INTO languages_iso639 VALUES ('bug', 'bug', 'bug', '  ', 'I', 'L', 'Buginese', false, '');
INSERT INTO languages_iso639 VALUES ('buh', '   ', '   ', '  ', 'I', 'L', 'Younuo Bunu', false, '');
INSERT INTO languages_iso639 VALUES ('bui', '   ', '   ', '  ', 'I', 'L', 'Bongili', false, '');
INSERT INTO languages_iso639 VALUES ('buj', '   ', '   ', '  ', 'I', 'L', 'Basa-Gurmana', false, '');
INSERT INTO languages_iso639 VALUES ('buk', '   ', '   ', '  ', 'I', 'L', 'Bugawac', false, '');
INSERT INTO languages_iso639 VALUES ('bul', 'bul', 'bul', 'bg', 'I', 'L', 'Bulgarian', false, '');
INSERT INTO languages_iso639 VALUES ('bum', '   ', '   ', '  ', 'I', 'L', 'Bulu (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('bun', '   ', '   ', '  ', 'I', 'L', 'Sherbro', false, '');
INSERT INTO languages_iso639 VALUES ('buo', '   ', '   ', '  ', 'I', 'L', 'Terei', false, '');
INSERT INTO languages_iso639 VALUES ('bup', '   ', '   ', '  ', 'I', 'L', 'Busoa', false, '');
INSERT INTO languages_iso639 VALUES ('buq', '   ', '   ', '  ', 'I', 'L', 'Brem', false, '');
INSERT INTO languages_iso639 VALUES ('bus', '   ', '   ', '  ', 'I', 'L', 'Bokobaru', false, '');
INSERT INTO languages_iso639 VALUES ('but', '   ', '   ', '  ', 'I', 'L', 'Bungain', false, '');
INSERT INTO languages_iso639 VALUES ('buu', '   ', '   ', '  ', 'I', 'L', 'Budu', false, '');
INSERT INTO languages_iso639 VALUES ('buv', '   ', '   ', '  ', 'I', 'L', 'Bun', false, '');
INSERT INTO languages_iso639 VALUES ('buw', '   ', '   ', '  ', 'I', 'L', 'Bubi', false, '');
INSERT INTO languages_iso639 VALUES ('bux', '   ', '   ', '  ', 'I', 'L', 'Boghom', false, '');
INSERT INTO languages_iso639 VALUES ('buy', '   ', '   ', '  ', 'I', 'L', 'Bullom So', false, '');
INSERT INTO languages_iso639 VALUES ('buz', '   ', '   ', '  ', 'I', 'L', 'Bukwen', false, '');
INSERT INTO languages_iso639 VALUES ('bva', '   ', '   ', '  ', 'I', 'L', 'Barein', false, '');
INSERT INTO languages_iso639 VALUES ('bvb', '   ', '   ', '  ', 'I', 'L', 'Bube', false, '');
INSERT INTO languages_iso639 VALUES ('bvc', '   ', '   ', '  ', 'I', 'L', 'Baelelea', false, '');
INSERT INTO languages_iso639 VALUES ('bvd', '   ', '   ', '  ', 'I', 'L', 'Baeggu', false, '');
INSERT INTO languages_iso639 VALUES ('bve', '   ', '   ', '  ', 'I', 'L', 'Berau Malay', false, '');
INSERT INTO languages_iso639 VALUES ('bvf', '   ', '   ', '  ', 'I', 'L', 'Boor', false, '');
INSERT INTO languages_iso639 VALUES ('bvg', '   ', '   ', '  ', 'I', 'L', 'Bonkeng', false, '');
INSERT INTO languages_iso639 VALUES ('bvh', '   ', '   ', '  ', 'I', 'L', 'Bure', false, '');
INSERT INTO languages_iso639 VALUES ('bvi', '   ', '   ', '  ', 'I', 'L', 'Belanda Viri', false, '');
INSERT INTO languages_iso639 VALUES ('bvj', '   ', '   ', '  ', 'I', 'L', 'Baan', false, '');
INSERT INTO languages_iso639 VALUES ('bvk', '   ', '   ', '  ', 'I', 'L', 'Bukat', false, '');
INSERT INTO languages_iso639 VALUES ('bvl', '   ', '   ', '  ', 'I', 'L', 'Bolivian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('bvm', '   ', '   ', '  ', 'I', 'L', 'Bamunka', false, '');
INSERT INTO languages_iso639 VALUES ('bvn', '   ', '   ', '  ', 'I', 'L', 'Buna', false, '');
INSERT INTO languages_iso639 VALUES ('bvo', '   ', '   ', '  ', 'I', 'L', 'Bolgo', false, '');
INSERT INTO languages_iso639 VALUES ('bvp', '   ', '   ', '  ', 'I', 'L', 'Bumang', false, '');
INSERT INTO languages_iso639 VALUES ('bvq', '   ', '   ', '  ', 'I', 'L', 'Birri', false, '');
INSERT INTO languages_iso639 VALUES ('bvr', '   ', '   ', '  ', 'I', 'L', 'Burarra', false, '');
INSERT INTO languages_iso639 VALUES ('bvt', '   ', '   ', '  ', 'I', 'L', 'Bati (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('bvu', '   ', '   ', '  ', 'I', 'L', 'Bukit Malay', false, '');
INSERT INTO languages_iso639 VALUES ('bvv', '   ', '   ', '  ', 'I', 'E', 'Baniva', false, '');
INSERT INTO languages_iso639 VALUES ('bvw', '   ', '   ', '  ', 'I', 'L', 'Boga', false, '');
INSERT INTO languages_iso639 VALUES ('bvx', '   ', '   ', '  ', 'I', 'L', 'Dibole', false, '');
INSERT INTO languages_iso639 VALUES ('bvy', '   ', '   ', '  ', 'I', 'L', 'Baybayanon', false, '');
INSERT INTO languages_iso639 VALUES ('bvz', '   ', '   ', '  ', 'I', 'L', 'Bauzi', false, '');
INSERT INTO languages_iso639 VALUES ('bwa', '   ', '   ', '  ', 'I', 'L', 'Bwatoo', false, '');
INSERT INTO languages_iso639 VALUES ('bwb', '   ', '   ', '  ', 'I', 'L', 'Namosi-Naitasiri-Serua', false, '');
INSERT INTO languages_iso639 VALUES ('bwc', '   ', '   ', '  ', 'I', 'L', 'Bwile', false, '');
INSERT INTO languages_iso639 VALUES ('bwd', '   ', '   ', '  ', 'I', 'L', 'Bwaidoka', false, '');
INSERT INTO languages_iso639 VALUES ('bwe', '   ', '   ', '  ', 'I', 'L', 'Bwe Karen', false, '');
INSERT INTO languages_iso639 VALUES ('bwf', '   ', '   ', '  ', 'I', 'L', 'Boselewa', false, '');
INSERT INTO languages_iso639 VALUES ('bwg', '   ', '   ', '  ', 'I', 'L', 'Barwe', false, '');
INSERT INTO languages_iso639 VALUES ('bwh', '   ', '   ', '  ', 'I', 'L', 'Bishuo', false, '');
INSERT INTO languages_iso639 VALUES ('bwi', '   ', '   ', '  ', 'I', 'L', 'Baniwa', false, '');
INSERT INTO languages_iso639 VALUES ('bwj', '   ', '   ', '  ', 'I', 'L', 'Láá Láá Bwamu', false, '');
INSERT INTO languages_iso639 VALUES ('bwk', '   ', '   ', '  ', 'I', 'L', 'Bauwaki', false, '');
INSERT INTO languages_iso639 VALUES ('bwl', '   ', '   ', '  ', 'I', 'L', 'Bwela', false, '');
INSERT INTO languages_iso639 VALUES ('bwm', '   ', '   ', '  ', 'I', 'L', 'Biwat', false, '');
INSERT INTO languages_iso639 VALUES ('bwn', '   ', '   ', '  ', 'I', 'L', 'Wunai Bunu', false, '');
INSERT INTO languages_iso639 VALUES ('bwo', '   ', '   ', '  ', 'I', 'L', 'Boro (Ethiopia)', false, '');
INSERT INTO languages_iso639 VALUES ('bwp', '   ', '   ', '  ', 'I', 'L', 'Mandobo Bawah', false, '');
INSERT INTO languages_iso639 VALUES ('bwq', '   ', '   ', '  ', 'I', 'L', 'Southern Bobo Madaré', false, '');
INSERT INTO languages_iso639 VALUES ('bwr', '   ', '   ', '  ', 'I', 'L', 'Bura-Pabir', false, '');
INSERT INTO languages_iso639 VALUES ('bws', '   ', '   ', '  ', 'I', 'L', 'Bomboma', false, '');
INSERT INTO languages_iso639 VALUES ('bwt', '   ', '   ', '  ', 'I', 'L', 'Bafaw-Balong', false, '');
INSERT INTO languages_iso639 VALUES ('bwu', '   ', '   ', '  ', 'I', 'L', 'Buli (Ghana)', false, '');
INSERT INTO languages_iso639 VALUES ('bww', '   ', '   ', '  ', 'I', 'L', 'Bwa', false, '');
INSERT INTO languages_iso639 VALUES ('bwx', '   ', '   ', '  ', 'I', 'L', 'Bu-Nao Bunu', false, '');
INSERT INTO languages_iso639 VALUES ('bwy', '   ', '   ', '  ', 'I', 'L', 'Cwi Bwamu', false, '');
INSERT INTO languages_iso639 VALUES ('bwz', '   ', '   ', '  ', 'I', 'L', 'Bwisi', false, '');
INSERT INTO languages_iso639 VALUES ('bxa', '   ', '   ', '  ', 'I', 'L', 'Tairaha', false, '');
INSERT INTO languages_iso639 VALUES ('bxb', '   ', '   ', '  ', 'I', 'L', 'Belanda Bor', false, '');
INSERT INTO languages_iso639 VALUES ('bxc', '   ', '   ', '  ', 'I', 'L', 'Molengue', false, '');
INSERT INTO languages_iso639 VALUES ('bxd', '   ', '   ', '  ', 'I', 'L', 'Pela', false, '');
INSERT INTO languages_iso639 VALUES ('bxe', '   ', '   ', '  ', 'I', 'L', 'Birale', false, '');
INSERT INTO languages_iso639 VALUES ('bxf', '   ', '   ', '  ', 'I', 'L', 'Bilur', false, '');
INSERT INTO languages_iso639 VALUES ('bxg', '   ', '   ', '  ', 'I', 'L', 'Bangala', false, '');
INSERT INTO languages_iso639 VALUES ('bxh', '   ', '   ', '  ', 'I', 'L', 'Buhutu', false, '');
INSERT INTO languages_iso639 VALUES ('bxi', '   ', '   ', '  ', 'I', 'E', 'Pirlatapa', false, '');
INSERT INTO languages_iso639 VALUES ('bxj', '   ', '   ', '  ', 'I', 'L', 'Bayungu', false, '');
INSERT INTO languages_iso639 VALUES ('bxk', '   ', '   ', '  ', 'I', 'L', 'Bukusu', false, '');
INSERT INTO languages_iso639 VALUES ('bxl', '   ', '   ', '  ', 'I', 'L', 'Jalkunan', false, '');
INSERT INTO languages_iso639 VALUES ('bxm', '   ', '   ', '  ', 'I', 'L', 'Mongolia Buriat', false, '');
INSERT INTO languages_iso639 VALUES ('bxn', '   ', '   ', '  ', 'I', 'L', 'Burduna', false, '');
INSERT INTO languages_iso639 VALUES ('bxo', '   ', '   ', '  ', 'I', 'L', 'Barikanchi', false, '');
INSERT INTO languages_iso639 VALUES ('bxp', '   ', '   ', '  ', 'I', 'L', 'Bebil', false, '');
INSERT INTO languages_iso639 VALUES ('bxq', '   ', '   ', '  ', 'I', 'L', 'Beele', false, '');
INSERT INTO languages_iso639 VALUES ('bxr', '   ', '   ', '  ', 'I', 'L', 'Russia Buriat', false, '');
INSERT INTO languages_iso639 VALUES ('bxs', '   ', '   ', '  ', 'I', 'L', 'Busam', false, '');
INSERT INTO languages_iso639 VALUES ('bxu', '   ', '   ', '  ', 'I', 'L', 'China Buriat', false, '');
INSERT INTO languages_iso639 VALUES ('bxv', '   ', '   ', '  ', 'I', 'L', 'Berakou', false, '');
INSERT INTO languages_iso639 VALUES ('bxw', '   ', '   ', '  ', 'I', 'L', 'Bankagooma', false, '');
INSERT INTO languages_iso639 VALUES ('bxx', '   ', '   ', '  ', 'I', 'L', 'Borna (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('bxz', '   ', '   ', '  ', 'I', 'L', 'Binahari', false, '');
INSERT INTO languages_iso639 VALUES ('bya', '   ', '   ', '  ', 'I', 'L', 'Batak', false, '');
INSERT INTO languages_iso639 VALUES ('byb', '   ', '   ', '  ', 'I', 'L', 'Bikya', false, '');
INSERT INTO languages_iso639 VALUES ('byc', '   ', '   ', '  ', 'I', 'L', 'Ubaghara', false, '');
INSERT INTO languages_iso639 VALUES ('byd', '   ', '   ', '  ', 'I', 'L', 'Benyadu''', false, '');
INSERT INTO languages_iso639 VALUES ('bye', '   ', '   ', '  ', 'I', 'L', 'Pouye', false, '');
INSERT INTO languages_iso639 VALUES ('byf', '   ', '   ', '  ', 'I', 'L', 'Bete', false, '');
INSERT INTO languages_iso639 VALUES ('byg', '   ', '   ', '  ', 'I', 'E', 'Baygo', false, '');
INSERT INTO languages_iso639 VALUES ('byh', '   ', '   ', '  ', 'I', 'L', 'Bhujel', false, '');
INSERT INTO languages_iso639 VALUES ('byi', '   ', '   ', '  ', 'I', 'L', 'Buyu', false, '');
INSERT INTO languages_iso639 VALUES ('byj', '   ', '   ', '  ', 'I', 'L', 'Bina (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('byk', '   ', '   ', '  ', 'I', 'L', 'Biao', false, '');
INSERT INTO languages_iso639 VALUES ('byl', '   ', '   ', '  ', 'I', 'L', 'Bayono', false, '');
INSERT INTO languages_iso639 VALUES ('bym', '   ', '   ', '  ', 'I', 'L', 'Bidyara', false, '');
INSERT INTO languages_iso639 VALUES ('byn', 'byn', 'byn', '  ', 'I', 'L', 'Bilin', false, '');
INSERT INTO languages_iso639 VALUES ('byo', '   ', '   ', '  ', 'I', 'L', 'Biyo', false, '');
INSERT INTO languages_iso639 VALUES ('byp', '   ', '   ', '  ', 'I', 'L', 'Bumaji', false, '');
INSERT INTO languages_iso639 VALUES ('byq', '   ', '   ', '  ', 'I', 'E', 'Basay', false, '');
INSERT INTO languages_iso639 VALUES ('byr', '   ', '   ', '  ', 'I', 'L', 'Baruya', false, '');
INSERT INTO languages_iso639 VALUES ('bys', '   ', '   ', '  ', 'I', 'L', 'Burak', false, '');
INSERT INTO languages_iso639 VALUES ('byt', '   ', '   ', '  ', 'I', 'E', 'Berti', false, '');
INSERT INTO languages_iso639 VALUES ('byv', '   ', '   ', '  ', 'I', 'L', 'Medumba', false, '');
INSERT INTO languages_iso639 VALUES ('byw', '   ', '   ', '  ', 'I', 'L', 'Belhariya', false, '');
INSERT INTO languages_iso639 VALUES ('byx', '   ', '   ', '  ', 'I', 'L', 'Qaqet', false, '');
INSERT INTO languages_iso639 VALUES ('byy', '   ', '   ', '  ', 'I', 'L', 'Buya', false, '');
INSERT INTO languages_iso639 VALUES ('byz', '   ', '   ', '  ', 'I', 'L', 'Banaro', false, '');
INSERT INTO languages_iso639 VALUES ('bza', '   ', '   ', '  ', 'I', 'L', 'Bandi', false, '');
INSERT INTO languages_iso639 VALUES ('bzb', '   ', '   ', '  ', 'I', 'L', 'Andio', false, '');
INSERT INTO languages_iso639 VALUES ('bzc', '   ', '   ', '  ', 'I', 'L', 'Southern Betsimisaraka Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('bzd', '   ', '   ', '  ', 'I', 'L', 'Bribri', false, '');
INSERT INTO languages_iso639 VALUES ('bze', '   ', '   ', '  ', 'I', 'L', 'Jenaama Bozo', false, '');
INSERT INTO languages_iso639 VALUES ('bzf', '   ', '   ', '  ', 'I', 'L', 'Boikin', false, '');
INSERT INTO languages_iso639 VALUES ('bzg', '   ', '   ', '  ', 'I', 'L', 'Babuza', false, '');
INSERT INTO languages_iso639 VALUES ('bzh', '   ', '   ', '  ', 'I', 'L', 'Mapos Buang', false, '');
INSERT INTO languages_iso639 VALUES ('bzi', '   ', '   ', '  ', 'I', 'L', 'Bisu', false, '');
INSERT INTO languages_iso639 VALUES ('bzj', '   ', '   ', '  ', 'I', 'L', 'Belize Kriol English', false, '');
INSERT INTO languages_iso639 VALUES ('bzk', '   ', '   ', '  ', 'I', 'L', 'Nicaragua Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('bzl', '   ', '   ', '  ', 'I', 'L', 'Boano (Sulawesi)', false, '');
INSERT INTO languages_iso639 VALUES ('bzm', '   ', '   ', '  ', 'I', 'L', 'Bolondo', false, '');
INSERT INTO languages_iso639 VALUES ('bzn', '   ', '   ', '  ', 'I', 'L', 'Boano (Maluku)', false, '');
INSERT INTO languages_iso639 VALUES ('bzo', '   ', '   ', '  ', 'I', 'L', 'Bozaba', false, '');
INSERT INTO languages_iso639 VALUES ('bzp', '   ', '   ', '  ', 'I', 'L', 'Kemberano', false, '');
INSERT INTO languages_iso639 VALUES ('bzq', '   ', '   ', '  ', 'I', 'L', 'Buli (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('bzr', '   ', '   ', '  ', 'I', 'E', 'Biri', false, '');
INSERT INTO languages_iso639 VALUES ('bzs', '   ', '   ', '  ', 'I', 'L', 'Brazilian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('bzt', '   ', '   ', '  ', 'I', 'C', 'Brithenig', false, '');
INSERT INTO languages_iso639 VALUES ('bzu', '   ', '   ', '  ', 'I', 'L', 'Burmeso', false, '');
INSERT INTO languages_iso639 VALUES ('bzv', '   ', '   ', '  ', 'I', 'L', 'Naami', false, '');
INSERT INTO languages_iso639 VALUES ('bzw', '   ', '   ', '  ', 'I', 'L', 'Basa (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('bzx', '   ', '   ', '  ', 'I', 'L', 'Kɛlɛngaxo Bozo', false, '');
INSERT INTO languages_iso639 VALUES ('bzy', '   ', '   ', '  ', 'I', 'L', 'Obanliku', false, '');
INSERT INTO languages_iso639 VALUES ('bzz', '   ', '   ', '  ', 'I', 'L', 'Evant', false, '');
INSERT INTO languages_iso639 VALUES ('caa', '   ', '   ', '  ', 'I', 'L', 'Chortí', false, '');
INSERT INTO languages_iso639 VALUES ('cab', '   ', '   ', '  ', 'I', 'L', 'Garifuna', false, '');
INSERT INTO languages_iso639 VALUES ('cac', '   ', '   ', '  ', 'I', 'L', 'Chuj', false, '');
INSERT INTO languages_iso639 VALUES ('cad', 'cad', 'cad', '  ', 'I', 'L', 'Caddo', false, '');
INSERT INTO languages_iso639 VALUES ('cae', '   ', '   ', '  ', 'I', 'L', 'Lehar', false, '');
INSERT INTO languages_iso639 VALUES ('caf', '   ', '   ', '  ', 'I', 'L', 'Southern Carrier', false, '');
INSERT INTO languages_iso639 VALUES ('cag', '   ', '   ', '  ', 'I', 'L', 'Nivaclé', false, '');
INSERT INTO languages_iso639 VALUES ('cah', '   ', '   ', '  ', 'I', 'L', 'Cahuarano', false, '');
INSERT INTO languages_iso639 VALUES ('caj', '   ', '   ', '  ', 'I', 'E', 'Chané', false, '');
INSERT INTO languages_iso639 VALUES ('cak', '   ', '   ', '  ', 'I', 'L', 'Kaqchikel', false, '');
INSERT INTO languages_iso639 VALUES ('cal', '   ', '   ', '  ', 'I', 'L', 'Carolinian', false, '');
INSERT INTO languages_iso639 VALUES ('cam', '   ', '   ', '  ', 'I', 'L', 'Cemuhî', false, '');
INSERT INTO languages_iso639 VALUES ('can', '   ', '   ', '  ', 'I', 'L', 'Chambri', false, '');
INSERT INTO languages_iso639 VALUES ('cao', '   ', '   ', '  ', 'I', 'L', 'Chácobo', false, '');
INSERT INTO languages_iso639 VALUES ('cap', '   ', '   ', '  ', 'I', 'L', 'Chipaya', false, '');
INSERT INTO languages_iso639 VALUES ('caq', '   ', '   ', '  ', 'I', 'L', 'Car Nicobarese', false, '');
INSERT INTO languages_iso639 VALUES ('car', 'car', 'car', '  ', 'I', 'L', 'Galibi Carib', false, '');
INSERT INTO languages_iso639 VALUES ('cas', '   ', '   ', '  ', 'I', 'L', 'Tsimané', false, '');
INSERT INTO languages_iso639 VALUES ('cat', 'cat', 'cat', 'ca', 'I', 'L', 'Catalan', false, '');
INSERT INTO languages_iso639 VALUES ('cav', '   ', '   ', '  ', 'I', 'L', 'Cavineña', false, '');
INSERT INTO languages_iso639 VALUES ('caw', '   ', '   ', '  ', 'I', 'L', 'Callawalla', false, '');
INSERT INTO languages_iso639 VALUES ('cax', '   ', '   ', '  ', 'I', 'L', 'Chiquitano', false, '');
INSERT INTO languages_iso639 VALUES ('cay', '   ', '   ', '  ', 'I', 'L', 'Cayuga', false, '');
INSERT INTO languages_iso639 VALUES ('caz', '   ', '   ', '  ', 'I', 'E', 'Canichana', false, '');
INSERT INTO languages_iso639 VALUES ('cbb', '   ', '   ', '  ', 'I', 'L', 'Cabiyarí', false, '');
INSERT INTO languages_iso639 VALUES ('cbc', '   ', '   ', '  ', 'I', 'L', 'Carapana', false, '');
INSERT INTO languages_iso639 VALUES ('cbd', '   ', '   ', '  ', 'I', 'L', 'Carijona', false, '');
INSERT INTO languages_iso639 VALUES ('cbe', '   ', '   ', '  ', 'I', 'E', 'Chipiajes', false, '');
INSERT INTO languages_iso639 VALUES ('cbg', '   ', '   ', '  ', 'I', 'L', 'Chimila', false, '');
INSERT INTO languages_iso639 VALUES ('cbh', '   ', '   ', '  ', 'I', 'E', 'Cagua', false, '');
INSERT INTO languages_iso639 VALUES ('cbi', '   ', '   ', '  ', 'I', 'L', 'Chachi', false, '');
INSERT INTO languages_iso639 VALUES ('cbj', '   ', '   ', '  ', 'I', 'L', 'Ede Cabe', false, '');
INSERT INTO languages_iso639 VALUES ('cbk', '   ', '   ', '  ', 'I', 'L', 'Chavacano', false, '');
INSERT INTO languages_iso639 VALUES ('cbl', '   ', '   ', '  ', 'I', 'L', 'Bualkhaw Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cbn', '   ', '   ', '  ', 'I', 'L', 'Nyahkur', false, '');
INSERT INTO languages_iso639 VALUES ('cbo', '   ', '   ', '  ', 'I', 'L', 'Izora', false, '');
INSERT INTO languages_iso639 VALUES ('cbr', '   ', '   ', '  ', 'I', 'L', 'Cashibo-Cacataibo', false, '');
INSERT INTO languages_iso639 VALUES ('cbs', '   ', '   ', '  ', 'I', 'L', 'Cashinahua', false, '');
INSERT INTO languages_iso639 VALUES ('cbt', '   ', '   ', '  ', 'I', 'L', 'Chayahuita', false, '');
INSERT INTO languages_iso639 VALUES ('cbu', '   ', '   ', '  ', 'I', 'L', 'Candoshi-Shapra', false, '');
INSERT INTO languages_iso639 VALUES ('cbv', '   ', '   ', '  ', 'I', 'L', 'Cacua', false, '');
INSERT INTO languages_iso639 VALUES ('cbw', '   ', '   ', '  ', 'I', 'L', 'Kinabalian', false, '');
INSERT INTO languages_iso639 VALUES ('cby', '   ', '   ', '  ', 'I', 'L', 'Carabayo', false, '');
INSERT INTO languages_iso639 VALUES ('cca', '   ', '   ', '  ', 'I', 'E', 'Cauca', false, '');
INSERT INTO languages_iso639 VALUES ('ccc', '   ', '   ', '  ', 'I', 'L', 'Chamicuro', false, '');
INSERT INTO languages_iso639 VALUES ('ccd', '   ', '   ', '  ', 'I', 'L', 'Cafundo Creole', false, '');
INSERT INTO languages_iso639 VALUES ('cce', '   ', '   ', '  ', 'I', 'L', 'Chopi', false, '');
INSERT INTO languages_iso639 VALUES ('ccg', '   ', '   ', '  ', 'I', 'L', 'Samba Daka', false, '');
INSERT INTO languages_iso639 VALUES ('cch', '   ', '   ', '  ', 'I', 'L', 'Atsam', false, '');
INSERT INTO languages_iso639 VALUES ('ccj', '   ', '   ', '  ', 'I', 'L', 'Kasanga', false, '');
INSERT INTO languages_iso639 VALUES ('ccl', '   ', '   ', '  ', 'I', 'L', 'Cutchi-Swahili', false, '');
INSERT INTO languages_iso639 VALUES ('ccm', '   ', '   ', '  ', 'I', 'L', 'Malaccan Creole Malay', false, '');
INSERT INTO languages_iso639 VALUES ('cco', '   ', '   ', '  ', 'I', 'L', 'Comaltepec Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('ccp', '   ', '   ', '  ', 'I', 'L', 'Chakma', false, '');
INSERT INTO languages_iso639 VALUES ('ccr', '   ', '   ', '  ', 'I', 'E', 'Cacaopera', false, '');
INSERT INTO languages_iso639 VALUES ('cda', '   ', '   ', '  ', 'I', 'L', 'Choni', false, '');
INSERT INTO languages_iso639 VALUES ('cde', '   ', '   ', '  ', 'I', 'L', 'Chenchu', false, '');
INSERT INTO languages_iso639 VALUES ('cdf', '   ', '   ', '  ', 'I', 'L', 'Chiru', false, '');
INSERT INTO languages_iso639 VALUES ('cdg', '   ', '   ', '  ', 'I', 'L', 'Chamari', false, '');
INSERT INTO languages_iso639 VALUES ('cdh', '   ', '   ', '  ', 'I', 'L', 'Chambeali', false, '');
INSERT INTO languages_iso639 VALUES ('cdi', '   ', '   ', '  ', 'I', 'L', 'Chodri', false, '');
INSERT INTO languages_iso639 VALUES ('cdj', '   ', '   ', '  ', 'I', 'L', 'Churahi', false, '');
INSERT INTO languages_iso639 VALUES ('cdm', '   ', '   ', '  ', 'I', 'L', 'Chepang', false, '');
INSERT INTO languages_iso639 VALUES ('cdn', '   ', '   ', '  ', 'I', 'L', 'Chaudangsi', false, '');
INSERT INTO languages_iso639 VALUES ('cdo', '   ', '   ', '  ', 'I', 'L', 'Min Dong Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('cdr', '   ', '   ', '  ', 'I', 'L', 'Cinda-Regi-Tiyal', false, '');
INSERT INTO languages_iso639 VALUES ('cds', '   ', '   ', '  ', 'I', 'L', 'Chadian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('cdy', '   ', '   ', '  ', 'I', 'L', 'Chadong', false, '');
INSERT INTO languages_iso639 VALUES ('cdz', '   ', '   ', '  ', 'I', 'L', 'Koda', false, '');
INSERT INTO languages_iso639 VALUES ('cea', '   ', '   ', '  ', 'I', 'E', 'Lower Chehalis', false, '');
INSERT INTO languages_iso639 VALUES ('ceb', 'ceb', 'ceb', '  ', 'I', 'L', 'Cebuano', false, '');
INSERT INTO languages_iso639 VALUES ('ceg', '   ', '   ', '  ', 'I', 'L', 'Chamacoco', false, '');
INSERT INTO languages_iso639 VALUES ('cek', '   ', '   ', '  ', 'I', 'L', 'Eastern Khumi Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cen', '   ', '   ', '  ', 'I', 'L', 'Cen', false, '');
INSERT INTO languages_iso639 VALUES ('ces', 'cze', 'ces', 'cs', 'I', 'L', 'Czech', false, '');
INSERT INTO languages_iso639 VALUES ('cet', '   ', '   ', '  ', 'I', 'L', 'Centúúm', false, '');
INSERT INTO languages_iso639 VALUES ('cfa', '   ', '   ', '  ', 'I', 'L', 'Dijim-Bwilim', false, '');
INSERT INTO languages_iso639 VALUES ('cfd', '   ', '   ', '  ', 'I', 'L', 'Cara', false, '');
INSERT INTO languages_iso639 VALUES ('cfg', '   ', '   ', '  ', 'I', 'L', 'Como Karim', false, '');
INSERT INTO languages_iso639 VALUES ('cfm', '   ', '   ', '  ', 'I', 'L', 'Falam Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cga', '   ', '   ', '  ', 'I', 'L', 'Changriwa', false, '');
INSERT INTO languages_iso639 VALUES ('cgc', '   ', '   ', '  ', 'I', 'L', 'Kagayanen', false, '');
INSERT INTO languages_iso639 VALUES ('cgg', '   ', '   ', '  ', 'I', 'L', 'Chiga', false, '');
INSERT INTO languages_iso639 VALUES ('cgk', '   ', '   ', '  ', 'I', 'L', 'Chocangacakha', false, '');
INSERT INTO languages_iso639 VALUES ('cha', 'cha', 'cha', 'ch', 'I', 'L', 'Chamorro', false, '');
INSERT INTO languages_iso639 VALUES ('chb', 'chb', 'chb', '  ', 'I', 'E', 'Chibcha', false, '');
INSERT INTO languages_iso639 VALUES ('chc', '   ', '   ', '  ', 'I', 'E', 'Catawba', false, '');
INSERT INTO languages_iso639 VALUES ('chd', '   ', '   ', '  ', 'I', 'L', 'Highland Oaxaca Chontal', false, '');
INSERT INTO languages_iso639 VALUES ('che', 'che', 'che', 'ce', 'I', 'L', 'Chechen', false, '');
INSERT INTO languages_iso639 VALUES ('chf', '   ', '   ', '  ', 'I', 'L', 'Tabasco Chontal', false, '');
INSERT INTO languages_iso639 VALUES ('chg', 'chg', 'chg', '  ', 'I', 'E', 'Chagatai', false, '');
INSERT INTO languages_iso639 VALUES ('chh', '   ', '   ', '  ', 'I', 'L', 'Chinook', false, '');
INSERT INTO languages_iso639 VALUES ('chj', '   ', '   ', '  ', 'I', 'L', 'Ojitlán Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('chk', 'chk', 'chk', '  ', 'I', 'L', 'Chuukese', false, '');
INSERT INTO languages_iso639 VALUES ('chl', '   ', '   ', '  ', 'I', 'L', 'Cahuilla', false, '');
INSERT INTO languages_iso639 VALUES ('chm', 'chm', 'chm', '  ', 'M', 'L', 'Mari (Russia)', false, '');
INSERT INTO languages_iso639 VALUES ('chn', 'chn', 'chn', '  ', 'I', 'L', 'Chinook jargon', false, '');
INSERT INTO languages_iso639 VALUES ('cho', 'cho', 'cho', '  ', 'I', 'L', 'Choctaw', false, '');
INSERT INTO languages_iso639 VALUES ('chp', 'chp', 'chp', '  ', 'I', 'L', 'Chipewyan', false, '');
INSERT INTO languages_iso639 VALUES ('chq', '   ', '   ', '  ', 'I', 'L', 'Quiotepec Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('chr', 'chr', 'chr', '  ', 'I', 'L', 'Cherokee', false, '');
INSERT INTO languages_iso639 VALUES ('cht', '   ', '   ', '  ', 'I', 'E', 'Cholón', false, '');
INSERT INTO languages_iso639 VALUES ('chu', 'chu', 'chu', 'cu', 'I', 'A', 'Church Slavic', false, '');
INSERT INTO languages_iso639 VALUES ('chv', 'chv', 'chv', 'cv', 'I', 'L', 'Chuvash', false, '');
INSERT INTO languages_iso639 VALUES ('chw', '   ', '   ', '  ', 'I', 'L', 'Chuwabu', false, '');
INSERT INTO languages_iso639 VALUES ('chx', '   ', '   ', '  ', 'I', 'L', 'Chantyal', false, '');
INSERT INTO languages_iso639 VALUES ('chy', 'chy', 'chy', '  ', 'I', 'L', 'Cheyenne', false, '');
INSERT INTO languages_iso639 VALUES ('chz', '   ', '   ', '  ', 'I', 'L', 'Ozumacín Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('cia', '   ', '   ', '  ', 'I', 'L', 'Cia-Cia', false, '');
INSERT INTO languages_iso639 VALUES ('cib', '   ', '   ', '  ', 'I', 'L', 'Ci Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('cic', '   ', '   ', '  ', 'I', 'L', 'Chickasaw', false, '');
INSERT INTO languages_iso639 VALUES ('cid', '   ', '   ', '  ', 'I', 'E', 'Chimariko', false, '');
INSERT INTO languages_iso639 VALUES ('cie', '   ', '   ', '  ', 'I', 'L', 'Cineni', false, '');
INSERT INTO languages_iso639 VALUES ('cih', '   ', '   ', '  ', 'I', 'L', 'Chinali', false, '');
INSERT INTO languages_iso639 VALUES ('cik', '   ', '   ', '  ', 'I', 'L', 'Chitkuli Kinnauri', false, '');
INSERT INTO languages_iso639 VALUES ('cim', '   ', '   ', '  ', 'I', 'L', 'Cimbrian', false, '');
INSERT INTO languages_iso639 VALUES ('cin', '   ', '   ', '  ', 'I', 'L', 'Cinta Larga', false, '');
INSERT INTO languages_iso639 VALUES ('cip', '   ', '   ', '  ', 'I', 'L', 'Chiapanec', false, '');
INSERT INTO languages_iso639 VALUES ('cir', '   ', '   ', '  ', 'I', 'L', 'Tiri', false, '');
INSERT INTO languages_iso639 VALUES ('ciw', '   ', '   ', '  ', 'I', 'L', 'Chippewa', false, '');
INSERT INTO languages_iso639 VALUES ('ciy', '   ', '   ', '  ', 'I', 'L', 'Chaima', false, '');
INSERT INTO languages_iso639 VALUES ('cja', '   ', '   ', '  ', 'I', 'L', 'Western Cham', false, '');
INSERT INTO languages_iso639 VALUES ('cje', '   ', '   ', '  ', 'I', 'L', 'Chru', false, '');
INSERT INTO languages_iso639 VALUES ('cjh', '   ', '   ', '  ', 'I', 'E', 'Upper Chehalis', false, '');
INSERT INTO languages_iso639 VALUES ('cji', '   ', '   ', '  ', 'I', 'L', 'Chamalal', false, '');
INSERT INTO languages_iso639 VALUES ('cjk', '   ', '   ', '  ', 'I', 'L', 'Chokwe', false, '');
INSERT INTO languages_iso639 VALUES ('cjm', '   ', '   ', '  ', 'I', 'L', 'Eastern Cham', false, '');
INSERT INTO languages_iso639 VALUES ('cjn', '   ', '   ', '  ', 'I', 'L', 'Chenapian', false, '');
INSERT INTO languages_iso639 VALUES ('cjo', '   ', '   ', '  ', 'I', 'L', 'Ashéninka Pajonal', false, '');
INSERT INTO languages_iso639 VALUES ('cjp', '   ', '   ', '  ', 'I', 'L', 'Cabécar', false, '');
INSERT INTO languages_iso639 VALUES ('cjs', '   ', '   ', '  ', 'I', 'L', 'Shor', false, '');
INSERT INTO languages_iso639 VALUES ('cjv', '   ', '   ', '  ', 'I', 'L', 'Chuave', false, '');
INSERT INTO languages_iso639 VALUES ('cjy', '   ', '   ', '  ', 'I', 'L', 'Jinyu Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('ckb', '   ', '   ', '  ', 'I', 'L', 'Central Kurdish', false, '');
INSERT INTO languages_iso639 VALUES ('ckh', '   ', '   ', '  ', 'I', 'L', 'Chak', false, '');
INSERT INTO languages_iso639 VALUES ('ckl', '   ', '   ', '  ', 'I', 'L', 'Cibak', false, '');
INSERT INTO languages_iso639 VALUES ('ckn', '   ', '   ', '  ', 'I', 'L', 'Kaang Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cko', '   ', '   ', '  ', 'I', 'L', 'Anufo', false, '');
INSERT INTO languages_iso639 VALUES ('ckq', '   ', '   ', '  ', 'I', 'L', 'Kajakse', false, '');
INSERT INTO languages_iso639 VALUES ('ckr', '   ', '   ', '  ', 'I', 'L', 'Kairak', false, '');
INSERT INTO languages_iso639 VALUES ('cks', '   ', '   ', '  ', 'I', 'L', 'Tayo', false, '');
INSERT INTO languages_iso639 VALUES ('ckt', '   ', '   ', '  ', 'I', 'L', 'Chukot', false, '');
INSERT INTO languages_iso639 VALUES ('cku', '   ', '   ', '  ', 'I', 'L', 'Koasati', false, '');
INSERT INTO languages_iso639 VALUES ('ckv', '   ', '   ', '  ', 'I', 'L', 'Kavalan', false, '');
INSERT INTO languages_iso639 VALUES ('ckx', '   ', '   ', '  ', 'I', 'L', 'Caka', false, '');
INSERT INTO languages_iso639 VALUES ('cky', '   ', '   ', '  ', 'I', 'L', 'Cakfem-Mushere', false, '');
INSERT INTO languages_iso639 VALUES ('ckz', '   ', '   ', '  ', 'I', 'L', 'Cakchiquel-Quiché Mixed Language', false, '');
INSERT INTO languages_iso639 VALUES ('cla', '   ', '   ', '  ', 'I', 'L', 'Ron', false, '');
INSERT INTO languages_iso639 VALUES ('clc', '   ', '   ', '  ', 'I', 'L', 'Chilcotin', false, '');
INSERT INTO languages_iso639 VALUES ('cld', '   ', '   ', '  ', 'I', 'L', 'Chaldean Neo-Aramaic', false, '');
INSERT INTO languages_iso639 VALUES ('cle', '   ', '   ', '  ', 'I', 'L', 'Lealao Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('clh', '   ', '   ', '  ', 'I', 'L', 'Chilisso', false, '');
INSERT INTO languages_iso639 VALUES ('cli', '   ', '   ', '  ', 'I', 'L', 'Chakali', false, '');
INSERT INTO languages_iso639 VALUES ('clj', '   ', '   ', '  ', 'I', 'L', 'Laitu Chin', false, '');
INSERT INTO languages_iso639 VALUES ('clk', '   ', '   ', '  ', 'I', 'L', 'Idu-Mishmi', false, '');
INSERT INTO languages_iso639 VALUES ('cll', '   ', '   ', '  ', 'I', 'L', 'Chala', false, '');
INSERT INTO languages_iso639 VALUES ('clm', '   ', '   ', '  ', 'I', 'L', 'Clallam', false, '');
INSERT INTO languages_iso639 VALUES ('clo', '   ', '   ', '  ', 'I', 'L', 'Lowland Oaxaca Chontal', false, '');
INSERT INTO languages_iso639 VALUES ('clt', '   ', '   ', '  ', 'I', 'L', 'Lautu Chin', false, '');
INSERT INTO languages_iso639 VALUES ('clu', '   ', '   ', '  ', 'I', 'L', 'Caluyanun', false, '');
INSERT INTO languages_iso639 VALUES ('clw', '   ', '   ', '  ', 'I', 'L', 'Chulym', false, '');
INSERT INTO languages_iso639 VALUES ('cly', '   ', '   ', '  ', 'I', 'L', 'Eastern Highland Chatino', false, '');
INSERT INTO languages_iso639 VALUES ('cma', '   ', '   ', '  ', 'I', 'L', 'Maa', false, '');
INSERT INTO languages_iso639 VALUES ('cme', '   ', '   ', '  ', 'I', 'L', 'Cerma', false, '');
INSERT INTO languages_iso639 VALUES ('cmg', '   ', '   ', '  ', 'I', 'H', 'Classical Mongolian', false, '');
INSERT INTO languages_iso639 VALUES ('cmi', '   ', '   ', '  ', 'I', 'L', 'Emberá-Chamí', false, '');
INSERT INTO languages_iso639 VALUES ('cml', '   ', '   ', '  ', 'I', 'L', 'Campalagian', false, '');
INSERT INTO languages_iso639 VALUES ('cmm', '   ', '   ', '  ', 'I', 'E', 'Michigamea', false, '');
INSERT INTO languages_iso639 VALUES ('cmn', '   ', '   ', '  ', 'I', 'L', 'Mandarin Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('cmo', '   ', '   ', '  ', 'I', 'L', 'Central Mnong', false, '');
INSERT INTO languages_iso639 VALUES ('cmr', '   ', '   ', '  ', 'I', 'L', 'Mro-Khimi Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cms', '   ', '   ', '  ', 'I', 'A', 'Messapic', false, '');
INSERT INTO languages_iso639 VALUES ('cmt', '   ', '   ', '  ', 'I', 'L', 'Camtho', false, '');
INSERT INTO languages_iso639 VALUES ('cna', '   ', '   ', '  ', 'I', 'L', 'Changthang', false, '');
INSERT INTO languages_iso639 VALUES ('cnb', '   ', '   ', '  ', 'I', 'L', 'Chinbon Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cnc', '   ', '   ', '  ', 'I', 'L', 'Côông', false, '');
INSERT INTO languages_iso639 VALUES ('cng', '   ', '   ', '  ', 'I', 'L', 'Northern Qiang', false, '');
INSERT INTO languages_iso639 VALUES ('cnh', '   ', '   ', '  ', 'I', 'L', 'Haka Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cni', '   ', '   ', '  ', 'I', 'L', 'Asháninka', false, '');
INSERT INTO languages_iso639 VALUES ('cnk', '   ', '   ', '  ', 'I', 'L', 'Khumi Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cnl', '   ', '   ', '  ', 'I', 'L', 'Lalana Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('cno', '   ', '   ', '  ', 'I', 'L', 'Con', false, '');
INSERT INTO languages_iso639 VALUES ('cns', '   ', '   ', '  ', 'I', 'L', 'Central Asmat', false, '');
INSERT INTO languages_iso639 VALUES ('cnt', '   ', '   ', '  ', 'I', 'L', 'Tepetotutla Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('cnu', '   ', '   ', '  ', 'I', 'L', 'Chenoua', false, '');
INSERT INTO languages_iso639 VALUES ('cnw', '   ', '   ', '  ', 'I', 'L', 'Ngawn Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cnx', '   ', '   ', '  ', 'I', 'H', 'Middle Cornish', false, '');
INSERT INTO languages_iso639 VALUES ('coa', '   ', '   ', '  ', 'I', 'L', 'Cocos Islands Malay', false, '');
INSERT INTO languages_iso639 VALUES ('cob', '   ', '   ', '  ', 'I', 'E', 'Chicomuceltec', false, '');
INSERT INTO languages_iso639 VALUES ('coc', '   ', '   ', '  ', 'I', 'L', 'Cocopa', false, '');
INSERT INTO languages_iso639 VALUES ('cod', '   ', '   ', '  ', 'I', 'L', 'Cocama-Cocamilla', false, '');
INSERT INTO languages_iso639 VALUES ('coe', '   ', '   ', '  ', 'I', 'L', 'Koreguaje', false, '');
INSERT INTO languages_iso639 VALUES ('cof', '   ', '   ', '  ', 'I', 'L', 'Colorado', false, '');
INSERT INTO languages_iso639 VALUES ('cog', '   ', '   ', '  ', 'I', 'L', 'Chong', false, '');
INSERT INTO languages_iso639 VALUES ('coh', '   ', '   ', '  ', 'I', 'L', 'Chonyi-Dzihana-Kauma', false, '');
INSERT INTO languages_iso639 VALUES ('coj', '   ', '   ', '  ', 'I', 'E', 'Cochimi', false, '');
INSERT INTO languages_iso639 VALUES ('cok', '   ', '   ', '  ', 'I', 'L', 'Santa Teresa Cora', false, '');
INSERT INTO languages_iso639 VALUES ('col', '   ', '   ', '  ', 'I', 'L', 'Columbia-Wenatchi', false, '');
INSERT INTO languages_iso639 VALUES ('com', '   ', '   ', '  ', 'I', 'L', 'Comanche', false, '');
INSERT INTO languages_iso639 VALUES ('con', '   ', '   ', '  ', 'I', 'L', 'Cofán', false, '');
INSERT INTO languages_iso639 VALUES ('coo', '   ', '   ', '  ', 'I', 'L', 'Comox', false, '');
INSERT INTO languages_iso639 VALUES ('cop', 'cop', 'cop', '  ', 'I', 'E', 'Coptic', false, '');
INSERT INTO languages_iso639 VALUES ('coq', '   ', '   ', '  ', 'I', 'E', 'Coquille', false, '');
INSERT INTO languages_iso639 VALUES ('cor', 'cor', 'cor', 'kw', 'I', 'L', 'Cornish', false, '');
INSERT INTO languages_iso639 VALUES ('cos', 'cos', 'cos', 'co', 'I', 'L', 'Corsican', false, '');
INSERT INTO languages_iso639 VALUES ('cot', '   ', '   ', '  ', 'I', 'L', 'Caquinte', false, '');
INSERT INTO languages_iso639 VALUES ('cou', '   ', '   ', '  ', 'I', 'L', 'Wamey', false, '');
INSERT INTO languages_iso639 VALUES ('cov', '   ', '   ', '  ', 'I', 'L', 'Cao Miao', false, '');
INSERT INTO languages_iso639 VALUES ('cow', '   ', '   ', '  ', 'I', 'E', 'Cowlitz', false, '');
INSERT INTO languages_iso639 VALUES ('cox', '   ', '   ', '  ', 'I', 'L', 'Nanti', false, '');
INSERT INTO languages_iso639 VALUES ('coy', '   ', '   ', '  ', 'I', 'E', 'Coyaima', false, '');
INSERT INTO languages_iso639 VALUES ('coz', '   ', '   ', '  ', 'I', 'L', 'Chochotec', false, '');
INSERT INTO languages_iso639 VALUES ('cpa', '   ', '   ', '  ', 'I', 'L', 'Palantla Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('cpb', '   ', '   ', '  ', 'I', 'L', 'Ucayali-Yurúa Ashéninka', false, '');
INSERT INTO languages_iso639 VALUES ('cpc', '   ', '   ', '  ', 'I', 'L', 'Ajyíninka Apurucayali', false, '');
INSERT INTO languages_iso639 VALUES ('cpg', '   ', '   ', '  ', 'I', 'E', 'Cappadocian Greek', false, '');
INSERT INTO languages_iso639 VALUES ('cpi', '   ', '   ', '  ', 'I', 'L', 'Chinese Pidgin English', false, '');
INSERT INTO languages_iso639 VALUES ('cpn', '   ', '   ', '  ', 'I', 'L', 'Cherepon', false, '');
INSERT INTO languages_iso639 VALUES ('cpo', '   ', '   ', '  ', 'I', 'L', 'Kpeego', false, '');
INSERT INTO languages_iso639 VALUES ('cps', '   ', '   ', '  ', 'I', 'L', 'Capiznon', false, '');
INSERT INTO languages_iso639 VALUES ('cpu', '   ', '   ', '  ', 'I', 'L', 'Pichis Ashéninka', false, '');
INSERT INTO languages_iso639 VALUES ('cpx', '   ', '   ', '  ', 'I', 'L', 'Pu-Xian Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('cpy', '   ', '   ', '  ', 'I', 'L', 'South Ucayali Ashéninka', false, '');
INSERT INTO languages_iso639 VALUES ('cqd', '   ', '   ', '  ', 'I', 'L', 'Chuanqiandian Cluster Miao', false, '');
INSERT INTO languages_iso639 VALUES ('cqu', '   ', '   ', '  ', 'I', 'L', 'Chilean Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('cra', '   ', '   ', '  ', 'I', 'L', 'Chara', false, '');
INSERT INTO languages_iso639 VALUES ('crb', '   ', '   ', '  ', 'I', 'E', 'Island Carib', false, '');
INSERT INTO languages_iso639 VALUES ('crc', '   ', '   ', '  ', 'I', 'L', 'Lonwolwol', false, '');
INSERT INTO languages_iso639 VALUES ('crd', '   ', '   ', '  ', 'I', 'L', 'Coeur d''Alene', false, '');
INSERT INTO languages_iso639 VALUES ('cre', 'cre', 'cre', 'cr', 'M', 'L', 'Cree', false, '');
INSERT INTO languages_iso639 VALUES ('crf', '   ', '   ', '  ', 'I', 'E', 'Caramanta', false, '');
INSERT INTO languages_iso639 VALUES ('crg', '   ', '   ', '  ', 'I', 'L', 'Michif', false, '');
INSERT INTO languages_iso639 VALUES ('crh', 'crh', 'crh', '  ', 'I', 'L', 'Crimean Tatar', false, '');
INSERT INTO languages_iso639 VALUES ('cri', '   ', '   ', '  ', 'I', 'L', 'Sãotomense', false, '');
INSERT INTO languages_iso639 VALUES ('crj', '   ', '   ', '  ', 'I', 'L', 'Southern East Cree', false, '');
INSERT INTO languages_iso639 VALUES ('crk', '   ', '   ', '  ', 'I', 'L', 'Plains Cree', false, '');
INSERT INTO languages_iso639 VALUES ('crl', '   ', '   ', '  ', 'I', 'L', 'Northern East Cree', false, '');
INSERT INTO languages_iso639 VALUES ('crm', '   ', '   ', '  ', 'I', 'L', 'Moose Cree', false, '');
INSERT INTO languages_iso639 VALUES ('crn', '   ', '   ', '  ', 'I', 'L', 'El Nayar Cora', false, '');
INSERT INTO languages_iso639 VALUES ('cro', '   ', '   ', '  ', 'I', 'L', 'Crow', false, '');
INSERT INTO languages_iso639 VALUES ('crq', '   ', '   ', '  ', 'I', 'L', 'Iyo''wujwa Chorote', false, '');
INSERT INTO languages_iso639 VALUES ('crr', '   ', '   ', '  ', 'I', 'E', 'Carolina Algonquian', false, '');
INSERT INTO languages_iso639 VALUES ('crs', '   ', '   ', '  ', 'I', 'L', 'Seselwa Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('crt', '   ', '   ', '  ', 'I', 'L', 'Iyojwa''ja Chorote', false, '');
INSERT INTO languages_iso639 VALUES ('crv', '   ', '   ', '  ', 'I', 'L', 'Chaura', false, '');
INSERT INTO languages_iso639 VALUES ('crw', '   ', '   ', '  ', 'I', 'L', 'Chrau', false, '');
INSERT INTO languages_iso639 VALUES ('crx', '   ', '   ', '  ', 'I', 'L', 'Carrier', false, '');
INSERT INTO languages_iso639 VALUES ('cry', '   ', '   ', '  ', 'I', 'L', 'Cori', false, '');
INSERT INTO languages_iso639 VALUES ('crz', '   ', '   ', '  ', 'I', 'E', 'Cruzeño', false, '');
INSERT INTO languages_iso639 VALUES ('csa', '   ', '   ', '  ', 'I', 'L', 'Chiltepec Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('csb', 'csb', 'csb', '  ', 'I', 'L', 'Kashubian', false, '');
INSERT INTO languages_iso639 VALUES ('csc', '   ', '   ', '  ', 'I', 'L', 'Catalan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('csd', '   ', '   ', '  ', 'I', 'L', 'Chiangmai Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('cse', '   ', '   ', '  ', 'I', 'L', 'Czech Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('csf', '   ', '   ', '  ', 'I', 'L', 'Cuba Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('csg', '   ', '   ', '  ', 'I', 'L', 'Chilean Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('csh', '   ', '   ', '  ', 'I', 'L', 'Asho Chin', false, '');
INSERT INTO languages_iso639 VALUES ('csi', '   ', '   ', '  ', 'I', 'E', 'Coast Miwok', false, '');
INSERT INTO languages_iso639 VALUES ('csj', '   ', '   ', '  ', 'I', 'L', 'Songlai Chin', false, '');
INSERT INTO languages_iso639 VALUES ('csk', '   ', '   ', '  ', 'I', 'L', 'Jola-Kasa', false, '');
INSERT INTO languages_iso639 VALUES ('csl', '   ', '   ', '  ', 'I', 'L', 'Chinese Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('csm', '   ', '   ', '  ', 'I', 'L', 'Central Sierra Miwok', false, '');
INSERT INTO languages_iso639 VALUES ('csn', '   ', '   ', '  ', 'I', 'L', 'Colombian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('cso', '   ', '   ', '  ', 'I', 'L', 'Sochiapam Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('csq', '   ', '   ', '  ', 'I', 'L', 'Croatia Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('csr', '   ', '   ', '  ', 'I', 'L', 'Costa Rican Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('css', '   ', '   ', '  ', 'I', 'E', 'Southern Ohlone', false, '');
INSERT INTO languages_iso639 VALUES ('cst', '   ', '   ', '  ', 'I', 'L', 'Northern Ohlone', false, '');
INSERT INTO languages_iso639 VALUES ('csv', '   ', '   ', '  ', 'I', 'L', 'Sumtu Chin', false, '');
INSERT INTO languages_iso639 VALUES ('csw', '   ', '   ', '  ', 'I', 'L', 'Swampy Cree', false, '');
INSERT INTO languages_iso639 VALUES ('csy', '   ', '   ', '  ', 'I', 'L', 'Siyin Chin', false, '');
INSERT INTO languages_iso639 VALUES ('csz', '   ', '   ', '  ', 'I', 'L', 'Coos', false, '');
INSERT INTO languages_iso639 VALUES ('cta', '   ', '   ', '  ', 'I', 'L', 'Tataltepec Chatino', false, '');
INSERT INTO languages_iso639 VALUES ('ctc', '   ', '   ', '  ', 'I', 'L', 'Chetco', false, '');
INSERT INTO languages_iso639 VALUES ('ctd', '   ', '   ', '  ', 'I', 'L', 'Tedim Chin', false, '');
INSERT INTO languages_iso639 VALUES ('cte', '   ', '   ', '  ', 'I', 'L', 'Tepinapa Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('ctg', '   ', '   ', '  ', 'I', 'L', 'Chittagonian', false, '');
INSERT INTO languages_iso639 VALUES ('cth', '   ', '   ', '  ', 'I', 'L', 'Thaiphum Chin', false, '');
INSERT INTO languages_iso639 VALUES ('ctl', '   ', '   ', '  ', 'I', 'L', 'Tlacoatzintepec Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('ctm', '   ', '   ', '  ', 'I', 'E', 'Chitimacha', false, '');
INSERT INTO languages_iso639 VALUES ('ctn', '   ', '   ', '  ', 'I', 'L', 'Chhintange', false, '');
INSERT INTO languages_iso639 VALUES ('cto', '   ', '   ', '  ', 'I', 'L', 'Emberá-Catío', false, '');
INSERT INTO languages_iso639 VALUES ('ctp', '   ', '   ', '  ', 'I', 'L', 'Western Highland Chatino', false, '');
INSERT INTO languages_iso639 VALUES ('cts', '   ', '   ', '  ', 'I', 'L', 'Northern Catanduanes Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('ctt', '   ', '   ', '  ', 'I', 'L', 'Wayanad Chetti', false, '');
INSERT INTO languages_iso639 VALUES ('ctu', '   ', '   ', '  ', 'I', 'L', 'Chol', false, '');
INSERT INTO languages_iso639 VALUES ('ctz', '   ', '   ', '  ', 'I', 'L', 'Zacatepec Chatino', false, '');
INSERT INTO languages_iso639 VALUES ('cua', '   ', '   ', '  ', 'I', 'L', 'Cua', false, '');
INSERT INTO languages_iso639 VALUES ('cub', '   ', '   ', '  ', 'I', 'L', 'Cubeo', false, '');
INSERT INTO languages_iso639 VALUES ('cuc', '   ', '   ', '  ', 'I', 'L', 'Usila Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('cug', '   ', '   ', '  ', 'I', 'L', 'Cung', false, '');
INSERT INTO languages_iso639 VALUES ('cuh', '   ', '   ', '  ', 'I', 'L', 'Chuka', false, '');
INSERT INTO languages_iso639 VALUES ('cui', '   ', '   ', '  ', 'I', 'L', 'Cuiba', false, '');
INSERT INTO languages_iso639 VALUES ('cuj', '   ', '   ', '  ', 'I', 'L', 'Mashco Piro', false, '');
INSERT INTO languages_iso639 VALUES ('cuk', '   ', '   ', '  ', 'I', 'L', 'San Blas Kuna', false, '');
INSERT INTO languages_iso639 VALUES ('cul', '   ', '   ', '  ', 'I', 'L', 'Culina', false, '');
INSERT INTO languages_iso639 VALUES ('cum', '   ', '   ', '  ', 'I', 'E', 'Cumeral', false, '');
INSERT INTO languages_iso639 VALUES ('cuo', '   ', '   ', '  ', 'I', 'E', 'Cumanagoto', false, '');
INSERT INTO languages_iso639 VALUES ('cup', '   ', '   ', '  ', 'I', 'E', 'Cupeño', false, '');
INSERT INTO languages_iso639 VALUES ('cuq', '   ', '   ', '  ', 'I', 'L', 'Cun', false, '');
INSERT INTO languages_iso639 VALUES ('cur', '   ', '   ', '  ', 'I', 'L', 'Chhulung', false, '');
INSERT INTO languages_iso639 VALUES ('cut', '   ', '   ', '  ', 'I', 'L', 'Teutila Cuicatec', false, '');
INSERT INTO languages_iso639 VALUES ('cuu', '   ', '   ', '  ', 'I', 'L', 'Tai Ya', false, '');
INSERT INTO languages_iso639 VALUES ('cuv', '   ', '   ', '  ', 'I', 'L', 'Cuvok', false, '');
INSERT INTO languages_iso639 VALUES ('cuw', '   ', '   ', '  ', 'I', 'L', 'Chukwa', false, '');
INSERT INTO languages_iso639 VALUES ('cux', '   ', '   ', '  ', 'I', 'L', 'Tepeuxila Cuicatec', false, '');
INSERT INTO languages_iso639 VALUES ('cvg', '   ', '   ', '  ', 'I', 'L', 'Chug', false, '');
INSERT INTO languages_iso639 VALUES ('cvn', '   ', '   ', '  ', 'I', 'L', 'Valle Nacional Chinantec', false, '');
INSERT INTO languages_iso639 VALUES ('cwa', '   ', '   ', '  ', 'I', 'L', 'Kabwa', false, '');
INSERT INTO languages_iso639 VALUES ('cwb', '   ', '   ', '  ', 'I', 'L', 'Maindo', false, '');
INSERT INTO languages_iso639 VALUES ('cwd', '   ', '   ', '  ', 'I', 'L', 'Woods Cree', false, '');
INSERT INTO languages_iso639 VALUES ('cwe', '   ', '   ', '  ', 'I', 'L', 'Kwere', false, '');
INSERT INTO languages_iso639 VALUES ('cwg', '   ', '   ', '  ', 'I', 'L', 'Chewong', false, '');
INSERT INTO languages_iso639 VALUES ('cwt', '   ', '   ', '  ', 'I', 'L', 'Kuwaataay', false, '');
INSERT INTO languages_iso639 VALUES ('cya', '   ', '   ', '  ', 'I', 'L', 'Nopala Chatino', false, '');
INSERT INTO languages_iso639 VALUES ('cyb', '   ', '   ', '  ', 'I', 'E', 'Cayubaba', false, '');
INSERT INTO languages_iso639 VALUES ('cym', 'wel', 'cym', 'cy', 'I', 'L', 'Welsh', false, '');
INSERT INTO languages_iso639 VALUES ('cyo', '   ', '   ', '  ', 'I', 'L', 'Cuyonon', false, '');
INSERT INTO languages_iso639 VALUES ('czh', '   ', '   ', '  ', 'I', 'L', 'Huizhou Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('czk', '   ', '   ', '  ', 'I', 'E', 'Knaanic', false, '');
INSERT INTO languages_iso639 VALUES ('czn', '   ', '   ', '  ', 'I', 'L', 'Zenzontepec Chatino', false, '');
INSERT INTO languages_iso639 VALUES ('czo', '   ', '   ', '  ', 'I', 'L', 'Min Zhong Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('czt', '   ', '   ', '  ', 'I', 'L', 'Zotung Chin', false, '');
INSERT INTO languages_iso639 VALUES ('daa', '   ', '   ', '  ', 'I', 'L', 'Dangaléat', false, '');
INSERT INTO languages_iso639 VALUES ('dac', '   ', '   ', '  ', 'I', 'L', 'Dambi', false, '');
INSERT INTO languages_iso639 VALUES ('dad', '   ', '   ', '  ', 'I', 'L', 'Marik', false, '');
INSERT INTO languages_iso639 VALUES ('dae', '   ', '   ', '  ', 'I', 'L', 'Duupa', false, '');
INSERT INTO languages_iso639 VALUES ('dag', '   ', '   ', '  ', 'I', 'L', 'Dagbani', false, '');
INSERT INTO languages_iso639 VALUES ('dah', '   ', '   ', '  ', 'I', 'L', 'Gwahatike', false, '');
INSERT INTO languages_iso639 VALUES ('dai', '   ', '   ', '  ', 'I', 'L', 'Day', false, '');
INSERT INTO languages_iso639 VALUES ('daj', '   ', '   ', '  ', 'I', 'L', 'Dar Fur Daju', false, '');
INSERT INTO languages_iso639 VALUES ('dak', 'dak', 'dak', '  ', 'I', 'L', 'Dakota', false, '');
INSERT INTO languages_iso639 VALUES ('dal', '   ', '   ', '  ', 'I', 'L', 'Dahalo', false, '');
INSERT INTO languages_iso639 VALUES ('dam', '   ', '   ', '  ', 'I', 'L', 'Damakawa', false, '');
INSERT INTO languages_iso639 VALUES ('dan', 'dan', 'dan', 'da', 'I', 'L', 'Danish', false, '');
INSERT INTO languages_iso639 VALUES ('dao', '   ', '   ', '  ', 'I', 'L', 'Daai Chin', false, '');
INSERT INTO languages_iso639 VALUES ('daq', '   ', '   ', '  ', 'I', 'L', 'Dandami Maria', false, '');
INSERT INTO languages_iso639 VALUES ('dar', 'dar', 'dar', '  ', 'I', 'L', 'Dargwa', false, '');
INSERT INTO languages_iso639 VALUES ('das', '   ', '   ', '  ', 'I', 'L', 'Daho-Doo', false, '');
INSERT INTO languages_iso639 VALUES ('dau', '   ', '   ', '  ', 'I', 'L', 'Dar Sila Daju', false, '');
INSERT INTO languages_iso639 VALUES ('dav', '   ', '   ', '  ', 'I', 'L', 'Taita', false, '');
INSERT INTO languages_iso639 VALUES ('daw', '   ', '   ', '  ', 'I', 'L', 'Davawenyo', false, '');
INSERT INTO languages_iso639 VALUES ('dax', '   ', '   ', '  ', 'I', 'L', 'Dayi', false, '');
INSERT INTO languages_iso639 VALUES ('daz', '   ', '   ', '  ', 'I', 'L', 'Dao', false, '');
INSERT INTO languages_iso639 VALUES ('dba', '   ', '   ', '  ', 'I', 'L', 'Bangime', false, '');
INSERT INTO languages_iso639 VALUES ('dbb', '   ', '   ', '  ', 'I', 'L', 'Deno', false, '');
INSERT INTO languages_iso639 VALUES ('dbd', '   ', '   ', '  ', 'I', 'L', 'Dadiya', false, '');
INSERT INTO languages_iso639 VALUES ('dbe', '   ', '   ', '  ', 'I', 'L', 'Dabe', false, '');
INSERT INTO languages_iso639 VALUES ('dbf', '   ', '   ', '  ', 'I', 'L', 'Edopi', false, '');
INSERT INTO languages_iso639 VALUES ('dbg', '   ', '   ', '  ', 'I', 'L', 'Dogul Dom Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dbi', '   ', '   ', '  ', 'I', 'L', 'Doka', false, '');
INSERT INTO languages_iso639 VALUES ('dbj', '   ', '   ', '  ', 'I', 'L', 'Ida''an', false, '');
INSERT INTO languages_iso639 VALUES ('dbl', '   ', '   ', '  ', 'I', 'L', 'Dyirbal', false, '');
INSERT INTO languages_iso639 VALUES ('dbm', '   ', '   ', '  ', 'I', 'L', 'Duguri', false, '');
INSERT INTO languages_iso639 VALUES ('dbn', '   ', '   ', '  ', 'I', 'L', 'Duriankere', false, '');
INSERT INTO languages_iso639 VALUES ('dbo', '   ', '   ', '  ', 'I', 'L', 'Dulbu', false, '');
INSERT INTO languages_iso639 VALUES ('dbp', '   ', '   ', '  ', 'I', 'L', 'Duwai', false, '');
INSERT INTO languages_iso639 VALUES ('dbq', '   ', '   ', '  ', 'I', 'L', 'Daba', false, '');
INSERT INTO languages_iso639 VALUES ('dbr', '   ', '   ', '  ', 'I', 'L', 'Dabarre', false, '');
INSERT INTO languages_iso639 VALUES ('dbt', '   ', '   ', '  ', 'I', 'L', 'Ben Tey Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dbu', '   ', '   ', '  ', 'I', 'L', 'Bondum Dom Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dbv', '   ', '   ', '  ', 'I', 'L', 'Dungu', false, '');
INSERT INTO languages_iso639 VALUES ('dbw', '   ', '   ', '  ', 'I', 'L', 'Bankan Tey Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dby', '   ', '   ', '  ', 'I', 'L', 'Dibiyaso', false, '');
INSERT INTO languages_iso639 VALUES ('dcc', '   ', '   ', '  ', 'I', 'L', 'Deccan', false, '');
INSERT INTO languages_iso639 VALUES ('dcr', '   ', '   ', '  ', 'I', 'E', 'Negerhollands', false, '');
INSERT INTO languages_iso639 VALUES ('dda', '   ', '   ', '  ', 'I', 'E', 'Dadi Dadi', false, '');
INSERT INTO languages_iso639 VALUES ('ddd', '   ', '   ', '  ', 'I', 'L', 'Dongotono', false, '');
INSERT INTO languages_iso639 VALUES ('dde', '   ', '   ', '  ', 'I', 'L', 'Doondo', false, '');
INSERT INTO languages_iso639 VALUES ('ddg', '   ', '   ', '  ', 'I', 'L', 'Fataluku', false, '');
INSERT INTO languages_iso639 VALUES ('ddi', '   ', '   ', '  ', 'I', 'L', 'West Goodenough', false, '');
INSERT INTO languages_iso639 VALUES ('ddj', '   ', '   ', '  ', 'I', 'L', 'Jaru', false, '');
INSERT INTO languages_iso639 VALUES ('ddn', '   ', '   ', '  ', 'I', 'L', 'Dendi (Benin)', false, '');
INSERT INTO languages_iso639 VALUES ('ddo', '   ', '   ', '  ', 'I', 'L', 'Dido', false, '');
INSERT INTO languages_iso639 VALUES ('ddr', '   ', '   ', '  ', 'I', 'E', 'Dhudhuroa', false, '');
INSERT INTO languages_iso639 VALUES ('dds', '   ', '   ', '  ', 'I', 'L', 'Donno So Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('ddw', '   ', '   ', '  ', 'I', 'L', 'Dawera-Daweloor', false, '');
INSERT INTO languages_iso639 VALUES ('dec', '   ', '   ', '  ', 'I', 'L', 'Dagik', false, '');
INSERT INTO languages_iso639 VALUES ('ded', '   ', '   ', '  ', 'I', 'L', 'Dedua', false, '');
INSERT INTO languages_iso639 VALUES ('dee', '   ', '   ', '  ', 'I', 'L', 'Dewoin', false, '');
INSERT INTO languages_iso639 VALUES ('def', '   ', '   ', '  ', 'I', 'L', 'Dezfuli', false, '');
INSERT INTO languages_iso639 VALUES ('deg', '   ', '   ', '  ', 'I', 'L', 'Degema', false, '');
INSERT INTO languages_iso639 VALUES ('deh', '   ', '   ', '  ', 'I', 'L', 'Dehwari', false, '');
INSERT INTO languages_iso639 VALUES ('dei', '   ', '   ', '  ', 'I', 'L', 'Demisa', false, '');
INSERT INTO languages_iso639 VALUES ('dek', '   ', '   ', '  ', 'I', 'L', 'Dek', false, '');
INSERT INTO languages_iso639 VALUES ('del', 'del', 'del', '  ', 'M', 'L', 'Delaware', false, '');
INSERT INTO languages_iso639 VALUES ('dem', '   ', '   ', '  ', 'I', 'L', 'Dem', false, '');
INSERT INTO languages_iso639 VALUES ('den', 'den', 'den', '  ', 'M', 'L', 'Slave (Athapascan)', false, '');
INSERT INTO languages_iso639 VALUES ('dep', '   ', '   ', '  ', 'I', 'E', 'Pidgin Delaware', false, '');
INSERT INTO languages_iso639 VALUES ('deq', '   ', '   ', '  ', 'I', 'L', 'Dendi (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('der', '   ', '   ', '  ', 'I', 'L', 'Deori', false, '');
INSERT INTO languages_iso639 VALUES ('des', '   ', '   ', '  ', 'I', 'L', 'Desano', false, '');
INSERT INTO languages_iso639 VALUES ('deu', 'ger', 'deu', 'de', 'I', 'L', 'German', false, '');
INSERT INTO languages_iso639 VALUES ('dev', '   ', '   ', '  ', 'I', 'L', 'Domung', false, '');
INSERT INTO languages_iso639 VALUES ('dez', '   ', '   ', '  ', 'I', 'L', 'Dengese', false, '');
INSERT INTO languages_iso639 VALUES ('dga', '   ', '   ', '  ', 'I', 'L', 'Southern Dagaare', false, '');
INSERT INTO languages_iso639 VALUES ('dgb', '   ', '   ', '  ', 'I', 'L', 'Bunoge Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dgc', '   ', '   ', '  ', 'I', 'L', 'Casiguran Dumagat Agta', false, '');
INSERT INTO languages_iso639 VALUES ('dgd', '   ', '   ', '  ', 'I', 'L', 'Dagaari Dioula', false, '');
INSERT INTO languages_iso639 VALUES ('dge', '   ', '   ', '  ', 'I', 'L', 'Degenan', false, '');
INSERT INTO languages_iso639 VALUES ('dgg', '   ', '   ', '  ', 'I', 'L', 'Doga', false, '');
INSERT INTO languages_iso639 VALUES ('dgh', '   ', '   ', '  ', 'I', 'L', 'Dghwede', false, '');
INSERT INTO languages_iso639 VALUES ('dgi', '   ', '   ', '  ', 'I', 'L', 'Northern Dagara', false, '');
INSERT INTO languages_iso639 VALUES ('dgk', '   ', '   ', '  ', 'I', 'L', 'Dagba', false, '');
INSERT INTO languages_iso639 VALUES ('dgl', '   ', '   ', '  ', 'I', 'L', 'Andaandi', false, '');
INSERT INTO languages_iso639 VALUES ('dgn', '   ', '   ', '  ', 'I', 'E', 'Dagoman', false, '');
INSERT INTO languages_iso639 VALUES ('dgo', '   ', '   ', '  ', 'I', 'L', 'Dogri (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('dgr', 'dgr', 'dgr', '  ', 'I', 'L', 'Dogrib', false, '');
INSERT INTO languages_iso639 VALUES ('dgs', '   ', '   ', '  ', 'I', 'L', 'Dogoso', false, '');
INSERT INTO languages_iso639 VALUES ('dgt', '   ', '   ', '  ', 'I', 'E', 'Ndrag''ngith', false, '');
INSERT INTO languages_iso639 VALUES ('dgu', '   ', '   ', '  ', 'I', 'L', 'Degaru', false, '');
INSERT INTO languages_iso639 VALUES ('dgw', '   ', '   ', '  ', 'I', 'E', 'Daungwurrung', false, '');
INSERT INTO languages_iso639 VALUES ('dgx', '   ', '   ', '  ', 'I', 'L', 'Doghoro', false, '');
INSERT INTO languages_iso639 VALUES ('dgz', '   ', '   ', '  ', 'I', 'L', 'Daga', false, '');
INSERT INTO languages_iso639 VALUES ('dhd', '   ', '   ', '  ', 'I', 'L', 'Dhundari', false, '');
INSERT INTO languages_iso639 VALUES ('dhg', '   ', '   ', '  ', 'I', 'L', 'Djangu', false, '');
INSERT INTO languages_iso639 VALUES ('dhi', '   ', '   ', '  ', 'I', 'L', 'Dhimal', false, '');
INSERT INTO languages_iso639 VALUES ('dhl', '   ', '   ', '  ', 'I', 'L', 'Dhalandji', false, '');
INSERT INTO languages_iso639 VALUES ('dhm', '   ', '   ', '  ', 'I', 'L', 'Zemba', false, '');
INSERT INTO languages_iso639 VALUES ('dhn', '   ', '   ', '  ', 'I', 'L', 'Dhanki', false, '');
INSERT INTO languages_iso639 VALUES ('dho', '   ', '   ', '  ', 'I', 'L', 'Dhodia', false, '');
INSERT INTO languages_iso639 VALUES ('dhr', '   ', '   ', '  ', 'I', 'L', 'Dhargari', false, '');
INSERT INTO languages_iso639 VALUES ('dhs', '   ', '   ', '  ', 'I', 'L', 'Dhaiso', false, '');
INSERT INTO languages_iso639 VALUES ('dhu', '   ', '   ', '  ', 'I', 'E', 'Dhurga', false, '');
INSERT INTO languages_iso639 VALUES ('dhv', '   ', '   ', '  ', 'I', 'L', 'Dehu', false, '');
INSERT INTO languages_iso639 VALUES ('dhw', '   ', '   ', '  ', 'I', 'L', 'Dhanwar (Nepal)', false, '');
INSERT INTO languages_iso639 VALUES ('dhx', '   ', '   ', '  ', 'I', 'L', 'Dhungaloo', false, '');
INSERT INTO languages_iso639 VALUES ('dia', '   ', '   ', '  ', 'I', 'L', 'Dia', false, '');
INSERT INTO languages_iso639 VALUES ('dib', '   ', '   ', '  ', 'I', 'L', 'South Central Dinka', false, '');
INSERT INTO languages_iso639 VALUES ('dic', '   ', '   ', '  ', 'I', 'L', 'Lakota Dida', false, '');
INSERT INTO languages_iso639 VALUES ('did', '   ', '   ', '  ', 'I', 'L', 'Didinga', false, '');
INSERT INTO languages_iso639 VALUES ('dif', '   ', '   ', '  ', 'I', 'E', 'Dieri', false, '');
INSERT INTO languages_iso639 VALUES ('dig', '   ', '   ', '  ', 'I', 'L', 'Digo', false, '');
INSERT INTO languages_iso639 VALUES ('dih', '   ', '   ', '  ', 'I', 'L', 'Kumiai', false, '');
INSERT INTO languages_iso639 VALUES ('dii', '   ', '   ', '  ', 'I', 'L', 'Dimbong', false, '');
INSERT INTO languages_iso639 VALUES ('dij', '   ', '   ', '  ', 'I', 'L', 'Dai', false, '');
INSERT INTO languages_iso639 VALUES ('dik', '   ', '   ', '  ', 'I', 'L', 'Southwestern Dinka', false, '');
INSERT INTO languages_iso639 VALUES ('dil', '   ', '   ', '  ', 'I', 'L', 'Dilling', false, '');
INSERT INTO languages_iso639 VALUES ('dim', '   ', '   ', '  ', 'I', 'L', 'Dime', false, '');
INSERT INTO languages_iso639 VALUES ('din', 'din', 'din', '  ', 'M', 'L', 'Dinka', false, '');
INSERT INTO languages_iso639 VALUES ('dio', '   ', '   ', '  ', 'I', 'L', 'Dibo', false, '');
INSERT INTO languages_iso639 VALUES ('dip', '   ', '   ', '  ', 'I', 'L', 'Northeastern Dinka', false, '');
INSERT INTO languages_iso639 VALUES ('diq', '   ', '   ', '  ', 'I', 'L', 'Dimli (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('dir', '   ', '   ', '  ', 'I', 'L', 'Dirim', false, '');
INSERT INTO languages_iso639 VALUES ('dis', '   ', '   ', '  ', 'I', 'L', 'Dimasa', false, '');
INSERT INTO languages_iso639 VALUES ('dit', '   ', '   ', '  ', 'I', 'E', 'Dirari', false, '');
INSERT INTO languages_iso639 VALUES ('diu', '   ', '   ', '  ', 'I', 'L', 'Diriku', false, '');
INSERT INTO languages_iso639 VALUES ('div', 'div', 'div', 'dv', 'I', 'L', 'Dhivehi', false, '');
INSERT INTO languages_iso639 VALUES ('diw', '   ', '   ', '  ', 'I', 'L', 'Northwestern Dinka', false, '');
INSERT INTO languages_iso639 VALUES ('dix', '   ', '   ', '  ', 'I', 'L', 'Dixon Reef', false, '');
INSERT INTO languages_iso639 VALUES ('diy', '   ', '   ', '  ', 'I', 'L', 'Diuwe', false, '');
INSERT INTO languages_iso639 VALUES ('diz', '   ', '   ', '  ', 'I', 'L', 'Ding', false, '');
INSERT INTO languages_iso639 VALUES ('dja', '   ', '   ', '  ', 'I', 'E', 'Djadjawurrung', false, '');
INSERT INTO languages_iso639 VALUES ('djb', '   ', '   ', '  ', 'I', 'L', 'Djinba', false, '');
INSERT INTO languages_iso639 VALUES ('djc', '   ', '   ', '  ', 'I', 'L', 'Dar Daju Daju', false, '');
INSERT INTO languages_iso639 VALUES ('djd', '   ', '   ', '  ', 'I', 'L', 'Djamindjung', false, '');
INSERT INTO languages_iso639 VALUES ('dje', '   ', '   ', '  ', 'I', 'L', 'Zarma', false, '');
INSERT INTO languages_iso639 VALUES ('djf', '   ', '   ', '  ', 'I', 'E', 'Djangun', false, '');
INSERT INTO languages_iso639 VALUES ('dji', '   ', '   ', '  ', 'I', 'L', 'Djinang', false, '');
INSERT INTO languages_iso639 VALUES ('djj', '   ', '   ', '  ', 'I', 'L', 'Djeebbana', false, '');
INSERT INTO languages_iso639 VALUES ('djk', '   ', '   ', '  ', 'I', 'L', 'Eastern Maroon Creole', false, '');
INSERT INTO languages_iso639 VALUES ('djm', '   ', '   ', '  ', 'I', 'L', 'Jamsay Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('djn', '   ', '   ', '  ', 'I', 'L', 'Djauan', false, '');
INSERT INTO languages_iso639 VALUES ('djo', '   ', '   ', '  ', 'I', 'L', 'Jangkang', false, '');
INSERT INTO languages_iso639 VALUES ('djr', '   ', '   ', '  ', 'I', 'L', 'Djambarrpuyngu', false, '');
INSERT INTO languages_iso639 VALUES ('dju', '   ', '   ', '  ', 'I', 'L', 'Kapriman', false, '');
INSERT INTO languages_iso639 VALUES ('djw', '   ', '   ', '  ', 'I', 'E', 'Djawi', false, '');
INSERT INTO languages_iso639 VALUES ('dka', '   ', '   ', '  ', 'I', 'L', 'Dakpakha', false, '');
INSERT INTO languages_iso639 VALUES ('dkk', '   ', '   ', '  ', 'I', 'L', 'Dakka', false, '');
INSERT INTO languages_iso639 VALUES ('dkr', '   ', '   ', '  ', 'I', 'L', 'Kuijau', false, '');
INSERT INTO languages_iso639 VALUES ('dks', '   ', '   ', '  ', 'I', 'L', 'Southeastern Dinka', false, '');
INSERT INTO languages_iso639 VALUES ('dkx', '   ', '   ', '  ', 'I', 'L', 'Mazagway', false, '');
INSERT INTO languages_iso639 VALUES ('dlg', '   ', '   ', '  ', 'I', 'L', 'Dolgan', false, '');
INSERT INTO languages_iso639 VALUES ('dlk', '   ', '   ', '  ', 'I', 'L', 'Dahalik', false, '');
INSERT INTO languages_iso639 VALUES ('dlm', '   ', '   ', '  ', 'I', 'E', 'Dalmatian', false, '');
INSERT INTO languages_iso639 VALUES ('dln', '   ', '   ', '  ', 'I', 'L', 'Darlong', false, '');
INSERT INTO languages_iso639 VALUES ('dma', '   ', '   ', '  ', 'I', 'L', 'Duma', false, '');
INSERT INTO languages_iso639 VALUES ('dmb', '   ', '   ', '  ', 'I', 'L', 'Mombo Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dmc', '   ', '   ', '  ', 'I', 'L', 'Gavak', false, '');
INSERT INTO languages_iso639 VALUES ('dmd', '   ', '   ', '  ', 'I', 'E', 'Madhi Madhi', false, '');
INSERT INTO languages_iso639 VALUES ('dme', '   ', '   ', '  ', 'I', 'L', 'Dugwor', false, '');
INSERT INTO languages_iso639 VALUES ('dmg', '   ', '   ', '  ', 'I', 'L', 'Upper Kinabatangan', false, '');
INSERT INTO languages_iso639 VALUES ('dmk', '   ', '   ', '  ', 'I', 'L', 'Domaaki', false, '');
INSERT INTO languages_iso639 VALUES ('dml', '   ', '   ', '  ', 'I', 'L', 'Dameli', false, '');
INSERT INTO languages_iso639 VALUES ('dmm', '   ', '   ', '  ', 'I', 'L', 'Dama', false, '');
INSERT INTO languages_iso639 VALUES ('dmo', '   ', '   ', '  ', 'I', 'L', 'Kemedzung', false, '');
INSERT INTO languages_iso639 VALUES ('dmr', '   ', '   ', '  ', 'I', 'L', 'East Damar', false, '');
INSERT INTO languages_iso639 VALUES ('dms', '   ', '   ', '  ', 'I', 'L', 'Dampelas', false, '');
INSERT INTO languages_iso639 VALUES ('dmu', '   ', '   ', '  ', 'I', 'L', 'Dubu', false, '');
INSERT INTO languages_iso639 VALUES ('dmv', '   ', '   ', '  ', 'I', 'L', 'Dumpas', false, '');
INSERT INTO languages_iso639 VALUES ('dmw', '   ', '   ', '  ', 'I', 'L', 'Mudburra', false, '');
INSERT INTO languages_iso639 VALUES ('dmx', '   ', '   ', '  ', 'I', 'L', 'Dema', false, '');
INSERT INTO languages_iso639 VALUES ('dmy', '   ', '   ', '  ', 'I', 'L', 'Demta', false, '');
INSERT INTO languages_iso639 VALUES ('dna', '   ', '   ', '  ', 'I', 'L', 'Upper Grand Valley Dani', false, '');
INSERT INTO languages_iso639 VALUES ('dnd', '   ', '   ', '  ', 'I', 'L', 'Daonda', false, '');
INSERT INTO languages_iso639 VALUES ('dne', '   ', '   ', '  ', 'I', 'L', 'Ndendeule', false, '');
INSERT INTO languages_iso639 VALUES ('dng', '   ', '   ', '  ', 'I', 'L', 'Dungan', false, '');
INSERT INTO languages_iso639 VALUES ('dni', '   ', '   ', '  ', 'I', 'L', 'Lower Grand Valley Dani', false, '');
INSERT INTO languages_iso639 VALUES ('dnj', '   ', '   ', '  ', 'I', 'L', 'Dan', false, '');
INSERT INTO languages_iso639 VALUES ('dnk', '   ', '   ', '  ', 'I', 'L', 'Dengka', false, '');
INSERT INTO languages_iso639 VALUES ('dnn', '   ', '   ', '  ', 'I', 'L', 'Dzùùngoo', false, '');
INSERT INTO languages_iso639 VALUES ('dnr', '   ', '   ', '  ', 'I', 'L', 'Danaru', false, '');
INSERT INTO languages_iso639 VALUES ('dnt', '   ', '   ', '  ', 'I', 'L', 'Mid Grand Valley Dani', false, '');
INSERT INTO languages_iso639 VALUES ('dnu', '   ', '   ', '  ', 'I', 'L', 'Danau', false, '');
INSERT INTO languages_iso639 VALUES ('dnv', '   ', '   ', '  ', 'I', 'L', 'Danu', false, '');
INSERT INTO languages_iso639 VALUES ('dnw', '   ', '   ', '  ', 'I', 'L', 'Western Dani', false, '');
INSERT INTO languages_iso639 VALUES ('dny', '   ', '   ', '  ', 'I', 'L', 'Dení', false, '');
INSERT INTO languages_iso639 VALUES ('doa', '   ', '   ', '  ', 'I', 'L', 'Dom', false, '');
INSERT INTO languages_iso639 VALUES ('dob', '   ', '   ', '  ', 'I', 'L', 'Dobu', false, '');
INSERT INTO languages_iso639 VALUES ('doc', '   ', '   ', '  ', 'I', 'L', 'Northern Dong', false, '');
INSERT INTO languages_iso639 VALUES ('doe', '   ', '   ', '  ', 'I', 'L', 'Doe', false, '');
INSERT INTO languages_iso639 VALUES ('dof', '   ', '   ', '  ', 'I', 'L', 'Domu', false, '');
INSERT INTO languages_iso639 VALUES ('doh', '   ', '   ', '  ', 'I', 'L', 'Dong', false, '');
INSERT INTO languages_iso639 VALUES ('doi', 'doi', 'doi', '  ', 'M', 'L', 'Dogri (macrolanguage)', false, '');
INSERT INTO languages_iso639 VALUES ('dok', '   ', '   ', '  ', 'I', 'L', 'Dondo', false, '');
INSERT INTO languages_iso639 VALUES ('dol', '   ', '   ', '  ', 'I', 'L', 'Doso', false, '');
INSERT INTO languages_iso639 VALUES ('don', '   ', '   ', '  ', 'I', 'L', 'Toura (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('doo', '   ', '   ', '  ', 'I', 'L', 'Dongo', false, '');
INSERT INTO languages_iso639 VALUES ('dop', '   ', '   ', '  ', 'I', 'L', 'Lukpa', false, '');
INSERT INTO languages_iso639 VALUES ('doq', '   ', '   ', '  ', 'I', 'L', 'Dominican Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('dor', '   ', '   ', '  ', 'I', 'L', 'Dori''o', false, '');
INSERT INTO languages_iso639 VALUES ('dos', '   ', '   ', '  ', 'I', 'L', 'Dogosé', false, '');
INSERT INTO languages_iso639 VALUES ('dot', '   ', '   ', '  ', 'I', 'L', 'Dass', false, '');
INSERT INTO languages_iso639 VALUES ('dov', '   ', '   ', '  ', 'I', 'L', 'Dombe', false, '');
INSERT INTO languages_iso639 VALUES ('dow', '   ', '   ', '  ', 'I', 'L', 'Doyayo', false, '');
INSERT INTO languages_iso639 VALUES ('dox', '   ', '   ', '  ', 'I', 'L', 'Bussa', false, '');
INSERT INTO languages_iso639 VALUES ('doy', '   ', '   ', '  ', 'I', 'L', 'Dompo', false, '');
INSERT INTO languages_iso639 VALUES ('doz', '   ', '   ', '  ', 'I', 'L', 'Dorze', false, '');
INSERT INTO languages_iso639 VALUES ('dpp', '   ', '   ', '  ', 'I', 'L', 'Papar', false, '');
INSERT INTO languages_iso639 VALUES ('drb', '   ', '   ', '  ', 'I', 'L', 'Dair', false, '');
INSERT INTO languages_iso639 VALUES ('drc', '   ', '   ', '  ', 'I', 'L', 'Minderico', false, '');
INSERT INTO languages_iso639 VALUES ('drd', '   ', '   ', '  ', 'I', 'L', 'Darmiya', false, '');
INSERT INTO languages_iso639 VALUES ('dre', '   ', '   ', '  ', 'I', 'L', 'Dolpo', false, '');
INSERT INTO languages_iso639 VALUES ('drg', '   ', '   ', '  ', 'I', 'L', 'Rungus', false, '');
INSERT INTO languages_iso639 VALUES ('dri', '   ', '   ', '  ', 'I', 'L', 'C''lela', false, '');
INSERT INTO languages_iso639 VALUES ('drl', '   ', '   ', '  ', 'I', 'L', 'Paakantyi', false, '');
INSERT INTO languages_iso639 VALUES ('drn', '   ', '   ', '  ', 'I', 'L', 'West Damar', false, '');
INSERT INTO languages_iso639 VALUES ('dro', '   ', '   ', '  ', 'I', 'L', 'Daro-Matu Melanau', false, '');
INSERT INTO languages_iso639 VALUES ('drq', '   ', '   ', '  ', 'I', 'E', 'Dura', false, '');
INSERT INTO languages_iso639 VALUES ('drr', '   ', '   ', '  ', 'I', 'E', 'Dororo', false, '');
INSERT INTO languages_iso639 VALUES ('drs', '   ', '   ', '  ', 'I', 'L', 'Gedeo', false, '');
INSERT INTO languages_iso639 VALUES ('drt', '   ', '   ', '  ', 'I', 'L', 'Drents', false, '');
INSERT INTO languages_iso639 VALUES ('dru', '   ', '   ', '  ', 'I', 'L', 'Rukai', false, '');
INSERT INTO languages_iso639 VALUES ('dry', '   ', '   ', '  ', 'I', 'L', 'Darai', false, '');
INSERT INTO languages_iso639 VALUES ('dsb', 'dsb', 'dsb', '  ', 'I', 'L', 'Lower Sorbian', false, '');
INSERT INTO languages_iso639 VALUES ('dse', '   ', '   ', '  ', 'I', 'L', 'Dutch Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('dsh', '   ', '   ', '  ', 'I', 'L', 'Daasanach', false, '');
INSERT INTO languages_iso639 VALUES ('dsi', '   ', '   ', '  ', 'I', 'L', 'Disa', false, '');
INSERT INTO languages_iso639 VALUES ('dsl', '   ', '   ', '  ', 'I', 'L', 'Danish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('dsn', '   ', '   ', '  ', 'I', 'L', 'Dusner', false, '');
INSERT INTO languages_iso639 VALUES ('dso', '   ', '   ', '  ', 'I', 'L', 'Desiya', false, '');
INSERT INTO languages_iso639 VALUES ('dsq', '   ', '   ', '  ', 'I', 'L', 'Tadaksahak', false, '');
INSERT INTO languages_iso639 VALUES ('dta', '   ', '   ', '  ', 'I', 'L', 'Daur', false, '');
INSERT INTO languages_iso639 VALUES ('dtb', '   ', '   ', '  ', 'I', 'L', 'Labuk-Kinabatangan Kadazan', false, '');
INSERT INTO languages_iso639 VALUES ('dtd', '   ', '   ', '  ', 'I', 'L', 'Ditidaht', false, '');
INSERT INTO languages_iso639 VALUES ('dth', '   ', '   ', '  ', 'I', 'E', 'Adithinngithigh', false, '');
INSERT INTO languages_iso639 VALUES ('dti', '   ', '   ', '  ', 'I', 'L', 'Ana Tinga Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dtk', '   ', '   ', '  ', 'I', 'L', 'Tene Kan Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dtm', '   ', '   ', '  ', 'I', 'L', 'Tomo Kan Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dto', '   ', '   ', '  ', 'I', 'L', 'Tommo So Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dtp', '   ', '   ', '  ', 'I', 'L', 'Central Dusun', false, '');
INSERT INTO languages_iso639 VALUES ('dtr', '   ', '   ', '  ', 'I', 'L', 'Lotud', false, '');
INSERT INTO languages_iso639 VALUES ('dts', '   ', '   ', '  ', 'I', 'L', 'Toro So Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dtt', '   ', '   ', '  ', 'I', 'L', 'Toro Tegu Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dtu', '   ', '   ', '  ', 'I', 'L', 'Tebul Ure Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dty', '   ', '   ', '  ', 'I', 'L', 'Dotyali', false, '');
INSERT INTO languages_iso639 VALUES ('dua', 'dua', 'dua', '  ', 'I', 'L', 'Duala', false, '');
INSERT INTO languages_iso639 VALUES ('dub', '   ', '   ', '  ', 'I', 'L', 'Dubli', false, '');
INSERT INTO languages_iso639 VALUES ('duc', '   ', '   ', '  ', 'I', 'L', 'Duna', false, '');
INSERT INTO languages_iso639 VALUES ('dud', '   ', '   ', '  ', 'I', 'L', 'Hun-Saare', false, '');
INSERT INTO languages_iso639 VALUES ('due', '   ', '   ', '  ', 'I', 'L', 'Umiray Dumaget Agta', false, '');
INSERT INTO languages_iso639 VALUES ('duf', '   ', '   ', '  ', 'I', 'L', 'Dumbea', false, '');
INSERT INTO languages_iso639 VALUES ('dug', '   ', '   ', '  ', 'I', 'L', 'Duruma', false, '');
INSERT INTO languages_iso639 VALUES ('duh', '   ', '   ', '  ', 'I', 'L', 'Dungra Bhil', false, '');
INSERT INTO languages_iso639 VALUES ('dui', '   ', '   ', '  ', 'I', 'L', 'Dumun', false, '');
INSERT INTO languages_iso639 VALUES ('duj', '   ', '   ', '  ', 'I', 'L', 'Dhuwal', false, '');
INSERT INTO languages_iso639 VALUES ('duk', '   ', '   ', '  ', 'I', 'L', 'Uyajitaya', false, '');
INSERT INTO languages_iso639 VALUES ('dul', '   ', '   ', '  ', 'I', 'L', 'Alabat Island Agta', false, '');
INSERT INTO languages_iso639 VALUES ('dum', 'dum', 'dum', '  ', 'I', 'H', 'Middle Dutch (ca. 1050-1350)', false, '');
INSERT INTO languages_iso639 VALUES ('dun', '   ', '   ', '  ', 'I', 'L', 'Dusun Deyah', false, '');
INSERT INTO languages_iso639 VALUES ('duo', '   ', '   ', '  ', 'I', 'L', 'Dupaninan Agta', false, '');
INSERT INTO languages_iso639 VALUES ('dup', '   ', '   ', '  ', 'I', 'L', 'Duano', false, '');
INSERT INTO languages_iso639 VALUES ('duq', '   ', '   ', '  ', 'I', 'L', 'Dusun Malang', false, '');
INSERT INTO languages_iso639 VALUES ('dur', '   ', '   ', '  ', 'I', 'L', 'Dii', false, '');
INSERT INTO languages_iso639 VALUES ('dus', '   ', '   ', '  ', 'I', 'L', 'Dumi', false, '');
INSERT INTO languages_iso639 VALUES ('duu', '   ', '   ', '  ', 'I', 'L', 'Drung', false, '');
INSERT INTO languages_iso639 VALUES ('duv', '   ', '   ', '  ', 'I', 'L', 'Duvle', false, '');
INSERT INTO languages_iso639 VALUES ('duw', '   ', '   ', '  ', 'I', 'L', 'Dusun Witu', false, '');
INSERT INTO languages_iso639 VALUES ('dux', '   ', '   ', '  ', 'I', 'L', 'Duungooma', false, '');
INSERT INTO languages_iso639 VALUES ('duy', '   ', '   ', '  ', 'I', 'E', 'Dicamay Agta', false, '');
INSERT INTO languages_iso639 VALUES ('duz', '   ', '   ', '  ', 'I', 'E', 'Duli', false, '');
INSERT INTO languages_iso639 VALUES ('dva', '   ', '   ', '  ', 'I', 'L', 'Duau', false, '');
INSERT INTO languages_iso639 VALUES ('dwa', '   ', '   ', '  ', 'I', 'L', 'Diri', false, '');
INSERT INTO languages_iso639 VALUES ('dwr', '   ', '   ', '  ', 'I', 'L', 'Dawro', false, '');
INSERT INTO languages_iso639 VALUES ('dws', '   ', '   ', '  ', 'I', 'C', 'Dutton World Speedwords', false, '');
INSERT INTO languages_iso639 VALUES ('dww', '   ', '   ', '  ', 'I', 'L', 'Dawawa', false, '');
INSERT INTO languages_iso639 VALUES ('dya', '   ', '   ', '  ', 'I', 'L', 'Dyan', false, '');
INSERT INTO languages_iso639 VALUES ('dyb', '   ', '   ', '  ', 'I', 'E', 'Dyaberdyaber', false, '');
INSERT INTO languages_iso639 VALUES ('dyd', '   ', '   ', '  ', 'I', 'E', 'Dyugun', false, '');
INSERT INTO languages_iso639 VALUES ('dyg', '   ', '   ', '  ', 'I', 'E', 'Villa Viciosa Agta', false, '');
INSERT INTO languages_iso639 VALUES ('dyi', '   ', '   ', '  ', 'I', 'L', 'Djimini Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('dym', '   ', '   ', '  ', 'I', 'L', 'Yanda Dom Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('dyn', '   ', '   ', '  ', 'I', 'L', 'Dyangadi', false, '');
INSERT INTO languages_iso639 VALUES ('dyo', '   ', '   ', '  ', 'I', 'L', 'Jola-Fonyi', false, '');
INSERT INTO languages_iso639 VALUES ('dyu', 'dyu', 'dyu', '  ', 'I', 'L', 'Dyula', false, '');
INSERT INTO languages_iso639 VALUES ('dyy', '   ', '   ', '  ', 'I', 'L', 'Dyaabugay', false, '');
INSERT INTO languages_iso639 VALUES ('dza', '   ', '   ', '  ', 'I', 'L', 'Tunzu', false, '');
INSERT INTO languages_iso639 VALUES ('dzd', '   ', '   ', '  ', 'I', 'L', 'Daza', false, '');
INSERT INTO languages_iso639 VALUES ('dze', '   ', '   ', '  ', 'I', 'E', 'Djiwarli', false, '');
INSERT INTO languages_iso639 VALUES ('dzg', '   ', '   ', '  ', 'I', 'L', 'Dazaga', false, '');
INSERT INTO languages_iso639 VALUES ('dzl', '   ', '   ', '  ', 'I', 'L', 'Dzalakha', false, '');
INSERT INTO languages_iso639 VALUES ('dzn', '   ', '   ', '  ', 'I', 'L', 'Dzando', false, '');
INSERT INTO languages_iso639 VALUES ('dzo', 'dzo', 'dzo', 'dz', 'I', 'L', 'Dzongkha', false, '');
INSERT INTO languages_iso639 VALUES ('eaa', '   ', '   ', '  ', 'I', 'E', 'Karenggapa', false, '');
INSERT INTO languages_iso639 VALUES ('ebg', '   ', '   ', '  ', 'I', 'L', 'Ebughu', false, '');
INSERT INTO languages_iso639 VALUES ('ebk', '   ', '   ', '  ', 'I', 'L', 'Eastern Bontok', false, '');
INSERT INTO languages_iso639 VALUES ('ebo', '   ', '   ', '  ', 'I', 'L', 'Teke-Ebo', false, '');
INSERT INTO languages_iso639 VALUES ('ebr', '   ', '   ', '  ', 'I', 'L', 'Ebrié', false, '');
INSERT INTO languages_iso639 VALUES ('ebu', '   ', '   ', '  ', 'I', 'L', 'Embu', false, '');
INSERT INTO languages_iso639 VALUES ('ecr', '   ', '   ', '  ', 'I', 'A', 'Eteocretan', false, '');
INSERT INTO languages_iso639 VALUES ('ecs', '   ', '   ', '  ', 'I', 'L', 'Ecuadorian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ecy', '   ', '   ', '  ', 'I', 'A', 'Eteocypriot', false, '');
INSERT INTO languages_iso639 VALUES ('eee', '   ', '   ', '  ', 'I', 'L', 'E', false, '');
INSERT INTO languages_iso639 VALUES ('efa', '   ', '   ', '  ', 'I', 'L', 'Efai', false, '');
INSERT INTO languages_iso639 VALUES ('efe', '   ', '   ', '  ', 'I', 'L', 'Efe', false, '');
INSERT INTO languages_iso639 VALUES ('efi', 'efi', 'efi', '  ', 'I', 'L', 'Efik', false, '');
INSERT INTO languages_iso639 VALUES ('ega', '   ', '   ', '  ', 'I', 'L', 'Ega', false, '');
INSERT INTO languages_iso639 VALUES ('egl', '   ', '   ', '  ', 'I', 'L', 'Emilian', false, '');
INSERT INTO languages_iso639 VALUES ('ego', '   ', '   ', '  ', 'I', 'L', 'Eggon', false, '');
INSERT INTO languages_iso639 VALUES ('egy', 'egy', 'egy', '  ', 'I', 'A', 'Egyptian (Ancient)', false, '');
INSERT INTO languages_iso639 VALUES ('ehu', '   ', '   ', '  ', 'I', 'L', 'Ehueun', false, '');
INSERT INTO languages_iso639 VALUES ('eip', '   ', '   ', '  ', 'I', 'L', 'Eipomek', false, '');
INSERT INTO languages_iso639 VALUES ('eit', '   ', '   ', '  ', 'I', 'L', 'Eitiep', false, '');
INSERT INTO languages_iso639 VALUES ('eiv', '   ', '   ', '  ', 'I', 'L', 'Askopan', false, '');
INSERT INTO languages_iso639 VALUES ('eja', '   ', '   ', '  ', 'I', 'L', 'Ejamat', false, '');
INSERT INTO languages_iso639 VALUES ('eka', 'eka', 'eka', '  ', 'I', 'L', 'Ekajuk', false, '');
INSERT INTO languages_iso639 VALUES ('ekc', '   ', '   ', '  ', 'I', 'E', 'Eastern Karnic', false, '');
INSERT INTO languages_iso639 VALUES ('eke', '   ', '   ', '  ', 'I', 'L', 'Ekit', false, '');
INSERT INTO languages_iso639 VALUES ('ekg', '   ', '   ', '  ', 'I', 'L', 'Ekari', false, '');
INSERT INTO languages_iso639 VALUES ('eki', '   ', '   ', '  ', 'I', 'L', 'Eki', false, '');
INSERT INTO languages_iso639 VALUES ('ekk', '   ', '   ', '  ', 'I', 'L', 'Standard Estonian', false, '');
INSERT INTO languages_iso639 VALUES ('ekl', '   ', '   ', '  ', 'I', 'L', 'Kol (Bangladesh)', false, '');
INSERT INTO languages_iso639 VALUES ('ekm', '   ', '   ', '  ', 'I', 'L', 'Elip', false, '');
INSERT INTO languages_iso639 VALUES ('eko', '   ', '   ', '  ', 'I', 'L', 'Koti', false, '');
INSERT INTO languages_iso639 VALUES ('ekp', '   ', '   ', '  ', 'I', 'L', 'Ekpeye', false, '');
INSERT INTO languages_iso639 VALUES ('ekr', '   ', '   ', '  ', 'I', 'L', 'Yace', false, '');
INSERT INTO languages_iso639 VALUES ('eky', '   ', '   ', '  ', 'I', 'L', 'Eastern Kayah', false, '');
INSERT INTO languages_iso639 VALUES ('ele', '   ', '   ', '  ', 'I', 'L', 'Elepi', false, '');
INSERT INTO languages_iso639 VALUES ('elh', '   ', '   ', '  ', 'I', 'L', 'El Hugeirat', false, '');
INSERT INTO languages_iso639 VALUES ('eli', '   ', '   ', '  ', 'I', 'E', 'Nding', false, '');
INSERT INTO languages_iso639 VALUES ('elk', '   ', '   ', '  ', 'I', 'L', 'Elkei', false, '');
INSERT INTO languages_iso639 VALUES ('ell', 'gre', 'ell', 'el', 'I', 'L', 'Modern Greek (1453-)', false, '');
INSERT INTO languages_iso639 VALUES ('elm', '   ', '   ', '  ', 'I', 'L', 'Eleme', false, '');
INSERT INTO languages_iso639 VALUES ('elo', '   ', '   ', '  ', 'I', 'L', 'El Molo', false, '');
INSERT INTO languages_iso639 VALUES ('elu', '   ', '   ', '  ', 'I', 'L', 'Elu', false, '');
INSERT INTO languages_iso639 VALUES ('elx', 'elx', 'elx', '  ', 'I', 'A', 'Elamite', false, '');
INSERT INTO languages_iso639 VALUES ('ema', '   ', '   ', '  ', 'I', 'L', 'Emai-Iuleha-Ora', false, '');
INSERT INTO languages_iso639 VALUES ('emb', '   ', '   ', '  ', 'I', 'L', 'Embaloh', false, '');
INSERT INTO languages_iso639 VALUES ('eme', '   ', '   ', '  ', 'I', 'L', 'Emerillon', false, '');
INSERT INTO languages_iso639 VALUES ('emg', '   ', '   ', '  ', 'I', 'L', 'Eastern Meohang', false, '');
INSERT INTO languages_iso639 VALUES ('emi', '   ', '   ', '  ', 'I', 'L', 'Mussau-Emira', false, '');
INSERT INTO languages_iso639 VALUES ('emk', '   ', '   ', '  ', 'I', 'L', 'Eastern Maninkakan', false, '');
INSERT INTO languages_iso639 VALUES ('emm', '   ', '   ', '  ', 'I', 'E', 'Mamulique', false, '');
INSERT INTO languages_iso639 VALUES ('emn', '   ', '   ', '  ', 'I', 'L', 'Eman', false, '');
INSERT INTO languages_iso639 VALUES ('emo', '   ', '   ', '  ', 'I', 'E', 'Emok', false, '');
INSERT INTO languages_iso639 VALUES ('emp', '   ', '   ', '  ', 'I', 'L', 'Northern Emberá', false, '');
INSERT INTO languages_iso639 VALUES ('ems', '   ', '   ', '  ', 'I', 'L', 'Pacific Gulf Yupik', false, '');
INSERT INTO languages_iso639 VALUES ('emu', '   ', '   ', '  ', 'I', 'L', 'Eastern Muria', false, '');
INSERT INTO languages_iso639 VALUES ('emw', '   ', '   ', '  ', 'I', 'L', 'Emplawas', false, '');
INSERT INTO languages_iso639 VALUES ('emx', '   ', '   ', '  ', 'I', 'L', 'Erromintxela', false, '');
INSERT INTO languages_iso639 VALUES ('emy', '   ', '   ', '  ', 'I', 'E', 'Epigraphic Mayan', false, '');
INSERT INTO languages_iso639 VALUES ('ena', '   ', '   ', '  ', 'I', 'L', 'Apali', false, '');
INSERT INTO languages_iso639 VALUES ('enb', '   ', '   ', '  ', 'I', 'L', 'Markweeta', false, '');
INSERT INTO languages_iso639 VALUES ('enc', '   ', '   ', '  ', 'I', 'L', 'En', false, '');
INSERT INTO languages_iso639 VALUES ('end', '   ', '   ', '  ', 'I', 'L', 'Ende', false, '');
INSERT INTO languages_iso639 VALUES ('enf', '   ', '   ', '  ', 'I', 'L', 'Forest Enets', false, '');
INSERT INTO languages_iso639 VALUES ('eng', 'eng', 'eng', 'en', 'I', 'L', 'English', true, '');
INSERT INTO languages_iso639 VALUES ('enh', '   ', '   ', '  ', 'I', 'L', 'Tundra Enets', false, '');
INSERT INTO languages_iso639 VALUES ('enm', 'enm', 'enm', '  ', 'I', 'H', 'Middle English (1100-1500)', false, '');
INSERT INTO languages_iso639 VALUES ('enn', '   ', '   ', '  ', 'I', 'L', 'Engenni', false, '');
INSERT INTO languages_iso639 VALUES ('eno', '   ', '   ', '  ', 'I', 'L', 'Enggano', false, '');
INSERT INTO languages_iso639 VALUES ('enq', '   ', '   ', '  ', 'I', 'L', 'Enga', false, '');
INSERT INTO languages_iso639 VALUES ('enr', '   ', '   ', '  ', 'I', 'L', 'Emumu', false, '');
INSERT INTO languages_iso639 VALUES ('enu', '   ', '   ', '  ', 'I', 'L', 'Enu', false, '');
INSERT INTO languages_iso639 VALUES ('env', '   ', '   ', '  ', 'I', 'L', 'Enwan (Edu State)', false, '');
INSERT INTO languages_iso639 VALUES ('enw', '   ', '   ', '  ', 'I', 'L', 'Enwan (Akwa Ibom State)', false, '');
INSERT INTO languages_iso639 VALUES ('eot', '   ', '   ', '  ', 'I', 'L', 'Beti (Côte d''Ivoire)', false, '');
INSERT INTO languages_iso639 VALUES ('epi', '   ', '   ', '  ', 'I', 'L', 'Epie', false, '');
INSERT INTO languages_iso639 VALUES ('epo', 'epo', 'epo', 'eo', 'I', 'C', 'Esperanto', false, '');
INSERT INTO languages_iso639 VALUES ('era', '   ', '   ', '  ', 'I', 'L', 'Eravallan', false, '');
INSERT INTO languages_iso639 VALUES ('erg', '   ', '   ', '  ', 'I', 'L', 'Sie', false, '');
INSERT INTO languages_iso639 VALUES ('erh', '   ', '   ', '  ', 'I', 'L', 'Eruwa', false, '');
INSERT INTO languages_iso639 VALUES ('eri', '   ', '   ', '  ', 'I', 'L', 'Ogea', false, '');
INSERT INTO languages_iso639 VALUES ('erk', '   ', '   ', '  ', 'I', 'L', 'South Efate', false, '');
INSERT INTO languages_iso639 VALUES ('ero', '   ', '   ', '  ', 'I', 'L', 'Horpa', false, '');
INSERT INTO languages_iso639 VALUES ('err', '   ', '   ', '  ', 'I', 'E', 'Erre', false, '');
INSERT INTO languages_iso639 VALUES ('ers', '   ', '   ', '  ', 'I', 'L', 'Ersu', false, '');
INSERT INTO languages_iso639 VALUES ('ert', '   ', '   ', '  ', 'I', 'L', 'Eritai', false, '');
INSERT INTO languages_iso639 VALUES ('erw', '   ', '   ', '  ', 'I', 'L', 'Erokwanas', false, '');
INSERT INTO languages_iso639 VALUES ('ese', '   ', '   ', '  ', 'I', 'L', 'Ese Ejja', false, '');
INSERT INTO languages_iso639 VALUES ('esh', '   ', '   ', '  ', 'I', 'L', 'Eshtehardi', false, '');
INSERT INTO languages_iso639 VALUES ('esi', '   ', '   ', '  ', 'I', 'L', 'North Alaskan Inupiatun', false, '');
INSERT INTO languages_iso639 VALUES ('esk', '   ', '   ', '  ', 'I', 'L', 'Northwest Alaska Inupiatun', false, '');
INSERT INTO languages_iso639 VALUES ('esl', '   ', '   ', '  ', 'I', 'L', 'Egypt Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('esm', '   ', '   ', '  ', 'I', 'E', 'Esuma', false, '');
INSERT INTO languages_iso639 VALUES ('esn', '   ', '   ', '  ', 'I', 'L', 'Salvadoran Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('eso', '   ', '   ', '  ', 'I', 'L', 'Estonian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('esq', '   ', '   ', '  ', 'I', 'E', 'Esselen', false, '');
INSERT INTO languages_iso639 VALUES ('ess', '   ', '   ', '  ', 'I', 'L', 'Central Siberian Yupik', false, '');
INSERT INTO languages_iso639 VALUES ('est', 'est', 'est', 'et', 'M', 'L', 'Estonian', false, '');
INSERT INTO languages_iso639 VALUES ('esu', '   ', '   ', '  ', 'I', 'L', 'Central Yupik', false, '');
INSERT INTO languages_iso639 VALUES ('etb', '   ', '   ', '  ', 'I', 'L', 'Etebi', false, '');
INSERT INTO languages_iso639 VALUES ('etc', '   ', '   ', '  ', 'I', 'E', 'Etchemin', false, '');
INSERT INTO languages_iso639 VALUES ('eth', '   ', '   ', '  ', 'I', 'L', 'Ethiopian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('etn', '   ', '   ', '  ', 'I', 'L', 'Eton (Vanuatu)', false, '');
INSERT INTO languages_iso639 VALUES ('eto', '   ', '   ', '  ', 'I', 'L', 'Eton (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('etr', '   ', '   ', '  ', 'I', 'L', 'Edolo', false, '');
INSERT INTO languages_iso639 VALUES ('ets', '   ', '   ', '  ', 'I', 'L', 'Yekhee', false, '');
INSERT INTO languages_iso639 VALUES ('ett', '   ', '   ', '  ', 'I', 'A', 'Etruscan', false, '');
INSERT INTO languages_iso639 VALUES ('etu', '   ', '   ', '  ', 'I', 'L', 'Ejagham', false, '');
INSERT INTO languages_iso639 VALUES ('etx', '   ', '   ', '  ', 'I', 'L', 'Eten', false, '');
INSERT INTO languages_iso639 VALUES ('etz', '   ', '   ', '  ', 'I', 'L', 'Semimi', false, '');
INSERT INTO languages_iso639 VALUES ('eus', 'baq', 'eus', 'eu', 'I', 'L', 'Basque', false, '');
INSERT INTO languages_iso639 VALUES ('eve', '   ', '   ', '  ', 'I', 'L', 'Even', false, '');
INSERT INTO languages_iso639 VALUES ('evh', '   ', '   ', '  ', 'I', 'L', 'Uvbie', false, '');
INSERT INTO languages_iso639 VALUES ('evn', '   ', '   ', '  ', 'I', 'L', 'Evenki', false, '');
INSERT INTO languages_iso639 VALUES ('ewe', 'ewe', 'ewe', 'ee', 'I', 'L', 'Ewe', false, '');
INSERT INTO languages_iso639 VALUES ('ewo', 'ewo', 'ewo', '  ', 'I', 'L', 'Ewondo', false, '');
INSERT INTO languages_iso639 VALUES ('ext', '   ', '   ', '  ', 'I', 'L', 'Extremaduran', false, '');
INSERT INTO languages_iso639 VALUES ('eya', '   ', '   ', '  ', 'I', 'E', 'Eyak', false, '');
INSERT INTO languages_iso639 VALUES ('eyo', '   ', '   ', '  ', 'I', 'L', 'Keiyo', false, '');
INSERT INTO languages_iso639 VALUES ('eza', '   ', '   ', '  ', 'I', 'L', 'Ezaa', false, '');
INSERT INTO languages_iso639 VALUES ('eze', '   ', '   ', '  ', 'I', 'L', 'Uzekwe', false, '');
INSERT INTO languages_iso639 VALUES ('faa', '   ', '   ', '  ', 'I', 'L', 'Fasu', false, '');
INSERT INTO languages_iso639 VALUES ('fab', '   ', '   ', '  ', 'I', 'L', 'Fa D''ambu', false, '');
INSERT INTO languages_iso639 VALUES ('fad', '   ', '   ', '  ', 'I', 'L', 'Wagi', false, '');
INSERT INTO languages_iso639 VALUES ('faf', '   ', '   ', '  ', 'I', 'L', 'Fagani', false, '');
INSERT INTO languages_iso639 VALUES ('fag', '   ', '   ', '  ', 'I', 'L', 'Finongan', false, '');
INSERT INTO languages_iso639 VALUES ('fah', '   ', '   ', '  ', 'I', 'L', 'Baissa Fali', false, '');
INSERT INTO languages_iso639 VALUES ('fai', '   ', '   ', '  ', 'I', 'L', 'Faiwol', false, '');
INSERT INTO languages_iso639 VALUES ('faj', '   ', '   ', '  ', 'I', 'L', 'Faita', false, '');
INSERT INTO languages_iso639 VALUES ('fak', '   ', '   ', '  ', 'I', 'L', 'Fang (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('fal', '   ', '   ', '  ', 'I', 'L', 'South Fali', false, '');
INSERT INTO languages_iso639 VALUES ('fam', '   ', '   ', '  ', 'I', 'L', 'Fam', false, '');
INSERT INTO languages_iso639 VALUES ('fan', 'fan', 'fan', '  ', 'I', 'L', 'Fang (Equatorial Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('fao', 'fao', 'fao', 'fo', 'I', 'L', 'Faroese', false, '');
INSERT INTO languages_iso639 VALUES ('fap', '   ', '   ', '  ', 'I', 'L', 'Palor', false, '');
INSERT INTO languages_iso639 VALUES ('far', '   ', '   ', '  ', 'I', 'L', 'Fataleka', false, '');
INSERT INTO languages_iso639 VALUES ('fas', 'per', 'fas', 'fa', 'M', 'L', 'Persian', false, '');
INSERT INTO languages_iso639 VALUES ('fat', 'fat', 'fat', '  ', 'I', 'L', 'Fanti', false, '');
INSERT INTO languages_iso639 VALUES ('fau', '   ', '   ', '  ', 'I', 'L', 'Fayu', false, '');
INSERT INTO languages_iso639 VALUES ('fax', '   ', '   ', '  ', 'I', 'L', 'Fala', false, '');
INSERT INTO languages_iso639 VALUES ('fay', '   ', '   ', '  ', 'I', 'L', 'Southwestern Fars', false, '');
INSERT INTO languages_iso639 VALUES ('faz', '   ', '   ', '  ', 'I', 'L', 'Northwestern Fars', false, '');
INSERT INTO languages_iso639 VALUES ('fbl', '   ', '   ', '  ', 'I', 'L', 'West Albay Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('fcs', '   ', '   ', '  ', 'I', 'L', 'Quebec Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('fer', '   ', '   ', '  ', 'I', 'L', 'Feroge', false, '');
INSERT INTO languages_iso639 VALUES ('ffi', '   ', '   ', '  ', 'I', 'L', 'Foia Foia', false, '');
INSERT INTO languages_iso639 VALUES ('ffm', '   ', '   ', '  ', 'I', 'L', 'Maasina Fulfulde', false, '');
INSERT INTO languages_iso639 VALUES ('fgr', '   ', '   ', '  ', 'I', 'L', 'Fongoro', false, '');
INSERT INTO languages_iso639 VALUES ('fia', '   ', '   ', '  ', 'I', 'L', 'Nobiin', false, '');
INSERT INTO languages_iso639 VALUES ('fie', '   ', '   ', '  ', 'I', 'L', 'Fyer', false, '');
INSERT INTO languages_iso639 VALUES ('fij', 'fij', 'fij', 'fj', 'I', 'L', 'Fijian', false, '');
INSERT INTO languages_iso639 VALUES ('fil', 'fil', 'fil', '  ', 'I', 'L', 'Filipino', false, '');
INSERT INTO languages_iso639 VALUES ('fin', 'fin', 'fin', 'fi', 'I', 'L', 'Finnish', false, '');
INSERT INTO languages_iso639 VALUES ('fip', '   ', '   ', '  ', 'I', 'L', 'Fipa', false, '');
INSERT INTO languages_iso639 VALUES ('fir', '   ', '   ', '  ', 'I', 'L', 'Firan', false, '');
INSERT INTO languages_iso639 VALUES ('fit', '   ', '   ', '  ', 'I', 'L', 'Tornedalen Finnish', false, '');
INSERT INTO languages_iso639 VALUES ('fiw', '   ', '   ', '  ', 'I', 'L', 'Fiwaga', false, '');
INSERT INTO languages_iso639 VALUES ('fkk', '   ', '   ', '  ', 'I', 'L', 'Kirya-Konzəl', false, '');
INSERT INTO languages_iso639 VALUES ('fkv', '   ', '   ', '  ', 'I', 'L', 'Kven Finnish', false, '');
INSERT INTO languages_iso639 VALUES ('fla', '   ', '   ', '  ', 'I', 'L', 'Kalispel-Pend d''Oreille', false, '');
INSERT INTO languages_iso639 VALUES ('flh', '   ', '   ', '  ', 'I', 'L', 'Foau', false, '');
INSERT INTO languages_iso639 VALUES ('fli', '   ', '   ', '  ', 'I', 'L', 'Fali', false, '');
INSERT INTO languages_iso639 VALUES ('fll', '   ', '   ', '  ', 'I', 'L', 'North Fali', false, '');
INSERT INTO languages_iso639 VALUES ('fln', '   ', '   ', '  ', 'I', 'E', 'Flinders Island', false, '');
INSERT INTO languages_iso639 VALUES ('flr', '   ', '   ', '  ', 'I', 'L', 'Fuliiru', false, '');
INSERT INTO languages_iso639 VALUES ('fly', '   ', '   ', '  ', 'I', 'L', 'Tsotsitaal', false, '');
INSERT INTO languages_iso639 VALUES ('fmp', '   ', '   ', '  ', 'I', 'L', 'Fe''fe''', false, '');
INSERT INTO languages_iso639 VALUES ('fmu', '   ', '   ', '  ', 'I', 'L', 'Far Western Muria', false, '');
INSERT INTO languages_iso639 VALUES ('fng', '   ', '   ', '  ', 'I', 'L', 'Fanagalo', false, '');
INSERT INTO languages_iso639 VALUES ('fni', '   ', '   ', '  ', 'I', 'L', 'Fania', false, '');
INSERT INTO languages_iso639 VALUES ('fod', '   ', '   ', '  ', 'I', 'L', 'Foodo', false, '');
INSERT INTO languages_iso639 VALUES ('foi', '   ', '   ', '  ', 'I', 'L', 'Foi', false, '');
INSERT INTO languages_iso639 VALUES ('fom', '   ', '   ', '  ', 'I', 'L', 'Foma', false, '');
INSERT INTO languages_iso639 VALUES ('fon', 'fon', 'fon', '  ', 'I', 'L', 'Fon', false, '');
INSERT INTO languages_iso639 VALUES ('for', '   ', '   ', '  ', 'I', 'L', 'Fore', false, '');
INSERT INTO languages_iso639 VALUES ('fos', '   ', '   ', '  ', 'I', 'E', 'Siraya', false, '');
INSERT INTO languages_iso639 VALUES ('fpe', '   ', '   ', '  ', 'I', 'L', 'Fernando Po Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('fqs', '   ', '   ', '  ', 'I', 'L', 'Fas', false, '');
INSERT INTO languages_iso639 VALUES ('fra', 'fre', 'fra', 'fr', 'I', 'L', 'French', true, '');
INSERT INTO languages_iso639 VALUES ('frc', '   ', '   ', '  ', 'I', 'L', 'Cajun French', false, '');
INSERT INTO languages_iso639 VALUES ('frd', '   ', '   ', '  ', 'I', 'L', 'Fordata', false, '');
INSERT INTO languages_iso639 VALUES ('frk', '   ', '   ', '  ', 'I', 'E', 'Frankish', false, '');
INSERT INTO languages_iso639 VALUES ('frm', 'frm', 'frm', '  ', 'I', 'H', 'Middle French (ca. 1400-1600)', false, '');
INSERT INTO languages_iso639 VALUES ('fro', 'fro', 'fro', '  ', 'I', 'H', 'Old French (842-ca. 1400)', false, '');
INSERT INTO languages_iso639 VALUES ('frp', '   ', '   ', '  ', 'I', 'L', 'Arpitan', false, '');
INSERT INTO languages_iso639 VALUES ('frq', '   ', '   ', '  ', 'I', 'L', 'Forak', false, '');
INSERT INTO languages_iso639 VALUES ('frr', 'frr', 'frr', '  ', 'I', 'L', 'Northern Frisian', false, '');
INSERT INTO languages_iso639 VALUES ('frs', 'frs', 'frs', '  ', 'I', 'L', 'Eastern Frisian', false, '');
INSERT INTO languages_iso639 VALUES ('frt', '   ', '   ', '  ', 'I', 'L', 'Fortsenal', false, '');
INSERT INTO languages_iso639 VALUES ('fry', 'fry', 'fry', 'fy', 'I', 'L', 'Western Frisian', false, '');
INSERT INTO languages_iso639 VALUES ('fse', '   ', '   ', '  ', 'I', 'L', 'Finnish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('fsl', '   ', '   ', '  ', 'I', 'L', 'French Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('fss', '   ', '   ', '  ', 'I', 'L', 'Finland-Swedish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('fub', '   ', '   ', '  ', 'I', 'L', 'Adamawa Fulfulde', false, '');
INSERT INTO languages_iso639 VALUES ('fuc', '   ', '   ', '  ', 'I', 'L', 'Pulaar', false, '');
INSERT INTO languages_iso639 VALUES ('fud', '   ', '   ', '  ', 'I', 'L', 'East Futuna', false, '');
INSERT INTO languages_iso639 VALUES ('fue', '   ', '   ', '  ', 'I', 'L', 'Borgu Fulfulde', false, '');
INSERT INTO languages_iso639 VALUES ('fuf', '   ', '   ', '  ', 'I', 'L', 'Pular', false, '');
INSERT INTO languages_iso639 VALUES ('fuh', '   ', '   ', '  ', 'I', 'L', 'Western Niger Fulfulde', false, '');
INSERT INTO languages_iso639 VALUES ('fui', '   ', '   ', '  ', 'I', 'L', 'Bagirmi Fulfulde', false, '');
INSERT INTO languages_iso639 VALUES ('fuj', '   ', '   ', '  ', 'I', 'L', 'Ko', false, '');
INSERT INTO languages_iso639 VALUES ('ful', 'ful', 'ful', 'ff', 'M', 'L', 'Fulah', false, '');
INSERT INTO languages_iso639 VALUES ('fum', '   ', '   ', '  ', 'I', 'L', 'Fum', false, '');
INSERT INTO languages_iso639 VALUES ('fun', '   ', '   ', '  ', 'I', 'L', 'Fulniô', false, '');
INSERT INTO languages_iso639 VALUES ('fuq', '   ', '   ', '  ', 'I', 'L', 'Central-Eastern Niger Fulfulde', false, '');
INSERT INTO languages_iso639 VALUES ('fur', 'fur', 'fur', '  ', 'I', 'L', 'Friulian', false, '');
INSERT INTO languages_iso639 VALUES ('fut', '   ', '   ', '  ', 'I', 'L', 'Futuna-Aniwa', false, '');
INSERT INTO languages_iso639 VALUES ('fuu', '   ', '   ', '  ', 'I', 'L', 'Furu', false, '');
INSERT INTO languages_iso639 VALUES ('fuv', '   ', '   ', '  ', 'I', 'L', 'Nigerian Fulfulde', false, '');
INSERT INTO languages_iso639 VALUES ('fuy', '   ', '   ', '  ', 'I', 'L', 'Fuyug', false, '');
INSERT INTO languages_iso639 VALUES ('fvr', '   ', '   ', '  ', 'I', 'L', 'Fur', false, '');
INSERT INTO languages_iso639 VALUES ('fwa', '   ', '   ', '  ', 'I', 'L', 'Fwâi', false, '');
INSERT INTO languages_iso639 VALUES ('fwe', '   ', '   ', '  ', 'I', 'L', 'Fwe', false, '');
INSERT INTO languages_iso639 VALUES ('gaa', 'gaa', 'gaa', '  ', 'I', 'L', 'Ga', false, '');
INSERT INTO languages_iso639 VALUES ('gab', '   ', '   ', '  ', 'I', 'L', 'Gabri', false, '');
INSERT INTO languages_iso639 VALUES ('gac', '   ', '   ', '  ', 'I', 'L', 'Mixed Great Andamanese', false, '');
INSERT INTO languages_iso639 VALUES ('gad', '   ', '   ', '  ', 'I', 'L', 'Gaddang', false, '');
INSERT INTO languages_iso639 VALUES ('gae', '   ', '   ', '  ', 'I', 'L', 'Guarequena', false, '');
INSERT INTO languages_iso639 VALUES ('gaf', '   ', '   ', '  ', 'I', 'L', 'Gende', false, '');
INSERT INTO languages_iso639 VALUES ('gag', '   ', '   ', '  ', 'I', 'L', 'Gagauz', false, '');
INSERT INTO languages_iso639 VALUES ('gah', '   ', '   ', '  ', 'I', 'L', 'Alekano', false, '');
INSERT INTO languages_iso639 VALUES ('gai', '   ', '   ', '  ', 'I', 'L', 'Borei', false, '');
INSERT INTO languages_iso639 VALUES ('gaj', '   ', '   ', '  ', 'I', 'L', 'Gadsup', false, '');
INSERT INTO languages_iso639 VALUES ('gak', '   ', '   ', '  ', 'I', 'L', 'Gamkonora', false, '');
INSERT INTO languages_iso639 VALUES ('gal', '   ', '   ', '  ', 'I', 'L', 'Galolen', false, '');
INSERT INTO languages_iso639 VALUES ('gam', '   ', '   ', '  ', 'I', 'L', 'Kandawo', false, '');
INSERT INTO languages_iso639 VALUES ('gan', '   ', '   ', '  ', 'I', 'L', 'Gan Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('gao', '   ', '   ', '  ', 'I', 'L', 'Gants', false, '');
INSERT INTO languages_iso639 VALUES ('gap', '   ', '   ', '  ', 'I', 'L', 'Gal', false, '');
INSERT INTO languages_iso639 VALUES ('gaq', '   ', '   ', '  ', 'I', 'L', 'Gata''', false, '');
INSERT INTO languages_iso639 VALUES ('gar', '   ', '   ', '  ', 'I', 'L', 'Galeya', false, '');
INSERT INTO languages_iso639 VALUES ('gas', '   ', '   ', '  ', 'I', 'L', 'Adiwasi Garasia', false, '');
INSERT INTO languages_iso639 VALUES ('gat', '   ', '   ', '  ', 'I', 'L', 'Kenati', false, '');
INSERT INTO languages_iso639 VALUES ('gau', '   ', '   ', '  ', 'I', 'L', 'Mudhili Gadaba', false, '');
INSERT INTO languages_iso639 VALUES ('gaw', '   ', '   ', '  ', 'I', 'L', 'Nobonob', false, '');
INSERT INTO languages_iso639 VALUES ('gax', '   ', '   ', '  ', 'I', 'L', 'Borana-Arsi-Guji Oromo', false, '');
INSERT INTO languages_iso639 VALUES ('gay', 'gay', 'gay', '  ', 'I', 'L', 'Gayo', false, '');
INSERT INTO languages_iso639 VALUES ('gaz', '   ', '   ', '  ', 'I', 'L', 'West Central Oromo', false, '');
INSERT INTO languages_iso639 VALUES ('gba', 'gba', 'gba', '  ', 'M', 'L', 'Gbaya (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('gbb', '   ', '   ', '  ', 'I', 'L', 'Kaytetye', false, '');
INSERT INTO languages_iso639 VALUES ('gbd', '   ', '   ', '  ', 'I', 'L', 'Karadjeri', false, '');
INSERT INTO languages_iso639 VALUES ('gbe', '   ', '   ', '  ', 'I', 'L', 'Niksek', false, '');
INSERT INTO languages_iso639 VALUES ('gbf', '   ', '   ', '  ', 'I', 'L', 'Gaikundi', false, '');
INSERT INTO languages_iso639 VALUES ('gbg', '   ', '   ', '  ', 'I', 'L', 'Gbanziri', false, '');
INSERT INTO languages_iso639 VALUES ('gbh', '   ', '   ', '  ', 'I', 'L', 'Defi Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('gbi', '   ', '   ', '  ', 'I', 'L', 'Galela', false, '');
INSERT INTO languages_iso639 VALUES ('gbj', '   ', '   ', '  ', 'I', 'L', 'Bodo Gadaba', false, '');
INSERT INTO languages_iso639 VALUES ('gbk', '   ', '   ', '  ', 'I', 'L', 'Gaddi', false, '');
INSERT INTO languages_iso639 VALUES ('gbl', '   ', '   ', '  ', 'I', 'L', 'Gamit', false, '');
INSERT INTO languages_iso639 VALUES ('gbm', '   ', '   ', '  ', 'I', 'L', 'Garhwali', false, '');
INSERT INTO languages_iso639 VALUES ('gbn', '   ', '   ', '  ', 'I', 'L', 'Mo''da', false, '');
INSERT INTO languages_iso639 VALUES ('gbo', '   ', '   ', '  ', 'I', 'L', 'Northern Grebo', false, '');
INSERT INTO languages_iso639 VALUES ('gbp', '   ', '   ', '  ', 'I', 'L', 'Gbaya-Bossangoa', false, '');
INSERT INTO languages_iso639 VALUES ('gbq', '   ', '   ', '  ', 'I', 'L', 'Gbaya-Bozoum', false, '');
INSERT INTO languages_iso639 VALUES ('gbr', '   ', '   ', '  ', 'I', 'L', 'Gbagyi', false, '');
INSERT INTO languages_iso639 VALUES ('gbs', '   ', '   ', '  ', 'I', 'L', 'Gbesi Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('gbu', '   ', '   ', '  ', 'I', 'L', 'Gagadu', false, '');
INSERT INTO languages_iso639 VALUES ('gbv', '   ', '   ', '  ', 'I', 'L', 'Gbanu', false, '');
INSERT INTO languages_iso639 VALUES ('gbw', '   ', '   ', '  ', 'I', 'L', 'Gabi-Gabi', false, '');
INSERT INTO languages_iso639 VALUES ('gbx', '   ', '   ', '  ', 'I', 'L', 'Eastern Xwla Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('gby', '   ', '   ', '  ', 'I', 'L', 'Gbari', false, '');
INSERT INTO languages_iso639 VALUES ('gbz', '   ', '   ', '  ', 'I', 'L', 'Zoroastrian Dari', false, '');
INSERT INTO languages_iso639 VALUES ('gcc', '   ', '   ', '  ', 'I', 'L', 'Mali', false, '');
INSERT INTO languages_iso639 VALUES ('gcd', '   ', '   ', '  ', 'I', 'E', 'Ganggalida', false, '');
INSERT INTO languages_iso639 VALUES ('gce', '   ', '   ', '  ', 'I', 'E', 'Galice', false, '');
INSERT INTO languages_iso639 VALUES ('gcf', '   ', '   ', '  ', 'I', 'L', 'Guadeloupean Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('gcl', '   ', '   ', '  ', 'I', 'L', 'Grenadian Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('gcn', '   ', '   ', '  ', 'I', 'L', 'Gaina', false, '');
INSERT INTO languages_iso639 VALUES ('gcr', '   ', '   ', '  ', 'I', 'L', 'Guianese Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('gct', '   ', '   ', '  ', 'I', 'L', 'Colonia Tovar German', false, '');
INSERT INTO languages_iso639 VALUES ('gda', '   ', '   ', '  ', 'I', 'L', 'Gade Lohar', false, '');
INSERT INTO languages_iso639 VALUES ('gdb', '   ', '   ', '  ', 'I', 'L', 'Pottangi Ollar Gadaba', false, '');
INSERT INTO languages_iso639 VALUES ('gdc', '   ', '   ', '  ', 'I', 'E', 'Gugu Badhun', false, '');
INSERT INTO languages_iso639 VALUES ('gdd', '   ', '   ', '  ', 'I', 'L', 'Gedaged', false, '');
INSERT INTO languages_iso639 VALUES ('gde', '   ', '   ', '  ', 'I', 'L', 'Gude', false, '');
INSERT INTO languages_iso639 VALUES ('gdf', '   ', '   ', '  ', 'I', 'L', 'Guduf-Gava', false, '');
INSERT INTO languages_iso639 VALUES ('gdg', '   ', '   ', '  ', 'I', 'L', 'Ga''dang', false, '');
INSERT INTO languages_iso639 VALUES ('gdh', '   ', '   ', '  ', 'I', 'L', 'Gadjerawang', false, '');
INSERT INTO languages_iso639 VALUES ('gdi', '   ', '   ', '  ', 'I', 'L', 'Gundi', false, '');
INSERT INTO languages_iso639 VALUES ('gdj', '   ', '   ', '  ', 'I', 'L', 'Gurdjar', false, '');
INSERT INTO languages_iso639 VALUES ('gdk', '   ', '   ', '  ', 'I', 'L', 'Gadang', false, '');
INSERT INTO languages_iso639 VALUES ('gdl', '   ', '   ', '  ', 'I', 'L', 'Dirasha', false, '');
INSERT INTO languages_iso639 VALUES ('gdm', '   ', '   ', '  ', 'I', 'L', 'Laal', false, '');
INSERT INTO languages_iso639 VALUES ('gdn', '   ', '   ', '  ', 'I', 'L', 'Umanakaina', false, '');
INSERT INTO languages_iso639 VALUES ('gdo', '   ', '   ', '  ', 'I', 'L', 'Ghodoberi', false, '');
INSERT INTO languages_iso639 VALUES ('gdq', '   ', '   ', '  ', 'I', 'L', 'Mehri', false, '');
INSERT INTO languages_iso639 VALUES ('gdr', '   ', '   ', '  ', 'I', 'L', 'Wipi', false, '');
INSERT INTO languages_iso639 VALUES ('gds', '   ', '   ', '  ', 'I', 'L', 'Ghandruk Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('gdt', '   ', '   ', '  ', 'I', 'E', 'Kungardutyi', false, '');
INSERT INTO languages_iso639 VALUES ('gdu', '   ', '   ', '  ', 'I', 'L', 'Gudu', false, '');
INSERT INTO languages_iso639 VALUES ('gdx', '   ', '   ', '  ', 'I', 'L', 'Godwari', false, '');
INSERT INTO languages_iso639 VALUES ('gea', '   ', '   ', '  ', 'I', 'L', 'Geruma', false, '');
INSERT INTO languages_iso639 VALUES ('geb', '   ', '   ', '  ', 'I', 'L', 'Kire', false, '');
INSERT INTO languages_iso639 VALUES ('gec', '   ', '   ', '  ', 'I', 'L', 'Gboloo Grebo', false, '');
INSERT INTO languages_iso639 VALUES ('ged', '   ', '   ', '  ', 'I', 'L', 'Gade', false, '');
INSERT INTO languages_iso639 VALUES ('geg', '   ', '   ', '  ', 'I', 'L', 'Gengle', false, '');
INSERT INTO languages_iso639 VALUES ('geh', '   ', '   ', '  ', 'I', 'L', 'Hutterite German', false, '');
INSERT INTO languages_iso639 VALUES ('gei', '   ', '   ', '  ', 'I', 'L', 'Gebe', false, '');
INSERT INTO languages_iso639 VALUES ('gej', '   ', '   ', '  ', 'I', 'L', 'Gen', false, '');
INSERT INTO languages_iso639 VALUES ('gek', '   ', '   ', '  ', 'I', 'L', 'Yiwom', false, '');
INSERT INTO languages_iso639 VALUES ('gel', '   ', '   ', '  ', 'I', 'L', 'ut-Ma''in', false, '');
INSERT INTO languages_iso639 VALUES ('geq', '   ', '   ', '  ', 'I', 'L', 'Geme', false, '');
INSERT INTO languages_iso639 VALUES ('ges', '   ', '   ', '  ', 'I', 'L', 'Geser-Gorom', false, '');
INSERT INTO languages_iso639 VALUES ('gew', '   ', '   ', '  ', 'I', 'L', 'Gera', false, '');
INSERT INTO languages_iso639 VALUES ('gex', '   ', '   ', '  ', 'I', 'L', 'Garre', false, '');
INSERT INTO languages_iso639 VALUES ('gey', '   ', '   ', '  ', 'I', 'L', 'Enya', false, '');
INSERT INTO languages_iso639 VALUES ('gez', 'gez', 'gez', '  ', 'I', 'A', 'Geez', false, '');
INSERT INTO languages_iso639 VALUES ('gfk', '   ', '   ', '  ', 'I', 'L', 'Patpatar', false, '');
INSERT INTO languages_iso639 VALUES ('gft', '   ', '   ', '  ', 'I', 'E', 'Gafat', false, '');
INSERT INTO languages_iso639 VALUES ('gfx', '   ', '   ', '  ', 'I', 'L', 'Mangetti Dune !Xung', false, '');
INSERT INTO languages_iso639 VALUES ('gga', '   ', '   ', '  ', 'I', 'L', 'Gao', false, '');
INSERT INTO languages_iso639 VALUES ('ggb', '   ', '   ', '  ', 'I', 'L', 'Gbii', false, '');
INSERT INTO languages_iso639 VALUES ('ggd', '   ', '   ', '  ', 'I', 'E', 'Gugadj', false, '');
INSERT INTO languages_iso639 VALUES ('gge', '   ', '   ', '  ', 'I', 'L', 'Guragone', false, '');
INSERT INTO languages_iso639 VALUES ('ggg', '   ', '   ', '  ', 'I', 'L', 'Gurgula', false, '');
INSERT INTO languages_iso639 VALUES ('ggk', '   ', '   ', '  ', 'I', 'E', 'Kungarakany', false, '');
INSERT INTO languages_iso639 VALUES ('ggl', '   ', '   ', '  ', 'I', 'L', 'Ganglau', false, '');
INSERT INTO languages_iso639 VALUES ('ggm', '   ', '   ', '  ', 'I', 'E', 'Gugu Mini', false, '');
INSERT INTO languages_iso639 VALUES ('ggn', '   ', '   ', '  ', 'I', 'L', 'Eastern Gurung', false, '');
INSERT INTO languages_iso639 VALUES ('ggo', '   ', '   ', '  ', 'I', 'L', 'Southern Gondi', false, '');
INSERT INTO languages_iso639 VALUES ('ggt', '   ', '   ', '  ', 'I', 'L', 'Gitua', false, '');
INSERT INTO languages_iso639 VALUES ('ggu', '   ', '   ', '  ', 'I', 'L', 'Gagu', false, '');
INSERT INTO languages_iso639 VALUES ('ggw', '   ', '   ', '  ', 'I', 'L', 'Gogodala', false, '');
INSERT INTO languages_iso639 VALUES ('gha', '   ', '   ', '  ', 'I', 'L', 'Ghadamès', false, '');
INSERT INTO languages_iso639 VALUES ('ghc', '   ', '   ', '  ', 'I', 'E', 'Hiberno-Scottish Gaelic', false, '');
INSERT INTO languages_iso639 VALUES ('ghe', '   ', '   ', '  ', 'I', 'L', 'Southern Ghale', false, '');
INSERT INTO languages_iso639 VALUES ('ghh', '   ', '   ', '  ', 'I', 'L', 'Northern Ghale', false, '');
INSERT INTO languages_iso639 VALUES ('ghk', '   ', '   ', '  ', 'I', 'L', 'Geko Karen', false, '');
INSERT INTO languages_iso639 VALUES ('ghl', '   ', '   ', '  ', 'I', 'L', 'Ghulfan', false, '');
INSERT INTO languages_iso639 VALUES ('ghn', '   ', '   ', '  ', 'I', 'L', 'Ghanongga', false, '');
INSERT INTO languages_iso639 VALUES ('gho', '   ', '   ', '  ', 'I', 'E', 'Ghomara', false, '');
INSERT INTO languages_iso639 VALUES ('ghr', '   ', '   ', '  ', 'I', 'L', 'Ghera', false, '');
INSERT INTO languages_iso639 VALUES ('ghs', '   ', '   ', '  ', 'I', 'L', 'Guhu-Samane', false, '');
INSERT INTO languages_iso639 VALUES ('ght', '   ', '   ', '  ', 'I', 'L', 'Kuke', false, '');
INSERT INTO languages_iso639 VALUES ('gia', '   ', '   ', '  ', 'I', 'L', 'Kitja', false, '');
INSERT INTO languages_iso639 VALUES ('gib', '   ', '   ', '  ', 'I', 'L', 'Gibanawa', false, '');
INSERT INTO languages_iso639 VALUES ('gic', '   ', '   ', '  ', 'I', 'L', 'Gail', false, '');
INSERT INTO languages_iso639 VALUES ('gid', '   ', '   ', '  ', 'I', 'L', 'Gidar', false, '');
INSERT INTO languages_iso639 VALUES ('gig', '   ', '   ', '  ', 'I', 'L', 'Goaria', false, '');
INSERT INTO languages_iso639 VALUES ('gih', '   ', '   ', '  ', 'I', 'L', 'Githabul', false, '');
INSERT INTO languages_iso639 VALUES ('gil', 'gil', 'gil', '  ', 'I', 'L', 'Gilbertese', false, '');
INSERT INTO languages_iso639 VALUES ('gim', '   ', '   ', '  ', 'I', 'L', 'Gimi (Eastern Highlands)', false, '');
INSERT INTO languages_iso639 VALUES ('gin', '   ', '   ', '  ', 'I', 'L', 'Hinukh', false, '');
INSERT INTO languages_iso639 VALUES ('gip', '   ', '   ', '  ', 'I', 'L', 'Gimi (West New Britain)', false, '');
INSERT INTO languages_iso639 VALUES ('giq', '   ', '   ', '  ', 'I', 'L', 'Green Gelao', false, '');
INSERT INTO languages_iso639 VALUES ('gir', '   ', '   ', '  ', 'I', 'L', 'Red Gelao', false, '');
INSERT INTO languages_iso639 VALUES ('gis', '   ', '   ', '  ', 'I', 'L', 'North Giziga', false, '');
INSERT INTO languages_iso639 VALUES ('git', '   ', '   ', '  ', 'I', 'L', 'Gitxsan', false, '');
INSERT INTO languages_iso639 VALUES ('giu', '   ', '   ', '  ', 'I', 'L', 'Mulao', false, '');
INSERT INTO languages_iso639 VALUES ('giw', '   ', '   ', '  ', 'I', 'L', 'White Gelao', false, '');
INSERT INTO languages_iso639 VALUES ('gix', '   ', '   ', '  ', 'I', 'L', 'Gilima', false, '');
INSERT INTO languages_iso639 VALUES ('giy', '   ', '   ', '  ', 'I', 'L', 'Giyug', false, '');
INSERT INTO languages_iso639 VALUES ('giz', '   ', '   ', '  ', 'I', 'L', 'South Giziga', false, '');
INSERT INTO languages_iso639 VALUES ('gji', '   ', '   ', '  ', 'I', 'L', 'Geji', false, '');
INSERT INTO languages_iso639 VALUES ('gjk', '   ', '   ', '  ', 'I', 'L', 'Kachi Koli', false, '');
INSERT INTO languages_iso639 VALUES ('gjm', '   ', '   ', '  ', 'I', 'E', 'Gunditjmara', false, '');
INSERT INTO languages_iso639 VALUES ('gjn', '   ', '   ', '  ', 'I', 'L', 'Gonja', false, '');
INSERT INTO languages_iso639 VALUES ('gju', '   ', '   ', '  ', 'I', 'L', 'Gujari', false, '');
INSERT INTO languages_iso639 VALUES ('gka', '   ', '   ', '  ', 'I', 'L', 'Guya', false, '');
INSERT INTO languages_iso639 VALUES ('gke', '   ', '   ', '  ', 'I', 'L', 'Ndai', false, '');
INSERT INTO languages_iso639 VALUES ('gkn', '   ', '   ', '  ', 'I', 'L', 'Gokana', false, '');
INSERT INTO languages_iso639 VALUES ('gko', '   ', '   ', '  ', 'I', 'E', 'Kok-Nar', false, '');
INSERT INTO languages_iso639 VALUES ('gkp', '   ', '   ', '  ', 'I', 'L', 'Guinea Kpelle', false, '');
INSERT INTO languages_iso639 VALUES ('gla', 'gla', 'gla', 'gd', 'I', 'L', 'Scottish Gaelic', false, '');
INSERT INTO languages_iso639 VALUES ('glc', '   ', '   ', '  ', 'I', 'L', 'Bon Gula', false, '');
INSERT INTO languages_iso639 VALUES ('gld', '   ', '   ', '  ', 'I', 'L', 'Nanai', false, '');
INSERT INTO languages_iso639 VALUES ('gle', 'gle', 'gle', 'ga', 'I', 'L', 'Irish', false, '');
INSERT INTO languages_iso639 VALUES ('glg', 'glg', 'glg', 'gl', 'I', 'L', 'Galician', false, '');
INSERT INTO languages_iso639 VALUES ('glh', '   ', '   ', '  ', 'I', 'L', 'Northwest Pashayi', false, '');
INSERT INTO languages_iso639 VALUES ('gli', '   ', '   ', '  ', 'I', 'E', 'Guliguli', false, '');
INSERT INTO languages_iso639 VALUES ('glj', '   ', '   ', '  ', 'I', 'L', 'Gula Iro', false, '');
INSERT INTO languages_iso639 VALUES ('glk', '   ', '   ', '  ', 'I', 'L', 'Gilaki', false, '');
INSERT INTO languages_iso639 VALUES ('gll', '   ', '   ', '  ', 'I', 'E', 'Garlali', false, '');
INSERT INTO languages_iso639 VALUES ('glo', '   ', '   ', '  ', 'I', 'L', 'Galambu', false, '');
INSERT INTO languages_iso639 VALUES ('glr', '   ', '   ', '  ', 'I', 'L', 'Glaro-Twabo', false, '');
INSERT INTO languages_iso639 VALUES ('glu', '   ', '   ', '  ', 'I', 'L', 'Gula (Chad)', false, '');
INSERT INTO languages_iso639 VALUES ('glv', 'glv', 'glv', 'gv', 'I', 'L', 'Manx', false, '');
INSERT INTO languages_iso639 VALUES ('glw', '   ', '   ', '  ', 'I', 'L', 'Glavda', false, '');
INSERT INTO languages_iso639 VALUES ('gly', '   ', '   ', '  ', 'I', 'E', 'Gule', false, '');
INSERT INTO languages_iso639 VALUES ('gma', '   ', '   ', '  ', 'I', 'E', 'Gambera', false, '');
INSERT INTO languages_iso639 VALUES ('gmb', '   ', '   ', '  ', 'I', 'L', 'Gula''alaa', false, '');
INSERT INTO languages_iso639 VALUES ('gmd', '   ', '   ', '  ', 'I', 'L', 'Mághdì', false, '');
INSERT INTO languages_iso639 VALUES ('gmh', 'gmh', 'gmh', '  ', 'I', 'H', 'Middle High German (ca. 1050-1500)', false, '');
INSERT INTO languages_iso639 VALUES ('gml', '   ', '   ', '  ', 'I', 'H', 'Middle Low German', false, '');
INSERT INTO languages_iso639 VALUES ('gmm', '   ', '   ', '  ', 'I', 'L', 'Gbaya-Mbodomo', false, '');
INSERT INTO languages_iso639 VALUES ('gmn', '   ', '   ', '  ', 'I', 'L', 'Gimnime', false, '');
INSERT INTO languages_iso639 VALUES ('gmu', '   ', '   ', '  ', 'I', 'L', 'Gumalu', false, '');
INSERT INTO languages_iso639 VALUES ('gmv', '   ', '   ', '  ', 'I', 'L', 'Gamo', false, '');
INSERT INTO languages_iso639 VALUES ('gmx', '   ', '   ', '  ', 'I', 'L', 'Magoma', false, '');
INSERT INTO languages_iso639 VALUES ('gmy', '   ', '   ', '  ', 'I', 'A', 'Mycenaean Greek', false, '');
INSERT INTO languages_iso639 VALUES ('gmz', '   ', '   ', '  ', 'I', 'L', 'Mgbolizhia', false, '');
INSERT INTO languages_iso639 VALUES ('gna', '   ', '   ', '  ', 'I', 'L', 'Kaansa', false, '');
INSERT INTO languages_iso639 VALUES ('gnb', '   ', '   ', '  ', 'I', 'L', 'Gangte', false, '');
INSERT INTO languages_iso639 VALUES ('gnc', '   ', '   ', '  ', 'I', 'E', 'Guanche', false, '');
INSERT INTO languages_iso639 VALUES ('gnd', '   ', '   ', '  ', 'I', 'L', 'Zulgo-Gemzek', false, '');
INSERT INTO languages_iso639 VALUES ('gne', '   ', '   ', '  ', 'I', 'L', 'Ganang', false, '');
INSERT INTO languages_iso639 VALUES ('gng', '   ', '   ', '  ', 'I', 'L', 'Ngangam', false, '');
INSERT INTO languages_iso639 VALUES ('gnh', '   ', '   ', '  ', 'I', 'L', 'Lere', false, '');
INSERT INTO languages_iso639 VALUES ('gni', '   ', '   ', '  ', 'I', 'L', 'Gooniyandi', false, '');
INSERT INTO languages_iso639 VALUES ('gnk', '   ', '   ', '  ', 'I', 'L', '//Gana', false, '');
INSERT INTO languages_iso639 VALUES ('gnl', '   ', '   ', '  ', 'I', 'E', 'Gangulu', false, '');
INSERT INTO languages_iso639 VALUES ('gnm', '   ', '   ', '  ', 'I', 'L', 'Ginuman', false, '');
INSERT INTO languages_iso639 VALUES ('gnn', '   ', '   ', '  ', 'I', 'L', 'Gumatj', false, '');
INSERT INTO languages_iso639 VALUES ('gno', '   ', '   ', '  ', 'I', 'L', 'Northern Gondi', false, '');
INSERT INTO languages_iso639 VALUES ('gnq', '   ', '   ', '  ', 'I', 'L', 'Gana', false, '');
INSERT INTO languages_iso639 VALUES ('gnr', '   ', '   ', '  ', 'I', 'E', 'Gureng Gureng', false, '');
INSERT INTO languages_iso639 VALUES ('gnt', '   ', '   ', '  ', 'I', 'L', 'Guntai', false, '');
INSERT INTO languages_iso639 VALUES ('gnu', '   ', '   ', '  ', 'I', 'L', 'Gnau', false, '');
INSERT INTO languages_iso639 VALUES ('gnw', '   ', '   ', '  ', 'I', 'L', 'Western Bolivian Guaraní', false, '');
INSERT INTO languages_iso639 VALUES ('gnz', '   ', '   ', '  ', 'I', 'L', 'Ganzi', false, '');
INSERT INTO languages_iso639 VALUES ('goa', '   ', '   ', '  ', 'I', 'L', 'Guro', false, '');
INSERT INTO languages_iso639 VALUES ('gob', '   ', '   ', '  ', 'I', 'L', 'Playero', false, '');
INSERT INTO languages_iso639 VALUES ('goc', '   ', '   ', '  ', 'I', 'L', 'Gorakor', false, '');
INSERT INTO languages_iso639 VALUES ('god', '   ', '   ', '  ', 'I', 'L', 'Godié', false, '');
INSERT INTO languages_iso639 VALUES ('goe', '   ', '   ', '  ', 'I', 'L', 'Gongduk', false, '');
INSERT INTO languages_iso639 VALUES ('gof', '   ', '   ', '  ', 'I', 'L', 'Gofa', false, '');
INSERT INTO languages_iso639 VALUES ('gog', '   ', '   ', '  ', 'I', 'L', 'Gogo', false, '');
INSERT INTO languages_iso639 VALUES ('goh', 'goh', 'goh', '  ', 'I', 'H', 'Old High German (ca. 750-1050)', false, '');
INSERT INTO languages_iso639 VALUES ('goi', '   ', '   ', '  ', 'I', 'L', 'Gobasi', false, '');
INSERT INTO languages_iso639 VALUES ('goj', '   ', '   ', '  ', 'I', 'L', 'Gowlan', false, '');
INSERT INTO languages_iso639 VALUES ('gok', '   ', '   ', '  ', 'I', 'L', 'Gowli', false, '');
INSERT INTO languages_iso639 VALUES ('gol', '   ', '   ', '  ', 'I', 'L', 'Gola', false, '');
INSERT INTO languages_iso639 VALUES ('gom', '   ', '   ', '  ', 'I', 'L', 'Goan Konkani', false, '');
INSERT INTO languages_iso639 VALUES ('gon', 'gon', 'gon', '  ', 'M', 'L', 'Gondi', false, '');
INSERT INTO languages_iso639 VALUES ('goo', '   ', '   ', '  ', 'I', 'L', 'Gone Dau', false, '');
INSERT INTO languages_iso639 VALUES ('gop', '   ', '   ', '  ', 'I', 'L', 'Yeretuar', false, '');
INSERT INTO languages_iso639 VALUES ('goq', '   ', '   ', '  ', 'I', 'L', 'Gorap', false, '');
INSERT INTO languages_iso639 VALUES ('gor', 'gor', 'gor', '  ', 'I', 'L', 'Gorontalo', false, '');
INSERT INTO languages_iso639 VALUES ('gos', '   ', '   ', '  ', 'I', 'L', 'Gronings', false, '');
INSERT INTO languages_iso639 VALUES ('got', 'got', 'got', '  ', 'I', 'A', 'Gothic', false, '');
INSERT INTO languages_iso639 VALUES ('gou', '   ', '   ', '  ', 'I', 'L', 'Gavar', false, '');
INSERT INTO languages_iso639 VALUES ('gow', '   ', '   ', '  ', 'I', 'L', 'Gorowa', false, '');
INSERT INTO languages_iso639 VALUES ('gox', '   ', '   ', '  ', 'I', 'L', 'Gobu', false, '');
INSERT INTO languages_iso639 VALUES ('goy', '   ', '   ', '  ', 'I', 'L', 'Goundo', false, '');
INSERT INTO languages_iso639 VALUES ('goz', '   ', '   ', '  ', 'I', 'L', 'Gozarkhani', false, '');
INSERT INTO languages_iso639 VALUES ('gpa', '   ', '   ', '  ', 'I', 'L', 'Gupa-Abawa', false, '');
INSERT INTO languages_iso639 VALUES ('gpe', '   ', '   ', '  ', 'I', 'L', 'Ghanaian Pidgin English', false, '');
INSERT INTO languages_iso639 VALUES ('gpn', '   ', '   ', '  ', 'I', 'L', 'Taiap', false, '');
INSERT INTO languages_iso639 VALUES ('gqa', '   ', '   ', '  ', 'I', 'L', 'Ga''anda', false, '');
INSERT INTO languages_iso639 VALUES ('gqi', '   ', '   ', '  ', 'I', 'L', 'Guiqiong', false, '');
INSERT INTO languages_iso639 VALUES ('gqn', '   ', '   ', '  ', 'I', 'E', 'Guana (Brazil)', false, '');
INSERT INTO languages_iso639 VALUES ('gqr', '   ', '   ', '  ', 'I', 'L', 'Gor', false, '');
INSERT INTO languages_iso639 VALUES ('gqu', '   ', '   ', '  ', 'I', 'L', 'Qau', false, '');
INSERT INTO languages_iso639 VALUES ('gra', '   ', '   ', '  ', 'I', 'L', 'Rajput Garasia', false, '');
INSERT INTO languages_iso639 VALUES ('grb', 'grb', 'grb', '  ', 'M', 'L', 'Grebo', false, '');
INSERT INTO languages_iso639 VALUES ('grc', 'grc', 'grc', '  ', 'I', 'H', 'Ancient Greek (to 1453)', false, '');
INSERT INTO languages_iso639 VALUES ('grd', '   ', '   ', '  ', 'I', 'L', 'Guruntum-Mbaaru', false, '');
INSERT INTO languages_iso639 VALUES ('grg', '   ', '   ', '  ', 'I', 'L', 'Madi', false, '');
INSERT INTO languages_iso639 VALUES ('grh', '   ', '   ', '  ', 'I', 'L', 'Gbiri-Niragu', false, '');
INSERT INTO languages_iso639 VALUES ('gri', '   ', '   ', '  ', 'I', 'L', 'Ghari', false, '');
INSERT INTO languages_iso639 VALUES ('grj', '   ', '   ', '  ', 'I', 'L', 'Southern Grebo', false, '');
INSERT INTO languages_iso639 VALUES ('grm', '   ', '   ', '  ', 'I', 'L', 'Kota Marudu Talantang', false, '');
INSERT INTO languages_iso639 VALUES ('grn', 'grn', 'grn', 'gn', 'M', 'L', 'Guarani', false, '');
INSERT INTO languages_iso639 VALUES ('gro', '   ', '   ', '  ', 'I', 'L', 'Groma', false, '');
INSERT INTO languages_iso639 VALUES ('grq', '   ', '   ', '  ', 'I', 'L', 'Gorovu', false, '');
INSERT INTO languages_iso639 VALUES ('grr', '   ', '   ', '  ', 'I', 'L', 'Taznatit', false, '');
INSERT INTO languages_iso639 VALUES ('grs', '   ', '   ', '  ', 'I', 'L', 'Gresi', false, '');
INSERT INTO languages_iso639 VALUES ('grt', '   ', '   ', '  ', 'I', 'L', 'Garo', false, '');
INSERT INTO languages_iso639 VALUES ('gru', '   ', '   ', '  ', 'I', 'L', 'Kistane', false, '');
INSERT INTO languages_iso639 VALUES ('grv', '   ', '   ', '  ', 'I', 'L', 'Central Grebo', false, '');
INSERT INTO languages_iso639 VALUES ('grw', '   ', '   ', '  ', 'I', 'L', 'Gweda', false, '');
INSERT INTO languages_iso639 VALUES ('grx', '   ', '   ', '  ', 'I', 'L', 'Guriaso', false, '');
INSERT INTO languages_iso639 VALUES ('gry', '   ', '   ', '  ', 'I', 'L', 'Barclayville Grebo', false, '');
INSERT INTO languages_iso639 VALUES ('grz', '   ', '   ', '  ', 'I', 'L', 'Guramalum', false, '');
INSERT INTO languages_iso639 VALUES ('gse', '   ', '   ', '  ', 'I', 'L', 'Ghanaian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('gsg', '   ', '   ', '  ', 'I', 'L', 'German Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('gsl', '   ', '   ', '  ', 'I', 'L', 'Gusilay', false, '');
INSERT INTO languages_iso639 VALUES ('gsm', '   ', '   ', '  ', 'I', 'L', 'Guatemalan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('gsn', '   ', '   ', '  ', 'I', 'L', 'Gusan', false, '');
INSERT INTO languages_iso639 VALUES ('gso', '   ', '   ', '  ', 'I', 'L', 'Southwest Gbaya', false, '');
INSERT INTO languages_iso639 VALUES ('gsp', '   ', '   ', '  ', 'I', 'L', 'Wasembo', false, '');
INSERT INTO languages_iso639 VALUES ('gss', '   ', '   ', '  ', 'I', 'L', 'Greek Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('gsw', 'gsw', 'gsw', '  ', 'I', 'L', 'Swiss German', false, '');
INSERT INTO languages_iso639 VALUES ('gta', '   ', '   ', '  ', 'I', 'L', 'Guató', false, '');
INSERT INTO languages_iso639 VALUES ('gti', '   ', '   ', '  ', 'I', 'L', 'Gbati-ri', false, '');
INSERT INTO languages_iso639 VALUES ('gtu', '   ', '   ', '  ', 'I', 'E', 'Aghu-Tharnggala', false, '');
INSERT INTO languages_iso639 VALUES ('gua', '   ', '   ', '  ', 'I', 'L', 'Shiki', false, '');
INSERT INTO languages_iso639 VALUES ('gub', '   ', '   ', '  ', 'I', 'L', 'Guajajára', false, '');
INSERT INTO languages_iso639 VALUES ('guc', '   ', '   ', '  ', 'I', 'L', 'Wayuu', false, '');
INSERT INTO languages_iso639 VALUES ('gud', '   ', '   ', '  ', 'I', 'L', 'Yocoboué Dida', false, '');
INSERT INTO languages_iso639 VALUES ('gue', '   ', '   ', '  ', 'I', 'L', 'Gurinji', false, '');
INSERT INTO languages_iso639 VALUES ('guf', '   ', '   ', '  ', 'I', 'L', 'Gupapuyngu', false, '');
INSERT INTO languages_iso639 VALUES ('gug', '   ', '   ', '  ', 'I', 'L', 'Paraguayan Guaraní', false, '');
INSERT INTO languages_iso639 VALUES ('guh', '   ', '   ', '  ', 'I', 'L', 'Guahibo', false, '');
INSERT INTO languages_iso639 VALUES ('gui', '   ', '   ', '  ', 'I', 'L', 'Eastern Bolivian Guaraní', false, '');
INSERT INTO languages_iso639 VALUES ('guj', 'guj', 'guj', 'gu', 'I', 'L', 'Gujarati', false, '');
INSERT INTO languages_iso639 VALUES ('guk', '   ', '   ', '  ', 'I', 'L', 'Gumuz', false, '');
INSERT INTO languages_iso639 VALUES ('gul', '   ', '   ', '  ', 'I', 'L', 'Sea Island Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('gum', '   ', '   ', '  ', 'I', 'L', 'Guambiano', false, '');
INSERT INTO languages_iso639 VALUES ('gun', '   ', '   ', '  ', 'I', 'L', 'Mbyá Guaraní', false, '');
INSERT INTO languages_iso639 VALUES ('guo', '   ', '   ', '  ', 'I', 'L', 'Guayabero', false, '');
INSERT INTO languages_iso639 VALUES ('gup', '   ', '   ', '  ', 'I', 'L', 'Gunwinggu', false, '');
INSERT INTO languages_iso639 VALUES ('guq', '   ', '   ', '  ', 'I', 'L', 'Aché', false, '');
INSERT INTO languages_iso639 VALUES ('gur', '   ', '   ', '  ', 'I', 'L', 'Farefare', false, '');
INSERT INTO languages_iso639 VALUES ('gus', '   ', '   ', '  ', 'I', 'L', 'Guinean Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('gut', '   ', '   ', '  ', 'I', 'L', 'Maléku Jaíka', false, '');
INSERT INTO languages_iso639 VALUES ('guu', '   ', '   ', '  ', 'I', 'L', 'Yanomamö', false, '');
INSERT INTO languages_iso639 VALUES ('guv', '   ', '   ', '  ', 'I', 'E', 'Gey', false, '');
INSERT INTO languages_iso639 VALUES ('guw', '   ', '   ', '  ', 'I', 'L', 'Gun', false, '');
INSERT INTO languages_iso639 VALUES ('gux', '   ', '   ', '  ', 'I', 'L', 'Gourmanchéma', false, '');
INSERT INTO languages_iso639 VALUES ('guz', '   ', '   ', '  ', 'I', 'L', 'Gusii', false, '');
INSERT INTO languages_iso639 VALUES ('gva', '   ', '   ', '  ', 'I', 'L', 'Guana (Paraguay)', false, '');
INSERT INTO languages_iso639 VALUES ('gvc', '   ', '   ', '  ', 'I', 'L', 'Guanano', false, '');
INSERT INTO languages_iso639 VALUES ('gve', '   ', '   ', '  ', 'I', 'L', 'Duwet', false, '');
INSERT INTO languages_iso639 VALUES ('gvf', '   ', '   ', '  ', 'I', 'L', 'Golin', false, '');
INSERT INTO languages_iso639 VALUES ('gvj', '   ', '   ', '  ', 'I', 'L', 'Guajá', false, '');
INSERT INTO languages_iso639 VALUES ('gvl', '   ', '   ', '  ', 'I', 'L', 'Gulay', false, '');
INSERT INTO languages_iso639 VALUES ('gvm', '   ', '   ', '  ', 'I', 'L', 'Gurmana', false, '');
INSERT INTO languages_iso639 VALUES ('gvn', '   ', '   ', '  ', 'I', 'L', 'Kuku-Yalanji', false, '');
INSERT INTO languages_iso639 VALUES ('gvo', '   ', '   ', '  ', 'I', 'L', 'Gavião Do Jiparaná', false, '');
INSERT INTO languages_iso639 VALUES ('gvp', '   ', '   ', '  ', 'I', 'L', 'Pará Gavião', false, '');
INSERT INTO languages_iso639 VALUES ('gvr', '   ', '   ', '  ', 'I', 'L', 'Western Gurung', false, '');
INSERT INTO languages_iso639 VALUES ('gvs', '   ', '   ', '  ', 'I', 'L', 'Gumawana', false, '');
INSERT INTO languages_iso639 VALUES ('gvy', '   ', '   ', '  ', 'I', 'E', 'Guyani', false, '');
INSERT INTO languages_iso639 VALUES ('gwa', '   ', '   ', '  ', 'I', 'L', 'Mbato', false, '');
INSERT INTO languages_iso639 VALUES ('gwb', '   ', '   ', '  ', 'I', 'L', 'Gwa', false, '');
INSERT INTO languages_iso639 VALUES ('gwc', '   ', '   ', '  ', 'I', 'L', 'Kalami', false, '');
INSERT INTO languages_iso639 VALUES ('gwd', '   ', '   ', '  ', 'I', 'L', 'Gawwada', false, '');
INSERT INTO languages_iso639 VALUES ('gwe', '   ', '   ', '  ', 'I', 'L', 'Gweno', false, '');
INSERT INTO languages_iso639 VALUES ('gwf', '   ', '   ', '  ', 'I', 'L', 'Gowro', false, '');
INSERT INTO languages_iso639 VALUES ('gwg', '   ', '   ', '  ', 'I', 'L', 'Moo', false, '');
INSERT INTO languages_iso639 VALUES ('gwi', 'gwi', 'gwi', '  ', 'I', 'L', 'Gwichʼin', false, '');
INSERT INTO languages_iso639 VALUES ('gwj', '   ', '   ', '  ', 'I', 'L', '/Gwi', false, '');
INSERT INTO languages_iso639 VALUES ('gwm', '   ', '   ', '  ', 'I', 'E', 'Awngthim', false, '');
INSERT INTO languages_iso639 VALUES ('gwn', '   ', '   ', '  ', 'I', 'L', 'Gwandara', false, '');
INSERT INTO languages_iso639 VALUES ('gwr', '   ', '   ', '  ', 'I', 'L', 'Gwere', false, '');
INSERT INTO languages_iso639 VALUES ('gwt', '   ', '   ', '  ', 'I', 'L', 'Gawar-Bati', false, '');
INSERT INTO languages_iso639 VALUES ('gwu', '   ', '   ', '  ', 'I', 'E', 'Guwamu', false, '');
INSERT INTO languages_iso639 VALUES ('gww', '   ', '   ', '  ', 'I', 'L', 'Kwini', false, '');
INSERT INTO languages_iso639 VALUES ('gwx', '   ', '   ', '  ', 'I', 'L', 'Gua', false, '');
INSERT INTO languages_iso639 VALUES ('gxx', '   ', '   ', '  ', 'I', 'L', 'Wè Southern', false, '');
INSERT INTO languages_iso639 VALUES ('gya', '   ', '   ', '  ', 'I', 'L', 'Northwest Gbaya', false, '');
INSERT INTO languages_iso639 VALUES ('gyb', '   ', '   ', '  ', 'I', 'L', 'Garus', false, '');
INSERT INTO languages_iso639 VALUES ('gyd', '   ', '   ', '  ', 'I', 'L', 'Kayardild', false, '');
INSERT INTO languages_iso639 VALUES ('gye', '   ', '   ', '  ', 'I', 'L', 'Gyem', false, '');
INSERT INTO languages_iso639 VALUES ('gyf', '   ', '   ', '  ', 'I', 'E', 'Gungabula', false, '');
INSERT INTO languages_iso639 VALUES ('gyg', '   ', '   ', '  ', 'I', 'L', 'Gbayi', false, '');
INSERT INTO languages_iso639 VALUES ('gyi', '   ', '   ', '  ', 'I', 'L', 'Gyele', false, '');
INSERT INTO languages_iso639 VALUES ('gyl', '   ', '   ', '  ', 'I', 'L', 'Gayil', false, '');
INSERT INTO languages_iso639 VALUES ('gym', '   ', '   ', '  ', 'I', 'L', 'Ngäbere', false, '');
INSERT INTO languages_iso639 VALUES ('gyn', '   ', '   ', '  ', 'I', 'L', 'Guyanese Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('gyr', '   ', '   ', '  ', 'I', 'L', 'Guarayu', false, '');
INSERT INTO languages_iso639 VALUES ('gyy', '   ', '   ', '  ', 'I', 'E', 'Gunya', false, '');
INSERT INTO languages_iso639 VALUES ('gza', '   ', '   ', '  ', 'I', 'L', 'Ganza', false, '');
INSERT INTO languages_iso639 VALUES ('gzi', '   ', '   ', '  ', 'I', 'L', 'Gazi', false, '');
INSERT INTO languages_iso639 VALUES ('gzn', '   ', '   ', '  ', 'I', 'L', 'Gane', false, '');
INSERT INTO languages_iso639 VALUES ('haa', '   ', '   ', '  ', 'I', 'L', 'Han', false, '');
INSERT INTO languages_iso639 VALUES ('hab', '   ', '   ', '  ', 'I', 'L', 'Hanoi Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hac', '   ', '   ', '  ', 'I', 'L', 'Gurani', false, '');
INSERT INTO languages_iso639 VALUES ('had', '   ', '   ', '  ', 'I', 'L', 'Hatam', false, '');
INSERT INTO languages_iso639 VALUES ('hae', '   ', '   ', '  ', 'I', 'L', 'Eastern Oromo', false, '');
INSERT INTO languages_iso639 VALUES ('haf', '   ', '   ', '  ', 'I', 'L', 'Haiphong Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hag', '   ', '   ', '  ', 'I', 'L', 'Hanga', false, '');
INSERT INTO languages_iso639 VALUES ('hah', '   ', '   ', '  ', 'I', 'L', 'Hahon', false, '');
INSERT INTO languages_iso639 VALUES ('hai', 'hai', 'hai', '  ', 'M', 'L', 'Haida', false, '');
INSERT INTO languages_iso639 VALUES ('haj', '   ', '   ', '  ', 'I', 'L', 'Hajong', false, '');
INSERT INTO languages_iso639 VALUES ('hak', '   ', '   ', '  ', 'I', 'L', 'Hakka Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('hal', '   ', '   ', '  ', 'I', 'L', 'Halang', false, '');
INSERT INTO languages_iso639 VALUES ('ham', '   ', '   ', '  ', 'I', 'L', 'Hewa', false, '');
INSERT INTO languages_iso639 VALUES ('han', '   ', '   ', '  ', 'I', 'L', 'Hangaza', false, '');
INSERT INTO languages_iso639 VALUES ('hao', '   ', '   ', '  ', 'I', 'L', 'Hakö', false, '');
INSERT INTO languages_iso639 VALUES ('hap', '   ', '   ', '  ', 'I', 'L', 'Hupla', false, '');
INSERT INTO languages_iso639 VALUES ('haq', '   ', '   ', '  ', 'I', 'L', 'Ha', false, '');
INSERT INTO languages_iso639 VALUES ('har', '   ', '   ', '  ', 'I', 'L', 'Harari', false, '');
INSERT INTO languages_iso639 VALUES ('has', '   ', '   ', '  ', 'I', 'L', 'Haisla', false, '');
INSERT INTO languages_iso639 VALUES ('hat', 'hat', 'hat', 'ht', 'I', 'L', 'Haitian', false, '');
INSERT INTO languages_iso639 VALUES ('hau', 'hau', 'hau', 'ha', 'I', 'L', 'Hausa', false, '');
INSERT INTO languages_iso639 VALUES ('hav', '   ', '   ', '  ', 'I', 'L', 'Havu', false, '');
INSERT INTO languages_iso639 VALUES ('haw', 'haw', 'haw', '  ', 'I', 'L', 'Hawaiian', false, '');
INSERT INTO languages_iso639 VALUES ('hax', '   ', '   ', '  ', 'I', 'L', 'Southern Haida', false, '');
INSERT INTO languages_iso639 VALUES ('hay', '   ', '   ', '  ', 'I', 'L', 'Haya', false, '');
INSERT INTO languages_iso639 VALUES ('haz', '   ', '   ', '  ', 'I', 'L', 'Hazaragi', false, '');
INSERT INTO languages_iso639 VALUES ('hba', '   ', '   ', '  ', 'I', 'L', 'Hamba', false, '');
INSERT INTO languages_iso639 VALUES ('hbb', '   ', '   ', '  ', 'I', 'L', 'Huba', false, '');
INSERT INTO languages_iso639 VALUES ('hbn', '   ', '   ', '  ', 'I', 'L', 'Heiban', false, '');
INSERT INTO languages_iso639 VALUES ('hbo', '   ', '   ', '  ', 'I', 'H', 'Ancient Hebrew', false, '');
INSERT INTO languages_iso639 VALUES ('hbs', '   ', '   ', 'sh', 'M', 'L', 'Serbo-Croatian', false, 'Code element for 639-1 has been deprecated');
INSERT INTO languages_iso639 VALUES ('hbu', '   ', '   ', '  ', 'I', 'L', 'Habu', false, '');
INSERT INTO languages_iso639 VALUES ('hca', '   ', '   ', '  ', 'I', 'L', 'Andaman Creole Hindi', false, '');
INSERT INTO languages_iso639 VALUES ('hch', '   ', '   ', '  ', 'I', 'L', 'Huichol', false, '');
INSERT INTO languages_iso639 VALUES ('hdn', '   ', '   ', '  ', 'I', 'L', 'Northern Haida', false, '');
INSERT INTO languages_iso639 VALUES ('hds', '   ', '   ', '  ', 'I', 'L', 'Honduras Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hdy', '   ', '   ', '  ', 'I', 'L', 'Hadiyya', false, '');
INSERT INTO languages_iso639 VALUES ('hea', '   ', '   ', '  ', 'I', 'L', 'Northern Qiandong Miao', false, '');
INSERT INTO languages_iso639 VALUES ('heb', 'heb', 'heb', 'he', 'I', 'L', 'Hebrew', false, '');
INSERT INTO languages_iso639 VALUES ('hed', '   ', '   ', '  ', 'I', 'L', 'Herdé', false, '');
INSERT INTO languages_iso639 VALUES ('heg', '   ', '   ', '  ', 'I', 'L', 'Helong', false, '');
INSERT INTO languages_iso639 VALUES ('heh', '   ', '   ', '  ', 'I', 'L', 'Hehe', false, '');
INSERT INTO languages_iso639 VALUES ('hei', '   ', '   ', '  ', 'I', 'L', 'Heiltsuk', false, '');
INSERT INTO languages_iso639 VALUES ('hem', '   ', '   ', '  ', 'I', 'L', 'Hemba', false, '');
INSERT INTO languages_iso639 VALUES ('her', 'her', 'her', 'hz', 'I', 'L', 'Herero', false, '');
INSERT INTO languages_iso639 VALUES ('hgm', '   ', '   ', '  ', 'I', 'L', 'Hai//om', false, '');
INSERT INTO languages_iso639 VALUES ('hgw', '   ', '   ', '  ', 'I', 'L', 'Haigwai', false, '');
INSERT INTO languages_iso639 VALUES ('hhi', '   ', '   ', '  ', 'I', 'L', 'Hoia Hoia', false, '');
INSERT INTO languages_iso639 VALUES ('hhr', '   ', '   ', '  ', 'I', 'L', 'Kerak', false, '');
INSERT INTO languages_iso639 VALUES ('hhy', '   ', '   ', '  ', 'I', 'L', 'Hoyahoya', false, '');
INSERT INTO languages_iso639 VALUES ('hia', '   ', '   ', '  ', 'I', 'L', 'Lamang', false, '');
INSERT INTO languages_iso639 VALUES ('hib', '   ', '   ', '  ', 'I', 'E', 'Hibito', false, '');
INSERT INTO languages_iso639 VALUES ('hid', '   ', '   ', '  ', 'I', 'L', 'Hidatsa', false, '');
INSERT INTO languages_iso639 VALUES ('hif', '   ', '   ', '  ', 'I', 'L', 'Fiji Hindi', false, '');
INSERT INTO languages_iso639 VALUES ('hig', '   ', '   ', '  ', 'I', 'L', 'Kamwe', false, '');
INSERT INTO languages_iso639 VALUES ('hih', '   ', '   ', '  ', 'I', 'L', 'Pamosu', false, '');
INSERT INTO languages_iso639 VALUES ('hii', '   ', '   ', '  ', 'I', 'L', 'Hinduri', false, '');
INSERT INTO languages_iso639 VALUES ('hij', '   ', '   ', '  ', 'I', 'L', 'Hijuk', false, '');
INSERT INTO languages_iso639 VALUES ('hik', '   ', '   ', '  ', 'I', 'L', 'Seit-Kaitetu', false, '');
INSERT INTO languages_iso639 VALUES ('hil', 'hil', 'hil', '  ', 'I', 'L', 'Hiligaynon', false, '');
INSERT INTO languages_iso639 VALUES ('hin', 'hin', 'hin', 'hi', 'I', 'L', 'Hindi', false, '');
INSERT INTO languages_iso639 VALUES ('hio', '   ', '   ', '  ', 'I', 'L', 'Tsoa', false, '');
INSERT INTO languages_iso639 VALUES ('hir', '   ', '   ', '  ', 'I', 'L', 'Himarimã', false, '');
INSERT INTO languages_iso639 VALUES ('hit', 'hit', 'hit', '  ', 'I', 'A', 'Hittite', false, '');
INSERT INTO languages_iso639 VALUES ('hiw', '   ', '   ', '  ', 'I', 'L', 'Hiw', false, '');
INSERT INTO languages_iso639 VALUES ('hix', '   ', '   ', '  ', 'I', 'L', 'Hixkaryána', false, '');
INSERT INTO languages_iso639 VALUES ('hji', '   ', '   ', '  ', 'I', 'L', 'Haji', false, '');
INSERT INTO languages_iso639 VALUES ('hka', '   ', '   ', '  ', 'I', 'L', 'Kahe', false, '');
INSERT INTO languages_iso639 VALUES ('hke', '   ', '   ', '  ', 'I', 'L', 'Hunde', false, '');
INSERT INTO languages_iso639 VALUES ('hkk', '   ', '   ', '  ', 'I', 'L', 'Hunjara-Kaina Ke', false, '');
INSERT INTO languages_iso639 VALUES ('hks', '   ', '   ', '  ', 'I', 'L', 'Hong Kong Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hla', '   ', '   ', '  ', 'I', 'L', 'Halia', false, '');
INSERT INTO languages_iso639 VALUES ('hlb', '   ', '   ', '  ', 'I', 'L', 'Halbi', false, '');
INSERT INTO languages_iso639 VALUES ('hld', '   ', '   ', '  ', 'I', 'L', 'Halang Doan', false, '');
INSERT INTO languages_iso639 VALUES ('hle', '   ', '   ', '  ', 'I', 'L', 'Hlersu', false, '');
INSERT INTO languages_iso639 VALUES ('hlt', '   ', '   ', '  ', 'I', 'L', 'Matu Chin', false, '');
INSERT INTO languages_iso639 VALUES ('hlu', '   ', '   ', '  ', 'I', 'A', 'Hieroglyphic Luwian', false, '');
INSERT INTO languages_iso639 VALUES ('hma', '   ', '   ', '  ', 'I', 'L', 'Southern Mashan Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmb', '   ', '   ', '  ', 'I', 'L', 'Humburi Senni Songhay', false, '');
INSERT INTO languages_iso639 VALUES ('hmc', '   ', '   ', '  ', 'I', 'L', 'Central Huishui Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmd', '   ', '   ', '  ', 'I', 'L', 'Large Flowery Miao', false, '');
INSERT INTO languages_iso639 VALUES ('hme', '   ', '   ', '  ', 'I', 'L', 'Eastern Huishui Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmf', '   ', '   ', '  ', 'I', 'L', 'Hmong Don', false, '');
INSERT INTO languages_iso639 VALUES ('hmg', '   ', '   ', '  ', 'I', 'L', 'Southwestern Guiyang Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmh', '   ', '   ', '  ', 'I', 'L', 'Southwestern Huishui Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmi', '   ', '   ', '  ', 'I', 'L', 'Northern Huishui Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmj', '   ', '   ', '  ', 'I', 'L', 'Ge', false, '');
INSERT INTO languages_iso639 VALUES ('hmk', '   ', '   ', '  ', 'I', 'E', 'Maek', false, '');
INSERT INTO languages_iso639 VALUES ('hml', '   ', '   ', '  ', 'I', 'L', 'Luopohe Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmm', '   ', '   ', '  ', 'I', 'L', 'Central Mashan Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmn', 'hmn', 'hmn', '  ', 'M', 'L', 'Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmo', 'hmo', 'hmo', 'ho', 'I', 'L', 'Hiri Motu', false, '');
INSERT INTO languages_iso639 VALUES ('hmp', '   ', '   ', '  ', 'I', 'L', 'Northern Mashan Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmq', '   ', '   ', '  ', 'I', 'L', 'Eastern Qiandong Miao', false, '');
INSERT INTO languages_iso639 VALUES ('hmr', '   ', '   ', '  ', 'I', 'L', 'Hmar', false, '');
INSERT INTO languages_iso639 VALUES ('hms', '   ', '   ', '  ', 'I', 'L', 'Southern Qiandong Miao', false, '');
INSERT INTO languages_iso639 VALUES ('hmt', '   ', '   ', '  ', 'I', 'L', 'Hamtai', false, '');
INSERT INTO languages_iso639 VALUES ('hmu', '   ', '   ', '  ', 'I', 'L', 'Hamap', false, '');
INSERT INTO languages_iso639 VALUES ('hmv', '   ', '   ', '  ', 'I', 'L', 'Hmong Dô', false, '');
INSERT INTO languages_iso639 VALUES ('hmw', '   ', '   ', '  ', 'I', 'L', 'Western Mashan Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmy', '   ', '   ', '  ', 'I', 'L', 'Southern Guiyang Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('hmz', '   ', '   ', '  ', 'I', 'L', 'Hmong Shua', false, '');
INSERT INTO languages_iso639 VALUES ('hna', '   ', '   ', '  ', 'I', 'L', 'Mina (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('hnd', '   ', '   ', '  ', 'I', 'L', 'Southern Hindko', false, '');
INSERT INTO languages_iso639 VALUES ('hne', '   ', '   ', '  ', 'I', 'L', 'Chhattisgarhi', false, '');
INSERT INTO languages_iso639 VALUES ('hnh', '   ', '   ', '  ', 'I', 'L', '//Ani', false, '');
INSERT INTO languages_iso639 VALUES ('hni', '   ', '   ', '  ', 'I', 'L', 'Hani', false, '');
INSERT INTO languages_iso639 VALUES ('hnj', '   ', '   ', '  ', 'I', 'L', 'Hmong Njua', false, '');
INSERT INTO languages_iso639 VALUES ('hnn', '   ', '   ', '  ', 'I', 'L', 'Hanunoo', false, '');
INSERT INTO languages_iso639 VALUES ('hno', '   ', '   ', '  ', 'I', 'L', 'Northern Hindko', false, '');
INSERT INTO languages_iso639 VALUES ('hns', '   ', '   ', '  ', 'I', 'L', 'Caribbean Hindustani', false, '');
INSERT INTO languages_iso639 VALUES ('hnu', '   ', '   ', '  ', 'I', 'L', 'Hung', false, '');
INSERT INTO languages_iso639 VALUES ('hoa', '   ', '   ', '  ', 'I', 'L', 'Hoava', false, '');
INSERT INTO languages_iso639 VALUES ('hob', '   ', '   ', '  ', 'I', 'L', 'Mari (Madang Province)', false, '');
INSERT INTO languages_iso639 VALUES ('hoc', '   ', '   ', '  ', 'I', 'L', 'Ho', false, '');
INSERT INTO languages_iso639 VALUES ('hod', '   ', '   ', '  ', 'I', 'E', 'Holma', false, '');
INSERT INTO languages_iso639 VALUES ('hoe', '   ', '   ', '  ', 'I', 'L', 'Horom', false, '');
INSERT INTO languages_iso639 VALUES ('hoh', '   ', '   ', '  ', 'I', 'L', 'Hobyót', false, '');
INSERT INTO languages_iso639 VALUES ('hoi', '   ', '   ', '  ', 'I', 'L', 'Holikachuk', false, '');
INSERT INTO languages_iso639 VALUES ('hoj', '   ', '   ', '  ', 'I', 'L', 'Hadothi', false, '');
INSERT INTO languages_iso639 VALUES ('hol', '   ', '   ', '  ', 'I', 'L', 'Holu', false, '');
INSERT INTO languages_iso639 VALUES ('hom', '   ', '   ', '  ', 'I', 'E', 'Homa', false, '');
INSERT INTO languages_iso639 VALUES ('hoo', '   ', '   ', '  ', 'I', 'L', 'Holoholo', false, '');
INSERT INTO languages_iso639 VALUES ('hop', '   ', '   ', '  ', 'I', 'L', 'Hopi', false, '');
INSERT INTO languages_iso639 VALUES ('hor', '   ', '   ', '  ', 'I', 'E', 'Horo', false, '');
INSERT INTO languages_iso639 VALUES ('hos', '   ', '   ', '  ', 'I', 'L', 'Ho Chi Minh City Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hot', '   ', '   ', '  ', 'I', 'L', 'Hote', false, '');
INSERT INTO languages_iso639 VALUES ('hov', '   ', '   ', '  ', 'I', 'L', 'Hovongan', false, '');
INSERT INTO languages_iso639 VALUES ('how', '   ', '   ', '  ', 'I', 'L', 'Honi', false, '');
INSERT INTO languages_iso639 VALUES ('hoy', '   ', '   ', '  ', 'I', 'L', 'Holiya', false, '');
INSERT INTO languages_iso639 VALUES ('hoz', '   ', '   ', '  ', 'I', 'L', 'Hozo', false, '');
INSERT INTO languages_iso639 VALUES ('hpo', '   ', '   ', '  ', 'I', 'L', 'Hpon', false, '');
INSERT INTO languages_iso639 VALUES ('hps', '   ', '   ', '  ', 'I', 'L', 'Hawai''i Pidgin Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hra', '   ', '   ', '  ', 'I', 'L', 'Hrangkhol', false, '');
INSERT INTO languages_iso639 VALUES ('hrc', '   ', '   ', '  ', 'I', 'L', 'Niwer Mil', false, '');
INSERT INTO languages_iso639 VALUES ('hre', '   ', '   ', '  ', 'I', 'L', 'Hre', false, '');
INSERT INTO languages_iso639 VALUES ('hrk', '   ', '   ', '  ', 'I', 'L', 'Haruku', false, '');
INSERT INTO languages_iso639 VALUES ('hrm', '   ', '   ', '  ', 'I', 'L', 'Horned Miao', false, '');
INSERT INTO languages_iso639 VALUES ('hro', '   ', '   ', '  ', 'I', 'L', 'Haroi', false, '');
INSERT INTO languages_iso639 VALUES ('hrp', '   ', '   ', '  ', 'I', 'E', 'Nhirrpi', false, '');
INSERT INTO languages_iso639 VALUES ('hrt', '   ', '   ', '  ', 'I', 'L', 'Hértevin', false, '');
INSERT INTO languages_iso639 VALUES ('hru', '   ', '   ', '  ', 'I', 'L', 'Hruso', false, '');
INSERT INTO languages_iso639 VALUES ('hrv', 'hrv', 'hrv', 'hr', 'I', 'L', 'Croatian', false, '');
INSERT INTO languages_iso639 VALUES ('hrw', '   ', '   ', '  ', 'I', 'L', 'Warwar Feni', false, '');
INSERT INTO languages_iso639 VALUES ('hrx', '   ', '   ', '  ', 'I', 'L', 'Hunsrik', false, '');
INSERT INTO languages_iso639 VALUES ('hrz', '   ', '   ', '  ', 'I', 'L', 'Harzani', false, '');
INSERT INTO languages_iso639 VALUES ('hsb', 'hsb', 'hsb', '  ', 'I', 'L', 'Upper Sorbian', false, '');
INSERT INTO languages_iso639 VALUES ('hsh', '   ', '   ', '  ', 'I', 'L', 'Hungarian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hsl', '   ', '   ', '  ', 'I', 'L', 'Hausa Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('hsn', '   ', '   ', '  ', 'I', 'L', 'Xiang Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('hss', '   ', '   ', '  ', 'I', 'L', 'Harsusi', false, '');
INSERT INTO languages_iso639 VALUES ('hti', '   ', '   ', '  ', 'I', 'L', 'Hoti', false, '');
INSERT INTO languages_iso639 VALUES ('hto', '   ', '   ', '  ', 'I', 'L', 'Minica Huitoto', false, '');
INSERT INTO languages_iso639 VALUES ('hts', '   ', '   ', '  ', 'I', 'L', 'Hadza', false, '');
INSERT INTO languages_iso639 VALUES ('htu', '   ', '   ', '  ', 'I', 'L', 'Hitu', false, '');
INSERT INTO languages_iso639 VALUES ('htx', '   ', '   ', '  ', 'I', 'A', 'Middle Hittite', false, '');
INSERT INTO languages_iso639 VALUES ('hub', '   ', '   ', '  ', 'I', 'L', 'Huambisa', false, '');
INSERT INTO languages_iso639 VALUES ('huc', '   ', '   ', '  ', 'I', 'L', '=/Hua', false, '');
INSERT INTO languages_iso639 VALUES ('hud', '   ', '   ', '  ', 'I', 'L', 'Huaulu', false, '');
INSERT INTO languages_iso639 VALUES ('hue', '   ', '   ', '  ', 'I', 'L', 'San Francisco Del Mar Huave', false, '');
INSERT INTO languages_iso639 VALUES ('huf', '   ', '   ', '  ', 'I', 'L', 'Humene', false, '');
INSERT INTO languages_iso639 VALUES ('hug', '   ', '   ', '  ', 'I', 'L', 'Huachipaeri', false, '');
INSERT INTO languages_iso639 VALUES ('huh', '   ', '   ', '  ', 'I', 'L', 'Huilliche', false, '');
INSERT INTO languages_iso639 VALUES ('hui', '   ', '   ', '  ', 'I', 'L', 'Huli', false, '');
INSERT INTO languages_iso639 VALUES ('huj', '   ', '   ', '  ', 'I', 'L', 'Northern Guiyang Hmong', false, '');
INSERT INTO languages_iso639 VALUES ('huk', '   ', '   ', '  ', 'I', 'L', 'Hulung', false, '');
INSERT INTO languages_iso639 VALUES ('hul', '   ', '   ', '  ', 'I', 'L', 'Hula', false, '');
INSERT INTO languages_iso639 VALUES ('hum', '   ', '   ', '  ', 'I', 'L', 'Hungana', false, '');
INSERT INTO languages_iso639 VALUES ('hun', 'hun', 'hun', 'hu', 'I', 'L', 'Hungarian', false, '');
INSERT INTO languages_iso639 VALUES ('huo', '   ', '   ', '  ', 'I', 'L', 'Hu', false, '');
INSERT INTO languages_iso639 VALUES ('hup', 'hup', 'hup', '  ', 'I', 'L', 'Hupa', false, '');
INSERT INTO languages_iso639 VALUES ('huq', '   ', '   ', '  ', 'I', 'L', 'Tsat', false, '');
INSERT INTO languages_iso639 VALUES ('hur', '   ', '   ', '  ', 'I', 'L', 'Halkomelem', false, '');
INSERT INTO languages_iso639 VALUES ('hus', '   ', '   ', '  ', 'I', 'L', 'Huastec', false, '');
INSERT INTO languages_iso639 VALUES ('hut', '   ', '   ', '  ', 'I', 'L', 'Humla', false, '');
INSERT INTO languages_iso639 VALUES ('huu', '   ', '   ', '  ', 'I', 'L', 'Murui Huitoto', false, '');
INSERT INTO languages_iso639 VALUES ('huv', '   ', '   ', '  ', 'I', 'L', 'San Mateo Del Mar Huave', false, '');
INSERT INTO languages_iso639 VALUES ('huw', '   ', '   ', '  ', 'I', 'E', 'Hukumina', false, '');
INSERT INTO languages_iso639 VALUES ('hux', '   ', '   ', '  ', 'I', 'L', 'Nüpode Huitoto', false, '');
INSERT INTO languages_iso639 VALUES ('huy', '   ', '   ', '  ', 'I', 'L', 'Hulaulá', false, '');
INSERT INTO languages_iso639 VALUES ('huz', '   ', '   ', '  ', 'I', 'L', 'Hunzib', false, '');
INSERT INTO languages_iso639 VALUES ('hvc', '   ', '   ', '  ', 'I', 'L', 'Haitian Vodoun Culture Language', false, '');
INSERT INTO languages_iso639 VALUES ('hve', '   ', '   ', '  ', 'I', 'L', 'San Dionisio Del Mar Huave', false, '');
INSERT INTO languages_iso639 VALUES ('hvk', '   ', '   ', '  ', 'I', 'L', 'Haveke', false, '');
INSERT INTO languages_iso639 VALUES ('hvn', '   ', '   ', '  ', 'I', 'L', 'Sabu', false, '');
INSERT INTO languages_iso639 VALUES ('hvv', '   ', '   ', '  ', 'I', 'L', 'Santa María Del Mar Huave', false, '');
INSERT INTO languages_iso639 VALUES ('hwa', '   ', '   ', '  ', 'I', 'L', 'Wané', false, '');
INSERT INTO languages_iso639 VALUES ('hwc', '   ', '   ', '  ', 'I', 'L', 'Hawai''i Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('hwo', '   ', '   ', '  ', 'I', 'L', 'Hwana', false, '');
INSERT INTO languages_iso639 VALUES ('hya', '   ', '   ', '  ', 'I', 'L', 'Hya', false, '');
INSERT INTO languages_iso639 VALUES ('hye', 'arm', 'hye', 'hy', 'I', 'L', 'Armenian', false, '');
INSERT INTO languages_iso639 VALUES ('iai', '   ', '   ', '  ', 'I', 'L', 'Iaai', false, '');
INSERT INTO languages_iso639 VALUES ('ian', '   ', '   ', '  ', 'I', 'L', 'Iatmul', false, '');
INSERT INTO languages_iso639 VALUES ('iap', '   ', '   ', '  ', 'I', 'L', 'Iapama', false, '');
INSERT INTO languages_iso639 VALUES ('iar', '   ', '   ', '  ', 'I', 'L', 'Purari', false, '');
INSERT INTO languages_iso639 VALUES ('iba', 'iba', 'iba', '  ', 'I', 'L', 'Iban', false, '');
INSERT INTO languages_iso639 VALUES ('ibb', '   ', '   ', '  ', 'I', 'L', 'Ibibio', false, '');
INSERT INTO languages_iso639 VALUES ('ibd', '   ', '   ', '  ', 'I', 'L', 'Iwaidja', false, '');
INSERT INTO languages_iso639 VALUES ('ibe', '   ', '   ', '  ', 'I', 'L', 'Akpes', false, '');
INSERT INTO languages_iso639 VALUES ('ibg', '   ', '   ', '  ', 'I', 'L', 'Ibanag', false, '');
INSERT INTO languages_iso639 VALUES ('ibl', '   ', '   ', '  ', 'I', 'L', 'Ibaloi', false, '');
INSERT INTO languages_iso639 VALUES ('ibm', '   ', '   ', '  ', 'I', 'L', 'Agoi', false, '');
INSERT INTO languages_iso639 VALUES ('ibn', '   ', '   ', '  ', 'I', 'L', 'Ibino', false, '');
INSERT INTO languages_iso639 VALUES ('ibo', 'ibo', 'ibo', 'ig', 'I', 'L', 'Igbo', false, '');
INSERT INTO languages_iso639 VALUES ('ibr', '   ', '   ', '  ', 'I', 'L', 'Ibuoro', false, '');
INSERT INTO languages_iso639 VALUES ('ibu', '   ', '   ', '  ', 'I', 'L', 'Ibu', false, '');
INSERT INTO languages_iso639 VALUES ('iby', '   ', '   ', '  ', 'I', 'L', 'Ibani', false, '');
INSERT INTO languages_iso639 VALUES ('ica', '   ', '   ', '  ', 'I', 'L', 'Ede Ica', false, '');
INSERT INTO languages_iso639 VALUES ('ich', '   ', '   ', '  ', 'I', 'L', 'Etkywan', false, '');
INSERT INTO languages_iso639 VALUES ('icl', '   ', '   ', '  ', 'I', 'L', 'Icelandic Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('icr', '   ', '   ', '  ', 'I', 'L', 'Islander Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('ida', '   ', '   ', '  ', 'I', 'L', 'Idakho-Isukha-Tiriki', false, '');
INSERT INTO languages_iso639 VALUES ('idb', '   ', '   ', '  ', 'I', 'L', 'Indo-Portuguese', false, '');
INSERT INTO languages_iso639 VALUES ('idc', '   ', '   ', '  ', 'I', 'L', 'Idon', false, '');
INSERT INTO languages_iso639 VALUES ('idd', '   ', '   ', '  ', 'I', 'L', 'Ede Idaca', false, '');
INSERT INTO languages_iso639 VALUES ('ide', '   ', '   ', '  ', 'I', 'L', 'Idere', false, '');
INSERT INTO languages_iso639 VALUES ('idi', '   ', '   ', '  ', 'I', 'L', 'Idi', false, '');
INSERT INTO languages_iso639 VALUES ('ido', 'ido', 'ido', 'io', 'I', 'C', 'Ido', false, '');
INSERT INTO languages_iso639 VALUES ('idr', '   ', '   ', '  ', 'I', 'L', 'Indri', false, '');
INSERT INTO languages_iso639 VALUES ('ids', '   ', '   ', '  ', 'I', 'L', 'Idesa', false, '');
INSERT INTO languages_iso639 VALUES ('idt', '   ', '   ', '  ', 'I', 'L', 'Idaté', false, '');
INSERT INTO languages_iso639 VALUES ('idu', '   ', '   ', '  ', 'I', 'L', 'Idoma', false, '');
INSERT INTO languages_iso639 VALUES ('ifa', '   ', '   ', '  ', 'I', 'L', 'Amganad Ifugao', false, '');
INSERT INTO languages_iso639 VALUES ('ifb', '   ', '   ', '  ', 'I', 'L', 'Batad Ifugao', false, '');
INSERT INTO languages_iso639 VALUES ('ife', '   ', '   ', '  ', 'I', 'L', 'Ifè', false, '');
INSERT INTO languages_iso639 VALUES ('iff', '   ', '   ', '  ', 'I', 'E', 'Ifo', false, '');
INSERT INTO languages_iso639 VALUES ('ifk', '   ', '   ', '  ', 'I', 'L', 'Tuwali Ifugao', false, '');
INSERT INTO languages_iso639 VALUES ('ifm', '   ', '   ', '  ', 'I', 'L', 'Teke-Fuumu', false, '');
INSERT INTO languages_iso639 VALUES ('ifu', '   ', '   ', '  ', 'I', 'L', 'Mayoyao Ifugao', false, '');
INSERT INTO languages_iso639 VALUES ('ify', '   ', '   ', '  ', 'I', 'L', 'Keley-I Kallahan', false, '');
INSERT INTO languages_iso639 VALUES ('igb', '   ', '   ', '  ', 'I', 'L', 'Ebira', false, '');
INSERT INTO languages_iso639 VALUES ('ige', '   ', '   ', '  ', 'I', 'L', 'Igede', false, '');
INSERT INTO languages_iso639 VALUES ('igg', '   ', '   ', '  ', 'I', 'L', 'Igana', false, '');
INSERT INTO languages_iso639 VALUES ('igl', '   ', '   ', '  ', 'I', 'L', 'Igala', false, '');
INSERT INTO languages_iso639 VALUES ('igm', '   ', '   ', '  ', 'I', 'L', 'Kanggape', false, '');
INSERT INTO languages_iso639 VALUES ('ign', '   ', '   ', '  ', 'I', 'L', 'Ignaciano', false, '');
INSERT INTO languages_iso639 VALUES ('igo', '   ', '   ', '  ', 'I', 'L', 'Isebe', false, '');
INSERT INTO languages_iso639 VALUES ('igs', '   ', '   ', '  ', 'I', 'C', 'Interglossa', false, '');
INSERT INTO languages_iso639 VALUES ('igw', '   ', '   ', '  ', 'I', 'L', 'Igwe', false, '');
INSERT INTO languages_iso639 VALUES ('ihb', '   ', '   ', '  ', 'I', 'L', 'Iha Based Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('ihi', '   ', '   ', '  ', 'I', 'L', 'Ihievbe', false, '');
INSERT INTO languages_iso639 VALUES ('ihp', '   ', '   ', '  ', 'I', 'L', 'Iha', false, '');
INSERT INTO languages_iso639 VALUES ('ihw', '   ', '   ', '  ', 'I', 'E', 'Bidhawal', false, '');
INSERT INTO languages_iso639 VALUES ('iii', 'iii', 'iii', 'ii', 'I', 'L', 'Sichuan Yi', false, '');
INSERT INTO languages_iso639 VALUES ('iin', '   ', '   ', '  ', 'I', 'E', 'Thiin', false, '');
INSERT INTO languages_iso639 VALUES ('ijc', '   ', '   ', '  ', 'I', 'L', 'Izon', false, '');
INSERT INTO languages_iso639 VALUES ('ije', '   ', '   ', '  ', 'I', 'L', 'Biseni', false, '');
INSERT INTO languages_iso639 VALUES ('ijj', '   ', '   ', '  ', 'I', 'L', 'Ede Ije', false, '');
INSERT INTO languages_iso639 VALUES ('ijn', '   ', '   ', '  ', 'I', 'L', 'Kalabari', false, '');
INSERT INTO languages_iso639 VALUES ('ijs', '   ', '   ', '  ', 'I', 'L', 'Southeast Ijo', false, '');
INSERT INTO languages_iso639 VALUES ('ike', '   ', '   ', '  ', 'I', 'L', 'Eastern Canadian Inuktitut', false, '');
INSERT INTO languages_iso639 VALUES ('iki', '   ', '   ', '  ', 'I', 'L', 'Iko', false, '');
INSERT INTO languages_iso639 VALUES ('ikk', '   ', '   ', '  ', 'I', 'L', 'Ika', false, '');
INSERT INTO languages_iso639 VALUES ('ikl', '   ', '   ', '  ', 'I', 'L', 'Ikulu', false, '');
INSERT INTO languages_iso639 VALUES ('iko', '   ', '   ', '  ', 'I', 'L', 'Olulumo-Ikom', false, '');
INSERT INTO languages_iso639 VALUES ('ikp', '   ', '   ', '  ', 'I', 'L', 'Ikpeshi', false, '');
INSERT INTO languages_iso639 VALUES ('ikr', '   ', '   ', '  ', 'I', 'E', 'Ikaranggal', false, '');
INSERT INTO languages_iso639 VALUES ('ikt', '   ', '   ', '  ', 'I', 'L', 'Inuinnaqtun', false, '');
INSERT INTO languages_iso639 VALUES ('iku', 'iku', 'iku', 'iu', 'M', 'L', 'Inuktitut', false, '');
INSERT INTO languages_iso639 VALUES ('ikv', '   ', '   ', '  ', 'I', 'L', 'Iku-Gora-Ankwa', false, '');
INSERT INTO languages_iso639 VALUES ('ikw', '   ', '   ', '  ', 'I', 'L', 'Ikwere', false, '');
INSERT INTO languages_iso639 VALUES ('ikx', '   ', '   ', '  ', 'I', 'L', 'Ik', false, '');
INSERT INTO languages_iso639 VALUES ('ikz', '   ', '   ', '  ', 'I', 'L', 'Ikizu', false, '');
INSERT INTO languages_iso639 VALUES ('ila', '   ', '   ', '  ', 'I', 'L', 'Ile Ape', false, '');
INSERT INTO languages_iso639 VALUES ('ilb', '   ', '   ', '  ', 'I', 'L', 'Ila', false, '');
INSERT INTO languages_iso639 VALUES ('ile', 'ile', 'ile', 'ie', 'I', 'C', 'Interlingue', false, '');
INSERT INTO languages_iso639 VALUES ('ilg', '   ', '   ', '  ', 'I', 'E', 'Garig-Ilgar', false, '');
INSERT INTO languages_iso639 VALUES ('ili', '   ', '   ', '  ', 'I', 'L', 'Ili Turki', false, '');
INSERT INTO languages_iso639 VALUES ('ilk', '   ', '   ', '  ', 'I', 'L', 'Ilongot', false, '');
INSERT INTO languages_iso639 VALUES ('ill', '   ', '   ', '  ', 'I', 'L', 'Iranun', false, '');
INSERT INTO languages_iso639 VALUES ('ilo', 'ilo', 'ilo', '  ', 'I', 'L', 'Iloko', false, '');
INSERT INTO languages_iso639 VALUES ('ils', '   ', '   ', '  ', 'I', 'L', 'International Sign', false, '');
INSERT INTO languages_iso639 VALUES ('ilu', '   ', '   ', '  ', 'I', 'L', 'Ili''uun', false, '');
INSERT INTO languages_iso639 VALUES ('ilv', '   ', '   ', '  ', 'I', 'L', 'Ilue', false, '');
INSERT INTO languages_iso639 VALUES ('ima', '   ', '   ', '  ', 'I', 'L', 'Mala Malasar', false, '');
INSERT INTO languages_iso639 VALUES ('ime', '   ', '   ', '  ', 'I', 'L', 'Imeraguen', false, '');
INSERT INTO languages_iso639 VALUES ('imi', '   ', '   ', '  ', 'I', 'L', 'Anamgura', false, '');
INSERT INTO languages_iso639 VALUES ('iml', '   ', '   ', '  ', 'I', 'E', 'Miluk', false, '');
INSERT INTO languages_iso639 VALUES ('imn', '   ', '   ', '  ', 'I', 'L', 'Imonda', false, '');
INSERT INTO languages_iso639 VALUES ('imo', '   ', '   ', '  ', 'I', 'L', 'Imbongu', false, '');
INSERT INTO languages_iso639 VALUES ('imr', '   ', '   ', '  ', 'I', 'L', 'Imroing', false, '');
INSERT INTO languages_iso639 VALUES ('ims', '   ', '   ', '  ', 'I', 'A', 'Marsian', false, '');
INSERT INTO languages_iso639 VALUES ('imy', '   ', '   ', '  ', 'I', 'A', 'Milyan', false, '');
INSERT INTO languages_iso639 VALUES ('ina', 'ina', 'ina', 'ia', 'I', 'C', 'Interlingua (International Auxiliary Language Association)', false, '');
INSERT INTO languages_iso639 VALUES ('inb', '   ', '   ', '  ', 'I', 'L', 'Inga', false, '');
INSERT INTO languages_iso639 VALUES ('ind', 'ind', 'ind', 'id', 'I', 'L', 'Indonesian', false, '');
INSERT INTO languages_iso639 VALUES ('ing', '   ', '   ', '  ', 'I', 'L', 'Degexit''an', false, '');
INSERT INTO languages_iso639 VALUES ('inh', 'inh', 'inh', '  ', 'I', 'L', 'Ingush', false, '');
INSERT INTO languages_iso639 VALUES ('inj', '   ', '   ', '  ', 'I', 'L', 'Jungle Inga', false, '');
INSERT INTO languages_iso639 VALUES ('inl', '   ', '   ', '  ', 'I', 'L', 'Indonesian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('inm', '   ', '   ', '  ', 'I', 'A', 'Minaean', false, '');
INSERT INTO languages_iso639 VALUES ('inn', '   ', '   ', '  ', 'I', 'L', 'Isinai', false, '');
INSERT INTO languages_iso639 VALUES ('ino', '   ', '   ', '  ', 'I', 'L', 'Inoke-Yate', false, '');
INSERT INTO languages_iso639 VALUES ('inp', '   ', '   ', '  ', 'I', 'L', 'Iñapari', false, '');
INSERT INTO languages_iso639 VALUES ('ins', '   ', '   ', '  ', 'I', 'L', 'Indian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('int', '   ', '   ', '  ', 'I', 'L', 'Intha', false, '');
INSERT INTO languages_iso639 VALUES ('inz', '   ', '   ', '  ', 'I', 'E', 'Ineseño', false, '');
INSERT INTO languages_iso639 VALUES ('ior', '   ', '   ', '  ', 'I', 'L', 'Inor', false, '');
INSERT INTO languages_iso639 VALUES ('iou', '   ', '   ', '  ', 'I', 'L', 'Tuma-Irumu', false, '');
INSERT INTO languages_iso639 VALUES ('iow', '   ', '   ', '  ', 'I', 'E', 'Iowa-Oto', false, '');
INSERT INTO languages_iso639 VALUES ('ipi', '   ', '   ', '  ', 'I', 'L', 'Ipili', false, '');
INSERT INTO languages_iso639 VALUES ('ipk', 'ipk', 'ipk', 'ik', 'M', 'L', 'Inupiaq', false, '');
INSERT INTO languages_iso639 VALUES ('ipo', '   ', '   ', '  ', 'I', 'L', 'Ipiko', false, '');
INSERT INTO languages_iso639 VALUES ('iqu', '   ', '   ', '  ', 'I', 'L', 'Iquito', false, '');
INSERT INTO languages_iso639 VALUES ('iqw', '   ', '   ', '  ', 'I', 'L', 'Ikwo', false, '');
INSERT INTO languages_iso639 VALUES ('ire', '   ', '   ', '  ', 'I', 'L', 'Iresim', false, '');
INSERT INTO languages_iso639 VALUES ('irh', '   ', '   ', '  ', 'I', 'L', 'Irarutu', false, '');
INSERT INTO languages_iso639 VALUES ('iri', '   ', '   ', '  ', 'I', 'L', 'Irigwe', false, '');
INSERT INTO languages_iso639 VALUES ('irk', '   ', '   ', '  ', 'I', 'L', 'Iraqw', false, '');
INSERT INTO languages_iso639 VALUES ('irn', '   ', '   ', '  ', 'I', 'L', 'Irántxe', false, '');
INSERT INTO languages_iso639 VALUES ('irr', '   ', '   ', '  ', 'I', 'L', 'Ir', false, '');
INSERT INTO languages_iso639 VALUES ('iru', '   ', '   ', '  ', 'I', 'L', 'Irula', false, '');
INSERT INTO languages_iso639 VALUES ('irx', '   ', '   ', '  ', 'I', 'L', 'Kamberau', false, '');
INSERT INTO languages_iso639 VALUES ('iry', '   ', '   ', '  ', 'I', 'L', 'Iraya', false, '');
INSERT INTO languages_iso639 VALUES ('isa', '   ', '   ', '  ', 'I', 'L', 'Isabi', false, '');
INSERT INTO languages_iso639 VALUES ('isc', '   ', '   ', '  ', 'I', 'L', 'Isconahua', false, '');
INSERT INTO languages_iso639 VALUES ('isd', '   ', '   ', '  ', 'I', 'L', 'Isnag', false, '');
INSERT INTO languages_iso639 VALUES ('ise', '   ', '   ', '  ', 'I', 'L', 'Italian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('isg', '   ', '   ', '  ', 'I', 'L', 'Irish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ish', '   ', '   ', '  ', 'I', 'L', 'Esan', false, '');
INSERT INTO languages_iso639 VALUES ('isi', '   ', '   ', '  ', 'I', 'L', 'Nkem-Nkum', false, '');
INSERT INTO languages_iso639 VALUES ('isk', '   ', '   ', '  ', 'I', 'L', 'Ishkashimi', false, '');
INSERT INTO languages_iso639 VALUES ('isl', 'ice', 'isl', 'is', 'I', 'L', 'Icelandic', false, '');
INSERT INTO languages_iso639 VALUES ('ism', '   ', '   ', '  ', 'I', 'L', 'Masimasi', false, '');
INSERT INTO languages_iso639 VALUES ('isn', '   ', '   ', '  ', 'I', 'L', 'Isanzu', false, '');
INSERT INTO languages_iso639 VALUES ('iso', '   ', '   ', '  ', 'I', 'L', 'Isoko', false, '');
INSERT INTO languages_iso639 VALUES ('isr', '   ', '   ', '  ', 'I', 'L', 'Israeli Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ist', '   ', '   ', '  ', 'I', 'L', 'Istriot', false, '');
INSERT INTO languages_iso639 VALUES ('isu', '   ', '   ', '  ', 'I', 'L', 'Isu (Menchum Division)', false, '');
INSERT INTO languages_iso639 VALUES ('ita', 'ita', 'ita', 'it', 'I', 'L', 'Italian', false, '');
INSERT INTO languages_iso639 VALUES ('itb', '   ', '   ', '  ', 'I', 'L', 'Binongan Itneg', false, '');
INSERT INTO languages_iso639 VALUES ('ite', '   ', '   ', '  ', 'I', 'E', 'Itene', false, '');
INSERT INTO languages_iso639 VALUES ('iti', '   ', '   ', '  ', 'I', 'L', 'Inlaod Itneg', false, '');
INSERT INTO languages_iso639 VALUES ('itk', '   ', '   ', '  ', 'I', 'L', 'Judeo-Italian', false, '');
INSERT INTO languages_iso639 VALUES ('itl', '   ', '   ', '  ', 'I', 'L', 'Itelmen', false, '');
INSERT INTO languages_iso639 VALUES ('itm', '   ', '   ', '  ', 'I', 'L', 'Itu Mbon Uzo', false, '');
INSERT INTO languages_iso639 VALUES ('ito', '   ', '   ', '  ', 'I', 'L', 'Itonama', false, '');
INSERT INTO languages_iso639 VALUES ('itr', '   ', '   ', '  ', 'I', 'L', 'Iteri', false, '');
INSERT INTO languages_iso639 VALUES ('its', '   ', '   ', '  ', 'I', 'L', 'Isekiri', false, '');
INSERT INTO languages_iso639 VALUES ('itt', '   ', '   ', '  ', 'I', 'L', 'Maeng Itneg', false, '');
INSERT INTO languages_iso639 VALUES ('itv', '   ', '   ', '  ', 'I', 'L', 'Itawit', false, '');
INSERT INTO languages_iso639 VALUES ('itw', '   ', '   ', '  ', 'I', 'L', 'Ito', false, '');
INSERT INTO languages_iso639 VALUES ('itx', '   ', '   ', '  ', 'I', 'L', 'Itik', false, '');
INSERT INTO languages_iso639 VALUES ('ity', '   ', '   ', '  ', 'I', 'L', 'Moyadan Itneg', false, '');
INSERT INTO languages_iso639 VALUES ('itz', '   ', '   ', '  ', 'I', 'L', 'Itzá', false, '');
INSERT INTO languages_iso639 VALUES ('ium', '   ', '   ', '  ', 'I', 'L', 'Iu Mien', false, '');
INSERT INTO languages_iso639 VALUES ('ivb', '   ', '   ', '  ', 'I', 'L', 'Ibatan', false, '');
INSERT INTO languages_iso639 VALUES ('ivv', '   ', '   ', '  ', 'I', 'L', 'Ivatan', false, '');
INSERT INTO languages_iso639 VALUES ('iwk', '   ', '   ', '  ', 'I', 'L', 'I-Wak', false, '');
INSERT INTO languages_iso639 VALUES ('iwm', '   ', '   ', '  ', 'I', 'L', 'Iwam', false, '');
INSERT INTO languages_iso639 VALUES ('iwo', '   ', '   ', '  ', 'I', 'L', 'Iwur', false, '');
INSERT INTO languages_iso639 VALUES ('iws', '   ', '   ', '  ', 'I', 'L', 'Sepik Iwam', false, '');
INSERT INTO languages_iso639 VALUES ('ixc', '   ', '   ', '  ', 'I', 'L', 'Ixcatec', false, '');
INSERT INTO languages_iso639 VALUES ('ixl', '   ', '   ', '  ', 'I', 'L', 'Ixil', false, '');
INSERT INTO languages_iso639 VALUES ('iya', '   ', '   ', '  ', 'I', 'L', 'Iyayu', false, '');
INSERT INTO languages_iso639 VALUES ('iyo', '   ', '   ', '  ', 'I', 'L', 'Mesaka', false, '');
INSERT INTO languages_iso639 VALUES ('iyx', '   ', '   ', '  ', 'I', 'L', 'Yaka (Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('izh', '   ', '   ', '  ', 'I', 'L', 'Ingrian', false, '');
INSERT INTO languages_iso639 VALUES ('izr', '   ', '   ', '  ', 'I', 'L', 'Izere', false, '');
INSERT INTO languages_iso639 VALUES ('izz', '   ', '   ', '  ', 'I', 'L', 'Izii', false, '');
INSERT INTO languages_iso639 VALUES ('jaa', '   ', '   ', '  ', 'I', 'L', 'Jamamadí', false, '');
INSERT INTO languages_iso639 VALUES ('jab', '   ', '   ', '  ', 'I', 'L', 'Hyam', false, '');
INSERT INTO languages_iso639 VALUES ('jac', '   ', '   ', '  ', 'I', 'L', 'Popti''', false, '');
INSERT INTO languages_iso639 VALUES ('jad', '   ', '   ', '  ', 'I', 'L', 'Jahanka', false, '');
INSERT INTO languages_iso639 VALUES ('jae', '   ', '   ', '  ', 'I', 'L', 'Yabem', false, '');
INSERT INTO languages_iso639 VALUES ('jaf', '   ', '   ', '  ', 'I', 'L', 'Jara', false, '');
INSERT INTO languages_iso639 VALUES ('jah', '   ', '   ', '  ', 'I', 'L', 'Jah Hut', false, '');
INSERT INTO languages_iso639 VALUES ('jaj', '   ', '   ', '  ', 'I', 'L', 'Zazao', false, '');
INSERT INTO languages_iso639 VALUES ('jak', '   ', '   ', '  ', 'I', 'L', 'Jakun', false, '');
INSERT INTO languages_iso639 VALUES ('jal', '   ', '   ', '  ', 'I', 'L', 'Yalahatan', false, '');
INSERT INTO languages_iso639 VALUES ('jam', '   ', '   ', '  ', 'I', 'L', 'Jamaican Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('jan', '   ', '   ', '  ', 'I', 'E', 'Jandai', false, '');
INSERT INTO languages_iso639 VALUES ('jao', '   ', '   ', '  ', 'I', 'L', 'Yanyuwa', false, '');
INSERT INTO languages_iso639 VALUES ('jaq', '   ', '   ', '  ', 'I', 'L', 'Yaqay', false, '');
INSERT INTO languages_iso639 VALUES ('jas', '   ', '   ', '  ', 'I', 'L', 'New Caledonian Javanese', false, '');
INSERT INTO languages_iso639 VALUES ('jat', '   ', '   ', '  ', 'I', 'L', 'Jakati', false, '');
INSERT INTO languages_iso639 VALUES ('jau', '   ', '   ', '  ', 'I', 'L', 'Yaur', false, '');
INSERT INTO languages_iso639 VALUES ('jav', 'jav', 'jav', 'jv', 'I', 'L', 'Javanese', false, '');
INSERT INTO languages_iso639 VALUES ('jax', '   ', '   ', '  ', 'I', 'L', 'Jambi Malay', false, '');
INSERT INTO languages_iso639 VALUES ('jay', '   ', '   ', '  ', 'I', 'L', 'Yan-nhangu', false, '');
INSERT INTO languages_iso639 VALUES ('jaz', '   ', '   ', '  ', 'I', 'L', 'Jawe', false, '');
INSERT INTO languages_iso639 VALUES ('jbe', '   ', '   ', '  ', 'I', 'L', 'Judeo-Berber', false, '');
INSERT INTO languages_iso639 VALUES ('jbi', '   ', '   ', '  ', 'I', 'E', 'Badjiri', false, '');
INSERT INTO languages_iso639 VALUES ('jbj', '   ', '   ', '  ', 'I', 'L', 'Arandai', false, '');
INSERT INTO languages_iso639 VALUES ('jbk', '   ', '   ', '  ', 'I', 'L', 'Barikewa', false, '');
INSERT INTO languages_iso639 VALUES ('jbn', '   ', '   ', '  ', 'I', 'L', 'Nafusi', false, '');
INSERT INTO languages_iso639 VALUES ('jbo', 'jbo', 'jbo', '  ', 'I', 'C', 'Lojban', false, '');
INSERT INTO languages_iso639 VALUES ('jbr', '   ', '   ', '  ', 'I', 'L', 'Jofotek-Bromnya', false, '');
INSERT INTO languages_iso639 VALUES ('jbt', '   ', '   ', '  ', 'I', 'L', 'Jabutí', false, '');
INSERT INTO languages_iso639 VALUES ('jbu', '   ', '   ', '  ', 'I', 'L', 'Jukun Takum', false, '');
INSERT INTO languages_iso639 VALUES ('jbw', '   ', '   ', '  ', 'I', 'E', 'Yawijibaya', false, '');
INSERT INTO languages_iso639 VALUES ('jcs', '   ', '   ', '  ', 'I', 'L', 'Jamaican Country Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('jct', '   ', '   ', '  ', 'I', 'L', 'Krymchak', false, '');
INSERT INTO languages_iso639 VALUES ('jda', '   ', '   ', '  ', 'I', 'L', 'Jad', false, '');
INSERT INTO languages_iso639 VALUES ('jdg', '   ', '   ', '  ', 'I', 'L', 'Jadgali', false, '');
INSERT INTO languages_iso639 VALUES ('jdt', '   ', '   ', '  ', 'I', 'L', 'Judeo-Tat', false, '');
INSERT INTO languages_iso639 VALUES ('jeb', '   ', '   ', '  ', 'I', 'L', 'Jebero', false, '');
INSERT INTO languages_iso639 VALUES ('jee', '   ', '   ', '  ', 'I', 'L', 'Jerung', false, '');
INSERT INTO languages_iso639 VALUES ('jeg', '   ', '   ', '  ', 'I', 'L', 'Jeng', false, '');
INSERT INTO languages_iso639 VALUES ('jeh', '   ', '   ', '  ', 'I', 'L', 'Jeh', false, '');
INSERT INTO languages_iso639 VALUES ('jei', '   ', '   ', '  ', 'I', 'L', 'Yei', false, '');
INSERT INTO languages_iso639 VALUES ('jek', '   ', '   ', '  ', 'I', 'L', 'Jeri Kuo', false, '');
INSERT INTO languages_iso639 VALUES ('jel', '   ', '   ', '  ', 'I', 'L', 'Yelmek', false, '');
INSERT INTO languages_iso639 VALUES ('jen', '   ', '   ', '  ', 'I', 'L', 'Dza', false, '');
INSERT INTO languages_iso639 VALUES ('jer', '   ', '   ', '  ', 'I', 'L', 'Jere', false, '');
INSERT INTO languages_iso639 VALUES ('jet', '   ', '   ', '  ', 'I', 'L', 'Manem', false, '');
INSERT INTO languages_iso639 VALUES ('jeu', '   ', '   ', '  ', 'I', 'L', 'Jonkor Bourmataguil', false, '');
INSERT INTO languages_iso639 VALUES ('jgb', '   ', '   ', '  ', 'I', 'E', 'Ngbee', false, '');
INSERT INTO languages_iso639 VALUES ('jge', '   ', '   ', '  ', 'I', 'L', 'Judeo-Georgian', false, '');
INSERT INTO languages_iso639 VALUES ('jgk', '   ', '   ', '  ', 'I', 'L', 'Gwak', false, '');
INSERT INTO languages_iso639 VALUES ('jgo', '   ', '   ', '  ', 'I', 'L', 'Ngomba', false, '');
INSERT INTO languages_iso639 VALUES ('jhi', '   ', '   ', '  ', 'I', 'L', 'Jehai', false, '');
INSERT INTO languages_iso639 VALUES ('jhs', '   ', '   ', '  ', 'I', 'L', 'Jhankot Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('jia', '   ', '   ', '  ', 'I', 'L', 'Jina', false, '');
INSERT INTO languages_iso639 VALUES ('jib', '   ', '   ', '  ', 'I', 'L', 'Jibu', false, '');
INSERT INTO languages_iso639 VALUES ('jic', '   ', '   ', '  ', 'I', 'L', 'Tol', false, '');
INSERT INTO languages_iso639 VALUES ('jid', '   ', '   ', '  ', 'I', 'L', 'Bu', false, '');
INSERT INTO languages_iso639 VALUES ('jie', '   ', '   ', '  ', 'I', 'L', 'Jilbe', false, '');
INSERT INTO languages_iso639 VALUES ('jig', '   ', '   ', '  ', 'I', 'L', 'Djingili', false, '');
INSERT INTO languages_iso639 VALUES ('jih', '   ', '   ', '  ', 'I', 'L', 'sTodsde', false, '');
INSERT INTO languages_iso639 VALUES ('jii', '   ', '   ', '  ', 'I', 'L', 'Jiiddu', false, '');
INSERT INTO languages_iso639 VALUES ('jil', '   ', '   ', '  ', 'I', 'L', 'Jilim', false, '');
INSERT INTO languages_iso639 VALUES ('jim', '   ', '   ', '  ', 'I', 'L', 'Jimi (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('jio', '   ', '   ', '  ', 'I', 'L', 'Jiamao', false, '');
INSERT INTO languages_iso639 VALUES ('jiq', '   ', '   ', '  ', 'I', 'L', 'Guanyinqiao', false, '');
INSERT INTO languages_iso639 VALUES ('jit', '   ', '   ', '  ', 'I', 'L', 'Jita', false, '');
INSERT INTO languages_iso639 VALUES ('jiu', '   ', '   ', '  ', 'I', 'L', 'Youle Jinuo', false, '');
INSERT INTO languages_iso639 VALUES ('jiv', '   ', '   ', '  ', 'I', 'L', 'Shuar', false, '');
INSERT INTO languages_iso639 VALUES ('jiy', '   ', '   ', '  ', 'I', 'L', 'Buyuan Jinuo', false, '');
INSERT INTO languages_iso639 VALUES ('jjr', '   ', '   ', '  ', 'I', 'L', 'Bankal', false, '');
INSERT INTO languages_iso639 VALUES ('jkm', '   ', '   ', '  ', 'I', 'L', 'Mobwa Karen', false, '');
INSERT INTO languages_iso639 VALUES ('jko', '   ', '   ', '  ', 'I', 'L', 'Kubo', false, '');
INSERT INTO languages_iso639 VALUES ('jkp', '   ', '   ', '  ', 'I', 'L', 'Paku Karen', false, '');
INSERT INTO languages_iso639 VALUES ('jkr', '   ', '   ', '  ', 'I', 'L', 'Koro (India)', false, '');
INSERT INTO languages_iso639 VALUES ('jku', '   ', '   ', '  ', 'I', 'L', 'Labir', false, '');
INSERT INTO languages_iso639 VALUES ('jle', '   ', '   ', '  ', 'I', 'L', 'Ngile', false, '');
INSERT INTO languages_iso639 VALUES ('jls', '   ', '   ', '  ', 'I', 'L', 'Jamaican Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('jma', '   ', '   ', '  ', 'I', 'L', 'Dima', false, '');
INSERT INTO languages_iso639 VALUES ('jmb', '   ', '   ', '  ', 'I', 'L', 'Zumbun', false, '');
INSERT INTO languages_iso639 VALUES ('jmc', '   ', '   ', '  ', 'I', 'L', 'Machame', false, '');
INSERT INTO languages_iso639 VALUES ('jmd', '   ', '   ', '  ', 'I', 'L', 'Yamdena', false, '');
INSERT INTO languages_iso639 VALUES ('jmi', '   ', '   ', '  ', 'I', 'L', 'Jimi (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('jml', '   ', '   ', '  ', 'I', 'L', 'Jumli', false, '');
INSERT INTO languages_iso639 VALUES ('jmn', '   ', '   ', '  ', 'I', 'L', 'Makuri Naga', false, '');
INSERT INTO languages_iso639 VALUES ('jmr', '   ', '   ', '  ', 'I', 'L', 'Kamara', false, '');
INSERT INTO languages_iso639 VALUES ('jms', '   ', '   ', '  ', 'I', 'L', 'Mashi (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('jmw', '   ', '   ', '  ', 'I', 'L', 'Mouwase', false, '');
INSERT INTO languages_iso639 VALUES ('jmx', '   ', '   ', '  ', 'I', 'L', 'Western Juxtlahuaca Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('jna', '   ', '   ', '  ', 'I', 'L', 'Jangshung', false, '');
INSERT INTO languages_iso639 VALUES ('jnd', '   ', '   ', '  ', 'I', 'L', 'Jandavra', false, '');
INSERT INTO languages_iso639 VALUES ('jng', '   ', '   ', '  ', 'I', 'E', 'Yangman', false, '');
INSERT INTO languages_iso639 VALUES ('jni', '   ', '   ', '  ', 'I', 'L', 'Janji', false, '');
INSERT INTO languages_iso639 VALUES ('jnj', '   ', '   ', '  ', 'I', 'L', 'Yemsa', false, '');
INSERT INTO languages_iso639 VALUES ('jnl', '   ', '   ', '  ', 'I', 'L', 'Rawat', false, '');
INSERT INTO languages_iso639 VALUES ('jns', '   ', '   ', '  ', 'I', 'L', 'Jaunsari', false, '');
INSERT INTO languages_iso639 VALUES ('job', '   ', '   ', '  ', 'I', 'L', 'Joba', false, '');
INSERT INTO languages_iso639 VALUES ('jod', '   ', '   ', '  ', 'I', 'L', 'Wojenaka', false, '');
INSERT INTO languages_iso639 VALUES ('jor', '   ', '   ', '  ', 'I', 'E', 'Jorá', false, '');
INSERT INTO languages_iso639 VALUES ('jos', '   ', '   ', '  ', 'I', 'L', 'Jordanian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('jow', '   ', '   ', '  ', 'I', 'L', 'Jowulu', false, '');
INSERT INTO languages_iso639 VALUES ('jpa', '   ', '   ', '  ', 'I', 'H', 'Jewish Palestinian Aramaic', false, '');
INSERT INTO languages_iso639 VALUES ('jpn', 'jpn', 'jpn', 'ja', 'I', 'L', 'Japanese', false, '');
INSERT INTO languages_iso639 VALUES ('jpr', 'jpr', 'jpr', '  ', 'I', 'L', 'Judeo-Persian', false, '');
INSERT INTO languages_iso639 VALUES ('jqr', '   ', '   ', '  ', 'I', 'L', 'Jaqaru', false, '');
INSERT INTO languages_iso639 VALUES ('jra', '   ', '   ', '  ', 'I', 'L', 'Jarai', false, '');
INSERT INTO languages_iso639 VALUES ('jrb', 'jrb', 'jrb', '  ', 'M', 'L', 'Judeo-Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('jrr', '   ', '   ', '  ', 'I', 'L', 'Jiru', false, '');
INSERT INTO languages_iso639 VALUES ('jrt', '   ', '   ', '  ', 'I', 'L', 'Jorto', false, '');
INSERT INTO languages_iso639 VALUES ('jru', '   ', '   ', '  ', 'I', 'L', 'Japrería', false, '');
INSERT INTO languages_iso639 VALUES ('jsl', '   ', '   ', '  ', 'I', 'L', 'Japanese Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('jua', '   ', '   ', '  ', 'I', 'L', 'Júma', false, '');
INSERT INTO languages_iso639 VALUES ('jub', '   ', '   ', '  ', 'I', 'L', 'Wannu', false, '');
INSERT INTO languages_iso639 VALUES ('juc', '   ', '   ', '  ', 'I', 'E', 'Jurchen', false, '');
INSERT INTO languages_iso639 VALUES ('jud', '   ', '   ', '  ', 'I', 'L', 'Worodougou', false, '');
INSERT INTO languages_iso639 VALUES ('juh', '   ', '   ', '  ', 'I', 'L', 'Hõne', false, '');
INSERT INTO languages_iso639 VALUES ('jui', '   ', '   ', '  ', 'I', 'E', 'Ngadjuri', false, '');
INSERT INTO languages_iso639 VALUES ('juk', '   ', '   ', '  ', 'I', 'L', 'Wapan', false, '');
INSERT INTO languages_iso639 VALUES ('jul', '   ', '   ', '  ', 'I', 'L', 'Jirel', false, '');
INSERT INTO languages_iso639 VALUES ('jum', '   ', '   ', '  ', 'I', 'L', 'Jumjum', false, '');
INSERT INTO languages_iso639 VALUES ('jun', '   ', '   ', '  ', 'I', 'L', 'Juang', false, '');
INSERT INTO languages_iso639 VALUES ('juo', '   ', '   ', '  ', 'I', 'L', 'Jiba', false, '');
INSERT INTO languages_iso639 VALUES ('jup', '   ', '   ', '  ', 'I', 'L', 'Hupdë', false, '');
INSERT INTO languages_iso639 VALUES ('jur', '   ', '   ', '  ', 'I', 'L', 'Jurúna', false, '');
INSERT INTO languages_iso639 VALUES ('jus', '   ', '   ', '  ', 'I', 'L', 'Jumla Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('jut', '   ', '   ', '  ', 'I', 'L', 'Jutish', false, '');
INSERT INTO languages_iso639 VALUES ('juu', '   ', '   ', '  ', 'I', 'L', 'Ju', false, '');
INSERT INTO languages_iso639 VALUES ('juw', '   ', '   ', '  ', 'I', 'L', 'Wãpha', false, '');
INSERT INTO languages_iso639 VALUES ('juy', '   ', '   ', '  ', 'I', 'L', 'Juray', false, '');
INSERT INTO languages_iso639 VALUES ('jvd', '   ', '   ', '  ', 'I', 'E', 'Javindo', false, '');
INSERT INTO languages_iso639 VALUES ('jvn', '   ', '   ', '  ', 'I', 'L', 'Caribbean Javanese', false, '');
INSERT INTO languages_iso639 VALUES ('jwi', '   ', '   ', '  ', 'I', 'L', 'Jwira-Pepesa', false, '');
INSERT INTO languages_iso639 VALUES ('jya', '   ', '   ', '  ', 'I', 'L', 'Jiarong', false, '');
INSERT INTO languages_iso639 VALUES ('jye', '   ', '   ', '  ', 'I', 'L', 'Judeo-Yemeni Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('jyy', '   ', '   ', '  ', 'I', 'L', 'Jaya', false, '');
INSERT INTO languages_iso639 VALUES ('kaa', 'kaa', 'kaa', '  ', 'I', 'L', 'Kara-Kalpak', false, '');
INSERT INTO languages_iso639 VALUES ('kab', 'kab', 'kab', '  ', 'I', 'L', 'Kabyle', false, '');
INSERT INTO languages_iso639 VALUES ('kac', 'kac', 'kac', '  ', 'I', 'L', 'Kachin', false, '');
INSERT INTO languages_iso639 VALUES ('kad', '   ', '   ', '  ', 'I', 'L', 'Adara', false, '');
INSERT INTO languages_iso639 VALUES ('kae', '   ', '   ', '  ', 'I', 'E', 'Ketangalan', false, '');
INSERT INTO languages_iso639 VALUES ('kaf', '   ', '   ', '  ', 'I', 'L', 'Katso', false, '');
INSERT INTO languages_iso639 VALUES ('kag', '   ', '   ', '  ', 'I', 'L', 'Kajaman', false, '');
INSERT INTO languages_iso639 VALUES ('kah', '   ', '   ', '  ', 'I', 'L', 'Kara (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('kai', '   ', '   ', '  ', 'I', 'L', 'Karekare', false, '');
INSERT INTO languages_iso639 VALUES ('kaj', '   ', '   ', '  ', 'I', 'L', 'Jju', false, '');
INSERT INTO languages_iso639 VALUES ('kak', '   ', '   ', '  ', 'I', 'L', 'Kayapa Kallahan', false, '');
INSERT INTO languages_iso639 VALUES ('kal', 'kal', 'kal', 'kl', 'I', 'L', 'Kalaallisut', false, '');
INSERT INTO languages_iso639 VALUES ('kam', 'kam', 'kam', '  ', 'I', 'L', 'Kamba (Kenya)', false, '');
INSERT INTO languages_iso639 VALUES ('kan', 'kan', 'kan', 'kn', 'I', 'L', 'Kannada', false, '');
INSERT INTO languages_iso639 VALUES ('kao', '   ', '   ', '  ', 'I', 'L', 'Xaasongaxango', false, '');
INSERT INTO languages_iso639 VALUES ('kap', '   ', '   ', '  ', 'I', 'L', 'Bezhta', false, '');
INSERT INTO languages_iso639 VALUES ('kaq', '   ', '   ', '  ', 'I', 'L', 'Capanahua', false, '');
INSERT INTO languages_iso639 VALUES ('kas', 'kas', 'kas', 'ks', 'I', 'L', 'Kashmiri', false, '');
INSERT INTO languages_iso639 VALUES ('kat', 'geo', 'kat', 'ka', 'I', 'L', 'Georgian', false, '');
INSERT INTO languages_iso639 VALUES ('kau', 'kau', 'kau', 'kr', 'M', 'L', 'Kanuri', false, '');
INSERT INTO languages_iso639 VALUES ('kav', '   ', '   ', '  ', 'I', 'L', 'Katukína', false, '');
INSERT INTO languages_iso639 VALUES ('kaw', 'kaw', 'kaw', '  ', 'I', 'A', 'Kawi', false, '');
INSERT INTO languages_iso639 VALUES ('kax', '   ', '   ', '  ', 'I', 'L', 'Kao', false, '');
INSERT INTO languages_iso639 VALUES ('kay', '   ', '   ', '  ', 'I', 'L', 'Kamayurá', false, '');
INSERT INTO languages_iso639 VALUES ('kaz', 'kaz', 'kaz', 'kk', 'I', 'L', 'Kazakh', false, '');
INSERT INTO languages_iso639 VALUES ('kba', '   ', '   ', '  ', 'I', 'E', 'Kalarko', false, '');
INSERT INTO languages_iso639 VALUES ('kbb', '   ', '   ', '  ', 'I', 'E', 'Kaxuiâna', false, '');
INSERT INTO languages_iso639 VALUES ('kbc', '   ', '   ', '  ', 'I', 'L', 'Kadiwéu', false, '');
INSERT INTO languages_iso639 VALUES ('kbd', 'kbd', 'kbd', '  ', 'I', 'L', 'Kabardian', false, '');
INSERT INTO languages_iso639 VALUES ('kbe', '   ', '   ', '  ', 'I', 'L', 'Kanju', false, '');
INSERT INTO languages_iso639 VALUES ('kbf', '   ', '   ', '  ', 'I', 'E', 'Kakauhua', false, '');
INSERT INTO languages_iso639 VALUES ('kbg', '   ', '   ', '  ', 'I', 'L', 'Khamba', false, '');
INSERT INTO languages_iso639 VALUES ('kbh', '   ', '   ', '  ', 'I', 'L', 'Camsá', false, '');
INSERT INTO languages_iso639 VALUES ('kbi', '   ', '   ', '  ', 'I', 'L', 'Kaptiau', false, '');
INSERT INTO languages_iso639 VALUES ('kbj', '   ', '   ', '  ', 'I', 'L', 'Kari', false, '');
INSERT INTO languages_iso639 VALUES ('kbk', '   ', '   ', '  ', 'I', 'L', 'Grass Koiari', false, '');
INSERT INTO languages_iso639 VALUES ('kbl', '   ', '   ', '  ', 'I', 'L', 'Kanembu', false, '');
INSERT INTO languages_iso639 VALUES ('kbm', '   ', '   ', '  ', 'I', 'L', 'Iwal', false, '');
INSERT INTO languages_iso639 VALUES ('kbn', '   ', '   ', '  ', 'I', 'L', 'Kare (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('kbo', '   ', '   ', '  ', 'I', 'L', 'Keliko', false, '');
INSERT INTO languages_iso639 VALUES ('kbp', '   ', '   ', '  ', 'I', 'L', 'Kabiyè', false, '');
INSERT INTO languages_iso639 VALUES ('kbq', '   ', '   ', '  ', 'I', 'L', 'Kamano', false, '');
INSERT INTO languages_iso639 VALUES ('kbr', '   ', '   ', '  ', 'I', 'L', 'Kafa', false, '');
INSERT INTO languages_iso639 VALUES ('kbs', '   ', '   ', '  ', 'I', 'L', 'Kande', false, '');
INSERT INTO languages_iso639 VALUES ('kbt', '   ', '   ', '  ', 'I', 'L', 'Abadi', false, '');
INSERT INTO languages_iso639 VALUES ('kbu', '   ', '   ', '  ', 'I', 'L', 'Kabutra', false, '');
INSERT INTO languages_iso639 VALUES ('kbv', '   ', '   ', '  ', 'I', 'L', 'Dera (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('kbw', '   ', '   ', '  ', 'I', 'L', 'Kaiep', false, '');
INSERT INTO languages_iso639 VALUES ('kbx', '   ', '   ', '  ', 'I', 'L', 'Ap Ma', false, '');
INSERT INTO languages_iso639 VALUES ('kby', '   ', '   ', '  ', 'I', 'L', 'Manga Kanuri', false, '');
INSERT INTO languages_iso639 VALUES ('kbz', '   ', '   ', '  ', 'I', 'L', 'Duhwa', false, '');
INSERT INTO languages_iso639 VALUES ('kca', '   ', '   ', '  ', 'I', 'L', 'Khanty', false, '');
INSERT INTO languages_iso639 VALUES ('kcb', '   ', '   ', '  ', 'I', 'L', 'Kawacha', false, '');
INSERT INTO languages_iso639 VALUES ('kcc', '   ', '   ', '  ', 'I', 'L', 'Lubila', false, '');
INSERT INTO languages_iso639 VALUES ('kcd', '   ', '   ', '  ', 'I', 'L', 'Ngkâlmpw Kanum', false, '');
INSERT INTO languages_iso639 VALUES ('kce', '   ', '   ', '  ', 'I', 'L', 'Kaivi', false, '');
INSERT INTO languages_iso639 VALUES ('kcf', '   ', '   ', '  ', 'I', 'L', 'Ukaan', false, '');
INSERT INTO languages_iso639 VALUES ('kcg', '   ', '   ', '  ', 'I', 'L', 'Tyap', false, '');
INSERT INTO languages_iso639 VALUES ('kch', '   ', '   ', '  ', 'I', 'L', 'Vono', false, '');
INSERT INTO languages_iso639 VALUES ('kci', '   ', '   ', '  ', 'I', 'L', 'Kamantan', false, '');
INSERT INTO languages_iso639 VALUES ('kcj', '   ', '   ', '  ', 'I', 'L', 'Kobiana', false, '');
INSERT INTO languages_iso639 VALUES ('kck', '   ', '   ', '  ', 'I', 'L', 'Kalanga', false, '');
INSERT INTO languages_iso639 VALUES ('kcl', '   ', '   ', '  ', 'I', 'L', 'Kela (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('kcm', '   ', '   ', '  ', 'I', 'L', 'Gula (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('kcn', '   ', '   ', '  ', 'I', 'L', 'Nubi', false, '');
INSERT INTO languages_iso639 VALUES ('kco', '   ', '   ', '  ', 'I', 'L', 'Kinalakna', false, '');
INSERT INTO languages_iso639 VALUES ('kcp', '   ', '   ', '  ', 'I', 'L', 'Kanga', false, '');
INSERT INTO languages_iso639 VALUES ('kcq', '   ', '   ', '  ', 'I', 'L', 'Kamo', false, '');
INSERT INTO languages_iso639 VALUES ('kcr', '   ', '   ', '  ', 'I', 'L', 'Katla', false, '');
INSERT INTO languages_iso639 VALUES ('kcs', '   ', '   ', '  ', 'I', 'L', 'Koenoem', false, '');
INSERT INTO languages_iso639 VALUES ('kct', '   ', '   ', '  ', 'I', 'L', 'Kaian', false, '');
INSERT INTO languages_iso639 VALUES ('kcu', '   ', '   ', '  ', 'I', 'L', 'Kami (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('kcv', '   ', '   ', '  ', 'I', 'L', 'Kete', false, '');
INSERT INTO languages_iso639 VALUES ('kcw', '   ', '   ', '  ', 'I', 'L', 'Kabwari', false, '');
INSERT INTO languages_iso639 VALUES ('kcx', '   ', '   ', '  ', 'I', 'L', 'Kachama-Ganjule', false, '');
INSERT INTO languages_iso639 VALUES ('kcy', '   ', '   ', '  ', 'I', 'L', 'Korandje', false, '');
INSERT INTO languages_iso639 VALUES ('kcz', '   ', '   ', '  ', 'I', 'L', 'Konongo', false, '');
INSERT INTO languages_iso639 VALUES ('kda', '   ', '   ', '  ', 'I', 'E', 'Worimi', false, '');
INSERT INTO languages_iso639 VALUES ('kdc', '   ', '   ', '  ', 'I', 'L', 'Kutu', false, '');
INSERT INTO languages_iso639 VALUES ('kdd', '   ', '   ', '  ', 'I', 'L', 'Yankunytjatjara', false, '');
INSERT INTO languages_iso639 VALUES ('kde', '   ', '   ', '  ', 'I', 'L', 'Makonde', false, '');
INSERT INTO languages_iso639 VALUES ('kdf', '   ', '   ', '  ', 'I', 'L', 'Mamusi', false, '');
INSERT INTO languages_iso639 VALUES ('kdg', '   ', '   ', '  ', 'I', 'L', 'Seba', false, '');
INSERT INTO languages_iso639 VALUES ('kdh', '   ', '   ', '  ', 'I', 'L', 'Tem', false, '');
INSERT INTO languages_iso639 VALUES ('kdi', '   ', '   ', '  ', 'I', 'L', 'Kumam', false, '');
INSERT INTO languages_iso639 VALUES ('kdj', '   ', '   ', '  ', 'I', 'L', 'Karamojong', false, '');
INSERT INTO languages_iso639 VALUES ('kdk', '   ', '   ', '  ', 'I', 'L', 'Numèè', false, '');
INSERT INTO languages_iso639 VALUES ('kdl', '   ', '   ', '  ', 'I', 'L', 'Tsikimba', false, '');
INSERT INTO languages_iso639 VALUES ('kdm', '   ', '   ', '  ', 'I', 'L', 'Kagoma', false, '');
INSERT INTO languages_iso639 VALUES ('kdn', '   ', '   ', '  ', 'I', 'L', 'Kunda', false, '');
INSERT INTO languages_iso639 VALUES ('kdp', '   ', '   ', '  ', 'I', 'L', 'Kaningdon-Nindem', false, '');
INSERT INTO languages_iso639 VALUES ('kdq', '   ', '   ', '  ', 'I', 'L', 'Koch', false, '');
INSERT INTO languages_iso639 VALUES ('kdr', '   ', '   ', '  ', 'I', 'L', 'Karaim', false, '');
INSERT INTO languages_iso639 VALUES ('kdt', '   ', '   ', '  ', 'I', 'L', 'Kuy', false, '');
INSERT INTO languages_iso639 VALUES ('kdu', '   ', '   ', '  ', 'I', 'L', 'Kadaru', false, '');
INSERT INTO languages_iso639 VALUES ('kdw', '   ', '   ', '  ', 'I', 'L', 'Koneraw', false, '');
INSERT INTO languages_iso639 VALUES ('kdx', '   ', '   ', '  ', 'I', 'L', 'Kam', false, '');
INSERT INTO languages_iso639 VALUES ('kdy', '   ', '   ', '  ', 'I', 'L', 'Keder', false, '');
INSERT INTO languages_iso639 VALUES ('kdz', '   ', '   ', '  ', 'I', 'L', 'Kwaja', false, '');
INSERT INTO languages_iso639 VALUES ('kea', '   ', '   ', '  ', 'I', 'L', 'Kabuverdianu', false, '');
INSERT INTO languages_iso639 VALUES ('keb', '   ', '   ', '  ', 'I', 'L', 'Kélé', false, '');
INSERT INTO languages_iso639 VALUES ('kec', '   ', '   ', '  ', 'I', 'L', 'Keiga', false, '');
INSERT INTO languages_iso639 VALUES ('ked', '   ', '   ', '  ', 'I', 'L', 'Kerewe', false, '');
INSERT INTO languages_iso639 VALUES ('kee', '   ', '   ', '  ', 'I', 'L', 'Eastern Keres', false, '');
INSERT INTO languages_iso639 VALUES ('kef', '   ', '   ', '  ', 'I', 'L', 'Kpessi', false, '');
INSERT INTO languages_iso639 VALUES ('keg', '   ', '   ', '  ', 'I', 'L', 'Tese', false, '');
INSERT INTO languages_iso639 VALUES ('keh', '   ', '   ', '  ', 'I', 'L', 'Keak', false, '');
INSERT INTO languages_iso639 VALUES ('kei', '   ', '   ', '  ', 'I', 'L', 'Kei', false, '');
INSERT INTO languages_iso639 VALUES ('kej', '   ', '   ', '  ', 'I', 'L', 'Kadar', false, '');
INSERT INTO languages_iso639 VALUES ('kek', '   ', '   ', '  ', 'I', 'L', 'Kekchí', false, '');
INSERT INTO languages_iso639 VALUES ('kel', '   ', '   ', '  ', 'I', 'L', 'Kela (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('kem', '   ', '   ', '  ', 'I', 'L', 'Kemak', false, '');
INSERT INTO languages_iso639 VALUES ('ken', '   ', '   ', '  ', 'I', 'L', 'Kenyang', false, '');
INSERT INTO languages_iso639 VALUES ('keo', '   ', '   ', '  ', 'I', 'L', 'Kakwa', false, '');
INSERT INTO languages_iso639 VALUES ('kep', '   ', '   ', '  ', 'I', 'L', 'Kaikadi', false, '');
INSERT INTO languages_iso639 VALUES ('keq', '   ', '   ', '  ', 'I', 'L', 'Kamar', false, '');
INSERT INTO languages_iso639 VALUES ('ker', '   ', '   ', '  ', 'I', 'L', 'Kera', false, '');
INSERT INTO languages_iso639 VALUES ('kes', '   ', '   ', '  ', 'I', 'L', 'Kugbo', false, '');
INSERT INTO languages_iso639 VALUES ('ket', '   ', '   ', '  ', 'I', 'L', 'Ket', false, '');
INSERT INTO languages_iso639 VALUES ('keu', '   ', '   ', '  ', 'I', 'L', 'Akebu', false, '');
INSERT INTO languages_iso639 VALUES ('kev', '   ', '   ', '  ', 'I', 'L', 'Kanikkaran', false, '');
INSERT INTO languages_iso639 VALUES ('kew', '   ', '   ', '  ', 'I', 'L', 'West Kewa', false, '');
INSERT INTO languages_iso639 VALUES ('kex', '   ', '   ', '  ', 'I', 'L', 'Kukna', false, '');
INSERT INTO languages_iso639 VALUES ('key', '   ', '   ', '  ', 'I', 'L', 'Kupia', false, '');
INSERT INTO languages_iso639 VALUES ('kez', '   ', '   ', '  ', 'I', 'L', 'Kukele', false, '');
INSERT INTO languages_iso639 VALUES ('kfa', '   ', '   ', '  ', 'I', 'L', 'Kodava', false, '');
INSERT INTO languages_iso639 VALUES ('kfb', '   ', '   ', '  ', 'I', 'L', 'Northwestern Kolami', false, '');
INSERT INTO languages_iso639 VALUES ('kfc', '   ', '   ', '  ', 'I', 'L', 'Konda-Dora', false, '');
INSERT INTO languages_iso639 VALUES ('kfd', '   ', '   ', '  ', 'I', 'L', 'Korra Koraga', false, '');
INSERT INTO languages_iso639 VALUES ('kfe', '   ', '   ', '  ', 'I', 'L', 'Kota (India)', false, '');
INSERT INTO languages_iso639 VALUES ('kff', '   ', '   ', '  ', 'I', 'L', 'Koya', false, '');
INSERT INTO languages_iso639 VALUES ('kfg', '   ', '   ', '  ', 'I', 'L', 'Kudiya', false, '');
INSERT INTO languages_iso639 VALUES ('kfh', '   ', '   ', '  ', 'I', 'L', 'Kurichiya', false, '');
INSERT INTO languages_iso639 VALUES ('kfi', '   ', '   ', '  ', 'I', 'L', 'Kannada Kurumba', false, '');
INSERT INTO languages_iso639 VALUES ('kfj', '   ', '   ', '  ', 'I', 'L', 'Kemiehua', false, '');
INSERT INTO languages_iso639 VALUES ('kfk', '   ', '   ', '  ', 'I', 'L', 'Kinnauri', false, '');
INSERT INTO languages_iso639 VALUES ('kfl', '   ', '   ', '  ', 'I', 'L', 'Kung', false, '');
INSERT INTO languages_iso639 VALUES ('kfm', '   ', '   ', '  ', 'I', 'L', 'Khunsari', false, '');
INSERT INTO languages_iso639 VALUES ('kfn', '   ', '   ', '  ', 'I', 'L', 'Kuk', false, '');
INSERT INTO languages_iso639 VALUES ('kfo', '   ', '   ', '  ', 'I', 'L', 'Koro (Côte d''Ivoire)', false, '');
INSERT INTO languages_iso639 VALUES ('kfp', '   ', '   ', '  ', 'I', 'L', 'Korwa', false, '');
INSERT INTO languages_iso639 VALUES ('kfq', '   ', '   ', '  ', 'I', 'L', 'Korku', false, '');
INSERT INTO languages_iso639 VALUES ('kfr', '   ', '   ', '  ', 'I', 'L', 'Kachchi', false, '');
INSERT INTO languages_iso639 VALUES ('kfs', '   ', '   ', '  ', 'I', 'L', 'Bilaspuri', false, '');
INSERT INTO languages_iso639 VALUES ('kft', '   ', '   ', '  ', 'I', 'L', 'Kanjari', false, '');
INSERT INTO languages_iso639 VALUES ('kfu', '   ', '   ', '  ', 'I', 'L', 'Katkari', false, '');
INSERT INTO languages_iso639 VALUES ('kfv', '   ', '   ', '  ', 'I', 'L', 'Kurmukar', false, '');
INSERT INTO languages_iso639 VALUES ('kfw', '   ', '   ', '  ', 'I', 'L', 'Kharam Naga', false, '');
INSERT INTO languages_iso639 VALUES ('kfx', '   ', '   ', '  ', 'I', 'L', 'Kullu Pahari', false, '');
INSERT INTO languages_iso639 VALUES ('kfy', '   ', '   ', '  ', 'I', 'L', 'Kumaoni', false, '');
INSERT INTO languages_iso639 VALUES ('kfz', '   ', '   ', '  ', 'I', 'L', 'Koromfé', false, '');
INSERT INTO languages_iso639 VALUES ('kga', '   ', '   ', '  ', 'I', 'L', 'Koyaga', false, '');
INSERT INTO languages_iso639 VALUES ('kgb', '   ', '   ', '  ', 'I', 'L', 'Kawe', false, '');
INSERT INTO languages_iso639 VALUES ('kgc', '   ', '   ', '  ', 'I', 'L', 'Kasseng', false, '');
INSERT INTO languages_iso639 VALUES ('kgd', '   ', '   ', '  ', 'I', 'L', 'Kataang', false, '');
INSERT INTO languages_iso639 VALUES ('kge', '   ', '   ', '  ', 'I', 'L', 'Komering', false, '');
INSERT INTO languages_iso639 VALUES ('kgf', '   ', '   ', '  ', 'I', 'L', 'Kube', false, '');
INSERT INTO languages_iso639 VALUES ('kgg', '   ', '   ', '  ', 'I', 'L', 'Kusunda', false, '');
INSERT INTO languages_iso639 VALUES ('kgi', '   ', '   ', '  ', 'I', 'L', 'Selangor Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('kgj', '   ', '   ', '  ', 'I', 'L', 'Gamale Kham', false, '');
INSERT INTO languages_iso639 VALUES ('kgk', '   ', '   ', '  ', 'I', 'L', 'Kaiwá', false, '');
INSERT INTO languages_iso639 VALUES ('kgl', '   ', '   ', '  ', 'I', 'E', 'Kunggari', false, '');
INSERT INTO languages_iso639 VALUES ('kgm', '   ', '   ', '  ', 'I', 'E', 'Karipúna', false, '');
INSERT INTO languages_iso639 VALUES ('kgn', '   ', '   ', '  ', 'I', 'L', 'Karingani', false, '');
INSERT INTO languages_iso639 VALUES ('kgo', '   ', '   ', '  ', 'I', 'L', 'Krongo', false, '');
INSERT INTO languages_iso639 VALUES ('kgp', '   ', '   ', '  ', 'I', 'L', 'Kaingang', false, '');
INSERT INTO languages_iso639 VALUES ('kgq', '   ', '   ', '  ', 'I', 'L', 'Kamoro', false, '');
INSERT INTO languages_iso639 VALUES ('kgr', '   ', '   ', '  ', 'I', 'L', 'Abun', false, '');
INSERT INTO languages_iso639 VALUES ('kgs', '   ', '   ', '  ', 'I', 'L', 'Kumbainggar', false, '');
INSERT INTO languages_iso639 VALUES ('kgt', '   ', '   ', '  ', 'I', 'L', 'Somyev', false, '');
INSERT INTO languages_iso639 VALUES ('kgu', '   ', '   ', '  ', 'I', 'L', 'Kobol', false, '');
INSERT INTO languages_iso639 VALUES ('kgv', '   ', '   ', '  ', 'I', 'L', 'Karas', false, '');
INSERT INTO languages_iso639 VALUES ('kgw', '   ', '   ', '  ', 'I', 'L', 'Karon Dori', false, '');
INSERT INTO languages_iso639 VALUES ('kgx', '   ', '   ', '  ', 'I', 'L', 'Kamaru', false, '');
INSERT INTO languages_iso639 VALUES ('kgy', '   ', '   ', '  ', 'I', 'L', 'Kyerung', false, '');
INSERT INTO languages_iso639 VALUES ('kha', 'kha', 'kha', '  ', 'I', 'L', 'Khasi', false, '');
INSERT INTO languages_iso639 VALUES ('khb', '   ', '   ', '  ', 'I', 'L', 'Lü', false, '');
INSERT INTO languages_iso639 VALUES ('khc', '   ', '   ', '  ', 'I', 'L', 'Tukang Besi North', false, '');
INSERT INTO languages_iso639 VALUES ('khd', '   ', '   ', '  ', 'I', 'L', 'Bädi Kanum', false, '');
INSERT INTO languages_iso639 VALUES ('khe', '   ', '   ', '  ', 'I', 'L', 'Korowai', false, '');
INSERT INTO languages_iso639 VALUES ('khf', '   ', '   ', '  ', 'I', 'L', 'Khuen', false, '');
INSERT INTO languages_iso639 VALUES ('khg', '   ', '   ', '  ', 'I', 'L', 'Khams Tibetan', false, '');
INSERT INTO languages_iso639 VALUES ('khh', '   ', '   ', '  ', 'I', 'L', 'Kehu', false, '');
INSERT INTO languages_iso639 VALUES ('khj', '   ', '   ', '  ', 'I', 'L', 'Kuturmi', false, '');
INSERT INTO languages_iso639 VALUES ('khk', '   ', '   ', '  ', 'I', 'L', 'Halh Mongolian', false, '');
INSERT INTO languages_iso639 VALUES ('khl', '   ', '   ', '  ', 'I', 'L', 'Lusi', false, '');
INSERT INTO languages_iso639 VALUES ('khm', 'khm', 'khm', 'km', 'I', 'L', 'Central Khmer', false, '');
INSERT INTO languages_iso639 VALUES ('khn', '   ', '   ', '  ', 'I', 'L', 'Khandesi', false, '');
INSERT INTO languages_iso639 VALUES ('kho', 'kho', 'kho', '  ', 'I', 'A', 'Khotanese', false, '');
INSERT INTO languages_iso639 VALUES ('khp', '   ', '   ', '  ', 'I', 'L', 'Kapori', false, '');
INSERT INTO languages_iso639 VALUES ('khq', '   ', '   ', '  ', 'I', 'L', 'Koyra Chiini Songhay', false, '');
INSERT INTO languages_iso639 VALUES ('khr', '   ', '   ', '  ', 'I', 'L', 'Kharia', false, '');
INSERT INTO languages_iso639 VALUES ('khs', '   ', '   ', '  ', 'I', 'L', 'Kasua', false, '');
INSERT INTO languages_iso639 VALUES ('kht', '   ', '   ', '  ', 'I', 'L', 'Khamti', false, '');
INSERT INTO languages_iso639 VALUES ('khu', '   ', '   ', '  ', 'I', 'L', 'Nkhumbi', false, '');
INSERT INTO languages_iso639 VALUES ('khv', '   ', '   ', '  ', 'I', 'L', 'Khvarshi', false, '');
INSERT INTO languages_iso639 VALUES ('khw', '   ', '   ', '  ', 'I', 'L', 'Khowar', false, '');
INSERT INTO languages_iso639 VALUES ('khx', '   ', '   ', '  ', 'I', 'L', 'Kanu', false, '');
INSERT INTO languages_iso639 VALUES ('khy', '   ', '   ', '  ', 'I', 'L', 'Kele (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('khz', '   ', '   ', '  ', 'I', 'L', 'Keapara', false, '');
INSERT INTO languages_iso639 VALUES ('kia', '   ', '   ', '  ', 'I', 'L', 'Kim', false, '');
INSERT INTO languages_iso639 VALUES ('kib', '   ', '   ', '  ', 'I', 'L', 'Koalib', false, '');
INSERT INTO languages_iso639 VALUES ('kic', '   ', '   ', '  ', 'I', 'L', 'Kickapoo', false, '');
INSERT INTO languages_iso639 VALUES ('kid', '   ', '   ', '  ', 'I', 'L', 'Koshin', false, '');
INSERT INTO languages_iso639 VALUES ('kie', '   ', '   ', '  ', 'I', 'L', 'Kibet', false, '');
INSERT INTO languages_iso639 VALUES ('kif', '   ', '   ', '  ', 'I', 'L', 'Eastern Parbate Kham', false, '');
INSERT INTO languages_iso639 VALUES ('kig', '   ', '   ', '  ', 'I', 'L', 'Kimaama', false, '');
INSERT INTO languages_iso639 VALUES ('kih', '   ', '   ', '  ', 'I', 'L', 'Kilmeri', false, '');
INSERT INTO languages_iso639 VALUES ('kii', '   ', '   ', '  ', 'I', 'E', 'Kitsai', false, '');
INSERT INTO languages_iso639 VALUES ('kij', '   ', '   ', '  ', 'I', 'L', 'Kilivila', false, '');
INSERT INTO languages_iso639 VALUES ('kik', 'kik', 'kik', 'ki', 'I', 'L', 'Kikuyu', false, '');
INSERT INTO languages_iso639 VALUES ('kil', '   ', '   ', '  ', 'I', 'L', 'Kariya', false, '');
INSERT INTO languages_iso639 VALUES ('kim', '   ', '   ', '  ', 'I', 'L', 'Karagas', false, '');
INSERT INTO languages_iso639 VALUES ('kin', 'kin', 'kin', 'rw', 'I', 'L', 'Kinyarwanda', false, '');
INSERT INTO languages_iso639 VALUES ('kio', '   ', '   ', '  ', 'I', 'L', 'Kiowa', false, '');
INSERT INTO languages_iso639 VALUES ('kip', '   ', '   ', '  ', 'I', 'L', 'Sheshi Kham', false, '');
INSERT INTO languages_iso639 VALUES ('kiq', '   ', '   ', '  ', 'I', 'L', 'Kosadle', false, '');
INSERT INTO languages_iso639 VALUES ('kir', 'kir', 'kir', 'ky', 'I', 'L', 'Kirghiz', false, '');
INSERT INTO languages_iso639 VALUES ('kis', '   ', '   ', '  ', 'I', 'L', 'Kis', false, '');
INSERT INTO languages_iso639 VALUES ('kit', '   ', '   ', '  ', 'I', 'L', 'Agob', false, '');
INSERT INTO languages_iso639 VALUES ('kiu', '   ', '   ', '  ', 'I', 'L', 'Kirmanjki (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('kiv', '   ', '   ', '  ', 'I', 'L', 'Kimbu', false, '');
INSERT INTO languages_iso639 VALUES ('kiw', '   ', '   ', '  ', 'I', 'L', 'Northeast Kiwai', false, '');
INSERT INTO languages_iso639 VALUES ('kix', '   ', '   ', '  ', 'I', 'L', 'Khiamniungan Naga', false, '');
INSERT INTO languages_iso639 VALUES ('kiy', '   ', '   ', '  ', 'I', 'L', 'Kirikiri', false, '');
INSERT INTO languages_iso639 VALUES ('kiz', '   ', '   ', '  ', 'I', 'L', 'Kisi', false, '');
INSERT INTO languages_iso639 VALUES ('kja', '   ', '   ', '  ', 'I', 'L', 'Mlap', false, '');
INSERT INTO languages_iso639 VALUES ('kjb', '   ', '   ', '  ', 'I', 'L', 'Q''anjob''al', false, '');
INSERT INTO languages_iso639 VALUES ('kjc', '   ', '   ', '  ', 'I', 'L', 'Coastal Konjo', false, '');
INSERT INTO languages_iso639 VALUES ('kjd', '   ', '   ', '  ', 'I', 'L', 'Southern Kiwai', false, '');
INSERT INTO languages_iso639 VALUES ('kje', '   ', '   ', '  ', 'I', 'L', 'Kisar', false, '');
INSERT INTO languages_iso639 VALUES ('kjf', '   ', '   ', '  ', 'I', 'L', 'Khalaj', false, '');
INSERT INTO languages_iso639 VALUES ('kjg', '   ', '   ', '  ', 'I', 'L', 'Khmu', false, '');
INSERT INTO languages_iso639 VALUES ('kjh', '   ', '   ', '  ', 'I', 'L', 'Khakas', false, '');
INSERT INTO languages_iso639 VALUES ('kji', '   ', '   ', '  ', 'I', 'L', 'Zabana', false, '');
INSERT INTO languages_iso639 VALUES ('kjj', '   ', '   ', '  ', 'I', 'L', 'Khinalugh', false, '');
INSERT INTO languages_iso639 VALUES ('kjk', '   ', '   ', '  ', 'I', 'L', 'Highland Konjo', false, '');
INSERT INTO languages_iso639 VALUES ('kjl', '   ', '   ', '  ', 'I', 'L', 'Western Parbate Kham', false, '');
INSERT INTO languages_iso639 VALUES ('kjm', '   ', '   ', '  ', 'I', 'L', 'Kháng', false, '');
INSERT INTO languages_iso639 VALUES ('kjn', '   ', '   ', '  ', 'I', 'L', 'Kunjen', false, '');
INSERT INTO languages_iso639 VALUES ('kjo', '   ', '   ', '  ', 'I', 'L', 'Harijan Kinnauri', false, '');
INSERT INTO languages_iso639 VALUES ('kjp', '   ', '   ', '  ', 'I', 'L', 'Pwo Eastern Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kjq', '   ', '   ', '  ', 'I', 'L', 'Western Keres', false, '');
INSERT INTO languages_iso639 VALUES ('kjr', '   ', '   ', '  ', 'I', 'L', 'Kurudu', false, '');
INSERT INTO languages_iso639 VALUES ('kjs', '   ', '   ', '  ', 'I', 'L', 'East Kewa', false, '');
INSERT INTO languages_iso639 VALUES ('kjt', '   ', '   ', '  ', 'I', 'L', 'Phrae Pwo Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kju', '   ', '   ', '  ', 'I', 'L', 'Kashaya', false, '');
INSERT INTO languages_iso639 VALUES ('kjx', '   ', '   ', '  ', 'I', 'L', 'Ramopa', false, '');
INSERT INTO languages_iso639 VALUES ('kjy', '   ', '   ', '  ', 'I', 'L', 'Erave', false, '');
INSERT INTO languages_iso639 VALUES ('kjz', '   ', '   ', '  ', 'I', 'L', 'Bumthangkha', false, '');
INSERT INTO languages_iso639 VALUES ('kka', '   ', '   ', '  ', 'I', 'L', 'Kakanda', false, '');
INSERT INTO languages_iso639 VALUES ('kkb', '   ', '   ', '  ', 'I', 'L', 'Kwerisa', false, '');
INSERT INTO languages_iso639 VALUES ('kkc', '   ', '   ', '  ', 'I', 'L', 'Odoodee', false, '');
INSERT INTO languages_iso639 VALUES ('kkd', '   ', '   ', '  ', 'I', 'L', 'Kinuku', false, '');
INSERT INTO languages_iso639 VALUES ('kke', '   ', '   ', '  ', 'I', 'L', 'Kakabe', false, '');
INSERT INTO languages_iso639 VALUES ('kkf', '   ', '   ', '  ', 'I', 'L', 'Kalaktang Monpa', false, '');
INSERT INTO languages_iso639 VALUES ('kkg', '   ', '   ', '  ', 'I', 'L', 'Mabaka Valley Kalinga', false, '');
INSERT INTO languages_iso639 VALUES ('kkh', '   ', '   ', '  ', 'I', 'L', 'Khün', false, '');
INSERT INTO languages_iso639 VALUES ('kki', '   ', '   ', '  ', 'I', 'L', 'Kagulu', false, '');
INSERT INTO languages_iso639 VALUES ('kkj', '   ', '   ', '  ', 'I', 'L', 'Kako', false, '');
INSERT INTO languages_iso639 VALUES ('kkk', '   ', '   ', '  ', 'I', 'L', 'Kokota', false, '');
INSERT INTO languages_iso639 VALUES ('kkl', '   ', '   ', '  ', 'I', 'L', 'Kosarek Yale', false, '');
INSERT INTO languages_iso639 VALUES ('kkm', '   ', '   ', '  ', 'I', 'L', 'Kiong', false, '');
INSERT INTO languages_iso639 VALUES ('kkn', '   ', '   ', '  ', 'I', 'L', 'Kon Keu', false, '');
INSERT INTO languages_iso639 VALUES ('kko', '   ', '   ', '  ', 'I', 'L', 'Karko', false, '');
INSERT INTO languages_iso639 VALUES ('kkp', '   ', '   ', '  ', 'I', 'L', 'Gugubera', false, '');
INSERT INTO languages_iso639 VALUES ('kkq', '   ', '   ', '  ', 'I', 'L', 'Kaiku', false, '');
INSERT INTO languages_iso639 VALUES ('kkr', '   ', '   ', '  ', 'I', 'L', 'Kir-Balar', false, '');
INSERT INTO languages_iso639 VALUES ('kks', '   ', '   ', '  ', 'I', 'L', 'Giiwo', false, '');
INSERT INTO languages_iso639 VALUES ('kkt', '   ', '   ', '  ', 'I', 'L', 'Koi', false, '');
INSERT INTO languages_iso639 VALUES ('kku', '   ', '   ', '  ', 'I', 'L', 'Tumi', false, '');
INSERT INTO languages_iso639 VALUES ('kkv', '   ', '   ', '  ', 'I', 'L', 'Kangean', false, '');
INSERT INTO languages_iso639 VALUES ('kkw', '   ', '   ', '  ', 'I', 'L', 'Teke-Kukuya', false, '');
INSERT INTO languages_iso639 VALUES ('kkx', '   ', '   ', '  ', 'I', 'L', 'Kohin', false, '');
INSERT INTO languages_iso639 VALUES ('kky', '   ', '   ', '  ', 'I', 'L', 'Guguyimidjir', false, '');
INSERT INTO languages_iso639 VALUES ('kkz', '   ', '   ', '  ', 'I', 'L', 'Kaska', false, '');
INSERT INTO languages_iso639 VALUES ('kla', '   ', '   ', '  ', 'I', 'E', 'Klamath-Modoc', false, '');
INSERT INTO languages_iso639 VALUES ('klb', '   ', '   ', '  ', 'I', 'L', 'Kiliwa', false, '');
INSERT INTO languages_iso639 VALUES ('klc', '   ', '   ', '  ', 'I', 'L', 'Kolbila', false, '');
INSERT INTO languages_iso639 VALUES ('kld', '   ', '   ', '  ', 'I', 'L', 'Gamilaraay', false, '');
INSERT INTO languages_iso639 VALUES ('kle', '   ', '   ', '  ', 'I', 'L', 'Kulung (Nepal)', false, '');
INSERT INTO languages_iso639 VALUES ('klf', '   ', '   ', '  ', 'I', 'L', 'Kendeje', false, '');
INSERT INTO languages_iso639 VALUES ('klg', '   ', '   ', '  ', 'I', 'L', 'Tagakaulo', false, '');
INSERT INTO languages_iso639 VALUES ('klh', '   ', '   ', '  ', 'I', 'L', 'Weliki', false, '');
INSERT INTO languages_iso639 VALUES ('kli', '   ', '   ', '  ', 'I', 'L', 'Kalumpang', false, '');
INSERT INTO languages_iso639 VALUES ('klj', '   ', '   ', '  ', 'I', 'L', 'Turkic Khalaj', false, '');
INSERT INTO languages_iso639 VALUES ('klk', '   ', '   ', '  ', 'I', 'L', 'Kono (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('kll', '   ', '   ', '  ', 'I', 'L', 'Kagan Kalagan', false, '');
INSERT INTO languages_iso639 VALUES ('klm', '   ', '   ', '  ', 'I', 'L', 'Migum', false, '');
INSERT INTO languages_iso639 VALUES ('kln', '   ', '   ', '  ', 'M', 'L', 'Kalenjin', false, '');
INSERT INTO languages_iso639 VALUES ('klo', '   ', '   ', '  ', 'I', 'L', 'Kapya', false, '');
INSERT INTO languages_iso639 VALUES ('klp', '   ', '   ', '  ', 'I', 'L', 'Kamasa', false, '');
INSERT INTO languages_iso639 VALUES ('klq', '   ', '   ', '  ', 'I', 'L', 'Rumu', false, '');
INSERT INTO languages_iso639 VALUES ('klr', '   ', '   ', '  ', 'I', 'L', 'Khaling', false, '');
INSERT INTO languages_iso639 VALUES ('kls', '   ', '   ', '  ', 'I', 'L', 'Kalasha', false, '');
INSERT INTO languages_iso639 VALUES ('klt', '   ', '   ', '  ', 'I', 'L', 'Nukna', false, '');
INSERT INTO languages_iso639 VALUES ('klu', '   ', '   ', '  ', 'I', 'L', 'Klao', false, '');
INSERT INTO languages_iso639 VALUES ('klv', '   ', '   ', '  ', 'I', 'L', 'Maskelynes', false, '');
INSERT INTO languages_iso639 VALUES ('klw', '   ', '   ', '  ', 'I', 'L', 'Lindu', false, '');
INSERT INTO languages_iso639 VALUES ('klx', '   ', '   ', '  ', 'I', 'L', 'Koluwawa', false, '');
INSERT INTO languages_iso639 VALUES ('kly', '   ', '   ', '  ', 'I', 'L', 'Kalao', false, '');
INSERT INTO languages_iso639 VALUES ('klz', '   ', '   ', '  ', 'I', 'L', 'Kabola', false, '');
INSERT INTO languages_iso639 VALUES ('kma', '   ', '   ', '  ', 'I', 'L', 'Konni', false, '');
INSERT INTO languages_iso639 VALUES ('kmb', 'kmb', 'kmb', '  ', 'I', 'L', 'Kimbundu', false, '');
INSERT INTO languages_iso639 VALUES ('kmc', '   ', '   ', '  ', 'I', 'L', 'Southern Dong', false, '');
INSERT INTO languages_iso639 VALUES ('kmd', '   ', '   ', '  ', 'I', 'L', 'Majukayang Kalinga', false, '');
INSERT INTO languages_iso639 VALUES ('kme', '   ', '   ', '  ', 'I', 'L', 'Bakole', false, '');
INSERT INTO languages_iso639 VALUES ('kmf', '   ', '   ', '  ', 'I', 'L', 'Kare (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('kmg', '   ', '   ', '  ', 'I', 'L', 'Kâte', false, '');
INSERT INTO languages_iso639 VALUES ('kmh', '   ', '   ', '  ', 'I', 'L', 'Kalam', false, '');
INSERT INTO languages_iso639 VALUES ('kmi', '   ', '   ', '  ', 'I', 'L', 'Kami (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('kmj', '   ', '   ', '  ', 'I', 'L', 'Kumarbhag Paharia', false, '');
INSERT INTO languages_iso639 VALUES ('kmk', '   ', '   ', '  ', 'I', 'L', 'Limos Kalinga', false, '');
INSERT INTO languages_iso639 VALUES ('kml', '   ', '   ', '  ', 'I', 'L', 'Tanudan Kalinga', false, '');
INSERT INTO languages_iso639 VALUES ('kmm', '   ', '   ', '  ', 'I', 'L', 'Kom (India)', false, '');
INSERT INTO languages_iso639 VALUES ('kmn', '   ', '   ', '  ', 'I', 'L', 'Awtuw', false, '');
INSERT INTO languages_iso639 VALUES ('kmo', '   ', '   ', '  ', 'I', 'L', 'Kwoma', false, '');
INSERT INTO languages_iso639 VALUES ('kmp', '   ', '   ', '  ', 'I', 'L', 'Gimme', false, '');
INSERT INTO languages_iso639 VALUES ('kmq', '   ', '   ', '  ', 'I', 'L', 'Kwama', false, '');
INSERT INTO languages_iso639 VALUES ('kmr', '   ', '   ', '  ', 'I', 'L', 'Northern Kurdish', false, '');
INSERT INTO languages_iso639 VALUES ('kms', '   ', '   ', '  ', 'I', 'L', 'Kamasau', false, '');
INSERT INTO languages_iso639 VALUES ('kmt', '   ', '   ', '  ', 'I', 'L', 'Kemtuik', false, '');
INSERT INTO languages_iso639 VALUES ('kmu', '   ', '   ', '  ', 'I', 'L', 'Kanite', false, '');
INSERT INTO languages_iso639 VALUES ('kmv', '   ', '   ', '  ', 'I', 'L', 'Karipúna Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('kmw', '   ', '   ', '  ', 'I', 'L', 'Komo (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('kmx', '   ', '   ', '  ', 'I', 'L', 'Waboda', false, '');
INSERT INTO languages_iso639 VALUES ('kmy', '   ', '   ', '  ', 'I', 'L', 'Koma', false, '');
INSERT INTO languages_iso639 VALUES ('kmz', '   ', '   ', '  ', 'I', 'L', 'Khorasani Turkish', false, '');
INSERT INTO languages_iso639 VALUES ('kna', '   ', '   ', '  ', 'I', 'L', 'Dera (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('knb', '   ', '   ', '  ', 'I', 'L', 'Lubuagan Kalinga', false, '');
INSERT INTO languages_iso639 VALUES ('knc', '   ', '   ', '  ', 'I', 'L', 'Central Kanuri', false, '');
INSERT INTO languages_iso639 VALUES ('knd', '   ', '   ', '  ', 'I', 'L', 'Konda', false, '');
INSERT INTO languages_iso639 VALUES ('kne', '   ', '   ', '  ', 'I', 'L', 'Kankanaey', false, '');
INSERT INTO languages_iso639 VALUES ('knf', '   ', '   ', '  ', 'I', 'L', 'Mankanya', false, '');
INSERT INTO languages_iso639 VALUES ('kng', '   ', '   ', '  ', 'I', 'L', 'Koongo', false, '');
INSERT INTO languages_iso639 VALUES ('kni', '   ', '   ', '  ', 'I', 'L', 'Kanufi', false, '');
INSERT INTO languages_iso639 VALUES ('knj', '   ', '   ', '  ', 'I', 'L', 'Western Kanjobal', false, '');
INSERT INTO languages_iso639 VALUES ('knk', '   ', '   ', '  ', 'I', 'L', 'Kuranko', false, '');
INSERT INTO languages_iso639 VALUES ('knl', '   ', '   ', '  ', 'I', 'L', 'Keninjal', false, '');
INSERT INTO languages_iso639 VALUES ('knm', '   ', '   ', '  ', 'I', 'L', 'Kanamarí', false, '');
INSERT INTO languages_iso639 VALUES ('knn', '   ', '   ', '  ', 'I', 'L', 'Konkani (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('kno', '   ', '   ', '  ', 'I', 'L', 'Kono (Sierra Leone)', false, '');
INSERT INTO languages_iso639 VALUES ('knp', '   ', '   ', '  ', 'I', 'L', 'Kwanja', false, '');
INSERT INTO languages_iso639 VALUES ('knq', '   ', '   ', '  ', 'I', 'L', 'Kintaq', false, '');
INSERT INTO languages_iso639 VALUES ('knr', '   ', '   ', '  ', 'I', 'L', 'Kaningra', false, '');
INSERT INTO languages_iso639 VALUES ('kns', '   ', '   ', '  ', 'I', 'L', 'Kensiu', false, '');
INSERT INTO languages_iso639 VALUES ('knt', '   ', '   ', '  ', 'I', 'L', 'Panoan Katukína', false, '');
INSERT INTO languages_iso639 VALUES ('knu', '   ', '   ', '  ', 'I', 'L', 'Kono (Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('knv', '   ', '   ', '  ', 'I', 'L', 'Tabo', false, '');
INSERT INTO languages_iso639 VALUES ('knw', '   ', '   ', '  ', 'I', 'L', 'Kung-Ekoka', false, '');
INSERT INTO languages_iso639 VALUES ('knx', '   ', '   ', '  ', 'I', 'L', 'Kendayan', false, '');
INSERT INTO languages_iso639 VALUES ('kny', '   ', '   ', '  ', 'I', 'L', 'Kanyok', false, '');
INSERT INTO languages_iso639 VALUES ('knz', '   ', '   ', '  ', 'I', 'L', 'Kalamsé', false, '');
INSERT INTO languages_iso639 VALUES ('koa', '   ', '   ', '  ', 'I', 'L', 'Konomala', false, '');
INSERT INTO languages_iso639 VALUES ('koc', '   ', '   ', '  ', 'I', 'E', 'Kpati', false, '');
INSERT INTO languages_iso639 VALUES ('kod', '   ', '   ', '  ', 'I', 'L', 'Kodi', false, '');
INSERT INTO languages_iso639 VALUES ('koe', '   ', '   ', '  ', 'I', 'L', 'Kacipo-Balesi', false, '');
INSERT INTO languages_iso639 VALUES ('kof', '   ', '   ', '  ', 'I', 'E', 'Kubi', false, '');
INSERT INTO languages_iso639 VALUES ('kog', '   ', '   ', '  ', 'I', 'L', 'Cogui', false, '');
INSERT INTO languages_iso639 VALUES ('koh', '   ', '   ', '  ', 'I', 'L', 'Koyo', false, '');
INSERT INTO languages_iso639 VALUES ('koi', '   ', '   ', '  ', 'I', 'L', 'Komi-Permyak', false, '');
INSERT INTO languages_iso639 VALUES ('koj', '   ', '   ', '  ', 'I', 'L', 'Sara Dunjo', false, '');
INSERT INTO languages_iso639 VALUES ('kok', 'kok', 'kok', '  ', 'M', 'L', 'Konkani (macrolanguage)', false, '');
INSERT INTO languages_iso639 VALUES ('kol', '   ', '   ', '  ', 'I', 'L', 'Kol (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('kom', 'kom', 'kom', 'kv', 'M', 'L', 'Komi', false, '');
INSERT INTO languages_iso639 VALUES ('kon', 'kon', 'kon', 'kg', 'M', 'L', 'Kongo', false, '');
INSERT INTO languages_iso639 VALUES ('koo', '   ', '   ', '  ', 'I', 'L', 'Konzo', false, '');
INSERT INTO languages_iso639 VALUES ('kop', '   ', '   ', '  ', 'I', 'L', 'Waube', false, '');
INSERT INTO languages_iso639 VALUES ('koq', '   ', '   ', '  ', 'I', 'L', 'Kota (Gabon)', false, '');
INSERT INTO languages_iso639 VALUES ('kor', 'kor', 'kor', 'ko', 'I', 'L', 'Korean', false, '');
INSERT INTO languages_iso639 VALUES ('kos', 'kos', 'kos', '  ', 'I', 'L', 'Kosraean', false, '');
INSERT INTO languages_iso639 VALUES ('kot', '   ', '   ', '  ', 'I', 'L', 'Lagwan', false, '');
INSERT INTO languages_iso639 VALUES ('kou', '   ', '   ', '  ', 'I', 'L', 'Koke', false, '');
INSERT INTO languages_iso639 VALUES ('kov', '   ', '   ', '  ', 'I', 'L', 'Kudu-Camo', false, '');
INSERT INTO languages_iso639 VALUES ('kow', '   ', '   ', '  ', 'I', 'L', 'Kugama', false, '');
INSERT INTO languages_iso639 VALUES ('kox', '   ', '   ', '  ', 'I', 'E', 'Coxima', false, '');
INSERT INTO languages_iso639 VALUES ('koy', '   ', '   ', '  ', 'I', 'L', 'Koyukon', false, '');
INSERT INTO languages_iso639 VALUES ('koz', '   ', '   ', '  ', 'I', 'L', 'Korak', false, '');
INSERT INTO languages_iso639 VALUES ('kpa', '   ', '   ', '  ', 'I', 'L', 'Kutto', false, '');
INSERT INTO languages_iso639 VALUES ('kpb', '   ', '   ', '  ', 'I', 'L', 'Mullu Kurumba', false, '');
INSERT INTO languages_iso639 VALUES ('kpc', '   ', '   ', '  ', 'I', 'L', 'Curripaco', false, '');
INSERT INTO languages_iso639 VALUES ('kpd', '   ', '   ', '  ', 'I', 'L', 'Koba', false, '');
INSERT INTO languages_iso639 VALUES ('kpe', 'kpe', 'kpe', '  ', 'M', 'L', 'Kpelle', false, '');
INSERT INTO languages_iso639 VALUES ('kpf', '   ', '   ', '  ', 'I', 'L', 'Komba', false, '');
INSERT INTO languages_iso639 VALUES ('kpg', '   ', '   ', '  ', 'I', 'L', 'Kapingamarangi', false, '');
INSERT INTO languages_iso639 VALUES ('kph', '   ', '   ', '  ', 'I', 'L', 'Kplang', false, '');
INSERT INTO languages_iso639 VALUES ('kpi', '   ', '   ', '  ', 'I', 'L', 'Kofei', false, '');
INSERT INTO languages_iso639 VALUES ('kpj', '   ', '   ', '  ', 'I', 'L', 'Karajá', false, '');
INSERT INTO languages_iso639 VALUES ('kpk', '   ', '   ', '  ', 'I', 'L', 'Kpan', false, '');
INSERT INTO languages_iso639 VALUES ('kpl', '   ', '   ', '  ', 'I', 'L', 'Kpala', false, '');
INSERT INTO languages_iso639 VALUES ('kpm', '   ', '   ', '  ', 'I', 'L', 'Koho', false, '');
INSERT INTO languages_iso639 VALUES ('kpn', '   ', '   ', '  ', 'I', 'E', 'Kepkiriwát', false, '');
INSERT INTO languages_iso639 VALUES ('kpo', '   ', '   ', '  ', 'I', 'L', 'Ikposo', false, '');
INSERT INTO languages_iso639 VALUES ('kpq', '   ', '   ', '  ', 'I', 'L', 'Korupun-Sela', false, '');
INSERT INTO languages_iso639 VALUES ('kpr', '   ', '   ', '  ', 'I', 'L', 'Korafe-Yegha', false, '');
INSERT INTO languages_iso639 VALUES ('kps', '   ', '   ', '  ', 'I', 'L', 'Tehit', false, '');
INSERT INTO languages_iso639 VALUES ('kpt', '   ', '   ', '  ', 'I', 'L', 'Karata', false, '');
INSERT INTO languages_iso639 VALUES ('kpu', '   ', '   ', '  ', 'I', 'L', 'Kafoa', false, '');
INSERT INTO languages_iso639 VALUES ('kpv', '   ', '   ', '  ', 'I', 'L', 'Komi-Zyrian', false, '');
INSERT INTO languages_iso639 VALUES ('kpw', '   ', '   ', '  ', 'I', 'L', 'Kobon', false, '');
INSERT INTO languages_iso639 VALUES ('kpx', '   ', '   ', '  ', 'I', 'L', 'Mountain Koiali', false, '');
INSERT INTO languages_iso639 VALUES ('kpy', '   ', '   ', '  ', 'I', 'L', 'Koryak', false, '');
INSERT INTO languages_iso639 VALUES ('kpz', '   ', '   ', '  ', 'I', 'L', 'Kupsabiny', false, '');
INSERT INTO languages_iso639 VALUES ('kqa', '   ', '   ', '  ', 'I', 'L', 'Mum', false, '');
INSERT INTO languages_iso639 VALUES ('kqb', '   ', '   ', '  ', 'I', 'L', 'Kovai', false, '');
INSERT INTO languages_iso639 VALUES ('kqc', '   ', '   ', '  ', 'I', 'L', 'Doromu-Koki', false, '');
INSERT INTO languages_iso639 VALUES ('kqd', '   ', '   ', '  ', 'I', 'L', 'Koy Sanjaq Surat', false, '');
INSERT INTO languages_iso639 VALUES ('kqe', '   ', '   ', '  ', 'I', 'L', 'Kalagan', false, '');
INSERT INTO languages_iso639 VALUES ('kqf', '   ', '   ', '  ', 'I', 'L', 'Kakabai', false, '');
INSERT INTO languages_iso639 VALUES ('kqg', '   ', '   ', '  ', 'I', 'L', 'Khe', false, '');
INSERT INTO languages_iso639 VALUES ('kqh', '   ', '   ', '  ', 'I', 'L', 'Kisankasa', false, '');
INSERT INTO languages_iso639 VALUES ('kqi', '   ', '   ', '  ', 'I', 'L', 'Koitabu', false, '');
INSERT INTO languages_iso639 VALUES ('kqj', '   ', '   ', '  ', 'I', 'L', 'Koromira', false, '');
INSERT INTO languages_iso639 VALUES ('kqk', '   ', '   ', '  ', 'I', 'L', 'Kotafon Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('kql', '   ', '   ', '  ', 'I', 'L', 'Kyenele', false, '');
INSERT INTO languages_iso639 VALUES ('kqm', '   ', '   ', '  ', 'I', 'L', 'Khisa', false, '');
INSERT INTO languages_iso639 VALUES ('kqn', '   ', '   ', '  ', 'I', 'L', 'Kaonde', false, '');
INSERT INTO languages_iso639 VALUES ('kqo', '   ', '   ', '  ', 'I', 'L', 'Eastern Krahn', false, '');
INSERT INTO languages_iso639 VALUES ('kqp', '   ', '   ', '  ', 'I', 'L', 'Kimré', false, '');
INSERT INTO languages_iso639 VALUES ('kqq', '   ', '   ', '  ', 'I', 'L', 'Krenak', false, '');
INSERT INTO languages_iso639 VALUES ('kqr', '   ', '   ', '  ', 'I', 'L', 'Kimaragang', false, '');
INSERT INTO languages_iso639 VALUES ('kqs', '   ', '   ', '  ', 'I', 'L', 'Northern Kissi', false, '');
INSERT INTO languages_iso639 VALUES ('kqt', '   ', '   ', '  ', 'I', 'L', 'Klias River Kadazan', false, '');
INSERT INTO languages_iso639 VALUES ('kqu', '   ', '   ', '  ', 'I', 'E', 'Seroa', false, '');
INSERT INTO languages_iso639 VALUES ('kqv', '   ', '   ', '  ', 'I', 'L', 'Okolod', false, '');
INSERT INTO languages_iso639 VALUES ('kqw', '   ', '   ', '  ', 'I', 'L', 'Kandas', false, '');
INSERT INTO languages_iso639 VALUES ('kqx', '   ', '   ', '  ', 'I', 'L', 'Mser', false, '');
INSERT INTO languages_iso639 VALUES ('kqy', '   ', '   ', '  ', 'I', 'L', 'Koorete', false, '');
INSERT INTO languages_iso639 VALUES ('kqz', '   ', '   ', '  ', 'I', 'E', 'Korana', false, '');
INSERT INTO languages_iso639 VALUES ('kra', '   ', '   ', '  ', 'I', 'L', 'Kumhali', false, '');
INSERT INTO languages_iso639 VALUES ('krb', '   ', '   ', '  ', 'I', 'E', 'Karkin', false, '');
INSERT INTO languages_iso639 VALUES ('krc', 'krc', 'krc', '  ', 'I', 'L', 'Karachay-Balkar', false, '');
INSERT INTO languages_iso639 VALUES ('krd', '   ', '   ', '  ', 'I', 'L', 'Kairui-Midiki', false, '');
INSERT INTO languages_iso639 VALUES ('kre', '   ', '   ', '  ', 'I', 'L', 'Panará', false, '');
INSERT INTO languages_iso639 VALUES ('krf', '   ', '   ', '  ', 'I', 'L', 'Koro (Vanuatu)', false, '');
INSERT INTO languages_iso639 VALUES ('krh', '   ', '   ', '  ', 'I', 'L', 'Kurama', false, '');
INSERT INTO languages_iso639 VALUES ('kri', '   ', '   ', '  ', 'I', 'L', 'Krio', false, '');
INSERT INTO languages_iso639 VALUES ('krj', '   ', '   ', '  ', 'I', 'L', 'Kinaray-A', false, '');
INSERT INTO languages_iso639 VALUES ('krk', '   ', '   ', '  ', 'I', 'E', 'Kerek', false, '');
INSERT INTO languages_iso639 VALUES ('krl', 'krl', 'krl', '  ', 'I', 'L', 'Karelian', false, '');
INSERT INTO languages_iso639 VALUES ('krm', '   ', '   ', '  ', 'I', 'L', 'Krim', false, '');
INSERT INTO languages_iso639 VALUES ('krn', '   ', '   ', '  ', 'I', 'L', 'Sapo', false, '');
INSERT INTO languages_iso639 VALUES ('krp', '   ', '   ', '  ', 'I', 'L', 'Korop', false, '');
INSERT INTO languages_iso639 VALUES ('krr', '   ', '   ', '  ', 'I', 'L', 'Kru''ng 2', false, '');
INSERT INTO languages_iso639 VALUES ('krs', '   ', '   ', '  ', 'I', 'L', 'Gbaya (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('krt', '   ', '   ', '  ', 'I', 'L', 'Tumari Kanuri', false, '');
INSERT INTO languages_iso639 VALUES ('kru', 'kru', 'kru', '  ', 'I', 'L', 'Kurukh', false, '');
INSERT INTO languages_iso639 VALUES ('krv', '   ', '   ', '  ', 'I', 'L', 'Kavet', false, '');
INSERT INTO languages_iso639 VALUES ('krw', '   ', '   ', '  ', 'I', 'L', 'Western Krahn', false, '');
INSERT INTO languages_iso639 VALUES ('krx', '   ', '   ', '  ', 'I', 'L', 'Karon', false, '');
INSERT INTO languages_iso639 VALUES ('kry', '   ', '   ', '  ', 'I', 'L', 'Kryts', false, '');
INSERT INTO languages_iso639 VALUES ('krz', '   ', '   ', '  ', 'I', 'L', 'Sota Kanum', false, '');
INSERT INTO languages_iso639 VALUES ('ksa', '   ', '   ', '  ', 'I', 'L', 'Shuwa-Zamani', false, '');
INSERT INTO languages_iso639 VALUES ('ksb', '   ', '   ', '  ', 'I', 'L', 'Shambala', false, '');
INSERT INTO languages_iso639 VALUES ('ksc', '   ', '   ', '  ', 'I', 'L', 'Southern Kalinga', false, '');
INSERT INTO languages_iso639 VALUES ('ksd', '   ', '   ', '  ', 'I', 'L', 'Kuanua', false, '');
INSERT INTO languages_iso639 VALUES ('kse', '   ', '   ', '  ', 'I', 'L', 'Kuni', false, '');
INSERT INTO languages_iso639 VALUES ('ksf', '   ', '   ', '  ', 'I', 'L', 'Bafia', false, '');
INSERT INTO languages_iso639 VALUES ('ksg', '   ', '   ', '  ', 'I', 'L', 'Kusaghe', false, '');
INSERT INTO languages_iso639 VALUES ('ksh', '   ', '   ', '  ', 'I', 'L', 'Kölsch', false, '');
INSERT INTO languages_iso639 VALUES ('ksi', '   ', '   ', '  ', 'I', 'L', 'Krisa', false, '');
INSERT INTO languages_iso639 VALUES ('ksj', '   ', '   ', '  ', 'I', 'L', 'Uare', false, '');
INSERT INTO languages_iso639 VALUES ('ksk', '   ', '   ', '  ', 'I', 'L', 'Kansa', false, '');
INSERT INTO languages_iso639 VALUES ('ksl', '   ', '   ', '  ', 'I', 'L', 'Kumalu', false, '');
INSERT INTO languages_iso639 VALUES ('ksm', '   ', '   ', '  ', 'I', 'L', 'Kumba', false, '');
INSERT INTO languages_iso639 VALUES ('ksn', '   ', '   ', '  ', 'I', 'L', 'Kasiguranin', false, '');
INSERT INTO languages_iso639 VALUES ('kso', '   ', '   ', '  ', 'I', 'L', 'Kofa', false, '');
INSERT INTO languages_iso639 VALUES ('ksp', '   ', '   ', '  ', 'I', 'L', 'Kaba', false, '');
INSERT INTO languages_iso639 VALUES ('ksq', '   ', '   ', '  ', 'I', 'L', 'Kwaami', false, '');
INSERT INTO languages_iso639 VALUES ('ksr', '   ', '   ', '  ', 'I', 'L', 'Borong', false, '');
INSERT INTO languages_iso639 VALUES ('kss', '   ', '   ', '  ', 'I', 'L', 'Southern Kisi', false, '');
INSERT INTO languages_iso639 VALUES ('kst', '   ', '   ', '  ', 'I', 'L', 'Winyé', false, '');
INSERT INTO languages_iso639 VALUES ('ksu', '   ', '   ', '  ', 'I', 'L', 'Khamyang', false, '');
INSERT INTO languages_iso639 VALUES ('ksv', '   ', '   ', '  ', 'I', 'L', 'Kusu', false, '');
INSERT INTO languages_iso639 VALUES ('ksw', '   ', '   ', '  ', 'I', 'L', 'S''gaw Karen', false, '');
INSERT INTO languages_iso639 VALUES ('ksx', '   ', '   ', '  ', 'I', 'L', 'Kedang', false, '');
INSERT INTO languages_iso639 VALUES ('ksy', '   ', '   ', '  ', 'I', 'L', 'Kharia Thar', false, '');
INSERT INTO languages_iso639 VALUES ('ksz', '   ', '   ', '  ', 'I', 'L', 'Kodaku', false, '');
INSERT INTO languages_iso639 VALUES ('kta', '   ', '   ', '  ', 'I', 'L', 'Katua', false, '');
INSERT INTO languages_iso639 VALUES ('ktb', '   ', '   ', '  ', 'I', 'L', 'Kambaata', false, '');
INSERT INTO languages_iso639 VALUES ('ktc', '   ', '   ', '  ', 'I', 'L', 'Kholok', false, '');
INSERT INTO languages_iso639 VALUES ('ktd', '   ', '   ', '  ', 'I', 'L', 'Kokata', false, '');
INSERT INTO languages_iso639 VALUES ('kte', '   ', '   ', '  ', 'I', 'L', 'Nubri', false, '');
INSERT INTO languages_iso639 VALUES ('ktf', '   ', '   ', '  ', 'I', 'L', 'Kwami', false, '');
INSERT INTO languages_iso639 VALUES ('ktg', '   ', '   ', '  ', 'I', 'E', 'Kalkutung', false, '');
INSERT INTO languages_iso639 VALUES ('kth', '   ', '   ', '  ', 'I', 'L', 'Karanga', false, '');
INSERT INTO languages_iso639 VALUES ('kti', '   ', '   ', '  ', 'I', 'L', 'North Muyu', false, '');
INSERT INTO languages_iso639 VALUES ('ktj', '   ', '   ', '  ', 'I', 'L', 'Plapo Krumen', false, '');
INSERT INTO languages_iso639 VALUES ('ktk', '   ', '   ', '  ', 'I', 'E', 'Kaniet', false, '');
INSERT INTO languages_iso639 VALUES ('ktl', '   ', '   ', '  ', 'I', 'L', 'Koroshi', false, '');
INSERT INTO languages_iso639 VALUES ('ktm', '   ', '   ', '  ', 'I', 'L', 'Kurti', false, '');
INSERT INTO languages_iso639 VALUES ('ktn', '   ', '   ', '  ', 'I', 'L', 'Karitiâna', false, '');
INSERT INTO languages_iso639 VALUES ('kto', '   ', '   ', '  ', 'I', 'L', 'Kuot', false, '');
INSERT INTO languages_iso639 VALUES ('ktp', '   ', '   ', '  ', 'I', 'L', 'Kaduo', false, '');
INSERT INTO languages_iso639 VALUES ('ktq', '   ', '   ', '  ', 'I', 'E', 'Katabaga', false, '');
INSERT INTO languages_iso639 VALUES ('ktr', '   ', '   ', '  ', 'I', 'L', 'Kota Marudu Tinagas', false, '');
INSERT INTO languages_iso639 VALUES ('kts', '   ', '   ', '  ', 'I', 'L', 'South Muyu', false, '');
INSERT INTO languages_iso639 VALUES ('ktt', '   ', '   ', '  ', 'I', 'L', 'Ketum', false, '');
INSERT INTO languages_iso639 VALUES ('ktu', '   ', '   ', '  ', 'I', 'L', 'Kituba (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('ktv', '   ', '   ', '  ', 'I', 'L', 'Eastern Katu', false, '');
INSERT INTO languages_iso639 VALUES ('ktw', '   ', '   ', '  ', 'I', 'E', 'Kato', false, '');
INSERT INTO languages_iso639 VALUES ('ktx', '   ', '   ', '  ', 'I', 'L', 'Kaxararí', false, '');
INSERT INTO languages_iso639 VALUES ('kty', '   ', '   ', '  ', 'I', 'L', 'Kango (Bas-Uélé District)', false, '');
INSERT INTO languages_iso639 VALUES ('ktz', '   ', '   ', '  ', 'I', 'L', 'Ju/''hoan', false, '');
INSERT INTO languages_iso639 VALUES ('kua', 'kua', 'kua', 'kj', 'I', 'L', 'Kuanyama', false, '');
INSERT INTO languages_iso639 VALUES ('kub', '   ', '   ', '  ', 'I', 'L', 'Kutep', false, '');
INSERT INTO languages_iso639 VALUES ('kuc', '   ', '   ', '  ', 'I', 'L', 'Kwinsu', false, '');
INSERT INTO languages_iso639 VALUES ('kud', '   ', '   ', '  ', 'I', 'L', '''Auhelawa', false, '');
INSERT INTO languages_iso639 VALUES ('kue', '   ', '   ', '  ', 'I', 'L', 'Kuman', false, '');
INSERT INTO languages_iso639 VALUES ('kuf', '   ', '   ', '  ', 'I', 'L', 'Western Katu', false, '');
INSERT INTO languages_iso639 VALUES ('kug', '   ', '   ', '  ', 'I', 'L', 'Kupa', false, '');
INSERT INTO languages_iso639 VALUES ('kuh', '   ', '   ', '  ', 'I', 'L', 'Kushi', false, '');
INSERT INTO languages_iso639 VALUES ('kui', '   ', '   ', '  ', 'I', 'L', 'Kuikúro-Kalapálo', false, '');
INSERT INTO languages_iso639 VALUES ('kuj', '   ', '   ', '  ', 'I', 'L', 'Kuria', false, '');
INSERT INTO languages_iso639 VALUES ('kuk', '   ', '   ', '  ', 'I', 'L', 'Kepo''', false, '');
INSERT INTO languages_iso639 VALUES ('kul', '   ', '   ', '  ', 'I', 'L', 'Kulere', false, '');
INSERT INTO languages_iso639 VALUES ('kum', 'kum', 'kum', '  ', 'I', 'L', 'Kumyk', false, '');
INSERT INTO languages_iso639 VALUES ('kun', '   ', '   ', '  ', 'I', 'L', 'Kunama', false, '');
INSERT INTO languages_iso639 VALUES ('kuo', '   ', '   ', '  ', 'I', 'L', 'Kumukio', false, '');
INSERT INTO languages_iso639 VALUES ('kup', '   ', '   ', '  ', 'I', 'L', 'Kunimaipa', false, '');
INSERT INTO languages_iso639 VALUES ('kuq', '   ', '   ', '  ', 'I', 'L', 'Karipuna', false, '');
INSERT INTO languages_iso639 VALUES ('kur', 'kur', 'kur', 'ku', 'M', 'L', 'Kurdish', false, '');
INSERT INTO languages_iso639 VALUES ('kus', '   ', '   ', '  ', 'I', 'L', 'Kusaal', false, '');
INSERT INTO languages_iso639 VALUES ('kut', 'kut', 'kut', '  ', 'I', 'L', 'Kutenai', false, '');
INSERT INTO languages_iso639 VALUES ('kuu', '   ', '   ', '  ', 'I', 'L', 'Upper Kuskokwim', false, '');
INSERT INTO languages_iso639 VALUES ('kuv', '   ', '   ', '  ', 'I', 'L', 'Kur', false, '');
INSERT INTO languages_iso639 VALUES ('kuw', '   ', '   ', '  ', 'I', 'L', 'Kpagua', false, '');
INSERT INTO languages_iso639 VALUES ('kux', '   ', '   ', '  ', 'I', 'L', 'Kukatja', false, '');
INSERT INTO languages_iso639 VALUES ('kuy', '   ', '   ', '  ', 'I', 'L', 'Kuuku-Ya''u', false, '');
INSERT INTO languages_iso639 VALUES ('kuz', '   ', '   ', '  ', 'I', 'E', 'Kunza', false, '');
INSERT INTO languages_iso639 VALUES ('kva', '   ', '   ', '  ', 'I', 'L', 'Bagvalal', false, '');
INSERT INTO languages_iso639 VALUES ('kvb', '   ', '   ', '  ', 'I', 'L', 'Kubu', false, '');
INSERT INTO languages_iso639 VALUES ('kvc', '   ', '   ', '  ', 'I', 'L', 'Kove', false, '');
INSERT INTO languages_iso639 VALUES ('kvd', '   ', '   ', '  ', 'I', 'L', 'Kui (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('kve', '   ', '   ', '  ', 'I', 'L', 'Kalabakan', false, '');
INSERT INTO languages_iso639 VALUES ('kvf', '   ', '   ', '  ', 'I', 'L', 'Kabalai', false, '');
INSERT INTO languages_iso639 VALUES ('kvg', '   ', '   ', '  ', 'I', 'L', 'Kuni-Boazi', false, '');
INSERT INTO languages_iso639 VALUES ('kvh', '   ', '   ', '  ', 'I', 'L', 'Komodo', false, '');
INSERT INTO languages_iso639 VALUES ('kvi', '   ', '   ', '  ', 'I', 'L', 'Kwang', false, '');
INSERT INTO languages_iso639 VALUES ('kvj', '   ', '   ', '  ', 'I', 'L', 'Psikye', false, '');
INSERT INTO languages_iso639 VALUES ('kvk', '   ', '   ', '  ', 'I', 'L', 'Korean Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('kvl', '   ', '   ', '  ', 'I', 'L', 'Kayaw', false, '');
INSERT INTO languages_iso639 VALUES ('kvm', '   ', '   ', '  ', 'I', 'L', 'Kendem', false, '');
INSERT INTO languages_iso639 VALUES ('kvn', '   ', '   ', '  ', 'I', 'L', 'Border Kuna', false, '');
INSERT INTO languages_iso639 VALUES ('kvo', '   ', '   ', '  ', 'I', 'L', 'Dobel', false, '');
INSERT INTO languages_iso639 VALUES ('kvp', '   ', '   ', '  ', 'I', 'L', 'Kompane', false, '');
INSERT INTO languages_iso639 VALUES ('kvq', '   ', '   ', '  ', 'I', 'L', 'Geba Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kvr', '   ', '   ', '  ', 'I', 'L', 'Kerinci', false, '');
INSERT INTO languages_iso639 VALUES ('kvs', '   ', '   ', '  ', 'I', 'L', 'Kunggara', false, '');
INSERT INTO languages_iso639 VALUES ('kvt', '   ', '   ', '  ', 'I', 'L', 'Lahta Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kvu', '   ', '   ', '  ', 'I', 'L', 'Yinbaw Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kvv', '   ', '   ', '  ', 'I', 'L', 'Kola', false, '');
INSERT INTO languages_iso639 VALUES ('kvw', '   ', '   ', '  ', 'I', 'L', 'Wersing', false, '');
INSERT INTO languages_iso639 VALUES ('kvx', '   ', '   ', '  ', 'I', 'L', 'Parkari Koli', false, '');
INSERT INTO languages_iso639 VALUES ('kvy', '   ', '   ', '  ', 'I', 'L', 'Yintale Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kvz', '   ', '   ', '  ', 'I', 'L', 'Tsakwambo', false, '');
INSERT INTO languages_iso639 VALUES ('kwa', '   ', '   ', '  ', 'I', 'L', 'Dâw', false, '');
INSERT INTO languages_iso639 VALUES ('kwb', '   ', '   ', '  ', 'I', 'L', 'Kwa', false, '');
INSERT INTO languages_iso639 VALUES ('kwc', '   ', '   ', '  ', 'I', 'L', 'Likwala', false, '');
INSERT INTO languages_iso639 VALUES ('kwd', '   ', '   ', '  ', 'I', 'L', 'Kwaio', false, '');
INSERT INTO languages_iso639 VALUES ('kwe', '   ', '   ', '  ', 'I', 'L', 'Kwerba', false, '');
INSERT INTO languages_iso639 VALUES ('kwf', '   ', '   ', '  ', 'I', 'L', 'Kwara''ae', false, '');
INSERT INTO languages_iso639 VALUES ('kwg', '   ', '   ', '  ', 'I', 'L', 'Sara Kaba Deme', false, '');
INSERT INTO languages_iso639 VALUES ('kwh', '   ', '   ', '  ', 'I', 'L', 'Kowiai', false, '');
INSERT INTO languages_iso639 VALUES ('kwi', '   ', '   ', '  ', 'I', 'L', 'Awa-Cuaiquer', false, '');
INSERT INTO languages_iso639 VALUES ('kwj', '   ', '   ', '  ', 'I', 'L', 'Kwanga', false, '');
INSERT INTO languages_iso639 VALUES ('kwk', '   ', '   ', '  ', 'I', 'L', 'Kwakiutl', false, '');
INSERT INTO languages_iso639 VALUES ('kwl', '   ', '   ', '  ', 'I', 'L', 'Kofyar', false, '');
INSERT INTO languages_iso639 VALUES ('kwm', '   ', '   ', '  ', 'I', 'L', 'Kwambi', false, '');
INSERT INTO languages_iso639 VALUES ('kwn', '   ', '   ', '  ', 'I', 'L', 'Kwangali', false, '');
INSERT INTO languages_iso639 VALUES ('kwo', '   ', '   ', '  ', 'I', 'L', 'Kwomtari', false, '');
INSERT INTO languages_iso639 VALUES ('kwp', '   ', '   ', '  ', 'I', 'L', 'Kodia', false, '');
INSERT INTO languages_iso639 VALUES ('kwq', '   ', '   ', '  ', 'I', 'L', 'Kwak', false, '');
INSERT INTO languages_iso639 VALUES ('kwr', '   ', '   ', '  ', 'I', 'L', 'Kwer', false, '');
INSERT INTO languages_iso639 VALUES ('kws', '   ', '   ', '  ', 'I', 'L', 'Kwese', false, '');
INSERT INTO languages_iso639 VALUES ('kwt', '   ', '   ', '  ', 'I', 'L', 'Kwesten', false, '');
INSERT INTO languages_iso639 VALUES ('kwu', '   ', '   ', '  ', 'I', 'L', 'Kwakum', false, '');
INSERT INTO languages_iso639 VALUES ('kwv', '   ', '   ', '  ', 'I', 'L', 'Sara Kaba Náà', false, '');
INSERT INTO languages_iso639 VALUES ('kww', '   ', '   ', '  ', 'I', 'L', 'Kwinti', false, '');
INSERT INTO languages_iso639 VALUES ('kwx', '   ', '   ', '  ', 'I', 'L', 'Khirwar', false, '');
INSERT INTO languages_iso639 VALUES ('kwy', '   ', '   ', '  ', 'I', 'L', 'San Salvador Kongo', false, '');
INSERT INTO languages_iso639 VALUES ('kwz', '   ', '   ', '  ', 'I', 'E', 'Kwadi', false, '');
INSERT INTO languages_iso639 VALUES ('kxa', '   ', '   ', '  ', 'I', 'L', 'Kairiru', false, '');
INSERT INTO languages_iso639 VALUES ('kxb', '   ', '   ', '  ', 'I', 'L', 'Krobu', false, '');
INSERT INTO languages_iso639 VALUES ('kxc', '   ', '   ', '  ', 'I', 'L', 'Konso', false, '');
INSERT INTO languages_iso639 VALUES ('kxd', '   ', '   ', '  ', 'I', 'L', 'Brunei', false, '');
INSERT INTO languages_iso639 VALUES ('kxe', '   ', '   ', '  ', 'I', 'L', 'Kakihum', false, '');
INSERT INTO languages_iso639 VALUES ('kxf', '   ', '   ', '  ', 'I', 'L', 'Manumanaw Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kxh', '   ', '   ', '  ', 'I', 'L', 'Karo (Ethiopia)', false, '');
INSERT INTO languages_iso639 VALUES ('kxi', '   ', '   ', '  ', 'I', 'L', 'Keningau Murut', false, '');
INSERT INTO languages_iso639 VALUES ('kxj', '   ', '   ', '  ', 'I', 'L', 'Kulfa', false, '');
INSERT INTO languages_iso639 VALUES ('kxk', '   ', '   ', '  ', 'I', 'L', 'Zayein Karen', false, '');
INSERT INTO languages_iso639 VALUES ('kxl', '   ', '   ', '  ', 'I', 'L', 'Nepali Kurux', false, '');
INSERT INTO languages_iso639 VALUES ('kxm', '   ', '   ', '  ', 'I', 'L', 'Northern Khmer', false, '');
INSERT INTO languages_iso639 VALUES ('kxn', '   ', '   ', '  ', 'I', 'L', 'Kanowit-Tanjong Melanau', false, '');
INSERT INTO languages_iso639 VALUES ('kxo', '   ', '   ', '  ', 'I', 'E', 'Kanoé', false, '');
INSERT INTO languages_iso639 VALUES ('kxp', '   ', '   ', '  ', 'I', 'L', 'Wadiyara Koli', false, '');
INSERT INTO languages_iso639 VALUES ('kxq', '   ', '   ', '  ', 'I', 'L', 'Smärky Kanum', false, '');
INSERT INTO languages_iso639 VALUES ('kxr', '   ', '   ', '  ', 'I', 'L', 'Koro (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('kxs', '   ', '   ', '  ', 'I', 'L', 'Kangjia', false, '');
INSERT INTO languages_iso639 VALUES ('kxt', '   ', '   ', '  ', 'I', 'L', 'Koiwat', false, '');
INSERT INTO languages_iso639 VALUES ('kxu', '   ', '   ', '  ', 'I', 'L', 'Kui (India)', false, '');
INSERT INTO languages_iso639 VALUES ('kxv', '   ', '   ', '  ', 'I', 'L', 'Kuvi', false, '');
INSERT INTO languages_iso639 VALUES ('kxw', '   ', '   ', '  ', 'I', 'L', 'Konai', false, '');
INSERT INTO languages_iso639 VALUES ('kxx', '   ', '   ', '  ', 'I', 'L', 'Likuba', false, '');
INSERT INTO languages_iso639 VALUES ('kxy', '   ', '   ', '  ', 'I', 'L', 'Kayong', false, '');
INSERT INTO languages_iso639 VALUES ('kxz', '   ', '   ', '  ', 'I', 'L', 'Kerewo', false, '');
INSERT INTO languages_iso639 VALUES ('kya', '   ', '   ', '  ', 'I', 'L', 'Kwaya', false, '');
INSERT INTO languages_iso639 VALUES ('kyb', '   ', '   ', '  ', 'I', 'L', 'Butbut Kalinga', false, '');
INSERT INTO languages_iso639 VALUES ('kyc', '   ', '   ', '  ', 'I', 'L', 'Kyaka', false, '');
INSERT INTO languages_iso639 VALUES ('kyd', '   ', '   ', '  ', 'I', 'L', 'Karey', false, '');
INSERT INTO languages_iso639 VALUES ('kye', '   ', '   ', '  ', 'I', 'L', 'Krache', false, '');
INSERT INTO languages_iso639 VALUES ('kyf', '   ', '   ', '  ', 'I', 'L', 'Kouya', false, '');
INSERT INTO languages_iso639 VALUES ('kyg', '   ', '   ', '  ', 'I', 'L', 'Keyagana', false, '');
INSERT INTO languages_iso639 VALUES ('kyh', '   ', '   ', '  ', 'I', 'L', 'Karok', false, '');
INSERT INTO languages_iso639 VALUES ('kyi', '   ', '   ', '  ', 'I', 'L', 'Kiput', false, '');
INSERT INTO languages_iso639 VALUES ('kyj', '   ', '   ', '  ', 'I', 'L', 'Karao', false, '');
INSERT INTO languages_iso639 VALUES ('kyk', '   ', '   ', '  ', 'I', 'L', 'Kamayo', false, '');
INSERT INTO languages_iso639 VALUES ('kyl', '   ', '   ', '  ', 'I', 'L', 'Kalapuya', false, '');
INSERT INTO languages_iso639 VALUES ('kym', '   ', '   ', '  ', 'I', 'L', 'Kpatili', false, '');
INSERT INTO languages_iso639 VALUES ('kyn', '   ', '   ', '  ', 'I', 'L', 'Northern Binukidnon', false, '');
INSERT INTO languages_iso639 VALUES ('kyo', '   ', '   ', '  ', 'I', 'L', 'Kelon', false, '');
INSERT INTO languages_iso639 VALUES ('kyp', '   ', '   ', '  ', 'I', 'L', 'Kang', false, '');
INSERT INTO languages_iso639 VALUES ('kyq', '   ', '   ', '  ', 'I', 'L', 'Kenga', false, '');
INSERT INTO languages_iso639 VALUES ('kyr', '   ', '   ', '  ', 'I', 'L', 'Kuruáya', false, '');
INSERT INTO languages_iso639 VALUES ('kys', '   ', '   ', '  ', 'I', 'L', 'Baram Kayan', false, '');
INSERT INTO languages_iso639 VALUES ('kyt', '   ', '   ', '  ', 'I', 'L', 'Kayagar', false, '');
INSERT INTO languages_iso639 VALUES ('kyu', '   ', '   ', '  ', 'I', 'L', 'Western Kayah', false, '');
INSERT INTO languages_iso639 VALUES ('kyv', '   ', '   ', '  ', 'I', 'L', 'Kayort', false, '');
INSERT INTO languages_iso639 VALUES ('kyw', '   ', '   ', '  ', 'I', 'L', 'Kudmali', false, '');
INSERT INTO languages_iso639 VALUES ('kyx', '   ', '   ', '  ', 'I', 'L', 'Rapoisi', false, '');
INSERT INTO languages_iso639 VALUES ('kyy', '   ', '   ', '  ', 'I', 'L', 'Kambaira', false, '');
INSERT INTO languages_iso639 VALUES ('kyz', '   ', '   ', '  ', 'I', 'L', 'Kayabí', false, '');
INSERT INTO languages_iso639 VALUES ('kza', '   ', '   ', '  ', 'I', 'L', 'Western Karaboro', false, '');
INSERT INTO languages_iso639 VALUES ('kzb', '   ', '   ', '  ', 'I', 'L', 'Kaibobo', false, '');
INSERT INTO languages_iso639 VALUES ('kzc', '   ', '   ', '  ', 'I', 'L', 'Bondoukou Kulango', false, '');
INSERT INTO languages_iso639 VALUES ('kzd', '   ', '   ', '  ', 'I', 'L', 'Kadai', false, '');
INSERT INTO languages_iso639 VALUES ('kze', '   ', '   ', '  ', 'I', 'L', 'Kosena', false, '');
INSERT INTO languages_iso639 VALUES ('kzf', '   ', '   ', '  ', 'I', 'L', 'Da''a Kaili', false, '');
INSERT INTO languages_iso639 VALUES ('kzg', '   ', '   ', '  ', 'I', 'L', 'Kikai', false, '');
INSERT INTO languages_iso639 VALUES ('kzi', '   ', '   ', '  ', 'I', 'L', 'Kelabit', false, '');
INSERT INTO languages_iso639 VALUES ('kzj', '   ', '   ', '  ', 'I', 'L', 'Coastal Kadazan', false, '');
INSERT INTO languages_iso639 VALUES ('kzk', '   ', '   ', '  ', 'I', 'E', 'Kazukuru', false, '');
INSERT INTO languages_iso639 VALUES ('kzl', '   ', '   ', '  ', 'I', 'L', 'Kayeli', false, '');
INSERT INTO languages_iso639 VALUES ('kzm', '   ', '   ', '  ', 'I', 'L', 'Kais', false, '');
INSERT INTO languages_iso639 VALUES ('kzn', '   ', '   ', '  ', 'I', 'L', 'Kokola', false, '');
INSERT INTO languages_iso639 VALUES ('kzo', '   ', '   ', '  ', 'I', 'L', 'Kaningi', false, '');
INSERT INTO languages_iso639 VALUES ('kzp', '   ', '   ', '  ', 'I', 'L', 'Kaidipang', false, '');
INSERT INTO languages_iso639 VALUES ('kzq', '   ', '   ', '  ', 'I', 'L', 'Kaike', false, '');
INSERT INTO languages_iso639 VALUES ('kzr', '   ', '   ', '  ', 'I', 'L', 'Karang', false, '');
INSERT INTO languages_iso639 VALUES ('kzs', '   ', '   ', '  ', 'I', 'L', 'Sugut Dusun', false, '');
INSERT INTO languages_iso639 VALUES ('kzt', '   ', '   ', '  ', 'I', 'L', 'Tambunan Dusun', false, '');
INSERT INTO languages_iso639 VALUES ('kzu', '   ', '   ', '  ', 'I', 'L', 'Kayupulau', false, '');
INSERT INTO languages_iso639 VALUES ('kzv', '   ', '   ', '  ', 'I', 'L', 'Komyandaret', false, '');
INSERT INTO languages_iso639 VALUES ('kzw', '   ', '   ', '  ', 'I', 'E', 'Karirí-Xocó', false, '');
INSERT INTO languages_iso639 VALUES ('kzx', '   ', '   ', '  ', 'I', 'L', 'Kamarian', false, '');
INSERT INTO languages_iso639 VALUES ('kzy', '   ', '   ', '  ', 'I', 'L', 'Kango (Tshopo District)', false, '');
INSERT INTO languages_iso639 VALUES ('kzz', '   ', '   ', '  ', 'I', 'L', 'Kalabra', false, '');
INSERT INTO languages_iso639 VALUES ('laa', '   ', '   ', '  ', 'I', 'L', 'Southern Subanen', false, '');
INSERT INTO languages_iso639 VALUES ('lab', '   ', '   ', '  ', 'I', 'A', 'Linear A', false, '');
INSERT INTO languages_iso639 VALUES ('lac', '   ', '   ', '  ', 'I', 'L', 'Lacandon', false, '');
INSERT INTO languages_iso639 VALUES ('lad', 'lad', 'lad', '  ', 'I', 'L', 'Ladino', false, '');
INSERT INTO languages_iso639 VALUES ('lae', '   ', '   ', '  ', 'I', 'L', 'Pattani', false, '');
INSERT INTO languages_iso639 VALUES ('laf', '   ', '   ', '  ', 'I', 'L', 'Lafofa', false, '');
INSERT INTO languages_iso639 VALUES ('lag', '   ', '   ', '  ', 'I', 'L', 'Langi', false, '');
INSERT INTO languages_iso639 VALUES ('lah', 'lah', 'lah', '  ', 'M', 'L', 'Lahnda', false, '');
INSERT INTO languages_iso639 VALUES ('lai', '   ', '   ', '  ', 'I', 'L', 'Lambya', false, '');
INSERT INTO languages_iso639 VALUES ('laj', '   ', '   ', '  ', 'I', 'L', 'Lango (Uganda)', false, '');
INSERT INTO languages_iso639 VALUES ('lak', '   ', '   ', '  ', 'I', 'L', 'Laka (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('lal', '   ', '   ', '  ', 'I', 'L', 'Lalia', false, '');
INSERT INTO languages_iso639 VALUES ('lam', 'lam', 'lam', '  ', 'I', 'L', 'Lamba', false, '');
INSERT INTO languages_iso639 VALUES ('lan', '   ', '   ', '  ', 'I', 'L', 'Laru', false, '');
INSERT INTO languages_iso639 VALUES ('lao', 'lao', 'lao', 'lo', 'I', 'L', 'Lao', false, '');
INSERT INTO languages_iso639 VALUES ('lap', '   ', '   ', '  ', 'I', 'L', 'Laka (Chad)', false, '');
INSERT INTO languages_iso639 VALUES ('laq', '   ', '   ', '  ', 'I', 'L', 'Qabiao', false, '');
INSERT INTO languages_iso639 VALUES ('lar', '   ', '   ', '  ', 'I', 'L', 'Larteh', false, '');
INSERT INTO languages_iso639 VALUES ('las', '   ', '   ', '  ', 'I', 'L', 'Lama (Togo)', false, '');
INSERT INTO languages_iso639 VALUES ('lat', 'lat', 'lat', 'la', 'I', 'A', 'Latin', false, '');
INSERT INTO languages_iso639 VALUES ('lau', '   ', '   ', '  ', 'I', 'L', 'Laba', false, '');
INSERT INTO languages_iso639 VALUES ('lav', 'lav', 'lav', 'lv', 'M', 'L', 'Latvian', false, '');
INSERT INTO languages_iso639 VALUES ('law', '   ', '   ', '  ', 'I', 'L', 'Lauje', false, '');
INSERT INTO languages_iso639 VALUES ('lax', '   ', '   ', '  ', 'I', 'L', 'Tiwa', false, '');
INSERT INTO languages_iso639 VALUES ('lay', '   ', '   ', '  ', 'I', 'L', 'Lama (Myanmar)', false, '');
INSERT INTO languages_iso639 VALUES ('laz', '   ', '   ', '  ', 'I', 'E', 'Aribwatsa', false, '');
INSERT INTO languages_iso639 VALUES ('lba', '   ', '   ', '  ', 'I', 'E', 'Lui', false, '');
INSERT INTO languages_iso639 VALUES ('lbb', '   ', '   ', '  ', 'I', 'L', 'Label', false, '');
INSERT INTO languages_iso639 VALUES ('lbc', '   ', '   ', '  ', 'I', 'L', 'Lakkia', false, '');
INSERT INTO languages_iso639 VALUES ('lbe', '   ', '   ', '  ', 'I', 'L', 'Lak', false, '');
INSERT INTO languages_iso639 VALUES ('lbf', '   ', '   ', '  ', 'I', 'L', 'Tinani', false, '');
INSERT INTO languages_iso639 VALUES ('lbg', '   ', '   ', '  ', 'I', 'L', 'Laopang', false, '');
INSERT INTO languages_iso639 VALUES ('lbi', '   ', '   ', '  ', 'I', 'L', 'La''bi', false, '');
INSERT INTO languages_iso639 VALUES ('lbj', '   ', '   ', '  ', 'I', 'L', 'Ladakhi', false, '');
INSERT INTO languages_iso639 VALUES ('lbk', '   ', '   ', '  ', 'I', 'L', 'Central Bontok', false, '');
INSERT INTO languages_iso639 VALUES ('lbl', '   ', '   ', '  ', 'I', 'L', 'Libon Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('lbm', '   ', '   ', '  ', 'I', 'L', 'Lodhi', false, '');
INSERT INTO languages_iso639 VALUES ('lbn', '   ', '   ', '  ', 'I', 'L', 'Lamet', false, '');
INSERT INTO languages_iso639 VALUES ('lbo', '   ', '   ', '  ', 'I', 'L', 'Laven', false, '');
INSERT INTO languages_iso639 VALUES ('lbq', '   ', '   ', '  ', 'I', 'L', 'Wampar', false, '');
INSERT INTO languages_iso639 VALUES ('lbr', '   ', '   ', '  ', 'I', 'L', 'Lohorung', false, '');
INSERT INTO languages_iso639 VALUES ('lbs', '   ', '   ', '  ', 'I', 'L', 'Libyan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('lbt', '   ', '   ', '  ', 'I', 'L', 'Lachi', false, '');
INSERT INTO languages_iso639 VALUES ('lbu', '   ', '   ', '  ', 'I', 'L', 'Labu', false, '');
INSERT INTO languages_iso639 VALUES ('lbv', '   ', '   ', '  ', 'I', 'L', 'Lavatbura-Lamusong', false, '');
INSERT INTO languages_iso639 VALUES ('lbw', '   ', '   ', '  ', 'I', 'L', 'Tolaki', false, '');
INSERT INTO languages_iso639 VALUES ('lbx', '   ', '   ', '  ', 'I', 'L', 'Lawangan', false, '');
INSERT INTO languages_iso639 VALUES ('lby', '   ', '   ', '  ', 'I', 'E', 'Lamu-Lamu', false, '');
INSERT INTO languages_iso639 VALUES ('lbz', '   ', '   ', '  ', 'I', 'L', 'Lardil', false, '');
INSERT INTO languages_iso639 VALUES ('lcc', '   ', '   ', '  ', 'I', 'L', 'Legenyem', false, '');
INSERT INTO languages_iso639 VALUES ('lcd', '   ', '   ', '  ', 'I', 'L', 'Lola', false, '');
INSERT INTO languages_iso639 VALUES ('lce', '   ', '   ', '  ', 'I', 'L', 'Loncong', false, '');
INSERT INTO languages_iso639 VALUES ('lcf', '   ', '   ', '  ', 'I', 'L', 'Lubu', false, '');
INSERT INTO languages_iso639 VALUES ('lch', '   ', '   ', '  ', 'I', 'L', 'Luchazi', false, '');
INSERT INTO languages_iso639 VALUES ('lcl', '   ', '   ', '  ', 'I', 'L', 'Lisela', false, '');
INSERT INTO languages_iso639 VALUES ('lcm', '   ', '   ', '  ', 'I', 'L', 'Tungag', false, '');
INSERT INTO languages_iso639 VALUES ('lcp', '   ', '   ', '  ', 'I', 'L', 'Western Lawa', false, '');
INSERT INTO languages_iso639 VALUES ('lcq', '   ', '   ', '  ', 'I', 'L', 'Luhu', false, '');
INSERT INTO languages_iso639 VALUES ('lcs', '   ', '   ', '  ', 'I', 'L', 'Lisabata-Nuniali', false, '');
INSERT INTO languages_iso639 VALUES ('lda', '   ', '   ', '  ', 'I', 'L', 'Kla-Dan', false, '');
INSERT INTO languages_iso639 VALUES ('ldb', '   ', '   ', '  ', 'I', 'L', 'Dũya', false, '');
INSERT INTO languages_iso639 VALUES ('ldd', '   ', '   ', '  ', 'I', 'L', 'Luri', false, '');
INSERT INTO languages_iso639 VALUES ('ldg', '   ', '   ', '  ', 'I', 'L', 'Lenyima', false, '');
INSERT INTO languages_iso639 VALUES ('ldh', '   ', '   ', '  ', 'I', 'L', 'Lamja-Dengsa-Tola', false, '');
INSERT INTO languages_iso639 VALUES ('ldi', '   ', '   ', '  ', 'I', 'L', 'Laari', false, '');
INSERT INTO languages_iso639 VALUES ('ldj', '   ', '   ', '  ', 'I', 'L', 'Lemoro', false, '');
INSERT INTO languages_iso639 VALUES ('ldk', '   ', '   ', '  ', 'I', 'L', 'Leelau', false, '');
INSERT INTO languages_iso639 VALUES ('ldl', '   ', '   ', '  ', 'I', 'L', 'Kaan', false, '');
INSERT INTO languages_iso639 VALUES ('ldm', '   ', '   ', '  ', 'I', 'L', 'Landoma', false, '');
INSERT INTO languages_iso639 VALUES ('ldn', '   ', '   ', '  ', 'I', 'C', 'Láadan', false, '');
INSERT INTO languages_iso639 VALUES ('ldo', '   ', '   ', '  ', 'I', 'L', 'Loo', false, '');
INSERT INTO languages_iso639 VALUES ('ldp', '   ', '   ', '  ', 'I', 'L', 'Tso', false, '');
INSERT INTO languages_iso639 VALUES ('ldq', '   ', '   ', '  ', 'I', 'L', 'Lufu', false, '');
INSERT INTO languages_iso639 VALUES ('lea', '   ', '   ', '  ', 'I', 'L', 'Lega-Shabunda', false, '');
INSERT INTO languages_iso639 VALUES ('leb', '   ', '   ', '  ', 'I', 'L', 'Lala-Bisa', false, '');
INSERT INTO languages_iso639 VALUES ('lec', '   ', '   ', '  ', 'I', 'L', 'Leco', false, '');
INSERT INTO languages_iso639 VALUES ('led', '   ', '   ', '  ', 'I', 'L', 'Lendu', false, '');
INSERT INTO languages_iso639 VALUES ('lee', '   ', '   ', '  ', 'I', 'L', 'Lyélé', false, '');
INSERT INTO languages_iso639 VALUES ('lef', '   ', '   ', '  ', 'I', 'L', 'Lelemi', false, '');
INSERT INTO languages_iso639 VALUES ('leg', '   ', '   ', '  ', 'I', 'L', 'Lengua', false, '');
INSERT INTO languages_iso639 VALUES ('leh', '   ', '   ', '  ', 'I', 'L', 'Lenje', false, '');
INSERT INTO languages_iso639 VALUES ('lei', '   ', '   ', '  ', 'I', 'L', 'Lemio', false, '');
INSERT INTO languages_iso639 VALUES ('lej', '   ', '   ', '  ', 'I', 'L', 'Lengola', false, '');
INSERT INTO languages_iso639 VALUES ('lek', '   ', '   ', '  ', 'I', 'L', 'Leipon', false, '');
INSERT INTO languages_iso639 VALUES ('lel', '   ', '   ', '  ', 'I', 'L', 'Lele (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('lem', '   ', '   ', '  ', 'I', 'L', 'Nomaande', false, '');
INSERT INTO languages_iso639 VALUES ('len', '   ', '   ', '  ', 'I', 'E', 'Lenca', false, '');
INSERT INTO languages_iso639 VALUES ('leo', '   ', '   ', '  ', 'I', 'L', 'Leti (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('lep', '   ', '   ', '  ', 'I', 'L', 'Lepcha', false, '');
INSERT INTO languages_iso639 VALUES ('leq', '   ', '   ', '  ', 'I', 'L', 'Lembena', false, '');
INSERT INTO languages_iso639 VALUES ('ler', '   ', '   ', '  ', 'I', 'L', 'Lenkau', false, '');
INSERT INTO languages_iso639 VALUES ('les', '   ', '   ', '  ', 'I', 'L', 'Lese', false, '');
INSERT INTO languages_iso639 VALUES ('let', '   ', '   ', '  ', 'I', 'L', 'Lesing-Gelimi', false, '');
INSERT INTO languages_iso639 VALUES ('leu', '   ', '   ', '  ', 'I', 'L', 'Kara (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('lev', '   ', '   ', '  ', 'I', 'L', 'Lamma', false, '');
INSERT INTO languages_iso639 VALUES ('lew', '   ', '   ', '  ', 'I', 'L', 'Ledo Kaili', false, '');
INSERT INTO languages_iso639 VALUES ('lex', '   ', '   ', '  ', 'I', 'L', 'Luang', false, '');
INSERT INTO languages_iso639 VALUES ('ley', '   ', '   ', '  ', 'I', 'L', 'Lemolang', false, '');
INSERT INTO languages_iso639 VALUES ('lez', 'lez', 'lez', '  ', 'I', 'L', 'Lezghian', false, '');
INSERT INTO languages_iso639 VALUES ('lfa', '   ', '   ', '  ', 'I', 'L', 'Lefa', false, '');
INSERT INTO languages_iso639 VALUES ('lfn', '   ', '   ', '  ', 'I', 'C', 'Lingua Franca Nova', false, '');
INSERT INTO languages_iso639 VALUES ('lga', '   ', '   ', '  ', 'I', 'L', 'Lungga', false, '');
INSERT INTO languages_iso639 VALUES ('lgb', '   ', '   ', '  ', 'I', 'L', 'Laghu', false, '');
INSERT INTO languages_iso639 VALUES ('lgg', '   ', '   ', '  ', 'I', 'L', 'Lugbara', false, '');
INSERT INTO languages_iso639 VALUES ('lgh', '   ', '   ', '  ', 'I', 'L', 'Laghuu', false, '');
INSERT INTO languages_iso639 VALUES ('lgi', '   ', '   ', '  ', 'I', 'L', 'Lengilu', false, '');
INSERT INTO languages_iso639 VALUES ('lgk', '   ', '   ', '  ', 'I', 'L', 'Lingarak', false, '');
INSERT INTO languages_iso639 VALUES ('lgl', '   ', '   ', '  ', 'I', 'L', 'Wala', false, '');
INSERT INTO languages_iso639 VALUES ('lgm', '   ', '   ', '  ', 'I', 'L', 'Lega-Mwenga', false, '');
INSERT INTO languages_iso639 VALUES ('lgn', '   ', '   ', '  ', 'I', 'L', 'Opuuo', false, '');
INSERT INTO languages_iso639 VALUES ('lgq', '   ', '   ', '  ', 'I', 'L', 'Logba', false, '');
INSERT INTO languages_iso639 VALUES ('lgr', '   ', '   ', '  ', 'I', 'L', 'Lengo', false, '');
INSERT INTO languages_iso639 VALUES ('lgt', '   ', '   ', '  ', 'I', 'L', 'Pahi', false, '');
INSERT INTO languages_iso639 VALUES ('lgu', '   ', '   ', '  ', 'I', 'L', 'Longgu', false, '');
INSERT INTO languages_iso639 VALUES ('lgz', '   ', '   ', '  ', 'I', 'L', 'Ligenza', false, '');
INSERT INTO languages_iso639 VALUES ('lha', '   ', '   ', '  ', 'I', 'L', 'Laha (Viet Nam)', false, '');
INSERT INTO languages_iso639 VALUES ('lhh', '   ', '   ', '  ', 'I', 'L', 'Laha (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('lhi', '   ', '   ', '  ', 'I', 'L', 'Lahu Shi', false, '');
INSERT INTO languages_iso639 VALUES ('lhl', '   ', '   ', '  ', 'I', 'L', 'Lahul Lohar', false, '');
INSERT INTO languages_iso639 VALUES ('lhm', '   ', '   ', '  ', 'I', 'L', 'Lhomi', false, '');
INSERT INTO languages_iso639 VALUES ('lhn', '   ', '   ', '  ', 'I', 'L', 'Lahanan', false, '');
INSERT INTO languages_iso639 VALUES ('lhp', '   ', '   ', '  ', 'I', 'L', 'Lhokpu', false, '');
INSERT INTO languages_iso639 VALUES ('lhs', '   ', '   ', '  ', 'I', 'E', 'Mlahsö', false, '');
INSERT INTO languages_iso639 VALUES ('lht', '   ', '   ', '  ', 'I', 'L', 'Lo-Toga', false, '');
INSERT INTO languages_iso639 VALUES ('lhu', '   ', '   ', '  ', 'I', 'L', 'Lahu', false, '');
INSERT INTO languages_iso639 VALUES ('lia', '   ', '   ', '  ', 'I', 'L', 'West-Central Limba', false, '');
INSERT INTO languages_iso639 VALUES ('lib', '   ', '   ', '  ', 'I', 'L', 'Likum', false, '');
INSERT INTO languages_iso639 VALUES ('lic', '   ', '   ', '  ', 'I', 'L', 'Hlai', false, '');
INSERT INTO languages_iso639 VALUES ('lid', '   ', '   ', '  ', 'I', 'L', 'Nyindrou', false, '');
INSERT INTO languages_iso639 VALUES ('lie', '   ', '   ', '  ', 'I', 'L', 'Likila', false, '');
INSERT INTO languages_iso639 VALUES ('lif', '   ', '   ', '  ', 'I', 'L', 'Limbu', false, '');
INSERT INTO languages_iso639 VALUES ('lig', '   ', '   ', '  ', 'I', 'L', 'Ligbi', false, '');
INSERT INTO languages_iso639 VALUES ('lih', '   ', '   ', '  ', 'I', 'L', 'Lihir', false, '');
INSERT INTO languages_iso639 VALUES ('lii', '   ', '   ', '  ', 'I', 'L', 'Lingkhim', false, '');
INSERT INTO languages_iso639 VALUES ('lij', '   ', '   ', '  ', 'I', 'L', 'Ligurian', false, '');
INSERT INTO languages_iso639 VALUES ('lik', '   ', '   ', '  ', 'I', 'L', 'Lika', false, '');
INSERT INTO languages_iso639 VALUES ('lil', '   ', '   ', '  ', 'I', 'L', 'Lillooet', false, '');
INSERT INTO languages_iso639 VALUES ('lim', 'lim', 'lim', 'li', 'I', 'L', 'Limburgan', false, '');
INSERT INTO languages_iso639 VALUES ('lin', 'lin', 'lin', 'ln', 'I', 'L', 'Lingala', false, '');
INSERT INTO languages_iso639 VALUES ('lio', '   ', '   ', '  ', 'I', 'L', 'Liki', false, '');
INSERT INTO languages_iso639 VALUES ('lip', '   ', '   ', '  ', 'I', 'L', 'Sekpele', false, '');
INSERT INTO languages_iso639 VALUES ('liq', '   ', '   ', '  ', 'I', 'L', 'Libido', false, '');
INSERT INTO languages_iso639 VALUES ('lir', '   ', '   ', '  ', 'I', 'L', 'Liberian English', false, '');
INSERT INTO languages_iso639 VALUES ('lis', '   ', '   ', '  ', 'I', 'L', 'Lisu', false, '');
INSERT INTO languages_iso639 VALUES ('lit', 'lit', 'lit', 'lt', 'I', 'L', 'Lithuanian', false, '');
INSERT INTO languages_iso639 VALUES ('liu', '   ', '   ', '  ', 'I', 'L', 'Logorik', false, '');
INSERT INTO languages_iso639 VALUES ('liv', '   ', '   ', '  ', 'I', 'L', 'Liv', false, '');
INSERT INTO languages_iso639 VALUES ('liw', '   ', '   ', '  ', 'I', 'L', 'Col', false, '');
INSERT INTO languages_iso639 VALUES ('lix', '   ', '   ', '  ', 'I', 'L', 'Liabuku', false, '');
INSERT INTO languages_iso639 VALUES ('liy', '   ', '   ', '  ', 'I', 'L', 'Banda-Bambari', false, '');
INSERT INTO languages_iso639 VALUES ('liz', '   ', '   ', '  ', 'I', 'L', 'Libinza', false, '');
INSERT INTO languages_iso639 VALUES ('lja', '   ', '   ', '  ', 'I', 'E', 'Golpa', false, '');
INSERT INTO languages_iso639 VALUES ('lje', '   ', '   ', '  ', 'I', 'L', 'Rampi', false, '');
INSERT INTO languages_iso639 VALUES ('lji', '   ', '   ', '  ', 'I', 'L', 'Laiyolo', false, '');
INSERT INTO languages_iso639 VALUES ('ljl', '   ', '   ', '  ', 'I', 'L', 'Li''o', false, '');
INSERT INTO languages_iso639 VALUES ('ljp', '   ', '   ', '  ', 'I', 'L', 'Lampung Api', false, '');
INSERT INTO languages_iso639 VALUES ('ljw', '   ', '   ', '  ', 'I', 'L', 'Yirandali', false, '');
INSERT INTO languages_iso639 VALUES ('ljx', '   ', '   ', '  ', 'I', 'E', 'Yuru', false, '');
INSERT INTO languages_iso639 VALUES ('lka', '   ', '   ', '  ', 'I', 'L', 'Lakalei', false, '');
INSERT INTO languages_iso639 VALUES ('lkb', '   ', '   ', '  ', 'I', 'L', 'Kabras', false, '');
INSERT INTO languages_iso639 VALUES ('lkc', '   ', '   ', '  ', 'I', 'L', 'Kucong', false, '');
INSERT INTO languages_iso639 VALUES ('lkd', '   ', '   ', '  ', 'I', 'L', 'Lakondê', false, '');
INSERT INTO languages_iso639 VALUES ('lke', '   ', '   ', '  ', 'I', 'L', 'Kenyi', false, '');
INSERT INTO languages_iso639 VALUES ('lkh', '   ', '   ', '  ', 'I', 'L', 'Lakha', false, '');
INSERT INTO languages_iso639 VALUES ('lki', '   ', '   ', '  ', 'I', 'L', 'Laki', false, '');
INSERT INTO languages_iso639 VALUES ('lkj', '   ', '   ', '  ', 'I', 'L', 'Remun', false, '');
INSERT INTO languages_iso639 VALUES ('lkl', '   ', '   ', '  ', 'I', 'L', 'Laeko-Libuat', false, '');
INSERT INTO languages_iso639 VALUES ('lkm', '   ', '   ', '  ', 'I', 'E', 'Kalaamaya', false, '');
INSERT INTO languages_iso639 VALUES ('lkn', '   ', '   ', '  ', 'I', 'L', 'Lakon', false, '');
INSERT INTO languages_iso639 VALUES ('lko', '   ', '   ', '  ', 'I', 'L', 'Khayo', false, '');
INSERT INTO languages_iso639 VALUES ('lkr', '   ', '   ', '  ', 'I', 'L', 'Päri', false, '');
INSERT INTO languages_iso639 VALUES ('lks', '   ', '   ', '  ', 'I', 'L', 'Kisa', false, '');
INSERT INTO languages_iso639 VALUES ('lkt', '   ', '   ', '  ', 'I', 'L', 'Lakota', false, '');
INSERT INTO languages_iso639 VALUES ('lku', '   ', '   ', '  ', 'I', 'E', 'Kungkari', false, '');
INSERT INTO languages_iso639 VALUES ('lky', '   ', '   ', '  ', 'I', 'L', 'Lokoya', false, '');
INSERT INTO languages_iso639 VALUES ('lla', '   ', '   ', '  ', 'I', 'L', 'Lala-Roba', false, '');
INSERT INTO languages_iso639 VALUES ('llb', '   ', '   ', '  ', 'I', 'L', 'Lolo', false, '');
INSERT INTO languages_iso639 VALUES ('llc', '   ', '   ', '  ', 'I', 'L', 'Lele (Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('lld', '   ', '   ', '  ', 'I', 'L', 'Ladin', false, '');
INSERT INTO languages_iso639 VALUES ('lle', '   ', '   ', '  ', 'I', 'L', 'Lele (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('llf', '   ', '   ', '  ', 'I', 'E', 'Hermit', false, '');
INSERT INTO languages_iso639 VALUES ('llg', '   ', '   ', '  ', 'I', 'L', 'Lole', false, '');
INSERT INTO languages_iso639 VALUES ('llh', '   ', '   ', '  ', 'I', 'L', 'Lamu', false, '');
INSERT INTO languages_iso639 VALUES ('lli', '   ', '   ', '  ', 'I', 'L', 'Teke-Laali', false, '');
INSERT INTO languages_iso639 VALUES ('llj', '   ', '   ', '  ', 'I', 'E', 'Ladji Ladji', false, '');
INSERT INTO languages_iso639 VALUES ('llk', '   ', '   ', '  ', 'I', 'E', 'Lelak', false, '');
INSERT INTO languages_iso639 VALUES ('lll', '   ', '   ', '  ', 'I', 'L', 'Lilau', false, '');
INSERT INTO languages_iso639 VALUES ('llm', '   ', '   ', '  ', 'I', 'L', 'Lasalimu', false, '');
INSERT INTO languages_iso639 VALUES ('lln', '   ', '   ', '  ', 'I', 'L', 'Lele (Chad)', false, '');
INSERT INTO languages_iso639 VALUES ('llo', '   ', '   ', '  ', 'I', 'L', 'Khlor', false, '');
INSERT INTO languages_iso639 VALUES ('llp', '   ', '   ', '  ', 'I', 'L', 'North Efate', false, '');
INSERT INTO languages_iso639 VALUES ('llq', '   ', '   ', '  ', 'I', 'L', 'Lolak', false, '');
INSERT INTO languages_iso639 VALUES ('lls', '   ', '   ', '  ', 'I', 'L', 'Lithuanian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('llu', '   ', '   ', '  ', 'I', 'L', 'Lau', false, '');
INSERT INTO languages_iso639 VALUES ('llx', '   ', '   ', '  ', 'I', 'L', 'Lauan', false, '');
INSERT INTO languages_iso639 VALUES ('lma', '   ', '   ', '  ', 'I', 'L', 'East Limba', false, '');
INSERT INTO languages_iso639 VALUES ('lmb', '   ', '   ', '  ', 'I', 'L', 'Merei', false, '');
INSERT INTO languages_iso639 VALUES ('lmc', '   ', '   ', '  ', 'I', 'E', 'Limilngan', false, '');
INSERT INTO languages_iso639 VALUES ('lmd', '   ', '   ', '  ', 'I', 'L', 'Lumun', false, '');
INSERT INTO languages_iso639 VALUES ('lme', '   ', '   ', '  ', 'I', 'L', 'Pévé', false, '');
INSERT INTO languages_iso639 VALUES ('lmf', '   ', '   ', '  ', 'I', 'L', 'South Lembata', false, '');
INSERT INTO languages_iso639 VALUES ('lmg', '   ', '   ', '  ', 'I', 'L', 'Lamogai', false, '');
INSERT INTO languages_iso639 VALUES ('lmh', '   ', '   ', '  ', 'I', 'L', 'Lambichhong', false, '');
INSERT INTO languages_iso639 VALUES ('lmi', '   ', '   ', '  ', 'I', 'L', 'Lombi', false, '');
INSERT INTO languages_iso639 VALUES ('lmj', '   ', '   ', '  ', 'I', 'L', 'West Lembata', false, '');
INSERT INTO languages_iso639 VALUES ('lmk', '   ', '   ', '  ', 'I', 'L', 'Lamkang', false, '');
INSERT INTO languages_iso639 VALUES ('lml', '   ', '   ', '  ', 'I', 'L', 'Hano', false, '');
INSERT INTO languages_iso639 VALUES ('lmm', '   ', '   ', '  ', 'I', 'L', 'Lamam', false, '');
INSERT INTO languages_iso639 VALUES ('lmn', '   ', '   ', '  ', 'I', 'L', 'Lambadi', false, '');
INSERT INTO languages_iso639 VALUES ('lmo', '   ', '   ', '  ', 'I', 'L', 'Lombard', false, '');
INSERT INTO languages_iso639 VALUES ('lmp', '   ', '   ', '  ', 'I', 'L', 'Limbum', false, '');
INSERT INTO languages_iso639 VALUES ('lmq', '   ', '   ', '  ', 'I', 'L', 'Lamatuka', false, '');
INSERT INTO languages_iso639 VALUES ('lmr', '   ', '   ', '  ', 'I', 'L', 'Lamalera', false, '');
INSERT INTO languages_iso639 VALUES ('lmu', '   ', '   ', '  ', 'I', 'L', 'Lamenu', false, '');
INSERT INTO languages_iso639 VALUES ('lmv', '   ', '   ', '  ', 'I', 'L', 'Lomaiviti', false, '');
INSERT INTO languages_iso639 VALUES ('lmw', '   ', '   ', '  ', 'I', 'L', 'Lake Miwok', false, '');
INSERT INTO languages_iso639 VALUES ('lmx', '   ', '   ', '  ', 'I', 'L', 'Laimbue', false, '');
INSERT INTO languages_iso639 VALUES ('lmy', '   ', '   ', '  ', 'I', 'L', 'Lamboya', false, '');
INSERT INTO languages_iso639 VALUES ('lmz', '   ', '   ', '  ', 'I', 'E', 'Lumbee', false, '');
INSERT INTO languages_iso639 VALUES ('lna', '   ', '   ', '  ', 'I', 'L', 'Langbashe', false, '');
INSERT INTO languages_iso639 VALUES ('lnb', '   ', '   ', '  ', 'I', 'L', 'Mbalanhu', false, '');
INSERT INTO languages_iso639 VALUES ('lnd', '   ', '   ', '  ', 'I', 'L', 'Lundayeh', false, '');
INSERT INTO languages_iso639 VALUES ('lng', '   ', '   ', '  ', 'I', 'A', 'Langobardic', false, '');
INSERT INTO languages_iso639 VALUES ('lnh', '   ', '   ', '  ', 'I', 'L', 'Lanoh', false, '');
INSERT INTO languages_iso639 VALUES ('lni', '   ', '   ', '  ', 'I', 'L', 'Daantanai''', false, '');
INSERT INTO languages_iso639 VALUES ('lnj', '   ', '   ', '  ', 'I', 'E', 'Leningitij', false, '');
INSERT INTO languages_iso639 VALUES ('lnl', '   ', '   ', '  ', 'I', 'L', 'South Central Banda', false, '');
INSERT INTO languages_iso639 VALUES ('lnm', '   ', '   ', '  ', 'I', 'L', 'Langam', false, '');
INSERT INTO languages_iso639 VALUES ('lnn', '   ', '   ', '  ', 'I', 'L', 'Lorediakarkar', false, '');
INSERT INTO languages_iso639 VALUES ('lno', '   ', '   ', '  ', 'I', 'L', 'Lango (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('lns', '   ', '   ', '  ', 'I', 'L', 'Lamnso''', false, '');
INSERT INTO languages_iso639 VALUES ('lnu', '   ', '   ', '  ', 'I', 'L', 'Longuda', false, '');
INSERT INTO languages_iso639 VALUES ('lnw', '   ', '   ', '  ', 'I', 'E', 'Lanima', false, '');
INSERT INTO languages_iso639 VALUES ('lnz', '   ', '   ', '  ', 'I', 'L', 'Lonzo', false, '');
INSERT INTO languages_iso639 VALUES ('loa', '   ', '   ', '  ', 'I', 'L', 'Loloda', false, '');
INSERT INTO languages_iso639 VALUES ('lob', '   ', '   ', '  ', 'I', 'L', 'Lobi', false, '');
INSERT INTO languages_iso639 VALUES ('loc', '   ', '   ', '  ', 'I', 'L', 'Inonhan', false, '');
INSERT INTO languages_iso639 VALUES ('loe', '   ', '   ', '  ', 'I', 'L', 'Saluan', false, '');
INSERT INTO languages_iso639 VALUES ('lof', '   ', '   ', '  ', 'I', 'L', 'Logol', false, '');
INSERT INTO languages_iso639 VALUES ('log', '   ', '   ', '  ', 'I', 'L', 'Logo', false, '');
INSERT INTO languages_iso639 VALUES ('loh', '   ', '   ', '  ', 'I', 'L', 'Narim', false, '');
INSERT INTO languages_iso639 VALUES ('loi', '   ', '   ', '  ', 'I', 'L', 'Loma (Côte d''Ivoire)', false, '');
INSERT INTO languages_iso639 VALUES ('loj', '   ', '   ', '  ', 'I', 'L', 'Lou', false, '');
INSERT INTO languages_iso639 VALUES ('lok', '   ', '   ', '  ', 'I', 'L', 'Loko', false, '');
INSERT INTO languages_iso639 VALUES ('lol', 'lol', 'lol', '  ', 'I', 'L', 'Mongo', false, '');
INSERT INTO languages_iso639 VALUES ('lom', '   ', '   ', '  ', 'I', 'L', 'Loma (Liberia)', false, '');
INSERT INTO languages_iso639 VALUES ('lon', '   ', '   ', '  ', 'I', 'L', 'Malawi Lomwe', false, '');
INSERT INTO languages_iso639 VALUES ('loo', '   ', '   ', '  ', 'I', 'L', 'Lombo', false, '');
INSERT INTO languages_iso639 VALUES ('lop', '   ', '   ', '  ', 'I', 'L', 'Lopa', false, '');
INSERT INTO languages_iso639 VALUES ('loq', '   ', '   ', '  ', 'I', 'L', 'Lobala', false, '');
INSERT INTO languages_iso639 VALUES ('lor', '   ', '   ', '  ', 'I', 'L', 'Téén', false, '');
INSERT INTO languages_iso639 VALUES ('los', '   ', '   ', '  ', 'I', 'L', 'Loniu', false, '');
INSERT INTO languages_iso639 VALUES ('lot', '   ', '   ', '  ', 'I', 'L', 'Otuho', false, '');
INSERT INTO languages_iso639 VALUES ('lou', '   ', '   ', '  ', 'I', 'L', 'Louisiana Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('lov', '   ', '   ', '  ', 'I', 'L', 'Lopi', false, '');
INSERT INTO languages_iso639 VALUES ('low', '   ', '   ', '  ', 'I', 'L', 'Tampias Lobu', false, '');
INSERT INTO languages_iso639 VALUES ('lox', '   ', '   ', '  ', 'I', 'L', 'Loun', false, '');
INSERT INTO languages_iso639 VALUES ('loy', '   ', '   ', '  ', 'I', 'L', 'Loke', false, '');
INSERT INTO languages_iso639 VALUES ('loz', 'loz', 'loz', '  ', 'I', 'L', 'Lozi', false, '');
INSERT INTO languages_iso639 VALUES ('lpa', '   ', '   ', '  ', 'I', 'L', 'Lelepa', false, '');
INSERT INTO languages_iso639 VALUES ('lpe', '   ', '   ', '  ', 'I', 'L', 'Lepki', false, '');
INSERT INTO languages_iso639 VALUES ('lpn', '   ', '   ', '  ', 'I', 'L', 'Long Phuri Naga', false, '');
INSERT INTO languages_iso639 VALUES ('lpo', '   ', '   ', '  ', 'I', 'L', 'Lipo', false, '');
INSERT INTO languages_iso639 VALUES ('lpx', '   ', '   ', '  ', 'I', 'L', 'Lopit', false, '');
INSERT INTO languages_iso639 VALUES ('lra', '   ', '   ', '  ', 'I', 'L', 'Rara Bakati''', false, '');
INSERT INTO languages_iso639 VALUES ('lrc', '   ', '   ', '  ', 'I', 'L', 'Northern Luri', false, '');
INSERT INTO languages_iso639 VALUES ('lre', '   ', '   ', '  ', 'I', 'E', 'Laurentian', false, '');
INSERT INTO languages_iso639 VALUES ('lrg', '   ', '   ', '  ', 'I', 'E', 'Laragia', false, '');
INSERT INTO languages_iso639 VALUES ('lri', '   ', '   ', '  ', 'I', 'L', 'Marachi', false, '');
INSERT INTO languages_iso639 VALUES ('lrk', '   ', '   ', '  ', 'I', 'L', 'Loarki', false, '');
INSERT INTO languages_iso639 VALUES ('lrl', '   ', '   ', '  ', 'I', 'L', 'Lari', false, '');
INSERT INTO languages_iso639 VALUES ('lrm', '   ', '   ', '  ', 'I', 'L', 'Marama', false, '');
INSERT INTO languages_iso639 VALUES ('lrn', '   ', '   ', '  ', 'I', 'L', 'Lorang', false, '');
INSERT INTO languages_iso639 VALUES ('lro', '   ', '   ', '  ', 'I', 'L', 'Laro', false, '');
INSERT INTO languages_iso639 VALUES ('lrr', '   ', '   ', '  ', 'I', 'L', 'Southern Yamphu', false, '');
INSERT INTO languages_iso639 VALUES ('lrt', '   ', '   ', '  ', 'I', 'L', 'Larantuka Malay', false, '');
INSERT INTO languages_iso639 VALUES ('lrv', '   ', '   ', '  ', 'I', 'L', 'Larevat', false, '');
INSERT INTO languages_iso639 VALUES ('lrz', '   ', '   ', '  ', 'I', 'L', 'Lemerig', false, '');
INSERT INTO languages_iso639 VALUES ('lsa', '   ', '   ', '  ', 'I', 'L', 'Lasgerdi', false, '');
INSERT INTO languages_iso639 VALUES ('lsd', '   ', '   ', '  ', 'I', 'L', 'Lishana Deni', false, '');
INSERT INTO languages_iso639 VALUES ('lse', '   ', '   ', '  ', 'I', 'L', 'Lusengo', false, '');
INSERT INTO languages_iso639 VALUES ('lsg', '   ', '   ', '  ', 'I', 'L', 'Lyons Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('lsh', '   ', '   ', '  ', 'I', 'L', 'Lish', false, '');
INSERT INTO languages_iso639 VALUES ('lsi', '   ', '   ', '  ', 'I', 'L', 'Lashi', false, '');
INSERT INTO languages_iso639 VALUES ('lsl', '   ', '   ', '  ', 'I', 'L', 'Latvian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('lsm', '   ', '   ', '  ', 'I', 'L', 'Saamia', false, '');
INSERT INTO languages_iso639 VALUES ('lso', '   ', '   ', '  ', 'I', 'L', 'Laos Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('lsp', '   ', '   ', '  ', 'I', 'L', 'Panamanian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('lsr', '   ', '   ', '  ', 'I', 'L', 'Aruop', false, '');
INSERT INTO languages_iso639 VALUES ('lss', '   ', '   ', '  ', 'I', 'L', 'Lasi', false, '');
INSERT INTO languages_iso639 VALUES ('lst', '   ', '   ', '  ', 'I', 'L', 'Trinidad and Tobago Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('lsy', '   ', '   ', '  ', 'I', 'L', 'Mauritian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ltc', '   ', '   ', '  ', 'I', 'H', 'Late Middle Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('ltg', '   ', '   ', '  ', 'I', 'L', 'Latgalian', false, '');
INSERT INTO languages_iso639 VALUES ('lti', '   ', '   ', '  ', 'I', 'L', 'Leti (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('ltn', '   ', '   ', '  ', 'I', 'L', 'Latundê', false, '');
INSERT INTO languages_iso639 VALUES ('lto', '   ', '   ', '  ', 'I', 'L', 'Tsotso', false, '');
INSERT INTO languages_iso639 VALUES ('lts', '   ', '   ', '  ', 'I', 'L', 'Tachoni', false, '');
INSERT INTO languages_iso639 VALUES ('ltu', '   ', '   ', '  ', 'I', 'L', 'Latu', false, '');
INSERT INTO languages_iso639 VALUES ('ltz', 'ltz', 'ltz', 'lb', 'I', 'L', 'Luxembourgish', false, '');
INSERT INTO languages_iso639 VALUES ('lua', 'lua', 'lua', '  ', 'I', 'L', 'Luba-Lulua', false, '');
INSERT INTO languages_iso639 VALUES ('lub', 'lub', 'lub', 'lu', 'I', 'L', 'Luba-Katanga', false, '');
INSERT INTO languages_iso639 VALUES ('luc', '   ', '   ', '  ', 'I', 'L', 'Aringa', false, '');
INSERT INTO languages_iso639 VALUES ('lud', '   ', '   ', '  ', 'I', 'L', 'Ludian', false, '');
INSERT INTO languages_iso639 VALUES ('lue', '   ', '   ', '  ', 'I', 'L', 'Luvale', false, '');
INSERT INTO languages_iso639 VALUES ('luf', '   ', '   ', '  ', 'I', 'L', 'Laua', false, '');
INSERT INTO languages_iso639 VALUES ('lug', 'lug', 'lug', 'lg', 'I', 'L', 'Ganda', false, '');
INSERT INTO languages_iso639 VALUES ('lui', 'lui', 'lui', '  ', 'I', 'L', 'Luiseno', false, '');
INSERT INTO languages_iso639 VALUES ('luj', '   ', '   ', '  ', 'I', 'L', 'Luna', false, '');
INSERT INTO languages_iso639 VALUES ('luk', '   ', '   ', '  ', 'I', 'L', 'Lunanakha', false, '');
INSERT INTO languages_iso639 VALUES ('lul', '   ', '   ', '  ', 'I', 'L', 'Olu''bo', false, '');
INSERT INTO languages_iso639 VALUES ('lum', '   ', '   ', '  ', 'I', 'L', 'Luimbi', false, '');
INSERT INTO languages_iso639 VALUES ('lun', 'lun', 'lun', '  ', 'I', 'L', 'Lunda', false, '');
INSERT INTO languages_iso639 VALUES ('luo', 'luo', 'luo', '  ', 'I', 'L', 'Luo (Kenya and Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('lup', '   ', '   ', '  ', 'I', 'L', 'Lumbu', false, '');
INSERT INTO languages_iso639 VALUES ('luq', '   ', '   ', '  ', 'I', 'L', 'Lucumi', false, '');
INSERT INTO languages_iso639 VALUES ('lur', '   ', '   ', '  ', 'I', 'L', 'Laura', false, '');
INSERT INTO languages_iso639 VALUES ('lus', 'lus', 'lus', '  ', 'I', 'L', 'Lushai', false, '');
INSERT INTO languages_iso639 VALUES ('lut', '   ', '   ', '  ', 'I', 'L', 'Lushootseed', false, '');
INSERT INTO languages_iso639 VALUES ('luu', '   ', '   ', '  ', 'I', 'L', 'Lumba-Yakkha', false, '');
INSERT INTO languages_iso639 VALUES ('luv', '   ', '   ', '  ', 'I', 'L', 'Luwati', false, '');
INSERT INTO languages_iso639 VALUES ('luw', '   ', '   ', '  ', 'I', 'L', 'Luo (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('luy', '   ', '   ', '  ', 'M', 'L', 'Luyia', false, '');
INSERT INTO languages_iso639 VALUES ('luz', '   ', '   ', '  ', 'I', 'L', 'Southern Luri', false, '');
INSERT INTO languages_iso639 VALUES ('lva', '   ', '   ', '  ', 'I', 'L', 'Maku''a', false, '');
INSERT INTO languages_iso639 VALUES ('lvk', '   ', '   ', '  ', 'I', 'L', 'Lavukaleve', false, '');
INSERT INTO languages_iso639 VALUES ('lvs', '   ', '   ', '  ', 'I', 'L', 'Standard Latvian', false, '');
INSERT INTO languages_iso639 VALUES ('lvu', '   ', '   ', '  ', 'I', 'L', 'Levuka', false, '');
INSERT INTO languages_iso639 VALUES ('lwa', '   ', '   ', '  ', 'I', 'L', 'Lwalu', false, '');
INSERT INTO languages_iso639 VALUES ('lwe', '   ', '   ', '  ', 'I', 'L', 'Lewo Eleng', false, '');
INSERT INTO languages_iso639 VALUES ('lwg', '   ', '   ', '  ', 'I', 'L', 'Wanga', false, '');
INSERT INTO languages_iso639 VALUES ('lwh', '   ', '   ', '  ', 'I', 'L', 'White Lachi', false, '');
INSERT INTO languages_iso639 VALUES ('lwl', '   ', '   ', '  ', 'I', 'L', 'Eastern Lawa', false, '');
INSERT INTO languages_iso639 VALUES ('lwm', '   ', '   ', '  ', 'I', 'L', 'Laomian', false, '');
INSERT INTO languages_iso639 VALUES ('lwo', '   ', '   ', '  ', 'I', 'L', 'Luwo', false, '');
INSERT INTO languages_iso639 VALUES ('lwt', '   ', '   ', '  ', 'I', 'L', 'Lewotobi', false, '');
INSERT INTO languages_iso639 VALUES ('lwu', '   ', '   ', '  ', 'I', 'L', 'Lawu', false, '');
INSERT INTO languages_iso639 VALUES ('lww', '   ', '   ', '  ', 'I', 'L', 'Lewo', false, '');
INSERT INTO languages_iso639 VALUES ('lya', '   ', '   ', '  ', 'I', 'L', 'Layakha', false, '');
INSERT INTO languages_iso639 VALUES ('lyg', '   ', '   ', '  ', 'I', 'L', 'Lyngngam', false, '');
INSERT INTO languages_iso639 VALUES ('lyn', '   ', '   ', '  ', 'I', 'L', 'Luyana', false, '');
INSERT INTO languages_iso639 VALUES ('lzh', '   ', '   ', '  ', 'I', 'H', 'Literary Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('lzl', '   ', '   ', '  ', 'I', 'L', 'Litzlitz', false, '');
INSERT INTO languages_iso639 VALUES ('lzn', '   ', '   ', '  ', 'I', 'L', 'Leinong Naga', false, '');
INSERT INTO languages_iso639 VALUES ('lzz', '   ', '   ', '  ', 'I', 'L', 'Laz', false, '');
INSERT INTO languages_iso639 VALUES ('maa', '   ', '   ', '  ', 'I', 'L', 'San Jerónimo Tecóatl Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('mab', '   ', '   ', '  ', 'I', 'L', 'Yutanduchi Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mad', 'mad', 'mad', '  ', 'I', 'L', 'Madurese', false, '');
INSERT INTO languages_iso639 VALUES ('mae', '   ', '   ', '  ', 'I', 'L', 'Bo-Rukul', false, '');
INSERT INTO languages_iso639 VALUES ('maf', '   ', '   ', '  ', 'I', 'L', 'Mafa', false, '');
INSERT INTO languages_iso639 VALUES ('mag', 'mag', 'mag', '  ', 'I', 'L', 'Magahi', false, '');
INSERT INTO languages_iso639 VALUES ('mah', 'mah', 'mah', 'mh', 'I', 'L', 'Marshallese', false, '');
INSERT INTO languages_iso639 VALUES ('mai', 'mai', 'mai', '  ', 'I', 'L', 'Maithili', false, '');
INSERT INTO languages_iso639 VALUES ('maj', '   ', '   ', '  ', 'I', 'L', 'Jalapa De Díaz Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('mak', 'mak', 'mak', '  ', 'I', 'L', 'Makasar', false, '');
INSERT INTO languages_iso639 VALUES ('mal', 'mal', 'mal', 'ml', 'I', 'L', 'Malayalam', false, '');
INSERT INTO languages_iso639 VALUES ('mam', '   ', '   ', '  ', 'I', 'L', 'Mam', false, '');
INSERT INTO languages_iso639 VALUES ('man', 'man', 'man', '  ', 'M', 'L', 'Mandingo', false, '');
INSERT INTO languages_iso639 VALUES ('maq', '   ', '   ', '  ', 'I', 'L', 'Chiquihuitlán Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('mar', 'mar', 'mar', 'mr', 'I', 'L', 'Marathi', false, '');
INSERT INTO languages_iso639 VALUES ('mas', 'mas', 'mas', '  ', 'I', 'L', 'Masai', false, '');
INSERT INTO languages_iso639 VALUES ('mat', '   ', '   ', '  ', 'I', 'L', 'San Francisco Matlatzinca', false, '');
INSERT INTO languages_iso639 VALUES ('mau', '   ', '   ', '  ', 'I', 'L', 'Huautla Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('mav', '   ', '   ', '  ', 'I', 'L', 'Sateré-Mawé', false, '');
INSERT INTO languages_iso639 VALUES ('maw', '   ', '   ', '  ', 'I', 'L', 'Mampruli', false, '');
INSERT INTO languages_iso639 VALUES ('max', '   ', '   ', '  ', 'I', 'L', 'North Moluccan Malay', false, '');
INSERT INTO languages_iso639 VALUES ('maz', '   ', '   ', '  ', 'I', 'L', 'Central Mazahua', false, '');
INSERT INTO languages_iso639 VALUES ('mba', '   ', '   ', '  ', 'I', 'L', 'Higaonon', false, '');
INSERT INTO languages_iso639 VALUES ('mbb', '   ', '   ', '  ', 'I', 'L', 'Western Bukidnon Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mbc', '   ', '   ', '  ', 'I', 'L', 'Macushi', false, '');
INSERT INTO languages_iso639 VALUES ('mbd', '   ', '   ', '  ', 'I', 'L', 'Dibabawon Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mbe', '   ', '   ', '  ', 'I', 'E', 'Molale', false, '');
INSERT INTO languages_iso639 VALUES ('mbf', '   ', '   ', '  ', 'I', 'L', 'Baba Malay', false, '');
INSERT INTO languages_iso639 VALUES ('mbh', '   ', '   ', '  ', 'I', 'L', 'Mangseng', false, '');
INSERT INTO languages_iso639 VALUES ('mbi', '   ', '   ', '  ', 'I', 'L', 'Ilianen Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mbj', '   ', '   ', '  ', 'I', 'L', 'Nadëb', false, '');
INSERT INTO languages_iso639 VALUES ('mbk', '   ', '   ', '  ', 'I', 'L', 'Malol', false, '');
INSERT INTO languages_iso639 VALUES ('mbl', '   ', '   ', '  ', 'I', 'L', 'Maxakalí', false, '');
INSERT INTO languages_iso639 VALUES ('mbm', '   ', '   ', '  ', 'I', 'L', 'Ombamba', false, '');
INSERT INTO languages_iso639 VALUES ('mbn', '   ', '   ', '  ', 'I', 'L', 'Macaguán', false, '');
INSERT INTO languages_iso639 VALUES ('mbo', '   ', '   ', '  ', 'I', 'L', 'Mbo (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('mbp', '   ', '   ', '  ', 'I', 'L', 'Malayo', false, '');
INSERT INTO languages_iso639 VALUES ('mbq', '   ', '   ', '  ', 'I', 'L', 'Maisin', false, '');
INSERT INTO languages_iso639 VALUES ('mbr', '   ', '   ', '  ', 'I', 'L', 'Nukak Makú', false, '');
INSERT INTO languages_iso639 VALUES ('mbs', '   ', '   ', '  ', 'I', 'L', 'Sarangani Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mbt', '   ', '   ', '  ', 'I', 'L', 'Matigsalug Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mbu', '   ', '   ', '  ', 'I', 'L', 'Mbula-Bwazza', false, '');
INSERT INTO languages_iso639 VALUES ('mbv', '   ', '   ', '  ', 'I', 'L', 'Mbulungish', false, '');
INSERT INTO languages_iso639 VALUES ('mbw', '   ', '   ', '  ', 'I', 'L', 'Maring', false, '');
INSERT INTO languages_iso639 VALUES ('mbx', '   ', '   ', '  ', 'I', 'L', 'Mari (East Sepik Province)', false, '');
INSERT INTO languages_iso639 VALUES ('mby', '   ', '   ', '  ', 'I', 'L', 'Memoni', false, '');
INSERT INTO languages_iso639 VALUES ('mbz', '   ', '   ', '  ', 'I', 'L', 'Amoltepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mca', '   ', '   ', '  ', 'I', 'L', 'Maca', false, '');
INSERT INTO languages_iso639 VALUES ('mcb', '   ', '   ', '  ', 'I', 'L', 'Machiguenga', false, '');
INSERT INTO languages_iso639 VALUES ('mcc', '   ', '   ', '  ', 'I', 'L', 'Bitur', false, '');
INSERT INTO languages_iso639 VALUES ('mcd', '   ', '   ', '  ', 'I', 'L', 'Sharanahua', false, '');
INSERT INTO languages_iso639 VALUES ('mce', '   ', '   ', '  ', 'I', 'L', 'Itundujia Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mcf', '   ', '   ', '  ', 'I', 'L', 'Matsés', false, '');
INSERT INTO languages_iso639 VALUES ('mcg', '   ', '   ', '  ', 'I', 'L', 'Mapoyo', false, '');
INSERT INTO languages_iso639 VALUES ('mch', '   ', '   ', '  ', 'I', 'L', 'Maquiritari', false, '');
INSERT INTO languages_iso639 VALUES ('mci', '   ', '   ', '  ', 'I', 'L', 'Mese', false, '');
INSERT INTO languages_iso639 VALUES ('mcj', '   ', '   ', '  ', 'I', 'L', 'Mvanip', false, '');
INSERT INTO languages_iso639 VALUES ('mck', '   ', '   ', '  ', 'I', 'L', 'Mbunda', false, '');
INSERT INTO languages_iso639 VALUES ('mcl', '   ', '   ', '  ', 'I', 'E', 'Macaguaje', false, '');
INSERT INTO languages_iso639 VALUES ('mcm', '   ', '   ', '  ', 'I', 'L', 'Malaccan Creole Portuguese', false, '');
INSERT INTO languages_iso639 VALUES ('mcn', '   ', '   ', '  ', 'I', 'L', 'Masana', false, '');
INSERT INTO languages_iso639 VALUES ('mco', '   ', '   ', '  ', 'I', 'L', 'Coatlán Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('mcp', '   ', '   ', '  ', 'I', 'L', 'Makaa', false, '');
INSERT INTO languages_iso639 VALUES ('mcq', '   ', '   ', '  ', 'I', 'L', 'Ese', false, '');
INSERT INTO languages_iso639 VALUES ('mcr', '   ', '   ', '  ', 'I', 'L', 'Menya', false, '');
INSERT INTO languages_iso639 VALUES ('mcs', '   ', '   ', '  ', 'I', 'L', 'Mambai', false, '');
INSERT INTO languages_iso639 VALUES ('mct', '   ', '   ', '  ', 'I', 'L', 'Mengisa', false, '');
INSERT INTO languages_iso639 VALUES ('mcu', '   ', '   ', '  ', 'I', 'L', 'Cameroon Mambila', false, '');
INSERT INTO languages_iso639 VALUES ('mcv', '   ', '   ', '  ', 'I', 'L', 'Minanibai', false, '');
INSERT INTO languages_iso639 VALUES ('mcw', '   ', '   ', '  ', 'I', 'L', 'Mawa (Chad)', false, '');
INSERT INTO languages_iso639 VALUES ('mcx', '   ', '   ', '  ', 'I', 'L', 'Mpiemo', false, '');
INSERT INTO languages_iso639 VALUES ('mcy', '   ', '   ', '  ', 'I', 'L', 'South Watut', false, '');
INSERT INTO languages_iso639 VALUES ('mcz', '   ', '   ', '  ', 'I', 'L', 'Mawan', false, '');
INSERT INTO languages_iso639 VALUES ('mda', '   ', '   ', '  ', 'I', 'L', 'Mada (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('mdb', '   ', '   ', '  ', 'I', 'L', 'Morigi', false, '');
INSERT INTO languages_iso639 VALUES ('mdc', '   ', '   ', '  ', 'I', 'L', 'Male (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('mdd', '   ', '   ', '  ', 'I', 'L', 'Mbum', false, '');
INSERT INTO languages_iso639 VALUES ('mde', '   ', '   ', '  ', 'I', 'L', 'Maba (Chad)', false, '');
INSERT INTO languages_iso639 VALUES ('mdf', 'mdf', 'mdf', '  ', 'I', 'L', 'Moksha', false, '');
INSERT INTO languages_iso639 VALUES ('mdg', '   ', '   ', '  ', 'I', 'L', 'Massalat', false, '');
INSERT INTO languages_iso639 VALUES ('mdh', '   ', '   ', '  ', 'I', 'L', 'Maguindanaon', false, '');
INSERT INTO languages_iso639 VALUES ('mdi', '   ', '   ', '  ', 'I', 'L', 'Mamvu', false, '');
INSERT INTO languages_iso639 VALUES ('mdj', '   ', '   ', '  ', 'I', 'L', 'Mangbetu', false, '');
INSERT INTO languages_iso639 VALUES ('mdk', '   ', '   ', '  ', 'I', 'L', 'Mangbutu', false, '');
INSERT INTO languages_iso639 VALUES ('mdl', '   ', '   ', '  ', 'I', 'L', 'Maltese Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mdm', '   ', '   ', '  ', 'I', 'L', 'Mayogo', false, '');
INSERT INTO languages_iso639 VALUES ('mdn', '   ', '   ', '  ', 'I', 'L', 'Mbati', false, '');
INSERT INTO languages_iso639 VALUES ('mdp', '   ', '   ', '  ', 'I', 'L', 'Mbala', false, '');
INSERT INTO languages_iso639 VALUES ('mdq', '   ', '   ', '  ', 'I', 'L', 'Mbole', false, '');
INSERT INTO languages_iso639 VALUES ('mdr', 'mdr', 'mdr', '  ', 'I', 'L', 'Mandar', false, '');
INSERT INTO languages_iso639 VALUES ('mds', '   ', '   ', '  ', 'I', 'L', 'Maria (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('mdt', '   ', '   ', '  ', 'I', 'L', 'Mbere', false, '');
INSERT INTO languages_iso639 VALUES ('mdu', '   ', '   ', '  ', 'I', 'L', 'Mboko', false, '');
INSERT INTO languages_iso639 VALUES ('mdv', '   ', '   ', '  ', 'I', 'L', 'Santa Lucía Monteverde Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mdw', '   ', '   ', '  ', 'I', 'L', 'Mbosi', false, '');
INSERT INTO languages_iso639 VALUES ('mdx', '   ', '   ', '  ', 'I', 'L', 'Dizin', false, '');
INSERT INTO languages_iso639 VALUES ('mdy', '   ', '   ', '  ', 'I', 'L', 'Male (Ethiopia)', false, '');
INSERT INTO languages_iso639 VALUES ('mdz', '   ', '   ', '  ', 'I', 'L', 'Suruí Do Pará', false, '');
INSERT INTO languages_iso639 VALUES ('mea', '   ', '   ', '  ', 'I', 'L', 'Menka', false, '');
INSERT INTO languages_iso639 VALUES ('meb', '   ', '   ', '  ', 'I', 'L', 'Ikobi', false, '');
INSERT INTO languages_iso639 VALUES ('mec', '   ', '   ', '  ', 'I', 'L', 'Mara', false, '');
INSERT INTO languages_iso639 VALUES ('med', '   ', '   ', '  ', 'I', 'L', 'Melpa', false, '');
INSERT INTO languages_iso639 VALUES ('mee', '   ', '   ', '  ', 'I', 'L', 'Mengen', false, '');
INSERT INTO languages_iso639 VALUES ('mef', '   ', '   ', '  ', 'I', 'L', 'Megam', false, '');
INSERT INTO languages_iso639 VALUES ('meh', '   ', '   ', '  ', 'I', 'L', 'Southwestern Tlaxiaco Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mei', '   ', '   ', '  ', 'I', 'L', 'Midob', false, '');
INSERT INTO languages_iso639 VALUES ('mej', '   ', '   ', '  ', 'I', 'L', 'Meyah', false, '');
INSERT INTO languages_iso639 VALUES ('mek', '   ', '   ', '  ', 'I', 'L', 'Mekeo', false, '');
INSERT INTO languages_iso639 VALUES ('mel', '   ', '   ', '  ', 'I', 'L', 'Central Melanau', false, '');
INSERT INTO languages_iso639 VALUES ('mem', '   ', '   ', '  ', 'I', 'E', 'Mangala', false, '');
INSERT INTO languages_iso639 VALUES ('men', 'men', 'men', '  ', 'I', 'L', 'Mende (Sierra Leone)', false, '');
INSERT INTO languages_iso639 VALUES ('meo', '   ', '   ', '  ', 'I', 'L', 'Kedah Malay', false, '');
INSERT INTO languages_iso639 VALUES ('mep', '   ', '   ', '  ', 'I', 'L', 'Miriwung', false, '');
INSERT INTO languages_iso639 VALUES ('meq', '   ', '   ', '  ', 'I', 'L', 'Merey', false, '');
INSERT INTO languages_iso639 VALUES ('mer', '   ', '   ', '  ', 'I', 'L', 'Meru', false, '');
INSERT INTO languages_iso639 VALUES ('mes', '   ', '   ', '  ', 'I', 'L', 'Masmaje', false, '');
INSERT INTO languages_iso639 VALUES ('met', '   ', '   ', '  ', 'I', 'L', 'Mato', false, '');
INSERT INTO languages_iso639 VALUES ('meu', '   ', '   ', '  ', 'I', 'L', 'Motu', false, '');
INSERT INTO languages_iso639 VALUES ('mev', '   ', '   ', '  ', 'I', 'L', 'Mano', false, '');
INSERT INTO languages_iso639 VALUES ('mew', '   ', '   ', '  ', 'I', 'L', 'Maaka', false, '');
INSERT INTO languages_iso639 VALUES ('mey', '   ', '   ', '  ', 'I', 'L', 'Hassaniyya', false, '');
INSERT INTO languages_iso639 VALUES ('mez', '   ', '   ', '  ', 'I', 'L', 'Menominee', false, '');
INSERT INTO languages_iso639 VALUES ('mfa', '   ', '   ', '  ', 'I', 'L', 'Pattani Malay', false, '');
INSERT INTO languages_iso639 VALUES ('mfb', '   ', '   ', '  ', 'I', 'L', 'Bangka', false, '');
INSERT INTO languages_iso639 VALUES ('mfc', '   ', '   ', '  ', 'I', 'L', 'Mba', false, '');
INSERT INTO languages_iso639 VALUES ('mfd', '   ', '   ', '  ', 'I', 'L', 'Mendankwe-Nkwen', false, '');
INSERT INTO languages_iso639 VALUES ('mfe', '   ', '   ', '  ', 'I', 'L', 'Morisyen', false, '');
INSERT INTO languages_iso639 VALUES ('mff', '   ', '   ', '  ', 'I', 'L', 'Naki', false, '');
INSERT INTO languages_iso639 VALUES ('mfg', '   ', '   ', '  ', 'I', 'L', 'Mogofin', false, '');
INSERT INTO languages_iso639 VALUES ('mfh', '   ', '   ', '  ', 'I', 'L', 'Matal', false, '');
INSERT INTO languages_iso639 VALUES ('mfi', '   ', '   ', '  ', 'I', 'L', 'Wandala', false, '');
INSERT INTO languages_iso639 VALUES ('mfj', '   ', '   ', '  ', 'I', 'L', 'Mefele', false, '');
INSERT INTO languages_iso639 VALUES ('mfk', '   ', '   ', '  ', 'I', 'L', 'North Mofu', false, '');
INSERT INTO languages_iso639 VALUES ('mfl', '   ', '   ', '  ', 'I', 'L', 'Putai', false, '');
INSERT INTO languages_iso639 VALUES ('mfm', '   ', '   ', '  ', 'I', 'L', 'Marghi South', false, '');
INSERT INTO languages_iso639 VALUES ('mfn', '   ', '   ', '  ', 'I', 'L', 'Cross River Mbembe', false, '');
INSERT INTO languages_iso639 VALUES ('mfo', '   ', '   ', '  ', 'I', 'L', 'Mbe', false, '');
INSERT INTO languages_iso639 VALUES ('mfp', '   ', '   ', '  ', 'I', 'L', 'Makassar Malay', false, '');
INSERT INTO languages_iso639 VALUES ('mfq', '   ', '   ', '  ', 'I', 'L', 'Moba', false, '');
INSERT INTO languages_iso639 VALUES ('mfr', '   ', '   ', '  ', 'I', 'L', 'Marithiel', false, '');
INSERT INTO languages_iso639 VALUES ('mfs', '   ', '   ', '  ', 'I', 'L', 'Mexican Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mft', '   ', '   ', '  ', 'I', 'L', 'Mokerang', false, '');
INSERT INTO languages_iso639 VALUES ('mfu', '   ', '   ', '  ', 'I', 'L', 'Mbwela', false, '');
INSERT INTO languages_iso639 VALUES ('mfv', '   ', '   ', '  ', 'I', 'L', 'Mandjak', false, '');
INSERT INTO languages_iso639 VALUES ('mfw', '   ', '   ', '  ', 'I', 'E', 'Mulaha', false, '');
INSERT INTO languages_iso639 VALUES ('mfx', '   ', '   ', '  ', 'I', 'L', 'Melo', false, '');
INSERT INTO languages_iso639 VALUES ('mfy', '   ', '   ', '  ', 'I', 'L', 'Mayo', false, '');
INSERT INTO languages_iso639 VALUES ('mfz', '   ', '   ', '  ', 'I', 'L', 'Mabaan', false, '');
INSERT INTO languages_iso639 VALUES ('mga', 'mga', 'mga', '  ', 'I', 'H', 'Middle Irish (900-1200)', false, '');
INSERT INTO languages_iso639 VALUES ('mgb', '   ', '   ', '  ', 'I', 'L', 'Mararit', false, '');
INSERT INTO languages_iso639 VALUES ('mgc', '   ', '   ', '  ', 'I', 'L', 'Morokodo', false, '');
INSERT INTO languages_iso639 VALUES ('mgd', '   ', '   ', '  ', 'I', 'L', 'Moru', false, '');
INSERT INTO languages_iso639 VALUES ('mge', '   ', '   ', '  ', 'I', 'L', 'Mango', false, '');
INSERT INTO languages_iso639 VALUES ('mgf', '   ', '   ', '  ', 'I', 'L', 'Maklew', false, '');
INSERT INTO languages_iso639 VALUES ('mgg', '   ', '   ', '  ', 'I', 'L', 'Mpumpong', false, '');
INSERT INTO languages_iso639 VALUES ('mgh', '   ', '   ', '  ', 'I', 'L', 'Makhuwa-Meetto', false, '');
INSERT INTO languages_iso639 VALUES ('mgi', '   ', '   ', '  ', 'I', 'L', 'Lijili', false, '');
INSERT INTO languages_iso639 VALUES ('mgj', '   ', '   ', '  ', 'I', 'L', 'Abureni', false, '');
INSERT INTO languages_iso639 VALUES ('mgk', '   ', '   ', '  ', 'I', 'L', 'Mawes', false, '');
INSERT INTO languages_iso639 VALUES ('mgl', '   ', '   ', '  ', 'I', 'L', 'Maleu-Kilenge', false, '');
INSERT INTO languages_iso639 VALUES ('mgm', '   ', '   ', '  ', 'I', 'L', 'Mambae', false, '');
INSERT INTO languages_iso639 VALUES ('mgn', '   ', '   ', '  ', 'I', 'L', 'Mbangi', false, '');
INSERT INTO languages_iso639 VALUES ('mgo', '   ', '   ', '  ', 'I', 'L', 'Meta''', false, '');
INSERT INTO languages_iso639 VALUES ('mgp', '   ', '   ', '  ', 'I', 'L', 'Eastern Magar', false, '');
INSERT INTO languages_iso639 VALUES ('mgq', '   ', '   ', '  ', 'I', 'L', 'Malila', false, '');
INSERT INTO languages_iso639 VALUES ('mgr', '   ', '   ', '  ', 'I', 'L', 'Mambwe-Lungu', false, '');
INSERT INTO languages_iso639 VALUES ('mgs', '   ', '   ', '  ', 'I', 'L', 'Manda (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('mgt', '   ', '   ', '  ', 'I', 'L', 'Mongol', false, '');
INSERT INTO languages_iso639 VALUES ('mgu', '   ', '   ', '  ', 'I', 'L', 'Mailu', false, '');
INSERT INTO languages_iso639 VALUES ('mgv', '   ', '   ', '  ', 'I', 'L', 'Matengo', false, '');
INSERT INTO languages_iso639 VALUES ('mgw', '   ', '   ', '  ', 'I', 'L', 'Matumbi', false, '');
INSERT INTO languages_iso639 VALUES ('mgy', '   ', '   ', '  ', 'I', 'L', 'Mbunga', false, '');
INSERT INTO languages_iso639 VALUES ('mgz', '   ', '   ', '  ', 'I', 'L', 'Mbugwe', false, '');
INSERT INTO languages_iso639 VALUES ('mha', '   ', '   ', '  ', 'I', 'L', 'Manda (India)', false, '');
INSERT INTO languages_iso639 VALUES ('mhb', '   ', '   ', '  ', 'I', 'L', 'Mahongwe', false, '');
INSERT INTO languages_iso639 VALUES ('mhc', '   ', '   ', '  ', 'I', 'L', 'Mocho', false, '');
INSERT INTO languages_iso639 VALUES ('mhd', '   ', '   ', '  ', 'I', 'L', 'Mbugu', false, '');
INSERT INTO languages_iso639 VALUES ('mhe', '   ', '   ', '  ', 'I', 'L', 'Besisi', false, '');
INSERT INTO languages_iso639 VALUES ('mhf', '   ', '   ', '  ', 'I', 'L', 'Mamaa', false, '');
INSERT INTO languages_iso639 VALUES ('mhg', '   ', '   ', '  ', 'I', 'L', 'Margu', false, '');
INSERT INTO languages_iso639 VALUES ('mhh', '   ', '   ', '  ', 'I', 'L', 'Maskoy Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('mhi', '   ', '   ', '  ', 'I', 'L', 'Ma''di', false, '');
INSERT INTO languages_iso639 VALUES ('mhj', '   ', '   ', '  ', 'I', 'L', 'Mogholi', false, '');
INSERT INTO languages_iso639 VALUES ('mhk', '   ', '   ', '  ', 'I', 'L', 'Mungaka', false, '');
INSERT INTO languages_iso639 VALUES ('mhl', '   ', '   ', '  ', 'I', 'L', 'Mauwake', false, '');
INSERT INTO languages_iso639 VALUES ('mhm', '   ', '   ', '  ', 'I', 'L', 'Makhuwa-Moniga', false, '');
INSERT INTO languages_iso639 VALUES ('mhn', '   ', '   ', '  ', 'I', 'L', 'Mócheno', false, '');
INSERT INTO languages_iso639 VALUES ('mho', '   ', '   ', '  ', 'I', 'L', 'Mashi (Zambia)', false, '');
INSERT INTO languages_iso639 VALUES ('mhp', '   ', '   ', '  ', 'I', 'L', 'Balinese Malay', false, '');
INSERT INTO languages_iso639 VALUES ('mhq', '   ', '   ', '  ', 'I', 'L', 'Mandan', false, '');
INSERT INTO languages_iso639 VALUES ('mhr', '   ', '   ', '  ', 'I', 'L', 'Eastern Mari', false, '');
INSERT INTO languages_iso639 VALUES ('mhs', '   ', '   ', '  ', 'I', 'L', 'Buru (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('mht', '   ', '   ', '  ', 'I', 'L', 'Mandahuaca', false, '');
INSERT INTO languages_iso639 VALUES ('mhu', '   ', '   ', '  ', 'I', 'L', 'Digaro-Mishmi', false, '');
INSERT INTO languages_iso639 VALUES ('mhw', '   ', '   ', '  ', 'I', 'L', 'Mbukushu', false, '');
INSERT INTO languages_iso639 VALUES ('mhx', '   ', '   ', '  ', 'I', 'L', 'Maru', false, '');
INSERT INTO languages_iso639 VALUES ('mhy', '   ', '   ', '  ', 'I', 'L', 'Ma''anyan', false, '');
INSERT INTO languages_iso639 VALUES ('mhz', '   ', '   ', '  ', 'I', 'L', 'Mor (Mor Islands)', false, '');
INSERT INTO languages_iso639 VALUES ('mia', '   ', '   ', '  ', 'I', 'L', 'Miami', false, '');
INSERT INTO languages_iso639 VALUES ('mib', '   ', '   ', '  ', 'I', 'L', 'Atatláhuca Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mic', 'mic', 'mic', '  ', 'I', 'L', 'Mi''kmaq', false, '');
INSERT INTO languages_iso639 VALUES ('mid', '   ', '   ', '  ', 'I', 'L', 'Mandaic', false, '');
INSERT INTO languages_iso639 VALUES ('mie', '   ', '   ', '  ', 'I', 'L', 'Ocotepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mif', '   ', '   ', '  ', 'I', 'L', 'Mofu-Gudur', false, '');
INSERT INTO languages_iso639 VALUES ('mig', '   ', '   ', '  ', 'I', 'L', 'San Miguel El Grande Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mih', '   ', '   ', '  ', 'I', 'L', 'Chayuco Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mii', '   ', '   ', '  ', 'I', 'L', 'Chigmecatitlán Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mij', '   ', '   ', '  ', 'I', 'L', 'Abar', false, '');
INSERT INTO languages_iso639 VALUES ('mik', '   ', '   ', '  ', 'I', 'L', 'Mikasuki', false, '');
INSERT INTO languages_iso639 VALUES ('mil', '   ', '   ', '  ', 'I', 'L', 'Peñoles Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mim', '   ', '   ', '  ', 'I', 'L', 'Alacatlatzala Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('min', 'min', 'min', '  ', 'I', 'L', 'Minangkabau', false, '');
INSERT INTO languages_iso639 VALUES ('mio', '   ', '   ', '  ', 'I', 'L', 'Pinotepa Nacional Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mip', '   ', '   ', '  ', 'I', 'L', 'Apasco-Apoala Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('miq', '   ', '   ', '  ', 'I', 'L', 'Mískito', false, '');
INSERT INTO languages_iso639 VALUES ('mir', '   ', '   ', '  ', 'I', 'L', 'Isthmus Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('mis', 'mis', 'mis', '  ', 'S', 'S', 'Uncoded languages', false, '');
INSERT INTO languages_iso639 VALUES ('mit', '   ', '   ', '  ', 'I', 'L', 'Southern Puebla Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('miu', '   ', '   ', '  ', 'I', 'L', 'Cacaloxtepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('miw', '   ', '   ', '  ', 'I', 'L', 'Akoye', false, '');
INSERT INTO languages_iso639 VALUES ('mix', '   ', '   ', '  ', 'I', 'L', 'Mixtepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('miy', '   ', '   ', '  ', 'I', 'L', 'Ayutla Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('miz', '   ', '   ', '  ', 'I', 'L', 'Coatzospan Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mjc', '   ', '   ', '  ', 'I', 'L', 'San Juan Colorado Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mjd', '   ', '   ', '  ', 'I', 'L', 'Northwest Maidu', false, '');
INSERT INTO languages_iso639 VALUES ('mje', '   ', '   ', '  ', 'I', 'E', 'Muskum', false, '');
INSERT INTO languages_iso639 VALUES ('mjg', '   ', '   ', '  ', 'I', 'L', 'Tu', false, '');
INSERT INTO languages_iso639 VALUES ('mjh', '   ', '   ', '  ', 'I', 'L', 'Mwera (Nyasa)', false, '');
INSERT INTO languages_iso639 VALUES ('mji', '   ', '   ', '  ', 'I', 'L', 'Kim Mun', false, '');
INSERT INTO languages_iso639 VALUES ('mjj', '   ', '   ', '  ', 'I', 'L', 'Mawak', false, '');
INSERT INTO languages_iso639 VALUES ('mjk', '   ', '   ', '  ', 'I', 'L', 'Matukar', false, '');
INSERT INTO languages_iso639 VALUES ('mjl', '   ', '   ', '  ', 'I', 'L', 'Mandeali', false, '');
INSERT INTO languages_iso639 VALUES ('mjm', '   ', '   ', '  ', 'I', 'L', 'Medebur', false, '');
INSERT INTO languages_iso639 VALUES ('mjn', '   ', '   ', '  ', 'I', 'L', 'Ma (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('mjo', '   ', '   ', '  ', 'I', 'L', 'Malankuravan', false, '');
INSERT INTO languages_iso639 VALUES ('mjp', '   ', '   ', '  ', 'I', 'L', 'Malapandaram', false, '');
INSERT INTO languages_iso639 VALUES ('mjq', '   ', '   ', '  ', 'I', 'E', 'Malaryan', false, '');
INSERT INTO languages_iso639 VALUES ('mjr', '   ', '   ', '  ', 'I', 'L', 'Malavedan', false, '');
INSERT INTO languages_iso639 VALUES ('mjs', '   ', '   ', '  ', 'I', 'L', 'Miship', false, '');
INSERT INTO languages_iso639 VALUES ('mjt', '   ', '   ', '  ', 'I', 'L', 'Sauria Paharia', false, '');
INSERT INTO languages_iso639 VALUES ('mju', '   ', '   ', '  ', 'I', 'L', 'Manna-Dora', false, '');
INSERT INTO languages_iso639 VALUES ('mjv', '   ', '   ', '  ', 'I', 'L', 'Mannan', false, '');
INSERT INTO languages_iso639 VALUES ('mjw', '   ', '   ', '  ', 'I', 'L', 'Karbi', false, '');
INSERT INTO languages_iso639 VALUES ('mjx', '   ', '   ', '  ', 'I', 'L', 'Mahali', false, '');
INSERT INTO languages_iso639 VALUES ('mjy', '   ', '   ', '  ', 'I', 'E', 'Mahican', false, '');
INSERT INTO languages_iso639 VALUES ('mjz', '   ', '   ', '  ', 'I', 'L', 'Majhi', false, '');
INSERT INTO languages_iso639 VALUES ('mka', '   ', '   ', '  ', 'I', 'L', 'Mbre', false, '');
INSERT INTO languages_iso639 VALUES ('mkb', '   ', '   ', '  ', 'I', 'L', 'Mal Paharia', false, '');
INSERT INTO languages_iso639 VALUES ('mkc', '   ', '   ', '  ', 'I', 'L', 'Siliput', false, '');
INSERT INTO languages_iso639 VALUES ('mkd', 'mac', 'mkd', 'mk', 'I', 'L', 'Macedonian', false, '');
INSERT INTO languages_iso639 VALUES ('mke', '   ', '   ', '  ', 'I', 'L', 'Mawchi', false, '');
INSERT INTO languages_iso639 VALUES ('mkf', '   ', '   ', '  ', 'I', 'L', 'Miya', false, '');
INSERT INTO languages_iso639 VALUES ('mkg', '   ', '   ', '  ', 'I', 'L', 'Mak (China)', false, '');
INSERT INTO languages_iso639 VALUES ('mki', '   ', '   ', '  ', 'I', 'L', 'Dhatki', false, '');
INSERT INTO languages_iso639 VALUES ('mkj', '   ', '   ', '  ', 'I', 'L', 'Mokilese', false, '');
INSERT INTO languages_iso639 VALUES ('mkk', '   ', '   ', '  ', 'I', 'L', 'Byep', false, '');
INSERT INTO languages_iso639 VALUES ('mkl', '   ', '   ', '  ', 'I', 'L', 'Mokole', false, '');
INSERT INTO languages_iso639 VALUES ('mkm', '   ', '   ', '  ', 'I', 'L', 'Moklen', false, '');
INSERT INTO languages_iso639 VALUES ('mkn', '   ', '   ', '  ', 'I', 'L', 'Kupang Malay', false, '');
INSERT INTO languages_iso639 VALUES ('mko', '   ', '   ', '  ', 'I', 'L', 'Mingang Doso', false, '');
INSERT INTO languages_iso639 VALUES ('mkp', '   ', '   ', '  ', 'I', 'L', 'Moikodi', false, '');
INSERT INTO languages_iso639 VALUES ('mkq', '   ', '   ', '  ', 'I', 'E', 'Bay Miwok', false, '');
INSERT INTO languages_iso639 VALUES ('mkr', '   ', '   ', '  ', 'I', 'L', 'Malas', false, '');
INSERT INTO languages_iso639 VALUES ('mks', '   ', '   ', '  ', 'I', 'L', 'Silacayoapan Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mkt', '   ', '   ', '  ', 'I', 'L', 'Vamale', false, '');
INSERT INTO languages_iso639 VALUES ('mku', '   ', '   ', '  ', 'I', 'L', 'Konyanka Maninka', false, '');
INSERT INTO languages_iso639 VALUES ('mkv', '   ', '   ', '  ', 'I', 'L', 'Mafea', false, '');
INSERT INTO languages_iso639 VALUES ('mkw', '   ', '   ', '  ', 'I', 'L', 'Kituba (Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('mkx', '   ', '   ', '  ', 'I', 'L', 'Kinamiging Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mky', '   ', '   ', '  ', 'I', 'L', 'East Makian', false, '');
INSERT INTO languages_iso639 VALUES ('mkz', '   ', '   ', '  ', 'I', 'L', 'Makasae', false, '');
INSERT INTO languages_iso639 VALUES ('mla', '   ', '   ', '  ', 'I', 'L', 'Malo', false, '');
INSERT INTO languages_iso639 VALUES ('mlb', '   ', '   ', '  ', 'I', 'L', 'Mbule', false, '');
INSERT INTO languages_iso639 VALUES ('mlc', '   ', '   ', '  ', 'I', 'L', 'Cao Lan', false, '');
INSERT INTO languages_iso639 VALUES ('mle', '   ', '   ', '  ', 'I', 'L', 'Manambu', false, '');
INSERT INTO languages_iso639 VALUES ('mlf', '   ', '   ', '  ', 'I', 'L', 'Mal', false, '');
INSERT INTO languages_iso639 VALUES ('mlg', 'mlg', 'mlg', 'mg', 'M', 'L', 'Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('mlh', '   ', '   ', '  ', 'I', 'L', 'Mape', false, '');
INSERT INTO languages_iso639 VALUES ('mli', '   ', '   ', '  ', 'I', 'L', 'Malimpung', false, '');
INSERT INTO languages_iso639 VALUES ('mlj', '   ', '   ', '  ', 'I', 'L', 'Miltu', false, '');
INSERT INTO languages_iso639 VALUES ('mlk', '   ', '   ', '  ', 'I', 'L', 'Ilwana', false, '');
INSERT INTO languages_iso639 VALUES ('mll', '   ', '   ', '  ', 'I', 'L', 'Malua Bay', false, '');
INSERT INTO languages_iso639 VALUES ('mlm', '   ', '   ', '  ', 'I', 'L', 'Mulam', false, '');
INSERT INTO languages_iso639 VALUES ('mln', '   ', '   ', '  ', 'I', 'L', 'Malango', false, '');
INSERT INTO languages_iso639 VALUES ('mlo', '   ', '   ', '  ', 'I', 'L', 'Mlomp', false, '');
INSERT INTO languages_iso639 VALUES ('mlp', '   ', '   ', '  ', 'I', 'L', 'Bargam', false, '');
INSERT INTO languages_iso639 VALUES ('mlq', '   ', '   ', '  ', 'I', 'L', 'Western Maninkakan', false, '');
INSERT INTO languages_iso639 VALUES ('mlr', '   ', '   ', '  ', 'I', 'L', 'Vame', false, '');
INSERT INTO languages_iso639 VALUES ('mls', '   ', '   ', '  ', 'I', 'L', 'Masalit', false, '');
INSERT INTO languages_iso639 VALUES ('mlt', 'mlt', 'mlt', 'mt', 'I', 'L', 'Maltese', false, '');
INSERT INTO languages_iso639 VALUES ('mlu', '   ', '   ', '  ', 'I', 'L', 'To''abaita', false, '');
INSERT INTO languages_iso639 VALUES ('mlv', '   ', '   ', '  ', 'I', 'L', 'Motlav', false, '');
INSERT INTO languages_iso639 VALUES ('mlw', '   ', '   ', '  ', 'I', 'L', 'Moloko', false, '');
INSERT INTO languages_iso639 VALUES ('mlx', '   ', '   ', '  ', 'I', 'L', 'Malfaxal', false, '');
INSERT INTO languages_iso639 VALUES ('mlz', '   ', '   ', '  ', 'I', 'L', 'Malaynon', false, '');
INSERT INTO languages_iso639 VALUES ('mma', '   ', '   ', '  ', 'I', 'L', 'Mama', false, '');
INSERT INTO languages_iso639 VALUES ('mmb', '   ', '   ', '  ', 'I', 'L', 'Momina', false, '');
INSERT INTO languages_iso639 VALUES ('mmc', '   ', '   ', '  ', 'I', 'L', 'Michoacán Mazahua', false, '');
INSERT INTO languages_iso639 VALUES ('mmd', '   ', '   ', '  ', 'I', 'L', 'Maonan', false, '');
INSERT INTO languages_iso639 VALUES ('mme', '   ', '   ', '  ', 'I', 'L', 'Mae', false, '');
INSERT INTO languages_iso639 VALUES ('mmf', '   ', '   ', '  ', 'I', 'L', 'Mundat', false, '');
INSERT INTO languages_iso639 VALUES ('mmg', '   ', '   ', '  ', 'I', 'L', 'North Ambrym', false, '');
INSERT INTO languages_iso639 VALUES ('mmh', '   ', '   ', '  ', 'I', 'L', 'Mehináku', false, '');
INSERT INTO languages_iso639 VALUES ('mmi', '   ', '   ', '  ', 'I', 'L', 'Musar', false, '');
INSERT INTO languages_iso639 VALUES ('mmj', '   ', '   ', '  ', 'I', 'L', 'Majhwar', false, '');
INSERT INTO languages_iso639 VALUES ('mmk', '   ', '   ', '  ', 'I', 'L', 'Mukha-Dora', false, '');
INSERT INTO languages_iso639 VALUES ('mml', '   ', '   ', '  ', 'I', 'L', 'Man Met', false, '');
INSERT INTO languages_iso639 VALUES ('mmm', '   ', '   ', '  ', 'I', 'L', 'Maii', false, '');
INSERT INTO languages_iso639 VALUES ('mmn', '   ', '   ', '  ', 'I', 'L', 'Mamanwa', false, '');
INSERT INTO languages_iso639 VALUES ('mmo', '   ', '   ', '  ', 'I', 'L', 'Mangga Buang', false, '');
INSERT INTO languages_iso639 VALUES ('mmp', '   ', '   ', '  ', 'I', 'L', 'Siawi', false, '');
INSERT INTO languages_iso639 VALUES ('mmq', '   ', '   ', '  ', 'I', 'L', 'Musak', false, '');
INSERT INTO languages_iso639 VALUES ('mmr', '   ', '   ', '  ', 'I', 'L', 'Western Xiangxi Miao', false, '');
INSERT INTO languages_iso639 VALUES ('mmt', '   ', '   ', '  ', 'I', 'L', 'Malalamai', false, '');
INSERT INTO languages_iso639 VALUES ('mmu', '   ', '   ', '  ', 'I', 'L', 'Mmaala', false, '');
INSERT INTO languages_iso639 VALUES ('mmv', '   ', '   ', '  ', 'I', 'E', 'Miriti', false, '');
INSERT INTO languages_iso639 VALUES ('mmw', '   ', '   ', '  ', 'I', 'L', 'Emae', false, '');
INSERT INTO languages_iso639 VALUES ('mmx', '   ', '   ', '  ', 'I', 'L', 'Madak', false, '');
INSERT INTO languages_iso639 VALUES ('mmy', '   ', '   ', '  ', 'I', 'L', 'Migaama', false, '');
INSERT INTO languages_iso639 VALUES ('mmz', '   ', '   ', '  ', 'I', 'L', 'Mabaale', false, '');
INSERT INTO languages_iso639 VALUES ('mna', '   ', '   ', '  ', 'I', 'L', 'Mbula', false, '');
INSERT INTO languages_iso639 VALUES ('mnb', '   ', '   ', '  ', 'I', 'L', 'Muna', false, '');
INSERT INTO languages_iso639 VALUES ('mnc', 'mnc', 'mnc', '  ', 'I', 'L', 'Manchu', false, '');
INSERT INTO languages_iso639 VALUES ('mnd', '   ', '   ', '  ', 'I', 'L', 'Mondé', false, '');
INSERT INTO languages_iso639 VALUES ('mne', '   ', '   ', '  ', 'I', 'L', 'Naba', false, '');
INSERT INTO languages_iso639 VALUES ('mnf', '   ', '   ', '  ', 'I', 'L', 'Mundani', false, '');
INSERT INTO languages_iso639 VALUES ('mng', '   ', '   ', '  ', 'I', 'L', 'Eastern Mnong', false, '');
INSERT INTO languages_iso639 VALUES ('mnh', '   ', '   ', '  ', 'I', 'L', 'Mono (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('mni', 'mni', 'mni', '  ', 'I', 'L', 'Manipuri', false, '');
INSERT INTO languages_iso639 VALUES ('mnj', '   ', '   ', '  ', 'I', 'L', 'Munji', false, '');
INSERT INTO languages_iso639 VALUES ('mnk', '   ', '   ', '  ', 'I', 'L', 'Mandinka', false, '');
INSERT INTO languages_iso639 VALUES ('mnl', '   ', '   ', '  ', 'I', 'L', 'Tiale', false, '');
INSERT INTO languages_iso639 VALUES ('mnm', '   ', '   ', '  ', 'I', 'L', 'Mapena', false, '');
INSERT INTO languages_iso639 VALUES ('mnn', '   ', '   ', '  ', 'I', 'L', 'Southern Mnong', false, '');
INSERT INTO languages_iso639 VALUES ('mnp', '   ', '   ', '  ', 'I', 'L', 'Min Bei Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('mnq', '   ', '   ', '  ', 'I', 'L', 'Minriq', false, '');
INSERT INTO languages_iso639 VALUES ('mnr', '   ', '   ', '  ', 'I', 'L', 'Mono (USA)', false, '');
INSERT INTO languages_iso639 VALUES ('mns', '   ', '   ', '  ', 'I', 'L', 'Mansi', false, '');
INSERT INTO languages_iso639 VALUES ('mnu', '   ', '   ', '  ', 'I', 'L', 'Mer', false, '');
INSERT INTO languages_iso639 VALUES ('mnv', '   ', '   ', '  ', 'I', 'L', 'Rennell-Bellona', false, '');
INSERT INTO languages_iso639 VALUES ('mnw', '   ', '   ', '  ', 'I', 'L', 'Mon', false, '');
INSERT INTO languages_iso639 VALUES ('mnx', '   ', '   ', '  ', 'I', 'L', 'Manikion', false, '');
INSERT INTO languages_iso639 VALUES ('mny', '   ', '   ', '  ', 'I', 'L', 'Manyawa', false, '');
INSERT INTO languages_iso639 VALUES ('mnz', '   ', '   ', '  ', 'I', 'L', 'Moni', false, '');
INSERT INTO languages_iso639 VALUES ('moa', '   ', '   ', '  ', 'I', 'L', 'Mwan', false, '');
INSERT INTO languages_iso639 VALUES ('moc', '   ', '   ', '  ', 'I', 'L', 'Mocoví', false, '');
INSERT INTO languages_iso639 VALUES ('mod', '   ', '   ', '  ', 'I', 'E', 'Mobilian', false, '');
INSERT INTO languages_iso639 VALUES ('moe', '   ', '   ', '  ', 'I', 'L', 'Montagnais', false, '');
INSERT INTO languages_iso639 VALUES ('mog', '   ', '   ', '  ', 'I', 'L', 'Mongondow', false, '');
INSERT INTO languages_iso639 VALUES ('moh', 'moh', 'moh', '  ', 'I', 'L', 'Mohawk', false, '');
INSERT INTO languages_iso639 VALUES ('moi', '   ', '   ', '  ', 'I', 'L', 'Mboi', false, '');
INSERT INTO languages_iso639 VALUES ('moj', '   ', '   ', '  ', 'I', 'L', 'Monzombo', false, '');
INSERT INTO languages_iso639 VALUES ('mok', '   ', '   ', '  ', 'I', 'L', 'Morori', false, '');
INSERT INTO languages_iso639 VALUES ('mom', '   ', '   ', '  ', 'I', 'E', 'Mangue', false, '');
INSERT INTO languages_iso639 VALUES ('mon', 'mon', 'mon', 'mn', 'M', 'L', 'Mongolian', false, '');
INSERT INTO languages_iso639 VALUES ('moo', '   ', '   ', '  ', 'I', 'L', 'Monom', false, '');
INSERT INTO languages_iso639 VALUES ('mop', '   ', '   ', '  ', 'I', 'L', 'Mopán Maya', false, '');
INSERT INTO languages_iso639 VALUES ('moq', '   ', '   ', '  ', 'I', 'L', 'Mor (Bomberai Peninsula)', false, '');
INSERT INTO languages_iso639 VALUES ('mor', '   ', '   ', '  ', 'I', 'L', 'Moro', false, '');
INSERT INTO languages_iso639 VALUES ('mos', 'mos', 'mos', '  ', 'I', 'L', 'Mossi', false, '');
INSERT INTO languages_iso639 VALUES ('mot', '   ', '   ', '  ', 'I', 'L', 'Barí', false, '');
INSERT INTO languages_iso639 VALUES ('mou', '   ', '   ', '  ', 'I', 'L', 'Mogum', false, '');
INSERT INTO languages_iso639 VALUES ('mov', '   ', '   ', '  ', 'I', 'L', 'Mohave', false, '');
INSERT INTO languages_iso639 VALUES ('mow', '   ', '   ', '  ', 'I', 'L', 'Moi (Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('mox', '   ', '   ', '  ', 'I', 'L', 'Molima', false, '');
INSERT INTO languages_iso639 VALUES ('moy', '   ', '   ', '  ', 'I', 'L', 'Shekkacho', false, '');
INSERT INTO languages_iso639 VALUES ('moz', '   ', '   ', '  ', 'I', 'L', 'Mukulu', false, '');
INSERT INTO languages_iso639 VALUES ('mpa', '   ', '   ', '  ', 'I', 'L', 'Mpoto', false, '');
INSERT INTO languages_iso639 VALUES ('mpb', '   ', '   ', '  ', 'I', 'L', 'Mullukmulluk', false, '');
INSERT INTO languages_iso639 VALUES ('mpc', '   ', '   ', '  ', 'I', 'L', 'Mangarayi', false, '');
INSERT INTO languages_iso639 VALUES ('mpd', '   ', '   ', '  ', 'I', 'L', 'Machinere', false, '');
INSERT INTO languages_iso639 VALUES ('mpe', '   ', '   ', '  ', 'I', 'L', 'Majang', false, '');
INSERT INTO languages_iso639 VALUES ('mpg', '   ', '   ', '  ', 'I', 'L', 'Marba', false, '');
INSERT INTO languages_iso639 VALUES ('mph', '   ', '   ', '  ', 'I', 'L', 'Maung', false, '');
INSERT INTO languages_iso639 VALUES ('mpi', '   ', '   ', '  ', 'I', 'L', 'Mpade', false, '');
INSERT INTO languages_iso639 VALUES ('mpj', '   ', '   ', '  ', 'I', 'L', 'Martu Wangka', false, '');
INSERT INTO languages_iso639 VALUES ('mpk', '   ', '   ', '  ', 'I', 'L', 'Mbara (Chad)', false, '');
INSERT INTO languages_iso639 VALUES ('mpl', '   ', '   ', '  ', 'I', 'L', 'Middle Watut', false, '');
INSERT INTO languages_iso639 VALUES ('mpm', '   ', '   ', '  ', 'I', 'L', 'Yosondúa Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mpn', '   ', '   ', '  ', 'I', 'L', 'Mindiri', false, '');
INSERT INTO languages_iso639 VALUES ('mpo', '   ', '   ', '  ', 'I', 'L', 'Miu', false, '');
INSERT INTO languages_iso639 VALUES ('mpp', '   ', '   ', '  ', 'I', 'L', 'Migabac', false, '');
INSERT INTO languages_iso639 VALUES ('mpq', '   ', '   ', '  ', 'I', 'L', 'Matís', false, '');
INSERT INTO languages_iso639 VALUES ('mpr', '   ', '   ', '  ', 'I', 'L', 'Vangunu', false, '');
INSERT INTO languages_iso639 VALUES ('mps', '   ', '   ', '  ', 'I', 'L', 'Dadibi', false, '');
INSERT INTO languages_iso639 VALUES ('mpt', '   ', '   ', '  ', 'I', 'L', 'Mian', false, '');
INSERT INTO languages_iso639 VALUES ('mpu', '   ', '   ', '  ', 'I', 'L', 'Makuráp', false, '');
INSERT INTO languages_iso639 VALUES ('mpv', '   ', '   ', '  ', 'I', 'L', 'Mungkip', false, '');
INSERT INTO languages_iso639 VALUES ('mpw', '   ', '   ', '  ', 'I', 'L', 'Mapidian', false, '');
INSERT INTO languages_iso639 VALUES ('mpx', '   ', '   ', '  ', 'I', 'L', 'Misima-Panaeati', false, '');
INSERT INTO languages_iso639 VALUES ('mpy', '   ', '   ', '  ', 'I', 'L', 'Mapia', false, '');
INSERT INTO languages_iso639 VALUES ('mpz', '   ', '   ', '  ', 'I', 'L', 'Mpi', false, '');
INSERT INTO languages_iso639 VALUES ('mqa', '   ', '   ', '  ', 'I', 'L', 'Maba (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('mqb', '   ', '   ', '  ', 'I', 'L', 'Mbuko', false, '');
INSERT INTO languages_iso639 VALUES ('mqc', '   ', '   ', '  ', 'I', 'L', 'Mangole', false, '');
INSERT INTO languages_iso639 VALUES ('mqe', '   ', '   ', '  ', 'I', 'L', 'Matepi', false, '');
INSERT INTO languages_iso639 VALUES ('mqf', '   ', '   ', '  ', 'I', 'L', 'Momuna', false, '');
INSERT INTO languages_iso639 VALUES ('mqg', '   ', '   ', '  ', 'I', 'L', 'Kota Bangun Kutai Malay', false, '');
INSERT INTO languages_iso639 VALUES ('mqh', '   ', '   ', '  ', 'I', 'L', 'Tlazoyaltepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mqi', '   ', '   ', '  ', 'I', 'L', 'Mariri', false, '');
INSERT INTO languages_iso639 VALUES ('mqj', '   ', '   ', '  ', 'I', 'L', 'Mamasa', false, '');
INSERT INTO languages_iso639 VALUES ('mqk', '   ', '   ', '  ', 'I', 'L', 'Rajah Kabunsuwan Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mql', '   ', '   ', '  ', 'I', 'L', 'Mbelime', false, '');
INSERT INTO languages_iso639 VALUES ('mqm', '   ', '   ', '  ', 'I', 'L', 'South Marquesan', false, '');
INSERT INTO languages_iso639 VALUES ('mqn', '   ', '   ', '  ', 'I', 'L', 'Moronene', false, '');
INSERT INTO languages_iso639 VALUES ('mqo', '   ', '   ', '  ', 'I', 'L', 'Modole', false, '');
INSERT INTO languages_iso639 VALUES ('mqp', '   ', '   ', '  ', 'I', 'L', 'Manipa', false, '');
INSERT INTO languages_iso639 VALUES ('mqq', '   ', '   ', '  ', 'I', 'L', 'Minokok', false, '');
INSERT INTO languages_iso639 VALUES ('mqr', '   ', '   ', '  ', 'I', 'L', 'Mander', false, '');
INSERT INTO languages_iso639 VALUES ('mqs', '   ', '   ', '  ', 'I', 'L', 'West Makian', false, '');
INSERT INTO languages_iso639 VALUES ('mqt', '   ', '   ', '  ', 'I', 'L', 'Mok', false, '');
INSERT INTO languages_iso639 VALUES ('mqu', '   ', '   ', '  ', 'I', 'L', 'Mandari', false, '');
INSERT INTO languages_iso639 VALUES ('mqv', '   ', '   ', '  ', 'I', 'L', 'Mosimo', false, '');
INSERT INTO languages_iso639 VALUES ('mqw', '   ', '   ', '  ', 'I', 'L', 'Murupi', false, '');
INSERT INTO languages_iso639 VALUES ('mqx', '   ', '   ', '  ', 'I', 'L', 'Mamuju', false, '');
INSERT INTO languages_iso639 VALUES ('mqy', '   ', '   ', '  ', 'I', 'L', 'Manggarai', false, '');
INSERT INTO languages_iso639 VALUES ('mqz', '   ', '   ', '  ', 'I', 'L', 'Pano', false, '');
INSERT INTO languages_iso639 VALUES ('mra', '   ', '   ', '  ', 'I', 'L', 'Mlabri', false, '');
INSERT INTO languages_iso639 VALUES ('mrb', '   ', '   ', '  ', 'I', 'L', 'Marino', false, '');
INSERT INTO languages_iso639 VALUES ('mrc', '   ', '   ', '  ', 'I', 'L', 'Maricopa', false, '');
INSERT INTO languages_iso639 VALUES ('mrd', '   ', '   ', '  ', 'I', 'L', 'Western Magar', false, '');
INSERT INTO languages_iso639 VALUES ('mre', '   ', '   ', '  ', 'I', 'E', 'Martha''s Vineyard Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mrf', '   ', '   ', '  ', 'I', 'L', 'Elseng', false, '');
INSERT INTO languages_iso639 VALUES ('mrg', '   ', '   ', '  ', 'I', 'L', 'Mising', false, '');
INSERT INTO languages_iso639 VALUES ('mrh', '   ', '   ', '  ', 'I', 'L', 'Mara Chin', false, '');
INSERT INTO languages_iso639 VALUES ('mri', 'mao', 'mri', 'mi', 'I', 'L', 'Maori', false, '');
INSERT INTO languages_iso639 VALUES ('mrj', '   ', '   ', '  ', 'I', 'L', 'Western Mari', false, '');
INSERT INTO languages_iso639 VALUES ('mrk', '   ', '   ', '  ', 'I', 'L', 'Hmwaveke', false, '');
INSERT INTO languages_iso639 VALUES ('mrl', '   ', '   ', '  ', 'I', 'L', 'Mortlockese', false, '');
INSERT INTO languages_iso639 VALUES ('mrm', '   ', '   ', '  ', 'I', 'L', 'Merlav', false, '');
INSERT INTO languages_iso639 VALUES ('mrn', '   ', '   ', '  ', 'I', 'L', 'Cheke Holo', false, '');
INSERT INTO languages_iso639 VALUES ('mro', '   ', '   ', '  ', 'I', 'L', 'Mru', false, '');
INSERT INTO languages_iso639 VALUES ('mrp', '   ', '   ', '  ', 'I', 'L', 'Morouas', false, '');
INSERT INTO languages_iso639 VALUES ('mrq', '   ', '   ', '  ', 'I', 'L', 'North Marquesan', false, '');
INSERT INTO languages_iso639 VALUES ('mrr', '   ', '   ', '  ', 'I', 'L', 'Maria (India)', false, '');
INSERT INTO languages_iso639 VALUES ('mrs', '   ', '   ', '  ', 'I', 'L', 'Maragus', false, '');
INSERT INTO languages_iso639 VALUES ('mrt', '   ', '   ', '  ', 'I', 'L', 'Marghi Central', false, '');
INSERT INTO languages_iso639 VALUES ('mru', '   ', '   ', '  ', 'I', 'L', 'Mono (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('mrv', '   ', '   ', '  ', 'I', 'L', 'Mangareva', false, '');
INSERT INTO languages_iso639 VALUES ('mrw', '   ', '   ', '  ', 'I', 'L', 'Maranao', false, '');
INSERT INTO languages_iso639 VALUES ('mrx', '   ', '   ', '  ', 'I', 'L', 'Maremgi', false, '');
INSERT INTO languages_iso639 VALUES ('mry', '   ', '   ', '  ', 'I', 'L', 'Mandaya', false, '');
INSERT INTO languages_iso639 VALUES ('mrz', '   ', '   ', '  ', 'I', 'L', 'Marind', false, '');
INSERT INTO languages_iso639 VALUES ('msa', 'may', 'msa', 'ms', 'M', 'L', 'Malay (macrolanguage)', false, '');
INSERT INTO languages_iso639 VALUES ('msb', '   ', '   ', '  ', 'I', 'L', 'Masbatenyo', false, '');
INSERT INTO languages_iso639 VALUES ('msc', '   ', '   ', '  ', 'I', 'L', 'Sankaran Maninka', false, '');
INSERT INTO languages_iso639 VALUES ('msd', '   ', '   ', '  ', 'I', 'L', 'Yucatec Maya Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mse', '   ', '   ', '  ', 'I', 'L', 'Musey', false, '');
INSERT INTO languages_iso639 VALUES ('msf', '   ', '   ', '  ', 'I', 'L', 'Mekwei', false, '');
INSERT INTO languages_iso639 VALUES ('msg', '   ', '   ', '  ', 'I', 'L', 'Moraid', false, '');
INSERT INTO languages_iso639 VALUES ('msh', '   ', '   ', '  ', 'I', 'L', 'Masikoro Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('msi', '   ', '   ', '  ', 'I', 'L', 'Sabah Malay', false, '');
INSERT INTO languages_iso639 VALUES ('msj', '   ', '   ', '  ', 'I', 'L', 'Ma (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('msk', '   ', '   ', '  ', 'I', 'L', 'Mansaka', false, '');
INSERT INTO languages_iso639 VALUES ('msl', '   ', '   ', '  ', 'I', 'L', 'Molof', false, '');
INSERT INTO languages_iso639 VALUES ('msm', '   ', '   ', '  ', 'I', 'L', 'Agusan Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('msn', '   ', '   ', '  ', 'I', 'L', 'Vurës', false, '');
INSERT INTO languages_iso639 VALUES ('mso', '   ', '   ', '  ', 'I', 'L', 'Mombum', false, '');
INSERT INTO languages_iso639 VALUES ('msp', '   ', '   ', '  ', 'I', 'E', 'Maritsauá', false, '');
INSERT INTO languages_iso639 VALUES ('msq', '   ', '   ', '  ', 'I', 'L', 'Caac', false, '');
INSERT INTO languages_iso639 VALUES ('msr', '   ', '   ', '  ', 'I', 'L', 'Mongolian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mss', '   ', '   ', '  ', 'I', 'L', 'West Masela', false, '');
INSERT INTO languages_iso639 VALUES ('msu', '   ', '   ', '  ', 'I', 'L', 'Musom', false, '');
INSERT INTO languages_iso639 VALUES ('msv', '   ', '   ', '  ', 'I', 'L', 'Maslam', false, '');
INSERT INTO languages_iso639 VALUES ('msw', '   ', '   ', '  ', 'I', 'L', 'Mansoanka', false, '');
INSERT INTO languages_iso639 VALUES ('msx', '   ', '   ', '  ', 'I', 'L', 'Moresada', false, '');
INSERT INTO languages_iso639 VALUES ('msy', '   ', '   ', '  ', 'I', 'L', 'Aruamu', false, '');
INSERT INTO languages_iso639 VALUES ('msz', '   ', '   ', '  ', 'I', 'L', 'Momare', false, '');
INSERT INTO languages_iso639 VALUES ('mta', '   ', '   ', '  ', 'I', 'L', 'Cotabato Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('mtb', '   ', '   ', '  ', 'I', 'L', 'Anyin Morofo', false, '');
INSERT INTO languages_iso639 VALUES ('mtc', '   ', '   ', '  ', 'I', 'L', 'Munit', false, '');
INSERT INTO languages_iso639 VALUES ('mtd', '   ', '   ', '  ', 'I', 'L', 'Mualang', false, '');
INSERT INTO languages_iso639 VALUES ('mte', '   ', '   ', '  ', 'I', 'L', 'Mono (Solomon Islands)', false, '');
INSERT INTO languages_iso639 VALUES ('mtf', '   ', '   ', '  ', 'I', 'L', 'Murik (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('mtg', '   ', '   ', '  ', 'I', 'L', 'Una', false, '');
INSERT INTO languages_iso639 VALUES ('mth', '   ', '   ', '  ', 'I', 'L', 'Munggui', false, '');
INSERT INTO languages_iso639 VALUES ('mti', '   ', '   ', '  ', 'I', 'L', 'Maiwa (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('mtj', '   ', '   ', '  ', 'I', 'L', 'Moskona', false, '');
INSERT INTO languages_iso639 VALUES ('mtk', '   ', '   ', '  ', 'I', 'L', 'Mbe''', false, '');
INSERT INTO languages_iso639 VALUES ('mtl', '   ', '   ', '  ', 'I', 'L', 'Montol', false, '');
INSERT INTO languages_iso639 VALUES ('mtm', '   ', '   ', '  ', 'I', 'E', 'Mator', false, '');
INSERT INTO languages_iso639 VALUES ('mtn', '   ', '   ', '  ', 'I', 'E', 'Matagalpa', false, '');
INSERT INTO languages_iso639 VALUES ('mto', '   ', '   ', '  ', 'I', 'L', 'Totontepec Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('mtp', '   ', '   ', '  ', 'I', 'L', 'Wichí Lhamtés Nocten', false, '');
INSERT INTO languages_iso639 VALUES ('mtq', '   ', '   ', '  ', 'I', 'L', 'Muong', false, '');
INSERT INTO languages_iso639 VALUES ('mtr', '   ', '   ', '  ', 'I', 'L', 'Mewari', false, '');
INSERT INTO languages_iso639 VALUES ('mts', '   ', '   ', '  ', 'I', 'L', 'Yora', false, '');
INSERT INTO languages_iso639 VALUES ('mtt', '   ', '   ', '  ', 'I', 'L', 'Mota', false, '');
INSERT INTO languages_iso639 VALUES ('mtu', '   ', '   ', '  ', 'I', 'L', 'Tututepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mtv', '   ', '   ', '  ', 'I', 'L', 'Asaro''o', false, '');
INSERT INTO languages_iso639 VALUES ('mtw', '   ', '   ', '  ', 'I', 'L', 'Southern Binukidnon', false, '');
INSERT INTO languages_iso639 VALUES ('mtx', '   ', '   ', '  ', 'I', 'L', 'Tidaá Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mty', '   ', '   ', '  ', 'I', 'L', 'Nabi', false, '');
INSERT INTO languages_iso639 VALUES ('mua', '   ', '   ', '  ', 'I', 'L', 'Mundang', false, '');
INSERT INTO languages_iso639 VALUES ('mub', '   ', '   ', '  ', 'I', 'L', 'Mubi', false, '');
INSERT INTO languages_iso639 VALUES ('muc', '   ', '   ', '  ', 'I', 'L', 'Ajumbu', false, '');
INSERT INTO languages_iso639 VALUES ('mud', '   ', '   ', '  ', 'I', 'L', 'Mednyj Aleut', false, '');
INSERT INTO languages_iso639 VALUES ('mue', '   ', '   ', '  ', 'I', 'L', 'Media Lengua', false, '');
INSERT INTO languages_iso639 VALUES ('mug', '   ', '   ', '  ', 'I', 'L', 'Musgu', false, '');
INSERT INTO languages_iso639 VALUES ('muh', '   ', '   ', '  ', 'I', 'L', 'Mündü', false, '');
INSERT INTO languages_iso639 VALUES ('mui', '   ', '   ', '  ', 'I', 'L', 'Musi', false, '');
INSERT INTO languages_iso639 VALUES ('muj', '   ', '   ', '  ', 'I', 'L', 'Mabire', false, '');
INSERT INTO languages_iso639 VALUES ('muk', '   ', '   ', '  ', 'I', 'L', 'Mugom', false, '');
INSERT INTO languages_iso639 VALUES ('mul', 'mul', 'mul', '  ', 'S', 'S', 'Multiple languages', false, '');
INSERT INTO languages_iso639 VALUES ('mum', '   ', '   ', '  ', 'I', 'L', 'Maiwala', false, '');
INSERT INTO languages_iso639 VALUES ('muo', '   ', '   ', '  ', 'I', 'L', 'Nyong', false, '');
INSERT INTO languages_iso639 VALUES ('mup', '   ', '   ', '  ', 'I', 'L', 'Malvi', false, '');
INSERT INTO languages_iso639 VALUES ('muq', '   ', '   ', '  ', 'I', 'L', 'Eastern Xiangxi Miao', false, '');
INSERT INTO languages_iso639 VALUES ('mur', '   ', '   ', '  ', 'I', 'L', 'Murle', false, '');
INSERT INTO languages_iso639 VALUES ('mus', 'mus', 'mus', '  ', 'I', 'L', 'Creek', false, '');
INSERT INTO languages_iso639 VALUES ('mut', '   ', '   ', '  ', 'I', 'L', 'Western Muria', false, '');
INSERT INTO languages_iso639 VALUES ('muu', '   ', '   ', '  ', 'I', 'L', 'Yaaku', false, '');
INSERT INTO languages_iso639 VALUES ('muv', '   ', '   ', '  ', 'I', 'L', 'Muthuvan', false, '');
INSERT INTO languages_iso639 VALUES ('mux', '   ', '   ', '  ', 'I', 'L', 'Bo-Ung', false, '');
INSERT INTO languages_iso639 VALUES ('muy', '   ', '   ', '  ', 'I', 'L', 'Muyang', false, '');
INSERT INTO languages_iso639 VALUES ('muz', '   ', '   ', '  ', 'I', 'L', 'Mursi', false, '');
INSERT INTO languages_iso639 VALUES ('mva', '   ', '   ', '  ', 'I', 'L', 'Manam', false, '');
INSERT INTO languages_iso639 VALUES ('mvb', '   ', '   ', '  ', 'I', 'E', 'Mattole', false, '');
INSERT INTO languages_iso639 VALUES ('mvd', '   ', '   ', '  ', 'I', 'L', 'Mamboru', false, '');
INSERT INTO languages_iso639 VALUES ('mve', '   ', '   ', '  ', 'I', 'L', 'Marwari (Pakistan)', false, '');
INSERT INTO languages_iso639 VALUES ('mvf', '   ', '   ', '  ', 'I', 'L', 'Peripheral Mongolian', false, '');
INSERT INTO languages_iso639 VALUES ('mvg', '   ', '   ', '  ', 'I', 'L', 'Yucuañe Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mvh', '   ', '   ', '  ', 'I', 'L', 'Mulgi', false, '');
INSERT INTO languages_iso639 VALUES ('mvi', '   ', '   ', '  ', 'I', 'L', 'Miyako', false, '');
INSERT INTO languages_iso639 VALUES ('mvk', '   ', '   ', '  ', 'I', 'L', 'Mekmek', false, '');
INSERT INTO languages_iso639 VALUES ('mvl', '   ', '   ', '  ', 'I', 'E', 'Mbara (Australia)', false, '');
INSERT INTO languages_iso639 VALUES ('mvm', '   ', '   ', '  ', 'I', 'L', 'Muya', false, '');
INSERT INTO languages_iso639 VALUES ('mvn', '   ', '   ', '  ', 'I', 'L', 'Minaveha', false, '');
INSERT INTO languages_iso639 VALUES ('mvo', '   ', '   ', '  ', 'I', 'L', 'Marovo', false, '');
INSERT INTO languages_iso639 VALUES ('mvp', '   ', '   ', '  ', 'I', 'L', 'Duri', false, '');
INSERT INTO languages_iso639 VALUES ('mvq', '   ', '   ', '  ', 'I', 'L', 'Moere', false, '');
INSERT INTO languages_iso639 VALUES ('mvr', '   ', '   ', '  ', 'I', 'L', 'Marau', false, '');
INSERT INTO languages_iso639 VALUES ('mvs', '   ', '   ', '  ', 'I', 'L', 'Massep', false, '');
INSERT INTO languages_iso639 VALUES ('mvt', '   ', '   ', '  ', 'I', 'L', 'Mpotovoro', false, '');
INSERT INTO languages_iso639 VALUES ('mvu', '   ', '   ', '  ', 'I', 'L', 'Marfa', false, '');
INSERT INTO languages_iso639 VALUES ('mvv', '   ', '   ', '  ', 'I', 'L', 'Tagal Murut', false, '');
INSERT INTO languages_iso639 VALUES ('mvw', '   ', '   ', '  ', 'I', 'L', 'Machinga', false, '');
INSERT INTO languages_iso639 VALUES ('mvx', '   ', '   ', '  ', 'I', 'L', 'Meoswar', false, '');
INSERT INTO languages_iso639 VALUES ('mvy', '   ', '   ', '  ', 'I', 'L', 'Indus Kohistani', false, '');
INSERT INTO languages_iso639 VALUES ('mvz', '   ', '   ', '  ', 'I', 'L', 'Mesqan', false, '');
INSERT INTO languages_iso639 VALUES ('mwa', '   ', '   ', '  ', 'I', 'L', 'Mwatebu', false, '');
INSERT INTO languages_iso639 VALUES ('mwb', '   ', '   ', '  ', 'I', 'L', 'Juwal', false, '');
INSERT INTO languages_iso639 VALUES ('mwc', '   ', '   ', '  ', 'I', 'L', 'Are', false, '');
INSERT INTO languages_iso639 VALUES ('mwe', '   ', '   ', '  ', 'I', 'L', 'Mwera (Chimwera)', false, '');
INSERT INTO languages_iso639 VALUES ('mwf', '   ', '   ', '  ', 'I', 'L', 'Murrinh-Patha', false, '');
INSERT INTO languages_iso639 VALUES ('mwg', '   ', '   ', '  ', 'I', 'L', 'Aiklep', false, '');
INSERT INTO languages_iso639 VALUES ('mwh', '   ', '   ', '  ', 'I', 'L', 'Mouk-Aria', false, '');
INSERT INTO languages_iso639 VALUES ('mwi', '   ', '   ', '  ', 'I', 'L', 'Labo', false, '');
INSERT INTO languages_iso639 VALUES ('mwj', '   ', '   ', '  ', 'I', 'L', 'Maligo', false, '');
INSERT INTO languages_iso639 VALUES ('mwk', '   ', '   ', '  ', 'I', 'L', 'Kita Maninkakan', false, '');
INSERT INTO languages_iso639 VALUES ('mwl', 'mwl', 'mwl', '  ', 'I', 'L', 'Mirandese', false, '');
INSERT INTO languages_iso639 VALUES ('mwm', '   ', '   ', '  ', 'I', 'L', 'Sar', false, '');
INSERT INTO languages_iso639 VALUES ('mwn', '   ', '   ', '  ', 'I', 'L', 'Nyamwanga', false, '');
INSERT INTO languages_iso639 VALUES ('mwo', '   ', '   ', '  ', 'I', 'L', 'Central Maewo', false, '');
INSERT INTO languages_iso639 VALUES ('mwp', '   ', '   ', '  ', 'I', 'L', 'Kala Lagaw Ya', false, '');
INSERT INTO languages_iso639 VALUES ('mwq', '   ', '   ', '  ', 'I', 'L', 'Mün Chin', false, '');
INSERT INTO languages_iso639 VALUES ('mwr', 'mwr', 'mwr', '  ', 'M', 'L', 'Marwari', false, '');
INSERT INTO languages_iso639 VALUES ('mws', '   ', '   ', '  ', 'I', 'L', 'Mwimbi-Muthambi', false, '');
INSERT INTO languages_iso639 VALUES ('mwt', '   ', '   ', '  ', 'I', 'L', 'Moken', false, '');
INSERT INTO languages_iso639 VALUES ('mwu', '   ', '   ', '  ', 'I', 'E', 'Mittu', false, '');
INSERT INTO languages_iso639 VALUES ('mwv', '   ', '   ', '  ', 'I', 'L', 'Mentawai', false, '');
INSERT INTO languages_iso639 VALUES ('mww', '   ', '   ', '  ', 'I', 'L', 'Hmong Daw', false, '');
INSERT INTO languages_iso639 VALUES ('mwx', '   ', '   ', '  ', 'I', 'L', 'Mediak', false, '');
INSERT INTO languages_iso639 VALUES ('mwy', '   ', '   ', '  ', 'I', 'L', 'Mosiro', false, '');
INSERT INTO languages_iso639 VALUES ('mwz', '   ', '   ', '  ', 'I', 'L', 'Moingi', false, '');
INSERT INTO languages_iso639 VALUES ('mxa', '   ', '   ', '  ', 'I', 'L', 'Northwest Oaxaca Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mxb', '   ', '   ', '  ', 'I', 'L', 'Tezoatlán Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mxc', '   ', '   ', '  ', 'I', 'L', 'Manyika', false, '');
INSERT INTO languages_iso639 VALUES ('mxd', '   ', '   ', '  ', 'I', 'L', 'Modang', false, '');
INSERT INTO languages_iso639 VALUES ('mxe', '   ', '   ', '  ', 'I', 'L', 'Mele-Fila', false, '');
INSERT INTO languages_iso639 VALUES ('mxf', '   ', '   ', '  ', 'I', 'L', 'Malgbe', false, '');
INSERT INTO languages_iso639 VALUES ('mxg', '   ', '   ', '  ', 'I', 'L', 'Mbangala', false, '');
INSERT INTO languages_iso639 VALUES ('mxh', '   ', '   ', '  ', 'I', 'L', 'Mvuba', false, '');
INSERT INTO languages_iso639 VALUES ('mxi', '   ', '   ', '  ', 'I', 'E', 'Mozarabic', false, '');
INSERT INTO languages_iso639 VALUES ('mxj', '   ', '   ', '  ', 'I', 'L', 'Miju-Mishmi', false, '');
INSERT INTO languages_iso639 VALUES ('mxk', '   ', '   ', '  ', 'I', 'L', 'Monumbo', false, '');
INSERT INTO languages_iso639 VALUES ('mxl', '   ', '   ', '  ', 'I', 'L', 'Maxi Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('mxm', '   ', '   ', '  ', 'I', 'L', 'Meramera', false, '');
INSERT INTO languages_iso639 VALUES ('mxn', '   ', '   ', '  ', 'I', 'L', 'Moi (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('mxo', '   ', '   ', '  ', 'I', 'L', 'Mbowe', false, '');
INSERT INTO languages_iso639 VALUES ('mxp', '   ', '   ', '  ', 'I', 'L', 'Tlahuitoltepec Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('mxq', '   ', '   ', '  ', 'I', 'L', 'Juquila Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('mxr', '   ', '   ', '  ', 'I', 'L', 'Murik (Malaysia)', false, '');
INSERT INTO languages_iso639 VALUES ('mxs', '   ', '   ', '  ', 'I', 'L', 'Huitepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mxt', '   ', '   ', '  ', 'I', 'L', 'Jamiltepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mxu', '   ', '   ', '  ', 'I', 'L', 'Mada (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('mxv', '   ', '   ', '  ', 'I', 'L', 'Metlatónoc Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mxw', '   ', '   ', '  ', 'I', 'L', 'Namo', false, '');
INSERT INTO languages_iso639 VALUES ('mxx', '   ', '   ', '  ', 'I', 'L', 'Mahou', false, '');
INSERT INTO languages_iso639 VALUES ('mxy', '   ', '   ', '  ', 'I', 'L', 'Southeastern Nochixtlán Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mxz', '   ', '   ', '  ', 'I', 'L', 'Central Masela', false, '');
INSERT INTO languages_iso639 VALUES ('mya', 'bur', 'mya', 'my', 'I', 'L', 'Burmese', false, '');
INSERT INTO languages_iso639 VALUES ('myb', '   ', '   ', '  ', 'I', 'L', 'Mbay', false, '');
INSERT INTO languages_iso639 VALUES ('myc', '   ', '   ', '  ', 'I', 'L', 'Mayeka', false, '');
INSERT INTO languages_iso639 VALUES ('myd', '   ', '   ', '  ', 'I', 'L', 'Maramba', false, '');
INSERT INTO languages_iso639 VALUES ('mye', '   ', '   ', '  ', 'I', 'L', 'Myene', false, '');
INSERT INTO languages_iso639 VALUES ('myf', '   ', '   ', '  ', 'I', 'L', 'Bambassi', false, '');
INSERT INTO languages_iso639 VALUES ('myg', '   ', '   ', '  ', 'I', 'L', 'Manta', false, '');
INSERT INTO languages_iso639 VALUES ('myh', '   ', '   ', '  ', 'I', 'L', 'Makah', false, '');
INSERT INTO languages_iso639 VALUES ('myi', '   ', '   ', '  ', 'I', 'L', 'Mina (India)', false, '');
INSERT INTO languages_iso639 VALUES ('myj', '   ', '   ', '  ', 'I', 'L', 'Mangayat', false, '');
INSERT INTO languages_iso639 VALUES ('myk', '   ', '   ', '  ', 'I', 'L', 'Mamara Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('myl', '   ', '   ', '  ', 'I', 'L', 'Moma', false, '');
INSERT INTO languages_iso639 VALUES ('mym', '   ', '   ', '  ', 'I', 'L', 'Me''en', false, '');
INSERT INTO languages_iso639 VALUES ('myo', '   ', '   ', '  ', 'I', 'L', 'Anfillo', false, '');
INSERT INTO languages_iso639 VALUES ('myp', '   ', '   ', '  ', 'I', 'L', 'Pirahã', false, '');
INSERT INTO languages_iso639 VALUES ('myr', '   ', '   ', '  ', 'I', 'L', 'Muniche', false, '');
INSERT INTO languages_iso639 VALUES ('mys', '   ', '   ', '  ', 'I', 'E', 'Mesmes', false, '');
INSERT INTO languages_iso639 VALUES ('myu', '   ', '   ', '  ', 'I', 'L', 'Mundurukú', false, '');
INSERT INTO languages_iso639 VALUES ('myv', 'myv', 'myv', '  ', 'I', 'L', 'Erzya', false, '');
INSERT INTO languages_iso639 VALUES ('myw', '   ', '   ', '  ', 'I', 'L', 'Muyuw', false, '');
INSERT INTO languages_iso639 VALUES ('myx', '   ', '   ', '  ', 'I', 'L', 'Masaaba', false, '');
INSERT INTO languages_iso639 VALUES ('myy', '   ', '   ', '  ', 'I', 'L', 'Macuna', false, '');
INSERT INTO languages_iso639 VALUES ('myz', '   ', '   ', '  ', 'I', 'H', 'Classical Mandaic', false, '');
INSERT INTO languages_iso639 VALUES ('mza', '   ', '   ', '  ', 'I', 'L', 'Santa María Zacatepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('mzb', '   ', '   ', '  ', 'I', 'L', 'Tumzabt', false, '');
INSERT INTO languages_iso639 VALUES ('mzc', '   ', '   ', '  ', 'I', 'L', 'Madagascar Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mzd', '   ', '   ', '  ', 'I', 'L', 'Malimba', false, '');
INSERT INTO languages_iso639 VALUES ('mze', '   ', '   ', '  ', 'I', 'L', 'Morawa', false, '');
INSERT INTO languages_iso639 VALUES ('mzg', '   ', '   ', '  ', 'I', 'L', 'Monastic Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mzh', '   ', '   ', '  ', 'I', 'L', 'Wichí Lhamtés Güisnay', false, '');
INSERT INTO languages_iso639 VALUES ('mzi', '   ', '   ', '  ', 'I', 'L', 'Ixcatlán Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('mzj', '   ', '   ', '  ', 'I', 'L', 'Manya', false, '');
INSERT INTO languages_iso639 VALUES ('mzk', '   ', '   ', '  ', 'I', 'L', 'Nigeria Mambila', false, '');
INSERT INTO languages_iso639 VALUES ('mzl', '   ', '   ', '  ', 'I', 'L', 'Mazatlán Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('mzm', '   ', '   ', '  ', 'I', 'L', 'Mumuye', false, '');
INSERT INTO languages_iso639 VALUES ('mzn', '   ', '   ', '  ', 'I', 'L', 'Mazanderani', false, '');
INSERT INTO languages_iso639 VALUES ('mzo', '   ', '   ', '  ', 'I', 'E', 'Matipuhy', false, '');
INSERT INTO languages_iso639 VALUES ('mzp', '   ', '   ', '  ', 'I', 'L', 'Movima', false, '');
INSERT INTO languages_iso639 VALUES ('mzq', '   ', '   ', '  ', 'I', 'L', 'Mori Atas', false, '');
INSERT INTO languages_iso639 VALUES ('mzr', '   ', '   ', '  ', 'I', 'L', 'Marúbo', false, '');
INSERT INTO languages_iso639 VALUES ('mzs', '   ', '   ', '  ', 'I', 'L', 'Macanese', false, '');
INSERT INTO languages_iso639 VALUES ('mzt', '   ', '   ', '  ', 'I', 'L', 'Mintil', false, '');
INSERT INTO languages_iso639 VALUES ('mzu', '   ', '   ', '  ', 'I', 'L', 'Inapang', false, '');
INSERT INTO languages_iso639 VALUES ('mzv', '   ', '   ', '  ', 'I', 'L', 'Manza', false, '');
INSERT INTO languages_iso639 VALUES ('mzw', '   ', '   ', '  ', 'I', 'L', 'Deg', false, '');
INSERT INTO languages_iso639 VALUES ('mzx', '   ', '   ', '  ', 'I', 'L', 'Mawayana', false, '');
INSERT INTO languages_iso639 VALUES ('mzy', '   ', '   ', '  ', 'I', 'L', 'Mozambican Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('mzz', '   ', '   ', '  ', 'I', 'L', 'Maiadomu', false, '');
INSERT INTO languages_iso639 VALUES ('naa', '   ', '   ', '  ', 'I', 'L', 'Namla', false, '');
INSERT INTO languages_iso639 VALUES ('nab', '   ', '   ', '  ', 'I', 'L', 'Southern Nambikuára', false, '');
INSERT INTO languages_iso639 VALUES ('nac', '   ', '   ', '  ', 'I', 'L', 'Narak', false, '');
INSERT INTO languages_iso639 VALUES ('nad', '   ', '   ', '  ', 'I', 'L', 'Nijadali', false, '');
INSERT INTO languages_iso639 VALUES ('nae', '   ', '   ', '  ', 'I', 'L', 'Naka''ela', false, '');
INSERT INTO languages_iso639 VALUES ('naf', '   ', '   ', '  ', 'I', 'L', 'Nabak', false, '');
INSERT INTO languages_iso639 VALUES ('nag', '   ', '   ', '  ', 'I', 'L', 'Naga Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('naj', '   ', '   ', '  ', 'I', 'L', 'Nalu', false, '');
INSERT INTO languages_iso639 VALUES ('nak', '   ', '   ', '  ', 'I', 'L', 'Nakanai', false, '');
INSERT INTO languages_iso639 VALUES ('nal', '   ', '   ', '  ', 'I', 'L', 'Nalik', false, '');
INSERT INTO languages_iso639 VALUES ('nam', '   ', '   ', '  ', 'I', 'L', 'Ngan''gityemerri', false, '');
INSERT INTO languages_iso639 VALUES ('nan', '   ', '   ', '  ', 'I', 'L', 'Min Nan Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('nao', '   ', '   ', '  ', 'I', 'L', 'Naaba', false, '');
INSERT INTO languages_iso639 VALUES ('nap', 'nap', 'nap', '  ', 'I', 'L', 'Neapolitan', false, '');
INSERT INTO languages_iso639 VALUES ('naq', '   ', '   ', '  ', 'I', 'L', 'Nama (Namibia)', false, '');
INSERT INTO languages_iso639 VALUES ('nar', '   ', '   ', '  ', 'I', 'L', 'Iguta', false, '');
INSERT INTO languages_iso639 VALUES ('nas', '   ', '   ', '  ', 'I', 'L', 'Naasioi', false, '');
INSERT INTO languages_iso639 VALUES ('nat', '   ', '   ', '  ', 'I', 'L', 'Hungworo', false, '');
INSERT INTO languages_iso639 VALUES ('nau', 'nau', 'nau', 'na', 'I', 'L', 'Nauru', false, '');
INSERT INTO languages_iso639 VALUES ('nav', 'nav', 'nav', 'nv', 'I', 'L', 'Navajo', false, '');
INSERT INTO languages_iso639 VALUES ('naw', '   ', '   ', '  ', 'I', 'L', 'Nawuri', false, '');
INSERT INTO languages_iso639 VALUES ('nax', '   ', '   ', '  ', 'I', 'L', 'Nakwi', false, '');
INSERT INTO languages_iso639 VALUES ('nay', '   ', '   ', '  ', 'I', 'E', 'Narrinyeri', false, '');
INSERT INTO languages_iso639 VALUES ('naz', '   ', '   ', '  ', 'I', 'L', 'Coatepec Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nba', '   ', '   ', '  ', 'I', 'L', 'Nyemba', false, '');
INSERT INTO languages_iso639 VALUES ('nbb', '   ', '   ', '  ', 'I', 'L', 'Ndoe', false, '');
INSERT INTO languages_iso639 VALUES ('nbc', '   ', '   ', '  ', 'I', 'L', 'Chang Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nbd', '   ', '   ', '  ', 'I', 'L', 'Ngbinda', false, '');
INSERT INTO languages_iso639 VALUES ('nbe', '   ', '   ', '  ', 'I', 'L', 'Konyak Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nbg', '   ', '   ', '  ', 'I', 'L', 'Nagarchal', false, '');
INSERT INTO languages_iso639 VALUES ('nbh', '   ', '   ', '  ', 'I', 'L', 'Ngamo', false, '');
INSERT INTO languages_iso639 VALUES ('nbi', '   ', '   ', '  ', 'I', 'L', 'Mao Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nbj', '   ', '   ', '  ', 'I', 'L', 'Ngarinman', false, '');
INSERT INTO languages_iso639 VALUES ('nbk', '   ', '   ', '  ', 'I', 'L', 'Nake', false, '');
INSERT INTO languages_iso639 VALUES ('nbl', 'nbl', 'nbl', 'nr', 'I', 'L', 'South Ndebele', false, '');
INSERT INTO languages_iso639 VALUES ('nbm', '   ', '   ', '  ', 'I', 'L', 'Ngbaka Ma''bo', false, '');
INSERT INTO languages_iso639 VALUES ('nbn', '   ', '   ', '  ', 'I', 'L', 'Kuri', false, '');
INSERT INTO languages_iso639 VALUES ('nbo', '   ', '   ', '  ', 'I', 'L', 'Nkukoli', false, '');
INSERT INTO languages_iso639 VALUES ('nbp', '   ', '   ', '  ', 'I', 'L', 'Nnam', false, '');
INSERT INTO languages_iso639 VALUES ('nbq', '   ', '   ', '  ', 'I', 'L', 'Nggem', false, '');
INSERT INTO languages_iso639 VALUES ('nbr', '   ', '   ', '  ', 'I', 'L', 'Numana-Nunku-Gbantu-Numbu', false, '');
INSERT INTO languages_iso639 VALUES ('nbs', '   ', '   ', '  ', 'I', 'L', 'Namibian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('nbt', '   ', '   ', '  ', 'I', 'L', 'Na', false, '');
INSERT INTO languages_iso639 VALUES ('nbu', '   ', '   ', '  ', 'I', 'L', 'Rongmei Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nbv', '   ', '   ', '  ', 'I', 'L', 'Ngamambo', false, '');
INSERT INTO languages_iso639 VALUES ('nbw', '   ', '   ', '  ', 'I', 'L', 'Southern Ngbandi', false, '');
INSERT INTO languages_iso639 VALUES ('nby', '   ', '   ', '  ', 'I', 'L', 'Ningera', false, '');
INSERT INTO languages_iso639 VALUES ('nca', '   ', '   ', '  ', 'I', 'L', 'Iyo', false, '');
INSERT INTO languages_iso639 VALUES ('ncb', '   ', '   ', '  ', 'I', 'L', 'Central Nicobarese', false, '');
INSERT INTO languages_iso639 VALUES ('ncc', '   ', '   ', '  ', 'I', 'L', 'Ponam', false, '');
INSERT INTO languages_iso639 VALUES ('ncd', '   ', '   ', '  ', 'I', 'L', 'Nachering', false, '');
INSERT INTO languages_iso639 VALUES ('nce', '   ', '   ', '  ', 'I', 'L', 'Yale', false, '');
INSERT INTO languages_iso639 VALUES ('ncf', '   ', '   ', '  ', 'I', 'L', 'Notsi', false, '');
INSERT INTO languages_iso639 VALUES ('ncg', '   ', '   ', '  ', 'I', 'L', 'Nisga''a', false, '');
INSERT INTO languages_iso639 VALUES ('nch', '   ', '   ', '  ', 'I', 'L', 'Central Huasteca Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nci', '   ', '   ', '  ', 'I', 'H', 'Classical Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('ncj', '   ', '   ', '  ', 'I', 'L', 'Northern Puebla Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nck', '   ', '   ', '  ', 'I', 'L', 'Nakara', false, '');
INSERT INTO languages_iso639 VALUES ('ncl', '   ', '   ', '  ', 'I', 'L', 'Michoacán Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('ncm', '   ', '   ', '  ', 'I', 'L', 'Nambo', false, '');
INSERT INTO languages_iso639 VALUES ('ncn', '   ', '   ', '  ', 'I', 'L', 'Nauna', false, '');
INSERT INTO languages_iso639 VALUES ('nco', '   ', '   ', '  ', 'I', 'L', 'Sibe', false, '');
INSERT INTO languages_iso639 VALUES ('ncp', '   ', '   ', '  ', 'I', 'L', 'Ndaktup', false, '');
INSERT INTO languages_iso639 VALUES ('ncr', '   ', '   ', '  ', 'I', 'L', 'Ncane', false, '');
INSERT INTO languages_iso639 VALUES ('ncs', '   ', '   ', '  ', 'I', 'L', 'Nicaraguan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('nct', '   ', '   ', '  ', 'I', 'L', 'Chothe Naga', false, '');
INSERT INTO languages_iso639 VALUES ('ncu', '   ', '   ', '  ', 'I', 'L', 'Chumburung', false, '');
INSERT INTO languages_iso639 VALUES ('ncx', '   ', '   ', '  ', 'I', 'L', 'Central Puebla Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('ncz', '   ', '   ', '  ', 'I', 'E', 'Natchez', false, '');
INSERT INTO languages_iso639 VALUES ('nda', '   ', '   ', '  ', 'I', 'L', 'Ndasa', false, '');
INSERT INTO languages_iso639 VALUES ('ndb', '   ', '   ', '  ', 'I', 'L', 'Kenswei Nsei', false, '');
INSERT INTO languages_iso639 VALUES ('ndc', '   ', '   ', '  ', 'I', 'L', 'Ndau', false, '');
INSERT INTO languages_iso639 VALUES ('ndd', '   ', '   ', '  ', 'I', 'L', 'Nde-Nsele-Nta', false, '');
INSERT INTO languages_iso639 VALUES ('nde', 'nde', 'nde', 'nd', 'I', 'L', 'North Ndebele', false, '');
INSERT INTO languages_iso639 VALUES ('ndf', '   ', '   ', '  ', 'I', 'E', 'Nadruvian', false, '');
INSERT INTO languages_iso639 VALUES ('ndg', '   ', '   ', '  ', 'I', 'L', 'Ndengereko', false, '');
INSERT INTO languages_iso639 VALUES ('ndh', '   ', '   ', '  ', 'I', 'L', 'Ndali', false, '');
INSERT INTO languages_iso639 VALUES ('ndi', '   ', '   ', '  ', 'I', 'L', 'Samba Leko', false, '');
INSERT INTO languages_iso639 VALUES ('ndj', '   ', '   ', '  ', 'I', 'L', 'Ndamba', false, '');
INSERT INTO languages_iso639 VALUES ('ndk', '   ', '   ', '  ', 'I', 'L', 'Ndaka', false, '');
INSERT INTO languages_iso639 VALUES ('ndl', '   ', '   ', '  ', 'I', 'L', 'Ndolo', false, '');
INSERT INTO languages_iso639 VALUES ('ndm', '   ', '   ', '  ', 'I', 'L', 'Ndam', false, '');
INSERT INTO languages_iso639 VALUES ('ndn', '   ', '   ', '  ', 'I', 'L', 'Ngundi', false, '');
INSERT INTO languages_iso639 VALUES ('ndo', 'ndo', 'ndo', 'ng', 'I', 'L', 'Ndonga', false, '');
INSERT INTO languages_iso639 VALUES ('ndp', '   ', '   ', '  ', 'I', 'L', 'Ndo', false, '');
INSERT INTO languages_iso639 VALUES ('ndq', '   ', '   ', '  ', 'I', 'L', 'Ndombe', false, '');
INSERT INTO languages_iso639 VALUES ('ndr', '   ', '   ', '  ', 'I', 'L', 'Ndoola', false, '');
INSERT INTO languages_iso639 VALUES ('nds', 'nds', 'nds', '  ', 'I', 'L', 'Low German', false, '');
INSERT INTO languages_iso639 VALUES ('ndt', '   ', '   ', '  ', 'I', 'L', 'Ndunga', false, '');
INSERT INTO languages_iso639 VALUES ('ndu', '   ', '   ', '  ', 'I', 'L', 'Dugun', false, '');
INSERT INTO languages_iso639 VALUES ('ndv', '   ', '   ', '  ', 'I', 'L', 'Ndut', false, '');
INSERT INTO languages_iso639 VALUES ('ndw', '   ', '   ', '  ', 'I', 'L', 'Ndobo', false, '');
INSERT INTO languages_iso639 VALUES ('ndx', '   ', '   ', '  ', 'I', 'L', 'Nduga', false, '');
INSERT INTO languages_iso639 VALUES ('ndy', '   ', '   ', '  ', 'I', 'L', 'Lutos', false, '');
INSERT INTO languages_iso639 VALUES ('ndz', '   ', '   ', '  ', 'I', 'L', 'Ndogo', false, '');
INSERT INTO languages_iso639 VALUES ('nea', '   ', '   ', '  ', 'I', 'L', 'Eastern Ngad''a', false, '');
INSERT INTO languages_iso639 VALUES ('neb', '   ', '   ', '  ', 'I', 'L', 'Toura (Côte d''Ivoire)', false, '');
INSERT INTO languages_iso639 VALUES ('nec', '   ', '   ', '  ', 'I', 'L', 'Nedebang', false, '');
INSERT INTO languages_iso639 VALUES ('ned', '   ', '   ', '  ', 'I', 'L', 'Nde-Gbite', false, '');
INSERT INTO languages_iso639 VALUES ('nee', '   ', '   ', '  ', 'I', 'L', 'Nêlêmwa-Nixumwak', false, '');
INSERT INTO languages_iso639 VALUES ('nef', '   ', '   ', '  ', 'I', 'L', 'Nefamese', false, '');
INSERT INTO languages_iso639 VALUES ('neg', '   ', '   ', '  ', 'I', 'L', 'Negidal', false, '');
INSERT INTO languages_iso639 VALUES ('neh', '   ', '   ', '  ', 'I', 'L', 'Nyenkha', false, '');
INSERT INTO languages_iso639 VALUES ('nei', '   ', '   ', '  ', 'I', 'A', 'Neo-Hittite', false, '');
INSERT INTO languages_iso639 VALUES ('nej', '   ', '   ', '  ', 'I', 'L', 'Neko', false, '');
INSERT INTO languages_iso639 VALUES ('nek', '   ', '   ', '  ', 'I', 'L', 'Neku', false, '');
INSERT INTO languages_iso639 VALUES ('nem', '   ', '   ', '  ', 'I', 'L', 'Nemi', false, '');
INSERT INTO languages_iso639 VALUES ('nen', '   ', '   ', '  ', 'I', 'L', 'Nengone', false, '');
INSERT INTO languages_iso639 VALUES ('neo', '   ', '   ', '  ', 'I', 'L', 'Ná-Meo', false, '');
INSERT INTO languages_iso639 VALUES ('nep', 'nep', 'nep', 'ne', 'M', 'L', 'Nepali (macrolanguage)', false, '');
INSERT INTO languages_iso639 VALUES ('neq', '   ', '   ', '  ', 'I', 'L', 'North Central Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('ner', '   ', '   ', '  ', 'I', 'L', 'Yahadian', false, '');
INSERT INTO languages_iso639 VALUES ('nes', '   ', '   ', '  ', 'I', 'L', 'Bhoti Kinnauri', false, '');
INSERT INTO languages_iso639 VALUES ('net', '   ', '   ', '  ', 'I', 'L', 'Nete', false, '');
INSERT INTO languages_iso639 VALUES ('neu', '   ', '   ', '  ', 'I', 'C', 'Neo', false, '');
INSERT INTO languages_iso639 VALUES ('nev', '   ', '   ', '  ', 'I', 'L', 'Nyaheun', false, '');
INSERT INTO languages_iso639 VALUES ('new', 'new', 'new', '  ', 'I', 'L', 'Newari', false, '');
INSERT INTO languages_iso639 VALUES ('nex', '   ', '   ', '  ', 'I', 'L', 'Neme', false, '');
INSERT INTO languages_iso639 VALUES ('ney', '   ', '   ', '  ', 'I', 'L', 'Neyo', false, '');
INSERT INTO languages_iso639 VALUES ('nez', '   ', '   ', '  ', 'I', 'L', 'Nez Perce', false, '');
INSERT INTO languages_iso639 VALUES ('nfa', '   ', '   ', '  ', 'I', 'L', 'Dhao', false, '');
INSERT INTO languages_iso639 VALUES ('nfd', '   ', '   ', '  ', 'I', 'L', 'Ahwai', false, '');
INSERT INTO languages_iso639 VALUES ('nfl', '   ', '   ', '  ', 'I', 'L', 'Ayiwo', false, '');
INSERT INTO languages_iso639 VALUES ('nfr', '   ', '   ', '  ', 'I', 'L', 'Nafaanra', false, '');
INSERT INTO languages_iso639 VALUES ('nfu', '   ', '   ', '  ', 'I', 'L', 'Mfumte', false, '');
INSERT INTO languages_iso639 VALUES ('nga', '   ', '   ', '  ', 'I', 'L', 'Ngbaka', false, '');
INSERT INTO languages_iso639 VALUES ('ngb', '   ', '   ', '  ', 'I', 'L', 'Northern Ngbandi', false, '');
INSERT INTO languages_iso639 VALUES ('ngc', '   ', '   ', '  ', 'I', 'L', 'Ngombe (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('ngd', '   ', '   ', '  ', 'I', 'L', 'Ngando (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('nge', '   ', '   ', '  ', 'I', 'L', 'Ngemba', false, '');
INSERT INTO languages_iso639 VALUES ('ngg', '   ', '   ', '  ', 'I', 'L', 'Ngbaka Manza', false, '');
INSERT INTO languages_iso639 VALUES ('ngh', '   ', '   ', '  ', 'I', 'L', 'N/u', false, '');
INSERT INTO languages_iso639 VALUES ('ngi', '   ', '   ', '  ', 'I', 'L', 'Ngizim', false, '');
INSERT INTO languages_iso639 VALUES ('ngj', '   ', '   ', '  ', 'I', 'L', 'Ngie', false, '');
INSERT INTO languages_iso639 VALUES ('ngk', '   ', '   ', '  ', 'I', 'L', 'Dalabon', false, '');
INSERT INTO languages_iso639 VALUES ('ngl', '   ', '   ', '  ', 'I', 'L', 'Lomwe', false, '');
INSERT INTO languages_iso639 VALUES ('ngm', '   ', '   ', '  ', 'I', 'L', 'Ngatik Men''s Creole', false, '');
INSERT INTO languages_iso639 VALUES ('ngn', '   ', '   ', '  ', 'I', 'L', 'Ngwo', false, '');
INSERT INTO languages_iso639 VALUES ('ngo', '   ', '   ', '  ', 'I', 'L', 'Ngoni', false, '');
INSERT INTO languages_iso639 VALUES ('ngp', '   ', '   ', '  ', 'I', 'L', 'Ngulu', false, '');
INSERT INTO languages_iso639 VALUES ('ngq', '   ', '   ', '  ', 'I', 'L', 'Ngurimi', false, '');
INSERT INTO languages_iso639 VALUES ('ngr', '   ', '   ', '  ', 'I', 'L', 'Engdewu', false, '');
INSERT INTO languages_iso639 VALUES ('ngs', '   ', '   ', '  ', 'I', 'L', 'Gvoko', false, '');
INSERT INTO languages_iso639 VALUES ('ngt', '   ', '   ', '  ', 'I', 'L', 'Ngeq', false, '');
INSERT INTO languages_iso639 VALUES ('ngu', '   ', '   ', '  ', 'I', 'L', 'Guerrero Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('ngv', '   ', '   ', '  ', 'I', 'E', 'Nagumi', false, '');
INSERT INTO languages_iso639 VALUES ('ngw', '   ', '   ', '  ', 'I', 'L', 'Ngwaba', false, '');
INSERT INTO languages_iso639 VALUES ('ngx', '   ', '   ', '  ', 'I', 'L', 'Nggwahyi', false, '');
INSERT INTO languages_iso639 VALUES ('ngy', '   ', '   ', '  ', 'I', 'L', 'Tibea', false, '');
INSERT INTO languages_iso639 VALUES ('ngz', '   ', '   ', '  ', 'I', 'L', 'Ngungwel', false, '');
INSERT INTO languages_iso639 VALUES ('nha', '   ', '   ', '  ', 'I', 'L', 'Nhanda', false, '');
INSERT INTO languages_iso639 VALUES ('nhb', '   ', '   ', '  ', 'I', 'L', 'Beng', false, '');
INSERT INTO languages_iso639 VALUES ('nhc', '   ', '   ', '  ', 'I', 'E', 'Tabasco Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhd', '   ', '   ', '  ', 'I', 'L', 'Chiripá', false, '');
INSERT INTO languages_iso639 VALUES ('nhe', '   ', '   ', '  ', 'I', 'L', 'Eastern Huasteca Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhf', '   ', '   ', '  ', 'I', 'L', 'Nhuwala', false, '');
INSERT INTO languages_iso639 VALUES ('nhg', '   ', '   ', '  ', 'I', 'L', 'Tetelcingo Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhh', '   ', '   ', '  ', 'I', 'L', 'Nahari', false, '');
INSERT INTO languages_iso639 VALUES ('nhi', '   ', '   ', '  ', 'I', 'L', 'Zacatlán-Ahuacatlán-Tepetzintla Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhk', '   ', '   ', '  ', 'I', 'L', 'Isthmus-Cosoleacaque Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhm', '   ', '   ', '  ', 'I', 'L', 'Morelos Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhn', '   ', '   ', '  ', 'I', 'L', 'Central Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nho', '   ', '   ', '  ', 'I', 'L', 'Takuu', false, '');
INSERT INTO languages_iso639 VALUES ('nhp', '   ', '   ', '  ', 'I', 'L', 'Isthmus-Pajapan Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhq', '   ', '   ', '  ', 'I', 'L', 'Huaxcaleca Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhr', '   ', '   ', '  ', 'I', 'L', 'Naro', false, '');
INSERT INTO languages_iso639 VALUES ('nht', '   ', '   ', '  ', 'I', 'L', 'Ometepec Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhu', '   ', '   ', '  ', 'I', 'L', 'Noone', false, '');
INSERT INTO languages_iso639 VALUES ('nhv', '   ', '   ', '  ', 'I', 'L', 'Temascaltepec Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhw', '   ', '   ', '  ', 'I', 'L', 'Western Huasteca Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhx', '   ', '   ', '  ', 'I', 'L', 'Isthmus-Mecayapan Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhy', '   ', '   ', '  ', 'I', 'L', 'Northern Oaxaca Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nhz', '   ', '   ', '  ', 'I', 'L', 'Santa María La Alta Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nia', 'nia', 'nia', '  ', 'I', 'L', 'Nias', false, '');
INSERT INTO languages_iso639 VALUES ('nib', '   ', '   ', '  ', 'I', 'L', 'Nakame', false, '');
INSERT INTO languages_iso639 VALUES ('nid', '   ', '   ', '  ', 'I', 'E', 'Ngandi', false, '');
INSERT INTO languages_iso639 VALUES ('nie', '   ', '   ', '  ', 'I', 'L', 'Niellim', false, '');
INSERT INTO languages_iso639 VALUES ('nif', '   ', '   ', '  ', 'I', 'L', 'Nek', false, '');
INSERT INTO languages_iso639 VALUES ('nig', '   ', '   ', '  ', 'I', 'E', 'Ngalakan', false, '');
INSERT INTO languages_iso639 VALUES ('nih', '   ', '   ', '  ', 'I', 'L', 'Nyiha (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('nii', '   ', '   ', '  ', 'I', 'L', 'Nii', false, '');
INSERT INTO languages_iso639 VALUES ('nij', '   ', '   ', '  ', 'I', 'L', 'Ngaju', false, '');
INSERT INTO languages_iso639 VALUES ('nik', '   ', '   ', '  ', 'I', 'L', 'Southern Nicobarese', false, '');
INSERT INTO languages_iso639 VALUES ('nil', '   ', '   ', '  ', 'I', 'L', 'Nila', false, '');
INSERT INTO languages_iso639 VALUES ('nim', '   ', '   ', '  ', 'I', 'L', 'Nilamba', false, '');
INSERT INTO languages_iso639 VALUES ('nin', '   ', '   ', '  ', 'I', 'L', 'Ninzo', false, '');
INSERT INTO languages_iso639 VALUES ('nio', '   ', '   ', '  ', 'I', 'L', 'Nganasan', false, '');
INSERT INTO languages_iso639 VALUES ('niq', '   ', '   ', '  ', 'I', 'L', 'Nandi', false, '');
INSERT INTO languages_iso639 VALUES ('nir', '   ', '   ', '  ', 'I', 'L', 'Nimboran', false, '');
INSERT INTO languages_iso639 VALUES ('nis', '   ', '   ', '  ', 'I', 'L', 'Nimi', false, '');
INSERT INTO languages_iso639 VALUES ('nit', '   ', '   ', '  ', 'I', 'L', 'Southeastern Kolami', false, '');
INSERT INTO languages_iso639 VALUES ('niu', 'niu', 'niu', '  ', 'I', 'L', 'Niuean', false, '');
INSERT INTO languages_iso639 VALUES ('niv', '   ', '   ', '  ', 'I', 'L', 'Gilyak', false, '');
INSERT INTO languages_iso639 VALUES ('niw', '   ', '   ', '  ', 'I', 'L', 'Nimo', false, '');
INSERT INTO languages_iso639 VALUES ('nix', '   ', '   ', '  ', 'I', 'L', 'Hema', false, '');
INSERT INTO languages_iso639 VALUES ('niy', '   ', '   ', '  ', 'I', 'L', 'Ngiti', false, '');
INSERT INTO languages_iso639 VALUES ('niz', '   ', '   ', '  ', 'I', 'L', 'Ningil', false, '');
INSERT INTO languages_iso639 VALUES ('nja', '   ', '   ', '  ', 'I', 'L', 'Nzanyi', false, '');
INSERT INTO languages_iso639 VALUES ('njb', '   ', '   ', '  ', 'I', 'L', 'Nocte Naga', false, '');
INSERT INTO languages_iso639 VALUES ('njd', '   ', '   ', '  ', 'I', 'L', 'Ndonde Hamba', false, '');
INSERT INTO languages_iso639 VALUES ('njh', '   ', '   ', '  ', 'I', 'L', 'Lotha Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nji', '   ', '   ', '  ', 'I', 'L', 'Gudanji', false, '');
INSERT INTO languages_iso639 VALUES ('njj', '   ', '   ', '  ', 'I', 'L', 'Njen', false, '');
INSERT INTO languages_iso639 VALUES ('njl', '   ', '   ', '  ', 'I', 'L', 'Njalgulgule', false, '');
INSERT INTO languages_iso639 VALUES ('njm', '   ', '   ', '  ', 'I', 'L', 'Angami Naga', false, '');
INSERT INTO languages_iso639 VALUES ('njn', '   ', '   ', '  ', 'I', 'L', 'Liangmai Naga', false, '');
INSERT INTO languages_iso639 VALUES ('njo', '   ', '   ', '  ', 'I', 'L', 'Ao Naga', false, '');
INSERT INTO languages_iso639 VALUES ('njr', '   ', '   ', '  ', 'I', 'L', 'Njerep', false, '');
INSERT INTO languages_iso639 VALUES ('njs', '   ', '   ', '  ', 'I', 'L', 'Nisa', false, '');
INSERT INTO languages_iso639 VALUES ('njt', '   ', '   ', '  ', 'I', 'L', 'Ndyuka-Trio Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('nju', '   ', '   ', '  ', 'I', 'L', 'Ngadjunmaya', false, '');
INSERT INTO languages_iso639 VALUES ('njx', '   ', '   ', '  ', 'I', 'L', 'Kunyi', false, '');
INSERT INTO languages_iso639 VALUES ('njy', '   ', '   ', '  ', 'I', 'L', 'Njyem', false, '');
INSERT INTO languages_iso639 VALUES ('njz', '   ', '   ', '  ', 'I', 'L', 'Nyishi', false, '');
INSERT INTO languages_iso639 VALUES ('nka', '   ', '   ', '  ', 'I', 'L', 'Nkoya', false, '');
INSERT INTO languages_iso639 VALUES ('nkb', '   ', '   ', '  ', 'I', 'L', 'Khoibu Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nkc', '   ', '   ', '  ', 'I', 'L', 'Nkongho', false, '');
INSERT INTO languages_iso639 VALUES ('nkd', '   ', '   ', '  ', 'I', 'L', 'Koireng', false, '');
INSERT INTO languages_iso639 VALUES ('nke', '   ', '   ', '  ', 'I', 'L', 'Duke', false, '');
INSERT INTO languages_iso639 VALUES ('nkf', '   ', '   ', '  ', 'I', 'L', 'Inpui Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nkg', '   ', '   ', '  ', 'I', 'L', 'Nekgini', false, '');
INSERT INTO languages_iso639 VALUES ('nkh', '   ', '   ', '  ', 'I', 'L', 'Khezha Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nki', '   ', '   ', '  ', 'I', 'L', 'Thangal Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nkj', '   ', '   ', '  ', 'I', 'L', 'Nakai', false, '');
INSERT INTO languages_iso639 VALUES ('nkk', '   ', '   ', '  ', 'I', 'L', 'Nokuku', false, '');
INSERT INTO languages_iso639 VALUES ('nkm', '   ', '   ', '  ', 'I', 'L', 'Namat', false, '');
INSERT INTO languages_iso639 VALUES ('nkn', '   ', '   ', '  ', 'I', 'L', 'Nkangala', false, '');
INSERT INTO languages_iso639 VALUES ('nko', '   ', '   ', '  ', 'I', 'L', 'Nkonya', false, '');
INSERT INTO languages_iso639 VALUES ('nkp', '   ', '   ', '  ', 'I', 'E', 'Niuatoputapu', false, '');
INSERT INTO languages_iso639 VALUES ('nkq', '   ', '   ', '  ', 'I', 'L', 'Nkami', false, '');
INSERT INTO languages_iso639 VALUES ('nkr', '   ', '   ', '  ', 'I', 'L', 'Nukuoro', false, '');
INSERT INTO languages_iso639 VALUES ('nks', '   ', '   ', '  ', 'I', 'L', 'North Asmat', false, '');
INSERT INTO languages_iso639 VALUES ('nkt', '   ', '   ', '  ', 'I', 'L', 'Nyika (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('nku', '   ', '   ', '  ', 'I', 'L', 'Bouna Kulango', false, '');
INSERT INTO languages_iso639 VALUES ('nkv', '   ', '   ', '  ', 'I', 'L', 'Nyika (Malawi and Zambia)', false, '');
INSERT INTO languages_iso639 VALUES ('nkw', '   ', '   ', '  ', 'I', 'L', 'Nkutu', false, '');
INSERT INTO languages_iso639 VALUES ('nkx', '   ', '   ', '  ', 'I', 'L', 'Nkoroo', false, '');
INSERT INTO languages_iso639 VALUES ('nkz', '   ', '   ', '  ', 'I', 'L', 'Nkari', false, '');
INSERT INTO languages_iso639 VALUES ('nla', '   ', '   ', '  ', 'I', 'L', 'Ngombale', false, '');
INSERT INTO languages_iso639 VALUES ('nlc', '   ', '   ', '  ', 'I', 'L', 'Nalca', false, '');
INSERT INTO languages_iso639 VALUES ('nld', 'dut', 'nld', 'nl', 'I', 'L', 'Dutch', false, '');
INSERT INTO languages_iso639 VALUES ('nle', '   ', '   ', '  ', 'I', 'L', 'East Nyala', false, '');
INSERT INTO languages_iso639 VALUES ('nlg', '   ', '   ', '  ', 'I', 'L', 'Gela', false, '');
INSERT INTO languages_iso639 VALUES ('nli', '   ', '   ', '  ', 'I', 'L', 'Grangali', false, '');
INSERT INTO languages_iso639 VALUES ('nlj', '   ', '   ', '  ', 'I', 'L', 'Nyali', false, '');
INSERT INTO languages_iso639 VALUES ('nlk', '   ', '   ', '  ', 'I', 'L', 'Ninia Yali', false, '');
INSERT INTO languages_iso639 VALUES ('nll', '   ', '   ', '  ', 'I', 'L', 'Nihali', false, '');
INSERT INTO languages_iso639 VALUES ('nlo', '   ', '   ', '  ', 'I', 'L', 'Ngul', false, '');
INSERT INTO languages_iso639 VALUES ('nlq', '   ', '   ', '  ', 'I', 'L', 'Lao Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nlu', '   ', '   ', '  ', 'I', 'L', 'Nchumbulu', false, '');
INSERT INTO languages_iso639 VALUES ('nlv', '   ', '   ', '  ', 'I', 'L', 'Orizaba Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nlw', '   ', '   ', '  ', 'I', 'E', 'Walangama', false, '');
INSERT INTO languages_iso639 VALUES ('nlx', '   ', '   ', '  ', 'I', 'L', 'Nahali', false, '');
INSERT INTO languages_iso639 VALUES ('nly', '   ', '   ', '  ', 'I', 'L', 'Nyamal', false, '');
INSERT INTO languages_iso639 VALUES ('nlz', '   ', '   ', '  ', 'I', 'L', 'Nalögo', false, '');
INSERT INTO languages_iso639 VALUES ('nma', '   ', '   ', '  ', 'I', 'L', 'Maram Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nmb', '   ', '   ', '  ', 'I', 'L', 'Big Nambas', false, '');
INSERT INTO languages_iso639 VALUES ('nmc', '   ', '   ', '  ', 'I', 'L', 'Ngam', false, '');
INSERT INTO languages_iso639 VALUES ('nmd', '   ', '   ', '  ', 'I', 'L', 'Ndumu', false, '');
INSERT INTO languages_iso639 VALUES ('nme', '   ', '   ', '  ', 'I', 'L', 'Mzieme Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nmf', '   ', '   ', '  ', 'I', 'L', 'Tangkhul Naga (India)', false, '');
INSERT INTO languages_iso639 VALUES ('nmg', '   ', '   ', '  ', 'I', 'L', 'Kwasio', false, '');
INSERT INTO languages_iso639 VALUES ('nmh', '   ', '   ', '  ', 'I', 'L', 'Monsang Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nmi', '   ', '   ', '  ', 'I', 'L', 'Nyam', false, '');
INSERT INTO languages_iso639 VALUES ('nmj', '   ', '   ', '  ', 'I', 'L', 'Ngombe (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('nmk', '   ', '   ', '  ', 'I', 'L', 'Namakura', false, '');
INSERT INTO languages_iso639 VALUES ('nml', '   ', '   ', '  ', 'I', 'L', 'Ndemli', false, '');
INSERT INTO languages_iso639 VALUES ('nmm', '   ', '   ', '  ', 'I', 'L', 'Manangba', false, '');
INSERT INTO languages_iso639 VALUES ('nmn', '   ', '   ', '  ', 'I', 'L', '!Xóõ', false, '');
INSERT INTO languages_iso639 VALUES ('nmo', '   ', '   ', '  ', 'I', 'L', 'Moyon Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nmp', '   ', '   ', '  ', 'I', 'E', 'Nimanbur', false, '');
INSERT INTO languages_iso639 VALUES ('nmq', '   ', '   ', '  ', 'I', 'L', 'Nambya', false, '');
INSERT INTO languages_iso639 VALUES ('nmr', '   ', '   ', '  ', 'I', 'E', 'Nimbari', false, '');
INSERT INTO languages_iso639 VALUES ('nms', '   ', '   ', '  ', 'I', 'L', 'Letemboi', false, '');
INSERT INTO languages_iso639 VALUES ('nmt', '   ', '   ', '  ', 'I', 'L', 'Namonuito', false, '');
INSERT INTO languages_iso639 VALUES ('nmu', '   ', '   ', '  ', 'I', 'L', 'Northeast Maidu', false, '');
INSERT INTO languages_iso639 VALUES ('nmv', '   ', '   ', '  ', 'I', 'E', 'Ngamini', false, '');
INSERT INTO languages_iso639 VALUES ('nmw', '   ', '   ', '  ', 'I', 'L', 'Nimoa', false, '');
INSERT INTO languages_iso639 VALUES ('nmx', '   ', '   ', '  ', 'I', 'L', 'Nama (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('nmy', '   ', '   ', '  ', 'I', 'L', 'Namuyi', false, '');
INSERT INTO languages_iso639 VALUES ('nmz', '   ', '   ', '  ', 'I', 'L', 'Nawdm', false, '');
INSERT INTO languages_iso639 VALUES ('nna', '   ', '   ', '  ', 'I', 'L', 'Nyangumarta', false, '');
INSERT INTO languages_iso639 VALUES ('nnb', '   ', '   ', '  ', 'I', 'L', 'Nande', false, '');
INSERT INTO languages_iso639 VALUES ('nnc', '   ', '   ', '  ', 'I', 'L', 'Nancere', false, '');
INSERT INTO languages_iso639 VALUES ('nnd', '   ', '   ', '  ', 'I', 'L', 'West Ambae', false, '');
INSERT INTO languages_iso639 VALUES ('nne', '   ', '   ', '  ', 'I', 'L', 'Ngandyera', false, '');
INSERT INTO languages_iso639 VALUES ('nnf', '   ', '   ', '  ', 'I', 'L', 'Ngaing', false, '');
INSERT INTO languages_iso639 VALUES ('nng', '   ', '   ', '  ', 'I', 'L', 'Maring Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nnh', '   ', '   ', '  ', 'I', 'L', 'Ngiemboon', false, '');
INSERT INTO languages_iso639 VALUES ('nni', '   ', '   ', '  ', 'I', 'L', 'North Nuaulu', false, '');
INSERT INTO languages_iso639 VALUES ('nnj', '   ', '   ', '  ', 'I', 'L', 'Nyangatom', false, '');
INSERT INTO languages_iso639 VALUES ('nnk', '   ', '   ', '  ', 'I', 'L', 'Nankina', false, '');
INSERT INTO languages_iso639 VALUES ('nnl', '   ', '   ', '  ', 'I', 'L', 'Northern Rengma Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nnm', '   ', '   ', '  ', 'I', 'L', 'Namia', false, '');
INSERT INTO languages_iso639 VALUES ('nnn', '   ', '   ', '  ', 'I', 'L', 'Ngete', false, '');
INSERT INTO languages_iso639 VALUES ('nno', 'nno', 'nno', 'nn', 'I', 'L', 'Norwegian Nynorsk', false, '');
INSERT INTO languages_iso639 VALUES ('nnp', '   ', '   ', '  ', 'I', 'L', 'Wancho Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nnq', '   ', '   ', '  ', 'I', 'L', 'Ngindo', false, '');
INSERT INTO languages_iso639 VALUES ('nnr', '   ', '   ', '  ', 'I', 'E', 'Narungga', false, '');
INSERT INTO languages_iso639 VALUES ('nns', '   ', '   ', '  ', 'I', 'L', 'Ningye', false, '');
INSERT INTO languages_iso639 VALUES ('nnt', '   ', '   ', '  ', 'I', 'E', 'Nanticoke', false, '');
INSERT INTO languages_iso639 VALUES ('nnu', '   ', '   ', '  ', 'I', 'L', 'Dwang', false, '');
INSERT INTO languages_iso639 VALUES ('nnv', '   ', '   ', '  ', 'I', 'E', 'Nugunu (Australia)', false, '');
INSERT INTO languages_iso639 VALUES ('nnw', '   ', '   ', '  ', 'I', 'L', 'Southern Nuni', false, '');
INSERT INTO languages_iso639 VALUES ('nnx', '   ', '   ', '  ', 'I', 'L', 'Ngong', false, '');
INSERT INTO languages_iso639 VALUES ('nny', '   ', '   ', '  ', 'I', 'E', 'Nyangga', false, '');
INSERT INTO languages_iso639 VALUES ('nnz', '   ', '   ', '  ', 'I', 'L', 'Nda''nda''', false, '');
INSERT INTO languages_iso639 VALUES ('noa', '   ', '   ', '  ', 'I', 'L', 'Woun Meu', false, '');
INSERT INTO languages_iso639 VALUES ('nob', 'nob', 'nob', 'nb', 'I', 'L', 'Norwegian Bokmål', false, '');
INSERT INTO languages_iso639 VALUES ('noc', '   ', '   ', '  ', 'I', 'L', 'Nuk', false, '');
INSERT INTO languages_iso639 VALUES ('nod', '   ', '   ', '  ', 'I', 'L', 'Northern Thai', false, '');
INSERT INTO languages_iso639 VALUES ('noe', '   ', '   ', '  ', 'I', 'L', 'Nimadi', false, '');
INSERT INTO languages_iso639 VALUES ('nof', '   ', '   ', '  ', 'I', 'L', 'Nomane', false, '');
INSERT INTO languages_iso639 VALUES ('nog', 'nog', 'nog', '  ', 'I', 'L', 'Nogai', false, '');
INSERT INTO languages_iso639 VALUES ('noh', '   ', '   ', '  ', 'I', 'L', 'Nomu', false, '');
INSERT INTO languages_iso639 VALUES ('noi', '   ', '   ', '  ', 'I', 'L', 'Noiri', false, '');
INSERT INTO languages_iso639 VALUES ('noj', '   ', '   ', '  ', 'I', 'L', 'Nonuya', false, '');
INSERT INTO languages_iso639 VALUES ('nok', '   ', '   ', '  ', 'I', 'E', 'Nooksack', false, '');
INSERT INTO languages_iso639 VALUES ('nol', '   ', '   ', '  ', 'I', 'E', 'Nomlaki', false, '');
INSERT INTO languages_iso639 VALUES ('nom', '   ', '   ', '  ', 'I', 'E', 'Nocamán', false, '');
INSERT INTO languages_iso639 VALUES ('non', 'non', 'non', '  ', 'I', 'H', 'Old Norse', false, '');
INSERT INTO languages_iso639 VALUES ('nop', '   ', '   ', '  ', 'I', 'L', 'Numanggang', false, '');
INSERT INTO languages_iso639 VALUES ('noq', '   ', '   ', '  ', 'I', 'L', 'Ngongo', false, '');
INSERT INTO languages_iso639 VALUES ('nor', 'nor', 'nor', 'no', 'M', 'L', 'Norwegian', false, '');
INSERT INTO languages_iso639 VALUES ('nos', '   ', '   ', '  ', 'I', 'L', 'Eastern Nisu', false, '');
INSERT INTO languages_iso639 VALUES ('not', '   ', '   ', '  ', 'I', 'L', 'Nomatsiguenga', false, '');
INSERT INTO languages_iso639 VALUES ('nou', '   ', '   ', '  ', 'I', 'L', 'Ewage-Notu', false, '');
INSERT INTO languages_iso639 VALUES ('nov', '   ', '   ', '  ', 'I', 'C', 'Novial', false, '');
INSERT INTO languages_iso639 VALUES ('now', '   ', '   ', '  ', 'I', 'L', 'Nyambo', false, '');
INSERT INTO languages_iso639 VALUES ('noy', '   ', '   ', '  ', 'I', 'L', 'Noy', false, '');
INSERT INTO languages_iso639 VALUES ('noz', '   ', '   ', '  ', 'I', 'L', 'Nayi', false, '');
INSERT INTO languages_iso639 VALUES ('npa', '   ', '   ', '  ', 'I', 'L', 'Nar Phu', false, '');
INSERT INTO languages_iso639 VALUES ('npb', '   ', '   ', '  ', 'I', 'L', 'Nupbikha', false, '');
INSERT INTO languages_iso639 VALUES ('npg', '   ', '   ', '  ', 'I', 'L', 'Ponyo-Gongwang Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nph', '   ', '   ', '  ', 'I', 'L', 'Phom Naga', false, '');
INSERT INTO languages_iso639 VALUES ('npi', '   ', '   ', '  ', 'I', 'L', 'Nepali (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('npl', '   ', '   ', '  ', 'I', 'L', 'Southeastern Puebla Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('npn', '   ', '   ', '  ', 'I', 'L', 'Mondropolon', false, '');
INSERT INTO languages_iso639 VALUES ('npo', '   ', '   ', '  ', 'I', 'L', 'Pochuri Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nps', '   ', '   ', '  ', 'I', 'L', 'Nipsan', false, '');
INSERT INTO languages_iso639 VALUES ('npu', '   ', '   ', '  ', 'I', 'L', 'Puimei Naga', false, '');
INSERT INTO languages_iso639 VALUES ('npy', '   ', '   ', '  ', 'I', 'L', 'Napu', false, '');
INSERT INTO languages_iso639 VALUES ('nqg', '   ', '   ', '  ', 'I', 'L', 'Southern Nago', false, '');
INSERT INTO languages_iso639 VALUES ('nqk', '   ', '   ', '  ', 'I', 'L', 'Kura Ede Nago', false, '');
INSERT INTO languages_iso639 VALUES ('nqm', '   ', '   ', '  ', 'I', 'L', 'Ndom', false, '');
INSERT INTO languages_iso639 VALUES ('nqn', '   ', '   ', '  ', 'I', 'L', 'Nen', false, '');
INSERT INTO languages_iso639 VALUES ('nqo', 'nqo', 'nqo', '  ', 'I', 'L', 'N''Ko', false, '');
INSERT INTO languages_iso639 VALUES ('nqq', '   ', '   ', '  ', 'I', 'L', 'Kyan-Karyaw Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nqy', '   ', '   ', '  ', 'I', 'L', 'Akyaung Ari Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nra', '   ', '   ', '  ', 'I', 'L', 'Ngom', false, '');
INSERT INTO languages_iso639 VALUES ('nrb', '   ', '   ', '  ', 'I', 'L', 'Nara', false, '');
INSERT INTO languages_iso639 VALUES ('nrc', '   ', '   ', '  ', 'I', 'A', 'Noric', false, '');
INSERT INTO languages_iso639 VALUES ('nre', '   ', '   ', '  ', 'I', 'L', 'Southern Rengma Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nrg', '   ', '   ', '  ', 'I', 'L', 'Narango', false, '');
INSERT INTO languages_iso639 VALUES ('nri', '   ', '   ', '  ', 'I', 'L', 'Chokri Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nrk', '   ', '   ', '  ', 'I', 'L', 'Ngarla', false, '');
INSERT INTO languages_iso639 VALUES ('nrl', '   ', '   ', '  ', 'I', 'L', 'Ngarluma', false, '');
INSERT INTO languages_iso639 VALUES ('nrm', '   ', '   ', '  ', 'I', 'L', 'Narom', false, '');
INSERT INTO languages_iso639 VALUES ('nrn', '   ', '   ', '  ', 'I', 'E', 'Norn', false, '');
INSERT INTO languages_iso639 VALUES ('nrp', '   ', '   ', '  ', 'I', 'A', 'North Picene', false, '');
INSERT INTO languages_iso639 VALUES ('nrr', '   ', '   ', '  ', 'I', 'E', 'Norra', false, '');
INSERT INTO languages_iso639 VALUES ('nrt', '   ', '   ', '  ', 'I', 'E', 'Northern Kalapuya', false, '');
INSERT INTO languages_iso639 VALUES ('nru', '   ', '   ', '  ', 'I', 'L', 'Narua', false, '');
INSERT INTO languages_iso639 VALUES ('nrx', '   ', '   ', '  ', 'I', 'E', 'Ngurmbur', false, '');
INSERT INTO languages_iso639 VALUES ('nrz', '   ', '   ', '  ', 'I', 'L', 'Lala', false, '');
INSERT INTO languages_iso639 VALUES ('nsa', '   ', '   ', '  ', 'I', 'L', 'Sangtam Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nsc', '   ', '   ', '  ', 'I', 'L', 'Nshi', false, '');
INSERT INTO languages_iso639 VALUES ('nsd', '   ', '   ', '  ', 'I', 'L', 'Southern Nisu', false, '');
INSERT INTO languages_iso639 VALUES ('nse', '   ', '   ', '  ', 'I', 'L', 'Nsenga', false, '');
INSERT INTO languages_iso639 VALUES ('nsf', '   ', '   ', '  ', 'I', 'L', 'Northwestern Nisu', false, '');
INSERT INTO languages_iso639 VALUES ('nsg', '   ', '   ', '  ', 'I', 'L', 'Ngasa', false, '');
INSERT INTO languages_iso639 VALUES ('nsh', '   ', '   ', '  ', 'I', 'L', 'Ngoshie', false, '');
INSERT INTO languages_iso639 VALUES ('nsi', '   ', '   ', '  ', 'I', 'L', 'Nigerian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('nsk', '   ', '   ', '  ', 'I', 'L', 'Naskapi', false, '');
INSERT INTO languages_iso639 VALUES ('nsl', '   ', '   ', '  ', 'I', 'L', 'Norwegian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('nsm', '   ', '   ', '  ', 'I', 'L', 'Sumi Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nsn', '   ', '   ', '  ', 'I', 'L', 'Nehan', false, '');
INSERT INTO languages_iso639 VALUES ('nso', 'nso', 'nso', '  ', 'I', 'L', 'Pedi', false, '');
INSERT INTO languages_iso639 VALUES ('nsp', '   ', '   ', '  ', 'I', 'L', 'Nepalese Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('nsq', '   ', '   ', '  ', 'I', 'L', 'Northern Sierra Miwok', false, '');
INSERT INTO languages_iso639 VALUES ('nsr', '   ', '   ', '  ', 'I', 'L', 'Maritime Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('nss', '   ', '   ', '  ', 'I', 'L', 'Nali', false, '');
INSERT INTO languages_iso639 VALUES ('nst', '   ', '   ', '  ', 'I', 'L', 'Tase Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nsu', '   ', '   ', '  ', 'I', 'L', 'Sierra Negra Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nsv', '   ', '   ', '  ', 'I', 'L', 'Southwestern Nisu', false, '');
INSERT INTO languages_iso639 VALUES ('nsw', '   ', '   ', '  ', 'I', 'L', 'Navut', false, '');
INSERT INTO languages_iso639 VALUES ('nsx', '   ', '   ', '  ', 'I', 'L', 'Nsongo', false, '');
INSERT INTO languages_iso639 VALUES ('nsy', '   ', '   ', '  ', 'I', 'L', 'Nasal', false, '');
INSERT INTO languages_iso639 VALUES ('nsz', '   ', '   ', '  ', 'I', 'L', 'Nisenan', false, '');
INSERT INTO languages_iso639 VALUES ('nte', '   ', '   ', '  ', 'I', 'L', 'Nathembo', false, '');
INSERT INTO languages_iso639 VALUES ('ntg', '   ', '   ', '  ', 'I', 'E', 'Ngantangarra', false, '');
INSERT INTO languages_iso639 VALUES ('nti', '   ', '   ', '  ', 'I', 'L', 'Natioro', false, '');
INSERT INTO languages_iso639 VALUES ('ntj', '   ', '   ', '  ', 'I', 'L', 'Ngaanyatjarra', false, '');
INSERT INTO languages_iso639 VALUES ('ntk', '   ', '   ', '  ', 'I', 'L', 'Ikoma-Nata-Isenye', false, '');
INSERT INTO languages_iso639 VALUES ('ntm', '   ', '   ', '  ', 'I', 'L', 'Nateni', false, '');
INSERT INTO languages_iso639 VALUES ('nto', '   ', '   ', '  ', 'I', 'L', 'Ntomba', false, '');
INSERT INTO languages_iso639 VALUES ('ntp', '   ', '   ', '  ', 'I', 'L', 'Northern Tepehuan', false, '');
INSERT INTO languages_iso639 VALUES ('ntr', '   ', '   ', '  ', 'I', 'L', 'Delo', false, '');
INSERT INTO languages_iso639 VALUES ('nts', '   ', '   ', '  ', 'I', 'E', 'Natagaimas', false, '');
INSERT INTO languages_iso639 VALUES ('ntu', '   ', '   ', '  ', 'I', 'L', 'Natügu', false, '');
INSERT INTO languages_iso639 VALUES ('ntw', '   ', '   ', '  ', 'I', 'E', 'Nottoway', false, '');
INSERT INTO languages_iso639 VALUES ('ntx', '   ', '   ', '  ', 'I', 'L', 'Tangkhul Naga (Myanmar)', false, '');
INSERT INTO languages_iso639 VALUES ('nty', '   ', '   ', '  ', 'I', 'L', 'Mantsi', false, '');
INSERT INTO languages_iso639 VALUES ('ntz', '   ', '   ', '  ', 'I', 'L', 'Natanzi', false, '');
INSERT INTO languages_iso639 VALUES ('nua', '   ', '   ', '  ', 'I', 'L', 'Yuanga', false, '');
INSERT INTO languages_iso639 VALUES ('nuc', '   ', '   ', '  ', 'I', 'E', 'Nukuini', false, '');
INSERT INTO languages_iso639 VALUES ('nud', '   ', '   ', '  ', 'I', 'L', 'Ngala', false, '');
INSERT INTO languages_iso639 VALUES ('nue', '   ', '   ', '  ', 'I', 'L', 'Ngundu', false, '');
INSERT INTO languages_iso639 VALUES ('nuf', '   ', '   ', '  ', 'I', 'L', 'Nusu', false, '');
INSERT INTO languages_iso639 VALUES ('nug', '   ', '   ', '  ', 'I', 'E', 'Nungali', false, '');
INSERT INTO languages_iso639 VALUES ('nuh', '   ', '   ', '  ', 'I', 'L', 'Ndunda', false, '');
INSERT INTO languages_iso639 VALUES ('nui', '   ', '   ', '  ', 'I', 'L', 'Ngumbi', false, '');
INSERT INTO languages_iso639 VALUES ('nuj', '   ', '   ', '  ', 'I', 'L', 'Nyole', false, '');
INSERT INTO languages_iso639 VALUES ('nuk', '   ', '   ', '  ', 'I', 'L', 'Nuu-chah-nulth', false, '');
INSERT INTO languages_iso639 VALUES ('nul', '   ', '   ', '  ', 'I', 'L', 'Nusa Laut', false, '');
INSERT INTO languages_iso639 VALUES ('num', '   ', '   ', '  ', 'I', 'L', 'Niuafo''ou', false, '');
INSERT INTO languages_iso639 VALUES ('nun', '   ', '   ', '  ', 'I', 'L', 'Anong', false, '');
INSERT INTO languages_iso639 VALUES ('nuo', '   ', '   ', '  ', 'I', 'L', 'Nguôn', false, '');
INSERT INTO languages_iso639 VALUES ('nup', '   ', '   ', '  ', 'I', 'L', 'Nupe-Nupe-Tako', false, '');
INSERT INTO languages_iso639 VALUES ('nuq', '   ', '   ', '  ', 'I', 'L', 'Nukumanu', false, '');
INSERT INTO languages_iso639 VALUES ('nur', '   ', '   ', '  ', 'I', 'L', 'Nukuria', false, '');
INSERT INTO languages_iso639 VALUES ('nus', '   ', '   ', '  ', 'I', 'L', 'Nuer', false, '');
INSERT INTO languages_iso639 VALUES ('nut', '   ', '   ', '  ', 'I', 'L', 'Nung (Viet Nam)', false, '');
INSERT INTO languages_iso639 VALUES ('nuu', '   ', '   ', '  ', 'I', 'L', 'Ngbundu', false, '');
INSERT INTO languages_iso639 VALUES ('nuv', '   ', '   ', '  ', 'I', 'L', 'Northern Nuni', false, '');
INSERT INTO languages_iso639 VALUES ('nuw', '   ', '   ', '  ', 'I', 'L', 'Nguluwan', false, '');
INSERT INTO languages_iso639 VALUES ('nux', '   ', '   ', '  ', 'I', 'L', 'Mehek', false, '');
INSERT INTO languages_iso639 VALUES ('nuy', '   ', '   ', '  ', 'I', 'L', 'Nunggubuyu', false, '');
INSERT INTO languages_iso639 VALUES ('nuz', '   ', '   ', '  ', 'I', 'L', 'Tlamacazapa Nahuatl', false, '');
INSERT INTO languages_iso639 VALUES ('nvh', '   ', '   ', '  ', 'I', 'L', 'Nasarian', false, '');
INSERT INTO languages_iso639 VALUES ('nvm', '   ', '   ', '  ', 'I', 'L', 'Namiae', false, '');
INSERT INTO languages_iso639 VALUES ('nvo', '   ', '   ', '  ', 'I', 'L', 'Nyokon', false, '');
INSERT INTO languages_iso639 VALUES ('nwa', '   ', '   ', '  ', 'I', 'E', 'Nawathinehena', false, '');
INSERT INTO languages_iso639 VALUES ('nwb', '   ', '   ', '  ', 'I', 'L', 'Nyabwa', false, '');
INSERT INTO languages_iso639 VALUES ('nwc', 'nwc', 'nwc', '  ', 'I', 'H', 'Classical Newari', false, '');
INSERT INTO languages_iso639 VALUES ('nwe', '   ', '   ', '  ', 'I', 'L', 'Ngwe', false, '');
INSERT INTO languages_iso639 VALUES ('nwg', '   ', '   ', '  ', 'I', 'E', 'Ngayawung', false, '');
INSERT INTO languages_iso639 VALUES ('nwi', '   ', '   ', '  ', 'I', 'L', 'Southwest Tanna', false, '');
INSERT INTO languages_iso639 VALUES ('nwm', '   ', '   ', '  ', 'I', 'L', 'Nyamusa-Molo', false, '');
INSERT INTO languages_iso639 VALUES ('nwo', '   ', '   ', '  ', 'I', 'E', 'Nauo', false, '');
INSERT INTO languages_iso639 VALUES ('nwr', '   ', '   ', '  ', 'I', 'L', 'Nawaru', false, '');
INSERT INTO languages_iso639 VALUES ('nwx', '   ', '   ', '  ', 'I', 'H', 'Middle Newar', false, '');
INSERT INTO languages_iso639 VALUES ('nwy', '   ', '   ', '  ', 'I', 'E', 'Nottoway-Meherrin', false, '');
INSERT INTO languages_iso639 VALUES ('nxa', '   ', '   ', '  ', 'I', 'L', 'Nauete', false, '');
INSERT INTO languages_iso639 VALUES ('nxd', '   ', '   ', '  ', 'I', 'L', 'Ngando (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('nxe', '   ', '   ', '  ', 'I', 'L', 'Nage', false, '');
INSERT INTO languages_iso639 VALUES ('nxg', '   ', '   ', '  ', 'I', 'L', 'Ngad''a', false, '');
INSERT INTO languages_iso639 VALUES ('nxi', '   ', '   ', '  ', 'I', 'L', 'Nindi', false, '');
INSERT INTO languages_iso639 VALUES ('nxk', '   ', '   ', '  ', 'I', 'L', 'Koki Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nxl', '   ', '   ', '  ', 'I', 'L', 'South Nuaulu', false, '');
INSERT INTO languages_iso639 VALUES ('nxm', '   ', '   ', '  ', 'I', 'A', 'Numidian', false, '');
INSERT INTO languages_iso639 VALUES ('nxn', '   ', '   ', '  ', 'I', 'E', 'Ngawun', false, '');
INSERT INTO languages_iso639 VALUES ('nxq', '   ', '   ', '  ', 'I', 'L', 'Naxi', false, '');
INSERT INTO languages_iso639 VALUES ('nxr', '   ', '   ', '  ', 'I', 'L', 'Ninggerum', false, '');
INSERT INTO languages_iso639 VALUES ('nxu', '   ', '   ', '  ', 'I', 'E', 'Narau', false, '');
INSERT INTO languages_iso639 VALUES ('nxx', '   ', '   ', '  ', 'I', 'L', 'Nafri', false, '');
INSERT INTO languages_iso639 VALUES ('nya', 'nya', 'nya', 'ny', 'I', 'L', 'Nyanja', false, '');
INSERT INTO languages_iso639 VALUES ('nyb', '   ', '   ', '  ', 'I', 'L', 'Nyangbo', false, '');
INSERT INTO languages_iso639 VALUES ('nyc', '   ', '   ', '  ', 'I', 'L', 'Nyanga-li', false, '');
INSERT INTO languages_iso639 VALUES ('nyd', '   ', '   ', '  ', 'I', 'L', 'Nyore', false, '');
INSERT INTO languages_iso639 VALUES ('nye', '   ', '   ', '  ', 'I', 'L', 'Nyengo', false, '');
INSERT INTO languages_iso639 VALUES ('nyf', '   ', '   ', '  ', 'I', 'L', 'Giryama', false, '');
INSERT INTO languages_iso639 VALUES ('nyg', '   ', '   ', '  ', 'I', 'L', 'Nyindu', false, '');
INSERT INTO languages_iso639 VALUES ('nyh', '   ', '   ', '  ', 'I', 'L', 'Nyigina', false, '');
INSERT INTO languages_iso639 VALUES ('nyi', '   ', '   ', '  ', 'I', 'L', 'Ama (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('nyj', '   ', '   ', '  ', 'I', 'L', 'Nyanga', false, '');
INSERT INTO languages_iso639 VALUES ('nyk', '   ', '   ', '  ', 'I', 'L', 'Nyaneka', false, '');
INSERT INTO languages_iso639 VALUES ('nyl', '   ', '   ', '  ', 'I', 'L', 'Nyeu', false, '');
INSERT INTO languages_iso639 VALUES ('nym', 'nym', 'nym', '  ', 'I', 'L', 'Nyamwezi', false, '');
INSERT INTO languages_iso639 VALUES ('nyn', 'nyn', 'nyn', '  ', 'I', 'L', 'Nyankole', false, '');
INSERT INTO languages_iso639 VALUES ('nyo', 'nyo', 'nyo', '  ', 'I', 'L', 'Nyoro', false, '');
INSERT INTO languages_iso639 VALUES ('nyp', '   ', '   ', '  ', 'I', 'E', 'Nyang''i', false, '');
INSERT INTO languages_iso639 VALUES ('nyq', '   ', '   ', '  ', 'I', 'L', 'Nayini', false, '');
INSERT INTO languages_iso639 VALUES ('nyr', '   ', '   ', '  ', 'I', 'L', 'Nyiha (Malawi)', false, '');
INSERT INTO languages_iso639 VALUES ('nys', '   ', '   ', '  ', 'I', 'L', 'Nyunga', false, '');
INSERT INTO languages_iso639 VALUES ('nyt', '   ', '   ', '  ', 'I', 'E', 'Nyawaygi', false, '');
INSERT INTO languages_iso639 VALUES ('nyu', '   ', '   ', '  ', 'I', 'L', 'Nyungwe', false, '');
INSERT INTO languages_iso639 VALUES ('nyv', '   ', '   ', '  ', 'I', 'E', 'Nyulnyul', false, '');
INSERT INTO languages_iso639 VALUES ('nyw', '   ', '   ', '  ', 'I', 'L', 'Nyaw', false, '');
INSERT INTO languages_iso639 VALUES ('nyx', '   ', '   ', '  ', 'I', 'E', 'Nganyaywana', false, '');
INSERT INTO languages_iso639 VALUES ('nyy', '   ', '   ', '  ', 'I', 'L', 'Nyakyusa-Ngonde', false, '');
INSERT INTO languages_iso639 VALUES ('nza', '   ', '   ', '  ', 'I', 'L', 'Tigon Mbembe', false, '');
INSERT INTO languages_iso639 VALUES ('nzb', '   ', '   ', '  ', 'I', 'L', 'Njebi', false, '');
INSERT INTO languages_iso639 VALUES ('nzi', 'nzi', 'nzi', '  ', 'I', 'L', 'Nzima', false, '');
INSERT INTO languages_iso639 VALUES ('nzk', '   ', '   ', '  ', 'I', 'L', 'Nzakara', false, '');
INSERT INTO languages_iso639 VALUES ('nzm', '   ', '   ', '  ', 'I', 'L', 'Zeme Naga', false, '');
INSERT INTO languages_iso639 VALUES ('nzs', '   ', '   ', '  ', 'I', 'L', 'New Zealand Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('nzu', '   ', '   ', '  ', 'I', 'L', 'Teke-Nzikou', false, '');
INSERT INTO languages_iso639 VALUES ('nzy', '   ', '   ', '  ', 'I', 'L', 'Nzakambay', false, '');
INSERT INTO languages_iso639 VALUES ('nzz', '   ', '   ', '  ', 'I', 'L', 'Nanga Dama Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('oaa', '   ', '   ', '  ', 'I', 'L', 'Orok', false, '');
INSERT INTO languages_iso639 VALUES ('oac', '   ', '   ', '  ', 'I', 'L', 'Oroch', false, '');
INSERT INTO languages_iso639 VALUES ('oar', '   ', '   ', '  ', 'I', 'A', 'Old Aramaic (up to 700 BCE)', false, '');
INSERT INTO languages_iso639 VALUES ('oav', '   ', '   ', '  ', 'I', 'H', 'Old Avar', false, '');
INSERT INTO languages_iso639 VALUES ('obi', '   ', '   ', '  ', 'I', 'E', 'Obispeño', false, '');
INSERT INTO languages_iso639 VALUES ('obk', '   ', '   ', '  ', 'I', 'L', 'Southern Bontok', false, '');
INSERT INTO languages_iso639 VALUES ('obl', '   ', '   ', '  ', 'I', 'L', 'Oblo', false, '');
INSERT INTO languages_iso639 VALUES ('obm', '   ', '   ', '  ', 'I', 'A', 'Moabite', false, '');
INSERT INTO languages_iso639 VALUES ('obo', '   ', '   ', '  ', 'I', 'L', 'Obo Manobo', false, '');
INSERT INTO languages_iso639 VALUES ('obr', '   ', '   ', '  ', 'I', 'H', 'Old Burmese', false, '');
INSERT INTO languages_iso639 VALUES ('obt', '   ', '   ', '  ', 'I', 'H', 'Old Breton', false, '');
INSERT INTO languages_iso639 VALUES ('obu', '   ', '   ', '  ', 'I', 'L', 'Obulom', false, '');
INSERT INTO languages_iso639 VALUES ('oca', '   ', '   ', '  ', 'I', 'L', 'Ocaina', false, '');
INSERT INTO languages_iso639 VALUES ('och', '   ', '   ', '  ', 'I', 'A', 'Old Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('oci', 'oci', 'oci', 'oc', 'I', 'L', 'Occitan (post 1500)', false, '');
INSERT INTO languages_iso639 VALUES ('oco', '   ', '   ', '  ', 'I', 'H', 'Old Cornish', false, '');
INSERT INTO languages_iso639 VALUES ('ocu', '   ', '   ', '  ', 'I', 'L', 'Atzingo Matlatzinca', false, '');
INSERT INTO languages_iso639 VALUES ('oda', '   ', '   ', '  ', 'I', 'L', 'Odut', false, '');
INSERT INTO languages_iso639 VALUES ('odk', '   ', '   ', '  ', 'I', 'L', 'Od', false, '');
INSERT INTO languages_iso639 VALUES ('odt', '   ', '   ', '  ', 'I', 'H', 'Old Dutch', false, '');
INSERT INTO languages_iso639 VALUES ('odu', '   ', '   ', '  ', 'I', 'L', 'Odual', false, '');
INSERT INTO languages_iso639 VALUES ('ofo', '   ', '   ', '  ', 'I', 'E', 'Ofo', false, '');
INSERT INTO languages_iso639 VALUES ('ofs', '   ', '   ', '  ', 'I', 'H', 'Old Frisian', false, '');
INSERT INTO languages_iso639 VALUES ('ofu', '   ', '   ', '  ', 'I', 'L', 'Efutop', false, '');
INSERT INTO languages_iso639 VALUES ('ogb', '   ', '   ', '  ', 'I', 'L', 'Ogbia', false, '');
INSERT INTO languages_iso639 VALUES ('ogc', '   ', '   ', '  ', 'I', 'L', 'Ogbah', false, '');
INSERT INTO languages_iso639 VALUES ('oge', '   ', '   ', '  ', 'I', 'H', 'Old Georgian', false, '');
INSERT INTO languages_iso639 VALUES ('ogg', '   ', '   ', '  ', 'I', 'L', 'Ogbogolo', false, '');
INSERT INTO languages_iso639 VALUES ('ogo', '   ', '   ', '  ', 'I', 'L', 'Khana', false, '');
INSERT INTO languages_iso639 VALUES ('ogu', '   ', '   ', '  ', 'I', 'L', 'Ogbronuagum', false, '');
INSERT INTO languages_iso639 VALUES ('oht', '   ', '   ', '  ', 'I', 'A', 'Old Hittite', false, '');
INSERT INTO languages_iso639 VALUES ('ohu', '   ', '   ', '  ', 'I', 'H', 'Old Hungarian', false, '');
INSERT INTO languages_iso639 VALUES ('oia', '   ', '   ', '  ', 'I', 'L', 'Oirata', false, '');
INSERT INTO languages_iso639 VALUES ('oin', '   ', '   ', '  ', 'I', 'L', 'Inebu One', false, '');
INSERT INTO languages_iso639 VALUES ('ojb', '   ', '   ', '  ', 'I', 'L', 'Northwestern Ojibwa', false, '');
INSERT INTO languages_iso639 VALUES ('ojc', '   ', '   ', '  ', 'I', 'L', 'Central Ojibwa', false, '');
INSERT INTO languages_iso639 VALUES ('ojg', '   ', '   ', '  ', 'I', 'L', 'Eastern Ojibwa', false, '');
INSERT INTO languages_iso639 VALUES ('oji', 'oji', 'oji', 'oj', 'M', 'L', 'Ojibwa', false, '');
INSERT INTO languages_iso639 VALUES ('ojp', '   ', '   ', '  ', 'I', 'H', 'Old Japanese', false, '');
INSERT INTO languages_iso639 VALUES ('ojs', '   ', '   ', '  ', 'I', 'L', 'Severn Ojibwa', false, '');
INSERT INTO languages_iso639 VALUES ('ojv', '   ', '   ', '  ', 'I', 'L', 'Ontong Java', false, '');
INSERT INTO languages_iso639 VALUES ('ojw', '   ', '   ', '  ', 'I', 'L', 'Western Ojibwa', false, '');
INSERT INTO languages_iso639 VALUES ('oka', '   ', '   ', '  ', 'I', 'L', 'Okanagan', false, '');
INSERT INTO languages_iso639 VALUES ('okb', '   ', '   ', '  ', 'I', 'L', 'Okobo', false, '');
INSERT INTO languages_iso639 VALUES ('okd', '   ', '   ', '  ', 'I', 'L', 'Okodia', false, '');
INSERT INTO languages_iso639 VALUES ('oke', '   ', '   ', '  ', 'I', 'L', 'Okpe (Southwestern Edo)', false, '');
INSERT INTO languages_iso639 VALUES ('okg', '   ', '   ', '  ', 'I', 'E', 'Koko Babangk', false, '');
INSERT INTO languages_iso639 VALUES ('okh', '   ', '   ', '  ', 'I', 'L', 'Koresh-e Rostam', false, '');
INSERT INTO languages_iso639 VALUES ('oki', '   ', '   ', '  ', 'I', 'L', 'Okiek', false, '');
INSERT INTO languages_iso639 VALUES ('okj', '   ', '   ', '  ', 'I', 'E', 'Oko-Juwoi', false, '');
INSERT INTO languages_iso639 VALUES ('okk', '   ', '   ', '  ', 'I', 'L', 'Kwamtim One', false, '');
INSERT INTO languages_iso639 VALUES ('okl', '   ', '   ', '  ', 'I', 'E', 'Old Kentish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('okm', '   ', '   ', '  ', 'I', 'H', 'Middle Korean (10th-16th cent.)', false, '');
INSERT INTO languages_iso639 VALUES ('okn', '   ', '   ', '  ', 'I', 'L', 'Oki-No-Erabu', false, '');
INSERT INTO languages_iso639 VALUES ('oko', '   ', '   ', '  ', 'I', 'H', 'Old Korean (3rd-9th cent.)', false, '');
INSERT INTO languages_iso639 VALUES ('okr', '   ', '   ', '  ', 'I', 'L', 'Kirike', false, '');
INSERT INTO languages_iso639 VALUES ('oks', '   ', '   ', '  ', 'I', 'L', 'Oko-Eni-Osayen', false, '');
INSERT INTO languages_iso639 VALUES ('oku', '   ', '   ', '  ', 'I', 'L', 'Oku', false, '');
INSERT INTO languages_iso639 VALUES ('okv', '   ', '   ', '  ', 'I', 'L', 'Orokaiva', false, '');
INSERT INTO languages_iso639 VALUES ('okx', '   ', '   ', '  ', 'I', 'L', 'Okpe (Northwestern Edo)', false, '');
INSERT INTO languages_iso639 VALUES ('ola', '   ', '   ', '  ', 'I', 'L', 'Walungge', false, '');
INSERT INTO languages_iso639 VALUES ('old', '   ', '   ', '  ', 'I', 'L', 'Mochi', false, '');
INSERT INTO languages_iso639 VALUES ('ole', '   ', '   ', '  ', 'I', 'L', 'Olekha', false, '');
INSERT INTO languages_iso639 VALUES ('olk', '   ', '   ', '  ', 'I', 'E', 'Olkol', false, '');
INSERT INTO languages_iso639 VALUES ('olm', '   ', '   ', '  ', 'I', 'L', 'Oloma', false, '');
INSERT INTO languages_iso639 VALUES ('olo', '   ', '   ', '  ', 'I', 'L', 'Livvi', false, '');
INSERT INTO languages_iso639 VALUES ('olr', '   ', '   ', '  ', 'I', 'L', 'Olrat', false, '');
INSERT INTO languages_iso639 VALUES ('oma', '   ', '   ', '  ', 'I', 'L', 'Omaha-Ponca', false, '');
INSERT INTO languages_iso639 VALUES ('omb', '   ', '   ', '  ', 'I', 'L', 'East Ambae', false, '');
INSERT INTO languages_iso639 VALUES ('omc', '   ', '   ', '  ', 'I', 'E', 'Mochica', false, '');
INSERT INTO languages_iso639 VALUES ('ome', '   ', '   ', '  ', 'I', 'E', 'Omejes', false, '');
INSERT INTO languages_iso639 VALUES ('omg', '   ', '   ', '  ', 'I', 'L', 'Omagua', false, '');
INSERT INTO languages_iso639 VALUES ('omi', '   ', '   ', '  ', 'I', 'L', 'Omi', false, '');
INSERT INTO languages_iso639 VALUES ('omk', '   ', '   ', '  ', 'I', 'E', 'Omok', false, '');
INSERT INTO languages_iso639 VALUES ('oml', '   ', '   ', '  ', 'I', 'L', 'Ombo', false, '');
INSERT INTO languages_iso639 VALUES ('omn', '   ', '   ', '  ', 'I', 'A', 'Minoan', false, '');
INSERT INTO languages_iso639 VALUES ('omo', '   ', '   ', '  ', 'I', 'L', 'Utarmbung', false, '');
INSERT INTO languages_iso639 VALUES ('omp', '   ', '   ', '  ', 'I', 'H', 'Old Manipuri', false, '');
INSERT INTO languages_iso639 VALUES ('omr', '   ', '   ', '  ', 'I', 'H', 'Old Marathi', false, '');
INSERT INTO languages_iso639 VALUES ('omt', '   ', '   ', '  ', 'I', 'L', 'Omotik', false, '');
INSERT INTO languages_iso639 VALUES ('omu', '   ', '   ', '  ', 'I', 'E', 'Omurano', false, '');
INSERT INTO languages_iso639 VALUES ('omw', '   ', '   ', '  ', 'I', 'L', 'South Tairora', false, '');
INSERT INTO languages_iso639 VALUES ('omx', '   ', '   ', '  ', 'I', 'H', 'Old Mon', false, '');
INSERT INTO languages_iso639 VALUES ('ona', '   ', '   ', '  ', 'I', 'L', 'Ona', false, '');
INSERT INTO languages_iso639 VALUES ('onb', '   ', '   ', '  ', 'I', 'L', 'Lingao', false, '');
INSERT INTO languages_iso639 VALUES ('one', '   ', '   ', '  ', 'I', 'L', 'Oneida', false, '');
INSERT INTO languages_iso639 VALUES ('ong', '   ', '   ', '  ', 'I', 'L', 'Olo', false, '');
INSERT INTO languages_iso639 VALUES ('oni', '   ', '   ', '  ', 'I', 'L', 'Onin', false, '');
INSERT INTO languages_iso639 VALUES ('onj', '   ', '   ', '  ', 'I', 'L', 'Onjob', false, '');
INSERT INTO languages_iso639 VALUES ('onk', '   ', '   ', '  ', 'I', 'L', 'Kabore One', false, '');
INSERT INTO languages_iso639 VALUES ('onn', '   ', '   ', '  ', 'I', 'L', 'Onobasulu', false, '');
INSERT INTO languages_iso639 VALUES ('ono', '   ', '   ', '  ', 'I', 'L', 'Onondaga', false, '');
INSERT INTO languages_iso639 VALUES ('onp', '   ', '   ', '  ', 'I', 'L', 'Sartang', false, '');
INSERT INTO languages_iso639 VALUES ('onr', '   ', '   ', '  ', 'I', 'L', 'Northern One', false, '');
INSERT INTO languages_iso639 VALUES ('ons', '   ', '   ', '  ', 'I', 'L', 'Ono', false, '');
INSERT INTO languages_iso639 VALUES ('ont', '   ', '   ', '  ', 'I', 'L', 'Ontenu', false, '');
INSERT INTO languages_iso639 VALUES ('onu', '   ', '   ', '  ', 'I', 'L', 'Unua', false, '');
INSERT INTO languages_iso639 VALUES ('onw', '   ', '   ', '  ', 'I', 'H', 'Old Nubian', false, '');
INSERT INTO languages_iso639 VALUES ('onx', '   ', '   ', '  ', 'I', 'L', 'Onin Based Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('ood', '   ', '   ', '  ', 'I', 'L', 'Tohono O''odham', false, '');
INSERT INTO languages_iso639 VALUES ('oog', '   ', '   ', '  ', 'I', 'L', 'Ong', false, '');
INSERT INTO languages_iso639 VALUES ('oon', '   ', '   ', '  ', 'I', 'L', 'Önge', false, '');
INSERT INTO languages_iso639 VALUES ('oor', '   ', '   ', '  ', 'I', 'L', 'Oorlams', false, '');
INSERT INTO languages_iso639 VALUES ('oos', '   ', '   ', '  ', 'I', 'A', 'Old Ossetic', false, '');
INSERT INTO languages_iso639 VALUES ('opa', '   ', '   ', '  ', 'I', 'L', 'Okpamheri', false, '');
INSERT INTO languages_iso639 VALUES ('opk', '   ', '   ', '  ', 'I', 'L', 'Kopkaka', false, '');
INSERT INTO languages_iso639 VALUES ('opm', '   ', '   ', '  ', 'I', 'L', 'Oksapmin', false, '');
INSERT INTO languages_iso639 VALUES ('opo', '   ', '   ', '  ', 'I', 'L', 'Opao', false, '');
INSERT INTO languages_iso639 VALUES ('opt', '   ', '   ', '  ', 'I', 'E', 'Opata', false, '');
INSERT INTO languages_iso639 VALUES ('opy', '   ', '   ', '  ', 'I', 'L', 'Ofayé', false, '');
INSERT INTO languages_iso639 VALUES ('ora', '   ', '   ', '  ', 'I', 'L', 'Oroha', false, '');
INSERT INTO languages_iso639 VALUES ('orc', '   ', '   ', '  ', 'I', 'L', 'Orma', false, '');
INSERT INTO languages_iso639 VALUES ('ore', '   ', '   ', '  ', 'I', 'L', 'Orejón', false, '');
INSERT INTO languages_iso639 VALUES ('org', '   ', '   ', '  ', 'I', 'L', 'Oring', false, '');
INSERT INTO languages_iso639 VALUES ('orh', '   ', '   ', '  ', 'I', 'L', 'Oroqen', false, '');
INSERT INTO languages_iso639 VALUES ('ori', 'ori', 'ori', 'or', 'M', 'L', 'Oriya (macrolanguage)', false, '');
INSERT INTO languages_iso639 VALUES ('orm', 'orm', 'orm', 'om', 'M', 'L', 'Oromo', false, '');
INSERT INTO languages_iso639 VALUES ('orn', '   ', '   ', '  ', 'I', 'L', 'Orang Kanaq', false, '');
INSERT INTO languages_iso639 VALUES ('oro', '   ', '   ', '  ', 'I', 'L', 'Orokolo', false, '');
INSERT INTO languages_iso639 VALUES ('orr', '   ', '   ', '  ', 'I', 'L', 'Oruma', false, '');
INSERT INTO languages_iso639 VALUES ('ors', '   ', '   ', '  ', 'I', 'L', 'Orang Seletar', false, '');
INSERT INTO languages_iso639 VALUES ('ort', '   ', '   ', '  ', 'I', 'L', 'Adivasi Oriya', false, '');
INSERT INTO languages_iso639 VALUES ('oru', '   ', '   ', '  ', 'I', 'L', 'Ormuri', false, '');
INSERT INTO languages_iso639 VALUES ('orv', '   ', '   ', '  ', 'I', 'H', 'Old Russian', false, '');
INSERT INTO languages_iso639 VALUES ('orw', '   ', '   ', '  ', 'I', 'L', 'Oro Win', false, '');
INSERT INTO languages_iso639 VALUES ('orx', '   ', '   ', '  ', 'I', 'L', 'Oro', false, '');
INSERT INTO languages_iso639 VALUES ('ory', '   ', '   ', '  ', 'I', 'L', 'Oriya (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('orz', '   ', '   ', '  ', 'I', 'L', 'Ormu', false, '');
INSERT INTO languages_iso639 VALUES ('osa', 'osa', 'osa', '  ', 'I', 'L', 'Osage', false, '');
INSERT INTO languages_iso639 VALUES ('osc', '   ', '   ', '  ', 'I', 'A', 'Oscan', false, '');
INSERT INTO languages_iso639 VALUES ('osi', '   ', '   ', '  ', 'I', 'L', 'Osing', false, '');
INSERT INTO languages_iso639 VALUES ('oso', '   ', '   ', '  ', 'I', 'L', 'Ososo', false, '');
INSERT INTO languages_iso639 VALUES ('osp', '   ', '   ', '  ', 'I', 'H', 'Old Spanish', false, '');
INSERT INTO languages_iso639 VALUES ('oss', 'oss', 'oss', 'os', 'I', 'L', 'Ossetian', false, '');
INSERT INTO languages_iso639 VALUES ('ost', '   ', '   ', '  ', 'I', 'L', 'Osatu', false, '');
INSERT INTO languages_iso639 VALUES ('osu', '   ', '   ', '  ', 'I', 'L', 'Southern One', false, '');
INSERT INTO languages_iso639 VALUES ('osx', '   ', '   ', '  ', 'I', 'H', 'Old Saxon', false, '');
INSERT INTO languages_iso639 VALUES ('ota', 'ota', 'ota', '  ', 'I', 'H', 'Ottoman Turkish (1500-1928)', false, '');
INSERT INTO languages_iso639 VALUES ('otb', '   ', '   ', '  ', 'I', 'H', 'Old Tibetan', false, '');
INSERT INTO languages_iso639 VALUES ('otd', '   ', '   ', '  ', 'I', 'L', 'Ot Danum', false, '');
INSERT INTO languages_iso639 VALUES ('ote', '   ', '   ', '  ', 'I', 'L', 'Mezquital Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('oti', '   ', '   ', '  ', 'I', 'E', 'Oti', false, '');
INSERT INTO languages_iso639 VALUES ('otk', '   ', '   ', '  ', 'I', 'H', 'Old Turkish', false, '');
INSERT INTO languages_iso639 VALUES ('otl', '   ', '   ', '  ', 'I', 'L', 'Tilapa Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('otm', '   ', '   ', '  ', 'I', 'L', 'Eastern Highland Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('otn', '   ', '   ', '  ', 'I', 'L', 'Tenango Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('otq', '   ', '   ', '  ', 'I', 'L', 'Querétaro Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('otr', '   ', '   ', '  ', 'I', 'L', 'Otoro', false, '');
INSERT INTO languages_iso639 VALUES ('ots', '   ', '   ', '  ', 'I', 'L', 'Estado de México Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('ott', '   ', '   ', '  ', 'I', 'L', 'Temoaya Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('otu', '   ', '   ', '  ', 'I', 'E', 'Otuke', false, '');
INSERT INTO languages_iso639 VALUES ('otw', '   ', '   ', '  ', 'I', 'L', 'Ottawa', false, '');
INSERT INTO languages_iso639 VALUES ('otx', '   ', '   ', '  ', 'I', 'L', 'Texcatepec Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('oty', '   ', '   ', '  ', 'I', 'A', 'Old Tamil', false, '');
INSERT INTO languages_iso639 VALUES ('otz', '   ', '   ', '  ', 'I', 'L', 'Ixtenco Otomi', false, '');
INSERT INTO languages_iso639 VALUES ('oua', '   ', '   ', '  ', 'I', 'L', 'Tagargrent', false, '');
INSERT INTO languages_iso639 VALUES ('oub', '   ', '   ', '  ', 'I', 'L', 'Glio-Oubi', false, '');
INSERT INTO languages_iso639 VALUES ('oue', '   ', '   ', '  ', 'I', 'L', 'Oune', false, '');
INSERT INTO languages_iso639 VALUES ('oui', '   ', '   ', '  ', 'I', 'H', 'Old Uighur', false, '');
INSERT INTO languages_iso639 VALUES ('oum', '   ', '   ', '  ', 'I', 'E', 'Ouma', false, '');
INSERT INTO languages_iso639 VALUES ('oun', '   ', '   ', '  ', 'I', 'L', '!O!ung', false, '');
INSERT INTO languages_iso639 VALUES ('owi', '   ', '   ', '  ', 'I', 'L', 'Owiniga', false, '');
INSERT INTO languages_iso639 VALUES ('owl', '   ', '   ', '  ', 'I', 'H', 'Old Welsh', false, '');
INSERT INTO languages_iso639 VALUES ('oyb', '   ', '   ', '  ', 'I', 'L', 'Oy', false, '');
INSERT INTO languages_iso639 VALUES ('oyd', '   ', '   ', '  ', 'I', 'L', 'Oyda', false, '');
INSERT INTO languages_iso639 VALUES ('oym', '   ', '   ', '  ', 'I', 'L', 'Wayampi', false, '');
INSERT INTO languages_iso639 VALUES ('oyy', '   ', '   ', '  ', 'I', 'L', 'Oya''oya', false, '');
INSERT INTO languages_iso639 VALUES ('ozm', '   ', '   ', '  ', 'I', 'L', 'Koonzime', false, '');
INSERT INTO languages_iso639 VALUES ('pab', '   ', '   ', '  ', 'I', 'L', 'Parecís', false, '');
INSERT INTO languages_iso639 VALUES ('pac', '   ', '   ', '  ', 'I', 'L', 'Pacoh', false, '');
INSERT INTO languages_iso639 VALUES ('pad', '   ', '   ', '  ', 'I', 'L', 'Paumarí', false, '');
INSERT INTO languages_iso639 VALUES ('pae', '   ', '   ', '  ', 'I', 'L', 'Pagibete', false, '');
INSERT INTO languages_iso639 VALUES ('paf', '   ', '   ', '  ', 'I', 'E', 'Paranawát', false, '');
INSERT INTO languages_iso639 VALUES ('pag', 'pag', 'pag', '  ', 'I', 'L', 'Pangasinan', false, '');
INSERT INTO languages_iso639 VALUES ('pah', '   ', '   ', '  ', 'I', 'L', 'Tenharim', false, '');
INSERT INTO languages_iso639 VALUES ('pai', '   ', '   ', '  ', 'I', 'L', 'Pe', false, '');
INSERT INTO languages_iso639 VALUES ('pak', '   ', '   ', '  ', 'I', 'L', 'Parakanã', false, '');
INSERT INTO languages_iso639 VALUES ('pal', 'pal', 'pal', '  ', 'I', 'A', 'Pahlavi', false, '');
INSERT INTO languages_iso639 VALUES ('pam', 'pam', 'pam', '  ', 'I', 'L', 'Pampanga', false, '');
INSERT INTO languages_iso639 VALUES ('pan', 'pan', 'pan', 'pa', 'I', 'L', 'Panjabi', false, '');
INSERT INTO languages_iso639 VALUES ('pao', '   ', '   ', '  ', 'I', 'L', 'Northern Paiute', false, '');
INSERT INTO languages_iso639 VALUES ('pap', 'pap', 'pap', '  ', 'I', 'L', 'Papiamento', false, '');
INSERT INTO languages_iso639 VALUES ('paq', '   ', '   ', '  ', 'I', 'L', 'Parya', false, '');
INSERT INTO languages_iso639 VALUES ('par', '   ', '   ', '  ', 'I', 'L', 'Panamint', false, '');
INSERT INTO languages_iso639 VALUES ('pas', '   ', '   ', '  ', 'I', 'L', 'Papasena', false, '');
INSERT INTO languages_iso639 VALUES ('pat', '   ', '   ', '  ', 'I', 'L', 'Papitalai', false, '');
INSERT INTO languages_iso639 VALUES ('pau', 'pau', 'pau', '  ', 'I', 'L', 'Palauan', false, '');
INSERT INTO languages_iso639 VALUES ('pav', '   ', '   ', '  ', 'I', 'L', 'Pakaásnovos', false, '');
INSERT INTO languages_iso639 VALUES ('paw', '   ', '   ', '  ', 'I', 'L', 'Pawnee', false, '');
INSERT INTO languages_iso639 VALUES ('pax', '   ', '   ', '  ', 'I', 'E', 'Pankararé', false, '');
INSERT INTO languages_iso639 VALUES ('pay', '   ', '   ', '  ', 'I', 'L', 'Pech', false, '');
INSERT INTO languages_iso639 VALUES ('paz', '   ', '   ', '  ', 'I', 'E', 'Pankararú', false, '');
INSERT INTO languages_iso639 VALUES ('pbb', '   ', '   ', '  ', 'I', 'L', 'Páez', false, '');
INSERT INTO languages_iso639 VALUES ('pbc', '   ', '   ', '  ', 'I', 'L', 'Patamona', false, '');
INSERT INTO languages_iso639 VALUES ('pbe', '   ', '   ', '  ', 'I', 'L', 'Mezontla Popoloca', false, '');
INSERT INTO languages_iso639 VALUES ('pbf', '   ', '   ', '  ', 'I', 'L', 'Coyotepec Popoloca', false, '');
INSERT INTO languages_iso639 VALUES ('pbg', '   ', '   ', '  ', 'I', 'E', 'Paraujano', false, '');
INSERT INTO languages_iso639 VALUES ('pbh', '   ', '   ', '  ', 'I', 'L', 'E''ñapa Woromaipu', false, '');
INSERT INTO languages_iso639 VALUES ('pbi', '   ', '   ', '  ', 'I', 'L', 'Parkwa', false, '');
INSERT INTO languages_iso639 VALUES ('pbl', '   ', '   ', '  ', 'I', 'L', 'Mak (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('pbn', '   ', '   ', '  ', 'I', 'L', 'Kpasam', false, '');
INSERT INTO languages_iso639 VALUES ('pbo', '   ', '   ', '  ', 'I', 'L', 'Papel', false, '');
INSERT INTO languages_iso639 VALUES ('pbp', '   ', '   ', '  ', 'I', 'L', 'Badyara', false, '');
INSERT INTO languages_iso639 VALUES ('pbr', '   ', '   ', '  ', 'I', 'L', 'Pangwa', false, '');
INSERT INTO languages_iso639 VALUES ('pbs', '   ', '   ', '  ', 'I', 'L', 'Central Pame', false, '');
INSERT INTO languages_iso639 VALUES ('pbt', '   ', '   ', '  ', 'I', 'L', 'Southern Pashto', false, '');
INSERT INTO languages_iso639 VALUES ('pbu', '   ', '   ', '  ', 'I', 'L', 'Northern Pashto', false, '');
INSERT INTO languages_iso639 VALUES ('pbv', '   ', '   ', '  ', 'I', 'L', 'Pnar', false, '');
INSERT INTO languages_iso639 VALUES ('pby', '   ', '   ', '  ', 'I', 'L', 'Pyu', false, '');
INSERT INTO languages_iso639 VALUES ('pca', '   ', '   ', '  ', 'I', 'L', 'Santa Inés Ahuatempan Popoloca', false, '');
INSERT INTO languages_iso639 VALUES ('pcb', '   ', '   ', '  ', 'I', 'L', 'Pear', false, '');
INSERT INTO languages_iso639 VALUES ('pcc', '   ', '   ', '  ', 'I', 'L', 'Bouyei', false, '');
INSERT INTO languages_iso639 VALUES ('pcd', '   ', '   ', '  ', 'I', 'L', 'Picard', false, '');
INSERT INTO languages_iso639 VALUES ('pce', '   ', '   ', '  ', 'I', 'L', 'Ruching Palaung', false, '');
INSERT INTO languages_iso639 VALUES ('pcf', '   ', '   ', '  ', 'I', 'L', 'Paliyan', false, '');
INSERT INTO languages_iso639 VALUES ('pcg', '   ', '   ', '  ', 'I', 'L', 'Paniya', false, '');
INSERT INTO languages_iso639 VALUES ('pch', '   ', '   ', '  ', 'I', 'L', 'Pardhan', false, '');
INSERT INTO languages_iso639 VALUES ('pci', '   ', '   ', '  ', 'I', 'L', 'Duruwa', false, '');
INSERT INTO languages_iso639 VALUES ('pcj', '   ', '   ', '  ', 'I', 'L', 'Parenga', false, '');
INSERT INTO languages_iso639 VALUES ('pck', '   ', '   ', '  ', 'I', 'L', 'Paite Chin', false, '');
INSERT INTO languages_iso639 VALUES ('pcl', '   ', '   ', '  ', 'I', 'L', 'Pardhi', false, '');
INSERT INTO languages_iso639 VALUES ('pcm', '   ', '   ', '  ', 'I', 'L', 'Nigerian Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('pcn', '   ', '   ', '  ', 'I', 'L', 'Piti', false, '');
INSERT INTO languages_iso639 VALUES ('pcp', '   ', '   ', '  ', 'I', 'L', 'Pacahuara', false, '');
INSERT INTO languages_iso639 VALUES ('pcw', '   ', '   ', '  ', 'I', 'L', 'Pyapun', false, '');
INSERT INTO languages_iso639 VALUES ('pda', '   ', '   ', '  ', 'I', 'L', 'Anam', false, '');
INSERT INTO languages_iso639 VALUES ('pdc', '   ', '   ', '  ', 'I', 'L', 'Pennsylvania German', false, '');
INSERT INTO languages_iso639 VALUES ('pdi', '   ', '   ', '  ', 'I', 'L', 'Pa Di', false, '');
INSERT INTO languages_iso639 VALUES ('pdn', '   ', '   ', '  ', 'I', 'L', 'Podena', false, '');
INSERT INTO languages_iso639 VALUES ('pdo', '   ', '   ', '  ', 'I', 'L', 'Padoe', false, '');
INSERT INTO languages_iso639 VALUES ('pdt', '   ', '   ', '  ', 'I', 'L', 'Plautdietsch', false, '');
INSERT INTO languages_iso639 VALUES ('pdu', '   ', '   ', '  ', 'I', 'L', 'Kayan', false, '');
INSERT INTO languages_iso639 VALUES ('pea', '   ', '   ', '  ', 'I', 'L', 'Peranakan Indonesian', false, '');
INSERT INTO languages_iso639 VALUES ('peb', '   ', '   ', '  ', 'I', 'E', 'Eastern Pomo', false, '');
INSERT INTO languages_iso639 VALUES ('ped', '   ', '   ', '  ', 'I', 'L', 'Mala (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('pee', '   ', '   ', '  ', 'I', 'L', 'Taje', false, '');
INSERT INTO languages_iso639 VALUES ('pef', '   ', '   ', '  ', 'I', 'E', 'Northeastern Pomo', false, '');
INSERT INTO languages_iso639 VALUES ('peg', '   ', '   ', '  ', 'I', 'L', 'Pengo', false, '');
INSERT INTO languages_iso639 VALUES ('peh', '   ', '   ', '  ', 'I', 'L', 'Bonan', false, '');
INSERT INTO languages_iso639 VALUES ('pei', '   ', '   ', '  ', 'I', 'L', 'Chichimeca-Jonaz', false, '');
INSERT INTO languages_iso639 VALUES ('pej', '   ', '   ', '  ', 'I', 'E', 'Northern Pomo', false, '');
INSERT INTO languages_iso639 VALUES ('pek', '   ', '   ', '  ', 'I', 'L', 'Penchal', false, '');
INSERT INTO languages_iso639 VALUES ('pel', '   ', '   ', '  ', 'I', 'L', 'Pekal', false, '');
INSERT INTO languages_iso639 VALUES ('pem', '   ', '   ', '  ', 'I', 'L', 'Phende', false, '');
INSERT INTO languages_iso639 VALUES ('peo', 'peo', 'peo', '  ', 'I', 'H', 'Old Persian (ca. 600-400 B.C.)', false, '');
INSERT INTO languages_iso639 VALUES ('pep', '   ', '   ', '  ', 'I', 'L', 'Kunja', false, '');
INSERT INTO languages_iso639 VALUES ('peq', '   ', '   ', '  ', 'I', 'L', 'Southern Pomo', false, '');
INSERT INTO languages_iso639 VALUES ('pes', '   ', '   ', '  ', 'I', 'L', 'Iranian Persian', false, '');
INSERT INTO languages_iso639 VALUES ('pev', '   ', '   ', '  ', 'I', 'L', 'Pémono', false, '');
INSERT INTO languages_iso639 VALUES ('pex', '   ', '   ', '  ', 'I', 'L', 'Petats', false, '');
INSERT INTO languages_iso639 VALUES ('pey', '   ', '   ', '  ', 'I', 'L', 'Petjo', false, '');
INSERT INTO languages_iso639 VALUES ('pez', '   ', '   ', '  ', 'I', 'L', 'Eastern Penan', false, '');
INSERT INTO languages_iso639 VALUES ('pfa', '   ', '   ', '  ', 'I', 'L', 'Pááfang', false, '');
INSERT INTO languages_iso639 VALUES ('pfe', '   ', '   ', '  ', 'I', 'L', 'Peere', false, '');
INSERT INTO languages_iso639 VALUES ('pfl', '   ', '   ', '  ', 'I', 'L', 'Pfaelzisch', false, '');
INSERT INTO languages_iso639 VALUES ('pga', '   ', '   ', '  ', 'I', 'L', 'Sudanese Creole Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('pgg', '   ', '   ', '  ', 'I', 'L', 'Pangwali', false, '');
INSERT INTO languages_iso639 VALUES ('pgi', '   ', '   ', '  ', 'I', 'L', 'Pagi', false, '');
INSERT INTO languages_iso639 VALUES ('pgk', '   ', '   ', '  ', 'I', 'L', 'Rerep', false, '');
INSERT INTO languages_iso639 VALUES ('pgl', '   ', '   ', '  ', 'I', 'A', 'Primitive Irish', false, '');
INSERT INTO languages_iso639 VALUES ('pgn', '   ', '   ', '  ', 'I', 'A', 'Paelignian', false, '');
INSERT INTO languages_iso639 VALUES ('pgs', '   ', '   ', '  ', 'I', 'L', 'Pangseng', false, '');
INSERT INTO languages_iso639 VALUES ('pgu', '   ', '   ', '  ', 'I', 'L', 'Pagu', false, '');
INSERT INTO languages_iso639 VALUES ('pha', '   ', '   ', '  ', 'I', 'L', 'Pa-Hng', false, '');
INSERT INTO languages_iso639 VALUES ('phd', '   ', '   ', '  ', 'I', 'L', 'Phudagi', false, '');
INSERT INTO languages_iso639 VALUES ('phg', '   ', '   ', '  ', 'I', 'L', 'Phuong', false, '');
INSERT INTO languages_iso639 VALUES ('phh', '   ', '   ', '  ', 'I', 'L', 'Phukha', false, '');
INSERT INTO languages_iso639 VALUES ('phk', '   ', '   ', '  ', 'I', 'L', 'Phake', false, '');
INSERT INTO languages_iso639 VALUES ('phl', '   ', '   ', '  ', 'I', 'L', 'Phalura', false, '');
INSERT INTO languages_iso639 VALUES ('phm', '   ', '   ', '  ', 'I', 'L', 'Phimbi', false, '');
INSERT INTO languages_iso639 VALUES ('phn', 'phn', 'phn', '  ', 'I', 'A', 'Phoenician', false, '');
INSERT INTO languages_iso639 VALUES ('pho', '   ', '   ', '  ', 'I', 'L', 'Phunoi', false, '');
INSERT INTO languages_iso639 VALUES ('phq', '   ', '   ', '  ', 'I', 'L', 'Phana''', false, '');
INSERT INTO languages_iso639 VALUES ('phr', '   ', '   ', '  ', 'I', 'L', 'Pahari-Potwari', false, '');
INSERT INTO languages_iso639 VALUES ('pht', '   ', '   ', '  ', 'I', 'L', 'Phu Thai', false, '');
INSERT INTO languages_iso639 VALUES ('phu', '   ', '   ', '  ', 'I', 'L', 'Phuan', false, '');
INSERT INTO languages_iso639 VALUES ('phv', '   ', '   ', '  ', 'I', 'L', 'Pahlavani', false, '');
INSERT INTO languages_iso639 VALUES ('phw', '   ', '   ', '  ', 'I', 'L', 'Phangduwali', false, '');
INSERT INTO languages_iso639 VALUES ('pia', '   ', '   ', '  ', 'I', 'L', 'Pima Bajo', false, '');
INSERT INTO languages_iso639 VALUES ('pib', '   ', '   ', '  ', 'I', 'L', 'Yine', false, '');
INSERT INTO languages_iso639 VALUES ('pic', '   ', '   ', '  ', 'I', 'L', 'Pinji', false, '');
INSERT INTO languages_iso639 VALUES ('pid', '   ', '   ', '  ', 'I', 'L', 'Piaroa', false, '');
INSERT INTO languages_iso639 VALUES ('pie', '   ', '   ', '  ', 'I', 'E', 'Piro', false, '');
INSERT INTO languages_iso639 VALUES ('pif', '   ', '   ', '  ', 'I', 'L', 'Pingelapese', false, '');
INSERT INTO languages_iso639 VALUES ('pig', '   ', '   ', '  ', 'I', 'L', 'Pisabo', false, '');
INSERT INTO languages_iso639 VALUES ('pih', '   ', '   ', '  ', 'I', 'L', 'Pitcairn-Norfolk', false, '');
INSERT INTO languages_iso639 VALUES ('pii', '   ', '   ', '  ', 'I', 'L', 'Pini', false, '');
INSERT INTO languages_iso639 VALUES ('pij', '   ', '   ', '  ', 'I', 'E', 'Pijao', false, '');
INSERT INTO languages_iso639 VALUES ('pil', '   ', '   ', '  ', 'I', 'L', 'Yom', false, '');
INSERT INTO languages_iso639 VALUES ('pim', '   ', '   ', '  ', 'I', 'E', 'Powhatan', false, '');
INSERT INTO languages_iso639 VALUES ('pin', '   ', '   ', '  ', 'I', 'L', 'Piame', false, '');
INSERT INTO languages_iso639 VALUES ('pio', '   ', '   ', '  ', 'I', 'L', 'Piapoco', false, '');
INSERT INTO languages_iso639 VALUES ('pip', '   ', '   ', '  ', 'I', 'L', 'Pero', false, '');
INSERT INTO languages_iso639 VALUES ('pir', '   ', '   ', '  ', 'I', 'L', 'Piratapuyo', false, '');
INSERT INTO languages_iso639 VALUES ('pis', '   ', '   ', '  ', 'I', 'L', 'Pijin', false, '');
INSERT INTO languages_iso639 VALUES ('pit', '   ', '   ', '  ', 'I', 'E', 'Pitta Pitta', false, '');
INSERT INTO languages_iso639 VALUES ('piu', '   ', '   ', '  ', 'I', 'L', 'Pintupi-Luritja', false, '');
INSERT INTO languages_iso639 VALUES ('piv', '   ', '   ', '  ', 'I', 'L', 'Pileni', false, '');
INSERT INTO languages_iso639 VALUES ('piw', '   ', '   ', '  ', 'I', 'L', 'Pimbwe', false, '');
INSERT INTO languages_iso639 VALUES ('pix', '   ', '   ', '  ', 'I', 'L', 'Piu', false, '');
INSERT INTO languages_iso639 VALUES ('piy', '   ', '   ', '  ', 'I', 'L', 'Piya-Kwonci', false, '');
INSERT INTO languages_iso639 VALUES ('piz', '   ', '   ', '  ', 'I', 'L', 'Pije', false, '');
INSERT INTO languages_iso639 VALUES ('pjt', '   ', '   ', '  ', 'I', 'L', 'Pitjantjatjara', false, '');
INSERT INTO languages_iso639 VALUES ('pka', '   ', '   ', '  ', 'I', 'H', 'Ardhamāgadhī Prākrit', false, '');
INSERT INTO languages_iso639 VALUES ('pkb', '   ', '   ', '  ', 'I', 'L', 'Pokomo', false, '');
INSERT INTO languages_iso639 VALUES ('pkc', '   ', '   ', '  ', 'I', 'E', 'Paekche', false, '');
INSERT INTO languages_iso639 VALUES ('pkg', '   ', '   ', '  ', 'I', 'L', 'Pak-Tong', false, '');
INSERT INTO languages_iso639 VALUES ('pkh', '   ', '   ', '  ', 'I', 'L', 'Pankhu', false, '');
INSERT INTO languages_iso639 VALUES ('pkn', '   ', '   ', '  ', 'I', 'L', 'Pakanha', false, '');
INSERT INTO languages_iso639 VALUES ('pko', '   ', '   ', '  ', 'I', 'L', 'Pökoot', false, '');
INSERT INTO languages_iso639 VALUES ('pkp', '   ', '   ', '  ', 'I', 'L', 'Pukapuka', false, '');
INSERT INTO languages_iso639 VALUES ('pkr', '   ', '   ', '  ', 'I', 'L', 'Attapady Kurumba', false, '');
INSERT INTO languages_iso639 VALUES ('pks', '   ', '   ', '  ', 'I', 'L', 'Pakistan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('pkt', '   ', '   ', '  ', 'I', 'L', 'Maleng', false, '');
INSERT INTO languages_iso639 VALUES ('pku', '   ', '   ', '  ', 'I', 'L', 'Paku', false, '');
INSERT INTO languages_iso639 VALUES ('pla', '   ', '   ', '  ', 'I', 'L', 'Miani', false, '');
INSERT INTO languages_iso639 VALUES ('plb', '   ', '   ', '  ', 'I', 'L', 'Polonombauk', false, '');
INSERT INTO languages_iso639 VALUES ('plc', '   ', '   ', '  ', 'I', 'L', 'Central Palawano', false, '');
INSERT INTO languages_iso639 VALUES ('pld', '   ', '   ', '  ', 'I', 'L', 'Polari', false, '');
INSERT INTO languages_iso639 VALUES ('ple', '   ', '   ', '  ', 'I', 'L', 'Palu''e', false, '');
INSERT INTO languages_iso639 VALUES ('plg', '   ', '   ', '  ', 'I', 'L', 'Pilagá', false, '');
INSERT INTO languages_iso639 VALUES ('plh', '   ', '   ', '  ', 'I', 'L', 'Paulohi', false, '');
INSERT INTO languages_iso639 VALUES ('pli', 'pli', 'pli', 'pi', 'I', 'A', 'Pali', false, '');
INSERT INTO languages_iso639 VALUES ('plj', '   ', '   ', '  ', 'I', 'L', 'Polci', false, '');
INSERT INTO languages_iso639 VALUES ('plk', '   ', '   ', '  ', 'I', 'L', 'Kohistani Shina', false, '');
INSERT INTO languages_iso639 VALUES ('pll', '   ', '   ', '  ', 'I', 'L', 'Shwe Palaung', false, '');
INSERT INTO languages_iso639 VALUES ('pln', '   ', '   ', '  ', 'I', 'L', 'Palenquero', false, '');
INSERT INTO languages_iso639 VALUES ('plo', '   ', '   ', '  ', 'I', 'L', 'Oluta Popoluca', false, '');
INSERT INTO languages_iso639 VALUES ('plp', '   ', '   ', '  ', 'I', 'L', 'Palpa', false, '');
INSERT INTO languages_iso639 VALUES ('plq', '   ', '   ', '  ', 'I', 'A', 'Palaic', false, '');
INSERT INTO languages_iso639 VALUES ('plr', '   ', '   ', '  ', 'I', 'L', 'Palaka Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('pls', '   ', '   ', '  ', 'I', 'L', 'San Marcos Tlalcoyalco Popoloca', false, '');
INSERT INTO languages_iso639 VALUES ('plt', '   ', '   ', '  ', 'I', 'L', 'Plateau Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('plu', '   ', '   ', '  ', 'I', 'L', 'Palikúr', false, '');
INSERT INTO languages_iso639 VALUES ('plv', '   ', '   ', '  ', 'I', 'L', 'Southwest Palawano', false, '');
INSERT INTO languages_iso639 VALUES ('plw', '   ', '   ', '  ', 'I', 'L', 'Brooke''s Point Palawano', false, '');
INSERT INTO languages_iso639 VALUES ('ply', '   ', '   ', '  ', 'I', 'L', 'Bolyu', false, '');
INSERT INTO languages_iso639 VALUES ('plz', '   ', '   ', '  ', 'I', 'L', 'Paluan', false, '');
INSERT INTO languages_iso639 VALUES ('pma', '   ', '   ', '  ', 'I', 'L', 'Paama', false, '');
INSERT INTO languages_iso639 VALUES ('pmb', '   ', '   ', '  ', 'I', 'L', 'Pambia', false, '');
INSERT INTO languages_iso639 VALUES ('pmc', '   ', '   ', '  ', 'I', 'E', 'Palumata', false, '');
INSERT INTO languages_iso639 VALUES ('pmd', '   ', '   ', '  ', 'I', 'E', 'Pallanganmiddang', false, '');
INSERT INTO languages_iso639 VALUES ('pme', '   ', '   ', '  ', 'I', 'L', 'Pwaamei', false, '');
INSERT INTO languages_iso639 VALUES ('pmf', '   ', '   ', '  ', 'I', 'L', 'Pamona', false, '');
INSERT INTO languages_iso639 VALUES ('pmh', '   ', '   ', '  ', 'I', 'H', 'Māhārāṣṭri Prākrit', false, '');
INSERT INTO languages_iso639 VALUES ('pmi', '   ', '   ', '  ', 'I', 'L', 'Northern Pumi', false, '');
INSERT INTO languages_iso639 VALUES ('pmj', '   ', '   ', '  ', 'I', 'L', 'Southern Pumi', false, '');
INSERT INTO languages_iso639 VALUES ('pmk', '   ', '   ', '  ', 'I', 'E', 'Pamlico', false, '');
INSERT INTO languages_iso639 VALUES ('pml', '   ', '   ', '  ', 'I', 'E', 'Lingua Franca', false, '');
INSERT INTO languages_iso639 VALUES ('pmm', '   ', '   ', '  ', 'I', 'L', 'Pomo', false, '');
INSERT INTO languages_iso639 VALUES ('pmn', '   ', '   ', '  ', 'I', 'L', 'Pam', false, '');
INSERT INTO languages_iso639 VALUES ('pmo', '   ', '   ', '  ', 'I', 'L', 'Pom', false, '');
INSERT INTO languages_iso639 VALUES ('pmq', '   ', '   ', '  ', 'I', 'L', 'Northern Pame', false, '');
INSERT INTO languages_iso639 VALUES ('pmr', '   ', '   ', '  ', 'I', 'L', 'Paynamar', false, '');
INSERT INTO languages_iso639 VALUES ('pms', '   ', '   ', '  ', 'I', 'L', 'Piemontese', false, '');
INSERT INTO languages_iso639 VALUES ('pmt', '   ', '   ', '  ', 'I', 'L', 'Tuamotuan', false, '');
INSERT INTO languages_iso639 VALUES ('pmu', '   ', '   ', '  ', 'I', 'L', 'Mirpur Panjabi', false, '');
INSERT INTO languages_iso639 VALUES ('pmw', '   ', '   ', '  ', 'I', 'L', 'Plains Miwok', false, '');
INSERT INTO languages_iso639 VALUES ('pmx', '   ', '   ', '  ', 'I', 'L', 'Poumei Naga', false, '');
INSERT INTO languages_iso639 VALUES ('pmy', '   ', '   ', '  ', 'I', 'L', 'Papuan Malay', false, '');
INSERT INTO languages_iso639 VALUES ('pmz', '   ', '   ', '  ', 'I', 'E', 'Southern Pame', false, '');
INSERT INTO languages_iso639 VALUES ('pna', '   ', '   ', '  ', 'I', 'L', 'Punan Bah-Biau', false, '');
INSERT INTO languages_iso639 VALUES ('pnb', '   ', '   ', '  ', 'I', 'L', 'Western Panjabi', false, '');
INSERT INTO languages_iso639 VALUES ('pnc', '   ', '   ', '  ', 'I', 'L', 'Pannei', false, '');
INSERT INTO languages_iso639 VALUES ('pne', '   ', '   ', '  ', 'I', 'L', 'Western Penan', false, '');
INSERT INTO languages_iso639 VALUES ('png', '   ', '   ', '  ', 'I', 'L', 'Pongu', false, '');
INSERT INTO languages_iso639 VALUES ('pnh', '   ', '   ', '  ', 'I', 'L', 'Penrhyn', false, '');
INSERT INTO languages_iso639 VALUES ('pni', '   ', '   ', '  ', 'I', 'L', 'Aoheng', false, '');
INSERT INTO languages_iso639 VALUES ('pnj', '   ', '   ', '  ', 'I', 'E', 'Pinjarup', false, '');
INSERT INTO languages_iso639 VALUES ('pnk', '   ', '   ', '  ', 'I', 'L', 'Paunaka', false, '');
INSERT INTO languages_iso639 VALUES ('pnm', '   ', '   ', '  ', 'I', 'L', 'Punan Batu 1', false, '');
INSERT INTO languages_iso639 VALUES ('pnn', '   ', '   ', '  ', 'I', 'L', 'Pinai-Hagahai', false, '');
INSERT INTO languages_iso639 VALUES ('pno', '   ', '   ', '  ', 'I', 'E', 'Panobo', false, '');
INSERT INTO languages_iso639 VALUES ('pnp', '   ', '   ', '  ', 'I', 'L', 'Pancana', false, '');
INSERT INTO languages_iso639 VALUES ('pnq', '   ', '   ', '  ', 'I', 'L', 'Pana (Burkina Faso)', false, '');
INSERT INTO languages_iso639 VALUES ('pnr', '   ', '   ', '  ', 'I', 'L', 'Panim', false, '');
INSERT INTO languages_iso639 VALUES ('pns', '   ', '   ', '  ', 'I', 'L', 'Ponosakan', false, '');
INSERT INTO languages_iso639 VALUES ('pnt', '   ', '   ', '  ', 'I', 'L', 'Pontic', false, '');
INSERT INTO languages_iso639 VALUES ('pnu', '   ', '   ', '  ', 'I', 'L', 'Jiongnai Bunu', false, '');
INSERT INTO languages_iso639 VALUES ('pnv', '   ', '   ', '  ', 'I', 'L', 'Pinigura', false, '');
INSERT INTO languages_iso639 VALUES ('pnw', '   ', '   ', '  ', 'I', 'L', 'Panytyima', false, '');
INSERT INTO languages_iso639 VALUES ('pnx', '   ', '   ', '  ', 'I', 'L', 'Phong-Kniang', false, '');
INSERT INTO languages_iso639 VALUES ('pny', '   ', '   ', '  ', 'I', 'L', 'Pinyin', false, '');
INSERT INTO languages_iso639 VALUES ('pnz', '   ', '   ', '  ', 'I', 'L', 'Pana (Central African Republic)', false, '');
INSERT INTO languages_iso639 VALUES ('poc', '   ', '   ', '  ', 'I', 'L', 'Poqomam', false, '');
INSERT INTO languages_iso639 VALUES ('pod', '   ', '   ', '  ', 'I', 'E', 'Ponares', false, '');
INSERT INTO languages_iso639 VALUES ('poe', '   ', '   ', '  ', 'I', 'L', 'San Juan Atzingo Popoloca', false, '');
INSERT INTO languages_iso639 VALUES ('pof', '   ', '   ', '  ', 'I', 'L', 'Poke', false, '');
INSERT INTO languages_iso639 VALUES ('pog', '   ', '   ', '  ', 'I', 'E', 'Potiguára', false, '');
INSERT INTO languages_iso639 VALUES ('poh', '   ', '   ', '  ', 'I', 'L', 'Poqomchi''', false, '');
INSERT INTO languages_iso639 VALUES ('poi', '   ', '   ', '  ', 'I', 'L', 'Highland Popoluca', false, '');
INSERT INTO languages_iso639 VALUES ('pok', '   ', '   ', '  ', 'I', 'L', 'Pokangá', false, '');
INSERT INTO languages_iso639 VALUES ('pol', 'pol', 'pol', 'pl', 'I', 'L', 'Polish', false, '');
INSERT INTO languages_iso639 VALUES ('pom', '   ', '   ', '  ', 'I', 'L', 'Southeastern Pomo', false, '');
INSERT INTO languages_iso639 VALUES ('pon', 'pon', 'pon', '  ', 'I', 'L', 'Pohnpeian', false, '');
INSERT INTO languages_iso639 VALUES ('poo', '   ', '   ', '  ', 'I', 'L', 'Central Pomo', false, '');
INSERT INTO languages_iso639 VALUES ('pop', '   ', '   ', '  ', 'I', 'L', 'Pwapwâ', false, '');
INSERT INTO languages_iso639 VALUES ('poq', '   ', '   ', '  ', 'I', 'L', 'Texistepec Popoluca', false, '');
INSERT INTO languages_iso639 VALUES ('por', 'por', 'por', 'pt', 'I', 'L', 'Portuguese', false, '');
INSERT INTO languages_iso639 VALUES ('pos', '   ', '   ', '  ', 'I', 'L', 'Sayula Popoluca', false, '');
INSERT INTO languages_iso639 VALUES ('pot', '   ', '   ', '  ', 'I', 'L', 'Potawatomi', false, '');
INSERT INTO languages_iso639 VALUES ('pov', '   ', '   ', '  ', 'I', 'L', 'Upper Guinea Crioulo', false, '');
INSERT INTO languages_iso639 VALUES ('pow', '   ', '   ', '  ', 'I', 'L', 'San Felipe Otlaltepec Popoloca', false, '');
INSERT INTO languages_iso639 VALUES ('pox', '   ', '   ', '  ', 'I', 'E', 'Polabian', false, '');
INSERT INTO languages_iso639 VALUES ('poy', '   ', '   ', '  ', 'I', 'L', 'Pogolo', false, '');
INSERT INTO languages_iso639 VALUES ('ppa', '   ', '   ', '  ', 'I', 'L', 'Pao', false, '');
INSERT INTO languages_iso639 VALUES ('ppe', '   ', '   ', '  ', 'I', 'L', 'Papi', false, '');
INSERT INTO languages_iso639 VALUES ('ppi', '   ', '   ', '  ', 'I', 'L', 'Paipai', false, '');
INSERT INTO languages_iso639 VALUES ('ppk', '   ', '   ', '  ', 'I', 'L', 'Uma', false, '');
INSERT INTO languages_iso639 VALUES ('ppl', '   ', '   ', '  ', 'I', 'L', 'Pipil', false, '');
INSERT INTO languages_iso639 VALUES ('ppm', '   ', '   ', '  ', 'I', 'L', 'Papuma', false, '');
INSERT INTO languages_iso639 VALUES ('ppn', '   ', '   ', '  ', 'I', 'L', 'Papapana', false, '');
INSERT INTO languages_iso639 VALUES ('ppo', '   ', '   ', '  ', 'I', 'L', 'Folopa', false, '');
INSERT INTO languages_iso639 VALUES ('ppp', '   ', '   ', '  ', 'I', 'L', 'Pelende', false, '');
INSERT INTO languages_iso639 VALUES ('ppq', '   ', '   ', '  ', 'I', 'L', 'Pei', false, '');
INSERT INTO languages_iso639 VALUES ('pps', '   ', '   ', '  ', 'I', 'L', 'San Luís Temalacayuca Popoloca', false, '');
INSERT INTO languages_iso639 VALUES ('ppt', '   ', '   ', '  ', 'I', 'L', 'Pare', false, '');
INSERT INTO languages_iso639 VALUES ('ppu', '   ', '   ', '  ', 'I', 'E', 'Papora', false, '');
INSERT INTO languages_iso639 VALUES ('pqa', '   ', '   ', '  ', 'I', 'L', 'Pa''a', false, '');
INSERT INTO languages_iso639 VALUES ('pqm', '   ', '   ', '  ', 'I', 'L', 'Malecite-Passamaquoddy', false, '');
INSERT INTO languages_iso639 VALUES ('prb', '   ', '   ', '  ', 'I', 'L', 'Lua''', false, '');
INSERT INTO languages_iso639 VALUES ('prc', '   ', '   ', '  ', 'I', 'L', 'Parachi', false, '');
INSERT INTO languages_iso639 VALUES ('prd', '   ', '   ', '  ', 'I', 'L', 'Parsi-Dari', false, '');
INSERT INTO languages_iso639 VALUES ('pre', '   ', '   ', '  ', 'I', 'L', 'Principense', false, '');
INSERT INTO languages_iso639 VALUES ('prf', '   ', '   ', '  ', 'I', 'L', 'Paranan', false, '');
INSERT INTO languages_iso639 VALUES ('prg', '   ', '   ', '  ', 'I', 'L', 'Prussian', false, '');
INSERT INTO languages_iso639 VALUES ('prh', '   ', '   ', '  ', 'I', 'L', 'Porohanon', false, '');
INSERT INTO languages_iso639 VALUES ('pri', '   ', '   ', '  ', 'I', 'L', 'Paicî', false, '');
INSERT INTO languages_iso639 VALUES ('prk', '   ', '   ', '  ', 'I', 'L', 'Parauk', false, '');
INSERT INTO languages_iso639 VALUES ('prl', '   ', '   ', '  ', 'I', 'L', 'Peruvian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('prm', '   ', '   ', '  ', 'I', 'L', 'Kibiri', false, '');
INSERT INTO languages_iso639 VALUES ('prn', '   ', '   ', '  ', 'I', 'L', 'Prasuni', false, '');
INSERT INTO languages_iso639 VALUES ('pro', 'pro', 'pro', '  ', 'I', 'H', 'Old Provençal (to 1500)', false, '');
INSERT INTO languages_iso639 VALUES ('prp', '   ', '   ', '  ', 'I', 'L', 'Parsi', false, '');
INSERT INTO languages_iso639 VALUES ('prq', '   ', '   ', '  ', 'I', 'L', 'Ashéninka Perené', false, '');
INSERT INTO languages_iso639 VALUES ('prr', '   ', '   ', '  ', 'I', 'E', 'Puri', false, '');
INSERT INTO languages_iso639 VALUES ('prs', '   ', '   ', '  ', 'I', 'L', 'Dari', false, '');
INSERT INTO languages_iso639 VALUES ('prt', '   ', '   ', '  ', 'I', 'L', 'Phai', false, '');
INSERT INTO languages_iso639 VALUES ('pru', '   ', '   ', '  ', 'I', 'L', 'Puragi', false, '');
INSERT INTO languages_iso639 VALUES ('prw', '   ', '   ', '  ', 'I', 'L', 'Parawen', false, '');
INSERT INTO languages_iso639 VALUES ('prx', '   ', '   ', '  ', 'I', 'L', 'Purik', false, '');
INSERT INTO languages_iso639 VALUES ('pry', '   ', '   ', '  ', 'I', 'L', 'Pray 3', false, '');
INSERT INTO languages_iso639 VALUES ('prz', '   ', '   ', '  ', 'I', 'L', 'Providencia Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('psa', '   ', '   ', '  ', 'I', 'L', 'Asue Awyu', false, '');
INSERT INTO languages_iso639 VALUES ('psc', '   ', '   ', '  ', 'I', 'L', 'Persian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('psd', '   ', '   ', '  ', 'I', 'L', 'Plains Indian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('pse', '   ', '   ', '  ', 'I', 'L', 'Central Malay', false, '');
INSERT INTO languages_iso639 VALUES ('psg', '   ', '   ', '  ', 'I', 'L', 'Penang Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('psh', '   ', '   ', '  ', 'I', 'L', 'Southwest Pashayi', false, '');
INSERT INTO languages_iso639 VALUES ('psi', '   ', '   ', '  ', 'I', 'L', 'Southeast Pashayi', false, '');
INSERT INTO languages_iso639 VALUES ('psl', '   ', '   ', '  ', 'I', 'L', 'Puerto Rican Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('psm', '   ', '   ', '  ', 'I', 'E', 'Pauserna', false, '');
INSERT INTO languages_iso639 VALUES ('psn', '   ', '   ', '  ', 'I', 'L', 'Panasuan', false, '');
INSERT INTO languages_iso639 VALUES ('pso', '   ', '   ', '  ', 'I', 'L', 'Polish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('psp', '   ', '   ', '  ', 'I', 'L', 'Philippine Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('psq', '   ', '   ', '  ', 'I', 'L', 'Pasi', false, '');
INSERT INTO languages_iso639 VALUES ('psr', '   ', '   ', '  ', 'I', 'L', 'Portuguese Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('pss', '   ', '   ', '  ', 'I', 'L', 'Kaulong', false, '');
INSERT INTO languages_iso639 VALUES ('pst', '   ', '   ', '  ', 'I', 'L', 'Central Pashto', false, '');
INSERT INTO languages_iso639 VALUES ('psu', '   ', '   ', '  ', 'I', 'H', 'Sauraseni Prākrit', false, '');
INSERT INTO languages_iso639 VALUES ('psw', '   ', '   ', '  ', 'I', 'L', 'Port Sandwich', false, '');
INSERT INTO languages_iso639 VALUES ('psy', '   ', '   ', '  ', 'I', 'E', 'Piscataway', false, '');
INSERT INTO languages_iso639 VALUES ('pta', '   ', '   ', '  ', 'I', 'L', 'Pai Tavytera', false, '');
INSERT INTO languages_iso639 VALUES ('pth', '   ', '   ', '  ', 'I', 'E', 'Pataxó Hã-Ha-Hãe', false, '');
INSERT INTO languages_iso639 VALUES ('pti', '   ', '   ', '  ', 'I', 'L', 'Pintiini', false, '');
INSERT INTO languages_iso639 VALUES ('ptn', '   ', '   ', '  ', 'I', 'L', 'Patani', false, '');
INSERT INTO languages_iso639 VALUES ('pto', '   ', '   ', '  ', 'I', 'L', 'Zo''é', false, '');
INSERT INTO languages_iso639 VALUES ('ptp', '   ', '   ', '  ', 'I', 'L', 'Patep', false, '');
INSERT INTO languages_iso639 VALUES ('ptr', '   ', '   ', '  ', 'I', 'L', 'Piamatsina', false, '');
INSERT INTO languages_iso639 VALUES ('ptt', '   ', '   ', '  ', 'I', 'L', 'Enrekang', false, '');
INSERT INTO languages_iso639 VALUES ('ptu', '   ', '   ', '  ', 'I', 'L', 'Bambam', false, '');
INSERT INTO languages_iso639 VALUES ('ptv', '   ', '   ', '  ', 'I', 'L', 'Port Vato', false, '');
INSERT INTO languages_iso639 VALUES ('ptw', '   ', '   ', '  ', 'I', 'E', 'Pentlatch', false, '');
INSERT INTO languages_iso639 VALUES ('pty', '   ', '   ', '  ', 'I', 'L', 'Pathiya', false, '');
INSERT INTO languages_iso639 VALUES ('pua', '   ', '   ', '  ', 'I', 'L', 'Western Highland Purepecha', false, '');
INSERT INTO languages_iso639 VALUES ('pub', '   ', '   ', '  ', 'I', 'L', 'Purum', false, '');
INSERT INTO languages_iso639 VALUES ('puc', '   ', '   ', '  ', 'I', 'L', 'Punan Merap', false, '');
INSERT INTO languages_iso639 VALUES ('pud', '   ', '   ', '  ', 'I', 'L', 'Punan Aput', false, '');
INSERT INTO languages_iso639 VALUES ('pue', '   ', '   ', '  ', 'I', 'L', 'Puelche', false, '');
INSERT INTO languages_iso639 VALUES ('puf', '   ', '   ', '  ', 'I', 'L', 'Punan Merah', false, '');
INSERT INTO languages_iso639 VALUES ('pug', '   ', '   ', '  ', 'I', 'L', 'Phuie', false, '');
INSERT INTO languages_iso639 VALUES ('pui', '   ', '   ', '  ', 'I', 'L', 'Puinave', false, '');
INSERT INTO languages_iso639 VALUES ('puj', '   ', '   ', '  ', 'I', 'L', 'Punan Tubu', false, '');
INSERT INTO languages_iso639 VALUES ('puk', '   ', '   ', '  ', 'I', 'L', 'Pu Ko', false, '');
INSERT INTO languages_iso639 VALUES ('pum', '   ', '   ', '  ', 'I', 'L', 'Puma', false, '');
INSERT INTO languages_iso639 VALUES ('puo', '   ', '   ', '  ', 'I', 'L', 'Puoc', false, '');
INSERT INTO languages_iso639 VALUES ('pup', '   ', '   ', '  ', 'I', 'L', 'Pulabu', false, '');
INSERT INTO languages_iso639 VALUES ('puq', '   ', '   ', '  ', 'I', 'E', 'Puquina', false, '');
INSERT INTO languages_iso639 VALUES ('pur', '   ', '   ', '  ', 'I', 'L', 'Puruborá', false, '');
INSERT INTO languages_iso639 VALUES ('pus', 'pus', 'pus', 'ps', 'M', 'L', 'Pushto', false, '');
INSERT INTO languages_iso639 VALUES ('put', '   ', '   ', '  ', 'I', 'L', 'Putoh', false, '');
INSERT INTO languages_iso639 VALUES ('puu', '   ', '   ', '  ', 'I', 'L', 'Punu', false, '');
INSERT INTO languages_iso639 VALUES ('puw', '   ', '   ', '  ', 'I', 'L', 'Puluwatese', false, '');
INSERT INTO languages_iso639 VALUES ('pux', '   ', '   ', '  ', 'I', 'L', 'Puare', false, '');
INSERT INTO languages_iso639 VALUES ('puy', '   ', '   ', '  ', 'I', 'E', 'Purisimeño', false, '');
INSERT INTO languages_iso639 VALUES ('puz', '   ', '   ', '  ', 'I', 'L', 'Purum Naga', false, '');
INSERT INTO languages_iso639 VALUES ('pwa', '   ', '   ', '  ', 'I', 'L', 'Pawaia', false, '');
INSERT INTO languages_iso639 VALUES ('pwb', '   ', '   ', '  ', 'I', 'L', 'Panawa', false, '');
INSERT INTO languages_iso639 VALUES ('pwg', '   ', '   ', '  ', 'I', 'L', 'Gapapaiwa', false, '');
INSERT INTO languages_iso639 VALUES ('pwi', '   ', '   ', '  ', 'I', 'E', 'Patwin', false, '');
INSERT INTO languages_iso639 VALUES ('pwm', '   ', '   ', '  ', 'I', 'L', 'Molbog', false, '');
INSERT INTO languages_iso639 VALUES ('pwn', '   ', '   ', '  ', 'I', 'L', 'Paiwan', false, '');
INSERT INTO languages_iso639 VALUES ('pwo', '   ', '   ', '  ', 'I', 'L', 'Pwo Western Karen', false, '');
INSERT INTO languages_iso639 VALUES ('pwr', '   ', '   ', '  ', 'I', 'L', 'Powari', false, '');
INSERT INTO languages_iso639 VALUES ('pww', '   ', '   ', '  ', 'I', 'L', 'Pwo Northern Karen', false, '');
INSERT INTO languages_iso639 VALUES ('pxm', '   ', '   ', '  ', 'I', 'L', 'Quetzaltepec Mixe', false, '');
INSERT INTO languages_iso639 VALUES ('pye', '   ', '   ', '  ', 'I', 'L', 'Pye Krumen', false, '');
INSERT INTO languages_iso639 VALUES ('pym', '   ', '   ', '  ', 'I', 'L', 'Fyam', false, '');
INSERT INTO languages_iso639 VALUES ('pyn', '   ', '   ', '  ', 'I', 'L', 'Poyanáwa', false, '');
INSERT INTO languages_iso639 VALUES ('pys', '   ', '   ', '  ', 'I', 'L', 'Paraguayan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('pyu', '   ', '   ', '  ', 'I', 'L', 'Puyuma', false, '');
INSERT INTO languages_iso639 VALUES ('pyx', '   ', '   ', '  ', 'I', 'A', 'Pyu (Myanmar)', false, '');
INSERT INTO languages_iso639 VALUES ('pyy', '   ', '   ', '  ', 'I', 'L', 'Pyen', false, '');
INSERT INTO languages_iso639 VALUES ('pzn', '   ', '   ', '  ', 'I', 'L', 'Para Naga', false, '');
INSERT INTO languages_iso639 VALUES ('qua', '   ', '   ', '  ', 'I', 'L', 'Quapaw', false, '');
INSERT INTO languages_iso639 VALUES ('qub', '   ', '   ', '  ', 'I', 'L', 'Huallaga Huánuco Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('quc', '   ', '   ', '  ', 'I', 'L', 'K''iche''', false, '');
INSERT INTO languages_iso639 VALUES ('qud', '   ', '   ', '  ', 'I', 'L', 'Calderón Highland Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('que', 'que', 'que', 'qu', 'M', 'L', 'Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('quf', '   ', '   ', '  ', 'I', 'L', 'Lambayeque Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qug', '   ', '   ', '  ', 'I', 'L', 'Chimborazo Highland Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('quh', '   ', '   ', '  ', 'I', 'L', 'South Bolivian Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qui', '   ', '   ', '  ', 'I', 'L', 'Quileute', false, '');
INSERT INTO languages_iso639 VALUES ('quk', '   ', '   ', '  ', 'I', 'L', 'Chachapoyas Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qul', '   ', '   ', '  ', 'I', 'L', 'North Bolivian Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qum', '   ', '   ', '  ', 'I', 'L', 'Sipacapense', false, '');
INSERT INTO languages_iso639 VALUES ('qun', '   ', '   ', '  ', 'I', 'E', 'Quinault', false, '');
INSERT INTO languages_iso639 VALUES ('qup', '   ', '   ', '  ', 'I', 'L', 'Southern Pastaza Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('quq', '   ', '   ', '  ', 'I', 'L', 'Quinqui', false, '');
INSERT INTO languages_iso639 VALUES ('qur', '   ', '   ', '  ', 'I', 'L', 'Yanahuanca Pasco Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qus', '   ', '   ', '  ', 'I', 'L', 'Santiago del Estero Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('quv', '   ', '   ', '  ', 'I', 'L', 'Sacapulteco', false, '');
INSERT INTO languages_iso639 VALUES ('quw', '   ', '   ', '  ', 'I', 'L', 'Tena Lowland Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('qux', '   ', '   ', '  ', 'I', 'L', 'Yauyos Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('quy', '   ', '   ', '  ', 'I', 'L', 'Ayacucho Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('quz', '   ', '   ', '  ', 'I', 'L', 'Cusco Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qva', '   ', '   ', '  ', 'I', 'L', 'Ambo-Pasco Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvc', '   ', '   ', '  ', 'I', 'L', 'Cajamarca Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qve', '   ', '   ', '  ', 'I', 'L', 'Eastern Apurímac Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvh', '   ', '   ', '  ', 'I', 'L', 'Huamalíes-Dos de Mayo Huánuco Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvi', '   ', '   ', '  ', 'I', 'L', 'Imbabura Highland Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('qvj', '   ', '   ', '  ', 'I', 'L', 'Loja Highland Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('qvl', '   ', '   ', '  ', 'I', 'L', 'Cajatambo North Lima Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvm', '   ', '   ', '  ', 'I', 'L', 'Margos-Yarowilca-Lauricocha Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvn', '   ', '   ', '  ', 'I', 'L', 'North Junín Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvo', '   ', '   ', '  ', 'I', 'L', 'Napo Lowland Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvp', '   ', '   ', '  ', 'I', 'L', 'Pacaraos Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvs', '   ', '   ', '  ', 'I', 'L', 'San Martín Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvw', '   ', '   ', '  ', 'I', 'L', 'Huaylla Wanca Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qvy', '   ', '   ', '  ', 'I', 'L', 'Queyu', false, '');
INSERT INTO languages_iso639 VALUES ('qvz', '   ', '   ', '  ', 'I', 'L', 'Northern Pastaza Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('qwa', '   ', '   ', '  ', 'I', 'L', 'Corongo Ancash Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qwc', '   ', '   ', '  ', 'I', 'H', 'Classical Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qwh', '   ', '   ', '  ', 'I', 'L', 'Huaylas Ancash Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qwm', '   ', '   ', '  ', 'I', 'E', 'Kuman (Russia)', false, '');
INSERT INTO languages_iso639 VALUES ('qws', '   ', '   ', '  ', 'I', 'L', 'Sihuas Ancash Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qwt', '   ', '   ', '  ', 'I', 'E', 'Kwalhioqua-Tlatskanai', false, '');
INSERT INTO languages_iso639 VALUES ('qxa', '   ', '   ', '  ', 'I', 'L', 'Chiquián Ancash Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxc', '   ', '   ', '  ', 'I', 'L', 'Chincha Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxh', '   ', '   ', '  ', 'I', 'L', 'Panao Huánuco Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxl', '   ', '   ', '  ', 'I', 'L', 'Salasaca Highland Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('qxn', '   ', '   ', '  ', 'I', 'L', 'Northern Conchucos Ancash Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxo', '   ', '   ', '  ', 'I', 'L', 'Southern Conchucos Ancash Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxp', '   ', '   ', '  ', 'I', 'L', 'Puno Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxq', '   ', '   ', '  ', 'I', 'L', 'Qashqa''i', false, '');
INSERT INTO languages_iso639 VALUES ('qxr', '   ', '   ', '  ', 'I', 'L', 'Cañar Highland Quichua', false, '');
INSERT INTO languages_iso639 VALUES ('qxs', '   ', '   ', '  ', 'I', 'L', 'Southern Qiang', false, '');
INSERT INTO languages_iso639 VALUES ('qxt', '   ', '   ', '  ', 'I', 'L', 'Santa Ana de Tusi Pasco Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxu', '   ', '   ', '  ', 'I', 'L', 'Arequipa-La Unión Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qxw', '   ', '   ', '  ', 'I', 'L', 'Jauja Wanca Quechua', false, '');
INSERT INTO languages_iso639 VALUES ('qya', '   ', '   ', '  ', 'I', 'C', 'Quenya', false, '');
INSERT INTO languages_iso639 VALUES ('qyp', '   ', '   ', '  ', 'I', 'E', 'Quiripi', false, '');
INSERT INTO languages_iso639 VALUES ('raa', '   ', '   ', '  ', 'I', 'L', 'Dungmali', false, '');
INSERT INTO languages_iso639 VALUES ('rab', '   ', '   ', '  ', 'I', 'L', 'Camling', false, '');
INSERT INTO languages_iso639 VALUES ('rac', '   ', '   ', '  ', 'I', 'L', 'Rasawa', false, '');
INSERT INTO languages_iso639 VALUES ('rad', '   ', '   ', '  ', 'I', 'L', 'Rade', false, '');
INSERT INTO languages_iso639 VALUES ('raf', '   ', '   ', '  ', 'I', 'L', 'Western Meohang', false, '');
INSERT INTO languages_iso639 VALUES ('rag', '   ', '   ', '  ', 'I', 'L', 'Logooli', false, '');
INSERT INTO languages_iso639 VALUES ('rah', '   ', '   ', '  ', 'I', 'L', 'Rabha', false, '');
INSERT INTO languages_iso639 VALUES ('rai', '   ', '   ', '  ', 'I', 'L', 'Ramoaaina', false, '');
INSERT INTO languages_iso639 VALUES ('raj', 'raj', 'raj', '  ', 'M', 'L', 'Rajasthani', false, '');
INSERT INTO languages_iso639 VALUES ('rak', '   ', '   ', '  ', 'I', 'L', 'Tulu-Bohuai', false, '');
INSERT INTO languages_iso639 VALUES ('ral', '   ', '   ', '  ', 'I', 'L', 'Ralte', false, '');
INSERT INTO languages_iso639 VALUES ('ram', '   ', '   ', '  ', 'I', 'L', 'Canela', false, '');
INSERT INTO languages_iso639 VALUES ('ran', '   ', '   ', '  ', 'I', 'L', 'Riantana', false, '');
INSERT INTO languages_iso639 VALUES ('rao', '   ', '   ', '  ', 'I', 'L', 'Rao', false, '');
INSERT INTO languages_iso639 VALUES ('rap', 'rap', 'rap', '  ', 'I', 'L', 'Rapanui', false, '');
INSERT INTO languages_iso639 VALUES ('raq', '   ', '   ', '  ', 'I', 'L', 'Saam', false, '');
INSERT INTO languages_iso639 VALUES ('rar', 'rar', 'rar', '  ', 'I', 'L', 'Rarotongan', false, '');
INSERT INTO languages_iso639 VALUES ('ras', '   ', '   ', '  ', 'I', 'L', 'Tegali', false, '');
INSERT INTO languages_iso639 VALUES ('rat', '   ', '   ', '  ', 'I', 'L', 'Razajerdi', false, '');
INSERT INTO languages_iso639 VALUES ('rau', '   ', '   ', '  ', 'I', 'L', 'Raute', false, '');
INSERT INTO languages_iso639 VALUES ('rav', '   ', '   ', '  ', 'I', 'L', 'Sampang', false, '');
INSERT INTO languages_iso639 VALUES ('raw', '   ', '   ', '  ', 'I', 'L', 'Rawang', false, '');
INSERT INTO languages_iso639 VALUES ('rax', '   ', '   ', '  ', 'I', 'L', 'Rang', false, '');
INSERT INTO languages_iso639 VALUES ('ray', '   ', '   ', '  ', 'I', 'L', 'Rapa', false, '');
INSERT INTO languages_iso639 VALUES ('raz', '   ', '   ', '  ', 'I', 'L', 'Rahambuu', false, '');
INSERT INTO languages_iso639 VALUES ('rbb', '   ', '   ', '  ', 'I', 'L', 'Rumai Palaung', false, '');
INSERT INTO languages_iso639 VALUES ('rbk', '   ', '   ', '  ', 'I', 'L', 'Northern Bontok', false, '');
INSERT INTO languages_iso639 VALUES ('rbl', '   ', '   ', '  ', 'I', 'L', 'Miraya Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('rbp', '   ', '   ', '  ', 'I', 'E', 'Barababaraba', false, '');
INSERT INTO languages_iso639 VALUES ('rcf', '   ', '   ', '  ', 'I', 'L', 'Réunion Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('rdb', '   ', '   ', '  ', 'I', 'L', 'Rudbari', false, '');
INSERT INTO languages_iso639 VALUES ('rea', '   ', '   ', '  ', 'I', 'L', 'Rerau', false, '');
INSERT INTO languages_iso639 VALUES ('reb', '   ', '   ', '  ', 'I', 'L', 'Rembong', false, '');
INSERT INTO languages_iso639 VALUES ('ree', '   ', '   ', '  ', 'I', 'L', 'Rejang Kayan', false, '');
INSERT INTO languages_iso639 VALUES ('reg', '   ', '   ', '  ', 'I', 'L', 'Kara (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('rei', '   ', '   ', '  ', 'I', 'L', 'Reli', false, '');
INSERT INTO languages_iso639 VALUES ('rej', '   ', '   ', '  ', 'I', 'L', 'Rejang', false, '');
INSERT INTO languages_iso639 VALUES ('rel', '   ', '   ', '  ', 'I', 'L', 'Rendille', false, '');
INSERT INTO languages_iso639 VALUES ('rem', '   ', '   ', '  ', 'I', 'E', 'Remo', false, '');
INSERT INTO languages_iso639 VALUES ('ren', '   ', '   ', '  ', 'I', 'L', 'Rengao', false, '');
INSERT INTO languages_iso639 VALUES ('rer', '   ', '   ', '  ', 'I', 'E', 'Rer Bare', false, '');
INSERT INTO languages_iso639 VALUES ('res', '   ', '   ', '  ', 'I', 'L', 'Reshe', false, '');
INSERT INTO languages_iso639 VALUES ('ret', '   ', '   ', '  ', 'I', 'L', 'Retta', false, '');
INSERT INTO languages_iso639 VALUES ('rey', '   ', '   ', '  ', 'I', 'L', 'Reyesano', false, '');
INSERT INTO languages_iso639 VALUES ('rga', '   ', '   ', '  ', 'I', 'L', 'Roria', false, '');
INSERT INTO languages_iso639 VALUES ('rge', '   ', '   ', '  ', 'I', 'L', 'Romano-Greek', false, '');
INSERT INTO languages_iso639 VALUES ('rgk', '   ', '   ', '  ', 'I', 'E', 'Rangkas', false, '');
INSERT INTO languages_iso639 VALUES ('rgn', '   ', '   ', '  ', 'I', 'L', 'Romagnol', false, '');
INSERT INTO languages_iso639 VALUES ('rgr', '   ', '   ', '  ', 'I', 'L', 'Resígaro', false, '');
INSERT INTO languages_iso639 VALUES ('rgs', '   ', '   ', '  ', 'I', 'L', 'Southern Roglai', false, '');
INSERT INTO languages_iso639 VALUES ('rgu', '   ', '   ', '  ', 'I', 'L', 'Ringgou', false, '');
INSERT INTO languages_iso639 VALUES ('rhg', '   ', '   ', '  ', 'I', 'L', 'Rohingya', false, '');
INSERT INTO languages_iso639 VALUES ('rhp', '   ', '   ', '  ', 'I', 'L', 'Yahang', false, '');
INSERT INTO languages_iso639 VALUES ('ria', '   ', '   ', '  ', 'I', 'L', 'Riang (India)', false, '');
INSERT INTO languages_iso639 VALUES ('rie', '   ', '   ', '  ', 'I', 'L', 'Rien', false, '');
INSERT INTO languages_iso639 VALUES ('rif', '   ', '   ', '  ', 'I', 'L', 'Tarifit', false, '');
INSERT INTO languages_iso639 VALUES ('ril', '   ', '   ', '  ', 'I', 'L', 'Riang (Myanmar)', false, '');
INSERT INTO languages_iso639 VALUES ('rim', '   ', '   ', '  ', 'I', 'L', 'Nyaturu', false, '');
INSERT INTO languages_iso639 VALUES ('rin', '   ', '   ', '  ', 'I', 'L', 'Nungu', false, '');
INSERT INTO languages_iso639 VALUES ('rir', '   ', '   ', '  ', 'I', 'L', 'Ribun', false, '');
INSERT INTO languages_iso639 VALUES ('rit', '   ', '   ', '  ', 'I', 'L', 'Ritarungo', false, '');
INSERT INTO languages_iso639 VALUES ('riu', '   ', '   ', '  ', 'I', 'L', 'Riung', false, '');
INSERT INTO languages_iso639 VALUES ('rjg', '   ', '   ', '  ', 'I', 'L', 'Rajong', false, '');
INSERT INTO languages_iso639 VALUES ('rji', '   ', '   ', '  ', 'I', 'L', 'Raji', false, '');
INSERT INTO languages_iso639 VALUES ('rjs', '   ', '   ', '  ', 'I', 'L', 'Rajbanshi', false, '');
INSERT INTO languages_iso639 VALUES ('rka', '   ', '   ', '  ', 'I', 'L', 'Kraol', false, '');
INSERT INTO languages_iso639 VALUES ('rkb', '   ', '   ', '  ', 'I', 'L', 'Rikbaktsa', false, '');
INSERT INTO languages_iso639 VALUES ('rkh', '   ', '   ', '  ', 'I', 'L', 'Rakahanga-Manihiki', false, '');
INSERT INTO languages_iso639 VALUES ('rki', '   ', '   ', '  ', 'I', 'L', 'Rakhine', false, '');
INSERT INTO languages_iso639 VALUES ('rkm', '   ', '   ', '  ', 'I', 'L', 'Marka', false, '');
INSERT INTO languages_iso639 VALUES ('rkt', '   ', '   ', '  ', 'I', 'L', 'Rangpuri', false, '');
INSERT INTO languages_iso639 VALUES ('rkw', '   ', '   ', '  ', 'I', 'E', 'Arakwal', false, '');
INSERT INTO languages_iso639 VALUES ('rma', '   ', '   ', '  ', 'I', 'L', 'Rama', false, '');
INSERT INTO languages_iso639 VALUES ('rmb', '   ', '   ', '  ', 'I', 'L', 'Rembarunga', false, '');
INSERT INTO languages_iso639 VALUES ('rmc', '   ', '   ', '  ', 'I', 'L', 'Carpathian Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmd', '   ', '   ', '  ', 'I', 'E', 'Traveller Danish', false, '');
INSERT INTO languages_iso639 VALUES ('rme', '   ', '   ', '  ', 'I', 'L', 'Angloromani', false, '');
INSERT INTO languages_iso639 VALUES ('rmf', '   ', '   ', '  ', 'I', 'L', 'Kalo Finnish Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmg', '   ', '   ', '  ', 'I', 'L', 'Traveller Norwegian', false, '');
INSERT INTO languages_iso639 VALUES ('rmh', '   ', '   ', '  ', 'I', 'L', 'Murkim', false, '');
INSERT INTO languages_iso639 VALUES ('rmi', '   ', '   ', '  ', 'I', 'L', 'Lomavren', false, '');
INSERT INTO languages_iso639 VALUES ('rmk', '   ', '   ', '  ', 'I', 'L', 'Romkun', false, '');
INSERT INTO languages_iso639 VALUES ('rml', '   ', '   ', '  ', 'I', 'L', 'Baltic Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmm', '   ', '   ', '  ', 'I', 'L', 'Roma', false, '');
INSERT INTO languages_iso639 VALUES ('rmn', '   ', '   ', '  ', 'I', 'L', 'Balkan Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmo', '   ', '   ', '  ', 'I', 'L', 'Sinte Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmp', '   ', '   ', '  ', 'I', 'L', 'Rempi', false, '');
INSERT INTO languages_iso639 VALUES ('rmq', '   ', '   ', '  ', 'I', 'L', 'Caló', false, '');
INSERT INTO languages_iso639 VALUES ('rms', '   ', '   ', '  ', 'I', 'L', 'Romanian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('rmt', '   ', '   ', '  ', 'I', 'L', 'Domari', false, '');
INSERT INTO languages_iso639 VALUES ('rmu', '   ', '   ', '  ', 'I', 'L', 'Tavringer Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmv', '   ', '   ', '  ', 'I', 'C', 'Romanova', false, '');
INSERT INTO languages_iso639 VALUES ('rmw', '   ', '   ', '  ', 'I', 'L', 'Welsh Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmx', '   ', '   ', '  ', 'I', 'L', 'Romam', false, '');
INSERT INTO languages_iso639 VALUES ('rmy', '   ', '   ', '  ', 'I', 'L', 'Vlax Romani', false, '');
INSERT INTO languages_iso639 VALUES ('rmz', '   ', '   ', '  ', 'I', 'L', 'Marma', false, '');
INSERT INTO languages_iso639 VALUES ('rna', '   ', '   ', '  ', 'I', 'E', 'Runa', false, '');
INSERT INTO languages_iso639 VALUES ('rnd', '   ', '   ', '  ', 'I', 'L', 'Ruund', false, '');
INSERT INTO languages_iso639 VALUES ('rng', '   ', '   ', '  ', 'I', 'L', 'Ronga', false, '');
INSERT INTO languages_iso639 VALUES ('rnl', '   ', '   ', '  ', 'I', 'L', 'Ranglong', false, '');
INSERT INTO languages_iso639 VALUES ('rnn', '   ', '   ', '  ', 'I', 'L', 'Roon', false, '');
INSERT INTO languages_iso639 VALUES ('rnp', '   ', '   ', '  ', 'I', 'L', 'Rongpo', false, '');
INSERT INTO languages_iso639 VALUES ('rnr', '   ', '   ', '  ', 'I', 'E', 'Nari Nari', false, '');
INSERT INTO languages_iso639 VALUES ('rnw', '   ', '   ', '  ', 'I', 'L', 'Rungwa', false, '');
INSERT INTO languages_iso639 VALUES ('rob', '   ', '   ', '  ', 'I', 'L', 'Tae''', false, '');
INSERT INTO languages_iso639 VALUES ('roc', '   ', '   ', '  ', 'I', 'L', 'Cacgia Roglai', false, '');
INSERT INTO languages_iso639 VALUES ('rod', '   ', '   ', '  ', 'I', 'L', 'Rogo', false, '');
INSERT INTO languages_iso639 VALUES ('roe', '   ', '   ', '  ', 'I', 'L', 'Ronji', false, '');
INSERT INTO languages_iso639 VALUES ('rof', '   ', '   ', '  ', 'I', 'L', 'Rombo', false, '');
INSERT INTO languages_iso639 VALUES ('rog', '   ', '   ', '  ', 'I', 'L', 'Northern Roglai', false, '');
INSERT INTO languages_iso639 VALUES ('roh', 'roh', 'roh', 'rm', 'I', 'L', 'Romansh', false, '');
INSERT INTO languages_iso639 VALUES ('rol', '   ', '   ', '  ', 'I', 'L', 'Romblomanon', false, '');
INSERT INTO languages_iso639 VALUES ('rom', 'rom', 'rom', '  ', 'M', 'L', 'Romany', false, '');
INSERT INTO languages_iso639 VALUES ('ron', 'rum', 'ron', 'ro', 'I', 'L', 'Romanian', false, '');
INSERT INTO languages_iso639 VALUES ('roo', '   ', '   ', '  ', 'I', 'L', 'Rotokas', false, '');
INSERT INTO languages_iso639 VALUES ('rop', '   ', '   ', '  ', 'I', 'L', 'Kriol', false, '');
INSERT INTO languages_iso639 VALUES ('ror', '   ', '   ', '  ', 'I', 'L', 'Rongga', false, '');
INSERT INTO languages_iso639 VALUES ('rou', '   ', '   ', '  ', 'I', 'L', 'Runga', false, '');
INSERT INTO languages_iso639 VALUES ('row', '   ', '   ', '  ', 'I', 'L', 'Dela-Oenale', false, '');
INSERT INTO languages_iso639 VALUES ('rpn', '   ', '   ', '  ', 'I', 'L', 'Repanbitip', false, '');
INSERT INTO languages_iso639 VALUES ('rpt', '   ', '   ', '  ', 'I', 'L', 'Rapting', false, '');
INSERT INTO languages_iso639 VALUES ('rri', '   ', '   ', '  ', 'I', 'L', 'Ririo', false, '');
INSERT INTO languages_iso639 VALUES ('rro', '   ', '   ', '  ', 'I', 'L', 'Waima', false, '');
INSERT INTO languages_iso639 VALUES ('rrt', '   ', '   ', '  ', 'I', 'E', 'Arritinngithigh', false, '');
INSERT INTO languages_iso639 VALUES ('rsb', '   ', '   ', '  ', 'I', 'L', 'Romano-Serbian', false, '');
INSERT INTO languages_iso639 VALUES ('rsi', '   ', '   ', '  ', 'I', 'L', 'Rennellese Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('rsl', '   ', '   ', '  ', 'I', 'L', 'Russian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('rtc', '   ', '   ', '  ', 'I', 'L', 'Rungtu Chin', false, '');
INSERT INTO languages_iso639 VALUES ('rth', '   ', '   ', '  ', 'I', 'L', 'Ratahan', false, '');
INSERT INTO languages_iso639 VALUES ('rtm', '   ', '   ', '  ', 'I', 'L', 'Rotuman', false, '');
INSERT INTO languages_iso639 VALUES ('rtw', '   ', '   ', '  ', 'I', 'L', 'Rathawi', false, '');
INSERT INTO languages_iso639 VALUES ('rub', '   ', '   ', '  ', 'I', 'L', 'Gungu', false, '');
INSERT INTO languages_iso639 VALUES ('ruc', '   ', '   ', '  ', 'I', 'L', 'Ruuli', false, '');
INSERT INTO languages_iso639 VALUES ('rue', '   ', '   ', '  ', 'I', 'L', 'Rusyn', false, '');
INSERT INTO languages_iso639 VALUES ('ruf', '   ', '   ', '  ', 'I', 'L', 'Luguru', false, '');
INSERT INTO languages_iso639 VALUES ('rug', '   ', '   ', '  ', 'I', 'L', 'Roviana', false, '');
INSERT INTO languages_iso639 VALUES ('ruh', '   ', '   ', '  ', 'I', 'L', 'Ruga', false, '');
INSERT INTO languages_iso639 VALUES ('rui', '   ', '   ', '  ', 'I', 'L', 'Rufiji', false, '');
INSERT INTO languages_iso639 VALUES ('ruk', '   ', '   ', '  ', 'I', 'L', 'Che', false, '');
INSERT INTO languages_iso639 VALUES ('run', 'run', 'run', 'rn', 'I', 'L', 'Rundi', false, '');
INSERT INTO languages_iso639 VALUES ('ruo', '   ', '   ', '  ', 'I', 'L', 'Istro Romanian', false, '');
INSERT INTO languages_iso639 VALUES ('rup', 'rup', 'rup', '  ', 'I', 'L', 'Macedo-Romanian', false, '');
INSERT INTO languages_iso639 VALUES ('ruq', '   ', '   ', '  ', 'I', 'L', 'Megleno Romanian', false, '');
INSERT INTO languages_iso639 VALUES ('rus', 'rus', 'rus', 'ru', 'I', 'L', 'Russian', false, '');
INSERT INTO languages_iso639 VALUES ('rut', '   ', '   ', '  ', 'I', 'L', 'Rutul', false, '');
INSERT INTO languages_iso639 VALUES ('ruu', '   ', '   ', '  ', 'I', 'L', 'Lanas Lobu', false, '');
INSERT INTO languages_iso639 VALUES ('ruy', '   ', '   ', '  ', 'I', 'L', 'Mala (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('ruz', '   ', '   ', '  ', 'I', 'L', 'Ruma', false, '');
INSERT INTO languages_iso639 VALUES ('rwa', '   ', '   ', '  ', 'I', 'L', 'Rawo', false, '');
INSERT INTO languages_iso639 VALUES ('rwk', '   ', '   ', '  ', 'I', 'L', 'Rwa', false, '');
INSERT INTO languages_iso639 VALUES ('rwm', '   ', '   ', '  ', 'I', 'L', 'Amba (Uganda)', false, '');
INSERT INTO languages_iso639 VALUES ('rwo', '   ', '   ', '  ', 'I', 'L', 'Rawa', false, '');
INSERT INTO languages_iso639 VALUES ('rwr', '   ', '   ', '  ', 'I', 'L', 'Marwari (India)', false, '');
INSERT INTO languages_iso639 VALUES ('rxd', '   ', '   ', '  ', 'I', 'L', 'Ngardi', false, '');
INSERT INTO languages_iso639 VALUES ('rxw', '   ', '   ', '  ', 'I', 'E', 'Karuwali', false, '');
INSERT INTO languages_iso639 VALUES ('ryn', '   ', '   ', '  ', 'I', 'L', 'Northern Amami-Oshima', false, '');
INSERT INTO languages_iso639 VALUES ('rys', '   ', '   ', '  ', 'I', 'L', 'Yaeyama', false, '');
INSERT INTO languages_iso639 VALUES ('ryu', '   ', '   ', '  ', 'I', 'L', 'Central Okinawan', false, '');
INSERT INTO languages_iso639 VALUES ('saa', '   ', '   ', '  ', 'I', 'L', 'Saba', false, '');
INSERT INTO languages_iso639 VALUES ('sab', '   ', '   ', '  ', 'I', 'L', 'Buglere', false, '');
INSERT INTO languages_iso639 VALUES ('sac', '   ', '   ', '  ', 'I', 'L', 'Meskwaki', false, '');
INSERT INTO languages_iso639 VALUES ('sad', 'sad', 'sad', '  ', 'I', 'L', 'Sandawe', false, '');
INSERT INTO languages_iso639 VALUES ('sae', '   ', '   ', '  ', 'I', 'L', 'Sabanê', false, '');
INSERT INTO languages_iso639 VALUES ('saf', '   ', '   ', '  ', 'I', 'L', 'Safaliba', false, '');
INSERT INTO languages_iso639 VALUES ('sag', 'sag', 'sag', 'sg', 'I', 'L', 'Sango', false, '');
INSERT INTO languages_iso639 VALUES ('sah', 'sah', 'sah', '  ', 'I', 'L', 'Yakut', false, '');
INSERT INTO languages_iso639 VALUES ('saj', '   ', '   ', '  ', 'I', 'L', 'Sahu', false, '');
INSERT INTO languages_iso639 VALUES ('sak', '   ', '   ', '  ', 'I', 'L', 'Sake', false, '');
INSERT INTO languages_iso639 VALUES ('sam', 'sam', 'sam', '  ', 'I', 'E', 'Samaritan Aramaic', false, '');
INSERT INTO languages_iso639 VALUES ('san', 'san', 'san', 'sa', 'I', 'A', 'Sanskrit', false, '');
INSERT INTO languages_iso639 VALUES ('sao', '   ', '   ', '  ', 'I', 'L', 'Sause', false, '');
INSERT INTO languages_iso639 VALUES ('sap', '   ', '   ', '  ', 'I', 'L', 'Sanapaná', false, '');
INSERT INTO languages_iso639 VALUES ('saq', '   ', '   ', '  ', 'I', 'L', 'Samburu', false, '');
INSERT INTO languages_iso639 VALUES ('sar', '   ', '   ', '  ', 'I', 'E', 'Saraveca', false, '');
INSERT INTO languages_iso639 VALUES ('sas', 'sas', 'sas', '  ', 'I', 'L', 'Sasak', false, '');
INSERT INTO languages_iso639 VALUES ('sat', 'sat', 'sat', '  ', 'I', 'L', 'Santali', false, '');
INSERT INTO languages_iso639 VALUES ('sau', '   ', '   ', '  ', 'I', 'L', 'Saleman', false, '');
INSERT INTO languages_iso639 VALUES ('sav', '   ', '   ', '  ', 'I', 'L', 'Saafi-Saafi', false, '');
INSERT INTO languages_iso639 VALUES ('saw', '   ', '   ', '  ', 'I', 'L', 'Sawi', false, '');
INSERT INTO languages_iso639 VALUES ('sax', '   ', '   ', '  ', 'I', 'L', 'Sa', false, '');
INSERT INTO languages_iso639 VALUES ('say', '   ', '   ', '  ', 'I', 'L', 'Saya', false, '');
INSERT INTO languages_iso639 VALUES ('saz', '   ', '   ', '  ', 'I', 'L', 'Saurashtra', false, '');
INSERT INTO languages_iso639 VALUES ('sba', '   ', '   ', '  ', 'I', 'L', 'Ngambay', false, '');
INSERT INTO languages_iso639 VALUES ('sbb', '   ', '   ', '  ', 'I', 'L', 'Simbo', false, '');
INSERT INTO languages_iso639 VALUES ('sbc', '   ', '   ', '  ', 'I', 'L', 'Kele (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('sbd', '   ', '   ', '  ', 'I', 'L', 'Southern Samo', false, '');
INSERT INTO languages_iso639 VALUES ('sbe', '   ', '   ', '  ', 'I', 'L', 'Saliba', false, '');
INSERT INTO languages_iso639 VALUES ('sbf', '   ', '   ', '  ', 'I', 'L', 'Shabo', false, '');
INSERT INTO languages_iso639 VALUES ('sbg', '   ', '   ', '  ', 'I', 'L', 'Seget', false, '');
INSERT INTO languages_iso639 VALUES ('sbh', '   ', '   ', '  ', 'I', 'L', 'Sori-Harengan', false, '');
INSERT INTO languages_iso639 VALUES ('sbi', '   ', '   ', '  ', 'I', 'L', 'Seti', false, '');
INSERT INTO languages_iso639 VALUES ('sbj', '   ', '   ', '  ', 'I', 'L', 'Surbakhal', false, '');
INSERT INTO languages_iso639 VALUES ('sbk', '   ', '   ', '  ', 'I', 'L', 'Safwa', false, '');
INSERT INTO languages_iso639 VALUES ('sbl', '   ', '   ', '  ', 'I', 'L', 'Botolan Sambal', false, '');
INSERT INTO languages_iso639 VALUES ('sbm', '   ', '   ', '  ', 'I', 'L', 'Sagala', false, '');
INSERT INTO languages_iso639 VALUES ('sbn', '   ', '   ', '  ', 'I', 'L', 'Sindhi Bhil', false, '');
INSERT INTO languages_iso639 VALUES ('sbo', '   ', '   ', '  ', 'I', 'L', 'Sabüm', false, '');
INSERT INTO languages_iso639 VALUES ('sbp', '   ', '   ', '  ', 'I', 'L', 'Sangu (Tanzania)', false, '');
INSERT INTO languages_iso639 VALUES ('sbq', '   ', '   ', '  ', 'I', 'L', 'Sileibi', false, '');
INSERT INTO languages_iso639 VALUES ('sbr', '   ', '   ', '  ', 'I', 'L', 'Sembakung Murut', false, '');
INSERT INTO languages_iso639 VALUES ('sbs', '   ', '   ', '  ', 'I', 'L', 'Subiya', false, '');
INSERT INTO languages_iso639 VALUES ('sbt', '   ', '   ', '  ', 'I', 'L', 'Kimki', false, '');
INSERT INTO languages_iso639 VALUES ('sbu', '   ', '   ', '  ', 'I', 'L', 'Stod Bhoti', false, '');
INSERT INTO languages_iso639 VALUES ('sbv', '   ', '   ', '  ', 'I', 'A', 'Sabine', false, '');
INSERT INTO languages_iso639 VALUES ('sbw', '   ', '   ', '  ', 'I', 'L', 'Simba', false, '');
INSERT INTO languages_iso639 VALUES ('sbx', '   ', '   ', '  ', 'I', 'L', 'Seberuang', false, '');
INSERT INTO languages_iso639 VALUES ('sby', '   ', '   ', '  ', 'I', 'L', 'Soli', false, '');
INSERT INTO languages_iso639 VALUES ('sbz', '   ', '   ', '  ', 'I', 'L', 'Sara Kaba', false, '');
INSERT INTO languages_iso639 VALUES ('scb', '   ', '   ', '  ', 'I', 'L', 'Chut', false, '');
INSERT INTO languages_iso639 VALUES ('sce', '   ', '   ', '  ', 'I', 'L', 'Dongxiang', false, '');
INSERT INTO languages_iso639 VALUES ('scf', '   ', '   ', '  ', 'I', 'L', 'San Miguel Creole French', false, '');
INSERT INTO languages_iso639 VALUES ('scg', '   ', '   ', '  ', 'I', 'L', 'Sanggau', false, '');
INSERT INTO languages_iso639 VALUES ('sch', '   ', '   ', '  ', 'I', 'L', 'Sakachep', false, '');
INSERT INTO languages_iso639 VALUES ('sci', '   ', '   ', '  ', 'I', 'L', 'Sri Lankan Creole Malay', false, '');
INSERT INTO languages_iso639 VALUES ('sck', '   ', '   ', '  ', 'I', 'L', 'Sadri', false, '');
INSERT INTO languages_iso639 VALUES ('scl', '   ', '   ', '  ', 'I', 'L', 'Shina', false, '');
INSERT INTO languages_iso639 VALUES ('scn', 'scn', 'scn', '  ', 'I', 'L', 'Sicilian', false, '');
INSERT INTO languages_iso639 VALUES ('sco', 'sco', 'sco', '  ', 'I', 'L', 'Scots', false, '');
INSERT INTO languages_iso639 VALUES ('scp', '   ', '   ', '  ', 'I', 'L', 'Helambu Sherpa', false, '');
INSERT INTO languages_iso639 VALUES ('scq', '   ', '   ', '  ', 'I', 'L', 'Sa''och', false, '');
INSERT INTO languages_iso639 VALUES ('scs', '   ', '   ', '  ', 'I', 'L', 'North Slavey', false, '');
INSERT INTO languages_iso639 VALUES ('scu', '   ', '   ', '  ', 'I', 'L', 'Shumcho', false, '');
INSERT INTO languages_iso639 VALUES ('scv', '   ', '   ', '  ', 'I', 'L', 'Sheni', false, '');
INSERT INTO languages_iso639 VALUES ('scw', '   ', '   ', '  ', 'I', 'L', 'Sha', false, '');
INSERT INTO languages_iso639 VALUES ('scx', '   ', '   ', '  ', 'I', 'A', 'Sicel', false, '');
INSERT INTO languages_iso639 VALUES ('sda', '   ', '   ', '  ', 'I', 'L', 'Toraja-Sa''dan', false, '');
INSERT INTO languages_iso639 VALUES ('sdb', '   ', '   ', '  ', 'I', 'L', 'Shabak', false, '');
INSERT INTO languages_iso639 VALUES ('sdc', '   ', '   ', '  ', 'I', 'L', 'Sassarese Sardinian', false, '');
INSERT INTO languages_iso639 VALUES ('sde', '   ', '   ', '  ', 'I', 'L', 'Surubu', false, '');
INSERT INTO languages_iso639 VALUES ('sdf', '   ', '   ', '  ', 'I', 'L', 'Sarli', false, '');
INSERT INTO languages_iso639 VALUES ('sdg', '   ', '   ', '  ', 'I', 'L', 'Savi', false, '');
INSERT INTO languages_iso639 VALUES ('sdh', '   ', '   ', '  ', 'I', 'L', 'Southern Kurdish', false, '');
INSERT INTO languages_iso639 VALUES ('sdj', '   ', '   ', '  ', 'I', 'L', 'Suundi', false, '');
INSERT INTO languages_iso639 VALUES ('sdk', '   ', '   ', '  ', 'I', 'L', 'Sos Kundi', false, '');
INSERT INTO languages_iso639 VALUES ('sdl', '   ', '   ', '  ', 'I', 'L', 'Saudi Arabian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sdm', '   ', '   ', '  ', 'I', 'L', 'Semandang', false, '');
INSERT INTO languages_iso639 VALUES ('sdn', '   ', '   ', '  ', 'I', 'L', 'Gallurese Sardinian', false, '');
INSERT INTO languages_iso639 VALUES ('sdo', '   ', '   ', '  ', 'I', 'L', 'Bukar-Sadung Bidayuh', false, '');
INSERT INTO languages_iso639 VALUES ('sdp', '   ', '   ', '  ', 'I', 'L', 'Sherdukpen', false, '');
INSERT INTO languages_iso639 VALUES ('sdr', '   ', '   ', '  ', 'I', 'L', 'Oraon Sadri', false, '');
INSERT INTO languages_iso639 VALUES ('sds', '   ', '   ', '  ', 'I', 'E', 'Sened', false, '');
INSERT INTO languages_iso639 VALUES ('sdt', '   ', '   ', '  ', 'I', 'E', 'Shuadit', false, '');
INSERT INTO languages_iso639 VALUES ('sdu', '   ', '   ', '  ', 'I', 'L', 'Sarudu', false, '');
INSERT INTO languages_iso639 VALUES ('sdx', '   ', '   ', '  ', 'I', 'L', 'Sibu Melanau', false, '');
INSERT INTO languages_iso639 VALUES ('sdz', '   ', '   ', '  ', 'I', 'L', 'Sallands', false, '');
INSERT INTO languages_iso639 VALUES ('sea', '   ', '   ', '  ', 'I', 'L', 'Semai', false, '');
INSERT INTO languages_iso639 VALUES ('seb', '   ', '   ', '  ', 'I', 'L', 'Shempire Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('sec', '   ', '   ', '  ', 'I', 'L', 'Sechelt', false, '');
INSERT INTO languages_iso639 VALUES ('sed', '   ', '   ', '  ', 'I', 'L', 'Sedang', false, '');
INSERT INTO languages_iso639 VALUES ('see', '   ', '   ', '  ', 'I', 'L', 'Seneca', false, '');
INSERT INTO languages_iso639 VALUES ('sef', '   ', '   ', '  ', 'I', 'L', 'Cebaara Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('seg', '   ', '   ', '  ', 'I', 'L', 'Segeju', false, '');
INSERT INTO languages_iso639 VALUES ('seh', '   ', '   ', '  ', 'I', 'L', 'Sena', false, '');
INSERT INTO languages_iso639 VALUES ('sei', '   ', '   ', '  ', 'I', 'L', 'Seri', false, '');
INSERT INTO languages_iso639 VALUES ('sej', '   ', '   ', '  ', 'I', 'L', 'Sene', false, '');
INSERT INTO languages_iso639 VALUES ('sek', '   ', '   ', '  ', 'I', 'L', 'Sekani', false, '');
INSERT INTO languages_iso639 VALUES ('sel', 'sel', 'sel', '  ', 'I', 'L', 'Selkup', false, '');
INSERT INTO languages_iso639 VALUES ('sen', '   ', '   ', '  ', 'I', 'L', 'Nanerigé Sénoufo', false, '');
INSERT INTO languages_iso639 VALUES ('seo', '   ', '   ', '  ', 'I', 'L', 'Suarmin', false, '');
INSERT INTO languages_iso639 VALUES ('sep', '   ', '   ', '  ', 'I', 'L', 'Sìcìté Sénoufo', false, '');
INSERT INTO languages_iso639 VALUES ('seq', '   ', '   ', '  ', 'I', 'L', 'Senara Sénoufo', false, '');
INSERT INTO languages_iso639 VALUES ('ser', '   ', '   ', '  ', 'I', 'L', 'Serrano', false, '');
INSERT INTO languages_iso639 VALUES ('ses', '   ', '   ', '  ', 'I', 'L', 'Koyraboro Senni Songhai', false, '');
INSERT INTO languages_iso639 VALUES ('set', '   ', '   ', '  ', 'I', 'L', 'Sentani', false, '');
INSERT INTO languages_iso639 VALUES ('seu', '   ', '   ', '  ', 'I', 'L', 'Serui-Laut', false, '');
INSERT INTO languages_iso639 VALUES ('sev', '   ', '   ', '  ', 'I', 'L', 'Nyarafolo Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('sew', '   ', '   ', '  ', 'I', 'L', 'Sewa Bay', false, '');
INSERT INTO languages_iso639 VALUES ('sey', '   ', '   ', '  ', 'I', 'L', 'Secoya', false, '');
INSERT INTO languages_iso639 VALUES ('sez', '   ', '   ', '  ', 'I', 'L', 'Senthang Chin', false, '');
INSERT INTO languages_iso639 VALUES ('sfb', '   ', '   ', '  ', 'I', 'L', 'Langue des signes de Belgique Francophone', false, '');
INSERT INTO languages_iso639 VALUES ('sfe', '   ', '   ', '  ', 'I', 'L', 'Eastern Subanen', false, '');
INSERT INTO languages_iso639 VALUES ('sfm', '   ', '   ', '  ', 'I', 'L', 'Small Flowery Miao', false, '');
INSERT INTO languages_iso639 VALUES ('sfs', '   ', '   ', '  ', 'I', 'L', 'South African Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sfw', '   ', '   ', '  ', 'I', 'L', 'Sehwi', false, '');
INSERT INTO languages_iso639 VALUES ('sga', 'sga', 'sga', '  ', 'I', 'H', 'Old Irish (to 900)', false, '');
INSERT INTO languages_iso639 VALUES ('sgb', '   ', '   ', '  ', 'I', 'L', 'Mag-antsi Ayta', false, '');
INSERT INTO languages_iso639 VALUES ('sgc', '   ', '   ', '  ', 'I', 'L', 'Kipsigis', false, '');
INSERT INTO languages_iso639 VALUES ('sgd', '   ', '   ', '  ', 'I', 'L', 'Surigaonon', false, '');
INSERT INTO languages_iso639 VALUES ('sge', '   ', '   ', '  ', 'I', 'L', 'Segai', false, '');
INSERT INTO languages_iso639 VALUES ('sgg', '   ', '   ', '  ', 'I', 'L', 'Swiss-German Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sgh', '   ', '   ', '  ', 'I', 'L', 'Shughni', false, '');
INSERT INTO languages_iso639 VALUES ('sgi', '   ', '   ', '  ', 'I', 'L', 'Suga', false, '');
INSERT INTO languages_iso639 VALUES ('sgj', '   ', '   ', '  ', 'I', 'L', 'Surgujia', false, '');
INSERT INTO languages_iso639 VALUES ('sgk', '   ', '   ', '  ', 'I', 'L', 'Sangkong', false, '');
INSERT INTO languages_iso639 VALUES ('sgm', '   ', '   ', '  ', 'I', 'E', 'Singa', false, '');
INSERT INTO languages_iso639 VALUES ('sgo', '   ', '   ', '  ', 'I', 'L', 'Songa', false, '');
INSERT INTO languages_iso639 VALUES ('sgp', '   ', '   ', '  ', 'I', 'L', 'Singpho', false, '');
INSERT INTO languages_iso639 VALUES ('sgr', '   ', '   ', '  ', 'I', 'L', 'Sangisari', false, '');
INSERT INTO languages_iso639 VALUES ('sgs', '   ', '   ', '  ', 'I', 'L', 'Samogitian', false, '');
INSERT INTO languages_iso639 VALUES ('sgt', '   ', '   ', '  ', 'I', 'L', 'Brokpake', false, '');
INSERT INTO languages_iso639 VALUES ('sgu', '   ', '   ', '  ', 'I', 'L', 'Salas', false, '');
INSERT INTO languages_iso639 VALUES ('sgw', '   ', '   ', '  ', 'I', 'L', 'Sebat Bet Gurage', false, '');
INSERT INTO languages_iso639 VALUES ('sgx', '   ', '   ', '  ', 'I', 'L', 'Sierra Leone Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sgy', '   ', '   ', '  ', 'I', 'L', 'Sanglechi', false, '');
INSERT INTO languages_iso639 VALUES ('sgz', '   ', '   ', '  ', 'I', 'L', 'Sursurunga', false, '');
INSERT INTO languages_iso639 VALUES ('sha', '   ', '   ', '  ', 'I', 'L', 'Shall-Zwall', false, '');
INSERT INTO languages_iso639 VALUES ('shb', '   ', '   ', '  ', 'I', 'L', 'Ninam', false, '');
INSERT INTO languages_iso639 VALUES ('shc', '   ', '   ', '  ', 'I', 'L', 'Sonde', false, '');
INSERT INTO languages_iso639 VALUES ('shd', '   ', '   ', '  ', 'I', 'L', 'Kundal Shahi', false, '');
INSERT INTO languages_iso639 VALUES ('she', '   ', '   ', '  ', 'I', 'L', 'Sheko', false, '');
INSERT INTO languages_iso639 VALUES ('shg', '   ', '   ', '  ', 'I', 'L', 'Shua', false, '');
INSERT INTO languages_iso639 VALUES ('shh', '   ', '   ', '  ', 'I', 'L', 'Shoshoni', false, '');
INSERT INTO languages_iso639 VALUES ('shi', '   ', '   ', '  ', 'I', 'L', 'Tachelhit', false, '');
INSERT INTO languages_iso639 VALUES ('shj', '   ', '   ', '  ', 'I', 'L', 'Shatt', false, '');
INSERT INTO languages_iso639 VALUES ('shk', '   ', '   ', '  ', 'I', 'L', 'Shilluk', false, '');
INSERT INTO languages_iso639 VALUES ('shl', '   ', '   ', '  ', 'I', 'L', 'Shendu', false, '');
INSERT INTO languages_iso639 VALUES ('shm', '   ', '   ', '  ', 'I', 'L', 'Shahrudi', false, '');
INSERT INTO languages_iso639 VALUES ('shn', 'shn', 'shn', '  ', 'I', 'L', 'Shan', false, '');
INSERT INTO languages_iso639 VALUES ('sho', '   ', '   ', '  ', 'I', 'L', 'Shanga', false, '');
INSERT INTO languages_iso639 VALUES ('shp', '   ', '   ', '  ', 'I', 'L', 'Shipibo-Conibo', false, '');
INSERT INTO languages_iso639 VALUES ('shq', '   ', '   ', '  ', 'I', 'L', 'Sala', false, '');
INSERT INTO languages_iso639 VALUES ('shr', '   ', '   ', '  ', 'I', 'L', 'Shi', false, '');
INSERT INTO languages_iso639 VALUES ('shs', '   ', '   ', '  ', 'I', 'L', 'Shuswap', false, '');
INSERT INTO languages_iso639 VALUES ('sht', '   ', '   ', '  ', 'I', 'E', 'Shasta', false, '');
INSERT INTO languages_iso639 VALUES ('shu', '   ', '   ', '  ', 'I', 'L', 'Chadian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('shv', '   ', '   ', '  ', 'I', 'L', 'Shehri', false, '');
INSERT INTO languages_iso639 VALUES ('shw', '   ', '   ', '  ', 'I', 'L', 'Shwai', false, '');
INSERT INTO languages_iso639 VALUES ('shx', '   ', '   ', '  ', 'I', 'L', 'She', false, '');
INSERT INTO languages_iso639 VALUES ('shy', '   ', '   ', '  ', 'I', 'L', 'Tachawit', false, '');
INSERT INTO languages_iso639 VALUES ('shz', '   ', '   ', '  ', 'I', 'L', 'Syenara Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('sia', '   ', '   ', '  ', 'I', 'E', 'Akkala Sami', false, '');
INSERT INTO languages_iso639 VALUES ('sib', '   ', '   ', '  ', 'I', 'L', 'Sebop', false, '');
INSERT INTO languages_iso639 VALUES ('sid', 'sid', 'sid', '  ', 'I', 'L', 'Sidamo', false, '');
INSERT INTO languages_iso639 VALUES ('sie', '   ', '   ', '  ', 'I', 'L', 'Simaa', false, '');
INSERT INTO languages_iso639 VALUES ('sif', '   ', '   ', '  ', 'I', 'L', 'Siamou', false, '');
INSERT INTO languages_iso639 VALUES ('sig', '   ', '   ', '  ', 'I', 'L', 'Paasaal', false, '');
INSERT INTO languages_iso639 VALUES ('sih', '   ', '   ', '  ', 'I', 'L', 'Zire', false, '');
INSERT INTO languages_iso639 VALUES ('sii', '   ', '   ', '  ', 'I', 'L', 'Shom Peng', false, '');
INSERT INTO languages_iso639 VALUES ('sij', '   ', '   ', '  ', 'I', 'L', 'Numbami', false, '');
INSERT INTO languages_iso639 VALUES ('sik', '   ', '   ', '  ', 'I', 'L', 'Sikiana', false, '');
INSERT INTO languages_iso639 VALUES ('sil', '   ', '   ', '  ', 'I', 'L', 'Tumulung Sisaala', false, '');
INSERT INTO languages_iso639 VALUES ('sim', '   ', '   ', '  ', 'I', 'L', 'Mende (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('sin', 'sin', 'sin', 'si', 'I', 'L', 'Sinhala', false, '');
INSERT INTO languages_iso639 VALUES ('sip', '   ', '   ', '  ', 'I', 'L', 'Sikkimese', false, '');
INSERT INTO languages_iso639 VALUES ('siq', '   ', '   ', '  ', 'I', 'L', 'Sonia', false, '');
INSERT INTO languages_iso639 VALUES ('sir', '   ', '   ', '  ', 'I', 'L', 'Siri', false, '');
INSERT INTO languages_iso639 VALUES ('sis', '   ', '   ', '  ', 'I', 'E', 'Siuslaw', false, '');
INSERT INTO languages_iso639 VALUES ('siu', '   ', '   ', '  ', 'I', 'L', 'Sinagen', false, '');
INSERT INTO languages_iso639 VALUES ('siv', '   ', '   ', '  ', 'I', 'L', 'Sumariup', false, '');
INSERT INTO languages_iso639 VALUES ('siw', '   ', '   ', '  ', 'I', 'L', 'Siwai', false, '');
INSERT INTO languages_iso639 VALUES ('six', '   ', '   ', '  ', 'I', 'L', 'Sumau', false, '');
INSERT INTO languages_iso639 VALUES ('siy', '   ', '   ', '  ', 'I', 'L', 'Sivandi', false, '');
INSERT INTO languages_iso639 VALUES ('siz', '   ', '   ', '  ', 'I', 'L', 'Siwi', false, '');
INSERT INTO languages_iso639 VALUES ('sja', '   ', '   ', '  ', 'I', 'L', 'Epena', false, '');
INSERT INTO languages_iso639 VALUES ('sjb', '   ', '   ', '  ', 'I', 'L', 'Sajau Basap', false, '');
INSERT INTO languages_iso639 VALUES ('sjd', '   ', '   ', '  ', 'I', 'L', 'Kildin Sami', false, '');
INSERT INTO languages_iso639 VALUES ('sje', '   ', '   ', '  ', 'I', 'L', 'Pite Sami', false, '');
INSERT INTO languages_iso639 VALUES ('sjg', '   ', '   ', '  ', 'I', 'L', 'Assangori', false, '');
INSERT INTO languages_iso639 VALUES ('sjk', '   ', '   ', '  ', 'I', 'E', 'Kemi Sami', false, '');
INSERT INTO languages_iso639 VALUES ('sjl', '   ', '   ', '  ', 'I', 'L', 'Sajalong', false, '');
INSERT INTO languages_iso639 VALUES ('sjm', '   ', '   ', '  ', 'I', 'L', 'Mapun', false, '');
INSERT INTO languages_iso639 VALUES ('sjn', '   ', '   ', '  ', 'I', 'C', 'Sindarin', false, '');
INSERT INTO languages_iso639 VALUES ('sjo', '   ', '   ', '  ', 'I', 'L', 'Xibe', false, '');
INSERT INTO languages_iso639 VALUES ('sjp', '   ', '   ', '  ', 'I', 'L', 'Surjapuri', false, '');
INSERT INTO languages_iso639 VALUES ('sjr', '   ', '   ', '  ', 'I', 'L', 'Siar-Lak', false, '');
INSERT INTO languages_iso639 VALUES ('sjs', '   ', '   ', '  ', 'I', 'E', 'Senhaja De Srair', false, '');
INSERT INTO languages_iso639 VALUES ('sjt', '   ', '   ', '  ', 'I', 'L', 'Ter Sami', false, '');
INSERT INTO languages_iso639 VALUES ('sju', '   ', '   ', '  ', 'I', 'L', 'Ume Sami', false, '');
INSERT INTO languages_iso639 VALUES ('sjw', '   ', '   ', '  ', 'I', 'L', 'Shawnee', false, '');
INSERT INTO languages_iso639 VALUES ('ska', '   ', '   ', '  ', 'I', 'L', 'Skagit', false, '');
INSERT INTO languages_iso639 VALUES ('skb', '   ', '   ', '  ', 'I', 'L', 'Saek', false, '');
INSERT INTO languages_iso639 VALUES ('skc', '   ', '   ', '  ', 'I', 'L', 'Ma Manda', false, '');
INSERT INTO languages_iso639 VALUES ('skd', '   ', '   ', '  ', 'I', 'L', 'Southern Sierra Miwok', false, '');
INSERT INTO languages_iso639 VALUES ('ske', '   ', '   ', '  ', 'I', 'L', 'Seke (Vanuatu)', false, '');
INSERT INTO languages_iso639 VALUES ('skf', '   ', '   ', '  ', 'I', 'L', 'Sakirabiá', false, '');
INSERT INTO languages_iso639 VALUES ('skg', '   ', '   ', '  ', 'I', 'L', 'Sakalava Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('skh', '   ', '   ', '  ', 'I', 'L', 'Sikule', false, '');
INSERT INTO languages_iso639 VALUES ('ski', '   ', '   ', '  ', 'I', 'L', 'Sika', false, '');
INSERT INTO languages_iso639 VALUES ('skj', '   ', '   ', '  ', 'I', 'L', 'Seke (Nepal)', false, '');
INSERT INTO languages_iso639 VALUES ('skk', '   ', '   ', '  ', 'I', 'L', 'Sok', false, '');
INSERT INTO languages_iso639 VALUES ('skm', '   ', '   ', '  ', 'I', 'L', 'Kutong', false, '');
INSERT INTO languages_iso639 VALUES ('skn', '   ', '   ', '  ', 'I', 'L', 'Kolibugan Subanon', false, '');
INSERT INTO languages_iso639 VALUES ('sko', '   ', '   ', '  ', 'I', 'L', 'Seko Tengah', false, '');
INSERT INTO languages_iso639 VALUES ('skp', '   ', '   ', '  ', 'I', 'L', 'Sekapan', false, '');
INSERT INTO languages_iso639 VALUES ('skq', '   ', '   ', '  ', 'I', 'L', 'Sininkere', false, '');
INSERT INTO languages_iso639 VALUES ('skr', '   ', '   ', '  ', 'I', 'L', 'Seraiki', false, '');
INSERT INTO languages_iso639 VALUES ('sks', '   ', '   ', '  ', 'I', 'L', 'Maia', false, '');
INSERT INTO languages_iso639 VALUES ('skt', '   ', '   ', '  ', 'I', 'L', 'Sakata', false, '');
INSERT INTO languages_iso639 VALUES ('sku', '   ', '   ', '  ', 'I', 'L', 'Sakao', false, '');
INSERT INTO languages_iso639 VALUES ('skv', '   ', '   ', '  ', 'I', 'L', 'Skou', false, '');
INSERT INTO languages_iso639 VALUES ('skw', '   ', '   ', '  ', 'I', 'E', 'Skepi Creole Dutch', false, '');
INSERT INTO languages_iso639 VALUES ('skx', '   ', '   ', '  ', 'I', 'L', 'Seko Padang', false, '');
INSERT INTO languages_iso639 VALUES ('sky', '   ', '   ', '  ', 'I', 'L', 'Sikaiana', false, '');
INSERT INTO languages_iso639 VALUES ('skz', '   ', '   ', '  ', 'I', 'L', 'Sekar', false, '');
INSERT INTO languages_iso639 VALUES ('slc', '   ', '   ', '  ', 'I', 'L', 'Sáliba', false, '');
INSERT INTO languages_iso639 VALUES ('sld', '   ', '   ', '  ', 'I', 'L', 'Sissala', false, '');
INSERT INTO languages_iso639 VALUES ('sle', '   ', '   ', '  ', 'I', 'L', 'Sholaga', false, '');
INSERT INTO languages_iso639 VALUES ('slf', '   ', '   ', '  ', 'I', 'L', 'Swiss-Italian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('slg', '   ', '   ', '  ', 'I', 'L', 'Selungai Murut', false, '');
INSERT INTO languages_iso639 VALUES ('slh', '   ', '   ', '  ', 'I', 'L', 'Southern Puget Sound Salish', false, '');
INSERT INTO languages_iso639 VALUES ('sli', '   ', '   ', '  ', 'I', 'L', 'Lower Silesian', false, '');
INSERT INTO languages_iso639 VALUES ('slj', '   ', '   ', '  ', 'I', 'L', 'Salumá', false, '');
INSERT INTO languages_iso639 VALUES ('slk', 'slo', 'slk', 'sk', 'I', 'L', 'Slovak', false, '');
INSERT INTO languages_iso639 VALUES ('sll', '   ', '   ', '  ', 'I', 'L', 'Salt-Yui', false, '');
INSERT INTO languages_iso639 VALUES ('slm', '   ', '   ', '  ', 'I', 'L', 'Pangutaran Sama', false, '');
INSERT INTO languages_iso639 VALUES ('sln', '   ', '   ', '  ', 'I', 'E', 'Salinan', false, '');
INSERT INTO languages_iso639 VALUES ('slp', '   ', '   ', '  ', 'I', 'L', 'Lamaholot', false, '');
INSERT INTO languages_iso639 VALUES ('slq', '   ', '   ', '  ', 'I', 'L', 'Salchuq', false, '');
INSERT INTO languages_iso639 VALUES ('slr', '   ', '   ', '  ', 'I', 'L', 'Salar', false, '');
INSERT INTO languages_iso639 VALUES ('sls', '   ', '   ', '  ', 'I', 'L', 'Singapore Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('slt', '   ', '   ', '  ', 'I', 'L', 'Sila', false, '');
INSERT INTO languages_iso639 VALUES ('slu', '   ', '   ', '  ', 'I', 'L', 'Selaru', false, '');
INSERT INTO languages_iso639 VALUES ('slv', 'slv', 'slv', 'sl', 'I', 'L', 'Slovenian', false, '');
INSERT INTO languages_iso639 VALUES ('slw', '   ', '   ', '  ', 'I', 'L', 'Sialum', false, '');
INSERT INTO languages_iso639 VALUES ('slx', '   ', '   ', '  ', 'I', 'L', 'Salampasu', false, '');
INSERT INTO languages_iso639 VALUES ('sly', '   ', '   ', '  ', 'I', 'L', 'Selayar', false, '');
INSERT INTO languages_iso639 VALUES ('slz', '   ', '   ', '  ', 'I', 'L', 'Ma''ya', false, '');
INSERT INTO languages_iso639 VALUES ('sma', 'sma', 'sma', '  ', 'I', 'L', 'Southern Sami', false, '');
INSERT INTO languages_iso639 VALUES ('smb', '   ', '   ', '  ', 'I', 'L', 'Simbari', false, '');
INSERT INTO languages_iso639 VALUES ('smc', '   ', '   ', '  ', 'I', 'E', 'Som', false, '');
INSERT INTO languages_iso639 VALUES ('smd', '   ', '   ', '  ', 'I', 'L', 'Sama', false, '');
INSERT INTO languages_iso639 VALUES ('sme', 'sme', 'sme', 'se', 'I', 'L', 'Northern Sami', false, '');
INSERT INTO languages_iso639 VALUES ('smf', '   ', '   ', '  ', 'I', 'L', 'Auwe', false, '');
INSERT INTO languages_iso639 VALUES ('smg', '   ', '   ', '  ', 'I', 'L', 'Simbali', false, '');
INSERT INTO languages_iso639 VALUES ('smh', '   ', '   ', '  ', 'I', 'L', 'Samei', false, '');
INSERT INTO languages_iso639 VALUES ('smj', 'smj', 'smj', '  ', 'I', 'L', 'Lule Sami', false, '');
INSERT INTO languages_iso639 VALUES ('smk', '   ', '   ', '  ', 'I', 'L', 'Bolinao', false, '');
INSERT INTO languages_iso639 VALUES ('sml', '   ', '   ', '  ', 'I', 'L', 'Central Sama', false, '');
INSERT INTO languages_iso639 VALUES ('smm', '   ', '   ', '  ', 'I', 'L', 'Musasa', false, '');
INSERT INTO languages_iso639 VALUES ('smn', 'smn', 'smn', '  ', 'I', 'L', 'Inari Sami', false, '');
INSERT INTO languages_iso639 VALUES ('smo', 'smo', 'smo', 'sm', 'I', 'L', 'Samoan', false, '');
INSERT INTO languages_iso639 VALUES ('smp', '   ', '   ', '  ', 'I', 'E', 'Samaritan', false, '');
INSERT INTO languages_iso639 VALUES ('smq', '   ', '   ', '  ', 'I', 'L', 'Samo', false, '');
INSERT INTO languages_iso639 VALUES ('smr', '   ', '   ', '  ', 'I', 'L', 'Simeulue', false, '');
INSERT INTO languages_iso639 VALUES ('sms', 'sms', 'sms', '  ', 'I', 'L', 'Skolt Sami', false, '');
INSERT INTO languages_iso639 VALUES ('smt', '   ', '   ', '  ', 'I', 'L', 'Simte', false, '');
INSERT INTO languages_iso639 VALUES ('smu', '   ', '   ', '  ', 'I', 'E', 'Somray', false, '');
INSERT INTO languages_iso639 VALUES ('smv', '   ', '   ', '  ', 'I', 'L', 'Samvedi', false, '');
INSERT INTO languages_iso639 VALUES ('smw', '   ', '   ', '  ', 'I', 'L', 'Sumbawa', false, '');
INSERT INTO languages_iso639 VALUES ('smx', '   ', '   ', '  ', 'I', 'L', 'Samba', false, '');
INSERT INTO languages_iso639 VALUES ('smy', '   ', '   ', '  ', 'I', 'L', 'Semnani', false, '');
INSERT INTO languages_iso639 VALUES ('smz', '   ', '   ', '  ', 'I', 'L', 'Simeku', false, '');
INSERT INTO languages_iso639 VALUES ('sna', 'sna', 'sna', 'sn', 'I', 'L', 'Shona', false, '');
INSERT INTO languages_iso639 VALUES ('snb', '   ', '   ', '  ', 'I', 'L', 'Sebuyau', false, '');
INSERT INTO languages_iso639 VALUES ('snc', '   ', '   ', '  ', 'I', 'L', 'Sinaugoro', false, '');
INSERT INTO languages_iso639 VALUES ('snd', 'snd', 'snd', 'sd', 'I', 'L', 'Sindhi', false, '');
INSERT INTO languages_iso639 VALUES ('sne', '   ', '   ', '  ', 'I', 'L', 'Bau Bidayuh', false, '');
INSERT INTO languages_iso639 VALUES ('snf', '   ', '   ', '  ', 'I', 'L', 'Noon', false, '');
INSERT INTO languages_iso639 VALUES ('sng', '   ', '   ', '  ', 'I', 'L', 'Sanga (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('snh', '   ', '   ', '  ', 'I', 'E', 'Shinabo', false, '');
INSERT INTO languages_iso639 VALUES ('sni', '   ', '   ', '  ', 'I', 'E', 'Sensi', false, '');
INSERT INTO languages_iso639 VALUES ('snj', '   ', '   ', '  ', 'I', 'L', 'Riverain Sango', false, '');
INSERT INTO languages_iso639 VALUES ('snk', 'snk', 'snk', '  ', 'I', 'L', 'Soninke', false, '');
INSERT INTO languages_iso639 VALUES ('snl', '   ', '   ', '  ', 'I', 'L', 'Sangil', false, '');
INSERT INTO languages_iso639 VALUES ('snm', '   ', '   ', '  ', 'I', 'L', 'Southern Ma''di', false, '');
INSERT INTO languages_iso639 VALUES ('snn', '   ', '   ', '  ', 'I', 'L', 'Siona', false, '');
INSERT INTO languages_iso639 VALUES ('sno', '   ', '   ', '  ', 'I', 'L', 'Snohomish', false, '');
INSERT INTO languages_iso639 VALUES ('snp', '   ', '   ', '  ', 'I', 'L', 'Siane', false, '');
INSERT INTO languages_iso639 VALUES ('snq', '   ', '   ', '  ', 'I', 'L', 'Sangu (Gabon)', false, '');
INSERT INTO languages_iso639 VALUES ('snr', '   ', '   ', '  ', 'I', 'L', 'Sihan', false, '');
INSERT INTO languages_iso639 VALUES ('sns', '   ', '   ', '  ', 'I', 'L', 'South West Bay', false, '');
INSERT INTO languages_iso639 VALUES ('snu', '   ', '   ', '  ', 'I', 'L', 'Senggi', false, '');
INSERT INTO languages_iso639 VALUES ('snv', '   ', '   ', '  ', 'I', 'L', 'Sa''ban', false, '');
INSERT INTO languages_iso639 VALUES ('snw', '   ', '   ', '  ', 'I', 'L', 'Selee', false, '');
INSERT INTO languages_iso639 VALUES ('snx', '   ', '   ', '  ', 'I', 'L', 'Sam', false, '');
INSERT INTO languages_iso639 VALUES ('sny', '   ', '   ', '  ', 'I', 'L', 'Saniyo-Hiyewe', false, '');
INSERT INTO languages_iso639 VALUES ('snz', '   ', '   ', '  ', 'I', 'L', 'Sinsauru', false, '');
INSERT INTO languages_iso639 VALUES ('soa', '   ', '   ', '  ', 'I', 'L', 'Thai Song', false, '');
INSERT INTO languages_iso639 VALUES ('sob', '   ', '   ', '  ', 'I', 'L', 'Sobei', false, '');
INSERT INTO languages_iso639 VALUES ('soc', '   ', '   ', '  ', 'I', 'L', 'So (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('sod', '   ', '   ', '  ', 'I', 'L', 'Songoora', false, '');
INSERT INTO languages_iso639 VALUES ('soe', '   ', '   ', '  ', 'I', 'L', 'Songomeno', false, '');
INSERT INTO languages_iso639 VALUES ('sog', 'sog', 'sog', '  ', 'I', 'A', 'Sogdian', false, '');
INSERT INTO languages_iso639 VALUES ('soh', '   ', '   ', '  ', 'I', 'L', 'Aka', false, '');
INSERT INTO languages_iso639 VALUES ('soi', '   ', '   ', '  ', 'I', 'L', 'Sonha', false, '');
INSERT INTO languages_iso639 VALUES ('soj', '   ', '   ', '  ', 'I', 'L', 'Soi', false, '');
INSERT INTO languages_iso639 VALUES ('sok', '   ', '   ', '  ', 'I', 'L', 'Sokoro', false, '');
INSERT INTO languages_iso639 VALUES ('sol', '   ', '   ', '  ', 'I', 'L', 'Solos', false, '');
INSERT INTO languages_iso639 VALUES ('som', 'som', 'som', 'so', 'I', 'L', 'Somali', false, '');
INSERT INTO languages_iso639 VALUES ('soo', '   ', '   ', '  ', 'I', 'L', 'Songo', false, '');
INSERT INTO languages_iso639 VALUES ('sop', '   ', '   ', '  ', 'I', 'L', 'Songe', false, '');
INSERT INTO languages_iso639 VALUES ('soq', '   ', '   ', '  ', 'I', 'L', 'Kanasi', false, '');
INSERT INTO languages_iso639 VALUES ('sor', '   ', '   ', '  ', 'I', 'L', 'Somrai', false, '');
INSERT INTO languages_iso639 VALUES ('sos', '   ', '   ', '  ', 'I', 'L', 'Seeku', false, '');
INSERT INTO languages_iso639 VALUES ('sot', 'sot', 'sot', 'st', 'I', 'L', 'Southern Sotho', false, '');
INSERT INTO languages_iso639 VALUES ('sou', '   ', '   ', '  ', 'I', 'L', 'Southern Thai', false, '');
INSERT INTO languages_iso639 VALUES ('sov', '   ', '   ', '  ', 'I', 'L', 'Sonsorol', false, '');
INSERT INTO languages_iso639 VALUES ('sow', '   ', '   ', '  ', 'I', 'L', 'Sowanda', false, '');
INSERT INTO languages_iso639 VALUES ('sox', '   ', '   ', '  ', 'I', 'L', 'Swo', false, '');
INSERT INTO languages_iso639 VALUES ('soy', '   ', '   ', '  ', 'I', 'L', 'Miyobe', false, '');
INSERT INTO languages_iso639 VALUES ('soz', '   ', '   ', '  ', 'I', 'L', 'Temi', false, '');
INSERT INTO languages_iso639 VALUES ('spa', 'spa', 'spa', 'es', 'I', 'L', 'Spanish', false, '');
INSERT INTO languages_iso639 VALUES ('spb', '   ', '   ', '  ', 'I', 'L', 'Sepa (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('spc', '   ', '   ', '  ', 'I', 'L', 'Sapé', false, '');
INSERT INTO languages_iso639 VALUES ('spd', '   ', '   ', '  ', 'I', 'L', 'Saep', false, '');
INSERT INTO languages_iso639 VALUES ('spe', '   ', '   ', '  ', 'I', 'L', 'Sepa (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('spg', '   ', '   ', '  ', 'I', 'L', 'Sian', false, '');
INSERT INTO languages_iso639 VALUES ('spi', '   ', '   ', '  ', 'I', 'L', 'Saponi', false, '');
INSERT INTO languages_iso639 VALUES ('spk', '   ', '   ', '  ', 'I', 'L', 'Sengo', false, '');
INSERT INTO languages_iso639 VALUES ('spl', '   ', '   ', '  ', 'I', 'L', 'Selepet', false, '');
INSERT INTO languages_iso639 VALUES ('spm', '   ', '   ', '  ', 'I', 'L', 'Akukem', false, '');
INSERT INTO languages_iso639 VALUES ('spo', '   ', '   ', '  ', 'I', 'L', 'Spokane', false, '');
INSERT INTO languages_iso639 VALUES ('spp', '   ', '   ', '  ', 'I', 'L', 'Supyire Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('spq', '   ', '   ', '  ', 'I', 'L', 'Loreto-Ucayali Spanish', false, '');
INSERT INTO languages_iso639 VALUES ('spr', '   ', '   ', '  ', 'I', 'L', 'Saparua', false, '');
INSERT INTO languages_iso639 VALUES ('sps', '   ', '   ', '  ', 'I', 'L', 'Saposa', false, '');
INSERT INTO languages_iso639 VALUES ('spt', '   ', '   ', '  ', 'I', 'L', 'Spiti Bhoti', false, '');
INSERT INTO languages_iso639 VALUES ('spu', '   ', '   ', '  ', 'I', 'L', 'Sapuan', false, '');
INSERT INTO languages_iso639 VALUES ('spv', '   ', '   ', '  ', 'I', 'L', 'Sambalpuri', false, '');
INSERT INTO languages_iso639 VALUES ('spx', '   ', '   ', '  ', 'I', 'A', 'South Picene', false, '');
INSERT INTO languages_iso639 VALUES ('spy', '   ', '   ', '  ', 'I', 'L', 'Sabaot', false, '');
INSERT INTO languages_iso639 VALUES ('sqa', '   ', '   ', '  ', 'I', 'L', 'Shama-Sambuga', false, '');
INSERT INTO languages_iso639 VALUES ('sqh', '   ', '   ', '  ', 'I', 'L', 'Shau', false, '');
INSERT INTO languages_iso639 VALUES ('sqi', 'alb', 'sqi', 'sq', 'M', 'L', 'Albanian', false, '');
INSERT INTO languages_iso639 VALUES ('sqk', '   ', '   ', '  ', 'I', 'L', 'Albanian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sqm', '   ', '   ', '  ', 'I', 'L', 'Suma', false, '');
INSERT INTO languages_iso639 VALUES ('sqn', '   ', '   ', '  ', 'I', 'E', 'Susquehannock', false, '');
INSERT INTO languages_iso639 VALUES ('sqo', '   ', '   ', '  ', 'I', 'L', 'Sorkhei', false, '');
INSERT INTO languages_iso639 VALUES ('sqq', '   ', '   ', '  ', 'I', 'L', 'Sou', false, '');
INSERT INTO languages_iso639 VALUES ('sqr', '   ', '   ', '  ', 'I', 'H', 'Siculo Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('sqs', '   ', '   ', '  ', 'I', 'L', 'Sri Lankan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sqt', '   ', '   ', '  ', 'I', 'L', 'Soqotri', false, '');
INSERT INTO languages_iso639 VALUES ('squ', '   ', '   ', '  ', 'I', 'L', 'Squamish', false, '');
INSERT INTO languages_iso639 VALUES ('sra', '   ', '   ', '  ', 'I', 'L', 'Saruga', false, '');
INSERT INTO languages_iso639 VALUES ('srb', '   ', '   ', '  ', 'I', 'L', 'Sora', false, '');
INSERT INTO languages_iso639 VALUES ('src', '   ', '   ', '  ', 'I', 'L', 'Logudorese Sardinian', false, '');
INSERT INTO languages_iso639 VALUES ('srd', 'srd', 'srd', 'sc', 'M', 'L', 'Sardinian', false, '');
INSERT INTO languages_iso639 VALUES ('sre', '   ', '   ', '  ', 'I', 'L', 'Sara', false, '');
INSERT INTO languages_iso639 VALUES ('srf', '   ', '   ', '  ', 'I', 'L', 'Nafi', false, '');
INSERT INTO languages_iso639 VALUES ('srg', '   ', '   ', '  ', 'I', 'L', 'Sulod', false, '');
INSERT INTO languages_iso639 VALUES ('srh', '   ', '   ', '  ', 'I', 'L', 'Sarikoli', false, '');
INSERT INTO languages_iso639 VALUES ('sri', '   ', '   ', '  ', 'I', 'L', 'Siriano', false, '');
INSERT INTO languages_iso639 VALUES ('srk', '   ', '   ', '  ', 'I', 'L', 'Serudung Murut', false, '');
INSERT INTO languages_iso639 VALUES ('srl', '   ', '   ', '  ', 'I', 'L', 'Isirawa', false, '');
INSERT INTO languages_iso639 VALUES ('srm', '   ', '   ', '  ', 'I', 'L', 'Saramaccan', false, '');
INSERT INTO languages_iso639 VALUES ('srn', 'srn', 'srn', '  ', 'I', 'L', 'Sranan Tongo', false, '');
INSERT INTO languages_iso639 VALUES ('sro', '   ', '   ', '  ', 'I', 'L', 'Campidanese Sardinian', false, '');
INSERT INTO languages_iso639 VALUES ('srp', 'srp', 'srp', 'sr', 'I', 'L', 'Serbian', false, '');
INSERT INTO languages_iso639 VALUES ('srq', '   ', '   ', '  ', 'I', 'L', 'Sirionó', false, '');
INSERT INTO languages_iso639 VALUES ('srr', 'srr', 'srr', '  ', 'I', 'L', 'Serer', false, '');
INSERT INTO languages_iso639 VALUES ('srs', '   ', '   ', '  ', 'I', 'L', 'Sarsi', false, '');
INSERT INTO languages_iso639 VALUES ('srt', '   ', '   ', '  ', 'I', 'L', 'Sauri', false, '');
INSERT INTO languages_iso639 VALUES ('sru', '   ', '   ', '  ', 'I', 'L', 'Suruí', false, '');
INSERT INTO languages_iso639 VALUES ('srv', '   ', '   ', '  ', 'I', 'L', 'Southern Sorsoganon', false, '');
INSERT INTO languages_iso639 VALUES ('srw', '   ', '   ', '  ', 'I', 'L', 'Serua', false, '');
INSERT INTO languages_iso639 VALUES ('srx', '   ', '   ', '  ', 'I', 'L', 'Sirmauri', false, '');
INSERT INTO languages_iso639 VALUES ('sry', '   ', '   ', '  ', 'I', 'L', 'Sera', false, '');
INSERT INTO languages_iso639 VALUES ('srz', '   ', '   ', '  ', 'I', 'L', 'Shahmirzadi', false, '');
INSERT INTO languages_iso639 VALUES ('ssb', '   ', '   ', '  ', 'I', 'L', 'Southern Sama', false, '');
INSERT INTO languages_iso639 VALUES ('ssc', '   ', '   ', '  ', 'I', 'L', 'Suba-Simbiti', false, '');
INSERT INTO languages_iso639 VALUES ('ssd', '   ', '   ', '  ', 'I', 'L', 'Siroi', false, '');
INSERT INTO languages_iso639 VALUES ('sse', '   ', '   ', '  ', 'I', 'L', 'Balangingi', false, '');
INSERT INTO languages_iso639 VALUES ('ssf', '   ', '   ', '  ', 'I', 'L', 'Thao', false, '');
INSERT INTO languages_iso639 VALUES ('ssg', '   ', '   ', '  ', 'I', 'L', 'Seimat', false, '');
INSERT INTO languages_iso639 VALUES ('ssh', '   ', '   ', '  ', 'I', 'L', 'Shihhi Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('ssi', '   ', '   ', '  ', 'I', 'L', 'Sansi', false, '');
INSERT INTO languages_iso639 VALUES ('ssj', '   ', '   ', '  ', 'I', 'L', 'Sausi', false, '');
INSERT INTO languages_iso639 VALUES ('ssk', '   ', '   ', '  ', 'I', 'L', 'Sunam', false, '');
INSERT INTO languages_iso639 VALUES ('ssl', '   ', '   ', '  ', 'I', 'L', 'Western Sisaala', false, '');
INSERT INTO languages_iso639 VALUES ('ssm', '   ', '   ', '  ', 'I', 'L', 'Semnam', false, '');
INSERT INTO languages_iso639 VALUES ('ssn', '   ', '   ', '  ', 'I', 'L', 'Waata', false, '');
INSERT INTO languages_iso639 VALUES ('sso', '   ', '   ', '  ', 'I', 'L', 'Sissano', false, '');
INSERT INTO languages_iso639 VALUES ('ssp', '   ', '   ', '  ', 'I', 'L', 'Spanish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ssq', '   ', '   ', '  ', 'I', 'L', 'So''a', false, '');
INSERT INTO languages_iso639 VALUES ('ssr', '   ', '   ', '  ', 'I', 'L', 'Swiss-French Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sss', '   ', '   ', '  ', 'I', 'L', 'Sô', false, '');
INSERT INTO languages_iso639 VALUES ('sst', '   ', '   ', '  ', 'I', 'L', 'Sinasina', false, '');
INSERT INTO languages_iso639 VALUES ('ssu', '   ', '   ', '  ', 'I', 'L', 'Susuami', false, '');
INSERT INTO languages_iso639 VALUES ('ssv', '   ', '   ', '  ', 'I', 'L', 'Shark Bay', false, '');
INSERT INTO languages_iso639 VALUES ('ssw', 'ssw', 'ssw', 'ss', 'I', 'L', 'Swati', false, '');
INSERT INTO languages_iso639 VALUES ('ssx', '   ', '   ', '  ', 'I', 'L', 'Samberigi', false, '');
INSERT INTO languages_iso639 VALUES ('ssy', '   ', '   ', '  ', 'I', 'L', 'Saho', false, '');
INSERT INTO languages_iso639 VALUES ('ssz', '   ', '   ', '  ', 'I', 'L', 'Sengseng', false, '');
INSERT INTO languages_iso639 VALUES ('sta', '   ', '   ', '  ', 'I', 'L', 'Settla', false, '');
INSERT INTO languages_iso639 VALUES ('stb', '   ', '   ', '  ', 'I', 'L', 'Northern Subanen', false, '');
INSERT INTO languages_iso639 VALUES ('std', '   ', '   ', '  ', 'I', 'L', 'Sentinel', false, '');
INSERT INTO languages_iso639 VALUES ('ste', '   ', '   ', '  ', 'I', 'L', 'Liana-Seti', false, '');
INSERT INTO languages_iso639 VALUES ('stf', '   ', '   ', '  ', 'I', 'L', 'Seta', false, '');
INSERT INTO languages_iso639 VALUES ('stg', '   ', '   ', '  ', 'I', 'L', 'Trieng', false, '');
INSERT INTO languages_iso639 VALUES ('sth', '   ', '   ', '  ', 'I', 'L', 'Shelta', false, '');
INSERT INTO languages_iso639 VALUES ('sti', '   ', '   ', '  ', 'I', 'L', 'Bulo Stieng', false, '');
INSERT INTO languages_iso639 VALUES ('stj', '   ', '   ', '  ', 'I', 'L', 'Matya Samo', false, '');
INSERT INTO languages_iso639 VALUES ('stk', '   ', '   ', '  ', 'I', 'L', 'Arammba', false, '');
INSERT INTO languages_iso639 VALUES ('stl', '   ', '   ', '  ', 'I', 'L', 'Stellingwerfs', false, '');
INSERT INTO languages_iso639 VALUES ('stm', '   ', '   ', '  ', 'I', 'L', 'Setaman', false, '');
INSERT INTO languages_iso639 VALUES ('stn', '   ', '   ', '  ', 'I', 'L', 'Owa', false, '');
INSERT INTO languages_iso639 VALUES ('sto', '   ', '   ', '  ', 'I', 'L', 'Stoney', false, '');
INSERT INTO languages_iso639 VALUES ('stp', '   ', '   ', '  ', 'I', 'L', 'Southeastern Tepehuan', false, '');
INSERT INTO languages_iso639 VALUES ('stq', '   ', '   ', '  ', 'I', 'L', 'Saterfriesisch', false, '');
INSERT INTO languages_iso639 VALUES ('str', '   ', '   ', '  ', 'I', 'L', 'Straits Salish', false, '');
INSERT INTO languages_iso639 VALUES ('sts', '   ', '   ', '  ', 'I', 'L', 'Shumashti', false, '');
INSERT INTO languages_iso639 VALUES ('stt', '   ', '   ', '  ', 'I', 'L', 'Budeh Stieng', false, '');
INSERT INTO languages_iso639 VALUES ('stu', '   ', '   ', '  ', 'I', 'L', 'Samtao', false, '');
INSERT INTO languages_iso639 VALUES ('stv', '   ', '   ', '  ', 'I', 'L', 'Silt''e', false, '');
INSERT INTO languages_iso639 VALUES ('stw', '   ', '   ', '  ', 'I', 'L', 'Satawalese', false, '');
INSERT INTO languages_iso639 VALUES ('sua', '   ', '   ', '  ', 'I', 'L', 'Sulka', false, '');
INSERT INTO languages_iso639 VALUES ('sub', '   ', '   ', '  ', 'I', 'L', 'Suku', false, '');
INSERT INTO languages_iso639 VALUES ('suc', '   ', '   ', '  ', 'I', 'L', 'Western Subanon', false, '');
INSERT INTO languages_iso639 VALUES ('sue', '   ', '   ', '  ', 'I', 'L', 'Suena', false, '');
INSERT INTO languages_iso639 VALUES ('sug', '   ', '   ', '  ', 'I', 'L', 'Suganga', false, '');
INSERT INTO languages_iso639 VALUES ('sui', '   ', '   ', '  ', 'I', 'L', 'Suki', false, '');
INSERT INTO languages_iso639 VALUES ('suj', '   ', '   ', '  ', 'I', 'L', 'Shubi', false, '');
INSERT INTO languages_iso639 VALUES ('suk', 'suk', 'suk', '  ', 'I', 'L', 'Sukuma', false, '');
INSERT INTO languages_iso639 VALUES ('sun', 'sun', 'sun', 'su', 'I', 'L', 'Sundanese', false, '');
INSERT INTO languages_iso639 VALUES ('suq', '   ', '   ', '  ', 'I', 'L', 'Suri', false, '');
INSERT INTO languages_iso639 VALUES ('sur', '   ', '   ', '  ', 'I', 'L', 'Mwaghavul', false, '');
INSERT INTO languages_iso639 VALUES ('sus', 'sus', 'sus', '  ', 'I', 'L', 'Susu', false, '');
INSERT INTO languages_iso639 VALUES ('sut', '   ', '   ', '  ', 'I', 'E', 'Subtiaba', false, '');
INSERT INTO languages_iso639 VALUES ('suv', '   ', '   ', '  ', 'I', 'L', 'Puroik', false, '');
INSERT INTO languages_iso639 VALUES ('suw', '   ', '   ', '  ', 'I', 'L', 'Sumbwa', false, '');
INSERT INTO languages_iso639 VALUES ('sux', 'sux', 'sux', '  ', 'I', 'A', 'Sumerian', false, '');
INSERT INTO languages_iso639 VALUES ('suy', '   ', '   ', '  ', 'I', 'L', 'Suyá', false, '');
INSERT INTO languages_iso639 VALUES ('suz', '   ', '   ', '  ', 'I', 'L', 'Sunwar', false, '');
INSERT INTO languages_iso639 VALUES ('sva', '   ', '   ', '  ', 'I', 'L', 'Svan', false, '');
INSERT INTO languages_iso639 VALUES ('svb', '   ', '   ', '  ', 'I', 'L', 'Ulau-Suain', false, '');
INSERT INTO languages_iso639 VALUES ('svc', '   ', '   ', '  ', 'I', 'L', 'Vincentian Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('sve', '   ', '   ', '  ', 'I', 'L', 'Serili', false, '');
INSERT INTO languages_iso639 VALUES ('svk', '   ', '   ', '  ', 'I', 'L', 'Slovakian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('svm', '   ', '   ', '  ', 'I', 'L', 'Slavomolisano', false, '');
INSERT INTO languages_iso639 VALUES ('svr', '   ', '   ', '  ', 'I', 'L', 'Savara', false, '');
INSERT INTO languages_iso639 VALUES ('svs', '   ', '   ', '  ', 'I', 'L', 'Savosavo', false, '');
INSERT INTO languages_iso639 VALUES ('svx', '   ', '   ', '  ', 'I', 'E', 'Skalvian', false, '');
INSERT INTO languages_iso639 VALUES ('swa', 'swa', 'swa', 'sw', 'M', 'L', 'Swahili (macrolanguage)', false, '');
INSERT INTO languages_iso639 VALUES ('swb', '   ', '   ', '  ', 'I', 'L', 'Maore Comorian', false, '');
INSERT INTO languages_iso639 VALUES ('swc', '   ', '   ', '  ', 'I', 'L', 'Congo Swahili', false, '');
INSERT INTO languages_iso639 VALUES ('swe', 'swe', 'swe', 'sv', 'I', 'L', 'Swedish', false, '');
INSERT INTO languages_iso639 VALUES ('swf', '   ', '   ', '  ', 'I', 'L', 'Sere', false, '');
INSERT INTO languages_iso639 VALUES ('swg', '   ', '   ', '  ', 'I', 'L', 'Swabian', false, '');
INSERT INTO languages_iso639 VALUES ('swh', '   ', '   ', '  ', 'I', 'L', 'Swahili (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('swi', '   ', '   ', '  ', 'I', 'L', 'Sui', false, '');
INSERT INTO languages_iso639 VALUES ('swj', '   ', '   ', '  ', 'I', 'L', 'Sira', false, '');
INSERT INTO languages_iso639 VALUES ('swk', '   ', '   ', '  ', 'I', 'L', 'Malawi Sena', false, '');
INSERT INTO languages_iso639 VALUES ('swl', '   ', '   ', '  ', 'I', 'L', 'Swedish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('swm', '   ', '   ', '  ', 'I', 'L', 'Samosa', false, '');
INSERT INTO languages_iso639 VALUES ('swn', '   ', '   ', '  ', 'I', 'L', 'Sawknah', false, '');
INSERT INTO languages_iso639 VALUES ('swo', '   ', '   ', '  ', 'I', 'L', 'Shanenawa', false, '');
INSERT INTO languages_iso639 VALUES ('swp', '   ', '   ', '  ', 'I', 'L', 'Suau', false, '');
INSERT INTO languages_iso639 VALUES ('swq', '   ', '   ', '  ', 'I', 'L', 'Sharwa', false, '');
INSERT INTO languages_iso639 VALUES ('swr', '   ', '   ', '  ', 'I', 'L', 'Saweru', false, '');
INSERT INTO languages_iso639 VALUES ('sws', '   ', '   ', '  ', 'I', 'L', 'Seluwasan', false, '');
INSERT INTO languages_iso639 VALUES ('swt', '   ', '   ', '  ', 'I', 'L', 'Sawila', false, '');
INSERT INTO languages_iso639 VALUES ('swu', '   ', '   ', '  ', 'I', 'L', 'Suwawa', false, '');
INSERT INTO languages_iso639 VALUES ('swv', '   ', '   ', '  ', 'I', 'L', 'Shekhawati', false, '');
INSERT INTO languages_iso639 VALUES ('sww', '   ', '   ', '  ', 'I', 'E', 'Sowa', false, '');
INSERT INTO languages_iso639 VALUES ('swx', '   ', '   ', '  ', 'I', 'L', 'Suruahá', false, '');
INSERT INTO languages_iso639 VALUES ('swy', '   ', '   ', '  ', 'I', 'L', 'Sarua', false, '');
INSERT INTO languages_iso639 VALUES ('sxb', '   ', '   ', '  ', 'I', 'L', 'Suba', false, '');
INSERT INTO languages_iso639 VALUES ('sxc', '   ', '   ', '  ', 'I', 'A', 'Sicanian', false, '');
INSERT INTO languages_iso639 VALUES ('sxe', '   ', '   ', '  ', 'I', 'L', 'Sighu', false, '');
INSERT INTO languages_iso639 VALUES ('sxg', '   ', '   ', '  ', 'I', 'L', 'Shixing', false, '');
INSERT INTO languages_iso639 VALUES ('sxk', '   ', '   ', '  ', 'I', 'E', 'Southern Kalapuya', false, '');
INSERT INTO languages_iso639 VALUES ('sxl', '   ', '   ', '  ', 'I', 'E', 'Selian', false, '');
INSERT INTO languages_iso639 VALUES ('sxm', '   ', '   ', '  ', 'I', 'L', 'Samre', false, '');
INSERT INTO languages_iso639 VALUES ('sxn', '   ', '   ', '  ', 'I', 'L', 'Sangir', false, '');
INSERT INTO languages_iso639 VALUES ('sxo', '   ', '   ', '  ', 'I', 'A', 'Sorothaptic', false, '');
INSERT INTO languages_iso639 VALUES ('sxr', '   ', '   ', '  ', 'I', 'L', 'Saaroa', false, '');
INSERT INTO languages_iso639 VALUES ('sxs', '   ', '   ', '  ', 'I', 'L', 'Sasaru', false, '');
INSERT INTO languages_iso639 VALUES ('sxu', '   ', '   ', '  ', 'I', 'L', 'Upper Saxon', false, '');
INSERT INTO languages_iso639 VALUES ('sxw', '   ', '   ', '  ', 'I', 'L', 'Saxwe Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('sya', '   ', '   ', '  ', 'I', 'L', 'Siang', false, '');
INSERT INTO languages_iso639 VALUES ('syb', '   ', '   ', '  ', 'I', 'L', 'Central Subanen', false, '');
INSERT INTO languages_iso639 VALUES ('syc', 'syc', 'syc', '  ', 'I', 'H', 'Classical Syriac', false, '');
INSERT INTO languages_iso639 VALUES ('syi', '   ', '   ', '  ', 'I', 'L', 'Seki', false, '');
INSERT INTO languages_iso639 VALUES ('syk', '   ', '   ', '  ', 'I', 'L', 'Sukur', false, '');
INSERT INTO languages_iso639 VALUES ('syl', '   ', '   ', '  ', 'I', 'L', 'Sylheti', false, '');
INSERT INTO languages_iso639 VALUES ('sym', '   ', '   ', '  ', 'I', 'L', 'Maya Samo', false, '');
INSERT INTO languages_iso639 VALUES ('syn', '   ', '   ', '  ', 'I', 'L', 'Senaya', false, '');
INSERT INTO languages_iso639 VALUES ('syo', '   ', '   ', '  ', 'I', 'L', 'Suoy', false, '');
INSERT INTO languages_iso639 VALUES ('syr', 'syr', 'syr', '  ', 'M', 'L', 'Syriac', false, '');
INSERT INTO languages_iso639 VALUES ('sys', '   ', '   ', '  ', 'I', 'L', 'Sinyar', false, '');
INSERT INTO languages_iso639 VALUES ('syw', '   ', '   ', '  ', 'I', 'L', 'Kagate', false, '');
INSERT INTO languages_iso639 VALUES ('syy', '   ', '   ', '  ', 'I', 'L', 'Al-Sayyid Bedouin Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('sza', '   ', '   ', '  ', 'I', 'L', 'Semelai', false, '');
INSERT INTO languages_iso639 VALUES ('szb', '   ', '   ', '  ', 'I', 'L', 'Ngalum', false, '');
INSERT INTO languages_iso639 VALUES ('szc', '   ', '   ', '  ', 'I', 'L', 'Semaq Beri', false, '');
INSERT INTO languages_iso639 VALUES ('szd', '   ', '   ', '  ', 'I', 'E', 'Seru', false, '');
INSERT INTO languages_iso639 VALUES ('sze', '   ', '   ', '  ', 'I', 'L', 'Seze', false, '');
INSERT INTO languages_iso639 VALUES ('szg', '   ', '   ', '  ', 'I', 'L', 'Sengele', false, '');
INSERT INTO languages_iso639 VALUES ('szl', '   ', '   ', '  ', 'I', 'L', 'Silesian', false, '');
INSERT INTO languages_iso639 VALUES ('szn', '   ', '   ', '  ', 'I', 'L', 'Sula', false, '');
INSERT INTO languages_iso639 VALUES ('szp', '   ', '   ', '  ', 'I', 'L', 'Suabo', false, '');
INSERT INTO languages_iso639 VALUES ('szv', '   ', '   ', '  ', 'I', 'L', 'Isu (Fako Division)', false, '');
INSERT INTO languages_iso639 VALUES ('szw', '   ', '   ', '  ', 'I', 'L', 'Sawai', false, '');
INSERT INTO languages_iso639 VALUES ('taa', '   ', '   ', '  ', 'I', 'L', 'Lower Tanana', false, '');
INSERT INTO languages_iso639 VALUES ('tab', '   ', '   ', '  ', 'I', 'L', 'Tabassaran', false, '');
INSERT INTO languages_iso639 VALUES ('tac', '   ', '   ', '  ', 'I', 'L', 'Lowland Tarahumara', false, '');
INSERT INTO languages_iso639 VALUES ('tad', '   ', '   ', '  ', 'I', 'L', 'Tause', false, '');
INSERT INTO languages_iso639 VALUES ('tae', '   ', '   ', '  ', 'I', 'L', 'Tariana', false, '');
INSERT INTO languages_iso639 VALUES ('taf', '   ', '   ', '  ', 'I', 'L', 'Tapirapé', false, '');
INSERT INTO languages_iso639 VALUES ('tag', '   ', '   ', '  ', 'I', 'L', 'Tagoi', false, '');
INSERT INTO languages_iso639 VALUES ('tah', 'tah', 'tah', 'ty', 'I', 'L', 'Tahitian', false, '');
INSERT INTO languages_iso639 VALUES ('taj', '   ', '   ', '  ', 'I', 'L', 'Eastern Tamang', false, '');
INSERT INTO languages_iso639 VALUES ('tak', '   ', '   ', '  ', 'I', 'L', 'Tala', false, '');
INSERT INTO languages_iso639 VALUES ('tal', '   ', '   ', '  ', 'I', 'L', 'Tal', false, '');
INSERT INTO languages_iso639 VALUES ('tam', 'tam', 'tam', 'ta', 'I', 'L', 'Tamil', false, '');
INSERT INTO languages_iso639 VALUES ('tan', '   ', '   ', '  ', 'I', 'L', 'Tangale', false, '');
INSERT INTO languages_iso639 VALUES ('tao', '   ', '   ', '  ', 'I', 'L', 'Yami', false, '');
INSERT INTO languages_iso639 VALUES ('tap', '   ', '   ', '  ', 'I', 'L', 'Taabwa', false, '');
INSERT INTO languages_iso639 VALUES ('taq', '   ', '   ', '  ', 'I', 'L', 'Tamasheq', false, '');
INSERT INTO languages_iso639 VALUES ('tar', '   ', '   ', '  ', 'I', 'L', 'Central Tarahumara', false, '');
INSERT INTO languages_iso639 VALUES ('tas', '   ', '   ', '  ', 'I', 'E', 'Tay Boi', false, '');
INSERT INTO languages_iso639 VALUES ('tat', 'tat', 'tat', 'tt', 'I', 'L', 'Tatar', false, '');
INSERT INTO languages_iso639 VALUES ('tau', '   ', '   ', '  ', 'I', 'L', 'Upper Tanana', false, '');
INSERT INTO languages_iso639 VALUES ('tav', '   ', '   ', '  ', 'I', 'L', 'Tatuyo', false, '');
INSERT INTO languages_iso639 VALUES ('taw', '   ', '   ', '  ', 'I', 'L', 'Tai', false, '');
INSERT INTO languages_iso639 VALUES ('tax', '   ', '   ', '  ', 'I', 'L', 'Tamki', false, '');
INSERT INTO languages_iso639 VALUES ('tay', '   ', '   ', '  ', 'I', 'L', 'Atayal', false, '');
INSERT INTO languages_iso639 VALUES ('taz', '   ', '   ', '  ', 'I', 'L', 'Tocho', false, '');
INSERT INTO languages_iso639 VALUES ('tba', '   ', '   ', '  ', 'I', 'L', 'Aikanã', false, '');
INSERT INTO languages_iso639 VALUES ('tbb', '   ', '   ', '  ', 'I', 'E', 'Tapeba', false, '');
INSERT INTO languages_iso639 VALUES ('tbc', '   ', '   ', '  ', 'I', 'L', 'Takia', false, '');
INSERT INTO languages_iso639 VALUES ('tbd', '   ', '   ', '  ', 'I', 'L', 'Kaki Ae', false, '');
INSERT INTO languages_iso639 VALUES ('tbe', '   ', '   ', '  ', 'I', 'L', 'Tanimbili', false, '');
INSERT INTO languages_iso639 VALUES ('tbf', '   ', '   ', '  ', 'I', 'L', 'Mandara', false, '');
INSERT INTO languages_iso639 VALUES ('tbg', '   ', '   ', '  ', 'I', 'L', 'North Tairora', false, '');
INSERT INTO languages_iso639 VALUES ('tbh', '   ', '   ', '  ', 'I', 'E', 'Thurawal', false, '');
INSERT INTO languages_iso639 VALUES ('tbi', '   ', '   ', '  ', 'I', 'L', 'Gaam', false, '');
INSERT INTO languages_iso639 VALUES ('tbj', '   ', '   ', '  ', 'I', 'L', 'Tiang', false, '');
INSERT INTO languages_iso639 VALUES ('tbk', '   ', '   ', '  ', 'I', 'L', 'Calamian Tagbanwa', false, '');
INSERT INTO languages_iso639 VALUES ('tbl', '   ', '   ', '  ', 'I', 'L', 'Tboli', false, '');
INSERT INTO languages_iso639 VALUES ('tbm', '   ', '   ', '  ', 'I', 'L', 'Tagbu', false, '');
INSERT INTO languages_iso639 VALUES ('tbn', '   ', '   ', '  ', 'I', 'L', 'Barro Negro Tunebo', false, '');
INSERT INTO languages_iso639 VALUES ('tbo', '   ', '   ', '  ', 'I', 'L', 'Tawala', false, '');
INSERT INTO languages_iso639 VALUES ('tbp', '   ', '   ', '  ', 'I', 'L', 'Taworta', false, '');
INSERT INTO languages_iso639 VALUES ('tbr', '   ', '   ', '  ', 'I', 'L', 'Tumtum', false, '');
INSERT INTO languages_iso639 VALUES ('tbs', '   ', '   ', '  ', 'I', 'L', 'Tanguat', false, '');
INSERT INTO languages_iso639 VALUES ('tbt', '   ', '   ', '  ', 'I', 'L', 'Tembo (Kitembo)', false, '');
INSERT INTO languages_iso639 VALUES ('tbu', '   ', '   ', '  ', 'I', 'E', 'Tubar', false, '');
INSERT INTO languages_iso639 VALUES ('tbv', '   ', '   ', '  ', 'I', 'L', 'Tobo', false, '');
INSERT INTO languages_iso639 VALUES ('tbw', '   ', '   ', '  ', 'I', 'L', 'Tagbanwa', false, '');
INSERT INTO languages_iso639 VALUES ('tbx', '   ', '   ', '  ', 'I', 'L', 'Kapin', false, '');
INSERT INTO languages_iso639 VALUES ('tby', '   ', '   ', '  ', 'I', 'L', 'Tabaru', false, '');
INSERT INTO languages_iso639 VALUES ('tbz', '   ', '   ', '  ', 'I', 'L', 'Ditammari', false, '');
INSERT INTO languages_iso639 VALUES ('tca', '   ', '   ', '  ', 'I', 'L', 'Ticuna', false, '');
INSERT INTO languages_iso639 VALUES ('tcb', '   ', '   ', '  ', 'I', 'L', 'Tanacross', false, '');
INSERT INTO languages_iso639 VALUES ('tcc', '   ', '   ', '  ', 'I', 'L', 'Datooga', false, '');
INSERT INTO languages_iso639 VALUES ('tcd', '   ', '   ', '  ', 'I', 'L', 'Tafi', false, '');
INSERT INTO languages_iso639 VALUES ('tce', '   ', '   ', '  ', 'I', 'L', 'Southern Tutchone', false, '');
INSERT INTO languages_iso639 VALUES ('tcf', '   ', '   ', '  ', 'I', 'L', 'Malinaltepec Me''phaa', false, '');
INSERT INTO languages_iso639 VALUES ('tcg', '   ', '   ', '  ', 'I', 'L', 'Tamagario', false, '');
INSERT INTO languages_iso639 VALUES ('tch', '   ', '   ', '  ', 'I', 'L', 'Turks And Caicos Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('tci', '   ', '   ', '  ', 'I', 'L', 'Wára', false, '');
INSERT INTO languages_iso639 VALUES ('tck', '   ', '   ', '  ', 'I', 'L', 'Tchitchege', false, '');
INSERT INTO languages_iso639 VALUES ('tcl', '   ', '   ', '  ', 'I', 'E', 'Taman (Myanmar)', false, '');
INSERT INTO languages_iso639 VALUES ('tcm', '   ', '   ', '  ', 'I', 'L', 'Tanahmerah', false, '');
INSERT INTO languages_iso639 VALUES ('tcn', '   ', '   ', '  ', 'I', 'L', 'Tichurong', false, '');
INSERT INTO languages_iso639 VALUES ('tco', '   ', '   ', '  ', 'I', 'L', 'Taungyo', false, '');
INSERT INTO languages_iso639 VALUES ('tcp', '   ', '   ', '  ', 'I', 'L', 'Tawr Chin', false, '');
INSERT INTO languages_iso639 VALUES ('tcq', '   ', '   ', '  ', 'I', 'L', 'Kaiy', false, '');
INSERT INTO languages_iso639 VALUES ('tcs', '   ', '   ', '  ', 'I', 'L', 'Torres Strait Creole', false, '');
INSERT INTO languages_iso639 VALUES ('tct', '   ', '   ', '  ', 'I', 'L', 'T''en', false, '');
INSERT INTO languages_iso639 VALUES ('tcu', '   ', '   ', '  ', 'I', 'L', 'Southeastern Tarahumara', false, '');
INSERT INTO languages_iso639 VALUES ('tcw', '   ', '   ', '  ', 'I', 'L', 'Tecpatlán Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('tcx', '   ', '   ', '  ', 'I', 'L', 'Toda', false, '');
INSERT INTO languages_iso639 VALUES ('tcy', '   ', '   ', '  ', 'I', 'L', 'Tulu', false, '');
INSERT INTO languages_iso639 VALUES ('tcz', '   ', '   ', '  ', 'I', 'L', 'Thado Chin', false, '');
INSERT INTO languages_iso639 VALUES ('tda', '   ', '   ', '  ', 'I', 'L', 'Tagdal', false, '');
INSERT INTO languages_iso639 VALUES ('tdb', '   ', '   ', '  ', 'I', 'L', 'Panchpargania', false, '');
INSERT INTO languages_iso639 VALUES ('tdc', '   ', '   ', '  ', 'I', 'L', 'Emberá-Tadó', false, '');
INSERT INTO languages_iso639 VALUES ('tdd', '   ', '   ', '  ', 'I', 'L', 'Tai Nüa', false, '');
INSERT INTO languages_iso639 VALUES ('tde', '   ', '   ', '  ', 'I', 'L', 'Tiranige Diga Dogon', false, '');
INSERT INTO languages_iso639 VALUES ('tdf', '   ', '   ', '  ', 'I', 'L', 'Talieng', false, '');
INSERT INTO languages_iso639 VALUES ('tdg', '   ', '   ', '  ', 'I', 'L', 'Western Tamang', false, '');
INSERT INTO languages_iso639 VALUES ('tdh', '   ', '   ', '  ', 'I', 'L', 'Thulung', false, '');
INSERT INTO languages_iso639 VALUES ('tdi', '   ', '   ', '  ', 'I', 'L', 'Tomadino', false, '');
INSERT INTO languages_iso639 VALUES ('tdj', '   ', '   ', '  ', 'I', 'L', 'Tajio', false, '');
INSERT INTO languages_iso639 VALUES ('tdk', '   ', '   ', '  ', 'I', 'L', 'Tambas', false, '');
INSERT INTO languages_iso639 VALUES ('tdl', '   ', '   ', '  ', 'I', 'L', 'Sur', false, '');
INSERT INTO languages_iso639 VALUES ('tdn', '   ', '   ', '  ', 'I', 'L', 'Tondano', false, '');
INSERT INTO languages_iso639 VALUES ('tdo', '   ', '   ', '  ', 'I', 'L', 'Teme', false, '');
INSERT INTO languages_iso639 VALUES ('tdq', '   ', '   ', '  ', 'I', 'L', 'Tita', false, '');
INSERT INTO languages_iso639 VALUES ('tdr', '   ', '   ', '  ', 'I', 'L', 'Todrah', false, '');
INSERT INTO languages_iso639 VALUES ('tds', '   ', '   ', '  ', 'I', 'L', 'Doutai', false, '');
INSERT INTO languages_iso639 VALUES ('tdt', '   ', '   ', '  ', 'I', 'L', 'Tetun Dili', false, '');
INSERT INTO languages_iso639 VALUES ('tdu', '   ', '   ', '  ', 'I', 'L', 'Tempasuk Dusun', false, '');
INSERT INTO languages_iso639 VALUES ('tdv', '   ', '   ', '  ', 'I', 'L', 'Toro', false, '');
INSERT INTO languages_iso639 VALUES ('tdx', '   ', '   ', '  ', 'I', 'L', 'Tandroy-Mahafaly Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('tdy', '   ', '   ', '  ', 'I', 'L', 'Tadyawan', false, '');
INSERT INTO languages_iso639 VALUES ('tea', '   ', '   ', '  ', 'I', 'L', 'Temiar', false, '');
INSERT INTO languages_iso639 VALUES ('teb', '   ', '   ', '  ', 'I', 'E', 'Tetete', false, '');
INSERT INTO languages_iso639 VALUES ('tec', '   ', '   ', '  ', 'I', 'L', 'Terik', false, '');
INSERT INTO languages_iso639 VALUES ('ted', '   ', '   ', '  ', 'I', 'L', 'Tepo Krumen', false, '');
INSERT INTO languages_iso639 VALUES ('tee', '   ', '   ', '  ', 'I', 'L', 'Huehuetla Tepehua', false, '');
INSERT INTO languages_iso639 VALUES ('tef', '   ', '   ', '  ', 'I', 'L', 'Teressa', false, '');
INSERT INTO languages_iso639 VALUES ('teg', '   ', '   ', '  ', 'I', 'L', 'Teke-Tege', false, '');
INSERT INTO languages_iso639 VALUES ('teh', '   ', '   ', '  ', 'I', 'L', 'Tehuelche', false, '');
INSERT INTO languages_iso639 VALUES ('tei', '   ', '   ', '  ', 'I', 'L', 'Torricelli', false, '');
INSERT INTO languages_iso639 VALUES ('tek', '   ', '   ', '  ', 'I', 'L', 'Ibali Teke', false, '');
INSERT INTO languages_iso639 VALUES ('tel', 'tel', 'tel', 'te', 'I', 'L', 'Telugu', false, '');
INSERT INTO languages_iso639 VALUES ('tem', 'tem', 'tem', '  ', 'I', 'L', 'Timne', false, '');
INSERT INTO languages_iso639 VALUES ('ten', '   ', '   ', '  ', 'I', 'E', 'Tama (Colombia)', false, '');
INSERT INTO languages_iso639 VALUES ('teo', '   ', '   ', '  ', 'I', 'L', 'Teso', false, '');
INSERT INTO languages_iso639 VALUES ('tep', '   ', '   ', '  ', 'I', 'E', 'Tepecano', false, '');
INSERT INTO languages_iso639 VALUES ('teq', '   ', '   ', '  ', 'I', 'L', 'Temein', false, '');
INSERT INTO languages_iso639 VALUES ('ter', 'ter', 'ter', '  ', 'I', 'L', 'Tereno', false, '');
INSERT INTO languages_iso639 VALUES ('tes', '   ', '   ', '  ', 'I', 'L', 'Tengger', false, '');
INSERT INTO languages_iso639 VALUES ('tet', 'tet', 'tet', '  ', 'I', 'L', 'Tetum', false, '');
INSERT INTO languages_iso639 VALUES ('teu', '   ', '   ', '  ', 'I', 'L', 'Soo', false, '');
INSERT INTO languages_iso639 VALUES ('tev', '   ', '   ', '  ', 'I', 'L', 'Teor', false, '');
INSERT INTO languages_iso639 VALUES ('tew', '   ', '   ', '  ', 'I', 'L', 'Tewa (USA)', false, '');
INSERT INTO languages_iso639 VALUES ('tex', '   ', '   ', '  ', 'I', 'L', 'Tennet', false, '');
INSERT INTO languages_iso639 VALUES ('tey', '   ', '   ', '  ', 'I', 'L', 'Tulishi', false, '');
INSERT INTO languages_iso639 VALUES ('tfi', '   ', '   ', '  ', 'I', 'L', 'Tofin Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('tfn', '   ', '   ', '  ', 'I', 'L', 'Tanaina', false, '');
INSERT INTO languages_iso639 VALUES ('tfo', '   ', '   ', '  ', 'I', 'L', 'Tefaro', false, '');
INSERT INTO languages_iso639 VALUES ('tfr', '   ', '   ', '  ', 'I', 'L', 'Teribe', false, '');
INSERT INTO languages_iso639 VALUES ('tft', '   ', '   ', '  ', 'I', 'L', 'Ternate', false, '');
INSERT INTO languages_iso639 VALUES ('tga', '   ', '   ', '  ', 'I', 'L', 'Sagalla', false, '');
INSERT INTO languages_iso639 VALUES ('tgb', '   ', '   ', '  ', 'I', 'L', 'Tobilung', false, '');
INSERT INTO languages_iso639 VALUES ('tgc', '   ', '   ', '  ', 'I', 'L', 'Tigak', false, '');
INSERT INTO languages_iso639 VALUES ('tgd', '   ', '   ', '  ', 'I', 'L', 'Ciwogai', false, '');
INSERT INTO languages_iso639 VALUES ('tge', '   ', '   ', '  ', 'I', 'L', 'Eastern Gorkha Tamang', false, '');
INSERT INTO languages_iso639 VALUES ('tgf', '   ', '   ', '  ', 'I', 'L', 'Chalikha', false, '');
INSERT INTO languages_iso639 VALUES ('tgh', '   ', '   ', '  ', 'I', 'L', 'Tobagonian Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('tgi', '   ', '   ', '  ', 'I', 'L', 'Lawunuia', false, '');
INSERT INTO languages_iso639 VALUES ('tgj', '   ', '   ', '  ', 'I', 'L', 'Tagin', false, '');
INSERT INTO languages_iso639 VALUES ('tgk', 'tgk', 'tgk', 'tg', 'I', 'L', 'Tajik', false, '');
INSERT INTO languages_iso639 VALUES ('tgl', 'tgl', 'tgl', 'tl', 'I', 'L', 'Tagalog', false, '');
INSERT INTO languages_iso639 VALUES ('tgn', '   ', '   ', '  ', 'I', 'L', 'Tandaganon', false, '');
INSERT INTO languages_iso639 VALUES ('tgo', '   ', '   ', '  ', 'I', 'L', 'Sudest', false, '');
INSERT INTO languages_iso639 VALUES ('tgp', '   ', '   ', '  ', 'I', 'L', 'Tangoa', false, '');
INSERT INTO languages_iso639 VALUES ('tgq', '   ', '   ', '  ', 'I', 'L', 'Tring', false, '');
INSERT INTO languages_iso639 VALUES ('tgr', '   ', '   ', '  ', 'I', 'L', 'Tareng', false, '');
INSERT INTO languages_iso639 VALUES ('tgs', '   ', '   ', '  ', 'I', 'L', 'Nume', false, '');
INSERT INTO languages_iso639 VALUES ('tgt', '   ', '   ', '  ', 'I', 'L', 'Central Tagbanwa', false, '');
INSERT INTO languages_iso639 VALUES ('tgu', '   ', '   ', '  ', 'I', 'L', 'Tanggu', false, '');
INSERT INTO languages_iso639 VALUES ('tgv', '   ', '   ', '  ', 'I', 'E', 'Tingui-Boto', false, '');
INSERT INTO languages_iso639 VALUES ('tgw', '   ', '   ', '  ', 'I', 'L', 'Tagwana Senoufo', false, '');
INSERT INTO languages_iso639 VALUES ('tgx', '   ', '   ', '  ', 'I', 'L', 'Tagish', false, '');
INSERT INTO languages_iso639 VALUES ('tgy', '   ', '   ', '  ', 'I', 'E', 'Togoyo', false, '');
INSERT INTO languages_iso639 VALUES ('tgz', '   ', '   ', '  ', 'I', 'E', 'Tagalaka', false, '');
INSERT INTO languages_iso639 VALUES ('tha', 'tha', 'tha', 'th', 'I', 'L', 'Thai', false, '');
INSERT INTO languages_iso639 VALUES ('thc', '   ', '   ', '  ', 'I', 'L', 'Tai Hang Tong', false, '');
INSERT INTO languages_iso639 VALUES ('thd', '   ', '   ', '  ', 'I', 'L', 'Thayore', false, '');
INSERT INTO languages_iso639 VALUES ('the', '   ', '   ', '  ', 'I', 'L', 'Chitwania Tharu', false, '');
INSERT INTO languages_iso639 VALUES ('thf', '   ', '   ', '  ', 'I', 'L', 'Thangmi', false, '');
INSERT INTO languages_iso639 VALUES ('thh', '   ', '   ', '  ', 'I', 'L', 'Northern Tarahumara', false, '');
INSERT INTO languages_iso639 VALUES ('thi', '   ', '   ', '  ', 'I', 'L', 'Tai Long', false, '');
INSERT INTO languages_iso639 VALUES ('thk', '   ', '   ', '  ', 'I', 'L', 'Tharaka', false, '');
INSERT INTO languages_iso639 VALUES ('thl', '   ', '   ', '  ', 'I', 'L', 'Dangaura Tharu', false, '');
INSERT INTO languages_iso639 VALUES ('thm', '   ', '   ', '  ', 'I', 'L', 'Aheu', false, '');
INSERT INTO languages_iso639 VALUES ('thn', '   ', '   ', '  ', 'I', 'L', 'Thachanadan', false, '');
INSERT INTO languages_iso639 VALUES ('thp', '   ', '   ', '  ', 'I', 'L', 'Thompson', false, '');
INSERT INTO languages_iso639 VALUES ('thq', '   ', '   ', '  ', 'I', 'L', 'Kochila Tharu', false, '');
INSERT INTO languages_iso639 VALUES ('thr', '   ', '   ', '  ', 'I', 'L', 'Rana Tharu', false, '');
INSERT INTO languages_iso639 VALUES ('ths', '   ', '   ', '  ', 'I', 'L', 'Thakali', false, '');
INSERT INTO languages_iso639 VALUES ('tht', '   ', '   ', '  ', 'I', 'L', 'Tahltan', false, '');
INSERT INTO languages_iso639 VALUES ('thu', '   ', '   ', '  ', 'I', 'L', 'Thuri', false, '');
INSERT INTO languages_iso639 VALUES ('thv', '   ', '   ', '  ', 'I', 'L', 'Tahaggart Tamahaq', false, '');
INSERT INTO languages_iso639 VALUES ('thw', '   ', '   ', '  ', 'I', 'L', 'Thudam', false, '');
INSERT INTO languages_iso639 VALUES ('thx', '   ', '   ', '  ', 'I', 'L', 'The', false, '');
INSERT INTO languages_iso639 VALUES ('thy', '   ', '   ', '  ', 'I', 'L', 'Tha', false, '');
INSERT INTO languages_iso639 VALUES ('thz', '   ', '   ', '  ', 'I', 'L', 'Tayart Tamajeq', false, '');
INSERT INTO languages_iso639 VALUES ('tia', '   ', '   ', '  ', 'I', 'L', 'Tidikelt Tamazight', false, '');
INSERT INTO languages_iso639 VALUES ('tic', '   ', '   ', '  ', 'I', 'L', 'Tira', false, '');
INSERT INTO languages_iso639 VALUES ('tid', '   ', '   ', '  ', 'I', 'L', 'Tidong', false, '');
INSERT INTO languages_iso639 VALUES ('tif', '   ', '   ', '  ', 'I', 'L', 'Tifal', false, '');
INSERT INTO languages_iso639 VALUES ('tig', 'tig', 'tig', '  ', 'I', 'L', 'Tigre', false, '');
INSERT INTO languages_iso639 VALUES ('tih', '   ', '   ', '  ', 'I', 'L', 'Timugon Murut', false, '');
INSERT INTO languages_iso639 VALUES ('tii', '   ', '   ', '  ', 'I', 'L', 'Tiene', false, '');
INSERT INTO languages_iso639 VALUES ('tij', '   ', '   ', '  ', 'I', 'L', 'Tilung', false, '');
INSERT INTO languages_iso639 VALUES ('tik', '   ', '   ', '  ', 'I', 'L', 'Tikar', false, '');
INSERT INTO languages_iso639 VALUES ('til', '   ', '   ', '  ', 'I', 'E', 'Tillamook', false, '');
INSERT INTO languages_iso639 VALUES ('tim', '   ', '   ', '  ', 'I', 'L', 'Timbe', false, '');
INSERT INTO languages_iso639 VALUES ('tin', '   ', '   ', '  ', 'I', 'L', 'Tindi', false, '');
INSERT INTO languages_iso639 VALUES ('tio', '   ', '   ', '  ', 'I', 'L', 'Teop', false, '');
INSERT INTO languages_iso639 VALUES ('tip', '   ', '   ', '  ', 'I', 'L', 'Trimuris', false, '');
INSERT INTO languages_iso639 VALUES ('tiq', '   ', '   ', '  ', 'I', 'L', 'Tiéfo', false, '');
INSERT INTO languages_iso639 VALUES ('tir', 'tir', 'tir', 'ti', 'I', 'L', 'Tigrinya', false, '');
INSERT INTO languages_iso639 VALUES ('tis', '   ', '   ', '  ', 'I', 'L', 'Masadiit Itneg', false, '');
INSERT INTO languages_iso639 VALUES ('tit', '   ', '   ', '  ', 'I', 'L', 'Tinigua', false, '');
INSERT INTO languages_iso639 VALUES ('tiu', '   ', '   ', '  ', 'I', 'L', 'Adasen', false, '');
INSERT INTO languages_iso639 VALUES ('tiv', 'tiv', 'tiv', '  ', 'I', 'L', 'Tiv', false, '');
INSERT INTO languages_iso639 VALUES ('tiw', '   ', '   ', '  ', 'I', 'L', 'Tiwi', false, '');
INSERT INTO languages_iso639 VALUES ('tix', '   ', '   ', '  ', 'I', 'L', 'Southern Tiwa', false, '');
INSERT INTO languages_iso639 VALUES ('tiy', '   ', '   ', '  ', 'I', 'L', 'Tiruray', false, '');
INSERT INTO languages_iso639 VALUES ('tiz', '   ', '   ', '  ', 'I', 'L', 'Tai Hongjin', false, '');
INSERT INTO languages_iso639 VALUES ('tja', '   ', '   ', '  ', 'I', 'L', 'Tajuasohn', false, '');
INSERT INTO languages_iso639 VALUES ('tjg', '   ', '   ', '  ', 'I', 'L', 'Tunjung', false, '');
INSERT INTO languages_iso639 VALUES ('tji', '   ', '   ', '  ', 'I', 'L', 'Northern Tujia', false, '');
INSERT INTO languages_iso639 VALUES ('tjl', '   ', '   ', '  ', 'I', 'L', 'Tai Laing', false, '');
INSERT INTO languages_iso639 VALUES ('tjm', '   ', '   ', '  ', 'I', 'E', 'Timucua', false, '');
INSERT INTO languages_iso639 VALUES ('tjn', '   ', '   ', '  ', 'I', 'E', 'Tonjon', false, '');
INSERT INTO languages_iso639 VALUES ('tjo', '   ', '   ', '  ', 'I', 'L', 'Temacine Tamazight', false, '');
INSERT INTO languages_iso639 VALUES ('tjs', '   ', '   ', '  ', 'I', 'L', 'Southern Tujia', false, '');
INSERT INTO languages_iso639 VALUES ('tju', '   ', '   ', '  ', 'I', 'E', 'Tjurruru', false, '');
INSERT INTO languages_iso639 VALUES ('tjw', '   ', '   ', '  ', 'I', 'L', 'Djabwurrung', false, '');
INSERT INTO languages_iso639 VALUES ('tka', '   ', '   ', '  ', 'I', 'E', 'Truká', false, '');
INSERT INTO languages_iso639 VALUES ('tkb', '   ', '   ', '  ', 'I', 'L', 'Buksa', false, '');
INSERT INTO languages_iso639 VALUES ('tkd', '   ', '   ', '  ', 'I', 'L', 'Tukudede', false, '');
INSERT INTO languages_iso639 VALUES ('tke', '   ', '   ', '  ', 'I', 'L', 'Takwane', false, '');
INSERT INTO languages_iso639 VALUES ('tkf', '   ', '   ', '  ', 'I', 'E', 'Tukumanféd', false, '');
INSERT INTO languages_iso639 VALUES ('tkg', '   ', '   ', '  ', 'I', 'L', 'Tesaka Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('tkl', 'tkl', 'tkl', '  ', 'I', 'L', 'Tokelau', false, '');
INSERT INTO languages_iso639 VALUES ('tkm', '   ', '   ', '  ', 'I', 'E', 'Takelma', false, '');
INSERT INTO languages_iso639 VALUES ('tkn', '   ', '   ', '  ', 'I', 'L', 'Toku-No-Shima', false, '');
INSERT INTO languages_iso639 VALUES ('tkp', '   ', '   ', '  ', 'I', 'L', 'Tikopia', false, '');
INSERT INTO languages_iso639 VALUES ('tkq', '   ', '   ', '  ', 'I', 'L', 'Tee', false, '');
INSERT INTO languages_iso639 VALUES ('tkr', '   ', '   ', '  ', 'I', 'L', 'Tsakhur', false, '');
INSERT INTO languages_iso639 VALUES ('tks', '   ', '   ', '  ', 'I', 'L', 'Takestani', false, '');
INSERT INTO languages_iso639 VALUES ('tkt', '   ', '   ', '  ', 'I', 'L', 'Kathoriya Tharu', false, '');
INSERT INTO languages_iso639 VALUES ('tku', '   ', '   ', '  ', 'I', 'L', 'Upper Necaxa Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('tkw', '   ', '   ', '  ', 'I', 'L', 'Teanu', false, '');
INSERT INTO languages_iso639 VALUES ('tkx', '   ', '   ', '  ', 'I', 'L', 'Tangko', false, '');
INSERT INTO languages_iso639 VALUES ('tkz', '   ', '   ', '  ', 'I', 'L', 'Takua', false, '');
INSERT INTO languages_iso639 VALUES ('tla', '   ', '   ', '  ', 'I', 'L', 'Southwestern Tepehuan', false, '');
INSERT INTO languages_iso639 VALUES ('tlb', '   ', '   ', '  ', 'I', 'L', 'Tobelo', false, '');
INSERT INTO languages_iso639 VALUES ('tlc', '   ', '   ', '  ', 'I', 'L', 'Yecuatla Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('tld', '   ', '   ', '  ', 'I', 'L', 'Talaud', false, '');
INSERT INTO languages_iso639 VALUES ('tlf', '   ', '   ', '  ', 'I', 'L', 'Telefol', false, '');
INSERT INTO languages_iso639 VALUES ('tlg', '   ', '   ', '  ', 'I', 'L', 'Tofanma', false, '');
INSERT INTO languages_iso639 VALUES ('tlh', 'tlh', 'tlh', '  ', 'I', 'C', 'Klingon', false, '');
INSERT INTO languages_iso639 VALUES ('tli', 'tli', 'tli', '  ', 'I', 'L', 'Tlingit', false, '');
INSERT INTO languages_iso639 VALUES ('tlj', '   ', '   ', '  ', 'I', 'L', 'Talinga-Bwisi', false, '');
INSERT INTO languages_iso639 VALUES ('tlk', '   ', '   ', '  ', 'I', 'L', 'Taloki', false, '');
INSERT INTO languages_iso639 VALUES ('tll', '   ', '   ', '  ', 'I', 'L', 'Tetela', false, '');
INSERT INTO languages_iso639 VALUES ('tlm', '   ', '   ', '  ', 'I', 'L', 'Tolomako', false, '');
INSERT INTO languages_iso639 VALUES ('tln', '   ', '   ', '  ', 'I', 'L', 'Talondo''', false, '');
INSERT INTO languages_iso639 VALUES ('tlo', '   ', '   ', '  ', 'I', 'L', 'Talodi', false, '');
INSERT INTO languages_iso639 VALUES ('tlp', '   ', '   ', '  ', 'I', 'L', 'Filomena Mata-Coahuitlán Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('tlq', '   ', '   ', '  ', 'I', 'L', 'Tai Loi', false, '');
INSERT INTO languages_iso639 VALUES ('tlr', '   ', '   ', '  ', 'I', 'L', 'Talise', false, '');
INSERT INTO languages_iso639 VALUES ('tls', '   ', '   ', '  ', 'I', 'L', 'Tambotalo', false, '');
INSERT INTO languages_iso639 VALUES ('tlt', '   ', '   ', '  ', 'I', 'L', 'Teluti', false, '');
INSERT INTO languages_iso639 VALUES ('tlu', '   ', '   ', '  ', 'I', 'L', 'Tulehu', false, '');
INSERT INTO languages_iso639 VALUES ('tlv', '   ', '   ', '  ', 'I', 'L', 'Taliabu', false, '');
INSERT INTO languages_iso639 VALUES ('tlx', '   ', '   ', '  ', 'I', 'L', 'Khehek', false, '');
INSERT INTO languages_iso639 VALUES ('tly', '   ', '   ', '  ', 'I', 'L', 'Talysh', false, '');
INSERT INTO languages_iso639 VALUES ('tma', '   ', '   ', '  ', 'I', 'L', 'Tama (Chad)', false, '');
INSERT INTO languages_iso639 VALUES ('tmb', '   ', '   ', '  ', 'I', 'L', 'Katbol', false, '');
INSERT INTO languages_iso639 VALUES ('tmc', '   ', '   ', '  ', 'I', 'L', 'Tumak', false, '');
INSERT INTO languages_iso639 VALUES ('tmd', '   ', '   ', '  ', 'I', 'L', 'Haruai', false, '');
INSERT INTO languages_iso639 VALUES ('tme', '   ', '   ', '  ', 'I', 'E', 'Tremembé', false, '');
INSERT INTO languages_iso639 VALUES ('tmf', '   ', '   ', '  ', 'I', 'L', 'Toba-Maskoy', false, '');
INSERT INTO languages_iso639 VALUES ('tmg', '   ', '   ', '  ', 'I', 'E', 'Ternateño', false, '');
INSERT INTO languages_iso639 VALUES ('tmh', 'tmh', 'tmh', '  ', 'M', 'L', 'Tamashek', false, '');
INSERT INTO languages_iso639 VALUES ('tmi', '   ', '   ', '  ', 'I', 'L', 'Tutuba', false, '');
INSERT INTO languages_iso639 VALUES ('tmj', '   ', '   ', '  ', 'I', 'L', 'Samarokena', false, '');
INSERT INTO languages_iso639 VALUES ('tmk', '   ', '   ', '  ', 'I', 'L', 'Northwestern Tamang', false, '');
INSERT INTO languages_iso639 VALUES ('tml', '   ', '   ', '  ', 'I', 'L', 'Tamnim Citak', false, '');
INSERT INTO languages_iso639 VALUES ('tmm', '   ', '   ', '  ', 'I', 'L', 'Tai Thanh', false, '');
INSERT INTO languages_iso639 VALUES ('tmn', '   ', '   ', '  ', 'I', 'L', 'Taman (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('tmo', '   ', '   ', '  ', 'I', 'L', 'Temoq', false, '');
INSERT INTO languages_iso639 VALUES ('tmp', '   ', '   ', '  ', 'I', 'L', 'Tai Mène', false, '');
INSERT INTO languages_iso639 VALUES ('tmq', '   ', '   ', '  ', 'I', 'L', 'Tumleo', false, '');
INSERT INTO languages_iso639 VALUES ('tmr', '   ', '   ', '  ', 'I', 'E', 'Jewish Babylonian Aramaic (ca. 200-1200 CE)', false, '');
INSERT INTO languages_iso639 VALUES ('tms', '   ', '   ', '  ', 'I', 'L', 'Tima', false, '');
INSERT INTO languages_iso639 VALUES ('tmt', '   ', '   ', '  ', 'I', 'L', 'Tasmate', false, '');
INSERT INTO languages_iso639 VALUES ('tmu', '   ', '   ', '  ', 'I', 'L', 'Iau', false, '');
INSERT INTO languages_iso639 VALUES ('tmv', '   ', '   ', '  ', 'I', 'L', 'Tembo (Motembo)', false, '');
INSERT INTO languages_iso639 VALUES ('tmw', '   ', '   ', '  ', 'I', 'L', 'Temuan', false, '');
INSERT INTO languages_iso639 VALUES ('tmy', '   ', '   ', '  ', 'I', 'L', 'Tami', false, '');
INSERT INTO languages_iso639 VALUES ('tmz', '   ', '   ', '  ', 'I', 'E', 'Tamanaku', false, '');
INSERT INTO languages_iso639 VALUES ('tna', '   ', '   ', '  ', 'I', 'L', 'Tacana', false, '');
INSERT INTO languages_iso639 VALUES ('tnb', '   ', '   ', '  ', 'I', 'L', 'Western Tunebo', false, '');
INSERT INTO languages_iso639 VALUES ('tnc', '   ', '   ', '  ', 'I', 'L', 'Tanimuca-Retuarã', false, '');
INSERT INTO languages_iso639 VALUES ('tnd', '   ', '   ', '  ', 'I', 'L', 'Angosturas Tunebo', false, '');
INSERT INTO languages_iso639 VALUES ('tne', '   ', '   ', '  ', 'I', 'L', 'Tinoc Kallahan', false, '');
INSERT INTO languages_iso639 VALUES ('tng', '   ', '   ', '  ', 'I', 'L', 'Tobanga', false, '');
INSERT INTO languages_iso639 VALUES ('tnh', '   ', '   ', '  ', 'I', 'L', 'Maiani', false, '');
INSERT INTO languages_iso639 VALUES ('tni', '   ', '   ', '  ', 'I', 'L', 'Tandia', false, '');
INSERT INTO languages_iso639 VALUES ('tnk', '   ', '   ', '  ', 'I', 'L', 'Kwamera', false, '');
INSERT INTO languages_iso639 VALUES ('tnl', '   ', '   ', '  ', 'I', 'L', 'Lenakel', false, '');
INSERT INTO languages_iso639 VALUES ('tnm', '   ', '   ', '  ', 'I', 'L', 'Tabla', false, '');
INSERT INTO languages_iso639 VALUES ('tnn', '   ', '   ', '  ', 'I', 'L', 'North Tanna', false, '');
INSERT INTO languages_iso639 VALUES ('tno', '   ', '   ', '  ', 'I', 'L', 'Toromono', false, '');
INSERT INTO languages_iso639 VALUES ('tnp', '   ', '   ', '  ', 'I', 'L', 'Whitesands', false, '');
INSERT INTO languages_iso639 VALUES ('tnq', '   ', '   ', '  ', 'I', 'E', 'Taino', false, '');
INSERT INTO languages_iso639 VALUES ('tnr', '   ', '   ', '  ', 'I', 'L', 'Ménik', false, '');
INSERT INTO languages_iso639 VALUES ('tns', '   ', '   ', '  ', 'I', 'L', 'Tenis', false, '');
INSERT INTO languages_iso639 VALUES ('tnt', '   ', '   ', '  ', 'I', 'L', 'Tontemboan', false, '');
INSERT INTO languages_iso639 VALUES ('tnu', '   ', '   ', '  ', 'I', 'L', 'Tay Khang', false, '');
INSERT INTO languages_iso639 VALUES ('tnv', '   ', '   ', '  ', 'I', 'L', 'Tangchangya', false, '');
INSERT INTO languages_iso639 VALUES ('tnw', '   ', '   ', '  ', 'I', 'L', 'Tonsawang', false, '');
INSERT INTO languages_iso639 VALUES ('tnx', '   ', '   ', '  ', 'I', 'L', 'Tanema', false, '');
INSERT INTO languages_iso639 VALUES ('tny', '   ', '   ', '  ', 'I', 'L', 'Tongwe', false, '');
INSERT INTO languages_iso639 VALUES ('tnz', '   ', '   ', '  ', 'I', 'L', 'Tonga (Thailand)', false, '');
INSERT INTO languages_iso639 VALUES ('tob', '   ', '   ', '  ', 'I', 'L', 'Toba', false, '');
INSERT INTO languages_iso639 VALUES ('toc', '   ', '   ', '  ', 'I', 'L', 'Coyutla Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('tod', '   ', '   ', '  ', 'I', 'L', 'Toma', false, '');
INSERT INTO languages_iso639 VALUES ('toe', '   ', '   ', '  ', 'I', 'E', 'Tomedes', false, '');
INSERT INTO languages_iso639 VALUES ('tof', '   ', '   ', '  ', 'I', 'L', 'Gizrra', false, '');
INSERT INTO languages_iso639 VALUES ('tog', 'tog', 'tog', '  ', 'I', 'L', 'Tonga (Nyasa)', false, '');
INSERT INTO languages_iso639 VALUES ('toh', '   ', '   ', '  ', 'I', 'L', 'Gitonga', false, '');
INSERT INTO languages_iso639 VALUES ('toi', '   ', '   ', '  ', 'I', 'L', 'Tonga (Zambia)', false, '');
INSERT INTO languages_iso639 VALUES ('toj', '   ', '   ', '  ', 'I', 'L', 'Tojolabal', false, '');
INSERT INTO languages_iso639 VALUES ('tol', '   ', '   ', '  ', 'I', 'L', 'Tolowa', false, '');
INSERT INTO languages_iso639 VALUES ('tom', '   ', '   ', '  ', 'I', 'L', 'Tombulu', false, '');
INSERT INTO languages_iso639 VALUES ('ton', 'ton', 'ton', 'to', 'I', 'L', 'Tonga (Tonga Islands)', false, '');
INSERT INTO languages_iso639 VALUES ('too', '   ', '   ', '  ', 'I', 'L', 'Xicotepec De Juárez Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('top', '   ', '   ', '  ', 'I', 'L', 'Papantla Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('toq', '   ', '   ', '  ', 'I', 'L', 'Toposa', false, '');
INSERT INTO languages_iso639 VALUES ('tor', '   ', '   ', '  ', 'I', 'L', 'Togbo-Vara Banda', false, '');
INSERT INTO languages_iso639 VALUES ('tos', '   ', '   ', '  ', 'I', 'L', 'Highland Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('tou', '   ', '   ', '  ', 'I', 'L', 'Tho', false, '');
INSERT INTO languages_iso639 VALUES ('tov', '   ', '   ', '  ', 'I', 'L', 'Upper Taromi', false, '');
INSERT INTO languages_iso639 VALUES ('tow', '   ', '   ', '  ', 'I', 'L', 'Jemez', false, '');
INSERT INTO languages_iso639 VALUES ('tox', '   ', '   ', '  ', 'I', 'L', 'Tobian', false, '');
INSERT INTO languages_iso639 VALUES ('toy', '   ', '   ', '  ', 'I', 'L', 'Topoiyo', false, '');
INSERT INTO languages_iso639 VALUES ('toz', '   ', '   ', '  ', 'I', 'L', 'To', false, '');
INSERT INTO languages_iso639 VALUES ('tpa', '   ', '   ', '  ', 'I', 'L', 'Taupota', false, '');
INSERT INTO languages_iso639 VALUES ('tpc', '   ', '   ', '  ', 'I', 'L', 'Azoyú Me''phaa', false, '');
INSERT INTO languages_iso639 VALUES ('tpe', '   ', '   ', '  ', 'I', 'L', 'Tippera', false, '');
INSERT INTO languages_iso639 VALUES ('tpf', '   ', '   ', '  ', 'I', 'L', 'Tarpia', false, '');
INSERT INTO languages_iso639 VALUES ('tpg', '   ', '   ', '  ', 'I', 'L', 'Kula', false, '');
INSERT INTO languages_iso639 VALUES ('tpi', 'tpi', 'tpi', '  ', 'I', 'L', 'Tok Pisin', false, '');
INSERT INTO languages_iso639 VALUES ('tpj', '   ', '   ', '  ', 'I', 'L', 'Tapieté', false, '');
INSERT INTO languages_iso639 VALUES ('tpk', '   ', '   ', '  ', 'I', 'E', 'Tupinikin', false, '');
INSERT INTO languages_iso639 VALUES ('tpl', '   ', '   ', '  ', 'I', 'L', 'Tlacoapa Me''phaa', false, '');
INSERT INTO languages_iso639 VALUES ('tpm', '   ', '   ', '  ', 'I', 'L', 'Tampulma', false, '');
INSERT INTO languages_iso639 VALUES ('tpn', '   ', '   ', '  ', 'I', 'E', 'Tupinambá', false, '');
INSERT INTO languages_iso639 VALUES ('tpo', '   ', '   ', '  ', 'I', 'L', 'Tai Pao', false, '');
INSERT INTO languages_iso639 VALUES ('tpp', '   ', '   ', '  ', 'I', 'L', 'Pisaflores Tepehua', false, '');
INSERT INTO languages_iso639 VALUES ('tpq', '   ', '   ', '  ', 'I', 'L', 'Tukpa', false, '');
INSERT INTO languages_iso639 VALUES ('tpr', '   ', '   ', '  ', 'I', 'L', 'Tuparí', false, '');
INSERT INTO languages_iso639 VALUES ('tpt', '   ', '   ', '  ', 'I', 'L', 'Tlachichilco Tepehua', false, '');
INSERT INTO languages_iso639 VALUES ('tpu', '   ', '   ', '  ', 'I', 'L', 'Tampuan', false, '');
INSERT INTO languages_iso639 VALUES ('tpv', '   ', '   ', '  ', 'I', 'L', 'Tanapag', false, '');
INSERT INTO languages_iso639 VALUES ('tpw', '   ', '   ', '  ', 'I', 'E', 'Tupí', false, '');
INSERT INTO languages_iso639 VALUES ('tpx', '   ', '   ', '  ', 'I', 'L', 'Acatepec Me''phaa', false, '');
INSERT INTO languages_iso639 VALUES ('tpy', '   ', '   ', '  ', 'I', 'L', 'Trumai', false, '');
INSERT INTO languages_iso639 VALUES ('tpz', '   ', '   ', '  ', 'I', 'L', 'Tinputz', false, '');
INSERT INTO languages_iso639 VALUES ('tqb', '   ', '   ', '  ', 'I', 'L', 'Tembé', false, '');
INSERT INTO languages_iso639 VALUES ('tql', '   ', '   ', '  ', 'I', 'L', 'Lehali', false, '');
INSERT INTO languages_iso639 VALUES ('tqm', '   ', '   ', '  ', 'I', 'L', 'Turumsa', false, '');
INSERT INTO languages_iso639 VALUES ('tqn', '   ', '   ', '  ', 'I', 'L', 'Tenino', false, '');
INSERT INTO languages_iso639 VALUES ('tqo', '   ', '   ', '  ', 'I', 'L', 'Toaripi', false, '');
INSERT INTO languages_iso639 VALUES ('tqp', '   ', '   ', '  ', 'I', 'L', 'Tomoip', false, '');
INSERT INTO languages_iso639 VALUES ('tqq', '   ', '   ', '  ', 'I', 'L', 'Tunni', false, '');
INSERT INTO languages_iso639 VALUES ('tqr', '   ', '   ', '  ', 'I', 'E', 'Torona', false, '');
INSERT INTO languages_iso639 VALUES ('tqt', '   ', '   ', '  ', 'I', 'L', 'Western Totonac', false, '');
INSERT INTO languages_iso639 VALUES ('tqu', '   ', '   ', '  ', 'I', 'L', 'Touo', false, '');
INSERT INTO languages_iso639 VALUES ('tqw', '   ', '   ', '  ', 'I', 'E', 'Tonkawa', false, '');
INSERT INTO languages_iso639 VALUES ('tra', '   ', '   ', '  ', 'I', 'L', 'Tirahi', false, '');
INSERT INTO languages_iso639 VALUES ('trb', '   ', '   ', '  ', 'I', 'L', 'Terebu', false, '');
INSERT INTO languages_iso639 VALUES ('trc', '   ', '   ', '  ', 'I', 'L', 'Copala Triqui', false, '');
INSERT INTO languages_iso639 VALUES ('trd', '   ', '   ', '  ', 'I', 'L', 'Turi', false, '');
INSERT INTO languages_iso639 VALUES ('tre', '   ', '   ', '  ', 'I', 'L', 'East Tarangan', false, '');
INSERT INTO languages_iso639 VALUES ('trf', '   ', '   ', '  ', 'I', 'L', 'Trinidadian Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('trg', '   ', '   ', '  ', 'I', 'L', 'Lishán Didán', false, '');
INSERT INTO languages_iso639 VALUES ('trh', '   ', '   ', '  ', 'I', 'L', 'Turaka', false, '');
INSERT INTO languages_iso639 VALUES ('tri', '   ', '   ', '  ', 'I', 'L', 'Trió', false, '');
INSERT INTO languages_iso639 VALUES ('trj', '   ', '   ', '  ', 'I', 'L', 'Toram', false, '');
INSERT INTO languages_iso639 VALUES ('trl', '   ', '   ', '  ', 'I', 'L', 'Traveller Scottish', false, '');
INSERT INTO languages_iso639 VALUES ('trm', '   ', '   ', '  ', 'I', 'L', 'Tregami', false, '');
INSERT INTO languages_iso639 VALUES ('trn', '   ', '   ', '  ', 'I', 'L', 'Trinitario', false, '');
INSERT INTO languages_iso639 VALUES ('tro', '   ', '   ', '  ', 'I', 'L', 'Tarao Naga', false, '');
INSERT INTO languages_iso639 VALUES ('trp', '   ', '   ', '  ', 'I', 'L', 'Kok Borok', false, '');
INSERT INTO languages_iso639 VALUES ('trq', '   ', '   ', '  ', 'I', 'L', 'San Martín Itunyoso Triqui', false, '');
INSERT INTO languages_iso639 VALUES ('trr', '   ', '   ', '  ', 'I', 'L', 'Taushiro', false, '');
INSERT INTO languages_iso639 VALUES ('trs', '   ', '   ', '  ', 'I', 'L', 'Chicahuaxtla Triqui', false, '');
INSERT INTO languages_iso639 VALUES ('trt', '   ', '   ', '  ', 'I', 'L', 'Tunggare', false, '');
INSERT INTO languages_iso639 VALUES ('tru', '   ', '   ', '  ', 'I', 'L', 'Turoyo', false, '');
INSERT INTO languages_iso639 VALUES ('trv', '   ', '   ', '  ', 'I', 'L', 'Taroko', false, '');
INSERT INTO languages_iso639 VALUES ('trw', '   ', '   ', '  ', 'I', 'L', 'Torwali', false, '');
INSERT INTO languages_iso639 VALUES ('trx', '   ', '   ', '  ', 'I', 'L', 'Tringgus-Sembaan Bidayuh', false, '');
INSERT INTO languages_iso639 VALUES ('try', '   ', '   ', '  ', 'I', 'E', 'Turung', false, '');
INSERT INTO languages_iso639 VALUES ('trz', '   ', '   ', '  ', 'I', 'E', 'Torá', false, '');
INSERT INTO languages_iso639 VALUES ('tsa', '   ', '   ', '  ', 'I', 'L', 'Tsaangi', false, '');
INSERT INTO languages_iso639 VALUES ('tsb', '   ', '   ', '  ', 'I', 'L', 'Tsamai', false, '');
INSERT INTO languages_iso639 VALUES ('tsc', '   ', '   ', '  ', 'I', 'L', 'Tswa', false, '');
INSERT INTO languages_iso639 VALUES ('tsd', '   ', '   ', '  ', 'I', 'L', 'Tsakonian', false, '');
INSERT INTO languages_iso639 VALUES ('tse', '   ', '   ', '  ', 'I', 'L', 'Tunisian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('tsf', '   ', '   ', '  ', 'I', 'L', 'Southwestern Tamang', false, '');
INSERT INTO languages_iso639 VALUES ('tsg', '   ', '   ', '  ', 'I', 'L', 'Tausug', false, '');
INSERT INTO languages_iso639 VALUES ('tsh', '   ', '   ', '  ', 'I', 'L', 'Tsuvan', false, '');
INSERT INTO languages_iso639 VALUES ('tsi', 'tsi', 'tsi', '  ', 'I', 'L', 'Tsimshian', false, '');
INSERT INTO languages_iso639 VALUES ('tsj', '   ', '   ', '  ', 'I', 'L', 'Tshangla', false, '');
INSERT INTO languages_iso639 VALUES ('tsk', '   ', '   ', '  ', 'I', 'L', 'Tseku', false, '');
INSERT INTO languages_iso639 VALUES ('tsl', '   ', '   ', '  ', 'I', 'L', 'Ts''ün-Lao', false, '');
INSERT INTO languages_iso639 VALUES ('tsm', '   ', '   ', '  ', 'I', 'L', 'Turkish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('tsn', 'tsn', 'tsn', 'tn', 'I', 'L', 'Tswana', false, '');
INSERT INTO languages_iso639 VALUES ('tso', 'tso', 'tso', 'ts', 'I', 'L', 'Tsonga', false, '');
INSERT INTO languages_iso639 VALUES ('tsp', '   ', '   ', '  ', 'I', 'L', 'Northern Toussian', false, '');
INSERT INTO languages_iso639 VALUES ('tsq', '   ', '   ', '  ', 'I', 'L', 'Thai Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('tsr', '   ', '   ', '  ', 'I', 'L', 'Akei', false, '');
INSERT INTO languages_iso639 VALUES ('tss', '   ', '   ', '  ', 'I', 'L', 'Taiwan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('tst', '   ', '   ', '  ', 'I', 'L', 'Tondi Songway Kiini', false, '');
INSERT INTO languages_iso639 VALUES ('tsu', '   ', '   ', '  ', 'I', 'L', 'Tsou', false, '');
INSERT INTO languages_iso639 VALUES ('tsv', '   ', '   ', '  ', 'I', 'L', 'Tsogo', false, '');
INSERT INTO languages_iso639 VALUES ('tsw', '   ', '   ', '  ', 'I', 'L', 'Tsishingini', false, '');
INSERT INTO languages_iso639 VALUES ('tsx', '   ', '   ', '  ', 'I', 'L', 'Mubami', false, '');
INSERT INTO languages_iso639 VALUES ('tsy', '   ', '   ', '  ', 'I', 'L', 'Tebul Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('tsz', '   ', '   ', '  ', 'I', 'L', 'Purepecha', false, '');
INSERT INTO languages_iso639 VALUES ('tta', '   ', '   ', '  ', 'I', 'E', 'Tutelo', false, '');
INSERT INTO languages_iso639 VALUES ('ttb', '   ', '   ', '  ', 'I', 'L', 'Gaa', false, '');
INSERT INTO languages_iso639 VALUES ('ttc', '   ', '   ', '  ', 'I', 'L', 'Tektiteko', false, '');
INSERT INTO languages_iso639 VALUES ('ttd', '   ', '   ', '  ', 'I', 'L', 'Tauade', false, '');
INSERT INTO languages_iso639 VALUES ('tte', '   ', '   ', '  ', 'I', 'L', 'Bwanabwana', false, '');
INSERT INTO languages_iso639 VALUES ('ttf', '   ', '   ', '  ', 'I', 'L', 'Tuotomb', false, '');
INSERT INTO languages_iso639 VALUES ('ttg', '   ', '   ', '  ', 'I', 'L', 'Tutong', false, '');
INSERT INTO languages_iso639 VALUES ('tth', '   ', '   ', '  ', 'I', 'L', 'Upper Ta''oih', false, '');
INSERT INTO languages_iso639 VALUES ('tti', '   ', '   ', '  ', 'I', 'L', 'Tobati', false, '');
INSERT INTO languages_iso639 VALUES ('ttj', '   ', '   ', '  ', 'I', 'L', 'Tooro', false, '');
INSERT INTO languages_iso639 VALUES ('ttk', '   ', '   ', '  ', 'I', 'L', 'Totoro', false, '');
INSERT INTO languages_iso639 VALUES ('ttl', '   ', '   ', '  ', 'I', 'L', 'Totela', false, '');
INSERT INTO languages_iso639 VALUES ('ttm', '   ', '   ', '  ', 'I', 'L', 'Northern Tutchone', false, '');
INSERT INTO languages_iso639 VALUES ('ttn', '   ', '   ', '  ', 'I', 'L', 'Towei', false, '');
INSERT INTO languages_iso639 VALUES ('tto', '   ', '   ', '  ', 'I', 'L', 'Lower Ta''oih', false, '');
INSERT INTO languages_iso639 VALUES ('ttp', '   ', '   ', '  ', 'I', 'L', 'Tombelala', false, '');
INSERT INTO languages_iso639 VALUES ('ttq', '   ', '   ', '  ', 'I', 'L', 'Tawallammat Tamajaq', false, '');
INSERT INTO languages_iso639 VALUES ('ttr', '   ', '   ', '  ', 'I', 'L', 'Tera', false, '');
INSERT INTO languages_iso639 VALUES ('tts', '   ', '   ', '  ', 'I', 'L', 'Northeastern Thai', false, '');
INSERT INTO languages_iso639 VALUES ('ttt', '   ', '   ', '  ', 'I', 'L', 'Muslim Tat', false, '');
INSERT INTO languages_iso639 VALUES ('ttu', '   ', '   ', '  ', 'I', 'L', 'Torau', false, '');
INSERT INTO languages_iso639 VALUES ('ttv', '   ', '   ', '  ', 'I', 'L', 'Titan', false, '');
INSERT INTO languages_iso639 VALUES ('ttw', '   ', '   ', '  ', 'I', 'L', 'Long Wat', false, '');
INSERT INTO languages_iso639 VALUES ('tty', '   ', '   ', '  ', 'I', 'L', 'Sikaritai', false, '');
INSERT INTO languages_iso639 VALUES ('ttz', '   ', '   ', '  ', 'I', 'L', 'Tsum', false, '');
INSERT INTO languages_iso639 VALUES ('tua', '   ', '   ', '  ', 'I', 'L', 'Wiarumus', false, '');
INSERT INTO languages_iso639 VALUES ('tub', '   ', '   ', '  ', 'I', 'L', 'Tübatulabal', false, '');
INSERT INTO languages_iso639 VALUES ('tuc', '   ', '   ', '  ', 'I', 'L', 'Mutu', false, '');
INSERT INTO languages_iso639 VALUES ('tud', '   ', '   ', '  ', 'I', 'E', 'Tuxá', false, '');
INSERT INTO languages_iso639 VALUES ('tue', '   ', '   ', '  ', 'I', 'L', 'Tuyuca', false, '');
INSERT INTO languages_iso639 VALUES ('tuf', '   ', '   ', '  ', 'I', 'L', 'Central Tunebo', false, '');
INSERT INTO languages_iso639 VALUES ('tug', '   ', '   ', '  ', 'I', 'L', 'Tunia', false, '');
INSERT INTO languages_iso639 VALUES ('tuh', '   ', '   ', '  ', 'I', 'L', 'Taulil', false, '');
INSERT INTO languages_iso639 VALUES ('tui', '   ', '   ', '  ', 'I', 'L', 'Tupuri', false, '');
INSERT INTO languages_iso639 VALUES ('tuj', '   ', '   ', '  ', 'I', 'L', 'Tugutil', false, '');
INSERT INTO languages_iso639 VALUES ('tuk', 'tuk', 'tuk', 'tk', 'I', 'L', 'Turkmen', false, '');
INSERT INTO languages_iso639 VALUES ('tul', '   ', '   ', '  ', 'I', 'L', 'Tula', false, '');
INSERT INTO languages_iso639 VALUES ('tum', 'tum', 'tum', '  ', 'I', 'L', 'Tumbuka', false, '');
INSERT INTO languages_iso639 VALUES ('tun', '   ', '   ', '  ', 'I', 'E', 'Tunica', false, '');
INSERT INTO languages_iso639 VALUES ('tuo', '   ', '   ', '  ', 'I', 'L', 'Tucano', false, '');
INSERT INTO languages_iso639 VALUES ('tuq', '   ', '   ', '  ', 'I', 'L', 'Tedaga', false, '');
INSERT INTO languages_iso639 VALUES ('tur', 'tur', 'tur', 'tr', 'I', 'L', 'Turkish', false, '');
INSERT INTO languages_iso639 VALUES ('tus', '   ', '   ', '  ', 'I', 'L', 'Tuscarora', false, '');
INSERT INTO languages_iso639 VALUES ('tuu', '   ', '   ', '  ', 'I', 'L', 'Tututni', false, '');
INSERT INTO languages_iso639 VALUES ('tuv', '   ', '   ', '  ', 'I', 'L', 'Turkana', false, '');
INSERT INTO languages_iso639 VALUES ('tux', '   ', '   ', '  ', 'I', 'E', 'Tuxináwa', false, '');
INSERT INTO languages_iso639 VALUES ('tuy', '   ', '   ', '  ', 'I', 'L', 'Tugen', false, '');
INSERT INTO languages_iso639 VALUES ('tuz', '   ', '   ', '  ', 'I', 'L', 'Turka', false, '');
INSERT INTO languages_iso639 VALUES ('tva', '   ', '   ', '  ', 'I', 'L', 'Vaghua', false, '');
INSERT INTO languages_iso639 VALUES ('tvd', '   ', '   ', '  ', 'I', 'L', 'Tsuvadi', false, '');
INSERT INTO languages_iso639 VALUES ('tve', '   ', '   ', '  ', 'I', 'L', 'Te''un', false, '');
INSERT INTO languages_iso639 VALUES ('tvk', '   ', '   ', '  ', 'I', 'L', 'Southeast Ambrym', false, '');
INSERT INTO languages_iso639 VALUES ('tvl', 'tvl', 'tvl', '  ', 'I', 'L', 'Tuvalu', false, '');
INSERT INTO languages_iso639 VALUES ('tvm', '   ', '   ', '  ', 'I', 'L', 'Tela-Masbuar', false, '');
INSERT INTO languages_iso639 VALUES ('tvn', '   ', '   ', '  ', 'I', 'L', 'Tavoyan', false, '');
INSERT INTO languages_iso639 VALUES ('tvo', '   ', '   ', '  ', 'I', 'L', 'Tidore', false, '');
INSERT INTO languages_iso639 VALUES ('tvs', '   ', '   ', '  ', 'I', 'L', 'Taveta', false, '');
INSERT INTO languages_iso639 VALUES ('tvt', '   ', '   ', '  ', 'I', 'L', 'Tutsa Naga', false, '');
INSERT INTO languages_iso639 VALUES ('tvu', '   ', '   ', '  ', 'I', 'L', 'Tunen', false, '');
INSERT INTO languages_iso639 VALUES ('tvw', '   ', '   ', '  ', 'I', 'L', 'Sedoa', false, '');
INSERT INTO languages_iso639 VALUES ('tvy', '   ', '   ', '  ', 'I', 'E', 'Timor Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('twa', '   ', '   ', '  ', 'I', 'E', 'Twana', false, '');
INSERT INTO languages_iso639 VALUES ('twb', '   ', '   ', '  ', 'I', 'L', 'Western Tawbuid', false, '');
INSERT INTO languages_iso639 VALUES ('twc', '   ', '   ', '  ', 'I', 'E', 'Teshenawa', false, '');
INSERT INTO languages_iso639 VALUES ('twd', '   ', '   ', '  ', 'I', 'L', 'Twents', false, '');
INSERT INTO languages_iso639 VALUES ('twe', '   ', '   ', '  ', 'I', 'L', 'Tewa (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('twf', '   ', '   ', '  ', 'I', 'L', 'Northern Tiwa', false, '');
INSERT INTO languages_iso639 VALUES ('twg', '   ', '   ', '  ', 'I', 'L', 'Tereweng', false, '');
INSERT INTO languages_iso639 VALUES ('twh', '   ', '   ', '  ', 'I', 'L', 'Tai Dón', false, '');
INSERT INTO languages_iso639 VALUES ('twi', 'twi', 'twi', 'tw', 'I', 'L', 'Twi', false, '');
INSERT INTO languages_iso639 VALUES ('twl', '   ', '   ', '  ', 'I', 'L', 'Tawara', false, '');
INSERT INTO languages_iso639 VALUES ('twm', '   ', '   ', '  ', 'I', 'L', 'Tawang Monpa', false, '');
INSERT INTO languages_iso639 VALUES ('twn', '   ', '   ', '  ', 'I', 'L', 'Twendi', false, '');
INSERT INTO languages_iso639 VALUES ('two', '   ', '   ', '  ', 'I', 'L', 'Tswapong', false, '');
INSERT INTO languages_iso639 VALUES ('twp', '   ', '   ', '  ', 'I', 'L', 'Ere', false, '');
INSERT INTO languages_iso639 VALUES ('twq', '   ', '   ', '  ', 'I', 'L', 'Tasawaq', false, '');
INSERT INTO languages_iso639 VALUES ('twr', '   ', '   ', '  ', 'I', 'L', 'Southwestern Tarahumara', false, '');
INSERT INTO languages_iso639 VALUES ('twt', '   ', '   ', '  ', 'I', 'E', 'Turiwára', false, '');
INSERT INTO languages_iso639 VALUES ('twu', '   ', '   ', '  ', 'I', 'L', 'Termanu', false, '');
INSERT INTO languages_iso639 VALUES ('tww', '   ', '   ', '  ', 'I', 'L', 'Tuwari', false, '');
INSERT INTO languages_iso639 VALUES ('twx', '   ', '   ', '  ', 'I', 'L', 'Tewe', false, '');
INSERT INTO languages_iso639 VALUES ('twy', '   ', '   ', '  ', 'I', 'L', 'Tawoyan', false, '');
INSERT INTO languages_iso639 VALUES ('txa', '   ', '   ', '  ', 'I', 'L', 'Tombonuo', false, '');
INSERT INTO languages_iso639 VALUES ('txb', '   ', '   ', '  ', 'I', 'A', 'Tokharian B', false, '');
INSERT INTO languages_iso639 VALUES ('txc', '   ', '   ', '  ', 'I', 'E', 'Tsetsaut', false, '');
INSERT INTO languages_iso639 VALUES ('txe', '   ', '   ', '  ', 'I', 'L', 'Totoli', false, '');
INSERT INTO languages_iso639 VALUES ('txg', '   ', '   ', '  ', 'I', 'A', 'Tangut', false, '');
INSERT INTO languages_iso639 VALUES ('txh', '   ', '   ', '  ', 'I', 'A', 'Thracian', false, '');
INSERT INTO languages_iso639 VALUES ('txi', '   ', '   ', '  ', 'I', 'L', 'Ikpeng', false, '');
INSERT INTO languages_iso639 VALUES ('txm', '   ', '   ', '  ', 'I', 'L', 'Tomini', false, '');
INSERT INTO languages_iso639 VALUES ('txn', '   ', '   ', '  ', 'I', 'L', 'West Tarangan', false, '');
INSERT INTO languages_iso639 VALUES ('txo', '   ', '   ', '  ', 'I', 'L', 'Toto', false, '');
INSERT INTO languages_iso639 VALUES ('txq', '   ', '   ', '  ', 'I', 'L', 'Tii', false, '');
INSERT INTO languages_iso639 VALUES ('txr', '   ', '   ', '  ', 'I', 'A', 'Tartessian', false, '');
INSERT INTO languages_iso639 VALUES ('txs', '   ', '   ', '  ', 'I', 'L', 'Tonsea', false, '');
INSERT INTO languages_iso639 VALUES ('txt', '   ', '   ', '  ', 'I', 'L', 'Citak', false, '');
INSERT INTO languages_iso639 VALUES ('txu', '   ', '   ', '  ', 'I', 'L', 'Kayapó', false, '');
INSERT INTO languages_iso639 VALUES ('txx', '   ', '   ', '  ', 'I', 'L', 'Tatana', false, '');
INSERT INTO languages_iso639 VALUES ('txy', '   ', '   ', '  ', 'I', 'L', 'Tanosy Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('tya', '   ', '   ', '  ', 'I', 'L', 'Tauya', false, '');
INSERT INTO languages_iso639 VALUES ('tye', '   ', '   ', '  ', 'I', 'L', 'Kyanga', false, '');
INSERT INTO languages_iso639 VALUES ('tyh', '   ', '   ', '  ', 'I', 'L', 'O''du', false, '');
INSERT INTO languages_iso639 VALUES ('tyi', '   ', '   ', '  ', 'I', 'L', 'Teke-Tsaayi', false, '');
INSERT INTO languages_iso639 VALUES ('tyj', '   ', '   ', '  ', 'I', 'L', 'Tai Do', false, '');
INSERT INTO languages_iso639 VALUES ('tyl', '   ', '   ', '  ', 'I', 'L', 'Thu Lao', false, '');
INSERT INTO languages_iso639 VALUES ('tyn', '   ', '   ', '  ', 'I', 'L', 'Kombai', false, '');
INSERT INTO languages_iso639 VALUES ('typ', '   ', '   ', '  ', 'I', 'E', 'Thaypan', false, '');
INSERT INTO languages_iso639 VALUES ('tyr', '   ', '   ', '  ', 'I', 'L', 'Tai Daeng', false, '');
INSERT INTO languages_iso639 VALUES ('tys', '   ', '   ', '  ', 'I', 'L', 'Tày Sa Pa', false, '');
INSERT INTO languages_iso639 VALUES ('tyt', '   ', '   ', '  ', 'I', 'L', 'Tày Tac', false, '');
INSERT INTO languages_iso639 VALUES ('tyu', '   ', '   ', '  ', 'I', 'L', 'Kua', false, '');
INSERT INTO languages_iso639 VALUES ('tyv', 'tyv', 'tyv', '  ', 'I', 'L', 'Tuvinian', false, '');
INSERT INTO languages_iso639 VALUES ('tyx', '   ', '   ', '  ', 'I', 'L', 'Teke-Tyee', false, '');
INSERT INTO languages_iso639 VALUES ('tyz', '   ', '   ', '  ', 'I', 'L', 'Tày', false, '');
INSERT INTO languages_iso639 VALUES ('tza', '   ', '   ', '  ', 'I', 'L', 'Tanzanian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('tzh', '   ', '   ', '  ', 'I', 'L', 'Tzeltal', false, '');
INSERT INTO languages_iso639 VALUES ('tzj', '   ', '   ', '  ', 'I', 'L', 'Tz''utujil', false, '');
INSERT INTO languages_iso639 VALUES ('tzl', '   ', '   ', '  ', 'I', 'C', 'Talossan', false, '');
INSERT INTO languages_iso639 VALUES ('tzm', '   ', '   ', '  ', 'I', 'L', 'Central Atlas Tamazight', false, '');
INSERT INTO languages_iso639 VALUES ('tzn', '   ', '   ', '  ', 'I', 'L', 'Tugun', false, '');
INSERT INTO languages_iso639 VALUES ('tzo', '   ', '   ', '  ', 'I', 'L', 'Tzotzil', false, '');
INSERT INTO languages_iso639 VALUES ('tzx', '   ', '   ', '  ', 'I', 'L', 'Tabriak', false, '');
INSERT INTO languages_iso639 VALUES ('uam', '   ', '   ', '  ', 'I', 'E', 'Uamué', false, '');
INSERT INTO languages_iso639 VALUES ('uan', '   ', '   ', '  ', 'I', 'L', 'Kuan', false, '');
INSERT INTO languages_iso639 VALUES ('uar', '   ', '   ', '  ', 'I', 'L', 'Tairuma', false, '');
INSERT INTO languages_iso639 VALUES ('uba', '   ', '   ', '  ', 'I', 'L', 'Ubang', false, '');
INSERT INTO languages_iso639 VALUES ('ubi', '   ', '   ', '  ', 'I', 'L', 'Ubi', false, '');
INSERT INTO languages_iso639 VALUES ('ubl', '   ', '   ', '  ', 'I', 'L', 'Buhi''non Bikol', false, '');
INSERT INTO languages_iso639 VALUES ('ubr', '   ', '   ', '  ', 'I', 'L', 'Ubir', false, '');
INSERT INTO languages_iso639 VALUES ('ubu', '   ', '   ', '  ', 'I', 'L', 'Umbu-Ungu', false, '');
INSERT INTO languages_iso639 VALUES ('uby', '   ', '   ', '  ', 'I', 'E', 'Ubykh', false, '');
INSERT INTO languages_iso639 VALUES ('uda', '   ', '   ', '  ', 'I', 'L', 'Uda', false, '');
INSERT INTO languages_iso639 VALUES ('ude', '   ', '   ', '  ', 'I', 'L', 'Udihe', false, '');
INSERT INTO languages_iso639 VALUES ('udg', '   ', '   ', '  ', 'I', 'L', 'Muduga', false, '');
INSERT INTO languages_iso639 VALUES ('udi', '   ', '   ', '  ', 'I', 'L', 'Udi', false, '');
INSERT INTO languages_iso639 VALUES ('udj', '   ', '   ', '  ', 'I', 'L', 'Ujir', false, '');
INSERT INTO languages_iso639 VALUES ('udl', '   ', '   ', '  ', 'I', 'L', 'Wuzlam', false, '');
INSERT INTO languages_iso639 VALUES ('udm', 'udm', 'udm', '  ', 'I', 'L', 'Udmurt', false, '');
INSERT INTO languages_iso639 VALUES ('udu', '   ', '   ', '  ', 'I', 'L', 'Uduk', false, '');
INSERT INTO languages_iso639 VALUES ('ues', '   ', '   ', '  ', 'I', 'L', 'Kioko', false, '');
INSERT INTO languages_iso639 VALUES ('ufi', '   ', '   ', '  ', 'I', 'L', 'Ufim', false, '');
INSERT INTO languages_iso639 VALUES ('uga', 'uga', 'uga', '  ', 'I', 'A', 'Ugaritic', false, '');
INSERT INTO languages_iso639 VALUES ('ugb', '   ', '   ', '  ', 'I', 'E', 'Kuku-Ugbanh', false, '');
INSERT INTO languages_iso639 VALUES ('uge', '   ', '   ', '  ', 'I', 'L', 'Ughele', false, '');
INSERT INTO languages_iso639 VALUES ('ugn', '   ', '   ', '  ', 'I', 'L', 'Ugandan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ugo', '   ', '   ', '  ', 'I', 'L', 'Ugong', false, '');
INSERT INTO languages_iso639 VALUES ('ugy', '   ', '   ', '  ', 'I', 'L', 'Uruguayan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('uha', '   ', '   ', '  ', 'I', 'L', 'Uhami', false, '');
INSERT INTO languages_iso639 VALUES ('uhn', '   ', '   ', '  ', 'I', 'L', 'Damal', false, '');
INSERT INTO languages_iso639 VALUES ('uig', 'uig', 'uig', 'ug', 'I', 'L', 'Uighur', false, '');
INSERT INTO languages_iso639 VALUES ('uis', '   ', '   ', '  ', 'I', 'L', 'Uisai', false, '');
INSERT INTO languages_iso639 VALUES ('uiv', '   ', '   ', '  ', 'I', 'L', 'Iyive', false, '');
INSERT INTO languages_iso639 VALUES ('uji', '   ', '   ', '  ', 'I', 'L', 'Tanjijili', false, '');
INSERT INTO languages_iso639 VALUES ('uka', '   ', '   ', '  ', 'I', 'L', 'Kaburi', false, '');
INSERT INTO languages_iso639 VALUES ('ukg', '   ', '   ', '  ', 'I', 'L', 'Ukuriguma', false, '');
INSERT INTO languages_iso639 VALUES ('ukh', '   ', '   ', '  ', 'I', 'L', 'Ukhwejo', false, '');
INSERT INTO languages_iso639 VALUES ('ukl', '   ', '   ', '  ', 'I', 'L', 'Ukrainian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ukp', '   ', '   ', '  ', 'I', 'L', 'Ukpe-Bayobiri', false, '');
INSERT INTO languages_iso639 VALUES ('ukq', '   ', '   ', '  ', 'I', 'L', 'Ukwa', false, '');
INSERT INTO languages_iso639 VALUES ('ukr', 'ukr', 'ukr', 'uk', 'I', 'L', 'Ukrainian', false, '');
INSERT INTO languages_iso639 VALUES ('uks', '   ', '   ', '  ', 'I', 'L', 'Urubú-Kaapor Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('uku', '   ', '   ', '  ', 'I', 'L', 'Ukue', false, '');
INSERT INTO languages_iso639 VALUES ('ukw', '   ', '   ', '  ', 'I', 'L', 'Ukwuani-Aboh-Ndoni', false, '');
INSERT INTO languages_iso639 VALUES ('uky', '   ', '   ', '  ', 'I', 'E', 'Kuuk-Yak', false, '');
INSERT INTO languages_iso639 VALUES ('ula', '   ', '   ', '  ', 'I', 'L', 'Fungwa', false, '');
INSERT INTO languages_iso639 VALUES ('ulb', '   ', '   ', '  ', 'I', 'L', 'Ulukwumi', false, '');
INSERT INTO languages_iso639 VALUES ('ulc', '   ', '   ', '  ', 'I', 'L', 'Ulch', false, '');
INSERT INTO languages_iso639 VALUES ('ule', '   ', '   ', '  ', 'I', 'E', 'Lule', false, '');
INSERT INTO languages_iso639 VALUES ('ulf', '   ', '   ', '  ', 'I', 'L', 'Usku', false, '');
INSERT INTO languages_iso639 VALUES ('uli', '   ', '   ', '  ', 'I', 'L', 'Ulithian', false, '');
INSERT INTO languages_iso639 VALUES ('ulk', '   ', '   ', '  ', 'I', 'L', 'Meriam', false, '');
INSERT INTO languages_iso639 VALUES ('ull', '   ', '   ', '  ', 'I', 'L', 'Ullatan', false, '');
INSERT INTO languages_iso639 VALUES ('ulm', '   ', '   ', '  ', 'I', 'L', 'Ulumanda''', false, '');
INSERT INTO languages_iso639 VALUES ('uln', '   ', '   ', '  ', 'I', 'L', 'Unserdeutsch', false, '');
INSERT INTO languages_iso639 VALUES ('ulu', '   ', '   ', '  ', 'I', 'L', 'Uma'' Lung', false, '');
INSERT INTO languages_iso639 VALUES ('ulw', '   ', '   ', '  ', 'I', 'L', 'Ulwa', false, '');
INSERT INTO languages_iso639 VALUES ('uma', '   ', '   ', '  ', 'I', 'L', 'Umatilla', false, '');
INSERT INTO languages_iso639 VALUES ('umb', 'umb', 'umb', '  ', 'I', 'L', 'Umbundu', false, '');
INSERT INTO languages_iso639 VALUES ('umc', '   ', '   ', '  ', 'I', 'A', 'Marrucinian', false, '');
INSERT INTO languages_iso639 VALUES ('umd', '   ', '   ', '  ', 'I', 'E', 'Umbindhamu', false, '');
INSERT INTO languages_iso639 VALUES ('umg', '   ', '   ', '  ', 'I', 'E', 'Umbuygamu', false, '');
INSERT INTO languages_iso639 VALUES ('umi', '   ', '   ', '  ', 'I', 'L', 'Ukit', false, '');
INSERT INTO languages_iso639 VALUES ('umm', '   ', '   ', '  ', 'I', 'L', 'Umon', false, '');
INSERT INTO languages_iso639 VALUES ('umn', '   ', '   ', '  ', 'I', 'L', 'Makyan Naga', false, '');
INSERT INTO languages_iso639 VALUES ('umo', '   ', '   ', '  ', 'I', 'E', 'Umotína', false, '');
INSERT INTO languages_iso639 VALUES ('ump', '   ', '   ', '  ', 'I', 'L', 'Umpila', false, '');
INSERT INTO languages_iso639 VALUES ('umr', '   ', '   ', '  ', 'I', 'E', 'Umbugarla', false, '');
INSERT INTO languages_iso639 VALUES ('ums', '   ', '   ', '  ', 'I', 'L', 'Pendau', false, '');
INSERT INTO languages_iso639 VALUES ('umu', '   ', '   ', '  ', 'I', 'L', 'Munsee', false, '');
INSERT INTO languages_iso639 VALUES ('una', '   ', '   ', '  ', 'I', 'L', 'North Watut', false, '');
INSERT INTO languages_iso639 VALUES ('und', 'und', 'und', '  ', 'S', 'S', 'Undetermined', false, '');
INSERT INTO languages_iso639 VALUES ('une', '   ', '   ', '  ', 'I', 'L', 'Uneme', false, '');
INSERT INTO languages_iso639 VALUES ('ung', '   ', '   ', '  ', 'I', 'L', 'Ngarinyin', false, '');
INSERT INTO languages_iso639 VALUES ('unk', '   ', '   ', '  ', 'I', 'L', 'Enawené-Nawé', false, '');
INSERT INTO languages_iso639 VALUES ('unm', '   ', '   ', '  ', 'I', 'E', 'Unami', false, '');
INSERT INTO languages_iso639 VALUES ('unn', '   ', '   ', '  ', 'I', 'L', 'Kurnai', false, '');
INSERT INTO languages_iso639 VALUES ('unr', '   ', '   ', '  ', 'I', 'L', 'Mundari', false, '');
INSERT INTO languages_iso639 VALUES ('unu', '   ', '   ', '  ', 'I', 'L', 'Unubahe', false, '');
INSERT INTO languages_iso639 VALUES ('unx', '   ', '   ', '  ', 'I', 'L', 'Munda', false, '');
INSERT INTO languages_iso639 VALUES ('unz', '   ', '   ', '  ', 'I', 'L', 'Unde Kaili', false, '');
INSERT INTO languages_iso639 VALUES ('uok', '   ', '   ', '  ', 'I', 'L', 'Uokha', false, '');
INSERT INTO languages_iso639 VALUES ('upi', '   ', '   ', '  ', 'I', 'L', 'Umeda', false, '');
INSERT INTO languages_iso639 VALUES ('upv', '   ', '   ', '  ', 'I', 'L', 'Uripiv-Wala-Rano-Atchin', false, '');
INSERT INTO languages_iso639 VALUES ('ura', '   ', '   ', '  ', 'I', 'L', 'Urarina', false, '');
INSERT INTO languages_iso639 VALUES ('urb', '   ', '   ', '  ', 'I', 'L', 'Urubú-Kaapor', false, '');
INSERT INTO languages_iso639 VALUES ('urc', '   ', '   ', '  ', 'I', 'E', 'Urningangg', false, '');
INSERT INTO languages_iso639 VALUES ('urd', 'urd', 'urd', 'ur', 'I', 'L', 'Urdu', false, '');
INSERT INTO languages_iso639 VALUES ('ure', '   ', '   ', '  ', 'I', 'L', 'Uru', false, '');
INSERT INTO languages_iso639 VALUES ('urf', '   ', '   ', '  ', 'I', 'E', 'Uradhi', false, '');
INSERT INTO languages_iso639 VALUES ('urg', '   ', '   ', '  ', 'I', 'L', 'Urigina', false, '');
INSERT INTO languages_iso639 VALUES ('urh', '   ', '   ', '  ', 'I', 'L', 'Urhobo', false, '');
INSERT INTO languages_iso639 VALUES ('uri', '   ', '   ', '  ', 'I', 'L', 'Urim', false, '');
INSERT INTO languages_iso639 VALUES ('urk', '   ', '   ', '  ', 'I', 'L', 'Urak Lawoi''', false, '');
INSERT INTO languages_iso639 VALUES ('url', '   ', '   ', '  ', 'I', 'L', 'Urali', false, '');
INSERT INTO languages_iso639 VALUES ('urm', '   ', '   ', '  ', 'I', 'L', 'Urapmin', false, '');
INSERT INTO languages_iso639 VALUES ('urn', '   ', '   ', '  ', 'I', 'L', 'Uruangnirin', false, '');
INSERT INTO languages_iso639 VALUES ('uro', '   ', '   ', '  ', 'I', 'L', 'Ura (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('urp', '   ', '   ', '  ', 'I', 'L', 'Uru-Pa-In', false, '');
INSERT INTO languages_iso639 VALUES ('urr', '   ', '   ', '  ', 'I', 'L', 'Lehalurup', false, '');
INSERT INTO languages_iso639 VALUES ('urt', '   ', '   ', '  ', 'I', 'L', 'Urat', false, '');
INSERT INTO languages_iso639 VALUES ('uru', '   ', '   ', '  ', 'I', 'E', 'Urumi', false, '');
INSERT INTO languages_iso639 VALUES ('urv', '   ', '   ', '  ', 'I', 'E', 'Uruava', false, '');
INSERT INTO languages_iso639 VALUES ('urw', '   ', '   ', '  ', 'I', 'L', 'Sop', false, '');
INSERT INTO languages_iso639 VALUES ('urx', '   ', '   ', '  ', 'I', 'L', 'Urimo', false, '');
INSERT INTO languages_iso639 VALUES ('ury', '   ', '   ', '  ', 'I', 'L', 'Orya', false, '');
INSERT INTO languages_iso639 VALUES ('urz', '   ', '   ', '  ', 'I', 'L', 'Uru-Eu-Wau-Wau', false, '');
INSERT INTO languages_iso639 VALUES ('usa', '   ', '   ', '  ', 'I', 'L', 'Usarufa', false, '');
INSERT INTO languages_iso639 VALUES ('ush', '   ', '   ', '  ', 'I', 'L', 'Ushojo', false, '');
INSERT INTO languages_iso639 VALUES ('usi', '   ', '   ', '  ', 'I', 'L', 'Usui', false, '');
INSERT INTO languages_iso639 VALUES ('usk', '   ', '   ', '  ', 'I', 'L', 'Usaghade', false, '');
INSERT INTO languages_iso639 VALUES ('usp', '   ', '   ', '  ', 'I', 'L', 'Uspanteco', false, '');
INSERT INTO languages_iso639 VALUES ('usu', '   ', '   ', '  ', 'I', 'L', 'Uya', false, '');
INSERT INTO languages_iso639 VALUES ('uta', '   ', '   ', '  ', 'I', 'L', 'Otank', false, '');
INSERT INTO languages_iso639 VALUES ('ute', '   ', '   ', '  ', 'I', 'L', 'Ute-Southern Paiute', false, '');
INSERT INTO languages_iso639 VALUES ('utp', '   ', '   ', '  ', 'I', 'L', 'Amba (Solomon Islands)', false, '');
INSERT INTO languages_iso639 VALUES ('utr', '   ', '   ', '  ', 'I', 'L', 'Etulo', false, '');
INSERT INTO languages_iso639 VALUES ('utu', '   ', '   ', '  ', 'I', 'L', 'Utu', false, '');
INSERT INTO languages_iso639 VALUES ('uum', '   ', '   ', '  ', 'I', 'L', 'Urum', false, '');
INSERT INTO languages_iso639 VALUES ('uun', '   ', '   ', '  ', 'I', 'L', 'Kulon-Pazeh', false, '');
INSERT INTO languages_iso639 VALUES ('uur', '   ', '   ', '  ', 'I', 'L', 'Ura (Vanuatu)', false, '');
INSERT INTO languages_iso639 VALUES ('uuu', '   ', '   ', '  ', 'I', 'L', 'U', false, '');
INSERT INTO languages_iso639 VALUES ('uve', '   ', '   ', '  ', 'I', 'L', 'West Uvean', false, '');
INSERT INTO languages_iso639 VALUES ('uvh', '   ', '   ', '  ', 'I', 'L', 'Uri', false, '');
INSERT INTO languages_iso639 VALUES ('uvl', '   ', '   ', '  ', 'I', 'L', 'Lote', false, '');
INSERT INTO languages_iso639 VALUES ('uwa', '   ', '   ', '  ', 'I', 'L', 'Kuku-Uwanh', false, '');
INSERT INTO languages_iso639 VALUES ('uya', '   ', '   ', '  ', 'I', 'L', 'Doko-Uyanga', false, '');
INSERT INTO languages_iso639 VALUES ('uzb', 'uzb', 'uzb', 'uz', 'M', 'L', 'Uzbek', false, '');
INSERT INTO languages_iso639 VALUES ('uzn', '   ', '   ', '  ', 'I', 'L', 'Northern Uzbek', false, '');
INSERT INTO languages_iso639 VALUES ('uzs', '   ', '   ', '  ', 'I', 'L', 'Southern Uzbek', false, '');
INSERT INTO languages_iso639 VALUES ('vaa', '   ', '   ', '  ', 'I', 'L', 'Vaagri Booli', false, '');
INSERT INTO languages_iso639 VALUES ('vae', '   ', '   ', '  ', 'I', 'L', 'Vale', false, '');
INSERT INTO languages_iso639 VALUES ('vaf', '   ', '   ', '  ', 'I', 'L', 'Vafsi', false, '');
INSERT INTO languages_iso639 VALUES ('vag', '   ', '   ', '  ', 'I', 'L', 'Vagla', false, '');
INSERT INTO languages_iso639 VALUES ('vah', '   ', '   ', '  ', 'I', 'L', 'Varhadi-Nagpuri', false, '');
INSERT INTO languages_iso639 VALUES ('vai', 'vai', 'vai', '  ', 'I', 'L', 'Vai', false, '');
INSERT INTO languages_iso639 VALUES ('vaj', '   ', '   ', '  ', 'I', 'L', 'Vasekela Bushman', false, '');
INSERT INTO languages_iso639 VALUES ('val', '   ', '   ', '  ', 'I', 'L', 'Vehes', false, '');
INSERT INTO languages_iso639 VALUES ('vam', '   ', '   ', '  ', 'I', 'L', 'Vanimo', false, '');
INSERT INTO languages_iso639 VALUES ('van', '   ', '   ', '  ', 'I', 'L', 'Valman', false, '');
INSERT INTO languages_iso639 VALUES ('vao', '   ', '   ', '  ', 'I', 'L', 'Vao', false, '');
INSERT INTO languages_iso639 VALUES ('vap', '   ', '   ', '  ', 'I', 'L', 'Vaiphei', false, '');
INSERT INTO languages_iso639 VALUES ('var', '   ', '   ', '  ', 'I', 'L', 'Huarijio', false, '');
INSERT INTO languages_iso639 VALUES ('vas', '   ', '   ', '  ', 'I', 'L', 'Vasavi', false, '');
INSERT INTO languages_iso639 VALUES ('vau', '   ', '   ', '  ', 'I', 'L', 'Vanuma', false, '');
INSERT INTO languages_iso639 VALUES ('vav', '   ', '   ', '  ', 'I', 'L', 'Varli', false, '');
INSERT INTO languages_iso639 VALUES ('vay', '   ', '   ', '  ', 'I', 'L', 'Wayu', false, '');
INSERT INTO languages_iso639 VALUES ('vbb', '   ', '   ', '  ', 'I', 'L', 'Southeast Babar', false, '');
INSERT INTO languages_iso639 VALUES ('vbk', '   ', '   ', '  ', 'I', 'L', 'Southwestern Bontok', false, '');
INSERT INTO languages_iso639 VALUES ('vec', '   ', '   ', '  ', 'I', 'L', 'Venetian', false, '');
INSERT INTO languages_iso639 VALUES ('ved', '   ', '   ', '  ', 'I', 'L', 'Veddah', false, '');
INSERT INTO languages_iso639 VALUES ('vel', '   ', '   ', '  ', 'I', 'L', 'Veluws', false, '');
INSERT INTO languages_iso639 VALUES ('vem', '   ', '   ', '  ', 'I', 'L', 'Vemgo-Mabas', false, '');
INSERT INTO languages_iso639 VALUES ('ven', 'ven', 'ven', 've', 'I', 'L', 'Venda', false, '');
INSERT INTO languages_iso639 VALUES ('veo', '   ', '   ', '  ', 'I', 'E', 'Ventureño', false, '');
INSERT INTO languages_iso639 VALUES ('vep', '   ', '   ', '  ', 'I', 'L', 'Veps', false, '');
INSERT INTO languages_iso639 VALUES ('ver', '   ', '   ', '  ', 'I', 'L', 'Mom Jango', false, '');
INSERT INTO languages_iso639 VALUES ('vgr', '   ', '   ', '  ', 'I', 'L', 'Vaghri', false, '');
INSERT INTO languages_iso639 VALUES ('vgt', '   ', '   ', '  ', 'I', 'L', 'Vlaamse Gebarentaal', false, '');
INSERT INTO languages_iso639 VALUES ('vic', '   ', '   ', '  ', 'I', 'L', 'Virgin Islands Creole English', false, '');
INSERT INTO languages_iso639 VALUES ('vid', '   ', '   ', '  ', 'I', 'L', 'Vidunda', false, '');
INSERT INTO languages_iso639 VALUES ('vie', 'vie', 'vie', 'vi', 'I', 'L', 'Vietnamese', false, '');
INSERT INTO languages_iso639 VALUES ('vif', '   ', '   ', '  ', 'I', 'L', 'Vili', false, '');
INSERT INTO languages_iso639 VALUES ('vig', '   ', '   ', '  ', 'I', 'L', 'Viemo', false, '');
INSERT INTO languages_iso639 VALUES ('vil', '   ', '   ', '  ', 'I', 'L', 'Vilela', false, '');
INSERT INTO languages_iso639 VALUES ('vin', '   ', '   ', '  ', 'I', 'L', 'Vinza', false, '');
INSERT INTO languages_iso639 VALUES ('vis', '   ', '   ', '  ', 'I', 'L', 'Vishavan', false, '');
INSERT INTO languages_iso639 VALUES ('vit', '   ', '   ', '  ', 'I', 'L', 'Viti', false, '');
INSERT INTO languages_iso639 VALUES ('viv', '   ', '   ', '  ', 'I', 'L', 'Iduna', false, '');
INSERT INTO languages_iso639 VALUES ('vka', '   ', '   ', '  ', 'I', 'E', 'Kariyarra', false, '');
INSERT INTO languages_iso639 VALUES ('vki', '   ', '   ', '  ', 'I', 'L', 'Ija-Zuba', false, '');
INSERT INTO languages_iso639 VALUES ('vkj', '   ', '   ', '  ', 'I', 'L', 'Kujarge', false, '');
INSERT INTO languages_iso639 VALUES ('vkk', '   ', '   ', '  ', 'I', 'L', 'Kaur', false, '');
INSERT INTO languages_iso639 VALUES ('vkl', '   ', '   ', '  ', 'I', 'L', 'Kulisusu', false, '');
INSERT INTO languages_iso639 VALUES ('vkm', '   ', '   ', '  ', 'I', 'E', 'Kamakan', false, '');
INSERT INTO languages_iso639 VALUES ('vko', '   ', '   ', '  ', 'I', 'L', 'Kodeoha', false, '');
INSERT INTO languages_iso639 VALUES ('vkp', '   ', '   ', '  ', 'I', 'L', 'Korlai Creole Portuguese', false, '');
INSERT INTO languages_iso639 VALUES ('vkt', '   ', '   ', '  ', 'I', 'L', 'Tenggarong Kutai Malay', false, '');
INSERT INTO languages_iso639 VALUES ('vku', '   ', '   ', '  ', 'I', 'L', 'Kurrama', false, '');
INSERT INTO languages_iso639 VALUES ('vlp', '   ', '   ', '  ', 'I', 'L', 'Valpei', false, '');
INSERT INTO languages_iso639 VALUES ('vls', '   ', '   ', '  ', 'I', 'L', 'Vlaams', false, '');
INSERT INTO languages_iso639 VALUES ('vma', '   ', '   ', '  ', 'I', 'L', 'Martuyhunira', false, '');
INSERT INTO languages_iso639 VALUES ('vmb', '   ', '   ', '  ', 'I', 'E', 'Barbaram', false, '');
INSERT INTO languages_iso639 VALUES ('vmc', '   ', '   ', '  ', 'I', 'L', 'Juxtlahuaca Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('vmd', '   ', '   ', '  ', 'I', 'L', 'Mudu Koraga', false, '');
INSERT INTO languages_iso639 VALUES ('vme', '   ', '   ', '  ', 'I', 'L', 'East Masela', false, '');
INSERT INTO languages_iso639 VALUES ('vmf', '   ', '   ', '  ', 'I', 'L', 'Mainfränkisch', false, '');
INSERT INTO languages_iso639 VALUES ('vmg', '   ', '   ', '  ', 'I', 'L', 'Lungalunga', false, '');
INSERT INTO languages_iso639 VALUES ('vmh', '   ', '   ', '  ', 'I', 'L', 'Maraghei', false, '');
INSERT INTO languages_iso639 VALUES ('vmi', '   ', '   ', '  ', 'I', 'E', 'Miwa', false, '');
INSERT INTO languages_iso639 VALUES ('vmj', '   ', '   ', '  ', 'I', 'L', 'Ixtayutla Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('vmk', '   ', '   ', '  ', 'I', 'L', 'Makhuwa-Shirima', false, '');
INSERT INTO languages_iso639 VALUES ('vml', '   ', '   ', '  ', 'I', 'E', 'Malgana', false, '');
INSERT INTO languages_iso639 VALUES ('vmm', '   ', '   ', '  ', 'I', 'L', 'Mitlatongo Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('vmp', '   ', '   ', '  ', 'I', 'L', 'Soyaltepec Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('vmq', '   ', '   ', '  ', 'I', 'L', 'Soyaltepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('vmr', '   ', '   ', '  ', 'I', 'L', 'Marenje', false, '');
INSERT INTO languages_iso639 VALUES ('vms', '   ', '   ', '  ', 'I', 'E', 'Moksela', false, '');
INSERT INTO languages_iso639 VALUES ('vmu', '   ', '   ', '  ', 'I', 'E', 'Muluridyi', false, '');
INSERT INTO languages_iso639 VALUES ('vmv', '   ', '   ', '  ', 'I', 'E', 'Valley Maidu', false, '');
INSERT INTO languages_iso639 VALUES ('vmw', '   ', '   ', '  ', 'I', 'L', 'Makhuwa', false, '');
INSERT INTO languages_iso639 VALUES ('vmx', '   ', '   ', '  ', 'I', 'L', 'Tamazola Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('vmy', '   ', '   ', '  ', 'I', 'L', 'Ayautla Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('vmz', '   ', '   ', '  ', 'I', 'L', 'Mazatlán Mazatec', false, '');
INSERT INTO languages_iso639 VALUES ('vnk', '   ', '   ', '  ', 'I', 'L', 'Vano', false, '');
INSERT INTO languages_iso639 VALUES ('vnm', '   ', '   ', '  ', 'I', 'L', 'Vinmavis', false, '');
INSERT INTO languages_iso639 VALUES ('vnp', '   ', '   ', '  ', 'I', 'L', 'Vunapu', false, '');
INSERT INTO languages_iso639 VALUES ('vol', 'vol', 'vol', 'vo', 'I', 'C', 'Volapük', false, '');
INSERT INTO languages_iso639 VALUES ('vor', '   ', '   ', '  ', 'I', 'L', 'Voro', false, '');
INSERT INTO languages_iso639 VALUES ('vot', 'vot', 'vot', '  ', 'I', 'L', 'Votic', false, '');
INSERT INTO languages_iso639 VALUES ('vra', '   ', '   ', '  ', 'I', 'L', 'Vera''a', false, '');
INSERT INTO languages_iso639 VALUES ('vro', '   ', '   ', '  ', 'I', 'L', 'Võro', false, '');
INSERT INTO languages_iso639 VALUES ('vrs', '   ', '   ', '  ', 'I', 'L', 'Varisi', false, '');
INSERT INTO languages_iso639 VALUES ('vrt', '   ', '   ', '  ', 'I', 'L', 'Burmbar', false, '');
INSERT INTO languages_iso639 VALUES ('vsi', '   ', '   ', '  ', 'I', 'L', 'Moldova Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('vsl', '   ', '   ', '  ', 'I', 'L', 'Venezuelan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('vsv', '   ', '   ', '  ', 'I', 'L', 'Valencian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('vto', '   ', '   ', '  ', 'I', 'L', 'Vitou', false, '');
INSERT INTO languages_iso639 VALUES ('vum', '   ', '   ', '  ', 'I', 'L', 'Vumbu', false, '');
INSERT INTO languages_iso639 VALUES ('vun', '   ', '   ', '  ', 'I', 'L', 'Vunjo', false, '');
INSERT INTO languages_iso639 VALUES ('vut', '   ', '   ', '  ', 'I', 'L', 'Vute', false, '');
INSERT INTO languages_iso639 VALUES ('vwa', '   ', '   ', '  ', 'I', 'L', 'Awa (China)', false, '');
INSERT INTO languages_iso639 VALUES ('waa', '   ', '   ', '  ', 'I', 'L', 'Walla Walla', false, '');
INSERT INTO languages_iso639 VALUES ('wab', '   ', '   ', '  ', 'I', 'L', 'Wab', false, '');
INSERT INTO languages_iso639 VALUES ('wac', '   ', '   ', '  ', 'I', 'L', 'Wasco-Wishram', false, '');
INSERT INTO languages_iso639 VALUES ('wad', '   ', '   ', '  ', 'I', 'L', 'Wandamen', false, '');
INSERT INTO languages_iso639 VALUES ('wae', '   ', '   ', '  ', 'I', 'L', 'Walser', false, '');
INSERT INTO languages_iso639 VALUES ('waf', '   ', '   ', '  ', 'I', 'E', 'Wakoná', false, '');
INSERT INTO languages_iso639 VALUES ('wag', '   ', '   ', '  ', 'I', 'L', 'Wa''ema', false, '');
INSERT INTO languages_iso639 VALUES ('wah', '   ', '   ', '  ', 'I', 'L', 'Watubela', false, '');
INSERT INTO languages_iso639 VALUES ('wai', '   ', '   ', '  ', 'I', 'L', 'Wares', false, '');
INSERT INTO languages_iso639 VALUES ('waj', '   ', '   ', '  ', 'I', 'L', 'Waffa', false, '');
INSERT INTO languages_iso639 VALUES ('wal', 'wal', 'wal', '  ', 'I', 'L', 'Wolaytta', false, '');
INSERT INTO languages_iso639 VALUES ('wam', '   ', '   ', '  ', 'I', 'E', 'Wampanoag', false, '');
INSERT INTO languages_iso639 VALUES ('wan', '   ', '   ', '  ', 'I', 'L', 'Wan', false, '');
INSERT INTO languages_iso639 VALUES ('wao', '   ', '   ', '  ', 'I', 'E', 'Wappo', false, '');
INSERT INTO languages_iso639 VALUES ('wap', '   ', '   ', '  ', 'I', 'L', 'Wapishana', false, '');
INSERT INTO languages_iso639 VALUES ('waq', '   ', '   ', '  ', 'I', 'L', 'Wageman', false, '');
INSERT INTO languages_iso639 VALUES ('war', 'war', 'war', '  ', 'I', 'L', 'Waray (Philippines)', false, '');
INSERT INTO languages_iso639 VALUES ('was', 'was', 'was', '  ', 'I', 'L', 'Washo', false, '');
INSERT INTO languages_iso639 VALUES ('wat', '   ', '   ', '  ', 'I', 'L', 'Kaninuwa', false, '');
INSERT INTO languages_iso639 VALUES ('wau', '   ', '   ', '  ', 'I', 'L', 'Waurá', false, '');
INSERT INTO languages_iso639 VALUES ('wav', '   ', '   ', '  ', 'I', 'L', 'Waka', false, '');
INSERT INTO languages_iso639 VALUES ('waw', '   ', '   ', '  ', 'I', 'L', 'Waiwai', false, '');
INSERT INTO languages_iso639 VALUES ('wax', '   ', '   ', '  ', 'I', 'L', 'Watam', false, '');
INSERT INTO languages_iso639 VALUES ('way', '   ', '   ', '  ', 'I', 'L', 'Wayana', false, '');
INSERT INTO languages_iso639 VALUES ('waz', '   ', '   ', '  ', 'I', 'L', 'Wampur', false, '');
INSERT INTO languages_iso639 VALUES ('wba', '   ', '   ', '  ', 'I', 'L', 'Warao', false, '');
INSERT INTO languages_iso639 VALUES ('wbb', '   ', '   ', '  ', 'I', 'L', 'Wabo', false, '');
INSERT INTO languages_iso639 VALUES ('wbe', '   ', '   ', '  ', 'I', 'L', 'Waritai', false, '');
INSERT INTO languages_iso639 VALUES ('wbf', '   ', '   ', '  ', 'I', 'L', 'Wara', false, '');
INSERT INTO languages_iso639 VALUES ('wbh', '   ', '   ', '  ', 'I', 'L', 'Wanda', false, '');
INSERT INTO languages_iso639 VALUES ('wbi', '   ', '   ', '  ', 'I', 'L', 'Vwanji', false, '');
INSERT INTO languages_iso639 VALUES ('wbj', '   ', '   ', '  ', 'I', 'L', 'Alagwa', false, '');
INSERT INTO languages_iso639 VALUES ('wbk', '   ', '   ', '  ', 'I', 'L', 'Waigali', false, '');
INSERT INTO languages_iso639 VALUES ('wbl', '   ', '   ', '  ', 'I', 'L', 'Wakhi', false, '');
INSERT INTO languages_iso639 VALUES ('wbm', '   ', '   ', '  ', 'I', 'L', 'Wa', false, '');
INSERT INTO languages_iso639 VALUES ('wbp', '   ', '   ', '  ', 'I', 'L', 'Warlpiri', false, '');
INSERT INTO languages_iso639 VALUES ('wbq', '   ', '   ', '  ', 'I', 'L', 'Waddar', false, '');
INSERT INTO languages_iso639 VALUES ('wbr', '   ', '   ', '  ', 'I', 'L', 'Wagdi', false, '');
INSERT INTO languages_iso639 VALUES ('wbt', '   ', '   ', '  ', 'I', 'L', 'Wanman', false, '');
INSERT INTO languages_iso639 VALUES ('wbv', '   ', '   ', '  ', 'I', 'L', 'Wajarri', false, '');
INSERT INTO languages_iso639 VALUES ('wbw', '   ', '   ', '  ', 'I', 'L', 'Woi', false, '');
INSERT INTO languages_iso639 VALUES ('wca', '   ', '   ', '  ', 'I', 'L', 'Yanomámi', false, '');
INSERT INTO languages_iso639 VALUES ('wci', '   ', '   ', '  ', 'I', 'L', 'Waci Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('wdd', '   ', '   ', '  ', 'I', 'L', 'Wandji', false, '');
INSERT INTO languages_iso639 VALUES ('wdg', '   ', '   ', '  ', 'I', 'L', 'Wadaginam', false, '');
INSERT INTO languages_iso639 VALUES ('wdj', '   ', '   ', '  ', 'I', 'L', 'Wadjiginy', false, '');
INSERT INTO languages_iso639 VALUES ('wdk', '   ', '   ', '  ', 'I', 'E', 'Wadikali', false, '');
INSERT INTO languages_iso639 VALUES ('wdu', '   ', '   ', '  ', 'I', 'E', 'Wadjigu', false, '');
INSERT INTO languages_iso639 VALUES ('wdy', '   ', '   ', '  ', 'I', 'E', 'Wadjabangayi', false, '');
INSERT INTO languages_iso639 VALUES ('wea', '   ', '   ', '  ', 'I', 'E', 'Wewaw', false, '');
INSERT INTO languages_iso639 VALUES ('wec', '   ', '   ', '  ', 'I', 'L', 'Wè Western', false, '');
INSERT INTO languages_iso639 VALUES ('wed', '   ', '   ', '  ', 'I', 'L', 'Wedau', false, '');
INSERT INTO languages_iso639 VALUES ('weg', '   ', '   ', '  ', 'I', 'L', 'Wergaia', false, '');
INSERT INTO languages_iso639 VALUES ('weh', '   ', '   ', '  ', 'I', 'L', 'Weh', false, '');
INSERT INTO languages_iso639 VALUES ('wei', '   ', '   ', '  ', 'I', 'L', 'Kiunum', false, '');
INSERT INTO languages_iso639 VALUES ('wem', '   ', '   ', '  ', 'I', 'L', 'Weme Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('weo', '   ', '   ', '  ', 'I', 'L', 'Wemale', false, '');
INSERT INTO languages_iso639 VALUES ('wep', '   ', '   ', '  ', 'I', 'L', 'Westphalien', false, '');
INSERT INTO languages_iso639 VALUES ('wer', '   ', '   ', '  ', 'I', 'L', 'Weri', false, '');
INSERT INTO languages_iso639 VALUES ('wes', '   ', '   ', '  ', 'I', 'L', 'Cameroon Pidgin', false, '');
INSERT INTO languages_iso639 VALUES ('wet', '   ', '   ', '  ', 'I', 'L', 'Perai', false, '');
INSERT INTO languages_iso639 VALUES ('weu', '   ', '   ', '  ', 'I', 'L', 'Rawngtu Chin', false, '');
INSERT INTO languages_iso639 VALUES ('wew', '   ', '   ', '  ', 'I', 'L', 'Wejewa', false, '');
INSERT INTO languages_iso639 VALUES ('wfg', '   ', '   ', '  ', 'I', 'L', 'Yafi', false, '');
INSERT INTO languages_iso639 VALUES ('wga', '   ', '   ', '  ', 'I', 'E', 'Wagaya', false, '');
INSERT INTO languages_iso639 VALUES ('wgb', '   ', '   ', '  ', 'I', 'L', 'Wagawaga', false, '');
INSERT INTO languages_iso639 VALUES ('wgg', '   ', '   ', '  ', 'I', 'E', 'Wangganguru', false, '');
INSERT INTO languages_iso639 VALUES ('wgi', '   ', '   ', '  ', 'I', 'L', 'Wahgi', false, '');
INSERT INTO languages_iso639 VALUES ('wgo', '   ', '   ', '  ', 'I', 'L', 'Waigeo', false, '');
INSERT INTO languages_iso639 VALUES ('wgu', '   ', '   ', '  ', 'I', 'E', 'Wirangu', false, '');
INSERT INTO languages_iso639 VALUES ('wgy', '   ', '   ', '  ', 'I', 'L', 'Warrgamay', false, '');
INSERT INTO languages_iso639 VALUES ('wha', '   ', '   ', '  ', 'I', 'L', 'Manusela', false, '');
INSERT INTO languages_iso639 VALUES ('whg', '   ', '   ', '  ', 'I', 'L', 'North Wahgi', false, '');
INSERT INTO languages_iso639 VALUES ('whk', '   ', '   ', '  ', 'I', 'L', 'Wahau Kenyah', false, '');
INSERT INTO languages_iso639 VALUES ('whu', '   ', '   ', '  ', 'I', 'L', 'Wahau Kayan', false, '');
INSERT INTO languages_iso639 VALUES ('wib', '   ', '   ', '  ', 'I', 'L', 'Southern Toussian', false, '');
INSERT INTO languages_iso639 VALUES ('wic', '   ', '   ', '  ', 'I', 'L', 'Wichita', false, '');
INSERT INTO languages_iso639 VALUES ('wie', '   ', '   ', '  ', 'I', 'E', 'Wik-Epa', false, '');
INSERT INTO languages_iso639 VALUES ('wif', '   ', '   ', '  ', 'I', 'E', 'Wik-Keyangan', false, '');
INSERT INTO languages_iso639 VALUES ('wig', '   ', '   ', '  ', 'I', 'L', 'Wik-Ngathana', false, '');
INSERT INTO languages_iso639 VALUES ('wih', '   ', '   ', '  ', 'I', 'L', 'Wik-Me''anha', false, '');
INSERT INTO languages_iso639 VALUES ('wii', '   ', '   ', '  ', 'I', 'L', 'Minidien', false, '');
INSERT INTO languages_iso639 VALUES ('wij', '   ', '   ', '  ', 'I', 'L', 'Wik-Iiyanh', false, '');
INSERT INTO languages_iso639 VALUES ('wik', '   ', '   ', '  ', 'I', 'L', 'Wikalkan', false, '');
INSERT INTO languages_iso639 VALUES ('wil', '   ', '   ', '  ', 'I', 'E', 'Wilawila', false, '');
INSERT INTO languages_iso639 VALUES ('wim', '   ', '   ', '  ', 'I', 'L', 'Wik-Mungkan', false, '');
INSERT INTO languages_iso639 VALUES ('win', '   ', '   ', '  ', 'I', 'L', 'Ho-Chunk', false, '');
INSERT INTO languages_iso639 VALUES ('wir', '   ', '   ', '  ', 'I', 'E', 'Wiraféd', false, '');
INSERT INTO languages_iso639 VALUES ('wiu', '   ', '   ', '  ', 'I', 'L', 'Wiru', false, '');
INSERT INTO languages_iso639 VALUES ('wiv', '   ', '   ', '  ', 'I', 'L', 'Vitu', false, '');
INSERT INTO languages_iso639 VALUES ('wiy', '   ', '   ', '  ', 'I', 'E', 'Wiyot', false, '');
INSERT INTO languages_iso639 VALUES ('wja', '   ', '   ', '  ', 'I', 'L', 'Waja', false, '');
INSERT INTO languages_iso639 VALUES ('wji', '   ', '   ', '  ', 'I', 'L', 'Warji', false, '');
INSERT INTO languages_iso639 VALUES ('wka', '   ', '   ', '  ', 'I', 'E', 'Kw''adza', false, '');
INSERT INTO languages_iso639 VALUES ('wkb', '   ', '   ', '  ', 'I', 'L', 'Kumbaran', false, '');
INSERT INTO languages_iso639 VALUES ('wkd', '   ', '   ', '  ', 'I', 'L', 'Wakde', false, '');
INSERT INTO languages_iso639 VALUES ('wkl', '   ', '   ', '  ', 'I', 'L', 'Kalanadi', false, '');
INSERT INTO languages_iso639 VALUES ('wku', '   ', '   ', '  ', 'I', 'L', 'Kunduvadi', false, '');
INSERT INTO languages_iso639 VALUES ('wkw', '   ', '   ', '  ', 'I', 'E', 'Wakawaka', false, '');
INSERT INTO languages_iso639 VALUES ('wky', '   ', '   ', '  ', 'I', 'E', 'Wangkayutyuru', false, '');
INSERT INTO languages_iso639 VALUES ('wla', '   ', '   ', '  ', 'I', 'L', 'Walio', false, '');
INSERT INTO languages_iso639 VALUES ('wlc', '   ', '   ', '  ', 'I', 'L', 'Mwali Comorian', false, '');
INSERT INTO languages_iso639 VALUES ('wle', '   ', '   ', '  ', 'I', 'L', 'Wolane', false, '');
INSERT INTO languages_iso639 VALUES ('wlg', '   ', '   ', '  ', 'I', 'L', 'Kunbarlang', false, '');
INSERT INTO languages_iso639 VALUES ('wli', '   ', '   ', '  ', 'I', 'L', 'Waioli', false, '');
INSERT INTO languages_iso639 VALUES ('wlk', '   ', '   ', '  ', 'I', 'E', 'Wailaki', false, '');
INSERT INTO languages_iso639 VALUES ('wll', '   ', '   ', '  ', 'I', 'L', 'Wali (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('wlm', '   ', '   ', '  ', 'I', 'H', 'Middle Welsh', false, '');
INSERT INTO languages_iso639 VALUES ('wln', 'wln', 'wln', 'wa', 'I', 'L', 'Walloon', false, '');
INSERT INTO languages_iso639 VALUES ('wlo', '   ', '   ', '  ', 'I', 'L', 'Wolio', false, '');
INSERT INTO languages_iso639 VALUES ('wlr', '   ', '   ', '  ', 'I', 'L', 'Wailapa', false, '');
INSERT INTO languages_iso639 VALUES ('wls', '   ', '   ', '  ', 'I', 'L', 'Wallisian', false, '');
INSERT INTO languages_iso639 VALUES ('wlu', '   ', '   ', '  ', 'I', 'E', 'Wuliwuli', false, '');
INSERT INTO languages_iso639 VALUES ('wlv', '   ', '   ', '  ', 'I', 'L', 'Wichí Lhamtés Vejoz', false, '');
INSERT INTO languages_iso639 VALUES ('wlw', '   ', '   ', '  ', 'I', 'L', 'Walak', false, '');
INSERT INTO languages_iso639 VALUES ('wlx', '   ', '   ', '  ', 'I', 'L', 'Wali (Ghana)', false, '');
INSERT INTO languages_iso639 VALUES ('wly', '   ', '   ', '  ', 'I', 'E', 'Waling', false, '');
INSERT INTO languages_iso639 VALUES ('wma', '   ', '   ', '  ', 'I', 'E', 'Mawa (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('wmb', '   ', '   ', '  ', 'I', 'L', 'Wambaya', false, '');
INSERT INTO languages_iso639 VALUES ('wmc', '   ', '   ', '  ', 'I', 'L', 'Wamas', false, '');
INSERT INTO languages_iso639 VALUES ('wmd', '   ', '   ', '  ', 'I', 'L', 'Mamaindé', false, '');
INSERT INTO languages_iso639 VALUES ('wme', '   ', '   ', '  ', 'I', 'L', 'Wambule', false, '');
INSERT INTO languages_iso639 VALUES ('wmh', '   ', '   ', '  ', 'I', 'L', 'Waima''a', false, '');
INSERT INTO languages_iso639 VALUES ('wmi', '   ', '   ', '  ', 'I', 'E', 'Wamin', false, '');
INSERT INTO languages_iso639 VALUES ('wmm', '   ', '   ', '  ', 'I', 'L', 'Maiwa (Indonesia)', false, '');
INSERT INTO languages_iso639 VALUES ('wmn', '   ', '   ', '  ', 'I', 'E', 'Waamwang', false, '');
INSERT INTO languages_iso639 VALUES ('wmo', '   ', '   ', '  ', 'I', 'L', 'Wom (Papua New Guinea)', false, '');
INSERT INTO languages_iso639 VALUES ('wms', '   ', '   ', '  ', 'I', 'L', 'Wambon', false, '');
INSERT INTO languages_iso639 VALUES ('wmt', '   ', '   ', '  ', 'I', 'L', 'Walmajarri', false, '');
INSERT INTO languages_iso639 VALUES ('wmw', '   ', '   ', '  ', 'I', 'L', 'Mwani', false, '');
INSERT INTO languages_iso639 VALUES ('wmx', '   ', '   ', '  ', 'I', 'L', 'Womo', false, '');
INSERT INTO languages_iso639 VALUES ('wnb', '   ', '   ', '  ', 'I', 'L', 'Wanambre', false, '');
INSERT INTO languages_iso639 VALUES ('wnc', '   ', '   ', '  ', 'I', 'L', 'Wantoat', false, '');
INSERT INTO languages_iso639 VALUES ('wnd', '   ', '   ', '  ', 'I', 'E', 'Wandarang', false, '');
INSERT INTO languages_iso639 VALUES ('wne', '   ', '   ', '  ', 'I', 'L', 'Waneci', false, '');
INSERT INTO languages_iso639 VALUES ('wng', '   ', '   ', '  ', 'I', 'L', 'Wanggom', false, '');
INSERT INTO languages_iso639 VALUES ('wni', '   ', '   ', '  ', 'I', 'L', 'Ndzwani Comorian', false, '');
INSERT INTO languages_iso639 VALUES ('wnk', '   ', '   ', '  ', 'I', 'L', 'Wanukaka', false, '');
INSERT INTO languages_iso639 VALUES ('wnm', '   ', '   ', '  ', 'I', 'E', 'Wanggamala', false, '');
INSERT INTO languages_iso639 VALUES ('wnn', '   ', '   ', '  ', 'I', 'E', 'Wunumara', false, '');
INSERT INTO languages_iso639 VALUES ('wno', '   ', '   ', '  ', 'I', 'L', 'Wano', false, '');
INSERT INTO languages_iso639 VALUES ('wnp', '   ', '   ', '  ', 'I', 'L', 'Wanap', false, '');
INSERT INTO languages_iso639 VALUES ('wnu', '   ', '   ', '  ', 'I', 'L', 'Usan', false, '');
INSERT INTO languages_iso639 VALUES ('wnw', '   ', '   ', '  ', 'I', 'L', 'Wintu', false, '');
INSERT INTO languages_iso639 VALUES ('wny', '   ', '   ', '  ', 'I', 'L', 'Wanyi', false, '');
INSERT INTO languages_iso639 VALUES ('woa', '   ', '   ', '  ', 'I', 'L', 'Tyaraity', false, '');
INSERT INTO languages_iso639 VALUES ('wob', '   ', '   ', '  ', 'I', 'L', 'Wè Northern', false, '');
INSERT INTO languages_iso639 VALUES ('woc', '   ', '   ', '  ', 'I', 'L', 'Wogeo', false, '');
INSERT INTO languages_iso639 VALUES ('wod', '   ', '   ', '  ', 'I', 'L', 'Wolani', false, '');
INSERT INTO languages_iso639 VALUES ('woe', '   ', '   ', '  ', 'I', 'L', 'Woleaian', false, '');
INSERT INTO languages_iso639 VALUES ('wof', '   ', '   ', '  ', 'I', 'L', 'Gambian Wolof', false, '');
INSERT INTO languages_iso639 VALUES ('wog', '   ', '   ', '  ', 'I', 'L', 'Wogamusin', false, '');
INSERT INTO languages_iso639 VALUES ('woi', '   ', '   ', '  ', 'I', 'L', 'Kamang', false, '');
INSERT INTO languages_iso639 VALUES ('wok', '   ', '   ', '  ', 'I', 'L', 'Longto', false, '');
INSERT INTO languages_iso639 VALUES ('wol', 'wol', 'wol', 'wo', 'I', 'L', 'Wolof', false, '');
INSERT INTO languages_iso639 VALUES ('wom', '   ', '   ', '  ', 'I', 'L', 'Wom (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('won', '   ', '   ', '  ', 'I', 'L', 'Wongo', false, '');
INSERT INTO languages_iso639 VALUES ('woo', '   ', '   ', '  ', 'I', 'L', 'Manombai', false, '');
INSERT INTO languages_iso639 VALUES ('wor', '   ', '   ', '  ', 'I', 'L', 'Woria', false, '');
INSERT INTO languages_iso639 VALUES ('wos', '   ', '   ', '  ', 'I', 'L', 'Hanga Hundi', false, '');
INSERT INTO languages_iso639 VALUES ('wow', '   ', '   ', '  ', 'I', 'L', 'Wawonii', false, '');
INSERT INTO languages_iso639 VALUES ('woy', '   ', '   ', '  ', 'I', 'E', 'Weyto', false, '');
INSERT INTO languages_iso639 VALUES ('wpc', '   ', '   ', '  ', 'I', 'L', 'Maco', false, '');
INSERT INTO languages_iso639 VALUES ('wra', '   ', '   ', '  ', 'I', 'L', 'Warapu', false, '');
INSERT INTO languages_iso639 VALUES ('wrb', '   ', '   ', '  ', 'I', 'E', 'Warluwara', false, '');
INSERT INTO languages_iso639 VALUES ('wrd', '   ', '   ', '  ', 'I', 'L', 'Warduji', false, '');
INSERT INTO languages_iso639 VALUES ('wrg', '   ', '   ', '  ', 'I', 'E', 'Warungu', false, '');
INSERT INTO languages_iso639 VALUES ('wrh', '   ', '   ', '  ', 'I', 'E', 'Wiradhuri', false, '');
INSERT INTO languages_iso639 VALUES ('wri', '   ', '   ', '  ', 'I', 'E', 'Wariyangga', false, '');
INSERT INTO languages_iso639 VALUES ('wrk', '   ', '   ', '  ', 'I', 'L', 'Garrwa', false, '');
INSERT INTO languages_iso639 VALUES ('wrl', '   ', '   ', '  ', 'I', 'L', 'Warlmanpa', false, '');
INSERT INTO languages_iso639 VALUES ('wrm', '   ', '   ', '  ', 'I', 'L', 'Warumungu', false, '');
INSERT INTO languages_iso639 VALUES ('wrn', '   ', '   ', '  ', 'I', 'L', 'Warnang', false, '');
INSERT INTO languages_iso639 VALUES ('wro', '   ', '   ', '  ', 'I', 'E', 'Worrorra', false, '');
INSERT INTO languages_iso639 VALUES ('wrp', '   ', '   ', '  ', 'I', 'L', 'Waropen', false, '');
INSERT INTO languages_iso639 VALUES ('wrr', '   ', '   ', '  ', 'I', 'L', 'Wardaman', false, '');
INSERT INTO languages_iso639 VALUES ('wrs', '   ', '   ', '  ', 'I', 'L', 'Waris', false, '');
INSERT INTO languages_iso639 VALUES ('wru', '   ', '   ', '  ', 'I', 'L', 'Waru', false, '');
INSERT INTO languages_iso639 VALUES ('wrv', '   ', '   ', '  ', 'I', 'L', 'Waruna', false, '');
INSERT INTO languages_iso639 VALUES ('wrw', '   ', '   ', '  ', 'I', 'E', 'Gugu Warra', false, '');
INSERT INTO languages_iso639 VALUES ('wrx', '   ', '   ', '  ', 'I', 'L', 'Wae Rana', false, '');
INSERT INTO languages_iso639 VALUES ('wry', '   ', '   ', '  ', 'I', 'L', 'Merwari', false, '');
INSERT INTO languages_iso639 VALUES ('wrz', '   ', '   ', '  ', 'I', 'E', 'Waray (Australia)', false, '');
INSERT INTO languages_iso639 VALUES ('wsa', '   ', '   ', '  ', 'I', 'L', 'Warembori', false, '');
INSERT INTO languages_iso639 VALUES ('wsi', '   ', '   ', '  ', 'I', 'L', 'Wusi', false, '');
INSERT INTO languages_iso639 VALUES ('wsk', '   ', '   ', '  ', 'I', 'L', 'Waskia', false, '');
INSERT INTO languages_iso639 VALUES ('wsr', '   ', '   ', '  ', 'I', 'L', 'Owenia', false, '');
INSERT INTO languages_iso639 VALUES ('wss', '   ', '   ', '  ', 'I', 'L', 'Wasa', false, '');
INSERT INTO languages_iso639 VALUES ('wsu', '   ', '   ', '  ', 'I', 'E', 'Wasu', false, '');
INSERT INTO languages_iso639 VALUES ('wsv', '   ', '   ', '  ', 'I', 'E', 'Wotapuri-Katarqalai', false, '');
INSERT INTO languages_iso639 VALUES ('wtf', '   ', '   ', '  ', 'I', 'L', 'Watiwa', false, '');
INSERT INTO languages_iso639 VALUES ('wth', '   ', '   ', '  ', 'I', 'E', 'Wathawurrung', false, '');
INSERT INTO languages_iso639 VALUES ('wti', '   ', '   ', '  ', 'I', 'L', 'Berta', false, '');
INSERT INTO languages_iso639 VALUES ('wtk', '   ', '   ', '  ', 'I', 'L', 'Watakataui', false, '');
INSERT INTO languages_iso639 VALUES ('wtm', '   ', '   ', '  ', 'I', 'L', 'Mewati', false, '');
INSERT INTO languages_iso639 VALUES ('wtw', '   ', '   ', '  ', 'I', 'L', 'Wotu', false, '');
INSERT INTO languages_iso639 VALUES ('wua', '   ', '   ', '  ', 'I', 'L', 'Wikngenchera', false, '');
INSERT INTO languages_iso639 VALUES ('wub', '   ', '   ', '  ', 'I', 'L', 'Wunambal', false, '');
INSERT INTO languages_iso639 VALUES ('wud', '   ', '   ', '  ', 'I', 'L', 'Wudu', false, '');
INSERT INTO languages_iso639 VALUES ('wuh', '   ', '   ', '  ', 'I', 'L', 'Wutunhua', false, '');
INSERT INTO languages_iso639 VALUES ('wul', '   ', '   ', '  ', 'I', 'L', 'Silimo', false, '');
INSERT INTO languages_iso639 VALUES ('wum', '   ', '   ', '  ', 'I', 'L', 'Wumbvu', false, '');
INSERT INTO languages_iso639 VALUES ('wun', '   ', '   ', '  ', 'I', 'L', 'Bungu', false, '');
INSERT INTO languages_iso639 VALUES ('wur', '   ', '   ', '  ', 'I', 'E', 'Wurrugu', false, '');
INSERT INTO languages_iso639 VALUES ('wut', '   ', '   ', '  ', 'I', 'L', 'Wutung', false, '');
INSERT INTO languages_iso639 VALUES ('wuu', '   ', '   ', '  ', 'I', 'L', 'Wu Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('wuv', '   ', '   ', '  ', 'I', 'L', 'Wuvulu-Aua', false, '');
INSERT INTO languages_iso639 VALUES ('wux', '   ', '   ', '  ', 'I', 'L', 'Wulna', false, '');
INSERT INTO languages_iso639 VALUES ('wuy', '   ', '   ', '  ', 'I', 'L', 'Wauyai', false, '');
INSERT INTO languages_iso639 VALUES ('wwa', '   ', '   ', '  ', 'I', 'L', 'Waama', false, '');
INSERT INTO languages_iso639 VALUES ('wwb', '   ', '   ', '  ', 'I', 'E', 'Wakabunga', false, '');
INSERT INTO languages_iso639 VALUES ('wwo', '   ', '   ', '  ', 'I', 'L', 'Wetamut', false, '');
INSERT INTO languages_iso639 VALUES ('wwr', '   ', '   ', '  ', 'I', 'E', 'Warrwa', false, '');
INSERT INTO languages_iso639 VALUES ('www', '   ', '   ', '  ', 'I', 'L', 'Wawa', false, '');
INSERT INTO languages_iso639 VALUES ('wxa', '   ', '   ', '  ', 'I', 'L', 'Waxianghua', false, '');
INSERT INTO languages_iso639 VALUES ('wxw', '   ', '   ', '  ', 'I', 'E', 'Wardandi', false, '');
INSERT INTO languages_iso639 VALUES ('wya', '   ', '   ', '  ', 'I', 'L', 'Wyandot', false, '');
INSERT INTO languages_iso639 VALUES ('wyb', '   ', '   ', '  ', 'I', 'L', 'Wangaaybuwan-Ngiyambaa', false, '');
INSERT INTO languages_iso639 VALUES ('wyi', '   ', '   ', '  ', 'I', 'E', 'Woiwurrung', false, '');
INSERT INTO languages_iso639 VALUES ('wym', '   ', '   ', '  ', 'I', 'L', 'Wymysorys', false, '');
INSERT INTO languages_iso639 VALUES ('wyr', '   ', '   ', '  ', 'I', 'L', 'Wayoró', false, '');
INSERT INTO languages_iso639 VALUES ('wyy', '   ', '   ', '  ', 'I', 'L', 'Western Fijian', false, '');
INSERT INTO languages_iso639 VALUES ('xaa', '   ', '   ', '  ', 'I', 'H', 'Andalusian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('xab', '   ', '   ', '  ', 'I', 'L', 'Sambe', false, '');
INSERT INTO languages_iso639 VALUES ('xac', '   ', '   ', '  ', 'I', 'L', 'Kachari', false, '');
INSERT INTO languages_iso639 VALUES ('xad', '   ', '   ', '  ', 'I', 'E', 'Adai', false, '');
INSERT INTO languages_iso639 VALUES ('xae', '   ', '   ', '  ', 'I', 'A', 'Aequian', false, '');
INSERT INTO languages_iso639 VALUES ('xag', '   ', '   ', '  ', 'I', 'E', 'Aghwan', false, '');
INSERT INTO languages_iso639 VALUES ('xai', '   ', '   ', '  ', 'I', 'E', 'Kaimbé', false, '');
INSERT INTO languages_iso639 VALUES ('xal', 'xal', 'xal', '  ', 'I', 'L', 'Kalmyk', false, '');
INSERT INTO languages_iso639 VALUES ('xam', '   ', '   ', '  ', 'I', 'E', '/Xam', false, '');
INSERT INTO languages_iso639 VALUES ('xan', '   ', '   ', '  ', 'I', 'L', 'Xamtanga', false, '');
INSERT INTO languages_iso639 VALUES ('xao', '   ', '   ', '  ', 'I', 'L', 'Khao', false, '');
INSERT INTO languages_iso639 VALUES ('xap', '   ', '   ', '  ', 'I', 'E', 'Apalachee', false, '');
INSERT INTO languages_iso639 VALUES ('xaq', '   ', '   ', '  ', 'I', 'A', 'Aquitanian', false, '');
INSERT INTO languages_iso639 VALUES ('xar', '   ', '   ', '  ', 'I', 'E', 'Karami', false, '');
INSERT INTO languages_iso639 VALUES ('xas', '   ', '   ', '  ', 'I', 'E', 'Kamas', false, '');
INSERT INTO languages_iso639 VALUES ('xat', '   ', '   ', '  ', 'I', 'L', 'Katawixi', false, '');
INSERT INTO languages_iso639 VALUES ('xau', '   ', '   ', '  ', 'I', 'L', 'Kauwera', false, '');
INSERT INTO languages_iso639 VALUES ('xav', '   ', '   ', '  ', 'I', 'L', 'Xavánte', false, '');
INSERT INTO languages_iso639 VALUES ('xaw', '   ', '   ', '  ', 'I', 'L', 'Kawaiisu', false, '');
INSERT INTO languages_iso639 VALUES ('xay', '   ', '   ', '  ', 'I', 'L', 'Kayan Mahakam', false, '');
INSERT INTO languages_iso639 VALUES ('xba', '   ', '   ', '  ', 'I', 'E', 'Kamba (Brazil)', false, '');
INSERT INTO languages_iso639 VALUES ('xbb', '   ', '   ', '  ', 'I', 'E', 'Lower Burdekin', false, '');
INSERT INTO languages_iso639 VALUES ('xbc', '   ', '   ', '  ', 'I', 'A', 'Bactrian', false, '');
INSERT INTO languages_iso639 VALUES ('xbd', '   ', '   ', '  ', 'I', 'E', 'Bindal', false, '');
INSERT INTO languages_iso639 VALUES ('xbe', '   ', '   ', '  ', 'I', 'E', 'Bigambal', false, '');
INSERT INTO languages_iso639 VALUES ('xbg', '   ', '   ', '  ', 'I', 'E', 'Bunganditj', false, '');
INSERT INTO languages_iso639 VALUES ('xbi', '   ', '   ', '  ', 'I', 'L', 'Kombio', false, '');
INSERT INTO languages_iso639 VALUES ('xbj', '   ', '   ', '  ', 'I', 'E', 'Birrpayi', false, '');
INSERT INTO languages_iso639 VALUES ('xbm', '   ', '   ', '  ', 'I', 'H', 'Middle Breton', false, '');
INSERT INTO languages_iso639 VALUES ('xbn', '   ', '   ', '  ', 'I', 'E', 'Kenaboi', false, '');
INSERT INTO languages_iso639 VALUES ('xbo', '   ', '   ', '  ', 'I', 'E', 'Bolgarian', false, '');
INSERT INTO languages_iso639 VALUES ('xbp', '   ', '   ', '  ', 'I', 'E', 'Bibbulman', false, '');
INSERT INTO languages_iso639 VALUES ('xbr', '   ', '   ', '  ', 'I', 'L', 'Kambera', false, '');
INSERT INTO languages_iso639 VALUES ('xbw', '   ', '   ', '  ', 'I', 'E', 'Kambiwá', false, '');
INSERT INTO languages_iso639 VALUES ('xbx', '   ', '   ', '  ', 'I', 'E', 'Kabixí', false, '');
INSERT INTO languages_iso639 VALUES ('xby', '   ', '   ', '  ', 'I', 'L', 'Batyala', false, '');
INSERT INTO languages_iso639 VALUES ('xcb', '   ', '   ', '  ', 'I', 'E', 'Cumbric', false, '');
INSERT INTO languages_iso639 VALUES ('xcc', '   ', '   ', '  ', 'I', 'A', 'Camunic', false, '');
INSERT INTO languages_iso639 VALUES ('xce', '   ', '   ', '  ', 'I', 'A', 'Celtiberian', false, '');
INSERT INTO languages_iso639 VALUES ('xcg', '   ', '   ', '  ', 'I', 'A', 'Cisalpine Gaulish', false, '');
INSERT INTO languages_iso639 VALUES ('xch', '   ', '   ', '  ', 'I', 'E', 'Chemakum', false, '');
INSERT INTO languages_iso639 VALUES ('xcl', '   ', '   ', '  ', 'I', 'H', 'Classical Armenian', false, '');
INSERT INTO languages_iso639 VALUES ('xcm', '   ', '   ', '  ', 'I', 'E', 'Comecrudo', false, '');
INSERT INTO languages_iso639 VALUES ('xcn', '   ', '   ', '  ', 'I', 'E', 'Cotoname', false, '');
INSERT INTO languages_iso639 VALUES ('xco', '   ', '   ', '  ', 'I', 'A', 'Chorasmian', false, '');
INSERT INTO languages_iso639 VALUES ('xcr', '   ', '   ', '  ', 'I', 'A', 'Carian', false, '');
INSERT INTO languages_iso639 VALUES ('xct', '   ', '   ', '  ', 'I', 'H', 'Classical Tibetan', false, '');
INSERT INTO languages_iso639 VALUES ('xcu', '   ', '   ', '  ', 'I', 'E', 'Curonian', false, '');
INSERT INTO languages_iso639 VALUES ('xcv', '   ', '   ', '  ', 'I', 'E', 'Chuvantsy', false, '');
INSERT INTO languages_iso639 VALUES ('xcw', '   ', '   ', '  ', 'I', 'E', 'Coahuilteco', false, '');
INSERT INTO languages_iso639 VALUES ('xcy', '   ', '   ', '  ', 'I', 'E', 'Cayuse', false, '');
INSERT INTO languages_iso639 VALUES ('xda', '   ', '   ', '  ', 'I', 'L', 'Darkinyung', false, '');
INSERT INTO languages_iso639 VALUES ('xdc', '   ', '   ', '  ', 'I', 'A', 'Dacian', false, '');
INSERT INTO languages_iso639 VALUES ('xdk', '   ', '   ', '  ', 'I', 'E', 'Dharuk', false, '');
INSERT INTO languages_iso639 VALUES ('xdm', '   ', '   ', '  ', 'I', 'A', 'Edomite', false, '');
INSERT INTO languages_iso639 VALUES ('xdy', '   ', '   ', '  ', 'I', 'L', 'Malayic Dayak', false, '');
INSERT INTO languages_iso639 VALUES ('xeb', '   ', '   ', '  ', 'I', 'A', 'Eblan', false, '');
INSERT INTO languages_iso639 VALUES ('xed', '   ', '   ', '  ', 'I', 'L', 'Hdi', false, '');
INSERT INTO languages_iso639 VALUES ('xeg', '   ', '   ', '  ', 'I', 'E', '//Xegwi', false, '');
INSERT INTO languages_iso639 VALUES ('xel', '   ', '   ', '  ', 'I', 'L', 'Kelo', false, '');
INSERT INTO languages_iso639 VALUES ('xem', '   ', '   ', '  ', 'I', 'L', 'Kembayan', false, '');
INSERT INTO languages_iso639 VALUES ('xep', '   ', '   ', '  ', 'I', 'A', 'Epi-Olmec', false, '');
INSERT INTO languages_iso639 VALUES ('xer', '   ', '   ', '  ', 'I', 'L', 'Xerénte', false, '');
INSERT INTO languages_iso639 VALUES ('xes', '   ', '   ', '  ', 'I', 'L', 'Kesawai', false, '');
INSERT INTO languages_iso639 VALUES ('xet', '   ', '   ', '  ', 'I', 'L', 'Xetá', false, '');
INSERT INTO languages_iso639 VALUES ('xeu', '   ', '   ', '  ', 'I', 'L', 'Keoru-Ahia', false, '');
INSERT INTO languages_iso639 VALUES ('xfa', '   ', '   ', '  ', 'I', 'A', 'Faliscan', false, '');
INSERT INTO languages_iso639 VALUES ('xga', '   ', '   ', '  ', 'I', 'A', 'Galatian', false, '');
INSERT INTO languages_iso639 VALUES ('xgb', '   ', '   ', '  ', 'I', 'E', 'Gbin', false, '');
INSERT INTO languages_iso639 VALUES ('xgd', '   ', '   ', '  ', 'I', 'E', 'Gudang', false, '');
INSERT INTO languages_iso639 VALUES ('xgf', '   ', '   ', '  ', 'I', 'E', 'Gabrielino-Fernandeño', false, '');
INSERT INTO languages_iso639 VALUES ('xgg', '   ', '   ', '  ', 'I', 'E', 'Goreng', false, '');
INSERT INTO languages_iso639 VALUES ('xgi', '   ', '   ', '  ', 'I', 'E', 'Garingbal', false, '');
INSERT INTO languages_iso639 VALUES ('xgl', '   ', '   ', '  ', 'I', 'E', 'Galindan', false, '');
INSERT INTO languages_iso639 VALUES ('xgm', '   ', '   ', '  ', 'I', 'E', 'Guwinmal', false, '');
INSERT INTO languages_iso639 VALUES ('xgr', '   ', '   ', '  ', 'I', 'E', 'Garza', false, '');
INSERT INTO languages_iso639 VALUES ('xgu', '   ', '   ', '  ', 'I', 'L', 'Unggumi', false, '');
INSERT INTO languages_iso639 VALUES ('xgw', '   ', '   ', '  ', 'I', 'E', 'Guwa', false, '');
INSERT INTO languages_iso639 VALUES ('xha', '   ', '   ', '  ', 'I', 'A', 'Harami', false, '');
INSERT INTO languages_iso639 VALUES ('xhc', '   ', '   ', '  ', 'I', 'E', 'Hunnic', false, '');
INSERT INTO languages_iso639 VALUES ('xhd', '   ', '   ', '  ', 'I', 'A', 'Hadrami', false, '');
INSERT INTO languages_iso639 VALUES ('xhe', '   ', '   ', '  ', 'I', 'L', 'Khetrani', false, '');
INSERT INTO languages_iso639 VALUES ('xho', 'xho', 'xho', 'xh', 'I', 'L', 'Xhosa', false, '');
INSERT INTO languages_iso639 VALUES ('xhr', '   ', '   ', '  ', 'I', 'A', 'Hernican', false, '');
INSERT INTO languages_iso639 VALUES ('xht', '   ', '   ', '  ', 'I', 'A', 'Hattic', false, '');
INSERT INTO languages_iso639 VALUES ('xhu', '   ', '   ', '  ', 'I', 'A', 'Hurrian', false, '');
INSERT INTO languages_iso639 VALUES ('xhv', '   ', '   ', '  ', 'I', 'L', 'Khua', false, '');
INSERT INTO languages_iso639 VALUES ('xia', '   ', '   ', '  ', 'I', 'L', 'Xiandao', false, '');
INSERT INTO languages_iso639 VALUES ('xib', '   ', '   ', '  ', 'I', 'A', 'Iberian', false, '');
INSERT INTO languages_iso639 VALUES ('xii', '   ', '   ', '  ', 'I', 'L', 'Xiri', false, '');
INSERT INTO languages_iso639 VALUES ('xil', '   ', '   ', '  ', 'I', 'A', 'Illyrian', false, '');
INSERT INTO languages_iso639 VALUES ('xin', '   ', '   ', '  ', 'I', 'E', 'Xinca', false, '');
INSERT INTO languages_iso639 VALUES ('xip', '   ', '   ', '  ', 'I', 'E', 'Xipináwa', false, '');
INSERT INTO languages_iso639 VALUES ('xir', '   ', '   ', '  ', 'I', 'E', 'Xiriâna', false, '');
INSERT INTO languages_iso639 VALUES ('xiv', '   ', '   ', '  ', 'I', 'A', 'Indus Valley Language', false, '');
INSERT INTO languages_iso639 VALUES ('xiy', '   ', '   ', '  ', 'I', 'L', 'Xipaya', false, '');
INSERT INTO languages_iso639 VALUES ('xjb', '   ', '   ', '  ', 'I', 'E', 'Minjungbal', false, '');
INSERT INTO languages_iso639 VALUES ('xjt', '   ', '   ', '  ', 'I', 'E', 'Jaitmatang', false, '');
INSERT INTO languages_iso639 VALUES ('xka', '   ', '   ', '  ', 'I', 'L', 'Kalkoti', false, '');
INSERT INTO languages_iso639 VALUES ('xkb', '   ', '   ', '  ', 'I', 'L', 'Northern Nago', false, '');
INSERT INTO languages_iso639 VALUES ('xkc', '   ', '   ', '  ', 'I', 'L', 'Kho''ini', false, '');
INSERT INTO languages_iso639 VALUES ('xkd', '   ', '   ', '  ', 'I', 'L', 'Mendalam Kayan', false, '');
INSERT INTO languages_iso639 VALUES ('xke', '   ', '   ', '  ', 'I', 'L', 'Kereho', false, '');
INSERT INTO languages_iso639 VALUES ('xkf', '   ', '   ', '  ', 'I', 'L', 'Khengkha', false, '');
INSERT INTO languages_iso639 VALUES ('xkg', '   ', '   ', '  ', 'I', 'L', 'Kagoro', false, '');
INSERT INTO languages_iso639 VALUES ('xkh', '   ', '   ', '  ', 'I', 'L', 'Karahawyana', false, '');
INSERT INTO languages_iso639 VALUES ('xki', '   ', '   ', '  ', 'I', 'L', 'Kenyan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('xkj', '   ', '   ', '  ', 'I', 'L', 'Kajali', false, '');
INSERT INTO languages_iso639 VALUES ('xkk', '   ', '   ', '  ', 'I', 'L', 'Kaco''', false, '');
INSERT INTO languages_iso639 VALUES ('xkl', '   ', '   ', '  ', 'I', 'L', 'Mainstream Kenyah', false, '');
INSERT INTO languages_iso639 VALUES ('xkn', '   ', '   ', '  ', 'I', 'L', 'Kayan River Kayan', false, '');
INSERT INTO languages_iso639 VALUES ('xko', '   ', '   ', '  ', 'I', 'L', 'Kiorr', false, '');
INSERT INTO languages_iso639 VALUES ('xkp', '   ', '   ', '  ', 'I', 'L', 'Kabatei', false, '');
INSERT INTO languages_iso639 VALUES ('xkq', '   ', '   ', '  ', 'I', 'L', 'Koroni', false, '');
INSERT INTO languages_iso639 VALUES ('xkr', '   ', '   ', '  ', 'I', 'E', 'Xakriabá', false, '');
INSERT INTO languages_iso639 VALUES ('xks', '   ', '   ', '  ', 'I', 'L', 'Kumbewaha', false, '');
INSERT INTO languages_iso639 VALUES ('xkt', '   ', '   ', '  ', 'I', 'L', 'Kantosi', false, '');
INSERT INTO languages_iso639 VALUES ('xku', '   ', '   ', '  ', 'I', 'L', 'Kaamba', false, '');
INSERT INTO languages_iso639 VALUES ('xkv', '   ', '   ', '  ', 'I', 'L', 'Kgalagadi', false, '');
INSERT INTO languages_iso639 VALUES ('xkw', '   ', '   ', '  ', 'I', 'L', 'Kembra', false, '');
INSERT INTO languages_iso639 VALUES ('xkx', '   ', '   ', '  ', 'I', 'L', 'Karore', false, '');
INSERT INTO languages_iso639 VALUES ('xky', '   ', '   ', '  ', 'I', 'L', 'Uma'' Lasan', false, '');
INSERT INTO languages_iso639 VALUES ('xkz', '   ', '   ', '  ', 'I', 'L', 'Kurtokha', false, '');
INSERT INTO languages_iso639 VALUES ('xla', '   ', '   ', '  ', 'I', 'L', 'Kamula', false, '');
INSERT INTO languages_iso639 VALUES ('xlb', '   ', '   ', '  ', 'I', 'E', 'Loup B', false, '');
INSERT INTO languages_iso639 VALUES ('xlc', '   ', '   ', '  ', 'I', 'A', 'Lycian', false, '');
INSERT INTO languages_iso639 VALUES ('xld', '   ', '   ', '  ', 'I', 'A', 'Lydian', false, '');
INSERT INTO languages_iso639 VALUES ('xle', '   ', '   ', '  ', 'I', 'A', 'Lemnian', false, '');
INSERT INTO languages_iso639 VALUES ('xlg', '   ', '   ', '  ', 'I', 'A', 'Ligurian (Ancient)', false, '');
INSERT INTO languages_iso639 VALUES ('xli', '   ', '   ', '  ', 'I', 'A', 'Liburnian', false, '');
INSERT INTO languages_iso639 VALUES ('xln', '   ', '   ', '  ', 'I', 'A', 'Alanic', false, '');
INSERT INTO languages_iso639 VALUES ('xlo', '   ', '   ', '  ', 'I', 'E', 'Loup A', false, '');
INSERT INTO languages_iso639 VALUES ('xlp', '   ', '   ', '  ', 'I', 'A', 'Lepontic', false, '');
INSERT INTO languages_iso639 VALUES ('xls', '   ', '   ', '  ', 'I', 'A', 'Lusitanian', false, '');
INSERT INTO languages_iso639 VALUES ('xlu', '   ', '   ', '  ', 'I', 'A', 'Cuneiform Luwian', false, '');
INSERT INTO languages_iso639 VALUES ('xly', '   ', '   ', '  ', 'I', 'A', 'Elymian', false, '');
INSERT INTO languages_iso639 VALUES ('xma', '   ', '   ', '  ', 'I', 'L', 'Mushungulu', false, '');
INSERT INTO languages_iso639 VALUES ('xmb', '   ', '   ', '  ', 'I', 'L', 'Mbonga', false, '');
INSERT INTO languages_iso639 VALUES ('xmc', '   ', '   ', '  ', 'I', 'L', 'Makhuwa-Marrevone', false, '');
INSERT INTO languages_iso639 VALUES ('xmd', '   ', '   ', '  ', 'I', 'L', 'Mbudum', false, '');
INSERT INTO languages_iso639 VALUES ('xme', '   ', '   ', '  ', 'I', 'A', 'Median', false, '');
INSERT INTO languages_iso639 VALUES ('xmf', '   ', '   ', '  ', 'I', 'L', 'Mingrelian', false, '');
INSERT INTO languages_iso639 VALUES ('xmg', '   ', '   ', '  ', 'I', 'L', 'Mengaka', false, '');
INSERT INTO languages_iso639 VALUES ('xmh', '   ', '   ', '  ', 'I', 'L', 'Kuku-Muminh', false, '');
INSERT INTO languages_iso639 VALUES ('xmj', '   ', '   ', '  ', 'I', 'L', 'Majera', false, '');
INSERT INTO languages_iso639 VALUES ('xmk', '   ', '   ', '  ', 'I', 'A', 'Ancient Macedonian', false, '');
INSERT INTO languages_iso639 VALUES ('xml', '   ', '   ', '  ', 'I', 'L', 'Malaysian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('xmm', '   ', '   ', '  ', 'I', 'L', 'Manado Malay', false, '');
INSERT INTO languages_iso639 VALUES ('xmn', '   ', '   ', '  ', 'I', 'H', 'Manichaean Middle Persian', false, '');
INSERT INTO languages_iso639 VALUES ('xmo', '   ', '   ', '  ', 'I', 'L', 'Morerebi', false, '');
INSERT INTO languages_iso639 VALUES ('xmp', '   ', '   ', '  ', 'I', 'E', 'Kuku-Mu''inh', false, '');
INSERT INTO languages_iso639 VALUES ('xmq', '   ', '   ', '  ', 'I', 'E', 'Kuku-Mangk', false, '');
INSERT INTO languages_iso639 VALUES ('xmr', '   ', '   ', '  ', 'I', 'A', 'Meroitic', false, '');
INSERT INTO languages_iso639 VALUES ('xms', '   ', '   ', '  ', 'I', 'L', 'Moroccan Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('xmt', '   ', '   ', '  ', 'I', 'L', 'Matbat', false, '');
INSERT INTO languages_iso639 VALUES ('xmu', '   ', '   ', '  ', 'I', 'E', 'Kamu', false, '');
INSERT INTO languages_iso639 VALUES ('xmv', '   ', '   ', '  ', 'I', 'L', 'Antankarana Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('xmw', '   ', '   ', '  ', 'I', 'L', 'Tsimihety Malagasy', false, '');
INSERT INTO languages_iso639 VALUES ('xmx', '   ', '   ', '  ', 'I', 'L', 'Maden', false, '');
INSERT INTO languages_iso639 VALUES ('xmy', '   ', '   ', '  ', 'I', 'L', 'Mayaguduna', false, '');
INSERT INTO languages_iso639 VALUES ('xmz', '   ', '   ', '  ', 'I', 'L', 'Mori Bawah', false, '');
INSERT INTO languages_iso639 VALUES ('xna', '   ', '   ', '  ', 'I', 'A', 'Ancient North Arabian', false, '');
INSERT INTO languages_iso639 VALUES ('xnb', '   ', '   ', '  ', 'I', 'L', 'Kanakanabu', false, '');
INSERT INTO languages_iso639 VALUES ('xng', '   ', '   ', '  ', 'I', 'H', 'Middle Mongolian', false, '');
INSERT INTO languages_iso639 VALUES ('xnh', '   ', '   ', '  ', 'I', 'L', 'Kuanhua', false, '');
INSERT INTO languages_iso639 VALUES ('xni', '   ', '   ', '  ', 'I', 'E', 'Ngarigu', false, '');
INSERT INTO languages_iso639 VALUES ('xnk', '   ', '   ', '  ', 'I', 'E', 'Nganakarti', false, '');
INSERT INTO languages_iso639 VALUES ('xnn', '   ', '   ', '  ', 'I', 'L', 'Northern Kankanay', false, '');
INSERT INTO languages_iso639 VALUES ('xno', '   ', '   ', '  ', 'I', 'H', 'Anglo-Norman', false, '');
INSERT INTO languages_iso639 VALUES ('xnr', '   ', '   ', '  ', 'I', 'L', 'Kangri', false, '');
INSERT INTO languages_iso639 VALUES ('xns', '   ', '   ', '  ', 'I', 'L', 'Kanashi', false, '');
INSERT INTO languages_iso639 VALUES ('xnt', '   ', '   ', '  ', 'I', 'E', 'Narragansett', false, '');
INSERT INTO languages_iso639 VALUES ('xnu', '   ', '   ', '  ', 'I', 'E', 'Nukunul', false, '');
INSERT INTO languages_iso639 VALUES ('xny', '   ', '   ', '  ', 'I', 'L', 'Nyiyaparli', false, '');
INSERT INTO languages_iso639 VALUES ('xnz', '   ', '   ', '  ', 'I', 'L', 'Kenzi', false, '');
INSERT INTO languages_iso639 VALUES ('xoc', '   ', '   ', '  ', 'I', 'E', 'O''chi''chi''', false, '');
INSERT INTO languages_iso639 VALUES ('xod', '   ', '   ', '  ', 'I', 'L', 'Kokoda', false, '');
INSERT INTO languages_iso639 VALUES ('xog', '   ', '   ', '  ', 'I', 'L', 'Soga', false, '');
INSERT INTO languages_iso639 VALUES ('xoi', '   ', '   ', '  ', 'I', 'L', 'Kominimung', false, '');
INSERT INTO languages_iso639 VALUES ('xok', '   ', '   ', '  ', 'I', 'L', 'Xokleng', false, '');
INSERT INTO languages_iso639 VALUES ('xom', '   ', '   ', '  ', 'I', 'L', 'Komo (Sudan)', false, '');
INSERT INTO languages_iso639 VALUES ('xon', '   ', '   ', '  ', 'I', 'L', 'Konkomba', false, '');
INSERT INTO languages_iso639 VALUES ('xoo', '   ', '   ', '  ', 'I', 'E', 'Xukurú', false, '');
INSERT INTO languages_iso639 VALUES ('xop', '   ', '   ', '  ', 'I', 'L', 'Kopar', false, '');
INSERT INTO languages_iso639 VALUES ('xor', '   ', '   ', '  ', 'I', 'L', 'Korubo', false, '');
INSERT INTO languages_iso639 VALUES ('xow', '   ', '   ', '  ', 'I', 'L', 'Kowaki', false, '');
INSERT INTO languages_iso639 VALUES ('xpa', '   ', '   ', '  ', 'I', 'E', 'Pirriya', false, '');
INSERT INTO languages_iso639 VALUES ('xpc', '   ', '   ', '  ', 'I', 'E', 'Pecheneg', false, '');
INSERT INTO languages_iso639 VALUES ('xpe', '   ', '   ', '  ', 'I', 'L', 'Liberia Kpelle', false, '');
INSERT INTO languages_iso639 VALUES ('xpg', '   ', '   ', '  ', 'I', 'A', 'Phrygian', false, '');
INSERT INTO languages_iso639 VALUES ('xpi', '   ', '   ', '  ', 'I', 'E', 'Pictish', false, '');
INSERT INTO languages_iso639 VALUES ('xpj', '   ', '   ', '  ', 'I', 'E', 'Mpalitjanh', false, '');
INSERT INTO languages_iso639 VALUES ('xpk', '   ', '   ', '  ', 'I', 'L', 'Kulina Pano', false, '');
INSERT INTO languages_iso639 VALUES ('xpm', '   ', '   ', '  ', 'I', 'E', 'Pumpokol', false, '');
INSERT INTO languages_iso639 VALUES ('xpn', '   ', '   ', '  ', 'I', 'E', 'Kapinawá', false, '');
INSERT INTO languages_iso639 VALUES ('xpo', '   ', '   ', '  ', 'I', 'E', 'Pochutec', false, '');
INSERT INTO languages_iso639 VALUES ('xpp', '   ', '   ', '  ', 'I', 'E', 'Puyo-Paekche', false, '');
INSERT INTO languages_iso639 VALUES ('xpq', '   ', '   ', '  ', 'I', 'E', 'Mohegan-Pequot', false, '');
INSERT INTO languages_iso639 VALUES ('xpr', '   ', '   ', '  ', 'I', 'A', 'Parthian', false, '');
INSERT INTO languages_iso639 VALUES ('xps', '   ', '   ', '  ', 'I', 'E', 'Pisidian', false, '');
INSERT INTO languages_iso639 VALUES ('xpt', '   ', '   ', '  ', 'I', 'E', 'Punthamara', false, '');
INSERT INTO languages_iso639 VALUES ('xpu', '   ', '   ', '  ', 'I', 'A', 'Punic', false, '');
INSERT INTO languages_iso639 VALUES ('xpy', '   ', '   ', '  ', 'I', 'E', 'Puyo', false, '');
INSERT INTO languages_iso639 VALUES ('xqa', '   ', '   ', '  ', 'I', 'H', 'Karakhanid', false, '');
INSERT INTO languages_iso639 VALUES ('xqt', '   ', '   ', '  ', 'I', 'A', 'Qatabanian', false, '');
INSERT INTO languages_iso639 VALUES ('xra', '   ', '   ', '  ', 'I', 'L', 'Krahô', false, '');
INSERT INTO languages_iso639 VALUES ('xrb', '   ', '   ', '  ', 'I', 'L', 'Eastern Karaboro', false, '');
INSERT INTO languages_iso639 VALUES ('xrd', '   ', '   ', '  ', 'I', 'E', 'Gundungurra', false, '');
INSERT INTO languages_iso639 VALUES ('xre', '   ', '   ', '  ', 'I', 'L', 'Kreye', false, '');
INSERT INTO languages_iso639 VALUES ('xrg', '   ', '   ', '  ', 'I', 'E', 'Minang', false, '');
INSERT INTO languages_iso639 VALUES ('xri', '   ', '   ', '  ', 'I', 'L', 'Krikati-Timbira', false, '');
INSERT INTO languages_iso639 VALUES ('xrm', '   ', '   ', '  ', 'I', 'E', 'Armazic', false, '');
INSERT INTO languages_iso639 VALUES ('xrn', '   ', '   ', '  ', 'I', 'E', 'Arin', false, '');
INSERT INTO languages_iso639 VALUES ('xrq', '   ', '   ', '  ', 'I', 'E', 'Karranga', false, '');
INSERT INTO languages_iso639 VALUES ('xrr', '   ', '   ', '  ', 'I', 'A', 'Raetic', false, '');
INSERT INTO languages_iso639 VALUES ('xrt', '   ', '   ', '  ', 'I', 'E', 'Aranama-Tamique', false, '');
INSERT INTO languages_iso639 VALUES ('xru', '   ', '   ', '  ', 'I', 'L', 'Marriammu', false, '');
INSERT INTO languages_iso639 VALUES ('xrw', '   ', '   ', '  ', 'I', 'L', 'Karawa', false, '');
INSERT INTO languages_iso639 VALUES ('xsa', '   ', '   ', '  ', 'I', 'A', 'Sabaean', false, '');
INSERT INTO languages_iso639 VALUES ('xsb', '   ', '   ', '  ', 'I', 'L', 'Sambal', false, '');
INSERT INTO languages_iso639 VALUES ('xsc', '   ', '   ', '  ', 'I', 'A', 'Scythian', false, '');
INSERT INTO languages_iso639 VALUES ('xsd', '   ', '   ', '  ', 'I', 'A', 'Sidetic', false, '');
INSERT INTO languages_iso639 VALUES ('xse', '   ', '   ', '  ', 'I', 'L', 'Sempan', false, '');
INSERT INTO languages_iso639 VALUES ('xsh', '   ', '   ', '  ', 'I', 'L', 'Shamang', false, '');
INSERT INTO languages_iso639 VALUES ('xsi', '   ', '   ', '  ', 'I', 'L', 'Sio', false, '');
INSERT INTO languages_iso639 VALUES ('xsj', '   ', '   ', '  ', 'I', 'L', 'Subi', false, '');
INSERT INTO languages_iso639 VALUES ('xsl', '   ', '   ', '  ', 'I', 'L', 'South Slavey', false, '');
INSERT INTO languages_iso639 VALUES ('xsm', '   ', '   ', '  ', 'I', 'L', 'Kasem', false, '');
INSERT INTO languages_iso639 VALUES ('xsn', '   ', '   ', '  ', 'I', 'L', 'Sanga (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('xso', '   ', '   ', '  ', 'I', 'E', 'Solano', false, '');
INSERT INTO languages_iso639 VALUES ('xsp', '   ', '   ', '  ', 'I', 'L', 'Silopi', false, '');
INSERT INTO languages_iso639 VALUES ('xsq', '   ', '   ', '  ', 'I', 'L', 'Makhuwa-Saka', false, '');
INSERT INTO languages_iso639 VALUES ('xsr', '   ', '   ', '  ', 'I', 'L', 'Sherpa', false, '');
INSERT INTO languages_iso639 VALUES ('xss', '   ', '   ', '  ', 'I', 'E', 'Assan', false, '');
INSERT INTO languages_iso639 VALUES ('xsu', '   ', '   ', '  ', 'I', 'L', 'Sanumá', false, '');
INSERT INTO languages_iso639 VALUES ('xsv', '   ', '   ', '  ', 'I', 'E', 'Sudovian', false, '');
INSERT INTO languages_iso639 VALUES ('xsy', '   ', '   ', '  ', 'I', 'L', 'Saisiyat', false, '');
INSERT INTO languages_iso639 VALUES ('xta', '   ', '   ', '  ', 'I', 'L', 'Alcozauca Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtb', '   ', '   ', '  ', 'I', 'L', 'Chazumba Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtc', '   ', '   ', '  ', 'I', 'L', 'Katcha-Kadugli-Miri', false, '');
INSERT INTO languages_iso639 VALUES ('xtd', '   ', '   ', '  ', 'I', 'L', 'Diuxi-Tilantongo Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xte', '   ', '   ', '  ', 'I', 'L', 'Ketengban', false, '');
INSERT INTO languages_iso639 VALUES ('xtg', '   ', '   ', '  ', 'I', 'A', 'Transalpine Gaulish', false, '');
INSERT INTO languages_iso639 VALUES ('xth', '   ', '   ', '  ', 'I', 'E', 'Yitha Yitha', false, '');
INSERT INTO languages_iso639 VALUES ('xti', '   ', '   ', '  ', 'I', 'L', 'Sinicahua Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtj', '   ', '   ', '  ', 'I', 'L', 'San Juan Teita Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtl', '   ', '   ', '  ', 'I', 'L', 'Tijaltepec Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtm', '   ', '   ', '  ', 'I', 'L', 'Magdalena Peñasco Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtn', '   ', '   ', '  ', 'I', 'L', 'Northern Tlaxiaco Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xto', '   ', '   ', '  ', 'I', 'A', 'Tokharian A', false, '');
INSERT INTO languages_iso639 VALUES ('xtp', '   ', '   ', '  ', 'I', 'L', 'San Miguel Piedras Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtq', '   ', '   ', '  ', 'I', 'H', 'Tumshuqese', false, '');
INSERT INTO languages_iso639 VALUES ('xtr', '   ', '   ', '  ', 'I', 'A', 'Early Tripuri', false, '');
INSERT INTO languages_iso639 VALUES ('xts', '   ', '   ', '  ', 'I', 'L', 'Sindihui Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtt', '   ', '   ', '  ', 'I', 'L', 'Tacahua Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtu', '   ', '   ', '  ', 'I', 'L', 'Cuyamecalco Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtv', '   ', '   ', '  ', 'I', 'E', 'Thawa', false, '');
INSERT INTO languages_iso639 VALUES ('xtw', '   ', '   ', '  ', 'I', 'L', 'Tawandê', false, '');
INSERT INTO languages_iso639 VALUES ('xty', '   ', '   ', '  ', 'I', 'L', 'Yoloxochitl Mixtec', false, '');
INSERT INTO languages_iso639 VALUES ('xtz', '   ', '   ', '  ', 'I', 'E', 'Tasmanian', false, '');
INSERT INTO languages_iso639 VALUES ('xua', '   ', '   ', '  ', 'I', 'L', 'Alu Kurumba', false, '');
INSERT INTO languages_iso639 VALUES ('xub', '   ', '   ', '  ', 'I', 'L', 'Betta Kurumba', false, '');
INSERT INTO languages_iso639 VALUES ('xud', '   ', '   ', '  ', 'I', 'E', 'Umiida', false, '');
INSERT INTO languages_iso639 VALUES ('xug', '   ', '   ', '  ', 'I', 'L', 'Kunigami', false, '');
INSERT INTO languages_iso639 VALUES ('xuj', '   ', '   ', '  ', 'I', 'L', 'Jennu Kurumba', false, '');
INSERT INTO languages_iso639 VALUES ('xul', '   ', '   ', '  ', 'I', 'E', 'Ngunawal', false, '');
INSERT INTO languages_iso639 VALUES ('xum', '   ', '   ', '  ', 'I', 'A', 'Umbrian', false, '');
INSERT INTO languages_iso639 VALUES ('xun', '   ', '   ', '  ', 'I', 'E', 'Unggarranggu', false, '');
INSERT INTO languages_iso639 VALUES ('xuo', '   ', '   ', '  ', 'I', 'L', 'Kuo', false, '');
INSERT INTO languages_iso639 VALUES ('xup', '   ', '   ', '  ', 'I', 'E', 'Upper Umpqua', false, '');
INSERT INTO languages_iso639 VALUES ('xur', '   ', '   ', '  ', 'I', 'A', 'Urartian', false, '');
INSERT INTO languages_iso639 VALUES ('xut', '   ', '   ', '  ', 'I', 'E', 'Kuthant', false, '');
INSERT INTO languages_iso639 VALUES ('xuu', '   ', '   ', '  ', 'I', 'L', 'Kxoe', false, '');
INSERT INTO languages_iso639 VALUES ('xve', '   ', '   ', '  ', 'I', 'A', 'Venetic', false, '');
INSERT INTO languages_iso639 VALUES ('xvi', '   ', '   ', '  ', 'I', 'L', 'Kamviri', false, '');
INSERT INTO languages_iso639 VALUES ('xvn', '   ', '   ', '  ', 'I', 'A', 'Vandalic', false, '');
INSERT INTO languages_iso639 VALUES ('xvo', '   ', '   ', '  ', 'I', 'A', 'Volscian', false, '');
INSERT INTO languages_iso639 VALUES ('xvs', '   ', '   ', '  ', 'I', 'A', 'Vestinian', false, '');
INSERT INTO languages_iso639 VALUES ('xwa', '   ', '   ', '  ', 'I', 'L', 'Kwaza', false, '');
INSERT INTO languages_iso639 VALUES ('xwc', '   ', '   ', '  ', 'I', 'E', 'Woccon', false, '');
INSERT INTO languages_iso639 VALUES ('xwd', '   ', '   ', '  ', 'I', 'E', 'Wadi Wadi', false, '');
INSERT INTO languages_iso639 VALUES ('xwe', '   ', '   ', '  ', 'I', 'L', 'Xwela Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('xwg', '   ', '   ', '  ', 'I', 'L', 'Kwegu', false, '');
INSERT INTO languages_iso639 VALUES ('xwj', '   ', '   ', '  ', 'I', 'E', 'Wajuk', false, '');
INSERT INTO languages_iso639 VALUES ('xwk', '   ', '   ', '  ', 'I', 'E', 'Wangkumara', false, '');
INSERT INTO languages_iso639 VALUES ('xwl', '   ', '   ', '  ', 'I', 'L', 'Western Xwla Gbe', false, '');
INSERT INTO languages_iso639 VALUES ('xwo', '   ', '   ', '  ', 'I', 'E', 'Written Oirat', false, '');
INSERT INTO languages_iso639 VALUES ('xwr', '   ', '   ', '  ', 'I', 'L', 'Kwerba Mamberamo', false, '');
INSERT INTO languages_iso639 VALUES ('xwt', '   ', '   ', '  ', 'I', 'E', 'Wotjobaluk', false, '');
INSERT INTO languages_iso639 VALUES ('xww', '   ', '   ', '  ', 'I', 'E', 'Wemba Wemba', false, '');
INSERT INTO languages_iso639 VALUES ('xxb', '   ', '   ', '  ', 'I', 'E', 'Boro (Ghana)', false, '');
INSERT INTO languages_iso639 VALUES ('xxk', '   ', '   ', '  ', 'I', 'L', 'Ke''o', false, '');
INSERT INTO languages_iso639 VALUES ('xxm', '   ', '   ', '  ', 'I', 'E', 'Minkin', false, '');
INSERT INTO languages_iso639 VALUES ('xxr', '   ', '   ', '  ', 'I', 'E', 'Koropó', false, '');
INSERT INTO languages_iso639 VALUES ('xxt', '   ', '   ', '  ', 'I', 'E', 'Tambora', false, '');
INSERT INTO languages_iso639 VALUES ('xya', '   ', '   ', '  ', 'I', 'E', 'Yaygir', false, '');
INSERT INTO languages_iso639 VALUES ('xyb', '   ', '   ', '  ', 'I', 'E', 'Yandjibara', false, '');
INSERT INTO languages_iso639 VALUES ('xyj', '   ', '   ', '  ', 'I', 'E', 'Mayi-Yapi', false, '');
INSERT INTO languages_iso639 VALUES ('xyk', '   ', '   ', '  ', 'I', 'E', 'Mayi-Kulan', false, '');
INSERT INTO languages_iso639 VALUES ('xyl', '   ', '   ', '  ', 'I', 'E', 'Yalakalore', false, '');
INSERT INTO languages_iso639 VALUES ('xyt', '   ', '   ', '  ', 'I', 'E', 'Mayi-Thakurti', false, '');
INSERT INTO languages_iso639 VALUES ('xyy', '   ', '   ', '  ', 'I', 'L', 'Yorta Yorta', false, '');
INSERT INTO languages_iso639 VALUES ('xzh', '   ', '   ', '  ', 'I', 'A', 'Zhang-Zhung', false, '');
INSERT INTO languages_iso639 VALUES ('xzm', '   ', '   ', '  ', 'I', 'E', 'Zemgalian', false, '');
INSERT INTO languages_iso639 VALUES ('xzp', '   ', '   ', '  ', 'I', 'H', 'Ancient Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('yaa', '   ', '   ', '  ', 'I', 'L', 'Yaminahua', false, '');
INSERT INTO languages_iso639 VALUES ('yab', '   ', '   ', '  ', 'I', 'L', 'Yuhup', false, '');
INSERT INTO languages_iso639 VALUES ('yac', '   ', '   ', '  ', 'I', 'L', 'Pass Valley Yali', false, '');
INSERT INTO languages_iso639 VALUES ('yad', '   ', '   ', '  ', 'I', 'L', 'Yagua', false, '');
INSERT INTO languages_iso639 VALUES ('yae', '   ', '   ', '  ', 'I', 'L', 'Pumé', false, '');
INSERT INTO languages_iso639 VALUES ('yaf', '   ', '   ', '  ', 'I', 'L', 'Yaka (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('yag', '   ', '   ', '  ', 'I', 'L', 'Yámana', false, '');
INSERT INTO languages_iso639 VALUES ('yah', '   ', '   ', '  ', 'I', 'L', 'Yazgulyam', false, '');
INSERT INTO languages_iso639 VALUES ('yai', '   ', '   ', '  ', 'I', 'L', 'Yagnobi', false, '');
INSERT INTO languages_iso639 VALUES ('yaj', '   ', '   ', '  ', 'I', 'L', 'Banda-Yangere', false, '');
INSERT INTO languages_iso639 VALUES ('yak', '   ', '   ', '  ', 'I', 'L', 'Yakama', false, '');
INSERT INTO languages_iso639 VALUES ('yal', '   ', '   ', '  ', 'I', 'L', 'Yalunka', false, '');
INSERT INTO languages_iso639 VALUES ('yam', '   ', '   ', '  ', 'I', 'L', 'Yamba', false, '');
INSERT INTO languages_iso639 VALUES ('yan', '   ', '   ', '  ', 'I', 'L', 'Mayangna', false, '');
INSERT INTO languages_iso639 VALUES ('yao', 'yao', 'yao', '  ', 'I', 'L', 'Yao', false, '');
INSERT INTO languages_iso639 VALUES ('yap', 'yap', 'yap', '  ', 'I', 'L', 'Yapese', false, '');
INSERT INTO languages_iso639 VALUES ('yaq', '   ', '   ', '  ', 'I', 'L', 'Yaqui', false, '');
INSERT INTO languages_iso639 VALUES ('yar', '   ', '   ', '  ', 'I', 'L', 'Yabarana', false, '');
INSERT INTO languages_iso639 VALUES ('yas', '   ', '   ', '  ', 'I', 'L', 'Nugunu (Cameroon)', false, '');
INSERT INTO languages_iso639 VALUES ('yat', '   ', '   ', '  ', 'I', 'L', 'Yambeta', false, '');
INSERT INTO languages_iso639 VALUES ('yau', '   ', '   ', '  ', 'I', 'L', 'Yuwana', false, '');
INSERT INTO languages_iso639 VALUES ('yav', '   ', '   ', '  ', 'I', 'L', 'Yangben', false, '');
INSERT INTO languages_iso639 VALUES ('yaw', '   ', '   ', '  ', 'I', 'L', 'Yawalapití', false, '');
INSERT INTO languages_iso639 VALUES ('yax', '   ', '   ', '  ', 'I', 'L', 'Yauma', false, '');
INSERT INTO languages_iso639 VALUES ('yay', '   ', '   ', '  ', 'I', 'L', 'Agwagwune', false, '');
INSERT INTO languages_iso639 VALUES ('yaz', '   ', '   ', '  ', 'I', 'L', 'Lokaa', false, '');
INSERT INTO languages_iso639 VALUES ('yba', '   ', '   ', '  ', 'I', 'L', 'Yala', false, '');
INSERT INTO languages_iso639 VALUES ('ybb', '   ', '   ', '  ', 'I', 'L', 'Yemba', false, '');
INSERT INTO languages_iso639 VALUES ('ybe', '   ', '   ', '  ', 'I', 'L', 'West Yugur', false, '');
INSERT INTO languages_iso639 VALUES ('ybh', '   ', '   ', '  ', 'I', 'L', 'Yakha', false, '');
INSERT INTO languages_iso639 VALUES ('ybi', '   ', '   ', '  ', 'I', 'L', 'Yamphu', false, '');
INSERT INTO languages_iso639 VALUES ('ybj', '   ', '   ', '  ', 'I', 'L', 'Hasha', false, '');
INSERT INTO languages_iso639 VALUES ('ybk', '   ', '   ', '  ', 'I', 'L', 'Bokha', false, '');
INSERT INTO languages_iso639 VALUES ('ybl', '   ', '   ', '  ', 'I', 'L', 'Yukuben', false, '');
INSERT INTO languages_iso639 VALUES ('ybm', '   ', '   ', '  ', 'I', 'L', 'Yaben', false, '');
INSERT INTO languages_iso639 VALUES ('ybn', '   ', '   ', '  ', 'I', 'E', 'Yabaâna', false, '');
INSERT INTO languages_iso639 VALUES ('ybo', '   ', '   ', '  ', 'I', 'L', 'Yabong', false, '');
INSERT INTO languages_iso639 VALUES ('ybx', '   ', '   ', '  ', 'I', 'L', 'Yawiyo', false, '');
INSERT INTO languages_iso639 VALUES ('yby', '   ', '   ', '  ', 'I', 'L', 'Yaweyuha', false, '');
INSERT INTO languages_iso639 VALUES ('ych', '   ', '   ', '  ', 'I', 'L', 'Chesu', false, '');
INSERT INTO languages_iso639 VALUES ('ycl', '   ', '   ', '  ', 'I', 'L', 'Lolopo', false, '');
INSERT INTO languages_iso639 VALUES ('ycn', '   ', '   ', '  ', 'I', 'L', 'Yucuna', false, '');
INSERT INTO languages_iso639 VALUES ('ycp', '   ', '   ', '  ', 'I', 'L', 'Chepya', false, '');
INSERT INTO languages_iso639 VALUES ('yda', '   ', '   ', '  ', 'I', 'E', 'Yanda', false, '');
INSERT INTO languages_iso639 VALUES ('ydd', '   ', '   ', '  ', 'I', 'L', 'Eastern Yiddish', false, '');
INSERT INTO languages_iso639 VALUES ('yde', '   ', '   ', '  ', 'I', 'L', 'Yangum Dey', false, '');
INSERT INTO languages_iso639 VALUES ('ydg', '   ', '   ', '  ', 'I', 'L', 'Yidgha', false, '');
INSERT INTO languages_iso639 VALUES ('ydk', '   ', '   ', '  ', 'I', 'L', 'Yoidik', false, '');
INSERT INTO languages_iso639 VALUES ('yds', '   ', '   ', '  ', 'I', 'L', 'Yiddish Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('yea', '   ', '   ', '  ', 'I', 'L', 'Ravula', false, '');
INSERT INTO languages_iso639 VALUES ('yec', '   ', '   ', '  ', 'I', 'L', 'Yeniche', false, '');
INSERT INTO languages_iso639 VALUES ('yee', '   ', '   ', '  ', 'I', 'L', 'Yimas', false, '');
INSERT INTO languages_iso639 VALUES ('yei', '   ', '   ', '  ', 'I', 'E', 'Yeni', false, '');
INSERT INTO languages_iso639 VALUES ('yej', '   ', '   ', '  ', 'I', 'L', 'Yevanic', false, '');
INSERT INTO languages_iso639 VALUES ('yel', '   ', '   ', '  ', 'I', 'L', 'Yela', false, '');
INSERT INTO languages_iso639 VALUES ('yer', '   ', '   ', '  ', 'I', 'L', 'Tarok', false, '');
INSERT INTO languages_iso639 VALUES ('yes', '   ', '   ', '  ', 'I', 'L', 'Nyankpa', false, '');
INSERT INTO languages_iso639 VALUES ('yet', '   ', '   ', '  ', 'I', 'L', 'Yetfa', false, '');
INSERT INTO languages_iso639 VALUES ('yeu', '   ', '   ', '  ', 'I', 'L', 'Yerukula', false, '');
INSERT INTO languages_iso639 VALUES ('yev', '   ', '   ', '  ', 'I', 'L', 'Yapunda', false, '');
INSERT INTO languages_iso639 VALUES ('yey', '   ', '   ', '  ', 'I', 'L', 'Yeyi', false, '');
INSERT INTO languages_iso639 VALUES ('yga', '   ', '   ', '  ', 'I', 'E', 'Malyangapa', false, '');
INSERT INTO languages_iso639 VALUES ('ygi', '   ', '   ', '  ', 'I', 'E', 'Yiningayi', false, '');
INSERT INTO languages_iso639 VALUES ('ygl', '   ', '   ', '  ', 'I', 'L', 'Yangum Gel', false, '');
INSERT INTO languages_iso639 VALUES ('ygm', '   ', '   ', '  ', 'I', 'L', 'Yagomi', false, '');
INSERT INTO languages_iso639 VALUES ('ygp', '   ', '   ', '  ', 'I', 'L', 'Gepo', false, '');
INSERT INTO languages_iso639 VALUES ('ygr', '   ', '   ', '  ', 'I', 'L', 'Yagaria', false, '');
INSERT INTO languages_iso639 VALUES ('ygu', '   ', '   ', '  ', 'I', 'L', 'Yugul', false, '');
INSERT INTO languages_iso639 VALUES ('ygw', '   ', '   ', '  ', 'I', 'L', 'Yagwoia', false, '');
INSERT INTO languages_iso639 VALUES ('yha', '   ', '   ', '  ', 'I', 'L', 'Baha Buyang', false, '');
INSERT INTO languages_iso639 VALUES ('yhd', '   ', '   ', '  ', 'I', 'L', 'Judeo-Iraqi Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('yhl', '   ', '   ', '  ', 'I', 'L', 'Hlepho Phowa', false, '');
INSERT INTO languages_iso639 VALUES ('yia', '   ', '   ', '  ', 'I', 'L', 'Yinggarda', false, '');
INSERT INTO languages_iso639 VALUES ('yid', 'yid', 'yid', 'yi', 'M', 'L', 'Yiddish', false, '');
INSERT INTO languages_iso639 VALUES ('yif', '   ', '   ', '  ', 'I', 'L', 'Ache', false, '');
INSERT INTO languages_iso639 VALUES ('yig', '   ', '   ', '  ', 'I', 'L', 'Wusa Nasu', false, '');
INSERT INTO languages_iso639 VALUES ('yih', '   ', '   ', '  ', 'I', 'L', 'Western Yiddish', false, '');
INSERT INTO languages_iso639 VALUES ('yii', '   ', '   ', '  ', 'I', 'L', 'Yidiny', false, '');
INSERT INTO languages_iso639 VALUES ('yij', '   ', '   ', '  ', 'I', 'L', 'Yindjibarndi', false, '');
INSERT INTO languages_iso639 VALUES ('yik', '   ', '   ', '  ', 'I', 'L', 'Dongshanba Lalo', false, '');
INSERT INTO languages_iso639 VALUES ('yil', '   ', '   ', '  ', 'I', 'E', 'Yindjilandji', false, '');
INSERT INTO languages_iso639 VALUES ('yim', '   ', '   ', '  ', 'I', 'L', 'Yimchungru Naga', false, '');
INSERT INTO languages_iso639 VALUES ('yin', '   ', '   ', '  ', 'I', 'L', 'Yinchia', false, '');
INSERT INTO languages_iso639 VALUES ('yip', '   ', '   ', '  ', 'I', 'L', 'Pholo', false, '');
INSERT INTO languages_iso639 VALUES ('yiq', '   ', '   ', '  ', 'I', 'L', 'Miqie', false, '');
INSERT INTO languages_iso639 VALUES ('yir', '   ', '   ', '  ', 'I', 'L', 'North Awyu', false, '');
INSERT INTO languages_iso639 VALUES ('yis', '   ', '   ', '  ', 'I', 'L', 'Yis', false, '');
INSERT INTO languages_iso639 VALUES ('yit', '   ', '   ', '  ', 'I', 'L', 'Eastern Lalu', false, '');
INSERT INTO languages_iso639 VALUES ('yiu', '   ', '   ', '  ', 'I', 'L', 'Awu', false, '');
INSERT INTO languages_iso639 VALUES ('yiv', '   ', '   ', '  ', 'I', 'L', 'Northern Nisu', false, '');
INSERT INTO languages_iso639 VALUES ('yix', '   ', '   ', '  ', 'I', 'L', 'Axi Yi', false, '');
INSERT INTO languages_iso639 VALUES ('yiz', '   ', '   ', '  ', 'I', 'L', 'Azhe', false, '');
INSERT INTO languages_iso639 VALUES ('yka', '   ', '   ', '  ', 'I', 'L', 'Yakan', false, '');
INSERT INTO languages_iso639 VALUES ('ykg', '   ', '   ', '  ', 'I', 'L', 'Northern Yukaghir', false, '');
INSERT INTO languages_iso639 VALUES ('yki', '   ', '   ', '  ', 'I', 'L', 'Yoke', false, '');
INSERT INTO languages_iso639 VALUES ('ykk', '   ', '   ', '  ', 'I', 'L', 'Yakaikeke', false, '');
INSERT INTO languages_iso639 VALUES ('ykl', '   ', '   ', '  ', 'I', 'L', 'Khlula', false, '');
INSERT INTO languages_iso639 VALUES ('ykm', '   ', '   ', '  ', 'I', 'L', 'Kap', false, '');
INSERT INTO languages_iso639 VALUES ('ykn', '   ', '   ', '  ', 'I', 'L', 'Kua-nsi', false, '');
INSERT INTO languages_iso639 VALUES ('yko', '   ', '   ', '  ', 'I', 'L', 'Yasa', false, '');
INSERT INTO languages_iso639 VALUES ('ykr', '   ', '   ', '  ', 'I', 'L', 'Yekora', false, '');
INSERT INTO languages_iso639 VALUES ('ykt', '   ', '   ', '  ', 'I', 'L', 'Kathu', false, '');
INSERT INTO languages_iso639 VALUES ('yku', '   ', '   ', '  ', 'I', 'L', 'Kuamasi', false, '');
INSERT INTO languages_iso639 VALUES ('yky', '   ', '   ', '  ', 'I', 'L', 'Yakoma', false, '');
INSERT INTO languages_iso639 VALUES ('yla', '   ', '   ', '  ', 'I', 'L', 'Yaul', false, '');
INSERT INTO languages_iso639 VALUES ('ylb', '   ', '   ', '  ', 'I', 'L', 'Yaleba', false, '');
INSERT INTO languages_iso639 VALUES ('yle', '   ', '   ', '  ', 'I', 'L', 'Yele', false, '');
INSERT INTO languages_iso639 VALUES ('ylg', '   ', '   ', '  ', 'I', 'L', 'Yelogu', false, '');
INSERT INTO languages_iso639 VALUES ('yli', '   ', '   ', '  ', 'I', 'L', 'Angguruk Yali', false, '');
INSERT INTO languages_iso639 VALUES ('yll', '   ', '   ', '  ', 'I', 'L', 'Yil', false, '');
INSERT INTO languages_iso639 VALUES ('ylm', '   ', '   ', '  ', 'I', 'L', 'Limi', false, '');
INSERT INTO languages_iso639 VALUES ('yln', '   ', '   ', '  ', 'I', 'L', 'Langnian Buyang', false, '');
INSERT INTO languages_iso639 VALUES ('ylo', '   ', '   ', '  ', 'I', 'L', 'Naluo Yi', false, '');
INSERT INTO languages_iso639 VALUES ('ylr', '   ', '   ', '  ', 'I', 'E', 'Yalarnnga', false, '');
INSERT INTO languages_iso639 VALUES ('ylu', '   ', '   ', '  ', 'I', 'L', 'Aribwaung', false, '');
INSERT INTO languages_iso639 VALUES ('yly', '   ', '   ', '  ', 'I', 'L', 'Nyâlayu', false, '');
INSERT INTO languages_iso639 VALUES ('ymb', '   ', '   ', '  ', 'I', 'L', 'Yambes', false, '');
INSERT INTO languages_iso639 VALUES ('ymc', '   ', '   ', '  ', 'I', 'L', 'Southern Muji', false, '');
INSERT INTO languages_iso639 VALUES ('ymd', '   ', '   ', '  ', 'I', 'L', 'Muda', false, '');
INSERT INTO languages_iso639 VALUES ('yme', '   ', '   ', '  ', 'I', 'E', 'Yameo', false, '');
INSERT INTO languages_iso639 VALUES ('ymg', '   ', '   ', '  ', 'I', 'L', 'Yamongeri', false, '');
INSERT INTO languages_iso639 VALUES ('ymh', '   ', '   ', '  ', 'I', 'L', 'Mili', false, '');
INSERT INTO languages_iso639 VALUES ('ymi', '   ', '   ', '  ', 'I', 'L', 'Moji', false, '');
INSERT INTO languages_iso639 VALUES ('ymk', '   ', '   ', '  ', 'I', 'L', 'Makwe', false, '');
INSERT INTO languages_iso639 VALUES ('yml', '   ', '   ', '  ', 'I', 'L', 'Iamalele', false, '');
INSERT INTO languages_iso639 VALUES ('ymm', '   ', '   ', '  ', 'I', 'L', 'Maay', false, '');
INSERT INTO languages_iso639 VALUES ('ymn', '   ', '   ', '  ', 'I', 'L', 'Yamna', false, '');
INSERT INTO languages_iso639 VALUES ('ymo', '   ', '   ', '  ', 'I', 'L', 'Yangum Mon', false, '');
INSERT INTO languages_iso639 VALUES ('ymp', '   ', '   ', '  ', 'I', 'L', 'Yamap', false, '');
INSERT INTO languages_iso639 VALUES ('ymq', '   ', '   ', '  ', 'I', 'L', 'Qila Muji', false, '');
INSERT INTO languages_iso639 VALUES ('ymr', '   ', '   ', '  ', 'I', 'L', 'Malasar', false, '');
INSERT INTO languages_iso639 VALUES ('yms', '   ', '   ', '  ', 'I', 'A', 'Mysian', false, '');
INSERT INTO languages_iso639 VALUES ('ymt', '   ', '   ', '  ', 'I', 'E', 'Mator-Taygi-Karagas', false, '');
INSERT INTO languages_iso639 VALUES ('ymx', '   ', '   ', '  ', 'I', 'L', 'Northern Muji', false, '');
INSERT INTO languages_iso639 VALUES ('ymz', '   ', '   ', '  ', 'I', 'L', 'Muzi', false, '');
INSERT INTO languages_iso639 VALUES ('yna', '   ', '   ', '  ', 'I', 'L', 'Aluo', false, '');
INSERT INTO languages_iso639 VALUES ('ynd', '   ', '   ', '  ', 'I', 'E', 'Yandruwandha', false, '');
INSERT INTO languages_iso639 VALUES ('yne', '   ', '   ', '  ', 'I', 'L', 'Lang''e', false, '');
INSERT INTO languages_iso639 VALUES ('yng', '   ', '   ', '  ', 'I', 'L', 'Yango', false, '');
INSERT INTO languages_iso639 VALUES ('ynh', '   ', '   ', '  ', 'I', 'L', 'Yangho', false, '');
INSERT INTO languages_iso639 VALUES ('ynk', '   ', '   ', '  ', 'I', 'L', 'Naukan Yupik', false, '');
INSERT INTO languages_iso639 VALUES ('ynl', '   ', '   ', '  ', 'I', 'L', 'Yangulam', false, '');
INSERT INTO languages_iso639 VALUES ('ynn', '   ', '   ', '  ', 'I', 'E', 'Yana', false, '');
INSERT INTO languages_iso639 VALUES ('yno', '   ', '   ', '  ', 'I', 'L', 'Yong', false, '');
INSERT INTO languages_iso639 VALUES ('ynq', '   ', '   ', '  ', 'I', 'L', 'Yendang', false, '');
INSERT INTO languages_iso639 VALUES ('yns', '   ', '   ', '  ', 'I', 'L', 'Yansi', false, '');
INSERT INTO languages_iso639 VALUES ('ynu', '   ', '   ', '  ', 'I', 'E', 'Yahuna', false, '');
INSERT INTO languages_iso639 VALUES ('yob', '   ', '   ', '  ', 'I', 'E', 'Yoba', false, '');
INSERT INTO languages_iso639 VALUES ('yog', '   ', '   ', '  ', 'I', 'L', 'Yogad', false, '');
INSERT INTO languages_iso639 VALUES ('yoi', '   ', '   ', '  ', 'I', 'L', 'Yonaguni', false, '');
INSERT INTO languages_iso639 VALUES ('yok', '   ', '   ', '  ', 'I', 'L', 'Yokuts', false, '');
INSERT INTO languages_iso639 VALUES ('yol', '   ', '   ', '  ', 'I', 'E', 'Yola', false, '');
INSERT INTO languages_iso639 VALUES ('yom', '   ', '   ', '  ', 'I', 'L', 'Yombe', false, '');
INSERT INTO languages_iso639 VALUES ('yon', '   ', '   ', '  ', 'I', 'L', 'Yongkom', false, '');
INSERT INTO languages_iso639 VALUES ('yor', 'yor', 'yor', 'yo', 'I', 'L', 'Yoruba', false, '');
INSERT INTO languages_iso639 VALUES ('yot', '   ', '   ', '  ', 'I', 'L', 'Yotti', false, '');
INSERT INTO languages_iso639 VALUES ('yox', '   ', '   ', '  ', 'I', 'L', 'Yoron', false, '');
INSERT INTO languages_iso639 VALUES ('yoy', '   ', '   ', '  ', 'I', 'L', 'Yoy', false, '');
INSERT INTO languages_iso639 VALUES ('ypa', '   ', '   ', '  ', 'I', 'L', 'Phala', false, '');
INSERT INTO languages_iso639 VALUES ('ypb', '   ', '   ', '  ', 'I', 'L', 'Labo Phowa', false, '');
INSERT INTO languages_iso639 VALUES ('ypg', '   ', '   ', '  ', 'I', 'L', 'Phola', false, '');
INSERT INTO languages_iso639 VALUES ('yph', '   ', '   ', '  ', 'I', 'L', 'Phupha', false, '');
INSERT INTO languages_iso639 VALUES ('ypm', '   ', '   ', '  ', 'I', 'L', 'Phuma', false, '');
INSERT INTO languages_iso639 VALUES ('ypn', '   ', '   ', '  ', 'I', 'L', 'Ani Phowa', false, '');
INSERT INTO languages_iso639 VALUES ('ypo', '   ', '   ', '  ', 'I', 'L', 'Alo Phola', false, '');
INSERT INTO languages_iso639 VALUES ('ypp', '   ', '   ', '  ', 'I', 'L', 'Phupa', false, '');
INSERT INTO languages_iso639 VALUES ('ypz', '   ', '   ', '  ', 'I', 'L', 'Phuza', false, '');
INSERT INTO languages_iso639 VALUES ('yra', '   ', '   ', '  ', 'I', 'L', 'Yerakai', false, '');
INSERT INTO languages_iso639 VALUES ('yrb', '   ', '   ', '  ', 'I', 'L', 'Yareba', false, '');
INSERT INTO languages_iso639 VALUES ('yre', '   ', '   ', '  ', 'I', 'L', 'Yaouré', false, '');
INSERT INTO languages_iso639 VALUES ('yri', '   ', '   ', '  ', 'I', 'L', 'Yarí', false, '');
INSERT INTO languages_iso639 VALUES ('yrk', '   ', '   ', '  ', 'I', 'L', 'Nenets', false, '');
INSERT INTO languages_iso639 VALUES ('yrl', '   ', '   ', '  ', 'I', 'L', 'Nhengatu', false, '');
INSERT INTO languages_iso639 VALUES ('yrm', '   ', '   ', '  ', 'I', 'L', 'Yirrk-Mel', false, '');
INSERT INTO languages_iso639 VALUES ('yrn', '   ', '   ', '  ', 'I', 'L', 'Yerong', false, '');
INSERT INTO languages_iso639 VALUES ('yrs', '   ', '   ', '  ', 'I', 'L', 'Yarsun', false, '');
INSERT INTO languages_iso639 VALUES ('yrw', '   ', '   ', '  ', 'I', 'L', 'Yarawata', false, '');
INSERT INTO languages_iso639 VALUES ('yry', '   ', '   ', '  ', 'I', 'L', 'Yarluyandi', false, '');
INSERT INTO languages_iso639 VALUES ('ysc', '   ', '   ', '  ', 'I', 'E', 'Yassic', false, '');
INSERT INTO languages_iso639 VALUES ('ysd', '   ', '   ', '  ', 'I', 'L', 'Samatao', false, '');
INSERT INTO languages_iso639 VALUES ('ysg', '   ', '   ', '  ', 'I', 'L', 'Sonaga', false, '');
INSERT INTO languages_iso639 VALUES ('ysl', '   ', '   ', '  ', 'I', 'L', 'Yugoslavian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('ysn', '   ', '   ', '  ', 'I', 'L', 'Sani', false, '');
INSERT INTO languages_iso639 VALUES ('yso', '   ', '   ', '  ', 'I', 'L', 'Nisi (China)', false, '');
INSERT INTO languages_iso639 VALUES ('ysp', '   ', '   ', '  ', 'I', 'L', 'Southern Lolopo', false, '');
INSERT INTO languages_iso639 VALUES ('ysr', '   ', '   ', '  ', 'I', 'E', 'Sirenik Yupik', false, '');
INSERT INTO languages_iso639 VALUES ('yss', '   ', '   ', '  ', 'I', 'L', 'Yessan-Mayo', false, '');
INSERT INTO languages_iso639 VALUES ('ysy', '   ', '   ', '  ', 'I', 'L', 'Sanie', false, '');
INSERT INTO languages_iso639 VALUES ('yta', '   ', '   ', '  ', 'I', 'L', 'Talu', false, '');
INSERT INTO languages_iso639 VALUES ('ytl', '   ', '   ', '  ', 'I', 'L', 'Tanglang', false, '');
INSERT INTO languages_iso639 VALUES ('ytp', '   ', '   ', '  ', 'I', 'L', 'Thopho', false, '');
INSERT INTO languages_iso639 VALUES ('ytw', '   ', '   ', '  ', 'I', 'L', 'Yout Wam', false, '');
INSERT INTO languages_iso639 VALUES ('yty', '   ', '   ', '  ', 'I', 'E', 'Yatay', false, '');
INSERT INTO languages_iso639 VALUES ('yua', '   ', '   ', '  ', 'I', 'L', 'Yucateco', false, '');
INSERT INTO languages_iso639 VALUES ('yub', '   ', '   ', '  ', 'I', 'E', 'Yugambal', false, '');
INSERT INTO languages_iso639 VALUES ('yuc', '   ', '   ', '  ', 'I', 'L', 'Yuchi', false, '');
INSERT INTO languages_iso639 VALUES ('yud', '   ', '   ', '  ', 'I', 'L', 'Judeo-Tripolitanian Arabic', false, '');
INSERT INTO languages_iso639 VALUES ('yue', '   ', '   ', '  ', 'I', 'L', 'Yue Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('yuf', '   ', '   ', '  ', 'I', 'L', 'Havasupai-Walapai-Yavapai', false, '');
INSERT INTO languages_iso639 VALUES ('yug', '   ', '   ', '  ', 'I', 'E', 'Yug', false, '');
INSERT INTO languages_iso639 VALUES ('yui', '   ', '   ', '  ', 'I', 'L', 'Yurutí', false, '');
INSERT INTO languages_iso639 VALUES ('yuj', '   ', '   ', '  ', 'I', 'L', 'Karkar-Yuri', false, '');
INSERT INTO languages_iso639 VALUES ('yuk', '   ', '   ', '  ', 'I', 'E', 'Yuki', false, '');
INSERT INTO languages_iso639 VALUES ('yul', '   ', '   ', '  ', 'I', 'L', 'Yulu', false, '');
INSERT INTO languages_iso639 VALUES ('yum', '   ', '   ', '  ', 'I', 'L', 'Quechan', false, '');
INSERT INTO languages_iso639 VALUES ('yun', '   ', '   ', '  ', 'I', 'L', 'Bena (Nigeria)', false, '');
INSERT INTO languages_iso639 VALUES ('yup', '   ', '   ', '  ', 'I', 'L', 'Yukpa', false, '');
INSERT INTO languages_iso639 VALUES ('yuq', '   ', '   ', '  ', 'I', 'L', 'Yuqui', false, '');
INSERT INTO languages_iso639 VALUES ('yur', '   ', '   ', '  ', 'I', 'L', 'Yurok', false, '');
INSERT INTO languages_iso639 VALUES ('yut', '   ', '   ', '  ', 'I', 'L', 'Yopno', false, '');
INSERT INTO languages_iso639 VALUES ('yuu', '   ', '   ', '  ', 'I', 'L', 'Yugh', false, '');
INSERT INTO languages_iso639 VALUES ('yuw', '   ', '   ', '  ', 'I', 'L', 'Yau (Morobe Province)', false, '');
INSERT INTO languages_iso639 VALUES ('yux', '   ', '   ', '  ', 'I', 'L', 'Southern Yukaghir', false, '');
INSERT INTO languages_iso639 VALUES ('yuy', '   ', '   ', '  ', 'I', 'L', 'East Yugur', false, '');
INSERT INTO languages_iso639 VALUES ('yuz', '   ', '   ', '  ', 'I', 'L', 'Yuracare', false, '');
INSERT INTO languages_iso639 VALUES ('yva', '   ', '   ', '  ', 'I', 'L', 'Yawa', false, '');
INSERT INTO languages_iso639 VALUES ('yvt', '   ', '   ', '  ', 'I', 'E', 'Yavitero', false, '');
INSERT INTO languages_iso639 VALUES ('ywa', '   ', '   ', '  ', 'I', 'L', 'Kalou', false, '');
INSERT INTO languages_iso639 VALUES ('ywg', '   ', '   ', '  ', 'I', 'L', 'Yinhawangka', false, '');
INSERT INTO languages_iso639 VALUES ('ywl', '   ', '   ', '  ', 'I', 'L', 'Western Lalu', false, '');
INSERT INTO languages_iso639 VALUES ('ywn', '   ', '   ', '  ', 'I', 'L', 'Yawanawa', false, '');
INSERT INTO languages_iso639 VALUES ('ywq', '   ', '   ', '  ', 'I', 'L', 'Wuding-Luquan Yi', false, '');
INSERT INTO languages_iso639 VALUES ('ywr', '   ', '   ', '  ', 'I', 'L', 'Yawuru', false, '');
INSERT INTO languages_iso639 VALUES ('ywt', '   ', '   ', '  ', 'I', 'L', 'Xishanba Lalo', false, '');
INSERT INTO languages_iso639 VALUES ('ywu', '   ', '   ', '  ', 'I', 'L', 'Wumeng Nasu', false, '');
INSERT INTO languages_iso639 VALUES ('yww', '   ', '   ', '  ', 'I', 'E', 'Yawarawarga', false, '');
INSERT INTO languages_iso639 VALUES ('yxa', '   ', '   ', '  ', 'I', 'E', 'Mayawali', false, '');
INSERT INTO languages_iso639 VALUES ('yxg', '   ', '   ', '  ', 'I', 'E', 'Yagara', false, '');
INSERT INTO languages_iso639 VALUES ('yxl', '   ', '   ', '  ', 'I', 'E', 'Yardliyawarra', false, '');
INSERT INTO languages_iso639 VALUES ('yxm', '   ', '   ', '  ', 'I', 'E', 'Yinwum', false, '');
INSERT INTO languages_iso639 VALUES ('yxu', '   ', '   ', '  ', 'I', 'E', 'Yuyu', false, '');
INSERT INTO languages_iso639 VALUES ('yxy', '   ', '   ', '  ', 'I', 'E', 'Yabula Yabula', false, '');
INSERT INTO languages_iso639 VALUES ('yyr', '   ', '   ', '  ', 'I', 'E', 'Yir Yoront', false, '');
INSERT INTO languages_iso639 VALUES ('yyu', '   ', '   ', '  ', 'I', 'L', 'Yau (Sandaun Province)', false, '');
INSERT INTO languages_iso639 VALUES ('yyz', '   ', '   ', '  ', 'I', 'L', 'Ayizi', false, '');
INSERT INTO languages_iso639 VALUES ('yzg', '   ', '   ', '  ', 'I', 'L', 'E''ma Buyang', false, '');
INSERT INTO languages_iso639 VALUES ('yzk', '   ', '   ', '  ', 'I', 'L', 'Zokhuo', false, '');
INSERT INTO languages_iso639 VALUES ('zaa', '   ', '   ', '  ', 'I', 'L', 'Sierra de Juárez Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zab', '   ', '   ', '  ', 'I', 'L', 'San Juan Guelavía Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zac', '   ', '   ', '  ', 'I', 'L', 'Ocotlán Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zad', '   ', '   ', '  ', 'I', 'L', 'Cajonos Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zae', '   ', '   ', '  ', 'I', 'L', 'Yareni Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zaf', '   ', '   ', '  ', 'I', 'L', 'Ayoquesco Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zag', '   ', '   ', '  ', 'I', 'L', 'Zaghawa', false, '');
INSERT INTO languages_iso639 VALUES ('zah', '   ', '   ', '  ', 'I', 'L', 'Zangwal', false, '');
INSERT INTO languages_iso639 VALUES ('zai', '   ', '   ', '  ', 'I', 'L', 'Isthmus Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zaj', '   ', '   ', '  ', 'I', 'L', 'Zaramo', false, '');
INSERT INTO languages_iso639 VALUES ('zak', '   ', '   ', '  ', 'I', 'L', 'Zanaki', false, '');
INSERT INTO languages_iso639 VALUES ('zal', '   ', '   ', '  ', 'I', 'L', 'Zauzou', false, '');
INSERT INTO languages_iso639 VALUES ('zam', '   ', '   ', '  ', 'I', 'L', 'Miahuatlán Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zao', '   ', '   ', '  ', 'I', 'L', 'Ozolotepec Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zap', 'zap', 'zap', '  ', 'M', 'L', 'Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zaq', '   ', '   ', '  ', 'I', 'L', 'Aloápam Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zar', '   ', '   ', '  ', 'I', 'L', 'Rincón Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zas', '   ', '   ', '  ', 'I', 'L', 'Santo Domingo Albarradas Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zat', '   ', '   ', '  ', 'I', 'L', 'Tabaa Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zau', '   ', '   ', '  ', 'I', 'L', 'Zangskari', false, '');
INSERT INTO languages_iso639 VALUES ('zav', '   ', '   ', '  ', 'I', 'L', 'Yatzachi Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zaw', '   ', '   ', '  ', 'I', 'L', 'Mitla Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zax', '   ', '   ', '  ', 'I', 'L', 'Xadani Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zay', '   ', '   ', '  ', 'I', 'L', 'Zayse-Zergulla', false, '');
INSERT INTO languages_iso639 VALUES ('zaz', '   ', '   ', '  ', 'I', 'L', 'Zari', false, '');
INSERT INTO languages_iso639 VALUES ('zbc', '   ', '   ', '  ', 'I', 'L', 'Central Berawan', false, '');
INSERT INTO languages_iso639 VALUES ('zbe', '   ', '   ', '  ', 'I', 'L', 'East Berawan', false, '');
INSERT INTO languages_iso639 VALUES ('zbl', 'zbl', 'zbl', '  ', 'I', 'C', 'Blissymbols', false, '');
INSERT INTO languages_iso639 VALUES ('zbt', '   ', '   ', '  ', 'I', 'L', 'Batui', false, '');
INSERT INTO languages_iso639 VALUES ('zbw', '   ', '   ', '  ', 'I', 'L', 'West Berawan', false, '');
INSERT INTO languages_iso639 VALUES ('zca', '   ', '   ', '  ', 'I', 'L', 'Coatecas Altas Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zch', '   ', '   ', '  ', 'I', 'L', 'Central Hongshuihe Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zdj', '   ', '   ', '  ', 'I', 'L', 'Ngazidja Comorian', false, '');
INSERT INTO languages_iso639 VALUES ('zea', '   ', '   ', '  ', 'I', 'L', 'Zeeuws', false, '');
INSERT INTO languages_iso639 VALUES ('zeg', '   ', '   ', '  ', 'I', 'L', 'Zenag', false, '');
INSERT INTO languages_iso639 VALUES ('zeh', '   ', '   ', '  ', 'I', 'L', 'Eastern Hongshuihe Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zen', 'zen', 'zen', '  ', 'I', 'L', 'Zenaga', false, '');
INSERT INTO languages_iso639 VALUES ('zga', '   ', '   ', '  ', 'I', 'L', 'Kinga', false, '');
INSERT INTO languages_iso639 VALUES ('zgb', '   ', '   ', '  ', 'I', 'L', 'Guibei Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zgh', '   ', '   ', '  ', 'I', 'L', 'Standard Moroccan Tamazight', false, '');
INSERT INTO languages_iso639 VALUES ('zgm', '   ', '   ', '  ', 'I', 'L', 'Minz Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zgn', '   ', '   ', '  ', 'I', 'L', 'Guibian Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zgr', '   ', '   ', '  ', 'I', 'L', 'Magori', false, '');
INSERT INTO languages_iso639 VALUES ('zha', 'zha', 'zha', 'za', 'M', 'L', 'Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zhb', '   ', '   ', '  ', 'I', 'L', 'Zhaba', false, '');
INSERT INTO languages_iso639 VALUES ('zhd', '   ', '   ', '  ', 'I', 'L', 'Dai Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zhi', '   ', '   ', '  ', 'I', 'L', 'Zhire', false, '');
INSERT INTO languages_iso639 VALUES ('zhn', '   ', '   ', '  ', 'I', 'L', 'Nong Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zho', 'chi', 'zho', 'zh', 'M', 'L', 'Chinese', false, '');
INSERT INTO languages_iso639 VALUES ('zhw', '   ', '   ', '  ', 'I', 'L', 'Zhoa', false, '');
INSERT INTO languages_iso639 VALUES ('zia', '   ', '   ', '  ', 'I', 'L', 'Zia', false, '');
INSERT INTO languages_iso639 VALUES ('zib', '   ', '   ', '  ', 'I', 'L', 'Zimbabwe Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('zik', '   ', '   ', '  ', 'I', 'L', 'Zimakani', false, '');
INSERT INTO languages_iso639 VALUES ('zil', '   ', '   ', '  ', 'I', 'L', 'Zialo', false, '');
INSERT INTO languages_iso639 VALUES ('zim', '   ', '   ', '  ', 'I', 'L', 'Mesme', false, '');
INSERT INTO languages_iso639 VALUES ('zin', '   ', '   ', '  ', 'I', 'L', 'Zinza', false, '');
INSERT INTO languages_iso639 VALUES ('zir', '   ', '   ', '  ', 'I', 'E', 'Ziriya', false, '');
INSERT INTO languages_iso639 VALUES ('ziw', '   ', '   ', '  ', 'I', 'L', 'Zigula', false, '');
INSERT INTO languages_iso639 VALUES ('ziz', '   ', '   ', '  ', 'I', 'L', 'Zizilivakan', false, '');
INSERT INTO languages_iso639 VALUES ('zka', '   ', '   ', '  ', 'I', 'L', 'Kaimbulawa', false, '');
INSERT INTO languages_iso639 VALUES ('zkb', '   ', '   ', '  ', 'I', 'E', 'Koibal', false, '');
INSERT INTO languages_iso639 VALUES ('zkd', '   ', '   ', '  ', 'I', 'L', 'Kadu', false, '');
INSERT INTO languages_iso639 VALUES ('zkg', '   ', '   ', '  ', 'I', 'E', 'Koguryo', false, '');
INSERT INTO languages_iso639 VALUES ('zkh', '   ', '   ', '  ', 'I', 'E', 'Khorezmian', false, '');
INSERT INTO languages_iso639 VALUES ('zkk', '   ', '   ', '  ', 'I', 'E', 'Karankawa', false, '');
INSERT INTO languages_iso639 VALUES ('zkn', '   ', '   ', '  ', 'I', 'L', 'Kanan', false, '');
INSERT INTO languages_iso639 VALUES ('zko', '   ', '   ', '  ', 'I', 'E', 'Kott', false, '');
INSERT INTO languages_iso639 VALUES ('zkp', '   ', '   ', '  ', 'I', 'E', 'São Paulo Kaingáng', false, '');
INSERT INTO languages_iso639 VALUES ('zkr', '   ', '   ', '  ', 'I', 'L', 'Zakhring', false, '');
INSERT INTO languages_iso639 VALUES ('zkt', '   ', '   ', '  ', 'I', 'E', 'Kitan', false, '');
INSERT INTO languages_iso639 VALUES ('zku', '   ', '   ', '  ', 'I', 'E', 'Kaurna', false, '');
INSERT INTO languages_iso639 VALUES ('zkv', '   ', '   ', '  ', 'I', 'E', 'Krevinian', false, '');
INSERT INTO languages_iso639 VALUES ('zkz', '   ', '   ', '  ', 'I', 'E', 'Khazar', false, '');
INSERT INTO languages_iso639 VALUES ('zlj', '   ', '   ', '  ', 'I', 'L', 'Liujiang Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zlm', '   ', '   ', '  ', 'I', 'L', 'Malay (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('zln', '   ', '   ', '  ', 'I', 'L', 'Lianshan Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zlq', '   ', '   ', '  ', 'I', 'L', 'Liuqian Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zma', '   ', '   ', '  ', 'I', 'L', 'Manda (Australia)', false, '');
INSERT INTO languages_iso639 VALUES ('zmb', '   ', '   ', '  ', 'I', 'L', 'Zimba', false, '');
INSERT INTO languages_iso639 VALUES ('zmc', '   ', '   ', '  ', 'I', 'E', 'Margany', false, '');
INSERT INTO languages_iso639 VALUES ('zmd', '   ', '   ', '  ', 'I', 'L', 'Maridan', false, '');
INSERT INTO languages_iso639 VALUES ('zme', '   ', '   ', '  ', 'I', 'E', 'Mangerr', false, '');
INSERT INTO languages_iso639 VALUES ('zmf', '   ', '   ', '  ', 'I', 'L', 'Mfinu', false, '');
INSERT INTO languages_iso639 VALUES ('zmg', '   ', '   ', '  ', 'I', 'L', 'Marti Ke', false, '');
INSERT INTO languages_iso639 VALUES ('zmh', '   ', '   ', '  ', 'I', 'E', 'Makolkol', false, '');
INSERT INTO languages_iso639 VALUES ('zmi', '   ', '   ', '  ', 'I', 'L', 'Negeri Sembilan Malay', false, '');
INSERT INTO languages_iso639 VALUES ('zmj', '   ', '   ', '  ', 'I', 'L', 'Maridjabin', false, '');
INSERT INTO languages_iso639 VALUES ('zmk', '   ', '   ', '  ', 'I', 'E', 'Mandandanyi', false, '');
INSERT INTO languages_iso639 VALUES ('zml', '   ', '   ', '  ', 'I', 'L', 'Madngele', false, '');
INSERT INTO languages_iso639 VALUES ('zmm', '   ', '   ', '  ', 'I', 'L', 'Marimanindji', false, '');
INSERT INTO languages_iso639 VALUES ('zmn', '   ', '   ', '  ', 'I', 'L', 'Mbangwe', false, '');
INSERT INTO languages_iso639 VALUES ('zmo', '   ', '   ', '  ', 'I', 'L', 'Molo', false, '');
INSERT INTO languages_iso639 VALUES ('zmp', '   ', '   ', '  ', 'I', 'L', 'Mpuono', false, '');
INSERT INTO languages_iso639 VALUES ('zmq', '   ', '   ', '  ', 'I', 'L', 'Mituku', false, '');
INSERT INTO languages_iso639 VALUES ('zmr', '   ', '   ', '  ', 'I', 'L', 'Maranunggu', false, '');
INSERT INTO languages_iso639 VALUES ('zms', '   ', '   ', '  ', 'I', 'L', 'Mbesa', false, '');
INSERT INTO languages_iso639 VALUES ('zmt', '   ', '   ', '  ', 'I', 'L', 'Maringarr', false, '');
INSERT INTO languages_iso639 VALUES ('zmu', '   ', '   ', '  ', 'I', 'E', 'Muruwari', false, '');
INSERT INTO languages_iso639 VALUES ('zmv', '   ', '   ', '  ', 'I', 'E', 'Mbariman-Gudhinma', false, '');
INSERT INTO languages_iso639 VALUES ('zmw', '   ', '   ', '  ', 'I', 'L', 'Mbo (Democratic Republic of Congo)', false, '');
INSERT INTO languages_iso639 VALUES ('zmx', '   ', '   ', '  ', 'I', 'L', 'Bomitaba', false, '');
INSERT INTO languages_iso639 VALUES ('zmy', '   ', '   ', '  ', 'I', 'L', 'Mariyedi', false, '');
INSERT INTO languages_iso639 VALUES ('zmz', '   ', '   ', '  ', 'I', 'L', 'Mbandja', false, '');
INSERT INTO languages_iso639 VALUES ('zna', '   ', '   ', '  ', 'I', 'L', 'Zan Gula', false, '');
INSERT INTO languages_iso639 VALUES ('zne', '   ', '   ', '  ', 'I', 'L', 'Zande (individual language)', false, '');
INSERT INTO languages_iso639 VALUES ('zng', '   ', '   ', '  ', 'I', 'L', 'Mang', false, '');
INSERT INTO languages_iso639 VALUES ('znk', '   ', '   ', '  ', 'I', 'E', 'Manangkari', false, '');
INSERT INTO languages_iso639 VALUES ('zns', '   ', '   ', '  ', 'I', 'L', 'Mangas', false, '');
INSERT INTO languages_iso639 VALUES ('zoc', '   ', '   ', '  ', 'I', 'L', 'Copainalá Zoque', false, '');
INSERT INTO languages_iso639 VALUES ('zoh', '   ', '   ', '  ', 'I', 'L', 'Chimalapa Zoque', false, '');
INSERT INTO languages_iso639 VALUES ('zom', '   ', '   ', '  ', 'I', 'L', 'Zou', false, '');
INSERT INTO languages_iso639 VALUES ('zoo', '   ', '   ', '  ', 'I', 'L', 'Asunción Mixtepec Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zoq', '   ', '   ', '  ', 'I', 'L', 'Tabasco Zoque', false, '');
INSERT INTO languages_iso639 VALUES ('zor', '   ', '   ', '  ', 'I', 'L', 'Rayón Zoque', false, '');
INSERT INTO languages_iso639 VALUES ('zos', '   ', '   ', '  ', 'I', 'L', 'Francisco León Zoque', false, '');
INSERT INTO languages_iso639 VALUES ('zpa', '   ', '   ', '  ', 'I', 'L', 'Lachiguiri Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpb', '   ', '   ', '  ', 'I', 'L', 'Yautepec Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpc', '   ', '   ', '  ', 'I', 'L', 'Choapan Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpd', '   ', '   ', '  ', 'I', 'L', 'Southeastern Ixtlán Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpe', '   ', '   ', '  ', 'I', 'L', 'Petapa Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpf', '   ', '   ', '  ', 'I', 'L', 'San Pedro Quiatoni Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpg', '   ', '   ', '  ', 'I', 'L', 'Guevea De Humboldt Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zph', '   ', '   ', '  ', 'I', 'L', 'Totomachapan Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpi', '   ', '   ', '  ', 'I', 'L', 'Santa María Quiegolani Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpj', '   ', '   ', '  ', 'I', 'L', 'Quiavicuzas Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpk', '   ', '   ', '  ', 'I', 'L', 'Tlacolulita Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpl', '   ', '   ', '  ', 'I', 'L', 'Lachixío Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpm', '   ', '   ', '  ', 'I', 'L', 'Mixtepec Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpn', '   ', '   ', '  ', 'I', 'L', 'Santa Inés Yatzechi Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpo', '   ', '   ', '  ', 'I', 'L', 'Amatlán Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpp', '   ', '   ', '  ', 'I', 'L', 'El Alto Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpq', '   ', '   ', '  ', 'I', 'L', 'Zoogocho Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpr', '   ', '   ', '  ', 'I', 'L', 'Santiago Xanica Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zps', '   ', '   ', '  ', 'I', 'L', 'Coatlán Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpt', '   ', '   ', '  ', 'I', 'L', 'San Vicente Coatlán Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpu', '   ', '   ', '  ', 'I', 'L', 'Yalálag Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpv', '   ', '   ', '  ', 'I', 'L', 'Chichicapan Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpw', '   ', '   ', '  ', 'I', 'L', 'Zaniza Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpx', '   ', '   ', '  ', 'I', 'L', 'San Baltazar Loxicha Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpy', '   ', '   ', '  ', 'I', 'L', 'Mazaltepec Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zpz', '   ', '   ', '  ', 'I', 'L', 'Texmelucan Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zqe', '   ', '   ', '  ', 'I', 'L', 'Qiubei Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zra', '   ', '   ', '  ', 'I', 'E', 'Kara (Korea)', false, '');
INSERT INTO languages_iso639 VALUES ('zrg', '   ', '   ', '  ', 'I', 'L', 'Mirgan', false, '');
INSERT INTO languages_iso639 VALUES ('zrn', '   ', '   ', '  ', 'I', 'L', 'Zerenkel', false, '');
INSERT INTO languages_iso639 VALUES ('zro', '   ', '   ', '  ', 'I', 'L', 'Záparo', false, '');
INSERT INTO languages_iso639 VALUES ('zrp', '   ', '   ', '  ', 'I', 'E', 'Zarphatic', false, '');
INSERT INTO languages_iso639 VALUES ('zrs', '   ', '   ', '  ', 'I', 'L', 'Mairasi', false, '');
INSERT INTO languages_iso639 VALUES ('zsa', '   ', '   ', '  ', 'I', 'L', 'Sarasira', false, '');
INSERT INTO languages_iso639 VALUES ('zsk', '   ', '   ', '  ', 'I', 'A', 'Kaskean', false, '');
INSERT INTO languages_iso639 VALUES ('zsl', '   ', '   ', '  ', 'I', 'L', 'Zambian Sign Language', false, '');
INSERT INTO languages_iso639 VALUES ('zsm', '   ', '   ', '  ', 'I', 'L', 'Standard Malay', false, '');
INSERT INTO languages_iso639 VALUES ('zsr', '   ', '   ', '  ', 'I', 'L', 'Southern Rincon Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zsu', '   ', '   ', '  ', 'I', 'L', 'Sukurum', false, '');
INSERT INTO languages_iso639 VALUES ('zte', '   ', '   ', '  ', 'I', 'L', 'Elotepec Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztg', '   ', '   ', '  ', 'I', 'L', 'Xanaguía Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztl', '   ', '   ', '  ', 'I', 'L', 'Lapaguía-Guivini Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztm', '   ', '   ', '  ', 'I', 'L', 'San Agustín Mixtepec Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztn', '   ', '   ', '  ', 'I', 'L', 'Santa Catarina Albarradas Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztp', '   ', '   ', '  ', 'I', 'L', 'Loxicha Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztq', '   ', '   ', '  ', 'I', 'L', 'Quioquitani-Quierí Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zts', '   ', '   ', '  ', 'I', 'L', 'Tilquiapan Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztt', '   ', '   ', '  ', 'I', 'L', 'Tejalapan Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztu', '   ', '   ', '  ', 'I', 'L', 'Güilá Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('ztx', '   ', '   ', '  ', 'I', 'L', 'Zaachila Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zty', '   ', '   ', '  ', 'I', 'L', 'Yatee Zapotec', false, '');
INSERT INTO languages_iso639 VALUES ('zua', '   ', '   ', '  ', 'I', 'L', 'Zeem', false, '');
INSERT INTO languages_iso639 VALUES ('zuh', '   ', '   ', '  ', 'I', 'L', 'Tokano', false, '');
INSERT INTO languages_iso639 VALUES ('zul', 'zul', 'zul', 'zu', 'I', 'L', 'Zulu', false, '');
INSERT INTO languages_iso639 VALUES ('zum', '   ', '   ', '  ', 'I', 'L', 'Kumzari', false, '');
INSERT INTO languages_iso639 VALUES ('zun', 'zun', 'zun', '  ', 'I', 'L', 'Zuni', false, '');
INSERT INTO languages_iso639 VALUES ('zuy', '   ', '   ', '  ', 'I', 'L', 'Zumaya', false, '');
INSERT INTO languages_iso639 VALUES ('zwa', '   ', '   ', '  ', 'I', 'L', 'Zay', false, '');
INSERT INTO languages_iso639 VALUES ('zxx', 'zxx', 'zxx', '  ', 'S', 'S', 'No linguistic content', false, '');
INSERT INTO languages_iso639 VALUES ('zyb', '   ', '   ', '  ', 'I', 'L', 'Yongbei Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zyg', '   ', '   ', '  ', 'I', 'L', 'Yang Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zyj', '   ', '   ', '  ', 'I', 'L', 'Youjiang Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zyn', '   ', '   ', '  ', 'I', 'L', 'Yongnan Zhuang', false, '');
INSERT INTO languages_iso639 VALUES ('zyp', '   ', '   ', '  ', 'I', 'L', 'Zyphe Chin', false, '');
INSERT INTO languages_iso639 VALUES ('zza', 'zza', 'zza', '  ', 'M', 'L', 'Zaza', false, '');
INSERT INTO languages_iso639 VALUES ('zzj', '   ', '   ', '  ', 'I', 'L', 'Zuojiang Zhuang', false, '');

--
-- PostgreSQL database dump complete
--

-- Added test data
--
-- Data for Name: thesaurus_format; Type: TABLE DATA; Schema: public; Owner: hadocdb
--

INSERT INTO thesaurus_format VALUES (2, 'PDF 1.7');
INSERT INTO thesaurus_format VALUES (1, 'CSV');
INSERT INTO thesaurus_format VALUES (3, 'XML/SKOS');

--
-- Data for Name: thesaurus_type; Type: TABLE DATA; Schema: public; Owner: hadocdb
--

INSERT INTO thesaurus_type VALUES (1, 'Liste contrôlée');
INSERT INTO thesaurus_type VALUES (2, 'Taxonomie');
INSERT INTO thesaurus_type VALUES (3, 'Thesaurus');




-- schema-1.2.sql ---------------------------- 
-- Table: log_journal
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

-- Table: thesaurus_concept
CREATE TABLE thesaurus_concept
(
  identifier text NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  modified timestamp without time zone NOT NULL DEFAULT now(),
  status text,
  notation text,
  topconcept boolean,
  CONSTRAINT pk_thesaurus_concept_identifier PRIMARY KEY (identifier)
);

-- Table: thesaurus_term
CREATE TABLE thesaurus_term
(
  identifier text NOT NULL,
  lexicalvalue text NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  modified timestamp without time zone NOT NULL DEFAULT now(),
  source text,
  prefered boolean,
  status integer,
  role integer,
  conceptid text,
  thesaurusid text NOT NULL,
  lang character varying(3) NOT NULL,
  CONSTRAINT pk_term_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_term_languages_iso639 FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_term_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: thesaurus -- adding defaulttopconcept
alter table thesaurus add defaulttopconcept boolean NOT NULL DEFAULT FALSE;

-- ALTER TABLE thesaurus_term DROP CONSTRAINT fk_term_thesaurus_concept;
ALTER TABLE thesaurus_term
  ADD CONSTRAINT fk_term_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Indexes on table thesaurus_term
CREATE INDEX idx_thesaurus_term_conceptid
  ON thesaurus_term
  USING btree (conceptid);

CREATE INDEX idx_thesaurus_term_thesaurusid
  ON thesaurus_term
  USING btree (thesaurusid);

-- Adding column thesaurusid to the thesaurus_concept table
ALTER TABLE thesaurus_concept ADD COLUMN thesaurusid text;
ALTER TABLE thesaurus_concept ALTER COLUMN thesaurusid SET NOT NULL;

-- ALTER TABLE thesaurus_concept DROP CONSTRAINT fk_concept_thesaurus;
ALTER TABLE thesaurus_concept
  ADD CONSTRAINT fk_concept_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- DROP INDEX fki_concept_thesaurus;
CREATE INDEX fki_concept_thesaurus
  ON thesaurus_concept
  USING btree
  (thesaurusid COLLATE pg_catalog."default");


CREATE TABLE thesaurus_term_role
(
  code text NOT NULL,
  label text NOT NULL,
  defaultrole boolean NOT NULL
);

ALTER TABLE ONLY thesaurus_term_role
    ADD CONSTRAINT pk_role_identifier PRIMARY KEY (code);

-- Temp hack to add foreign key
UPDATE thesaurus_term SET role = null;

ALTER TABLE thesaurus_term DROP COLUMN role;

ALTER TABLE thesaurus_term ADD COLUMN role text;

-- ALTER TABLE thesaurus_term DROP CONSTRAINT fk_term_thesaurus_concept;
ALTER TABLE thesaurus_term
  ADD CONSTRAINT fk_term_thesaurus_term_role FOREIGN KEY (role)
      REFERENCES thesaurus_term_role (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

CREATE INDEX idx_thesaurus_term_role
  ON thesaurus_term
  USING btree (role);


-- data-1.2.sql ---------------------------- 
INSERT INTO thesaurus_term_role VALUES ('P', 'Partitive', false);
INSERT INTO thesaurus_term_role VALUES ('I', 'Instance', false);
INSERT INTO thesaurus_term_role VALUES ('SP', 'Spelling variant', true);
INSERT INTO thesaurus_term_role VALUES ('MS', 'MisSpelling', false);
INSERT INTO thesaurus_term_role VALUES ('H', 'Hidden label', false);
INSERT INTO thesaurus_term_role VALUES ('AB', 'Abbreviation', false);
INSERT INTO thesaurus_term_role VALUES ('FT', 'Full form of the term', false);-- schema-1.3.sql ---------------------------- 
-- Table: hierarchical_relationship
CREATE TABLE hierarchical_relationship
(
  childconceptid text NOT NULL,
  parentconceptid text NOT NULL,
  CONSTRAINT pk_hierarchical_relationship PRIMARY KEY (childconceptid, parentconceptid)
);

-- Index: idx_parentconceptid
CREATE INDEX idx_hierarchical_relationship_parentconceptid
  ON hierarchical_relationship
  USING btree
  (parentconceptid);


-- Index: idx_childconceptid
CREATE INDEX idx_hierarchical_relationship_childconceptid
  ON hierarchical_relationship
  USING btree
  (childconceptid);

-- Foreign Key to reference the child concept
ALTER TABLE hierarchical_relationship
  ADD CONSTRAINT fk_child_hierarchical_relationship_thesaurus_concept FOREIGN KEY (childconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

-- Foreign Key to reference the parent concept
ALTER TABLE hierarchical_relationship
  ADD CONSTRAINT fk_parent_hierarchical_relationship_thesaurus_concept FOREIGN KEY (parentconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
  
-- Table: top_relationship
CREATE TABLE top_relationship
(
  childconceptid text NOT NULL,
  rootconceptid text NOT NULL,
  CONSTRAINT pk_top_relationship PRIMARY KEY (childconceptid, rootconceptid)
);

-- Index: idx_childconceptid
CREATE INDEX idx_top_relationship_childconceptid
  ON top_relationship
  USING btree
  (childconceptid);

-- Index: idx_parentconceptid
CREATE INDEX idx_top_relationship_rootconceptid
  ON top_relationship
  USING btree
  (rootconceptid);
  
-- Foreign Key to reference the child concept
ALTER TABLE top_relationship
  ADD CONSTRAINT fk_child_top_relationship_thesaurus_concept FOREIGN KEY (childconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
-- Foreign Key to reference the root concept
ALTER TABLE top_relationship
  ADD CONSTRAINT fk_root_top_relationship_thesaurus_concept FOREIGN KEY (rootconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE note_type
(
  code text NOT NULL,
  label text NOT NULL,
  isterm boolean NOT NULL,
  isconcept boolean NOT NULL,
  CONSTRAINT pk_note_type PRIMARY KEY (code),
  CONSTRAINT chk_not_false_values CHECK (NOT (isterm = false AND isconcept = false))
);
      
CREATE TABLE note
(
  identifier text NOT NULL,
  lexicalvalue text NOT NULL,
  lang text NOT NULL,
  source text,
  notetypecode text NOT NULL,
  conceptid text,
  termid text,
  CONSTRAINT pk_note_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_note_languages_iso639 FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_note_notetype FOREIGN KEY (notetypecode)
      REFERENCES note_type (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_note_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_note_thesaurus_term FOREIGN KEY (termid)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX fki_note_languages_iso639
  ON note
  USING btree
  (lang);

CREATE INDEX fki_note_notetype
  ON note
  USING btree
  (notetypecode);

CREATE INDEX fki_note_thesaurus_concept
  ON note
  USING btree
  (conceptid);

CREATE INDEX fki_note_thesaurus_term
  ON note
  USING btree
  (termid);
  
ALTER TABLE note ADD COLUMN created timestamp without time zone DEFAULT now() NOT NULL;
ALTER TABLE note ADD COLUMN modified timestamp without time zone DEFAULT now() NOT NULL;

CREATE TABLE associative_relationship_role
(
  code character varying(255) NOT NULL,
  label character varying(255),
  defaultrole boolean,
  CONSTRAINT associative_relationship_role_pkey PRIMARY KEY (code)
);

CREATE TABLE associative_relationship
(
  conceptid1 character varying(255) NOT NULL,
  conceptid2 character varying(255) NOT NULL,
  "role" character varying(255),
  CONSTRAINT associative_relationship_pkey PRIMARY KEY (conceptid1, conceptid2),  
  CONSTRAINT fk2b76ee66da7e7931 FOREIGN KEY ("role")
      REFERENCES associative_relationship_role (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk2b76ee66fdebaa20 FOREIGN KEY (conceptid1)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk2b76ee66fdebaa21 FOREIGN KEY (conceptid2)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX fki_associative_relationship_concept1
  ON associative_relationship
  USING btree
  (conceptid1);
  
CREATE INDEX fki_associative_relationship_concept2
  ON associative_relationship
  USING btree
  (conceptid2);
  
CREATE INDEX fki_associative_relationship_role
  ON associative_relationship
  USING btree
  (role);

ALTER TABLE  associative_relationship 
    RENAME conceptId1 TO concept1;
    
ALTER TABLE  associative_relationship 
    RENAME conceptId2 TO concept2;

CREATE TABLE thesaurus_array
(
  identifier text NOT NULL,
  ordered boolean DEFAULT false NOT NULL,
  notation text,
  thesaurusid text NOT NULL,
  superordinateconceptid text,
  CONSTRAINT pk_thesaurus_array_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_thesaurus_array_thesaurus_concept FOREIGN KEY (superordinateconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_thesaurus_array_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX fki_thesaurus_array_thesaurus_concept
  ON thesaurus_array
  USING btree
  (superordinateconceptid);

CREATE INDEX fki_thesaurus_array_thesaurus
  ON thesaurus_array
  USING btree
  (thesaurusid);

CREATE TABLE node_label
(
  id integer NOT NULL,
  lexicalvalue text NOT NULL,
  modified  timestamp without time zone DEFAULT now() NOT NULL,
  created  timestamp without time zone DEFAULT now() NOT NULL,
  lang text,
  thesaurusarrayid text NOT NULL,
  CONSTRAINT pk_note_label_id PRIMARY KEY (id),
  CONSTRAINT fk_node_label_thesaurus_array FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE node_label_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE node_label_id_seq OWNED BY node_label.id;

CREATE INDEX fki_node_label_thesaurus_array
  ON node_label
  USING btree
  (thesaurusarrayid);

CREATE TABLE thesaurus_array_concept
(
  thesaurusarrayid text NOT NULL,
  conceptid text NOT NULL,
  CONSTRAINT pk_thesaurus_array_concept PRIMARY KEY (thesaurusarrayid, conceptid),
  CONSTRAINT fk_thesaurus_array_concept_thesaurus_array FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_thesaurus_array_concept_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX fki_thesaurus_array_concept_thesaurus_array
  ON thesaurus_array_concept
  USING btree
  (thesaurusarrayid);
  
CREATE INDEX fki_thesaurus_array_concept_thesaurus_concept
  ON thesaurus_array_concept
  USING btree
  (conceptid);
-- data-1.3.sql ---------------------------- 
INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('scopeNote','Note d''application', false, true);

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('historyNote','Note historique', true, true);

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('definition','Définition', true, false);

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('editorialNote','Note éditoriale', true, false);
    
INSERT INTO associative_relationship_role(
            code, label, defaultrole)
    VALUES ('TA', 'Terme associé', true);
    
    
UPDATE thesaurus_term SET role=null;
DELETE FROM thesaurus_term_role;
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('AB', 'Abréviation', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('AV', 'Appellation vernaculaire', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('D', 'Dénomination', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('EM', 'Employer', true);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('EP', 'Employé', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('FD', 'Forme développée', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('NC', 'Nom commun', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('NS', 'Nom scientifique', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('VO', 'Variante orthographique', false);-- schema-1.4.sql ---------------------------- 
--DROP SEQUENCE concept_group_type_code_seq;
--DROP SEQUENCE concept_group_label_identifier_seq;
--DROP TABLE concept_group_label;
--DROP TABLE concept_group_concepts;
--DROP TABLE concept_group;
--DROP TABLE concept_group_type;

CREATE TABLE concept_group_type
(
  code text NOT NULL,
  label text NOT NULL,
  CONSTRAINT pk_concept_group_type_code PRIMARY KEY (code)
);

CREATE TABLE concept_group
(
  identifier text NOT NULL,
  thesaurusid text NOT NULL,
  conceptgrouptypecode text  NOT NULL,
  CONSTRAINT pk_concept_group_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_concept_group_thesaurus_identifier FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_concept_group_concept_group_type_code FOREIGN KEY (conceptgrouptypecode)
      REFERENCES concept_group_type (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE concept_group_concepts
(
  conceptgroupid text NOT NULL,
  conceptid text NOT NULL,
  CONSTRAINT pk_concept_group_concepts PRIMARY KEY (conceptgroupid, conceptid),
  CONSTRAINT fk_concept_group_concepts_concept_group_identifier FOREIGN KEY (conceptgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_concept_group_concepts_thesaurus_concept_identifier FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE concept_group_concepts DROP CONSTRAINT fk_concept_group_concepts_concept_group_identifier;

ALTER TABLE concept_group_concepts
  ADD CONSTRAINT fk_concept_group_concepts_concept_group_identifier FOREIGN KEY (conceptgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE concept_group_concepts DROP CONSTRAINT fk_concept_group_concepts_thesaurus_concept_identifier;

ALTER TABLE concept_group_concepts
  ADD CONSTRAINT fk_concept_group_concepts_thesaurus_concept_identifier FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE; 

CREATE TABLE concept_group_label
(
  identifier integer NOT NULL,
  lexicalvalue text  NOT NULL,
  created timestamp without time zone DEFAULT now() NOT NULL,
  modified timestamp without time zone DEFAULT now() NOT NULL,
  lang text NOT NULL,
  conceptgroupid text  NOT NULL,
  CONSTRAINT pk_concept_group_label_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_concept_group_label_languages_iso639_id FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_concept_group_label_concept_group_identifier FOREIGN KEY (conceptgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE concept_group_label_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
    
ALTER TABLE thesaurus_concept DROP CONSTRAINT fk_concept_thesaurus;

ALTER TABLE thesaurus_concept
  ADD CONSTRAINT fk_concept_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_array DROP CONSTRAINT fk_thesaurus_array_thesaurus;

ALTER TABLE thesaurus_array
  ADD CONSTRAINT fk_thesaurus_array_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_array_concept DROP CONSTRAINT fk_thesaurus_array_concept_thesaurus_concept;
ALTER TABLE thesaurus_array_concept DROP CONSTRAINT fk_thesaurus_array_concept_thesaurus_array;

ALTER TABLE thesaurus_array_concept
  ADD CONSTRAINT fk_thesaurus_array_concept_thesaurus_array FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


ALTER TABLE thesaurus_array_concept
  ADD CONSTRAINT fk_thesaurus_array_concept_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_languages DROP CONSTRAINT fk_thesaurus_languages_thesaurus_identifier;
ALTER TABLE thesaurus_languages DROP CONSTRAINT fk_thesaurus_languages_languages_iso639_id;

ALTER TABLE thesaurus_languages
  ADD CONSTRAINT fk_thesaurus_languages_thesaurus_identifier FOREIGN KEY (thesaurus_identifier)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_languages
  ADD CONSTRAINT fk_thesaurus_languages_languages_iso639_id FOREIGN KEY (iso639_id)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_term DROP CONSTRAINT fk_term_thesaurus;
ALTER TABLE thesaurus_term DROP CONSTRAINT fk_term_thesaurus_concept;


ALTER TABLE thesaurus_term
  ADD CONSTRAINT fk_term_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_term
  ADD CONSTRAINT fk_term_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE associative_relationship DROP CONSTRAINT fk2b76ee66da7e7931;
ALTER TABLE associative_relationship DROP CONSTRAINT fk2b76ee66fdebaa20;
ALTER TABLE associative_relationship DROP CONSTRAINT fk2b76ee66fdebaa21;

ALTER TABLE associative_relationship
  ADD CONSTRAINT fk_role FOREIGN KEY (role)
      REFERENCES associative_relationship_role (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE associative_relationship
  ADD CONSTRAINT fk_concept1 FOREIGN KEY (concept1)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE associative_relationship
  ADD CONSTRAINT fk_concept2 FOREIGN KEY (concept2)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE languages_iso639 ALTER id TYPE character(5);
ALTER TABLE thesaurus_languages ALTER iso639_id TYPE character(5);

--Adding status to thesaurus_concept
ALTER TABLE thesaurus_concept DROP COLUMN status;
ALTER TABLE thesaurus_concept ADD COLUMN status integer;

-- Index: idx_languages_iso639_part1
CREATE INDEX idx_languages_iso639_part1
  ON languages_iso639
  USING btree
  (part1);
  
-- Constraint to define unique term = lexicalvalue + thesaurusid + lang
ALTER TABLE thesaurus_term
  ADD CONSTRAINT chk_term_uniq UNIQUE(lexicalvalue, thesaurusid, lang);
  
ALTER TABLE thesaurus_term ALTER lang TYPE character(5);
ALTER TABLE node_label ALTER lang TYPE character(5);

ALTER TABLE node_label
 ADD CONSTRAINT fk_node_label_languages_iso639 FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE concept_group_label ALTER lang TYPE character(5);

ALTER TABLE note ALTER lang TYPE character(5);

ALTER TABLE thesaurus_concept ALTER COLUMN status SET DEFAULT 0;
ALTER TABLE thesaurus_term ALTER COLUMN status SET DEFAULT 0;

ALTER TABLE associative_relationship ALTER concept1 TYPE text;
ALTER TABLE associative_relationship ALTER concept2 TYPE text;
ALTER TABLE associative_relationship ALTER "role" TYPE text;
-- data-1.4.sql ---------------------------- 
--Concept Group Types
INSERT INTO concept_group_type VALUES ('D','Domaine');
INSERT INTO concept_group_type VALUES ('T','Thématique');
INSERT INTO concept_group_type VALUES ('F','Facette');

INSERT INTO languages_iso639(
            id, part2b, part2t, part1, scope, "type", ref_name, toplanguage, 
            "comment")
    VALUES ('fr-FR', '', '', 'fr', 'I', 'L', 'French', true, 
            '');            
            
UPDATE thesaurus_type SET label='Thésaurus' WHERE label='Thesaurus';

UPDATE thesaurus_term SET lang='fr-FR'  WHERE lang='fra';
UPDATE thesaurus_languages SET iso639_id='fr-FR'  WHERE iso639_id='fra';
UPDATE node_label SET lang='fr-FR'  WHERE lang='fra';
UPDATE concept_group_label SET lang='fr-FR'  WHERE lang='fra';
UPDATE note SET lang='fr-FR'  WHERE lang='fra';
DELETE FROM languages_iso639 WHERE id='fra';

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('example', 'Exemple', false, true);
    
UPDATE thesaurus_concept SET status=1;
UPDATE thesaurus_term SET status=1;-- schema-1.5.sql ---------------------------- 
-- ALTER TABLE thesaurus_term DROP COLUMN hidden;
ALTER TABLE thesaurus_term ADD COLUMN hidden boolean NOT NULL DEFAULT false;

--ALTER TABLE thesaurus_term
--  ADD CONSTRAINT chk_hidden_values CHECK (NOT (prefered = true AND hidden = true));

ALTER TABLE thesaurus_version_history DROP CONSTRAINT fk_thesaurus_organization_thesaurus;
ALTER TABLE thesaurus_version_history
  ADD CONSTRAINT fk_thesaurus_version_history_thesaurus FOREIGN KEY (thesaurus_identifier)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_version_history DROP COLUMN currentversion;
ALTER TABLE thesaurus_version_history ADD COLUMN status text NOT NULL;

ALTER TABLE thesaurus_version_history DROP COLUMN thisversion;
ALTER TABLE thesaurus_version_history ADD COLUMN thisversion boolean NOT NULL DEFAULT false;

ALTER TABLE thesaurus_version_history DROP COLUMN status;
ALTER TABLE thesaurus_version_history ADD COLUMN status integer NOT NULL DEFAULT 0;

--create unique index chk_uniq_thisversion on thesaurus_version_history(thesaurus_identifier, thisversion) where thisversion;

-- Table: admin_user_id
CREATE TABLE admin_user_id
(
  identifier text NOT NULL,
  CONSTRAINT pk_admin_user_id PRIMARY KEY (identifier)
);

CREATE TABLE thesaurus_ark
(
  identifier text NOT NULL,
  created timestamp without time zone,
  entity text,
  CONSTRAINT thesaurus_ark_pkey PRIMARY KEY (identifier)
);

CREATE FUNCTION int_to_text(INT4) RETURNS TEXT AS 'SELECT textin(int4out($1));' LANGUAGE SQL STRICT IMMUTABLE;
CREATE CAST (INT4 AS TEXT) WITH FUNCTION int_to_text(INT4) AS IMPLICIT;

ALTER TABLE thesaurus_version_history ADD COLUMN userid text;

ALTER TABLE thesaurus ADD COLUMN archived boolean DEFAULT FALSE;
ALTER TABLE thesaurus ADD COLUMN ispolyhierarchical boolean DEFAULT FALSE;

ALTER TABLE languages_iso639 DROP COLUMN part2b;
ALTER TABLE languages_iso639 DROP COLUMN part2t;
ALTER TABLE languages_iso639 DROP COLUMN scope;
ALTER TABLE languages_iso639 DROP COLUMN type;
ALTER TABLE languages_iso639 DROP COLUMN comment;
ALTER TABLE languages_iso639 ADD COLUMN principallanguage boolean DEFAULT FALSE;-- data-1.5.sql ---------------------------- 
INSERT INTO admin_user_id(identifier)
    VALUES ('admin');
    
    
DELETE FROM languages_iso639 where toplanguage =false;
UPDATE languages_iso639 SET principallanguage=true;

UPDATE languages_iso639 SET ref_name='Français/France' where id='fr-FR';
INSERT INTO languages_iso639(
            id, part1, ref_name, toplanguage, principallanguage)
    VALUES ('en-US', 'en', 'Anglais/USA', true, true);
  
INSERT INTO languages_iso639(
            id, part1, ref_name, toplanguage, principallanguage)
    VALUES ('it-IT', 'it', 'Italien/Italie', true, true);
    
INSERT INTO languages_iso639(
            id, part1, ref_name, toplanguage, principallanguage)
    VALUES ('de-DE', 'de', 'Allemand/Allemagne', true, true);   

INSERT INTO languages_iso639(
            id, part1, ref_name, toplanguage, principallanguage)
    VALUES ('en-UK', 'en', 'Anglais/United Kingdom', true, false);   

UPDATE thesaurus_term SET lang='en-US' where lang='eng';
UPDATE thesaurus_term SET lang='it-IT' where lang='ita';
UPDATE thesaurus_term SET lang='de-DE' where lang='deu';

UPDATE thesaurus_languages SET iso639_id='en-US' where iso639_id='eng';
UPDATE thesaurus_languages SET iso639_id='it-IT' where iso639_id='ita';
UPDATE thesaurus_languages SET iso639_id='de-DE' where iso639_id='deu';

UPDATE node_label SET lang='en-US'  WHERE lang='eng';
UPDATE node_label SET lang='it-IT'  WHERE lang='ita';
UPDATE node_label SET lang='de-DE'  WHERE lang='deu';

UPDATE concept_group_label SET lang='en-US'  WHERE lang='eng';
UPDATE concept_group_label SET lang='it-IT'  WHERE lang='ita';
UPDATE concept_group_label SET lang='de-DE'  WHERE lang='deu';

UPDATE note SET lang='en-US'  WHERE lang='eng';
UPDATE note SET lang='it-IT'  WHERE lang='ita';
UPDATE note SET lang='de-DE'  WHERE lang='deu';

DELETE FROM languages_iso639 where id='deu';
DELETE FROM languages_iso639 where id='eng';
DELETE FROM languages_iso639 where id='ita';
-- schema-1.6.sql ---------------------------- 
--Add role to hierarchical relationships
ALTER TABLE hierarchical_relationship ADD COLUMN role integer NOT NULL DEFAULT 0;

--Add parent to a group concept
ALTER TABLE concept_group ADD COLUMN parentgroupid text;

ALTER TABLE concept_group
  ADD CONSTRAINT fk_concept_group FOREIGN KEY (parentgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      

ALTER TABLE thesaurus_array_concept ADD COLUMN arrayorder integer NOT NULL DEFAULT 0;

CREATE TABLE split_nonpreferredterm
(
  identifier text NOT NULL,
  lexicalvalue text NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  modified timestamp without time zone NOT NULL DEFAULT now(),
  source text,
  status integer,
  thesaurusid text NOT NULL,
  lang character(5) NOT NULL,
  CONSTRAINT pk_snpt_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_snpt_languages_iso639 FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_snpt_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX idx_split_nonpreferredterm_thesaurusid
  ON split_nonpreferredterm
  USING btree
  (thesaurusid);

CREATE TABLE compound_equivalence
(
  id_split_nonpreferredterm text NOT NULL,
  id_preferredterm text NOT NULL,
  CONSTRAINT compound_equivalence_pk PRIMARY KEY (id_split_nonpreferredterm, id_preferredterm),
  CONSTRAINT fk_preferredterm FOREIGN KEY (id_preferredterm)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_split_nonpreferredterm FOREIGN KEY (id_split_nonpreferredterm)
      REFERENCES split_nonpreferredterm (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE concept_group DROP CONSTRAINT fk_concept_group;

ALTER TABLE concept_group
  ADD CONSTRAINT fk_concept_group FOREIGN KEY (parentgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;


CREATE TABLE custom_concept_attribute_type
(
  identifier integer NOT NULL,
  code text,
  thesaurusid text,
  value text,
  CONSTRAINT pk_custom_concept_attribute_type PRIMARY KEY (identifier),
  CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid)
  REFERENCES thesaurus (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_custom_concept

-- DROP INDEX fki_custom_concept;

CREATE INDEX fki_custom_concept
ON custom_concept_attribute_type
USING btree
(thesaurusid);

CREATE TABLE custom_term_attribute_type
(
  identifier integer NOT NULL,
  code text,
  thesaurusid text,
  value text,
  CONSTRAINT pk_custom_term_attribute_type PRIMARY KEY (identifier),
  CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid)
  REFERENCES thesaurus (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_custom_term

-- DROP INDEX fki_custom_term;

CREATE INDEX fki_custom_term
ON custom_term_attribute_type
USING btree
(thesaurusid);


CREATE SEQUENCE custom_term_attribute_type_identifier_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE SEQUENCE custom_concept_attribute_type_identifier_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE custom_term_attribute
(
  identifier text NOT NULL,
  termid text NOT NULL,
  typeid integer NOT NULL,
  lang text NOT NULL,
  lexicalvalue text NOT NULL,
  CONSTRAINT pk_custom_term_attribute PRIMARY KEY (identifier),
  CONSTRAINT fk_custom_term_attribute_lang FOREIGN KEY (lang)
  REFERENCES languages_iso639 (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_term_attribute_termid FOREIGN KEY (termid)
  REFERENCES thesaurus_term (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_term_attribute_typeid FOREIGN KEY (typeid)
  REFERENCES custom_term_attribute_type (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_lang

-- DROP INDEX fki_lang;

CREATE INDEX fki_custom_term_attribute_lang
ON custom_term_attribute
USING btree
(lang);

-- Index: fki_termid

-- DROP INDEX fki_termid;

CREATE INDEX fki_custom_term_attribute_termid
ON custom_term_attribute
USING btree
(termid);

-- Index: fki_typeid

-- DROP INDEX fki_typeid;

CREATE INDEX fki_custom_term_attribute_typeid
ON custom_term_attribute
USING btree
(typeid);

CREATE TABLE custom_concept_attribute
(
  identifier text NOT NULL,
  conceptid text NOT NULL,
  typeid integer NOT NULL,
  lang text NOT NULL,
  lexicalvalue text NOT NULL,
  CONSTRAINT pk_custom_concept_attribute PRIMARY KEY (identifier),
  CONSTRAINT fk_custom_concept_attribute_lang FOREIGN KEY (lang)
  REFERENCES languages_iso639 (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_concept_attribute_conceptid FOREIGN KEY (conceptid)
  REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_concept_attribute_typeid FOREIGN KEY (typeid)
  REFERENCES custom_concept_attribute_type (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_lang

-- DROP INDEX fki_lang;

CREATE INDEX fki_custom_concept_attribute_lang
ON custom_concept_attribute
USING btree
(lang);

-- Index: fki_conceptid

-- DROP INDEX fki_conceptid;

CREATE INDEX fki_custom_concept_attribute_conceptid
ON custom_concept_attribute
USING btree
(conceptid);

-- Index: fki_typeid

-- DROP INDEX fki_typeid;

CREATE INDEX fki_custom_concept_attribute_typeid
ON custom_concept_attribute
USING btree
(typeid);

--Add parent to a array concept
ALTER TABLE thesaurus_array ADD COLUMN parentarrayid text;

ALTER TABLE thesaurus_array
  ADD CONSTRAINT fk_concept_array FOREIGN KEY (parentarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
      
ALTER TABLE custom_term_attribute DROP CONSTRAINT fk_custom_term_attribute_termid;

ALTER TABLE custom_term_attribute
  ADD CONSTRAINT fk_custom_term_attribute_termid FOREIGN KEY (termid)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE custom_concept_attribute DROP CONSTRAINT fk_custom_concept_attribute_conceptid;

ALTER TABLE custom_concept_attribute
  ADD CONSTRAINT fk_custom_concept_attribute_conceptid FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE custom_term_attribute DROP CONSTRAINT fk_custom_term_attribute_typeid;

ALTER TABLE custom_term_attribute
  ADD CONSTRAINT fk_custom_term_attribute_typeid FOREIGN KEY (typeid)
      REFERENCES custom_term_attribute_type (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE custom_concept_attribute DROP CONSTRAINT fk_custom_concept_attribute_typeid;

ALTER TABLE custom_concept_attribute
  ADD CONSTRAINT fk_custom_concept_attribute_typeid FOREIGN KEY (typeid)
      REFERENCES custom_concept_attribute_type (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE custom_term_attribute_type DROP CONSTRAINT fk_thesaurus_id;

ALTER TABLE custom_term_attribute_type
  ADD CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE custom_concept_attribute_type DROP CONSTRAINT fk_thesaurus_id;

ALTER TABLE custom_concept_attribute_type
   ADD CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

-- data-1.6.sql ---------------------------- 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('cy', 'cy', 'Gallois', true, true);
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('de-CH', 'de', 'Allemand/suisse', true, false);
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('de-IT', 'de', 'Allemand/Italie', true, false);
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('el', 'el', 'Grec', true, true);
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('es', 'es', 'Espagnol', true, true);
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('fr-BE', 'fr', 'Français/Belgique', true, false); 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('fr-CA', 'fr', 'Français/Canada', true, false); 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('fr-CH', 'fr', 'Français/Suisse', true, false); 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('it-CH', 'it', 'Italien/Suisse', true, false); 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('nl-BE', 'nl', 'Néerlandais/Belgique', true, false); 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('nl-NL', 'nl', 'Néerlandais/Pays-Bas', true, true); 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('pt', 'pt', 'Portugais', true, false); 
INSERT INTO languages_iso639(id, part1, ref_name, toplanguage, principallanguage) VALUES ('ru', 'ru', 'Russe', true, true); -- schema-1.7.sql ---------------------------- 
ALTER TABLE thesaurus DROP COLUMN format;

CREATE TABLE thesaurus_formats (
	format_identifier integer NOT NULL,
	thesaurus_identifier text NOT NULL
);

ALTER TABLE ONLY thesaurus_formats
    ADD CONSTRAINT pk_thesaurus_formats PRIMARY KEY (format_identifier, thesaurus_identifier);

ALTER TABLE thesaurus_formats
  ADD CONSTRAINT fk_thesaurus_formats_format_identifier FOREIGN KEY (format_identifier)
      REFERENCES thesaurus_format (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
    
ALTER TABLE thesaurus_formats
  ADD CONSTRAINT fk_thesaurus_formats_thesaurus_identifier FOREIGN KEY (thesaurus_identifier)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


-- schema-audit-1.3.sql ---------------------------- 
DROP TABLE IF EXISTS log_journal;
--
-- Name: revinfo; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE revinfo (
    rev integer NOT NULL,
    revtstmp bigint,
    username character varying(255)
);



--
-- Name: revinfo_identifier_seq; Type: SEQUENCE; Schema: public; Owner: hadocdb
--

CREATE SEQUENCE revinfo_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: revinfoentitytypes; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE revinfoentitytypes (
    id integer NOT NULL,
    entityclassname character varying(255),
    revision integer
);



--
-- Name: revinfoentitytypes_identifier_seq; Type: SEQUENCE; Schema: public; Owner: hadocdb
--

CREATE SEQUENCE revinfoentitytypes_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: thesaurus_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_aud (
    identifier character varying(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    contributor character varying(255),
    coverage character varying(255),
    date timestamp without time zone,
    description character varying(255),
    publisher character varying(255),
    relation character varying(255),
    rights character varying(255),
    source character varying(255),
    subject character varying(255),
    title character varying(255),
    created timestamp without time zone,
    defaulttopconcept boolean,
    format integer,
    type integer,
    creator integer
);



--
-- Name: thesaurus_languages_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_languages_aud (
    rev integer NOT NULL,
    thesaurus_identifier character varying(255) NOT NULL,
    iso639_id character varying(255) NOT NULL,
    revtype smallint
);





--
-- Name: thesaurus_term_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_term_aud (
    identifier character varying(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    lexicalvalue character varying(255),
    created timestamp without time zone,
    modified timestamp without time zone,
    source character varying(255),
    prefered boolean,
    status integer,
    role character varying(255),
    lang character varying(255),
    thesaurusid character varying(255),
    conceptid character varying(255)
);



--
-- Name: thesaurus_thesaurusterm_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_thesaurusterm_aud (
    rev integer NOT NULL,
    thesaurusid character varying(255) NOT NULL,
    identifier character varying(255) NOT NULL,
    revtype smallint
);



--
-- Name: thesaurus_thesaurusversionhistory_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_thesaurusversionhistory_aud (
    rev integer NOT NULL,
    identifier character varying(255) NOT NULL,
    revtype smallint
);



--
-- Name: revinfo_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- Name: revinfoentitytypes_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY revinfoentitytypes
    ADD CONSTRAINT revinfoentitytypes_pkey PRIMARY KEY (id);


--
-- Name: thesaurus_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_aud
    ADD CONSTRAINT thesaurus_aud_pkey PRIMARY KEY (identifier, rev);


--
-- Name: thesaurus_languages_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_languages_aud
    ADD CONSTRAINT thesaurus_languages_aud_pkey PRIMARY KEY (rev, thesaurus_identifier, iso639_id);


--
-- Name: thesaurus_term_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_term_aud
    ADD CONSTRAINT thesaurus_term_aud_pkey PRIMARY KEY (identifier, rev);


--
-- Name: thesaurus_thesaurusterm_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_thesaurusterm_aud
    ADD CONSTRAINT thesaurus_thesaurusterm_aud_pkey PRIMARY KEY (rev, thesaurusid, identifier);


--
-- Name: thesaurus_thesaurusversionhistory_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_thesaurusversionhistory_aud
    ADD CONSTRAINT thesaurus_thesaurusversionhistory_aud_pkey PRIMARY KEY (rev, identifier);


--
-- Name: fk29bf1de7d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_languages_aud
    ADD CONSTRAINT fk29bf1de7d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- Name: fk85e9bf12d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_thesaurusterm_aud
    ADD CONSTRAINT fk85e9bf12d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);

--
-- Name: fkb34829cbd0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_aud
    ADD CONSTRAINT fkb34829cbd0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- Name: fkbfbbfde2d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_term_aud
    ADD CONSTRAINT fkbfbbfde2d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- Name: fke94d7a05c144bbed; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY revinfoentitytypes
    ADD CONSTRAINT fke94d7a05c144bbed FOREIGN KEY (revision) REFERENCES revinfo(rev);


--
-- Name: fke9bd902d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_thesaurusversionhistory_aud
    ADD CONSTRAINT fke9bd902d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);
    
    
    
--
-- Name: hierarchical_relationship_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE hierarchical_relationship_aud (
    rev integer NOT NULL,
    childconceptid character varying(255) NOT NULL,
    parentconceptid character varying(255) NOT NULL,
    revtype smallint
);

--
-- Name: thesaurus_concept_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_concept_aud (
    identifier character varying(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    created timestamp without time zone,
    modified timestamp without time zone,
    status character varying(255),
    notation character varying(255),
    topconcept boolean,
    thesaurusid character varying(255)
);

--
-- Name: top_relationship_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE top_relationship_aud (
    rev integer NOT NULL,
    childconceptid character varying(255) NOT NULL,
    rootconceptid character varying(255) NOT NULL,
    revtype smallint
);

--
-- Name: hierarchical_relationship_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY hierarchical_relationship_aud
    ADD CONSTRAINT hierarchical_relationship_aud_pkey PRIMARY KEY (rev, childconceptid, parentconceptid);

    --
-- Name: fkcd658dffd0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY hierarchical_relationship_aud
    ADD CONSTRAINT fkcd658dffd0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);

--
-- Name: thesaurus_concept_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_concept_aud
    ADD CONSTRAINT thesaurus_concept_aud_pkey PRIMARY KEY (identifier, rev);
    
    --
-- Name: fk2cf9f474d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_concept_aud
    ADD CONSTRAINT fk2cf9f474d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);

    --
-- Name: top_relationship_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY top_relationship_aud
    ADD CONSTRAINT top_relationship_aud_pkey PRIMARY KEY (rev, childconceptid, rootconceptid);

--
-- Name: fkb18c9db3d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY top_relationship_aud
    ADD CONSTRAINT fkb18c9db3d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);
    
CREATE TABLE associative_relationship_aud
(
  concept1 character varying(255) NOT NULL,
  concept2 character varying(255) NOT NULL,
  rev integer NOT NULL,
  revtype smallint,
  "role" character varying(255)
); 
ALTER TABLE ONLY associative_relationship_aud
    ADD CONSTRAINT associative_relationship_aud_pkey PRIMARY KEY (rev, concept1, concept2);
    
ALTER TABLE ONLY associative_relationship_aud
    ADD CONSTRAINT fka0197937d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);
-- schema-audit-1.4.sql ---------------------------- 
ALTER TABLE thesaurus_aud ALTER identifier TYPE text;
ALTER TABLE thesaurus_aud ALTER contributor TYPE text;
ALTER TABLE thesaurus_aud ALTER coverage TYPE text;
ALTER TABLE thesaurus_aud ALTER description TYPE text;
ALTER TABLE thesaurus_aud ALTER publisher TYPE text;
ALTER TABLE thesaurus_aud ALTER relation TYPE text;
ALTER TABLE thesaurus_aud ALTER rights TYPE text;
ALTER TABLE thesaurus_aud ALTER source TYPE text;
ALTER TABLE thesaurus_aud ALTER subject TYPE text;
ALTER TABLE thesaurus_aud ALTER title TYPE text;

ALTER TABLE thesaurus_languages_aud ALTER thesaurus_identifier TYPE text;

ALTER TABLE thesaurus_term_aud ALTER identifier TYPE text;
ALTER TABLE thesaurus_term_aud ALTER lexicalvalue TYPE text;
ALTER TABLE thesaurus_term_aud ALTER source TYPE text;
ALTER TABLE thesaurus_term_aud ALTER conceptid TYPE text;
ALTER TABLE thesaurus_term_aud ALTER thesaurusid TYPE text;

ALTER TABLE thesaurus_thesaurusterm_aud ALTER thesaurusid TYPE text;
ALTER TABLE thesaurus_thesaurusterm_aud ALTER identifier TYPE text;

ALTER TABLE thesaurus_thesaurusversionhistory_aud ALTER identifier TYPE text;

ALTER TABLE hierarchical_relationship_aud ALTER childconceptid TYPE text;
ALTER TABLE hierarchical_relationship_aud ALTER parentconceptid TYPE text;

ALTER TABLE thesaurus_concept_aud ALTER identifier TYPE text;
ALTER TABLE thesaurus_concept_aud ALTER notation TYPE text;
ALTER TABLE thesaurus_concept_aud ALTER thesaurusid TYPE text;

ALTER TABLE top_relationship_aud ALTER childconceptid TYPE text;
ALTER TABLE top_relationship_aud ALTER rootconceptid TYPE text;

ALTER TABLE associative_relationship_aud ALTER concept1 TYPE text;
ALTER TABLE associative_relationship_aud ALTER concept2 TYPE text;
ALTER TABLE associative_relationship_aud ALTER "role" TYPE text;-- schema-audit-1.5.sql ---------------------------- 
TRUNCATE TABLE thesaurus_term_aud,
thesaurus_aud,
thesaurus_concept_aud,
thesaurus_languages_aud,
thesaurus_thesaurusterm_aud,
thesaurus_thesaurusversionhistory_aud,
hierarchical_relationship_aud,
top_relationship_aud,
associative_relationship_aud,
revinfo,
revinfoentitytypes;

--ALTER TABLE thesaurus_term_aud DROP COLUMN hidden;
ALTER TABLE thesaurus_term_aud ADD COLUMN hidden boolean DEFAULT false;

-- ERREUR ordre en double !!! ALTER TABLE thesaurus_aud ADD COLUMN archived boolean;
ALTER TABLE revinfo ADD COLUMN thesaurusid text; 

ALTER TABLE thesaurus_aud ADD COLUMN archived boolean;

ALTER TABLE thesaurus_aud ADD COLUMN ispolyhierarchical boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN prefered_mod boolean; 
ALTER TABLE thesaurus_term_aud ADD COLUMN lexicalvalue_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN created_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN modified_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN source_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN hidden_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN status_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN role_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN language_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN thesaurus_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN concept_mod boolean;

ALTER TABLE thesaurus_concept_aud ADD COLUMN created_mod boolean; 
ALTER TABLE thesaurus_concept_aud ADD COLUMN modified_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN status_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN notation_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN topconcept_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN thesaurus_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN parentconcepts_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN rootconcepts_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN associativerelationshipleft_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN associativerelationshipright_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN conceptarrays_mod boolean;
-- schema-audit-1.6.sql ---------------------------- 
ALTER TABLE hierarchical_relationship_aud ADD COLUMN role integer;


CREATE TABLE thesaurus_array_concept_aud (
    rev integer NOT NULL,
    arrayorder integer,
    thesaurusarrayid character varying(255) NOT NULL,
    conceptId character varying(255) NOT NULL,
    revtype smallint
);

-- schema-audit-1.7.sql ----------------------------

ALTER TABLE thesaurus_aud DROP COLUMN format;

CREATE TABLE thesaurus_formats_aud (
    rev integer NOT NULL,
    thesaurus_identifier text NOT NULL,
    format_identifier integer NOT NULL,
    revtype smallint
);

ALTER TABLE ONLY thesaurus_formats_aud
    ADD CONSTRAINT thesaurus_formats_aud_pkey PRIMARY KEY (rev, thesaurus_identifier, format_identifier);
