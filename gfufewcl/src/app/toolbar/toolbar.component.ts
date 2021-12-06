import { Component, OnInit } from '@angular/core';
import { APP_SETTINGS } from '../shared/settings/app-settings';
import { SecurityService } from '../shared/services/security.service';
import { LoadService } from '../shared/services/load.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {

  isCollapsed = true;
  codFiscale: string;
  cognome: string;
  nome: string;
  codProfilo: string;


  // allowedRichiesta: Array<string> = ['FITTIZIO'];
  // allowdProfili: Array<string> = ['FITTIZIO'];
  // allowedAnagrafiche: Array<string> = ['FITTIZIO'];


  constructor(private secureService: SecurityService, private loadService: LoadService, private router: Router) {
    this.codFiscale = APP_SETTINGS.codiceFiscale;
    this.cognome = APP_SETTINGS.cognome;
    this.nome = APP_SETTINGS.nome;
    this.codProfilo = APP_SETTINGS.codProfilo;

  }

  toggleMenu() {
    this.isCollapsed = !this.isCollapsed;
  }
  ngOnInit() {
  }
  ssoLogout() {
    this.loadService.logOutUtente();
    this.secureService.localLogout().subscribe(
      (response: any) => {
        const status = response.status;
        const pippo = response.body;
      });
    this.secureService.ssoLogout();
    // window.open('http://www.regione.piemonte.it', '_self');
  }

  navigate(e: any, path: string): void {
    e.preventDefault();
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => this.router.navigate([path]));
  }

}
