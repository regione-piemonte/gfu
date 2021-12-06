/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface Erogazione {
  idErogazione?: number;
  idTipoErogazione: number;
  descTipoErogazione: string;
  idFinanziamento: number;
  importoErogazione: number;
  valuta: string;
  numDetermina?: string;
  dataDetermina?: string;
}
