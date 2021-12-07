/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { DateParserFormatter } from 'src/app/shared/utils/date-parser-formatter';
import { Profilo } from './profilo';

/* THIS CALSS IS NOT USED ANYMORE */
export class Utente {

  public idUtente: number;
  public cognome: string;
  public nome: string;
  public codiceFiscale: string;
  public mail: string;
  public dataCancellazioneUtente: DateParserFormatter;
  public dataUltimoAccesso: DateParserFormatter;
  public profilo: Profilo;

  constructor() {
    this.idUtente = null;
    this.cognome = null;
    this.nome = null;
    this.codiceFiscale = null;
    this.mail = null;
    this.dataCancellazioneUtente = null;
    this.dataUltimoAccesso = null;
    this.profilo = null;
  }
}

