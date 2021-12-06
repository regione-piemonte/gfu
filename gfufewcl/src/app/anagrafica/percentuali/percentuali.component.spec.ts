import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PercentualiComponent } from './percentuali.component';

describe('PercentualiComponent', () => {
  let component: PercentualiComponent;
  let fixture: ComponentFixture<PercentualiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PercentualiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PercentualiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
