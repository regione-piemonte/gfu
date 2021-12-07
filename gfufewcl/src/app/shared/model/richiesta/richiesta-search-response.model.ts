/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface RichiestaSearchResponse {
  idRichiesta: number;
  numProtocollo: string;
  numPraticaPraurb: string;
  dataProtocollo: string;
  descRichiedente: string;
  descTipoRichiedente: string;
}
