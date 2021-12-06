import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiestaFinanziamentoSearchComponent } from './richiesta-finanziamento-search.component';

describe('RichiestaFinanziamentoSearchComponent', () => {
  let component: RichiestaFinanziamentoSearchComponent;
  let fixture: ComponentFixture<RichiestaFinanziamentoSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiestaFinanziamentoSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiestaFinanziamentoSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
