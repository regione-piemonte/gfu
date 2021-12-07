/*
* Copyright Regione Piemonte - 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
*/


DROP TABLE IF EXISTS yucca_r_regpie_associazione_unioni_comuni CASCADE
;

DROP TABLE IF EXISTS yucca_t_regpie_istat_limiti_amministrativi CASCADE
;

DROP TABLE IF EXISTS yucca_t_regpie_istat_popolazione_residente CASCADE
;

DROP TABLE IF EXISTS yucca_t_regpie_unioni_comuni CASCADE
;

DROP TABLE IF EXISTS atti_t_determina CASCADE;


CREATE TABLE yucca_r_regpie_associazione_unioni_comuni
(
	id_associazione_unione_comune numeric NOT NULL   DEFAULT nextval('seq_id_associazione_unione_comune'::regclass),
	tipologia varchar(50) NOT NULL,	-- Tipologia
	r_status integer NOT NULL,	-- Stato del record: 1=attuale;  2=modificato;  0=cessato. N.B. lo stato 2=modificato si riferisce alla variazione nei comuni che compongono l’unione
	provincia varchar(3) NOT NULL,	-- Sigla provincia
	istat_comune varchar(6) NOT NULL,	-- Codice ISTAT comune
	d_stop date NOT NULL,	-- Data fine validità
	d_start date NOT NULL,	-- Data inizio validità
	desc_unione_siope varchar(250) NULL,	-- Denominazione dell’unione da SIOPE
	desc_unione varchar(250) NOT NULL,	-- Denominazione dell’unione
	desc_comune varchar(250) NOT NULL,	-- Denominazione del comune
	cod_provincia varchar(3) NOT NULL,	-- Codice ISTAT della provincia
	cod_istat_unione varchar(15) NOT NULL,	-- Codice ISTAT unione 
	cod_fisc_unione varchar(11) NULL,	-- Codice Fiscale dell'unione
	desc_provincia varchar(100) NOT NULL
)
;

CREATE TABLE yucca_t_regpie_istat_limiti_amministrativi
(
	id_comune integer NOT NULL,
	id_comune_next integer NULL,	-- ID Comune NEXT (Quando r_status=2 punta all’ ID successivo)
	id_comune_prev integer NULL,	-- ID Comune PREV (Punta all’ID precedente se il record relativo al Comune ha subito modifiche)
	d_start date NOT NULL,	-- Data inizio validità del record
	d_stop date NOT NULL,	-- Data fine validità del record
	r_status integer NULL,	-- Stato del record: 1=attuale; 2=modificato; 0=cessato. N.B.: lo stato 2=modificato si riferisce alla variazione di ISTAT comune, denominazione comune, ISTAT provincia, denominazione provincia, sigla provincia, regione
	cod_catasto varchar(4) NOT NULL,	-- Codice catastale del comune
	istat_comune varchar(6) NOT NULL,	-- Codice ISTAT del comune
	desc_comune varchar(100) NOT NULL,	-- Denominazione del comune
	cap varchar(5) NULL,	-- Codice di Avviamento Postale
	altitudine integer NULL,	-- Altitudine
	superficie_hm2 integer NULL,	-- Superficie in ettometri quadri
	istat_provincia varchar(3) NOT NULL,	-- Codice ISTAT della provincia
	desc_provincia varchar(100) NOT NULL,	-- Denominazione della provincia
	sigla_prov varchar(2) NOT NULL,	-- Sigla della provincia
	istat_zona_altimetrica integer NULL,	-- Codice ISTAT della zona altimetrica
	desc_zona_altimetrica varchar(100) NULL,	-- Descrizione della zona altimetrica
	istat_regione varchar(2) NOT NULL,	-- Codice ISTAT della regione
	desc_regione varchar(100) NOT NULL,	-- Denominazione della regione
	cod_stato varchar(3) NOT NULL,	-- Denominazione dello stato
	desc_stato varchar(50) NOT NULL
)
;

CREATE TABLE yucca_t_regpie_istat_popolazione_residente
(
	id_comune numeric NOT NULL,
	anno numeric NOT NULL,	-- Anno di riferimento
	data_aggiornamento_istat date NULL,	-- Data relativa all’ultimo aggiornamento ISTAT
	istat_comune varchar(6) NOT NULL,	-- Codice ISTAT del comune
	pop_maschile numeric NULL,	-- Totale popolazione maschile
	pop_femminile numeric NULL,	-- Totale popolazione femminile
	pop_totale numeric NOT NULL	-- Popolazione totale (maschile e femminile)
)
;

CREATE TABLE yucca_t_regpie_unioni_comuni
(
	id_unione numeric NOT NULL   DEFAULT nextval('seq_id_unione'::regclass),
	tipologia varchar(50) NOT NULL,	-- Tipologia unione
	r_status integer NOT NULL,	-- Stato del record: 1=attuale; 2=modificato;  0=cessato.  N.B. lo stato 2=modificato si riferisce alla variazione nei comuni che compongono l’unione
	d_stop_unione date NOT NULL,	-- Data fine validità dell’unione
	d_stop date NOT NULL,	-- Data fine validità. N.B. indica le modifiche di composizione dell’unione
	d_start_unione date NOT NULL,	-- Data inizio validità dell’unione
	d_start date NOT NULL,	-- Data inizio validità. N.B. indica le modifiche di composizione dell’unione
	desc_unione_siope varchar(250) NULL,	-- Denominazione dell’unione da SIOPE
	desc_unione varchar(250) NOT NULL,	-- Denominazione dell’unione
	cod_istat_unione varchar(15) NOT NULL,	-- Codice ISTAT unione 
	cod_fisc_unione varchar(11) NULL,	-- Codice fiscale unione 
	cod_tipo varchar(8) NOT NULL
)
;

CREATE TABLE atti_t_determina
(
	id_determina numeric NOT NULL   DEFAULT nextval('seq_id_determina'::regclass),
	cod_direzione varchar(6) NOT NULL,
	numero numeric NOT NULL,
	data date NOT NULL,
	url varchar(250) NOT NULL
)
;