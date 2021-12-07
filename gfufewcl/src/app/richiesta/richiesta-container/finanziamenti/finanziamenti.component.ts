/**
 * @license
 * Copyright Regione Piemonte - 2021
 *
 * SPDX-License-Identifier: EUPL-1.2-or-later.
 */
import { Component, Inject, Input, LOCALE_ID, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { formatNumber } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { forkJoin, Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap, takeUntil, tap } from 'rxjs/operators';
import { LocalDataSource } from 'ng2-smart-table';

import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { RichiestaService } from 'src/app/shared/services/richiesta.service';
import { AdeguamentoUrb, Erogazione, Finanziamento, LeggeProvvDR, Parere, Percentuale, PraticaUrb, PraticaUrbGfu, PraticaUrbSintetica, Rendiconto,
  Richiedente, StatoFinanziamento, TettoMaxRichiesta, TipoErogazione } from 'src/app/shared/model';

@Component({
  selector: 'app-finanziamenti',
  templateUrl: './finanziamenti.component.html',
  styleUrls: ['./finanziamenti.component.css']
})
export class FinanziamentiComponent implements OnInit, OnDestroy, OnChanges {

  @Input() idRichiesta: number;
  @Input() richiedenti: Richiedente[];
  @Input() provvedimenti: LeggeProvvDR[];
  @Input() tipiErogazione: TipoErogazione[];
  @Input() codProfilo: string;
  @Input() percentuali: Percentuale[];
  @Input() pareri: Parere[];
  @Input() rendiconti: Rendiconto[];

  toggleRadioBtn = true;

  private unsubscribe$ = new Subject();

  selectedTab = 'finanziamento';
  selectedFin: Finanziamento = null;

  statiFinanziamento: StatoFinanziamento[];
  importoTettoMax: TettoMaxRichiesta;

  searchPraUrbResult: PraticaUrbSintetica[];
  praUrbDetails: PraticaUrb;

  finanziamentoForm: FormGroup;
  erogazioniForm: FormGroup;
  searchPraUrbForm: FormGroup;

  enableValida = true;
  enableSospendi = false;
  enableRifiuta = false;
  enableRevoca = true;
  enableAnnulla = false;
  enableRipristina = true;

  enableAttivaAcconto = true;
  enableAttivaSaldo = true;

  enableCercaDetAcconto = false;
  enableCercaDetSaldo = false;
  enableUpdateErogazioni = false;
  determinaUrlAcconto: string;
  determinaUrlSaldo: string;

  tableInitFlag = false;
  rinunciaPresenteFlag = false;

  loading = false;

  provvedimentiTableSource = new LocalDataSource();
  provvedimentiTableColumns;
  provvedimentiTableSettings;

  praUrbTableSource = new LocalDataSource();
  praUrbTableColumns;
  praUrbTableSettings;

  searchComune = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      tap(() => this.loading = true),
      switchMap( input => {
        return this.richiestaService.searchComuni(input)
        .pipe(
          map(list => list.slice(0, 10))
        );
      }),
      tap(() => this.loading = false),
    )

  comuneFieldFormatter = (comune) => comune.descComune;

  constructor(
    private fb: FormBuilder,
    private messageService: MessageService,
    private richiestaService: RichiestaService,
    private anagraficaService: AnagraficaService,
    @Inject(LOCALE_ID) public locale: string
  ) { }

  ngOnChanges(changes: SimpleChanges) {
    this.selectedFin = null;
    this.selectedTab = 'finanziamento';

    this.loadTableData();
  }

  ngOnInit() {
    this.anagraficaService.getStatiFinanziamento().subscribe(
      res => {
        this.statiFinanziamento = res;
        this.configForms();
        this.configTables();
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  callToggle() {
    this.toggleRadioBtn = !this.toggleRadioBtn;
  }

  loadTableData() {
    this.provvedimentiTableSource.load(this.provvedimenti);
    this.provvedimentiTableSource.setSort([{ field: 'descLeggeProvvDr', direction: 'asc' }]);
  }

  configForms() {
    this.finanziamentoForm = this.fb.group({
      stato: [{value: null, disabled: true}],
      note: null,
      tipoImporto: ['percentuale'],
      impFinanziabile: null,
      impAmmesso: [{value: null, disabled: true}],
      percentuale: null,
      importoMaxComune: [{value: null, disabled: true}],
      importoMaxTotale: [{value: null, disabled: true}],
      parere: null,
      attoApprovazione: null,
      rendiconto: null,
      importoRinuncia: [{value: null, disabled: true}],
      attoRinuncia: [{value: null, disabled: true}],
    });

    this.erogazioniForm = this.fb.group({
      impAcconto: [{value: null, disabled: true}],
      numDetAcconto: [{value: null, disabled: true}],
      dataDetAcconto: [{value: null, disabled: true}],
      impSaldo: [{value: null, disabled: true}],
      numDetSaldo: [{value: null, disabled: true}],
      dataDetSaldo: [{value: null, disabled: true}],
    });

    this.searchPraUrbForm = this.fb.group({
      comune: [null, Validators.required],
      dataPraUrbDa: [null, Validators.required],
      dataPraUrbA: [null, Validators.required],
    });

    this.finanziamentoForm.get('note').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
    });

    this.finanziamentoForm.get('tipoImporto').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
      if (value === 'percentuale') {
        this.finanziamentoForm.get('impFinanziabile').enable();
        this.finanziamentoForm.get('percentuale').enable();
      } else if (value === 'tettomax') {
        this.finanziamentoForm.get('impFinanziabile').disable();
        this.finanziamentoForm.get('percentuale').disable();
      }
    });

    this.finanziamentoForm.get('impFinanziabile').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
      if (value && this.finanziamentoForm.get('percentuale').value) {
        value = value.replace('.', '');
        value = value.replace(',', '.');
        const newImp = value * this.finanziamentoForm.get('percentuale').value.valorePercentuale / 100;
        if ( Number.isNaN(newImp) ) {
          this.finanziamentoForm.get('impAmmesso').setValue(null);
        } else {
          const impStr = this.formatImporto(newImp);
          this.finanziamentoForm.get('impAmmesso').setValue(impStr);
        }
        // this.checkStatoFinanziamento();
      } else {
        this.finanziamentoForm.get('impAmmesso').reset();
      }
    });

    this.finanziamentoForm.get('percentuale').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
      if (value && this.finanziamentoForm.get('impFinanziabile').value) {
        let importo = this.finanziamentoForm.get('impFinanziabile').value.replace('.', '');
        importo = importo.replace(',', '.');
        const newImp = value.valorePercentuale * importo / 100;
        if ( Number.isNaN(newImp) ) {
          this.finanziamentoForm.get('impAmmesso').setValue(null);
        } else {
          const impStr = this.formatImporto(newImp);
          this.finanziamentoForm.get('impAmmesso').setValue(impStr);
        }
        // this.checkStatoFinanziamento();
      } else {
        this.finanziamentoForm.get('impAmmesso').reset();
      }
    });

    this.finanziamentoForm.get('parere').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
    });

    this.finanziamentoForm.get('attoApprovazione').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
    });

    this.finanziamentoForm.get('rendiconto').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
    });

    this.finanziamentoForm.get('importoRinuncia').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
    });

    this.finanziamentoForm.get('attoRinuncia').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( value => {
      this.disableButtons();
    });

    this.erogazioniForm.get('numDetAcconto').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( numDet => {
      this.enableUpdateErogazioni = false;
      this.determinaUrlAcconto = null;
      if (numDet && this.erogazioniForm.get('dataDetAcconto').value) {
        this.enableCercaDetAcconto = true;
      } else {
        this.enableCercaDetAcconto = false;
        this.enableUpdateErogazioni = false;
      }
    });

    this.erogazioniForm.get('dataDetAcconto').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( dataDet => {
      this.enableUpdateErogazioni = false;
      this.determinaUrlAcconto = null;
      if (dataDet && this.erogazioniForm.get('numDetAcconto').value) {
        this.enableCercaDetAcconto = true;
      } else {
        this.enableCercaDetAcconto = false;
        this.enableUpdateErogazioni = false;
      }
    });

    this.erogazioniForm.get('numDetSaldo').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( numDet => {
      this.enableUpdateErogazioni = false;
      this.determinaUrlSaldo = null;
      if (numDet && this.erogazioniForm.get('dataDetSaldo').value) {
        this.enableCercaDetSaldo = true;
      } else {
        this.enableCercaDetSaldo = false;
        this.enableUpdateErogazioni = false;
      }
    });

    this.erogazioniForm.get('dataDetSaldo').valueChanges.pipe(
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( dataDet => {
      this.enableUpdateErogazioni = false;
      this.determinaUrlSaldo = null;
      if (dataDet && this.erogazioniForm.get('numDetSaldo').value) {
        this.enableCercaDetSaldo = true;
      } else {
        this.enableCercaDetSaldo = false;
        this.enableUpdateErogazioni = false;
      }
    });
  }

  disableButtons() {
    this.enableValida = false;
    this.enableSospendi = false;
    this.enableRifiuta = false;
    this.enableRevoca = false;
    this.enableAnnulla = false;
    this.enableRipristina = false;
  }

  configTables() {
    this.provvedimentiTableColumns = {
      descLeggeProvvDr: {
        title: 'LEGGE - PROVVEDIMENTO - D.R.',
        sort: false,
        editable: false,
      }
    };

    this.provvedimentiTableSettings = {
      columns: this.provvedimentiTableColumns,
      mode: 'external',
      selectMode: 'single',
      hideSubHeader: true,
      actions: {
        columnTitle: 'AZIONI',
        position: 'right',
        add: false,
        edit: false,
        delete: false
      },
      pager: {
        display: true,
        perPage: 5
      }
    };

    this.praUrbTableColumns = {
      numProvvedimento: {
        title: 'NUM PROVVEDIMENTO',
        editable: false,
      },
      dataProvvedimento: {
        title: 'DATA PROVVEDIMENTO',
        editable: false,
      },
      descTipoProvvedimento: {
        title: 'TIPO PROVVEDIMENTO',
        editable: false,
      },
      esitorovvedimento: {
        title: 'ESITO PROVVEDIMENTO',
        editable: false,
      },
      numBUR: {
        title: 'NUM BUR',
        editable: false,
      },
      dataBUR: {
        title: 'DATA BUR',
        editable: false,
      },
    };

    this.praUrbTableSettings = {
      columns: this.praUrbTableColumns,
        mode: 'external',
        selectMode: 'single',
        hideSubHeader: true,
        actions: {
          columnTitle: 'AZIONI',
          position: 'right',
          add: false,
          delete: false,
        },
        edit: {
          editButtonContent: '<i class="fas fa-link" aria-hidden="true" title="Associa"></i>',
        },
        pager: {
          display: false
        }
    };
  }

  importoChangeEvent(event) {
    if (
      event.key === 'Backspace' || event.key === 'Delete' ||
      event.key === 'ArrowLeft' || event.key === 'ArrowRight' ||
      event.key === 'Tab' || event.key === 'Delete'
    ) {
      return;
    }

    const allowedChars = new RegExp('[\\d\\.\\,]');
    const check = allowedChars.test(event.key);
    if (!check) {
      event.preventDefault();
    }
  }

  formatImporto(value: number) {
    if (this.selectedFin.valuta.toUpperCase() === 'LIRE') {
      return value;
    }
    if (!value) {
      return '';
    }

    // let impStr = Math.round(value * 100) / 100 + '';
    // impStr = impStr.replace('.', ',');
    // const index = impStr.indexOf(',');
    // if (index === -1) {
    //   impStr = impStr + ',00';
    // } else if (index > -1 && index === impStr.length - 2) {
    //   impStr = impStr + '0';
    // }
    const impStr = formatNumber(value, this.locale, '1.2-2');
    return impStr;
  }

  reverseFormatImporto(value: string) {
    if (!value) {
      return null;
    }

    let impStr: string;
    impStr = value.replace('.', '');
    impStr = impStr.replace(',', '.');
    return Number(impStr);
  }

  updateForms() {
    const reverseFormatDate = (date: string): string => {
      const splitDate = !!date ? date.split('/') : undefined;
      return splitDate ? splitDate[2] + '-' + splitDate[1] + '-' + splitDate[0] : '';
    };

    if (this.selectedFin) {

      const importoMaxComune = this.selectedFin.valuta.toUpperCase() === 'EURO' ?
        this.formatImporto(this.importoTettoMax.importoTettoMax) : this.importoTettoMax.importoTettoMax;
      const importoMaxTotale = this.selectedFin.valuta.toUpperCase() === 'EURO' ?
        this.formatImporto(this.importoTettoMax.importoTettoMaxTotRichiedenti) : this.importoTettoMax.importoTettoMaxTotRichiedenti;
      this.finanziamentoForm.get('importoMaxComune').setValue(importoMaxComune);
      this.finanziamentoForm.get('importoMaxTotale').setValue(importoMaxTotale);

      if (this.selectedFin.statoFinanziamento) {
        this.finanziamentoForm.get('stato').setValue(this.selectedFin.statoFinanziamento.descrizione);
      } else {
        this.finanziamentoForm.get('stato').setValue('IN CORSO');
      }

      if (this.selectedFin.fkImportoTettoMax) {
        this.finanziamentoForm.get('tipoImporto').setValue('tettomax', {emitEvent: false});
        this.toggleRadioBtn = false;
        this.finanziamentoForm.get('impFinanziabile').disable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').disable({emitEvent: false});
      } else {
        this.finanziamentoForm.get('tipoImporto').setValue('percentuale', {emitEvent: false});
        this.toggleRadioBtn = true;
        this.finanziamentoForm.get('impFinanziabile').enable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').enable({emitEvent: false});
      }

      this.finanziamentoForm.get('note').setValue(this.selectedFin.note, {emitEvent: false});
      this.finanziamentoForm.get('impFinanziabile').setValue( this.formatImporto(this.selectedFin.importoFinanziabile), {emitEvent: false});
      this.finanziamentoForm.get('percentuale').setValue(this.selectedFin.percentuale, {emitEvent: false});
      this.finanziamentoForm.get('impAmmesso').setValue( this.formatImporto(this.selectedFin.importoAmmesso) );
      this.finanziamentoForm.get('parere').setValue(this.selectedFin.parere, {emitEvent: false});
      this.finanziamentoForm.get('attoApprovazione').setValue(this.selectedFin.attoApprovazione, {emitEvent: false});
      this.finanziamentoForm.get('rendiconto').setValue(this.selectedFin.rendiconto, {emitEvent: false});

      let numeroComuniAttivi = 0;
      let numRinunce = 0;
      this.richiedenti.forEach( richiedente => {
        const info = richiedente.provvedimentiToRichiedente.find(element => element.idLeggeProvvDr === this.selectedFin.idLeggeProvvDr);
        if (info.flagRinuncia) {
          numRinunce++;
        }
      });
      numeroComuniAttivi = this.richiedenti.length - numRinunce;

      if (numRinunce > 0) {
        this.rinunciaPresenteFlag = true;
        this.finanziamentoForm.get('importoRinuncia').enable({emitEvent: false});
        this.finanziamentoForm.get('attoRinuncia').enable({emitEvent: false});
        if (this.selectedFin.rinuncia) {
          this.finanziamentoForm.get('importoRinuncia').setValue( this.formatImporto(this.selectedFin.rinuncia.importo), {emitEvent: false});
          this.finanziamentoForm.get('attoRinuncia').setValue(this.selectedFin.rinuncia.attoRinuncia, {emitEvent: false});
        }
      } else {
        this.rinunciaPresenteFlag = false;
        this.finanziamentoForm.get('importoRinuncia').reset(null, {emitEvent: false});
        this.finanziamentoForm.get('attoRinuncia').reset(null, {emitEvent: false});
        this.finanziamentoForm.get('importoRinuncia').disable({emitEvent: false});
        this.finanziamentoForm.get('attoRinuncia').disable({emitEvent: false});
      }

      const erogazioni: Erogazione[] = this.selectedFin.erogazioni;

      if (erogazioni && erogazioni.length > 0) {
        const acconto = erogazioni.find(erogazione => erogazione.descTipoErogazione === 'ACCONTO');
        const saldo = erogazioni.find(erogazione => erogazione.descTipoErogazione === 'SALDO');

        if (acconto) {
          this.erogazioniForm.get('impAcconto').setValue( this.formatImporto(acconto.importoErogazione) );
          this.erogazioniForm.get('numDetAcconto').setValue(acconto.numDetermina);
          this.erogazioniForm.get('dataDetAcconto').setValue(reverseFormatDate(acconto.dataDetermina));
          this.calcolaImportoSaldo();
        }

        if (saldo) {
          this.erogazioniForm.get('impSaldo').setValue( this.formatImporto(saldo.importoErogazione) );
          this.erogazioniForm.get('numDetSaldo').setValue(saldo.numDetermina);
          this.erogazioniForm.get('dataDetSaldo').setValue(reverseFormatDate(saldo.dataDetermina));
        } else {
          this.erogazioniForm.get('numDetSaldo').setValue(null);
          this.erogazioniForm.get('dataDetSaldo').setValue(reverseFormatDate(null));
        }
      } else {
        this.erogazioniForm.reset();
        if (this.selectedFin.statoFinanziamento && this.selectedFin.statoFinanziamento.codStatoFinanziamento === 'VALID') {
          let importoBase;
          if (this.selectedFin.rinuncia) {
            importoBase = this.selectedFin.rinuncia.importo;
          } else {
            if (this.finanziamentoForm.get('tipoImporto').value === 'percentuale') {
              importoBase = this.reverseFormatImporto(this.finanziamentoForm.get('impAmmesso').value);
            } else {
              importoBase = this.importoTettoMax.importoTettoMaxTotRichiedenti;
            }
          }
          this.erogazioniForm.get('impAcconto').setValue( this.formatImporto(importoBase / 2) );
        }
      }

      if (this.codProfilo === 'GUEST') {
        this.finanziamentoForm.disable();
        this.erogazioniForm.disable();
        this.enableValida = false;
        this.enableSospendi = false;
        this.enableRifiuta = false;
        this.enableRevoca = false;
        this.enableAnnulla = false;
        this.enableRipristina = false;
        this.enableAttivaAcconto = false;
        this.enableAttivaSaldo = false;
      } else {
        this.checkStatoFinanziamento();
      }
    } else {
      this.finanziamentoForm.reset();
    }
  }

  checkStatoFinanziamento() {
    let codStatoFin = null;
    if (this.selectedFin.statoFinanziamento) {
      codStatoFin = this.selectedFin.statoFinanziamento.codStatoFinanziamento;
    }

    /* buttons */
    if (codStatoFin === 'INCORSO') {
      let flgDoc = false;
      let docCount = 0;
      let rinunciaCount = 0;
      this.richiedenti.forEach(richiedente => {
        const info = richiedente.provvedimentiToRichiedente.find(item => item.idLeggeProvvDr === this.selectedFin.idLeggeProvvDr);
        if (info.flagDocumentazione && !info.flagRinuncia) {
          docCount++;
        }
        if (info.flagRinuncia) {
          rinunciaCount++;
        }
      });
      flgDoc = docCount === this.richiedenti.length - rinunciaCount;

      if (flgDoc && this.selectedFin.importoAmmesso) {
        this.enableValida = true;
      } else {
        this.enableValida = false;
      }
    } else {
      this.enableValida = false;
    }

    this.enableSospendi = codStatoFin === 'INCORSO';
    this.enableRifiuta = codStatoFin === 'INCORSO' || codStatoFin === 'SOSP';
    this.enableRevoca = codStatoFin === 'ACCEROG' || codStatoFin === 'PERFSAL';
    this.enableAnnulla = codStatoFin === 'INCORSO' || codStatoFin === 'SOSP' || codStatoFin === 'PERFACC' || codStatoFin === 'VALID';
    this.enableRipristina = codStatoFin === 'SOSP' || codStatoFin === 'RESP' || codStatoFin === 'REVOC' || codStatoFin === 'ANNXRIN' || codStatoFin === 'VALID';

    this.enableAttivaAcconto = codStatoFin === 'VALID' && this.erogazioniForm.get('impAcconto').value;
    this.enableAttivaSaldo = codStatoFin === 'ACCEROG' && this.erogazioniForm.get('impSaldo').value;
    this.enableCercaDetAcconto = codStatoFin === 'PERFACC' && this.erogazioniForm.get('numDetAcconto').value && this.erogazioniForm.get('dataDetAcconto').value;
    this.enableCercaDetSaldo = codStatoFin === 'PERFSAL' && this.erogazioniForm.get('numDetSaldo').value && this.erogazioniForm.get('dataDetSaldo').value;

    /* forms */
    switch (codStatoFin) {

      case 'INCORSO': /*in corso*/
        this.finanziamentoForm.get('parere').enable({emitEvent: false});
        this.finanziamentoForm.get('attoApprovazione').enable({emitEvent: false});
        this.finanziamentoForm.get('rendiconto').enable({emitEvent: false});
        this.finanziamentoForm.get('impFinanziabile').enable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').enable({emitEvent: false});
        this.finanziamentoForm.get('tipoImporto').enable({emitEvent: false});
        this.erogazioniForm.disable();
        break;

      case 'VALID': /*validato*/
        this.finanziamentoForm.get('parere').enable({emitEvent: false});
        this.finanziamentoForm.get('attoApprovazione').enable({emitEvent: false});
        this.finanziamentoForm.get('rendiconto').enable({emitEvent: false});
        this.finanziamentoForm.get('impFinanziabile').disable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').disable({emitEvent: false});
        this.finanziamentoForm.get('tipoImporto').disable({emitEvent: false});
        this.erogazioniForm.disable();
        break;

      case 'SOSP': /*sospeso*/
        this.finanziamentoForm.disable({emitEvent: false});
        this.finanziamentoForm.get('note').enable({emitEvent: false});
        this.erogazioniForm.disable();
        break;

      case 'PERFACC': /*perfezionato per acconto*/
        this.finanziamentoForm.get('parere').enable({emitEvent: false});
        this.finanziamentoForm.get('attoApprovazione').enable({emitEvent: false});
        this.finanziamentoForm.get('rendiconto').enable({emitEvent: false});
        this.finanziamentoForm.get('tipoImporto').disable({emitEvent: false});
        this.finanziamentoForm.get('impFinanziabile').disable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').disable({emitEvent: false});
        this.erogazioniForm.get('numDetAcconto').enable();
        this.erogazioniForm.get('dataDetAcconto').enable();
        this.erogazioniForm.get('numDetSaldo').disable();
        this.erogazioniForm.get('dataDetSaldo').disable();
        break;

      case 'ACCEROG': /*acconto erogato*/
        this.finanziamentoForm.get('parere').enable({emitEvent: false});
        this.finanziamentoForm.get('attoApprovazione').enable({emitEvent: false});
        this.finanziamentoForm.get('rendiconto').enable({emitEvent: false});
        this.finanziamentoForm.get('tipoImporto').disable({emitEvent: false});
        this.finanziamentoForm.get('impFinanziabile').disable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').disable({emitEvent: false});
        this.erogazioniForm.disable();
        this.erogazioniForm.get('impSaldo').enable();
        break;

      case 'PERFSAL': /*perfezionato per saldo*/
        this.finanziamentoForm.get('parere').enable({emitEvent: false});
        this.finanziamentoForm.get('attoApprovazione').enable({emitEvent: false});
        this.finanziamentoForm.get('rendiconto').enable({emitEvent: false});
        this.finanziamentoForm.get('tipoImporto').disable({emitEvent: false});
        this.finanziamentoForm.get('impFinanziabile').disable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').disable({emitEvent: false});
        this.finanziamentoForm.get('importoRinuncia').disable({emitEvent: false});
        this.erogazioniForm.get('numDetAcconto').disable();
        this.erogazioniForm.get('dataDetAcconto').disable();
        this.erogazioniForm.get('impSaldo').disable();
        this.erogazioniForm.get('numDetSaldo').enable();
        this.erogazioniForm.get('dataDetSaldo').enable();
        break;

      case 'CONCL': /*concluso*/
        this.finanziamentoForm.get('parere').disable({emitEvent: false});
        this.finanziamentoForm.get('attoApprovazione').disable({emitEvent: false});
        this.finanziamentoForm.get('rendiconto').enable({emitEvent: false});
        this.finanziamentoForm.get('tipoImporto').disable({emitEvent: false});
        this.finanziamentoForm.get('impFinanziabile').disable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').disable({emitEvent: false});
        this.finanziamentoForm.get('importoRinuncia').disable({emitEvent: false});
        this.finanziamentoForm.get('attoRinuncia').disable({emitEvent: false});
        this.erogazioniForm.disable();
        break;

      case 'RESP': /*respinto*/
        // same as case 9

      case 'REVOC': /*revocato*/
        // same as case 9

      case 'ANNXRIN': /*annullato per rinuncia*/
        this.finanziamentoForm.disable({emitEvent: false});
        this.finanziamentoForm.get('note').enable({emitEvent: false});
        this.erogazioniForm.disable();
        break;

      default: /*null*/
        this.finanziamentoForm.get('parere').enable({emitEvent: false});
        this.finanziamentoForm.get('attoApprovazione').enable({emitEvent: false});
        this.finanziamentoForm.get('rendiconto').enable({emitEvent: false});
        this.finanziamentoForm.get('impFinanziabile').enable({emitEvent: false});
        this.finanziamentoForm.get('percentuale').enable({emitEvent: false});
        this.finanziamentoForm.get('tipoImporto').enable({emitEvent: false});
        this.erogazioniForm.disable();
        break;
    }

  }

  selectProvvedimento(selected: LeggeProvvDR[]) {
    if (!this.tableInitFlag) {
      this.tableInitFlag = true;
      return;
    }

    if (selected[0]) {
      const getFinanziamento = this.richiestaService.getFinanziamentoByLpd(this.idRichiesta, selected[0].idLeggeProvvDr);
      const getTettoMax = this.richiestaService.getTettoMaxRichiesta(this.idRichiesta, selected[0].idLeggeProvvDr);
      forkJoin([getFinanziamento, getTettoMax]).subscribe(
        res => {
          this.changeTab('finanziamento');
          this.importoTettoMax = res[1];
          if (res[0]) {
            this.selectedFin = res[0];
            /* MOCK */
            // this.selectedFin.praticaUrbGfu = {
            //   idPraticaUrb: 77777,
            //   numPraticaUrb: 'B90179',
            //   numAttoApprovazioneUrb: '20',
            //   dataAttoApprovazioneUrb: '11/08/2021',
            //   fkFinanziamento: this.selectedFin.idFinanziamento
            // };
            /* --- */
            if (this.selectedFin.praticaUrbGfu && this.selectedFin.praticaUrbGfu.numPraticaUrb) {
              this.getPraUrbDetails();
            } else {
              this.praUrbDetails = null;
            }
          } else {
            this.selectedFin = {
              idLeggeProvvDr: selected[0].idLeggeProvvDr,
              statoFinanziamento: null,
              valuta: 'EURO'
            };
          }
          this.updateForms();
        }, err => {
          this.selectedFin = null;
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.selectedFin = null;
      this.updateForms();
    }
  }

  changeTab(tab) {
    this.selectedTab = tab;
    // this.updateForms();
  }

  onValida() {
    if (this.selectedFin.importoAmmesso !== this.reverseFormatImporto(this.finanziamentoForm.get('impAmmesso').value) ) {
      this.messageService.showMessage('Info', 'Il valore corrente dell\'importo ammesso (' + this.finanziamentoForm.get('impAmmesso').value + ')' +
      'non corrisponde al valore salvato in precedenza (' + this.formatImporto(this.selectedFin.importoAmmesso) + '). Salvare il nuovo importo prima di procedere alla validazione del finanziamento.');
      return;
    }

    this.richiestaService.cambioStato(this.selectedFin.idFinanziamento, 10).subscribe(
      res => {
        this.selectedFin = res;
        this.finanziamentoForm.get('stato').setValue(this.selectedFin.statoFinanziamento.descrizione);
        this.messageService.showMessage('Info', 'Lo stato del finanziamento è stato aggiornato correttamente.');

        let importoBase;
        if (this.finanziamentoForm.get('tipoImporto').value === 'percentuale') {
          importoBase = this.reverseFormatImporto(this.finanziamentoForm.get('impAmmesso').value);
        } else {
          importoBase = this.importoTettoMax.importoTettoMaxTotRichiedenti;
        }
        this.erogazioniForm.get('impAcconto').setValue( this.formatImporto(importoBase / 2) );

        this.updateForms();
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onChangeStatoFinanziamento(idStato: number) {
    switch (idStato) {
      case 2:
        this.cambiaStato(2);
        break;

      case 3:
        let importoErogazione;
        // if (this.selectedFin.rinuncia && this.selectedFin.rinuncia.importo) {
        //   importoErogazione = this.selectedFin.rinuncia.importo / 2;
        // } else {
        //   importoErogazione = Number( this.reverseFormatImporto(this.erogazioniForm.get('impAcconto').value) );
        // }
        importoErogazione = Number( this.reverseFormatImporto(this.erogazioniForm.get('impAcconto').value) );

        const erogazioneAcconto: Erogazione = {
          idTipoErogazione: this.tipiErogazione[0].idTipoErogazione,
          descTipoErogazione: this.tipiErogazione[0].descrizione,
          idFinanziamento: this.selectedFin.idFinanziamento,
          importoErogazione,
          valuta: 'EURO',
          numDetermina: null,
          dataDetermina: null
        };

        this.richiestaService.saveErogazione(this.selectedFin.idFinanziamento, erogazioneAcconto).subscribe(
          res => {
            this.selectedFin = res;
            this.updateForms();
            this.messageService.showMessage('Info', 'Lo stato del finanziamento è stato aggiornato correttamente.');
          }, err => {
            this.selectedFin.statoFinanziamento = this.statiFinanziamento.find(stato => stato.codStatoFinanziamento === 'VALID');
            if (err.error && err.error.message) {
              this.messageService.showMessage('Errore', err.error.message);
            } else {
              this.messageService.showGenericError();
            }
          }
        );
        break;

      case 5:
        if (this.rinunciaPresenteFlag) {
          const rinuncia = this.selectedFin.rinuncia;
          if (!rinuncia) {
            this.messageService.showMessage('Info', 'Per poter erogare il saldo è necessario indicare l\'importo rinuncia nella sezione FINANZIAMENTO.');
            return;
          }
        }
        let importoStr = this.erogazioniForm.get('impSaldo').value.replace('.', '');
        importoStr = importoStr.replace(',', '.');
        const importo = importoStr * 1;
        if ( Number.isNaN(importo) ) {
          this.messageService.showMessage('Info', 'Il valore dell\'importo non ha un formato corretto.');
          return;
        }

        const erogazioneSaldo: Erogazione = {
          idTipoErogazione: this.tipiErogazione[1].idTipoErogazione,
          descTipoErogazione: this.tipiErogazione[1].descrizione,
          idFinanziamento: this.selectedFin.idFinanziamento,
          importoErogazione: importo,
          valuta: 'EURO',
          numDetermina: null,
          dataDetermina: null
        };

        this.richiestaService.saveErogazione(this.selectedFin.idFinanziamento, erogazioneSaldo).subscribe(
          res => {
            this.selectedFin = res;
            this.updateForms();

            this.messageService.showMessage('Info', 'Lo stato del finanziamento è stato aggiornato correttamente.');
          }, err => {
            this.selectedFin.statoFinanziamento = this.statiFinanziamento.find(stato => stato.codStatoFinanziamento === 'ACCEROG');
            if (err.error && err.error.message) {
              this.messageService.showMessage('Errore', err.error.message);
            } else {
              this.messageService.showGenericError();
            }
          }
        );
        break;

      case 7:
        this.cambiaStato(7);
        break;

      case 8:
        this.cambiaStato(8);
        break;

      case 9:
        this.cambiaStato(9);
        break;

      default:
        break;
    }
  }

  cambiaStato(idStato: number) {
    this.richiestaService.cambioStato(this.selectedFin.idFinanziamento, idStato).subscribe(
      res => {
        this.selectedFin = res;
        this.finanziamentoForm.get('stato').setValue(this.selectedFin.statoFinanziamento.descrizione);

        this.richiestaService.getRichiedentiFull(this.idRichiesta).subscribe(
          resp => {
            this.richiedenti = resp;
            this.updateForms();
            this.messageService.showMessage('Info', 'Lo stato del finanziamento è stato aggiornato correttamente.');
          }, error => {
            if (error.error && error.error.message) {
              this.messageService.showMessage('Errore', error.error.message);
            } else {
              this.messageService.showGenericError();
            }
          }
        );

      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  calcolaImportoSaldo() {
    let importoBase;
    if (this.selectedFin.rinuncia) {
      importoBase = this.selectedFin.rinuncia.importo;
    } else {
      if (this.finanziamentoForm.get('tipoImporto').value === 'percentuale') {
        importoBase = this.selectedFin.importoAmmesso;
      } else {
        importoBase = this.importoTettoMax.importoTettoMaxTotRichiedenti;
      }
    }
    this.erogazioniForm.get('impSaldo').setValue( this.formatImporto(importoBase - this.selectedFin.erogazioni.find(erogazione => erogazione.idTipoErogazione === 1).importoErogazione) );
  }

  onCercaDeterminaAcconto() {
    const numDet = Number(this.erogazioniForm.get('numDetAcconto').value);
    if ( isNaN(numDet) ) {
      this.messageService.showMessage('Errore', 'Il campo "Num. determina acconto" deve avere un valore numerico.');
      return;
    }

    const dataSplit = this.erogazioniForm.get('dataDetAcconto').value.split('-');
    const dataDet = dataSplit[2] + '/' + dataSplit[1] + '/' + dataSplit[0];

    const filters = {
      numDetermina: numDet,
      dataDetermina: dataDet,
    };

    this.richiestaService.searchDetermina(filters).subscribe(
      res => {
        if (!res || !res.numDetermina) {
          this.messageService.showMessage('Errore', 'La determina non è stata trovata.');
          this.determinaUrlAcconto = null;
          this.enableUpdateErogazioni = false;
          return;
        }

        this.determinaUrlAcconto = res.url;
        this.enableUpdateErogazioni = true;
      },
      err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
          if (err.error.code === '49') {
            this.enableUpdateErogazioni = true;
          }
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onCercaDeterminaSaldo() {
    const numDet = Number(this.erogazioniForm.get('numDetSaldo').value);
    if ( isNaN(numDet) ) {
      this.messageService.showMessage('Errore', 'Il campo "Num. determina saldo" deve avere un valore numerico.');
      return;
    }

    const dataSplit = this.erogazioniForm.get('dataDetSaldo').value.split('-');
    const dataDet = dataSplit[2] + '/' + dataSplit[1] + '/' + dataSplit[0];

    const filters = {
      numDetermina: numDet,
      dataDetermina: dataDet,
    };

    this.richiestaService.searchDetermina(filters).subscribe(
      res => {
        if (!res || !res.numDetermina) {
          this.messageService.showMessage('Errore', 'La determina non è stata trovata.');
          this.determinaUrlSaldo = null;
          this.enableUpdateErogazioni = false;
          return;
        }

        this.determinaUrlSaldo = res.url;
        this.enableUpdateErogazioni = true;
      },
      err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
          if (err.error.code === '49') {
            this.enableUpdateErogazioni = true;
          }
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onConsultaAllegato(tipo: string) {
    const determinaUrl = tipo === 'acconto' ? this.determinaUrlAcconto : this.determinaUrlSaldo;
    window.open(determinaUrl, '_blank');
  }

  onSalva() {

    const impFinanziabile = this.reverseFormatImporto(this.finanziamentoForm.get('impFinanziabile').value) * 1;
    if ( Number.isNaN(impFinanziabile) ) {
      this.messageService.showMessage('Info', 'Il valore dell\'importo non ha un formato corretto.');
      return;
    }

    this.selectedFin.note = this.finanziamentoForm.get('note').value;
    this.selectedFin.importoFinanziabile = impFinanziabile;
    this.selectedFin.percentuale = this.finanziamentoForm.get('percentuale').value;
    this.selectedFin.importoAmmesso = this.reverseFormatImporto(this.finanziamentoForm.get('impAmmesso').value);
    this.selectedFin.parere = this.finanziamentoForm.get('parere').value;
    this.selectedFin.attoApprovazione = this.finanziamentoForm.get('attoApprovazione').value;
    this.selectedFin.rendiconto = this.finanziamentoForm.get('rendiconto').value;

    if (this.finanziamentoForm.get('tipoImporto').value === 'tettomax') {
      this.selectedFin.fkImportoTettoMax = this.importoTettoMax.idTettoMax;
    } else {
      // needed to handle Salva before VALIDA:
      this.selectedFin.fkImportoTettoMax = null;
    }

    if (this.selectedFin.rinuncia && this.selectedFin.rinuncia.idFinanziamentoRinuncia) {

      const impRinuncia = this.reverseFormatImporto(this.finanziamentoForm.get('importoRinuncia').value) * 1;
      if ( Number.isNaN(impRinuncia) ) {
        this.messageService.showMessage('Info', 'Il valore dell\'importo della rinuncia non ha un formato corretto.');
        return;
      }
      this.selectedFin.rinuncia.importo = impRinuncia;
      this.selectedFin.rinuncia.attoRinuncia = this.finanziamentoForm.get('attoRinuncia').value;

    } else if (this.finanziamentoForm.get('importoRinuncia').value || this.finanziamentoForm.get('attoRinuncia').value) {

      const impRinuncia = this.reverseFormatImporto(this.finanziamentoForm.get('importoRinuncia').value) * 1;
      if ( Number.isNaN(impRinuncia) ) {
        this.messageService.showMessage('Info', 'Il valore dell\'importo della rinuncia non ha un formato corretto.');
        return;
      }
      this.selectedFin.rinuncia = {
        idFinanziamentoRinuncia: null,
        importo: impRinuncia,
        valuta: 'EURO',
        attoRinuncia: this.finanziamentoForm.get('attoRinuncia').value
      };
    }

    if (!this.selectedFin.idFinanziamento) {
      this.richiestaService.saveFinanziamento(this.idRichiesta, this.selectedFin, this.selectedFin.idLeggeProvvDr).subscribe(
        res => {
          this.selectedFin = res;
          this.updateForms();
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.richiestaService.saveFinanziamento(this.idRichiesta, this.selectedFin).subscribe(
        res => {
          // cant save response (missing erogazioni)
          // this.selectedFin = res;
          this.updateForms();
          this.messageService.showMessage('Info', 'Il finanziamento è stato aggiornato correttamente.');
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    }
  }

  updateErogazioni() {
    const formatDate = (date: string): string => {
      const splitDate = date.split('-');
      return splitDate[2] + '/' + splitDate[1] + '/' + splitDate[0];
    };

    if (this.selectedFin.statoFinanziamento.codStatoFinanziamento === 'PERFACC') {
      const numDet = this.erogazioniForm.get('numDetAcconto').value;
      const dataDet = this.erogazioniForm.get('dataDetAcconto').value;
      if (numDet && dataDet) {
        const erogazioneAcconto = this.selectedFin.erogazioni.find(erogazione => erogazione.idTipoErogazione === 1);
        erogazioneAcconto.numDetermina = numDet;
        erogazioneAcconto.dataDetermina = formatDate(dataDet);

        this.richiestaService.updateErogazione(this.selectedFin.idFinanziamento, erogazioneAcconto).subscribe(
          res => {
            this.selectedFin = res;
            this.enableUpdateErogazioni = false;
            this.updateForms();
            this.messageService.showMessage('Info', 'Lo stato del finanziamento è stato aggiornato correttamente.');
          }, err => {
            if (err.error && err.error.message) {
              this.messageService.showMessage('Errore', err.error.message);
              if (err.error.code === '41') {
                this.erogazioniForm.get('numDetAcconto').reset();
                this.erogazioniForm.get('dataDetAcconto').reset();
              }
            } else {
              this.messageService.showGenericError();
            }
          }
        );
      } else {
        this.messageService.showMessage('Errore', 'Inserisci i dati relativi alla determina.');
      }
    } else if (this.selectedFin.statoFinanziamento.codStatoFinanziamento === 'PERFSAL') {
      const numDet = this.erogazioniForm.get('numDetSaldo').value;
      const dataDet = this.erogazioniForm.get('dataDetSaldo').value;
      if (numDet && dataDet) {
        const erogazioneSaldo = this.selectedFin.erogazioni.find(erogazione => erogazione.idTipoErogazione === 2);
        erogazioneSaldo.numDetermina = numDet;
        erogazioneSaldo.dataDetermina = formatDate(dataDet);

        this.richiestaService.updateErogazione(this.selectedFin.idFinanziamento, erogazioneSaldo).subscribe(
          res => {
            this.selectedFin = res;
            this.enableUpdateErogazioni = false;
            this.updateForms();
            this.messageService.showMessage('Info', 'Lo stato del finanziamento è stato aggiornato correttamente.');
          }, err => {
            if (err.error && err.error.message) {
              this.messageService.showMessage('Errore', err.error.message);
            } else {
              this.messageService.showGenericError();
            }
          }
        );
      } else {
        this.messageService.showMessage('Errore', 'Inserisci i dati relativi alla determina.');
      }
    }
  }

  searchPraUrb() {
    if (!this.searchPraUrbForm.valid) {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti.');
      return;
    }

    /* MOCK */
    // this.searchPraUrbResult = [
    //   {
    //     numPratica: 'B90179',
    //     numProvvedimento: '20',
    //     dataProvvedimento: '17/03/2021',
    //     descTipoProvvedimento: 'APPROVAZIONE',
    //     esitoProvvedimento: 'FAVOREVOLE',
    //     numBUR: '13',
    //     dataBUR: '01/04/2021'
    //   },
    //   {
    //     numPratica: 'B90179',
    //     numProvvedimento: '89',
    //     dataProvvedimento: '19/12/2021',
    //     descTipoProvvedimento: 'ADOZIONE',
    //     esitoProvvedimento: 'FAVOREVOLE',
    //     numBUR: '5',
    //     dataBUR: '01/04/2021'
    //   },
    //   {
    //     numPratica: 'A10123',
    //     numProvvedimento: '10',
    //     dataProvvedimento: '17/03/2021',
    //     descTipoProvvedimento: 'ADOZIONE',
    //     esitoProvvedimento: 'FAVOREVOLE',
    //     numBUR: '11',
    //     dataBUR: '01/04/2021'
    //   },
    // ];
    // this.praUrbTableSource.load(this.searchPraUrbResult);
    // return;
    /* --- */

    let dataFrom = null;
    let dataTo = null;

    if (this.searchPraUrbForm.get('dataPraUrbDa').value) {
      const dataFromSplit = this.searchPraUrbForm.get('dataPraUrbDa').value.split('-');
      dataFrom = dataFromSplit[2] + '/' + dataFromSplit[1] + '/' + dataFromSplit[0];
    }
    if (this.searchPraUrbForm.get('dataPraUrbA').value) {
      const dataToSplit = this.searchPraUrbForm.get('dataPraUrbA').value.split('-');
      dataTo = dataToSplit[2] + '/' + dataToSplit[1] + '/' + dataToSplit[0];
    }

    const filters = {
      istatComune: this.searchPraUrbForm.get('comune').value.istatComune || null,
      dataProvvedimentoDa: dataFrom,
      dataProvvedimentoA: dataTo
    };

    this.richiestaService.searchPraUrbByFilters(filters).subscribe(
      res => {
        if (res && res.length > 0) {
          this.searchPraUrbResult = res;
          this.praUrbTableSource.load(this.searchPraUrbResult);
          this.praUrbTableSource.setSort([{ field: 'numProvvedimento', direction: 'asc' }]);
        } else {
          this.searchPraUrbResult = null;
          this.messageService.showMessage('Attenzione', 'La ricerca non ha prodotto alcun risultato.');
        }
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  getPraUrbDetails() {
    this.richiestaService.getPraUrbDetails(this.selectedFin.idFinanziamento, this.selectedFin.praticaUrbGfu.numPraticaUrb).subscribe(
      res => {
        this.praUrbDetails = res;
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
    /* MOCK */
    // this.praUrbDetails = {
    //   numPratica: 'B90179',
    //   numProtocollo: '00001889',
    //   dataIngressoRegione: '08/08/2021',
    //   descTipoPratica: 'VARIANTE AL PRG',
    //   oggettoPratica: 'Variante n. 16',
    //   descStatoPratica: 'IN CORSO',
    //   elencoComunePv: 'COLLEGNO',
    //   provvedimentiUrb: [
    //     {
    //       numProvvedimento: '20',
    //       dataProvvedimento: '08/08/2021',
    //       descTipoProvvedimento: 'APPROVAZIONE',
    //       esitoProvvedimento: 'FAVOREVOLE',
    //       numBUR: '13',
    //       dataBUR: '09/08/2021'
    //     },
    //     {
    //       numProvvedimento: '89',
    //       dataProvvedimento: '08/08/2021',
    //       descTipoProvvedimento: 'ADOZIONE',
    //       esitoProvvedimento: 'FAVOREVOLE',
    //       numBUR: '5',
    //       dataBUR: '09/08/2021'
    //     }
    //   ],
    //   conferenzeUrb: [
    //     {
    //       descTipoConferenza: 'Conferenza per Progetto Preliminare',
    //       descTipoPassoIter: 'Seduta finale',
    //       dataPassoIter: '05/08/2021',
    //       numProtocolloPassoIter: null
    //     }
    //   ],
    //   adeguamentoUrb: [
    //     { descTipoPianoSovr: 'PTO' },
    //     { descTipoPianoSovr: 'PAI' },
    //     { descTipoPianoSovr: 'PROVA' }
    //   ],
    // };
    /* - */
  }

  clearPraUrbForm() {
    this.searchPraUrbForm.reset();
    this.searchPraUrbResult = null;
  }

  aggiungiPraUrb(event) {
    const nuovaPraUrbGfu: PraticaUrbGfu = {
      numPraticaUrb: event.data.numPratica,
      numAttoApprovazioneUrb: event.data.numProvvedimento,
      dataAttoApprovazioneUrb: event.data.dataProvvedimento,
      fkFinanziamento: this.selectedFin.idFinanziamento
    };

    this.richiestaService.postPraUrb(this.selectedFin.idFinanziamento, nuovaPraUrbGfu).subscribe(
      res => {
        this.selectedFin = res;
        this.getPraUrbDetails();
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  onEliminaPraUrb() {
    const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler eliminare l\'associazione tra il finanziamento e la Pratica Urbanistica?');
    modalRef.result.then(confirm => {
      if (confirm) {
        this.deletePraUrb();
      }
    });
  }

  deletePraUrb() {
    this.richiestaService.deletePraUrb(this.selectedFin.idFinanziamento).subscribe(
      res => {
        this.selectedFin = res;
        this.praUrbDetails = null;
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );
  }

  buildAdeguamentiString(adeguamenti: AdeguamentoUrb[]) {
    let concatString = '';
    adeguamenti.forEach( (item, index) => {
      if (index === adeguamenti.length - 1) {
        concatString += item.descTipoPianoSovr;
      } else {
        concatString += item.descTipoPianoSovr + ' - ';
      }
    });
    return concatString;
  }

  comparePercentuali(p1: Percentuale, p2: Percentuale) {
    return p1 && p2 && p1.idPercentuale === p2.idPercentuale;
  }

  comparePareri(p1: Parere, p2: Parere) {
    return p1 && p2 && p1.idParere === p2.idParere;
  }

  compareRendiconti(r1: Rendiconto, r2: Rendiconto) {
    return r1 && r2 && r1.idRendiconto === r2.idRendiconto;
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
