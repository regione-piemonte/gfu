/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
// import { Utente } from 'src/app/profilo/model/utente';
import { Utente } from '../model/utente/utente.model';


export let APP_SETTINGS = {
  codiceFiscale: '',
  codProfilo: '',
  cognome: '',
  nome: '',
  utente: new Utente()
};
