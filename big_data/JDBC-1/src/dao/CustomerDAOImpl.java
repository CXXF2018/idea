package dao;

import bean.Customers;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDAOImpl extends BaseDAO<Customers> implements CustomerDAO {
    @Override
    public void insert(Connection connection, Customers customer) {
        String sql="insert into customers(name,email,birth) values(?,?,?)";
        try {
            Update(connection,sql,customer.getName(),customer.getEmail(),customer.getBirth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql="delete from customers where id=?";
        try {
            Update(connection,sql,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Connection connection, Customers customer) {

        String sql="update customers set name=?,email=?,brith=? where=id=?";

        try {
            Update(connection,sql,customer.getName(),customer.getEmail(),customer.getBirth());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customers getCustomerById(Connection connection, int id) {
        String sql="select * from customers where id=?";
        Customers customers = getInstance(connection, sql, id);

        return customers;
    }

    @Override
    public List<Customers> getAll(Connection connection) {
        String sql="select * from customers";
        List<Customers> customersList = getForList(connection, sql);
        return customersList;
    }

    @Override
    public Long getCount(Connection connection) {

        String sql="select count(*) from customers";
        return getValue(connection, sql);

    }

    @Override
    public Date getMaxBirth(Connection connection) {

        String sql="select Max(birth) from customers";
        return getValue(connection,sql);

    }
}
