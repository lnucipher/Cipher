import { Routes, Router } from '@angular/router';
import { inject } from '@angular/core';
import { UserService } from './core/auth/services/user.service';
import { SignInComponent } from './features/sign-in/sign-in.component';
import { SignUpComponent } from './features/sign-up/sign-up.component';
import { ProfileSetupComponent } from './features/profile-setup/profile-setup.component';
import { HomeComponent } from './features/home/pages/home.component';
import { HomeGuard } from './core/guards/home.guard';
import { ProfileSetupGuard } from './core/guards/profile-setup.guard';
import { map } from 'rxjs/operators';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [HomeGuard],
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [HomeGuard],
  },
  {
    path: 'sign-up',
    component: SignUpComponent,
  },
  {
    path: 'sign-in',
    component: SignInComponent,
  },
  {
    path: 'profile-setup',
    component: ProfileSetupComponent,
    canActivate: [ProfileSetupGuard],
  },
  {
    path: '**', //any path that doesnt match anything
    redirectTo: '', //redirect to --> home
  },
];
