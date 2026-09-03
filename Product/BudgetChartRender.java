/**
 * This class is responsible for rendering a pie chart visualization of the budget allocation
 * for a specific department using JFreeChart. It retrieves budget data from the database 
 * and dynamically updates the chart when called.
 * 
 */
//package iaproductcode;

import javax.swing.*;
import java.awt.*;

// Chart Imports
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class BudgetChartRender extends JPanel
{
  private JavaDBAccess dbAccess;
  private int departmentID;

  /**
   * Constructor to initialize the BudgetChartRender panel.
   * 
   * @param departmentID The ID of the department for which the budget chart is rendered.
   */
  public BudgetChartRender(int departmentID)
  {
    this.dbAccess = new JavaDBAccess();
    this.departmentID = departmentID;
    this.setLayout(new BorderLayout());
    this.updateChart(); // Renders the chart everytime the method is called.
  }

  /**
   * Updates and redraws the pie chart based on the current budget data from the database
   * for the specified department.
   */
  public void updateChart()
  {
    DefaultPieDataset dataset = new DefaultPieDataset();

    // Retrieve budget data for the selected department
    Object[][] budgetData = dbAccess.getBudgetData();

    for (int r = 0; r<budgetData.length; r++)
    {
      int deptID = Integer.parseInt(budgetData[r][1].toString()); // Department ID column
      if (deptID == departmentID)
      {
        // Retrieves Budget Category
        String category = budgetData[r][2].toString();
        // Retrieves Allocated Amount
        double allocatedAmount = Double.parseDouble(budgetData[r][3].toString());
        // Sets value of piechart by category using allocated amount vs total allocated
        dataset.setValue(category, allocatedAmount); 
      }
    }
    // Generate Pie Chart
    JFreeChart chart = ChartFactory.createPieChart(
        "Budget Allocation Pie Chart", // Chart Title
        dataset, // Pie chart data source
        true, // Shows legend
        true, // Info shown when hovered (tooltips)
        false // Disables chart clicking & modification (URLS)
    );

    ChartPanel chartPanel = new ChartPanel(chart);
    removeAll();  // Clear previous chart to ensure update everytime
    this.add(chartPanel, BorderLayout.CENTER);
    this.revalidate();
    this.repaint();
  }
}
