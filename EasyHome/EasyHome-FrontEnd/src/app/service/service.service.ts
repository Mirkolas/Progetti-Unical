import { Injectable } from '@angular/core';
import {BehaviorSubject, catchError, map, Observable, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Immobile} from '../model/Immobile';
import {ImmobileMinimal} from '../model/ImmobileMinimal';

@Injectable({
  providedIn: 'root'
})
export class ServiceService {

  constructor(private http: HttpClient) { }
  private apiUrl = 'api';

  private immobiliSubject = new BehaviorSubject<ImmobileMinimal[]>([]); // Memorizza gli immobili
  immobili$ = this.immobiliSubject.asObservable(); // Espone gli immobili come Observable

  private asteSubject = new BehaviorSubject<ImmobileMinimal[]>([]); // Memorizza gli immobili
  aste$ = this.asteSubject.asObservable(); // Espone gli immobili come Observable


  private modificaSubject = new BehaviorSubject<ImmobileMinimal[]>([]); // Memorizza gli immobili
  modifica$ = this.modificaSubject.asObservable(); // Espone gli immobili come Observable

  private messaggiSubject = new BehaviorSubject<any[]>([]);
  messaggi$ = this.messaggiSubject.asObservable();

  getImmobiliMinimal(tipo: string | null, affittoVendita: string | null, luogo: string | null): void {
    const params = {
      tipo: tipo || '',
      categoria: affittoVendita || '',
      provincia: luogo || ''
    };

    this.http.get<ImmobileMinimal[]>(`${this.apiUrl}/open/immobili`, {
      params: params,
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true,
    }).subscribe({
      next: (immobili) => {
        console.log("Dati ricevuti dal backend:", immobili);
        this.immobiliSubject.next(immobili);
      },
      error: (err) => console.error("Errore nel caricamento degli immobili", err),
    });
  }

  getImmobiliObservable(): Observable<ImmobileMinimal[]> {
    return this.immobili$; // Espone gli immobili come Observable
  }

  getAsteObservable(): Observable<ImmobileMinimal[]> {
    return this.aste$; // Espone gli immobili come Observable
  }

  getMessaggiObservable(): Observable<any[]> {
    return this.messaggi$; // Espone gli immobili come Observable
  }

  getModificaObservable(): Observable<any[]> {
    return this.modifica$; // Espone gli immobili come Observable
  }


  addAnnuncio(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/immobili/createImmobile`, formData, {
      withCredentials: true
    });
  }
  addRecensione(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/recensioni/createRecensione`, formData, {
      withCredentials: true
    });
  }

  getImmobiliUtente(username: string): Observable<Immobile[]> {
    return this.http.get<Immobile[]>(`${this.apiUrl}/auth/${username}/immobili`, {
      withCredentials: true
    });
  }

  addrichiesta(contatti: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/CreaRichiesta`, contatti, {
      withCredentials: true,
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  eliminaImmobile(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/auth/immobili/deleteImmobile/${id}`);
  }

  getUsers(): Observable<{ username: string; role: string }[]> {
    return this.http.get<{ username: string; role: string }[]>(`${this.apiUrl}/admin/users`);
  }

  changeUserRole(username: string, newRole: string): Observable<any> {
    const body = {username, newRole };

    return this.http.post(`${this.apiUrl}/admin/change_role`, body, {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true,
      responseType: 'text'
    });
  }




  deleteUser(username: string): Observable<any> {
    return this.http.delete(`${this.apiUrl}/admin/users/${username}`).pipe(
      catchError((error) => {
        console.error('Errore nella risposta HTTP:', error);
        if (error.status === 404) {
          console.error('Utente non trovato');
        } else {
          console.error('Errore imprevisto:', error);
        }
        return throwError(() => new Error('Errore nella richiesta'));
      })
    );
  }


  contattaVenditore(form: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/messaggi/createMessaggio`, form, {
      withCredentials: true
    })
  }

  // Metodo per ottenere i dettagli di un immobile tramite ID
  getImmobileById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/auth/dettaglio/${id}`, {
      withCredentials:true
    });
  }


  getMarkers(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/open/markers`);
  }

  getImageSrc(imagePath: string): string {
    if (imagePath) {
      const parts = imagePath.split('/');
      if (parts.length === 3) {
        return `/api/open/images/${parts[0]}/${parts[1]}/${parts[2]}`;
      }
    }
    return 'assets/no-image.png';
  }


  getMessaggiById(username: string): void {
    this.http.get<any>(`${this.apiUrl}/auth/${username}/messaggi`, {
      withCredentials: true
    }).subscribe({
      next: (response) => {
        console.log(response);
        this.messaggiSubject.next(response);
      },
      error: (err) => console.error("GetMessaggiById doesn't work", err),
    });
  }

  updateImmobile(immobileId: number, formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/auth/immobili/updateImmobile/${immobileId}`, formData, {
      withCredentials: true
    });
  }

  getImmobiliMinimalByUsername(username: string): void {
    this.http.get<ImmobileMinimal[]>(`${this.apiUrl}/auth/immobili/${username}`, {
      withCredentials: true
    }).subscribe({
      next: (immobili) => {
        console.log("Dati ricevuti dal backend:", immobili);
        this.modificaSubject.next(immobili);
      },
      error: (err) => console.error("Errore nel caricamento degli immobili", err),
    });
  }

  getRecensioniByVenditore(venditore: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/auth/${venditore}/recensioni`);
  }

  createAd(adName: string, adsetId: string, creativeId: string) {
    return this.http.post(`${this.apiUrl}/auth/facebook/ads`, { adName, adsetId, creativeId },{
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true
    });
  }

  deleteMessaggio(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/auth/messaggi/deleteMessaggio/${id}`, {
      withCredentials: true
    })
  }

}

