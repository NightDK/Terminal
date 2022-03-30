import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bank theBank = new Bank("БАНк Дураков");
        User aUser = theBank.addUser("Иван", "Иванов", "1234");

        Account newAccount = new Account("МИР", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {
            curUser = ATM.mainMenuPrompt(theBank, scanner);
            ATM.printUserMenu(curUser, scanner);
        }
    }

    private static void printUserMenu(User theUser, Scanner scanner) {
        theUser.printAccountsSummary();

        int choice;
        do {
            System.out.printf("Добро пожаловать %s, Что хотите сделать?\n", theUser.getFirstName());
            System.out.println("  1) Покажите историю транзакций");
            System.out.println("  2) Снять деньги со счёта");
            System.out.println("  3) Положить деньги на счёт");
            System.out.println("  4) Перевести");
            System.out.println("  5) Выход");
            System.out.println();
            System.out.print("Ваш выбор: ");
            choice = scanner.nextInt();

            if (choice < 1 || choice > 5) {
                System.out.println("Неверный ввод. Выберите 1 - 5");
            }
        } while (choice < 1 || choice > 5);

        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, scanner);
                break;
            case 2:
                ATM.withdrawFunds(theUser, scanner);
                break;
            case 3:
                ATM.depositFunds(theUser, scanner);
                break;
            case 4:
                ATM.transferFunds(theUser, scanner);
                break;
            case 5:
                scanner.nextLine();
                break;
        }
        if (choice != 5) {
            ATM.printUserMenu(theUser, scanner);
        }
    }

    private static void depositFunds(User theUser, Scanner scanner) {
        int toAcct;
        double amount;
        String memo;
        do {
            System.out.printf("Выберите номер (1-%d) вашего счёта: ",
                    theUser.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Неверный ввод, повторите.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        do {
            System.out.print("Выберите сумму для внесения: ");
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Вы не можете внести меньше 0.");
            }
        } while (amount < 0);
        scanner.nextLine();

        System.out.print("Напишите коментарий: ");
        memo = scanner.nextLine();
        theUser.addAcctTransaction(toAcct, amount, memo);
    }

    private static void withdrawFunds(User theUser, Scanner scanner) {
        int formAcct;
        double amount, acctBal;
        String memo;
        do {
            System.out.printf("Выберите номер счёта (1-%d) для снятия: ",
                    theUser.numAccounts());
            formAcct = scanner.nextInt() - 1;
            if (formAcct < 0 || formAcct >= theUser.numAccounts()) {
                System.out.println("Неверный ввод, повторите.");
            }
        } while (formAcct < 0 || formAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(formAcct);

        do {
            System.out.printf("Выберите сумму для снятия (максимум %.02f руб.): ", acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Вы не можете снять меньше 0.");
            } else if (amount > acctBal) {
                System.out.printf("Недостаточно средств для снятия.\n Ваш баланс %.02f руб.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);
        scanner.nextLine();

        System.out.print("Напишите коментарий: ");
        memo = scanner.nextLine();
        theUser.addAcctTransaction(formAcct, -1* amount, memo);
    }

    private static void transferFunds(User theUser, Scanner scanner) {
        int formAcct, toAcct;
        double amount, acctBal;
        do {
            System.out.printf("Выберите номер счёта (1-%d) ИЗ которого будет сделан перевод: ",
                    theUser.numAccounts());
            formAcct = scanner.nextInt() - 1;
            if (formAcct < 0 || formAcct >= theUser.numAccounts()) {
                System.out.println("Неверный ввод, повторите.");
            }
        } while (formAcct < 0 || formAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(formAcct);

        do {
            System.out.printf("Выберите номер счёта (1-%d) НА который будет сделан перевод: ",
                    theUser.numAccounts());
            toAcct = scanner.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("Неверный ввод, повторите.");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        do {
            System.out.printf("Выберите сумму для перевода (максимум %.02f руб.): ", acctBal);
            amount = scanner.nextDouble();
            if (amount < 0) {
                System.out.println("Вы не можете перевести меньше 0.");
            } else if (amount > acctBal) {
                System.out.printf("Недостаточно средств для перевода.\n Ваш баланс %.02f руб.\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        theUser.addAcctTransaction(formAcct, -1* amount, String.format("Перевод НА счёт %s",
                theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, 1* amount, String.format("перевод ИЗ счёта %s",
                theUser.getAcctUUID(formAcct)));
    }

    private static void showTransHistory(User theUser, Scanner scanner) {
        int theAcct;
        do {
            System.out.printf("Выберите номер счёта (1-%d) транзакции которых показать: ", theUser.numAccounts());
            theAcct = scanner.nextInt() -1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("Неверный ввод, повторите.");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());
        theUser.printAcctTransactionHistory(theAcct);
    }

    public static User mainMenuPrompt(Bank theBank, Scanner scanner) {
        String userID;
        String pin;
        User authUser;

        do {
            System.out.printf("\n\nДобро пожаловать в %s \n\n", theBank.getName());
            System.out.print("Введите Ваш ID: ");
            userID = scanner.nextLine();
            System.out.print("Введите PIN-код: ");
            pin = scanner.nextLine();

            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Неверный ID или PIN-код. \n Повторите ввод.");
            }
        } while (authUser == null);
        return authUser;
    }
}
