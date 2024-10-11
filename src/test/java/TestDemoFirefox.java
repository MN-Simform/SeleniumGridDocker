import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

public class TestDemoFirefox {

    @Test
    public void demo() throws MalformedURLException, URISyntaxException {

        String productName = "Adidas Original";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "firefox");
//        capabilities.setPlatform(Platform.WIN10);

        WebDriver driver = new RemoteWebDriver(new URI("http://172.16.3.207:4444").toURL(),capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("https://rahulshettyacademy.com/client/");

        driver.findElement(By.id("userEmail")).sendKeys("nimit@gmail.com");
        driver.findElement(By.id("userPassword")).sendKeys("Nimit@123");
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

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ng-animating")));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-container")));

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

        String confirmMessage = driver.findElement(By.cssSelector(".hero-primary")).getText();
        Assert.assertTrue(confirmMessage.equalsIgnoreCase("Thankyou for the order."));
        driver.close();
    }
}
