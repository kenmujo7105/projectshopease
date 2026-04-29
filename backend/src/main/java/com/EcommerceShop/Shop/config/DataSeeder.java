package com.EcommerceShop.Shop.config;

import com.EcommerceShop.Shop.entity.Brand;
import com.EcommerceShop.Shop.entity.Category;
import com.EcommerceShop.Shop.entity.Product;
import com.EcommerceShop.Shop.entity.ProductCategory;
import com.EcommerceShop.Shop.entity.ProductDetail;
import com.EcommerceShop.Shop.repository.BrandRepository;
import com.EcommerceShop.Shop.repository.CategoryRepository;
import com.EcommerceShop.Shop.repository.ProductDetailRepository;
import com.EcommerceShop.Shop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * DataSeeder – tự động chèn dữ liệu mẫu vào database khi ứng dụng khởi động.
 *
 * Thứ tự seed để tránh vi phạm khoá ngoại:
 *   1. Brand  (thương hiệu)
 *   2. Category  (danh mục)
 *   3. Product + ProductDetail + ProductCategory
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final BrandRepository        brandRepository;
    private final CategoryRepository     categoryRepository;
    private final ProductRepository      productRepository;
    private final ProductDetailRepository productDetailRepository;

    @Bean
    @Transactional
    public CommandLineRunner seedDatabase() {
        return args -> {
            log.info("[DataSeeder] Kiểm tra và seed dữ liệu ban đầu...");

            // ─────────────────────────────────────────────
            // 1. Brands
            // ─────────────────────────────────────────────
            Brand apple   = saveBrand("Apple",   "Thương hiệu công nghệ cao cấp đến từ Mỹ, nổi tiếng với iPhone, MacBook và iPad.",
                                      "https://placehold.co/200x80/1d1d1f/ffffff?text=Apple");
            Brand samsung = saveBrand("Samsung", "Tập đoàn công nghệ đa quốc gia Hàn Quốc, dẫn đầu thị trường điện thoại Android.",
                                      "https://placehold.co/200x80/1428a0/ffffff?text=Samsung");
            Brand dell    = saveBrand("Dell",    "Nhà sản xuất máy tính xách tay và máy tính để bàn hàng đầu thế giới.",
                                      "https://placehold.co/200x80/007db8/ffffff?text=Dell");
            Brand sony    = saveBrand("Sony",    "Thương hiệu điện tử tiêu dùng Nhật Bản với sản phẩm âm thanh và hình ảnh đỉnh cao.",
                                      "https://placehold.co/200x80/000000/ffffff?text=Sony");
            Brand logitech = saveBrand("Logitech","Chuyên sản xuất thiết bị ngoại vi máy tính chất lượng cao như chuột, bàn phím và tai nghe.",
                                      "https://placehold.co/200x80/00b8d9/ffffff?text=Logitech");

            // ─────────────────────────────────────────────
            // 2. Categories
            // ─────────────────────────────────────────────
            Category smartphones = saveCategory("Điện Thoại Thông Minh",
                    "Điện thoại thông minh với hiệu năng cao và nhiều tính năng hiện đại.",
                    "https://placehold.co/400x300/6366f1/ffffff?text=Smartphones");
            Category laptops = saveCategory("Máy Tính Xách Tay",
                    "Laptop mỏng nhẹ, hiệu năng mạnh mẽ phục vụ công việc và giải trí.",
                    "https://placehold.co/400x300/0ea5e9/ffffff?text=Laptops");
            Category accessories = saveCategory("Phụ Kiện Công Nghệ",
                    "Tai nghe, bàn phím, chuột và các phụ kiện công nghệ đa dạng.",
                    "https://placehold.co/400x300/10b981/ffffff?text=Accessories");

            // ─────────────────────────────────────────────
            // 3. Products
            // ─────────────────────────────────────────────

            // ── Smartphones ─────────────────────────────
            createProduct(
                "iPhone 15 Pro Max",
                "iPhone 15 Pro Max trang bị chip A17 Pro mạnh mẽ, khung Titanium cao cấp, camera 48MP với zoom quang 5x và màn hình Super Retina XDR 6.7 inch. Trải nghiệm điện thoại thông minh đỉnh cao từ Apple.",
                "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=600&q=80",
                4.9, apple,
                List.of(
                    detail("256GB – Titanium Tự Nhiên",  34_990_000, 45),
                    detail("512GB – Titanium Đen",       39_990_000, 28),
                    detail("1TB  – Titanium Trắng",      44_990_000, 15)
                ),
                List.of(smartphones)
            );

            createProduct(
                "Samsung Galaxy S24 Ultra",
                "Galaxy S24 Ultra sở hữu bút S Pen tích hợp, chip Snapdragon 8 Gen 3, camera 200MP và màn hình Dynamic AMOLED 2X 6.8 inch với tần số quét 120Hz. Thiết bị Android cao cấp nhất từ Samsung.",
                "https://images.unsplash.com/photo-1706439136191-1e5f70fbf2fc?w=600&q=80",
                4.8, samsung,
                List.of(
                    detail("256GB – Titanium Gray",      29_990_000, 60),
                    detail("512GB – Titanium Black",     33_990_000, 35)
                ),
                List.of(smartphones)
            );

            createProduct(
                "iPhone 15",
                "iPhone 15 với chip A16 Bionic, Dynamic Island tiện lợi, camera chính 48MP và cổng USB-C hiện đại. Màn hình Super Retina XDR 6.1 inch sắc nét cho trải nghiệm xem phim và chơi game tuyệt vời.",
                "https://images.unsplash.com/photo-1692607431210-c1954f0b9ab7?w=600&q=80",
                4.7, apple,
                List.of(
                    detail("128GB – Hồng",               22_990_000, 80),
                    detail("256GB – Xanh",               25_990_000, 55)
                ),
                List.of(smartphones)
            );

            createProduct(
                "Samsung Galaxy A55 5G",
                "Galaxy A55 5G mang đến trải nghiệm tầm trung cao cấp với màn hình Super AMOLED 6.6 inch, camera 50MP, pin 5000mAh và khả năng kết nối 5G nhanh chóng. Thiết kế kim loại bền đẹp.",
                "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=600&q=80",
                4.5, samsung,
                List.of(
                    detail("128GB – Xanh Dương Tuyệt Vời", 10_990_000, 120),
                    detail("256GB – Tím Nhạt",             12_490_000, 70)
                ),
                List.of(smartphones)
            );

            // ── Laptops ──────────────────────────────────
            createProduct(
                "MacBook Pro M3 14 inch",
                "MacBook Pro M3 14 inch với chip Apple M3 thế hệ mới, màn hình Liquid Retina XDR 14.2 inch cùng thời lượng pin lên đến 18 giờ. Hiệu năng vượt trội cho lập trình viên, nhà thiết kế và các chuyên gia sáng tạo.",
                "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=600&q=80",
                4.9, apple,
                List.of(
                    detail("M3 / 8GB RAM / 512GB SSD – Bạc",    49_990_000, 20),
                    detail("M3 Pro / 18GB RAM / 512GB SSD – Đen Không Gian", 62_990_000, 12),
                    detail("M3 Max / 36GB RAM / 1TB SSD – Đen Không Gian",  89_990_000,  8)
                ),
                List.of(laptops)
            );

            createProduct(
                "Dell XPS 15 OLED",
                "Dell XPS 15 trang bị màn hình OLED 3.5K cảm ứng, bộ vi xử lý Intel Core i9 thế hệ 13, RAM 32GB và SSD 1TB. Laptop cao cấp lý tưởng cho những ai yêu cầu màu sắc chính xác và hiệu năng tối đa.",
                "https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=600&q=80",
                4.7, dell,
                List.of(
                    detail("i7 / 16GB / 512GB – Bạc",     39_990_000, 18),
                    detail("i9 / 32GB / 1TB – Đen Platinum", 54_990_000, 10)
                ),
                List.of(laptops)
            );

            createProduct(
                "MacBook Air M2 13 inch",
                "MacBook Air M2 với thiết kế siêu mỏng không quạt, chip Apple M2, màn hình Liquid Retina 13.6 inch và pin kéo dài đến 18 giờ. Lựa chọn hoàn hảo cho học sinh, sinh viên và chuyên gia năng động.",
                "https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=600&q=80",
                4.8, apple,
                List.of(
                    detail("M2 / 8GB / 256GB – Ánh Sao",   28_990_000, 30),
                    detail("M2 / 8GB / 512GB – Xám Không Gian", 33_990_000, 22),
                    detail("M2 / 16GB / 512GB – Bạc",     37_490_000, 15)
                ),
                List.of(laptops)
            );

            createProduct(
                "Dell Inspiron 15 3000",
                "Dell Inspiron 15 phù hợp cho sinh viên và gia đình với chip Intel Core i5 thế hệ 12, RAM 8GB, SSD 512GB và màn hình Full HD 15.6 inch chống chói. Cấu hình tốt, giá thành hợp lý.",
                "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=600&q=80",
                4.3, dell,
                List.of(
                    detail("i5 / 8GB / 512GB – Xanh Lam",  16_490_000, 50),
                    detail("i5 / 16GB / 512GB – Đen",      19_490_000, 35)
                ),
                List.of(laptops)
            );

            // ── Accessories ──────────────────────────────
            createProduct(
                "Sony WH-1000XM5",
                "Tai nghe chống ồn chủ động Sony WH-1000XM5 với 8 micro thu âm, chip QN1 tiên tiến giúp khử tiếng ồn xuất sắc, thời lượng pin 30 giờ và âm thanh LDAC chất lượng cao. Hỗ trợ kết nối đa thiết bị.",
                "https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=600&q=80",
                4.8, sony,
                List.of(
                    detail("Đen – Kết nối Bluetooth 5.2",  8_490_000, 40),
                    detail("Bạc – Kết nối Bluetooth 5.2",  8_490_000, 30)
                ),
                List.of(accessories)
            );

            createProduct(
                "Logitech MX Master 3S",
                "Chuột không dây Logitech MX Master 3S với cảm biến Darkfield 8000 DPI, bánh cuộn điện từ MagSpeed siêu êm, pin sạc USB-C kéo dài 70 ngày và kết nối đa thiết bị Bolt/Bluetooth.",
                "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600&q=80",
                4.7, logitech,
                List.of(
                    detail("Graphite – Receiver Bolt",     1_990_000, 75),
                    detail("Pale Grey – Receiver Bolt",    1_990_000, 60)
                ),
                List.of(accessories)
            );

            createProduct(
                "Apple AirPods Pro (Thế hệ 2)",
                "AirPods Pro Gen 2 với chip H2, chống ồn chủ động ANC thế hệ mới mạnh hơn 2x, chế độ Transparency tự nhiên và âm thanh Adaptive Audio thông minh. Hộp sạc MagSafe hỗ trợ sạc không dây.",
                "https://images.unsplash.com/photo-1600294037681-c80b4cb5b434?w=600&q=80",
                4.8, apple,
                List.of(
                    detail("Trắng – Hộp sạc MagSafe Lightning",  6_490_000, 55),
                    detail("Trắng – Hộp sạc MagSafe USB-C",      6_990_000, 45)
                ),
                List.of(accessories)
            );

            createProduct(
                "Logitech G Pro X TKL",
                "Bàn phím cơ gaming Logitech G Pro X TKL thiết kế TenKeyLess, switch GX Blue/Red có thể hoán đổi, đèn nền RGB LIGHTSYNC và khung nhôm cao cấp. Được các game thủ chuyên nghiệp tin dùng.",
                "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&q=80",
                4.6, logitech,
                List.of(
                    detail("GX Blue (Click) – Đen",        3_290_000, 50),
                    detail("GX Red (Linear) – Đen",        3_290_000, 45)
                ),
                List.of(accessories)
            );

            createProduct(
                "Sony WF-1000XM5",
                "Tai nghe true wireless Sony WF-1000XM5 nhỏ gọn nhất từ trước đến nay, chống ồn chủ động vượt trội với chip V2 và processor QN2e, âm thanh LDAC 990kbps không dây và thời lượng pin 36 giờ cùng hộp sạc.",
                "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600&q=80",
                4.7, sony,
                List.of(
                    detail("Đen – Bluetooth 5.3",          5_990_000, 35),
                    detail("Bạch Kim – Bluetooth 5.3",     5_990_000, 25)
                ),
                List.of(accessories)
            );

            log.info("[DataSeeder] ✅ Seed hoàn tất: {} sản phẩm đã được tạo.", productRepository.count());
        };
    }

    // ─────────────────────────────────────────────────────────────
    // Helper methods
    // ─────────────────────────────────────────────────────────────

    private Brand saveBrand(String name, String description, String logoUrl) {
        return brandRepository.findByName(name).orElseGet(() -> {
            Brand brand = Brand.builder()
                    .name(name)
                    .description(description)
                    .logoUrl(logoUrl)
                    .build();
            return brandRepository.save(brand);
        });
    }

    private Category saveCategory(String name, String description, String urlImage) {
        Category existing = categoryRepository.findByName(name);
        if (existing != null) return existing;
        Category category = Category.builder()
                .name(name)
                .description(description)
                .urlImage(urlImage)
                .build();
        return categoryRepository.save(category);
    }

    private ProductDetail detail(String info, double price, int quantity) {
        return ProductDetail.builder()
                .info(info)
                .price(price)
                .quantity(quantity)
                .build();
    }

    /**
     * Tạo sản phẩm hoàn chỉnh gồm các biến thể (ProductDetail) và liên kết danh mục (ProductCategory).
     */
    private void createProduct(String name,
                               String description,
                               String imageUrl,
                               double averageRate,
                               Brand brand,
                               List<ProductDetail> details,
                               List<Category> categories) {

        if (productRepository.existsByName(name)) {
            log.debug("[DataSeeder] Bỏ qua (đã tồn tại): {}", name);
            return;
        }
        Product product = Product.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .averageRate(averageRate)
                .brand(brand)
                .productDetails(new ArrayList<>())
                .productCategories(new ArrayList<>())
                .build();

        Product saved = productRepository.save(product);

        // ProductDetails – gán product sau khi product được lưu
        details.forEach(d -> {
            d.setProduct(saved);
            productDetailRepository.save(d);
        });

        // ProductCategory links
        categories.forEach(cat -> {
            ProductCategory pc = ProductCategory.builder()
                    .product(saved)
                    .category(cat)
                    .build();
            saved.getProductCategories().add(pc);
        });

        productRepository.save(saved);
    }
}
