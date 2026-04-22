import {Component, Injectable, OnInit, TemplateRef, ViewChild} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {AuthComponent} from '../auth/auth.component';

import { DialogService} from '../../service/dialog.service';
import {ServiceService} from '../../service/service.service';
import {AuthService} from '../../auth/auth.service';


@Component({
  selector: 'navbar',
  templateUrl: './navbar.component.html',
  standalone: false,
  styleUrl: './navbar.component.css'
})
@Injectable({
  providedIn: 'root'
})
export class NavbarComponent implements OnInit{
  logged = false;
  username: string | null = null;
  userRole: string | null = null;
  isVenditore = false;
  @ViewChild('authOptionsDialog') authOptionsDialog!: TemplateRef<any>;

  menuItems = [
    { label: 'Home', icon: 'home', route: '/' },
    { label: 'About', icon: 'info', route: '/about' },
    { label: 'Aste', icon: 'gavel', route: '/aste' },
    { label: 'Contatti', icon: 'contact_mail', route: '/contacts' },

  ];
  role: string | null=null;

  constructor(private authService: AuthService, private dialog: MatDialog, private router: Router, private dialogService: DialogService,private service: ServiceService) {}

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

  toggleLogin() {
    if (!this.logged) {
      this.openLoginDialog();
    } else {
      this.logout();
    }
  }

  navigateTo(route: string) {
    this.closeDialog();
    setTimeout(() => {
      this.router.navigate([route]);
    }, 150);
  }

  modificaAnnuncio() {
    if(this.username!=null) this.service.getImmobiliMinimalByUsername(this.username);
    this.navigateTo('/modifica');
  }

  notifiche() {
    this.router.navigate(['/messaggi']);
  }

  openLoginDialog(): void {
    this.dialog.closeAll();
    this.dialogService.openDialog(AuthComponent);
  }


  // Logout dell'utente
  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        sessionStorage.clear();
        this.menuItems = [
          { label: 'Home', icon: 'home', route: '/' },
          { label: 'About', icon: 'info', route: '/about' },
          { label: 'Aste', icon: 'gavel', route: '/aste' },
          { label: 'Contatti', icon: 'contact_mail', route: '/contacts' },

        ];
        this.userRole = null;
        this.logged = false;
        this.username = null;
        this.removeModifyItem();
        this.router.navigate(['/']);
      },
      error: (err) => console.error('Errore nel recupero degli utenti:', err),
    });
  }

  ngOnInit(): void {
    this.userRole = sessionStorage.getItem('userRole');
    this.username = sessionStorage.getItem('username');
    this.role=sessionStorage.getItem("role");
    if (this.username) {
      this.logged = true;
      this.isVenditore = this.userRole === 'venditore'; // Imposta il ruolo iniziale
      this.updateMenuItems();
    }
  }

  removeModifyItem() {
    const modifyItemIndex = this.menuItems.findIndex(item => item.label === 'Gestisci Annunci');
    if (modifyItemIndex !== -1) {
      this.menuItems.splice(modifyItemIndex, 1); // Rimuove l'elemento
    }
  }

  // Metodo per aggiornare i menu dinamicamente
  updateMenuItems(): void {
    this.removeModifyItem();
    if (this.logged && this.isVenditore) {
      this.menuItems.push({ label: 'Gestisci Annunci', icon: 'build', route: '' });
    }
    if(this.logged && this.role=="ROLE_ADMIN"){
      this.menuItems=[];
      this.menuItems.push( { label: 'Home', icon: 'home', route: '/' });
      this.menuItems.push({ label: 'Modifica Annunci', icon: 'build', route: '/modifica' });
      this.menuItems.push( { label: 'Admin', icon: 'admin_panel_settings', route: '/admin' });
    }
  }

  toggleRole() {
    this.isVenditore = !this.isVenditore; // Cambia il ruolo
    this.userRole = this.isVenditore ? 'venditore' : 'acquirente'; // Salva il nuovo ruolo
    sessionStorage.setItem('userRole', this.userRole); // Salva il ruolo in localStorage
    console.log(this.isVenditore ? 'Venditore' : 'Acquirente');
    window.location.reload();
    this.updateMenuItems();  // Aggiorna i menu

  }

}
