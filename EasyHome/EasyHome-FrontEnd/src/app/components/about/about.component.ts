import {Component, ViewChild} from '@angular/core';

@Component({
  selector: 'app-about',
  standalone: false,
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent {

  center = {lat: 39.3042, lng: 16.2503};
  zoom = 12; // Livello di zoom
  markerPosition = {lat: 39.3042, lng: 16.2503};

  // Coordinate della sede (Cosenza)
  latitude: number = 39.314460;
  longitude: number = 16.254225;

  constructor() {
  }


}
