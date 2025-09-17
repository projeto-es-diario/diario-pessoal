import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  private isBrowser: boolean;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    // Verifica se o código está rodando no navegador
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, user);
  }

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials);
  }

  saveToken(token: string): void {
    // Só executa se estiver no navegador
    if (this.isBrowser) {
      localStorage.setItem('authToken', token);
    }
  }

  getToken(): string | null {
    // Só executa se estiver no navegador
    if (this.isBrowser) {
      return localStorage.getItem('authToken');
    }
    return null;
  }

  logout(): void {
    // Só executa se estiver no navegador
    if (this.isBrowser) {
      localStorage.removeItem('authToken');
    }
  }
}