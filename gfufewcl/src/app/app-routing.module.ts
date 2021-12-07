/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ProfileGuard } from './shared/guards/profile.guard';

import { HomePageComponent } from './home-page/home-page.component';

import { RichiestaContainerComponent } from './richiesta/richiesta-container/richiesta-container.component';
import { RichiestaFinanziamentoSearchComponent } from './richiesta/richiesta-finanziamento-search/richiesta-finanziamento-search.component';
import { AssociaDeterminaComponent } from './richiesta/associa-determina/associa-determina.component';

import { UtentiComponent } from './anagrafica/utenti/utenti.component';
import { PercentualiComponent } from './anagrafica/percentuali/percentuali.component';
import { PareriComponent } from './anagrafica/pareri/pareri.component';
import { RendicontiComponent } from './anagrafica/rendiconti/rendiconti.component';
import { ProvvedimentiAnagComponent } from './anagrafica/provvedimenti/provvedimenti-anag.component';
import { VincoloPopolazioneComponent } from './anagrafica/vincolo-popolazione/vincolo-popolazione.component';
import { TettoMaxComponent } from './anagrafica/tetto-max/tetto-max.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  {
    path: 'richiesta',
    children: [
      { path: 'nuova', component: RichiestaContainerComponent, canActivate: [ProfileGuard], data: {page: 'nuova-richiesta'}  /*runGuardsAndResolvers: 'always'*/ },
      { path: 'edit/:id', component: RichiestaContainerComponent },
      { path: 'search', component: RichiestaFinanziamentoSearchComponent },
      { path: 'associa-determina', component: AssociaDeterminaComponent, canActivate: [ProfileGuard], data: {page: 'associa-determina'} }
    ],
  },
  {
    path: 'anagrafica',
    children: [
      { path: 'utenti', component: UtentiComponent, canActivate: [ProfileGuard], data: {page: 'utenti'} },
      { path: 'percentuali', component: PercentualiComponent },
      { path: 'pareri', component: PareriComponent },
      { path: 'rendiconti', component: RendicontiComponent },
      { path: 'provvedimenti', component: ProvvedimentiAnagComponent },
      { path: 'vincolo-popolazione', component: VincoloPopolazioneComponent },
      { path: 'tetto-max', component: TettoMaxComponent }
    ],
    canActivate: [ProfileGuard], data: {page: 'anagrafica'}
  },
];


@NgModule({
  imports: [RouterModule.forRoot(routes/*, { onSameUrlNavigation: 'reload' }*/)],
  exports: [RouterModule]
})
export class AppRoutingModule {


}
