import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewStrategiesComponent } from './view-strategies.component';

describe('ViewStrategiesComponent', () => {
  let component: ViewStrategiesComponent;
  let fixture: ComponentFixture<ViewStrategiesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewStrategiesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewStrategiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
