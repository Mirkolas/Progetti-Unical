import {Component, OnInit} from '@angular/core';
import {ServiceService} from '../../service/service.service';

@Component({
  selector: 'app-messaggi',
  standalone: false,

  templateUrl: './messaggi.component.html',
  styleUrl: './messaggi.component.css'
})
export class MessaggiComponent implements OnInit {
  constructor(private service: ServiceService) {}

  messages: any[] = [];
  username: string | null = '';

  ngOnInit() {
    this.username=sessionStorage.getItem('username');
    if(this.username!=null) this.service.getMessaggiById(this.username);
    this.service.getMessaggiObservable().subscribe(data => {
      console.log('Dati ricevuti dal backend:', data);

      if (data && data.length > 0) {
        this.messages = data;
      }
    });
  }

  toggleExpand(message: any) {
    message.expanded = !message.expanded;
  }

  deleteMessage(index: number) {
    this.service.deleteMessaggio(index).subscribe(data => {
      window.location.reload();
    })
  }
}




