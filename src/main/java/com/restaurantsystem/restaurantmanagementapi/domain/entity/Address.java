package com.restaurantsystem.restaurantmanagementapi.domain.entity;

import java.util.Objects;

public final class Address {

    private final String street;
    private final String number;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String zipCode;
    private final String complement;

    public Address(
            String street,
            String number,
            String neighborhood,
            String city,
            String state,
            String zipCode,
            String complement
    ) {
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.complement = complement;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getComplement() {
        return complement;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Address address)) {
            return false;
        }
        return Objects.equals(street, address.street)
                && Objects.equals(number, address.number)
                && Objects.equals(neighborhood, address.neighborhood)
                && Objects.equals(city, address.city)
                && Objects.equals(state, address.state)
                && Objects.equals(zipCode, address.zipCode)
                && Objects.equals(complement, address.complement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, number, neighborhood, city, state, zipCode, complement);
    }
}
