import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { WishlistService } from '../services/wishlist.service';
import { WishlistItem } from '../shared/models/wishlist.model';
import { Subscription } from 'rxjs';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit, OnDestroy {
  wishlistItems: WishlistItem[] = [];
  isLoading = true;
  error: string | null = null;
  private wishlistSubscription?: Subscription;
  private isAuthenticated = false;

  constructor(
    private wishlistService: WishlistService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    console.log('Wishlist component initialized');
    // Kiểm tra xác thực
    this.isAuthenticated = !!this.authService.getToken();
    if (!this.isAuthenticated) {
      console.log('User không được xác thực, chuyển hướng đến trang login');
      this.router.navigate(['/login']);
      return;
    }
    
    this.loadWishlist();
  }

  ngOnDestroy(): void {
    // Clean up subscription when component is destroyed
    if (this.wishlistSubscription) {
      this.wishlistSubscription.unsubscribe();
    }
  }

  loadWishlist(): void {
    this.isLoading = true;
    this.error = null;
    console.log('Loading wishlist data...');
    
    // Unsubscribe from previous subscription if it exists
    if (this.wishlistSubscription) {
      this.wishlistSubscription.unsubscribe();
    }
    
    this.wishlistSubscription = this.wishlistService.wishlistItems$.subscribe({
      next: (items) => {
        console.log('Wishlist items received in component:', items);
        this.wishlistItems = items;
        this.isLoading = false;
        
        if (!items || items.length === 0) {
          console.log('Wishlist empty or invalid items structure');
        }
      },
      error: (err) => {
        this.error = 'Failed to load wishlist. Please try again later.';
        this.isLoading = false;
        console.error('Error loading wishlist in component:', err);
        
        // If error related to authentication, redirect to login
        if (err.message && (
          err.message.toLowerCase().includes('login') || 
          err.message.toLowerCase().includes('session') || 
          err.message.toLowerCase().includes('token') || 
          err.message.toLowerCase().includes('unauthorized')
        )) {
          this.router.navigate(['/login']);
        }
      }
    });
    // Force trigger the load from service, in case the BehaviorSubject didn't update initially
    this.wishlistService.loadWishlist();
  }

  removeFromWishlist(wishlistItemId: number): void {
    console.log('Removing item from wishlist, id:', wishlistItemId);
    this.wishlistService.removeFromWishlist(wishlistItemId).subscribe({
      next: (response) => {
        console.log('Item removed successfully:', response);
        // After successful removal, the service will refresh the wishlist (already handled in service)
      },
      error: (err) => {
        this.error = `Failed to remove item: ${err.message}`;
        console.error('Error removing from wishlist:', err);
        
        // If error related to authentication, redirect to login
        if (err.message && (
          err.message.toLowerCase().includes('login') || 
          err.message.toLowerCase().includes('session') || 
          err.message.toLowerCase().includes('token') || 
          err.message.toLowerCase().includes('unauthorized')
        )) {
          this.router.navigate(['/login']);
        }
      }
    });
  }
  
  // Thêm phương thức để chuyển hướng đến trang sản phẩm
  navigateToProducts(): void {
    this.router.navigate(['/products']);
  }
} 