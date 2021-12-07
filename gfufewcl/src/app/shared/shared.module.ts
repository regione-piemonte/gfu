/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseComponent } from './components/base-component/base-component.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { BodyComponent } from './components/body/body.component';
import { AppRoutingModule } from '../app-routing.module';
import { HttpClientModule } from '@angular/common/http';
import { ProfileDirective } from './directives/profile.directive';
import { ModalComponent } from './components/modal/modal.component';

@NgModule({
  declarations: [
    BaseComponent,
    HeaderComponent,
    FooterComponent,
    BodyComponent,
    ProfileDirective,
    ModalComponent
  ],
  entryComponents: [ ModalComponent ],
  imports: [
    CommonModule,
    AppRoutingModule,
    HttpClientModule
  ],
  exports: [
    HeaderComponent,
    BodyComponent,
    FooterComponent,
    BaseComponent
  ]
})
export class SharedModule { }
