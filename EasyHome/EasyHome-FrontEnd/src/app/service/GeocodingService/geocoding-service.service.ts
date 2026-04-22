import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GeocodingService {

  //KEY FUNZIONANTE
  private apiUrl = 'https://maps.googleapis.com/maps/api/geocode/json';

  constructor(private http: HttpClient) {}

  getLatLng(address: string): Observable<any> {
    const params = new HttpParams()
      .set('address', address)
      .set('key', 'AIzaSyD2U3yjRS_sW8tXrKFEZy8Ve07RE-JWjc8');

    return this.http.get(this.apiUrl, { params }).pipe(
      catchError(error => {
        console.error('Errore nella geocodifica:', error);
        return throwError(error);
      })
    );
  }

}

