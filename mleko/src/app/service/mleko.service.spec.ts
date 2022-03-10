import { TestBed } from '@angular/core/testing';

import { MlekoService } from './mleko.service';

describe('MlekoService', () => {
  let service: MlekoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MlekoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
