/*
* Copyright Regione Piemonte - 2021
* SPDX-License-Identifier: EUPL-1.2-or-later.
*/

/* Drop Views */

DROP VIEW IF EXISTS gfu_v_praurb
;

/* Drop Tables */

DROP TABLE IF EXISTS gfu_r_comuni_forma_associativa CASCADE
;

DROP TABLE IF EXISTS gfu_r_richiedente_provv CASCADE
;

DROP TABLE IF EXISTS gfu_r_richiesta_provv CASCADE
;

DROP TABLE IF EXISTS gfu_r_richiesta_richiedente CASCADE
;

DROP TABLE IF EXISTS gfu_t_erogazione CASCADE
;

DROP TABLE IF EXISTS gfu_t_pratica_urb CASCADE
;

DROP TABLE IF EXISTS gfu_t_finanziamento_rinuncia CASCADE
;

DROP TABLE IF EXISTS gfu_t_finanziamento CASCADE
;

DROP TABLE IF EXISTS gfu_t_forma_associativa CASCADE
;

DROP TABLE IF EXISTS gfu_t_legge_provv_dr CASCADE
;

DROP TABLE IF EXISTS gfu_t_parametri_appl CASCADE
;

DROP TABLE IF EXISTS gfu_t_richiedente CASCADE
;

DROP TABLE IF EXISTS gfu_t_richiesta CASCADE
;

DROP TABLE IF EXISTS gfu_t_utente CASCADE
;

DROP TABLE IF EXISTS gfu_d_dr CASCADE
;

DROP TABLE IF EXISTS gfu_d_legge CASCADE
;

DROP TABLE IF EXISTS gfu_d_parere CASCADE
;

DROP TABLE IF EXISTS gfu_d_percentuale CASCADE
;

DROP TABLE IF EXISTS gfu_d_profilo CASCADE
;

DROP TABLE IF EXISTS gfu_d_provvedimento CASCADE
;

DROP TABLE IF EXISTS gfu_d_rendiconto CASCADE
;

DROP TABLE IF EXISTS gfu_d_stato_finanziamento CASCADE
;

DROP TABLE IF EXISTS gfu_d_tetto_max CASCADE
;

DROP TABLE IF EXISTS gfu_d_tipo_dr CASCADE
;

DROP TABLE IF EXISTS gfu_d_tipo_erogazione CASCADE
;

DROP TABLE IF EXISTS gfu_d_tipo_forma_associativa CASCADE
;

DROP TABLE IF EXISTS gfu_d_vincolo_popolazione CASCADE
;

DROP TABLE IF EXISTS csi_log_audit CASCADE
;




/* Drop Sequences */

DROP SEQUENCE  IF EXISTS  seq_id_audit  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_associazione_unione_comune  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_dr  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_erogazione  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_pratica_urb  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_finanziamento_rinuncia  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_finanziamento  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_forma_associativa  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_legge  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_legge_provv_dr  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_parametro  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_parere  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_percentuale  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_profilo  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_provvedimento  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_rendiconto  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_richiedente  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_richiesta  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_stato_finanziamento  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_tetto_max  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_tipo_dr  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_tipo_erogazione  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_tipo_forma_associativa  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_unione  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_utente  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_vincolo_popolazione  CASCADE
;

DROP SEQUENCE  IF EXISTS  seq_id_determina  CASCADE
;

/* Create Sequences */

CREATE SEQUENCE seq_id_audit
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_associazione_unione_comune
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_dr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_erogazione
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_pratica_urb
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_finanziamento_rinuncia
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_finanziamento
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_forma_associativa
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_legge
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_legge_provv_dr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_parametro
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_parere
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_percentuale
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_profilo
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_provvedimento
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_rendiconto
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_richiedente
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_richiesta
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_stato_finanziamento
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_tetto_max
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_tipo_dr
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_tipo_erogazione
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_tipo_forma_associativa
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_unione
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_utente
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_vincolo_popolazione
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

CREATE SEQUENCE seq_id_determina
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1
;

/* Create Tables */

CREATE TABLE csi_log_audit
(
	audit_id numeric NOT NULL   DEFAULT nextval('seq_id_audit'::regclass),
	data_ora timestamp without time zone NOT NULL,
	id_app varchar(100) NOT NULL,
	ip_address varchar(40) NOT NULL,
	utente varchar(100) NOT NULL,
	operazione varchar(50) NOT NULL,
	ogg_oper varchar(50000) NOT NULL,
	key_oper varchar(500) NULL
)
;

