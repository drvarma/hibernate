package com.annotation.employee;

import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

public class EmployeeDriver {
	private static SessionFactory factory = null;
	public static void main(String[] args) {
		try{
			factory = new AnnotationConfiguration().configure().buildSessionFactory();
		}catch(Throwable e){
			System.err.println("Failed to create sesson");
			e.printStackTrace();
		}
		
		EmployeeDriver driver = new EmployeeDriver();
		Integer emp1 = driver.addEmployee("Raghu", "Varma", 4500);
		Integer emp2 = driver.addEmployee("Siddhartha", "Gadiraju", 5000);
		driver.listEmployee();
		driver.updateEmployeeLastName(emp2,"Gadhiraju");
		driver.updateEmployeeSalary(emp1,5500);
		driver.listEmployee();
		driver.deleteEmployee(emp2);
		driver.listEmployee();
		

	}
	public Integer addEmployee(String fName, String lName, int salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer empID = null;
		try{
			tx = session.beginTransaction();
			Employee emp = new Employee(fName, lName, salary);
			empID = (Integer) session.save(emp);
			tx.commit();
		}catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return empID;
	}
	public void listEmployee() {
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			List employees = session.createQuery("FROM EMPLOYEE").list();
			for(Iterator iterator = employees.iterator(); iterator.hasNext();){
				Employee emp = (Employee) iterator.next();
				System.out.println("Name: "+emp.getFirst_name()+" "+emp.getLast_name());
				System.out.println("Salary: "+emp.getSalary());
			}
			tx.commit();
		}catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		
	}
	public void updateEmployeeLastName(Integer empID, String lName) {
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Employee emp = (Employee)session.get(Employee.class, empID);
			emp.setLast_name(lName);
			session.update(emp);
			tx.commit();
		}catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		
	}
	public void updateEmployeeSalary(Integer empID, int sal) {
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Employee emp = (Employee)session.get(Employee.class, empID);
			emp.setSalary(sal);
			session.update(emp);
			tx.commit();
		}catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		
	}
	public void deleteEmployee(Integer empID) {
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			Employee emp = (Employee)session.get(Employee.class, empID);
			session.delete(emp);
			tx.commit();
		}catch(HibernateException e){
			if(tx!=null) tx.rollback();
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}

}
