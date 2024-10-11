import org.example.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

public class TestE2E {

    WebDriver driver;

    @Parameters("browser")
    @BeforeTest
    public void setup(String browser) throws MalformedURLException, URISyntaxException {
        driver = new DriverManager().getDriver(browser);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://rahulshettyacademy.com/client/");
    }

    @Test
    public void e2e() {

        String productName = "Adidas Original";
        String userName = "nimit@gmail.com";
        String userPassword = "Nimit@123";

        driver.findElement(By.id("userEmail")).sendKeys(userName);
        driver.findElement(By.id("userPassword")).sendKeys(userPassword);
        driver.findElement(By.id("login")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mb-3")));

        List<WebElement> productList =  driver.findElements(By.cssSelector(".mb-3"));
        WebElement desiredProduct = productList.stream()
                .filter(product -> product.findElement(By.cssSelector("b")).getText().equalsIgnoreCase(productName))
                .findFirst()
                .orElse(null);

        assert desiredProduct != null;
        System.out.println(desiredProduct.getText());
        desiredProduct.findElement(By.cssSelector(".card-body button:last-of-type")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating")));

        driver.findElement(By.xpath("//button[@routerlink='/dashboard/cart']")).click();

        List<WebElement> cartProducts = driver.findElements(By.cssSelector(".cartSection h3"));
        boolean match = cartProducts.stream().anyMatch(cartProduct -> cartProduct.getText().equalsIgnoreCase(productName));
        Assert.assertTrue(match);
        driver.findElement(By.xpath("//button[normalize-space()='Checkout']")).click();

        Actions a = new Actions(driver);
        a.sendKeys(driver.findElement(By.cssSelector("[placeholder='Select Country")), "india").build().perform();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-results")));

        driver.findElement(By.xpath("(//button[contains(@class,'ta-item')])[2]")).click();
        driver.findElement(By.cssSelector(".action__submit")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".hero-primary")));

        String confirmMessage = driver.findElement(By.cssSelector(".hero-primary")).getText();
        Assert.assertTrue(confirmMessage.equalsIgnoreCase("Thankyou for the order."));
    }

    @AfterTest
    public void tearDown(){
        driver.quit();
    }
}
