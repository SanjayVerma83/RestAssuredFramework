package listeners;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportListener implements ITestListener {

    private ExtentReports extentReports;
    private ExtentTest extentTest;

    @Override
    public void onStart(org.testng.ITestContext context) {

        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter("Reports/ExtentReport.html");

        extentReports = new ExtentReports();

        extentReports.attachReporter(sparkReporter);
    }

    @Override
    public void onTestStart(ITestResult result) {

        extentTest =
                extentReports.createTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        extentTest.pass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {

        extentTest.fail(result.getThrowable());
    }

    @Override
    public void onFinish(org.testng.ITestContext context) {

        extentReports.flush();
    }
}