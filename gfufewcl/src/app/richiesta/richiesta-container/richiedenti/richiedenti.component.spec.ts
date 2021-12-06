import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RichiedentiComponent } from './richiedenti.component';

describe('RichiestaFinanziamentoComponent', () => {
  let component: RichiedentiComponent;
  let fixture: ComponentFixture<RichiedentiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RichiedentiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RichiedentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
