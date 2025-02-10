package com.ecomapplication.Service;

import com.ecomapplication.Dto.AddressDTO;
import com.ecomapplication.Entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, User user) ;

    List<AddressDTO> getAddresses();

    AddressDTO getAddressesById(String addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(String addressId, AddressDTO addressDTO);

    String deleteAddress(String addressId);

}
