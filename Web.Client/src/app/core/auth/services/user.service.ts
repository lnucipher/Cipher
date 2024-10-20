import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, throwError } from 'rxjs';

import { JwtService } from './jwt.service';
import {
  map,
  distinctUntilChanged,
  tap,
  shareReplay,
  catchError,
} from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { User } from '../user.model';
import { Router } from '@angular/router';

@Injectable({ providedIn: 'root' })
export class UserService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser = this.currentUserSubject
    .asObservable()
    .pipe(distinctUntilChanged());

  public isAuthenticated = this.currentUser.pipe(map((user) => !!user));

  constructor(
    private readonly http: HttpClient,
    private readonly jwtService: JwtService,
    private readonly router: Router
  ) {}

  //login method
  login(credentials: {
    username: string;
    password: string;
  }): Observable<{ user: User }> {
    return this.http
      .post<{ user: User }>('auth/signin', { user: credentials })
      .pipe(
        tap(({ user }) => this.setAuth(user)) // set authentication with the response user
      );
  }

  checkUsername(username: string): Observable<{ available: boolean }> {
    return this.http.post<{ available: boolean }>('auth/isUserExist', {
      username,
    });
  }

  register(userData: {
    username: string;
    password: string;
    displayName: string;
    birthDate: string;
    bio: string;
    avatarFile: File | null;
  }): Observable<{ user: User }> {
    const formData = new FormData();

    const requestBody = {
      username: userData.username,
      password: userData.password,
      displayName: userData.displayName,
      birthDate: userData.birthDate,
      bio: userData.bio,
    };
    formData.append('requestBody', JSON.stringify(requestBody));

    if (userData.avatarFile) {
      formData.append('avatarFile', userData.avatarFile);
    }

    return this.http.post<{ user: User }>('auth/signup', formData).pipe(
      tap(({ user }) => {
        this.setAuth(user); // Save the user data including the token
      })
    );
  }

  logout(): void {
    this.purgeAuth(); // clear authentication data (token and user info)
    void this.router.navigate(['/']); // navigate to the home page(without auth basically navigate to login)
  }

  getCurrentUser(): Observable<{ user: User }> {
    return this.http.get<{ user: User }>('/user').pipe(
      tap({
        next: ({ user }) => this.setAuth(user), // set the authentication state with the current user
        error: () => this.purgeAuth(), // clear authentication if error occurs
      }),
      shareReplay(1)
    );
  }

  update(user: Partial<User>): Observable<{ user: User }> {
    return this.http.put<{ user: User }>('/user', { user }).pipe(
      tap(({ user }) => {
        this.currentUserSubject.next(user); // update the current user state with the response user
      })
    );
  }

  setAuth(user: User): void {
    this.jwtService.saveToken(user.token); // save the token
    this.currentUserSubject.next({
      id: user.id,
      username: user.username,
      passwordHash: user.passwordHash || '', // ??maybe fix later
      token: user.token,
      name: user.name,
      bio: user.bio,
      avatarUrl: user.avatarUrl,
      status: user.status || 'offline', // offline as default status
      lastSeen: new Date(), // default value for lastSeen??
    });
  }

  purgeAuth(): void {
    this.jwtService.destroyToken(); // remove the token from localStorage
    this.currentUserSubject.next(null); // set the current user to null, effectively logging out
  }

  //passing the data from the signUp to profile-setup
  private formData1: any = {}; //temporarily store the data passed from the sign-up form
  setFormData1(data: any) {
    this.formData1 = data; // save sign-up form data
  }
  getFormData1() {
    return this.formData1; // retrieve stored sign-up data
  }

  //to check the signUp form completion for route guarding purpose
  private signUpCompleteSubject = new BehaviorSubject<boolean>(false);
  public isSignUpComplete = this.signUpCompleteSubject.asObservable(); // observable for sign-up completion
  markSignUpComplete() {
    this.signUpCompleteSubject.next(true); // mark sign-up as complete
  }
}
