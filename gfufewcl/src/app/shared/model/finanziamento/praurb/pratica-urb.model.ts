/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { AdeguamentoUrb } from './adeguamento-urb.model';
import { ConferenzaUrb } from './conferenza-urb.model';
import { ProvvedimentoUrb } from './provvedimento-urb.model';

export interface PraticaUrb {
  numPratica: string;
  numProtocollo: string;
  dataIngressoRegione: string;
  descTipoPratica: string;
  oggettoPratica: string;
  descStatoPratica: string;
  elencoComunePv: string;
  provvedimentiUrb?: ProvvedimentoUrb[];
  conferenzeUrb?: ConferenzaUrb[];
  adeguamentoUrb?: AdeguamentoUrb[];
}
