package ams.dev.api.barber_shop.enums;

public class ConstantsSQL {

    public static final String SELECT_CLIENT_EMAIL_AND_BARBERSHOP_ID = "Client.findByEmailAndBarbershopId";
    public static final String SELECT_CLIENT_EMAIL_AND_BARBERSHOP_AND_IDNOT = "Client.findByEmailAndBarbershopIdAndIdNot";
    public static final String SELECT_CLIENT_ID_AND_BARBERSHOP_ID = "Client.findByIdAndBarbershopId";
    public static final String SELECT_CLIENTS_BY_BARBERSHOP_ID = "Client.findAllByBarbershopId";
    public static final String SELECT_CLIENTS_WITH_FILTERS = "Client.findAllWithFilters";

}
