import { Component } from '@angular/core';
import { LeftsideBarComponent } from './leftside-bar/leftside-bar.component';
import { CurrentChatComponent } from './current-chat/current-chat.component';
import { ContactsBarComponent } from './contacts-bar/contacts-bar.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
  standalone: true,
  imports: [LeftsideBarComponent, CurrentChatComponent, ContactsBarComponent]
})
export class HomeComponent {

}
