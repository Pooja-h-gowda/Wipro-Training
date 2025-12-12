import java.util.Scanner;
public class Fac {
    static int findFac(int num) {
        int fact = 1;
        for (int i = 1; i <= num; i++) {
            fact = fact * i;
        }
        return fact; 
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number");
        int n = sc.nextInt();
        int res = findFac(n); 
        System.out.println("Factorial of " + n + " is: " + res);
        sc.close();
    }
}
    
