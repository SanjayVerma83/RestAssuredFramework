
package listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;

public class ExtentReportListener implements ITestListener {

    private static final ThreadLocal<ExtentTest> test =
            new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        System.out.println("ExtentReports listener started.");
        System.out.println("Report path: "
                + System.getProperty("user.dir")
                + "\\Reports\\ExtentReport.html");
    }

    @Override
    public void onTestStart(ITestResult result) {

        ExtentTest extentTest =
                ExtentManager.getInstance()
                        .createTest(result.getMethod().getMethodName());

        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip("Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {

        System.out.println("Flushing ExtentReports...");

        ExtentManager.getInstance().flush();

        System.out.println("ExtentReports flush completed.");
    }
}
