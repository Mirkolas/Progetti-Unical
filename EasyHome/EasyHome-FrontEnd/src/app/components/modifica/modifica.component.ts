import {Component, OnInit, TemplateRef} from '@angular/core';
import {ServiceService} from '../../service/service.service';
import {ImmobileMinimal} from '../../model/ImmobileMinimal';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-modifica',
  templateUrl: './modifica.component.html',
  styleUrl: './modifica.component.css',
  standalone: false,
})

export class ModificaComponent implements OnInit{

  idDaEliminare: number | null = null;
  modificaMinimal: ImmobileMinimal[] = [];
  constructor(private service: ServiceService, private modalService: BsModalService) {}

  ngOnInit() {

    let username = sessionStorage.getItem('username');
    if (username) {
      this.service.getImmobiliMinimalByUsername(username);
    }



      this.service.getModificaObservable().subscribe(data => {
        console.log('Dati ricevuti dal backend:', data);
        if (data && data.length > 0) {
          this.modificaMinimal = data;
          sessionStorage.setItem('modifica', JSON.stringify(this.modificaMinimal));
        }
      });
    }



  getImageSrc(imagePath: string): string {
    return this.service.getImageSrc(imagePath);
  }

  eliminaImmobile(id: number) {
    if (confirm('Sei sicuro di voler eliminare questo immobile?')) {
      this.service.eliminaImmobile(id).subscribe({
        next: () => {
          window.location.reload();
        },
        error: err => {
          console.error('Errore durante l\'eliminazione:', err);
          alert('Si è verificato un errore. Riprova più tardi.');
        }
      });
    }
  }


  apriModale(template: TemplateRef<any>, id: number) {
    this.idDaEliminare = id;
    this.modalService.show(template);
  }


  confermaEliminazione() {
    if (this.idDaEliminare !== null) {
      this.service.eliminaImmobile(this.idDaEliminare).subscribe({
        next: () => {
          window.location.reload();
        },
        error: err => {
          console.error('Errore durante l\'eliminazione:', err);
          alert('Si è verificato un errore. Riprova più tardi.');
        }
      });
    }
  }

  onImageError(event: Event) {
    (event.target as HTMLImageElement).src = 'assets/no-image.png';
  }

  annulla()
  {
    this.modalService.hide();
  }


}
