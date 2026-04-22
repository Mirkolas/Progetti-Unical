
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

@Injectable({
  providedIn: 'root',
})
export class DialogService {
  constructor(private dialog: MatDialog) {}

  openDialog(templateRef: any): void {
    this.dialog.closeAll();
    this.dialog.open(templateRef, {
      width: '400px', // Puoi specificare altre opzioni, come la larghezza
    });
  }

  closeAll(): void {
    this.dialog.closeAll();
  }
}
