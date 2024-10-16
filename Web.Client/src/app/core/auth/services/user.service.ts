import { Injectable } from "@angular/core";
import { Observable, BehaviorSubject, throwError } from "rxjs";

import { JwtService } from "./jwt.service";
import { map, distinctUntilChanged, tap, shareReplay,catchError} from "rxjs/operators";
import { HttpClient } from "@angular/common/http";
import { User } from "../user.model";
import { Router } from "@angular/router";

@Injectable({ providedIn: "root" })
export class UserService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser = this.currentUserSubject
    .asObservable()
    .pipe(distinctUntilChanged());

  public isAuthenticated = this.currentUser.pipe(map((user) => !!user));

  constructor(
    private readonly http: HttpClient,
    private readonly jwtService: JwtService,
    private readonly router: Router,
  ) {}

  login(credentials: {
    username: string;
    password: string;
  }): Observable<{ user: User }> {
    return this.http
      .post<{ user: User }>("/users/signin", { user: credentials })
      .pipe(tap(({ user }) => this.setAuth(user)));
  }

  checkUsername(username: string): Observable<{ available: boolean }> {
    return this.http.post<{ available: boolean }>('/users/check-username', { username });
  }

  register(userData: {
    username: string;
    password: string;
    displayName: string;
    birthDate: string;
    avatarUrl: string;
    bio: string;
  }): Observable<{ user: User }> {
    return this.http
      .post<{ user: User }>("/users", { user: userData })
      .pipe(
        tap(({ user }) => this.setAuth(user)),
        catchError((error) => {
          if (error.status === 409) {

            return throwError(() => new Error('Username is already taken. Please choose another.'));
          }

          return throwError(() => new Error('Registration failed. Please try again.'));
        })
      );
  }

  logout(): void {
    this.purgeAuth();
    void this.router.navigate(["/"]);
  }

  getCurrentUser(): Observable<{ user: User }> {
    return this.http.get<{ user: User }>("/user").pipe(
      tap({
        next: ({ user }) => this.setAuth(user),
        error: () => this.purgeAuth(),
      }),
      shareReplay(1),
    );
  }

  update(user: Partial<User>): Observable<{ user: User }> {
    return this.http.put<{ user: User }>("/user", { user }).pipe(
      tap(({ user }) => {
        this.currentUserSubject.next(user);
      }),
    );
  }

  private formData1: any = {};

  setFormData1(data: any) {
    this.formData1 = data;
  }

  getFormData1() {
    return this.formData1;
  }


  setAuth(user: User): void {
    this.jwtService.saveToken(user.token);
    this.currentUserSubject.next(user);
  }

  purgeAuth(): void {
    this.jwtService.destroyToken();
    this.currentUserSubject.next(null);
  }

  private signUpCompleteSubject = new BehaviorSubject<boolean>(false);
  public isSignUpComplete = this.signUpCompleteSubject.asObservable();

  markSignUpComplete() {
    this.signUpCompleteSubject.next(true);
  }
}