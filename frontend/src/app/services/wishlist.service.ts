import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, of, catchError, tap, map, throwError } from 'rxjs';
import { environment } from '../../enviroments/enviroment';
import { Product, ProductVariant } from '../shared/models/product.model';
import { WishlistItem, WishlistResponse } from '../shared/models/wishlist.model';
import { AuthService } from '../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class WishlistService {
  private apiUrl = `${environment.apiUrl}/wishlists`;
  
  // Store wishlist items in BehaviorSubject for real-time updates across components
  private wishlistItems = new BehaviorSubject<WishlistItem[]>([]);
  wishlistItems$ = this.wishlistItems.asObservable();

  // Getter công khai cho giá trị hiện tại của wishlist
  public get currentWishlistItems(): WishlistItem[] {
    return this.wishlistItems.value;
  }

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) {
    // Initially load wishlist items
    this.loadWishlist();
  }

  // Get all wishlist items for current user
  loadWishlist(): void {
    console.log('Fetching wishlist from:', this.apiUrl);
    
    // Kiểm tra xem user đã đăng nhập chưa
    if (!this.authService.getToken()) {
      console.log('Token không tồn tại, không thể tải wishlist');
      this.wishlistItems.next([]);
      return;
    }
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
    
    this.http.get<WishlistResponse>(this.apiUrl, { headers })
      .pipe(
        tap(response => console.log('Wishlist API response:', response)),
        catchError(error => {
          console.error('Error fetching wishlist:', error);
          // Nếu là lỗi 401, có thể là token đã hết hạn
          if (error instanceof HttpErrorResponse && error.status === 401) {
            console.log('Token đã hết hạn hoặc không hợp lệ');
            // Có thể chuyển hướng về trang login ở đây
          }
          return of({ data: [] });
        })
      )
      .subscribe(response => {
        const items = response.data || [];
        console.log('Wishlist items processed:', items);
        this.wishlistItems.next(items);
      });
  }

  // Add product variant to wishlist
  addToWishlist(productVariantId: number): Observable<any> {
    console.log('Adding product variant to wishlist:', productVariantId);
    
    // Kiểm tra token
    if (!this.authService.getToken()) {
      return throwError(() => new Error('Không có quyền truy cập. Vui lòng đăng nhập.'));
    }
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
    
    return this.http.post(this.apiUrl, { productVariantId }, { headers })
      .pipe(
        tap(response => {
          console.log('Add to wishlist response:', response);
          this.loadWishlist(); // Reload wishlist after adding
        }),
        catchError(error => {
          console.error('Error adding to wishlist:', error);
          let errorMessage = 'Đã xảy ra lỗi khi thêm vào danh sách yêu thích';
          
          if (error instanceof HttpErrorResponse) {
            if (error.status === 401) {
              errorMessage = 'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.';
            } else if (error.status === 409) {
              errorMessage = 'Sản phẩm đã có trong danh sách yêu thích.';
            }
          }
          
          return throwError(() => new Error(errorMessage));
        })
      );
  }

  // Remove product from wishlist
  removeFromWishlist(wishlistItemId: number): Observable<any> {
    console.log('Removing item from wishlist:', wishlistItemId);
    
    // Kiểm tra token
    if (!this.authService.getToken()) {
      return throwError(() => new Error('Không có quyền truy cập. Vui lòng đăng nhập.'));
    }
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    });
    
    return this.http.delete(`${this.apiUrl}/${wishlistItemId}`, { headers })
      .pipe(
        tap(response => {
          console.log('Remove from wishlist response:', response);
          this.loadWishlist(); // Reload wishlist after removing
        }),
        catchError(error => {
          console.error('Error removing from wishlist:', error);
          let errorMessage = 'Đã xảy ra lỗi khi xóa khỏi danh sách yêu thích';
          
          if (error instanceof HttpErrorResponse) {
            if (error.status === 401) {
              errorMessage = 'Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.';
            } else if (error.status === 404) {
              errorMessage = 'Không tìm thấy sản phẩm trong danh sách yêu thích.';
              // Nếu sản phẩm không tồn tại, vẫn nên tải lại danh sách
              this.loadWishlist();
            }
          }
          
          return throwError(() => new Error(errorMessage));
        })
      );
  }

  // Check if product variant is in wishlist
  isInWishlist(productVariantId: number): boolean {
    const items = this.wishlistItems.value;
    return items.some(item => item.product_variant_id === productVariantId);
  }
} 