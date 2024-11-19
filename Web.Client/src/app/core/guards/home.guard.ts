import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../auth/services/user.service';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { JwtService } from '../auth/services/jwt.service';

@Injectable({
  providedIn: 'root',
})

// base page-->home, guard prevents new users/not authenticated users to accesing home
export class HomeGuard implements CanActivate {
  constructor(
    private userService: UserService,
    private router: Router,
    private jwtService: JwtService
  ) {}

  canActivate(): boolean {
    const isAuthenticated = this.jwtService.getToken();
    if (!isAuthenticated) {
      this.userService.purgeAuth(); // Clear authentication data
      this.router.navigate(['/signin']); // Redirect to the signin page
      return false; // Prevent access
    }
    console.log('token exists');
    return true; // Allow access
  }
}
