package com.EcommerceShop.Shop.service.ServiceImpl;

import com.EcommerceShop.Shop.entity.Address;
import com.EcommerceShop.Shop.repository.AddressRepository;
import com.EcommerceShop.Shop.dto.request.AddressRequest;
import com.EcommerceShop.Shop.dto.response.AddressResponse;
import com.EcommerceShop.Shop.mapper.AddressMapper;
import com.EcommerceShop.Shop.exception.AppException;
import com.EcommerceShop.Shop.enums.ErrorCode;
import com.EcommerceShop.Shop.entity.User;
import com.EcommerceShop.Shop.repository.UserRepository;
import com.EcommerceShop.Shop.service.AddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    AddressRepository addressRepository ;
    UserRepository userRepository ;
    AddressMapper addressMapper ;
    public AddressResponse create(String userId, AddressRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)) ;
        Address address = addressMapper.toAddress(request) ;
        if(request.getIsDefault() == null){
            address.setIsDefault(false);
        }
        address.setUser(user);
        user.getAddresses().add(address) ;
        return addressMapper.toAddressResponse(addressRepository.save(address));
    }

    public List<AddressResponse> getAll(String userId){
        return userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)).getAddresses().stream()
                .map(addressMapper::toAddressResponse).toList() ;
    }

    public AddressResponse update(String userId, Long addressId, AddressRequest request){
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_ID_WRONG)) ;
        addressMapper.update(address,request);
        return addressMapper.toAddressResponse(addressRepository.save(address)) ;
    }

    public void delete(String userId, Long addressId){
        //?

        addressRepository.deleteById(addressId);
    }

}
