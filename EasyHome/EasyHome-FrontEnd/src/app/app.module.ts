import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './components/home/home.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import {MatToolbar} from "@angular/material/toolbar";
import {MatIcon} from '@angular/material/icon';
import {NgOptimizedImage} from '@angular/common';
import { GoogleMapsModule  } from '@angular/google-maps';
import { AboutComponent } from './components/about/about.component';
import { FooterComponent } from './components/footer/footer.component';
import {MatFormField} from "@angular/material/form-field";
import { MatFormFieldModule } from '@angular/material/form-field';
import { ContactsComponent } from './components/contacts/contacts.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AnnunciComponent } from './components/annunci/annunci.component';
import {MatCard, MatCardContent} from '@angular/material/card';
import { AggiungiComponent } from './components/aggiungi/aggiungi.component';
import {provideHttpClient} from "@angular/common/http";
import { AuthComponent } from './components/auth/auth.component';
import { AdminpageComponent } from './components/adminpage/adminpage.component';
import { ErrorpageComponent } from './components/errorpage/errorpage.component';
import { RecensioneComponent } from './components/recensione/recensione.component';
import { AsteComponent } from './components/aste/aste.component';
import { ContattavenditoreComponent } from './components/contattavenditore/contattavenditore.component';
import { MessaggiComponent } from './components/messaggi/messaggi.component';
import { AnnuncioDettaglioComponent } from './components/annunci/annuncio-dettaglio/annuncio-dettaglio.component';
import { CookiesComponent } from './components/cookies/cookies.component';
import { TerminiecondizioniComponent } from './components/terminiecondizioni/terminiecondizioni.component';
import { PrivacyComponent } from './components/privacy/privacy.component';
import { ModificaComponent } from './components/modifica/modifica.component';
import { ModificaDettaglioComponent } from './components/modifica/modifica-dettaglio/modifica-dettaglio.component';
import { LoadingComponent } from './components/loading/loading.component';
import { FacebookComponent } from './components/facebook/facebook.component';
import { AsteDettaglioComponent } from './components/aste/aste-dettaglio/aste-dettaglio.component';
import { SuccessErrorDialogComponent } from './components/success-error-dialog/success-error-dialog.component';
import { ModalModule } from 'ngx-bootstrap/modal';
@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    NavbarComponent,
    AboutComponent,
    FooterComponent,
    ContactsComponent,
    AnnunciComponent,
    AggiungiComponent,
    AuthComponent,
    AdminpageComponent,
    ErrorpageComponent,
    RecensioneComponent,
    AsteComponent,
    ContattavenditoreComponent,
    MessaggiComponent,
    AnnuncioDettaglioComponent,
    CookiesComponent,
    TerminiecondizioniComponent,
    PrivacyComponent,
    ModificaComponent,
    ModificaDettaglioComponent,
    LoadingComponent,
    SuccessErrorDialogComponent,
    AsteDettaglioComponent,
    FacebookComponent,




],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatButtonModule,
    MatMenuModule,
    MatToolbar,
    MatDialogModule,
    MatIcon,
    NgOptimizedImage,
    GoogleMapsModule,
    MatFormField,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatCardContent,
    MatCard,
    FormsModule,
    ModalModule.forRoot()
  ],
  providers: [provideHttpClient()],
  bootstrap: [AppComponent]
})
export class AppModule {
}
