import {CanActivateFn, Router} from '@angular/router';
import { AuthService } from './auth.service';
import { inject } from '@angular/core';
import {firstValueFrom} from "rxjs";
import {UserRole} from './user-role';
import {NavbarComponent} from '../components/navbar/navbar.component';

export const authGuard: CanActivateFn = (route, state) => {

  const authService = inject(AuthService);
  const router = inject(Router);
  const navbar = inject(NavbarComponent)

  const expectedRole:UserRole[] = route.data['requiredRoles']
  const expectedType:string[] = route.data['type']

  return firstValueFrom(authService.getUser())
    .then(user => {
      if(!user) {
        router.navigate([  "/" ]);
        navbar.openLoginDialog()
      }

      if (user) {
        if(!expectedRole && !expectedType){
          return true;
        }

        if(expectedRole && expectedRole[0]==user.authorities[0].authority){
          return true;
        }

        if(expectedType && expectedType[0] == sessionStorage.getItem('userRole')){
          return true;
        }

        router.navigate([  "/403" ]);
      }

      return false;
    })
    .catch(() => router.navigate([  "/" ]));
};
