/**
 * @license
 * Copyright CSI-Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Injectable } from '@angular/core';
import { Router} from '@angular/router';
import { Observable, of } from 'rxjs';
import { SrvError } from '../model/srv-error';
import { timeout, retryWhen, catchError } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Utente } from 'src/app/profilo/model/utente';
import { selectiveRetryStrategy } from './security.util';



@Injectable()

export class SecurityService {

  constructor(
    private router: Router,
    private config: ConfigService,
    private http: HttpClient
    ) { console.log('....Redirecting to PP '); }

/*
   * @param url permette di saltare ad un url esterno
   */

  public jumpToURL(url: string) {
    console.log('....Redirecting to ' + url + '...');
    this.router.navigate(['/']).then(result => {window.location.href = url; });
  }

  /**
   * permette di visualizzare una pagina di cortesia
   */
  public jumpToMessagePage() {
    console.log('....Redirecting to message page...');
    this.router.navigate(['/messagepage']);
  }

  /*
     * effettua l'invalidazione della sessione applicativa, sia sul
     * client che sul server, richiamando un apposito servizio di backend
     */

    localLogout(): Observable<Utente | SrvError>  {
            console.log('************   NG - load.service.ts - localLogout() ');
            return this.http
              .get<Utente>(this.config.apiRootPath + '/profilo/utente/sessione')
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


  ssoLogout(): void {
    console.log('************   NG - load.service.ts - ssoLogout() ');
     // this.router.navigate(['/']).then(result => { window.location.href = this.config.getSSOLogoutURL(); });
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
}



}
