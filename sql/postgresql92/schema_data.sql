--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.2
-- Dumped by pg_dump version 9.3.2
-- Started on 2013-12-17 10:52:41 CET

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 235 (class 3079 OID 12595)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 3387 (class 0 OID 0)
-- Dependencies: 235
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 236 (class 1255 OID 20498)
-- Name: int_to_text(integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION int_to_text(integer) RETURNS text
    LANGUAGE sql IMMUTABLE STRICT
    AS $_$SELECT textin(int4out($1));$_$;


SET search_path = pg_catalog;

--
-- TOC entry 2809 (class 2605 OID 20499)
-- Name: CAST (integer AS text); Type: CAST; Schema: pg_catalog; Owner: -
--

CREATE CAST (integer AS text) WITH FUNCTION public.int_to_text(integer) AS IMPLICIT;


SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 196 (class 1259 OID 20482)
-- Name: admin_user_id; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE admin_user_id (
    identifier text NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 20927)
-- Name: alignment; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE alignment (
    identifier text NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone DEFAULT now() NOT NULL,
    author text,
    source_concept_id text,
    alignment_type integer,
    external_target_thesaurus_id text,
    internal_target_thesaurus_id text,
    and_relation boolean NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 20947)
-- Name: alignment_concept; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE alignment_concept (
    identifier integer NOT NULL,
    external_target_concept_id text,
    internal_target_concept_id text,
    alignment_id text
);


--
-- TOC entry 226 (class 1259 OID 20975)
-- Name: alignment_concept_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE alignment_concept_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3388 (class 0 OID 0)
-- Dependencies: 226
-- Name: alignment_concept_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE alignment_concept_identifier_seq OWNED BY alignment_concept.identifier;


--
-- TOC entry 224 (class 1259 OID 20965)
-- Name: alignment_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE alignment_type (
    identifier integer NOT NULL,
    label text NOT NULL,
    isocode text NOT NULL,
    default_type boolean NOT NULL,
    multi_concept boolean NOT NULL
);


--
-- TOC entry 225 (class 1259 OID 20973)
-- Name: alignment_type_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE alignment_type_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3389 (class 0 OID 0)
-- Dependencies: 225
-- Name: alignment_type_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE alignment_type_identifier_seq OWNED BY alignment_type.identifier;


--
-- TOC entry 186 (class 1259 OID 20103)
-- Name: associative_relationship; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE associative_relationship (
    concept1 text NOT NULL,
    concept2 text NOT NULL,
    role text
);


--
-- TOC entry 219 (class 1259 OID 20854)
-- Name: associative_relationship_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE associative_relationship_aud (
    concept1 text NOT NULL,
    concept2 text NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    role text
);


--
-- TOC entry 185 (class 1259 OID 20095)
-- Name: associative_relationship_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE associative_relationship_role (
    code character varying(255) NOT NULL,
    label character varying(255),
    defaultrole boolean,
    skoslabel text
);


--
-- TOC entry 199 (class 1259 OID 20574)
-- Name: compound_equivalence; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE compound_equivalence (
    id_split_nonpreferredterm text NOT NULL,
    id_preferredterm text NOT NULL
);


--
-- TOC entry 192 (class 1259 OID 20196)
-- Name: concept_group; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE concept_group (
    identifier text NOT NULL,
    thesaurusid text NOT NULL,
    conceptgrouptypecode text NOT NULL,
    parentgroupid text,
    notation text,
    isdynamic boolean DEFAULT false,
    parentconceptid text
);


--
-- TOC entry 193 (class 1259 OID 20214)
-- Name: concept_group_concepts; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE concept_group_concepts (
    conceptgroupid text NOT NULL,
    conceptid text NOT NULL
);


--
-- TOC entry 194 (class 1259 OID 20242)
-- Name: concept_group_label; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE concept_group_label (
    identifier integer NOT NULL,
    lexicalvalue text NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone DEFAULT now() NOT NULL,
    lang character varying(5) NOT NULL,
    conceptgroupid text NOT NULL
);


--
-- TOC entry 195 (class 1259 OID 20262)
-- Name: concept_group_label_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE concept_group_label_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 191 (class 1259 OID 20188)
-- Name: concept_group_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE concept_group_type (
    code text NOT NULL,
    label text NOT NULL,
    skoslabel text
);


--
-- TOC entry 205 (class 1259 OID 20655)
-- Name: custom_concept_attribute; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE custom_concept_attribute (
    identifier text NOT NULL,
    conceptid text NOT NULL,
    typeid integer NOT NULL,
    lang character varying(5) NOT NULL,
    lexicalvalue text NOT NULL
);


--
-- TOC entry 200 (class 1259 OID 20597)
-- Name: custom_concept_attribute_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE custom_concept_attribute_type (
    identifier integer NOT NULL,
    code text,
    thesaurusid text,
    value text,
    exportable boolean
);


--
-- TOC entry 203 (class 1259 OID 20627)
-- Name: custom_concept_attribute_type_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE custom_concept_attribute_type_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 204 (class 1259 OID 20629)
-- Name: custom_term_attribute; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE custom_term_attribute (
    identifier text NOT NULL,
    termid text NOT NULL,
    typeid integer NOT NULL,
    lang character varying(5) NOT NULL,
    lexicalvalue text NOT NULL
);


--
-- TOC entry 201 (class 1259 OID 20611)
-- Name: custom_term_attribute_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE custom_term_attribute_type (
    identifier integer NOT NULL,
    code text,
    thesaurusid text,
    value text
);


--
-- TOC entry 202 (class 1259 OID 20625)
-- Name: custom_term_attribute_type_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE custom_term_attribute_type_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 227 (class 1259 OID 20977)
-- Name: external_thesaurus; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE external_thesaurus (
    identifier text NOT NULL,
    external_id text NOT NULL,
    thesaurus_type text NOT NULL
);


--
-- TOC entry 230 (class 1259 OID 21006)
-- Name: external_thesaurus_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE external_thesaurus_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3390 (class 0 OID 0)
-- Dependencies: 230
-- Name: external_thesaurus_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE external_thesaurus_identifier_seq OWNED BY external_thesaurus.identifier;


--
-- TOC entry 228 (class 1259 OID 20996)
-- Name: external_thesaurus_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE external_thesaurus_type (
    identifier integer NOT NULL,
    label text NOT NULL
);


--
-- TOC entry 229 (class 1259 OID 21004)
-- Name: external_thesaurus_type_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE external_thesaurus_type_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3391 (class 0 OID 0)
-- Dependencies: 229
-- Name: external_thesaurus_type_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE external_thesaurus_type_identifier_seq OWNED BY external_thesaurus_type.identifier;


--
-- TOC entry 181 (class 1259 OID 19990)
-- Name: hierarchical_relationship; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE hierarchical_relationship (
    childconceptid text NOT NULL,
    parentconceptid text NOT NULL,
    role integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 216 (class 1259 OID 20815)
-- Name: hierarchical_relationship_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE hierarchical_relationship_aud (
    rev integer NOT NULL,
    childconceptid text NOT NULL,
    parentconceptid text NOT NULL,
    revtype smallint,
    role integer
);


--
-- TOC entry 168 (class 1259 OID 19812)
-- Name: languages_iso639; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE languages_iso639 (
    id character varying(5) NOT NULL,
    part1 character(2),
    ref_name character varying(150) NOT NULL,
    toplanguage boolean DEFAULT false NOT NULL,
    principallanguage boolean DEFAULT false
);


--
-- TOC entry 3392 (class 0 OID 0)
-- Dependencies: 168
-- Name: TABLE languages_iso639; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE languages_iso639 IS 'This table stores all ISO639-3 languages code
(http://www.sil.org/iso639-3/iso-639-3_20130123.tab)';


--
-- TOC entry 188 (class 1259 OID 20150)
-- Name: node_label; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE node_label (
    id integer NOT NULL,
    lexicalvalue text NOT NULL,
    modified timestamp without time zone DEFAULT now() NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    lang character varying(5),
    thesaurusarrayid text NOT NULL
);


--
-- TOC entry 189 (class 1259 OID 20165)
-- Name: node_label_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE node_label_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3393 (class 0 OID 0)
-- Dependencies: 189
-- Name: node_label_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE node_label_id_seq OWNED BY node_label.id;


--
-- TOC entry 184 (class 1259 OID 20039)
-- Name: note; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE note (
    identifier text NOT NULL,
    lexicalvalue text NOT NULL,
    lang character varying(5) NOT NULL,
    source text,
    notetypecode text NOT NULL,
    conceptid text,
    termid text,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone DEFAULT now() NOT NULL
);


--
-- TOC entry 183 (class 1259 OID 20030)
-- Name: note_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE note_type (
    code text NOT NULL,
    label text NOT NULL,
    isterm boolean NOT NULL,
    isconcept boolean NOT NULL,
    CONSTRAINT chk_not_false_values CHECK ((NOT ((isterm = false) AND (isconcept = false))))
);


--
-- TOC entry 207 (class 1259 OID 20734)
-- Name: revinfo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE revinfo (
    rev integer NOT NULL,
    revtstmp bigint,
    username character varying(255),
    thesaurusid text
);


--
-- TOC entry 208 (class 1259 OID 20737)
-- Name: revinfo_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE revinfo_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 209 (class 1259 OID 20739)
-- Name: revinfoentitytypes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE revinfoentitytypes (
    id integer NOT NULL,
    entityclassname character varying(255),
    revision integer
);


--
-- TOC entry 210 (class 1259 OID 20742)
-- Name: revinfoentitytypes_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE revinfoentitytypes_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 198 (class 1259 OID 20553)
-- Name: split_nonpreferredterm; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE split_nonpreferredterm (
    identifier text NOT NULL,
    lexicalvalue text NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone DEFAULT now() NOT NULL,
    source text,
    status integer,
    thesaurusid text NOT NULL,
    lang character varying(5) NOT NULL
);


--
-- TOC entry 233 (class 1259 OID 21027)
-- Name: suggestion; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE suggestion (
    identifier integer NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    creator text NOT NULL,
    recipient text NOT NULL,
    content text NOT NULL,
    term_id text,
    concept_id text
);


--
-- TOC entry 234 (class 1259 OID 21046)
-- Name: suggestion_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE suggestion_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3394 (class 0 OID 0)
-- Dependencies: 234
-- Name: suggestion_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE suggestion_identifier_seq OWNED BY suggestion.identifier;


--
-- TOC entry 169 (class 1259 OID 19816)
-- Name: thesaurus; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus (
    identifier text NOT NULL,
    contributor text,
    coverage text,
    date timestamp without time zone,
    description text,
    publisher text,
    relation text,
    rights text,
    source text,
    subject text,
    title text NOT NULL,
    type integer,
    creator integer,
    created timestamp without time zone DEFAULT now() NOT NULL,
    defaulttopconcept boolean DEFAULT false NOT NULL,
    archived boolean DEFAULT false,
    ispolyhierarchical boolean DEFAULT false
);


--
-- TOC entry 3395 (class 0 OID 0)
-- Dependencies: 169
-- Name: TABLE thesaurus; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus IS 'This table stores Thesaurus items.';


--
-- TOC entry 197 (class 1259 OID 20490)
-- Name: thesaurus_ark; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_ark (
    identifier text NOT NULL,
    created timestamp without time zone,
    entity text
);


--
-- TOC entry 187 (class 1259 OID 20129)
-- Name: thesaurus_array; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_array (
    identifier text NOT NULL,
    ordered boolean DEFAULT false NOT NULL,
    notation text,
    thesaurusid text NOT NULL,
    superordinateconceptid text,
    parentarrayid text
);


--
-- TOC entry 190 (class 1259 OID 20168)
-- Name: thesaurus_array_concept; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_array_concept (
    thesaurusarrayid text NOT NULL,
    conceptid text NOT NULL,
    arrayorder integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 20907)
-- Name: thesaurus_array_concept_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_array_concept_aud (
    rev integer NOT NULL,
    arrayorder integer,
    thesaurusarrayid character varying(255) NOT NULL,
    conceptid character varying(255) NOT NULL,
    revtype smallint
);


--
-- TOC entry 211 (class 1259 OID 20744)
-- Name: thesaurus_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_aud (
    identifier text NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    contributor text,
    coverage text,
    date timestamp without time zone,
    description text,
    publisher text,
    relation text,
    rights text,
    source text,
    subject text,
    title text,
    created timestamp without time zone,
    defaulttopconcept boolean,
    type integer,
    creator integer,
    archived boolean,
    ispolyhierarchical boolean
);


--
-- TOC entry 178 (class 1259 OID 19922)
-- Name: thesaurus_concept; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_concept (
    identifier text NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone DEFAULT now() NOT NULL,
    notation text,
    topconcept boolean,
    thesaurusid text NOT NULL,
    status integer DEFAULT 0
);


--
-- TOC entry 217 (class 1259 OID 20821)
-- Name: thesaurus_concept_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_concept_aud (
    identifier text NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    created timestamp without time zone,
    modified timestamp without time zone,
    status character varying(255),
    notation text,
    topconcept boolean,
    thesaurusid text,
    created_mod boolean,
    modified_mod boolean,
    status_mod boolean,
    notation_mod boolean,
    topconcept_mod boolean,
    thesaurus_mod boolean,
    parentconcepts_mod boolean,
    rootconcepts_mod boolean,
    associativerelationshipleft_mod boolean,
    associativerelationshipright_mod boolean,
    conceptarrays_mod boolean
);


--
-- TOC entry 170 (class 1259 OID 19823)
-- Name: thesaurus_organization; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_organization (
    identifier integer NOT NULL,
    name text,
    homepage text,
    email text
);


--
-- TOC entry 3396 (class 0 OID 0)
-- Dependencies: 170
-- Name: TABLE thesaurus_organization; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_organization IS 'This table stores data related to thesaurus item creators (a creator is an organization, with a name and a link to its homepage).';


--
-- TOC entry 171 (class 1259 OID 19829)
-- Name: thesaurus_creator_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE thesaurus_creator_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3397 (class 0 OID 0)
-- Dependencies: 171
-- Name: thesaurus_creator_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE thesaurus_creator_identifier_seq OWNED BY thesaurus_organization.identifier;


--
-- TOC entry 172 (class 1259 OID 19831)
-- Name: thesaurus_format; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_format (
    identifier integer NOT NULL,
    label text NOT NULL
);


--
-- TOC entry 3398 (class 0 OID 0)
-- Dependencies: 172
-- Name: TABLE thesaurus_format; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_format IS 'This table stores thesaurus file formats or medium (PDF 1.7, CSV, XML/SKOS, etc.).';


--
-- TOC entry 173 (class 1259 OID 19837)
-- Name: thesaurus_format_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE thesaurus_format_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3399 (class 0 OID 0)
-- Dependencies: 173
-- Name: thesaurus_format_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE thesaurus_format_identifier_seq OWNED BY thesaurus_format.identifier;


--
-- TOC entry 206 (class 1259 OID 20716)
-- Name: thesaurus_formats; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_formats (
    format_identifier integer NOT NULL,
    thesaurus_identifier text NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 20913)
-- Name: thesaurus_formats_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_formats_aud (
    rev integer NOT NULL,
    thesaurus_identifier text NOT NULL,
    format_identifier integer NOT NULL,
    revtype smallint
);


--
-- TOC entry 174 (class 1259 OID 19839)
-- Name: thesaurus_languages; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_languages (
    iso639_id character varying(5) NOT NULL,
    thesaurus_identifier text NOT NULL
);


--
-- TOC entry 3400 (class 0 OID 0)
-- Dependencies: 174
-- Name: TABLE thesaurus_languages; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_languages IS 'This table is a join table between thesaurus and languages_iso639';


--
-- TOC entry 212 (class 1259 OID 20750)
-- Name: thesaurus_languages_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_languages_aud (
    rev integer NOT NULL,
    thesaurus_identifier text NOT NULL,
    iso639_id character varying(255) NOT NULL,
    revtype smallint
);


--
-- TOC entry 179 (class 1259 OID 19932)
-- Name: thesaurus_term; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_term (
    identifier text NOT NULL,
    lexicalvalue text NOT NULL,
    created timestamp without time zone DEFAULT now() NOT NULL,
    modified timestamp without time zone DEFAULT now() NOT NULL,
    source text,
    prefered boolean,
    status integer DEFAULT 0,
    conceptid text,
    thesaurusid text NOT NULL,
    lang character varying(5) NOT NULL,
    role text,
    hidden boolean DEFAULT false NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 20756)
-- Name: thesaurus_term_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_term_aud (
    identifier text NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    lexicalvalue text,
    created timestamp without time zone,
    modified timestamp without time zone,
    source text,
    prefered boolean,
    status integer,
    role character varying(255),
    lang character varying(255),
    thesaurusid text,
    conceptid text,
    hidden boolean DEFAULT false,
    prefered_mod boolean,
    lexicalvalue_mod boolean,
    created_mod boolean,
    modified_mod boolean,
    source_mod boolean,
    hidden_mod boolean,
    status_mod boolean,
    role_mod boolean,
    language_mod boolean,
    thesaurus_mod boolean,
    concept_mod boolean
);


--
-- TOC entry 180 (class 1259 OID 19976)
-- Name: thesaurus_term_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_term_role (
    code text NOT NULL,
    label text NOT NULL,
    defaultrole boolean NOT NULL
);


--
-- TOC entry 214 (class 1259 OID 20762)
-- Name: thesaurus_thesaurusterm_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_thesaurusterm_aud (
    rev integer NOT NULL,
    thesaurusid text NOT NULL,
    identifier text NOT NULL,
    revtype smallint
);


--
-- TOC entry 215 (class 1259 OID 20768)
-- Name: thesaurus_thesaurusversionhistory_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_thesaurusversionhistory_aud (
    rev integer NOT NULL,
    identifier text NOT NULL,
    revtype smallint
);


--
-- TOC entry 175 (class 1259 OID 19845)
-- Name: thesaurus_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_type (
    identifier integer NOT NULL,
    label text NOT NULL
);


--
-- TOC entry 3401 (class 0 OID 0)
-- Dependencies: 175
-- Name: TABLE thesaurus_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_type IS 'This table stores the different types of thesaurus (structured lists, thesaurus, taxonomy, etc.).';


--
-- TOC entry 176 (class 1259 OID 19851)
-- Name: thesaurus_type_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE thesaurus_type_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3402 (class 0 OID 0)
-- Dependencies: 176
-- Name: thesaurus_type_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE thesaurus_type_identifier_seq OWNED BY thesaurus_type.identifier;


--
-- TOC entry 177 (class 1259 OID 19853)
-- Name: thesaurus_version_history; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE thesaurus_version_history (
    identifier text NOT NULL,
    date timestamp without time zone DEFAULT now() NOT NULL,
    versionnote text,
    thesaurus_identifier text NOT NULL,
    thisversion boolean DEFAULT false NOT NULL,
    status integer DEFAULT 0 NOT NULL,
    userid text
);


--
-- TOC entry 3403 (class 0 OID 0)
-- Dependencies: 177
-- Name: TABLE thesaurus_version_history; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE thesaurus_version_history IS 'This table stores versions history for thesaurus';


--
-- TOC entry 182 (class 1259 OID 20010)
-- Name: top_relationship; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE top_relationship (
    childconceptid text NOT NULL,
    rootconceptid text NOT NULL
);


--
-- TOC entry 218 (class 1259 OID 20827)
-- Name: top_relationship_aud; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE top_relationship_aud (
    rev integer NOT NULL,
    childconceptid text NOT NULL,
    rootconceptid text NOT NULL,
    revtype smallint
);


--
-- TOC entry 231 (class 1259 OID 21009)
-- Name: user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_role (
    identifier integer NOT NULL,
    username text,
    thesaurus_id text,
    role integer
);


--
-- TOC entry 232 (class 1259 OID 21022)
-- Name: user_role_identifier_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE user_role_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3404 (class 0 OID 0)
-- Dependencies: 232
-- Name: user_role_identifier_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE user_role_identifier_seq OWNED BY user_role.identifier;


--
-- TOC entry 2967 (class 2604 OID 19860)
-- Name: identifier; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_format ALTER COLUMN identifier SET DEFAULT nextval('thesaurus_format_identifier_seq'::regclass);


--
-- TOC entry 2966 (class 2604 OID 19861)
-- Name: identifier; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_organization ALTER COLUMN identifier SET DEFAULT nextval('thesaurus_creator_identifier_seq'::regclass);


--
-- TOC entry 2968 (class 2604 OID 19862)
-- Name: identifier; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_type ALTER COLUMN identifier SET DEFAULT nextval('thesaurus_type_identifier_seq'::regclass);


--
-- TOC entry 3342 (class 0 OID 20482)
-- Dependencies: 196
-- Data for Name: admin_user_id; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO admin_user_id (identifier) VALUES ('admin');


--
-- TOC entry 3368 (class 0 OID 20927)
-- Dependencies: 222
-- Data for Name: alignment; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3369 (class 0 OID 20947)
-- Dependencies: 223
-- Data for Name: alignment_concept; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 226
-- Name: alignment_concept_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('alignment_concept_identifier_seq', 1, false);


--
-- TOC entry 3370 (class 0 OID 20965)
-- Dependencies: 224
-- Data for Name: alignment_type; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (1, 'Equivalence exacte', '=EQ', false, true);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (2, 'Equivalence inexacte', '~EQ', false, true);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (3, 'Alignement générique', 'BM', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (4, 'Alignement spécifique', 'NM', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (5, 'Alignement générique générique', 'BMG', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (6, 'Alignement spécifique générique', 'NMG', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (7, 'Alignement générique partitif', 'BMP', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (8, 'Alignement spécifique partitif', 'NMP', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (9, 'Alignement générique instance', 'BMI', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (10, 'Alignement spécifique instance', 'NMI', false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept) VALUES (11, 'Alignement associatif', 'RM', false, false);


--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 225
-- Name: alignment_type_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('alignment_type_identifier_seq', 1, false);


--
-- TOC entry 3332 (class 0 OID 20103)
-- Dependencies: 186
-- Data for Name: associative_relationship; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3365 (class 0 OID 20854)
-- Dependencies: 219
-- Data for Name: associative_relationship_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3331 (class 0 OID 20095)
-- Dependencies: 185
-- Data for Name: associative_relationship_role; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO associative_relationship_role (code, label, defaultrole, skoslabel) VALUES ('TA', 'Terme associé', true, 'TermeAssocie');


--
-- TOC entry 3345 (class 0 OID 20574)
-- Dependencies: 199
-- Data for Name: compound_equivalence; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3338 (class 0 OID 20196)
-- Dependencies: 192
-- Data for Name: concept_group; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3339 (class 0 OID 20214)
-- Dependencies: 193
-- Data for Name: concept_group_concepts; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3340 (class 0 OID 20242)
-- Dependencies: 194
-- Data for Name: concept_group_label; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 195
-- Name: concept_group_label_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('concept_group_label_identifier_seq', 1, false);


--
-- TOC entry 3337 (class 0 OID 20188)
-- Dependencies: 191
-- Data for Name: concept_group_type; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO concept_group_type (code, label, skoslabel) VALUES ('T', 'Thématique', 'Thematique');
INSERT INTO concept_group_type (code, label, skoslabel) VALUES ('F', 'Facette', 'Facette');
INSERT INTO concept_group_type (code, label, skoslabel) VALUES ('D', 'Domaine', 'Domaine');


--
-- TOC entry 3351 (class 0 OID 20655)
-- Dependencies: 205
-- Data for Name: custom_concept_attribute; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3346 (class 0 OID 20597)
-- Dependencies: 200
-- Data for Name: custom_concept_attribute_type; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 203
-- Name: custom_concept_attribute_type_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('custom_concept_attribute_type_identifier_seq', 1, false);


--
-- TOC entry 3350 (class 0 OID 20629)
-- Dependencies: 204
-- Data for Name: custom_term_attribute; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3347 (class 0 OID 20611)
-- Dependencies: 201
-- Data for Name: custom_term_attribute_type; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 202
-- Name: custom_term_attribute_type_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('custom_term_attribute_type_identifier_seq', 1, false);


--
-- TOC entry 3373 (class 0 OID 20977)
-- Dependencies: 227
-- Data for Name: external_thesaurus; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 230
-- Name: external_thesaurus_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('external_thesaurus_identifier_seq', 1, false);


--
-- TOC entry 3374 (class 0 OID 20996)
-- Dependencies: 228
-- Data for Name: external_thesaurus_type; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO external_thesaurus_type (identifier, label) VALUES (1, 'Autorités');
INSERT INTO external_thesaurus_type (identifier, label) VALUES (2, 'Classification');
INSERT INTO external_thesaurus_type (identifier, label) VALUES (3, 'Ontologie');
INSERT INTO external_thesaurus_type (identifier, label) VALUES (4, 'Taxonomie');
INSERT INTO external_thesaurus_type (identifier, label) VALUES (5, 'Terminologie');
INSERT INTO external_thesaurus_type (identifier, label) VALUES (6, 'Thésaurus');
INSERT INTO external_thesaurus_type (identifier, label) VALUES (7, 'Vedettes-matière');
INSERT INTO external_thesaurus_type (identifier, label) VALUES (8, 'Autre');


--
-- TOC entry 3411 (class 0 OID 0)
-- Dependencies: 229
-- Name: external_thesaurus_type_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('external_thesaurus_type_identifier_seq', 1, false);


--
-- TOC entry 3327 (class 0 OID 19990)
-- Dependencies: 181
-- Data for Name: hierarchical_relationship; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3362 (class 0 OID 20815)
-- Dependencies: 216
-- Data for Name: hierarchical_relationship_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3314 (class 0 OID 19812)
-- Dependencies: 168
-- Data for Name: languages_iso639; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('fr-FR', 'fr', 'Français/France', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('en-US', 'en', 'Anglais/USA', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('it-IT', 'it', 'Italien/Italie', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('de-DE', 'de', 'Allemand/Allemagne', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('en-UK', 'en', 'Anglais/United Kingdom', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('cy', 'cy', 'Gallois', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('de-CH', 'de', 'Allemand/suisse', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('de-IT', 'de', 'Allemand/Italie', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('el', 'el', 'Grec', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('es', 'es', 'Espagnol', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('fr-BE', 'fr', 'Français/Belgique', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('fr-CA', 'fr', 'Français/Canada', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('fr-CH', 'fr', 'Français/Suisse', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('it-CH', 'it', 'Italien/Suisse', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('nl-BE', 'nl', 'Néerlandais/Belgique', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('nl-NL', 'nl', 'Néerlandais/Pays-Bas', true, true);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('pt', 'pt', 'Portugais', true, false);
INSERT INTO languages_iso639 (id, part1, ref_name, toplanguage, principallanguage) VALUES ('ru', 'ru', 'Russe', true, true);


--
-- TOC entry 3334 (class 0 OID 20150)
-- Dependencies: 188
-- Data for Name: node_label; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 189
-- Name: node_label_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('node_label_id_seq', 1, false);


--
-- TOC entry 3330 (class 0 OID 20039)
-- Dependencies: 184
-- Data for Name: note; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3329 (class 0 OID 20030)
-- Dependencies: 183
-- Data for Name: note_type; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO note_type (code, label, isterm, isconcept) VALUES ('scopeNote', 'Note d''application', false, true);
INSERT INTO note_type (code, label, isterm, isconcept) VALUES ('historyNote', 'Note historique', true, true);
INSERT INTO note_type (code, label, isterm, isconcept) VALUES ('definition', 'Définition', true, false);
INSERT INTO note_type (code, label, isterm, isconcept) VALUES ('editorialNote', 'Note éditoriale', true, false);
INSERT INTO note_type (code, label, isterm, isconcept) VALUES ('example', 'Exemple', false, true);


--
-- TOC entry 3353 (class 0 OID 20734)
-- Dependencies: 207
-- Data for Name: revinfo; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 208
-- Name: revinfo_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('revinfo_identifier_seq', 1, false);


--
-- TOC entry 3355 (class 0 OID 20739)
-- Dependencies: 209
-- Data for Name: revinfoentitytypes; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 210
-- Name: revinfoentitytypes_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('revinfoentitytypes_identifier_seq', 1, false);


--
-- TOC entry 3344 (class 0 OID 20553)
-- Dependencies: 198
-- Data for Name: split_nonpreferredterm; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3379 (class 0 OID 21027)
-- Dependencies: 233
-- Data for Name: suggestion; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 234
-- Name: suggestion_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('suggestion_identifier_seq', 1, false);


--
-- TOC entry 3315 (class 0 OID 19816)
-- Dependencies: 169
-- Data for Name: thesaurus; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3343 (class 0 OID 20490)
-- Dependencies: 197
-- Data for Name: thesaurus_ark; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3333 (class 0 OID 20129)
-- Dependencies: 187
-- Data for Name: thesaurus_array; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3336 (class 0 OID 20168)
-- Dependencies: 190
-- Data for Name: thesaurus_array_concept; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3366 (class 0 OID 20907)
-- Dependencies: 220
-- Data for Name: thesaurus_array_concept_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3357 (class 0 OID 20744)
-- Dependencies: 211
-- Data for Name: thesaurus_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3324 (class 0 OID 19922)
-- Dependencies: 178
-- Data for Name: thesaurus_concept; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3363 (class 0 OID 20821)
-- Dependencies: 217
-- Data for Name: thesaurus_concept_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 171
-- Name: thesaurus_creator_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('thesaurus_creator_identifier_seq', 1, false);


--
-- TOC entry 3318 (class 0 OID 19831)
-- Dependencies: 172
-- Data for Name: thesaurus_format; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO thesaurus_format (identifier, label) VALUES (2, 'PDF 1.7');
INSERT INTO thesaurus_format (identifier, label) VALUES (1, 'CSV');
INSERT INTO thesaurus_format (identifier, label) VALUES (3, 'XML/SKOS');


--
-- TOC entry 3417 (class 0 OID 0)
-- Dependencies: 173
-- Name: thesaurus_format_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('thesaurus_format_identifier_seq', 1, false);


--
-- TOC entry 3352 (class 0 OID 20716)
-- Dependencies: 206
-- Data for Name: thesaurus_formats; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3367 (class 0 OID 20913)
-- Dependencies: 221
-- Data for Name: thesaurus_formats_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3320 (class 0 OID 19839)
-- Dependencies: 174
-- Data for Name: thesaurus_languages; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3358 (class 0 OID 20750)
-- Dependencies: 212
-- Data for Name: thesaurus_languages_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3316 (class 0 OID 19823)
-- Dependencies: 170
-- Data for Name: thesaurus_organization; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3325 (class 0 OID 19932)
-- Dependencies: 179
-- Data for Name: thesaurus_term; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3359 (class 0 OID 20756)
-- Dependencies: 213
-- Data for Name: thesaurus_term_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3326 (class 0 OID 19976)
-- Dependencies: 180
-- Data for Name: thesaurus_term_role; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('AB', 'Abréviation', false);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('AV', 'Appellation vernaculaire', false);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('D', 'Dénomination', false);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('EM', 'Employer', true);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('EP', 'Employé', false);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('FD', 'Forme développée', false);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('NC', 'Nom commun', false);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('NS', 'Nom scientifique', false);
INSERT INTO thesaurus_term_role (code, label, defaultrole) VALUES ('VO', 'Variante orthographique', false);


--
-- TOC entry 3360 (class 0 OID 20762)
-- Dependencies: 214
-- Data for Name: thesaurus_thesaurusterm_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3361 (class 0 OID 20768)
-- Dependencies: 215
-- Data for Name: thesaurus_thesaurusversionhistory_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3321 (class 0 OID 19845)
-- Dependencies: 175
-- Data for Name: thesaurus_type; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO thesaurus_type (identifier, label) VALUES (1, 'Liste contrôlée');
INSERT INTO thesaurus_type (identifier, label) VALUES (2, 'Taxonomie');
INSERT INTO thesaurus_type (identifier, label) VALUES (3, 'Thésaurus');


--
-- TOC entry 3418 (class 0 OID 0)
-- Dependencies: 176
-- Name: thesaurus_type_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('thesaurus_type_identifier_seq', 1, false);


--
-- TOC entry 3323 (class 0 OID 19853)
-- Dependencies: 177
-- Data for Name: thesaurus_version_history; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3328 (class 0 OID 20010)
-- Dependencies: 182
-- Data for Name: top_relationship; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3364 (class 0 OID 20827)
-- Dependencies: 218
-- Data for Name: top_relationship_aud; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3377 (class 0 OID 21009)
-- Dependencies: 231
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3419 (class 0 OID 0)
-- Dependencies: 232
-- Name: user_role_identifier_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('user_role_identifier_seq', 1, false);


--
-- TOC entry 3118 (class 2606 OID 20895)
-- Name: associative_relationship_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY associative_relationship_aud
    ADD CONSTRAINT associative_relationship_aud_pkey PRIMARY KEY (rev, concept1, concept2);


--
-- TOC entry 3047 (class 2606 OID 20434)
-- Name: associative_relationship_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY associative_relationship
    ADD CONSTRAINT associative_relationship_pkey PRIMARY KEY (concept1, concept2);


--
-- TOC entry 3044 (class 2606 OID 20102)
-- Name: associative_relationship_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY associative_relationship_role
    ADD CONSTRAINT associative_relationship_role_pkey PRIMARY KEY (code);


--
-- TOC entry 3019 (class 2606 OID 21204)
-- Name: chk_term_uniq; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term
    ADD CONSTRAINT chk_term_uniq UNIQUE (lexicalvalue, thesaurusid, lang);


--
-- TOC entry 3078 (class 2606 OID 20581)
-- Name: compound_equivalence_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compound_equivalence
    ADD CONSTRAINT compound_equivalence_pk PRIMARY KEY (id_split_nonpreferredterm, id_preferredterm);


--
-- TOC entry 3112 (class 2606 OID 20885)
-- Name: hierarchical_relationship_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY hierarchical_relationship_aud
    ADD CONSTRAINT hierarchical_relationship_aud_pkey PRIMARY KEY (rev, childconceptid, parentconceptid);


--
-- TOC entry 3071 (class 2606 OID 20489)
-- Name: pk_admin_user_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY admin_user_id
    ADD CONSTRAINT pk_admin_user_id PRIMARY KEY (identifier);


--
-- TOC entry 3123 (class 2606 OID 20936)
-- Name: pk_alignment; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment
    ADD CONSTRAINT pk_alignment PRIMARY KEY (identifier);


--
-- TOC entry 3125 (class 2606 OID 20954)
-- Name: pk_alignment_concept; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment_concept
    ADD CONSTRAINT pk_alignment_concept PRIMARY KEY (identifier);


--
-- TOC entry 3127 (class 2606 OID 20972)
-- Name: pk_alignment_type; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment_type
    ADD CONSTRAINT pk_alignment_type PRIMARY KEY (identifier);


--
-- TOC entry 3067 (class 2606 OID 20221)
-- Name: pk_concept_group_concepts; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group_concepts
    ADD CONSTRAINT pk_concept_group_concepts PRIMARY KEY (conceptgroupid, conceptid);


--
-- TOC entry 3065 (class 2606 OID 20203)
-- Name: pk_concept_group_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group
    ADD CONSTRAINT pk_concept_group_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3069 (class 2606 OID 20251)
-- Name: pk_concept_group_label_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group_label
    ADD CONSTRAINT pk_concept_group_label_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3063 (class 2606 OID 20195)
-- Name: pk_concept_group_type_code; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group_type
    ADD CONSTRAINT pk_concept_group_type_code PRIMARY KEY (code);


--
-- TOC entry 3094 (class 2606 OID 20662)
-- Name: pk_custom_concept_attribute; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_concept_attribute
    ADD CONSTRAINT pk_custom_concept_attribute PRIMARY KEY (identifier);


--
-- TOC entry 3081 (class 2606 OID 20604)
-- Name: pk_custom_concept_attribute_type; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_concept_attribute_type
    ADD CONSTRAINT pk_custom_concept_attribute_type PRIMARY KEY (identifier);


--
-- TOC entry 3089 (class 2606 OID 20636)
-- Name: pk_custom_term_attribute; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_term_attribute
    ADD CONSTRAINT pk_custom_term_attribute PRIMARY KEY (identifier);


--
-- TOC entry 3084 (class 2606 OID 20618)
-- Name: pk_custom_term_attribute_type; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_term_attribute_type
    ADD CONSTRAINT pk_custom_term_attribute_type PRIMARY KEY (identifier);


--
-- TOC entry 3130 (class 2606 OID 20984)
-- Name: pk_external_thesaurus; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY external_thesaurus
    ADD CONSTRAINT pk_external_thesaurus PRIMARY KEY (identifier);


--
-- TOC entry 3132 (class 2606 OID 21003)
-- Name: pk_external_thesaurus_type; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY external_thesaurus_type
    ADD CONSTRAINT pk_external_thesaurus_type PRIMARY KEY (identifier);


--
-- TOC entry 3030 (class 2606 OID 19997)
-- Name: pk_hierarchical_relationship; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY hierarchical_relationship
    ADD CONSTRAINT pk_hierarchical_relationship PRIMARY KEY (childconceptid, parentconceptid);


--
-- TOC entry 2998 (class 2606 OID 21055)
-- Name: pk_languages_iso639; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY languages_iso639
    ADD CONSTRAINT pk_languages_iso639 PRIMARY KEY (id);


--
-- TOC entry 3042 (class 2606 OID 20046)
-- Name: pk_note_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY note
    ADD CONSTRAINT pk_note_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3057 (class 2606 OID 20159)
-- Name: pk_note_label_id; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY node_label
    ADD CONSTRAINT pk_note_label_id PRIMARY KEY (id);


--
-- TOC entry 3036 (class 2606 OID 20038)
-- Name: pk_note_type; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY note_type
    ADD CONSTRAINT pk_note_type PRIMARY KEY (code);


--
-- TOC entry 3026 (class 2606 OID 19983)
-- Name: pk_role_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term_role
    ADD CONSTRAINT pk_role_identifier PRIMARY KEY (code);


--
-- TOC entry 3076 (class 2606 OID 20562)
-- Name: pk_snpt_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY split_nonpreferredterm
    ADD CONSTRAINT pk_snpt_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3138 (class 2606 OID 21035)
-- Name: pk_suggestion; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY suggestion
    ADD CONSTRAINT pk_suggestion PRIMARY KEY (identifier);


--
-- TOC entry 3024 (class 2606 OID 19941)
-- Name: pk_term_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term
    ADD CONSTRAINT pk_term_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3061 (class 2606 OID 20175)
-- Name: pk_thesaurus_array_concept; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_array_concept
    ADD CONSTRAINT pk_thesaurus_array_concept PRIMARY KEY (thesaurusarrayid, conceptid);


--
-- TOC entry 3054 (class 2606 OID 20137)
-- Name: pk_thesaurus_array_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_array
    ADD CONSTRAINT pk_thesaurus_array_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3017 (class 2606 OID 19931)
-- Name: pk_thesaurus_concept_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_concept
    ADD CONSTRAINT pk_thesaurus_concept_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3006 (class 2606 OID 19866)
-- Name: pk_thesaurus_format_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_format
    ADD CONSTRAINT pk_thesaurus_format_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3096 (class 2606 OID 20723)
-- Name: pk_thesaurus_formats; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_formats
    ADD CONSTRAINT pk_thesaurus_formats PRIMARY KEY (format_identifier, thesaurus_identifier);


--
-- TOC entry 3002 (class 2606 OID 19868)
-- Name: pk_thesaurus_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus
    ADD CONSTRAINT pk_thesaurus_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3009 (class 2606 OID 21102)
-- Name: pk_thesaurus_languages; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages
    ADD CONSTRAINT pk_thesaurus_languages PRIMARY KEY (iso639_id, thesaurus_identifier);


--
-- TOC entry 3004 (class 2606 OID 19872)
-- Name: pk_thesaurus_organization; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_organization
    ADD CONSTRAINT pk_thesaurus_organization PRIMARY KEY (identifier);


--
-- TOC entry 3011 (class 2606 OID 19874)
-- Name: pk_thesaurus_type_identifier; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_type
    ADD CONSTRAINT pk_thesaurus_type_identifier PRIMARY KEY (identifier);


--
-- TOC entry 3014 (class 2606 OID 19876)
-- Name: pk_thesaurus_version_history; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_version_history
    ADD CONSTRAINT pk_thesaurus_version_history PRIMARY KEY (identifier);


--
-- TOC entry 3034 (class 2606 OID 20017)
-- Name: pk_top_relationship; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY top_relationship
    ADD CONSTRAINT pk_top_relationship PRIMARY KEY (childconceptid, rootconceptid);


--
-- TOC entry 3134 (class 2606 OID 21016)
-- Name: pk_user_role; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT pk_user_role PRIMARY KEY (identifier);


--
-- TOC entry 3098 (class 2606 OID 20772)
-- Name: revinfo_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- TOC entry 3100 (class 2606 OID 20774)
-- Name: revinfoentitytypes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY revinfoentitytypes
    ADD CONSTRAINT revinfoentitytypes_pkey PRIMARY KEY (id);


--
-- TOC entry 3073 (class 2606 OID 20497)
-- Name: thesaurus_ark_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_ark
    ADD CONSTRAINT thesaurus_ark_pkey PRIMARY KEY (identifier);


--
-- TOC entry 3102 (class 2606 OID 20868)
-- Name: thesaurus_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_aud
    ADD CONSTRAINT thesaurus_aud_pkey PRIMARY KEY (identifier, rev);


--
-- TOC entry 3114 (class 2606 OID 20887)
-- Name: thesaurus_concept_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_concept_aud
    ADD CONSTRAINT thesaurus_concept_aud_pkey PRIMARY KEY (identifier, rev);


--
-- TOC entry 3120 (class 2606 OID 20920)
-- Name: thesaurus_formats_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_formats_aud
    ADD CONSTRAINT thesaurus_formats_aud_pkey PRIMARY KEY (rev, thesaurus_identifier, format_identifier);


--
-- TOC entry 3104 (class 2606 OID 20870)
-- Name: thesaurus_languages_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages_aud
    ADD CONSTRAINT thesaurus_languages_aud_pkey PRIMARY KEY (rev, thesaurus_identifier, iso639_id);


--
-- TOC entry 3106 (class 2606 OID 20872)
-- Name: thesaurus_term_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term_aud
    ADD CONSTRAINT thesaurus_term_aud_pkey PRIMARY KEY (identifier, rev);


--
-- TOC entry 3108 (class 2606 OID 20876)
-- Name: thesaurus_thesaurusterm_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_thesaurusterm_aud
    ADD CONSTRAINT thesaurus_thesaurusterm_aud_pkey PRIMARY KEY (rev, thesaurusid, identifier);


--
-- TOC entry 3110 (class 2606 OID 20878)
-- Name: thesaurus_thesaurusversionhistory_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_thesaurusversionhistory_aud
    ADD CONSTRAINT thesaurus_thesaurusversionhistory_aud_pkey PRIMARY KEY (rev, identifier);


--
-- TOC entry 3116 (class 2606 OID 20891)
-- Name: top_relationship_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY top_relationship_aud
    ADD CONSTRAINT top_relationship_aud_pkey PRIMARY KEY (rev, childconceptid, rootconceptid);


--
-- TOC entry 3045 (class 1259 OID 21026)
-- Name: associative_relationship_role_skoslabel_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX associative_relationship_role_skoslabel_idx ON associative_relationship_role USING btree (skoslabel);


--
-- TOC entry 3048 (class 1259 OID 20427)
-- Name: fki_associative_relationship_concept1; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_associative_relationship_concept1 ON associative_relationship USING btree (concept1);


--
-- TOC entry 3049 (class 1259 OID 20435)
-- Name: fki_associative_relationship_concept2; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_associative_relationship_concept2 ON associative_relationship USING btree (concept2);


--
-- TOC entry 3050 (class 1259 OID 20441)
-- Name: fki_associative_relationship_role; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_associative_relationship_role ON associative_relationship USING btree (role);


--
-- TOC entry 3015 (class 1259 OID 19975)
-- Name: fki_concept_thesaurus; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_concept_thesaurus ON thesaurus_concept USING btree (thesaurusid);


--
-- TOC entry 3079 (class 1259 OID 20610)
-- Name: fki_custom_concept; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_concept ON custom_concept_attribute_type USING btree (thesaurusid);


--
-- TOC entry 3090 (class 1259 OID 20679)
-- Name: fki_custom_concept_attribute_conceptid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_concept_attribute_conceptid ON custom_concept_attribute USING btree (conceptid);


--
-- TOC entry 3091 (class 1259 OID 21128)
-- Name: fki_custom_concept_attribute_lang; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_concept_attribute_lang ON custom_concept_attribute USING btree (lang);


--
-- TOC entry 3092 (class 1259 OID 20680)
-- Name: fki_custom_concept_attribute_typeid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_concept_attribute_typeid ON custom_concept_attribute USING btree (typeid);


--
-- TOC entry 3082 (class 1259 OID 20624)
-- Name: fki_custom_term; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_term ON custom_term_attribute_type USING btree (thesaurusid);


--
-- TOC entry 3085 (class 1259 OID 21144)
-- Name: fki_custom_term_attribute_lang; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_term_attribute_lang ON custom_term_attribute USING btree (lang);


--
-- TOC entry 3086 (class 1259 OID 20653)
-- Name: fki_custom_term_attribute_termid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_term_attribute_termid ON custom_term_attribute USING btree (termid);


--
-- TOC entry 3087 (class 1259 OID 20654)
-- Name: fki_custom_term_attribute_typeid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_custom_term_attribute_typeid ON custom_term_attribute USING btree (typeid);


--
-- TOC entry 3055 (class 1259 OID 20167)
-- Name: fki_node_label_thesaurus_array; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_node_label_thesaurus_array ON node_label USING btree (thesaurusarrayid);


--
-- TOC entry 3037 (class 1259 OID 21173)
-- Name: fki_note_languages_iso639; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_note_languages_iso639 ON note USING btree (lang);


--
-- TOC entry 3038 (class 1259 OID 20068)
-- Name: fki_note_notetype; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_note_notetype ON note USING btree (notetypecode);


--
-- TOC entry 3039 (class 1259 OID 20069)
-- Name: fki_note_thesaurus_concept; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_note_thesaurus_concept ON note USING btree (conceptid);


--
-- TOC entry 3040 (class 1259 OID 20070)
-- Name: fki_note_thesaurus_term; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_note_thesaurus_term ON note USING btree (termid);


--
-- TOC entry 3058 (class 1259 OID 20186)
-- Name: fki_thesaurus_array_concept_thesaurus_array; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_array_concept_thesaurus_array ON thesaurus_array_concept USING btree (thesaurusarrayid);


--
-- TOC entry 3059 (class 1259 OID 20187)
-- Name: fki_thesaurus_array_concept_thesaurus_concept; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_array_concept_thesaurus_concept ON thesaurus_array_concept USING btree (conceptid);


--
-- TOC entry 3051 (class 1259 OID 20149)
-- Name: fki_thesaurus_array_thesaurus; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_array_thesaurus ON thesaurus_array USING btree (thesaurusid);


--
-- TOC entry 3052 (class 1259 OID 20148)
-- Name: fki_thesaurus_array_thesaurus_concept; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_array_thesaurus_concept ON thesaurus_array USING btree (superordinateconceptid);


--
-- TOC entry 3007 (class 1259 OID 19878)
-- Name: fki_thesaurus_language_identifier; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_language_identifier ON thesaurus_languages USING btree (thesaurus_identifier);


--
-- TOC entry 3012 (class 1259 OID 19879)
-- Name: fki_thesaurus_organization_thesaurus; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_organization_thesaurus ON thesaurus_version_history USING btree (thesaurus_identifier);


--
-- TOC entry 2999 (class 1259 OID 19880)
-- Name: fki_thesaurus_thesaurus_organization; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_thesaurus_organization ON thesaurus USING btree (creator);


--
-- TOC entry 3000 (class 1259 OID 19881)
-- Name: fki_thesaurus_type; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_thesaurus_type ON thesaurus USING btree (type);


--
-- TOC entry 3121 (class 1259 OID 20995)
-- Name: idx_alignment_sourceconceptid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_alignment_sourceconceptid ON alignment USING btree (source_concept_id);


--
-- TOC entry 3027 (class 1259 OID 19999)
-- Name: idx_hierarchical_relationship_childconceptid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hierarchical_relationship_childconceptid ON hierarchical_relationship USING btree (childconceptid);


--
-- TOC entry 3028 (class 1259 OID 19998)
-- Name: idx_hierarchical_relationship_parentconceptid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_hierarchical_relationship_parentconceptid ON hierarchical_relationship USING btree (parentconceptid);


--
-- TOC entry 2996 (class 1259 OID 20360)
-- Name: idx_languages_iso639_part1; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_languages_iso639_part1 ON languages_iso639 USING btree (part1);


--
-- TOC entry 3074 (class 1259 OID 20573)
-- Name: idx_split_nonpreferredterm_thesaurusid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_split_nonpreferredterm_thesaurusid ON split_nonpreferredterm USING btree (thesaurusid);


--
-- TOC entry 3128 (class 1259 OID 21008)
-- Name: idx_thesaurus_externalid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_thesaurus_externalid ON external_thesaurus USING btree (external_id);


--
-- TOC entry 3020 (class 1259 OID 19968)
-- Name: idx_thesaurus_term_conceptid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_thesaurus_term_conceptid ON thesaurus_term USING btree (conceptid);


--
-- TOC entry 3021 (class 1259 OID 19989)
-- Name: idx_thesaurus_term_role; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_thesaurus_term_role ON thesaurus_term USING btree (role);


--
-- TOC entry 3022 (class 1259 OID 19969)
-- Name: idx_thesaurus_term_thesaurusid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_thesaurus_term_thesaurusid ON thesaurus_term USING btree (thesaurusid);


--
-- TOC entry 3031 (class 1259 OID 20018)
-- Name: idx_top_relationship_childconceptid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_top_relationship_childconceptid ON top_relationship USING btree (childconceptid);


--
-- TOC entry 3032 (class 1259 OID 20019)
-- Name: idx_top_relationship_rootconceptid; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX idx_top_relationship_rootconceptid ON top_relationship USING btree (rootconceptid);


--
-- TOC entry 3135 (class 1259 OID 21025)
-- Name: user_role_thesaurus_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX user_role_thesaurus_id_idx ON user_role USING btree (thesaurus_id);


--
-- TOC entry 3136 (class 1259 OID 21024)
-- Name: user_role_username_thesaurus_id_idx; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX user_role_username_thesaurus_id_idx ON user_role USING btree (username, thesaurus_id);


--
-- TOC entry 3191 (class 2606 OID 20785)
-- Name: fk29bf1de7d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages_aud
    ADD CONSTRAINT fk29bf1de7d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3196 (class 2606 OID 20842)
-- Name: fk2cf9f474d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_concept_aud
    ADD CONSTRAINT fk2cf9f474d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3193 (class 2606 OID 20790)
-- Name: fk85e9bf12d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_thesaurusterm_aud
    ADD CONSTRAINT fk85e9bf12d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3204 (class 2606 OID 20960)
-- Name: fk_alignment; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment_concept
    ADD CONSTRAINT fk_alignment FOREIGN KEY (alignment_id) REFERENCES alignment(identifier) ON DELETE CASCADE;


--
-- TOC entry 3203 (class 2606 OID 20955)
-- Name: fk_alignment_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment_concept
    ADD CONSTRAINT fk_alignment_concept FOREIGN KEY (internal_target_concept_id) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3202 (class 2606 OID 20990)
-- Name: fk_alignment_external_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment
    ADD CONSTRAINT fk_alignment_external_thesaurus FOREIGN KEY (external_target_thesaurus_id) REFERENCES external_thesaurus(identifier);


--
-- TOC entry 3199 (class 2606 OID 20937)
-- Name: fk_alignment_sourceconcept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment
    ADD CONSTRAINT fk_alignment_sourceconcept FOREIGN KEY (source_concept_id) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3200 (class 2606 OID 20942)
-- Name: fk_alignment_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment
    ADD CONSTRAINT fk_alignment_thesaurus FOREIGN KEY (internal_target_thesaurus_id) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3201 (class 2606 OID 20985)
-- Name: fk_alignment_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY alignment
    ADD CONSTRAINT fk_alignment_type FOREIGN KEY (alignment_type) REFERENCES alignment_type(identifier);


--
-- TOC entry 3149 (class 2606 OID 20000)
-- Name: fk_child_hierarchical_relationship_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY hierarchical_relationship
    ADD CONSTRAINT fk_child_hierarchical_relationship_thesaurus_concept FOREIGN KEY (childconceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3151 (class 2606 OID 20020)
-- Name: fk_child_top_relationship_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY top_relationship
    ADD CONSTRAINT fk_child_top_relationship_thesaurus_concept FOREIGN KEY (childconceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3157 (class 2606 OID 20428)
-- Name: fk_concept1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY associative_relationship
    ADD CONSTRAINT fk_concept1 FOREIGN KEY (concept1) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3158 (class 2606 OID 20436)
-- Name: fk_concept2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY associative_relationship
    ADD CONSTRAINT fk_concept2 FOREIGN KEY (concept2) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3162 (class 2606 OID 20681)
-- Name: fk_concept_array; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_array
    ADD CONSTRAINT fk_concept_array FOREIGN KEY (parentarrayid) REFERENCES thesaurus_array(identifier) ON DELETE SET NULL;


--
-- TOC entry 3170 (class 2606 OID 20592)
-- Name: fk_concept_group; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group
    ADD CONSTRAINT fk_concept_group FOREIGN KEY (parentgroupid) REFERENCES concept_group(identifier) ON DELETE SET NULL;


--
-- TOC entry 3169 (class 2606 OID 20209)
-- Name: fk_concept_group_concept_group_type_code; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group
    ADD CONSTRAINT fk_concept_group_concept_group_type_code FOREIGN KEY (conceptgrouptypecode) REFERENCES concept_group_type(code);


--
-- TOC entry 3171 (class 2606 OID 20232)
-- Name: fk_concept_group_concepts_concept_group_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group_concepts
    ADD CONSTRAINT fk_concept_group_concepts_concept_group_identifier FOREIGN KEY (conceptgroupid) REFERENCES concept_group(identifier) ON DELETE CASCADE;


--
-- TOC entry 3172 (class 2606 OID 20237)
-- Name: fk_concept_group_concepts_thesaurus_concept_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group_concepts
    ADD CONSTRAINT fk_concept_group_concepts_thesaurus_concept_identifier FOREIGN KEY (conceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3173 (class 2606 OID 20257)
-- Name: fk_concept_group_label_concept_group_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group_label
    ADD CONSTRAINT fk_concept_group_label_concept_group_identifier FOREIGN KEY (conceptgroupid) REFERENCES concept_group(identifier) ON DELETE CASCADE;


--
-- TOC entry 3174 (class 2606 OID 21116)
-- Name: fk_concept_group_label_languages_iso639_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group_label
    ADD CONSTRAINT fk_concept_group_label_languages_iso639_id FOREIGN KEY (lang) REFERENCES languages_iso639(id);


--
-- TOC entry 3168 (class 2606 OID 20204)
-- Name: fk_concept_group_thesaurus_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group
    ADD CONSTRAINT fk_concept_group_thesaurus_identifier FOREIGN KEY (thesaurusid) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3144 (class 2606 OID 20264)
-- Name: fk_concept_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_concept
    ADD CONSTRAINT fk_concept_thesaurus FOREIGN KEY (thesaurusid) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3185 (class 2606 OID 20691)
-- Name: fk_custom_concept_attribute_conceptid; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_concept_attribute
    ADD CONSTRAINT fk_custom_concept_attribute_conceptid FOREIGN KEY (conceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3184 (class 2606 OID 21129)
-- Name: fk_custom_concept_attribute_lang; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_concept_attribute
    ADD CONSTRAINT fk_custom_concept_attribute_lang FOREIGN KEY (lang) REFERENCES languages_iso639(id);


--
-- TOC entry 3186 (class 2606 OID 20701)
-- Name: fk_custom_concept_attribute_typeid; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_concept_attribute
    ADD CONSTRAINT fk_custom_concept_attribute_typeid FOREIGN KEY (typeid) REFERENCES custom_concept_attribute_type(identifier) ON DELETE CASCADE;


--
-- TOC entry 3181 (class 2606 OID 21145)
-- Name: fk_custom_term_attribute_lang; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_term_attribute
    ADD CONSTRAINT fk_custom_term_attribute_lang FOREIGN KEY (lang) REFERENCES languages_iso639(id);


--
-- TOC entry 3182 (class 2606 OID 20686)
-- Name: fk_custom_term_attribute_termid; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_term_attribute
    ADD CONSTRAINT fk_custom_term_attribute_termid FOREIGN KEY (termid) REFERENCES thesaurus_term(identifier) ON DELETE CASCADE;


--
-- TOC entry 3183 (class 2606 OID 20696)
-- Name: fk_custom_term_attribute_typeid; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_term_attribute
    ADD CONSTRAINT fk_custom_term_attribute_typeid FOREIGN KEY (typeid) REFERENCES custom_term_attribute_type(identifier) ON DELETE CASCADE;


--
-- TOC entry 3167 (class 2606 OID 20922)
-- Name: fk_group_parent_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY concept_group
    ADD CONSTRAINT fk_group_parent_concept FOREIGN KEY (parentconceptid) REFERENCES thesaurus_concept(identifier) ON DELETE SET NULL;


--
-- TOC entry 3164 (class 2606 OID 21160)
-- Name: fk_node_label_languages_iso639; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY node_label
    ADD CONSTRAINT fk_node_label_languages_iso639 FOREIGN KEY (lang) REFERENCES languages_iso639(id);


--
-- TOC entry 3163 (class 2606 OID 20160)
-- Name: fk_node_label_thesaurus_array; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY node_label
    ADD CONSTRAINT fk_node_label_thesaurus_array FOREIGN KEY (thesaurusarrayid) REFERENCES thesaurus_array(identifier) ON DELETE CASCADE;


--
-- TOC entry 3156 (class 2606 OID 21174)
-- Name: fk_note_languages_iso639; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY note
    ADD CONSTRAINT fk_note_languages_iso639 FOREIGN KEY (lang) REFERENCES languages_iso639(id);


--
-- TOC entry 3153 (class 2606 OID 20052)
-- Name: fk_note_notetype; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY note
    ADD CONSTRAINT fk_note_notetype FOREIGN KEY (notetypecode) REFERENCES note_type(code);


--
-- TOC entry 3154 (class 2606 OID 20057)
-- Name: fk_note_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY note
    ADD CONSTRAINT fk_note_thesaurus_concept FOREIGN KEY (conceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3155 (class 2606 OID 20062)
-- Name: fk_note_thesaurus_term; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY note
    ADD CONSTRAINT fk_note_thesaurus_term FOREIGN KEY (termid) REFERENCES thesaurus_term(identifier) ON DELETE CASCADE;


--
-- TOC entry 3150 (class 2606 OID 20005)
-- Name: fk_parent_hierarchical_relationship_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY hierarchical_relationship
    ADD CONSTRAINT fk_parent_hierarchical_relationship_thesaurus_concept FOREIGN KEY (parentconceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3178 (class 2606 OID 20582)
-- Name: fk_preferredterm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compound_equivalence
    ADD CONSTRAINT fk_preferredterm FOREIGN KEY (id_preferredterm) REFERENCES thesaurus_term(identifier) ON DELETE CASCADE;


--
-- TOC entry 3159 (class 2606 OID 20442)
-- Name: fk_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY associative_relationship
    ADD CONSTRAINT fk_role FOREIGN KEY (role) REFERENCES associative_relationship_role(code);


--
-- TOC entry 3152 (class 2606 OID 20025)
-- Name: fk_root_top_relationship_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY top_relationship
    ADD CONSTRAINT fk_root_top_relationship_thesaurus_concept FOREIGN KEY (rootconceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3176 (class 2606 OID 21190)
-- Name: fk_snpt_languages_iso639; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY split_nonpreferredterm
    ADD CONSTRAINT fk_snpt_languages_iso639 FOREIGN KEY (lang) REFERENCES languages_iso639(id);


--
-- TOC entry 3175 (class 2606 OID 20568)
-- Name: fk_snpt_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY split_nonpreferredterm
    ADD CONSTRAINT fk_snpt_thesaurus FOREIGN KEY (thesaurusid) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3177 (class 2606 OID 21048)
-- Name: fk_split_nonpreferredterm; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY compound_equivalence
    ADD CONSTRAINT fk_split_nonpreferredterm FOREIGN KEY (id_split_nonpreferredterm) REFERENCES split_nonpreferredterm(identifier) ON DELETE CASCADE;


--
-- TOC entry 3207 (class 2606 OID 21041)
-- Name: fk_suggestion_concept_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY suggestion
    ADD CONSTRAINT fk_suggestion_concept_id FOREIGN KEY (concept_id) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3206 (class 2606 OID 21036)
-- Name: fk_suggestion_term_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY suggestion
    ADD CONSTRAINT fk_suggestion_term_id FOREIGN KEY (term_id) REFERENCES thesaurus_term(identifier) ON DELETE CASCADE;


--
-- TOC entry 3148 (class 2606 OID 21205)
-- Name: fk_term_languages_iso639; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term
    ADD CONSTRAINT fk_term_languages_iso639 FOREIGN KEY (lang) REFERENCES languages_iso639(id);


--
-- TOC entry 3146 (class 2606 OID 20294)
-- Name: fk_term_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term
    ADD CONSTRAINT fk_term_thesaurus FOREIGN KEY (thesaurusid) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3147 (class 2606 OID 20299)
-- Name: fk_term_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term
    ADD CONSTRAINT fk_term_thesaurus_concept FOREIGN KEY (conceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3145 (class 2606 OID 19984)
-- Name: fk_term_thesaurus_term_role; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term
    ADD CONSTRAINT fk_term_thesaurus_term_role FOREIGN KEY (role) REFERENCES thesaurus_term_role(code);


--
-- TOC entry 3165 (class 2606 OID 20274)
-- Name: fk_thesaurus_array_concept_thesaurus_array; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_array_concept
    ADD CONSTRAINT fk_thesaurus_array_concept_thesaurus_array FOREIGN KEY (thesaurusarrayid) REFERENCES thesaurus_array(identifier) ON DELETE CASCADE;


--
-- TOC entry 3166 (class 2606 OID 20279)
-- Name: fk_thesaurus_array_concept_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_array_concept
    ADD CONSTRAINT fk_thesaurus_array_concept_thesaurus_concept FOREIGN KEY (conceptid) REFERENCES thesaurus_concept(identifier) ON DELETE CASCADE;


--
-- TOC entry 3160 (class 2606 OID 20269)
-- Name: fk_thesaurus_array_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_array
    ADD CONSTRAINT fk_thesaurus_array_thesaurus FOREIGN KEY (thesaurusid) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3161 (class 2606 OID 20138)
-- Name: fk_thesaurus_array_thesaurus_concept; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_array
    ADD CONSTRAINT fk_thesaurus_array_thesaurus_concept FOREIGN KEY (superordinateconceptid) REFERENCES thesaurus_concept(identifier);


--
-- TOC entry 3187 (class 2606 OID 20724)
-- Name: fk_thesaurus_formats_format_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_formats
    ADD CONSTRAINT fk_thesaurus_formats_format_identifier FOREIGN KEY (format_identifier) REFERENCES thesaurus_format(identifier) ON DELETE CASCADE;


--
-- TOC entry 3188 (class 2606 OID 20729)
-- Name: fk_thesaurus_formats_thesaurus_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_formats
    ADD CONSTRAINT fk_thesaurus_formats_thesaurus_identifier FOREIGN KEY (thesaurus_identifier) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3180 (class 2606 OID 20706)
-- Name: fk_thesaurus_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_term_attribute_type
    ADD CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3179 (class 2606 OID 20711)
-- Name: fk_thesaurus_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY custom_concept_attribute_type
    ADD CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3142 (class 2606 OID 21103)
-- Name: fk_thesaurus_languages_languages_iso639_id; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages
    ADD CONSTRAINT fk_thesaurus_languages_languages_iso639_id FOREIGN KEY (iso639_id) REFERENCES languages_iso639(id) ON DELETE CASCADE;


--
-- TOC entry 3141 (class 2606 OID 20284)
-- Name: fk_thesaurus_languages_thesaurus_identifier; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_languages
    ADD CONSTRAINT fk_thesaurus_languages_thesaurus_identifier FOREIGN KEY (thesaurus_identifier) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3139 (class 2606 OID 19902)
-- Name: fk_thesaurus_thesaurus_organization; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus
    ADD CONSTRAINT fk_thesaurus_thesaurus_organization FOREIGN KEY (creator) REFERENCES thesaurus_organization(identifier);


--
-- TOC entry 3140 (class 2606 OID 19907)
-- Name: fk_thesaurus_type; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus
    ADD CONSTRAINT fk_thesaurus_type FOREIGN KEY (type) REFERENCES thesaurus_type(identifier);


--
-- TOC entry 3143 (class 2606 OID 20459)
-- Name: fk_thesaurus_version_history_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_version_history
    ADD CONSTRAINT fk_thesaurus_version_history_thesaurus FOREIGN KEY (thesaurus_identifier) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3205 (class 2606 OID 21017)
-- Name: fk_user_role_thesaurus; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fk_user_role_thesaurus FOREIGN KEY (thesaurus_id) REFERENCES thesaurus(identifier) ON DELETE CASCADE;


--
-- TOC entry 3198 (class 2606 OID 20862)
-- Name: fka0197937d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY associative_relationship_aud
    ADD CONSTRAINT fka0197937d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3197 (class 2606 OID 20849)
-- Name: fkb18c9db3d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY top_relationship_aud
    ADD CONSTRAINT fkb18c9db3d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3190 (class 2606 OID 20795)
-- Name: fkb34829cbd0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_aud
    ADD CONSTRAINT fkb34829cbd0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3192 (class 2606 OID 20800)
-- Name: fkbfbbfde2d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_term_aud
    ADD CONSTRAINT fkbfbbfde2d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3195 (class 2606 OID 20835)
-- Name: fkcd658dffd0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY hierarchical_relationship_aud
    ADD CONSTRAINT fkcd658dffd0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- TOC entry 3189 (class 2606 OID 20805)
-- Name: fke94d7a05c144bbed; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY revinfoentitytypes
    ADD CONSTRAINT fke94d7a05c144bbed FOREIGN KEY (revision) REFERENCES revinfo(rev);


--
-- TOC entry 3194 (class 2606 OID 20810)
-- Name: fke9bd902d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY thesaurus_thesaurusversionhistory_aud
    ADD CONSTRAINT fke9bd902d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


-- Completed on 2013-12-17 10:52:56 CET

--
-- PostgreSQL database dump complete
--

