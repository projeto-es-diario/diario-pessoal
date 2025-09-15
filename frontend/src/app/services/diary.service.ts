import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiaryService {

  private apiUrl = 'http://localhost:8080/api/diary';

  constructor(private http: HttpClient) { }

  getEntries(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/entries`);
  }

  createEntry(entry: { content: string, mood?: number }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/entries`, entry);
  }

  searchEntries(keyword?: string, date?: string): Observable<any[]> {
    let params = new HttpParams();
    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (date) {
      params = params.set('date', date);
    }
    return this.http.get<any[]>(`${this.apiUrl}/entries/search`, { params });
  }
}
