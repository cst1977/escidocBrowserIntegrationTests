package de.escidoc.browser.test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class EscidocBrowserSeleniumIT {

    private WebDriver driver;
    private String baseURL;
    private Actions builder;
    private Map<String, String> listElements;

    public void prepareToLogin()  {
        WebElement okButton = getElementsByClass("v-button-caption").get(0);
        okButton.click();
        try {
            Thread.currentThread().sleep(5000);
            WebElement loginButton = getElementsByClass("v-button-caption").get(1);
            loginButton.click();
        }
        catch(InterruptedException ex) {
            System.err.println("Thread interrupted");
        }
        driver.findElement(By.name("j_username")).clear();
        driver.findElement(By.name("j_username")).sendKeys("sysadmin");
        driver.findElement(By.name("j_password")).clear();
        driver.findElement(By.name("j_password")).sendKeys("escidoc");
        driver.findElement(By.name("Abschicken")).click();
    }

    public void prepareToLogout() {
        WebElement logoutButton = getElementsByClass("v-button-caption").get(0);
        logoutButton.click();
    }

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseURL = "http://localhost:8080/browser";
        driver.manage().timeouts().implicitlyWait(300, TimeUnit.SECONDS);
    }
    
    @Test
    public void testAll() throws Exception {
        driver.get(baseURL + "/browser/?escidocurl=http://localhost:8080");
        prepareToLogin();
        List<WebElement> listContexts = getElementsByClass("v-tree-node v-tree-node-root");

        for (int index = 0; index < listContexts.size(); index++) {
            listContexts = getElementsByClass("v-tree-node v-tree-node-root");
            WebElement element = listContexts.get(index);
            try {
                element.click();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }

        List<WebElement> listContainers = getElementsByClass("v-tree-node-children v-tree-node-children-root");
        containers:
        for (int i = 0; i < listContainers.size(); i++) {
            List<WebElement> subElements = driver.findElements(By.xpath(getElementXPath(driver, listContainers.get(i)) + "/div"));
            subElements:
            for (int j = 0; j < subElements.size(); j++) {
                listContainers = getElementsByClass("v-tree-node-children v-tree-node-children-root");
                subElements = driver.findElements(By.xpath(getElementXPath(driver, listContainers.get(i)) + "/div"));
                WebElement subElement = driver.findElement(By.xpath(getElementXPath(driver, subElements.get(j))));
                try {
                    subElement.click();
                    subElements = driver.findElements(By.xpath(getElementXPath(driver, listContainers.get(i)) + "/div"));
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }
            }
            listContainers = getElementsByClass("v-tree-node-children v-tree-node-children-root");
            try {
                //element.click();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
        List<WebElement> listItems = getElementsByClass("v-tree-node v-tree-node-root");

        for (int index = 0; index < listItems.size(); index++) {
            listItems = getElementsByClass("v-tree-node v-tree-node-root");
            WebElement element = listItems.get(index);
            try {
                element.click();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
        }
    }

    public List<WebElement> getElementsByClass(String className) {
        String javaScript = "function getElementsByClass(){"
                + "var container = document.getElementsByTagName (\"body\")[0];"
                + "if (container.getElementsByClassName) {"
                + "var vTreeNodes = container.getElementsByClassName (\""
                + className
                + "\");"
                + "return vTreeNodes"
                //+ "for (var i = 0; i < vTreeNodes.length; i++) {"
                //+ "var vTreeNode = vTreeNodes[i]"
                //+ "return vTreeNode;"
                //+ "}"
                + "}"
                + "}"
                + "return getElementsByClass();";
        return (List<WebElement>) ((JavascriptExecutor) driver).executeScript(javaScript);
    }

    public String getElementXPath(WebDriver driver, WebElement element) {

        String javaScript = "function getElementXPath(elt){"
                + "var path = \"\";"
                + "for (; elt && elt.nodeType == 1; elt = elt.parentNode){"
                + "idx = getElementIdx(elt);"
                + "xname = elt.tagName;"
                + "if (idx >= 1){"
                + "xname += \"[\" + idx + \"]\";"
                + "}"
                + "path = \"/\" + xname + path;"
                + "}"
                + "return path;"
                + "}"
                + "function getElementIdx(elt){"
                + "var count = 1;"
                + "for (var sib = elt.previousSibling; sib ; sib = sib.previousSibling){"
                + "if(sib.nodeType == 1 && sib.tagName == elt.tagName){"
                + "count++;"
                + "}"
                + "}"
                + "return count;"
                + "}"
                + "return getElementXPath(arguments[0]).toLowerCase();";

        return (String) ((JavascriptExecutor) driver).executeScript(javaScript, element);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}
