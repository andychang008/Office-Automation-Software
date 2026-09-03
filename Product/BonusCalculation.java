//package iaproductcode;

/**
 * BonusCalculation class provides a utility method to calculate the bonus amount
 * for an employee based on their salary and a given bonus rate.
 *
 * This class supports simple percentage-based bonus calculations.
 * Intended for use in the Office Automation System.
 *
 */
public class BonusCalculation
{
  /**
   * Calculates the bonus amount based on salary and bonus rate.
   *
   * @param salary The base salary of the employee.
   * @param bonusRate The bonus rate as a percentage.
   * @return The calculated bonus amount.
   */
  public static double calculateBonus(double salary, double bonusRate)
  {
    return salary * (bonusRate / 100);
  }
  
  /**
   * Main method for testing the bonus calculation logic.
   * Demonstrates usage of the calculateBonus method.
   */
  public static void main(String[] args)
  {
    double salary = 100;
    double bonusPercent = 20;
    System.out.println(calculateBonus(salary, bonusPercent));
  }
}
