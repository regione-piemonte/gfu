/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface ProvvedimentoSearch {
  idRichiesta: number;
  numProtocollo: string;
  dataProtocollo: string;
  idLeggeProvvDr: number;
  descLeggeProvvDr: string;
  idFinanziamento: number;
}
