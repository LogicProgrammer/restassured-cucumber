package com.test;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = {"com.cocomore.step"},
        plugin = {"pretty", "html:target/report/report.html", "json:target/report/report.json"
                ,"junit:target/report/junit.xml","de.monochromata.cucumber.report.PrettyReports:target/cucumber"},
        features = {"src/test/resources/feature"},
        monochrome = true,
        stepNotifications = true,
        tags = "@api"
)
public class StoryRunner {


}
