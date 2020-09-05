package com.Project.Client;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.Project.BussinessLogic.CustomerService;
import com.Project.BussinessLogic.UserService;
import com.Project.Dao.AdminDao;
import com.Project.bean.Role;
import com.Project.bean.Transaction;
import com.Project.bean.User;

public class Client {

	private static Scanner s;

	public static void main(String[] args) throws SQLException, ParseException {
		boolean quit = false;
		double balance;
		int i = 3;
		int temp = 3;
		String userId = "";
		String password = "";
		boolean p = false;
		String fromDate = "";
		String toDate = "";

		UserService uService = new UserService();
		User user = new User();
		System.out.println("Welcome to Online Banking System!!!");
		System.out.println("Please enter your credentials:");

		do {
			s = new Scanner(System.in);
			System.out.println("Please enter your customer id.");
			userId = s.nextLine();
			System.out.print("Please enter your password : ");
			password = s.nextLine();
			temp--;
			user.setUserId(userId);
			user.setPassword(password);
			p = false;
			p = uService.checkValidUser(user);
			if (p == false)
				System.out.println("You have " + (temp)
						+ " attempsts left,Please enter the correct choice:");
			else
				break;
		} while (temp > 0);

		if (temp == 0) {
			System.out.println("You have exceeded your attempts");
			System.exit(0);
		}
		Role role = new Role();
		if (p == true) {
			role = uService.RetrieveRole(userId);
			System.out.println("Welcome " + role.getRoleName());

		} else {
			System.out.println("Please give correct credentials!!!");
			System.out.println("Incorrect customerid/password");
		}
		String roletype = role.getRoleName();
		if (roletype.equals("Customer")) {
			int choice = 0;
			temp = 3;
			do {
				do {

					try {

						System.out
								.println("Press 1 for Viewing Balance, Press 2 for Withdrawal, "

										+ "Press 3 for Deposit, Press 4 for viewing Transaction HIstory,Press 5 for exit");
						temp--;
						choice = s.nextInt();

					} catch (InputMismatchException e) {
						i--;
						System.out
								.println("You have "
										+ (i)
										+ " attempsts left,Please enter the correct choice:");
						// throw e;
					}
					if (i > temp) {
						break;
					}
				} while (i > 0);

				if (i == 0) {
					System.out.println("You have exceeded your attempts");
					System.exit(0);
				}

				CustomerService cusService = new CustomerService();
				switch (choice) {
				case 1:
					System.out.println("Your Balance is "
							+ cusService.getCustomerBalance(userId));
					break;
				case 2:
					System.out.println("Enter the amount to be withdrawn :");
					double amount1 = s.nextDouble();
					double cusBal = cusService.getCustomerBalance(userId);
					if (cusBal <= 1000)
						System.out
								.println("The minimum balance of Rs.1000 is required.You cannot withdraw given amount.");
					else if (amount1 <= cusBal - 1000)
						balance = cusService
								.CustomerWithdrawal(userId, amount1);
					else
						System.out.println("Insufficient Balance!!!");

					break;
				case 3:
					System.out.println("Enter the amount to be deposited:");
					double amount = s.nextDouble();
					balance = cusService.CustomerDeposit(userId, amount);
					break;
				case 4:
					Scanner sc = new Scanner(System.in);
					System.out
							.println("Enter the dates to view the Transaction Statement");
					System.out.println("Enter the FromDate as yyyy-mm-dd:");
					fromDate = sc.nextLine();

					System.out.println("Enter the ToDate as yyyy-mm-dd:");
					toDate = sc.nextLine();
					if (java.sql.Date.valueOf(fromDate).compareTo(
							java.sql.Date.valueOf(toDate)) > 0) {
						System.out.println("Please enter valid dates.");
						break;
					}

					List<Transaction> transaction = new ArrayList<Transaction>();
					transaction = cusService
							.ViewTranscationHistoryBetweenDates(userId,
									fromDate, toDate);
					for (Transaction t : transaction) {
						System.out.println(t);
					}
					break;

				case 5:
					quit = true;

				default:
					System.out.println("Please enter correct option");
					}
			
			} while (!quit);

		} else if (roletype.equals("Admin")) {
			
			do{
				Scanner sc1=new Scanner(System.in);
				quit = false;
				AdminDao AdDao=new AdminDao();
				System.out.println("Press 1 to create Account, Press 2 for Deletion of Account,Press 3 for quit");
				int choice = s.nextInt();
				switch(choice){
				case 1:
					System.out.println("Enter name:");
					String name=sc1.nextLine();
					System.out.println("Enter address:");
					String address=sc1.nextLine();
					System.out.println("Enter mobileno:");
					String mobileno =sc1.nextLine();
					System.out.println("Enter occupation:");
					String occupation=sc1.nextLine();
					System.out.println("Enter dob:");
					String dob=sc1.nextLine();
					System.out.println("Enter password");
					String password1=sc1.nextLine();
					User user1=new User("1006",name,address,mobileno,occupation,dob,password1);
					p=AdDao.AddCustomer(user1);		
					System.out.println("New customer created.");
					break;
				case 2:
				case 3:
				default:
					System.out.println("Please enter correct option");
				}
			}while(!quit);
				
	

		} else if (roletype.equals("Manager")) {
			System.out.println("Press 1 view customer details , press 2 for account details,press 3 for quit");
		}
		System.out.println("Do you want to change your password:");
		System.out.println("Press y to change");
		if (s.nextLine().equalsIgnoreCase("y")) {
			String pass = s.nextLine();
			boolean a = true;
			if (a == uService.UpdatePassword(user, pass)) {
				System.out.println("Your Password is Changed.");

			}
			s.close();
		}
	}
}
