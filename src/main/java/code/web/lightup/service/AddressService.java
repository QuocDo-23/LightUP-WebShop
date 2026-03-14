package code.web.lightup.service;

import code.web.lightup.dao.AddressDAO;
import code.web.lightup.model.Address;

import java.util.List;

public class AddressService {
    private final AddressDAO addressDAO;

    public AddressService() {
        this.addressDAO = new AddressDAO();
    }

    public boolean insertAddress(Address address) {
        return addressDAO.insertAddress(address);
    }

    public Address getAddressById(int addressId) {
        return addressDAO.getAddressById(addressId);
    }

    public Address getDefaultAddress(int userId) {
        return addressDAO.getDefaultAddress(userId);
    }

    public boolean updateAddress(Address address) {
        return addressDAO.updateAddress(address);
    }

    public boolean deleteAddress(int addressId) {
        return addressDAO.deleteAddress(addressId);
    }

    public boolean unsetAllDefault(int userId) {
        return addressDAO.unsetAllDefault(userId);
    }

    public boolean setAsDefault(int addressId, int userId) {
        return addressDAO.setAsDefault(addressId, userId);
    }

    public List<Address> getAddressByUserId(int userId) {
        return addressDAO.getAddressByUserId(userId);
    }
}
