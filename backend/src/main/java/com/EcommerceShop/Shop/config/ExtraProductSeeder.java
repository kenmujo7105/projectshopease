package com.EcommerceShop.Shop.config;

import com.EcommerceShop.Shop.entity.*;
import com.EcommerceShop.Shop.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j @Configuration @RequiredArgsConstructor
public class ExtraProductSeeder {
    private final BrandRepository brandRepo;
    private final CategoryRepository catRepo;
    private final ProductRepository productRepo;
    private final ProductDetailRepository detailRepo;

    @Bean @Order(2) @Transactional
    public CommandLineRunner seedExtraProducts() {
        return args -> {
            Brand apple   = b("Apple",   "Apple Inc – iPhone, Mac, iPad.", "https://placehold.co/200x80/000/fff?text=Apple");
            Brand samsung = b("Samsung", "Samsung Electronics Hàn Quốc.", "https://placehold.co/200x80/1428a0/fff?text=Samsung");
            Brand asus    = b("ASUS",    "ASUS – laptop và gaming Đài Loan.", "https://placehold.co/200x80/1a1a2e/fff?text=ASUS");
            Brand xiaomi  = b("Xiaomi",  "Xiaomi – công nghệ Trung Quốc.", "https://placehold.co/200x80/ff6900/fff?text=Xiaomi");
            Brand lenovo  = b("Lenovo",  "Lenovo – ThinkPad và Legion.", "https://placehold.co/200x80/e2231a/fff?text=Lenovo");
            Brand google  = b("Google",  "Google – Pixel và Wear OS.", "https://placehold.co/200x80/4285f4/fff?text=Google");
            Brand razer   = b("Razer",   "Razer – gaming cao cấp.", "https://placehold.co/200x80/00c805/000?text=Razer");
            Brand msi     = b("MSI",     "MSI – gaming và màn hình.", "https://placehold.co/200x80/cc0000/fff?text=MSI");
            Brand lg      = b("LG",      "LG Electronics – OLED display.", "https://placehold.co/200x80/a50034/fff?text=LG");
            Brand logitech= b("Logitech","Logitech – chuột, bàn phím.", "https://placehold.co/200x80/00b8d9/fff?text=Logitech");

            Category phones   = c("Điện Thoại Thông Minh","Smartphone Android và iOS.","https://placehold.co/400x300/6366f1/fff?text=Phones");
            Category laptops  = c("Máy Tính Xách Tay","Laptop văn phòng và gaming.","https://placehold.co/400x300/0ea5e9/fff?text=Laptops");
            Category acc      = c("Phụ Kiện Công Nghệ","Phụ kiện và thiết bị ngoại vi.","https://placehold.co/400x300/10b981/fff?text=Accessories");
            Category tablets  = c("Máy Tính Bảng","Tablet cho công việc và giải trí.","https://placehold.co/400x300/8b5cf6/fff?text=Tablets");
            Category watches  = c("Đồng Hồ Thông Minh","Smartwatch theo dõi sức khỏe.","https://placehold.co/400x300/f59e0b/fff?text=Watches");
            Category monitors = c("Màn Hình","Màn hình gaming và văn phòng.","https://placehold.co/400x300/ef4444/fff?text=Monitors");
            Category gaming   = c("Thiết Bị Gaming","Chuột, bàn phím, tai nghe gaming.","https://placehold.co/400x300/7c3aed/fff?text=Gaming");

            // Tablets (7)
            p("iPad Pro M4 11 inch","Chip M4, màn hình Ultra Retina XDR OLED 11 inch, ProMotion 120Hz, siêu mỏng 5.3mm.","https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600",4.9,apple,List.of(d("256GB Wi-Fi – Bạc",27_990_000,20),d("512GB 5G – Đen",36_990_000,10)),List.of(tablets));
            p("iPad Pro M4 13 inch","Chip M4, màn hình OLED tandem 13 inch, mỏng nhất lịch sử iPad, hiệu năng vượt trội.","https://images.unsplash.com/photo-1632406588434-d5ebdb3eb3b3?w=600",4.9,apple,List.of(d("256GB Wi-Fi – Bạc",37_990_000,12),d("1TB 5G – Đen",58_990_000,6)),List.of(tablets));
            p("iPad Air M2 11 inch","Chip M2, Liquid Retina 11 inch, hỗ trợ Apple Pencil Pro và Magic Keyboard Folio.","https://images.unsplash.com/photo-1547954575-855750c57bd3?w=600",4.7,apple,List.of(d("128GB Wi-Fi – Tím",18_990_000,28),d("256GB 5G – Xanh",24_990_000,14)),List.of(tablets));
            p("Samsung Galaxy Tab S9 Ultra","AMOLED 14.6 inch, S Pen tích hợp, Snapdragon 8 Gen 2, IP68, pin 11200mAh.","https://images.unsplash.com/photo-1608303588026-884930af2559?w=600",4.8,samsung,List.of(d("256GB Wi-Fi – Graphite",27_990_000,12),d("512GB 5G – Beige",36_990_000,7)),List.of(tablets));
            p("Samsung Galaxy Tab S9 FE","Exynos 1380, màn hình 10.9 inch, pin 8000mAh, kháng nước IP68.","https://images.unsplash.com/photo-1585790050230-5dd28404ccb9?w=600",4.4,samsung,List.of(d("128GB Wi-Fi – Bạc",10_990_000,40),d("256GB – Mint",13_490_000,22)),List.of(tablets));
            p("Xiaomi Pad 6 Pro","Snapdragon 8+ Gen 1, màn hình 11 inch 144Hz, loa 4 chiều Dolby Atmos.","https://images.unsplash.com/photo-1589739900266-43b2843f4c12?w=600",4.5,xiaomi,List.of(d("256GB – Đen",11_990_000,30),d("512GB – Vàng",14_990_000,14)),List.of(tablets));
            p("Lenovo Tab P12 Pro","AMOLED 12.6 inch 2K, Snapdragon 870, Precision Pen 3, âm thanh JBL 4 loa.","https://images.unsplash.com/photo-1561154464-82e9adf32764?w=600",4.3,lenovo,List.of(d("256GB Wi-Fi – Xám",15_990_000,15),d("256GB 5G – Đen",18_990_000,8)),List.of(tablets));

            // Smartwatches (6)
            p("Apple Watch Series 9 45mm","Chip S9 SiP, màn hình 2000 nit, tính năng Double Tap, sạc từ tính nhanh.","https://images.unsplash.com/photo-1551816230-ef5deaed4a26?w=600",4.8,apple,List.of(d("Nhôm Midnight – GPS",10_990_000,35),d("Nhôm Starlight – GPS+Cell",13_490_000,20)),List.of(watches));
            p("Apple Watch Ultra 2","Vỏ Titanium 49mm, pin 60 giờ, màn hình 3000 nit, chống nước 100m, GPS L1/L5.","https://images.unsplash.com/photo-1717610394492-f252c5cf19c6?w=600",4.9,apple,List.of(d("49mm Titanium – Alpine Loop Indigo",23_990_000,12)),List.of(watches));
            p("Samsung Galaxy Watch 6 Classic 47mm","Vòng bezel xoay cơ học, Super AMOLED 1.5 inch, đo ECG và huyết áp.","https://images.unsplash.com/photo-1579586337278-3befd40fd17a?w=600",4.6,samsung,List.of(d("47mm – Đen",10_490_000,25),d("47mm – Bạc",10_490_000,18)),List.of(watches));
            p("Samsung Galaxy Watch 6 44mm","Super AMOLED tròn, cảm biến BioActive Plus, theo dõi giấc ngủ nâng cao.","https://images.unsplash.com/photo-1617043786394-f977fa12eddf?w=600",4.5,samsung,List.of(d("44mm – Graphite",7_490_000,38),d("44mm – Gold",7_490_000,28)),List.of(watches));
            p("Google Pixel Watch 3 45mm","Wear OS 4, Fitbit Advanced, Snapdragon W5+, AMOLED tròn, phát hiện tai nạn.","https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?w=600",4.5,google,List.of(d("45mm – Obsidian",11_990_000,18),d("45mm – Porcelain",11_990_000,12)),List.of(watches));
            p("Xiaomi Watch S3","AMOLED 1.43 inch, HyperOS, bezel kim loại thay được, GPS đa hệ thống.","https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600",4.3,xiaomi,List.of(d("Đen – Dây Fluororubber",3_490_000,50),d("Bạc – Dây Milanese",3_990_000,35)),List.of(watches));

            // Monitors (9)
            p("LG UltraGear 27GP850-B","27 inch Nano IPS QHD 165Hz, 1ms GtG, G-Sync Compatible, HDR400.","https://images.unsplash.com/photo-1616763355548-1b606f439f86?w=600",4.7,lg,List.of(d("27\" QHD 165Hz – Đen",10_990_000,22)),List.of(monitors));
            p("LG UltraFine 32UN880-B","32 inch 4K IPS, chân Ergo, USB-C 96W, sRGB 99%, thiết kế tối giản.","https://images.unsplash.com/photo-1547082299-de196ea013d6?w=600",4.6,lg,List.of(d("32\" 4K UHD 60Hz – Đen",15_990_000,10)),List.of(monitors));
            p("ASUS ROG Swift PG27AQN","27 inch Fast IPS QHD 360Hz, G-Sync Ultimate, HDR600, 1ms GTG.","https://images.unsplash.com/photo-1585792180666-f7347c490ee2?w=600",4.8,asus,List.of(d("27\" 2560x1440 360Hz",18_990_000,12)),List.of(monitors));
            p("ASUS ProArt PA32UCX-PK","32 inch Mini LED 4K, 99% DCI-P3, Delta E<1, USB-C 96W, HDR1400.","https://images.unsplash.com/photo-1527443224154-c4a573d91440?w=600",4.9,asus,List.of(d("32\" 4K 120Hz HDR1400",53_990_000,5)),List.of(monitors));
            p("Dell UltraSharp U2723QE","27 inch IPS Black 4K, hub USB-C 90W, Delta E<2, chống phản chiếu.","https://images.unsplash.com/photo-1587202372634-32705e3bf49c?w=600",4.7,lg,List.of(d("27\" 3840x2160 60Hz",17_990_000,14)),List.of(monitors));
            p("Samsung Odyssey G9 OLED 49\"","OLED cong 49 inch DQHD 240Hz, AMD FreeSync Premium Pro, HDR True Black.","https://images.unsplash.com/photo-1540103711724-ebf833bde8d1?w=600",4.8,samsung,List.of(d("49\" 5120x1440 240Hz",48_990_000,5)),List.of(monitors));
            p("Samsung Smart Monitor M8 32\"","32 inch 4K Smart TV tích hợp, webcam SlimFit 4K, USB-C, AirPlay 2.","https://images.unsplash.com/photo-1593640408182-31c228f8b1d7?w=600",4.5,samsung,List.of(d("32\" 4K 60Hz – Trắng",12_990_000,18),d("32\" 4K 60Hz – Đen",12_990_000,15)),List.of(monitors));
            p("MSI MAG274QRF-QD","27 inch QD-IPS QHD 165Hz, 95% DCI-P3, FreeSync Premium, HDR400.","https://images.unsplash.com/photo-1592890288564-76628a30a657?w=600",4.6,msi,List.of(d("27\" 2560x1440 165Hz",8_990_000,25)),List.of(monitors));
            p("MSI MEG 342C QD-OLED","34 inch QD-OLED cong UWQHD 175Hz, sRGB 99.3%, USB-C 90W, KVM.","https://images.unsplash.com/photo-1524657738410-a49b00a42696?w=600",4.7,msi,List.of(d("34\" 3440x1440 175Hz",24_990_000,8)),List.of(monitors));

            // Gaming Gear (9)
            p("Razer DeathAdder V3 Pro","Chuột gaming không dây siêu nhẹ 63g, Focus Pro 30K DPI, pin 90 giờ.","https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600",4.8,razer,List.of(d("Đen – Wireless",3_490_000,40),d("Trắng – Wireless",3_490_000,30)),List.of(gaming));
            p("Razer BlackWidow V4 Pro","Bàn phím cơ full-size, Razer Yellow switch, RGB Chroma, không dây đa chế độ.","https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600",4.7,razer,List.of(d("Full-size Đen – Yellow Switch",4_990_000,20)),List.of(gaming));
            p("Razer Kraken V3 HyperSense","Tai nghe gaming USB, cảm ứng xúc giác HyperSense, THX Spatial 7.1.","https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=600",4.5,razer,List.of(d("Over-ear Đen – USB",3_190_000,30)),List.of(gaming));
            p("Razer Basilisk V3 Pro","11 nút lập trình, HyperScroll Pro, Focus Pro 30K DPI, không dây đa chế độ.","https://images.unsplash.com/photo-1605289982774-9a6fef564df8?w=600",4.6,razer,List.of(d("Đen – Wireless+Bluetooth",3_790_000,35)),List.of(gaming));
            p("Logitech G502 X Plus","LIGHTFORCE hybrid switch, HERO 25K DPI, LIGHTSPEED 130 giờ, RGB.","https://images.unsplash.com/photo-1629429408209-1f912961dbd8?w=600",4.7,logitech,List.of(d("Đen – LIGHTSPEED",3_290_000,45),d("Trắng – LIGHTSPEED",3_290_000,35)),List.of(gaming));
            p("Logitech G Pro X Superlight 2","Chuột 60g siêu nhẹ, HERO 2 32K DPI, LIGHTSPEED không dây, pin 95 giờ.","https://images.unsplash.com/photo-1601787834168-cb4c89bc3ef0?w=600",4.8,logitech,List.of(d("Đen – LIGHTSPEED",3_990_000,30),d("Trắng – LIGHTSPEED",3_990_000,20)),List.of(gaming));
            p("Logitech G733 Lightspeed","Tai nghe gaming không dây nhẹ, Blue VO!CE mic, RGB LIGHTSYNC, 29h pin.","https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=600",4.5,logitech,List.of(d("Trắng – 2.4GHz Wireless",2_990_000,40),d("Xanh – 2.4GHz Wireless",2_990_000,30)),List.of(gaming));
            p("MSI Clutch GM51 Lightweight Wireless","59g siêu nhẹ, PixArt PAW3395 26K DPI, sạc Qi không dây, đa kết nối.","https://images.unsplash.com/photo-1583394838336-acd977736f90?w=600",4.4,msi,List.of(d("Đen – Wireless",2_490_000,35)),List.of(gaming));
            p("ASUS ROG Strix Scope II 96 Wireless","Bàn phím 96% compact, ROG NX switch, ba chế độ kết nối BT/2.4GHz/USB-C.","https://images.unsplash.com/photo-1591370874773-6702e8f12fd8?w=600",4.6,asus,List.of(d("Đen – ROG NX Red",3_590_000,22),d("Đen – ROG NX Blue",3_590_000,18)),List.of(gaming));

            // More Smartphones (6)
            p("Xiaomi 14 Pro","Snapdragon 8 Gen 3, camera Leica 50MP, sạc 120W, LTPO AMOLED 120Hz.","https://images.unsplash.com/photo-1598327105026-f552a5f26a92?w=600",4.7,xiaomi,List.of(d("256GB – Đen",22_990_000,40),d("512GB – Trắng",26_990_000,25)),List.of(phones));
            p("Xiaomi Redmi Note 13 Pro+ 5G","Camera 200MP, AMOLED 1.5K 120Hz, Dimensity 7200 Ultra, sạc 120W, IP68.","https://images.unsplash.com/photo-1598965675045-45c5e72c7d05?w=600",4.4,xiaomi,List.of(d("256GB – Tím",9_490_000,90),d("512GB – Đen",11_490_000,55)),List.of(phones));
            p("Google Pixel 8 Pro","Tensor G3, camera 50MP AI Pro, LTPO OLED 120Hz, 7 năm cập nhật Android.","https://images.unsplash.com/photo-1598964402285-5e4dd7adabe3?w=600",4.7,google,List.of(d("128GB – Bay",27_990_000,28),d("256GB – Obsidian",31_990_000,18)),List.of(phones));
            p("Samsung Galaxy Z Fold 5","Màn hình gập 7.6 inch, Snapdragon 8 Gen 2, bản lề Flex Hinge cải tiến.","https://images.unsplash.com/photo-1678741118547-a02b62e5ea44?w=600",4.7,samsung,List.of(d("256GB – Phantom Black",41_990_000,18),d("512GB – Cream",47_990_000,10)),List.of(phones));
            p("Samsung Galaxy A35 5G","Exynos 1380, Super AMOLED 6.6 inch 120Hz, camera 50MP, kháng nước IP67.","https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=600",4.3,samsung,List.of(d("128GB – Xanh Awesome",9_490_000,100),d("256GB – Tím",10_990_000,65)),List.of(phones));
            p("iPhone 14 Plus","A15 Bionic, màn hình Super Retina XDR 6.7 inch, pin 26 giờ, camera 12MP.","https://images.unsplash.com/photo-1663499482523-1c0c1bae4ce1?w=600",4.5,apple,List.of(d("128GB – Đêm Đen",22_990_000,50),d("256GB – Tím",26_990_000,35)),List.of(phones));

            // More Laptops (5)
            p("ASUS ROG Zephyrus G14 2024","Ryzen AI 9 HX, RTX 4070, OLED 2.8K 120Hz, 1.65kg, AniMe Matrix LED.","https://images.unsplash.com/photo-1593642632315-2192947a7858?w=600",4.8,asus,List.of(d("R9/32GB/1TB – Eclipse Gray",45_990_000,10),d("R9/32GB/1TB – Platinum White",45_990_000,7)),List.of(laptops));
            p("Lenovo ThinkPad X1 Carbon Gen 12","1.09kg, Intel Core Ultra 7, IPS 2.8K OLED tùy chọn, chuẩn MIL-SPEC.","https://images.unsplash.com/photo-1593642533144-3d62aa4783ec?w=600",4.7,lenovo,List.of(d("Ultra 7/32GB/1TB – Đen",49_990_000,8)),List.of(laptops));
            p("Lenovo Legion 5 Pro Gen 8","Ryzen 7 7745HX, RTX 4070, 16 inch QHD 240Hz, MUX Switch, tản nhiệt Legion.","https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=600",4.6,lenovo,List.of(d("R7/16GB/1TB – Storm Grey",29_990_000,14),d("R7/32GB/1TB – Storm Grey",35_990_000,8)),List.of(laptops));
            p("MSI Titan GT77 HX","i9-13980HX, RTX 4090 175W, 17.3 inch 4K 144Hz, bàn phím cơ Cherry MX.","https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=600",4.6,msi,List.of(d("i9/64GB/4TB – Đen",109_990_000,3)),List.of(laptops));
            p("HP Spectre x360 14","2-in-1 OLED 2.8K cảm ứng, Intel Core Ultra 7, bút MPP 2.0 tặng kèm.","https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600",4.7,google,List.of(d("Ultra 7/16GB/1TB – Nightfall Black",38_990_000,10),d("Ultra 7/32GB/2TB – Natural Silver",46_990_000,6)),List.of(laptops));

            // Accessories (3)
            p("Apple Magic Keyboard với Touch ID","Bàn phím không dây Bluetooth, có Touch ID, pin sạc USB-C, layout tiếng Việt.","https://images.unsplash.com/photo-1541140532154-b024d705b90a?w=600",4.5,apple,List.of(d("Bạc – Bluetooth",2_990_000,40),d("Đen Không Gian – Bluetooth",3_290_000,25)),List.of(acc));
            p("Logitech MX Keys S","Bàn phím không dây thông minh, backlight tự động, kết nối 3 thiết bị.","https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600",4.6,logitech,List.of(d("Graphite – LOGI BOLT",2_490_000,45),d("Pale Grey – LOGI BOLT",2_490_000,35)),List.of(acc));
            p("Anker 737 GaNPrime 120W","Sạc nhanh GaN 120W 3 cổng (2×USB-C + 1×USB-A), nhỏ gọn, hỗ trợ PPS.","https://images.unsplash.com/photo-1583394838336-acd977736f90?w=600",4.6,razer,List.of(d("Đen – 120W GaN",1_290_000,80),d("Trắng – 120W GaN",1_290_000,60)),List.of(acc));

            log.info("[ExtraProductSeeder] Hoàn tất – tổng sản phẩm: {}", productRepo.count());
        };
    }

