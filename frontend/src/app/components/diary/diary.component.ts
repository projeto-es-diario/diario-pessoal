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
  entries: any[] = [];
  moods = [1, 2, 3, 4, 5]; // 1: Sad, 5: Happy

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
        this.entries = data;
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
        this.entries = data;
      },
      error: (err) => {
        console.error('Failed to search entries', err);
      }
    });
  }

  onSubmit(): void {
    if (this.entryForm.valid) {
      this.diaryService.createEntry(this.entryForm.value).subscribe({
        next: () => {
          this.loadEntries(); // Refresh the list
          this.entryForm.reset();
        },
        error: (err) => {
          console.error('Failed to create entry', err);
        }
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
