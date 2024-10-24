import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserService } from '../auth/services/user.service';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})

// base page-->home, guard prevents new users/not authenticated users to accesing home
export class HomeGuard implements CanActivate {
  constructor(private userService: UserService, private router: Router) {}

  canActivate(): Observable<boolean> { //returns true/false
    return this.userService.isAuthenticated.pipe(
      map((isAuth) => {
        if (!isAuth) {
          this.router.navigate(['/sign-in']); // redirect if not false
        }
        return isAuth; //let user use the route if true
      })
    );
  }
}
