import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ServiceService } from '../../../service/service.service';


@Component({
  selector: 'app-aste-dettaglio',
  templateUrl: './aste-dettaglio.component.html',
  styleUrls: ['./aste-dettaglio.component.css'],
  standalone: false
})
export class AsteDettaglioComponent implements OnInit {
  immobileId!: number;
  asteDetails: any;
  latitudine: number | null = null;
  longitudine: number | null = null;
  markerPosition: google.maps.LatLngLiteral = { lat: 0, lng: 0 };
  center: google.maps.LatLngLiteral = { lat: 0, lng: 0 };
  zoom: number = 14;
  loading: boolean = true;
  recensioni: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private service: ServiceService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.immobileId = Number(params.get('id'));
      console.log('ID immobile ricevuto:', this.immobileId);

      this.service.getImmobileById(this.immobileId).subscribe((data) => {
        console.log(data);
        this.asteDetails = data;
        this.latitudine = this.asteDetails.latitudine;
        this.longitudine = this.asteDetails.longitudine;

        console.log(this.asteDetails.utente.username);
        this.caricaRecensioni(this.asteDetails.utente.username)

        // Imposta la mappa
        if (this.latitudine && this.longitudine) {
          this.markerPosition = { lat: this.latitudine, lng: this.longitudine };
          this.center = { lat: this.latitudine, lng: this.longitudine };
        }
      });
    });
  }

  getImageSrc(imagePath: string): string {
    return this.service.getImageSrc(imagePath);
  }


  caricaRecensioni(venditore : string) {
    if (venditore) {
      this.service.getRecensioniByVenditore(venditore).subscribe(
        (data) => {
          this.recensioni = data;
          console.log(this.recensioni);
          this.loading = false;
        },
        (error) => {
          console.error('Errore nel caricamento delle recensioni:', error);
          this.loading = false;
        }
      );
    }
  }

  getStarsArray(rating: number): number[] {
    return Array(rating).fill(0);
  }

  calculateEndDate(publicationDate: string): string {
    const date = new Date(publicationDate);
    date.setDate(date.getDate() + 60); // Aggiunge 60 giorni
    return date.toISOString().split('T')[0]; // Restituisce la data in formato YYYY-MM-DD
  }


}
