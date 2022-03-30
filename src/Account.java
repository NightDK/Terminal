import java.util.ArrayList;

public class Account {

    private String name;
    private String uuid;
    private User holder;
    private ArrayList<Transaction> transactions;

    public Account (String name, User holder, Bank theBank) {

        this.name = name;
        this.holder = holder;
        this.uuid = theBank.getNewAccountUUID();
        this.transactions = new ArrayList<Transaction>();
    }

    public String getUUID() {
        return this.uuid;
    }

    public String getSummaryLine() {
        double balance = this.getBalance();
        if (balance >= 0) {
            return  String.format("%s : %.02f руб.: %s", this.uuid, balance, this.name);
        } else {
            return  String.format("%s : -%.02f руб. : %s", this.uuid, balance, this.name);
        }
    }

    public double getBalance() {
        double balance = 0;
        for (Transaction t : this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }

    public void printTransHistory() {
        System.out.printf("\nИстория операций с счётом %s\n", this.uuid);
        for (int i = this.transactions.size() -1; i >= 0; i--) {
            System.out.println(this.transactions.get(i).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction(double amount, String memo) {
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans);
    }
}
