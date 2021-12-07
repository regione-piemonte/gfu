/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { APP_SETTINGS } from '../settings/app-settings';

@Injectable({
  providedIn: 'root'
})
export class ProfileGuard implements CanActivate {

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    const page = next.data.page as string;
    const codProfilo = APP_SETTINGS.codProfilo;

    // routes available only for ADMIN
    const adminPageList = ['anagrafica', 'associa-determina', 'nuova-richiesta'];

    if (codProfilo === 'SUPERADMIN') {
      return true;
    }

    if (codProfilo === 'ADMIN') {
      if (adminPageList.includes(page)) {
        return true;
      }
    }

    return false;
  }

}
