/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface Richiesta {
  idRichiesta?: number;
  dataProtocollo: string;
  numProtocollo: string;
  note?: string;
  idAssociazione?: number;
  descAssociazione?: string;
  codTipoFormaAssociativa?: string;
  descTipoFormaAssociativa?: string;
  // formaAssociativa?: FormaAssociativa;
  // richiedenti?: Richiedente[];
}
