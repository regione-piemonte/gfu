/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { DateParserFormatter } from '../../utils/date-parser-formatter';
import { Profilo } from './profilo.model';

export class Utente {
  idUtente?: number;
  cognome: string;
  nome?: string;
  codiceFiscale: string;
  email?: string;
  dataCancellazione?: DateParserFormatter;
  dataInserimento?: DateParserFormatter;
  profilo: Profilo;

  constructor() {
    this.idUtente = null;
    this.cognome = null;
    this.nome = null;
    this.codiceFiscale = null;
    this.email = null;
    this.dataCancellazione = null;
    this.dataInserimento = null;
    this.profilo = null;
  }
}
