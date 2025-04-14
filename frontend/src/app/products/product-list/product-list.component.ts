import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Subscription } from 'rxjs';
import { ProductService } from '../../services/product.service';
import { WishlistService } from '../../services/wishlist.service';
import { ProductListItem } from '../../shared/models/product.model';
import { WishlistItem } from '../../shared/models/wishlist.model';

@Component({
  selector: 'app-product-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule
  ],
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit, OnDestroy {
  products: ProductListItem[] = [];
  isLoading = true;
  error: string | null = null;
  private productSubscription?: Subscription;
  private wishlistSubscription?: Subscription;
  wishlistProductVariantIds: Set<number> = new Set();

  constructor(
    private productService: ProductService,
    private wishlistService: WishlistService
  ) { }

  ngOnInit(): void {
    console.log('ProductListComponent initialized');
    this.loadProducts();
    this.subscribeToWishlist();
  }

  ngOnDestroy(): void {
    if (this.productSubscription) {
      this.productSubscription.unsubscribe();
    }
    if (this.wishlistSubscription) {
      this.wishlistSubscription.unsubscribe();
    }
  }

  loadProducts(): void {
    this.isLoading = true;
    this.error = null;
    console.log('Loading products...');
    
    this.productSubscription = this.productService.getProducts().subscribe({
      next: (response) => {
        console.log('Products received:', response);
        this.products = response.data?.content || [];
        this.isLoading = false;
        if (this.products.length === 0) {
          console.log('No products found in data.content');
        }
      },
      error: (err) => {
        this.error = 'Failed to load products. Please try again later.';
        this.isLoading = false;
        console.error('Error loading products:', err);
      }
    });
  }

  subscribeToWishlist(): void {
    // Theo dõi thay đổi trong wishlist để cập nhật trạng thái nút yêu thích
    this.wishlistSubscription = this.wishlistService.wishlistItems$.subscribe((items: WishlistItem[]) => {
      this.wishlistProductVariantIds = new Set(items.map(item => item.product_variant_id));
      console.log('Wishlist updated in ProductList:', this.wishlistProductVariantIds);
    });
    // Đảm bảo wishlist đã được tải
    this.wishlistService.loadWishlist(); 
  }

  isInWishlist(product: ProductListItem): boolean {
    return product.primaryVariantId ? this.wishlistProductVariantIds.has(product.primaryVariantId) : false;
  }

  toggleWishlist(product: ProductListItem): void {
    if (!product.primaryVariantId) {
      console.warn('Cannot toggle wishlist: Product primaryVariantId is missing', product);
      return;
    }

    const variantId = product.primaryVariantId;
    if (this.isInWishlist(product)) {
      console.log('Removing from wishlist, variantId:', variantId);
      // Tìm đúng wishlist_item_id để xóa
      const wishlistItem = this.wishlistService.currentWishlistItems.find((item: WishlistItem) => item.product_variant_id === variantId);
      if (wishlistItem) {
        this.wishlistService.removeFromWishlist(wishlistItem.wishlist_item_id).subscribe({
          error: (err: any) => this.handleWishlistError(err, 'remove')
        });
      } else {
        console.error('Could not find wishlistItem to remove for variantId:', variantId);
      }
    } else {
      console.log('Adding to wishlist, variantId:', variantId);
      this.wishlistService.addToWishlist(variantId).subscribe({
        error: (err: any) => this.handleWishlistError(err, 'add')
      });
    }
  }
  
  handleWishlistError(error: any, action: 'add' | 'remove'): void {
      this.error = `Failed to ${action} item ${action === 'add' ? 'to' : 'from'} wishlist: ${error.message || 'Unknown error'}`;
      console.error(`Error ${action}ing wishlist:`, error);
  }
} 