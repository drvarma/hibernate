import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ManageEmployee {
	private static SessionFactory factory;
	Session session = factory.openSession();
	Transaction tx = null;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable e) {
			System.err.println("Failed to create sessionfactoery object" + e);
			throw new ExceptionInInitializerError();
		}

		ManageEmployee me = new ManageEmployee();

		HashMap map3 = new HashMap();
		map3.put("Masters", new Certificate("MS"));
		map3.put("Bachlors", new Certificate("BTech"));
		Integer empID3 = me.addEmployee("Raghu", "varma", 6500, map3);

		HashMap map1 = new HashMap();
		map1.put("Doctrate", new Certificate("Phd"));
		map1.put("Masters", new Certificate("MS"));
		map1.put("Bachlors", new Certificate("BTech"));
		Integer empID1 = me.addEmployee("Siddhartha", "Gadiraju", 11000, map1);

		HashMap map2 = new HashMap();
		map2.put("Masters", new Certificate("MTech"));
		map2.put("Bachlors", new Certificate("BTech"));
		Integer empID2 = me.addEmployee("Meena", "Varma", 5000, map2);

		me.listEmployee();
		 me.updateEmployee(empID3, 6700);
		 me.deleteEmployee(empID1);
		me.listEmployee();
	}

	public void deleteEmployee(Integer empID) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee emp = (Employee) session.get(Employee.class, empID);
			session.delete(emp);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	 public void updateEmployee(Integer EmployeeID, int salary ){
	      Session session = factory.openSession();
	      Transaction tx = null;
	      try{
	         tx = session.beginTransaction();
	         Employee employee = 
	                    (Employee)session.get(Employee.class, EmployeeID); 
	         employee.setSalary( salary );
	         session.update(employee);
	         tx.commit();
	      }catch (HibernateException e) {
	         if (tx!=null) tx.rollback();
	         e.printStackTrace(); 
	      }finally {
	         session.close(); 
	      }
	   }

	public void listEmployee() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List employees = session.createQuery("FROM Employee").list();
			for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
				Employee emp = (Employee) iterator.next();
				System.out.println("Name: " + emp.getFirstName() + " "
						+ emp.getLastName());
				System.out.println("Salary: " + emp.getSalary());
				Map cert = emp.getCertificates();
				System.out.println(((Certificate) cert.get("Masters"))
						.getName());
				System.out.println(((Certificate) cert.get("Bachlors"))
						.getName());
//				System.out.println(((Certificate) cert.get("Doctrate"))
//						.getName());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	public Integer addEmployee(String fname, String lname, int salary,
			HashMap cert) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer empID = null;
		try {
			tx = session.beginTransaction();
			Employee emp = new Employee(fname, lname, salary);
			emp.setCertificates(cert);
			empID = (Integer) session.save(emp);
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return empID;
	}

}
