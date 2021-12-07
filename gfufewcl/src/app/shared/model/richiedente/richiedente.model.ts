/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { DocumentazioneRinuncia } from './documentazione-rinuncia.model';

export interface Richiedente {
  idRichiedente?: number;
  istatComune: string;
  descComune?: string;
  popolazione: number;
  descProvincia?: string;
  siglaProvincia?: string;
  provvedimentiToRichiedente?: DocumentazioneRinuncia[];
}
