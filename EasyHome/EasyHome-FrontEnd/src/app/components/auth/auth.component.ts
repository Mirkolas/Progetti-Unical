import {Component, TemplateRef, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AuthService} from '../../auth/auth.service';
import {DialogService} from '../../service/dialog.service';
import {UserRole} from '../../auth/user-role';
import {FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';

@Component({
  selector: 'app-auth',
  standalone: false,

  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent {
  @ViewChild('authOptionsDialog') authOptionsDialog!: TemplateRef<any>;
  @ViewChild('registerDialog') registerDialog!: TemplateRef<any>;

  constructor(private dialogService: DialogService,private fb: FormBuilder, private dialog: MatDialog, private router: Router, private authService: AuthService) {
  }

  errorMessage: string = '';

user = { firstName: '',lastName:'',birthdate: '',country:'', province:'',city:'',cap:'',id:'',phoneNumber:'',address:'', username: '', email: '', password: '', confirmPassword: '', gender:'', acceptedTerms: '' };

  ngOnInit(): void {
    setTimeout(() => {
      this.dialogService.openDialog(this.authOptionsDialog);
    });
  }


  openDialogWithPrevent(event: Event, dialogTemplate: TemplateRef<any>) {
    event.preventDefault();
    this.closeDialog();
    this.dialog.open(dialogTemplate, {
      width: '400px',
    });
  }

  closeDialog() {
    this.dialog.closeAll();
  }

  passwordMismatch(): boolean {
    return this.user.password !== this.user.confirmPassword;
  }

  register(form: NgForm) {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    if (this.user.password !== this.user.confirmPassword) {
      alert('Le password non coincidono!');
      return;
    }
    console.log('Form da inviare:', this.user);

    this.authService.register(this.user)
      .subscribe({
        next: (response: any) => {
          console.log('Registrazione effettuata con successo:', response);

          this.closeDialog();
        },
        error: (err) => {
          console.error('Errore durante la registrazione:', err);
          this.initializeForm();
        }
      });
  }



  login() {
    this.authService.login(this.user.username, this.user.password)
      .subscribe({
        next: (response: any) => {
          console.log('Login effettuato con successo:', response);

          sessionStorage.setItem('username', response.username);
          sessionStorage.setItem('role', response.role);
          sessionStorage.setItem('userRole', 'acquirente');
          this.closeDialog();
          setTimeout(() => {
            if (response.role === 'ROLE_ADMIN') {
              this.router.navigate(['/admin']).then(() => {
                window.location.reload();
              });
            } else {
              this.router.navigate(['/']).then(() => {
                window.location.reload();
              });
            }
          }, 500);
        },
        error: (err) => {
          console.error('Errore durante il login:', err);

          if (err.status === 401) {
            this.errorMessage = "Credenziali errate. Riprova.";
          } else {
            this.errorMessage = "Si è verificato un errore. Riprova più tardi.";
          }
          this.initializeForm();
        }
      });
  }
initializeForm() {
    this.user = { firstName: '',lastName:'',birthdate: '',country:'', province:'',city:'',cap:'',id:'',phoneNumber:'',address:'', username: '', email: '', password: '', confirmPassword: '',gender:'', acceptedTerms: '' };
  }
}
