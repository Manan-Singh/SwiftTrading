import { TestBed, inject } from '@angular/core/testing';

import { StrategyService } from './strategy.service';

describe('StrategyService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StrategyService]
    });
  });

  it('should be created', inject([StrategyService], (service: StrategyService) => {
    expect(service).toBeTruthy();
  }));
});
