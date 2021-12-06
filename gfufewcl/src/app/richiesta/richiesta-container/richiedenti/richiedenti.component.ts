import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subject } from 'rxjs';
import { debounceTime, distinctUntilChanged, map, switchMap, takeUntil, tap } from 'rxjs/operators';
import { Ng2SmartTableComponent, LocalDataSource } from 'ng2-smart-table';

import { MessageService } from 'src/app/shared/services/message.service';
import { AnagraficaService } from 'src/app/shared/services/anagrafica.service';
import { RichiestaService } from 'src/app/shared/services/richiesta.service';
import { Comune, Richiedente, AssociazioneComuni, TipoFormaAssociativa, Richiesta } from 'src/app/shared/model';

@Component({
  selector: 'app-richiedenti',
  templateUrl: './richiedenti.component.html',
  styleUrls: ['./richiedenti.component.css']
})
export class RichiedentiComponent implements OnInit, OnDestroy {

  @Input() richiesta: Richiesta;
  @Input() codProfilo: string;
  @Output() richiedentiOut = new EventEmitter();

  @ViewChild('associazioneTable') associazioneTable: Ng2SmartTableComponent;

  private unsubscribe$ = new Subject();
  private DELAY = 300;

  // showAssociazioni = false;

  selectedTab = 'CS'; // CS (Comune Singolo), AR (Associazione Registrata), AN (Ass. Nuova)
  enableTabCS = true;
  enableTabAR = true;
  enableTabAN = true;

  comuniLoading = false;
  associazioniLoading = false;

  tipiFormaAssociativa: TipoFormaAssociativa[] = [];
  comuniSearchList: Comune[];
  associazioniSearchList: AssociazioneComuni[];

  associazioneSelected: AssociazioneComuni;
  comuniFromAssociazione: Comune[];

  ricercaForm: FormGroup;
  formaAssociativaForm: FormGroup;
  // showFormaAssociativa = false;
  enableSalvaFA = true;

  comuniRichiedenti: Richiedente[] = [];
  richiedentiTableSource = new LocalDataSource();
  associazioneTableSource = new LocalDataSource();

  richiedentiTableColumns;
  richiedentiTableSettings;
  associazioneTableColumns;
  associazioneTableSettings;

