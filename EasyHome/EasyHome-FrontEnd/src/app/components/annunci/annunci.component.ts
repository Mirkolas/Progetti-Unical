import { Component, OnInit } from '@angular/core';
import { ServiceService } from '../../service/service.service';
import {ImmobileMinimal} from '../../model/ImmobileMinimal';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-annunci',
  standalone: false,
  templateUrl: './annunci.component.html',
  styleUrls: ['./annunci.component.css']
})
export class AnnunciComponent implements OnInit {
  immobiliminimal: ImmobileMinimal[] = [];

  selectedImmobili: string | null = 'Tipo';
  selectedAffittoVendita: string | null = 'Categoria';
  selectedLuogo: string | null = 'Provincia';
  selectedSorting: string  = 'filtro';

  constructor(  private route: ActivatedRoute,private service: ServiceService) {}

  ngOnInit() {

    // Recupera i query parameters dalla URL
    this.route.queryParams.subscribe(params => {
      this.selectedImmobili = params['immobili'];
      this.selectedAffittoVendita = params['affittoVendita'];
      this.selectedLuogo = params['luogo'];

      // Chiama il servizio con i parametri passati
      this.service.getImmobiliMinimal(this.selectedImmobili, this.selectedAffittoVendita, this.selectedLuogo);
    });

    this.service.getImmobiliObservable().subscribe(data => {
      console.log('Dati ricevuti dal backend:', data);

      if (data && data.length > 0) {
        this.immobiliminimal = data;
      }
    });
  }
  sortImmobili() {
    this.immobiliminimal.sort((a, b) =>
      this.selectedSorting === 'prezzo'
        ? (a.prezzo_scontato || a.prezzo) - (b.prezzo_scontato || b.prezzo)
        : a.mq - b.mq
    );
  }



  getImageSrc(imagePath: string): string {
    return this.service.getImageSrc(imagePath);
  }


  onImageError(event: Event) {
    (event.target as HTMLImageElement).src = 'assets/no-image.png';
  }

}
