package org.example;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class DriverManager {

    public WebDriver driver;

    public WebDriver getDriver(String browser) throws URISyntaxException, MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if(browser.equalsIgnoreCase("chrome")){
            capabilities.setBrowserName("chrome");
        } else if(browser.equalsIgnoreCase("firefox")){
            capabilities.setBrowserName("firefox");
        } else {
            capabilities.setBrowserName("MicrosoftEdge");
        }
        return driver = new RemoteWebDriver(new URI("http://172.16.3.207:4444").toURL(),capabilities);

    }
}