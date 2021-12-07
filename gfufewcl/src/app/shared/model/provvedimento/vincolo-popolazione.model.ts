/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface VincoloPopolazione {
  idVincoloPopolazione: number;
  descrizione: string;
  segno: string;
  popolazione: number;
  isValid: boolean;
}
