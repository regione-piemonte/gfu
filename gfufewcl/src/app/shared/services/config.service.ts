/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  constructor() {}

  apiRootPath = '/gfuwebauthless/api';



  //apiRootPath = '/gfuweb/api';

  getHomeUrl() {
    return environment.homeUrl;
  }

  getGfuServiceUrl() {
    return environment.gfuService;
  }



}