  searchComune = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(this.DELAY),
      distinctUntilChanged(),
      tap(() => this.comuniLoading = true),
      switchMap( input => {
        return this.richiestaService.searchComuni(input)
        .pipe(
          tap(list => this.comuniSearchList = list),
          map(comuni => {
            const comuniString = [];
            if (comuni) {
              comuni.forEach( comune => comuniString.push(comune.descComune) );
            }
            return comuniString.slice(0, 10);
          })
        );
      }),
      tap(() => this.comuniLoading = false),
    )

  searchAssociazione = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(this.DELAY),
      distinctUntilChanged(),
      tap(() => this.associazioniLoading = true),
      switchMap( input => {
        return this.richiestaService.searchAssociazioni(input)
        .pipe(
          tap(list => this.associazioniSearchList = list && list.length > 0 ? list : null ),
          map(associazioni => {
            const associazioniString = [];
            if (associazioni) {
              associazioni.forEach( associazione => associazioniString.push(associazione.descAssociazione) );
            }
            return associazioniString.slice(0, 10);
          })
        );
      }),
      tap(() => this.associazioniLoading = false),
    )

  constructor(
    private messageService: MessageService,
    private richiestaService: RichiestaService,
    private anagraficaService: AnagraficaService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.buildForms();
    this.configTables();
    // this.changeTab('CS');

    this.anagraficaService.getTipiFormaAssociativa().subscribe(
      res => {
        this.tipiFormaAssociativa = res;
      }, err => {
        if (err.error && err.error.message) {
          this.messageService.showMessage('Errore', err.error.message);
        } else {
          this.messageService.showGenericError();
        }
      }
    );

    this.richiestaService.getRichiedentiFull(this.richiesta.idRichiesta).subscribe(
      res => {
        if (res && res.length > 0) {
          this.comuniRichiedenti = res;
          this.loadData();
        } else {
          this.richiestaService.getRichiedentiLight(this.richiesta.idRichiesta).subscribe(
            lightRes => {
              if ( lightRes && lightRes.length > 0 ) {
                this.comuniRichiedenti = lightRes;
                this.loadData();
              }
            },
            lightErr => {
              if (lightErr.error && lightErr.error.message) {
                this.messageService.showMessage('Errore', lightErr.error.message);
              } else {
                this.messageService.showGenericError();
              }
            }
          );
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

  loadData() {
    this.enableTabAN = false;
    this.richiedentiTableSource.load(this.comuniRichiedenti);
    this.richiedentiTableSource.setSort([{ field: 'descComune', direction: 'asc' }]);

    if (this.comuniRichiedenti.length === 1 && !this.richiesta.idAssociazione) {
      this.enableTabAR = false;

      this.richiedentiOut.emit({
        richiedentiList: this.comuniRichiedenti
      });
    } else {
      this.changeTab('AR');
      this.enableTabCS = false;

      this.richiestaService.getAssociazioneById(this.richiesta.idAssociazione).subscribe(
        res => {
          // nested setTimout is not an elegant and clean approach, but couldn't find a better one
          setTimeout( () => {
            this.associazioneSelected = res;
            this.ricercaForm.get('associazione').disable();
            this.ricercaForm.get('associazione').setValue(this.associazioneSelected.descAssociazione.toUpperCase(), {emitEvent: false});
            this.ricercaForm.get('tipoAssRegistrata').setValue(this.associazioneSelected.descTipoFormaAssociativa);
            this.associazioneTableSource.load(this.associazioneSelected.comuni);

            setTimeout(() => {
              this.associazioneTable.grid.getRows().forEach(row => {
                if ( this.comuniRichiedenti.some(comune => comune.istatComune === row['data'].istatComune) ) {
                  row.isSelected = true;
                }
              });

              setTimeout(() => {
                this.setCheckBoxes();
              }, 0);
            }, 0);

            this.richiedentiOut.emit({
              richiedentiList: this.comuniRichiedenti,
              associazione: this.associazioneSelected
            });
          }, this.DELAY);
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    }

    if (this.codProfilo === 'GUEST') {
      this.ricercaForm.disable();
    }
  }

  buildForms() {
    this.ricercaForm = this.fb.group({
      // tipo: 'C',
      comune: null,
      associazione: null,
      tipoAssRegistrata: [{value: null, disabled: true}],
    });
    this.formaAssociativaForm = this.fb.group({
      tipo: null,
      denom: null
    });

    // this.ricercaForm.get('tipo').valueChanges.pipe(
    //   takeUntil(this.unsubscribe$)
    // ).subscribe( tipo => {
    //   this.showAssociazioni = tipo === 'U' ? true : false;
    // });

    this.ricercaForm.get('associazione').valueChanges.pipe(
      debounceTime(this.DELAY),
      distinctUntilChanged(),
      takeUntil(this.unsubscribe$)
    ).subscribe( desc => {
      if (this.associazioniSearchList && desc && desc.length > 2) {
        this.associazioneSelected = this.associazioniSearchList.find(uni => uni.descAssociazione === desc);
        if (this.associazioneSelected) {
          this.ricercaForm.get('tipoAssRegistrata').setValue(this.associazioneSelected.descTipoFormaAssociativa);
          this.associazioneTableSource.load(this.associazioneSelected.comuni);
        } else {
          this.ricercaForm.get('tipoAssRegistrata').setValue(null);
        }
      } else {
        this.associazioneSelected = null;
        this.ricercaForm.get('tipoAssRegistrata').setValue(null);
        this.associazioneTableSource.refresh();
      }
    });
  }

  configTables() {
    this.richiedentiTableColumns = {
      descComune: {
        title: 'Comune',
        editable: false
      },
      siglaProvincia: {
        title: 'Provincia',
        editable: false
      }
    };

    this.richiedentiTableSettings = {
      columns: this.richiedentiTableColumns,
      mode: 'external',
      hideSubHeader: true,
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
        add: false,
        edit: false,
        delete: this.codProfilo !== 'GUEST',
      },
      delete: {
        deleteButtonContent: '<i class="fa fa-trash" aria-hidden="true" title="elimina"></i>'
      },
      pager: {
        display: false
      }
    };

    this.associazioneTableColumns = {
      descComune: {
        title: 'Comune',
        editable: false
      },
      siglaProvincia: {
        title: 'Provincia',
        editable: false
      }
    };

    this.associazioneTableSettings = {
      columns: this.associazioneTableColumns,
      hideSubHeader: true,
      mode: 'external',
      selectMode: 'multi',
      actions: {
        columnTitle: 'Azioni',
        position: 'right',
        add: false,
        edit: false,
        delete: false
      },
      // pager: {
      //   display: true,
      //   perPage: 5
      // }
      pager: {
        display: false
      }
    };
  }

  changeTab(tab, keepAssociazione = false) {
    if ( !!tab && (tab !== this.selectedTab) ) {
      this.selectedTab = tab;
      if (!keepAssociazione) {
        this.associazioneSelected = null;
        this.ricercaForm.get('associazione').reset();
        // this.ricercaForm.get('associazione').setValue(null);
        // this.ricercaForm.get('tipo').setValue('C');
        this.ricercaForm.enable();
        this.ricercaForm.get('tipoAssRegistrata').setValue(null);
        this.ricercaForm.get('tipoAssRegistrata').disable();
      }
      if (this.selectedTab !== 'AN') {
        this.formaAssociativaForm.clearValidators();
        this.formaAssociativaForm.reset();
        // this.showFormaAssociativa = false;
      } else {
        this.resetNuovaAssociazione();
      }
    }
  }

  resetNuovaAssociazione() {
    this.formaAssociativaForm.reset();
    this.formaAssociativaForm.get('tipo').setValidators(Validators.required);
    this.formaAssociativaForm.get('denom').setValidators(Validators.required);
    this.formaAssociativaForm.enable();
    this.enableSalvaFA = true;
    // this.showFormaAssociativa = true;
  }

  onAggiungi(fromAssocciazione = false) {
    if (this.selectedTab === 'CS') {
      this.aggiungiComuneSingolo();
    } else if (this.selectedTab === 'AR') {
      this.aggiungiAssociazione();
    } else {
      if (!fromAssocciazione) {
        const denom = this.ricercaForm.get('comune').value;
        if (this.comuniSearchList && this.comuniSearchList.length > 0) {
          const comune = this.comuniSearchList.find(com => com.descComune === denom.toUpperCase());
          if (comune) {
            const check = this.comuniRichiedenti.some(com => com.istatComune === comune.istatComune);
            if (!check) {
              this.comuniRichiedenti.push(comune as Richiedente);
              this.richiedentiTableSource.load(this.comuniRichiedenti);
              this.richiedentiTableSource.setSort([{ field: 'descComune', direction: 'asc' }]);
            } else {
              this.messageService.showMessage('Errore', 'Il comune selezionato è già presente nella lista dei richiedenti.');
            }
          } else {
            this.messageService.showMessage('Errore', 'Selezionare un comune valido.');
          }
        } else {
          this.messageService.showMessage('Errore', 'Selezionare un comune valido.');
        }
      } else {
        if (this.comuniFromAssociazione && this.comuniFromAssociazione.length > 0) {

          let duplicateFlag = false;
          this.comuniFromAssociazione.forEach( element => {
            const check = this.comuniRichiedenti.some(com => com.istatComune === element.istatComune);
            if (check) {
              duplicateFlag = true;
            }
          });

          if (duplicateFlag) {
            this.messageService.showMessage('Errore', 'Uno o più comuni selezionati sono già presenti nella lista dei richiedenti.');
            return;
          }

          this.comuniFromAssociazione.forEach(comune => this.comuniRichiedenti.push(comune as Richiedente));
          this.richiedentiTableSource.load(this.comuniRichiedenti);

        } else {
          this.messageService.showMessage('Errore', 'Selezionare almeno un comune dalla lista.');
        }
      }
      this.enableTabCS = false;
      this.enableTabAR = false;
    }

    this.ricercaForm.get('comune').reset();
    // if ( this.selectedTab !== 'AR' ) {
    if ( this.selectedTab === 'CS' ) {
      this.ricercaForm.get('associazione').reset();
    }
    this.comuniSearchList = null;
    this.associazioniSearchList = null;
  }

  aggiungiComuneSingolo() {
    if (this.comuniRichiedenti.length > 0) {
      this.messageService.showMessage('Errore', 'Il richiedente è già stato selezionato. Eliminarlo per aggiungerne uno diverso o creare una nuova forma associativa dall\'apposito tab.');
      return;
    }

    const denom = this.ricercaForm.get('comune').value;
    if (this.comuniSearchList && this.comuniSearchList.length > 0) {
      const comune = this.comuniSearchList.find(com => com.descComune === denom.toUpperCase());
      if (comune) {

        this.richiestaService.saveRichiedenti(this.richiesta.idRichiesta, [comune as Richiedente]).subscribe(
          res => {
            this.comuniRichiedenti = [];
            this.comuniRichiedenti.push(res[0]);
            this.richiedentiTableSource.load(this.comuniRichiedenti);
            this.richiedentiTableSource.setSort([{ field: 'descComune', direction: 'asc' }]);
            this.enableTabAR = false;
            this.enableTabAN = false;

            this.richiedentiOut.emit({
              richiedentiList: this.comuniRichiedenti
            });

          }, err => {
            if (err.error && err.error.message) {
              this.messageService.showMessage('Errore', err.error.message);
            } else {
              this.messageService.showGenericError();
            }
          }
        );

      } else {
        this.messageService.showMessage('Errore', 'Selezionare un comune valido.');
      }
    } else {
      this.messageService.showMessage('Errore', 'Selezionare un comune valido.');
    }
  }

  aggiungiAssociazione() {
    if (!this.comuniFromAssociazione || this.comuniFromAssociazione.length === 0) {
      this.messageService.showMessage('Errore', 'Selezionare almeno un comune dalla lista.');
      return;
    }

    const comuniFromAssociazioneFiltered = [];
    this.comuniFromAssociazione.forEach( element => {
      const foundComune = this.comuniRichiedenti.find(comune => comune.istatComune === element.istatComune);
      if ( !foundComune ) {
        comuniFromAssociazioneFiltered.push(element as Richiedente);
      }
    });

    if (this.associazioneSelected.idAssociazione === 0) {
      delete this.associazioneSelected.idAssociazione;
      this.richiestaService.saveAssociazione(this.associazioneSelected).subscribe(
        res => {
          this.associazioneSelected = res;
          this.linkAndSaveRichiedenti(this.associazioneSelected, comuniFromAssociazioneFiltered);
        }, err => {
          if (err.error && err.error.message) {
            this.messageService.showMessage('Errore', err.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.linkAndSaveRichiedenti(this.associazioneSelected, comuniFromAssociazioneFiltered);
    }
    this.enableTabCS = false;
    this.enableTabAN = false;
  }

  linkAndSaveRichiedenti(associazione: AssociazioneComuni, comuniList: Comune[]) {
    if ( !this.richiesta.idAssociazione ) {
      this.richiestaService.linkAssociazioneToRichiesta(this.richiesta.idRichiesta, associazione.idAssociazione).subscribe(
        response => {
          // update richiesta with associazione info
          this.richiesta.idAssociazione = associazione.idAssociazione;
          this.richiesta.descAssociazione = associazione.descAssociazione;
          this.richiesta.descTipoFormaAssociativa = associazione.descTipoFormaAssociativa;
          this.richiesta.codTipoFormaAssociativa = associazione.codTipoFormaAssociativa;
          this.saveRichiedenti(associazione, comuniList);
        }, error => {
          if (error.error && error.error.message) {
            this.messageService.showMessage('Errore', error.error.message);
          } else {
            this.messageService.showGenericError();
          }
        }
      );
    } else {
      this.saveRichiedenti(associazione, comuniList);
    }

  }

  saveRichiedenti(associazione: AssociazioneComuni, comuniList: Comune[]): void {
    this.richiestaService.saveRichiedenti(this.richiesta.idRichiesta, comuniList as Richiedente[]).subscribe(
      res => {
        (res || []).forEach(richiedente => {
          const foundRichiedenteIndex = (this.comuniRichiedenti || []).findIndex(comune => comune.istatComune === richiedente.istatComune);
          if ( foundRichiedenteIndex > - 1 ) {
            this.comuniRichiedenti.splice(foundRichiedenteIndex, 1, richiedente);
          } else {
            this.comuniRichiedenti.push(richiedente);
          }
        });
        this.richiedentiTableSource.load(this.comuniRichiedenti);
        this.richiedentiTableSource.setSort([{ field: 'descComune', direction: 'asc' }]);
        this.setCheckBoxes();
        // this.enableTabAN = this.selectedTab === 'AN';

        this.richiedentiOut.emit({
          richiedentiList: this.comuniRichiedenti,
          associazione
        });

        this.ricercaForm.disable({emitEvent: false});

        if (this.selectedTab === 'AN') {
          this.formaAssociativaForm.disable();
          this.enableSalvaFA = false;

          this.loadData();
          this.enableTabAR = true;
          this.changeTab('AR', true);
          // this.ricercaForm.get('tipoAssRegistrata').setValue(this.associazioneSelected.descTipoFormaAssociativa);
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

  salvaFormaAssociativa() {
    if (!this.formaAssociativaForm.valid) {
      this.messageService.showMessage('Errore', 'Inserire i dati obbligatori per la forma associativa.');
      return;
    }

    const content = 'Una volta effettuato il salvataggio non sarà più possibile modificare la nuova Forma Associativa. ' +
      'Per modificare la lista dei richiedenti sarà necessario svuotare l\'attuale lista e selezionare un\'altra Forma Associativa.<br>Sei sicuro/a di voler procedere?';
    const modalRef = this.messageService.showConfirmation('Attenzione', content);
    modalRef.result.then(confirm => {
      if (confirm) {
        const tipoValue = this.formaAssociativaForm.get('tipo').value as { codTipo: string, descrizione: string };
        const codTipoFormaAssociativa = tipoValue && tipoValue.codTipo;
        const descTipoFormaAssociativa = tipoValue && tipoValue.descrizione;
        const newFA: AssociazioneComuni = {
          descAssociazione: this.formaAssociativaForm.get('denom').value,
          codTipoFormaAssociativa,
          descTipoFormaAssociativa,
          comuni: this.comuniRichiedenti as Comune[]
        };
        this.richiestaService.saveAssociazione(newFA).subscribe(
          res => {
            this.associazioneSelected = res;
            this.linkAndSaveRichiedenti(res, res.comuni);
          }, err => {
            if (err.error && err.error.message) {
              this.messageService.showMessage('Errore', err.error.message);
            } else {
              this.messageService.showGenericError();
            }
          }
        );
      }
    });
  }

  setCheckBoxes() {
    const cbArray = [];
    const tdArray = Array.from(document.getElementsByClassName('ng2-smart-action-multiple-select'));
    for (const el of tdArray) {
      if (el.children[0]['checked']) {
        cbArray.push(el.children[0]);
        el.children[0]['disabled'] = true;
      } else {
        el.children[0]['disabled'] = false;
      }
    }
  }

  rimuoviRichiedente(data) {
    if (this.selectedTab === 'CS') {
      const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler eliminare l\'elemento selezionato?');
      modalRef.result.then(confirm => {
        if (confirm) {
          this.richiestaService.deleteRichiedente(this.richiesta.idRichiesta, data.idRichiedente).subscribe(
            res => {
              this.comuniRichiedenti = [];
              this.richiedentiTableSource.load(this.comuniRichiedenti);
              this.checkRichiedentiList();
              this.richiedentiOut.emit(null);
            }, err => {
              if (err.error && err.error.message) {
                this.messageService.showMessage('Errore', err.error.message);
              } else {
                this.messageService.showGenericError();
              }
            }
          );
        }
      });

    } else if (this.selectedTab === 'AR') {
      const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler eliminare l\'elemento selezionato?');
      modalRef.result.then(confirm => {
        if (confirm) {
          this.richiestaService.deleteRichiedente(this.richiesta.idRichiesta, data.idRichiedente).subscribe(
            res => {
              this.richiedentiTableSource.remove(data);
              const index = this.comuniRichiedenti.indexOf(data);
              this.comuniRichiedenti.splice(index, 1);
              this.checkRichiedentiList();

              this.associazioneTable.grid.getSelectedRows().forEach(row => {
                if (row.data.istatComune === data.istatComune) {
                  row.isSelected = false;
                }
              });
              setTimeout(() => {
                this.setCheckBoxes();
              }, 0);

              this.richiedentiOut.emit({
                richiedentiList: this.comuniRichiedenti,
                associazione: this.associazioneSelected
              });
            }, err => {
              if (err.error && err.error.message) {
                this.messageService.showMessage('Errore', err.error.message);
              } else {
                this.messageService.showGenericError();
              }
            }
          );
        }
      });

    } else {
      if (data.idRichiedente) {
        const content = 'La Forma Associativa non può essere modificata. Desideri svuotare completamente la lista dei richiedenti e selezionare un\'altra Forma Associativa?';
        const modalRef = this.messageService.showConfirmation('Attenzione', content);
        modalRef.result.then(confirm => {
          if (confirm) {
            this.comuniRichiedenti.forEach( (richiedente, index) => {
              this.richiestaService.deleteRichiedente(this.richiesta.idRichiesta, richiedente.idRichiedente).subscribe(
                res => {
                  if (index === this.comuniRichiedenti.length - 1) {
                    this.checkRichiedentiList(true);
                    this.resetNuovaAssociazione();
                    // this.ricercaForm.get('tipo').setValue('C');
                    this.associazioneSelected = null;
                    this.ricercaForm.get('associazione').reset();
                    this.ricercaForm.enable();
                    this.ricercaForm.get('tipoAssRegistrata').setValue(null);
                    this.ricercaForm.get('tipoAssRegistrata').disable();
                  }
                }, err => {
                  if (err.error && err.error.message) {
                    this.messageService.showMessage('Errore', err.error.message);
                  } else {
                    this.messageService.showGenericError();
                  }
                }
              );
            });
          }
        });
      } else {
        const modalRef = this.messageService.showConfirmation('Attenzione', 'Sei sicuro/a di voler eliminare l\'elemento selezionato?');
        modalRef.result.then(confirm => {
          if (confirm) {
            this.richiedentiTableSource.remove(data);
            const index = this.comuniRichiedenti.indexOf(data);
            this.comuniRichiedenti.splice(index, 1);
            this.checkRichiedentiList();

            this.richiedentiOut.emit({
              richiedentiList: this.comuniRichiedenti,
              associazione: this.associazioneSelected
            });
          }
        });
      }

    }
  }

  checkRichiedentiList(clear = false) {
    if (clear) {
      this.comuniRichiedenti = [];
      this.richiedentiOut.emit(null);
    }

    if (this.comuniRichiedenti.length === 0) {
      this.enableTabCS = true;
      this.enableTabAR = true;
      this.enableTabAN = true;
      this.associazioneSelected = null;
      this.associazioneTableSource.refresh();
      this.ricercaForm.get('tipoAssRegistrata').setValue(null);
      this.ricercaForm.get('associazione').reset();
      this.ricercaForm.get('associazione').enable();
    }
  }

  selectFromAssociazione(selection) {
    this.comuniFromAssociazione = selection;
  }

  ngOnDestroy() {
    this.unsubscribe$.next();
    this.unsubscribe$.complete();
  }

}
