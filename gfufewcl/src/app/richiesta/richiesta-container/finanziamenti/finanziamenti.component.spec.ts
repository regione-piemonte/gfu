import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinanziamentiComponent } from './finanziamenti.component';

describe('FinanziamentiComponent', () => {
  let component: FinanziamentiComponent;
  let fixture: ComponentFixture<FinanziamentiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FinanziamentiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinanziamentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
