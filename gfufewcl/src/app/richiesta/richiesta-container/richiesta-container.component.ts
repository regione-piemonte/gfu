import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin } from 'rxjs';

import { APP_SETTINGS } from 'src/app/shared/settings/app-settings';
import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { RichiestaService } from 'src/app/shared/services/richiesta.service';
import { Finanziamento, LeggeProvvDR, Parere, Percentuale, Rendiconto, Richiedente, Richiesta, TipoErogazione } from 'src/app/shared/model';
import { ProvvedimentiComponent } from './provvedimenti/provvedimenti.component';

@Component({
  selector: 'app-richiesta-container',
  templateUrl: './richiesta-container.component.html',
  styleUrls: ['./richiesta-container.component.css']
})
export class RichiestaContainerComponent implements OnInit {

  @ViewChild('appProvvedimenti') appProvvedimenti: ProvvedimentiComponent;

  codProfilo: string;

  richiesta: Richiesta;
  idRichiestaFromPath: string;
  editMode = false;

  richiedentiList: Richiedente[];
  provvedimentiList: LeggeProvvDR[];
  // finanziamentiList: Finanziamento[];

  tipiErogazione: TipoErogazione[] = [
    {
      idTipoErogazione: 1,
      descrizione: 'ACCONTO',
      codTipoErogazione: 'ACC'
    },
    {
      idTipoErogazione: 2,
      descrizione: 'SALDO',
      codTipoErogazione: 'SAL'
    },
  ];

  selectedTab = 'R';

  generalDataForm: FormGroup;

  enableRichiedentiTab = false;
  enableProvvTab = false;
  enableFinanzTab = false;

  percentuali: Percentuale[];
  pareri: Parere[];
  rendiconti: Rendiconto[];

  constructor(
    private activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private messageService: MessageService,
    private richiestaService: RichiestaService,
    private anagraficaService: AnagraficaService,
  ) {
    this.idRichiestaFromPath = this.activatedRoute.snapshot.paramMap.get('id');
    this.codProfilo = APP_SETTINGS.codProfilo;
  }

  ngOnInit() {
    this.generalDataForm = this.fb.group({
      numProtocollo: [null, Validators.required],
      dataRichiesta: [null, Validators.required],
      richiedente: null,
      note: null
    });

    if (this.idRichiestaFromPath) {
      this.richiestaService.getRichiestaById(Number(this.idRichiestaFromPath)).subscribe(
        res => {
          this.richiesta = res;
          this.editMode = true;
          this.changeTab('R');
          this.loadData();
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

  loadData() {
    const dataSplit = this.richiesta.dataProtocollo.split('/');
    const formattedDate = dataSplit[2] + '-' + dataSplit[1] + '-' + dataSplit[0];
    this.generalDataForm.get('numProtocollo').setValue(this.richiesta.numProtocollo);
    this.generalDataForm.get('dataRichiesta').setValue(formattedDate);
    this.generalDataForm.get('note').setValue(this.richiesta.note);
    this.enableRichiedentiTab = true;
  }

  changeTab(tab) {
    this.selectedTab = tab;
    if (this.selectedTab === 'R' && this.codProfilo !== 'GUEST') {
      this.generalDataForm.enable();
    } else {
      this.generalDataForm.disable();
    }

    if (this.selectedTab === 'F') {
      const getPercentuali = this.anagraficaService.getPercentuali(true);
      const getPareri = this.anagraficaService.getPareri(true);
      const getRendiconti = this.anagraficaService.getRendiconti(true);

      forkJoin([getPercentuali, getPareri, getRendiconti]).subscribe(
        res => {
          this.percentuali = res[0];
          this.pareri = res[1];
          this.rendiconti = res[2];
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else if (this.selectedTab === 'P') {
      this.appProvvedimenti.loadData();
    }

  }

  salvaDatiGenerali() {
    if (this.generalDataForm.valid) {
      let data;
      const dataSplit = this.generalDataForm.get('dataRichiesta').value.split('-');
      data = dataSplit[2] + '/' + dataSplit[1] + '/' + dataSplit[0];

      const nuovaRichiesta: Richiesta = {
        numProtocollo: this.generalDataForm.get('numProtocollo').value,
        dataProtocollo: data,
        note: this.generalDataForm.get('note').value
      };
      if (this.idRichiestaFromPath || (!!this.richiesta && !!this.richiesta.idRichiesta)) {
        nuovaRichiesta.idRichiesta = this.richiesta.idRichiesta;
      }
      this.richiestaService.saveRichiesta(nuovaRichiesta).subscribe(
        res => {
          this.richiesta = res;
          this.enableRichiedentiTab = true;
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.messageService.showMessage('Errore', 'Dati obbligatori mancanti.');
    }
  }

  setRichiedenti(data) {
    this.richiedentiList = null;
    if (!data || data.richiedentiList.length === 0) {
      this.generalDataForm.get('richiedente').reset();
      if (this.richiesta.idAssociazione) {
        this.richiesta.idAssociazione = null;
      }
      this.richiedentiList = null;
      this.enableProvvTab = false;
      this.enableFinanzTab = false;
    } else {
      if (data.richiedentiList.length === 1 && !this.richiesta.idAssociazione) {
        this.generalDataForm.get('richiedente').setValue(data.richiedentiList[0].descComune);
      } else {
        this.generalDataForm.get('richiedente').setValue(data.associazione.descAssociazione.toUpperCase());
      }
      // this.richiedentiList = null;
      setTimeout(() => {
        this.richiedentiList = data.richiedentiList;
      }, 100);
      this.enableProvvTab = true;
    }
  }

  onProvvedimentiSaved(richiedenti: Richiedente[]) {
    this.richiedentiList = richiedenti;

    if (!this.richiedentiList[0].provvedimentiToRichiedente || this.richiedentiList[0].provvedimentiToRichiedente.length === 0) {
      this.provvedimentiList = [];
      this.enableFinanzTab = false;
      return;
    }

    const lpdList = [];
    const lpdRequests = [];
    this.richiedentiList[0].provvedimentiToRichiedente.forEach( element => {
      lpdRequests.push(
        this.anagraficaService.getLpdById(element.idLeggeProvvDr)
      );
    });

    forkJoin(lpdRequests).subscribe(
      resp => {
        resp.forEach( (lpd: LeggeProvvDR, index) => {
          lpdList.push(lpd);
          if (index === resp.length - 1) {
            this.provvedimentiList = null;
            this.provvedimentiList = lpdList;
          }
        });

        this.enableFinanzTab = true;
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
