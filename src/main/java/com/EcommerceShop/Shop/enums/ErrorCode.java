package com.EcommerceShop.Shop.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_EXISTED(1001,"User not existed!", HttpStatus.NOT_FOUND),
    USER_EXISTED(1002,"User existed!", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003,"Unauthenticated! Xác thực thất bại", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1004,"Unauthorized! Bạn không có quyền thực hiện thao tác này!", HttpStatus.FORBIDDEN),
    BAD_REQUEST(1005,"Bad Request", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1006,"Invalid Token!", HttpStatus.BAD_REQUEST),
    CATEGORY_EXISTED(1007,"Category existed", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(1008,"Category not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(1009,"Product not found", HttpStatus.NOT_FOUND),
    ITEM_NOT_EXIST(1010,"Item not existed!", HttpStatus.NOT_FOUND),
    BRAND_NOT_FOUND(1011,"Brand not found!", HttpStatus.NOT_FOUND),
    ADDRESS_ID_WRONG(1012,"AddressId wrong!", HttpStatus.NOT_FOUND),
    CLOUDINARY_ERROR(1013,"Lỗi up load lên cloudinary", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(1014,"Lỗi không tìm thấy order", HttpStatus.NOT_FOUND),
    PASSWORD_WRONG(1015,"Password sai!", HttpStatus.BAD_REQUEST),
    CHUA_MUA_HANG(1016,"Bạn chưa mua hàng nên không thể đánh giá được", HttpStatus.BAD_REQUEST),
    DA_CO_FEEDBACK(1017,"Bạn đã có feedback cho product này rồi" , HttpStatus.BAD_REQUEST),
    FEEDBACK_KHONG_TON_TAI(1018,"Feedback không tồn tại", HttpStatus.BAD_REQUEST),
    SO_LUONG_SAN_PHAM_KHONG_DU(1019,"Số lượng sản phẩm không đủ", HttpStatus.BAD_REQUEST),
    VERIFY_COUNT_NOT_ENOUGH(1020,"Đã hết lượt verify-otp", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1021,"Otp không hợp lệ", HttpStatus.BAD_REQUEST),
    REGISTER_SESSION_NOT_FOUND(1022,"Thông tin đăng ký đã hết hiệu lực. Vui lòng đăng kí lại!", HttpStatus.NOT_FOUND) ,
    HET_THOI_GIAN_HIEU_LUC(1023,"Đã qua thời gian cho phép", HttpStatus.BAD_REQUEST) ,
    NOT_VERIFY(1024,"Chưa xác thực!", HttpStatus.BAD_REQUEST)
    ;
    ErrorCode(int code , String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus ;
    }

    int code ;
    String message;
    HttpStatus httpStatus ;
}
