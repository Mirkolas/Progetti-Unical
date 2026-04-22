import {Component, Input} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ServiceService } from '../../service/service.service';
import {MatDialog} from '@angular/material/dialog';
import {SuccessErrorDialogComponent} from '../success-error-dialog/success-error-dialog.component';

@Component({
  selector: 'app-recensione',
  templateUrl: './recensione.component.html',
  styleUrls: ['./recensione.component.css'],
  standalone: false
})
export class RecensioneComponent {
  @Input() immobileId!: number;
  @Input() venditore!: string;
  username: string | null = '';
  form: FormGroup;
  rating: number = 0;
  descrizione: string = '';

  constructor(private dialog : MatDialog,private fb: FormBuilder, private router: Router, private service: ServiceService) {
    this.form = this.fb.group({
      rating: [0, Validators.required],
      descrizione: ['', Validators.required]
    });
  }


  setRating(value: number) {
    this.rating = value;
    this.form.get('rating')?.setValue(value);
  }

  submitRecensione() {
    this.username = sessionStorage.getItem('username');
    if (this.form.valid && this.username!=this.venditore) {
      const formData = new FormData();
      formData.append('rating', this.form.get('rating')?.value.toString());
      formData.append('descrizione', this.form.get('descrizione')?.value);
      formData.append('idImmobile', this.immobileId.toString());

      if(this.username!=null) formData.append('acquirente', this.username);
      console.log('Form Submitted', formData.keys());

      this.service.addRecensione(formData).subscribe({
        next: (response) => {
          this.dialog.open(SuccessErrorDialogComponent, {
            data: {
              title: 'Successo',
              message: 'Recensione inviata con successo!'
            }
          });

          window.location.reload();


        },
        error: (error) => {
          console.error('Errore durante l\'invio della recensione', error);
          this.dialog.open(SuccessErrorDialogComponent, {
            data: {
              title: 'Errore',
              message: "Errore durante l'invio della recensione."
            }
          });

        }
      });
    } else {
      this.dialog.open(SuccessErrorDialogComponent, {
        data: {
          title: 'Info',
          message: 'Non è possibile inviare la recensione.'
        }
      });

    }
  }
}
