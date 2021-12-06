import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvvedimentiComponent } from './provvedimenti.component';

describe('ProvvedimentiComponent', () => {
  let component: ProvvedimentiComponent;
  let fixture: ComponentFixture<ProvvedimentiComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvvedimentiComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvvedimentiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
