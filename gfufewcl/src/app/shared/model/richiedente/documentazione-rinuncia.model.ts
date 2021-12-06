/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface DocumentazioneRinuncia {
  idLeggeProvvDr: number;
  descLeggeProvvDr?: string;
  descVincoloPopolazione?: string;
  flagDocumentazione: boolean;
  flagRinuncia: boolean;
}
