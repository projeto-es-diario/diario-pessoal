import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DiaryService } from '../../services/diary.service';
import { AuthService } from '../../services/auth.service';
import { CommonModule } from '@angular/common';
import { MoodChartComponent } from '../mood-chart/mood-chart.component';

@Component({
  selector: 'app-diary',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MoodChartComponent],
  templateUrl: './diary.component.html',
  styleUrls: ['./diary.component.css']
})
export class DiaryComponent implements OnInit {
  entryForm: FormGroup;
  searchForm: FormGroup;
  
  allEntries: any[] = [];
  publishedEntries: any[] = [];
  drafts: any[] = [];

  moods = [1, 2, 3, 4, 5]; 
  editingDraftId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private diaryService: DiaryService,
    private authService: AuthService,
    private router: Router
  ) {
    this.entryForm = this.fb.group({
      content: ['', [Validators.required, Validators.maxLength(5000)]],
      mood: [null]
    });

    this.searchForm = this.fb.group({
      keyword: [''],
      date: ['']
    });
  }

  ngOnInit(): void {
    this.loadEntries();
  }

  loadEntries(): void {
    this.diaryService.getEntries().subscribe({
      next: (data) => {
        this.allEntries = data;
        this.publishedEntries = data.filter(e => e.status === 'PUBLISHED');
        this.drafts = data.filter(e => e.status === 'DRAFT');
      },
      error: (err) => {
        console.error('Failed to load entries', err);
      }
    });
  }

  onSearch(): void {
    const { keyword, date } = this.searchForm.value;
    this.diaryService.searchEntries(keyword, date).subscribe({
      next: (data) => {
        this.allEntries = data;
        this.publishedEntries = data.filter(e => e.status === 'PUBLISHED');
        this.drafts = data.filter(e => e.status === 'DRAFT');
      },
      error: (err) => {
        console.error('Failed to search entries', err);
      }
    });
  }

  onSubmit(status: 'PUBLISHED' | 'DRAFT'): void {
    if (this.entryForm.valid) {
      const entryData = { ...this.entryForm.value, status };
      
      this.diaryService.createEntry(entryData).subscribe({
        next: () => {
          this.loadEntries(); 
          this.entryForm.reset();
          this.editingDraftId = null;
        },
        error: (err) => {
          console.error('Failed to create entry', err);
        }
      });
    }
  }

  loadDraftForEditing(draft: any): void {
    this.entryForm.patchValue({
      content: draft.content,
      mood: draft.mood
    });
    this.editingDraftId = draft.id;
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}