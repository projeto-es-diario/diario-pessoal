import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { DiaryService } from './diary.service';

describe('DiaryService', () => {
  let service: DiaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient()]
    });
    service = TestBed.inject(DiaryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});