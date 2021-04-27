package dao;

import bean.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public interface CustomerDAO {

    void insert(Connection connection, Customers customer);

    void deleteById(Connection connection,int id);

    void update(Connection connection,Customers customer);

    Customers getCustomerById(Connection connection,int id);

    List<Customers> getAll(Connection connection);

    Long getCount(Connection connection);

    Date getMaxBirth(Connection connection);


}
