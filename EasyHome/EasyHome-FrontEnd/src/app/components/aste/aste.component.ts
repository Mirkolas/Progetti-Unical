import { Component, OnInit } from '@angular/core';
import { ServiceService } from '../../service/service.service';
import { ImmobileMinimal } from '../../model/ImmobileMinimal';

@Component({
  selector: 'app-aste',
  standalone: false,
  templateUrl: './aste.component.html',
  styleUrls: ['./aste.component.css']
})
export class AsteComponent implements OnInit {
  aste: ImmobileMinimal[] = [];

  constructor(private service: ServiceService) {}

  ngOnInit() {

    this.service.getImmobiliMinimal('Tutti','Aste','Tutte');

    // Chiamata al servizio per ottenere gli immobili
    this.service.getImmobiliObservable().subscribe(data => {
      console.log('Dati ricevuti dal backend:', data);
      if (data && data.length > 0) {
        this.aste = data;
      }
    });
  }

  getImageSrc(imagePath: string): string {
    return this.service.getImageSrc(imagePath);
  }

  onImageError(event: Event) {
    (event.target as HTMLImageElement).src = 'assets/no-image.png';
  }

}
