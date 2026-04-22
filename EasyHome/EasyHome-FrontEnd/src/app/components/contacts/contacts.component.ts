import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ServiceService } from '../../service/service.service';
import { Router } from '@angular/router';
import {SuccessErrorDialogComponent} from '../success-error-dialog/success-error-dialog.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-contacts',
  standalone: false,
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.css'] // Correzione nel nome del file (styleUrls invece di styleUrl)
})
export class ContactsComponent {
  contactForm: FormGroup;
  formSubmitted = false;  // Flag per tracciare se il form è stato inviato

  constructor(private dialog: MatDialog,private fb: FormBuilder, private service: ServiceService, private router: Router) {
    this.contactForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(2)]],
      cognome: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      tipo: ['', [Validators.required]],
      domanda: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onSubmit() {
    this.formSubmitted = true;  // Flag per segnare l'invio del modulo
    if (this.contactForm.valid) {
      console.log('Form Submitted', this.contactForm.value);
    } else {
      console.log('Form is invalid');
    }
  }

  aggiungiContatto() {
    this.formSubmitted = true;  // Setta il flag a true quando invii il contatto

    if (this.contactForm.valid) {
      console.log('Contatto da inviare:', this.contactForm.value);

      this.service.addrichiesta(this.contactForm.value).subscribe({
        next: (response) => {

          this.dialog.open(SuccessErrorDialogComponent, {
            data: {
              title: 'Successo',
              message: 'Messaggio inviato con successo!'
            }
          });
          this.router.navigate(['/']);
        },
        error: (error) => {
          console.error('Errore:', error);
          this.dialog.open(SuccessErrorDialogComponent, {
            data: {
              title: 'Errore',
              message: 'Errore durante l\'invio del contatto'
            }
          });

        }
      });
    } else {
      this.dialog.open(SuccessErrorDialogComponent, {
        data: {
          title: 'Info',
          message: 'Per favore, completa tutti i campi.'
        }
      });


    }
  }
}