CREATE TABLE gfu_d_dr
(
	id_dr numeric NOT NULL   DEFAULT nextval('seq_id_dr'::regclass),
	fk_tipo_dr numeric NOT NULL,
	numero varchar(25) NULL,
	data date NULL,
	descrizione varchar(200) NOT NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_d_legge
(
	id_legge numeric NOT NULL   DEFAULT nextval('seq_id_legge'::regclass),
	descrizione varchar(100) NOT NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_d_parere
(
	id_parere numeric NOT NULL   DEFAULT nextval('seq_id_parere'::regclass),
	descrizione varchar(100) NOT NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_d_percentuale
(
	id_percentuale numeric NOT NULL   DEFAULT nextval('seq_id_percentuale'::regclass),
	valore_percentuale numeric NOT NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_d_profilo
(
	id_profilo numeric NOT NULL   DEFAULT nextval('seq_id_profilo'::regclass),
	descrizione varchar(50) NOT NULL,
	cod_profilo varchar(10) NOT NULL
)
;

CREATE TABLE gfu_d_provvedimento
(
	id_provvedimento numeric NOT NULL   DEFAULT nextval('seq_id_provvedimento'::regclass),
	descrizione varchar(150) NOT NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_d_rendiconto
(
	id_rendiconto numeric NOT NULL   DEFAULT nextval('seq_id_rendiconto'::regclass),
	descrizione varchar(50) NOT NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_d_stato_finanziamento
(
	id_stato_finanziamento numeric NOT NULL   DEFAULT nextval('seq_id_stato_finanziamento'::regclass),
	descrizione varchar(50) NOT NULL,
	cod_stato_finanziamento varchar(7) NOT NULL
)
;

CREATE TABLE gfu_d_tetto_max
(
	id_tetto_max numeric NOT NULL   DEFAULT nextval('seq_id_tetto_max'::regclass),
	importo numeric NOT NULL,
	valuta varchar(50) NOT NULL,
	data_inizio date NOT NULL,
	data_fine date NULL
)
;

CREATE TABLE gfu_d_tipo_dr
(
	id_tipo_dr numeric(5) NOT NULL   DEFAULT nextval('seq_id_tipo_dr'::regclass),
	cod_tipo_dr varchar(50) NOT NULL,
	descrizione varchar(100) NOT NULL
)
;

CREATE TABLE gfu_d_tipo_erogazione
(
	id_tipo_erogazione numeric NOT NULL   DEFAULT nextval('seq_id_tipo_erogazione'::regclass),
	descrizione varchar(50) NOT NULL,
	cod_tipo_erogazione varchar(3) NOT NULL
)
;

CREATE TABLE gfu_d_tipo_forma_associativa
(
	id_tipo_forma_associativa numeric NOT NULL   DEFAULT nextval('seq_id_tipo_forma_associativa'::regclass),	-- CONVENZIONE ASSOCIAZIONE ----------------- Frutto del porting del pregresso:  CONSORZIO (DEPRECATO) COMUNITA' MONTANE (DEPRECATO)
	descrizione varchar(50) NOT NULL,	-- Unione di Comuni | CONVENZIONI | Consorzi | Comunit?? Montane
	cod_tipo varchar(8) NOT NULL,	-- Cod. sintetico univoco  UNIONE | CONVENZIONE | CONSORZ | COM_MONT
	is_valid boolean NULL
)
;

CREATE TABLE gfu_d_vincolo_popolazione
(
	id_vincolo_popolazione numeric NOT NULL   DEFAULT nextval('seq_id_vincolo_popolazione'::regclass),
	segno varchar(1) NOT NULL,
	popolazione numeric NOT NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_r_comuni_forma_associativa
(
	fk_forma_associativa numeric NOT NULL,
	istat_comune varchar(50) NOT NULL
)
;

CREATE TABLE gfu_r_richiedente_provv
(
	fk_richiedente numeric NOT NULL,
	fk_legge_provv_dr numeric NOT NULL,
	flg_documentazione boolean NOT NULL   DEFAULT false,
	flg_rinuncia boolean NOT NULL   DEFAULT false
)
;

CREATE TABLE gfu_r_richiesta_provv
(
	fk_richiesta numeric NOT NULL,
	fk_legge_provv_dr numeric NOT NULL,
	fk_finanziamento numeric NULL
)
;

CREATE TABLE gfu_r_richiesta_richiedente
(
	fk_richiesta numeric NOT NULL,	-- In questa "tabella di relazione" lego la richiesta al richidente e al tipo. Aggiungo il tipo perch?? in caso di COMUNE ?? possibile che lo stesso richiedente si presenti con cappelli diversi  - appartengo a un' UNIONE - ho sottoscritto una CONVENZIONE - mi presento da solo  NB (il tipo richidente modifica la percentuale di  importo finanziabile).
	fk_richiedente numeric NOT NULL	-- Ogni richiesta pu?? contenere n richiedenti di tipo diverso
)
;

CREATE TABLE gfu_t_erogazione
(
	id_erogazione numeric NOT NULL   DEFAULT nextval('seq_id_erogazione'::regclass),
	fk_finanziamento numeric NOT NULL,
	fk_tipo_erogazione numeric NOT NULL,
	importo_erogazione numeric (30,2) NOT NULL,
	valuta varchar(10) NOT NULL,
	num_determina varchar(50) NULL,
	data_determina date NULL
)
;

CREATE TABLE gfu_t_finanziamento
(
	id_finanziamento numeric NOT NULL   DEFAULT nextval('seq_id_finanziamento'::regclass),
	fk_legge_provv_dr numeric NULL,
	importo_finanziabile numeric (30,2) NULL,
	fk_percentuale numeric NULL,
	importo_ammesso numeric (30,2) NULL,
	valuta varchar(10) NULL,
	fk_parere numeric NULL,
	fk_rendiconto numeric NULL,
	atto_approvazione varchar(200) NULL,
	note varchar(400) NULL,
	fk_stato_finanziamento numeric NOT NULL,
	fk_stato_finanz_prec numeric NULL,
	fk_tetto_max numeric NULL,
	fk_finanziamento_rinuncia numeric NULL
)
;

CREATE TABLE gfu_t_pratica_urb
(
	id_pratica_urb numeric NOT NULL   DEFAULT nextval('seq_id_pratica_urb'::regclass),
	num_pratica_urb varchar(20) NOT NULL,
	fk_finanziamento numeric NOT NULL,
	num_atto_approvazione_urb varchar(20) NULL,
	data_atto_approvazione_urb date NULL
)
;

CREATE TABLE gfu_t_finanziamento_rinuncia
(
	id_finanziamento_rinuncia numeric NOT NULL   DEFAULT nextval('seq_id_finanziamento_rinuncia'::regclass),
	importo numeric (30,2) NOT NULL,
	valuta varchar(50) NOT NULL,
	atto_rinuncia varchar(250) NULL
)
;

CREATE TABLE gfu_t_forma_associativa
(
	id_forma_associativa numeric NOT NULL   DEFAULT nextval('seq_id_forma_associativa'::regclass),
	fk_tipo_forma_associativa numeric NOT NULL,	-- Chiave per la tabella di dec per individuare se la forma associativa censita ??  UNIONE di COMUNI CONVENZIONI  CONSORZI   (deprecate) COMUNITA MONTANE (deprecate)
	cod_forma_associativa varchar(25) NOT NULL,	-- E' il codice univoco  CHIAVE LOGICA che permetter?? poi l'aggancio al servizio nel caso delle UNIONI DI COMUNI, per esempio.  Come il COMUNE ha il COD_ISTAT come chiave logica le UNIONI DEI COMUNI hanno il COD_UNIONE o SILGLA_UNIONE come chiave logica
	descrizione varchar(200) NOT NULL
)
;

CREATE TABLE gfu_t_legge_provv_dr
(
	id_legge_provv_dr numeric NOT NULL   DEFAULT nextval('seq_id_legge_provv_dr'::regclass),
	fk_legge numeric NOT NULL,
	fk_provvedimento numeric NULL,
	fk_dr numeric NULL,
	descrizione varchar(450) NOT NULL,
	fk_vincolo_popolazione numeric NULL,
	is_valid boolean NULL
)
;

CREATE TABLE gfu_t_parametri_appl
(
	id_parametro numeric NOT NULL   DEFAULT nextval('seq_id_parametro'::regclass),
	key varchar(50) NULL,
	valore varchar(50) NULL,
	descrizione varchar(100) NULL	-- Tabella che accoglie parametri configurazioni dall'applicativo dall'utente. In particolare il primo che verr?? gestito sar?? IMPORTO_MASSIMO_FINANZIABILE (che nel tempo potrebbe variare)
)
;

CREATE TABLE gfu_t_richiedente
(
	id_richiedente numeric NOT NULL   DEFAULT nextval('seq_id_richiedente'::regclass),
	istat_comune varchar(50) NOT NULL,	-- Questa colonna conterr?? l'id esterno della chiamata ai servizi. Rispettivamente il  COD_ISTAT o COD_BELFIORE  (da valutare quale) del Comune PARTITA_IVA dell' Unione dei comuni IDENTIFICATIVO_INTERNO per il pregresso (Comunit?? montane e Consorzi)
	popolazione numeric NOT NULL
)
;

CREATE TABLE gfu_t_richiesta
(
	id_richiesta numeric NOT NULL   DEFAULT nextval('seq_id_richiesta'::regclass),
	data_protocollo date NOT NULL,
	num_protocollo varchar(15) NOT NULL,
	fk_forma_associativa numeric NULL,
	note varchar(50) NULL,
	is_valid boolean NULL   DEFAULT true
)
;

CREATE TABLE gfu_t_utente
(
	id_utente numeric NOT NULL   DEFAULT nextval('seq_id_utente'::regclass),
	cognome varchar(50) NULL,
	nome varchar(50) NULL,
	codice_fiscale varchar(16) NULL,
	email varchar(50) NULL,
	fk_profilo numeric NULL,
	data_inserimento date NULL,
	data_cancellazione date NULL
)
;



/* Create Views */

CREATE OR REPLACE VIEW gfu_v_praurb AS 
SELECT richieste.legge_provv_dr,
       richieste.numero_protocollo,
       richieste.data_protocollo,
       richieste.richiedente_istat,
       richieste.richiedente,
       richieste.elenco_comuni_istat,
       richieste.elenco_comuni,
       richieste.stato_finanziamento,
       richieste.importo_finanziabile,
       richieste.numero_atto_approvazione,
       richieste.data_atto_approvazione,
       richieste.numero_pratica_x_praurb,
       richieste.atto_approvazione,
       richieste.parere,
       richieste.rendiconto,
       richieste.acconto,
       richieste.num_determina_acconto,
       richieste.data_determina_acconto,
       richieste.saldo,
       richieste.num_determina_saldo,
       richieste.data_determina_saldo
FROM ( 
 SELECT ra.id_richiesta,
        lpd.descrizione AS legge_provv_dr,
        ra.num_protocollo AS numero_protocollo,
        ra.data_protocollo,
        DECODE(ra.fk_forma_associativa,NULL,string_agg(re.istat_comune,', '),NULL) AS richiedente_istat,
        DECODE(ra.fk_forma_associativa,NULL,string_agg((SELECT((yla.desc_comune||' (')||yla.sigla_prov)||')' FROM yucca_t_regpie_istat_limiti_amministrativi yla WHERE yla.istat_comune = re.istat_comune AND yla.istat_regione = '01' AND yla.d_stop = (SELECT MAX(yla1.d_stop) FROM yucca_t_regpie_istat_limiti_amministrativi yla1 WHERE yla1.istat_comune = yla.istat_comune)),', '),(SELECT fa.descrizione FROM gfu_t_forma_associativa fa WHERE fa.id_forma_associativa = ra.fk_forma_associativa)) AS richiedente,
        DECODE(ra.fk_forma_associativa,NULL,NULL,string_agg(re.istat_comune,', ')) AS elenco_comuni_istat,
        DECODE(ra.fk_forma_associativa,NULL,NULL,string_agg((SELECT ((yla.desc_comune||' (')||yla.sigla_prov)||')' FROM yucca_t_regpie_istat_limiti_amministrativi yla WHERE yla.istat_comune = re.istat_comune AND yla.istat_regione = '01' AND yla.d_stop = (SELECT MAX(yla1.d_stop) FROM yucca_t_regpie_istat_limiti_amministrativi yla1 WHERE yla1.istat_comune = yla.istat_comune)), ', ')) AS elenco_comuni,
        DECODE(FIN.stato_finanziamento,NULL,'IN CORSO',FIN.stato_finanziamento) AS STATO_FINANZIAMENTO,
        FIN.importo_finanziabile,
        FIN.numero_atto_approvazione,
        FIN.data_atto_approvazione,
        FIN.numero_pratica_x_praurb,
        FIN.atto_approvazione,
        FIN.parere,
        FIN.rendiconto,
        FIN.acconto,
        FIN.num_determina_acconto,
        FIN.data_determina_acconto,
        FIN.saldo,
        FIN.num_determina_saldo,
        FIN.data_determina_saldo
   FROM gfu.gfu_t_legge_provv_dr lpd,
        gfu_t_richiesta ra,
        gfu_r_richiesta_provv rp LEFT JOIN (SELECT f.id_finanziamento,
                                                   (SELECT sf.descrizione FROM gfu_d_stato_finanziamento sf WHERE sf.id_stato_finanziamento = f.fk_stato_finanziamento) AS stato_finanziamento,
                                                   f.importo_finanziabile,
                                                   pu.num_atto_approvazione_urb numero_atto_approvazione,
                                                   pu.data_atto_approvazione_urb data_atto_approvazione,
                                                   pu.num_pratica_urb numero_pratica_x_praurb,
                                                   f.atto_approvazione,
                                                   (SELECT p.descrizione FROM gfu_d_parere p WHERE p.id_parere = f.fk_parere) AS parere,
                                                   (SELECT rend.descrizione FROM gfu_d_rendiconto rend WHERE rend.id_rendiconto = f.fk_rendiconto) AS rendiconto,
                                                   e.acconto,
                                                   e.num_determina_acconto,
                                                   e.data_determina_acconto,
                                                   e.saldo,
                                                   e.num_determina_saldo,
                                                   e.data_determina_saldo
                                            FROM gfu_t_finanziamento f LEFT JOIN (SELECT acc.fk_finanziamento,
                                                                                         acc.importo_erogazione AS acconto,
                                                                                         sal.importo_erogazione AS saldo,
                                                                                         acc.num_determina AS num_determina_acconto,
                                                                                         acc.data_determina AS data_determina_acconto,
                                                                                         sal.num_determina AS num_determina_saldo,
                                                                                         sal.data_determina AS data_determina_saldo
                                                                                  FROM gfu_t_erogazione acc LEFT JOIN gfu_t_erogazione sal ON sal.fk_finanziamento = acc.fk_finanziamento
                                                                                  WHERE acc.fk_tipo_erogazione = 1
		                                                                          AND sal.fk_tipo_erogazione = 2) e ON e.fk_finanziamento = f.id_finanziamento
		                                                               LEFT JOIN gfu_t_pratica_urb pu ON f.id_finanziamento = pu.fk_finanziamento) FIN ON FIN.id_finanziamento = rp.fk_finanziamento,
        gfu_t_richiedente re,
        gfu_r_richiesta_richiedente rare,
        gfu_r_richiedente_provv rep
  WHERE ra.id_richiesta = rp.fk_richiesta
    AND ra.id_richiesta = rare.fk_richiesta
    AND lpd.id_legge_provv_dr = rp.fk_legge_provv_dr
    AND lpd.id_legge_provv_dr = rep.fk_legge_provv_dr
    AND re.id_richiedente = rare.fk_richiedente
    AND re.id_richiedente = rep.fk_richiedente
    and rep.fk_legge_provv_dr = rp.fk_legge_provv_dr
	AND rep.flg_rinuncia != TRUE
	AND FIN.stato_finanziamento NOT IN (SELECT sf.descrizione FROM gfu_d_stato_finanziamento sf WHERE sf.cod_stato_finanziamento IN ('RESP','REVOC','ANNXRIN'))
 GROUP BY ra.id_richiesta,
          lpd.descrizione,
          ra.num_protocollo,
		  ra.data_protocollo,
		  ra.fk_forma_associativa,
		  FIN.stato_finanziamento,
		  FIN.importo_finanziabile,
		  FIN.numero_atto_approvazione,
          FIN.data_atto_approvazione,
          FIN.numero_pratica_x_praurb,
		  FIN.atto_approvazione,
		  FIN.parere,
		  FIN.rendiconto,
		  FIN.acconto,
		  FIN.num_determina_acconto,
		  FIN.data_determina_acconto,
		  FIN.saldo,
		  FIN.num_determina_saldo,
		  FIN.data_determina_saldo) richieste
;

/* Create Primary Keys, Indexes, Uniques, Checks */

ALTER TABLE csi_log_audit ADD CONSTRAINT pk_csi_log_audit
	PRIMARY KEY (audit_id)
;

CREATE UNIQUE INDEX ixpk_csi_log_audit ON csi_log_audit (audit_id ASC)
;

ALTER TABLE gfu_d_dr ADD CONSTRAINT pk_gfu_d_dr
	PRIMARY KEY (id_dr)
;

CREATE INDEX ixfk_gfu_d_dr_gfu_d_tipo_dr ON gfu_d_dr (fk_tipo_dr ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_d_dr ON gfu_d_dr (id_dr ASC)
;

ALTER TABLE gfu_d_legge ADD CONSTRAINT pk_gfu_d_legge
	PRIMARY KEY (id_legge)
;

CREATE UNIQUE INDEX ixpk_gfu_d_legge ON gfu_d_legge (id_legge ASC)
;

ALTER TABLE gfu_d_parere ADD CONSTRAINT pk_gfu_d_parere
	PRIMARY KEY (id_parere)
;

CREATE UNIQUE INDEX ixpk_gfu_d_parere ON gfu_d_parere (id_parere ASC)
;

ALTER TABLE gfu_d_percentuale ADD CONSTRAINT pk_gfu_d_percentuale
	PRIMARY KEY (id_percentuale)
;

CREATE UNIQUE INDEX ixpk_gfu_d_percentuale ON gfu_d_percentuale (id_percentuale ASC)
;

ALTER TABLE gfu_d_profilo ADD CONSTRAINT pk_gfu_d_profilo
	PRIMARY KEY (id_profilo)
;

CREATE UNIQUE INDEX ixpk_gfu_d_profilo ON gfu_d_profilo (id_profilo ASC)
;

ALTER TABLE gfu_d_provvedimento ADD CONSTRAINT pk_gfu_d_provvedimento
	PRIMARY KEY (id_provvedimento)
;

CREATE UNIQUE INDEX ixpk_gfu_d_provvedimento ON gfu_d_provvedimento (id_provvedimento ASC)
;

ALTER TABLE gfu_d_rendiconto ADD CONSTRAINT pk_gfu_d_rendiconto
	PRIMARY KEY (id_rendiconto)
;

CREATE UNIQUE INDEX ixpk_gfu_d_rendiconto ON gfu_d_rendiconto (id_rendiconto ASC)
;

ALTER TABLE gfu_d_stato_finanziamento ADD CONSTRAINT pk_gfu_d_stato_finanziamento
	PRIMARY KEY (id_stato_finanziamento)
;

CREATE UNIQUE INDEX ixpk_gfu_d_stato_finanziamento ON gfu_d_stato_finanziamento (id_stato_finanziamento ASC)
;

ALTER TABLE gfu_d_tetto_max ADD CONSTRAINT pk_gfu_d_tetto_max
	PRIMARY KEY (id_tetto_max)
;

CREATE UNIQUE INDEX ixpk_gfu_d_tetto_max ON gfu_d_tetto_max (id_tetto_max ASC)
;

ALTER TABLE gfu_d_tipo_dr ADD CONSTRAINT pk_gfu_d_tipo_dr
	PRIMARY KEY (id_tipo_dr)
;

CREATE UNIQUE INDEX ixpk_gfu_d_tipo_dr ON gfu_d_tipo_dr (id_tipo_dr ASC)
;

ALTER TABLE gfu_d_tipo_erogazione ADD CONSTRAINT pk_gfu_d_tipo_erogazione
	PRIMARY KEY (id_tipo_erogazione)
;

CREATE UNIQUE INDEX ixpk_gfu_d_tipo_erogazione ON gfu_d_tipo_erogazione (id_tipo_erogazione ASC)
;

ALTER TABLE gfu_d_tipo_forma_associativa ADD CONSTRAINT pk_gfu_d_tipo_forma_associativa
	PRIMARY KEY (id_tipo_forma_associativa)
;

CREATE UNIQUE INDEX ixpk_gfu_d_tipo_forma_associativa ON gfu_d_tipo_forma_associativa (id_tipo_forma_associativa ASC)
;

ALTER TABLE gfu_d_vincolo_popolazione ADD CONSTRAINT pk_gfu_d_vincolo_popolazione
	PRIMARY KEY (id_vincolo_popolazione)
;

CREATE UNIQUE INDEX ixpk_gfu_d_vincolo_popolazione ON gfu_d_vincolo_popolazione (id_vincolo_popolazione ASC)
;

ALTER TABLE gfu_r_comuni_forma_associativa ADD CONSTRAINT pk_gfu_r_comuni_forma_associativa
	PRIMARY KEY (fk_forma_associativa,istat_comune)
;

CREATE INDEX ixfk_gfu_r_comuni_forma_associativa_gfu_t_forma_associativa ON gfu_r_comuni_forma_associativa (fk_forma_associativa ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_r_comuni_forma_associativa ON gfu_r_comuni_forma_associativa (fk_forma_associativa ASC,istat_comune ASC)
;

ALTER TABLE gfu_r_richiedente_provv ADD CONSTRAINT pk_gfu_r_richiedente_provv
	PRIMARY KEY (fk_richiedente,fk_legge_provv_dr)
;

CREATE INDEX ixfk_gfu_r_richiedente_provv_gfu_t_richiedente ON gfu_r_richiedente_provv (fk_richiedente ASC)
;

CREATE INDEX ixfk_gfu_r_richiedente_provv_gfu_t_legge_provv_dr ON gfu_r_richiedente_provv (fk_legge_provv_dr ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_r_richiedente_provv ON gfu_r_richiedente_provv (fk_richiedente ASC,fk_legge_provv_dr ASC)
;

ALTER TABLE gfu_r_richiesta_provv ADD CONSTRAINT pk_gfu_r_richiesta_provv
	PRIMARY KEY (fk_richiesta,fk_legge_provv_dr)
;

CREATE UNIQUE INDEX ixpk_gfu_r_richiesta_provv ON gfu_r_richiesta_provv (fk_richiesta ASC,fk_legge_provv_dr ASC)
;

CREATE INDEX ixfk_gfu_r_richiesta_provv_gfu_t_richiesta ON gfu_r_richiesta_provv (fk_richiesta ASC)
;

CREATE INDEX ixfk_gfu_r_richiesta_provv_gfu_t_legge_provv_dr ON gfu_r_richiesta_provv (fk_legge_provv_dr ASC)
;

CREATE INDEX ixfk_gfu_r_richiesta_provv_gfu_t_finanziamento ON gfu_r_richiesta_provv (fk_finanziamento ASC)
;

ALTER TABLE gfu_r_richiesta_richiedente ADD CONSTRAINT pk_gfu_r_richiesta_richiedente
	PRIMARY KEY (fk_richiesta,fk_richiedente)
;

CREATE INDEX ixfk_gfu_r_richiesta_richiedente_gfu_t_richiedente ON gfu_r_richiesta_richiedente (fk_richiedente ASC)
;

CREATE INDEX ixfk_gfu_r_richiesta_richiedente_gfu_t_richiesta ON gfu_r_richiesta_richiedente (fk_richiesta ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_r_richiesta_richiedente ON gfu_r_richiesta_richiedente (fk_richiesta ASC,fk_richiedente ASC)
;

ALTER TABLE gfu_t_erogazione ADD CONSTRAINT pk_gfu_t_erogazione
	PRIMARY KEY (id_erogazione)
;

CREATE INDEX ixfk_gfu_t_erogazione_gfu_d_tipo_erogazione ON gfu_t_erogazione (fk_tipo_erogazione ASC)
;

CREATE INDEX ixfk_gfu_t_erogazione_gfu_t_finanziamento ON gfu_t_erogazione (fk_finanziamento ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_t_erogazione ON gfu_t_erogazione (id_erogazione ASC)
;

ALTER TABLE gfu_t_pratica_urb ADD CONSTRAINT pk_gfu_t_pratica_urb
	PRIMARY KEY (id_pratica_urb)
;

CREATE UNIQUE INDEX ixpk_gfu_t_pratica_urb ON gfu_t_pratica_urb (id_pratica_urb ASC)
;

ALTER TABLE gfu_t_finanziamento ADD CONSTRAINT pk_gfu_t_finanziamento
	PRIMARY KEY (id_finanziamento)
;

CREATE INDEX ixfk_gfu_t_finanziamento_gfu_d_parere ON gfu_t_finanziamento (fk_parere ASC)
;

CREATE INDEX ixfk_gfu_t_finanziamento_gfu_d_percentuale ON gfu_t_finanziamento (fk_percentuale ASC)
;

CREATE INDEX ixfk_gfu_t_finanziamento_gfu_d_rendiconto ON gfu_t_finanziamento (fk_rendiconto ASC)
;

CREATE INDEX ixfk_gfu_t_finanziamento_gfu_d_stato_finanziamento ON gfu_t_finanziamento (fk_stato_finanziamento ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_t_finanziamento ON gfu_t_finanziamento (id_finanziamento ASC)
;

CREATE INDEX ixfk_gfu_t_finanziamento_gfu_t_finanziamento_rinuncia ON gfu_t_finanziamento (fk_finanziamento_rinuncia ASC)
;

CREATE INDEX ixfk_gfu_t_finanziamento_gfu_d_tetto_max ON gfu_t_finanziamento (fk_tetto_max ASC)
;

ALTER TABLE gfu_t_finanziamento_rinuncia ADD CONSTRAINT pk_gfu_t_finanziamento_rinuncia
	PRIMARY KEY (id_finanziamento_rinuncia)
;

CREATE INDEX ixpk_gfu_t_finanziamento_rinuncia ON gfu_t_finanziamento_rinuncia (id_finanziamento_rinuncia ASC)
;

ALTER TABLE gfu_t_forma_associativa ADD CONSTRAINT pk_gfu_t_forma_associativa
	PRIMARY KEY (id_forma_associativa)
;

CREATE INDEX ixfk_gfu_t_forma_associativa_gfu_d_tipo_forma_associativa ON gfu_t_forma_associativa (fk_tipo_forma_associativa ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_t_forma_associativa ON gfu_t_forma_associativa (id_forma_associativa ASC)
;

ALTER TABLE gfu_t_legge_provv_dr ADD CONSTRAINT pk_gfu_t_legge_provv_dr
	PRIMARY KEY (id_legge_provv_dr)
;

CREATE INDEX ixfk_gfu_t_legge_provv_dr_gfu_d_dr ON gfu_t_legge_provv_dr (fk_dr ASC)
;

CREATE INDEX ixfk_gfu_t_legge_provv_dr_gfu_d_provvedimento ON gfu_t_legge_provv_dr (fk_provvedimento ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_t_legge_provv_dr ON gfu_t_legge_provv_dr (id_legge_provv_dr ASC)
;

CREATE INDEX ixfk_gfu_t_legge_provv_dr_gfu_d_vincolo_popolazione ON gfu_t_legge_provv_dr (fk_vincolo_popolazione ASC)
;

CREATE INDEX ixfk_gfu_t_legge_provv_dr_gfu_d_legge ON gfu_t_legge_provv_dr (fk_legge ASC)
;

ALTER TABLE gfu_t_parametri_appl ADD CONSTRAINT pk_gfu_t_parametri_appl
	PRIMARY KEY (id_parametro)
;

CREATE UNIQUE INDEX ixpk_gfu_t_parametri_appl ON gfu_t_parametri_appl (id_parametro ASC)
;

ALTER TABLE gfu_t_richiedente ADD CONSTRAINT pk_gfu_t_richiedente
	PRIMARY KEY (id_richiedente)
;

CREATE UNIQUE INDEX ixpk_gfu_t_richiedente ON gfu_t_richiedente (id_richiedente ASC)
;

ALTER TABLE gfu_t_richiesta ADD CONSTRAINT pk_gfu_t_richiesta
	PRIMARY KEY (id_richiesta)
;

CREATE INDEX ixfk_gfu_t_richiesta_gfu_t_forma_associativa ON gfu_t_richiesta (fk_forma_associativa ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_t_richiesta ON gfu_t_richiesta (id_richiesta ASC)
;

ALTER TABLE gfu_t_utente ADD CONSTRAINT pk_gfu_t_utente
	PRIMARY KEY (id_utente)
;

CREATE INDEX ixfk_gfu_t_utente_gfu_d_profilo ON gfu_t_utente (fk_profilo ASC)
;

CREATE UNIQUE INDEX ixpk_gfu_t_utente ON gfu_t_utente (id_utente ASC)
;

ALTER TABLE yucca_r_regpie_associazione_unioni_comuni ADD CONSTRAINT pk_yucca_r_regpie_associazione_unioni_comuni
	PRIMARY KEY (id_associazione_unione_comune)
;

CREATE UNIQUE INDEX ixpk_yucca_r_regpie_associazione_unioni_comuni ON yucca_r_regpie_associazione_unioni_comuni (id_associazione_unione_comune ASC)
;

CREATE INDEX ix_yucca_r_regpie_associazione_unioni_comuni_desc_unione ON yucca_r_regpie_associazione_unioni_comuni (desc_unione ASC)
;

ALTER TABLE yucca_t_regpie_istat_limiti_amministrativi ADD CONSTRAINT pk_yucca_t_istat_limiti_amministrativi_rp
	PRIMARY KEY (id_comune)
;

CREATE UNIQUE INDEX ixpk_yucca_t_istat_limiti_amministrativi_rp ON yucca_t_regpie_istat_limiti_amministrativi (id_comune ASC)
;

ALTER TABLE yucca_t_regpie_istat_popolazione_residente ADD CONSTRAINT pk_yucca_t_istat_popolazione_residente_rp
	PRIMARY KEY (id_comune,anno,istat_comune)
;

CREATE INDEX ixpk_yucca_t_istat_popolazione_residente_rp ON yucca_t_regpie_istat_popolazione_residente (id_comune ASC,anno ASC)
;

ALTER TABLE yucca_t_regpie_unioni_comuni ADD CONSTRAINT pk_yucca_t_regpie_unioni_comuni
	PRIMARY KEY (id_unione)
;

CREATE UNIQUE INDEX ixpk_yucca_t_regpie_unioni_comuni ON yucca_t_regpie_unioni_comuni (id_unione ASC)
;

CREATE INDEX ix_yucca_t_regpie_unioni_comuni_desc_unione ON yucca_t_regpie_unioni_comuni (desc_unione ASC)
;

ALTER TABLE atti_t_determina ADD CONSTRAINT pk_id_determina
	PRIMARY KEY (id_determina)
;

CREATE UNIQUE INDEX ixpk_id_determina ON atti_t_determina (id_determina ASC)
;

/* Create Foreign Key Constraints */

ALTER TABLE gfu_d_dr ADD CONSTRAINT fk_gfu_d_dr_gfu_d_tipo_dr
	FOREIGN KEY (fk_tipo_dr) REFERENCES gfu_d_tipo_dr (id_tipo_dr) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_comuni_forma_associativa ADD CONSTRAINT fk_gfu_r_comuni_forma_associativa_gfu_t_forma_associativa
	FOREIGN KEY (fk_forma_associativa) REFERENCES gfu_t_forma_associativa (id_forma_associativa) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_richiedente_provv ADD CONSTRAINT fk_gfu_r_richiedente_provv_gfu_t_legge_provv_dr
	FOREIGN KEY (fk_legge_provv_dr) REFERENCES gfu_t_legge_provv_dr (id_legge_provv_dr) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_richiedente_provv ADD CONSTRAINT fk_gfu_r_richiedente_provv_gfu_t_richiedente
	FOREIGN KEY (fk_richiedente) REFERENCES gfu_t_richiedente (id_richiedente) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_richiesta_provv ADD CONSTRAINT fk_gfu_r_richiesta_provv_gfu_t_finanziamento
	FOREIGN KEY (fk_finanziamento) REFERENCES gfu_t_finanziamento (id_finanziamento) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_richiesta_provv ADD CONSTRAINT fk_gfu_r_richiesta_provv_gfu_t_legge_provv_dr
	FOREIGN KEY (fk_legge_provv_dr) REFERENCES gfu_t_legge_provv_dr (id_legge_provv_dr) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_richiesta_provv ADD CONSTRAINT fk_gfu_r_richiesta_provv_gfu_t_richiesta
	FOREIGN KEY (fk_richiesta) REFERENCES gfu_t_richiesta (id_richiesta) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_richiesta_richiedente ADD CONSTRAINT fk_gfu_r_richiesta_richiedente_gfu_t_richiedente
	FOREIGN KEY (fk_richiedente) REFERENCES gfu_t_richiedente (id_richiedente) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_r_richiesta_richiedente ADD CONSTRAINT fk_gfu_r_richiesta_richiedente_gfu_t_richiesta
	FOREIGN KEY (fk_richiesta) REFERENCES gfu_t_richiesta (id_richiesta) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_erogazione ADD CONSTRAINT fk_gfu_t_erogazione_gfu_d_tipo_erogazione
	FOREIGN KEY (fk_tipo_erogazione) REFERENCES gfu_d_tipo_erogazione (id_tipo_erogazione) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_erogazione ADD CONSTRAINT fk_gfu_t_erogazione_gfu_t_finanziamento
	FOREIGN KEY (fk_finanziamento) REFERENCES gfu_t_finanziamento (id_finanziamento) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_pratica_urb ADD CONSTRAINT fk_gfu_t_pratica_urb_gfu_t_finanziamento
	FOREIGN KEY (fk_finanziamento) REFERENCES gfu_t_finanziamento (id_finanziamento) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_finanziamento ADD CONSTRAINT fk_gfu_t_finanziamento_gfu_d_parere
	FOREIGN KEY (fk_parere) REFERENCES gfu_d_parere (id_parere) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_finanziamento ADD CONSTRAINT fk_gfu_t_finanziamento_gfu_d_percentuale
	FOREIGN KEY (fk_percentuale) REFERENCES gfu_d_percentuale (id_percentuale) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_finanziamento ADD CONSTRAINT fk_gfu_t_finanziamento_gfu_d_rendiconto
	FOREIGN KEY (fk_rendiconto) REFERENCES gfu_d_rendiconto (id_rendiconto) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_finanziamento ADD CONSTRAINT fk_gfu_t_finanziamento_gfu_d_stato_finanziamento
	FOREIGN KEY (fk_stato_finanziamento) REFERENCES gfu_d_stato_finanziamento (id_stato_finanziamento) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_finanziamento ADD CONSTRAINT fk_gfu_t_finanziamento_gfu_d_tetto_max
	FOREIGN KEY (fk_tetto_max) REFERENCES gfu_d_tetto_max (id_tetto_max) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_finanziamento ADD CONSTRAINT fk_gfu_t_finanziamento_gfu_t_finanziamento_rinuncia
	FOREIGN KEY (fk_finanziamento_rinuncia) REFERENCES gfu_t_finanziamento_rinuncia (id_finanziamento_rinuncia) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_forma_associativa ADD CONSTRAINT fk_gfu_t_forma_associativa_gfu_d_tipo_forma_associativa
	FOREIGN KEY (fk_tipo_forma_associativa) REFERENCES gfu_d_tipo_forma_associativa (id_tipo_forma_associativa) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_legge_provv_dr ADD CONSTRAINT fk_gfu_r_legge_provv_dr_gfu_d_dr
	FOREIGN KEY (fk_dr) REFERENCES gfu_d_dr (id_dr) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_legge_provv_dr ADD CONSTRAINT fk_gfu_r_legge_provv_dr_gfu_d_legge
	FOREIGN KEY (fk_legge) REFERENCES gfu_d_legge (id_legge) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_legge_provv_dr ADD CONSTRAINT fk_gfu_r_legge_provv_dr_gfu_d_provvedimento
	FOREIGN KEY (fk_provvedimento) REFERENCES gfu_d_provvedimento (id_provvedimento) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_legge_provv_dr ADD CONSTRAINT fk_gfu_t_legge_provv_dr_gfu_d_vincolo_popolazione
	FOREIGN KEY (fk_vincolo_popolazione) REFERENCES gfu_d_vincolo_popolazione (id_vincolo_popolazione) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_richiesta ADD CONSTRAINT fk_gfu_t_richiesta_gfu_t_forma_associativa
	FOREIGN KEY (fk_forma_associativa) REFERENCES gfu_t_forma_associativa (id_forma_associativa) ON DELETE No Action ON UPDATE No Action
;

ALTER TABLE gfu_t_utente ADD CONSTRAINT fk_gfu_t_utente_gfu_d_profilo
	FOREIGN KEY (fk_profilo) REFERENCES gfu_d_profilo (id_profilo) ON DELETE No Action ON UPDATE No Action
;

/* Create Table Comments, Sequences for Autonumber Columns */

COMMENT ON TABLE gfu_d_legge
	IS 'Tabella di anagrafica Leggi Regionali'
;

COMMENT ON TABLE gfu_d_tipo_forma_associativa
	IS 'Tabella che censisce la tipologia di richiedente: coloro che possono beneficiare della richiesta:

COMUNI | CONVENZIONE | UNIONE DI  COMUNI  (Allo stato attuale, ma
bisogner?? anche gestire il pregresso ossia

COMUNITA'' MONTANE, CONSORZI, deprecati per legge) 

Questa tabella viene alimentata nel momento 
dell''inserimento delle convenzioni essendo come 
tipo un "usa e getta".
L'
;

COMMENT ON COLUMN gfu_d_tipo_forma_associativa.id_tipo_forma_associativa
	IS 'CONVENZIONE
ASSOCIAZIONE
-----------------
Frutto del porting del pregresso:

CONSORZIO (DEPRECATO)
COMUNITA'' MONTANE (DEPRECATO)'
;

COMMENT ON COLUMN gfu_d_tipo_forma_associativa.descrizione
	IS 'Unione di Comuni | CONVENZIONI | Consorzi | Comunit?? Montane'
;

COMMENT ON COLUMN gfu_d_tipo_forma_associativa.cod_tipo
	IS 'Cod. sintetico univoco

UNIONE | CONVENZIONE | CONSORZ | COM_MONT'
;

COMMENT ON COLUMN gfu_r_richiesta_richiedente.fk_richiesta
	IS 'In questa "tabella di relazione" lego la richiesta
al richidente e al tipo. Aggiungo il tipo perch?? in caso di COMUNE ?? possibile che lo stesso richiedente si presenti
con cappelli diversi 
- appartengo a un'' UNIONE
- ho sottoscritto una CONVENZIONE
- mi presento da solo

NB (il tipo richidente modifica la percentuale di 
importo finanziabile).'
;

COMMENT ON COLUMN gfu_r_richiesta_richiedente.fk_richiedente
	IS 'Ogni richiesta pu?? contenere n richiedenti di tipo diverso'
;

COMMENT ON TABLE gfu_t_finanziamento
	IS 'IMPORTO_AMMESSO:  viene calcolato ed ?? in ro? se fosse cos?? togliamolo.

Se il problema fosse il pregresso (verifica sul vecchio DB)
potremmo fare una distinzione tra dato nuovo e dato importato dal vecchio DB)'
;

COMMENT ON TABLE gfu_t_forma_associativa
	IS 'Questa tavola ha il mandato di gestire DATI delle 
UNIONI dei Comuni il cui servizio adesso non si pu?? costruire, ma arriver??.
Inoltre vanno a confluire anche i dati del 
pregresso delle FORME associative il cui impianto avverr??
one shot alla messa in ese.

Dunque le forse associative da gestire saranno


UNIONI DI COMUNI (In futuro ci sar?? un servizio)
CONVENZIONI (data entry nell''interfaccia di ins della richiesta in cui si potranno associare i comuni convenzionati)
CONSORZI (deprecati, ma presenti per la vista sul pregresso)
COMUNITA_MONTANE (deprecati, ma presenti per la vista sul pregresso)'
;

COMMENT ON COLUMN gfu_t_forma_associativa.fk_tipo_forma_associativa
	IS 'Chiave per la tabella di dec per
individuare se la forma associativa censita ??

UNIONE di COMUNI
CONVENZIONI 
CONSORZI   (deprecate)
COMUNITA MONTANE (deprecate)'
;

COMMENT ON COLUMN gfu_t_forma_associativa.cod_forma_associativa
	IS 'E'' il codice univoco  CHIAVE LOGICA che permetter?? poi l''aggancio al servizio nel caso delle UNIONI DI COMUNI, per esempio.

Come il COMUNE ha il COD_ISTAT come chiave logica
le UNIONI DEI COMUNI hanno il COD_UNIONE o SILGLA_UNIONE come chiave logica'
;

COMMENT ON COLUMN gfu_t_parametri_appl.descrizione
	IS 'Tabella che accoglie parametri configurazioni dall''applicativo dall''utente.
In particolare
il primo che verr?? gestito sar??
IMPORTO_MASSIMO_FINANZIABILE (che nel tempo potrebbe variare)'
;

COMMENT ON COLUMN gfu_t_richiedente.istat_comune
	IS 'Questa colonna conterr?? l''id esterno della chiamata ai servizi.
Rispettivamente il

COD_ISTAT o COD_BELFIORE  (da valutare quale) del Comune
PARTITA_IVA dell'' Unione dei comuni
IDENTIFICATIVO_INTERNO per il pregresso (Comunit?? montane e Consorzi)'
;

COMMENT ON TABLE gfu_t_richiesta
	IS 'Tabella che censisce le richieste di finanziamento che i Comuni presentano a Regione.'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.tipologia
	IS 'Tipologia'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.r_status
	IS 'Stato del record:
1=attuale; 
2=modificato; 
0=cessato.
N.B. lo stato 2=modificato si riferisce alla variazione nei comuni che compongono l???unione'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.provincia
	IS 'Sigla provincia'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.istat_comune
	IS 'Codice ISTAT comune.
N.B.: se si gestisce lo storico non ?? univoco'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.d_stop
	IS 'Data fine validit??'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.d_start
	IS 'Data inizio validit??'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.desc_unione_siope
	IS 'Denominazione dell???unione da SIOPE'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.desc_unione
	IS 'Denominazione dell???unione'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.desc_comune
	IS 'Denominazione del comune'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.cod_provincia
	IS 'Codice ISTAT della provincia'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.cod_istat_unione
	IS 'Codice ISTAT unione (non ?? stato possibile valorizzare il campo per l???unione dei comuni della pianura biellese, l???unione dei comuni del biellese centrale e l???unione della morena frontale canavesana)

N.B.: Si ?? provveduto ad assegnare un Codice ISTAT unione temporaneo dove non risultava essere valorizzato:
TMP#####1 per COLLINE DEL RIDDONE;
TMP#####2 per UNIONE DEI COMUNI DEL BIELLESE CENTRALE
TMP#####3 per UNIONE DEI COMUNI DELLA PIANURA BIELLESE
TMP#####4 per UNIONE DELLA MORENA FRONTALE CANAVESANA'
;

COMMENT ON COLUMN yucca_r_regpie_associazione_unioni_comuni.cod_fisc_unione
	IS 'Codice Fiscale dell''unione'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.id_comune_next
	IS 'ID Comune NEXT (Quando r_status=2 punta all??? ID successivo)'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.id_comune_prev
	IS 'ID Comune PREV (Punta all???ID precedente se il record relativo al Comune ha subito modifiche)'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.d_start
	IS 'Data inizio validit?? del record'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.d_stop
	IS 'Data fine validit?? del record'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.r_status
	IS 'Stato del record:
1=attuale;
2=modificato;
0=cessato.
N.B.: lo stato 2=modificato si riferisce alla variazione di ISTAT comune, denominazione comune, ISTAT provincia, denominazione provincia, sigla provincia, regione'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.cod_catasto
	IS 'Codice catastale del comune'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.istat_comune
	IS 'Codice ISTAT del comune'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.desc_comune
	IS 'Denominazione del comune'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.cap
	IS 'Codice di Avviamento Postale'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.altitudine
	IS 'Altitudine'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.superficie_hm2
	IS 'Superficie in ettometri quadri'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.istat_provincia
	IS 'Codice ISTAT della provincia'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.desc_provincia
	IS 'Denominazione della provincia'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.sigla_prov
	IS 'Sigla della provincia'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.istat_zona_altimetrica
	IS 'Codice ISTAT della zona altimetrica'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.desc_zona_altimetrica
	IS 'Descrizione della zona altimetrica'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.istat_regione
	IS 'Codice ISTAT della regione'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.desc_regione
	IS 'Denominazione della regione'
;

COMMENT ON COLUMN yucca_t_regpie_istat_limiti_amministrativi.cod_stato
	IS 'Denominazione dello stato'
;

COMMENT ON COLUMN yucca_t_regpie_istat_popolazione_residente.anno
	IS 'Anno di riferimento'
;

COMMENT ON COLUMN yucca_t_regpie_istat_popolazione_residente.data_aggiornamento_istat
	IS 'Data relativa all???ultimo aggiornamento ISTAT'
;

COMMENT ON COLUMN yucca_t_regpie_istat_popolazione_residente.istat_comune
	IS 'Codice ISTAT del comune'
;

COMMENT ON COLUMN yucca_t_regpie_istat_popolazione_residente.pop_maschile
	IS 'Totale popolazione maschile'
;

COMMENT ON COLUMN yucca_t_regpie_istat_popolazione_residente.pop_femminile
	IS 'Totale popolazione femminile'
;

COMMENT ON COLUMN yucca_t_regpie_istat_popolazione_residente.pop_totale
	IS 'Popolazione totale (maschile e femminile)'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.tipologia
	IS 'Tipologia unione'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.r_status
	IS 'Stato del record:
1=attuale;
2=modificato; 
0=cessato. 
N.B. lo stato 2=modificato si riferisce alla variazione nei comuni che compongono l???unione'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.d_stop_unione
	IS 'Data fine validit?? dell???unione'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.d_stop
	IS 'Data fine validit??.
N.B. indica le modifiche di composizione dell???unione'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.d_start_unione
	IS 'Data inizio validit?? dell???unione'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.d_start
	IS 'Data inizio validit??.
N.B. indica le modifiche di composizione dell???unione'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.desc_unione_siope
	IS 'Denominazione dell???unione da SIOPE'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.desc_unione
	IS 'Denominazione dell???unione'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.cod_istat_unione
	IS 'Codice ISTAT unione (non ?? stato possibile valorizzare il campo per l???unione dei comuni della pianura biellese, l???unione dei comuni del biellese centrale e l???unione della morena frontale canavesana)

N.B.: Si ?? provveduto ad assegnare un Codice ISTAT unione temporaneo dove non risultava essere valorizzato:
TMP#####1 per COLLINE DEL RIDDONE;
TMP#####2 per UNIONE DEI COMUNI DEL BIELLESE CENTRALE
TMP#####3 per UNIONE DEI COMUNI DELLA PIANURA BIELLESE
TMP#####4 per UNIONE DELLA MORENA FRONTALE CANAVESANA'
;

COMMENT ON COLUMN yucca_t_regpie_unioni_comuni.cod_fisc_unione
	IS 'Codice fiscale unione (non ?? stato possibile valorizzare il campo per l???unione dei comuni della pianura biellese, l???unione dei comuni del biellese centrale e l???unione della morena frontale canavesana)'
;