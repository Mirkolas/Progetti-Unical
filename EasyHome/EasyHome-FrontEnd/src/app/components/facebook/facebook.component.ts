import { Component } from '@angular/core';
import {ServiceService} from '../../service/service.service';

@Component({
  selector: 'app-facebook',
  templateUrl: './facebook.component.html',
  standalone: false,
  styleUrls: ['./facebook.component.css']
})
export class FacebookComponent {
  constructor(private service: ServiceService) {}

  //ALTERNATIVA CON API GRAPH
  createAd() {
    this.service.createAd('Annuncio Test', '1234567890', '0987654321')
      .subscribe(response => console.log('Annuncio creato:', response));
  }

  shareOnFacebook() {
    const url = encodeURIComponent('https://tinyurl.com/2wy37tam');
    const quote = encodeURIComponent('Guarda questo sito!');
    const facebookUrl = `https://www.facebook.com/sharer/sharer.php?u=${url}&quote=${quote}`;

    window.open(facebookUrl, '_blank');
  }

}
