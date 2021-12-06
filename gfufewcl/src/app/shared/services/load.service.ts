/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, of, Subscription } from 'rxjs';
import {  retryWhen, timeout } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

import { SrvError } from '../model/srv-error';
import { APP_SETTINGS } from '../settings/app-settings';
// import { Utente } from 'src/app/profilo/model/utente';
import { Utente } from '../model';
import { ConfigService } from './config.service';
import { selectiveRetryStrategy } from './security.util';


@Injectable({
  providedIn: 'root'
})
export class LoadService {
  public utenteCollegatoFull: Utente;
  public utente: Utente;
  public cf: string;
  route: string;
  public router: Router;
  public subscriptionUte: Subscription;
  public logout: boolean;

  constructor(private config: ConfigService, private http: HttpClient) {}

  init() {
    // console.log(' MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM ');

    if (this.logout === true) {
        console.log('Non sei loggato. Devi riautenticarti');
    }

    return new Promise<void>((resolve, reject) => {
      console.log('AppInitService.init() called');
      this.getProfiloCollegato().subscribe(
        (response: any | SrvError) => {
          // console.log(
          //   'NG - dentro alla promise load.componente.ts loadProfiloCollegato()   Utente collegato 1 ',
          //   response
          // );
          this.utenteCollegatoFull = response.fullUtenteDto;
          this.cf = this.utenteCollegatoFull.codiceFiscale;
          console.log('load.component.ts.  CF = ', this.cf);
          console.log('load.component.ts Profilo = ', this.utenteCollegatoFull.profilo);
          console.log('load.component.ts = Cognome = ', this.utenteCollegatoFull.cognome);

          APP_SETTINGS.codiceFiscale = this.cf;
          APP_SETTINGS.cognome = this.utenteCollegatoFull.cognome;
          APP_SETTINGS.nome = this.utenteCollegatoFull.nome;
          // APP_SETTINGS.utente = this.utenteCollegatoFull;
          APP_SETTINGS.codProfilo = this.utenteCollegatoFull.profilo.codProfilo;

          // XXX MOCK
          // APP_SETTINGS.codProfilo = 'GUEST';
          // APP_SETTINGS.codProfilo = 'ADMIN';
          // APP_SETTINGS.codProfilo = 'SUPERADMIN';

         // console.log('***NG ------ APP_SETTINGS.utente  ', APP_SETTINGS.utente);
          console.log('***NG ------ APP_SETTINGS.cognome  ', APP_SETTINGS.cognome);
          console.log('***NG ------ APP_SETTINGS.nome  ', APP_SETTINGS.nome);
          console.log('***NG ------ APP_SETTINGS.codProfilo  ', APP_SETTINGS.codProfilo);
          this.logout = false;
        },
        err => {
          console.log('load.componente.ts loadProfiloCollegato() @@@ERRORE@@@  Utente collegato ');
        }
      );

      setTimeout(() => {
        console.log('AppInitService Finished');
        resolve();
      }, 500);
    });

  }

  logOutUtente() {
      this.logout = true;
  }

  getProfiloCollegato(): Observable<Utente | SrvError> {
    console.log('NG - load.service.ts - getProfiloCollegato()');
    return this.http
      .get<Utente>(this.config.apiRootPath + '/utenti/utente/profilo')
      .pipe(
        timeout(3000),
        retryWhen(
          selectiveRetryStrategy({
            scalingDuration: 10,
            excludedStatusCodes: [302, 0],
            maxRetryAttempts: 2
          })
        ),
        catchError(errorHandler => {
          return of(errorHandler);
        })
      );
  }




  }