    private Brand b(String name, String desc, String logo) {
        return brandRepo.findByName(name).orElseGet(() ->
            brandRepo.save(Brand.builder().name(name).description(desc).logoUrl(logo).build()));
    }

    private Category c(String name, String desc, String img) {
        Category ex = catRepo.findByName(name);
        if (ex != null) return ex;
        return catRepo.save(Category.builder().name(name).description(desc).urlImage(img).build());
    }

    private ProductDetail d(String info, double price, int qty) {
        return ProductDetail.builder().info(info).price(price).quantity(qty).build();
    }

    private void p(String name, String desc, String img, double rate, Brand brand,
                   List<ProductDetail> details, List<Category> cats) {
        if (productRepo.existsByName(name)) {
            log.debug("[ExtraProductSeeder] Bỏ qua (đã tồn tại): {}", name);
            return;
        }
        Product saved = productRepo.save(Product.builder()
                .name(name).description(desc).imageUrl(img).averageRate(rate).brand(brand)
                .productDetails(new ArrayList<>()).productCategories(new ArrayList<>()).build());
        details.forEach(dt -> { dt.setProduct(saved); detailRepo.save(dt); });
        cats.forEach(cat -> saved.getProductCategories().add(
                ProductCategory.builder().product(saved).category(cat).build()));
        productRepo.save(saved);
    }
}
