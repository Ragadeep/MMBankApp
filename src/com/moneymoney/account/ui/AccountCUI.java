package com.moneymoney.account.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class AccountCUI {
	private static Scanner scanner = new Scanner(System.in);
	private static SavingsAccountService savingsAccountService = new SavingsAccountServiceImpl();
	private static SavingsAccount savingsAccounts;

	public static void start() {

		do {
			System.out.println("****** Welcome to Money Money Bank********");
			System.out.println("1. Open New Savings Account");
			System.out.println("2. Update Account");
			System.out.println("3. Close Account");
			System.out.println("4. Search Account");
			System.out.println("5. Withdraw");
			System.out.println("6. Deposit");
			System.out.println("7. FundTransfer");
			System.out.println("8. Check Current Balance");
			System.out.println("9. Get All Savings Account Details");
			System.out.println("10. Sort Accounts");
			System.out.println("11. Exit");
			System.out.println();
			System.out.println("Make your choice: ");

			int choice = scanner.nextInt();

			performOperation(choice);

		} while (true);
	}

	private static void performOperation(int choice) {
		switch (choice) {
		case 1:
			acceptInput("SA");
			break;
		case 2:
			updateAccount();
			break;
		case 9:
			showAllAccounts();
			break;
		case 3:
			delete();
			break;
		case 5:
			withdraw();
			break;
		case 6:
			deposit();
			break;
		case 7:
			fundTransfer();
			break;
		case 8:
			checkBalance();
			break;
		case 11:
			try {
				DBUtil.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
		default:
			System.err.println("Invalid Choice!");
			break;
		}

	}

	private static void updateAccount() {
		System.out.println("Enter Account Number to Update details: ");
		int accountNumber = scanner.nextInt();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (AccountNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("To Update Your Name Enter " + "1");
		System.out.println("To Update Your Salary Type Enter " + "2");
		System.out.println("To Update Your Name and Salary Type Enter " + "3");
		int select = scanner.nextInt();
		selectToUpdate(select, savingsAccount);
	}

	private static void selectToUpdate(int select, SavingsAccount savingsAccount) {
		switch (select) {

		case 1:
			System.out.println("Enter new Name : ");
			String changeName = scanner.nextLine();
			changeName = scanner.nextLine();
			savingsAccount.getBankAccount().setAccountHolderName(changeName);
			boolean name;
			try {
				name = savingsAccountService.updateAccount(savingsAccount);
				if (name == true) {
					System.out.println("Name Changed for "
							+ savingsAccount.getBankAccount()
									.getAccountNumber() + " to " + changeName);
				}
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;

		case 2:

			System.out
					.println("If your account type is salaried enter (n) to change your account type as unsalaried");
			System.out
					.println("If your account type is savings enter (y)to change your account type as salaried");
			boolean changeSalaryType = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			savingsAccount.setSalary(changeSalaryType);

			boolean salary;
			try {
				salary = savingsAccountService.updateAccount(savingsAccount);
				if (salary == true) {
					System.out.println("Salary type of "
							+ savingsAccount.getBankAccount()
									.getAccountNumber() + "Changed to "
							+ changeSalaryType);
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;

		case 3:
			System.out.println("Enter new name : ");
			String changename = scanner.nextLine();
			changename = scanner.nextLine();
			savingsAccount.getBankAccount().setAccountHolderName(changename);
			System.out
					.println("If your account type is salaried enter (n) to change your account type as unsalaried");
			System.out
					.println("If your account type is savings enter (y)to change your account type as salaried");
			boolean changeTypeOfSalary = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			savingsAccount.setSalary(changeTypeOfSalary);

			boolean salaryResult;
			try {
				salaryResult = savingsAccountService
						.updateAccount(savingsAccount);
				if (salaryResult == true) {
					System.out.println("Name and Salary type for "
							+ savingsAccount.getBankAccount()
									.getAccountNumber() + " Changed to "
							+ changename + " and " + changeTypeOfSalary);
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			break;

		default:
			System.err.println("Invalid Choice!");
			break;
		}
	}

	private static void checkBalance() {
		System.out.println("Enter Account Number to Check Balance: ");
		int accountNumber = scanner.nextInt();
		try {
			double savingsAccount = savingsAccountService
					.checkBalance(accountNumber);
			// savingsAccountService.checkBalance(accountNumber);
			System.out.println(savingsAccount);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (AccountNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void delete() {
		System.out.println("Enter Account Number to Delete: ");
		int deleteAccount = scanner.nextInt();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(deleteAccount);
			savingsAccountService.delete(savingsAccount);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AccountNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void fundTransfer() {
		System.out.println("Enter Account Sender's Number: ");
		int senderAccountNumber = scanner.nextInt();
		System.out.println("Enter Account Receiver's Number: ");
		int receiverAccountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		try {
			SavingsAccount senderSavingsAccount = savingsAccountService
					.getAccountById(senderAccountNumber);
			SavingsAccount receiverSavingsAccount = savingsAccountService
					.getAccountById(receiverAccountNumber);
			savingsAccountService.fundTransfer(senderSavingsAccount,
					receiverSavingsAccount, amount);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deposit() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.deposit(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void withdraw() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.withdraw(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void sortMenu(String sortWay) {
		do {
			System.out.println("+++++Ways of Sorting+++++++");
			System.out.println("1. Account Number");
			System.out.println("2. Account Holder Name");
			System.out.println("3. Account Balance");
			System.out.println("4. Exit from Sorting");

			int choice = scanner.nextInt();

		} while (true);

	}

	private static void showAllAccounts() {
		List<SavingsAccount> savingsAccounts;
		try {
			savingsAccounts = savingsAccountService.getAllSavingsAccount();
			for (SavingsAccount savingsAccount : savingsAccounts) {
				System.out.println(savingsAccount);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

	private static void acceptInput(String type) {
		if (type.equalsIgnoreCase("SA")) {
			System.out.println("Enter your Full Name: ");
			String accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
			System.out
					.println("Enter Initial Balance(type na for Zero Balance): ");
			String accountBalanceStr = scanner.next();
			double accountBalance = 0.0;
			if (!accountBalanceStr.equalsIgnoreCase("na")) {
				accountBalance = Double.parseDouble(accountBalanceStr);
			}
			System.out.println("Salaried?(y/n): ");
			boolean salary = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			createSavingsAccount(accountHolderName, accountBalance, salary);
		}
	}

	private static void createSavingsAccount(String accountHolderName,
			double accountBalance, boolean salary) {
		try {
			savingsAccountService.createNewAccount(accountHolderName,
					accountBalance, salary);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
