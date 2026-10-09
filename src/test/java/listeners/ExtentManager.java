
package listeners;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {

    private static ExtentReports extent;

    public static synchronized ExtentReports getInstance() {

        if (extent == null) {

            File reportDirectory = new File("Reports");

            if (!reportDirectory.exists()) {
                reportDirectory.mkdirs();
            }

            ExtentSparkReporter sparkReporter =
                    new ExtentSparkReporter(
                            new File(reportDirectory, "ExtentReport.html")
                                    .getAbsolutePath());

            sparkReporter.config()
                    .setReportName("REST Assured API Automation Report");

            sparkReporter.config()
                    .setDocumentTitle("API Test Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("Framework", "REST Assured");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", "QA Automation");

        }

        return extent;
    }
}
