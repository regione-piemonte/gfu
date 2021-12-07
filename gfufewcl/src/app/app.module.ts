/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { BrowserModule } from '@angular/platform-browser';
import { APP_INITIALIZER, LOCALE_ID, NgModule } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbAccordionModule, NgbDateParserFormatter, NgbModalModule, NgbTypeaheadModule } from '@ng-bootstrap/ng-bootstrap';
import { Ng2SmartTableModule } from 'ng2-smart-table';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { SecurityService } from './shared/services/security.service';
import { DateParserFormatter } from './shared/utils/date-parser-formatter';
import { LoadService } from './shared/services/load.service';

import { ProfiloComponent } from './profilo/profilo.component';
import { ToolbarComponent } from './toolbar/toolbar.component';
import { HomePageComponent } from './home-page/home-page.component';

import { RichiestaContainerComponent } from './richiesta/richiesta-container/richiesta-container.component';
import { RichiedentiComponent } from './richiesta/richiesta-container/richiedenti/richiedenti.component';
import { ProvvedimentiComponent } from './richiesta/richiesta-container/provvedimenti/provvedimenti.component';
import { FinanziamentiComponent } from './richiesta/richiesta-container/finanziamenti/finanziamenti.component';
import { RichiestaFinanziamentoSearchComponent } from './richiesta/richiesta-finanziamento-search/richiesta-finanziamento-search.component';
import { AssociaDeterminaComponent } from './richiesta/associa-determina/associa-determina.component';

import { UtentiComponent } from './anagrafica/utenti/utenti.component';
import { PercentualiComponent } from './anagrafica/percentuali/percentuali.component';
import { PareriComponent } from './anagrafica/pareri/pareri.component';
import { RendicontiComponent } from './anagrafica/rendiconti/rendiconti.component';
import { ProvvedimentiAnagComponent } from './anagrafica/provvedimenti/provvedimenti-anag.component';
import { VincoloPopolazioneComponent } from './anagrafica/vincolo-popolazione/vincolo-popolazione.component';
import { TettoMaxComponent } from './anagrafica/tetto-max/tetto-max.component';

import localeIt from '@angular/common/locales/it';

// registerLocaleData(LOCALE_ID, 'it');
registerLocaleData(localeIt);

export function get_settings(appLoadService: LoadService) {
  return (): Promise<any> => {
    return appLoadService.init();
  };
}

@NgModule({
  declarations: [
    AppComponent,
    RichiedentiComponent,
    RichiestaFinanziamentoSearchComponent,
    ToolbarComponent,
    ProfiloComponent, // not used.
    HomePageComponent,
    RichiestaContainerComponent,
    ProvvedimentiComponent,
    FinanziamentiComponent,
    UtentiComponent, // used instead of the old ProfiloComponent
    PercentualiComponent,
    PareriComponent,
    RendicontiComponent,
    ProvvedimentiAnagComponent,
    AssociaDeterminaComponent,
    VincoloPopolazioneComponent,
    TettoMaxComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    NgbTypeaheadModule,
    NgbModalModule,
    Ng2SmartTableModule,
    NgbAccordionModule
  ],
  providers: [ SecurityService,
    { provide: NgbDateParserFormatter, useClass: DateParserFormatter },
    { provide: LOCALE_ID, useValue: 'it' },
    LoadService,
    { provide: APP_INITIALIZER, useFactory: get_settings, deps: [LoadService], multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
