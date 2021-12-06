/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Comune } from './comune.model';

export interface AssociazioneComuni {
  idAssociazione?: number;
  istatAssociazione?: string;
  descAssociazione: string;
  codTipoFormaAssociativa?: string;
  descTipoFormaAssociativa: string;
  comuni: Comune[];
}
