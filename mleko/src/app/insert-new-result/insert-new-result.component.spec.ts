import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InsertNewResultComponent } from './insert-new-result.component';

describe('InsertNewResultComponent', () => {
  let component: InsertNewResultComponent;
  let fixture: ComponentFixture<InsertNewResultComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InsertNewResultComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InsertNewResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
