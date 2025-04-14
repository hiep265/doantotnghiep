import { Product } from './product.model';

export interface Wishlist {
  id: number;
  userId: number;
  createdAt: Date;
}

export interface WishlistItem {
  wishlist_item_id: number;
  user_id: number;
  product_id: number;
  product_name: string;
  product_variant_id: number;
  price: number;
  stock_quantity: number;
  product_image_url?: string;
  color_name?: string;
  size_name?: string;
  added_at: Date;
}

export interface WishlistResponse {
  data: WishlistItem[];
} 