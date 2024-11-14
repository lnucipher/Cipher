import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../../core/auth/services/user.service';
import { User } from '../../../../core/auth/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-leftside-bar',
  templateUrl: './leftside-bar.component.html',
  styleUrl: './leftside-bar.component.css',
  standalone: true,
})
export class LeftsideBarComponent implements OnInit {
  user: User | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.userService.currentUser.subscribe((user) => {
      this.user = user;
    });
  }

  getAvatarUrl(): string {
    return this.user
      ? `https://localhost:5000/identity/${this.user.avatarUrl}`
      : '';
  }

  logout(): void {
    this.userService.logout();
  }
}
