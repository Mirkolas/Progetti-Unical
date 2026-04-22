import { Injectable } from '@angular/core';
import {BehaviorSubject, catchError, Observable, of, switchMap, tap} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {UserRole} from './user-role';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'api';

  currentUserSubject = new BehaviorSubject<any | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();


  constructor(private http: HttpClient) {
  }

  login(username: string, password: string): Observable<void> {
    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);

    return this.http.post<void>(`${this.apiUrl}/login`, body.toString(), {
      headers: {'Content-Type': 'application/x-www-form-urlencoded'},
      withCredentials: true,
    });

  }

  register(form: any): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/open/createUser`, form, {
      headers: {'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': 'http://localhost:4200'},
      withCredentials: true,
    });
  }


  logout() {
    return this.http.post<void>(`${this.apiUrl}/logout`, {}, {withCredentials: true}).pipe(
      switchMap(() => {
        console.log("performing logout");
        this.currentUserSubject.next(null);
        return of(null);
      })
    );
  }


  getUser(): Observable<any | null> {
    if (this.currentUserSubject.value) {
      return of(this.currentUserSubject.value);

    }
    return this.http.get<any>(`/${this.apiUrl}/open/check-user`, {
      withCredentials: true,
    }).pipe(
      switchMap((user) => {
        this.currentUserSubject.next(user);

        return of(user);
      }),
      catchError(() => {
        this.currentUserSubject.next(null);
        return of(null);
      })
    );
  }
}
