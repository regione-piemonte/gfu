/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Erogazione } from './erogazione.model';
import { Parere } from './parere.model';
import { Percentuale } from './percentuale.model';
import { PraticaUrbGfu } from './praurb/pratica-urb-gfu.model';
import { Rendiconto } from './rendiconto.model';
import { Rinuncia } from './rinuncia.model';
import { StatoFinanziamento } from './stato-finanziamento.model';

export interface Finanziamento {
  idFinanziamento?: number;
  idLeggeProvvDr: number;
  importoFinanziabile?: number;
  importoAmmesso?: number;
  valuta?: string;
  parere?: Parere;
  percentuale?: Percentuale;
  rendiconto?: Rendiconto;
  attoApprovazione?: string;
  note?: string;
  statoFinanziamento: StatoFinanziamento;
  statoFinanziamentoPrec?: StatoFinanziamento;
  erogazioni?: Erogazione[];
  fkImportoTettoMax?: number;
  rinuncia?: Rinuncia;
  praticaUrbGfu?: PraticaUrbGfu;
}
