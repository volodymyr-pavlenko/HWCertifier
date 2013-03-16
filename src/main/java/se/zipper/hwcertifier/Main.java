package se.zipper.hwcertifier;

import org.ini4j.MultiMap;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.zipper.hwcertifier.controller.ActionController;
import se.zipper.hwcertifier.domain.TaskRequest;
import se.zipper.hwcertifier.exception.BadParametersException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: dreamer
 * Date: 12.03.13
 * Time: 9:46
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        ActionController controller;
        log.info("Spring context initialized");
        try {
            TaskRequest tr = (TaskRequest)applicationContext.getBean("taskRequest");
            tr.processConfiguration(args);
            controller = (ActionController) applicationContext.getBean(tr.getTask() + "Controller");
        } catch (NoSuchBeanDefinitionException e) {
            controller = (ActionController) applicationContext.getBean("helpController");
        }
        try {
            controller.process();
        } catch (BadParametersException e) {
            System.out.println(e.getMessage());
            ((ActionController)applicationContext.getBean("helpController")).process();
        }
    }
}
