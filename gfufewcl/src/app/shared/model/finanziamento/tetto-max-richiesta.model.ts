/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface TettoMaxRichiesta {
  idTettoMax: number;
  importoTettoMax: number;
  importoTettoMaxTotRichiedenti: number;
  valuta?: string; // todo: check after service is updated
}
