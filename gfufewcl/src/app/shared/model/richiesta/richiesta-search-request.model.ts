/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
export interface RichiestaSearchRequest {
  numProtocollo?: string;
  dataProtocolloDa?: string;
  dataProtocolloA?: string;
  siglaProv?: string;
  istatComune?: string;
  idTipoFormaAssociativa?: number;
  denominazioneAssociazione?: string;
  idLeggeProvvDr?: number;
  idVincoloPopolazione?: number;
  idStatoFinanziamento?: number;
  numDetermina?: string;
  dataDetermina?: string;
  idParere?: number;
  idRendiconto?: number;
  attoRinuncia?: string;
  flagPraticaUrbanisticaAssociata?: boolean;
  numPraticaUrb?: string;
  numAttoApprovazioneUrb?: string;
  dataAttoApprovazioneUrb?: string;
}
