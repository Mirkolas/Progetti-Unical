import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ServiceService } from '../../../service/service.service';


@Component({
  selector: 'app-annuncio-dettaglio',
  templateUrl: './annuncio-dettaglio.component.html',
  styleUrls: ['./annuncio-dettaglio.component.css'],
  standalone: false
})
export class AnnuncioDettaglioComponent implements OnInit {
  immobileId!: number;
  immobileDetails: any;
  latitudine: number | null = null;
  longitudine: number | null = null;
  markerPosition: google.maps.LatLngLiteral = { lat: 0, lng: 0 };
  center: google.maps.LatLngLiteral = { lat: 0, lng: 0 };
  zoom: number = 14;
  loading: boolean = true;
  recensioni: any[] = [];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private service: ServiceService
  ) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.immobileId = Number(params.get('id'));
      console.log('ID immobile ricevuto:', this.immobileId);

      this.service.getImmobileById(this.immobileId).subscribe((data) => {
        console.log(data);
        this.immobileDetails = data;
        this.latitudine = this.immobileDetails.latitudine;
        this.longitudine = this.immobileDetails.longitudine;

        console.log(this.immobileDetails.utente.username);
        this.caricaRecensioni(this.immobileDetails.utente.username)

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


}
