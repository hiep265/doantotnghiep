import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common'; // Import CommonModule
import { AuthService } from '../auth/auth.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-home',
  standalone: true, // Đánh dấu là standalone
  imports: [
    CommonModule,
    RouterModule  // Đảm bảo RouterModule được import
  ],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  currentUser$: Observable<string | null>;

  constructor(
    private authService: AuthService,
    private router: Router  // Thêm Router
  ) {
    this.currentUser$ = this.authService.currentUser$;
  }

  ngOnInit(): void {
    console.log('HomeComponent loaded');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // Thêm phương thức điều hướng
  navigateToWishlist(): void {
    console.log('Navigating to wishlist...');
    this.router.navigate(['/wishlist']);
  }
}
