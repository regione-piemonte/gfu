/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { TipoFormaAssociativa } from './tipo-forma-associativa.model';

export interface FormaAssociativa {
  idFormaAssociativa?: number;
  tipoFormaAssociativa: TipoFormaAssociativa;
  codFormaAssociativa?: string;
  descrizione: string;
}
